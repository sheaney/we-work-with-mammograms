package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import controllers.validations.APIValidations;
import helpers.UploaderHelper;
import lib.PasswordGenerator;
import lib.PatientContainer;
import lib.json.errors.JSONErrors;
import lib.json.permissions.JSONPermissions;
import lib.permissions.PatientUpdateInfoPermission;
import lib.permissions.PatientViewInfoPermission;
import lib.permissions.Permission;
import models.*;
import play.Logger;
import views.html.*;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.fasterxml.jackson.databind.JsonNode;

import content.Uploader;

import javax.imageio.ImageIO;

import static play.libs.F.Tuple;

@Restrict(@Group({ "STAFF" }))
public class Staffs extends Controller {

	final static Form<Patient> patientForm = Form.form(Patient.class);
	final static Form<Study> newStudyForm = Form.form(Study.class);

	public static Result staff() {
		return ok(staff.render(session().get("user")));
	}

	public static Result newStudy(Long patientId) {
        // Check that this patient actually exists
        Patient patient = Patient.findById(patientId);
        Staff staff = API.obtainStaff();

        PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

        if (patientContainer == null)
            return notFound("El paciente no existe");

        PatientViewInfoPermission viewPermission = new PatientViewInfoPermission(patientContainer.getAccessPrivileges());
        if (!viewPermission.canViewStudies())
            return unauthorized("No tiene permiso para ver información del paciente");

        PatientUpdateInfoPermission updatePermission = new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges());
        if (!updatePermission.canUpdateStudies())
            return unauthorized("No tiene permisio para crear nuevos estudios");

		return ok(newStudy.render(patient, session().get("user"), newStudyForm));
	}

	public static Promise<Result> createNewStudy(final Long patientId) {
		Promise<Result> promise = Promise.promise(new Function0<Result>() {
			public final Patient patient = Patient.findById(patientId);
            public final Staff staff = Staff.findById(Long.parseLong(session().get("id")));

            // What to return in case of failure???
			@Override
			public Result apply() throws Throwable {
				Form<Study> filledForm = newStudyForm.bindFromRequest();
				if (filledForm.hasErrors()) {
					return badRequest(filledForm.errorsAsJson());
				} else {
                    Study study = filledForm.get();

                    // Set commenter for comments added to study
                    Iterator<Comment> comments = study.getComments().iterator();

                    while (comments.hasNext()) {
                        Comment comment = comments.next();
                        if (comment.getContent().matches("\\s*")) {
                            comments.remove();
                        } else {
                            Logger.info(String.format("Adding comment by %s", staff.getFullName()));
                            comment.setCommenter(staff);
                        }
                    }

					MultipartFormData body = request().body()
							.asMultipartFormData();
					List<MultipartFormData.FilePart> parts = body.getFiles();
					// Need to verify that all parts are image files first
                    for (MultipartFormData.FilePart part : parts) {
                        if (!part.getContentType().matches("image/.*")) {
                            return badRequest(JSONErrors.studyCreationErrors("Solo se pueden subir imágenes"));
                        }
                    }

                    // Obtain uploader
                    Tuple<Uploader, String> uploaderAndLog = UploaderHelper.obtainUploaderAndLogMsg(true);
                    Uploader uploader = uploaderAndLog._1;
                    String logMsg = uploaderAndLog._2;

                    // Associate study with patient
                    study.setOwner(patient);
                    study.save();

                    Long studyId = study.getId();

					// Create mammograms and persist images
					for (MultipartFormData.FilePart part : parts) {
                        // Associate mammogram with study
                        Mammogram mammogram = new Mammogram();
                        mammogram.setStudy(study);
                        mammogram.save();

                        // Calculate mammogram key and update with value
                        Long mammogramId = mammogram.getId();
                        String key = UploaderHelper.getMammogramImageKey(studyId, mammogramId);

                        // Upload to AWS s3 or write to disk
						File imageFile = part.getFile();
						Logger.info(logMsg);
                        try {
                            uploader.write(key, imageFile);
                        } catch (Uploader.FileWriterException fwe) {
                            Logger.error(fwe.getMessage());
                            return badRequest(JSONErrors.studyCreationErrors("No se puede guardar la toma en este momento"));
                        } catch (Uploader.AWSException awse) {
                            Logger.error(awse.getMessage());
                            return badRequest(JSONErrors.studyCreationErrors("No se puede subir la toma en este momento"));
                        }
					}
				}

                flash("success", "Se ha creado un nuevo estudio para el paciente");
                boolean updatedABorrowedPatient = staff.findBorrowedPatient(patient) != null;
                return ok(showPatient.render(patientId, session().get("user"), updatedABorrowedPatient));
			}

		});

		return promise;
	}

	public static Result study(Long patientId, Long id) {
        // Check that this patient actually exists
        Patient patient = Patient.findById(patientId);
        Staff staff = API.obtainStaff();

        PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

        if (patientContainer == null)
            return notFound("Patient doesn't exist.");

        Study s = Study.findById(id);
        if (s == null)
            return badRequest("El estudio no existe");

        PatientViewInfoPermission viewPermission = new PatientViewInfoPermission(patientContainer.getAccessPrivileges());
        if (!viewPermission.canViewStudies())
            return unauthorized("No tiene permiso para ver información del paciente");

        PatientUpdateInfoPermission updatePermission = new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges());
        return ok(study.render(id, session().get("user"), s, newStudyForm, updatePermission));
	}

	public static Result showPatient(Long id) {

		boolean borrowed;

		Patient patient = Patient.findById(id);
		Staff staff = API.obtainStaff();

		if(staff.findBorrowedPatient(patient) != null) {
			borrowed = true;
        } else {
			borrowed = false;
	    }

		return ok(showPatient.render(id, session().get("user"), borrowed));
	}

	public static Result editPatient(Long id) {
		return ok(editPatient.render(id, session().get("user")));
	}

	public static Result sharePatient(Long id) {
		return ok(sharePatient.render(id, session().get("user")));
	}

	public static Result createSharedPatient(Long id, Long borrowerId) {
		Patient patient = Patient.findById(id);
		Staff sharer = API.obtainStaff(); // get staff from session
		if (!sharer.canSharePatient(patient)) {
			return badRequest("Este paciente no te pertenece y no se puede compartir");
		}

		JsonNode jsonNode = request().body().asJson();
		// Create shared patient
		Staff borrower = Staff.findById(borrowerId);
		PatientViewInfoPermission patientViewInfo = JSONPermissions.unbindViewInfoPermissions(jsonNode);
		PatientUpdateInfoPermission patientUpdateInfo = JSONPermissions.unbindUpdateInfoPermissions(jsonNode);
		int accessPrivileges = Permission.concatAccessPrivileges(patientViewInfo, patientUpdateInfo);
		SharedPatient sharedPatient = new SharedPatient(sharer, borrower, patient, accessPrivileges);

		Logger.info("sharer = " + sharedPatient.getSharer().getName());
		Logger.info("borrower = " + sharedPatient.getBorrower().getName());
		Logger.info("shared patient = " + sharedPatient.getSharedInstance().getPersonalInfo().getName());
		Logger.info(String.valueOf(sharedPatient.getAccessPrivileges()));
		SharedPatient alreadySharedPatient = PatientContainer.getAlreadySharedPatient(sharedPatient, sharer, borrower);
		if (alreadySharedPatient != null) {
			alreadySharedPatient.setAccessPrivileges(sharedPatient.getAccessPrivileges());
			alreadySharedPatient.update();
			flash("success", "El paciente previamente compartido se ha modificado exitosamente");
			return ok("El paciente previamente compartido se ha modificado exitosamente");
		} else {
			SharedPatient.create(sharedPatient);
			flash("success", "El paciente se ha compartido exitosamente");
			return ok("success");
		}
	}

	public static Result newPatient() {
		return ok(newPatient.render(session().get("user"), patientForm));
	}

	public static Result createPatient() {
		Form<Patient> filledForm = patientForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(newPatient.render(session().get("user"),
					filledForm));
		} else {
			PasswordGenerator pg = new PasswordGenerator();
			Patient patient = filledForm.get();
			patient.getPersonalInfo().setPassword(pg.next());
			patient.setOwner(API.obtainStaff());
			Patient.create(patient);
			flash("success", "Un nuevo paciente se ha creado");
			return redirect(routes.Staffs.staff());
		}
  }

    public static Result showStaff(Long id) {
          return ok(showStaff.render(id, session().get("user")));
      }

    public static Result showMammogram(Long sid, Long mid) {
        Study study = Study.findById(sid);
        Mammogram mammogram = Mammogram.findById(mid);
        return ok(showMammogram.render(study, mammogram, session().get("user")));
    }

    public static Result renderMammogram(Long sid, Long mid) throws IOException {
        String key = UploaderHelper.getMammogramImageKey(sid, mid);
        // Obtain reader
        Tuple<Uploader, String> readerAndLogMsg = UploaderHelper.obtainUploaderAndLogMsg(false);
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
