package lib.json.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import models.Staff;
import play.libs.Json;

import static lib.json.JSONConstants.*;

/**
 * Created by fernando on 5/11/14.
 */
public class JSONComment {


    public static ObjectNode serviceComment(Comment c){
        ObjectNode jsonServiceComment = Json.newObject();
        jsonServiceComment.put(ID,c.getId());
        jsonServiceComment.put(CONTENT,c.getContent());
        jsonServiceComment.put(CREATED_AT,c.getCreatedAt().getTime());
        Staff commenter = c.getCommenter();
        if (commenter != null)
            jsonServiceComment.put(STAFF_COMMENTER,commenter.getId());
        else
            jsonServiceComment.put(SERVICE_COMMENTER,c.getServiceCommenter().getId());
        jsonServiceComment.put(COMMENTED_STUDY,c.getCommented().getId());
        return jsonServiceComment;
    }

}
