package br.poker.model.table;

import org.junit.Test;

import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.action.Action;
import br.poker.model.action.Call;
import br.poker.model.action.Check;
import br.poker.model.action.Fold;
import br.poker.model.action.Raise;
import static br.poker.model.table.PokerTable.NO_LIMIT_HOLDEM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.core.Is.is;

public class PokerTableTest {
	private static final String TABLE_NAME = "TEST TABLE";
	private static final String US_DOLLAR = "USD";
	private static final int ONE_CENT = 1;
	private static final int TWO_CENTS = 2;
	private static final int POT = 30;
	private static final int FIRST_CHAIR = 1;
	private static final int THIRD_CHAIR = 3;

	@Test
	public void shouldCreateA9PlayerTable() {
		PokerTable table = aTable();
		table.setGameType(NO_LIMIT_HOLDEM);
		table.setTableName(TABLE_NAME);
		table.setSB(ONE_CENT);
		table.setBB(TWO_CENTS);
		table.setTotalPot(POT);
		table.setCurrency(US_DOLLAR);

		assertThat(table.getTableName(), is(TABLE_NAME));
		assertThat(table.getGameType(), is(NO_LIMIT_HOLDEM));
		assertThat(table.getSeatsNumber(), is(9));
		assertThat(table.getSittingPlayers(), is(0));
		assertThat(table.getSB(), is(ONE_CENT));
		assertThat(table.getBB(), is(TWO_CENTS));
		assertThat(table.getTotalPot(), is(POT));
		assertThat(table.getCurrency(), is(US_DOLLAR));
	}

	@Test
	public void shouldKnowWherePlayersAreSitting() {
		PokerTable table = aTable();

		assertThat(table.getSittingPlayers(), is(0));
		assertTrue(table.isPositionFree(FIRST_CHAIR));
		assertTrue(table.isPositionFree(THIRD_CHAIR));

		table.seat(new Player(), FIRST_CHAIR);
		assertFalse(table.isPositionFree(FIRST_CHAIR));
		assertThat(table.getSittingPlayers(), is(1));

		table.seat(new Player(), THIRD_CHAIR);
		assertEquals(false, table.isPositionFree(THIRD_CHAIR));
		assertThat(table.getSittingPlayers(), is(2));
	}

	@Test
	public void shoulDealPlayerCards() {
		PokerTable table = aTable();
		Player john = new Player();
		Player paul = new Player();

		table.seat(john, FIRST_CHAIR);
		table.seat(paul, THIRD_CHAIR);

		assertTrue(john.getHand().isEmpty());
		assertTrue(paul.getHand().isEmpty());

		table.startNewHand();
		table.dealPlayerCards();

		assertThat(john.getHand().size(), is(2));
		assertThat(paul.getHand().size(), is(2));
		assertTrue(table.getBoard().isEmpty());

		table.dealFlopCards(new Card("5h"), new Card("6h"), new Card("7h"));
		assertThat(table.getBoard().size(), is(3));

		table.dealTurnCard(new Card("8s"));
		assertThat(table.getBoard().size(), is(4));

		table.dealRiverCard(new Card("Ac"));
		assertThat(table.getBoard().size(), is(5));

		table.startNewHand();
		assertTrue(john.getHand().isEmpty());
		assertTrue(table.getBoard().isEmpty());
	}

	@Test
	public void shouldAllowPlayersToBet() {
		PokerTable table = aTable();
		table.setSB(5);
		table.setBB(10);

		Player fernando = new Player();
		fernando.setStack(100);

		Player john = new Player();
		john.setStack(100);

		table.seat(fernando, FIRST_CHAIR);
		table.seat(john, THIRD_CHAIR);
		table.postSmallBlind(fernando);
		table.postBigBlind(john);
		
		assertThat(fernando.getStack(), is(95));
		assertThat(john.getStack(), is(90));

		assertThat(table.getTotalPot(), is(15));

		table.action(fernando, new Raise(10));
		assertThat(table.getTotalPot(), is(25));
		assertThat(fernando.getStack(), is(85));
		assertThat(john.getStack(), is(90));

		table.action(john, new Call(5));
		assertThat(table.getTotalPot(), is(30));
		assertThat(fernando.getStack(), is(85));
		assertThat(john.getStack(), is(85));

		table.action(fernando, new Check());
		table.action(fernando, new Fold());
		assertThat(table.getTotalPot(), is(30));
	}

	@Test
	public void playersShouldNotBeOnCurrentHandAfterFolding() {
		PokerTable table = aTable();
		Player john = new Player();
		Player paul = new Player();
		Player carl = new Player();

		table.seat(john, 0);
		table.seat(paul, 1);
		table.seat(carl, 2);

		assertThat(table.getSittingPlayers(), is(3));

		table.dealPlayerCards();
		assertThat(table.getPlayersOnCurrentHand(), is(3));

		table.action(john, Action.FOLD);
		assertThat(table.getPlayersOnCurrentHand(), is(2));

		table.action(paul, Action.FOLD);
		assertThat(table.getPlayersOnCurrentHand(), is(1));
	}
	
	private PokerTable aTable() {
		return PokerTableFactory.createPokerTable("POKERSTARS", 9);
	}
}
