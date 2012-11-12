package br.poker.model.table.structure;

import static br.poker.util.DummyImage.anImage;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.ocr.TemplateAlphabet;

@RunWith(MockitoJUnitRunner.class)
public class BoxInfoTest {
	private static final String WHATEVER = "whatever";
	private static final int X = 0;
	private static final int Y = 0;
	private static final int WIDTH = 5;
	private static final int HEIGHT = 10;
	private BoxInfo info;
	private BufferedImage tableImage;
	private BufferedImage textImage;
	@Mock private ImageCutOp cutOpMock;
	@Mock private TemplateAlphabet alphabetMock;
	
	@Before
	public void setup() {
		tableImage = anImage();
		textImage = anImage();
		info = new BoxInfo(X, Y, WIDTH, HEIGHT, cutOpMock);
		when(cutOpMock.process(tableImage, X, Y, WIDTH, HEIGHT)).thenReturn(textImage);
	}
	
	@Test
	public void shouldExtractTextInfoFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn(WHATEVER);
		String text = info.getText(tableImage, alphabetMock);
		assertThat(text, is(WHATEVER));
	}
	
	@Test
	public void shouldReturnTextWithNoSpacesBelowNorAfter() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn("   remove spaces from me    ");
		String text = info.getText(tableImage, alphabetMock);
		assertThat(text, is("remove spaces from me"));
	}
	
	@Test
	public void shouldExtractNumberFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn("$1.09");
		int value = info.getNumber(tableImage, alphabetMock);
		assertThat(value, is(109));
	}
	
	@Test
	public void itShouldExtractInvalidNumberFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn("PP*!@");
		int value = info.getNumber(tableImage, alphabetMock);
		assertThat(value, is(-1));
	}
	
	@Test
	public void shouldExtractPotValueFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn("POT $0.89");
		int value = info.getPotValue(tableImage, alphabetMock);
		assertThat(value, is(89));
	}
	
	@Test
	public void shouldExtractPlayMoneuPotValueFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(textImage)).thenReturn("Pot: 15");
		int value = info.getPotValue(tableImage, alphabetMock);
		assertThat(value, is(1500));
	}
}
