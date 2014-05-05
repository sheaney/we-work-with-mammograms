package lib.permissions;

/**
 * Patient permissions consist of 31 bits 3 bits dictate permissions for
 * updating patient info Therefore an access privilege submask only takes into
 * account these last bits
 */
public class PatientUpdateInfoPermission extends Permission {
	private final static String ACCESS_PRIVILEGES_SUBMASK = "0000000000000000000000000111000";
	private final static String UPDATE_PERSONAL_INFO 	  = "0000000000000000000000000100000";
	private final static String UPDATE_MEDICAL_INFO 	  = "0000000000000000000000000010000";
	private final static String UPDATE_STUDIES 			  = "0000000000000000000000000001000";

	private final int updatePersonalInfo = ByteDecodeEncoder
			.encode(UPDATE_PERSONAL_INFO);
	private final int updateMedicalInfo = ByteDecodeEncoder
			.encode(UPDATE_MEDICAL_INFO);
	private final int updateStudies = ByteDecodeEncoder.encode(UPDATE_STUDIES);

	public PatientUpdateInfoPermission(boolean personalInfo,
			boolean medicalInfo, boolean studies) {
		super(calculateAccessPrivileges(personalInfo, medicalInfo, studies),
				ACCESS_PRIVILEGES_SUBMASK);
	}

	public PatientUpdateInfoPermission(int accessPrivileges) {
		super(accessPrivileges, ACCESS_PRIVILEGES_SUBMASK);
	}

	public boolean canUpdatePersonalInfo() {
		return hasPermission(updatePersonalInfo);
	}

	public boolean canUpdateMedicalInfo() {
		return hasPermission(updateMedicalInfo);
	}

	public boolean canUpdateStudies() {
		return hasPermission(updateStudies);
	}

	private static int calculateAccessPrivileges(boolean personalInfo,
			boolean medicalInfo, boolean studies) {
		int accessPrivileges = 0;
		if (personalInfo) {
			accessPrivileges += ByteDecodeEncoder.encode(UPDATE_PERSONAL_INFO);
		}
		if (medicalInfo) {
			accessPrivileges += ByteDecodeEncoder.encode(UPDATE_MEDICAL_INFO);
		}
		if (studies) {
			accessPrivileges += ByteDecodeEncoder.encode(UPDATE_STUDIES);
		}
		return accessPrivileges;
	}
}
