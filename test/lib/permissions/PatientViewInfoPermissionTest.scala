package lib.permissions

import org.scalatest.FunSpec
import org.scalatest.Matchers

class PatientViewInfoPermissionTest extends FunSpec with Matchers {

  def verifyPermission(allowed: Boolean, accessPrivileges: Seq[String])(checkAccessPrivilege: PatientViewInfoPermission => Boolean): Unit = {
    for (ap <- accessPrivileges) {
      val encodedAccessPrivileges = ByteDecodeEncoder.encode(ap)
      val permission = new PatientViewInfoPermission(encodedAccessPrivileges)
      checkAccessPrivilege(permission) should be(allowed)
    }
  }

  describe("#canViewPersonalInfo") {

    it("returns true if enabled privilege ...1xx") {
      val accessPrivileges = Seq("100101", "101", "100")
      verifyPermission(allowed = true, accessPrivileges)(_.canViewPersonalInfo())

    }

    it("returns false if disabled privilege ...0xx") {
      val accessPrivileges = Seq("001", "011", "1000")
      verifyPermission(allowed = false, accessPrivileges)(_.canViewPersonalInfo())
    }

  }

  describe("#canViewMedicalInfo") {

    it("returns true if enabled privilege ...x1x") {
      val accessPrivileges = Seq("010", "011", "110")
      verifyPermission(allowed = true, accessPrivileges)(_.canViewMedicalInfo())
    }

    it("returns false if disabled privilege ...x0x") {
      val accessPrivileges = Seq("1000", "101", "1001")
      verifyPermission(allowed = false, accessPrivileges)(_.canViewMedicalInfo())
    }

  }

  describe("#canViewStudies") {

    it("returns true if enabled privilege ...xx1") {
      val accessPrivileges = Seq("001", "101", "011")
      verifyPermission(allowed = true, accessPrivileges)(_.canViewStudies())
    }

    it("returns false if disabled privilege ...xx0") {
      val accessPrivileges = Seq("1000", "010", "110")
      verifyPermission(allowed = false, accessPrivileges)(_.canViewStudies())
    }

  }

  describe("boolean constructor") {
    it("view personal info permission is set to true") {
      val (personalInfo, medicalInfo, studies) = (true, false, false)
      val permissions = new PatientViewInfoPermission(personalInfo, medicalInfo, studies)

      val errorMsg = "Only should be able to view personal info"
      assert(permissions.canViewPersonalInfo(), errorMsg)
      assert(!permissions.canViewMedicalInfo(), errorMsg)
      assert(!permissions.canViewStudies(), errorMsg)
    }

    it("view medical info permission is set to true") {
      val (personalInfo, medicalInfo, studies) = (false, true, false)
      val permissions = new PatientViewInfoPermission(personalInfo, medicalInfo, studies)

      val errorMsg = "Only should be able to view medical info"
      assert(!permissions.canViewPersonalInfo(), errorMsg)
      assert(permissions.canViewMedicalInfo(), errorMsg)
      assert(!permissions.canViewStudies(), errorMsg)
    }

    it("view studies permission is set to true") {
      val (personalInfo, medicalInfo, studies) = (false, false, true)
      val permissions = new PatientViewInfoPermission(personalInfo, medicalInfo, studies)

      val errorMsg = "Only should be able to view studies"
      assert(!permissions.canViewPersonalInfo(), errorMsg)
      assert(!permissions.canViewMedicalInfo(), errorMsg)
      assert(permissions.canViewStudies(), errorMsg)
    }
  }

}