package lib.permissions;

public abstract class Permission {
	private final int particularAccessPrivileges;
	
	public Permission(String accessPrivileges, String accessPrivilegesSubmask) {
		this.particularAccessPrivileges = ByteDecodeEncoder.encode(accessPrivileges) & ByteDecodeEncoder.encode(accessPrivilegesSubmask);
	}
	
	protected boolean hasPermission(int privilege) {
		return (privilege & particularAccessPrivileges) == privilege;
	}
	
}