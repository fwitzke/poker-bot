package br.poker.model.table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PlayerInfo {
	Integer stack;
	Boolean isOnHand;
	int position;
	
	public PlayerInfo() {
		
	}
	
	public PlayerInfo(int position, int stack) {
		this.position = position;
		this.stack = stack;
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