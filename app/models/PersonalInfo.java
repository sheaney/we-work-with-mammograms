package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PersonalInfo extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	Long id;
	
	@Required 
	String name;
	
	@Required 
	String firstLastName;
	
	@Required 
	String secondLastName;
	
	@Required 
	String address;
	
	@Required 
	String email;
	
	@Required 
	String telephone;
	
	@Required
	@Formats.DateTime(pattern="dd/MM/yyyy")
	Date birthdate;
	
	@JsonIgnore
	String password;
	
    public static Finder<String,PersonalInfo> find = new Finder<String,PersonalInfo>(
            String.class, PersonalInfo.class
    );
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
    public static PersonalInfo authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }
}
