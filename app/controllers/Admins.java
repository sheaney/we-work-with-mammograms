package controllers;

import lib.PasswordGenerator;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.newStaff;
import views.html.admin;

public class Admins extends Controller {

	final static Form<Staff> staffForm = Form.form(Staff.class);
	
	public static Result admin() {
		return ok(admin.render(session("email")));
	}

	public static Result newStaff() {
		return ok(newStaff.render("Juanito", staffForm));
	}

	public static Result createStaff() {
		Form<Staff> filledForm = staffForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newStaff.render("Juanito", filledForm));
		} else {
			PasswordGenerator pg = new PasswordGenerator();
			Staff staff = filledForm.get();
			staff.setPassword(pg.next());
			Staff.create(staff);
			flash("success", "Un nuevo personal se ha creado");
			return redirect(routes.Admins.admin());
		}
	}
}


