package br.poker.bot.controller;

import java.awt.image.BufferedImage;

import br.poker.bot.input.ScreenCapture;
import br.poker.bot.output.OutputHandler;
import br.poker.bot.player.BotPlayer;
import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;
import br.poker.model.table.PokerTableFactory;
import br.poker.model.table.structure.ActionInfo;
import br.poker.util.Logger;

public class TableController implements Runnable {
	private PokerTable table;
	private BotPlayer bot;
	private ScreenCapture screenCapture;
	private OutputHandler handler;
	private Thread tracker;
	private String tableWindowName;
	private static boolean stopTracking;
	private int totalCaptures;
	private long captureInterval = 500L; // Default sleep time

	public TableController(String windowName, BotPlayer bot, OutputHandler handler) {
		this.tableWindowName = windowName;
		this.bot = bot;
		this.handler = handler;
		screenCapture = new ScreenCapture();
		table = PokerTableFactory.createPokerTable();
		TableController.stopTracking = false;

		updateTableInfo();
	}

	public void updateTableInfo() {
		table.update(capture());
		table.update(tableWindowName);
	}

	public void actOnTable(PokerTable table) {
		if (table.isActionRequired()) {
			Action action = bot.getActionFor(table);
			ActionInfo box = table.getActionInfo(action);
			handler.clickAt(box, tableWindowName);
		}
	}

	private BufferedImage capture() {
		totalCaptures++;
		return screenCapture.capture(tableWindowName);
	}

	public void startTracking() {
		TableController.stopTracking = false;
		tracker = new Thread(this, "Table Controller: " + tableWindowName);
		tracker.start();
	}

	public void stopTracking() {
		TableController.stopTracking = true;
		tracker.interrupt();
	}

	public void run() {
		try {
			String threadName = Thread.currentThread().getName();
			Logger.log("Started running thread " + threadName);

			while (!stopTracking) {
				boolean success = table.update(capture());
				if (success) {
					Logger.out("Capture no.  " + totalCaptures + "\n" + table);
					actOnTable(table);
				}

				Thread.sleep(captureInterval);
			}
			Logger.log("End of thread " + threadName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PokerTable getPokerTable() {
		return table;
	}

	public void setPokerTable(PokerTable pokerTable) {
		this.table = pokerTable;
	}

	public int getTotalCaptures() {
		return totalCaptures;
	}

	public void setCaptureInterval(long time) {
		this.captureInterval = time;
	}
}
