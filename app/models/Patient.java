package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import play.Play;
import play.db.ebean.Model;

@Entity
public class Patient extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	PersonalInfo personalInfo;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	MedicalInfo medicalInfo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="staff_id", nullable=false)
	Staff owner;
	
	@OneToMany(mappedBy = "owner")
	List<Study> studies = new ArrayList<Study>();
	
	@OneToMany(mappedBy = "shared")
	List<SharedPatient> sharedInstances = new ArrayList<SharedPatient>();
    
	public static Finder<String,Patient> find = new Finder<String,Patient>(Play.application().configuration().getString("datasource"), String.class, Patient.class);
	
	public static void create(Patient patient) {
		patient.save();
	}
	
	public static List<Patient> all() {
		return find.all();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public MedicalInfo getMedicalInfo() {
		return medicalInfo;
	}

	public void setMedicalInfo(MedicalInfo medicalInfo) {
		this.medicalInfo = medicalInfo;
	}

	public Staff getOwner() {
		return owner;
	}

	public void setOwner(Staff owner) {
		this.owner = owner;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}

	public List<SharedPatient> getSharedInstances() {
		return sharedInstances;
	}

	public void setSharedInstances(List<SharedPatient> sharedInstances) {
		this.sharedInstances = sharedInstances;
	}
	
}
