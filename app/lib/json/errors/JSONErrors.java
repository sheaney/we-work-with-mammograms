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

    public static ObjectNode studyCreationErrors(String msg) {
        List<String> errorMsgs = new LinkedList<>();
        errorMsgs.add(msg);

        ObjectNode errors = Json.newObject();
        errors.put(MSG, Json.toJson(errorMsgs));

        return errors;
    }

    public static ObjectNode undefinedPatient() {
        ObjectNode json = Json.newObject();
        json.put(MSG, "El paciente no existe");
        return json;
    }

    public static ObjectNode undefinedStaff() {
        ObjectNode json = Json.newObject();
        json.put(MSG, "El personal no existe");
        return json;
    }

    public static ObjectNode undefinedMammogram() {
        ObjectNode json = Json.newObject();
        json.put(MSG, "La mamografía no existe");
        return json;
    }

    public static ObjectNode forbiddenAccess() {
        ObjectNode json = Json.newObject();
        json.put(MSG, "No tiene permiso para acceder a este paciente");
        return json;
    }

    public static ObjectNode undefined(String msg) {
        return Json.newObject().put("UNDEFINED",msg);
    }

    public static ObjectNode notFound(String msg) {
        return Json.newObject().put("NOT_FOUND", msg);
    }

    public static ObjectNode ok(String msg) {
        return Json.newObject().put("STATUS", msg);
    }

    public static ObjectNode notAllowed(String msg){
        return Json.newObject().put("NOT ALLOWED",msg);
    }

}
