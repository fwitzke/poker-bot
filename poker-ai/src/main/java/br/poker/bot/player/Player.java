package br.poker.bot.player;

import br.poker.model.Card;
import br.poker.model.Hand;

public class Player {
    private String name;
    private Hand hand;
    private int stack;

    public Player() {
        hand = new Hand();
    }

    public Player(String name) {
		this();
		setName(name);
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int size) {
        this.stack = size;
    }

    public void dealCard(Card card) {
        hand.addCard(card);
    }

    public void clearHand() {
        hand.makeEmpty();
    }
    
    public void take(int amount) {
    	stack -= amount;
    }
    
    public void put(int amount) {
    	stack += amount;
    }

    @Override
    public String toString() {
    	String playerDescription = name + " | " + stack;
    	if(hand.size() == 2)
    		playerDescription += " | Cards: " + hand;
    	else if(hand.size() == 1)
    		playerDescription += " | on hand ";
		return playerDescription;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player p2 = (Player) obj;
            return name.equals(p2.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
