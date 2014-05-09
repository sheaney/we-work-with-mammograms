package controllers.validations;

import lib.PatientContainer;
import models.Patient;
import models.Staff;

/**
 * Created by fernando on 5/8/14.
 */
public class APIValidations {

    /**
     *
     * @param staff the staff that could own, borrow the patient, or neither
     * @param patientId such patient
     * @return null if the Patient is non existing or a PatientContainer from the method PatientContainer.getPatientContainer()
     */
    public static PatientContainer canAccessPatient(Staff staff, Long patientId){
        // Validate that patient really does exist
        Patient patient = Patient.findById(patientId);
        if (patient == null){
            return null;
        }
        return PatientContainer.getPatientContainer(staff, patient);

    }
}
