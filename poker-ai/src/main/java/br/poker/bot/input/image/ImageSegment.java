package br.poker.bot.input.image;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import br.poker.bot.input.image.operations.GrayscaleOp;
import br.poker.bot.input.image.operations.ImageCompareOp;
import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.bot.input.image.operations.ThresholdGrayscaleOp;
import br.poker.bot.input.image.operations.TrimOp;
import br.poker.model.table.structure.BoxInfo;
import br.poker.util.PokerStarsTableMock;

public class ImageSegment {
	private BufferedImage image;
	private GrayscaleOp grayscaleOp;
	private ImageCompareOp imageCompareOp;
	private ImageCutOp imageCutOp;
	private ThresholdGrayscaleOp thresholdOp;
	private TrimOp trimOp;

	public ImageSegment(BufferedImage image) {
		this.image = image;
		this.grayscaleOp = new GrayscaleOp();
		this.imageCompareOp = new ImageCompareOp();
		this.thresholdOp = new ThresholdGrayscaleOp();
		this.trimOp = new TrimOp();
		this.imageCutOp = new ImageCutOp();
	}

	public ImageSegment toGrayscale() {
		this.image = grayscaleOp.process(image);
		return this;
	}

	public ImageSegment cut(BoxInfo region) {
		return new ImageSegment(imageCutOp.process(image, region));
	}
	
	public ImageSegment applyThreshold() {
		return new ImageSegment(thresholdOp.process(image));
	}

	public ImageSegment[] extractLettersSegments() {
		BufferedImage[] extracted = trimOp.extractLetterSegments(image);
		ImageSegment[] segments = new ImageSegment[extracted.length];
		for(int i=0; i < extracted.length; i++)
			segments[i] = new ImageSegment(extracted[i]).trim();
		return segments;
	}

	public ImageSegment trim() {
		return new ImageSegment(trimOp.process(image));
	}

	@Override
	public boolean equals(Object another) {
		if(!(another instanceof ImageSegment))
			return false;
		
		ImageSegment anotherSegment = (ImageSegment)another;
		return imageCompareOp.equals(image, anotherSegment.getImage());
	}
	
	public void showImage() {
		new PokerStarsTableMock(this.toString(), Arrays.asList(image)).show();
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setGrayscaleOp(GrayscaleOp grayscaleOp) {
		this.grayscaleOp = grayscaleOp;
	}

	public void setImageCompareOp(ImageCompareOp imageCompareOp) {
		this.imageCompareOp = imageCompareOp;
	}

	public void setImageCutOp(ImageCutOp imageCutOp) {
		this.imageCutOp = imageCutOp;
	}

	public void setThresholdOp(ThresholdGrayscaleOp thresholdOp) {
		this.thresholdOp = thresholdOp;
	}

	public void setTrimOp(TrimOp trimOp) {
		this.trimOp = trimOp;
	}
}
