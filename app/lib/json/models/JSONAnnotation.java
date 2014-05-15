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

    private static final String ANNOTATED = "annotatedMammogram";
    private static final String ANNOTATOR = "annotatorStaff";

    public static ObjectNode serviceAnnotation(Annotation ann) {
        ObjectNode jsonServiceAnnotation = Json.newObject();
        jsonServiceAnnotation.put(ID,ann.getId());
        jsonServiceAnnotation.put(CREATED_AT,ann.getCreatedAt().getTime());
        jsonServiceAnnotation.put(CONTENT,ann.getContent());
        jsonServiceAnnotation.put(ANNOTATED_MAMMOGRAM,ann.getAnnotated().getId());
        Staff annotator = ann.getAnnotator();
        if(annotator != null)
            jsonServiceAnnotation.put(ANNOTATOR, annotator.getId());
        else
            jsonServiceAnnotation.put(SERVICE_ANNOTATOR,ann.getServiceAnnotator().getId());
        return jsonServiceAnnotation;
    }
}
