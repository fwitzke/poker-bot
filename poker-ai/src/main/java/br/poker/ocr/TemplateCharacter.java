package br.poker.ocr;

import java.awt.image.BufferedImage;

public class TemplateCharacter {
    private BufferedImage template;
    private String value;
    private float acceptable = 0.97f;
    
    public TemplateCharacter(String value, BufferedImage img) {
        setValue(value);
        setTemplate(img);
    }

    public boolean verifyMatch(TemplateCharacter anotherChar) {
        return verifyMatchPercentile(anotherChar) >= acceptable;
    }

    public float verifyMatchPercentile(TemplateCharacter anotherChar) {
        float match = 0.0f;

        BufferedImage anotherTemplate = anotherChar.getTemplate();

        //not same size
        if(template.getWidth() != anotherTemplate.getWidth() ||
                template.getHeight() != anotherTemplate.getHeight())
            return 0.0f;

        int width = template.getWidth();//>anotherTemplate.getWidth()?anotherTemplate.getWidth():template.getWidth();
        int height = template.getHeight();//>anotherTemplate.getHeight()?anotherTemplate.getHeight():template.getHeight();
        float totalPixels = width*height;
        float matchedPixels = 0f;

        for(int x=0; x<width; x++) {
            for(int y=0;y<height; y++) {

                if( template.getRGB(x, y) == anotherTemplate.getRGB(x, y) )
                    matchedPixels++;
            }
        }

        match = matchedPixels/totalPixels;
        
        return match;
    }

    public void setTemplate(BufferedImage template) {
        this.template = template;
    }

    public BufferedImage getTemplate() {
        return template;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        String res ="";

        res += "(" + getValue() + ")";
        res += " - ";
        if(template == null)
            res += 0 + " x " + 0;
        else
            res += template.getWidth() + " x " + template.getHeight();

        return res;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TemplateCharacter) {
            TemplateCharacter otherChar = (TemplateCharacter)obj;

            if(this.getValue() == null) {
                if(otherChar.getValue() == null)
                    return true;
                else
                    return false;
            } else {
                return this.getValue().equals(otherChar.getValue());
            }
            
        } else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
