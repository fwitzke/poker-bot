package br.poker.bot.player.ai;
import br.poker.model.Deck;
import br.poker.model.Hand;
import br.poker.util.HandEvaluator;

public class OddsCalculator {
	private final int BOARD_MAX_SIZE = 5;
	private final int SAMPLE_SIZE = 200000;
	private HandEvaluator handEvaluator = new HandEvaluator();
	
	public Odds getProbability(Hand playerCards, Hand visibleBoardCards, int opponents) {
		Deck deck = new Deck();
		deck.extractHand(playerCards);
		deck.extractHand(visibleBoardCards);
		
		Odds odds = new Odds();
		
		for(int i=0; i < SAMPLE_SIZE; i++){
			Deck copy = deck.clone();

			Hand possibleBoardCards = new Hand();
			int boardMissingCards = BOARD_MAX_SIZE - visibleBoardCards.size();
			for(int b=0; b < boardMissingCards; b++) {
				possibleBoardCards.addCard(copy.deal());
			}
			Hand boardCards = visibleBoardCards.join(possibleBoardCards);
			
			int myRank = getRank(boardCards.join(playerCards));
			
			int[] ranks = new int[opponents+1];
			ranks[0] = myRank;
			for(int a=1; a <= opponents; a++) {
				Hand possibleOpponentCards = new Hand(copy.deal(), copy.deal());
				int opponentRank = getRank(boardCards.join(possibleOpponentCards));
				ranks[a] = opponentRank;
			}
			compareRankings(odds,ranks);
		}
		return odds;
	}

	protected int getRank(Hand hand) {
		return HandEvaluator.rankHand(handEvaluator.getBest5CardHand(hand));
	}

	protected void compareRankings(Odds odds, int[] ranks) {
		int myRank = ranks[0];
		
		int smaller=0;
		int bigger=0;
		for(int i=1; i < ranks.length; i++) {
			if(myRank < ranks[i])    	smaller++;
			else if(myRank > ranks[i])	bigger++;
		}
		
		if(smaller > 0)
			odds.LOSE++;
		else if(bigger == ranks.length-1)
			odds.WIN++;
		else
			odds.TIE++;
	}
}