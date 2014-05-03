package lib.json.patient;

import static lib.json.JSONConstants.ID;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lib.permissions.PatientViewInfoPermission;
import models.Patient;
import models.SharedPatient;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONPatient {
	private final static String PERSONAL_INFO = "personalInfo";
	private final static String MEDICAL_INFO = "medicalInfo";
	private final static String STUDIES = "studies";
	private final static String SHARED = "shared";
	private final static String ACCESS_PRIVILEGES = "accessPrivileges";
	
	private final static String FIELD = "field";
	private final static String MSG = "msg";
	
	public static ObjectNode staffPatient(Patient patient, boolean isShared) {
		ObjectNode json = Json.newObject();
		json.put(ID, patient.getId());
		json.put(ACCESS_PRIVILEGES, Integer.MAX_VALUE);
		json.put(SHARED, Json.toJson(isShared));
		json.put(PERSONAL_INFO, Json.toJson(patient.getPersonalInfo()));
		json.put(MEDICAL_INFO, Json.toJson(patient.getMedicalInfo()));
		json.put(STUDIES, Json.toJson(patient.getStudies()));
		
		return json;
	}
	
	public static ObjectNode staffBorrowedPatient(SharedPatient borrowed) {
		ObjectNode json = Json.newObject();
		
		addPermittedInfo(borrowed, json);
		
		return json;
	}
	
	public static List<ObjectNode> staffPatientFailure(Map<String, String> errors) {
		List<ObjectNode> errorsLst = new LinkedList<ObjectNode>();
		
		for (Map.Entry<String, String> error : errors.entrySet()) {
			ObjectNode node = Json.newObject();
			String field = error.getKey();
			String msg = error.getValue();
			node.put(FIELD, field);
			node.put(MSG, msg);
			errorsLst.add(node);
		}
		
		return errorsLst;
	}
	
	private static void addPermittedInfo(SharedPatient borrowed, ObjectNode json) {
		int accessPrivileges = borrowed.getAccessPrivileges();
		PatientViewInfoPermission viewPermissions = new PatientViewInfoPermission(accessPrivileges);
		Patient patient = borrowed.getSharedInstance();
		
		json.put(ID, patient.getId());
		json.put(ACCESS_PRIVILEGES, accessPrivileges);
		if (viewPermissions.canViewPersonalInfo()) {
			json.put(PERSONAL_INFO, Json.toJson(patient.getPersonalInfo()));
		}
		if (viewPermissions.canViewMedicalInfo()) {
			json.put(MEDICAL_INFO, Json.toJson(patient.getMedicalInfo()));
		}
		if (viewPermissions.canViewStudies()) {
			json.put(STUDIES, Json.toJson(patient.getStudies()));
		}
	}

}
