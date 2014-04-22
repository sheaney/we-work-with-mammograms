package controllers;

import models.Patient;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.patient;

public class Patients extends Controller {
	
	public static Result patient() {
		return ok(patient.render(Patient.findById(Long.parseLong(session("id")))));
	}
	
}
