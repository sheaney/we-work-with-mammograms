package controllers

import integration.{ UserLogin, PlayBrowserSpec }
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import models._
import factories.Factories
import play.api.libs.json.Json
import play.api.http.Status
import lib.permissions._
import org.scalatest.BeforeAndAfter
import scala.Some
import play.api.mvc.Call

/**
 * Created by fernando on 5/8/14.
 */
class APITest extends PlayBrowserSpec with UserLogin with Factories with BeforeAndAfter {

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

    def notFoundAndForbidden(notFoundCall: => Call, forbiddenCall: Patient => Call) {
      describe("Not found or forbidden") {
        it("should return notFound error if the patient doesn't exist") {
          val session = createSession()
          val fakeRequest = createFakeRequest(notFoundCall, session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.NOT_FOUND)
        }

        it("should return forbidden if the patient is neither owned or borrowed by the staff member") {
          val staff = sampleStaff
          val patient = samplePatient

          patient.save()
          staff.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(forbiddenCall(patient), session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.FORBIDDEN)

          patient.delete()
          staff.delete()
        }
      }
    }

    def patientOwnedOrBorrowed(ownedCall: Patient => Call, borrowedCall: Patient => Call) {
      describe("When patient is own or borrowed") {
        it("should return a json node with the patient's information if it's owned by the staff") {
          val staff = sampleStaff
          val patient = samplePatient

          staff.save()

          patient.setOwner(staff)
          patient.save()

          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(ownedCall(patient), session)
          val Some(result) = route(fakeRequest)
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
          val fakeRequest = createFakeRequest(borrowedCall(sharedPatientInstance), session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.OK)

          sharer.delete()
        }
      }
    }

    describe("#getPatientInfo") {
      def getPatientUrl(id: Long): Call = controllers.routes.API.getPatientInfo(id)

      notFoundAndForbidden(getPatientUrl(-1), p => getPatientUrl(p.getId))
      patientOwnedOrBorrowed(p => getPatientUrl(p.getId), p => getPatientUrl(p.getId))
    }

    describe("#staff") {
      def staffUrl: Call = controllers.routes.API.staff()

      it ("obtains OK response for all staff") {
        val staffList = for (i <- 0 to 10) yield (sampleStaff)
        staffList.foreach(_.save)
        val firstStaff = staffList.head
        val session = createSession(Some(firstStaff))
        val fakeRequest = createFakeRequest(staffUrl, session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.OK)
      }
    }

    describe("#getStaff") {
      def getStaffUrl(id: Long): Call = controllers.routes.API.getStaff(id)

      it("obtains a not found if staff does not exist") {
        val staff = sampleStaff
        staff.save
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(getStaffUrl(-1), session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.NOT_FOUND)
      }

      it("obtains OK if staff exists") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(getStaffUrl(staff.getId), session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.OK)
      }
    }

    describe("#getMammogram") {
      def getMammogramUrl(id: Long): Call = controllers.routes.API.getMammogram(id)

      it("obtains a not found if staff does not exist") {
        val staff = sampleStaff
        staff.save
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(getMammogramUrl(-1), session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.NOT_FOUND)
      }

      it("obtains OK if staff exists") {
        val (staff, mammogram) = (new Staff, new Mammogram)
        staff.save
        mammogram.save
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(getMammogramUrl(mammogram.getId), session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.OK)
      }
    }

    describe("#createAnnotation") {
      def createAnnotationUrl(mid: Long): Call =
        controllers.routes.API.createAnnotation(mid)

      it("obtains not found if mammogram does not exist") {
        val staff = sampleStaff
        staff.save
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(createAnnotationUrl(-1), session)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe (Status.NOT_FOUND)
      }

      it("successfully creates a new annotation") {
        val mammogram = new Mammogram
        val staff = sampleStaff
        staff.save()
        mammogram.save()
        val content = "A new annotation!"
        val json = Json.toJson(Map("content" -> content))
        val session = createSession(Some(staff))
        val fakeRequest = createFakeRequest(createAnnotationUrl(mammogram.getId), session)
        val fakeRequestWithJson = fakeRequest.withJsonBody(json)
        val Some(result) = route(fakeRequestWithJson)
        status(result) shouldBe (Status.OK)
      }

      describe("Failing constraints") {

        it("fails to create a new annotation (content length exceeds 200 chars)") {
          val mammogram = new Mammogram
          val staff = sampleStaff
          staff.save()
          mammogram.save()
          val content = "*" * 201
          val json = Json.toJson(Map("content" -> content))
          val session = createSession(Some(staff))
          val fakeRequest = createFakeRequest(createAnnotationUrl(mammogram.getId), session)
          val fakeRequestWithJson = fakeRequest.withJsonBody(json)
          val Some(result) = route(fakeRequestWithJson)
          status(result) shouldBe (Status.BAD_REQUEST)
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
      def updateStudiesUrl(pid: Long, sid: Long): Call = {
        controllers.routes.API.updateStudy(pid, sid)
      }

      describe("sad paths") {
        it("returns NotFound if patient does not exist") {
          val session = createSession()
          val fakeRequest = createFakeRequest(updateStudiesUrl(-1, 1), session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.NOT_FOUND)
        }

        it("returns NotFound if study does not exist") {
          val staff = new staffFactory { val id = 1L }.value
          staff.save()

          val patient = new patientFactory { val id = 1L }.value
          patient.save()
          val session = createSession(Some(staff))

          val fakeRequest = createFakeRequest(updateStudiesUrl(patient.getId, -1), session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.NOT_FOUND)

          staff.delete()
        }

        it("returns Unauthorized if staff does not have access privileges for updating") {
          val sharer = sampleStaff
          val borrower = sampleStaff
          val sharedPatientInstance = samplePatient
          val study = sampleStudy

          sharedPatientInstance.getStudies.add(study)
          sharer.save(); borrower.save(); sharedPatientInstance.save()

          val updateStudiesPermission = false
          val updatePermission = new PatientUpdateInfoPermission(false, false, updateStudiesPermission)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, updatePermission.getAccessPrivileges())
          sharedPatient.save()

          val session = createSession(Some(borrower))
          val fakeRequest = createFakeRequest(updateStudiesUrl(sharedPatientInstance.getId, study.getId), session)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe (Status.UNAUTHORIZED)
        }

      }

    }
  }

}
