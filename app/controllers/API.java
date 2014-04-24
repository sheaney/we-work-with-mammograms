package controllers;

import java.util.List;

import lib.json.staff.JSONStaff;
import models.Patient;
import models.Staff;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class API extends Controller {
	
	public static Result getPatient(Long id) {
		Patient patient = Patient.findById(id);
		return ok(Json.toJson(patient));
	}
	
	public static Result staff() {
		List<Staff> staff = Staff.all();
		return ok(Json.toJson(staff));
	}
	
	public static Result getStaff(Long id) {
		Staff staff = Staff.findById(id);
		return ok(JSONStaff.staffWithPatients(staff));
	}

}
