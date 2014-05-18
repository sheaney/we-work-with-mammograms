package content;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by sheaney on 5/8/14.
 */
public class S3Uploader implements Uploader {
    public static final String BUCKET_NAME = "wwwm";

    public void write(String key, File file) throws AWSException {
        AWSCredentials credentials = getCredentials();

        AmazonS3 s3 = new AmazonS3Client(credentials);

        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        try {
            s3.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
        }  catch (Exception e) {
            throw new AWSException(e.getMessage(), e);
        }

    }

    public BufferedImage read(String key) throws AWSException {
        AWSCredentials credentials = getCredentials();
        AmazonS3 s3 = new AmazonS3Client(credentials);

        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        try {
            S3Object s3Object = s3.getObject(new GetObjectRequest(BUCKET_NAME, key));

            return ImageIO.read(s3Object.getObjectContent());
        }  catch (Exception e) {
            throw new AWSException(e.getMessage(), e);
        }

    }

    public void testConnection() throws AWSException {
        AWSCredentials credentials = getCredentials();
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        try {
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
        } catch (Exception e) {
            throw new AWSException(e.getMessage(), e);
        }
    }

    private AWSCredentials getCredentials() {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/config).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AWSException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentials;
    }
}
