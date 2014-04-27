package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Patient;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.patient;

@Restrict(@Group({"PATIENT"}))
public class Patients extends Controller {
	
	public static Result patient() {
		return ok(patient.render(session().get("user")));
	}
	
}
