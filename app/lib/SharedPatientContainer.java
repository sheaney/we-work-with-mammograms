package lib;

import models.Patient;
import models.SharedPatient;

public class SharedPatientContainer extends PatientContainer {
	private SharedPatient sharedPatient;
	
	public SharedPatientContainer(SharedPatient sharedPatient) {
		this.sharedPatient = sharedPatient;
	}
	
	public SharedPatient getSharedPatient() {
		return sharedPatient;
	}

	@Override
	public int getAccessPrivileges() {
		return sharedPatient.getAccessPrivileges();
	}

}
