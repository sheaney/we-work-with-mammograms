package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.Play;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Comment extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@MaxLength(value = 200)
	String content;
	
	@Required
	Date createdAt = new Date(System.currentTimeMillis());
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="staff_id")
	Staff commenter;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="service_auth_id")
    Staff serviceCommenter;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="study_id", nullable=false)
	Study commented;

    public static Finder<String,Comment> find = new Finder<String,Comment>(Play.application().configuration().getString("datasource"), String.class, Comment.class);

    public static Comment findById(Long id) {
        return find.byId(String.valueOf(id));
    }

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

	public Staff getCommenter() {
		return commenter;
	}

	public void setCommenter(Staff commenter) {
		this.commenter = commenter;
	}

	public Study getCommented() {
		return commented;
	}

	public void setCommented(Study commented) {
		this.commented = commented;
	}

    public Staff getServiceCommenter() {
        return serviceCommenter;
    }

    public void setServiceCommenter(Staff serviceCommenter) {
        this.serviceCommenter = serviceCommenter;
    }
}
