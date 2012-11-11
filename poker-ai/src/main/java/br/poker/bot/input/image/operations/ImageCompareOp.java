package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

public class ImageCompareOp {
	public boolean equals(BufferedImage anImage, BufferedImage anotherImage) {
		if(anImage == null && anotherImage == null)
			return true;
		
		int width = anImage.getWidth();
		int height = anImage.getHeight();
		int width2 = anotherImage.getWidth();
		int height2 = anotherImage.getHeight();
		if(width != width2 || height != height2)
			return false;
		
		int anImageRGB = getImageRGB_Sum(anImage, width, height);
		int anotherImageRGB = getImageRGB_Sum(anotherImage, width2, height2);
		return anImageRGB == anotherImageRGB;
	}

	private int getImageRGB_Sum(BufferedImage image, int width, int height) {
		int sum = 0;
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				sum += image.getRGB(x, y);
		return sum;
	}
}
