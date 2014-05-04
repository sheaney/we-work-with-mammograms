package lib;

import models.Patient;

public class OwnPatientContainer extends PatientContainer {

	private Patient patient;
	
	public OwnPatientContainer(Patient patient) {
		this.patient = patient;
	}
	
	public Patient getPatient() {
		return patient;
	}

	@Override
	public int getAccessPrivileges() {
		return Integer.MAX_VALUE;
	}
}
