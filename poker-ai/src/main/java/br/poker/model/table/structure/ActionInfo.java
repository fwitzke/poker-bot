package br.poker.model.table.structure;

import br.poker.bot.input.image.ImageSegment;
import br.poker.model.action.Action;
import br.poker.ocr.TemplateAlphabet;
import static br.poker.util.Helper.defined;
import static br.poker.util.Helper.toCents;

public class ActionInfo extends BoxInfo {
	private Action action;
	
	public ActionInfo(int x, int y, int width, int height) {
		setPositionX(x);
		setPositionY(y);
		setWidth(width);
		setHeight(height);
	}

	public ActionInfo(BoxInfo box) {
		this(box.getPositionX(), box.getPositionY(), box.getWidth(), box.getHeight());
	}

	public Action getAction(ImageSegment tableImage, TemplateAlphabet alphabet) {
		Action action = null;
		String actionText = "";
        String actionValueText  = "";

        ImageSegment actionImage = tableImage.cut(this); 
        try { //TODO exception on ExtractLetterSegments, investigate, test and remove try/catch
        	actionText = alphabet.retrieveWord(actionImage).trim();
        } catch (Exception e) {
    		return null;
		}
        
        if(!defined(actionText) || actionText.contains("?")) {
            //Try to cut the image in 2 and get Action and Value
            int x, y, width, height;

            x=0; y=0;
            width = actionImage.getImage().getWidth();
            height = actionImage.getImage().getHeight()/2;

            BoxInfo upBoundaries = new BoxInfo(x, y, width, height);
            BoxInfo downBoundaries = new BoxInfo(x, y+height, width, height);
			ImageSegment up = actionImage.cut(upBoundaries);
            ImageSegment down = actionImage.cut(downBoundaries);

            actionText = alphabet.retrieveWord(up).trim();
            actionValueText = alphabet.retrieveWord(down).trim().replaceAll("\\$", "");
        }

        if("fold".equalsIgnoreCase(actionText)) {
            action = Action.FOLD;
        } else if("check".equalsIgnoreCase(actionText)) {
            action = Action.CHECK;
        } else {
			int value = toCents(actionValueText);
			if("raise to".equalsIgnoreCase(actionText)) {
			    action = Action.RAISE(value);
			} else if("call".equalsIgnoreCase(actionText)) {
			    action = Action.CALL(value);
			}
		}
        return action;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
