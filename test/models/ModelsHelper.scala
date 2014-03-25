package models

import com.avaje.ebean.Ebean
import play.core.j.JavaGlobalSettingsAdapter
import play.api.test.FakeApplication
import helpers.TestSetup.testGlobalSettings
import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers

trait ModelsHelper extends FunSpec with BeforeAndAfterAll with Matchers {
  var app: FakeApplication = _

  override def beforeAll() {
    val globalSettings = new JavaGlobalSettingsAdapter(testGlobalSettings)
    app = FakeApplication(withGlobal = Some(globalSettings))
  }

  def rowCount: Int = Ebean.find(classOf[Patient]).findRowCount()

}