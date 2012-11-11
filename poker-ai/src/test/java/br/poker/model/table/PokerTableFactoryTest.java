package br.poker.model.table;

import static org.hamcrest.core.Is.*;
import org.junit.Test;
import static br.poker.model.table.PokerTableFactory.*;
import br.poker.model.table.pokerstars.PokerStars9PlayersTable;

import static org.junit.Assert.*;

public class PokerTableFactoryTest {
	@Test
	public void shouldCreateDefaultPokerTable() throws Exception {
		PokerTable table = PokerTableFactory.createPokerTable();
		assertTrue(table instanceof PokerStars9PlayersTable);
	}
	
	@Test
	public void shouldCreatePokerTableAccordingly() throws Exception {
		PokerTable table = PokerTableFactory.createPokerTable(POKERSTARS, 9);
		assertThat(table.getSeatsNumber(), is(9));
	}
	
	@Test
	public void shouldReturnNullIfTableParametersDontMatchAnyTable() throws Exception {
		PokerTable table = PokerTableFactory.createPokerTable("UNKOWN_ROOM", 6);
		assertNull(table);
	}
}
