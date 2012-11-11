package br.poker.bot.aspects;

import java.util.HashMap;
import java.util.Map;

import br.poker.bot.player.Player;
import br.poker.model.action.Action;
import br.poker.model.handhistory.Entry;
import br.poker.model.handhistory.HandHistory;
import br.poker.model.table.PokerTable;
import br.poker.model.table.TableState;

public aspect HandHistoryTracking {
	private static Map<Integer, PlayerInfo> playersInfo = new HashMap<Integer, PlayerInfo>();
	private TableState tableState;

	class PlayerInfo {
		Integer stack;
		Boolean isOnHand;
	}
	
	pointcut table_updates(PokerTable table) : execution(* *.update(*)) && this(table);

	before(PokerTable table): table_updates(table){
		storePlayerActionInfo(table);
		tableState = table.getTableState();
	}

	after(PokerTable table) returning() : table_updates(table) {
		checkPlayerActionInfo(table);
		checkTableStateInfo(table);
	}

	private void checkTableStateInfo(PokerTable table) {
		TableState currentState = table.getTableState();
		if(tableState != currentState) {
			if(currentState == TableState.PRE_FLOP)
				table.getHandHistory().clear();
		}
	}
	
	private void storePlayerActionInfo(PokerTable table) {
		for(int i=0; i < table.getSeatsNumber(); i++){
			Player player = table.getPlayerAt(i);
			if(player != null) {
				PlayerInfo info = playersInfo.get(i) != null? playersInfo.get(i) : new PlayerInfo();
				int stack = player.getStack();
				if(stack != -1) {
					info.stack = stack;
				}
				info.isOnHand = !player.getHand().isEmpty();
				playersInfo.put(i, info);
			}
		}
	}

	private void checkPlayerActionInfo(PokerTable table) {
		for(int i=0; i < table.getSeatsNumber(); i++){
			Player player = table.getPlayerAt(i);
			if(player != null) {
				PlayerInfo before = playersInfo.get(i);
				if(before == null || before.stack == null)
					continue;
				HandHistory history = table.getHandHistory();
				
				int stack = player.getStack();
				if (before.stack > stack && (stack != -1)) {
					int raiseValue = before.stack-stack;
					if(raiseValue == table.getSB()) {
						if(!history.hasSmallBlind())
							history.addEntry(new Entry(player, Action.POST_SB));
					}else if(raiseValue == table.getBB()) {
						if(!history.hasBigBlind())
							history.addEntry(new Entry(player, Action.POST_BB));
					} else {
						history.addEntry(new Entry(player, Action.RAISE(raiseValue)));
					}
				} else if (before.stack < stack) {
					history.addEntry(new Entry(player, Action.WINS_POT));
				} else if((boolean) before.isOnHand && player.getHand().isEmpty()) {
					history.addEntry(new Entry(player, Action.FOLD));
				}
			}
		}
	}
}
