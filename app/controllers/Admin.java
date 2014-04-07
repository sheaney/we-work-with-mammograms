package controllers;

//import models.Admin;
import models.Staff;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.newStaff;

public class Admin extends Controller {

	final static Form<Staff> staffForm = Form.form(Staff.class);

	public static Result newStaff() {
		return ok(newStaff.render("Juanito", staffForm));
	}

	public static Result createStaff() {
		Form<Staff> filledForm = staffForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newStaff.render("Juanito", filledForm));
		} else {
			Staff.create(filledForm.get());
			return redirect(routes.Application.index());
		}

//		return TODO;
	}
}


