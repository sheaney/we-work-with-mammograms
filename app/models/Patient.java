package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Patient extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	PersonalInfo personalInfo;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	MedicalInfo medicalInfo;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="staff_id", nullable=false)
	Staff owner;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	List<Study> studies = new ArrayList<Study>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "sharedInstance", cascade = CascadeType.ALL)
	List<SharedPatient> sharedInstances = new ArrayList<SharedPatient>();
	
	@Required
	boolean viewComments;
	
	@Required
	boolean viewAnnotations;
	
	public static Finder<String,Patient> find = new Finder<String,Patient>(Play.application().configuration().getString("datasource"), String.class, Patient.class);
	
	public static void create(Patient patient) {
		patient.save();
	}
	
	public static List<Patient> all() {
		return find.all();
	}
	
	public static Map<String, String> availableNumberedOptions() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("10", "10");
		options.put("9", "9");
		options.put("8", "8");
		options.put("7", "7");
		options.put("6", "6");
		options.put("5", "5");
		options.put("4", "4");
		options.put("3", "3");
		options.put("2", "2");
		options.put("1", "1");
		options.put("0", "0");
		return options;
	}
	
	public static Patient findById(Long id) {
		return find.byId(String.valueOf(id));
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

	public boolean canViewComments() {
		return viewComments;
	}

	public void setViewComments(boolean viewComments) {
		this.viewComments = viewComments;
	}

	public boolean canViewAnnotations() {
		return viewAnnotations;
	}

	public void setViewAnnotations(boolean viewAnnotations) {
		this.viewAnnotations = viewAnnotations;
	}
	
	@Override
	public String toString() {
		return this.personalInfo != null ? this.personalInfo.getFullName() : "Sin nombre";
	}

}
