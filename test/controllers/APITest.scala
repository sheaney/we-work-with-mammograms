package controllers

import integration.{UserLogin, PlayBrowserSpec}
import play.api.test.{TestBrowser, WsTestClient}
import models.Staff
import scala.concurrent.Await
import scala.concurrent.duration._
import controllers._
import org.openqa.selenium.WebDriver

/**
 * Created by fernando on 5/8/14.
 */
class APITest extends PlayBrowserSpec with UserLogin with WsTestClient{

  describe("Get JSON node with patient information"){
    it("should return notFound error if the patient doesn't exist"){
      Given("There is a logged in staff member")
      var staff = login[Staff]
      When("trying to access with an invalid id")
      go to (host + controllers.routes.API.getPatientInfo(-1).url)
      pageSource should include ("NOT_FOUND")

      val testBrowser = new TestBrowser(webDriver,Option(host))

      testBrowser.goTo(controllers.routes.API.getPatientInfo(-1).url)

      /*val client = wsCall(controllers.routes.API.getPatientInfo(-1))(port = 9000)
      val futureResponse = client.get()
      Then("The server's response status should be equal to notfound (404)")
      val response = Await.result(futureResponse,Duration.Inf)
      response.status shouldBe(404)*/

      staff.delete
      //logout(staff)

    }

    it("should return forbidden if the patient is neither owned or borrowed by the staff member"){

    }

    it("should return a json node with the patient's information displaying it depending on the access privileges of the staff member"){

    }
  }
}
