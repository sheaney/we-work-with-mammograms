package controllers;

import java.io.File;
import java.util.*;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import content.Uploader;
import controllers.validations.APIValidations;
import helpers.UploaderHelper;
import lib.PatientContainer;
import lib.json.errors.JSONErrors;
import lib.json.models.JSONAnnotation;
import lib.json.models.JSONMammogram;
import lib.json.models.JSONStaff;
import lib.permissions.PatientUpdateInfoPermission;
import models.*;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import views.html.showPatient;
import security.StandardDeadboltHandler;

import static play.mvc.BodyParser.*;

@Restrict(value={@Group("STAFF")}, handler = StandardDeadboltHandler.class)
public class API extends Controller {
	final static Form<PersonalInfo> personalInfoBinding = Form.form(PersonalInfo.class);
	final static Form<MedicalInfo> medicalInfoBinding = Form.form(MedicalInfo.class);
    final static Form<Annotation> annotationBinding = Form.form(Annotation.class);
    final static Form<Study> studyForm = Form.form(Study.class);

	public static Result staff() {
		List<Staff> staff = Staff.all();
		List<Staff> staffWithoutCurrent = APIHelpers.filterMatchingStaffFromList(staff, obtainStaff());
		return ok(Json.toJson(staffWithoutCurrent));
	}

	public static Result getStaff(Long id) {
		Staff staff = Staff.findById(id);
        if (staff == null)
            return notFound(JSONErrors.undefinedStaff());
		return ok(JSONStaff.staffWithPatients(staff));
	}

	public static Result getPatientInfo(Long id) {
		Staff staff = obtainStaff();
		Patient patient = Patient.findById(id);
		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound(JSONErrors.undefinedPatient());
		else if (patientContainer.isEmpty())
			return forbidden(JSONErrors.forbiddenAccess());

		// success
		return ok(JSONStaff.staffPatient(staff, patientContainer));
	}

    public static Result getMammogram(Long mid) {
        Mammogram mammogram = Mammogram.findById(mid);
        if (mammogram == null)
            return notFound(JSONErrors.undefinedMammogram()); // return json with error msg

        return ok(JSONMammogram.mammogramWithAnnotations(mammogram));

    }

    @Of(BodyParser.Json.class)
    public static Result createAnnotation(Long mid) {
        Mammogram mammogram = Mammogram.findById(mid);
        Staff annotator = obtainStaff();
        System.out.println("Submitting annotation");

        if (mammogram == null)
            return notFound(JSONErrors.undefinedMammogram());

        JsonNode jsonNode = request().body().asJson();
        Form<Annotation> binding = annotationBinding.bind(jsonNode);
        if (binding.hasErrors()) {
            return badRequest(binding.errorsAsJson());
        } else {
            Annotation annotation = binding.get();
            annotation.setAnnotator(annotator);
            annotation.setAnnotated(mammogram);
            annotation.save();
            return ok(JSONAnnotation.regularAnnotation(annotation));
        }
    }

	@Of(BodyParser.Json.class)
	public static Result updatePersonalInfo(Long id) {
		// Check that this patient actually exists
		Patient patient = Patient.findById(id);
		Staff staff = obtainStaff();

		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound("Patient doesn't exist.");

		if (new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges()).canUpdatePersonalInfo()) {
			JsonNode jsonNode = request().body().asJson();
			Form<PersonalInfo> binding = personalInfoBinding.bind(jsonNode);
			if (binding.hasErrors()) {
				return badRequest(Json.toJson(JSONErrors.patientInfoErrors(getErrors(binding))));
			} else {
				PersonalInfo info = binding.get();
				patient.setPersonalInfo(info);
				patient.getPersonalInfo().update();
				return ok("Success");
			}
		} else

			return unauthorized("Can't update info.");
	}

	@Of(BodyParser.Json.class)
	public static Result updateMedicalInfo(Long id) {
		// Check that this patient actually exists
		Patient patient = Patient.findById(id);
		Staff staff = obtainStaff();

		PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

		if (patientContainer == null)
			return notFound("Patient doesn't exist.");

		else if (new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges()).canUpdateMedicalInfo()) {
			JsonNode jsonNode = request().body().asJson();
			Form<MedicalInfo> binding = medicalInfoBinding.bind(jsonNode);
			if (binding.hasErrors()) {
                return badRequest(Json.toJson(JSONErrors.patientInfoErrors(getErrors(binding))));
			} else {
				MedicalInfo info = binding.get();
				patient.setMedicalInfo(info);
				patient.getMedicalInfo().update();
				return ok("success");
			}
		} else
			return unauthorized("Can't update info");

	}

    public static F.Promise<Result> updateStudy(final Long pid, final Long sid) {
        return F.Promise.promise(new F.Function0<Result>() {
            final Patient patient = Patient.findById(pid);
            final Study studyToUpdate = Study.findById(sid);
            final Staff staff = obtainStaff();

            @Override
            public Result apply() throws Throwable {
                PatientContainer patientContainer = APIValidations.getPatientAccess(staff, patient);

                if (patientContainer == null)
                    return notFound("Patient doesn't exist.");

                if (studyToUpdate == null)
                    return notFound("Study doesn't exist.");

                if (!new PatientUpdateInfoPermission(patientContainer.getAccessPrivileges()).canUpdateStudies())
                    return unauthorized("Can't update info");

                Form<Study> binding = studyForm.bindFromRequest();
                if (binding.hasErrors()) {
                    return badRequest(binding.errorsAsJson());
                } else {
                    Study study = binding.get();

                    // Set commenter for comments added to study
                    Iterator<Comment> comments = study.getComments().iterator();
                    while (comments.hasNext()) {
                        Comment comment = comments.next();
                        if (comment.getContent().matches("\\s*")) {
                            comments.remove();
                        } else {
                            Logger.info(String.format("Adding comment by %s", staff.getFullName()));
                            comment.setCommenter(staff);
                            comment.setCommented(studyToUpdate);
                            comment.save();
                        }
                    }

                    Http.MultipartFormData body = request().body()
                            .asMultipartFormData();
                    List<Http.MultipartFormData.FilePart> parts = body.getFiles();
                    // Need to verify that all parts are image files first
                    for (Http.MultipartFormData.FilePart part : parts) {
                        if (!part.getContentType().matches("image/.*")) {
                            return badRequest(JSONErrors.studyCreationErrors("Solo se pueden subir imágenes"));
                        }
                    }

                    // Obtain uploader
                    F.Tuple<Uploader, String> uploaderAndLog = UploaderHelper.obtainUploaderAndLogMsg(true);
                    Uploader uploader = uploaderAndLog._1;
                    String logMsg = uploaderAndLog._2;

                    // Create mammograms and persist images
                    for (Http.MultipartFormData.FilePart part : parts) {
                        // Associate mammogram with study
                        Mammogram mammogram = new Mammogram();
                        mammogram.setStudy(studyToUpdate);
                        mammogram.save();

                        // Calculate mammogram key and update with value
                        Long studyId = studyToUpdate.getId();
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

                    studyToUpdate.save();
                }

                flash("success", "Se ha actualizado el estudio del paciente el paciente");
                boolean updatedABorrowedPatient = staff.findBorrowedPatient(patient) != null;
                return ok(showPatient.render(patient.getId(), session().get("user"), updatedABorrowedPatient));
            }

        });
    }

    public static Staff obtainStaff() {
		Long staffId = Long.parseLong(session().get("id"));
		return Staff.findById(staffId);
	}

	private static <T> Map<String, String> getErrors(Form<T> form) {
		Map<String, String> errors = new HashMap<String, String>();

		for (Map.Entry<String, List<ValidationError>> entry : form.errors().entrySet()) {
			String field = entry.getKey();
			List<ValidationError> validationErrors = entry.getValue();
			List<String> messages = new LinkedList<String>();
			for (ValidationError ve : validationErrors) {
				String msg = Messages.get(ve.message(), ve.arguments());
				messages.add(msg);
			}
			String concatMessages = concatenateStrings(messages, "\n");
			errors.put(field, concatMessages);
		}

		return errors;
	}

	private static String concatenateStrings(List<String> strings, String sep) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings)
			sb.append(s).append(sep);
		return sb.toString();
	}
}
