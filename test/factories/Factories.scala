package factories

import java.text.SimpleDateFormat

import models.Admin
import models.MedicalInfo
import models.Patient
import models.PersonalInfo
import models.Staff

trait Factories {

  def sampleStaff = {
    val ft = new SimpleDateFormat("dd/MM/yyyy")

    val s = new Staff
    s.setName("Juan Estefano")
    s.setFirstLastName("Rodríguez")
    s.setSecondLastName("Heaney")
    s.setEmail("j.e.r.h@gmail.com")
    s.setBirthdate(ft.parse("29/07/1985"))
    s.setCedula("AQWERTYGSDGN")
    s.setRFC("83473847asfdjklj")
    s.setPassword("secret")
    s
  }

  def sampleAdmin = {
    val a = new Admin
    a.setEmail("xyz@xyz.com")
    a.setPassword("3847aslkfjx45")
    a
  }

  def samplePatient: Patient = {
    val p = new Patient
    p.setPersonalInfo(samplePersonalInfo)
    p.setMedicalInfo(sampleMedicalInfo)
    p.setViewComments(true)
    p.setViewAnnotations(true)
    p
  }

  private def samplePersonalInfo = {
    val ft = new SimpleDateFormat("dd/MM/yyyy")

    val pi = new PersonalInfo
    pi.setName("María")
    pi.setFirstLastName("Dávalos")
    pi.setSecondLastName("González")
    pi.setAddress("Bosque Mágico 123, Col. Robledo, Mty N.L.")
    pi.setEmail("mdavalos@xyz.com")
    pi.setTelephone("12345678")
    pi.setBirthdate(ft.parse("21/02/1978"))
    pi.setPassword("123xysdf938dfx948")
    pi
  }

  private def sampleMedicalInfo = {
    val mi = new MedicalInfo
    mi.setSexualActivityStartAge(18)
    mi.setPregnancies(1)
    mi.setcSections(0)
    mi.setNaturalDeliveries(1)
    mi.setAbortions(0)
    mi.setMenopauseStartAge(34)
    mi.setFamilyPredisposition(true)
    mi.setHormonalReplacementTherapy(true)
    mi.setPreviousMammaryDiseases(false)
    mi.setMenstrualPeriodStartAge(15)
    mi.setBreastfedChildren(true)
    mi
  }
}