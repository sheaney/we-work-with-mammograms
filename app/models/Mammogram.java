package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Mammogram extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="study_id", nullable=false)
	Study study;
	
	@OneToMany(mappedBy = "annotated")
	List<Annotation> annotations = new ArrayList<Annotation>();

    public static Finder<String,Mammogram> find = new Finder<String,Mammogram>(Play.application().configuration().getString("datasource"), String.class, Mammogram.class);

    public static Mammogram findById(Long id) {
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

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
	
}
