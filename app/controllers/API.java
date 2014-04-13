package controllers;

import java.util.List;

import models.Patient;
import models.Staff;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class API extends Controller {
	
	public static Result staff() {
		List<Staff> staff = Staff.all();
		return ok(Json.toJson(staff));
	}
	
	public static Result patient() {
		List<Patient> patients = Patient.all();
		return ok(Json.toJson(patients));
	}

}
