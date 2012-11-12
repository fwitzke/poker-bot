package br.poker.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.ImageUtil;
import br.poker.util.DummyImage;
import static br.poker.ocr.TemplateAlphabet.allCharacters;
import static br.poker.ocr.TemplateAlphabet.lowerCaseLetters;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateAlphabetTest {
	private static final int THRESHOLD = 100;
	private static final boolean BLACK_LETTER = true;
	private TemplateAlphabet alphabet;
	private BufferedImage unknownWord;
	@Mock private TemplateAlphabetSerializer serializer;

	@Before
	public void setup() {
		alphabet = new TemplateAlphabet(new AlphabetProperties(THRESHOLD, BLACK_LETTER));
		alphabet.setSerializer(serializer);
		unknownWord = ImageUtil.loadImage("images/tests/unknown.png");
	}

	@Test
	public void shouldMatchAKnownTemplateCharacter() {
		TemplateCharacter b = aCharacter("b", dummyCharImage());
		alphabet.addTemplateCharacter(b);
		assertTrue(alphabet.matches(b));
	}

	@Test
	public void shouldSaveUnkownNamesForStudyingLater() {
		ImageSegment segment = new ImageSegment(unknownWord);
		String word = alphabet.retrieveWord(segment);
		assertTrue(word.contains("?"));
		verify(serializer).saveUnknown(segment, new File("images/unknown"));
	}
	
	@Test
	public void shouldNotMatchWhenCharacterIsNotKnown() {
		assertFalse(alphabet.matches(aCharacter("ANY", dummyCharImage())));
	}

	@Test
	public void shouldTellWhichCharactersAreKnown() {
		alphabet.addTemplateCharacter(aCharacter("a", null));
		alphabet.addTemplateCharacter(aCharacter("b", null));

		assertThat(alphabet.contains("a"), is(true));
		assertThat(alphabet.contains("b"), is(true));
	}

	@Test
	public void shouldTellAllCharactersAreUnknownWhenAlphabetIsEmpty() {
		List<String> unknown = alphabet.getUnknownCharacters();
		List<String> all = allCharacters();

		assertThat(unknown.size(), is(all.size()));
		for (String character : all)
			assertTrue(unknown.contains(character));
	}

	@Test
	public void shouldTellWhichCharactersAreUnknown() {
		for (String known : lowerCaseLetters())
			alphabet.addTemplateCharacter(aCharacter(known, dummyCharImage()));

		List<String> unknownChars = alphabet.getUnknownCharacters();

		assertThat(unknownChars.size(), is(expectedUnkownCharacters().size()));
		for (String unknown : expectedUnkownCharacters())
			assertTrue(unknownChars.contains(unknown));
	}

	@Test
	public void shouldRetrieveTemplatesByLetter() {
		alphabet.addTemplateCharacter(aCharacter("z", dummyCharImage()));
		alphabet.addTemplateCharacter(aCharacter("z", dummyCharImage()));

		assertEquals(2, alphabet.getTemplatesByLetter("z").size());
	}
	
	private TemplateCharacter aCharacter(String c, BufferedImage template) {
		return new TemplateCharacter(c, template);
	}

	private List<String> expectedUnkownCharacters() {
		List<String> expectedUnkownChars = allCharacters();
		expectedUnkownChars.removeAll(lowerCaseLetters());
		return expectedUnkownChars;
	}

	private BufferedImage dummyCharImage() {
		return DummyImage.anImage();
	}
}
