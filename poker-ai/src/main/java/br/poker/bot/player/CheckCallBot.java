package br.poker.bot.player;

import br.poker.model.action.Action;
import br.poker.model.action.Call;
import br.poker.model.table.PokerTable;
import static br.poker.model.action.Action.CHECK;

public class CheckCallBot implements BotPlayer {
	public Action getActionFor(PokerTable table) {
		for (Action action : table.getAvailableActions()) {
			if (action.equals(CHECK) || action instanceof Call)
				return action;
		}
		return CHECK;
	}
}
