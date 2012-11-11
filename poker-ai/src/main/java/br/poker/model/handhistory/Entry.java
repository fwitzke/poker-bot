package br.poker.model.handhistory;

import br.poker.bot.player.Player;
import br.poker.model.action.Action;

public class Entry {
	private final Player player;
	private final Action action;

	public Entry(Player player, Action action) {
		this.player = player;
		this.action = action;
	}

	public Player getPlayer() {
		return player;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public String toString() {
		return player.getName() + " " + action;
	}
	
	public boolean equals(Object obj) {
		Entry other = (Entry) obj;
		return player.equals(other.getPlayer()) && action.equals(other.getAction());
	}
}
