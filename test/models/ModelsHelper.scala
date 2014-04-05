package models

import com.avaje.ebean.Ebean
import play.core.j.JavaGlobalSettingsAdapter
import play.api.test.FakeApplication
import helpers.TestSetup.testGlobalSettings
import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfterEach
import org.scalatest.Matchers

trait ModelsHelper extends FunSpec with BeforeAndAfterEach with Matchers {
  var app: FakeApplication = _

  override def beforeEach() {
    val globalSettings = new JavaGlobalSettingsAdapter(testGlobalSettings)
    app = FakeApplication(withGlobal = Some(globalSettings))
  }

  def rowCount: Int = Ebean.find(classOf[Patient]).findRowCount()

  def staffRowCount: Int = Ebean.find(classOf[Staff]).findRowCount()

  def getDBPatient: Patient = {
    val result = Ebean.find(classOf[Patient])
    val iterator = result.findIterate
    if (iterator.hasNext) iterator.next else null
  }

  def getDBStaff: Staff = {
    val result = Ebean.find(classOf[Staff])
    val iterator = result.findIterate
    if (iterator.hasNext) iterator.next else null
  }

  def comparePatients(patientOne: Patient, patientTwo: Patient) {

    patientOne.id should equal(patientTwo.id)

    // Get personal info
    val patientOnePersonalInfo = patientOne.getPersonalInfo
    val patientTwoPersonalInfo = patientTwo.getPersonalInfo

    patientTwoPersonalInfo.getName should equal(patientOnePersonalInfo.getName)
    patientTwoPersonalInfo.getFirstLastName should equal(patientOnePersonalInfo.getFirstLastName)
    patientTwoPersonalInfo.getSecondLastName should equal(patientOnePersonalInfo.getSecondLastName)

  }

}