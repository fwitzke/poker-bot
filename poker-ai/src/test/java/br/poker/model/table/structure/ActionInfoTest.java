package br.poker.model.table.structure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.poker.bot.input.image.ImageSegment;
import br.poker.model.action.Action;
import br.poker.ocr.TemplateAlphabet;
import static br.poker.util.DummyImage.anImage;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.core.Is.is;
@RunWith(MockitoJUnitRunner.class)
public class ActionInfoTest {
	private static final int X = 0;
	private static final int Y = 0;
	private static final int WIDTH = 5;
	private static final int HEIGHT = 10;
	private static final String FOLD = "FOLD";
	private static final String CALL = "CALL";
	private static final String CHECK = "CHECK";
	private static final String RAISE_TO = "RAISE TO";
	private ActionInfo info;
	@Mock private ImageSegment tableImage;
	@Mock private ImageSegment actionImage;
	@Mock private TemplateAlphabet alphabetMock;
	
	@Before
	public void setup() {
		info = new ActionInfo(X, Y, WIDTH, HEIGHT);
		when(tableImage.cut(info)).thenReturn(actionImage);
	}
	
	@Test
	public void shouldExtractFoldActionFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(actionImage)).thenReturn(FOLD);
		assertThat(info.getAction(tableImage, alphabetMock), is(Action.FOLD));
	}
	
	@Test
	public void shouldExtractCallActionFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(actionImage)).thenReturn(CALL);
		assertThat(info.getAction(tableImage, alphabetMock), is(Action.CALL(-1)));
	}
	
	@Test
	public void shouldExtractCheckActionFromImageWithinBox() throws Exception {
		when(alphabetMock.retrieveWord(actionImage)).thenReturn(CHECK);
		assertThat(info.getAction(tableImage, alphabetMock), is(Action.CHECK));
	}
	
	@Test
	public void shouldCutImageInTwoToExtractActionAndValue() throws Exception {
		ImageSegment halfUp = mock(ImageSegment.class);
		ImageSegment halfDown = mock(ImageSegment.class);

		when(actionImage.getImage()).thenReturn(anImage());
		final int WIDTH = actionImage.getImage().getWidth();
		final int HEIGHT = actionImage.getImage().getHeight();
		final int HALF_HEIGHT = HEIGHT/2;

		BoxInfo upBox = new BoxInfo(X, Y, WIDTH, HALF_HEIGHT);
		BoxInfo downBox = new BoxInfo(X, Y + HALF_HEIGHT, WIDTH, HALF_HEIGHT);
		when(actionImage.cut(upBox)).thenReturn(halfUp);
		when(actionImage.cut(downBox)).thenReturn(halfDown);
		when(alphabetMock.retrieveWord(actionImage)).thenReturn("");
		when(alphabetMock.retrieveWord(halfUp)).thenReturn(RAISE_TO);
		when(alphabetMock.retrieveWord(halfDown)).thenReturn("$0.10");
		
		Action action = info.getAction(tableImage, alphabetMock);
		assertThat(action, is(Action.RAISE(10)));
	}
}
