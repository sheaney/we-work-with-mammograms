package controllers;

import lib.PasswordGenerator;
import lib.json.permissions.JSONPermissions;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import lib.permissions.Permission;
import models.Patient;
import models.SharedPatient;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.editPatient;
import views.html.newPatient;
import views.html.newStudy;
import views.html.sharePatient;
import views.html.showPatient;
import views.html.showStaff;
import views.html.staff;
import views.html.study;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.fasterxml.jackson.databind.JsonNode;

@Restrict(@Group({"STAFF"}))
public class Staffs extends Controller {
	
	final static Form<Patient> patientForm = Form.form(Patient.class);
	final static Form<SharedPatient> sharedPatientForm = Form.form(SharedPatient.class);
	
	public static Result staff() {
		return ok(staff.render(session().get("user")));
	}
	
	public static Result newStudy(Long patientId) {
		return ok(newStudy.render("Juanito"));
	}
	
	public static Result study(Long patientId, Long id) {
		return ok(study.render(id, "Juanito"));
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
		JsonNode jsonNode = request().body().asJson();
		// Create patient
		PatientViewInfoPermission patientViewInfo = JSONPermissions.unbindViewInfoPermissions(jsonNode);
		PatientUpdateInfoPermission patientUpdateInfo = JSONPermissions.unbindUpdateInfoPermissions(jsonNode);
		Staff sharer = API.obtainStaff(); // get staff from session
		Staff borrower = Staff.findById(borrowerId);
		Patient patient = Patient.findById(id);
		int accessPrivileges = Permission.concatAccessPrivileges(patientViewInfo, patientUpdateInfo);
		SharedPatient sharedPatient = new SharedPatient(sharer, borrower, patient, accessPrivileges);
		System.out.println("sharer = " + sharedPatient.getSharer().getName());
		System.out.println("borrower = " + sharedPatient.getBorrower().getName());
		System.out.println("shared patient = " + sharedPatient.getSharedInstance().getPersonalInfo().getName());
		System.out.println(sharedPatient.getAccessPrivileges());
		SharedPatient.create(sharedPatient);
		return ok("success");
//		return badRequest("failure");
	}
	
    public static Result newPatient() {
        return ok(newPatient.render("Juanito", patientForm));
    }
    
    public static Result createPatient() {
		Form<Patient> filledForm = patientForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newPatient.render(session().get("user"), filledForm));
		} else {
			PasswordGenerator pg = new PasswordGenerator();
			Patient patient = filledForm.get();
			patient.getPersonalInfo().setPassword(pg.next());
			Patient.create(patient);
			flash("success", "Un nuevo paciente se ha creado");
			return redirect(routes.Staffs.staff());
		}
    }
    
    public static Result showStaff(Long id) {
        return ok(showStaff.render(id, "Juanito"));
    }
	
}
