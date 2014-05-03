package controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lib.json.patient.JSONPatient;
import lib.json.staff.JSONStaff;
import models.Patient;
import models.PersonalInfo;
import models.Staff;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class API extends Controller {
	final static Form<PersonalInfo> personalInfoBinding = Form.form(PersonalInfo.class);
	
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
	
	public static Result getPatientInfo(Long id) {
		// Get staff ID from session or from API access token
		Long staffId = Long.parseLong(session().get("id"));
		Staff staff = Staff.findById(staffId);
		
		// Validate that patient really does exist, and return appropriate error or success message
		return ok(JSONStaff.staffPatient(staff, id));
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePersonalInfo(Long id) {
		Patient patient = Patient.findById(id);
		JsonNode jsonNode = request().body().asJson();
		System.out.println(jsonNode);
		Form<PersonalInfo> binding = personalInfoBinding.bind(jsonNode);
		if (binding.hasErrors()) {
			return badRequest(Json.toJson(JSONPatient.staffPatientFailure(getErrors(binding))));
		} else {
			PersonalInfo info = binding.get();
			patient.setPersonalInfo(info);
			patient.getPersonalInfo().update();
			return ok("foo");
		}
	}
	
	private static <T> Map<String, String> getErrors(Form<T> form) {
		Map<String, String> errors = new HashMap<String, String>();
		
		for (Map.Entry<String, List<ValidationError>> entry : form.errors().entrySet()) {
			String field = entry.getKey();
			List<ValidationError> validationErrors = entry.getValue();
			List<String> messages = new LinkedList<String>();
			for (ValidationError ve : validationErrors) {
				String msg = Messages.get(ve.message());
				messages.add(msg);
			}
			String concatMessages = concatenateStrings(messages, "\n");
			errors.put(field, concatMessages);
		}
		
		return errors;
	}
	
	private static String concatenateStrings(List<String> strings, String sep) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s + sep);
		}
		return sb.toString();
	}

}
