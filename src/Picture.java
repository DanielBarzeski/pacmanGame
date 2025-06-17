import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {
    public static final BufferedImage
            HEART = getBufImage("heart"),
            CHERRY = getBufImage("cherry"),
            APPLE = getBufImage("apple"),
            PACMAN = getBufImage("pacman"),
            GHOST = getBufImage("ghost"),
            GHOST_SCARED_BLUE = getBufImage("ghost_scared_blue"),
            GHOST_SCARED_WHITE = getBufImage("ghost_scared_white"),
            WINNING = getBufImage("winning"),
            LOSING = getBufImage("losing");


    private static BufferedImage getBufImage(String pathname) {
        try {
            return ImageIO.read(new File("images/" + pathname + ".png"));
        } catch (IOException e) {
            System.out.println("Image " + pathname + ".png does not exist.");
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, 64, 64);
            g2d.dispose();
            return image;
        }
    }

    public static BufferedImage colored(BufferedImage image, Color replacement) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);

                if (isRed(color) && !isTransparent(color)) {
                    result.setRGB(x, y, replacement.getRGB());
                } else {
                    result.setRGB(x, y, rgb);
                }
            }
        }

        return result;
    }

    private static boolean isRed(Color color) {
        return color.getRed() > 120 && color.getGreen() < 100 && color.getBlue() < 100;
    }

    private static boolean isTransparent(Color color) {
        return color.getAlpha() == 0;
    }
}
