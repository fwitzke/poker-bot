package br.poker.bot.controller;

import java.util.List;
import java.util.Scanner;

import br.poker.bot.input.ScreenCapture;
import br.poker.bot.output.OutputHandler;
import br.poker.bot.player.SmartPlayer;
import br.poker.bot.player.ai.HandDecisionMatrix;
import br.poker.util.Logger;
import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
	private static ScreenCapture capture;
	private Scanner scanner;
	private List<String> pokerTables;

	public Main() {
		capture = new ScreenCapture();
		scanner = new Scanner(System.in);
		start();
	}

	private void start() {
		out.println("Welcome to JPokerBot v0.1 alpha");
		out.println("Instructions:");
		out.println("ls - list poker tables");
		out.println("start <table name> - starts tracking table <table name>");
		while (true) {
			pokerTables = capture.listPokerTables();
			out.print("> ");
			String action = scanner.nextLine();
			if ("ls".equalsIgnoreCase(action)) {
				listPokerTables();
			} else if ("start".equalsIgnoreCase(action)) {
				String tableName = null;
				if(pokerTables.size() == 1) {
					tableName = pokerTables.get(0);
				} else {
					out.print("table name: ");
					tableName = scanner.nextLine();
				}
				startTracking(tableName);
			} else if ("exit".equalsIgnoreCase(action)) {
				exit(0);
			}
		}
	}

	private TableController startTracking(String tableName) {
		SmartPlayer bot = new SmartPlayer(new HandDecisionMatrix());
		TableController controller = new TableController(tableName, bot, new OutputHandler());
		controller.startTracking();
		return controller;
	}

	private void listPokerTables() {
		if (pokerTables.isEmpty()) {
			out.println("No tables found");
		} else {
			for (String tableName : pokerTables) {
				out.println(tableName);
			}
		}
	}

	public static void main(String[] args) {
		Logger.setLevel(Logger.LOG);
		new Main();
	}
}
