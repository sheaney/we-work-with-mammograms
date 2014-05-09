package lib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Patient;
import models.SharedPatient;
import models.Staff;

public abstract class PatientContainer {

	public abstract int getAccessPrivileges();

	/**
	 * Useful for finding out if a patient is a staff's own patient or if the
	 * patient is a borrowed patient
	 * 
	 * @param staff
	 *            member which we are interested in
	 * @param patient
	 *            asked about
	 * @return instance of OwnPatientContainer, SharedPatientContainer or EmptyPatientContainer
	 *         if patient is staff's own, borrowed, or none respectively
	 */
	public static PatientContainer getPatientContainer(Staff staff,
			Patient patient) {
		Patient own = staff.findOwnPatient(patient);
		if (own != null) {
			return new OwnPatientContainer(own);
		}
		
		SharedPatient borrowed = staff.findBorrowedPatient(patient);
		if (borrowed != null) {
			return new SharedPatientContainer(borrowed);
		}

		return new EmptyPatientContainer();
	}

	/**
	 * Get already shared patient between a sharer and a borrower
	 * 
	 * @param patient
	 *            SharedPatient
	 * @param sharer
	 *            Staff
	 * @param borrower
	 *            Staff
	 * @return SharedPatient or null if no shared patient exists between sharer
	 *         and borrower
	 */
	public static SharedPatient getAlreadySharedPatient(SharedPatient patient,
			Staff sharer, Staff borrower) {
		// Ids
		long patientSharerId = patient.getSharer().getId();
		long patientBorrowerId = patient.getBorrower().getId();
		SharedPatient existingSharedPatient = null;

		// Lookup if patient is among sharer's shared patients
		boolean sharedBySharer = false;
		List<SharedPatient> sharedPatients = sharer.getSharedPatients();
		for (SharedPatient shared : sharedPatients) {
			sharedBySharer = shared.getSharer().getId() == patientSharerId;
			if (sharedBySharer) {
				break;
			}
		}

		if (!sharedBySharer)
			return null;

		// Lookup if patient is among borrower's borrowed patients
		boolean borrowedByBorrower = false;
		List<SharedPatient> borrowedPatients = borrower.getBorrowedPatients();
		for (SharedPatient borrowed : borrowedPatients) {
			borrowedByBorrower = borrowed.getBorrower().getId() == patientBorrowerId;
			if (borrowedByBorrower) {
				existingSharedPatient = borrowed;
				break;
			}
		}

		return existingSharedPatient;
	}

    /**
     * Returns true if the container is empty
     *
     * @return if the patient attribute is null or if called from an EmptyPatientContainer
     **/
    public abstract boolean isEmpty();

}
