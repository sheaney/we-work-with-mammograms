package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class SharedPatient extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@ManyToOne
	@JoinColumn(name="staff_id", nullable=false)
	Staff sharer;
	
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Staff shared;

}
