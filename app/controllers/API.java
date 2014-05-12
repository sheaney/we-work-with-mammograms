package controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import controllers.validations.APIValidations;
import lib.PatientContainer;
import lib.json.errors.JSONErrors;
import lib.json.models.JSONPatient;
import lib.json.models.JSONStaff;
import lib.permissions.PatientUpdateInfoPermission;
import models.MedicalInfo;
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
import security.ServiceDeadboltHandler;
import security.StandardDeadboltHandler;

//TODO refactor this class
@Restrict(value={@Group("STAFF")}, handler = StandardDeadboltHandler.class)
public class API extends Controller {
	final static Form<PersonalInfo> personalInfoBinding = Form.form(PersonalInfo.class);
	final static Form<MedicalInfo> medicalInfoBinding = Form.form(MedicalInfo.class);

	public static Result getPatient(Long id) {
		Patient patient = Patient.findById(id);
		return ok(Json.toJson(patient));
	}

	public static Result staff() {
		List<Staff> staff = Staff.all();
		List<Staff> staffWithoutCurrent = APIHelpers.filterMatchingStaffFromList(staff, obtainStaff());
		return ok(Json.toJson(staffWithoutCurrent));
	}

	public static Result getStaff(Long id) {
		Staff staff = Staff.findById(id);
		return ok(JSONStaff.staffWithPatients(staff));
	}

	public static Result getPatientInfo(Long id) {
		Staff staff = obtainStaff();
		Patient patient = Patient.findById(id);
		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound(Json.newObject().put("NOT_FOUND", "")); // return json with error msg
		else if (patientContainer.isEmpty())
			return forbidden(Json.newObject().put("FORBIDDEN", "")); // return json with error msg

		// success
		return ok(JSONStaff.staffPatient(staff, patientContainer));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePersonalInfo(Long id) {
		// Check that this patient actually exists
		Patient patient = Patient.findById(id);
		Staff staff = obtainStaff();

		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound("Patient doesn't exist.");

		if (new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges()).canUpdatePersonalInfo()) {
			JsonNode jsonNode = request().body().asJson();
			Form<PersonalInfo> binding = personalInfoBinding.bind(jsonNode);
			if (binding.hasErrors()) {
				return badRequest(Json.toJson(JSONErrors.patientInfoErrors(getErrors(binding))));
			} else {
				PersonalInfo info = binding.get();
				patient.setPersonalInfo(info);
				patient.getPersonalInfo().update();
				return ok("Success");
			}
		} else

			return unauthorized("Can't update info.");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateMedicalInfo(Long id) {
		// Check that this patient actually exists
		Patient patient = Patient.findById(id);
		Staff staff = obtainStaff();

		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound("Patient doesn't exist.");

		else if (new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges()).canUpdateMedicalInfo()) {
			JsonNode jsonNode = request().body().asJson();
			Form<MedicalInfo> binding = medicalInfoBinding.bind(jsonNode);
			if (binding.hasErrors()) {
				System.out.println("Errors");
				return badRequest(Json.toJson(JSONErrors.patientInfoErrors(getErrors(binding))));
			} else {
				System.out.println("Successful Update");
				MedicalInfo info = binding.get();
				patient.setMedicalInfo(info);
				patient.getMedicalInfo().update();
				return ok("success");
			}
		} else
			return unauthorized("Can't update info");
		
	}

	public static Staff obtainStaff() {
		// Get staff ID from session or from API access token
		Long staffId = Long.parseLong(session().get("id"));
		return Staff.findById(staffId);
	}

	private static <T> Map<String, String> getErrors(Form<T> form) {
		Map<String, String> errors = new HashMap<String, String>();

		for (Map.Entry<String, List<ValidationError>> entry : form.errors().entrySet()) {
			String field = entry.getKey();
			List<ValidationError> validationErrors = entry.getValue();
			List<String> messages = new LinkedList<String>();
			for (ValidationError ve : validationErrors) {
				String msg = Messages.get(ve.message(), ve.arguments());
				messages.add(msg);
			}
			String concatMessages = concatenateStrings(messages, "\n");
			errors.put(field, concatMessages);
		}

		return errors;
	}

	private static String concatenateStrings(List<String> strings, String sep) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings)
			sb.append(s).append(sep);
		return sb.toString();
	}
}
