package br.poker.model.table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BettingStructure {
	private int smallBlind;
	private int bigBlind;
	private int ante;

	public BettingStructure(int smallBlind, int bigBlind) {
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public int getAnte() {
		return ante;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}