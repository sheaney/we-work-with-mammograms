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

}