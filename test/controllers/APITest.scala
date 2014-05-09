package controllers

import integration.{UserLogin, PlayBrowserSpec}
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
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
      val fakeRequest = FakeRequest("GET","/api/patient/-1/info")
      val session = Map("id"->"1","timeOfLogin"-> System.currentTimeMillis().toString(),"type"->"STAFF","user"->"Juanito")
      val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
      status(result) shouldBe(404)
    }

    it("should return forbidden if the patient is neither owned or borrowed by the staff member"){

    }

    it("should return a json node with the patient's information displaying it depending on the access privileges of the staff member"){

    }
  }
}
