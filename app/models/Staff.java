package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Staff extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@OneToMany(mappedBy="owner")
	Set<Patient> ownPatients = new HashSet<Patient>();
	
	@OneToMany(mappedBy="commenter")
	Set<Comment> comments = new HashSet<Comment>();
	
	@OneToMany(mappedBy="ownsPermission")
	Set<Permission> permissions = new HashSet<Permission>();
	
	@OneToMany(mappedBy="annotator")
	Set<Annotation> annotations = new HashSet<Annotation>();
	
	@OneToMany(mappedBy="sharer")
	Set<SharedPatient> sharedPatients = new HashSet<SharedPatient>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Patient> getOwnPatients() {
		return ownPatients;
	}

	public void setOwnPatients(Set<Patient> ownPatients) {
		this.ownPatients = ownPatients;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	public Set<SharedPatient> getSharedPatients() {
		return sharedPatients;
	}

	public void setSharedPatients(Set<SharedPatient> sharedPatients) {
		this.sharedPatients = sharedPatients;
	}

}
