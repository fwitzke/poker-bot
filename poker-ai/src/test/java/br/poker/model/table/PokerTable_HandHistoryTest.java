package br.poker.model.table;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.action.Action;
import br.poker.model.handhistory.Entry;
import br.poker.model.handhistory.HandHistory;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;
@RunWith(MockitoJUnitRunner.class)
public class PokerTable_HandHistoryTest {
	private static final int RAISE_VALUE = 10;
	private static final int SB = 15;
	private static final int BB = 30;
	private static final int INITIAL_STACK = 100;
	private Player john, paul, jack;
	@Mock private BufferedImage tableImage;

	@Before
	public void setup() {
		john = new Player("john");
		john.setStack(INITIAL_STACK);
		
		paul = new Player("paul");
		paul.setStack(INITIAL_STACK);
		
		jack = new Player("jack");
		jack.setStack(INITIAL_STACK);
	}
	
	@Test
	public void itShouldDetectWhenAPlayerRaises() throws Exception {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.take(RAISE_VALUE);
				return true;
			}
		};
		
		table.update(tableImage);
		assertThat(table.getHandHistory().get(0), is(new Entry(john, Action.RAISE(RAISE_VALUE))));
	}
	
	@Test
	public void itShouldDetectAPlayerPostingBlinds() throws Exception {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.take(SB);
				paul.take(BB);
				setTableState(TableState.FLOP);
				return true;
			}
		};
		table.update(tableImage);
		HandHistory history = table.getHandHistory();
		assertThat(history.get(0), is(new Entry(john, Action.POST_SB)));
		assertThat(history.get(1), is(new Entry(paul, Action.POST_BB)));
	}
	
	@Test
	public void itShouldDetectWhenManyPlayersBet() throws Exception {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.take(RAISE_VALUE);
				paul.take(RAISE_VALUE);
				jack.take(RAISE_VALUE);
				return true;
			}
		};
		
		table.update(tableImage);
		HandHistory history = table.getHandHistory();
		assertThat(history.size(), is(3));
		assertThat(history.get(0), is(new Entry(john, Action.RAISE(RAISE_VALUE))));
		assertThat(history.get(1), is(new Entry(paul, Action.RAISE(RAISE_VALUE))));
		assertThat(history.get(2), is(new Entry(jack, Action.RAISE(RAISE_VALUE))));
	}
	
	@Test
	public void itShouldDetectWhenAPlayerWins() throws Exception {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.put(10);
				return true;
			}
		};
		
		table.update(tableImage);
		assertThat(table.getHandHistory().get(0), is(new Entry(john, Action.WINS_POT)));
	}
	
	@Test
	public void itShouldDetectWhenAPlayerFolds() throws Exception {
		john.dealCard(new Card());
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.clearHand();
				return true;
			}
		};
		table.update(tableImage);
		assertThat(table.getHandHistory().get(0), is(new Entry(john, Action.FOLD)));
	}
	
	@Test
	public void itShouldNotGenerateActionsWhenPlayerStackCannotBeRead() throws Exception {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.setStack(-1);
				return true;
			}
		};
		table.update(tableImage);
		assertThat(table.getHandHistory().size(), is(0));
	}
	
	@Test
	public void itShouldNotPostSmallBlindTwiceInSameHandHistory() {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.take(SB);
				jack.take(SB);
				return true;
			}
		};
		table.update(tableImage);
		assertThat(table.getHandHistory().size(), is(1));
	}
	
	@Test
	public void itShouldNotPostBigBlindTwiceInSameHandHistory() {
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				john.take(BB);
				jack.take(BB);
				return true;
			}
		};
		table.update(tableImage);
		assertThat(table.getHandHistory().size(), is(1));
	}
	
	@Test
	public void itShouldClearHistoryWhenStartingANewHand() {
		Entry any = new Entry(john, Action.POST_BB);
		PokerTable table = new PokerTableStub(9) {
			public boolean update(BufferedImage tableImage) {
				setTableState(TableState.PRE_FLOP);
				return true;
			}
		};
		table.setTableState(TableState.RIVER);
		table.getHandHistory().addEntry(any);
		
		table.update(tableImage);
		assertThat(table.getHandHistory().size(), is(0));
	}
	
	class PokerTableStub extends PokerTable {
		public PokerTableStub(int seatsNumber) {
			super(seatsNumber);
			seat(john, 2);
			seat(paul, 3);
			seat(jack, 4);
			setSB(SB);
			setBB(BB);
		}
		public boolean isVisible(BufferedImage tableImage) {
			return true;
		}
		public boolean isPlayerOnHand(int playerPosition, BufferedImage tableImage) {
			return true;
		}
	};
}
