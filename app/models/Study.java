package models;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Study extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	Date createdAt = new Date(System.currentTimeMillis());
	
	@OneToMany(mappedBy = "commented")
	Set<Comment> comments = new HashSet<Comment>();
	
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Patient owner;
	
	@OneToMany(mappedBy = "study")
	Set<Mammogram> mammograms = new HashSet<Mammogram>();

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

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Patient getOwner() {
		return owner;
	}

	public void setOwner(Patient owner) {
		this.owner = owner;
	}

	public Set<Mammogram> getMammograms() {
		return mammograms;
	}

	public void setMammograms(Set<Mammogram> mammograms) {
		this.mammograms = mammograms;
	}

}
