package models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.data.format.Formats;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Staff extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	@Email
	String email;
	
//	@Required
	String password;
	
	@Required
	String role;
	
	@Required
	String name;

	@Required
	String firstLastName;
	
	@Required
	String secondLastName;
	
	@Required
	String address;
	
	@Required
	String telephone;
	
	@Required
	@Formats.DateTime(pattern="dd/MM/yyyy")
	Date birthdate;
	
	@Required
	String cedula;
	
	@Required
	String RFC;
	
	@OneToMany(mappedBy="owner", cascade = CascadeType.ALL)
	List<Patient> ownPatients;
	
	@OneToMany(mappedBy="commenter")
	List<Comment> comments = new ArrayList<Comment>();
	
	@OneToMany(mappedBy="ownsPermission")
	List<Permission> permissions = new ArrayList<Permission>();
	
	@OneToMany(mappedBy="annotator")
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	@OneToMany(mappedBy="sharer")
	List<SharedPatient> sharedPatients = new ArrayList<SharedPatient>();
	
	public static void create(Staff staff) {
		staff.save();
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getRFC() {
		return RFC;
	}

	public void setRFC(String rFC) {
		RFC = rFC;
	}

	public List<Patient> getOwnPatients() {
		return ownPatients;
	}

	public void appendOwnPatient(Patient newPatient) {
		this.ownPatients.add(newPatient);
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public List<SharedPatient> getSharedPatients() {
		return sharedPatients;
	}

	public void setSharedPatients(List<SharedPatient> sharedPatients) {
		this.sharedPatients = sharedPatients;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setOwnPatients(List<Patient> ownPatients) {
		this.ownPatients = ownPatients;
	}

}
