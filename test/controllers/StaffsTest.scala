package controllers

import integration.{UserLogin, PlayBrowserSpec}
import factories.Factories
import org.scalatest.BeforeAndAfter
import models.{Mammogram, SharedPatient, Staff}
import play.api.http.Status
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.libs.json._
import lib.permissions.{Permission, PatientViewInfoPermission, PatientUpdateInfoPermission}

class StaffsTest extends PlayBrowserSpec with UserLogin with Factories with BeforeAndAfter {
  describe("Staff is logged into system") {
    def createSession(staff: Option[Staff] = None): Seq[(String, String)] = {
      val staffId = staff match {
        case Some(staff) => staff.getId.toString
        case None => "1"
      }

      Map(
        "id" -> staffId,
        "timeOfLogin" -> System.currentTimeMillis().toString(),
        "type" -> "STAFF",
        "user" -> "Juanito").toSeq
    }

    describe("#staff") {
      def staffUrl(): Call = controllers.routes.Staffs.staff()

      it("request has OK status") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(staffUrl).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }
    }

    describe("#newStudy") {
      def newStudyUrl(pid: Long): Call = controllers.routes.Staffs.newStudy(pid)

      it("NotFound status if patient does not exist") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(newStudyUrl(pid = -1)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      describe("Unauthorized if staff does not have permission to view patient studies") {

        it("Tries to view study creation view for patient that does not belong to him") {
          val (staff, patient) = (sampleStaff, samplePatient)
          staff.save();
          patient.save()
          val session = createSession(Some(staff))
          val fakeRequest = FakeRequest(newStudyUrl(pid = patient.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }

        it("Tries to view study creation view, not having permission to view patient studies") {
          val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
          sharer.save();
          borrower.save();
          sharedPatientInstance.save()
          val (viewPersonalInfo, viewMedicalInfo, viewStudies) = (true, true, false)
          val updatePersonalInfo, updateMedicalInfo, updateStudies = true
          val viewPermission = new PatientViewInfoPermission(viewPersonalInfo, viewMedicalInfo, viewStudies)
          val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfo, updateMedicalInfo, updateStudies)
          val accessPrivileges = Permission.concatAccessPrivileges(viewPermission, updatePermission)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()

          val session = createSession(Some(borrower))
          val fakeRequest = FakeRequest(newStudyUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }

        it("Tries to view study creation view, not having permission to update patient studies") {
          val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
          sharer.save();
          borrower.save();
          sharedPatientInstance.save()
          val viewPersonalInfo, viewMedicalInfo, viewStudies = true
          val (updatePersonalInfo, updateMedicalInfo, updateStudies) = (true, true, false)
          val viewPermission = new PatientViewInfoPermission(viewPersonalInfo, viewMedicalInfo, viewStudies)
          val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfo, updateMedicalInfo, updateStudies)
          val accessPrivileges = Permission.concatAccessPrivileges(viewPermission, updatePermission)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()

          val session = createSession(Some(borrower))
          val fakeRequest = FakeRequest(newStudyUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }
      }

      it("OK status if staff has permission to view and update studies") {
        val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
        sharer.save();
        borrower.save();
        sharedPatientInstance.save()
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, Integer.MAX_VALUE)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)
        borrower.update()

        val session = createSession(Some(borrower))
        val fakeRequest = FakeRequest(newStudyUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }
    }

    describe("#createNewStudy") {
      def createNewStudyUrl(pid: Long): Call = controllers.routes.Staffs.createNewStudy(pid)

      it("NotFound if patient does not exist") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(createNewStudyUrl(pid = -1)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      describe("Unauthorized if staff does not have permission to update patient studies") {

        it("Tries to create a study for patient that does not belong to him and is not borrowed") {
          val (staff, patient) = (sampleStaff, samplePatient)
          staff.save();
          patient.save()
          val session = createSession(Some(staff))
          val fakeRequest = FakeRequest(createNewStudyUrl(pid = patient.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }

        it("Tries to create a study, not having permission to update patient studies") {
          val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
          sharer.save();
          borrower.save();
          sharedPatientInstance.save()
          val viewPersonalInfo, viewMedicalInfo, viewStudies = true
          val (updatePersonalInfo, updateMedicalInfo, updateStudies) = (true, true, false)
          val viewPermission = new PatientViewInfoPermission(viewPersonalInfo, viewMedicalInfo, viewStudies)
          val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfo, updateMedicalInfo, updateStudies)
          val accessPrivileges = Permission.concatAccessPrivileges(viewPermission, updatePermission)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()

          val session = createSession(Some(borrower))
          val fakeRequest = FakeRequest(createNewStudyUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }
      }

    }

    describe("#study") {
      def studyUrl(pid: Long, sid: Long): Call = controllers.routes.Staffs.study(pid, sid)

      it("NotFound status if patient does not exist") {
        val (staff, study) = (sampleStaff, sampleStudy)
        staff.save(); study.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(studyUrl(pid = -1, sid = study.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      it("NotFound status if study does not exist") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.save(); patient.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(studyUrl(pid = patient.getId, sid = -1)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      describe("Unauthorized if staff does not have permission to view patient studies") {

        it("Tries to view study of patient that does not belong to him") {
          val (staff, patient, study) = (sampleStaff, samplePatient, sampleStudy)
          staff.save(); patient.save(); study.save()

          val session = createSession(Some(staff))
          val fakeRequest = FakeRequest(studyUrl(pid = patient.getId, sid = study.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }

        it("Tries to view study, not having permission to patient studies") {
          val (sharer, borrower, sharedPatientInstance, study) = (sampleStaff, sampleStaff, samplePatient, sampleStudy)
          sharer.save(); borrower.save(); sharedPatientInstance.save()
          val (viewPersonalInfo, viewMedicalInfo, viewStudies) = (true, true, false)
          val updatePersonalInfo, updateMedicalInfo, updateStudies = true
          val viewPermission = new PatientViewInfoPermission(viewPersonalInfo, viewMedicalInfo, viewStudies)
          val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfo, updateMedicalInfo, updateStudies)
          val accessPrivileges = Permission.concatAccessPrivileges(viewPermission, updatePermission)
          val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
          sharedPatient.save()

          borrower.getBorrowedPatients().add(sharedPatient)
          borrower.update()
          sharedPatientInstance.getStudies.add(study)
          sharedPatientInstance.update()

          val session = createSession(Some(borrower))
          val fakeRequest = FakeRequest(studyUrl(pid = sharedPatientInstance.getId, sid = study.getId)).withSession(session: _*)
          val Some(result) = route(fakeRequest)
          status(result) shouldBe Status.UNAUTHORIZED
        }
      }

      it("OK status if staff has permission to view studies") {
        val (sharer, borrower, sharedPatientInstance, study) = (sampleStaff, sampleStaff, samplePatient, sampleStudy)
        sharer.save(); borrower.save(); sharedPatientInstance.save()
        val (viewPersonalInfo, viewMedicalInfo, viewStudies) = (false, false, true)
        val updatePersonalInfo, updateMedicalInfo, updateStudies = false
        val viewPermission = new PatientViewInfoPermission(viewPersonalInfo, viewMedicalInfo, viewStudies)
        val updatePermission = new PatientUpdateInfoPermission(updatePersonalInfo, updateMedicalInfo, updateStudies)
        val accessPrivileges = Permission.concatAccessPrivileges(viewPermission, updatePermission)
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)
        borrower.update()
        sharedPatientInstance.getStudies.add(study)
        sharedPatientInstance.update()

        val session = createSession(Some(borrower))
        val fakeRequest = FakeRequest(studyUrl(pid = sharedPatientInstance.getId, sid = study.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }

    }

    describe("#showPatient") {
      def showPatientUrl(pid: Long): Call = controllers.routes.Staffs.showPatient(pid)

      it("NotFound status if patient does not exist") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(showPatientUrl(pid = -1)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      it("Unauthorized status if patient does not belong to and is not borrowed by staff") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.save(); patient.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(showPatientUrl(pid = patient.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }

      it("OK status if patient belongs to staff") {
        val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
        sharer.save();
        borrower.save();
        sharedPatientInstance.save()
        val accessPrivileges = 0 // accessPrivileges do not matter in this case
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)
        borrower.update()

        val session = createSession(Some(borrower))
        val fakeRequest = FakeRequest(showPatientUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }

    }

    describe("#sharePatient") {
      def sharePatientUrl(pid: Long): Call = controllers.routes.Staffs.sharePatient(pid)

      it("NotFound status if patient does not exist") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(sharePatientUrl(pid = -1)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      it("Unauthorized status if patient does not belong to and is not borrowed by staff") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.save(); patient.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(sharePatientUrl(pid = patient.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }

      it("OK status if patient belongs to staff") {
        val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
        sharer.save();
        borrower.save();
        sharedPatientInstance.save()
        val accessPrivileges = 0 // accessPrivileges do not matter in this case
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)
        borrower.update()

        val session = createSession(Some(borrower))
        val fakeRequest = FakeRequest(sharePatientUrl(pid = sharedPatientInstance.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }
    }

    describe("#createSharedPatient") {
      def createSharedPatientUrl(pid: Long, bid: Long): Call = controllers.routes.Staffs.createSharedPatient(pid, bid)

      def sharedPatientJson = {
        Json.toJson(Map(
          "viewPersonalInfo" -> true,
          "viewMedicalInfo" -> true,
          "viewStudies" -> true,
          "updatePersonalInfo" -> true,
          "updateMedicalInfo" -> true,
          "updateStudies" -> true
        ))
      }

      def emptyJson = Json.toJson(Map.empty[String, String])

      it("NotFound status if patient does not exist") {
        val staff = sampleStaff
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(createSharedPatientUrl(pid = -1, bid = staff.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.NOT_FOUND
      }

      it("Unauthorized status if patient does not belong and is not borrowed by staff") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.save(); patient.save()
        val session = createSession(Some(staff))
        val fakeRequest = FakeRequest(createSharedPatientUrl(pid = patient.getId, bid = staff.getId)).withSession(session: _*)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }

      it("BadRequest if json request body is missing") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.getOwnPatients.add(patient)
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest =
          FakeRequest(createSharedPatientUrl(pid = patient.getId, bid = staff.getId)).
            withSession(session: _*).
            withJsonBody(emptyJson)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.BAD_REQUEST
      }

      it("Ok if json request is correctly formatted") {
        val (staff, patient) = (sampleStaff, samplePatient)
        staff.getOwnPatients.add(patient)
        staff.save()
        val session = createSession(Some(staff))
        val fakeRequest =
          FakeRequest(createSharedPatientUrl(pid = patient.getId, bid = staff.getId)).
            withSession(session: _*).
            withJsonBody(sharedPatientJson)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.OK
      }

      it("Unauthorized when trying to share borrowed patient") {
        val (sharer, borrower, sharedPatientInstance) = (sampleStaff, sampleStaff, samplePatient)
        sharer.save();
        borrower.save();
        sharedPatientInstance.save()
        val accessPrivileges = 0 // accessPrivileges do not matter in this case
        val sharedPatient = new SharedPatient(sharer, borrower, sharedPatientInstance, accessPrivileges)
        sharedPatient.save()

        borrower.getBorrowedPatients().add(sharedPatient)
        borrower.update()

        val session = createSession(Some(borrower))
        val fakeRequest =
          FakeRequest(createSharedPatientUrl(pid = sharedPatientInstance.getId, bid = borrower.getId)).
            withSession(session: _*).
            withJsonBody(sharedPatientJson)
        val Some(result) = route(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }

    }
  }
}
