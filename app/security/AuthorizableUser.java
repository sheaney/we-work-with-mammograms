package security;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Http.Context;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class AuthorizableUser implements Subject {

	private Context context;
	private Long timeOfLogin;

	public AuthorizableUser(Context context) {
		this.context = context;
		this.timeOfLogin = Long.parseLong(context.session().get("timeOfLogin"));
	}

	@Override
	public List<? extends Role> getRoles() {
		return new ArrayList<Roles>() {
			{
				String type = context.session().get("type");
				if (type != null) {
					add(Roles.valueOf(type));
				}
			}
		};
	}

	//The following classes are required for the correct use of Deadbolt2, but they need not be implemented.
	@Override
	public List<? extends Permission> getPermissions() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return null;
	}

	public Long getTimeOfLogin() {
		return timeOfLogin;
	}

	public void setTimeOfLogin(Long timeOfLogin) {
		this.timeOfLogin = timeOfLogin;
	}
}