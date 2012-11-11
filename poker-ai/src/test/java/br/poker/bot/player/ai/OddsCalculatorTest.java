package br.poker.bot.player.ai;

import org.junit.Before;
import org.junit.Test;

import br.poker.model.Hand;
import static br.poker.util.NumberBetween.between;

import static org.junit.Assert.assertThat;

public class OddsCalculatorTest {
	private OddsCalculator handGuru;

	@Before
	public void setup() {
		handGuru = new OddsCalculator();
	}

	@Test
	public void itCalculatesOddsAgainst1Opponent() {
		Hand playerCards = new Hand("Ks", "Qs");
		Hand boardCards = new Hand("2c", "3c", "4c");
		Odds odds = handGuru.getProbability(playerCards, boardCards, 1);

		assertThat(odds.win(), between(0.30, 0.32));
		assertThat(odds.lose(), between(0.61, 0.63));
		assertThat(odds.tie(), between(0.04, 0.06));
	}
}