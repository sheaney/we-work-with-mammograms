package controllers

import integration.PlayBrowserSpec
import play.api.test.{Helpers, WsTestClient}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Json
import factories.Factories
import play.api.http.Status

/**
 * Created by fernando on 5/11/14.
 */
class APIServiceTest extends PlayBrowserSpec with WsTestClient with Factories {

  describe("listing all patient id's"){
    it("should be empty when no patients exist in the database"){
      Given("There is a valid service token in the database")
      val service = sampleService
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.ServiceAPI.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients")
      val emptyArray = Json.toJson(Seq.empty[Long])

      patients should be(emptyArray)
    }

    it("should return all the patients id's in the database"){
      Given("There are some patients in the database")
      //put a patient in the database
      val patient = samplePatient
      patient.save
      And("There is a valid service token in the database")
      val service = sampleService
      Then("The external service makes a request for patient id's with the correct token on it's request header")
      val request = wsCall(controllers.routes.ServiceAPI.getPatients)(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val patients = (response.json \ "Patients")

      patients(0).as[Long] should be(patient.getId())
    }
  }

  describe("Listing a patient's personal and medical info by id"){
    it("should return a patient's personal info when ?info=personal"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=personal")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"personal"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val personalInfo = response.json \ "personalInfo"

      personalInfo should not be null
    }

    it("should return a patient's medical info when ?info=medical"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=medical")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"medical"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val medicalInfo = response.json \ "medicalInfo"

      medicalInfo should not be null
    }

    it("should return a patient's both personal and medical info when the ?info parameter is not given"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/patient/:id")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,null))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)
      val personalInfo = response.json \ "personalInfo"
      val medicalInfo = response.json \ "medicalInfo"

      personalInfo should not be null
      medicalInfo should not be null
    }

    it("should return a HTTP BAD REQUEST status when the ?info parameter is something undefined"){
      Given("There is a patient in the db")
      val patient = samplePatient
      patient.save
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/patient/:id?info=\"not defined\"")
      val request = wsCall(controllers.routes.ServiceAPI.getServicePatient(patient.getId,"not defined"))(Helpers.testServerPort).withHeaders(("Authorization",service.getAuthToken))
      val response = Await.result(request.get(),Duration.Inf)

      response.status should be(Status.BAD_REQUEST)
    }
  }

  describe("Getting a study with it's id"){
    it("should return an ok status when the study exists") {//and also return the corresponding json response
      Given("There is a study in the db")
      val study = sampleStudy
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/study/:id")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceStudy(study.getId))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.OK)
    }

    it("should return an not found status when the study doesn't exist"){
      Given("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/study/-1")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceStudy(-1))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.NOT_FOUND)
    }
  }

  describe("Getting a comment with it's id"){
    it("should return an ok status when the comment exists"){ //and also return the corresponding json response
      Given("there is a study with some comments")
      val study = sampleStudy
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/comments/:id")
      val comment = study.getComments.get(0)
      val request = wsCall(controllers.routes.ServiceAPI.getServiceComment(comment.getId))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.OK)
    }

    it("should return a not found status when the comment doesn't exist"){
      Given("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/comments/-1")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceComment(-1))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.NOT_FOUND)
    }
  }

  describe("Getting a Mammogram with it's id"){
    it("should return an ok status when the mammogram exists"){ //and also return the corresponding json response
      Given("There is a study with some mammograms")
      val study = sampleStudy
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/mammogram/:id")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceMammogram(study.getMammograms.get(0).getId))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.OK)
    }

    it("should return a not found status when the mammogram doesn't exist"){
     Given("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/mammogram/-1")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceMammogram(-1))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.NOT_FOUND)
    }
  }

  describe("Getting an Annotation with ir's id"){
    it("should return an ok status when the annotation exists"){//and also return the corresponding json response
      Given("There is a study with some mammograms that have some annotations within")
      val study = sampleStudy
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/annotation/:id")
      val annotation = study.getMammograms.get(0).getAnnotations.get(0)
      val request = wsCall(controllers.routes.ServiceAPI.getServiceAnnotation(annotation.getId))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.OK)
    }

    it("should return a not found status when the annotation doesn't exist"){
      Given("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/annotation/-1")
      val request = wsCall(controllers.routes.ServiceAPI.getServiceAnnotation(-1))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.NOT_FOUND)
    }
  }

  describe("Getting a patient's full layout given its id"){
    it("should return an ok status when the patient exists"){
      Given("you have some content in the db")
      val study = sampleStudy
      And("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/schema/:id")
      val patientId = study.getOwner.getId
      val request = wsCall(controllers.routes.ServiceAPI.getSchema(patientId))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.OK)
    }
    it("should return a not found status when the patient doesn't exist"){
      Given("the requester has a valid service token")
      val service = sampleService
      Then("The external service makes a request to the uri -> api/service/schema/-1")
      val request = wsCall(controllers.routes.ServiceAPI.getSchema(-1))(Helpers.testServerPort).withHeaders(("Authorization", service.getAuthToken))
      val response = Await.result(request.get(), Duration.Inf)

      response.status should be(Status.NOT_FOUND)
    }
  }
}
