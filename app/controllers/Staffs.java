package controllers;

import java.io.File;
import java.util.List;

import content.FileWriter;
import content.S3Uploader;
import lib.PasswordGenerator;
import lib.PatientContainer;
import lib.json.permissions.JSONPermissions;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import lib.permissions.Permission;
import models.*;
import play.Play;
import views.html.*;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;




import com.fasterxml.jackson.databind.JsonNode;

import content.Uploader;

@Restrict(@Group({ "STAFF" }))
public class Staffs extends Controller {

	final static Form<Patient> patientForm = Form.form(Patient.class);
	final static Form<Study> newStudyForm = Form.form(Study.class);

	public static Result staff() {
		return ok(staff.render(session().get("user")));
	}

	public static Result newStudy(Long patientId) {
		Patient patient = Patient.findById(patientId);
		return ok(newStudy.render(patient, newStudyForm));
	}

	public static Promise<Result> createNewStudy(final Long patientId) {
		Promise<Result> promise = Promise.promise(new Function0<Result>() {
			public final Patient patient = Patient.findById(patientId);

            // What to return in case of failure???
			@Override
			public Result apply() throws Throwable {
				// first we check that the images uploaded are actual images
				Form<Study> filledForm = newStudyForm.bindFromRequest();
				//TODO check for errors and form validation
				if (filledForm.hasErrors()) {
					return badRequest(newStudy.render(patient, filledForm));
				} else {
                    Study study = filledForm.get();
					MultipartFormData body = request().body()
							.asMultipartFormData();
					List<MultipartFormData.FilePart> parts = body.getFiles();
					// Need to verify that all parts are image files first

                    // Obtain uploader
                    Boolean uploadConfig = Play.application().configuration().getBoolean("s3Upload");
                    boolean s3Upload = uploadConfig != null && uploadConfig;
                    Uploader uploader = s3Upload ? new S3Uploader() : new FileWriter();
                    String logMsg = s3Upload ? "Uploading mammogram image to s3" : "Writing mammogram image to disk";

                    // Associate study with patient
                    study.setOwner(patient);
                    study.save();

                    String studyId = String.valueOf(study.getId());

					// Create mammograms and persist images
					for (MultipartFormData.FilePart part : parts) {
                        // Associate mammogram with study
                        Mammogram mammogram = new Mammogram();
                        mammogram.setStudy(study);
                        mammogram.save();

                        // Calculate mammogram key and update with value
                        String mammogramId = String.valueOf(mammogram.getId());
                        String key = String.format("images/study/%s/mammograms/%s", studyId, mammogramId);

                        // Upload to AWS s3 or write to disk
						File imageFile = part.getFile();
						System.out.println(logMsg);
						uploader.write(key, imageFile);
					}
				}

                flash("success", "Se ha creado un nuevo estudio para el paciente");
                // TODO borrowed = false for now
                return ok(showPatient.render(patientId, session().get("user"), false));
			}

		});
		
		return promise;
	}

	public static Result study(Long patientId, Long id) {
		return ok(study.render(id, session().get("user")));
	}

	public static Result showPatient(Long id) {
		
		boolean borrowed;
		
		Patient patient = Patient.findById(id);
		Staff staff = Staff.findById(Long.parseLong(session().get("id")));
		
		if(staff.findBorrowedPatient(patient) != null) {
			borrowed = true;
        } else {
			borrowed = false;
	    }
				
		return ok(showPatient.render(id, session().get("user"), borrowed));
	}

	public static Result editPatient(Long id) {
		return ok(editPatient.render(id, session().get("user")));
	}

	public static Result sharePatient(Long id) {
		return ok(sharePatient.render(id, session().get("user")));
	}
	
	public static Result createSharedPatient(Long id, Long borrowerId) {
		Patient patient = Patient.findById(id);
		Staff sharer = API.obtainStaff(); // get staff from session
		if (!sharer.canSharePatient(patient)) {
			return badRequest("Este paciente no te pertenece y no se puede compartir");
		}
		
		JsonNode jsonNode = request().body().asJson();
		// Create shared patient
		Staff borrower = Staff.findById(borrowerId);
		PatientViewInfoPermission patientViewInfo = JSONPermissions.unbindViewInfoPermissions(jsonNode);
		PatientUpdateInfoPermission patientUpdateInfo = JSONPermissions.unbindUpdateInfoPermissions(jsonNode);
		int accessPrivileges = Permission.concatAccessPrivileges(patientViewInfo, patientUpdateInfo);
		SharedPatient sharedPatient = new SharedPatient(sharer, borrower, patient, accessPrivileges);
		
		System.out.println("sharer = " + sharedPatient.getSharer().getName());
		System.out.println("borrower = " + sharedPatient.getBorrower().getName());
		System.out.println("shared patient = " + sharedPatient.getSharedInstance().getPersonalInfo().getName());
		System.out.println(sharedPatient.getAccessPrivileges());
		SharedPatient alreadySharedPatient = PatientContainer.getAlreadySharedPatient(sharedPatient, sharer, borrower);
		if (alreadySharedPatient != null) {
			alreadySharedPatient.setAccessPrivileges(sharedPatient.getAccessPrivileges());
			alreadySharedPatient.update();
			flash("success", "El paciente previamente compartido se ha modificado exitosamente");
			return ok("El paciente previamente compartido se ha modificado exitosamente");
		} else {
			SharedPatient.create(sharedPatient);
			flash("success", "El paciente se ha compartido exitosamente");
			return ok("success");
		}
	}

	public static Result createSharedPatient(Long id) {
		return null;
	}

	public static Result newPatient() {
		return ok(newPatient.render(session().get("user"), patientForm));
	}

	public static Result createPatient() {
		Form<Patient> filledForm = patientForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newPatient.render(session().get("user"),
					filledForm));
		} else {
			PasswordGenerator pg = new PasswordGenerator();
			Patient patient = filledForm.get();
			patient.getPersonalInfo().setPassword(pg.next());
			patient.setOwner(API.obtainStaff());
			Patient.create(patient);
			flash("success", "Un nuevo paciente se ha creado");
			return redirect(routes.Staffs.staff());
		}
  }

  public static Result showStaff(Long id) {
      return ok(showStaff.render(id, session().get("user")));
  }
	
}
