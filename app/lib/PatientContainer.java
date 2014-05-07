package lib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Patient;
import models.SharedPatient;
import models.Staff;

public abstract class PatientContainer {

	public abstract int getAccessPrivileges();

	public static PatientContainer getPatientContainer(Staff staff, Patient patient) {
		Long patientId = patient.getId();
		List<Patient> ownPatients = staff.getOwnPatients();
		List<SharedPatient> borrowedPatients = staff.getBorrowedPatients();

		Set<Long> ownPatientIds = getOwnPatientIds(ownPatients);
		Set<Long> borrowedPatientIds = getBorrowedPatientIds(borrowedPatients);

		if (ownPatientIds.contains(patientId)) {
			Patient own = getFromOwnPatients(patientId, ownPatients);
			return new OwnPatientContainer(own);
		} else if (borrowedPatientIds.contains(patientId)) {
			SharedPatient borrowed = getFromBorrowedPatients(patientId,
					borrowedPatients);
			return new SharedPatientContainer(borrowed);
		}

		return null;
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

	private static Set<Long> getBorrowedPatientIds(
			List<SharedPatient> borrowedPatients) {
		Set<Long> ids = new HashSet<Long>();

		for (SharedPatient borrowedPatient : borrowedPatients) {
			Patient borrowed = borrowedPatient.getSharedInstance();
			ids.add(borrowed.getId());
		}

		return ids;
	}

	private static Set<Long> getOwnPatientIds(List<Patient> ownPatients) {
		Set<Long> ids = new HashSet<Long>();

		for (Patient ownPatient : ownPatients) {
			ids.add(ownPatient.getId());
		}

		return ids;
	}

	private static Patient getFromOwnPatients(Long id, List<Patient> ownPatients) {
		for (Patient ownPatient : ownPatients) {
			if (ownPatient.getId().equals(id)) {
				return ownPatient;
			}
		}
		return null;
	}

	private static SharedPatient getFromBorrowedPatients(Long id,
			List<SharedPatient> borrowedPatients) {
		for (SharedPatient borrowedPatient : borrowedPatients) {
			Patient borrowed = borrowedPatient.getSharedInstance();
			if (borrowed.getId().equals(id)) {
				return borrowedPatient;
			}
		}
		return null;
	}
}
