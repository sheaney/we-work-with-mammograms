package lib.json.permissions;

import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONPermissions {
	public final static String VIEW_PERSONAL_INFO = "viewPersonalInfo";
	public final static String UPDATE_PERSONAL_INFO = "updatePersonalInfo";
	public final static String VIEW_MEDICAL_INFO = "viewMedicalInfo";
	public final static String UPDATE_MEDICAL_INFO = "updateMedicalInfo";
	public final static String VIEW_STUDIES = "viewStudies";
	public final static String UPDATE_STUDIES = "updateStudies";

	public static PatientViewInfoPermission unbindViewInfoPermissions(JsonNode json) {
        try {
            boolean viewPatientInfo = json.get(VIEW_PERSONAL_INFO).asBoolean();
            boolean viewMedicalInfo = json.get(VIEW_MEDICAL_INFO).asBoolean();
            boolean viewStudies = json.get(VIEW_STUDIES).asBoolean();
            PatientViewInfoPermission viewInfoPermissions = new PatientViewInfoPermission(
                    viewPatientInfo, viewMedicalInfo, viewStudies);
            return viewInfoPermissions;
        } catch (NullPointerException npe) {
            return null;
        }
	}
	
	public static PatientUpdateInfoPermission unbindUpdateInfoPermissions(JsonNode json) {
        try {
            boolean updatePersonalInfo = json.get(UPDATE_PERSONAL_INFO).asBoolean();
            boolean updateMedicalInfo = json.get(UPDATE_MEDICAL_INFO).asBoolean();
            boolean updateStudies = json.get(UPDATE_STUDIES).asBoolean();
            PatientUpdateInfoPermission updateInfoPermissions = new PatientUpdateInfoPermission(
                    updatePersonalInfo, updateMedicalInfo, updateStudies);

            return updateInfoPermissions;
        } catch (NullPointerException npe) {
            return null;
        }
	}

}