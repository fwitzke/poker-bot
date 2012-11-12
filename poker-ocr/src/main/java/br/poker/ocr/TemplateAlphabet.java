package br.poker.ocr;

import static br.poker.bot.input.image.LetterExtractor.getLettersFromImage;
import static java.util.Arrays.asList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.ImageUtil;
import br.poker.util.Logger;

public class TemplateAlphabet implements Serializable {
	private static final long serialVersionUID = -1401391791912994410L;
	private AlphabetProperties properties;
	private List<TemplateCharacter> templateCharacters;
	private static TemplateAlphabetSerializer serializer; // TODO remove it,
															// move logic to
															// Serializer

	public TemplateAlphabet(AlphabetProperties properties) {
		templateCharacters = new ArrayList<TemplateCharacter>();
		this.properties = properties;
	}

	public static TemplateAlphabet fromFile(File folder) {
		if (!folder.exists())
			throw new RuntimeException("Problem loading alphabet " + folder
					+ ".");
		serializer = new TemplateAlphabetSerializer();
		return serializer.load(folder);
	}

	public static TemplateAlphabet fromFile(String alphabetFolder) {
		try {
			URL folderURL = TemplateAlphabet.class.getClassLoader().getResource(alphabetFolder);
			return fromFile(new File(folderURL.toURI()));
		} catch (URISyntaxException e) {
			//TODO handle it properly
			e.printStackTrace();
			System.err.println(e);
			return null;
		}
	}

	/**
	 * True if there is already a template for this string
	 * 
	 * @param t
	 * @return
	 */
	public boolean hasCharacter(TemplateCharacter t) {
		return templateCharacters.contains(t);
	}

	public boolean matches(TemplateCharacter t) {
		for (TemplateCharacter tc : templateCharacters) {
			if (tc.verifyMatchPercentile(t) == 1.0f) { // 100% match
				return true;
			}
		}
		return false;
	}

	public void addTemplateCharacter(TemplateCharacter template) {
		templateCharacters.add(template);
	}

	public String retrieveCharacter(BufferedImage image) {
		// Whitespaces will be treated as null image
		if (image == null)
			return " ";

		// Try to match any template
		for (TemplateCharacter template : templateCharacters) {
			if (template.verifyMatch(new TemplateCharacter("", image))) {
				return template.getValue();
			}
		}

		// TODO Unknown Chars: Verify what to do in these cases
		Logger.debug("Unknown Symbol: " + image.getWidth() + "x"
				+ image.getHeight());

		// Try to split the image in two pieces and test them as separate images
		String result = split(image);
		if (result != null)
			return result;

		return "?";
	}

	private String split(BufferedImage image) {
		int meanCharWidth = getMeanCharWidth();
		if (meanCharWidth <= 0)
			return null;

		int width = image.getWidth();
		int height = image.getHeight();

		int twoCharacters = (meanCharWidth * 2) - 1; //TODO Check this
		if (width > twoCharacters) {
			// TODO: Verify how many times bigger than a normal char it is.
			Logger.debug("Trying to split the image (" + width + "x" + height + ")");

			for (int m = meanCharWidth - 1; m <= meanCharWidth + 1; m++) {
				Logger.debug("Spliting in " + m);

				BufferedImage letters[] = ImageUtil.split(image, m);
				String s1 = retrieveCharacter(letters[0]);
				String s2 = retrieveCharacter(letters[1]);

				Logger.debug("S1: " + s1 + "   S2:" + s2);

				// if found a match for both images
				if (!"?".equals(s1) && !"?".equals(s2)) {
					Logger.debug("Found a match while splitting: " + s1 + s2);
					return s1 + s2;
				}
			}
		}
		return null;
	}

	public String retrieveWord(BufferedImage toRead) { //TODO REMOVE THIS METHOD
		return retrieveWord(new ImageSegment(toRead));
	}
		
	public String retrieveWord(ImageSegment toRead) {
		String result = "";
		ImageSegment[] letters = getLettersFromImage(toRead, properties);

		for (ImageSegment letter : letters)
			result += retrieveCharacter(letter.getImage());

		if (result.contains("?") && serializer != null)
			serializer.saveUnknown(toRead, new File("images/unknown"));

		return result.trim();
	}

	public int getMeanCharWidth() {
		if (templateCharacters.isEmpty())
			return 0;

		int width_acc = 0;
		int totalChar = templateCharacters.size();
		for (TemplateCharacter t : templateCharacters) {
			width_acc += t.getTemplate().getWidth();
		}

		return width_acc / totalChar;
	}

	public boolean contains(String character) {
		for (TemplateCharacter ch : templateCharacters) {
			if (character.equals(ch.getValue()))
				return true;
		}
		return false;
	}

	public int size() {
		return templateCharacters.size();
	}

	public AlphabetProperties getProperties() {
		return properties;
	}

	public void setProperties(AlphabetProperties properties) {
		this.properties = properties;
	}

	public List<String> getUnknownCharacters() {
		List<String> unknown = new ArrayList<String>();

		List<String> shouldKnowCharacters = allCharacters();
		for (String shouldKnow : shouldKnowCharacters) {
			boolean doIKnow = false;
			for (TemplateCharacter template : templateCharacters) {
				if (shouldKnow.equals(template.getValue())) {
					doIKnow = true;
					break;
				}
			}

			if (!doIKnow)
				unknown.add(shouldKnow);
		}

		return unknown;
	}

	public static List<String> allCharacters() {
		List<String> allChars = new ArrayList<String>();

		allChars.addAll(lowerCaseLetters());
		allChars.addAll(upperCaseLetters());
		allChars.addAll(numbers());
		allChars.addAll(specials());
		return allChars;
	}

	public static List<String> lowerCaseLetters() {
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		return asList(letters);
	}

	public static List<String> upperCaseLetters() {
		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		return asList(letters);
	}

	public static List<String> numbers() {
		String[] numbers = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		return asList(numbers);
	}

	public static List<String> specials() {
		String[] specials = { "!", "@", "$", "*", "(", ")", "-", "_", ",", "." };
		return asList(specials);
	}

	public List<TemplateCharacter> getTemplatesByLetter(String letter) {
		List<TemplateCharacter> templates = new ArrayList<TemplateCharacter>();

		for (TemplateCharacter template : templateCharacters)
			if (letter.equals(template.getValue()))
				templates.add(template);
		return templates;
	}

	public List<TemplateCharacter> getTemplateCharacters() {
		return templateCharacters;
	}

	public void setSerializer(TemplateAlphabetSerializer serializer) {
		TemplateAlphabet.serializer = serializer;
	}
}
