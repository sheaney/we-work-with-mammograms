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
public class Mammogram extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@Required
	String url;
	
	@ManyToOne
	@JoinColumn(name="study_id", nullable=false)
	Study study;
	
	@OneToMany(mappedBy = "annotated")
	Set<Annotation> annotations;
	
}
