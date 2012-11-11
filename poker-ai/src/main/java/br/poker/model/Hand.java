package br.poker.model;

/***************************************************************************
Copyright (c) 2000:
      University of Alberta,
      Deptartment of Computing Science
      Computer Poker Research Group

      See "Liscence.txt"
***************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
* Stores a Hand of Cards (up to a maximum of 7)
*
* @author  Aaron Davidson
*/

public class Hand {
   public final static int MAX_CARDS = 7;
   
   private int[] cards;
   
   public Hand() {
      cards = new int[MAX_CARDS + 1];
      cards[0] = 0;
   }
   
   /**
    * @param cs A string representing a Hand of cards
    */
   public Hand(String cs) {
      cards = new int[MAX_CARDS + 1];
      cards[0] = 0;
      StringTokenizer t = new StringTokenizer(cs," -");
      while(t.hasMoreTokens()) {
         String s = t.nextToken();
         if (s.length()==2) {
            Card c = new Card(s.charAt(0),s.charAt(1));
            if (c.getIndex() != Card.BAD_CARD)
               addCard(c);
         }
      }     
   }
   
   /**
    * Duplicate an existing hand.
    * @param h the hand to clone.
    */
   public Hand(Hand h) {
      cards = new int[MAX_CARDS + 1];
      cards[0] = h.size();
      for (int i=1;i<=cards[0];i++)
         cards[i] = h.cards[i];
   }
   
   public Hand(String ... cards) {
	   this();
	   for (String card: cards) {
		   addCard(new Card(card));
	   }
   }

   public Hand(Card ... cards) {
	   this();
	   for(Card c : cards){
		   addCard(c);
	   }
   }

   /**
    * Get the size of the hand.
    * @return the number of cards in the hand
    */
   public int size() {
      return cards[0];
   }
   
   /**
    * Remove the last card in the hand.
    */
   public void removeCard() {
      if (cards[0] > 0) {
         cards[0]--;
      }
   }
   
   /**
    * Remove the all cards from the hand.
    */
   public void makeEmpty() {
      cards[0] = 0;
   }
   
   /**
    * Add a card to the hand. (if there is room)
    * @param c the card to add
    * @return true if the card was added, false otherwise
    */
   public boolean addCard(Card c) {
      if (c == null) return false;
      if (cards[0] == MAX_CARDS) return false; 
      cards[0]++;
      cards[cards[0]] = c.getIndex();
      return true;
   }
   
   /**
    * Add a card to the hand. (if there is room)
    * @param i the index value of the card to add
    * @return true if the card was added, false otherwise
    */
   public boolean addCard(int i) {
      if (cards[0] == MAX_CARDS) return false; 
      cards[0]++;
      cards[cards[0]] = i;
      return true;
   }
   
   /**
    * Get the a specified card in the hand
    * @param pos the position (1..n) of the card in the hand
    * @return the card at position pos
    */
   public Card getCard(int pos) {
      if (pos < 1 || pos > cards[0]) return null;
      return new Card(cards[pos]);
   }
   
   
   /**
    * Add a card to the hand. (if there is room)
    * @param c the card to add
    */
   public void setCard(int pos, Card c) {
      if (cards[0] < pos) return; 
      cards[pos] = c.getIndex();
   }
   
   /** 
    * Obtain the array of card indexes for this hand.
    * First element contains the size of the hand.
    * @return array of card indexs (size = MAX_CARDS+1)
    */
   public int[] getCardArray() {
      return cards;
   }  
   
   
   /** 
    * Bubble Sort the hand to have cards in descending order, but card index.
    * Used for database indexing.
    */
   public void sort() {
      boolean flag = true;
      while (flag) {
         flag = false;
         for (int i=1;i<cards[0];i++) {
            if (cards[i] < cards[i+1]) {
               flag = true;
               int t = cards[i];
               cards[i] = cards[i+1];
               cards[i+1] = t;
            }
         }
      }
   }
   
   public List<Card> getCards() {
	  List<Card> cards = new ArrayList<Card>();
	   for(int i=0; i < size(); i++) {
		   cards.add(getCard(i));
	   }
	   return cards;
   }
   
   /**
    * Get a string representation of this Hand.
    */
   public String toString() {
      String s = new String();
      for (int i=1;i<=cards[0];i++)
         s += getCard(i).toString();
      return s;
   }


   /**
    * Get the specified card id 
    * @param pos the position (1..n) of the card in the hand
    * @return the card at position pos
    */
   public int getCardIndex(int pos) {
      return cards[pos];
   }

	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Hand join(Hand... cards) {
		Hand allCards = new Hand();
		for(int i=1; i <= this.size(); i++)
			allCards.addCard(this.getCard(i));
		
		for(Hand h : cards)
			for(int i=1; i <= h.size(); i++)
				allCards.addCard(h.getCard(i));
		return allCards;
	}
	
	@Override
	public boolean equals(Object obj) {
		Hand other = (Hand)obj;
		if(other.getCardArray().length == cards.length) {
			for(int i=0; i < other.getCardArray().length; i++) {
				Card card = other.getCard(i);
				if(card.equals(cards[i]))
					continue;
				else
					return false;
			}
			return true;
		} else 
			return false;
	}
}