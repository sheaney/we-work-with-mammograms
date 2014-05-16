package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import com.fasterxml.jackson.databind.JsonNode;
import content.Uploader;
import helpers.UploaderHelper;
import lib.json.errors.JSONErrors;
import lib.json.models.*;
import models.*;
import play.data.Form;
import play.libs.F;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Controller;
import security.AuthorizableService;
import security.AuthorizableUser;
import security.ServiceDeadboltHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private static Long getAuthorizableServiceId(){
        //this is only to get the id of the commenting service,
        //deadbolt does this to allow us into this method, but we do it again for the id
        ServiceDeadboltHandler sdbh = new ServiceDeadboltHandler();
        AuthorizableService as = (AuthorizableService)sdbh.getSubject(ctx());
        return Long.parseLong(as.getIdentifier());
    }

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
            {content: "A string"}
        */

        Comment com = Comment.findById(cid);
        if(com != null) {//comment to update exists
            Long serviceId = getAuthorizableServiceId();
            if(com.getServiceCommenter().getId() == serviceId) { //service is allowed to update
                //get data content from json request
                String content = null;
                try {
                    JsonNode jsonRequest = request().body().asJson();
                    content = jsonRequest.get(CONTENT).asText();
                } catch (Exception e) {
                    return badRequest(JSONErrors.undefined("Bad format request"));
                }

                if (content.matches("\\s*"))
                    return badRequest(JSONErrors.undefined("Update comment content is empty!"));

                com.setContent(content);
                com.update();
                //return as proof of updating
                ObjectNode result = JSONErrors.ok("Comment updated successfully");
                result.put("body",JSONComment.serviceComment(com));
                return ok(result);
            }else{
                return badRequest(JSONErrors.notAllowed("Service update not allowed in this comment"));
            }
        }else{
            return notFound(JSONErrors.notFound("Comment not found"));
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result putServiceAnnotation(Long aid){
        /*
            {content: "A string"}
        */

        Annotation ann = Annotation.findById(aid);
        if(ann != null) {//comment to update exists
            Long serviceId = getAuthorizableServiceId();
            if(ann.getServiceAnnotator().getId() == serviceId) { //service is allowed to update
                //get data content from json request
                String content = null;
                try {
                    JsonNode jsonRequest = request().body().asJson();
                    content = jsonRequest.get(CONTENT).asText();
                } catch (Exception e) {
                    return badRequest(JSONErrors.undefined("Bad format request"));
                }

                if (content.matches("\\s*"))
                    return badRequest(JSONErrors.undefined("Update annotation content is empty!"));

                ann.setContent(content);
                ann.update();
                //return as proof of updating
                ObjectNode result = JSONErrors.ok("Annotation updated successfully");
                result.put("body",JSONAnnotation.serviceAnnotation(ann));
                return ok(result);
            }else{
                return badRequest(JSONErrors.notAllowed("Service update not allowed in this annotation"));
            }
        }else{
            return notFound(JSONErrors.notFound("Annotation not found"));
        }
    }

    public static Result postServiceComment(){
        /*
            {content: "A string"
            commentedStudy: StudyId}
        */

        Long serviceId = getAuthorizableServiceId();

        //we pull everything we need from the json request/db
        String content = null;
        Long commentedId = null;
        try {
            JsonNode jsonRequest = request().body().asJson();
            content = jsonRequest.get(CONTENT).asText();
            commentedId = jsonRequest.get(COMMENTED_STUDY).asLong();
        } catch (Exception e) {
            return badRequest(JSONErrors.undefined("Bad format request"));
        }

        if (content.matches("\\s*"))
            return badRequest(JSONErrors.undefined("New comment content is empty!"));

        Study commented = Study.findById(commentedId);
        if(commented == null)
            return notFound(JSONErrors.notFound("Study not found."));

        //we don't need to check for this to be null because if it was, we wouldn't be here because of deadbolt2
        ServiceAuth commenter = ServiceAuth.findById(serviceId);

        //create and set attributes to the new comment
        Comment com = new Comment();
        com.setCommented(commented);
        com.setServiceCommenter(commenter);
        com.setContent(content);
        com.save();

        //return the saved object for the service to confirm
        ObjectNode result = JSONErrors.ok("Comment saved!");
        result.put("body",JSONComment.serviceComment(com));

        return ok(result);
    }

    public static Result postServiceAnnotation(){
        /*
            {content: "A string"
            annotatedMammogram: MammogramId}
        */

        Long serviceId = getAuthorizableServiceId();
        //we pull everything we need from the json request/db
        String content = null;
        Long mammogramId = null;
        try {
            JsonNode jsonRequest = request().body().asJson();
            content = jsonRequest.get(CONTENT).asText();
            mammogramId = jsonRequest.get(ANNOTATED_MAMMOGRAM).asLong();
        } catch (Exception e) {
            return badRequest(JSONErrors.undefined("Bad format request"));
        }
        if (content.matches("\\s*"))
            return badRequest(JSONErrors.undefined("New annotation content is empty!"));

        Mammogram annotated = Mammogram.findById(mammogramId);
        if(annotated == null)
            return notFound(JSONErrors.notFound("Mammogram not found."));

        //we don't need to check for this to be null because if it was, we wouldn't be here because of deadbolt2
        ServiceAuth annotator = ServiceAuth.findById(serviceId);

        //the comment to create in the database
        Annotation ann = new Annotation();

        ann.setAnnotated(annotated);
        ann.setServiceAnnotator(annotator);
        ann.setContent(content);
        ann.save();

        //return the saved object for the service to confirm
        ObjectNode result = JSONErrors.ok("Annotation saved!");
        result.put("body",JSONAnnotation.serviceAnnotation(ann));

        return ok(result);
    }

    public static Result renderMammogram(Long sid, Long mid){
        ByteArrayInputStream bais = null;
        try {
            // TODO Extract mammogram key into a utility helper method
            String key = String.format("images/study/%d/mammogram/%d", sid, mid);
            // Obtain reader
            F.Tuple<Uploader, String> readerAndLogMsg = UploaderHelper.obtainUploaderAndLogMsg(false);
            Uploader reader = readerAndLogMsg._1;
            System.out.println(readerAndLogMsg._2);
            BufferedImage image = reader.read(key);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            bais = new ByteArrayInputStream(baos.toByteArray());
        } catch (Uploader.UploaderException e) {
            return  notFound(Json.newObject().put("NOT_FOUND","Image not found"));
        } catch (IOException e) {
            return badRequest(Json.newObject().put("ERROR","Error writing/getting the image from memory"));
        }

        return ok(bais).as("image/png");
    }
}
