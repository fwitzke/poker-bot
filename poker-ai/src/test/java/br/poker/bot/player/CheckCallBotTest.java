package br.poker.bot.player;

import org.junit.Test;

import br.poker.bot.player.BotPlayer;
import br.poker.bot.player.CheckCallBot;
import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;
import static br.poker.model.action.Action.*;
import static br.poker.model.table.PokerTableFactory.createPokerTable;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class CheckCallBotTest {
	@Test
	public void shouldChooseCheckIfActionIsAvailable() throws Exception {
		BotPlayer bot = new CheckCallBot();
		PokerTable table = aTableWithActionsAvailable(CHECK, FOLD);
		assertThat(bot.getActionFor(table), is(CHECK));
	}

	@Test
	public void shouldChooseCallIfActionIsAvailable() throws Exception {
		BotPlayer bot = new CheckCallBot();
		PokerTable table = aTableWithActionsAvailable(CALL(10), FOLD, RAISE(20));
		assertThat(bot.getActionFor(table), is(CALL(10)));
	}

	private PokerTable aTableWithActionsAvailable(Action... actions) {
		PokerTable table = createPokerTable();
		for (Action action : actions)
			table.getAvailableActions().add(action);
		return table;
	}
}
