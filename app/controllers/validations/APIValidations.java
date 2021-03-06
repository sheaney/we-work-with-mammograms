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
     * @param patient such patient
     * @return null if the Patient is non existing or a PatientContainer from the method PatientContainer.getPatientContainer()
     */
    public static PatientContainer getPatientAccess(Staff staff, Patient patient){
        // Validate that patient really does exist
        if (patient == null){
            return null;
        }
        return PatientContainer.getPatientContainer(staff, patient);

    }
}
