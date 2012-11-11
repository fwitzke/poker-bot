package br.poker.bot.player;

import org.junit.Test;

import br.poker.bot.player.BotPlayer;
import br.poker.bot.player.DummyBot;
import br.poker.model.table.PokerTable;
import static br.poker.model.action.Action.FOLD;
import static br.poker.model.table.PokerTableFactory.createPokerTable;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class DummyBotTest {
	@Test
	public void shouldCreateADummyBotPlayerThatAlwaysFold() throws Exception {
		BotPlayer bot = new DummyBot();
		PokerTable table = createPokerTable();
		assertThat(bot.getActionFor(table), is(FOLD));
	}
}