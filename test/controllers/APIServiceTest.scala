package controllers

import integration.PlayBrowserSpec
import play.api.test.{Helpers, WsTestClient}
import models.ServiceAuth
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.{Json, JsArray, JsValue}
import java.util

/**
 * Created by fernando on 5/11/14.
 */
class APIServiceTest extends PlayBrowserSpec with WsTestClient {

  describe("listing all patient id's"){
    it("should be empty when no patients exist in the database"){
      Given("There is a valid service token in the database")
      val service = new ServiceAuth("email@email.com")
      service.save
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.API.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients").asInstanceOf[JsArray]

      val emptyArray = Json.toJson(Seq.empty[Long])
      patients should be(emptyArray)
      service.delete()
    }

    it("should return all the patients id's in the database"){
      Given("There are some patients in the database")
      //put some patients in the database
      And("There is a valid service token in the database")
      val service = new ServiceAuth("email@email.com")
      service.save
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.API.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients").toString()

    }
  }

  describe("Listing a patient's personal and medical info by id"){

  }

  describe(""){

  }
}
