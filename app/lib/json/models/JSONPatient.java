package lib.json.models;

import static lib.json.JSONConstants.ID;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import models.Patient;
import models.SharedPatient;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class JSONPatient {
	private final static String PERSONAL_INFO = "personalInfo";
	private final static String MEDICAL_INFO = "medicalInfo";
	private final static String STUDIES = "studies";
	private final static String SHARED = "shared";
	private final static String UPDATEABLE_PERSONAL_INFO = "updateablePersonalInfo";
	private final static String UPDATEABLE_MEDICAL_INFO = "updateableMedicalInfo";
	private final static String UPDATEABLE_STUDIES = "updateableStudies";
	
	public static ObjectNode staffPatient(Patient patient, boolean isShared) {
		ObjectNode json = Json.newObject();
		json.put(ID, patient.getId());
		json.put(SHARED, Json.toJson(isShared));
		json.put(UPDATEABLE_PERSONAL_INFO, true);
		json.put(PERSONAL_INFO, Json.toJson(patient.getPersonalInfo()));
		json.put(UPDATEABLE_MEDICAL_INFO, true);
		json.put(MEDICAL_INFO, Json.toJson(patient.getMedicalInfo()));
		json.put(UPDATEABLE_STUDIES, true);
		json.put(STUDIES, Json.toJson(patient.getStudies()));
		
		return json;
	}
	
	public static ObjectNode staffBorrowedPatient(SharedPatient borrowed) {
		ObjectNode json = Json.newObject();
		
		addPermittedInfo(borrowed, json);
		
		return json;
	}
	
	private static void addPermittedInfo(SharedPatient borrowed, ObjectNode json) {
		int accessPrivileges = borrowed.getAccessPrivileges();
		PatientViewInfoPermission viewPermissions = new PatientViewInfoPermission(accessPrivileges);
		PatientUpdateInfoPermission updatePermissions = new PatientUpdateInfoPermission(accessPrivileges);
		Patient patient = borrowed.getSharedInstance();
		
		json.put(ID, patient.getId());
		if (viewPermissions.canViewPersonalInfo()) {
			json.put(UPDATEABLE_PERSONAL_INFO, updatePermissions.canUpdatePersonalInfo());
			json.put(PERSONAL_INFO, Json.toJson(patient.getPersonalInfo()));
		}
		if (viewPermissions.canViewMedicalInfo()) {
			json.put(UPDATEABLE_MEDICAL_INFO, updatePermissions.canUpdateMedicalInfo());
			json.put(MEDICAL_INFO, Json.toJson(patient.getMedicalInfo()));
		}
		if (viewPermissions.canViewStudies()) {
			json.put(UPDATEABLE_STUDIES, updatePermissions.canUpdateStudies());
			json.put(STUDIES, Json.toJson(patient.getStudies()));
		}
	}

    public static ObjectNode allPatientsService(){
        List<Patient> patients = Patient.all();
        List<Long> ids = new ArrayList<Long>();
        ObjectNode JsonPatientsIds = Json.newObject();
        for(Patient patient: patients){
            ids.add(patient.getId());
        }
        JsonPatientsIds.put("Patients",Json.toJson(ids));
        return JsonPatientsIds;
    }
}
