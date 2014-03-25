package controllers;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

@Singleton
public class Application extends Controller {
	
	@Inject
	public Application() {}

	@Inject
    public Result index() {
        return ok(index.render());
    }

	@Inject
    public Result staff() {
        return ok(staff.render("Juanito"));
    }

	@Inject
    public Result newPatient() {
        return ok(newPatient.render("Juanito"));
    }

	@Inject
    public Result newStudy() {
        return ok(newStudy.render("Juanito"));
    }

	@Inject
    public Result study(Long id) {
        return ok(study.render(id, "Juanito"));
    }

	@Inject
    public Result editStudy(Long id) {
        return ok(editStudy.render(id, "Juanito"));
    }

}
