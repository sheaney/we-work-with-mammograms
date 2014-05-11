package content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sheaney on 5/8/14.
 */
public class FileWriter implements Uploader {

    public InputStream read(String key) throws UploaderException {
        FileInputStream fileInput = null;
        File file = new File(key);
        try {
            fileInput = new FileInputStream(file);
        } catch (Exception e) {
            throw new FileWriterException(e.getMessage(), e);
        }

        return fileInput;
    }

    public void write(String key, File file) throws UploaderException {
        String filePath = key;

        try {
            BufferedImage bi = ImageIO.read(file);
            // Create output file if it does not exist
            File outputFile = new File(filePath);
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            } else {
                outputFile.setLastModified(System.currentTimeMillis());
            }

            ImageIO.write(bi, getFileExtension(file), outputFile);

        } catch (Exception e) {
            throw new FileWriterException(e.getMessage(), e);
        }

    }

    public String getFileExtension(File file) throws UploaderException {
        Path path = Paths.get(file.getPath());
        String extension = null;

        try {
            String contentType = Files.probeContentType(path);
            String [] splitContentType = contentType.split("/");
            extension = splitContentType.length > 1 ? splitContentType[1] : splitContentType[0];
        } catch (IOException ioe) {
            throw new FileWriterException(ioe.getMessage(), ioe);
        }

        return extension;
    }

}
