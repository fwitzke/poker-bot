package br.poker.util;

import static br.poker.util.Helper.defined;
import static br.poker.util.Helper.toCents;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HelperTest {
	@Test
	public void shouldConvertFromStringToCents() {
		assertThat(toCents("0.02"), is(2));
		assertThat(toCents("0.5"), is(50));
		assertThat(toCents("1"), is(100));
		assertThat(toCents("5.60"), is(560));
	}

	@Test
	public void shouldAcceptDollarSignInNumberFormat() throws Exception {
		assertThat(toCents("$0.5"), is(50));
		assertThat(toCents("$1.08"), is(108));
		assertThat(toCents("$0.07"), is(7));
	}

	@Test
	public void shouldAcceptCommas() {
		assertThat(toCents("1,000"), is(100000));
		assertThat(toCents("1,000.23"), is(100023));
	}
	
	@Test
	public void itShouldReturnZeroWhenAllIn() throws Exception {
		assertThat(toCents("All In"), is(0));
	}
	
	@Test
	public void shouldConvertInvalidInputToMinusOne() {
		assertThat(toCents("S.0"), is(-1));
	}

	@Test
	public void shouldCheckForDefinedObject() {
		assertFalse(defined(null));
		assertFalse(defined(""));

		assertTrue(defined("any string"));
		assertTrue(defined(new Helper()));
	}
}
