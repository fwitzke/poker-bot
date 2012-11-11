package br.poker.model.table.pokerstars;

import org.junit.Test;

import br.poker.model.table.PokerTable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class PokerStars9PlayersTableTest {
	@Test
	public void testStarsTableCreation() throws Exception {
		PokerTable table = new PokerStars9PlayersTable();
		assertThat(table.getTableInfo().getSeats(), is(9));
		assertNotNull(table.getTableInfo().getPokerPlayerInfo(0));
		assertNotNull(table.getAlphabet());
	}
}
