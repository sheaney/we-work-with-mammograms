package controllers;

import java.util.List;

import lib.PasswordGenerator;
import lib.json.permissions.JSONPermissions;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import lib.permissions.Permission;
import lib.PatientContainer;
import models.Patient;
import models.SharedPatient;
import models.Staff;
import models.Study;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import views.html.*;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

@Restrict(@Group({ "STAFF" }))
public class Staffs extends Controller {

	final static Form<Patient> patientForm = Form.form(Patient.class);
	final static Form<Study> newStudyForm = Form.form(Study.class);

	public static Result staff() {
		return ok(staff.render(session().get("user")));
	}

	public static Result newStudy(Long patientId) {
		return ok(newStudy.render(session().get("user"), patientId, newStudyForm));
	}

	public static Promise<Result> createNewStudy(final Long patientId) {
		return Promise.promise(new Function0<Result>() {

			@Override
			public Result apply() throws Throwable {
				// first we check that the images uploaded are actual images
				Form<Study> filledForm = newStudyForm.bindFromRequest();
				//TODO check for errors and form validation
				if (filledForm.hasErrors()) {
					return badRequest(newStudy.render(session()
							.get("user"),patientId,filledForm));
				} else {
					MultipartFormData body = request().body()
							.asMultipartFormData();
					List<MultipartFormData.FilePart> parts = body.getFiles();
					//let's assume for meow that they are all image files
					
					/*for (MultipartFormData.FilePart part : parts) {
						System.out.println(part.getContentType() + " "
								+ part.getFilename());
					}*/
				}
				
				// new study to save asynchronously to db
				// Study study = new Study();
				// find patient asynchronously
				// Promise<Patient> patient = Patient.findByIdAsync(patientId);
				// get the patient once the promise completes with a timeout of
				// 10 secs
				// study.setOwner(patient.get(10000L));

				return ok(showPatient.render(patientId, session().get("user")));
			}

		});
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
