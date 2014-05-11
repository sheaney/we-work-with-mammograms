package content;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;

/**
 * Created by sheaney on 5/8/14.
 */
public class S3Uploader implements Uploader {
    public static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");
    public static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY");
    public static final String BUCKET_NAME = "wwwm";

    public void write(String key, File file) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY,
                SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);

        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        System.out.println("Uploading a new object to S3 from a file\n");

        try {
            s3.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it "
                            + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out
                    .println("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }

    }

    public InputStream read(String key) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY,
                SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);

        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        System.out.println("Reading an object from S3\n");
        try {
            S3Object s3Object = s3.getObject(new GetObjectRequest(BUCKET_NAME, key));

            return s3Object.getObjectContent();
        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it "
                            + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out
                    .println("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }

        return null;
    }

    public void testConnection() {
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,
                secretKey);
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.DEFAULT_REGION));

        try {
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it "
                            + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out
                    .println("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
