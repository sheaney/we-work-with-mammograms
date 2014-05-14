package models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import lib.DBExecutionContext;
import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.F.Function0;
import play.libs.F.Promise;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Study extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	Date createdAt = new Date(System.currentTimeMillis());
	
	@OneToMany(mappedBy = "commented", cascade = CascadeType.ALL)
	@Valid
	List<Comment> comments = new ArrayList<Comment>();
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="patient_id", nullable=false)
	Patient owner;
	
	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
	List<Mammogram> mammograms = new ArrayList<Mammogram>();

    public static Finder<String,Study> find = new Finder<String,Study>(Play.application().configuration().getString("datasource"), String.class, Study.class);

    public static Study findById(Long id) {
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Patient getOwner() {
		return owner;
	}

	public void setOwner(Patient owner) {
		this.owner = owner;
	}

	public List<Mammogram> getMammograms() {
		return mammograms;
	}

	public void setMammograms(List<Mammogram> mammograms) {
		this.mammograms = mammograms;
	}
	
    public Promise<Void> asyncSave() {
        return Promise.promise(new Function0<Void>() {
            @Override
            public Void apply() throws Throwable {
                save();
                return null;
            }
        },DBExecutionContext.ctx);
    }
    //TODO this method...
    public String validate(){
    	return null;
    }
}
