package integration

import models.{ Admin, Staff, Patient, PersonalInfo }
import com.avaje.ebean.Ebean
import scala.reflect.ClassTag
import play.db.ebean.Model

trait UserLogin { self: PlayBrowserSpec =>

  def login[T <: Model: ClassTag](email: String, pwd: String): T = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    val user = createUser[T](email, pwd)

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

  def logout[T <: Model](user: T): Unit = {
    And(s"${user.toString} successfully logs out")

    click on id("logout")
    pageSource should include("¡Bienvenido!")

    user.delete
  }

  def createUser[T <: Model: ClassTag](email: String, pwd: String): T = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    val user = clazz.newInstance().asInstanceOf[T]

    user match {
      case staff: Staff =>
        staff.setEmail(email)
        staff.setPassword(pwd)
      case admin: Admin =>
        admin.setEmail(email)
        admin.setPassword(pwd)
      case patient: Patient =>
        val personalInfo = new PersonalInfo
        personalInfo.setEmail(email)
        personalInfo.setPassword(pwd)
        patient.setPersonalInfo(personalInfo)
      case _ =>
        fail(s"Could not create user for ${clazz.getName}")
    }

    user.save()
    user
  }

}