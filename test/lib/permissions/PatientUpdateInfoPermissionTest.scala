package lib.permissions

import org.scalatest.Matchers
import org.scalatest.FunSpec

class PatientUpdateInfoPermissionTest extends FunSpec with Matchers {
  describe("#canUpdatePersonalInfo") {

    def updatingMedicalInfoIs(allowed: Boolean, accessPrivileges: Seq[String]) {
      for (ap <- accessPrivileges) {
        val permission = new PatientUpdateInfoPermission(ap)
        permission.canUpdatePersonalInfo should be(allowed)
      }
    }

    it("returns true if enabled privilege ...1xxyyy") {
      val accessPrivileges = Seq("100101", "110101", "101100")
      updatingMedicalInfoIs(allowed = true, accessPrivileges)
    }

    it("returns false if disabled privilege ...0xxyyy") {
      val accessPrivileges = Seq("000001", "011011", "001000")
      updatingMedicalInfoIs(allowed = false, accessPrivileges)
    }

  }

  describe("#canUpdateMedicalInfo") {

    def updatingMedicalInfoIs(allowed: Boolean, accessPrivileges: Seq[String]) {
      for (ap <- accessPrivileges) {
        val permission = new PatientUpdateInfoPermission(ap)
        permission.canUpdateMedicalInfo should be(allowed)
      }
    }

    it("returns true if enabled privilege ...x1xyyy") {
      val accessPrivileges = Seq("010010", "011011", "010110")
      updatingMedicalInfoIs(allowed = true, accessPrivileges)
    }

    it("returns false if disabled privilege ...x0xyyy") {
      val accessPrivileges = Seq("100000", "000101", "0011001")
      updatingMedicalInfoIs(allowed = false, accessPrivileges)
    }

  }

  describe("#canUpdateStudies") {

    def updatingStudiesIs(allowed: Boolean, accessPrivileges: Seq[String]) {
      for (ap <- accessPrivileges) {
        val permission = new PatientUpdateInfoPermission(ap)
        permission.canUpdateStudies should be(allowed)
      }
    }

    it("returns true if enabled privilege ...xx1yyy") {
      val accessPrivileges = Seq("001001", "111101", "101011")
      updatingStudiesIs(allowed = true, accessPrivileges)
    }

    it("returns false if disabled privilege ...xx0yyy") {
      implicit val accessPrivileges = Seq("100000", "000010", "010110")
      updatingStudiesIs(allowed = false, accessPrivileges)
    }

  }
}