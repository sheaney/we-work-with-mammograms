package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class MedicalInfo extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	Long id;
}
