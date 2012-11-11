package br.poker.ocr;

import java.awt.image.BufferedImage;

public class TrainingInput {
    private String value;
    private BufferedImage image;

    public TrainingInput(String value, BufferedImage image) {
        this.value = value;
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public BufferedImage getImage() {
        return image;
    }
}
