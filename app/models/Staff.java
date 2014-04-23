package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.Play;
import play.data.format.Formats;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Staff extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	@Email
	String email;
	
	@JsonIgnore
	String password;
	
	@Required
	//Role of the medical staff within their professional health environment
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
	
	@OneToMany(mappedBy="annotator")
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	@OneToMany(mappedBy="sharer")
	List<SharedPatient> sharedPatients = new ArrayList<SharedPatient>();
	
	@OneToMany(mappedBy="borrower")
	List<SharedPatient> borrowedPatients = new ArrayList<SharedPatient>();
	
	public static Finder<String,Staff> find = new Finder<String,Staff>(Play.application().configuration().getString("datasource"), String.class, Staff.class);
	
	public static void create(Staff staff) {
		staff.save();
	}
	
	public static List<Staff> all() {
		return find.all();
	}
	
	public String getFullName(){
		return this.name + " " + this.firstLastName + " " + this.secondLastName;
	}
	
	public static Staff findById(Long id) {
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
	
    public List<SharedPatient> getBorrowedPatients() {
		return borrowedPatients;
	}

	public void setBorrowedPatients(List<SharedPatient> borrowedPatients) {
		this.borrowedPatients = borrowedPatients;
	}

	public static Staff authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }
}
