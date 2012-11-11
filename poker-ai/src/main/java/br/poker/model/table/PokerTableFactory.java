package br.poker.model.table;

import br.poker.model.table.pokerstars.PokerStars9PlayersTable;

public class PokerTableFactory {
	public static final String POKERSTARS = "POKERSTARS";

	public static PokerTable createPokerTable() {
		return createPokerTable(POKERSTARS, 9);
	}

	public static PokerTable createPokerTable(String tableRoom, int seats) {
		try {
			if (POKERSTARS.equals(tableRoom)) {
				switch (seats) {
				case 9:
					return new PokerStars9PlayersTable();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return null;
	}
}
