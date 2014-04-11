package controllers;

import models.Admin;
import models.PersonalInfo;
import models.Staff;
import models.Patient;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	final static Form<Patient> patientForm = Form.form(Patient.class);

	public static Result index() {
		String type = session("type");
		if(type != null){
			if(type.equals("ADMIN")){
				return TODO;
			}else if (type.equals("STAFF")){
				return redirect(routes.Application.staff());
			}else if (type.equals("PATIENT")){
				return redirect(routes.Application.patient());
			}
		}
		return redirect(routes.Application.login());
	}

	public static Result login() {
		return ok(login.render(Form.form(Login.class)));
	}

	public static Result logout(){
		session().clear();
		return redirect(routes.Application.login());
	}	
	
	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
	    if (loginForm.hasErrors()) {
	    	
	        return badRequest(login.render(loginForm));
	    } else {
	        session().clear();
	        session("email", loginForm.get().email);
	        
	        session("type", loginForm.get().type);
	        //System.out.println("TIPO EN SESION: "+session("type"));
	        return redirect(
	            routes.Application.index()
	        );
	    }
	}

	public static Result staff() {
		return ok(staff.render(session("email")));
	}

	public static Result patient() {
		return ok(patient.render(session("email")));
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

    public static Result settings(){
        return ok(settings.render("Juanito"));
    }
    
	public static class Login {

		public String email;
		public String password;
		public String type = "";
		public String rol[]=  {"STAFF", "ADMIN", "PATIENT"};

		public String validate() {
			Staff staff = Staff.authenticate(email, password);
			Admin admin = Admin.authenticate(email,password);
			PersonalInfo patientPersonalInfo = PersonalInfo.authenticate(email,password);
			if (admin != null){
				type = rol[1];
				return null;
			}else if(staff != null){
				type = rol[0];
				return null;
			}else if(patientPersonalInfo != null){
				type = rol[2];
				return null;
			}else{
				return "Invalid user or password";
			}
		}
	}
}
