package br.poker.util;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static br.poker.bot.input.image.ImageUtil.loadImage;
import static java.util.Arrays.asList;

public class PokerStarsTableMock {
	private String pokerTableTitle;
	private JFrame pokerWindow;
	private JPanel imagePanel;
	private List<BufferedImage> images;
	private int currentImage;
	private PokerTableMouseListener pokerTableMouseListener;
	
	public PokerStarsTableMock(String windowTitle) {
		this(windowTitle, asList(loadImage("images/3_players_my_cards.png")));
	}

	public PokerStarsTableMock(String windowTitle, List<BufferedImage> images) {
		pokerTableTitle = windowTitle;
		this.images = images;
		this.pokerTableMouseListener = new PokerTableMouseListener();
	}

	public void show(Point position) {
		currentImage = 0;
		pokerWindow = createPokerTableWindow();
		pokerWindow.setLocation(position);
		pokerWindow.setVisible(true);
	}
	
	public void show() {
		show(new Point(10, 10));
	}

	public void destroy() {
		pokerWindow.setVisible(false);
		pokerWindow = null;
	}
	
	public void nextImage() {
		if(++currentImage >= images.size())
			currentImage = 0;
		updateImagePanel();
	}

	private void updateImagePanel() {
		pokerWindow.getContentPane().remove(imagePanel);
		imagePanel = createImagePanel(images.get(currentImage));
		pokerWindow.getContentPane().add(imagePanel);
		pokerWindow.validate();
		pokerWindow.repaint();
	}

	private JFrame createPokerTableWindow() {
		JFrame frame = new JFrame(pokerTableTitle);

		BufferedImage displayImage = images.get(currentImage);
		int width = displayImage.getWidth();
		int height = displayImage.getHeight();

		imagePanel = createImagePanel(displayImage);

		frame.setLayout(new GridLayout());
		frame.getContentPane().add(imagePanel);
		frame.setSize(width + 8, height + 27);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		return frame;
	}

	public boolean wasClickedAt(int x, int y) {
		return pokerTableMouseListener.clickedPoints.contains(new Point(x, y));
	}
	
	private JPanel createImagePanel(final BufferedImage displayImage) {
		PokerStarsPanel panel = new PokerStarsPanel(displayImage);
		panel.addMouseListener(pokerTableMouseListener);
		return panel;
	}
	
	private final class PokerTableMouseListener extends MouseAdapter {
		private List<Point> clickedPoints = new ArrayList<Point>();

		@Override
		public void mouseClicked(MouseEvent event) {
			clickedPoints.add(event.getPoint());
			System.out.println(event.getPoint());
		}
	}

	private class PokerStarsPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final BufferedImage backgroung;

		public PokerStarsPanel(BufferedImage backgroung) {
			this.backgroung = backgroung;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroung, 0, 0, null);
		}
	}

	public static void main(String[] args) {
		new PokerStarsTableMock("MOCK TABLE").show();
	}
}
