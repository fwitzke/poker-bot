package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;

public abstract class AbstractImageOp {
    public static final int ALPHA = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;
    final int whiteColor[] = {255, 255, 255, 255};
    final int blackColor[] = {255, 0, 0, 0};
    final int redColor[] = {255, 255, 0, 0};
    final int greenColor[] = {255, 0, 255, 0};
    final int blueColor[] = {255, 0, 0, 255};
    final boolean debug = true;

    public BufferedImage processAndCountTime(BufferedImage toProcessImage) {
        BufferedImage res = process(toProcessImage);
        return res;
    }

    public abstract BufferedImage process(BufferedImage toProcessImage);

    //public abstract String getClassName();

    /**
     * Converts array of pixels to 32-bit integer
     * @param rgbArray
     * @return
     */
    public int getRgbInteger(int[] rgbArray) {
        //Writes the RGB info in a 32-Bit integer
        int rgb = (rgbArray[ALPHA] << 24) | (rgbArray[RED] << 16) | (rgbArray[GREEN] << 8) | rgbArray[BLUE];

        return rgb;
    }

    /**
     * Get RGB array from integer with pixel info
     * @param rgb
     * @return
     */
    public int[] getRgbArray(int rgb) {
        int rgbArray[] = new int[4];

        //Get RGB from 32-bit integer
        rgbArray[ALPHA] = ((rgb >> 24) & 0xff);
        rgbArray[RED] = ((rgb >> 16) & 0xff);
        rgbArray[GREEN] = ((rgb >> 8) & 0xff);
        rgbArray[BLUE] = ((rgb) & 0xff);

        return rgbArray;
    }

    public void line(BufferedImage image, int x0, int y0, int x1, int y1, int[] color) {
        int aux;
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            //swap(x0, y0)
            aux = x0;
            x0 = y0;
            y0 = aux;
            //swap(x1, y1)
            aux = x1;
            x1 = y1;
            y1 = aux;
        }

        if (x0 > x1) {
            //swap(x0, x1)
            aux = x0;
            x0 = x1;
            x1 = aux;
            //swap(y0, y1)
            aux = y0;
            y0 = y1;
            y1 = aux;
        }

        int deltax = x1 - x0;
        int deltay = Math.abs(y1 - y0);
        float error = 0f;
        float deltaerr = Float.valueOf(deltay) / Float.valueOf(deltax);
        int ystep = (y0 < y1) ? (1) : (-1);
        int y = y0;

        for (int x = x0; x <= x1; x++) {
            int xPlot, yPlot;
            if (steep) {
                //plot(y,x);
                xPlot = y;
                yPlot = x;
            } else {
                //plot(x,y);
                xPlot = x;
                yPlot = y;
            }

            image.setRGB(xPlot, yPlot, getRgbInteger(color));

            //Verify y axis error
            error = error + deltaerr;
            if (error >= 0.5) {
                y = y + ystep;
                error = error - 1.0f;
            }
        }
    }
}
