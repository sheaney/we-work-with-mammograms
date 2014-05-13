package content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by sheaney on 5/8/14.
 */
public class FileWriter implements Uploader {

    public BufferedImage read(String key) throws UploaderException {
        BufferedImage bufferedImage = null;
        File file = new File(key);
        try {
            bufferedImage = ImageIO.read(new FileInputStream(file));
        } catch (Exception e) {
            throw new FileWriterException(e.getMessage(), e);
        }

        return bufferedImage;
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

            ImageIO.write(bi, "png", outputFile);

        } catch (Exception e) {
            throw new FileWriterException(e.getMessage(), e);
        }

    }

}
