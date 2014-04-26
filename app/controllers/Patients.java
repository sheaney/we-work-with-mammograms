package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.patient;

public class Patients extends Controller {
	
	public static Result patient() {
		String email = session().get("email");
		session("id",1+"");
		return ok(patient.render(session("email")));
	}
	
}
