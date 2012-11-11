package br.poker.model.table.structure;

import java.awt.image.BufferedImage;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.ocr.TemplateAlphabet;
import static br.poker.util.Helper.toCents;

public class BoxInfo {
	private int positionX, positionY, width, height;
	protected ImageCutOp cut;

	public BoxInfo() {
		this.cut = new ImageCutOp();
	}

	public BoxInfo(int x, int y, int width, int height, ImageCutOp cutOp) {
		this.cut = cutOp;
		setPositionX(x);
		setPositionY(y);
		setWidth(width);
		setHeight(height);
	}

	public BoxInfo(int x, int y, int width, int height) {
		this(x, y, width, height, new ImageCutOp());
	}

	public String getText(BufferedImage tableImage, TemplateAlphabet alphabet) {
		BufferedImage textImage = cut.process(tableImage, this);
		return alphabet.retrieveWord(textImage).trim();
	}

	public int getNumber(BufferedImage tableImage, TemplateAlphabet alphabet) {
		return toCents(getText(tableImage, alphabet));
	}

	public int getPotValue(BufferedImage tableImage, TemplateAlphabet alphabet) {
		String text = getText(tableImage, alphabet);
		text = text.replaceAll("POT", "");
		text = text.replaceAll("Pot:", "");
		return toCents(text);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String toString() {
		return "[Position: (" + getPositionX() + "," + getPositionY() + ")"
				+ "Size: " + getWidth() + "x" + getHeight() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxInfo other = (BoxInfo) obj;
		if (height != other.height)
			return false;
		if (positionX != other.positionX)
			return false;
		if (positionY != other.positionY)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
