package integration

import models.ModelsHelper
import factories.Factories
import play.api.test.Helpers.running
import factories._
import models.Patient
import models.Staff
import models.SharedPatient
import lib.PatientContainer

class PatientContainerTest extends ModelsHelper with Factories {
  def newSharedPatient(spId: Long, s: Staff, b: Staff, si: Patient): SharedPatient = {
    new sharedPatientFactory {
      val id = spId
      val sharer = s
      val borrower = b
      val sharedInstance = si
      val accessPrivileges = Integer.MAX_VALUE
    }.value
  }

  def setupRelationships(sharer: Staff, borrower: Staff, patient: Patient, sharedPatient: SharedPatient) {
    sharer.getOwnPatients().add(patient)
    sharer.getSharedPatients().add(sharedPatient)
    patient.setOwner(sharer)

    borrower.getBorrowedPatients().add(sharedPatient)
    sharedPatient.setBorrower(borrower)

    patient.getSharedInstances().add(sharedPatient)
    sharedPatient.setSharedInstance(patient)
  }

  describe("PatientContainer#getAlreadySharedPatient") {
    it("returns existing shared patient between sharer and borrower") {
      running(app) {
        val id = 1L
        val sharer = new staffFactory { val id = 1L }.value
        val borrower = new staffFactory { val id = 2L }.value
        val sharedInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        val sharedPatientToLookup = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        PatientContainer.getAlreadySharedPatient(sharedPatientToLookup, sharer, borrower) shouldBe (sharedPatient)
      }
    }

    describe("returns false when shared patient has not already been shared between sharer and borrower") {
      it("different sharers") {
        val id = 1L
        val sharer = new staffFactory { val id = 1L }.value
        val borrower = new staffFactory { val id = 2L }.value
        val sharedInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        val anotherSharer = new staffFactory { val id = 2L }.value // different sharer
        val sameBorrower = new staffFactory { val id = 2L }.value
        val sameSharedInstance = new patientFactory { val id = 1L }.value
        val sameSharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        val sharedPatientToLookup = newSharedPatient(id, anotherSharer, sameBorrower, sameSharedInstance)
        setupRelationships(anotherSharer, sameBorrower, sameSharedInstance, sameSharedPatient)

        PatientContainer.getAlreadySharedPatient(sharedPatientToLookup, sharer, borrower) shouldBe (null)
      }
      
      it("different borrowers") {
        val id = 1L
        val sharer = new staffFactory { val id = 1L }.value
        val borrower = new staffFactory { val id = 2L }.value
        val sharedInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        val sameSharer = new staffFactory { val id = 1L }.value
        val anotherBorrower = new staffFactory { val id = 3L }.value // different borrower
        val sameSharedInstance = new patientFactory { val id = 1L }.value
        val sameSharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        val sharedPatientToLookup = newSharedPatient(id, sameSharer, anotherBorrower, sameSharedInstance)
        setupRelationships(sameSharer, anotherBorrower, sameSharedInstance, sameSharedPatient)

        PatientContainer.getAlreadySharedPatient(sharedPatientToLookup, sharer, borrower) shouldBe (null)
      }

    }

  }

}