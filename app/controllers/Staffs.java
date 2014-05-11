package controllers;

import java.io.File;
import java.util.List;

import lib.PasswordGenerator;
import lib.PatientContainer;
import lib.json.permissions.JSONPermissions;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import lib.permissions.Permission;
import models.Patient;
import models.SharedPatient;
import models.Staff;
import models.Study;
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

			@Override
			public Result apply() throws Throwable {
				// first we check that the images uploaded are actual images
				Form<Study> filledForm = newStudyForm.bindFromRequest();
				//TODO check for errors and form validation
				if (filledForm.hasErrors()) {
					return badRequest(newStudy.render(patient, filledForm));
				} else {
					MultipartFormData body = request().body()
							.asMultipartFormData();
					List<MultipartFormData.FilePart> parts = body.getFiles();
					//let's assume for meow that they are all image files
					
					// Test upload
					for (MultipartFormData.FilePart part : parts) {
						String key = part.getContentType() + " " + part.getFilename();
						File imageFile = part.getFile();
                        System.out.println(imageFile.exists());
						System.out.println("Uploading file to AWS bucket");
//						Uploader.upload(key, imageFile);
					}
				}

        flash("success", "Se ha creado un nuevo estudio para el paciente");
				return ok(showPatient.render(patientId, session().get("user")));
			}

		});
		
		return promise;
	}

	public static Result study(Long patientId, Long id) {
		return ok(study.render(id, session().get("user")));
	}

	public static Result showPatient(Long id) {
		return ok(showPatient.render(id, "Juanito"));
	}

	public static Result editPatient(Long id) {
		return ok(editPatient.render(id, "Juanito"));
	}

	public static Result sharePatient(Long id) {
		return ok(sharePatient.render(id, "Juanito"));
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
		return ok(showStaff.render(id, "Juanito"));
	}

}
