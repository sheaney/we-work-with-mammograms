package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Admin extends Model{
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	String email;
	
	@Required
	String password;
	
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
}
