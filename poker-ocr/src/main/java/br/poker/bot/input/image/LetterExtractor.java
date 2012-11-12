package br.poker.bot.input.image;

import br.poker.bot.input.image.operations.ThresholdGrayscaleOp;
import br.poker.ocr.AlphabetProperties;

public class LetterExtractor {
	/**
	 * Gets all letters identified in the image. This method assumes that the
	 * image contains a single Line with text It converts image to Grayscale,
	 * apply Threshold and than extract Trimmed Letter Segments
	 */
	public static ImageSegment[] getLettersFromImage(ImageSegment image, AlphabetProperties properties) {
		ImageSegment copy = new ImageSegment(image.getImage()); // TODO should not modify Image after
																// extracting letters, so  will make a
																// copy for now

		ThresholdGrayscaleOp thop = new ThresholdGrayscaleOp();
		thop.setThreshold(properties.getThreshold());
		thop.setLetterBlack(properties.isLetterBlack());
		copy.setThresholdOp(thop);

		return copy.toGrayscale().applyThreshold().extractLettersSegments();
	}
}
