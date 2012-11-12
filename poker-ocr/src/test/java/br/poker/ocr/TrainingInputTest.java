package br.poker.ocr;

import static org.hamcrest.core.Is.*;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import br.poker.util.DummyImage;

public class TrainingInputTest {
	private static final String PIGROXALOT = "pigroxalot";
	private BufferedImage template;

	@Before
	public void createDummyImage() {
		template = DummyImage.anImage();
	}
	
	@Test
	public void shouldCreateATrainingInput() {
		TrainingInput input = new TrainingInput(PIGROXALOT, template);
		assertThat(input.getValue(), is(PIGROXALOT));
		assertThat(input.getImage(), is(template));
	}
}
