package controllers;

import lib.Email.Postman;
import lib.PasswordGenerator;
import models.Admin;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin;
import views.html.newStaff;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;


@Restrict(@Group({"ADMIN"}))
public class Admins extends Controller {

	final static Form<Staff> staffForm = Form.form(Staff.class);
	
	public static Result admin() {
		return ok(admin.render(session().get("user")));
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
//            Postman.welcomeEmail(staff.getFullName(),staff.getEmail(),staff.getPassword());
			flash("success", "Un nuevo personal se ha creado");
			return redirect(routes.Admins.admin());
		}
	}
}


