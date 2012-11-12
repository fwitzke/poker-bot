package br.poker.ocr;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.input.image.LetterExtractor;
import br.poker.util.Logger;

public class TemplateTrainer {
    private List<TrainingInput> cltTrainingInput;
    private TemplateAlphabet templateAlphabet;

    public TemplateTrainer() {
        cltTrainingInput = new ArrayList<TrainingInput>();
    }

    public void addTrainingInput(String string, BufferedImage loadImage) {
        cltTrainingInput.add(new TrainingInput(string, loadImage));
    }

    public void learn(int threshold) {
        learn(new AlphabetProperties(threshold, true));
    }

    /**
     * Creates a TemplateAlphabet based on TrainingInput files
     */
    public void learn(AlphabetProperties properties) {
        templateAlphabet = new TemplateAlphabet(properties);

        Logger.log("Started learning procedure...");

        //Verify existing input
        if (cltTrainingInput.isEmpty()) {
            Logger.log("[ERROR] There is no training input ");
            return;
        } else {
            Logger.log("Using " + cltTrainingInput.size() + " training inputs");
        }

        for (TrainingInput input : cltTrainingInput) {
            //Try to extract letters from input image
        	ImageSegment[] letters = LetterExtractor.getLettersFromImage(new ImageSegment(input.getImage()), properties);

            //Perfect Match
            if (letters.length == input.getValue().length()) {
                Logger.log("Learning letter templates of input String: \"" + input.getValue() + "\"...");

                for (int i = 0; i < letters.length; i++) {
                    String lt = input.getValue().substring(i, i + 1);
                    TemplateCharacter newChar = new TemplateCharacter(lt, letters[i].getImage());

                    addCharToTemplate(newChar);
                }

            } else {
                //Learn it as a symbol
                if(letters.length == 1) {
                    TemplateCharacter newChar = new TemplateCharacter(input.getValue(), letters[0].getImage());
                    addCharToTemplate(newChar);

                //TODO:  In these cases could ask the user to provide the text of each letter found
                } else {
                    Logger.error("[TemplateTrainer]=> The image letters didn't match the input String: \"" + input.getValue() + "\"");
                    Logger.error("[TemplateTrainer]=> Expected " + input.getValue().length() + " but found "+ letters.length);
                }
            }
        }

        Logger.log("This alphabet has learned " + templateAlphabet.size() + " characters");
    }

    private void addCharToTemplate(TemplateCharacter newChar) {
        //includes the new char if it doesn't exist yet
        if (!templateAlphabet.hasCharacter(newChar)) {
            //Do not include white spaces in template
            if (" ".equals(newChar.getValue())) {
                Logger.log(" Skipping character '" + newChar.getValue() + "'");
            } else {
                Logger.log(" Learned character '" + newChar.getValue() + "'");
                templateAlphabet.addTemplateCharacter(newChar);
            }
        } else {
            Logger.log("Alphabet already contains another template for this symbol: " + newChar.getValue());

            if(!templateAlphabet.matches(newChar)) {
                Logger.log("Found another template for this symbol: " + newChar.getValue());
                templateAlphabet.addTemplateCharacter(newChar);
            }
        }
    }
    
    public TemplateAlphabet getTemplateAlphabet() {
        return templateAlphabet;
    }
}
