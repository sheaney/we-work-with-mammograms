package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import security.Roles;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;

@Entity
public class Admin extends Model implements Subject{
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	String email;
	
	@Required
	String password;
	
	List<Roles> roles = new ArrayList<Roles>();
	
    public static Finder<String,Admin> find = new Finder<String,Admin>(
            String.class, Admin.class
    );
    
	public static Admin findById(Long id) {
		return find.byId(String.valueOf(id));
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public static Admin authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Roles> getRoles() {
		if(roles.isEmpty()){
			roles.add(Roles.ADMIN);
		}
		return roles;
	}
}
