package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import lib.json.errors.JSONErrors;
import lib.json.models.*;
import models.*;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Controller;
import security.ServiceDeadboltHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

import static lib.json.JSONConstants.*;

/**
 * Created by fernando on 5/9/14.
 */

@Restrict(value={@Group("SERVICE")}, handler = ServiceDeadboltHandler.class)
public class ServiceAPI extends Controller {

    public static Result test(){
        return ok(JSONErrors.ok());
    }

    public static Result getPatients(){
        return ok(JSONPatient.getServicePatientIds());
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

    public static Result getServiceStudy(Long sid){
        Study study = Study.findById(sid);
        if(study != null){
            return ok(JSONStudy.serviceStudy(study));
        }
        return notFound(JSONErrors.notFound());
    }

    public static Result getServiceComment(Long cid){
        Comment comment = Comment.findById(cid);
        if(comment != null){
            return ok(JSONComment.serviceComment(comment));
        }
        return notFound(JSONErrors.notFound());
    }

    public static Result getServiceMammogram(Long mid){
        Mammogram mam = Mammogram.findById(mid);
        if(mam != null){
            return ok(JSONMammogram.serviceMammogram(mam));
        }
        return notFound(JSONErrors.notFound());
    }

    public static Result getServiceAnnotation(Long aid){
        Annotation ann = Annotation.findById(aid);
        if(ann != null){
            return ok(JSONAnnotation.serviceAnnotation(ann));
        }
        return notFound(JSONErrors.notFound());
    }

    public static Result getSchema(Long pid){
        Patient patient = Patient.findById(pid);
        if(patient != null) {
            ObjectNode schema = Json.newObject();
            schema.put(ID,pid);
            List<Study> studies = patient.getStudies();

            ObjectNode allStudies = Json.newObject();
            for(Study study: studies){
                Long sid = study.getId();
                ObjectNode oneStudy = Json.newObject();

                oneStudy.put(ID,sid);
                oneStudy.put(COMMENTS,JSONStudy.getCommentIds(study));

                List<Mammogram> mammograms = study.getMammograms();
                ObjectNode allMammograms = Json.newObject();
                for(Mammogram mam: mammograms){
                    Long mid = mam.getId();
                    ObjectNode oneMammogram = Json.newObject();
                    oneMammogram.put(ID,mid);
                    oneMammogram.put(ANNOTATIONS,JSONMammogram.getAnnotationIds(mam));
                    allMammograms.put(MAMMOGRAM+"-" + mid, oneMammogram);
                }

                oneStudy.put(MAMMOGRAMS,allMammograms);
                allStudies.put(STUDY+"-"+sid,oneStudy);
            }

            schema.put(STUDIES,allStudies);
            return ok(schema);
        }
        return notFound(JSONErrors.notFound());
    }


}
