package integration

import play.api.test.Helpers.running
import play.libs.WS
import play.test.Helpers._
import models.{ Staff, Admin, Patient }

class IntegrationScalaTest extends PlayBrowserSpec with UserLogin {

  describe("Staff logs in,") {

    it("After successful login, staff home page is displayed") {
      Given("there exists a staff in the DB")
      val email = "test@test.com"
      val pwd = "test"
      val staff = createUser[Staff](email, pwd)

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
      click on id("logout")

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

  describe("Staff, Patient and Admin users can log in") {
    it("staff can log in") {
      val staff = login[Staff](email = "foo@bar.com", pwd = "123")
      logout(staff)
    }
    
    it("patient can log in") {
      val patient = login[Patient](email = "foo@bar.com", pwd = "123")
      logout(patient)
    }
    
    ignore("Admin home not implemented yet!") {
      it("admin can log in") {
        val admin = login[Admin](email = "foo@bar.com", pwd = "123")
        logout(admin)
      }
    }
  }

}