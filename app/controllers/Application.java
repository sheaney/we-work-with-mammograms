package controllers;

import models.Admin;
import models.PersonalInfo;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.Routes;
import views.html.*;

public class Application extends Controller {
	
	public static Result index() {
		String type = session("type");
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
	        
	        return redirect(
	            routes.Application.index()
	        );
	    }
	}
	
	public static Result contact() {
		return ok(contact.render("Juanito"));
	}

    public static Result settings(){
        return ok(settings.render("Juanito"));
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
            
            // Patient actions
            controllers.routes.javascript.Patients.patient(),

            // API actions
            controllers.routes.javascript.API.staff(),
            controllers.routes.javascript.API.getStaff()
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
				return "errors.invalid.login";
			}
		}
	}
}
