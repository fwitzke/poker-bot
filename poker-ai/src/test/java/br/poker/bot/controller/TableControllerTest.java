package br.poker.bot.controller;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.output.OutputHandler;
import br.poker.bot.player.BotPlayer;
import br.poker.model.action.Action;
import br.poker.model.table.PokerTable;
import br.poker.model.table.structure.ActionInfo;
import br.poker.util.PokerStarsTableMock;
import static br.poker.bot.input.image.ImageUtil.loadImage;
import static br.poker.model.table.PokerTable.NO_LIMIT_HOLDEM;
import static java.util.Arrays.asList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class TableControllerTest {
	private static final String TABLE_NAME = "MOCK TABLE - $0.05/$0.10 USD - No Limit Hold'em";
	private static final int X = 120;
	private static final int Y = 80;
	private static final int W = 10;
	private static final int H = 15;;
	private PokerStarsTableMock mockTable;
	private List<String> imagesNames = asList("images/2_players_flop.png",
			"images/3_players_my_cards.png");
	private TableController controller;
	@Mock private BotPlayer botPlayerMock;
	@Mock private PokerTable tableMock;
	@Mock private OutputHandler handlerMock;

	@Before
	public void setUp() throws InterruptedException {
		mockTable = new PokerStarsTableMock(TABLE_NAME, displayImages());
		mockTable.show();
		controller = new TableController(TABLE_NAME, botPlayerMock, handlerMock);
	}

	private List<BufferedImage> displayImages() {
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for (String fileName : imagesNames)
			images.add(loadImage(fileName));
		return images;
	}

	@After
	public void tearDown() {
		mockTable.destroy();
	}

	@Test
	public void shouldExtractPokerTableModelWhenCreatingController() {
		PokerTable table = controller.getPokerTable();

		assertNotNull(table);
		assertThat(controller.getTotalCaptures(), is(1));
		assertThat(table.getGameType(), is(NO_LIMIT_HOLDEM));
		assertThat(table.getSB(), is(5));
		assertThat(table.getBB(), is(10));
	}

	@Test
	public void shouldUpdateTableModelWhenTableImageChanges() throws Exception {
		controller.updateTableInfo();
		assertFalse(controller.getPokerTable().isActionRequired());

		mockTable.nextImage();
		Thread.sleep(200); // just to make sure the window was repainted
		controller.updateTableInfo();
		assertTrue(controller.getPokerTable().isActionRequired());
	}

	@Test
	public void shouldAskBotForActionWhenActionIsRequired() {
		when(tableMock.isActionRequired()).thenReturn(true);
		controller.actOnTable(tableMock);
		verify(botPlayerMock).getActionFor(tableMock);
	}

	@Test
	public void shouldNotInteractWithBotWhenActionIsNotRequired() {
		when(tableMock.isActionRequired()).thenReturn(false);
		controller.actOnTable(tableMock);
		verifyZeroInteractions(botPlayerMock);
	}

	@Test
	public void shouldActOnTableWhenBotPlayerActs() throws Exception {
		ActionInfo foldInfo = new ActionInfo(X, Y, W, H);
		stub(tableMock.getActionInfo(Action.FOLD)).toReturn(foldInfo);
		stub(tableMock.isActionRequired()).toReturn(true);
		stub(botPlayerMock.getActionFor(tableMock)).toReturn(Action.FOLD);
		
		// calls method
		controller.actOnTable(tableMock);

		// checks result
		verify(handlerMock).clickAt(foldInfo, TABLE_NAME);
	}

}