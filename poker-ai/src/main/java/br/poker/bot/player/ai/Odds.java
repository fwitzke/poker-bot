package br.poker.bot.player.ai;
public class Odds {
	public int WIN, TIE, LOSE;

	public double win() {
		return WIN / totalCombinations();
	}

	public double lose() {
		return LOSE / totalCombinations();
	}

	public double tie() {
		return TIE / totalCombinations();
	}

	public double totalCombinations() {
		return WIN + TIE + LOSE;
	}
	
	@Override
	public String toString() {
		String res="";
		res += "WIN:  " + WIN  + " | " + win() + "\n";
		res += "LOSE: " + LOSE + " | " + lose() + "\n";
		res += "TIE:  " + TIE  + " | " + tie() + "\n";
		res += "TOTAL_COMBINATIONS: " + totalCombinations();
		return res;
	}
}