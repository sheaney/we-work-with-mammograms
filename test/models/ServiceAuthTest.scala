package models

import integration.PlayBrowserSpec

class ServiceAuthTest extends PlayBrowserSpec {

  describe("Constructor method"){
    it("should generate a token string"){
      val email = "something@somethingelse.com"
      val service = new ServiceAuth(email)
      service.getAuthToken should not be null
    }
  }

  describe("#verify"){
    it("should return the serviceAuth model if the authToken is in the database") {
      val service = new ServiceAuth("something@somethingelse.com")
      service.save()
      val verified = ServiceAuth.verifyService(service.getAuthToken)
      verified should not be null
    }

    it("should return null if the token is nowhere to be found"){
      val notVerified = ServiceAuth.verifyService("not a real token")
      notVerified shouldBe null
    }
  }
}