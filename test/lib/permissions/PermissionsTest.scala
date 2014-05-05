package lib.permissions

import org.scalatest.FunSpec

class PermissionsTest extends FunSpec {
  describe("Permission#concatAccessPrivileges") {
    it("ORs patient permissions") {
      assert(Permission.concatAccessPrivileges() === 0)
      
      val viewPrivileges = 7
      val viewPermission = new PatientViewInfoPermission(viewPrivileges)
      
      assert(Permission.concatAccessPrivileges(viewPermission) === viewPrivileges)
      assert(Permission.concatAccessPrivileges(viewPermission, viewPermission) === viewPrivileges)
      
      val updatePrivileges = 8
      val updatePermission = new PatientUpdateInfoPermission(updatePrivileges)
      
      assert(Permission.concatAccessPrivileges(updatePermission) === updatePrivileges)
      assert(Permission.concatAccessPrivileges(updatePermission, updatePermission) === updatePrivileges)
      
      assert(Permission.concatAccessPrivileges(viewPermission, updatePermission) === (viewPrivileges | updatePrivileges))
    }
  }

}