package br.poker.bot.player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.player.ai.HandDecisionMatrix;
import br.poker.model.Hand;
import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;
import br.poker.model.table.TableState;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class SmartPlayerTest {
	@Mock private Hand hand;
	@Mock private HandDecisionMatrix matrix;
	@Mock private PokerTable table;
	@Mock private Player pigroxalot;
	private SmartPlayer bot;
	
	@Before
	public void setup() {
		when(table.getMyself()).thenReturn(pigroxalot);
		when(pigroxalot.getHand()).thenReturn(hand);
		bot = new SmartPlayer(matrix);
	}
	
	@Test
	public void itShouldUseHandMatrixDuringPreFlop() throws Exception {
		when(table.getTableState()).thenReturn(TableState.PRE_FLOP);
		bot.getActionFor(table);
		verify(matrix).getAction(hand);
	}
	
	@Test
	public void itShouldCheckCallAfterPreFlop() throws Exception {
		when(table.getTableState()).thenReturn(TableState.FLOP);
		Action action = bot.getActionFor(table);
		verifyZeroInteractions(matrix);
		assertThat(action, is(Action.CHECK));
	}
}
