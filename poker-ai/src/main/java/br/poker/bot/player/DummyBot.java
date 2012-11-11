package br.poker.bot.player;

import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;

public class DummyBot implements BotPlayer {
	public Action getActionFor(PokerTable table) {
		return Action.FOLD;
	}
}
