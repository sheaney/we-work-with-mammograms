package security;

import be.objectify.deadbolt.core.models.Role;

public enum Roles implements Role{
	ADMIN, PATIENT, STAFF;

	@Override
	public String getName() {
		return name();
	}
}
