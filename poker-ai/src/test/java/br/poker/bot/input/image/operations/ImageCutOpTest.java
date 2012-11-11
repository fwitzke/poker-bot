package br.poker.bot.input.image.operations;

import static br.poker.bot.input.image.ImageUtil.loadImage;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import br.poker.model.table.structure.BoxInfo;

public class ImageCutOpTest {
	private static final int Y = 46;
	private static final int X = 129;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 40;
	private BufferedImage toCut;
	private ImageCutOp cut;

	@Before
	public void setUp() {
		toCut = loadImage("images/2_players_flop.png");
		cut = new ImageCutOp();
	}

	@Test
	public void shouldCutTheImage() {
		BufferedImage result = cut.process(toCut, X, Y, WIDTH, HEIGHT);

		assertThat(result.getWidth(), is(WIDTH));
		assertThat(result.getHeight(), is(HEIGHT));
	}

	@Test
	public void shouldCutTheImageBasedOnBoxInfo() {
		BoxInfo info = new BoxInfo(X, Y, WIDTH, HEIGHT, new ImageCutOp());
		BufferedImage result = cut.process(toCut, info);

		assertThat(result.getWidth(), is(WIDTH));
		assertThat(result.getHeight(), is(HEIGHT));
	}
}
