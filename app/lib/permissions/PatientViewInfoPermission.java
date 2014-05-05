package lib.permissions;

/**
 * Patient permissions consist of 31 bits Last 3 bits dictate permissions for
 * viewing patient info Therefore an access privilege submask only takes into
 * account these last bits
 */
final public class PatientViewInfoPermission extends Permission {
	private final static String ACCESS_PRIVILEGES_SUBMASK = "0000000000000000000000000000111";
	private final static String VIEW_PERSONAL_INFO 		  = "0000000000000000000000000000100";
	private final static String VIEW_MEDICAL_INFO 		  = "0000000000000000000000000000010";
	private final static String VIEW_STUDIES 			  = "0000000000000000000000000000001";

	private final int viewPersonalInfo = ByteDecodeEncoder
			.encode(VIEW_PERSONAL_INFO);
	private final int viewMedicalInfo = ByteDecodeEncoder
			.encode(VIEW_MEDICAL_INFO);
	private final int viewStudies = ByteDecodeEncoder.encode(VIEW_STUDIES);

	public PatientViewInfoPermission(boolean personalInfo, boolean medicalInfo,
			boolean studies) {
		super(calculateAccessPrivileges(personalInfo, medicalInfo, studies),
				ACCESS_PRIVILEGES_SUBMASK);
	}

	public PatientViewInfoPermission(int accessPrivileges) {
		super(accessPrivileges, ACCESS_PRIVILEGES_SUBMASK);
	}

	public boolean canViewPersonalInfo() {
		return hasPermission(viewPersonalInfo);
	}

	public boolean canViewMedicalInfo() {
		return hasPermission(viewMedicalInfo);
	}

	public boolean canViewStudies() {
		return hasPermission(viewStudies);
	}

	private static int calculateAccessPrivileges(boolean personalInfo,
			boolean medicalInfo, boolean studies) {
		int accessPrivileges = 0;
		if (personalInfo) {
			accessPrivileges += ByteDecodeEncoder.encode(VIEW_PERSONAL_INFO);
		}
		if (medicalInfo) {
			accessPrivileges += ByteDecodeEncoder.encode(VIEW_MEDICAL_INFO);
		}
		if (studies) {
			accessPrivileges += ByteDecodeEncoder.encode(VIEW_STUDIES);
		}
		return accessPrivileges;
	}

}
