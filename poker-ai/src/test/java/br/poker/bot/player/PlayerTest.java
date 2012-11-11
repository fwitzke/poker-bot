package br.poker.bot.player;

import org.junit.Test;

import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.Hand;

import static org.hamcrest.core.Is.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class PlayerTest {
	private static final String PLAYER_NAME = "pigroxalot";
	private static final Card FIVE_HEARTS = new Card("5h");
	private static final Card SIX_HEARTS = new Card("6h");

	@Test
	public void shouldCreatePlayer() {
		Player player = new Player();
		player.setName(PLAYER_NAME);
		player.setStack(100);

		assertThat(player.getName(), is(PLAYER_NAME));
		assertThat(player.getStack(), is(100));
		assertThat(player.getHand().size(), is(0));
	}

	@Test
	public void shouldDealPlayerCards() {
		Player player = new Player();
		player.dealCard(FIVE_HEARTS);
		player.dealCard(SIX_HEARTS);

		Hand hand = player.getHand();
		assertEquals(2, hand.size());
		assertEquals(FIVE_HEARTS, hand.getCard(1));
		assertEquals(SIX_HEARTS, hand.getCard(2));
	}
}
