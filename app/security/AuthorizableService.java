package security;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernando on 5/9/14.
 */
public class AuthorizableService implements Subject {

    //id of this class' db counterpart ServiceAuth
    Long id;

    public AuthorizableService(Long id){
        this.id = id;
    }


    @Override
    public List<? extends Role> getRoles() {
        return new ArrayList<Roles>() {
            {
                add(Roles.valueOf("SERVICE"));
            }
        };
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return this.id.toString();
    }
}
