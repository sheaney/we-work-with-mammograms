package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Permission extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	Byte value;
	
	@ManyToOne
	@JoinColumn(name="staff_id", nullable=false)
	Staff ownsPermission;
	
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Patient patientBoundedByPermissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getValue() {
		return value;
	}

	public void setValue(Byte value) {
		this.value = value;
	}

	public Staff getOwnsPermission() {
		return ownsPermission;
	}

	public void setOwnsPermission(Staff ownsPermission) {
		this.ownsPermission = ownsPermission;
	}

	public Patient getPatientBoundedByPermissions() {
		return patientBoundedByPermissions;
	}

	public void setPatientBoundedByPermissions(Patient patientBoundedByPermissions) {
		this.patientBoundedByPermissions = patientBoundedByPermissions;
	}
	
}
