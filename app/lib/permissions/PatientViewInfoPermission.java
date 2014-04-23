package lib.permissions;

/**
 * Patient permissions consist of 31 bits
 * Last 3 bits dictate permissions for viewing patient info
 * Therefore an access privilege submask only takes into account these last bits
 */
final public class PatientViewInfoPermission extends Permission {
	private final static String ACCESS_PRIVILEGES_SUBMASK = "0000000000000000000000000000111";
	private final static String VIEW_PERSONAL_INFO 		  = "0000000000000000000000000000100";
	private final static String VIEW_MEDICAL_INFO 		  = "0000000000000000000000000000010";
	private final static String VIEW_STUDIES 			  = "0000000000000000000000000000001";
	
	private final int viewPersonalInfo;
	private final int viewMedicalInfo;
	private final int viewStudies;
	
	public PatientViewInfoPermission(String accessPrivileges) {
		super(accessPrivileges, ACCESS_PRIVILEGES_SUBMASK);
		this.viewPersonalInfo = ByteDecodeEncoder.encode(VIEW_PERSONAL_INFO);
		this.viewMedicalInfo = ByteDecodeEncoder.encode(VIEW_MEDICAL_INFO);
		this.viewStudies = ByteDecodeEncoder.encode(VIEW_STUDIES);
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
	
}
