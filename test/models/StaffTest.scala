package models

import factories.Factories
import integration.PlayBrowserSpec

class StaffTest extends PlayBrowserSpec with Factories {
  
  describe("Staff#findOwnPatient") {
    it("returns the patient if it exists") {
      val (staff, patient, anotherPatient, andAnotherPatient) = (sampleStaff, samplePatient, samplePatient, samplePatient)
      staff.getOwnPatients().add(patient)
      staff.getOwnPatients().add(anotherPatient)
      staff.getOwnPatients().add(andAnotherPatient)
      staff.save()

      val sameStaff = Staff.findById(staff.getId)

      sameStaff.findOwnPatient(patient).getId() shouldBe (patient.getId())
    }

    it("returns null if the patient does not exist") {
      val (staff, patient) = (sampleStaff, samplePatient)
      staff.save(); patient.save()

      staff.findOwnPatient(patient) shouldBe null
    }

  }

  describe("Staff#findBorrowedPatient") {
    it("returns the borrowed patient if it exists") {
      val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
      sharer.save(); borrower.save(); sharedPatientInstance.save()
      val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, Integer.MAX_VALUE)
      sharedPatient.save()

      borrower.getBorrowedPatients().add(sharedPatient)
      borrower.save()

      val sameBorrower = Staff.findById(borrower.getId())
      sameBorrower.findBorrowedPatient(sharedPatientInstance).getId() shouldBe sharedPatient.getId()
    }

    it("returns null if the borrowed patient does not exist") {
      val (borrower, sharedPatientInstance) = (sampleStaff, samplePatient)
      borrower.save(); sharedPatientInstance.save()
      borrower.findBorrowedPatient(sharedPatientInstance) shouldBe null
    }

  }

  describe("Saves staff with own patient") {
    it("successfully saves a staffs patient") {
        val (staff, patient) = (sampleStaff, samplePatient)

        staff.getOwnPatients.add(patient)
        staff.save()

        val sameStaff = Staff.findById(staff.getId)

        assert(sameStaff.getOwnPatients().get(0).getId() === patient.getId())
      }
    }
}
