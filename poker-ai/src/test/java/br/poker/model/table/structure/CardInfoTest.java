package br.poker.model.table.structure;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.model.Card;
import br.poker.ocr.TemplateAlphabet;
import br.poker.util.DummyImage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.is;


@RunWith(MockitoJUnitRunner.class)
public class CardInfoTest {
	private static final int X = 2;
	private static final int Y = 3;
	private static final int WIDTH = 1;
	private static final int HEIGHT = 2;
	private CardInfo info;
	private BufferedImage tableImage;
	private BufferedImage cardRankImage;
	private BufferedImage cardValueImage;
	@Mock private ImageCutOp cutOpMock;
	@Mock private TemplateAlphabet alphabetMock;
	
	@Before
	public void setup() {
		tableImage = anyImage();
		info = new CardInfo(X, Y, WIDTH, HEIGHT, cutOpMock);
		cardRankImage = anyImage();
		cardValueImage = anyImage();
		when(cutOpMock.process(tableImage, X, Y, WIDTH, HEIGHT)).thenReturn(cardRankImage);
		when(cutOpMock.process(tableImage, X, Y+HEIGHT, WIDTH, HEIGHT)).thenReturn(cardValueImage);
	}
	
	@Test
	public void shouldExtractCardInfoFromImage() throws Exception {
		when(alphabetMock.retrieveWord(cardRankImage)).thenReturn("Q");
		when(alphabetMock.retrieveWord(cardValueImage)).thenReturn("c");
		
		assertThat(info.getCard(tableImage, alphabetMock), is(new Card("Qc")));
	}
	
	@Test
	public void shouldTrimTextValuesFromAlphabet() throws Exception {
		when(alphabetMock.retrieveWord(cardRankImage)).thenReturn("  J ");
		when(alphabetMock.retrieveWord(cardValueImage)).thenReturn(" h ");
		
		assertThat(info.getCard(tableImage, alphabetMock), is(new Card("Jh")));
	}
	
	@Test
	public void shouldReturnNullWhenCardRankIsNotRecognized() throws Exception {
		when(alphabetMock.retrieveWord(cardRankImage)).thenReturn("?");
		when(alphabetMock.retrieveWord(cardValueImage)).thenReturn("c");
		
		assertNull(info.getCard(tableImage, alphabetMock));
	}
	
	@Test
	public void shouldReturnNullWhenCardValueIsNotRecognized() throws Exception {
		when(alphabetMock.retrieveWord(cardRankImage)).thenReturn("Q");
		when(alphabetMock.retrieveWord(cardValueImage)).thenReturn("?");
		
		assertNull(info.getCard(tableImage, alphabetMock));
	}

	@Test
	public void shouldNotUseTwoCharactersToRepresent10Card() throws Exception {
		when(alphabetMock.retrieveWord(cardRankImage)).thenReturn("10");
		when(alphabetMock.retrieveWord(cardValueImage)).thenReturn("d");
		assertThat(info.getCard(tableImage, alphabetMock), is(new Card("Td")));
	}
	
	private BufferedImage anyImage() {
		return DummyImage.anImage();
	}
}
