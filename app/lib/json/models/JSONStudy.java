package lib.json.models;

import static lib.json.JSONConstants.ID;
import static lib.json.JSONConstants.CREATED_AT;
import static lib.json.JSONConstants.MAMMOGRAMS;
import static lib.json.JSONConstants.COMMENTS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import models.Study;
import play.libs.Json;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernando on 5/11/14.
 */

public class JSONStudy {

    private final static String OWNER = "owner";

    public static ObjectNode serviceStudy(Study study) {
        ObjectNode jsonStudy = Json.newObject();
        jsonStudy.put(ID,study.getId());
        jsonStudy.put(CREATED_AT,study.getCreatedAt().getTime());
        jsonStudy.put(OWNER,study.getOwner().getId());
        jsonStudy.put(COMMENTS,study.getComments().size());
        jsonStudy.put(MAMMOGRAMS,study.getMammograms().size());
        return jsonStudy;
    }

    public static JsonNode getCommentIds(Study study){
        List<Comment> comments = study.getComments();
        List<Long> ids = new ArrayList<Long>();
        for(Comment comment : comments){
            ids.add(comment.getId());
        }
        return Json.toJson(ids);
    }
}
