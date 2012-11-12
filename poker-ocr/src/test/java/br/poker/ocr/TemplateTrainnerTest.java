package br.poker.ocr;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import static br.poker.bot.input.image.ImageUtil.loadImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TemplateTrainnerTest {
	private static final int THRESHOLD = 159;
	private static final String A_TO_Z_LOWER = "a b c d e f g h i j k l m n o p q r s t u v x w y z";
	private static final String A_TO_Z_UPPER = "A B C D E F G H I J K L M N O P Q R S T U V X W Y Z";
	private static final String NUMBERS = "0 1 2 3 4 5 6 7 8 9";
	private static final String SAMPLE_1_TEXT = "Fernando Muraro Witzke is a very nice guy";
	private static final String SAMPLE_2_TEXT = "This is a simple test so Please recognize this text";
	private static final String SAMPLE_3_TEXT = "The quick brown fox jumps over the lazy dog";
	private static final String SAMPLE_4_TEXT = "MuMu";
	private BufferedImage sample1;
	private BufferedImage sample2;
	private BufferedImage sample3;
	private BufferedImage sample4;
	private BufferedImage sample5;
	private BufferedImage a_to_z_upper_Notepad;
	private BufferedImage a_to_z_lower_Notepad;
	private BufferedImage numbers_Notepad;

	@Before
	public void loadNotepadAlphabetInputImages() {
		a_to_z_upper_Notepad = loadImage("images/sample_notepad/AZ.png");
		a_to_z_lower_Notepad = loadImage("images/sample_notepad/az_.png");
		numbers_Notepad = loadImage("images/sample_notepad/09.png");
		
		sample1 = loadImage("images/sample_notepad/sample_1.png");
		sample2 = loadImage("images/sample_notepad/sample_2.png");
		sample3 = loadImage("images/sample_notepad/sample_3.png");
		sample4 = loadImage("images/sample_notepad/sample_4.png");
		sample5 = loadImage("images/sample_notepad/sample_5.png");
	}
	
    @Test
    public void shouldLearnNotepadFontSample() {
        TemplateTrainer trainer = new TemplateTrainer();
		trainer.addTrainingInput(A_TO_Z_UPPER, a_to_z_upper_Notepad);
		trainer.addTrainingInput(A_TO_Z_LOWER, a_to_z_lower_Notepad);
		trainer.addTrainingInput(NUMBERS, numbers_Notepad);
        trainer.learn(THRESHOLD);

        TemplateAlphabet notepadAlphabet = trainer.getTemplateAlphabet();

        int alphabetSize = (26*2) + 10;

        assertNotNull(notepadAlphabet);
		assertEquals(alphabetSize, notepadAlphabet.size());
		assertEquals(SAMPLE_1_TEXT, notepadAlphabet.retrieveWord(sample1));
		assertEquals(SAMPLE_2_TEXT, notepadAlphabet.retrieveWord(sample2));
		assertEquals(SAMPLE_3_TEXT, notepadAlphabet.retrieveWord(sample3));
		assertEquals(SAMPLE_4_TEXT, notepadAlphabet.retrieveWord(sample4));
		assertEquals("", notepadAlphabet.retrieveWord(sample5));
    }
    
	@Test
    public void shouldLearnBasedOnTrainningInputs() {
        TemplateTrainer trainner = new TemplateTrainer();
        trainner.addTrainingInput("Oracle", loadImage("images/trainning_input_1.png"));
        trainner.addTrainingInput("Beginning Android 2", loadImage("images/android2.png"));
        trainner.addTrainingInput("Oracle SOA Suite 11g Handbook", loadImage("images/handbook.png"));
        trainner.learn(THRESHOLD);

        TemplateAlphabet bookeAlphabet = trainner.getTemplateAlphabet();
        assertNotNull(bookeAlphabet);
        assertEquals(21, bookeAlphabet.size());
        assertEquals("Andro id 2 Beginning", bookeAlphabet.retrieveWord(loadImage("images/fuzz_andro.png")));
    }

}