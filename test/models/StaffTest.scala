package models

import play.api.test.FakeApplication
import play.api.test.Helpers.running
import play.core.j.JavaGlobalSettingsAdapter
import helpers.TestSetup.samplePatient
import helpers.TestSetup.sampleStaff

class StaffTest extends ModelsHelper {
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

  }
}