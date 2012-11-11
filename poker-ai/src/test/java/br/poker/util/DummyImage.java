package br.poker.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

import br.poker.bot.input.image.ImageSegment;

public class DummyImage extends BufferedImage {
	public static BufferedImage anImage() {
		return new DummyImage(10, 10, BufferedImage.TYPE_INT_RGB);
	}
	
	public static ImageSegment anImageSegment() {
		return new ImageSegment(anImage());
	}
	
	public DummyImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
	}

	public DummyImage(int i, int j, int typeIntRgb) {
		super(i, j, typeIntRgb);
	}
	
}
