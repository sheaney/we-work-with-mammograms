package integration

import play.api.test.Helpers.running
import play.libs.WS
import play.test.Helpers._
import models.{ Staff, Admin, Patient }
import java.text.SimpleDateFormat

class IntegrationScalaTest extends PlayBrowserSpec with UserLogin {

  describe("Staff logs in,") {

    it("After successful login, staff home page is displayed") {
      Given("there exists a staff in the DB")
      val email = "test@test.com"
      val pwd = "test"
      val staff = createUser[Staff]

      When("visiting the index page")
      go to (host + "/")
      pageSource should include("¡Bienvenido!")

      And("entering staff login credentials")
      emailField("email").value = staff.getEmail
      pwdField("password").value = staff.getPassword

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

      Then("user sees the login screen with a form submission error")
      pageSource should include("Invalid user or password")
    }
  }

  describe("Staff, Patient and Admin users can log in") {
    it("staff can log in") {
      val staff = login[Staff]
      logout(staff)
    }

    it("patient can log in") {
      val patient = login[Patient]
      logout(patient)
    }

    it("admin can log in") {
      val admin = login[Admin]
      logout(admin)
    }
  }

  describe("Admin creates a staff member") {
    val newStaffUrl = host + "/admin/staff/new"
    
    it("Successfully creates a Staff user") {
      val admin = login[Admin]

      When("admin goes to new staff form page")
      go to (newStaffUrl)
      pageSource should include("Crear Personal")

      val staff = sampleStaff
      val format = new SimpleDateFormat("dd/MM/yyyy");

      And("admin fills in fields for creating a new staff member")
      textField("name").value = staff.getName()
      textField("firstLastName").value = staff.getFirstLastName()
      textField("secondLastName").value = staff.getSecondLastName()
      textField("address").value = staff.getAddress()
      textField("email").value = staff.getEmail()
      textField("telephone").value = staff.getTelephone()
      textField("birthdate").value = format.format(staff.getBirthdate())
      singleSel("role").value = "Doctor"
      textField("RFC").value = staff.getRFC();
      textField("cedula").value = staff.getCedula()
      
      And("submits form")
      submit()
      
      Then("admin gets redirected to home page with success message")
      pageSource should include("Un nuevo personal se ha creado")

      logout(admin)
    }
    
    it("Fails to create a staff member") {
      val admin = login[Admin]

      When("admin goes to new staff form page")
      go to (newStaffUrl)
      pageSource should include("Crear Personal")

      val staff = sampleStaff

      And("admin fills in fields for creating a new staff user")
      textField("name").value = staff.getName()
      textField("firstLastName").value = staff.getFirstLastName()
      textField("secondLastName").value = staff.getSecondLastName()
      textField("address").value = staff.getAddress()
      textField("email").value = staff.getEmail()
      textField("telephone").value = staff.getTelephone()
      textField("birthdate").value = "foo bar" // insert incorrect date format
      singleSel("role").value = "Doctor"
      textField("RFC").value = staff.getRFC();
      textField("cedula").value = staff.getCedula()
      
      And("submits form")
      submit()
      
      Then("admin should see a message indicating wrong date format")
      pageSource should include("Fecha es inválida")

      logout(admin)
    }
  }

}