package lib.permissions

import org.scalatest.Matchers
import org.scalatest.FunSpec

class PatientUpdateInfoPermissionTest extends FunSpec with Matchers {
  def verifyPermission(allowed: Boolean, accessPrivileges: Seq[String])(checkAccessPrivilege: PatientUpdateInfoPermission => Boolean): Unit = {
    for (ap <- accessPrivileges) {
      val encodedAccessPrivileges = ByteDecodeEncoder.encode(ap)
      val permission = new PatientUpdateInfoPermission(encodedAccessPrivileges)
      checkAccessPrivilege(permission) should be(allowed)
    }
  }

  describe("#canUpdatePersonalInfo") {

    it("returns true if enabled privilege ...1xxyyy") {
      val accessPrivileges = Seq("100101", "110101", "101100")
      verifyPermission(allowed = true, accessPrivileges)(_.canUpdatePersonalInfo())
    }

    it("returns false if disabled privilege ...0xxyyy") {
      val accessPrivileges = Seq("000001", "011011", "001000")
      verifyPermission(allowed = false, accessPrivileges)(_.canUpdatePersonalInfo())
    }

  }

  describe("#canUpdateMedicalInfo") {

    it("returns true if enabled privilege ...x1xyyy") {
      val accessPrivileges = Seq("010010", "011011", "010110")
      verifyPermission(allowed = true, accessPrivileges)(_.canUpdateMedicalInfo())
    }

    it("returns false if disabled privilege ...x0xyyy") {
      val accessPrivileges = Seq("100000", "000101", "0011001")
      verifyPermission(allowed = false, accessPrivileges)(_.canUpdateMedicalInfo())
    }

  }

  describe("#canUpdateStudies") {

    it("returns true if enabled privilege ...xx1yyy") {
      val accessPrivileges = Seq("001001", "111101", "101011")
      verifyPermission(allowed = true, accessPrivileges)(_.canUpdateStudies())
    }

    it("returns false if disabled privilege ...xx0yyy") {
      implicit val accessPrivileges = Seq("100000", "000010", "010110")
      verifyPermission(allowed = false, accessPrivileges)(_.canUpdateStudies())
    }

  }
}