package lib.permissions;

public abstract class Permission {
	private final int accessPrivileges;
	private final int particularAccessPrivileges;
	
	public Permission(int accessPrivileges, String accessPrivilegesSubmask) {
		this.accessPrivileges = accessPrivileges;
		this.particularAccessPrivileges = accessPrivileges & ByteDecodeEncoder.encode(accessPrivilegesSubmask);
	}
	
	public static int concatAccessPrivileges(Permission... permissions){
		int ORedAccessPrivileges = 0;
		
		for (Permission permission : permissions) {
			ORedAccessPrivileges |= permission.particularAccessPrivileges;
		}
		
		return ORedAccessPrivileges;
	}
	
	protected boolean hasPermission(int privilege) {
		return (privilege & particularAccessPrivileges) == privilege;
	}

	public int getAccessPrivileges() {
		return accessPrivileges;
	}
	
}