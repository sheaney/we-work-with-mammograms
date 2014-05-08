package controllers

import org.scalatest.Assertions._
import factories.Factories
import models.ModelsHelper

class APITest extends ModelsHelper with Factories {

  describe("Returns patient info if it exists") {
    //running(app) {
      val patient = new patientFactory { val id = 1L }.value

      //getPatientInfo(patient.getId())

    //}

  }

}