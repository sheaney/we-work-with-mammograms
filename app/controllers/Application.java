package controllers;

import models.Admin;
import models.PersonalInfo;
import models.Staff;
import models.Patient;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.Routes;
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
	        session("email", loginForm.get().getEmail());
	        
	        session("type", loginForm.get().getType());
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

    public static Result jsRoutes() {
      response().setContentType("text/javascript");
      return ok(
          Routes.javascriptRouter("jsRoutes",
            // Admin actions
            controllers.routes.javascript.Admin.newStaff(),
            controllers.routes.javascript.Admin.createStaff(),

            // Staff actions
            controllers.routes.javascript.Application.index(),
            controllers.routes.javascript.Application.staff(),
            controllers.routes.javascript.Application.patient(),
            controllers.routes.javascript.Application.newPatient(),
            controllers.routes.javascript.Application.createPatient(),
            controllers.routes.javascript.Application.contact(),
            controllers.routes.javascript.Application.showPatient(),
            controllers.routes.javascript.Application.sharePatient(),
            controllers.routes.javascript.Application.showStaff(),
            controllers.routes.javascript.Application.settings(),
            controllers.routes.javascript.Application.newStudy(),
            controllers.routes.javascript.Application.study(),

            // Api
            controllers.routes.javascript.API.staff(),
            controllers.routes.javascript.API.patient()
          )
      );
    }
    
	public static class Login {

		public String email;
		public String password;
		private String type = "";
		public String roles[]=  {"STAFF", "ADMIN", "PATIENT"};
		
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String validate() {
			Staff staff = Staff.authenticate(email, password);
			Admin admin = Admin.authenticate(email,password);
			PersonalInfo patientPersonalInfo = PersonalInfo.authenticate(email,password);
			if (admin != null){
				type = roles[1];
				return null;
			}else if(staff != null){
				type = roles[0];
				return null;
			}else if(patientPersonalInfo != null){
				type = roles[2];
				return null;
			}else{
				return "Invalid user or password";
			}
		}
	}
}
