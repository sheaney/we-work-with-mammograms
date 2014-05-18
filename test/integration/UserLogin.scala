package integration

import scala.reflect.ClassTag

import factories.Factories
import models.Admin
import models.Patient
import models.Staff
import play.db.ebean.Model

trait UserLogin extends Factories { self: PlayBrowserSpec =>

  def login[T <: Model: ClassTag]: T = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    val user = createUser[T]
    val (email, pwd, shortName) = getEmailAndPasswordAndShortName(user)
    
    Given(s"${clazz.getName} is about to log in")
    go to (host + "/")
    pageSource should include("¡Bienvenido!")

    emailField("email").value = email
    pwdField("password").value = pwd
    
    click on ("login")
    
    user match {
      case _: Staff =>
        pageSource should include("Compartidos")
        pageSource should include("Propios")
        pageSource should include(shortName)
      case _: Admin =>
        pageSource should include(email)
      case _: Patient =>
        pageSource should include(shortName)
    }

    // Return logged in user
    user
  }

  def logout[T <: Model: ClassTag](user: T): Unit = {
    Then(s"${implicitly[ClassTag[T]].runtimeClass.getName} successfully logs out")

    click on id("logout")
    pageSource should include("¡Bienvenido!")

    user.delete
  }
  
  def createUser[T <: Model: ClassTag]: T = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    
    val user =
      clazz match {
        case x if x == classOf[Patient] =>
          samplePatient
        case x if x == classOf[Staff] =>
          sampleStaff
        case x if x == classOf[Admin] =>
          sampleAdmin
      }
    
    user.save
    user.asInstanceOf[T]
  }
  
  private def getEmailAndPasswordAndShortName[T <: Model: ClassTag](user: T): (String, String, String) = {
    user match {
      case staff: Staff =>
        (staff.getEmail, staff.getPassword, staff.getShortName)
      case admin: Admin =>
        (admin.getEmail, admin.getPassword, "")
      case patient: Patient =>
        (patient.getPersonalInfo.getEmail, patient.getPersonalInfo.getPassword, patient.getPersonalInfo.getShortName)
      case _ =>
        fail(s"Could not obtain user and password for ${implicitly[ClassTag[T]].runtimeClass}")
    }
  }

}