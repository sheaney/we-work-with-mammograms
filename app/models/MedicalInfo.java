package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class MedicalInfo extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	Long id;
	
	@Required 
	int sexualActivityStartAge;
	
	@Required 
	int pregnancies;
	
	@Required 
	int cSections;
	
	@Required 
	int naturalDeliveries;
	
	@Required 
	int abortions;
	
	@Required 
	int menopauseStartAge;
	
	@Required 
	boolean familyPredisposition;
	
	@Required 
	boolean hormonalReplacementTherapy;
	
	@Required 
	boolean previousMammaryDiseases;
	
	@Required 
	int menstrualPeriodStartAge;
	
	@Required 
	boolean breastfedChildren;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSexualActivityStartAge() {
		return sexualActivityStartAge;
	}

	public void setSexualActivityStartAge(int sexualActivityStartAge) {
		this.sexualActivityStartAge = sexualActivityStartAge;
	}

	public int getPregnancies() {
		return pregnancies;
	}

	public void setPregnancies(int pregnancies) {
		this.pregnancies = pregnancies;
	}

	public int getcSections() {
		return cSections;
	}

	public void setcSections(int cSections) {
		this.cSections = cSections;
	}

	public int getNaturalDeliveries() {
		return naturalDeliveries;
	}

	public void setNaturalDeliveries(int naturalDeliveries) {
		this.naturalDeliveries = naturalDeliveries;
	}

	public int getAbortions() {
		return abortions;
	}

	public void setAbortions(int abortions) {
		this.abortions = abortions;
	}

	public int getMenopauseStartAge() {
		return menopauseStartAge;
	}

	public void setMenopauseStartAge(int menopauseStartAge) {
		this.menopauseStartAge = menopauseStartAge;
	}

	public boolean isFamilyPredisposition() {
		return familyPredisposition;
	}

	public void setFamilyPredisposition(boolean familyPredisposition) {
		this.familyPredisposition = familyPredisposition;
	}

	public boolean isHormonalReplacementTherapy() {
		return hormonalReplacementTherapy;
	}

	public void setHormonalReplacementTherapy(boolean hormonalReplacementTherapy) {
		this.hormonalReplacementTherapy = hormonalReplacementTherapy;
	}

	public boolean isPreviousMammaryDiseases() {
		return previousMammaryDiseases;
	}

	public void setPreviousMammaryDiseases(boolean previousMammaryDiseases) {
		this.previousMammaryDiseases = previousMammaryDiseases;
	}

	public int getMenstrualPeriodStartAge() {
		return menstrualPeriodStartAge;
	}

	public void setMenstrualPeriodStartAge(int menstrualPeriodStartAge) {
		this.menstrualPeriodStartAge = menstrualPeriodStartAge;
	}

	public boolean isBreastfedChildren() {
		return breastfedChildren;
	}

	public void setBreastfedChildren(boolean breastfedChildren) {
		this.breastfedChildren = breastfedChildren;
	}
	
}
