package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result staff() {
        return ok(staff.render("Juanito"));
    }

    public static Result patient(){
        return ok(patient.render("Juanito"));
    }

    public static Result newPatient() {
        return ok(newPatient.render("Juanito"));
    }

    public static Result newStudy() {
        return ok(newStudy.render("Juanito"));
    }

    public static Result study(Long id) {
        return ok(study.render(id, "Juanito"));
    }

    public static Result editStudy(Long id) {
        return ok(editStudy.render(id, "Juanito"));
    }

    public static Result showPatient(Long id) {
        return ok(showPatient.render(id, "Juanito"));
    } 

}
