package snake.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;
public class FileTools {
    public static final int PROFILE_PIC_SIZE = 100;

    public static Image getImage(String filePath) throws IOException {
        URL inputStream = FileTools.class.getResource(filePath);

        return ImageIO.read(inputStream);
    }

    public static String encodeImageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {}
        return "";
    }
}

