package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Annotation extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@MaxLength(value = 200)
	String content;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="mammogram_id", nullable=false)
	Mammogram annotated;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="staff_id", nullable=false)
	Staff annotator;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Mammogram getAnnotated() {
		return annotated;
	}

	public void setAnnotated(Mammogram annotated) {
		this.annotated = annotated;
	}

	public Staff getAnnotator() {
		return annotator;
	}

	public void setAnnotator(Staff annotator) {
		this.annotator = annotator;
	}
	
}
