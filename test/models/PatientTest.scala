package models

import play.api.test.FakeApplication
import play.api.test.Helpers.running
import play.core.j.JavaGlobalSettingsAdapter

class PatientTest extends ModelsHelper {
  describe("Saving patient to database") {
    it("modifies row count by 1") {
      running(app) {
        val p = helpers.TestSetup.samplePatient
        p.save()
        rowCount[Patient] should equal(1)
        
        retrieveDbRecord[Patient]() { dbPatient =>
          dbPatient.getPersonalInfo.getName should equal(p.getPersonalInfo.getName)
        }

      }
    }
  }
}