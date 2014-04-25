package integration

import models.Admin
import models.Patient
import models.Staff

class AuthorizationTests extends PlayBrowserSpec with UserLogin {

  describe("Access to Admin Controller") {
    it("should redirect to login page if no user is present") {
      Given("there is no logged in subject")
      When("visiting the /admin route")
      go to (host + "/admin")
      Then("The app should redirect to login page")
      pageSource should include("¡Bienvenido!")
    }

    it("should grant access on correct authorization") {
      Given("there is a logged in subject")
      val admin = login[Admin]
      When("visiting the /admin route")
      go to (host + "/admin")
      Then("The app should redirect to admin main page (allowing access)")
      pageSource should include(admin.getEmail())
      logout(admin)
    }

    it("should return 403 - forbidden on incorrect authorization") {
      Given("there is a logged in subject with other than ADMIN roles")
      val staff = login[Staff]
      When("visiting the /admin route")
      go to (host + "/admin")
      Then("The app should redirect to 403 - forbidden page")
      pageSource should include("Prohibido")
      logout(staff)
    }
  }

  describe("Access to Staff Controller") {
    it("should redirect to login page if no user is present") {
      Given("there is no logged in subject")
      When("visiting the /staff route")
      go to (host + "/staff")
      Then("The app should redirect to login page")
      pageSource should include("¡Bienvenido!")
    }

    it("should grant access on correct authorization") {
      Given("there is a logged in subject")
      val staff = login[Staff]
      When("visiting the /staff route")
      go to (host + "/staff")
      Then("The app should redirect to staff main page (allowing access)")
      pageSource should include(staff.getFullName())
      logout(staff)
    }

    it("should return 403 - forbidden on incorrect authorization") {
      Given("there is a logged in subject with other than STAFF roles")
      val patient = login[Patient]
      When("visiting the /staff route")
      go to (host + "/staff")
      Then("The app should redirect to 403 - forbidden page")
      pageSource should include("Prohibido")
      logout(patient)
    }
  }

  describe("Access to Patient Controller") {
    it("should redirect to login page if no user is present") {
      Given("there is no logged in subject")
      When("visiting the /patient route")
      go to (host + "/patient")
      Then("The app should redirect to login page")
      pageSource should include("¡Bienvenido!")
    }

    it("should grant access on correct authentication") {
      Given("there is a logged in subject")
      val patient = login[Patient]
      When("visiting the /patient route")
      go to (host + "/patient")
      Then("The app should redirect to staff main page (allowing access)")
      pageSource should include(patient.getPersonalInfo().getFullName())
      logout(patient)
    }

    it("should return 403 - forbidden on incorrect authentication") {
      Given("there is a logged in subject with other than PATIENT roles")
      val staff = login[Staff]
      When("visiting the /patient route")
      go to (host + "/patient")
      Then("The app should redirect to 403 - forbidden page")
      pageSource should include("Prohibido")
      logout(staff)
    }
  }
}