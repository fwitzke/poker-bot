package br.poker.model.table;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PokerTableSnapshot {
	private BettingStructure bettingStructure;
	private List<PlayerInfo> playersInfo;

	public PokerTableSnapshot(BettingStructure bettingStructure, List<PlayerInfo> playersInfo) {
		this.bettingStructure = bettingStructure;
		this.playersInfo = playersInfo;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
