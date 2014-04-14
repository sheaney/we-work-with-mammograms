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
import helpers.TestSetup.testDbSettings
import org.scalatest.GivenWhenThen
import play.api.Configuration
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import play.api.GlobalSettings
import java.io.File
import play.api.Mode

trait PlayBrowserSpec extends FunSpec with GivenWhenThen with BeforeAndAfterAll with Matchers with WebBrowser {

  implicit val webDriver: WebDriver = new HtmlUnitDriver

  val host = s"http://localhost:${Helpers.testServerPort}"
  val defaultConfig = Configuration(ConfigFactory.load("application.conf"))
  val testConfig = Configuration.from(Map() ++ testDbSettings.asScala)
  val globalConfig = defaultConfig ++ testConfig
  var app: FakeApplication = _
  var server: TestServer = _

  def globalSettings: GlobalSettings = {
    new GlobalSettings {
      override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
        super.onLoadConfig(globalConfig, path, classloader, mode)
      }
    }
  }

  override def beforeAll() {
    val gs = globalSettings
    app = FakeApplication(withGlobal = Some(gs))
    server = TestServer(port = Helpers.testServerPort, application = app)
    server.start
  }

  override def afterAll() {
    server.stop
    close
    quit
  }

}