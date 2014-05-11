package content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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
        String fileExtension = getFileExtension(file);
        String filePath = key + "." +  fileExtension;

        try {
            BufferedImage bi = ImageIO.read(file);
            // Create output file if it does not exist
            File outputFile = new File(filePath);
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            } else {
                outputFile.setLastModified(System.currentTimeMillis());
            }

            ImageIO.write(bi, fileExtension, outputFile);

        } catch (Exception e) {
            throw new FileWriterException(e.getMessage(), e);
        }

    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
