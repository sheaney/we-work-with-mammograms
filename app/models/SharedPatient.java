package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class SharedPatient extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@JsonIgnore
	@Required
	@ManyToOne
	@JoinColumn(name="sharer_id", nullable=false)
	Staff sharer;
	
	@JsonIgnore
	@Required
	@ManyToOne
	@JoinColumn(name="borrower_id", nullable=false)
	Staff borrower;
	
	@Required
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Patient shared;
	
	@Required
	int accessPrivileges;

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

	public Patient getShared() {
		return shared;
	}

	public void setShared(Patient shared) {
		this.shared = shared;
	}

	public Staff getBorrower() {
		return borrower;
	}

	public void setBorrower(Staff borrower) {
		this.borrower = borrower;
	}

	public int getAccessPrivileges() {
		return accessPrivileges;
	}

	public void setAccessPrivileges(int permission) {
		this.accessPrivileges = permission;
	}

}
