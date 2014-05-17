package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import content.Uploader;
import controllers.validations.APIValidations;
import helpers.UploaderHelper;
import lib.PatientContainer;
import lib.json.errors.JSONErrors;
import models.Mammogram;
import models.Patient;
import models.Staff;
import models.Study;
import play.Logger;
import play.api.mvc.Call;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.patient;
import views.html.showMammogram;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Restrict(@Group({"PATIENT"}))
public class Patients extends Controller {
	
	public static Result patient() {
		return ok(patient.render(session().get("user")));
	}

    public static Result getPatient(Long id) {
        Patient patient = Patient.findById(id);

        if (getPatientFromSession().getId() != id) {
            return unauthorized(JSONErrors.forbiddenAccess());
        }

        return ok(Json.toJson(patient));
    }

    public static Patient getPatientFromSession() {
        Long patientId = Long.parseLong(session().get("id"));
        return Patient.findById(patientId);
    }

    public static Result showMammogram(Long sid, Long mid) {
        Study study = Study.findById(sid);
        Mammogram mammogram = Mammogram.findById(mid);
        Call renderAction = routes.Patients.renderMammogram(sid, mid);
        boolean isPatient = true;
        return ok(showMammogram.render(study, mammogram, session().get("user"), isPatient, renderAction));
    }

    public static Result renderMammogram(Long sid, Long mid) throws IOException {
        String key = UploaderHelper.getMammogramImageKey(sid, mid);
        // Obtain reader
        F.Tuple<Uploader, String> readerAndLogMsg = UploaderHelper.obtainUploaderAndLogMsg(false);
        Uploader reader = readerAndLogMsg._1;
        Logger.info(readerAndLogMsg._2);
        try {
            BufferedImage image = reader.read(key);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return ok(bais).as("image/png");
        } catch (IOException ioe) {
            Logger.error(ioe.getMessage());
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return notFound("Error al renderear mamografía");
    }
	
}
