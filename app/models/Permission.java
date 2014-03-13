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
	
}
