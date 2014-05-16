package lib.json.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Annotation;
import models.Staff;
import play.libs.Json;

import static lib.json.JSONConstants.*;

/**
 * Created by fernando on 5/12/14.
 */
public class JSONAnnotation {

    public static ObjectNode serviceAnnotation(Annotation ann) {
        ObjectNode jsonServiceAnnotation = Json.newObject();
        jsonServiceAnnotation.put(ID,ann.getId());
        jsonServiceAnnotation.put(CREATED_AT,ann.getCreatedAt().getTime());
        jsonServiceAnnotation.put(CONTENT,ann.getContent());
        jsonServiceAnnotation.put(ANNOTATED_MAMMOGRAM,ann.getAnnotated().getId());
        Staff annotator = ann.getAnnotator();
        if(annotator != null)
            jsonServiceAnnotation.put(STAFF_ANNOTATOR, annotator.getId());
        else
            jsonServiceAnnotation.put(SERVICE_ANNOTATOR,ann.getServiceAnnotator().getId());
        return jsonServiceAnnotation;
    }

    public static ObjectNode regularAnnotation(Annotation annotation) {
        ObjectNode json = Json.newObject();
        json.put(ID, annotation.getId());
        json.put(CREATED_AT, annotation.getCreatedAt().getTime());
        json.put(CONTENT, annotation.getContent());
        Staff annotator = annotation.getAnnotator();
        if(annotator != null) {
            json.put(STAFF_ANNOTATOR, annotator.getShortName());
        }
        else {
            json.put(SERVICE_ANNOTATOR, "Servicio Externo");
        }

        return json;

    }
}
