package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

public class GrayscaleOp extends AbstractImageOp {
	public BufferedImage process(BufferedImage image) {
		if (image == null)
			return image;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// 32-bit integer with RGB and Alpha Information
				int rgb = image.getRGB(x, y);
				// Get RGB from 32-bit integer
				int pixelRGB[] = getRgbArray(rgb);

				// Convert the color to a gray scale
				int grayScale = (int) (0.3 * pixelRGB[RED] + 0.59
						* pixelRGB[GREEN] + 0.11 * pixelRGB[BLUE]);
				pixelRGB[RED] = grayScale;
				pixelRGB[GREEN] = grayScale;
				pixelRGB[BLUE] = grayScale;

				// Sets the pixel color
				rgb = getRgbInteger(pixelRGB);
				result.setRGB(x, y, rgb);
			}
		}
		return result;
	}
}
