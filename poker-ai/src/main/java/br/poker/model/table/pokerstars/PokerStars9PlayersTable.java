package br.poker.model.table.pokerstars;

import static br.poker.bot.input.image.operations.AbstractImageOp.BLUE;
import static br.poker.bot.input.image.operations.AbstractImageOp.GREEN;
import static br.poker.bot.input.image.operations.AbstractImageOp.RED;
import static br.poker.ocr.TemplateAlphabet.fromFile;

import java.awt.image.BufferedImage;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.model.table.PokerTable;
import br.poker.model.table.structure.CardExInfo;
import br.poker.model.table.structure.PokerTableInfo;

public class PokerStars9PlayersTable extends PokerTable {
	public PokerStars9PlayersTable() throws Exception {
        super(9);
        tableInfo = new PokerTableInfo("tables/stars/poker_stars_9_players.xml");
        setAlphabet(fromFile("tables/stars/fonts/general"));
        setAlphabetDeck(fromFile("tables/stars/fonts/deck"));
        setAlphabetActions(fromFile("tables/stars/fonts/actions"));
    }

	@Override
	public boolean isPlayerOnHand(int playerPosition, BufferedImage tableImage)  {
        CardExInfo cardInfo = tableInfo.getPokerPlayerInfo(playerPosition).getVisibleCardInfo();
		int cardPosX = cardInfo.getPositionX();
        int cardPosY = cardInfo.getPositionY();

        //just to access the method to read rgb
        ImageCutOp op = new ImageCutOp();

        //Check 2 pixels present in the cards icon
        int[] rgbArray = op.getRgbArray(tableImage.getRGB(cardPosX+2, cardPosY+2));
        if(rgbArray[RED] > 220 && rgbArray[GREEN] > 120 && rgbArray[BLUE] > 120) { //looks like red
            return true;
        }
        return false;
    }

	@Override
	public boolean isVisible(BufferedImage tableImage) {
        //just to access the method to read rgb
        ImageCutOp anyOp = new ImageCutOp();

        //Check if that specific pixel is green
        int[] rgbArray = anyOp.getRgbArray(tableImage.getRGB(353, 98)); //TODO remove hard coding
        return rgbArray[RED] < 30 && rgbArray[GREEN] > 100 && rgbArray[BLUE] < 30;
    }
}
