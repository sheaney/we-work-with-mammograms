package integration

import org.scalatest.BeforeAndAfterAll
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.FunSpec
import play.api.test.TestServer
import org.scalatest.Matchers
import play.api.test.Helpers
import org.scalatest.selenium.WebBrowser
import play.api.test.FakeApplication
import play.core.j.JavaGlobalSettingsAdapter
import helpers.TestSetup.testGlobalSettings
import org.scalatest.GivenWhenThen
 
trait PlayBrowserSpec extends FunSpec with GivenWhenThen with BeforeAndAfterAll with Matchers with WebBrowser {
  
  implicit val webDriver: WebDriver = new HtmlUnitDriver
 
  val host = s"http://localhost:${Helpers.testServerPort}"
  var app: FakeApplication = _
  var server: TestServer = _
 
  override def beforeAll() {
    val globalSettings = new JavaGlobalSettingsAdapter(testGlobalSettings)
    app = FakeApplication(withGlobal = Some(globalSettings))
    server = TestServer(port = Helpers.testServerPort)
    server.start
  }
 
  override def afterAll() {
    server.stop
    close
    quit
  }  
  
}