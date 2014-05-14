package lib.json.errors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONErrors {
	
	private final static String FIELD = "field";
	private final static String MSG = "msg";

	public static List<ObjectNode> patientInfoErrors(Map<String, String> errors) {
		List<ObjectNode> errorsLst = new LinkedList<ObjectNode>();
		
		for (Map.Entry<String, String> error : errors.entrySet()) {
			ObjectNode node = Json.newObject();
			String field = error.getKey();
			String msg = error.getValue();
			node.put(FIELD, field);
			node.put(MSG, msg);
			errorsLst.add(node);
		}
		
		return errorsLst;
	}

    public static ObjectNode undefined() {
        return Json.newObject().put("UNDEFINED","Undefined parameter.");
    }

    public static ObjectNode notFound(String msg) {
        return Json.newObject().put("NOT_FOUND", msg);
    }

    public static ObjectNode ok(String msg) {
        return Json.newObject().put("STATUS", msg);
    }
}
