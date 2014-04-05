package models

import play.api.test.FakeApplication
import play.api.test.Helpers.running
import play.core.j.JavaGlobalSettingsAdapter
import helpers.TestSetup.samplePatient
import helpers.TestSetup.sampleStaff

class StaffTest2 extends ModelsHelper {
  describe("Saving a staff member to database") {
    it("should have same staff info and patients") {
      running(app) {
        val s = sampleStaff
        s.appendOwnPatient(samplePatient);
        staffRowCount should equal(0)
        s.save()
        staffRowCount should equal(1)
        val dbStaff = getDBStaff
        dbStaff.getName should equal(s.getName)
        dbStaff.getFirstLastName should equal(s.getFirstLastName)
        dbStaff.getOwnPatients.size should equal(1)

        val dbPatient = dbStaff.getOwnPatients.get(0)
        val patient = s.getOwnPatients.get(0)

        comparePatients(dbPatient, patient)
      }
    }

    it("should update attributes") {
      running(app) {
        var staff = sampleStaff
        staff.save()
        staff = getDBStaff
        staff.appendOwnPatient(samplePatient)
        staff.update()
        val dbStaff = getDBStaff
        
        val dbPatient = dbStaff.getOwnPatients.get(0)
        val patient = staff.getOwnPatients.get(0)
        
        comparePatients(dbPatient,patient)
      }
    }
  }
}