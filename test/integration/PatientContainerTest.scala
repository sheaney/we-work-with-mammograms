package integration

import models.ModelsHelper
import factories.Factories
import play.api.test.Helpers.running
import factories._
import models.Patient
import models.Staff
import models.SharedPatient
import lib.{ PatientContainer, OwnPatientContainer, SharedPatientContainer }

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

  describe("PatientContainer#getPatientContainer") {
    it("returns intance of OwnPatientContainer if patient belongs to staff") {
      running(app) {
        val staff = new Staff
        val patient = new patientFactory { val id = 1L }.value
        staff.getOwnPatients().add(patient)
        PatientContainer.getPatientContainer(staff, patient) match {
          case _: OwnPatientContainer =>
          case _ => fail("Should return an instance of PatientContainer")
        }
      }
    }

    it("returns intance of SharedPatientContainer if patient has been borrowed by staff") {
      running(app) {
        val staff = new Staff
        val patient = new patientFactory { val id = 1L }.value
        val sharedPatient = newSharedPatient(1L, null, null, patient)
        staff.getBorrowedPatients().add(sharedPatient)
        PatientContainer.getPatientContainer(staff, patient) match {
          case _: SharedPatientContainer =>
          case _ => fail("Should return an instance of PatientContainer")
        }
      }
    }

    it("PatientContainer#isEmpty returns true if patient is not among staff's own or borrowed patients") {
      running(app) {
        val staff = new Staff
        val patient = new patientFactory { val id = 1L }.value
        val differentPatient = new patientFactory { val id = 2L }.value
        val anotherPatient = new patientFactory { val id = 3L }.value
        val sharedPatient = newSharedPatient(1L, null, null, differentPatient)
        staff.getOwnPatients().add(anotherPatient)
        staff.getBorrowedPatients().add(sharedPatient)
        PatientContainer.getPatientContainer(staff, patient).isEmpty shouldBe (true)
      }
    }
  }

  describe("PatientContainer#getAlreadySharedPatient") {
    def setupRelationships(sharer: Staff, borrower: Staff, patient: Patient, sharedPatient: SharedPatient) {
      sharer.getOwnPatients().add(patient)
      sharer.getSharedPatients().add(sharedPatient)
      patient.setOwner(sharer)

      borrower.getBorrowedPatients().add(sharedPatient)
      sharedPatient.setBorrower(borrower)

      patient.getSharedInstances().add(sharedPatient)
      sharedPatient.setSharedInstance(patient)
    }
    
    it("returns shared patient between sharer and borrower") {
      running(app) {
        val id = 1L
        val sharer = new staffFactory { val id = 1L }.value
        val borrower = new staffFactory { val id = 2L }.value
        val sharedInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        val sharedPatientToLookup = newSharedPatient(id, sharer, borrower, sharedInstance)
        setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

        PatientContainer.getAlreadySharedPatient(sharedPatientToLookup, sharer, borrower).getId shouldBe (sharedPatient.getId)
      }
    }

    describe("returns null when shared patient has not already been shared between sharer and borrower") {
      it("different sharers") {
        running(app) {
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
      }

      it("different borrowers") {
        running(app) {
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

    it("returns null when sharing a different patient between sharer and borrower") {
      val id = 1L
      val sharer = new staffFactory { val id = 1L }.value
      val borrower = new staffFactory { val id = 2L }.value
      val sharedInstance = new patientFactory { val id = 1L }.value
      val sharedPatient = newSharedPatient(id, sharer, borrower, sharedInstance)
      setupRelationships(sharer, borrower, sharedInstance, sharedPatient)

      val newId = 2L
      val anotherSharedInstance = new patientFactory { val id = 2L }.value
      val anotherSharedPatient = newSharedPatient(newId, sharer, borrower, anotherSharedInstance)

      PatientContainer.getAlreadySharedPatient(anotherSharedPatient, sharer, borrower) shouldBe (null)
    }


  }

}