package br.poker.bot.input.image;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.input.image.operations.GrayscaleOp;
import br.poker.bot.input.image.operations.ImageCompareOp;
import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.bot.input.image.operations.ThresholdGrayscaleOp;
import br.poker.bot.input.image.operations.TrimOp;
import br.poker.util.DummyImage;

@RunWith(MockitoJUnitRunner.class)
public class ImageSegmentTest {
	private ImageSegment segment;
	private BufferedImage anImage;
	@Mock private GrayscaleOp grayscaleOp;
	@Mock private ImageCompareOp imageCompareOp;
	@Mock private ImageCutOp imageCutOp;
	@Mock private ThresholdGrayscaleOp thresholdOp;
	@Mock private TrimOp trimOp;
	
	@Before
	public void setup() {
		anImage = DummyImage.anImage();
		stub(imageCompareOp.equals(anImage, anImage)).toReturn(true);
		
		segment = new ImageSegment(anImage);
		segment.setGrayscaleOp(grayscaleOp);
		segment.setImageCompareOp(imageCompareOp);
		segment.setImageCutOp(imageCutOp);
		segment.setThresholdOp(thresholdOp);
		segment.setTrimOp(trimOp);
	}
	
	@Test
	public void shouldCreateAnImageSegment() {
		assertThat(segment.getImage(), is(anImage));
	}

	@Test
	public void shouldConvertImageSegmentToGrayscale() {
		assertNotNull(segment.toGrayscale());
		verify(grayscaleOp).process(anImage);
	}

	@Test
	public void shouldApplyThresholdValuesToImage() {
		assertNotNull(segment.applyThreshold());
		verify(thresholdOp).process(anImage);
	}

	@Test
	public void shouldTrimImage() {
		assertNotNull(segment.trim());
		verify(trimOp).process(anImage);
	}
	
	@Test
	public void shouldExtractLettersFromImage() {
		stub(trimOp.extractLetterSegments(anImage)).toReturn(new BufferedImage[1]);
		assertNotNull(segment.extractLettersSegments());
		verify(trimOp).extractLetterSegments(anImage);
	}
	
	@Test
	public void shouldCutImageSegment() {
		assertNotNull(segment.cut(0, 0, 5, 5));
		verify(imageCutOp).process(anImage, 0, 0, 5, 5);
	}
	
	@Test
	public void shouldCompareTwoImageSegments() {
		ImageSegment anotherSegment = new ImageSegment(anImage);
		assertTrue(segment.equals(anotherSegment));
		verify(imageCompareOp).equals(segment.getImage(), anotherSegment.getImage());
	}
}
