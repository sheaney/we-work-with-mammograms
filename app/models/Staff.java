package models;

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
	Set<Patient> ownPatients;
	
	@OneToMany(mappedBy="commenter")
	Set<Comment> comments;
	
	@OneToMany(mappedBy="ownsPermission")
	Set<Permission> permissions;
	
	@OneToMany(mappedBy="annotator")
	Set<Annotation> annotations;
	
	@OneToMany(mappedBy="sharer")
	Set<SharedPatient> sharedPatients;

}
