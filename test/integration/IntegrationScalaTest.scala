package integration

import play.api.test.Helpers.running
import play.libs.WS
import play.test.Helpers._
import models.Staff

class IntegrationScalaTest extends PlayBrowserSpec {
  def createStaff(email: String, pwd: String) {
    val staff = new Staff
    // set staff email and pwd
    staff.save
  }
  
  describe("Staff logs in") {
    it("After successful login, staff home page is displayed") {
      Given("there exists a staff in the DB")
      val email = "test@test.com"
      val pwd   = "test"
      createStaff(email, pwd)
      
      When("visiting the index page")
      go to (host + "/")
      pageTitle should be("Iniciar Sesión")
      
      And("entering staff login credentials")
      emailField("email").value = email
      pwdField("password").value = pwd
      
      And("clicking on the login button")
      click on ("login")
      
      Then("the user should see information from the staff home page")
      pageSource should include ("Compartidos")
      pageSource should include ("Propios")
    }
  }

}