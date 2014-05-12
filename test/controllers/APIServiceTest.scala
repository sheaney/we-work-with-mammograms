package controllers

import integration.PlayBrowserSpec
import play.api.test.{Helpers, WsTestClient}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Json
import factories.Factories

/**
 * Created by fernando on 5/11/14.
 */
class APIServiceTest extends PlayBrowserSpec with WsTestClient with Factories {

  describe("listing all patient id's"){
    it("should be empty when no patients exist in the database"){
      Given("There is a valid service token in the database")
      val service = sampleService
      service.save
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.ServiceAPI.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients")

      val emptyArray = Json.toJson(Seq.empty[Long])
      patients should be(emptyArray)
      service.delete()
    }

    it("should return all the patients id's in the database"){
      Given("There are some patients in the database")
      //put some patients in the database
      val patient = samplePatient
      patient.save
      And("There is a valid service token in the database")
      val service = sampleService
      service.save
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.ServiceAPI.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients")

      patients(0).as[Long] should be(patient.getId())

      service.delete()
      patient.delete()
    }
  }

  describe("Listing a patient's personal and medical info by id"){
    it("should return a patient's personal info when ?info=personal"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      service.save
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=personal")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"personal"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val personalInfo = response.json \ "personalInfo"

      personalInfo should not be null

      service.delete
      patient.delete
    }

    it("should return a patient's medical info when ?info=medical"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      service.save
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=medical")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"medical"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val medicalInfo = response.json \ "medicalInfo"

      medicalInfo should not be null

      service.delete
      patient.delete
    }

    it("should return a patient's both personal and medical info when the ?info parameter is not given"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      service.save
      Then("The external service makes a request to the uri -> api/service/patient/:id")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,null))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val personalInfo = response.json \ "personalInfo"
      val medicalInfo = response.json \ "medicalInfo"

      personalInfo should not be null
      medicalInfo should not be null

      service.delete
      patient.delete
    }

    it("should return a HTTP BAD REQUEST status when the ?info parameter is something undefined"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      service.save
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=\"not defined\"")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"not defined"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)

      response.status should be(400)

      service.delete
      patient.delete
    }
  }

  describe(""){

  }
}
