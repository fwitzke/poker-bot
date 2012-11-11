package br.poker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	public static final int NUM_CARDS = 52;
	private List<Card> deckCards = new ArrayList<Card>(NUM_CARDS);
	private static Random random;
	
	static {
		random = new Random();
	}

	public Deck() {
		for (int i = 0; i < NUM_CARDS; i++)
			deckCards.add(new Card(i));
	}

	public Deck(final List<Card> cards) {
		deckCards.addAll(cards);
	}

	public int size() {
		return deckCards.size();
	}

	public void extractHand(Hand hand) {
		for(int i=0; i < hand.size(); i++) { 
			deckCards.remove(hand.getCard(i+1));
		}
	}

	public Card get(int position) {
		return deckCards.get(position);
	}
	
	public List<Card> getCards() {
		return deckCards;
	}

	public Card deal() {
		int chosen = random.nextInt(size());
		return deckCards.remove(chosen);
	}

	public Deck without(Card ...cards) {
		List<Card> newDeckCards = new ArrayList<Card>();
		newDeckCards.addAll(deckCards);
		for (Card card : cards)
			newDeckCards.remove(card);
		return new Deck(newDeckCards);
	}
	
	@Override
	public Deck clone() {
		return new Deck(getCards());
	}
}