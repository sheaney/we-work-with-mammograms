package content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by sheaney on 5/8/14.
 */
public class FileWriter implements Uploader {

    public InputStream read(String key) throws FileNotFoundException {
        System.out.println(key);
        File file = new File(key);
        FileInputStream fileInput = new FileInputStream(file);

        return fileInput;
    }

    public void write(String key, File file) {
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
            System.out.println(outputFile.lastModified());

            ImageIO.write(bi, fileExtension, outputFile);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
