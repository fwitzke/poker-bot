package br.poker.bot.input.image.operations;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ThresholdRGBOp extends AbstractImageOp {

    //Threshold values
    private int thresholdRed, thresholdGreen, thresholdBlue;

    public ThresholdRGBOp() throws Exception {
        //Arbitrary values
        setThresholdRed(230);
        setThresholdGreen(64);
        setThresholdBlue(72);
    }

    public BufferedImage process(BufferedImage toProcessImage) {
        BufferedImage result = null;

        if (toProcessImage != null) {

            //Creates a new Destination image
            result = new BufferedImage(toProcessImage.getWidth(), toProcessImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            int height = toProcessImage.getHeight();
            int width = toProcessImage.getWidth();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                   
                    //32-bit integer with RGB and Alpha Information
                    int rgb = toProcessImage.getRGB(x, y);
                    //Get RGB from 32-bit integer
                    int rgbArray[] = getRgbArray(rgb);


                    float[] hsv = new float[3];
                    Color.RGBtoHSB(rgbArray[1], rgbArray[2], rgbArray[3], hsv);

                    if (hsv[0] >= 0.9 && hsv[1] >= 0.3 && hsv[2] >= 0.3) {
                        result.setRGB(x, y, getRgbInteger(blackColor));
                    } else if (hsv[0] >= 0.0 && hsv[1] >= 0.0 && hsv[2] >= 0.9) {
                        result.setRGB(x, y, getRgbInteger(blueColor));
                    } else {
                        result.setRGB(x, y, getRgbInteger(whiteColor));
                    }

                    /*
                    //Red Color
                    if(	rgbArray[RED] >= thresholdRed &&
                    rgbArray[GREEN] >= thresholdGreen && rgbArray[GREEN] <= 225 &&
                    rgbArray[BLUE]  >= thresholdBlue) {
                    result.setRGB(x, y, getRgbInteger(blackColor));
                    //result.setRGB(x, y, getRgbInteger(rgbArray));
                    }
                    else
                    if(rgbArray[RED] > 200 &&
                    rgbArray[GREEN] > 200 &&
                    rgbArray[BLUE]  > 200) {
                    result.setRGB(x, y, getRgbInteger(blueColor));
                    }
                    //Other Colors
                    else
                    result.setRGB(x, y, getRgbInteger(whiteColor));

                     */
                }
            }
        }

        return result;
    }

    public int getThresholdRed() {
        return thresholdRed;
    }

    public void setThresholdRed(int thresholdRed) throws Exception {
        if (thresholdRed < 0 || thresholdRed > 255) {
            throw new Exception("Threshold value must be a number between 0 and 255");
        }

        this.thresholdRed = thresholdRed;
    }

    public int getThresholdGreen() {
        return thresholdGreen;
    }

    public void setThresholdGreen(int thresholdGreen) throws Exception {
        if (thresholdGreen < 0 || thresholdGreen > 255) {
            throw new Exception("Threshold value must be a number between 0 and 255");
        }

        this.thresholdGreen = thresholdGreen;
    }

    public int getThresholdBlue() {
        return thresholdBlue;
    }

    public void setThresholdBlue(int thresholdBlue) throws Exception {
        if (thresholdBlue < 0 || thresholdBlue > 255) {
            throw new Exception("Threshold value must be a number between 0 and 255");
        }

        this.thresholdBlue = thresholdBlue;
    }

}
