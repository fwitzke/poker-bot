package br.poker.model.handhistory;

import org.junit.Test;

import br.poker.bot.player.Player;
import br.poker.model.action.Action;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class HandHistoryTest {
	@Test
	public void itShouldNotHaveSmallBlindWhenEmpty() throws Exception {
		assertThat(new HandHistory().hasSmallBlind(), is(false));
	}
	
	@Test
	public void itShouldHaveSmallBlindPosted() throws Exception {
		HandHistory history = new HandHistory();
		history.addEntry(postSB());
		assertThat(history.hasSmallBlind(), is(true));
	}
	
	@Test
	public void itShouldNotHaveBigBlindWhenEmpty() throws Exception {
		assertThat(new HandHistory().hasBigBlind(), is(false));
	}
	
	@Test
	public void itShouldHaveBigBlindPosted() throws Exception {
		HandHistory history = new HandHistory();
		history.addEntry(postBB());
		assertThat(history.hasBigBlind(), is(true));
	}
	
	@Test
	public void itShouldClearHandHistory() throws Exception {
		HandHistory history = new HandHistory();
		history.addEntry(postBB());
		history.clear();
		assertThat(history.size(), is(0));
	}
	
	private Entry postSB() {
		return new Entry(new Player(), Action.POST_SB);
	}

	private Entry postBB() {
		return new Entry(new Player(), Action.POST_BB);
	}
}
