package content;

import java.io.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.omg.SendingContext.RunTime;

import javax.management.RuntimeErrorException;

public interface Uploader {
    static abstract class UploaderException extends RuntimeException {
        private final String message;
        private final Throwable throwable;
        public UploaderException(String message, Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
        }
    }
    static class AWSException extends UploaderException {
        public AWSException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
    static class FileWriterException extends UploaderException {
        public FileWriterException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }

    public InputStream read(String key) throws UploaderException;
    public void write(String key, File file) throws UploaderException;

}
