package br.poker.bot.player;

import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;

public interface BotPlayer {
	Action getActionFor(PokerTable table);
}
