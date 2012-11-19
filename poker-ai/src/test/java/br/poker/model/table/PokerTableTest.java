package br.poker.model.table;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.action.Action;
import br.poker.model.action.Call;
import br.poker.model.action.Check;
import br.poker.model.action.Fold;
import br.poker.model.action.Raise;

public class PokerTableTest {
	private static final int FIRST_CHAIR = 1;
	private static final int THIRD_CHAIR = 3;
	private PokerTable table;

	@Before
	public void setup() {
		table = PokerTableFactory.createPokerTable("POKERSTARS", 9);
	}
	
	@Test
	public void shouldKnowWherePlayersAreSitting() {
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
	
	@Test
	public void itTakesPokerTableSnapshot() {
		Player anyone = new Player("anyone", 30);
		
		table.setSB(5);
		table.setBB(10);
		table.seat(anyone, 0);
		
		PokerTableSnapshot snapshot = new PokerTableSnapshot(new BettingStructure(5, 10), asList(new PlayerInfo(0, 30)));
		
		assertThat(table.takeSnapshot(), is(snapshot));
	}
}
