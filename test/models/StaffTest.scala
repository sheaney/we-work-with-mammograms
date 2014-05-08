package models

import org.scalatest.Assertions._

import factories.Factories
import helpers.TestSetup.samplePatient
import helpers.TestSetup.sampleStaff
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import play.core.j.JavaGlobalSettingsAdapter

class StaffTest extends ModelsHelper with Factories {
  
  describe("Staff#findOwnPatient") {
    it("returns the patient if it exists") {
      running(app) {
        val staff = new Staff()
        val patient = new patientFactory { val id = 1L }.value
        val anotherPatient = new patientFactory { val id = 2L }.value
        val andAnotherPatient = new patientFactory { val id = 3L }.value
        staff.getOwnPatients().add(patient)
        staff.getOwnPatients().add(anotherPatient)
        staff.getOwnPatients().add(andAnotherPatient)

        //      assert(staff.findOwnPatient.getId() == patient.getId())
        staff.findOwnPatient(patient).getId() shouldBe (patient.getId())
      }
    }

    it("returns null if the patient does not exist") {
      running(app) {
        val staff = new Staff()
        val patient = new Patient()
        patient.setId(1L)

        staff.findOwnPatient(patient) shouldBe (null)
      }
    }

  }

  describe("Staff#findBorrowedPatient") {
    it("returns the borrowed patient if it exists") {
      running(app) {
        val sharer = new Staff
        val borrower = new Staff
        val sharedPatientInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, Integer.MAX_VALUE)

        borrower.getBorrowedPatients().add(sharedPatient);

        borrower.findBorrowedPatient(sharedPatientInstance).getId() shouldBe (sharedPatient.getId())
      }

    }

    it("returns null if the borrowed patient does not exist") {
      running(app) {
        val sharer = new Staff
        val borrower = new Staff
        val sharedPatientInstance = new patientFactory { val id = 1L }.value
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, Integer.MAX_VALUE)

        borrower.findBorrowedPatient(sharedPatientInstance) shouldBe (null)
      }
    }

  }

  describe("Saving a staff member to database") {

    ignore("test should fail") {
      it("fails retrieving first staff record that does not exist in DB") {
        running(app) {
          retrieveDbRecord[Staff]() { dbStaff =>
            dbStaff.delete()
          }
        }
      }
    }

    it("im memory staff has same info as db saved record") {
      running(app) {
        val staff = sampleStaff
        staff.appendOwnPatient(samplePatient);
        staff.save()
        rowCount[Staff] should equal(1)
        rowCount[Patient] should equal(1)

        retrieveDbRecord[Staff]() { dbStaff =>
          dbStaff.getName should equal(staff.getName)
          dbStaff.getFirstLastName should equal(staff.getFirstLastName)
          dbStaff.getOwnPatients.size should equal(1)

          val dbPatient = dbStaff.getOwnPatients.get(0)
          val patient = staff.getOwnPatients.get(0)

          comparePatients(dbPatient, patient)
        }
      }
    }

    it("updates staff attributes correctly") {
      running(app) {
        val staff = sampleStaff
        staff.save()
        rowCount[Staff] should equal(1)
        rowCount[Patient] should equal(0)

        retrieveDbRecord[Staff](deleteAfter = false) { dbStaff =>
          dbStaff.appendOwnPatient(samplePatient)
          dbStaff.update()
        }

        rowCount[Staff] should equal(1)
        rowCount[Patient] should equal(1)

        retrieveDbRecord[Staff]() { dbStaff =>
          val dbPatient = dbStaff.getOwnPatients.get(0)
          val patient = dbStaff.getOwnPatients.get(0)

          comparePatients(dbPatient, patient)
        }
      }
    }

    it("should validate a Staff's Login information") {
      running(app) {
        import play.api.Play.current
        val staff = sampleStaff
        staff.save()
        assert(Staff.authenticate(staff.getEmail, staff.getPassword) != null)
      }
    }

    describe("Staff#canSharePatient") {
      it("returns true if patient is among staff's own patients") {
        running(app) {
          val staff = new Staff
          val patient = new patientFactory { val id = 1L }.value
          val patientToShare = new patientFactory { val id = 1L }.value
          staff.getOwnPatients().add(patient)
          staff.canSharePatient(patientToShare) shouldBe true
        }
      }

      it("returns false if patient is not among staff's own patients") {
        running(app) {
          val staff = new Staff
          val patient = new patientFactory { val id = 1L }.value
          val patientToShare = new patientFactory { val id = 2L }.value
          staff.getOwnPatients().add(patient)
          staff.canSharePatient(patientToShare) shouldBe false
        }
      }
    }
  }
}
