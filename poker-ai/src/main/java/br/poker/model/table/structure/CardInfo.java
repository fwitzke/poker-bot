package br.poker.model.table.structure;

import java.awt.image.BufferedImage;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.model.Card;
import br.poker.ocr.TemplateAlphabet;
import static br.poker.util.Helper.*;

public class CardInfo extends BoxInfo {
	private int index;
	private ImageCutOp cut;
	
	public CardInfo(int x, int y, int width, int height, ImageCutOp cut) {
		setPositionX(x);
		setPositionY(y);
		setWidth(width);
		setHeight(height);
		this.cut = cut;
	}

	public CardInfo() {
		this.cut = new ImageCutOp();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Card getCard(BufferedImage tableImage, TemplateAlphabet alphabet) {
		int x = getPositionX();
		int y = getPositionY();
		int w = getWidth();
		int h = getHeight();

		BufferedImage cardRank = cut.process(tableImage, x, y, w, h);
		BufferedImage cardSuite = cut.process(tableImage, x, y + h, w, h);

		String cardRankText = alphabet.retrieveWord(cardRank);
		String cardValueText = alphabet.retrieveWord(cardSuite);

		if("10".equals(cardRankText))
			cardRankText = "T";
		
		if (defined(cardRankText) && !cardRankText.contains("?")
				&& defined(cardValueText) && !cardValueText.contains("?")) {
			return new Card(cardRankText.trim() + cardValueText.trim());
		}

		return null;
	}
}
