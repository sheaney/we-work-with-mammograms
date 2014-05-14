package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import com.fasterxml.jackson.databind.JsonNode;
import lib.json.errors.JSONErrors;
import lib.json.models.*;
import models.*;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Controller;
import security.ServiceDeadboltHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedList;
import java.util.List;

import static lib.json.JSONConstants.*;

/**
 * Created by fernando on 5/9/14.
 */

@Restrict(value={@Group("SERVICE")}, handler = ServiceDeadboltHandler.class)
public class ServiceAPI extends Controller {

    final static Form<Comment> commentBinding = Form.form(Comment.class);
    final static Form<Annotation> annotationBinding = Form.form(Annotation.class);

    public static Result test(){
        return ok(JSONErrors.ok("Service API is working!"));
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
            return notFound(JSONErrors.notFound("Patient not found."));
        }
    }

    public static Result getServiceStudy(Long sid){
        Study study = Study.findById(sid);
        if(study != null){
            return ok(JSONStudy.serviceStudy(study));
        }
        return notFound(JSONErrors.notFound("Study not found."));
    }

    public static Result getServiceComment(Long cid){
        Comment comment = Comment.findById(cid);
        if(comment != null){
            return ok(JSONComment.serviceComment(comment));
        }
        return notFound(JSONErrors.notFound("Comment not found."));
    }

    public static Result getServiceMammogram(Long mid){
        Mammogram mam = Mammogram.findById(mid);
        if(mam != null){
            return ok(JSONMammogram.serviceMammogram(mam));
        }
        return notFound(JSONErrors.notFound("Mammogram not found."));
    }

    public static Result getServiceAnnotation(Long aid){
        Annotation ann = Annotation.findById(aid);
        if(ann != null){
            return ok(JSONAnnotation.serviceAnnotation(ann));
        }
        return notFound(JSONErrors.notFound("Annotation not found."));
    }

    public static Result getSchema(Long pid){
        Patient patient = Patient.findById(pid);
        if(patient != null) {
            ObjectNode schema = Json.newObject();
            schema.put(ID,pid);
            List<Study> studies = patient.getStudies();
            List<ObjectNode> studiesJson = new LinkedList<>();

            ObjectNode allStudies = Json.newObject();
            for(Study study: studies){
                ObjectNode studyJson = Json.newObject();
                studyJson.put(ID, study.getId());
                studyJson.put(COMMENTS, JSONStudy.getCommentIds(study));
                List<ObjectNode> mammogramsJson = new LinkedList<>();

                for (Mammogram mammogram : study.getMammograms()) {
                    ObjectNode mammogramJson = Json.newObject();
                    mammogramJson.put(ID, mammogram.getId());
                    mammogramJson.put(ANNOTATIONS, JSONMammogram.getAnnotationIds(mammogram));
                    mammogramsJson.add(mammogramJson);
                }

                studyJson.put(MAMMOGRAMS, Json.toJson(mammogramsJson));
                studiesJson.add(studyJson);
            }

            schema.put(STUDIES, Json.toJson(studiesJson));

            return ok(schema);
        }
        return notFound(JSONErrors.notFound("Patient not found."));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result putServiceComment(Long cid) {
        /*
            {content: "A string"
            commenter: StaffId
            commented: StudyId}
        */
        Comment com = Comment.findById(cid);
        JsonNode jsonRequest = request().body().asJson();
//        System.out.println(jsonRequest);
        String content = jsonRequest.get(CONTENT).asText();
        if(com != null) {
            com.setContent(content);
            com.update();
            return ok(JSONErrors.ok("Comment updated successfully"));
        }else{
            return notFound(JSONErrors.notFound("Comment not found"));
        }
    }

    public static Result postServiceComment(){
        Comment com = new Comment();
        JsonNode jsonRequest = request().body().asJson();
        String content = jsonRequest.get(CONTENT).asText();
        Study commented = Study.findById(jsonRequest.get(COMMENTED).asLong());
        Staff commenter = Staff.findById(jsonRequest.get(COMMENTER).asLong());
        if(commented == null)
            return notFound(JSONErrors.notFound("Study not found."));
        if(commenter == null)
            return notFound(JSONErrors.notFound("Commenting staff not found"));
        com.setCommented(commented);
        com.setCommenter(commenter);
        com.setContent(content);
        com.save();
        ObjectNode json = Json.newObject();
        json.put("id", com.getId());
        json.put("content", com.getContent());
        json.put("commenter", com.getCommenter().getId());
        json.put("commented", com.getCommented().getId());

        return ok(json);
    }

    public static Result putServiceAnnotation(Long aid){
        /*
            {content: "A string"
            annotatorStaff: StaffId
            annotatedMammogram: MammogramId}
        */
        Annotation ann = Annotation.findById(aid);
        JsonNode jsonRequest = request().body().asJson();
        String content = jsonRequest.get(CONTENT).asText();
        if(ann != null) {
            ann.setContent(content);
            ann.update();
            return ok(JSONErrors.ok("Annotation updated successfully"));
        }else{
            Mammogram annotated = Mammogram.findById(jsonRequest.get(ANNOTATED).asLong());
            Staff annotator = Staff.findById(jsonRequest.get(ANNOTATOR).asLong());
            if(annotated == null)
                return notFound(JSONErrors.notFound("Mammogram not found."));
            if(annotator == null)
                return notFound(JSONErrors.notFound("Commenting staff not found"));
            ann = new Annotation();
            ann.setAnnotated(annotated);
            ann.setAnnotator(annotator);
            ann.setContent(content);
            ann.update();
            return ok(JSONErrors.ok("Annotation updated successfully"));
        }
    }
}
