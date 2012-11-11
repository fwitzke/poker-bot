package br.poker.bot.player.ai;

import br.poker.model.Card;
import br.poker.model.Hand;
import br.poker.model.action.Action;

public class HandDecisionMatrix {
	private String[] columnsOrder = {"A","K","Q","J","T","9","8","7","6","5","4","3","2"};
	private int[][] cardRanks;
	private String matrixStr = 
		  "1|1|1|1|1|2|2|3|3|4|4|5|5|\n"
		+ "1|1|1|1|1|2|3|3|4|4|5|5|5|\n"
		+ "1|1|1|1|1|2|3|4|4|5|5|5|5|\n"
		+ "1|1|1|1|1|2|4|4|5|5|5|5|5|\n"
		+ "1|1|1|1|1|2|4|5|5|5|5|5|5|\n"
		+ "2|3|4|4|2|1|2|5|5|5|5|5|5|\n"
		+ "2|4|4|4|4|3|2|2|5|5|5|5|5|\n"
		+ "3|4|4|4|4|5|3|2|2|5|5|5|5|\n"
		+ "3|4|5|5|5|5|5|3|2|2|5|5|5|\n"
		+ "4|4|5|5|5|5|5|5|3|3|2|5|5|\n"
		+ "4|5|5|5|5|5|5|5|5|3|3|2|5|\n"
		+ "5|5|5|5|5|5|5|5|5|5|3|3|2|\n" 
		+ "5|5|5|5|5|5|5|5|5|5|5|3|3|\n";
	
	public HandDecisionMatrix() {
		cardRanks = createRanksMatrix();
	}

	private int[][] createRanksMatrix() {
		String[] rows = matrixStr.split("\n");
		int[][] matrix = new int[rows.length][rows.length];
		for(int r=0; r < rows.length; r++) {
			String row = rows[r];
			String[] columns = row.split("\\|");
			for (int c=0; c < columns.length; c++) {
				String column = columns[c];
				matrix[r][c] = Integer.parseInt(column);
			}
		}
		return matrix;
	}
	
	private int getCardIndex(Card c) {
		String card = c.toString().substring(0, 1);
		for(int i=0; i < columnsOrder.length ; i++)
			if(columnsOrder[i].equals(card))
				return i;
		return -1;
	}
	
	public Action getAction(Hand hand) {
		hand.sort();
		Card card1 = hand.getCard(1);
		Card card2 = hand.getCard(2);
		int card1Index = getCardIndex(card1);
		int card2Index = getCardIndex(card2);
		int rank = 0;
		if (card1.getSuit() == card2.getSuit()) 
			rank = cardRanks[card1Index][card2Index];
		else
			rank = cardRanks[card2Index][card1Index];
		
		switch(rank) {
			case 1:
				return Action.RAISE(0);
			case 2: 
				return Action.CALL(0);
			case 3:
				return Action.FOLD;
			default:
				return Action.FOLD;
		}
	}
}
