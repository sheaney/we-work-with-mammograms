package content;

import java.awt.image.BufferedImage;
import java.io.*;

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

    public BufferedImage read(String key) throws UploaderException;
    public void write(String key, File file) throws UploaderException;

}
