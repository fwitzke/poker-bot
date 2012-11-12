package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import br.poker.bot.input.image.ImageUtil;
import br.poker.bot.input.image.operations.ImageCompareOp;

import static org.junit.Assert.*;

public class ImageCompareOpTest {
	private ImageCompareOp compareOp;
	private BufferedImage anImage;
	private BufferedImage anotherImage;

	@Before
	public void setup() {
		compareOp = new ImageCompareOp();
		anImage = ImageUtil.loadImage("images/tests/unknown.png");
		anotherImage = ImageUtil.loadImage("images/tests/unknown2.png");
	}
	
	@Test
	public void twoImagesShouldBeEqual() throws Exception {
		assertTrue(compareOp.equals(anImage, anImage));
	}
	
	@Test
	public void shouldDetectDifferentImages() throws Exception {
		assertFalse(compareOp.equals(anImage, anotherImage));
	}
}
