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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Staff getSharer() {
		return sharer;
	}

	public void setSharer(Staff sharer) {
		this.sharer = sharer;
	}

	public Staff getShared() {
		return shared;
	}

	public void setShared(Staff shared) {
		this.shared = shared;
	}

}
