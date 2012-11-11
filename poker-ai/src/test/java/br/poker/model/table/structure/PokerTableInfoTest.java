package br.poker.model.table.structure;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class PokerTableInfoTest {
	private PokerTableInfo info;

	@Before
	public void setup() throws Exception {
		info = new PokerTableInfo("tables/stars/poker_stars_9_players.xml");
	}

	@Test
	public void shouldExtractTableSeatsNumber() throws Exception {
		assertThat(info.getSeats(), is(9));
	}

	@Test
	public void shouldExtractCardInfo() throws Exception {
		CardInfo cardInfo = info.getCardInfo(0);
		assertThat(cardInfo.getIndex(), is(0));
		assertThat(cardInfo.getPositionX(), is(269));
		assertThat(cardInfo.getPositionY(), is(185));
		assertThat(cardInfo.getWidth(), is(14));
		assertThat(cardInfo.getHeight(), is(15));
	}

	@Test
	public void shouldExtractPotInfo() throws Exception {
		BoxInfo potInfo = info.getPotInfo();
		assertThat(potInfo.getPositionX(), is(355));
		assertThat(potInfo.getPositionY(), is(45));
		assertThat(potInfo.getWidth(), is(90));
		assertThat(potInfo.getHeight(), is(15));
	}

	@Test
	public void shouldExtractPokerPlayerInfo() {
		PokerPlayerInfo playerInfo = info.getPokerPlayerInfo(0);
		assertThat(playerInfo.getIndex(), is(0));
		assertThat(playerInfo.getNameInfo().getPositionX(), is(564));
		assertThat(playerInfo.getNameInfo().getPositionY(), is(49));
		assertThat(playerInfo.getNameInfo().getWidth(), is(93));
		assertThat(playerInfo.getNameInfo().getHeight(), is(16));

		assertThat(playerInfo.getStackInfo().getPositionX(), is(564));
		assertThat(playerInfo.getStackInfo().getPositionY(), is(68));
		assertThat(playerInfo.getStackInfo().getWidth(), is(93));
		assertThat(playerInfo.getStackInfo().getHeight(), is(16));

		assertThat(playerInfo.getVisibleCardInfo().getPositionX(), is(515));
		assertThat(playerInfo.getVisibleCardInfo().getPositionY(), is(113));

		assertThat(playerInfo.getCardInfo_0().getPositionX(), is(999));
		assertThat(playerInfo.getCardInfo_0().getPositionY(), is(999));
		assertThat(playerInfo.getCardInfo_0().getWidth(), is(14));
		assertThat(playerInfo.getCardInfo_0().getHeight(), is(15));

		assertThat(playerInfo.getCardInfo_1().getPositionX(), is(999));
		assertThat(playerInfo.getCardInfo_1().getPositionY(), is(999));
		assertThat(playerInfo.getCardInfo_1().getWidth(), is(14));
		assertThat(playerInfo.getCardInfo_1().getHeight(), is(15));
	}
	
	@Test
	public void shouldExtractActionInfo() throws Exception {
		ActionInfo actionInfo = info.getActionInfo(0);
		
		assertNotNull(actionInfo);
		assertThat(actionInfo.getPositionX(), is(385));
		assertThat(actionInfo.getPositionY(), is(490));
		assertThat(actionInfo.getWidth(), is(120));
		assertThat(actionInfo.getHeight(), is(48));
	}
	
	@Test
	public void shouldExtractMultipleActionInfos() throws Exception {
		assertNotNull(info.getActionInfo(0));
		assertNotNull(info.getActionInfo(1));
		assertNotNull(info.getActionInfo(2));
	}
}
