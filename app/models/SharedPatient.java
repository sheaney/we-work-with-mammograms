package models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SharedPatient extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;

	@Required
	Date createdAt = new Date(System.currentTimeMillis());

	@JsonIgnore
	@Required
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sharer_id", nullable = false)
	Staff sharer;

	@JsonIgnore
	@Required
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "borrower_id", nullable = false)
	Staff borrower;

	@Required
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "patient_id", nullable = false)
	Patient sharedInstance;

	@Required
	int accessPrivileges;

	public SharedPatient(Staff sharer, Staff borrower, Patient sharedInstance,
			int accessPrivileges) {
		this.sharer = sharer;
		this.borrower = borrower;
		this.sharedInstance = sharedInstance;
		this.accessPrivileges = accessPrivileges;
	}
	
	public static Finder<String,SharedPatient> find = new Finder<String,SharedPatient>(Play.application().configuration().getString("datasource"), String.class, SharedPatient.class);
	
	public static void create(SharedPatient sharedPatient) {
		sharedPatient.save();
	}
	
	public static SharedPatient findById(Long id) {
		return find.byId(String.valueOf(id));
	}

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

	public Patient getSharedInstance() {
		return sharedInstance;
	}

	public void setSharedInstance(Patient shared) {
		this.sharedInstance = shared;
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
