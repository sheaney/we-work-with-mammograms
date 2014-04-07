package controllers;

import models.Patient;
import models.Staff;
import models.Admin;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.contact;
import views.html.editPatient;
import views.html.index;
import views.html.login;
import views.html.newPatient;
import views.html.newStudy;
import views.html.patient;
import views.html.sharePatient;
import views.html.showPatient;
import views.html.staff;
import views.html.study;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result login() {
		return ok(login.render(Form.form(Login.class)));
	}

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
	    if (loginForm.hasErrors()) {
	        return badRequest(login.render(loginForm));
	    } else {
	        session().clear();
	        session("email", loginForm.get().email);
	        return redirect(
	            routes.Application.staff()
	        );
	    }
	}

	public static Result staff() {
		return ok(staff.render("Juanito"));
	}

	public static Result patient() {
		return ok(patient.render("Juanito"));
	}

	public static Result newPatient() {
		return ok(newPatient.render("Juanito"));
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

	public static Result contact() {
		return ok(contact.render("Juanito"));
	}

	public static class Login {

		public String email;
		public String password;

		public String validate() {
			if (Staff.authenticate(email, password) == null 
					|| Patient.authenticate(email,password) == null
					|| Admin.authenticate(email,password) == null) {
				return "Invalid user or password";
			}
			return null;
		}
	}
}
