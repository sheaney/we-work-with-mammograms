package lib.json.models;

import static lib.json.JSONConstants.CREATED_AT;
import static lib.json.JSONConstants.ID;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import play.libs.Json;

/**
 * Created by fernando on 5/11/14.
 */
public class JSONComment {
    private final static String CONTENT = "content";
    private final static String COMMENTER = "commenter";
    private final static String COMMENTED = "commented";

    public static ObjectNode serviceComment(Comment c){
        ObjectNode jsonServiceComment = Json.newObject();
        jsonServiceComment.put(ID,c.getId());
        jsonServiceComment.put(CONTENT,c.getContent());
        jsonServiceComment.put(CREATED_AT,c.getCreatedAt().getTime());
        jsonServiceComment.put(COMMENTER,c.getCommenter().getId());
        jsonServiceComment.put(COMMENTED,c.getCommented().getId());
        return jsonServiceComment;
    }

}
