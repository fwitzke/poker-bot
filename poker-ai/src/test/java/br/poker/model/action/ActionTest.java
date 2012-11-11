package br.poker.model.action;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

public class ActionTest {
	@Test
	public void shouldBeAbleToCreateActions() {
		Action check = new Check();
		Action raise = new Raise(30);

		assertThat(raise.getValue(), is(30));
		assertThat(check.getValue(), is(0));
	}

}