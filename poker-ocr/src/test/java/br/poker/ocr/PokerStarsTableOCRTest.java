package br.poker.ocr;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.ImageUtil;
import br.poker.util.PNGFilter;
public class PokerStarsTableOCRTest {
    private TemplateAlphabetSerializer serializer;
	private TemplateAlphabet general;
	private TemplateAlphabet deck;
	private TemplateAlphabet actions;

    @Before
    public void setup() {
    	serializer = new TemplateAlphabetSerializer();

    	general = anAlphabet("images/test_stars/trainning-general", 175, false);
    	deck = anAlphabet("images/test_stars/trainning-deck", 180, true);
    	actions = anAlphabet("images/test_stars/trainning-actions", 135, false);
    	serializer.save(general, new File("generated/tables/stars/fonts/general"));
    	serializer.save(deck, new File("generated/tables/stars/fonts/deck"));
    	serializer.save(actions, new File("generated/tables/stars/fonts/actions"));
    }
    
    @Test
    public void itShouldReadPlayerNames() {
        assertEquals("brian832", general.retrieveWord(loadImage("images/test_stars/trainning-general/brian832.png")));
        assertEquals("eightball159", general.retrieveWord(loadImage("images/test_stars/eightball159.png")));
        assertEquals("disoncar", general.retrieveWord(loadImage("images/test_stars/disoncar.png")));
        assertEquals("rake shark", general.retrieveWord(loadImage("images/test_stars/trainning-general/rake shark.png")));
        assertEquals("d.nikolay33", general.retrieveWord(loadImage("images/test_stars/trainning-general/d.nikolay33.png")));
        assertEquals("volandx1", general.retrieveWord(loadImage("images/test_stars/trainning-general/volandx1.png")));
        assertEquals("SoBeRb1o", general.retrieveWord(loadImage("images/test_stars/trainning-general/SoBeRb1o.png")));
        assertEquals("wvaseckin", general.retrieveWord(loadImage("images/test_stars/wvaseckin.png")));
        assertEquals("EmiiR17", general.retrieveWord(loadImage("images/test_stars/trainning-general/EmiiR17.png")));
        assertEquals("imperator464", general.retrieveWord(loadImage("images/test_stars/trainning-general/imperator464.png")));
        assertEquals("pigroxalot", general.retrieveWord(loadImage("images/tests/player_name/pigroxalot.png")));
        assertEquals("LOKO13907", general.retrieveWord(loadImage("images/tests/player_name/LOKO13907.png")));
        assertEquals("Lilsti", general.retrieveWord(loadImage("images/tests/player_name/Lilsti.png")));
        assertEquals("fumetas_gti", general.retrieveWord(loadImage("images/tests/player_name/fumetas_gti.png")));
        assertEquals("jopito14", general.retrieveWord(loadImage("images/tests/player_name/jopito14.png")));
        assertEquals("guaquin", general.retrieveWord(loadImage("images/tests/player_name/guaquin.png")));
        assertEquals("guaquin", general.retrieveWord(loadImage("images/tests/player_name/guaquin_2.png")));
        assertEquals("ljack834", general.retrieveWord(loadImage("images/tests/player_name/ljack834.png")));
        assertEquals("Red Chevy 16", general.retrieveWord(loadImage("images/tests/player_name/Red Chevy 16.png")));
        assertEquals("Red Chevy 16", general.retrieveWord(loadImage("images/tests/player_name/Red Chevy 16_2.png")));
        assertEquals("Dick TracySC", general.retrieveWord(loadImage("images/tests/player_name/Dick TracySC.png")));
        assertEquals("7s@nti@g@7", general.retrieveWord(loadImage("images/tests/player_name/santiaga.png")));
        assertEquals("7s@nti@g@7", general.retrieveWord(loadImage("images/tests/player_name/santiaga_2.png")));
        assertEquals("stymie62", general.retrieveWord(loadImage("images/tests/player_name/stymie62.png")));
        assertEquals("crackerjim2", general.retrieveWord(loadImage("images/tests/player_name/crackerjim2.png")));
        assertEquals("GoodGuyDave", general.retrieveWord(loadImage("images/tests/player_name/GoodGuyDave.png")));
        assertEquals("GoodGuyDave", general.retrieveWord(loadImage("images/tests/player_name/GoodGuyDave_2.png")));
        assertEquals("mikesfish", general.retrieveWord(loadImage("images/tests/player_name/mikefish.png")));
        assertEquals("PLAYER2996", general.retrieveWord(loadImage("images/tests/player_name/PLAYER2996.png")));
        assertEquals("AHO2356", general.retrieveWord(loadImage("images/tests/player_name/AHO2356.png")));
        assertEquals("tralrtrmp", general.retrieveWord(loadImage("images/tests/player_name/tralrtrmp.png")));
        assertEquals("AceMan8", general.retrieveWord(loadImage("images/tests/player_name/AceMan8.png")));
    }
    
    @Test
    public void itShouldReadStackInfo() {
    	assertEquals("$7.92518", general.retrieveWord(loadImage("images/test_stars/trainning-general/$7.92518.png")));
    	assertEquals("$5.18", general.retrieveWord(loadImage("images/test_stars/trainning-general/$5.18.png")));
    	assertEquals("$4.16", general.retrieveWord(loadImage("images/test_stars/trainning-general/$4.16.png")));
    	assertEquals("7,456", general.retrieveWord(loadImage("images/test_stars/7456.png")));
    	assertEquals("All In", general.retrieveWord(loadImage("images/test_stars/AllIn.png")));
    }

    @Test
    public void itShouldReadCardsInfo() {
        assertEquals("c", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/c.png")));
        assertEquals("d", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/d.png")));
        assertEquals("h", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/h.png")));
        assertEquals("s", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/s.png")));
        
        assertEquals("2", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/2.png")));
        assertEquals("3", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/3.png")));
        assertEquals("4", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/4.png")));
        assertEquals("4", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/4_1.png")));
        assertEquals("5", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/5.png")));
        assertEquals("6", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/6.png")));
        assertEquals("7", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/7.png")));
        assertEquals("8", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/8.png")));
        assertEquals("9", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/9.png")));
        assertEquals("10", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/10.png")));
        assertEquals("J", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/J.png")));
        assertEquals("Q", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/Q.png")));
        assertEquals("K", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/K.png")));
        assertEquals("A", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/A.png")));
        assertEquals("A", deck.retrieveWord(loadImage("images/test_stars/trainning-deck/A_1.png")));
    }

    @Test
    public void itShouldReadActionsInfo() {
        assertEquals("Fold", actions.retrieveWord(loadImage("images/test_stars/Fold_Button.png")));
        assertEquals("Call", actions.retrieveWord(loadImage("images/test_stars/Call_Button.png")));
        assertEquals("Raise To", actions.retrieveWord(loadImage("images/test_stars/Raise_Button.png")));
        assertEquals("Check", actions.retrieveWord(loadImage("images/test_stars/Check_Button.png")));
        assertEquals("Bet", actions.retrieveWord(loadImage("images/test_stars/Bet_Button.png")));
        assertEquals("10", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/10.png")));
        assertEquals("20", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/20.png")));
        assertEquals("30", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/30.png")));
        assertEquals("40", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/40.png")));
        assertEquals("567", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/567.png")));
        assertEquals("1589", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/1589.png")));
        assertEquals("$0.02", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/$0.02.png")));
        assertEquals("$0.04", actions.retrieveWord(loadImage("images/test_stars/trainning-actions/$0.04.png")));
    }
    
    @Test
	public void itShouldReadPotInfo() throws Exception {
		assertThat(general.retrieveWord(loadImage("images/tests/pot_value/pot_86.png")), is("POT $0.86"));
		assertThat(general.retrieveWord(loadImage("images/tests/pot_value/pot_75.png")), is("Pot: 75"));
		assertThat(general.retrieveWord(loadImage("images/tests/pot_value/pot_344.png")), is("Pot: 344"));
		assertThat(general.retrieveWord(loadImage("images/tests/pot_value/pot_576.png")), is("Pot: 576"));
		assertThat(general.retrieveWord(loadImage("images/tests/pot_value/915.png")), is("915"));
	}
    
    private ImageSegment loadImage(String fileName) {
    	return new ImageSegment(ImageUtil.loadImage(fileName));
    }
    
    private TemplateAlphabet anAlphabet(String trainningInputFolder, int threshold, boolean isLetterBlack) {
        File trainningFolder = new File(trainningInputFolder);
        TemplateTrainer trainer = new TemplateTrainer();
        for(File trainningInputImage : trainningFolder.listFiles(new PNGFilter())) {
            String fileName = trainningInputImage.getName();
            String value = fileName.substring(0, fileName.indexOf(".png"));
            if(value.contains("_at")) {
            	value = value.replaceAll("_at", "@");
            }
            if(value.contains("_colon")) {
            	value = value.replaceAll("_colon", ":");
            }
            if(value.contains("_comma")) {
            	value = value.replaceAll("_comma", ",");
            }
            if(value.contains("_")) {
            	int indexOfUnderscore = value.indexOf("_");
            	if(indexOfUnderscore+1 != value.length()) // isnt underscore the last char?
            		value = value.substring(0, indexOfUnderscore);
            }
            trainer.addTrainingInput(value, ImageUtil.loadImage(trainningInputImage));
        }

        trainer.learn(new AlphabetProperties(threshold, isLetterBlack));
        return trainer.getTemplateAlphabet();
    }
}
