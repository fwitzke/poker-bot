package br.poker.bot.output;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import br.poker.bot.input.ScreenCapture;
import br.poker.model.table.structure.BoxInfo;

public class OutputHandler {
	private Robot robot;

	public OutputHandler() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void moveMouseTo(int x, int y) {
		robot.mouseMove(x, y);
	}


	public void clickAt(BoxInfo box, String windowTitle) {
		int x = box.getPositionX() + box.getWidth()/2;
		int y = box.getPositionY() + box.getHeight()/2;
		clickAt(x, y, windowTitle);
	}
	
	public void clickAt(int relativeX, int relativeY, String windowTitle) {
		Point absolute = getWindowPosition(windowTitle);
		moveMouseTo(absolute.x + relativeX, absolute.y + relativeY);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public Point getWindowPosition(String windowTitle) {
		ScreenCapture sc = new ScreenCapture();
		BufferedImage windowCapture = sc.capture(windowTitle);
		BufferedImage screenshot = sc.takeScreenshot();

		// Tries to find the captured screen within the screenshot
		int pixel_Center = windowCapture.getRGB(0, 0);
		int pixel_Right = windowCapture.getRGB(1, 0);
		int pixel_Down = windowCapture.getRGB(0, 1);

		for (int x = 0; x < screenshot.getWidth(); x++) {
			for (int y = 0; y < screenshot.getHeight(); y++) {
				if (screenshot.getRGB(x, y) == pixel_Center
						&& screenshot.getRGB(x + 1, y) == pixel_Right
						&& screenshot.getRGB(x, y + 1) == pixel_Down) {

					return new Point(x, y);
				}
			}
		}

		return new Point(0, 0);
	}
}
