package br.poker.bot.output;

import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.poker.model.table.structure.BoxInfo;
import br.poker.util.PokerStarsTableMock;
import static java.awt.MouseInfo.getPointerInfo;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class OutputHandlerTest {
	private static final String WINDOW_TITLE = "WINDOW TITLE";
	private static final Point START_POSITION = new Point(30, 30);
	private static final Point TITLE_BAR_SIZE = new Point(4, 23);
	private static final Point EXPECTED_POSITION = new Point(START_POSITION.x+TITLE_BAR_SIZE.x, START_POSITION.y + TITLE_BAR_SIZE.y);
	private static final int X = 50;
	private static final int Y = 50;
	private PokerStarsTableMock mockTable;
	private OutputHandler handler;

	@Before
	public void setup() {
		mockTable = new PokerStarsTableMock(WINDOW_TITLE);
		mockTable.show(START_POSITION);
		handler = new OutputHandler();
	}

	@After
	public void after() {
		mockTable.destroy();
	}

	@Test
	public void shouldMoveMouseToSpecificPosition() throws InterruptedException {
		handler.moveMouseTo(300, 100);
		assertThat(getPointerInfo().getLocation().x, is(300));
		assertThat(getPointerInfo().getLocation().y, is(100));

		handler.moveMouseTo(800, 100);
		assertThat(getPointerInfo().getLocation().x, is(800));
		assertThat(getPointerInfo().getLocation().y, is(100));

		handler.moveMouseTo(800, 200);
		assertThat(getPointerInfo().getLocation().x, is(800));
		assertThat(getPointerInfo().getLocation().y, is(200));

		handler.moveMouseTo(300, 200);
		assertThat(getPointerInfo().getLocation().x, is(300));
		assertThat(getPointerInfo().getLocation().y, is(200));
	}

	@Test
	public void shouldDetectWindowAbsolutePosition() throws Exception {
		assertThat(handler.getWindowPosition(WINDOW_TITLE), is(EXPECTED_POSITION));
	}
	
	@Test
	public void shouldClickAtPokerTableActionButton() {
		handler.clickAt(X, Y, WINDOW_TITLE);
		handler.clickAt(X, Y, WINDOW_TITLE);
		assertThat(mockTable.wasClickedAt(X, Y), is(true));
	}
	
	@Test
	public void shouldClickInTheMiddleOfABoxInfo() throws Exception {
		handler.clickAt(new BoxInfo(X, Y, 10, 10), WINDOW_TITLE);
		handler.clickAt(new BoxInfo(X, Y, 10, 10), WINDOW_TITLE);
		assertThat(mockTable.wasClickedAt(X+5, Y+5), is(true));
	}
}
