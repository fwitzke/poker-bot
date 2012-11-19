package br.poker.model.table;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import br.poker.bot.input.image.ImageSegment;
import br.poker.bot.player.Player;
import br.poker.model.Card;
import br.poker.model.Deck;
import br.poker.model.action.Action;
import br.poker.model.action.Call;
import br.poker.model.action.Check;
import br.poker.model.action.Fold;
import br.poker.model.action.Raise;
import br.poker.model.handhistory.HandHistory;
import br.poker.model.table.structure.ActionInfo;
import br.poker.model.table.structure.CardInfo;
import br.poker.model.table.structure.PokerPlayerInfo;
import br.poker.model.table.structure.PokerTableInfo;
import br.poker.ocr.TemplateAlphabet;
import br.poker.util.Logger;
import static br.poker.util.Helper.defined;
import static br.poker.util.Helper.toCents;

public abstract class PokerTable {
	private String name;
    private int seatsNumber;
    private int SB; //small blind
    private int BB; //big blind
    private int totalPot;
    private Player[] players;
    private Deck deck;
    private List<Card> board;
    private GameType gameType;
    private String currency;
    private TableState tableState;
    private boolean actionRequired;
    private List<Action> availableActions;
    protected PokerTableInfo tableInfo;
    protected TemplateAlphabet alphabet, alphabetDeck, alphabetActions;
    
    private TableStateListener stateListener;
	private HandHistory handHistory;
	private static final int MY_POSITION = 4; //TODO REMOVE HARDCODING
    private static final String MY_NAME = "pigroxalot"; //TODO REMOVE HARDCODING
    
    public abstract boolean isPlayerOnHand(int playerPosition, BufferedImage tableImage);
    public abstract boolean isVisible(BufferedImage tableImage);

    //TODO include the concept of HAND HISTORY add a current and hand history collection to store actions
    public PokerTable(int seatsNumber) {
    	this.seatsNumber = seatsNumber;
        setTotalPot(0);
        availableActions = new  ArrayList<Action>();
        players = new Player[seatsNumber];
        deck = new Deck();
        board = new ArrayList<Card>();
        handHistory = new HandHistory();
        stateListener = new TableStateListener();
    }

    public void setTableName(String tableName) {
        this.name = tableName;
    }

    public String getTableName() {
        return name;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public int getSB() {
        return SB;
    }

    public void setSB(int sB) {
        SB = sB;
    }

    public int getBB() {
        return BB;
    }

    public void setBB(int bB) {
        BB = bB;
    }

    public void setTotalPot(int pot) {
        this.totalPot = pot;
    }

    public int getTotalPot() {
        return totalPot;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isPositionFree(int pos) {
        if (pos < 0 || pos >= seatsNumber) {
            return false;
        }
        return players[pos] == null;
    }

    public void seat(Player player, int pos) {
        players[pos] = player;
    }

    public void startNewHand() {
        deck = new Deck();
        board = new ArrayList<Card>();

        //clears players hands
        for (Player p : players) {
            if (p != null) {
                p.clearHand();
            }
        }
    }

    public int getPlayersOnCurrentHand() {
        int total=0;
        for(Player p:players)
            if(p != null && p.getHand().size() != 0) //player with dealt cards
                total++;
        return total;
    }

    public int getSittingPlayers() {
        int total=0;
        for(Player p:players)
            if(p != null) total++;
        return total;
    }

    public void dealPlayerCards() {
        for (Player p : players) {
            if (p != null) {
                p.dealCard(deck.deal());
                p.dealCard(deck.deal());
            }
        }
    }

    public void dealFlopCards(Card c1, Card c2, Card c3) {
        board.add(c1);
        board.add(c2);
        board.add(c3);
    }

    public void dealTurnCard(Card c1) {
        board.add(c1);
    }

    public void dealRiverCard(Card c1) {
        board.add(c1);
    }

    public List<Card> getBoard() {
        return board;
    }

    public void postBigBlind(Player player) {
        //Action ac = Action.POST_BB;

        player.take(getBB());
        totalPot += getBB();
    }

    public void postSmallBlind(Player player) {
        //Action ac = Action.POST_SB;

        player.take(getSB());
        totalPot += getSB();
    }

    public TableState getTableState() {
        return tableState;
    }

    public void setTableState(TableState tableState) {
        this.tableState = tableState;
    }

    private void action(Player player, Action action, int value) {
        player.take(value);
        totalPot += value;

        if(action == Action.FOLD) {
            player.clearHand();
        }
    }

    public void action(Player player, Action action) {
        action(player, action, action.getValue());
    }

    public TemplateAlphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(TemplateAlphabet alphabet) {
        this.alphabet = alphabet;
    }

    public TemplateAlphabet getAlphabetActions() {
        return alphabetActions;
    }

    public void setAlphabetActions(TemplateAlphabet alphabetActions) {
        this.alphabetActions = alphabetActions;
    }

    public TemplateAlphabet getAlphabetDeck() {
        return alphabetDeck;
    }

    public void setAlphabetDeck(TemplateAlphabet alphabetDeck) {
        this.alphabetDeck = alphabetDeck;
    }

    public PokerTableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(PokerTableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public Player getPlayerAt(int position) {
        return players[position];
    }

    public void setActionRequired(boolean act) {
        actionRequired = act;
    }
    
    public boolean isActionRequired() {
        return actionRequired;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }

    /**
     * Updates table info based on table image
     * @param Table Image
     * @return true if updated table successfully
     */
	public boolean update(BufferedImage tableImage) { //TODO change this to Use ImageSegment
		if(!isVisible(tableImage) ) {
            Logger.error("Table is not visible");
            return false;
        }
		
		stateListener.before(this);
        updatePlayersInfo(tableImage);
        updateTableInfo(tableImage);
        updateTableState();
        updateMyInfo(tableImage);
        updateAvailableActionsInfo(tableImage);
        stateListener.after(this);
        return true;
	}
	
	private void updateTableState() {
		switch(getBoard().size()) {
            case 0:
                setTableState(TableState.PRE_FLOP);
                break;
            case 3:
                setTableState(TableState.FLOP);
                break;
            case 4:
                setTableState(TableState.TURN);
                break;
            case 5:
                setTableState(TableState.RIVER);
                break;
            default:
                Logger.debug("Invalid number of board cards: " + getBoard().size());
        }
	}

    private void updateTableInfo(BufferedImage tableImage) {
    	board = new ArrayList<Card>();
    	
    	//Get Board Cards Info
        for (int cardIndex = 0; cardIndex < 5; cardIndex++) {
            CardInfo cardInfo = tableInfo.getCardInfo(cardIndex);
            Card card = cardInfo.getCard(tableImage, alphabetDeck);
            if (defined(card)) {
                getBoard().add(card);
            }
        }

        int potValue = tableInfo.getPotInfo().getPotValue(tableImage, alphabet);
        setTotalPot(potValue);
    }
    
    private void updatePlayersInfo(BufferedImage tableImage) {
        List<PokerPlayerInfo> playersInfo = getTableInfo().getPokerPlayerInfoList();

        for (PokerPlayerInfo info : playersInfo) {
            int playerPosition = info.getIndex();

            Logger.debug("Player" + playerPosition + ": " + info.toString());

            //Name and Stack info
            String playerName = info.getNameInfo().getText(tableImage, alphabet);
            int playerStack = info.getStackInfo().getNumber(tableImage, alphabet);

            if (defined(playerName)) {
                Logger.debug("Player Name: " + playerName + " | Stack: " + playerStack);

                Player player = new Player();
                player.setName(playerName);
                player.setStack(playerStack);

                if (isPlayerOnHand(playerPosition, tableImage)) {
                    player.dealCard(new Card()); //Just to mark this player on current hand
                }

                this.seat(player, playerPosition);
            } else {
            	players[playerPosition] = null;
                Logger.debug("Seat empty");
            }
        }
    }

    private void updateMyInfo(BufferedImage tableImage) {
    	if(!isPlayerOnTable(MY_NAME))
    		return;
    	
    	PokerPlayerInfo myInfo = tableInfo.getPokerPlayerInfo(MY_POSITION);
        CardInfo cardInfo_0 = myInfo.getCardInfo_0();
        CardInfo cardInfo_1 = myInfo.getCardInfo_1();
        Card card_0 = cardInfo_0.getCard(tableImage, alphabetDeck);
        Card card_1 = cardInfo_1.getCard(tableImage, alphabetDeck);
        
        if(defined(card_0) && defined(card_1)) {
            getPlayerAt(MY_POSITION).dealCard(card_0);
            getPlayerAt(MY_POSITION).dealCard(card_1);
        }
    }
    
	private boolean isPlayerOnTable(String playerName) {
		Player me = players[MY_POSITION];
        return (defined(me) && playerName.equals(me.getName()));
	}

	//TODO This method is taking too long to run when there are no actions visible
    private void updateAvailableActionsInfo(BufferedImage tableImage) {
    	if(!isPlayerOnTable(MY_NAME))
    		return;
    	
    	Action action1, action2, action3;

    	ActionInfo action1Box = tableInfo.getActionInfo(0);
		action1 = action1Box.getAction(new ImageSegment(tableImage), alphabetActions);
        
        ActionInfo action2Box = tableInfo.getActionInfo(1);
        action2 = action2Box.getAction(new ImageSegment(tableImage), alphabetActions);

        ActionInfo action3box = tableInfo.getActionInfo(2);
        action3 = action3box.getAction(new ImageSegment(tableImage), alphabetActions);

        availableActions = new ArrayList<Action>();
        if(defined(action1)) {
            setActionRequired(true);
            availableActions.add(action1);
            availableActions.add(action2);
            availableActions.add(action3);
        } else {
            setActionRequired(false);
        }
    }
    
    /**
     * Extracts information from Table Window Title
     * @param Table Window Title
     * @return void
     */
	public PokerTable update(String windowName) {
		if(defined(windowName)) {
            String titleInfoArray[] = windowName.split("-");
            Logger.debug("TableName: " + windowName + ". String split in " + titleInfoArray.length + " pieces...");

            //Table Name - titleInfoArray[0]
            String tableName = titleInfoArray[0].trim();
            setTableName(tableName);

            //Blinds and Currency Information - titleInfoArray[1]
            String blindsInfo = titleInfoArray[1].trim();
            Logger.debug("BlindsInfo: " + blindsInfo);
            int slashIndex = blindsInfo.indexOf("/");
            int spaceIndex = blindsInfo.indexOf(" ");

            try {
                Logger.debug("Parsing small blind...");
                String smallBlind = blindsInfo.substring(0, slashIndex);
                String bigBlind = blindsInfo.substring(slashIndex+1,spaceIndex);

                setSB(toCents(smallBlind));
                setBB(toCents(bigBlind));
            } catch(Exception e) {
                Logger.debug("Unable to parse blinds: " + e.toString());
            }
            
            setCurrency(blindsInfo.substring(spaceIndex+1));

            //Game Type - titleInfoArray[2]
            String gameType = titleInfoArray[2].trim();
            Logger.debug("GameType: " + gameType);
            if("No Limit Hold'em".equals(gameType))
                setGameType(GameType.NO_LIMIT_HOLDEM);
            else if("Limit Hold'em".equals(gameType))
                setGameType(GameType.LIMIT_HOLDEM);
        }
        return this;
	}
	
	public ActionInfo getActionInfo(Action action) {
		ActionInfo actionInfo = tableInfo.getActionInfo(0);
		if(action instanceof Fold)
			actionInfo = tableInfo.getActionInfo(0);
		else if(action instanceof Check || action instanceof Call)
			actionInfo = (availableActions.size()==2)?tableInfo.getActionInfo(2):tableInfo.getActionInfo(1);
		else if(action instanceof Raise)
			actionInfo = tableInfo.getActionInfo(2);
		else actionInfo = tableInfo.getActionInfo(0);
		return actionInfo;
	}
	
	public HandHistory getHandHistory() {
		return handHistory;
	}
	
	public Player getMyself() {
		return getPlayerAt(MY_POSITION);
	}
	
	public String toString() {
		String res= "";
		
		res += "Table: " + name + ". Blinds: " + SB + "/" + BB + "\n";
		for(int i=0; i < players.length; i++) {
			Player player = players[i];
			if(player!=null)
				res += "Seat " + i + ": " + player + "\n";
		}
		res += "State: " + tableState;
		res += "\nBoard: " + (board.isEmpty() ? " empty" : "");
		for(Card c : board) {
			res += c.toString() + " ";
		}
		res += "\nPot: " + totalPot;
		
		res+= "\nActionRequired? " + isActionRequired() + "\n";
		if(isActionRequired()) {
			for(Action ac:availableActions) {
				if(ac != null)
					res += " " + ac.getClass().getName() + " (" + ac.getValue() + ") ";
			}
		}
		return res;
	}
}
