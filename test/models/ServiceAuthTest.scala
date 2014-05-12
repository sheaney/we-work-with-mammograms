package models

import play.api.test.Helpers.running

/**
 * Created by fernando on 5/9/14.
 */
class ServiceAuthTest extends ModelsHelper{

  describe("Constructor method"){
    it("should generate a token string"){
      running(app) {
        val email = "something@somethingelse.com"
        val service = new ServiceAuth(email)
        service.getAuthToken should not be null
      }
    }
  }

  describe("Method verify() of the ServiceAuthTest"){
    it("should return true if the authToken is in the database") {
      running(app) {
        val service = new ServiceAuth("something@somethingelse.com")
        service.save()
        val verified = ServiceAuth.verifyService(service.getAuthToken)
        verified should be(true)
      }
    }

    it("should return false if the token is nowhere to be found"){
      running(app) {
        val notVerified = ServiceAuth.verifyService("not a real token")
        notVerified should be(false)
      }
    }
  }
}