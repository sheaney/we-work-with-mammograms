package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Patient extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	PersonalInfo personalInfo;
	
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	MedicalInfo medicalInfo;
	
	@ManyToOne
	@JoinColumn(name="staff_id", nullable=false)
	Staff owner;
	
	@OneToMany(mappedBy = "owner")
	Set<Study> studies = new HashSet<Study>();
	
	@OneToMany(mappedBy = "patientBoundedByPermissions")
	Set<Permission> permissions = new HashSet<Permission>();
	
	@OneToMany(mappedBy = "shared")
	Set<SharedPatient> sharedInstances = new HashSet<SharedPatient>();

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

	public Set<Study> getStudies() {
		return studies;
	}

	public void setStudies(Set<Study> studies) {
		this.studies = studies;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<SharedPatient> getSharedInstances() {
		return sharedInstances;
	}

	public void setSharedInstances(Set<SharedPatient> sharedInstances) {
		this.sharedInstances = sharedInstances;
	}
	
}
