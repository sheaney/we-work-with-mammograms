package security;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Http.Context;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class AuthorizableUser implements Subject {

	private Context context;

	public AuthorizableUser(Context context) {
		this.context = context;
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

	@Override
	public List<? extends Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

}