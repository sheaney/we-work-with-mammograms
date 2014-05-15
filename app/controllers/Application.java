package controllers;

import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import models.Admin;
import models.PersonalInfo;
import models.Staff;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import security.Roles;
import views.html.contact;
import views.html.login;
import views.html.settings;

public class Application extends Controller {
	
	public static Result index() {
		String type = session().get("type");
		if(type != null){
			if(type.equals("ADMIN")){
				return redirect(routes.Admins.admin());
			}else if (type.equals("STAFF")){
				return redirect(routes.Staffs.staff());
			}else if (type.equals("PATIENT")){
				return redirect(routes.Patients.patient());
			}
		}
		return redirect(routes.Application.login());
	}
	
	public static Result login() {
		if(session().get("id") != null){
			return redirect(routes.Application.index());
		}else{
			return ok(login.render(Form.form(Login.class)));
		}
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
	        return redirect(routes.Application.index());
	    }
	}
	
	
	public static Result contact() {
		return ok(contact.render(session().get("user")));
	}

    public static Result settings(){
        return ok(settings.render(session().get("user")));
    }

    public static Result jsRoutes() {
      response().setContentType("text/javascript");
      return ok(
          Routes.javascriptRouter("jsRoutes",
            // Application actions
            controllers.routes.javascript.Application.index(),
            controllers.routes.javascript.Application.contact(),
            controllers.routes.javascript.Application.settings(),
            
            // Admin actions
            controllers.routes.javascript.Admins.admin(),
            controllers.routes.javascript.Admins.newStaff(),
            controllers.routes.javascript.Admins.createStaff(),

            // Staff actions
            controllers.routes.javascript.Staffs.staff(),
            controllers.routes.javascript.Staffs.newPatient(),
            controllers.routes.javascript.Staffs.createPatient(),
            controllers.routes.javascript.Staffs.showPatient(),
            controllers.routes.javascript.Staffs.sharePatient(),
            controllers.routes.javascript.Staffs.showStaff(),
            controllers.routes.javascript.Staffs.newStudy(),
            controllers.routes.javascript.Staffs.study(),
            controllers.routes.javascript.Staffs.createSharedPatient(),
            controllers.routes.javascript.Staffs.showMammogram(),
            
            // Patient actions
            controllers.routes.javascript.Patients.patient(),
            controllers.routes.javascript.Patients.showMammogram(),

            // API actions
            controllers.routes.javascript.API.staff(),
            controllers.routes.javascript.API.getStaff(),
            controllers.routes.javascript.API.updatePersonalInfo(),
            controllers.routes.javascript.API.updateMedicalInfo()
          )
      );
    }
    
	public static class Login {

		public String email;
		public String password;
		
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

		public String validate() {
			Staff staff = Staff.authenticate(email, password);
			Admin admin = Admin.authenticate(email,password);
			PersonalInfo patientPersonalInfo = PersonalInfo.authenticate(email,password);
			if(staff != null){
				session().clear();
				session("id", staff.getId().toString());
		        session("type", Roles.STAFF.getName());
		        session("timeOfLogin", String.valueOf(System.currentTimeMillis()));
		        session("user", staff.getShortName());
				return null;
			}else if (admin != null){
				session().clear();
				session("id", admin.getId().toString());
		        session("type", Roles.ADMIN.getName());
		        session("timeOfLogin", String.valueOf(System.currentTimeMillis()));
		        session("user",admin.getEmail());
				return null;
			}else if(patientPersonalInfo != null){
				session().clear();
				session("id", patientPersonalInfo.getPatient().getId().toString());
		        session("type", Roles.PATIENT.getName());
		        session("timeOfLogin", String.valueOf(System.currentTimeMillis()));
		        session("user", patientPersonalInfo.getShortName());
				return null;
			}else{
				return "error.invalid.login";
			}
		}
	}
}
