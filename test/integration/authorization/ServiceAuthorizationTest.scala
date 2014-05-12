package integration.authorization

import integration.PlayBrowserSpec
import models.ServiceAuth
import play.api.test.{Helpers, WsTestClient}
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import play.api.http.Status

/**
 * Created by fernando on 5/9/14.
 */
class ServiceAuthorizationTest extends PlayBrowserSpec with WsTestClient{

  describe("Service accesses to the API") {
    it("should return HTTP OK status when having correct credentials"){
      Given("The external service has an authToken that exists in the database")
      val service = new ServiceAuth("email@email.com")
      service.save
      Then("The external service makes a request with the correct token on it's request header")
      val request = wsCall(controllers.routes.TestServiceController.test)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      response.status should be (Status.OK)
      service.delete()
    }

    it("should return HTTP UNAUTHORIZED status having incorrect credentials"){
      val request = wsCall(controllers.routes.TestServiceController.test)(Helpers.testServerPort).withHeaders(("Authorization","Invalid credentials"))
      val response = Await.result(request.get(),Duration.Inf)
      response.status should be (Status.UNAUTHORIZED)
    }

    it("should also return HTTP UNAUTHORIZED status when no credentials are given in the request header"){
      val request = wsCall(controllers.routes.TestServiceController.test)(Helpers.testServerPort)
      val response = Await.result(request.get(),Duration.Inf)
      response.status should be (Status.UNAUTHORIZED)
    }
  }
}
