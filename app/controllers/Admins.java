package controllers;

import lib.Email.Postman;
import lib.PasswordGenerator;
import models.Admin;
import models.ServiceAuth;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin;
import views.html.newService;
import views.html.newStaff;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import java.util.List;


@Restrict(@Group({"ADMIN"}))
public class Admins extends Controller {

	final static Form<Staff> staffForm = Form.form(Staff.class);
	final static Form<ServiceAuth> serviceForm = Form.form(ServiceAuth.class);

	public static Result admin() {
		List<ServiceAuth> allSeviceTokens = ServiceAuth.find.all();

        return ok(admin.render(session().get("user"),allSeviceTokens));
	}

	public static Result newStaff() {
		return ok(newStaff.render(session().get("user"), staffForm));
	}

	public static Result createStaff() {
		Form<Staff> filledForm = staffForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newStaff.render(session().get("user"), filledForm));
		} else {
			PasswordGenerator pg = new PasswordGenerator();
			Staff staff = filledForm.get();
			staff.setPassword(pg.next());
			Staff.create(staff);
            //Postman.welcomeEmail(staff.getFullName(),staff.getEmail(),staff.getPassword());
			flash("success", "Un nuevo personal se ha creado");
			return redirect(routes.Admins.admin());
		}
	}

    public static Result createServiceAuth(){
        Form<ServiceAuth> filledForm = serviceForm.bindFromRequest();
        if(filledForm.hasErrors()){
            return badRequest(newService.render(session().get("user"),filledForm));
        }else{
            ServiceAuth serviceToken = new ServiceAuth(filledForm.field("email").value());
            serviceToken.save();
            flash("success", "Un nuevo token de autorización ha sido creado!");
            return redirect(routes.Admins.admin());
        }
    }

    public static Result newService(){
        return ok(newService.render(session().get("user"), serviceForm));
    }



}


