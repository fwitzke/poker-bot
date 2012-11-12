package br.poker.bot.input.image;

import static java.lang.String.format;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import br.poker.bot.input.image.operations.TrimOp;

public class ImageUtil {
	
    public static BufferedImage loadImage(String imageName) {
        BufferedImage res = null;
        try {
            File imagemFile = new File(imageName);
            res = ImageIO.read(imagemFile);
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println(format("Failed loading file %s due to exception: %s", imageName, e));
        }
        return res;
    }

    public static BufferedImage loadImage(File imageFile) {
        BufferedImage res = null;
        try {
            res = ImageIO.read(imageFile);
        } catch (IOException e) {
        	System.err.println(format("Failed loading file %s due to exception: %s", imageFile.getName(), e));
        }
        return res;
    }

    public static void writeImage(String fileName, Image image, String imageFormat) {
        writeImage(fileName, toBufferedImage(image), imageFormat);
    }

    /**
     * Writes the specified BufferedImage to file system
     * @param fileName
     * @param image
     * @param imageFormat
     */
    public static void writeImage(String fileName, BufferedImage image, String imageFormat) {
        try {
            File imageFile = new File(fileName);
            ImageIO.write(image, imageFormat, imageFile);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static BufferedImage getStubImage(List<Point> cltPoint, int width, int height) {
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Sets white background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                resultImage.setRGB(x, y, getRgbInteger(new int[]{255, 255, 255, 255}));
            }
        }

        //For each point, paint it black
        for (Point p : cltPoint) {
            resultImage.setRGB(p.x, p.y, getRgbInteger(new int[]{255, 0, 0, 0}));
        }


        return resultImage;
    }

    public static int getRgbInteger(int[] rgbArray) {
        //Writes the RGB info in a 32-Bit integer
        int rgb = (rgbArray[0] << 24) | (rgbArray[1] << 16) | (rgbArray[2] << 8) | rgbArray[3];

        return rgb;
    }

    /**
     * Get RGB array from integer with pixel info
     * @param rgb
     * @return
     */
    public static int[] getRgbArray(int rgb) {
        int rgbArray[] = new int[4];

        //Get RGB from 32-bit integer
        rgbArray[0] = ((rgb >> 24) & 0xff);
        rgbArray[1] = ((rgb >> 16) & 0xff);
        rgbArray[2] = ((rgb >> 8) & 0xff);
        rgbArray[3] = ((rgb) & 0xff);

        return rgbArray;
    }

    /**
     * This method returns a buffered image with the contents of an image
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false;

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * Splits the image into two separates. The index is used as separator
     * @param img
     * @param i
     * @return
     */
    public static BufferedImage[] split(BufferedImage img, int i) {
        BufferedImage[] res = new BufferedImage[2];

        int img_1_size = i + 1;
        int img_2_size = img.getWidth() - img_1_size;

        //Image 1
        res[0] = new BufferedImage(img_1_size, img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < img_1_size; x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                res[0].setRGB(x, y, img.getRGB(x, y));
            }
        }

        //Image 2
        res[1] = new BufferedImage(img_2_size, img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = img_1_size; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                res[1].setRGB(x-img_1_size, y, img.getRGB(x, y));
            }
        }


        //Trim
        TrimOp op = new TrimOp();
        res[0] = op.process(res[0]);
        res[1] = op.process(res[1]);

        return res;
    }
}
