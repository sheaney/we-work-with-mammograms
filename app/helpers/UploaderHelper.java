package helpers;

import content.FileWriter;
import content.S3Uploader;
import content.Uploader;
import play.Play;
import play.libs.F;

/**
 * Created by sheaney on 5/13/14.
 */
public class UploaderHelper {
    public static F.Tuple<Uploader, String> obtainUploaderAndLogMsg(boolean upload) {
        String logMsg = "";

        // Obtain uploader
        Boolean uploadConfig = Play.application().configuration().getBoolean("s3Upload");
        boolean s3Upload = uploadConfig != null && uploadConfig;
        Uploader uploader = s3Upload ? new S3Uploader() : new FileWriter();
        if (upload) {
            logMsg = s3Upload ? "Uploading mammogram image to s3" : "Writing mammogram image to disk";
        } else {
            logMsg = s3Upload ? "Reading mammogram image from s3" : "Reading mammogram image from disk";
        }

        F.Tuple<Uploader, String> uploaderAndLog = new F.Tuple(uploader, logMsg);

        return uploaderAndLog;
    }

}
