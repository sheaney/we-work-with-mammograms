package lib.json.models;

import static lib.json.JSONConstants.ANNOTATIONS;
import static lib.json.JSONConstants.ID;
import static lib.json.JSONConstants.CREATED_AT;
import static lib.json.JSONConstants.STUDY;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Annotation;
import models.Mammogram;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernando on 5/12/14.
 */
public class JSONMammogram {

    public static ObjectNode serviceMammogram(Mammogram mam) {
        ObjectNode jsonServiceMammogram = Json.newObject();
        jsonServiceMammogram.put(ID,mam.getId());
        jsonServiceMammogram.put(CREATED_AT,mam.getCreatedAt().getTime());
        jsonServiceMammogram.put(STUDY,mam.getStudy().getId());
        jsonServiceMammogram.put(ANNOTATIONS,mam.getAnnotations().size());
        //TODO Image link?
        return jsonServiceMammogram;
    }

    public static JsonNode getAnnotationIds(Mammogram mam){
        List<Annotation> annotations = mam.getAnnotations();
        List<Long> ids = new ArrayList<Long>();
        for(Annotation ann : annotations){
            ids.add(ann.getId());
        }
        return Json.toJson(ids);
    }

}
