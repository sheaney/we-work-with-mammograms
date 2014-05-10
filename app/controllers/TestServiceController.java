package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Controller;
import security.ServiceDeadboltHandler;

/**
 * Created by fernando on 5/9/14.
 */

@Restrict(value={@Group("SERVICE")}, handler = ServiceDeadboltHandler.class)
public class TestServiceController extends Controller {

    public static Result test(){
        return ok(Json.newObject().put("STATUS", "OK"));
    }
}
