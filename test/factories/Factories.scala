package factories

import java.text.SimpleDateFormat
import models._

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
  
  trait patientFactory {
    val id: Long
    def value: Patient = {
      val p = new Patient
      p.setId(id)
      p
    }
  }
  
  trait staffFactory {
    val id: Long
    def value: Staff = {
      val s = new Staff
      s.setId(id)
      s
    }
  }
  
  trait sharedPatientFactory {
    val id: Long
    val sharer: Staff
    val borrower: Staff
    val sharedInstance: Patient
    val accessPrivileges: Int
    def value: SharedPatient = {
      val sp = new SharedPatient(sharer, borrower, sharedInstance, accessPrivileges)
      sp.setId(id)
      sp
    }
  }

  trait studyFactory {
    val id: Long
    def value: Study = {
      val s = new Study
      s.setId(id)
      s
    }
  }

  def sampleAdmin = {
    val a = new Admin
    a.setEmail("xyz@xyz.com")
    a.setPassword("3847aslkfjx45")
    a
  }

  def sampleStudy: Study = {
    val s = new Study
    s
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