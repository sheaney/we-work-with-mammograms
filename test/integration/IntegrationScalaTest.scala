package integration

import play.api.test.Helpers.running
import play.libs.WS
import play.test.Helpers._
import models.Staff

class IntegrationScalaTest extends PlayBrowserSpec {
  def createStaff(email: String, pwd: String): Staff = {
    val s = new Staff
    s.setEmail(email);
    s.setPassword(pwd);
    s.save
    s
  }

  describe("Staff logs in,") {

    it("After successful login, staff home page is displayed") {
      Given("there exists a staff in the DB")
      val email = "test@test.com"
      val pwd = "test"
      val staff = createStaff(email, pwd)

      When("visiting the index page")
      go to (host + "/")
      pageSource should include("¡Bienvenido!")

      And("entering staff login credentials")
      emailField("email").value = email
      pwdField("password").value = pwd

      And("clicking on the login button")
      click on ("login")
      
      Then("the user should see information from the staff home page")
      pageSource should include("Compartidos")
      pageSource should include("Propios")
      
      Then("user logs out")
      click on id ("logout")
      
      Then("user should see ¡Bienvenido!")
      pageSource should include("¡Bienvenido!")
      
      staff.delete
      
    }

    it("After failed login, show form errors") {
      When("visiting the index page")
      go to (host + "/")
      pageSource should include("¡Bienvenido!")
      
      And("entering staff login credentials")
      emailField("email").value = "fake"
      pwdField("password").value = "notInDB"

      And("clicking on the login button")
      click on ("login")
      
      Then("The user show see the login screen with a from submissioni error")
      pageSource should include("Invalid user or password")
    }
  }

}