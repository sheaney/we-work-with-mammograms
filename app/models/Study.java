package models;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Study extends Model {
	@Id
	Long id;
	
	Date createdAt = new Date(System.currentTimeMillis());
	
	@OneToMany(mappedBy = "commented")
	Set<Comment> comments;
	
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Patient owner;
	
	@OneToMany(mappedBy = "study")
	Set<Mammogram> mammograms;

}
