package br.poker.bot.player.ai;

import org.junit.Before;
import org.junit.Test;

import br.poker.model.Hand;
import br.poker.model.action.Action;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class HandDecisionMatrixTest {
	private HandDecisionMatrix matrix;

	@Before
	public void setup() {
		matrix = new HandDecisionMatrix();
	}
	
	@Test
	public void itShouldAlwaysRaiseAA() throws Exception {
		Hand pairOfAces = new Hand("As", "Ac");
		assertThat(matrix.getAction(pairOfAces), is(Action.RAISE(0)));
	}
	
	@Test
	public void itShouldNeverCall25() throws Exception {
		assertThat(matrix.getAction(new Hand("2s", "5c")), is(Action.FOLD));
	}
	
	@Test
	public void itShouldCall78WhenSuited() throws Exception {
		assertThat(matrix.getAction(new Hand("7c", "8s")), is(Action.FOLD));
		assertThat(matrix.getAction(new Hand("7s", "8s")), is(Action.CALL(0)));
	}
}