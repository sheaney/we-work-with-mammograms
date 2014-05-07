package lib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Patient;
import models.SharedPatient;
import models.Staff;

public abstract class PatientContainer {

	public abstract int getAccessPrivileges();

	public static PatientContainer getPatientContainer(Staff staff,
			Long patientId) {
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
	 * Find out if a SharedPatient has already been shared between a sharer and a borrower
	 * 
	 * @param patient SharedPatient
	 * @param sharer Staff
	 * @param borrower Staff
	 * @return true | false depending if shared patient already was shared
	 */
	public static boolean hasAlreadySharedThePatient(
			SharedPatient patient, Staff sharer, Staff borrower) {

		boolean hasSameSharer = patient.getSharer().getId() == sharer.getId();
		boolean hasSameBorrower = patient.getBorrower().getId() == borrower
				.getId();
		
		return hasSameSharer && hasSameBorrower;
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
