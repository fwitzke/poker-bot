package br.poker.bot.input.image.operations;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import br.poker.util.Logger;

public class TrimOp extends AbstractImageOp {
    //Store image ranges
    class Range {
        public int start, end;
        public Range(int from, int to) {
            start = from;
            end = to;
        }
    }

    public BufferedImage process(BufferedImage image) {
    	if(image == null)
    		return null;
    	
        int height = image.getHeight();
        int width = image.getWidth();

        int start_range = -1;
        int end_range = -1;
        ArrayList<Range> imgRanges = new ArrayList<Range>();

        boolean foundBlackDot = false;

        //Look for columns with black dots
        for (int y = 0; y < height; y++) {

            foundBlackDot = false;
            for (int x = 0; x < width; x++) {
                //32-bit integer with RGB and Alpha Information
                int rgb = image.getRGB(x, y);

                //Get RGB from 32-bit integer
                int rgbArray[] = getRgbArray(rgb);

                if (rgbArray[RED] == blackColor[RED]) {
                    //System.out.println("Black dot found at column " + x + " line " + y);
                    foundBlackDot = true;
                    break;
                }

            }

            //Verify if the column had a black dot
            if(foundBlackDot) {
                if(start_range < 0)
                    start_range = y;
                else
                    end_range = y;
            } else {

                if(start_range >= 0) {
                    if(end_range < 0) end_range = start_range;
                    imgRanges.add(new Range(start_range, end_range));

                    //System.out.println("Adding range: " + start_range + " - " + end_range);
                }

                start_range = -1;
                end_range = -1;
            }
        }


        //whitespaces will be returned as null
        if(imgRanges.isEmpty()) {
            if(start_range == -1) {
                //Logger.log("Empty Ranges: It can be a whitespace!!! ");
                return null;
            } else {
                //has black dots in all lines of the image
                if(end_range < 0) end_range = start_range;
                    imgRanges.add(new Range(start_range, end_range));
            }
        }

        Range firstRange = imgRanges.get(0);
        Range lastRange = imgRanges.get(imgRanges.size() -1);
        
        int imgHeight = (lastRange.end-firstRange.start) + 1;
        BufferedImage dest = new BufferedImage(image.getWidth(), imgHeight, BufferedImage.TYPE_INT_RGB);
        
        //System.out.println("Creating image of size " + image.getWidth() + " x " + imgHeight);

        for(int x=0; x < image.getWidth(); x++) {
            for(int y=firstRange.start; y <= lastRange.end; y++){
                dest.setRGB(x, y-firstRange.start, image.getRGB(x, y));
            }
        }

        return dest;
        
    }


    

    /**
     * Extracts each letter from a given text line.
     * Important: Image should be already segmented!!!
     * @param image
     * @return
     */
    public BufferedImage[] extractLetterSegments(BufferedImage image) {
        BufferedImage[] imgArray = null;

        int height = image.getHeight();
        int width = image.getWidth();

        int start_range = -1;
        int end_range = -1;
        int white_space_count=0;
        ArrayList<Range> imgRanges = new ArrayList<Range>();

        boolean foundBlackDot = false;
        int blackDotsFound = 0;

        
        //Look for columns with black dots
        for (int x = 0; x < width; x++) {

            foundBlackDot = false;
            for (int y = 0; y < height; y++) {
                //32-bit integer with RGB and Alpha Information
                int rgb = image.getRGB(x, y);

                //Get RGB from 32-bit integer
                int rgbArray[] = getRgbArray(rgb);

                if (rgbArray[RED] == blackColor[RED]) {
                    //System.out.println("Black dot found at column " + x + " line " + y);
                    foundBlackDot = true;
                    blackDotsFound++;

                    break;
                }

            }

            //Verify if the column had a black dot
            if(foundBlackDot) {
                if(start_range < 0)
                    start_range = x;
                else
                    end_range = x;


                white_space_count = 0;
            } else {


                white_space_count++;
                
                if(start_range >= 0) {
                    if(end_range < 0) end_range = start_range;

                    imgRanges.add(new Range(start_range, end_range));

//                    System.out.println("Adding range: " + start_range + " - " + end_range);
                }

                //TODO IT CANNOT BE HARD-CODED. I should be able to adjust it as needed
                //Found a White Space
                if(white_space_count == 5) {
                    Logger.debug("White Space between: " + (x-4) + " x " + x);

                    imgRanges.add(new Range(x-4, x));

                    //TODO: Verify: if I reset it, each time N pixels are found, I put a space, but do I want this?
                    //white_space_count=0;
                }


                start_range = -1;
                end_range = -1;
            }
        }

        //No letter found
        if(blackDotsFound == 0) {
            return new BufferedImage[0];
        } else {
            if(white_space_count == 0) //no borders on image, all black dots
                imgRanges.add(new Range(start_range, end_range));
        }
        

        imgArray = new BufferedImage[imgRanges.size()];
        for(Range range : imgRanges) {
            int imgWidth = (range.end-range.start) + 1;
            BufferedImage dest = new BufferedImage(imgWidth, image.getHeight(), BufferedImage.TYPE_INT_RGB);

            //System.out.println("Creating image of size " + imgWidth + " x " + image.getHeight());
            
            for(int x=range.start; x <= range.end; x++) {
                for(int y=0; y < image.getHeight(); y++){
                    dest.setRGB(x-range.start, y, image.getRGB(x, y));
                }
            }

            //set the image in array
            imgArray[imgRanges.indexOf(range)] = dest;
        }

        return imgArray;
    }
}
