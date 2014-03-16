package integration

import play.api.test.Helpers.running
import play.libs.WS
import play.test.Helpers._

class IntegrationScalaTest extends PlayBrowserSpec {
  describe("index") {
    it("displays login") {
      go to (host + "/")
      pageTitle should be("Iniciar Sesión")
    }
  }

}