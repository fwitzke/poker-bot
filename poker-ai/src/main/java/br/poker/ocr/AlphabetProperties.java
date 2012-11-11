package br.poker.ocr;

public class AlphabetProperties {
	private int threshold;
	private boolean letterBlack;

	public AlphabetProperties(int threshold, boolean letterBlack) {
		this.threshold = threshold;
		this.letterBlack = letterBlack;
	}

	public int getThreshold() {
		return threshold;
	}

	public boolean isLetterBlack() {
		return letterBlack;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void setLetterBlack(boolean letterBlack) {
		this.letterBlack = letterBlack;
	}
}
