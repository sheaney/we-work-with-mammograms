package lib.json.models;

import static lib.json.JSONConstants.ID;
import static lib.json.JSONConstants.CREATED_AT;
import static lib.json.JSONConstants.CONTENT;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Annotation;
import play.libs.Json;

/**
 * Created by fernando on 5/12/14.
 */
public class JSONAnnotation {

    private static final String ANNOTATED = "annotatedMammogram";
    private static final String ANNOTATOR = "annotatorStaff";

    public static ObjectNode serviceAnnotation(Annotation ann) {
        ObjectNode jsonServiceAnnotation = Json.newObject();
        jsonServiceAnnotation.put(ID,ann.getId());
        jsonServiceAnnotation.put(CREATED_AT,ann.getCreatedAt().getTime());
        jsonServiceAnnotation.put(CONTENT,ann.getContent());
        jsonServiceAnnotation.put(ANNOTATED,ann.getAnnotated().getId());
        jsonServiceAnnotation.put(ANNOTATOR,ann.getAnnotator().getId());
        return jsonServiceAnnotation;
    }
}
