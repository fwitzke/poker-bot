package br.poker.model.table;

import static br.poker.bot.input.image.ImageUtil.loadImage;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.Hand;
import br.poker.model.action.Action;
import br.poker.model.table.pokerstars.PokerStars9PlayersTable;

public class PokerTable_updateTest {
	private static final int SEATS_NUMBER = 9;
	private static final Card FIVE_DIAMONDS = new Card("5d");
	private static final Card TWO_CLOUDS = new Card("2c");
	private static final Card THREE_CLOUDS = new Card("3c");
	private static final Card NINE_CLOUDS = new Card("9c");
	private static final Card QUEEN_CLOUDS = new Card("Qc");
	private static final Card FOUR_HEARTS = new Card("4h");
	private static final Card SEVEN_HEARTS = new Card("7h");
	private static final Card QUEEN_HEARTS = new Card("Qh");

	private static BufferedImage img_2_players_flop;
    private static BufferedImage img_3_players_river;
    private static BufferedImage img_3_players_my_cards;
    private static BufferedImage img_invalid_table;
	private PokerTable table;
    
	@BeforeClass
	public static void loadImages() {
		img_2_players_flop = loadImage("images/2_players_flop.png");
		img_3_players_river = loadImage("images/3_players_river.png");
		img_3_players_my_cards = loadImage("images/3_players_my_cards.png");
		img_invalid_table = loadImage("images/invalid_table_image.png");
	}

	@Before
	public void createTable() throws Exception {
		table = new PokerStars9PlayersTable();
	}
	
	@AfterClass
	public static void release() {
		img_2_players_flop = null;
		img_3_players_river = null;
		img_3_players_my_cards = null;
		img_invalid_table = null;
	}
    
    @Test
    public void shouldUpdateTableInfo2PlayersOnFlop() {
        table.update(img_2_players_flop);
        
        //Table Info - Players Sit
        assertNotNull(table);
        assertThat(table.getSeatsNumber(), is(SEATS_NUMBER));
        assertThat(table.getSittingPlayers(), is(8));

        //Player name and stack info
        Player rakeShark = table.getPlayerAt(0);
		assertThat(rakeShark.getName(), is("rake shark"));
        assertThat(rakeShark.getStack(), is(518));
        Player nikolay = table.getPlayerAt(1);
		assertThat(nikolay.getName(), is("d.nikolay33"));
        assertThat(nikolay.getStack(), is(416));
        Player wvaseckin = table.getPlayerAt(2);
		assertThat(wvaseckin.getName(), is("wvaseckin"));
        assertThat(wvaseckin.getStack(), is(381));
        Player imperator464 = table.getPlayerAt(7);
		assertThat(imperator464.getName(), is("imperator464"));
        assertThat(imperator464.getStack(), is(187));

        //Table Info - Cards
        List<Card> board = table.getBoard();
		assertThat(board.size(), is(3));
        assertThat(board.get(0), is(QUEEN_CLOUDS));
        assertThat(board.get(1), is(SEVEN_HEARTS));
        assertThat(board.get(2), is(TWO_CLOUDS));

        //Table State
        assertThat(table.getTableState(), is(TableState.FLOP));

        //Table Info - Current Hand
        assertThat(table.getTotalPot(), is(86));
        assertThat(table.getPlayersOnCurrentHand(), is(2));
    }

    @Test
    public void shouldUpdateTableInfo3PlayersOnRiver() {
        table.update(img_3_players_river);

        //Table Info - Players Sit
        assertNotNull(table);
        assertThat(table.getSeatsNumber(), is(SEATS_NUMBER));
        assertThat(table.getSittingPlayers(), is(9));

        //Player name and stack info
        Player playerZero = table.getPlayerAt(0);
		assertThat(playerZero.getName(), is("11winner84"));
        assertThat(playerZero.getStack(), is(1142));
        Player witz = table.getPlayerAt(1);
		assertThat(witz.getName(), is("Witz96"));
        assertThat(witz.getStack(), is(493));
        Player girlstar67 = table.getPlayerAt(2);
		assertThat(girlstar67.getName(), is("girlstars67"));
        assertThat(girlstar67.getStack(), is(240));
        Player soberb10 = table.getPlayerAt(8);
		assertThat(soberb10.getName(), is("SoBeRb1o"));
        assertThat(soberb10.getStack(), is(175));

        //Table Info - Cards
        List<Card> board = table.getBoard();
        assertThat(board.size(), is(5));
        assertThat(board.get(0), is(NINE_CLOUDS));
        assertThat(board.get(1), is(QUEEN_HEARTS));
        assertThat(board.get(2), is(FOUR_HEARTS));
        assertThat(board.get(3), is(THREE_CLOUDS));
        assertThat(board.get(4), is(FIVE_DIAMONDS));

        //Table State
        assertThat(table.getTableState(), is(TableState.RIVER));

        //Table Info - Current Hand
        assertThat(table.getTotalPot(), is(20));
        assertThat(table.getPlayersOnCurrentHand(), is(3));
    }

    @Test
    public void shouldUpdateMyInfoFromImage() {
        table.update(img_3_players_my_cards);

        Player pigroxalot = table.getPlayerAt(4);
        Hand hand = pigroxalot.getHand();
        assertThat(hand.size(), is(2));

        Card firstCard = hand.getCard(1);
        Card secondCard = hand.getCard(2);
        assertThat(firstCard, is(new Card("9d")));
        assertThat(secondCard, is(new Card("4s")));
        assertThat(table.getTableState(), is(TableState.PRE_FLOP));
    }

    @Test
    public void shouldKnowWhenActionIsRequired() {
        table.update(img_3_players_my_cards);

        assertTrue(table.isActionRequired());

        List<Action> availableActions = table.getAvailableActions();
        assertThat(availableActions.size(), is(3));
        assertThat(availableActions.get(0), is(Action.FOLD));
        assertThat(availableActions.get(1), is(Action.CALL(2)));
        assertThat(availableActions.get(2), is(Action.RAISE(4)));
    }
    
    @Test
	public void itShouldDetectWhenAPlayerLeavesTheTable() throws Exception {
    	table.update(img_2_players_flop);
    	assertThat(table.isPositionFree(8), is(false));
    	table.update(img_3_players_my_cards);
    	assertThat(table.isPositionFree(8), is(true));
	}
    
    @Test
    public void shouldNotBreakWhenTableImageIsInvalid() {
        table.update(img_invalid_table);
        assertThat(table.getSB(), is(0));
        assertThat(table.getBB(), is(0));
    }
    
    @Test
	public void shouldUpdateBoardInfoWhenTableImageChanges() throws Exception {
		table.update(img_2_players_flop);
    	assertThat(table.getBoard().get(0), is(QUEEN_CLOUDS));
    	assertThat(table.getBoard().size(), is(3));
    	
    	table.update(img_3_players_my_cards);
    	assertTrue(table.getBoard().isEmpty());
	}
    
    @Test
    public void shouldExtractTableInfoFromTableName_NoLimit_1_2() {
        String name = "Euaimon II - $0.01/$0.02 USD - No Limit Hold'em";
        table.update(name);

        assertThat(table.getTableName(), is("Euaimon II"));
        assertThat(table.getSB(), is(1));
        assertThat(table.getBB(), is(2));
        assertThat(table.getCurrency(), is("USD"));
        assertThat(table.getGameType(), is(GameType.NO_LIMIT_HOLDEM));
    }

    @Test
    public void shouldExtractTableInfoFromTableName_NoLimit_5_10() {
        String name = "West VII - $0.05/$0.10 USD - No Limit Hold'em";
        table.update(name);

        assertThat(table.getTableName(), is("West VII"));
        assertThat(table.getSB(), is(5));
        assertThat(table.getBB(), is(10));
        assertThat(table.getCurrency(), is("USD"));
        assertThat(table.getGameType(), is(GameType.NO_LIMIT_HOLDEM));
    }

    @Test
    public void shouldExtractTableInfoFromTableName_Limit_PlayMoney() {
        String name = "Propus VIII - 200/400 Play Money - Limit Hold'em";
        table.update(name);

        assertThat(table.getTableName(), is("Propus VIII"));
        assertThat(table.getSB(), is(20000));
        assertThat(table.getBB(), is(40000));
        assertThat(table.getCurrency(), is("Play Money"));
        assertThat(table.getGameType(), is(GameType.LIMIT_HOLDEM));
    }
}