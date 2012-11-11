package br.poker.bot.input;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.poker.util.PokerStarsTableMock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.core.Is.is;

public class ScreenCaptureTest {
	private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
	private static final String WINDOW_TITLE = "Poker Table Test";
	private ScreenCapture screenCapture;
	private PokerStarsTableMock windowMock;
    
    @Before
    public void createScreenCaptureUtility() {
    	screenCapture = new ScreenCapture();
    }
    
    @Before
    public void createDummyWindow() throws Exception {
    	windowMock = new PokerStarsTableMock(WINDOW_TITLE);
		windowMock.show();
    }

    @After
    public void destroyDummyWindow() throws Exception {
    	windowMock.destroy();
    }
    
    @Test
    public void shouldCaptureScreenByWindowTitle() {
        BufferedImage capture = screenCapture.capture(WINDOW_TITLE);
        assertNotNull(capture);
        assertTrue(capture.getWidth() > 0 && capture.getHeight() > 0);
    }
    
    @Test
    public void shouldReturnNullIfScreenCannotBeCaptured() {
    	assertNull(screenCapture.capture("non existent window"));
    }
    
    @Test
    public void shouldEnumerateAllWindows() {
        List<String> windows = screenCapture.enumerateWindows();

        for (String windowTitle : windows) {
            System.out.println(windowTitle);
        }

        assertNotNull(windows);
        assertTrue(windows.size() > 0);
    }

    @Test
    public void shouldTakeScreenshotBasedOnResolution() {
    	Rectangle resolution = new Rectangle(0, 0, WIDTH, HEIGHT);
		BufferedImage screenshot = screenCapture.takeScreenshot(resolution);
		assertThat(screenshot.getWidth(), is(WIDTH));
    	assertThat(screenshot.getHeight(), is(HEIGHT));
    }
    
    @Test
    public void shouldTakeScreenshotBasedOnDefaultResolution() {
    	BufferedImage screenshot = screenCapture.takeScreenshot();
		assertThat(screenshot.getWidth(), is(ScreenCapture.DEFAULT_WIDTH));
    	assertThat(screenshot.getHeight(), is(ScreenCapture.DEFAULT_HEIGHT));
    }
    
    @Test
    public void shouldEnumerateWindowsByClass() {
        //String windowClass="PokerStarsTableFrameClass";
    	//String windowClass = "Notepad";
    	String windowClass = "SunAwtFrame"; // Java Window
        List<String> windows = screenCapture.enumerateWindowsByWindowClass(windowClass);
        assertTrue(windows.size() > 0);
    }
}
