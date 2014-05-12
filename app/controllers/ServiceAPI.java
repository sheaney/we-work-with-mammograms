package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import lib.json.errors.JSONErrors;
import lib.json.models.JSONPatient;
import models.Patient;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Controller;
import security.ServiceDeadboltHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by fernando on 5/9/14.
 */

@Restrict(value={@Group("SERVICE")}, handler = ServiceDeadboltHandler.class)
public class ServiceAPI extends Controller {

    public static Result test(){
        return ok(JSONErrors.ok());
    }

    public static Result getPatients(){
        return ok(JSONPatient.allPatientsService());
    }

    public static Result getServicePatient(Long id, String info){
        Patient patient = Patient.findById(id);
        if(patient != null) {
            ObjectNode servicePatient = JSONPatient.servicePatient(patient,info);
            if(servicePatient.has("UNDEFINED")) {//wrong info parameter
                return badRequest(servicePatient);
            }else{
                return ok(servicePatient);
            }
        }else{ //patient doesn't exist
            return notFound(JSONErrors.notFound());
        }
    }
}
