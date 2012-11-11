package br.poker.bot.player;

import br.poker.bot.player.ai.HandDecisionMatrix;
import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;
import br.poker.model.table.TableState;

public class SmartPlayer implements BotPlayer {
	private final HandDecisionMatrix matrix;

	public SmartPlayer(HandDecisionMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public Action getActionFor(PokerTable table) {
		Player myself = table.getMyself();
		if(table.getTableState() == TableState.PRE_FLOP)
			return matrix.getAction(myself.getHand());
		else
			return new CheckCallBot().getActionFor(table);
	}
}
