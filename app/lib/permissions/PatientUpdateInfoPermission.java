package lib.permissions;

/**
 * Patient permissions consist of 31 bits 3 bits dictate permissions for
 * updating patient info Therefore an access privilege submask only takes into
 * account these last bits
 */
public class PatientUpdateInfoPermission extends Permission {
	private final static String ACCESS_PRIVILEGES_SUBMASK = "0000000000000000000000000000111";
	private final static String UPDATE_PERSONAL_INFO = "0000000000000000000000000000100";
	private final static String UPDATE_MEDICAL_INFO = "0000000000000000000000000000010";
	private final static String UPDATE_STUDIES = "0000000000000000000000000000001";

	private final int updatePersonalInfo;
	private final int updateMedicalInfo;
	private final int updateStudies;

	public PatientUpdateInfoPermission(int accessPrivileges) {
		super(accessPrivileges, ACCESS_PRIVILEGES_SUBMASK);
		this.updatePersonalInfo = ByteDecodeEncoder.encode(UPDATE_PERSONAL_INFO);
		this.updateMedicalInfo = ByteDecodeEncoder.encode(UPDATE_MEDICAL_INFO);
		this.updateStudies = ByteDecodeEncoder.encode(UPDATE_STUDIES);
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
}
