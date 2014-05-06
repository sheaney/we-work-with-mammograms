package controllers

import org.scalatest.FunSpec
import scala.collection.JavaConverters._
import models.Staff
import org.scalatest.Matchers
import models.ModelsHelper
import play.api.test.Helpers.running

class APIHelpersTest extends ModelsHelper {

  def newStaff(id: Long): Staff = {
    val staff = new Staff()
    staff.setId(id)
    staff
  }
  
  def getNStaffMembers(n: Int): Seq[Staff] = {
    for {
      id <- 1 to 10
      s = newStaff(id)
    } yield s
  }

  it("APIHelpers#filterMatchingFromList") {
    running(app) {
      // Filtering empty list
      val emptyList = List.empty[Staff].asJava
      var resultingList = APIHelpers.filterMatchingStaffFromList(emptyList, newStaff(0)).asScala
      resultingList shouldBe empty

      // 1 member list with different id as staff being compared
      var s = new Staff()
      s.setId(1)
      var list = List(s).asJava
      resultingList = APIHelpers.filterMatchingStaffFromList(list, newStaff(2)).asScala
      resultingList should have size 1
      resultingList should equal (list.asScala)

      // 1 member list with same id as staff being compared
      list = List(newStaff(1)).asJava
      resultingList = APIHelpers.filterMatchingStaffFromList(list, newStaff(1)).asScala
      resultingList shouldBe empty

      // 10 member list gets filtered
      list = getNStaffMembers(n = 10).asJava
      resultingList = APIHelpers.filterMatchingStaffFromList(list, newStaff(1)).asScala
      resultingList should have size 9

      // 10 member list get unfiltered
      list = getNStaffMembers(n = 10).asJava
      resultingList = APIHelpers.filterMatchingStaffFromList(list, newStaff(11)).asScala
      resultingList should have size 10
      
      // 10 member list where staff is null
      list = getNStaffMembers(n = 10).asJava
      resultingList = APIHelpers.filterMatchingStaffFromList(list, null).asScala
      resultingList should have size 10
    }

  }

}