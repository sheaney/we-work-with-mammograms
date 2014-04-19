package integration

import models.{ Admin, Staff, Patient, PersonalInfo, MedicalInfo }
import com.avaje.ebean.Ebean
import scala.reflect.ClassTag
import play.db.ebean.Model
import factories.Factories

trait UserLogin extends Factories { self: PlayBrowserSpec =>

  def login[T <: Model: ClassTag]: T = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    val user = createUser[T]
    val (email, pwd) = getEmailAndPassword(user)

    Given(s"${clazz.getName} is logged in")
    go to (host + "/")
    pageSource should include("¡Bienvenido!")

    emailField("email").value = email
    pwdField("password").value = pwd

    click on ("login")

    // for now, later it should include the name
    pageSource should include(email)
    
    user match {
      case _: Staff =>
        pageSource should include("Compartidos")
        pageSource should include("Propios")
      case _ =>
        
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
  
  private def getEmailAndPassword[T <: Model: ClassTag](user: T): (String, String) = {
    user match {
      case staff: Staff =>
        (staff.getEmail, staff.getPassword)
      case admin: Admin =>
        (admin.getEmail, admin.getPassword)
      case patient: Patient =>
        (patient.getPersonalInfo.getEmail, patient.getPersonalInfo.getPassword)
      case _ =>
        fail(s"Could not obtain user and password for ${implicitly[ClassTag[T]].runtimeClass}")
    }
  }

}