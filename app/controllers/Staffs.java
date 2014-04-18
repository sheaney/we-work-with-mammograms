package controllers;

import models.Patient;
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

public class Staffs extends Controller {
	
	final static Form<Patient> patientForm = Form.form(Patient.class);

	public static Result staff() {
		return ok(staff.render(session("email")));
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
	
    public static Result newPatient() {
        return ok(newPatient.render("Juanito", patientForm));
    }
    
    public static Result createPatient() {
		Form<Patient> filledForm = patientForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newPatient.render("Juanito", filledForm));
		} else {
			Patient.create(filledForm.get());
			return redirect(routes.Application.index());
		}
    }
    
    public static Result showStaff(Long id) {
        return ok(showStaff.render(id, "Juanito"));
    }
	
}
