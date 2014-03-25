package models

import helpers.TestSetup.testGlobalSettings
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import play.core.j.JavaGlobalSettingsAdapter

class PatientTest extends ModelsHelper {
  describe("Saving patient to database") {
    it("modifies row count by 1") {
      running(app) {
        val p = new Patient
        p.save()
        rowCount should equal(1)
      }
    }
  }
}