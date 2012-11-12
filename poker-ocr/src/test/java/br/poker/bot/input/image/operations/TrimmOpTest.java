package br.poker.bot.input.image.operations;

import static org.junit.Assert.assertEquals;
import java.awt.image.BufferedImage;
import org.junit.Test;

import br.poker.bot.input.image.ImageUtil;
import br.poker.bot.input.image.operations.TrimOp;

public class TrimmOpTest {	
    @Test
    public void shouldExtractLetterSegmentsFromImage() {
    	BufferedImage emptyLineImage = ImageUtil.loadImage("images/tests/single_line_text.png");

        TrimOp trimm = new TrimOp();
        BufferedImage[] letters = trimm.extractLetterSegments(emptyLineImage);

        assertEquals(21, letters.length);
    }
	
	@Test
    public void shouldReturnZeroLettersFromEmptyLine() {
        BufferedImage emptyLineImage = ImageUtil.loadImage("images/tests/empty_line_image.png");

        TrimOp trimm = new TrimOp();
        BufferedImage[] letters = trimm.extractLetterSegments(emptyLineImage);

        assertEquals(0, letters.length);
    }
}
