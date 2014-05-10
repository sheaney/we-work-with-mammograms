package controllers

import integration.{ UserLogin, PlayBrowserSpec }
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import models.Staff
import scala.concurrent.Await
import scala.concurrent.duration._
import controllers._
import org.openqa.selenium.WebDriver
import models.Patient
import factories.Factories
import helpers.TestSetup.samplePatient
import helpers.TestSetup.sampleStaff
import models.SharedPatient

/**
 * Created by fernando on 5/8/14.
 */
class APITest extends PlayBrowserSpec with UserLogin with Factories {
  def createFakeRequest(method: String, url: String, session: Map[String, String]) = {
    FakeRequest(method, url).withSession(session.toSeq: _*)
  }

  describe("Get JSON node with patient information") {

    def getPatientUrl(id: Long): String = {
      controllers.routes.API.getPatientInfo(id).url
    }

    it("should return notFound error if the patient doesn't exist") {
      val session = Map("id" -> "1", "timeOfLogin" -> System.currentTimeMillis().toString(), "type" -> "STAFF", "user" -> "Juanito")
      val fakeRequest = createFakeRequest(GET, getPatientUrl(-1), session)
      val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
      status(result) shouldBe (404)
    }

    it("should return forbidden if the patient is neither owned or borrowed by the staff member") {
      val staff = sampleStaff
      val patient = samplePatient

      patient.save()
      staff.save()

      val session = Map(
        "id" -> staff.getId().toString(),
        "timeOfLogin" -> System.currentTimeMillis().toString(),
        "type" -> "STAFF",
        "user" -> "Juanito")
      val fakeRequest = createFakeRequest(GET, getPatientUrl(patient.getId()), session)
      val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
      status(result) shouldBe (403)

      patient.delete()
      staff.delete()

    }

    describe("When patient is own or borrowed") {
      it("should return a json node with the patient's information if it's owned by the staff") {
        val staff = sampleStaff
        val patient = samplePatient

        staff.save()

        patient.setOwner(staff)
        patient.save()

        val session = Map(
          "id" -> staff.getId().toString(),
          "timeOfLogin" -> System.currentTimeMillis().toString(),
          "type" -> "STAFF",
          "user" -> "Juanito")
        val fakeRequest = createFakeRequest(GET, getPatientUrl(patient.getId()), session)
        val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
        status(result) shouldBe (200)

        staff.delete()

      }

      it("should return a json node with the patient's information depending on the privileges if it's borrowed by the staff") {
        val sharer = sampleStaff
        val borrower = sampleStaff
        val sharedPatientInstance = new patientFactory { val id = 1L }.value
        sharer.save(); borrower.save(); sharedPatientInstance.save()
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, Integer.MAX_VALUE)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)

        val session = Map(
          "id" -> borrower.getId().toString(),
          "timeOfLogin" -> System.currentTimeMillis().toString(),
          "type" -> "STAFF",
          "user" -> "Juanito")
        val fakeRequest = createFakeRequest(GET, getPatientUrl(sharedPatient.getId()), session)
        val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
        status(result) shouldBe (200)

        sharer.delete()
      }
    }
  }
}
