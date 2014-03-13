package models;

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
	Set<Study> studies;
	
	@OneToMany(mappedBy = "patientBoundedByPermissions")
	Set<Permission> permissions;
	
	@OneToMany(mappedBy = "shared")
	Set<SharedPatient> sharedInstances;
	
	
}
