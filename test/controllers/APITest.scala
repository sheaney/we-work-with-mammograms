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
import play.api.libs.json.Json
import play.api.http.Status
import lib.permissions._

/**
 * Created by fernando on 5/8/14.
 */
class APITest extends PlayBrowserSpec with UserLogin with Factories {

  def createFakeRequest(call: Call, session: Map[String, String]) = {
    FakeRequest(call.method, call.url).withSession(session.toSeq: _*)
  }

  describe("Staff is logged into the system") {
    def createSession(staff: Option[Staff] = None): Map[String, String] = {
      val staffId = staff match {
        case Some(staff) => staff.getId.toString
        case None => "1"
      }

      Map(
        "id" -> staffId,
        "timeOfLogin" -> System.currentTimeMillis().toString(),
        "type" -> "STAFF",
        "user" -> "Juanito")
    }

    describe("Get JSON node with patient information") {

      def getPatientUrl(id: Long): Call = {
        controllers.routes.API.getPatientInfo(id)
      }

      it("should return notFound error if the patient doesn't exist") {
        val session = createSession()
        val fakeRequest = createFakeRequest(getPatientUrl(-1), session)
        val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
        status(result) shouldBe (Status.NOT_FOUND)
      }

      it("should return forbidden if the patient is neither owned or borrowed by the staff member") {
        val staff = sampleStaff
        val patient = samplePatient

        patient.save()
        staff.save()

        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(getPatientUrl(patient.getId()), session)
        val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
        status(result) shouldBe (Status.FORBIDDEN)

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

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(getPatientUrl(patient.getId()), session)
          val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
          status(result) shouldBe (Status.OK)

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

          val session = createSession(Some(borrower))
          val fakeRequest = createFakeRequest(getPatientUrl(sharedPatientInstance.getId()), session)
          val Some(result) = route(fakeRequest.withSession(session.toSeq: _*))
          status(result) shouldBe (Status.OK)

          sharer.delete()
        }
      }
    }

    describe("Updates Personal Info of a patient") {

      def updatePersonalInfoUrl(id: Long): Call = {
        controllers.routes.API.updatePersonalInfo(id)
      }

      it("Patient doesn't exist") {
        val session = createSession()
        val fakeRequest = createFakeRequest(updatePersonalInfoUrl(-1), session)
        val fakeRequestWithSession = fakeRequest.withSession(session.toSeq: _*)
        val json = Json.toJson(Map("foo" -> "bar"))
        val fakeRequestWithJson = fakeRequest.withJsonBody(json)
        val Some(result) = route(fakeRequestWithJson)
        status(result) shouldBe (Status.NOT_FOUND)

      }

      it("Tries to update patient information of a patient that doesn't belong to him/her") {
        val patient = samplePatient
        val staff = sampleStaff

        patient.save()
        staff.save()

        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(updatePersonalInfoUrl(patient.getId()), session)
        val json = Json.toJson(Map("foo" -> "bar"))
        val fakeRequestWithJson = fakeRequest.withJsonBody(json)
        val Some(result) = route(fakeRequestWithJson)
        status(result) shouldBe (Status.UNAUTHORIZED)

        staff.delete()

      }

      def personalInfoJson(patient: Patient, correct: Boolean = true) = {
        def successfulPersonalInfoJson() = {
          val personal_fields =
            Map("id" -> patient.getId().toString(),
              "name" -> (if (correct) "Maria" else ""),
              "firstLastName" -> "Dávalos",
              "secondLastName" -> "González",
              "address" -> "Bosque Mágico 123, Col. Robledo, Mty, N.L.",
              "email" -> "mdavalos@xyz.com",
              "telephone" -> "85234725",
              "birthdate" -> "21/02/1978")
          Json.toJson(personal_fields)
        }
        successfulPersonalInfoJson()
      }

      describe("Updates own patient") {
        it("Tries to updates patient information with missing fields") {
          val patient = samplePatient
          val staff = sampleStaff

          staff.save()
          patient.setOwner(staff)
          patient.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(updatePersonalInfoUrl(patient.getId()), session)
          val json = personalInfoJson(patient, correct = false)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.BAD_REQUEST)

          staff.delete()

        }

        it("Updates patient information successfully") {

          val patient = samplePatient
          val staff = sampleStaff

          staff.save()
          patient.setOwner(staff)
          patient.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(updatePersonalInfoUrl(patient.getId()), session)
          val fakeRequestWithSession = fakeRequest.withSession(session.toSeq: _*)

          val json = personalInfoJson(patient, correct = true)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.OK)

          staff.delete()

        }
      }

      describe("Updates borrowed patient") {

        it("Doesn't have access privileges") {

          val sharer = sampleStaff
          val borrower = sampleStaff
          val sharedPatientInstance = new patientFactory { val id = 1L }.value
          sharer.save(); borrower.save(); sharedPatientInstance.save()
          val updatePersonalInfoPermission = false
          val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfoPermission, false, false)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()

          val session = createSession(Some(borrower))
          val fakeRequest = createFakeRequest(updatePersonalInfoUrl(sharedPatientInstance.getId()), session)

          val json = personalInfoJson(sharedPatientInstance, correct = true)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.UNAUTHORIZED)

          sharer.delete()

        }

        describe("Has access privileges") {

          it("Tries to update patient information with missing fields") {

            val sharer = sampleStaff
            val borrower = sampleStaff
            val sharedPatientInstance = new patientFactory { val id = 1L }.value
            sharer.save(); borrower.save(); sharedPatientInstance.save()
            val updatePersonalInfoPermission = true
            val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfoPermission, false, false)
            val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
            sharedPatient.save()

            borrower.getBorrowedPatients().add(sharedPatient)
            borrower.update()

            val session = createSession(Some(borrower))
            val fakeRequest = createFakeRequest(updatePersonalInfoUrl(sharedPatientInstance.getId()), session)
            val json = personalInfoJson(sharedPatientInstance, correct = false)
            val fakeRequestWithJson = fakeRequest.withJsonBody(json)
            val Some(result) = route(fakeRequestWithJson)
            status(result) shouldBe (Status.BAD_REQUEST)

            sharer.delete()

          }

//          it("Updates patient information successfully") {
//
//            val sharer = sampleStaff
//            val borrower = sampleStaff
//            val sharedPatientInstance = new patientFactory { val id = 1L }.value
//            sharer.save(); borrower.save(); sharedPatientInstance.save()
//            val updatePersonalInfoPermission = true
//            val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfoPermission, false, false)
//            val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
//            sharedPatient.save()
//
//            borrower.getBorrowedPatients().add(sharedPatient)
//            borrower.update()
//            val patientId = sharedPatientInstance.getId()
//
//            val session = createSession(Some(borrower))
//
//            val fakeRequest = createFakeRequest(updatePersonalInfoUrl(patientId), session)
//            val fakeRequestWithSession = fakeRequest.withSession(session.toSeq: _*)
//
//            val json = personalInfoJson(sharedPatientInstance, correct = true)
//            val fakeRequestWithJson = fakeRequest.withJsonBody(json)
//            val Some(result) = route(fakeRequestWithJson)
//            status(result) shouldBe (Status.OK)
//
//           sharer.delete()
//
//          }

        }
      }
    }

    describe("Updates Medical Info of a patient") {

      def updateMedicalInfoUrl(id: Long): Call = {
        controllers.routes.API.updateMedicalInfo(id)
      }

      it("Patient doesn't exist") {
        val session = createSession()
        val fakeRequest = createFakeRequest(updateMedicalInfoUrl(-1), session)
        val json = Json.toJson(Map("foo" -> "bar"))
        val fakeRequestWithJson = fakeRequest.withJsonBody(json)
        val Some(result) = route(fakeRequestWithJson)
        status(result) shouldBe (Status.NOT_FOUND)

      }

      it("Tries to update patient information of a patient that doesn't belong to him/her") {
        val patient = samplePatient
        val staff = sampleStaff

        patient.save()
        staff.save()

        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(updateMedicalInfoUrl(patient.getId()), session)
        val json = Json.toJson(Map("foo" -> "bar"))
        val fakeRequestWithJson = fakeRequest.withJsonBody(json)
        val Some(result) = route(fakeRequestWithJson)
        status(result) shouldBe (Status.UNAUTHORIZED)

        staff.delete()

      }

      def medicalInfoJson(patient: Patient, correct: Boolean = true) = {
        def successfulMedicalInfoJson() = {
          val medicalFields =
            Map("id" -> patient.getId().toString(),
              "sexualActivityStartAge" -> (if (correct) "18" else ""),
              "pregnancies" -> "1",
              "cSections" -> "0",
              "naturalDeliveries" -> "1",
              "abortions" -> "0",
              "menopauseStartAge" -> "34",
              "familyPredisposition" -> "true",
              "hormonalReplacementTherapy" -> "true",
              "previousMammaryDiseases" -> "false",
              "menstrualPeriodStartAge" -> "15",
              "breastfedChildren" -> "true")
          Json.toJson(medicalFields)
        }
        successfulMedicalInfoJson()
      }

      describe("Updates own patient") {
        it("Tries to updates patient information with missing fields") {
          val patient = samplePatient
          val staff = sampleStaff

          staff.save()
          patient.setOwner(staff)
          patient.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(updateMedicalInfoUrl(patient.getId()), session)
          val json = medicalInfoJson(patient, correct = false)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.BAD_REQUEST)

          staff.delete()

        }

        it("Updates patient information successfully") {

          val patient = samplePatient
          val staff = sampleStaff

          staff.save()
          patient.setOwner(staff)
          patient.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(updateMedicalInfoUrl(patient.getId()), session)

          val json = medicalInfoJson(patient)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.OK)

          staff.delete()

        }
      }

      describe("Updates borrowed patient") {

        it("Doesn't have access privileges") {

          val sharer = sampleStaff
          val borrower = sampleStaff
          val sharedPatientInstance = new patientFactory { val id = 1L }.value
          sharer.save(); borrower.save(); sharedPatientInstance.save()
          val updateMedicalInfoPermission = false
          val updatePermission = new PatientUpdateInfoPermission(false, updateMedicalInfoPermission, false)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()

          val session = createSession(Some(borrower))
          val fakeRequest = createFakeRequest(updateMedicalInfoUrl(sharedPatientInstance.getId()), session)

          val json = Json.toJson(Map.empty[String, String])
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.UNAUTHORIZED)

          sharer.delete()

        }

        describe("Has access privileges") {

          it("Tries to update patient information with missing fields") {

            val sharer = sampleStaff
            val borrower = sampleStaff
            val sharedPatientInstance = new patientFactory { val id = 1L }.value
            sharer.save(); borrower.save(); sharedPatientInstance.save()
            val updateMedicalInfoPermission = true
            val updatePermission = new PatientUpdateInfoPermission(false, updateMedicalInfoPermission, false)
            val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
            sharedPatient.save()

            borrower.getBorrowedPatients().add(sharedPatient)
            borrower.update()

            val session = createSession(Some(borrower))
            val fakeRequest = createFakeRequest(updateMedicalInfoUrl(sharedPatientInstance.getId()), session)
            val json = medicalInfoJson(sharedPatientInstance, correct = false)
            val fakeRequestWithJson = fakeRequest.withJsonBody(json)
            val Some(result) = route(fakeRequestWithJson)
            status(result) shouldBe (Status.BAD_REQUEST)

            sharer.delete()

          }

//          it("Updates patient information successfully") {
//
//            val sharer = sampleStaff
//            val borrower = sampleStaff
//            val sharedPatientInstance = new patientFactory { val id = 1L }.value
//            sharer.save(); borrower.save(); sharedPatientInstance.save()
//            val updatePersonalInfoPermission = true
//            val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfoPermission, false, false)
//            val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
//            sharedPatient.save()
//
//            borrower.getBorrowedPatients().add(sharedPatient)
//            borrower.update()
//            val patientId = sharedPatientInstance.getId()
//
//            val session = createSession(Some(borrower))
//
//            val fakeRequest = createFakeRequest(updateMedicalInfoUrl(patientId), session)
//            val fakeRequestWithSession = fakeRequest.withSession(session.toSeq: _*)
//
//            val json = medicalInfoJson(sharedPatientInstance, correct = true)
//            val fakeRequestWithJson = fakeRequest.withJsonBody(json)
//            val Some(result) = route(fakeRequestWithJson)
//            status(result) shouldBe (Status.OK)
//
//            sharer.delete()
//
//          }

        }

      }
    }

    describe("Updating patient studies") {
      it("NotFound if patient does not exist") {

      }
    }
  }

}
