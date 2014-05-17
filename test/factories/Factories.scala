package factories

import java.text.SimpleDateFormat
import models._
import scala.collection.JavaConverters._

trait Factories {

  def sampleService = {
    val s = new ServiceAuth("email@example.com")
    s.save
    s
  }

  def sampleStudy = {
    val study = new Study

    val patient = samplePatient
    patient.save

    study.setOwner(patient)
    study.save

    val staff = sampleStaff
    staff.save

    val comments = List(sampleComment(study,staff))
    study.setComments(comments.asJava)

    val mammograms = List(sampleMammogram(study,staff))
    study.setMammograms(mammograms.asJava)

    study.update
    study
  }

  def sampleMammogram(s:Study,staffForAnn:Staff):Mammogram = {
    val mam = new Mammogram
    mam.setStudy(s)
    mam.save
    mam.setAnnotations(List(sampleAnnotations(mam,staffForAnn)).asJava)
    mam.update
    mam
  }

  def sampleAnnotations(m:Mammogram,annotator:Staff):Annotation = {
    val ann1 = new Annotation
    ann1.setAnnotated(m)
    ann1.setContent("A not so random String")
    ann1.setAnnotator(annotator)
    ann1.save
    ann1
  }

  def sampleComment(s: Study, commenter: Staff): Comment = {
    val comment1 = new Comment
    comment1.setContent("Another not so random String")
    comment1.setCommented(s)
    comment1.setCommenter(commenter)
    comment1.save
    comment1
  }

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

  def samplePatient: Patient = {
    val p = new Patient
    p.setPersonalInfo(samplePersonalInfo)
    p.setMedicalInfo(sampleMedicalInfo)
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
