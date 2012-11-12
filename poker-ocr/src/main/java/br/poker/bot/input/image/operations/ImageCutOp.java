package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

public class ImageCutOp extends AbstractImageOp {
    @Override
    public BufferedImage process(BufferedImage toProcessImage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BufferedImage process(BufferedImage image, int positionX, int positionY, int width, int height) {
        if (image != null) {
            //Creates a new Destination image
            BufferedImage destination = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x+positionX, y+positionY);
                    destination.setRGB(x, y, rgb);
                }
            }

            return destination;
        } else {
            return image;
        }
    }

//	public BufferedImage process(BufferedImage image, BoxInfo info) {
//		int x = info.getPositionX();
//		int y = info.getPositionY();
//		int width = info.getWidth();
//		int height = info.getHeight();
//		
//		return process(image, x, y, width, height);
//	}
}
