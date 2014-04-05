package controllers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Staff;
import play.data.Form;
import play.data.validation.ValidationError;
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
			Map<String, List<ValidationError>> errores = filledForm.errors();
			for (Entry<String, List<ValidationError>> entry : errores.entrySet()) {
				List<ValidationError> es = entry.getValue();
				System.out.println(entry.getKey());
				for (ValidationError e : es) {
					System.out.println(e.message());
				}
			}
			return badRequest(newStaff.render("Juanito", filledForm));
		} else {
			Staff.create(filledForm.get());
			return redirect(routes.Application.index());
		}

//		return TODO;
	}
}


