package br.poker.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.ImageUtil;
import static br.poker.bot.input.image.ImageUtil.loadImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.core.Is.is;

public class TemplateAlphabetSerializerTest {
	private static final String NUMBERS = "0 1 2 3 4 5 6 7 8 9";
	private static final String A_TO_Z_LOWER = "a b c d e f g h i j k l m n o p q r s t u v x w y z";
	private static final String A_TO_Z_UPPER = "A B C D E F G H I J K L M N O P Q R S T U V X W Y Z";
	private static final int THRESHOLD = 159;
	private static final boolean LETTER_IS_BLACK = true;
	private List<String> filesToDelete;
	private static File destinationFolder;
	private TemplateAlphabet alphabet;
	private TemplateTrainer trainer;
	private TemplateAlphabetSerializer serializer;
	private BufferedImage anUnknownWord;
	private BufferedImage anotherUnkownWord;
	
	@Before
	public void createTemplateTrainer() {
		trainer = new TemplateTrainer();
        trainer.addTrainingInput(A_TO_Z_UPPER, loadImage("images/sample_notepad/AZ.png"));
        trainer.addTrainingInput(A_TO_Z_LOWER, loadImage("images/sample_notepad/az_.png"));
        trainer.addTrainingInput(NUMBERS, loadImage("images/sample_notepad/09.png"));
        trainer.learn(new AlphabetProperties(THRESHOLD, LETTER_IS_BLACK));
        alphabet = trainer.getTemplateAlphabet();
        serializer = new TemplateAlphabetSerializer();
	}
	
	@Before
	public void loadUnknownWords() {
		anUnknownWord = ImageUtil.loadImage("images/tests/unknown.png");;
		anotherUnkownWord = ImageUtil.loadImage("images/tests/unknown2.png");;
		filesToDelete = new ArrayList<String>();
	}
	
	@BeforeClass
	public static void setupFolders() {
        destinationFolder = new File("generated/tests/font_notepad");
	}
	
	@AfterClass
	public static void deleteTestData() {
		removeFolder(destinationFolder);
	}

	@After
	public void deleteUnknownFiles() {
		for(String fileName : filesToDelete)
			deleteFile(fileName);
	}
	
	@Test
    public void shouldSaveTemplateAlphabet() {
		assertFalse(destinationFolder.exists());
		serializer.save(alphabet, destinationFolder);
		assertTrue(destinationFolder.exists());
    }
	
	@Test
	public void shouldRetrieveTemplateAlphabet() {
        TemplateAlphabet restored = TemplateAlphabet.fromFile(destinationFolder);
        assertNotNull(restored);
        assertTrue(restored.getProperties().isLetterBlack());
        assertThat(restored.getProperties().getThreshold(), is(THRESHOLD));
        assertEquals("The quick brown fox jumps over the lazy dog",
                restored.retrieveWord(ImageUtil.loadImage("images/sample_notepad/sample_3.png")));
	}

	@Test
	public void shouldSaveUnknownWords() throws Exception {
		File outputFolder = new File("images/tests/unknown");
		int length = outputFolder.list().length;
		saveUnknown(anUnknownWord, outputFolder);
		assertThat(outputFolder.listFiles().length, is(length+1));

		saveUnknown(anotherUnkownWord, outputFolder);
		assertThat(outputFolder.listFiles().length, is(length+2));
	}

	@Test
	public void shouldNotSaveDuplicateUnknownWords() {
		File outputFolder = new File("images/tests/unknown");
		int length = outputFolder.list().length;
		saveUnknown(anUnknownWord, outputFolder);
		saveUnknown(anUnknownWord, outputFolder);
		assertThat(outputFolder.listFiles().length, is(length+1));
	}

	private void saveUnknown(BufferedImage image, File outputFolder) {
		String fileName = serializer.saveUnknown(new ImageSegment(image), outputFolder);
		filesToDelete.add(fileName);
	}
	
	private void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}
	
	private static void removeFolder(File toRemove) {
		while(toRemove.exists()) {
			for(File child:toRemove.listFiles())
				child.delete();
			toRemove.delete();
		}
	}
}
