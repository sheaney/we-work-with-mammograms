package lib.permissions

import org.scalatest.FunSpec
import org.scalatest.Matchers

class PatientViewInfoPermissionTest extends FunSpec with Matchers {

  describe("#canViewPersonalInfo") {

    def viewingPersonalInfoShouldBe(allowed: Boolean, accessPrivileges: Seq[String]) {
      for (ap <- accessPrivileges) {
        val permission = new PatientViewInfoPermission(ap)
        permission.canViewPersonalInfo should be(allowed)
      }
    }

    it("returns true if enabled privilege ...1xx") {
      val accessPrivileges = Seq("100101", "101", "100")
      viewingPersonalInfoShouldBe(allowed = true, accessPrivileges)
    }

    it("returns false if disabled privilege ...0xx") {
      val accessPrivileges = Seq("001", "011", "1000")
      viewingPersonalInfoShouldBe(allowed = false, accessPrivileges)
    }

  }

    describe("#canViewMedicalInfo") {
      
      def viewingMedicalInfoShouldBe(allowed: Boolean, accessPrivileges: Seq[String]) {
        for (ap <- accessPrivileges) {
          val permission = new PatientViewInfoPermission(ap)
          permission.canViewMedicalInfo should be(allowed)
        }
      }
      
      it("returns true if enabled privilege ...x1x") {
        val accessPrivileges = Seq("010", "011", "110")
        viewingMedicalInfoShouldBe(allowed = true, accessPrivileges)
      }
      
      it("returns false if disabled privilege ...x0x") {
        val accessPrivileges = Seq("1000", "101", "1001")
        viewingMedicalInfoShouldBe(allowed = false, accessPrivileges)
      }
      
    }
    
    describe("#canViewStudies") {
      
      def viewingStudiesShouldBe(allowed: Boolean, accessPrivileges: Seq[String]) {
        for (ap <- accessPrivileges) {
          val permission = new PatientViewInfoPermission(ap)
          permission.canViewStudies should be(allowed)
        }
      }
      
      it("returns true if enabled privilege ...xx1") {
        val accessPrivileges = Seq("001", "101", "011")
        viewingStudiesShouldBe(allowed = true, accessPrivileges)
      }
      
      it("returns false if disabled privilege ...xx0") {
        implicit val accessPrivileges = Seq("1000", "010", "110")
        viewingStudiesShouldBe(allowed = false, accessPrivileges)
      }
      
    }

}