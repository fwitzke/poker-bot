package br.poker.bot.input;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jna.extra.GDI32Extra;
import jna.extra.User32Extra;
import br.poker.util.Logger;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class ScreenCapture {
	public static final int DEFAULT_WIDTH = 1360;
	public static final int DEFAULT_HEIGHT = 768;

	public BufferedImage capture(String windowName) {
        try {
        	return capture(getWindowHandle(windowName));
        } catch (Exception e) {
        	Logger.error("Unable to capture screen due to: " + e.getMessage());
		}
        return null;
    }
    
    private BufferedImage capture(HWND hWnd) {
        HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
        HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

        RECT bounds = new RECT();
        User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;

        HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

        HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
        GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, new DWORD(0x00CC0020));

        GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
        GDI32.INSTANCE.DeleteDC(hdcMemDC);

        BITMAPINFO bmi = new BITMAPINFO();
        bmi.bmiHeader.biWidth = width;
        bmi.bmiHeader.biHeight = -height;
        bmi.bmiHeader.biPlanes = 1;
        bmi.bmiHeader.biBitCount = 32;
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

        Memory buffer = new Memory(width * height * 4);
        GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

        GDI32.INSTANCE.DeleteObject(hBitmap);
        User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);

        return image;
    }
    
    /**
     * Retrieves list of current available windows
     * @return windows titles
     */
    public List<String> enumerateWindows() {
        return enumerateWindowsByWindowClass(null);
    }

    public List<String> listPokerTables() {
        String windowClass = "PokerStarsTableFrameClass"; //TODO should not be hard coded
        return enumerateWindowsByWindowClass(windowClass);
    }

    public List<String> enumerateWindowsByWindowClass(String windowClass) {
        final List<String> windows = new ArrayList<String>();
        final String strWindowName = windowClass;

        User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new WNDENUMPROC() {
            public boolean callback(HWND hwnd, Pointer pntr) {
                String title = getWindowText(hwnd);

                //adds only windows with title name
                if (!"".equals(title)) {
                    //if the caller specified a class name, filter it
                    if (strWindowName != null) {
                        if(strWindowName.equals(getWindowClass(hwnd)))
                            windows.add(title);
                    } else {
                        windows.add(title);
                    }
                }
                return true;
            }
        }, null);

        return windows;
    }
    
    /**
     * Retrieves text from Window Title bar
     * @param handle
     * @return
     */
    private String getWindowText(HWND handle) {
        String text = null;

        int length = User32.INSTANCE.GetWindowTextLength(handle) + 1;
        char[] text_c = new char[length];
        User32.INSTANCE.GetWindowText(handle, text_c, length);

        text = Native.toString(text_c);

        return text;
    }

    private String getWindowClass(HWND handle) {
        String text = null;

        int length = 100;
        char[] text_c = new char[length];
        User32.INSTANCE.GetClassName(handle, text_c, length);

        text = Native.toString(text_c);

        return text;
    }

    private HWND getWindowHandle(String windowTitle) {
        HWND hWnd = User32.INSTANCE.FindWindow(null, windowTitle);
        return hWnd;
    }
 
    public BufferedImage takeScreenshot() {
    	return takeScreenshot(new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

	public BufferedImage takeScreenshot(Rectangle rectangle) {
		try {
			return new Robot().createScreenCapture(rectangle);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return null;
	}

}
