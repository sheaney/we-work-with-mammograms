package lib.json.models;

import static lib.json.JSONConstants.ADDRESS;
import static lib.json.JSONConstants.BIRTHDATE;
import static lib.json.JSONConstants.DATE_FORMATTER;
import static lib.json.JSONConstants.EMAIL;
import static lib.json.JSONConstants.FIRST_LAST_NAME;
import static lib.json.JSONConstants.ID;
import static lib.json.JSONConstants.NAME;
import static lib.json.JSONConstants.ROLE;
import static lib.json.JSONConstants.SECOND_LAST_NAME;
import static lib.json.JSONConstants.TELEPHONE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lib.OwnPatientContainer;
import lib.PatientContainer;
import lib.SharedPatientContainer;
import lib.json.models.JSONPatient;
import models.Patient;
import models.SharedPatient;
import models.Staff;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONStaff {
	private final static String CEDULA = "cedula";
	private final static String RFC = "RFC";
	private final static String OWN_PATIENTS = "ownPatients";
	private final static String BORROWED_PATIENTS = "borrowedPatients";

	public static ObjectNode staffWithPatients(Staff staff) {
		ObjectNode result = Json.newObject();

		result.put(ID, staff.getId());
		result.put(EMAIL, staff.getEmail());
		result.put(ROLE, staff.getRole());
		result.put(NAME, staff.getName());
		result.put(FIRST_LAST_NAME, staff.getFirstLastName());
		result.put(SECOND_LAST_NAME, staff.getSecondLastName());
		result.put(ADDRESS, staff.getAddress());
		result.put(TELEPHONE, staff.getTelephone());
		result.put(BIRTHDATE, DATE_FORMATTER.format(staff.getBirthdate()));
		result.put(CEDULA, staff.getCedula());
		result.put(RFC, staff.getRFC());
		result.put(OWN_PATIENTS, Json.toJson(ownPatients(staff)));
		result.put(BORROWED_PATIENTS, Json.toJson(borrowedPatients(staff)));

		return result;
	}
	
	public static ObjectNode staffPatient(Staff staff,PatientContainer patientContainer) {
		
		if (patientContainer instanceof OwnPatientContainer) {
			Set<Long> sharedIds = getSharedPatientIds(staff);
			Patient searched = ((OwnPatientContainer) patientContainer).getPatient();
			return JSONPatient.staffPatient(searched, sharedIds.contains(searched.getId()));
		}
		SharedPatient borrowed = ((SharedPatientContainer) patientContainer).getSharedPatient();
		return JSONPatient.staffBorrowedPatient(borrowed);
	}

	private static List<ObjectNode> ownPatients(Staff staff) {
		Set<Long> sharedIds = getSharedPatientIds(staff);
		List<Patient> ownPatients = staff.getOwnPatients();
		List<ObjectNode> result = new ArrayList<ObjectNode>();

		for (Patient patient : ownPatients) {
			boolean isShared = sharedIds.contains(patient.getId());
			result.add(JSONPatient.staffPatient(patient, isShared));
		}

		return result;
	}

	private static List<ObjectNode> borrowedPatients(Staff staff) {
		List<SharedPatient> borrowedPatients = staff.getBorrowedPatients();
		List<ObjectNode> result = new ArrayList<ObjectNode>();

		for (SharedPatient borrowedPatient : borrowedPatients) {
			result.add(JSONPatient.staffBorrowedPatient(borrowedPatient));
		}

		return result;
	}

	private static Set<Long> getSharedPatientIds(Staff staff) {
		Set<Long> ids = new HashSet<Long>();

		List<SharedPatient> sharedPatients = staff.getSharedPatients();
		for (SharedPatient sharedPatient : sharedPatients) {
			Patient shared = sharedPatient.getSharedInstance();
			ids.add(shared.getId());
		}

		return ids;
	}
	


}
