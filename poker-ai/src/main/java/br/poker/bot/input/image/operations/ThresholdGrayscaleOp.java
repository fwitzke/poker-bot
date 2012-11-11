package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

public class ThresholdGrayscaleOp extends AbstractImageOp {
	private int threshold;
	private boolean letterBlack;

	public ThresholdGrayscaleOp() {
		// Arbitrary values
		setThreshold(40);
		letterBlack = true;
	}

	public BufferedImage process(BufferedImage toProcessImage) {
		BufferedImage result = null;
		if (toProcessImage != null) {
			result = new BufferedImage(toProcessImage.getWidth(),
					toProcessImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			int height = toProcessImage.getHeight();
			int width = toProcessImage.getWidth();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					// 32-bit integer with RGB and Alpha Information
					int rgb = toProcessImage.getRGB(x, y);
					// Get RGB from 32-bit integer
					int rgbArray[] = getRgbArray(rgb);

					// Considering that RED == GREEN == BLUE (grayscale image)
					if (letterBlack) {
						// Black Letters
						if (rgbArray[RED] <= threshold) {
							result.setRGB(x, y, getRgbInteger(blackColor));
						} else {
							result.setRGB(x, y, getRgbInteger(whiteColor));
						}
					} else {
						// White Letters
						if (rgbArray[RED] > threshold) {
							result.setRGB(x, y, getRgbInteger(blackColor));
						} else {
							result.setRGB(x, y, getRgbInteger(whiteColor));
						}
					}

				}
			}
		}

		return result;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void setLetterBlack(boolean letterBlack) {
		this.letterBlack = letterBlack;
	}

	public boolean isLetterBlack() {
		return letterBlack;
	}
}
