package br.poker.model.table.structure;

import static br.poker.util.XmlUtil.getTagValue;
import static java.lang.Integer.parseInt;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.poker.bot.input.image.operations.ImageCutOp;
import br.poker.util.Logger;

public class PokerTableInfo {
    private Document xmlDoc;
    private int seats;
    private BoxInfo potInfo;
    private List<CardInfo> cardInfoList;
    private List<PokerPlayerInfo> pokerPlayerInfoList;
	private ActionInfo[] actionInfos;

    public PokerTableInfo(String configFileName) throws Exception {
        cardInfoList = new ArrayList<CardInfo>(5);
        pokerPlayerInfoList = new ArrayList<PokerPlayerInfo>();
        actionInfos = new ActionInfo[3];
        
        URL resource = getClass().getClassLoader().getResource(configFileName);
        File stream = new File(resource.toURI());
        
        try {
            Logger.log("Parsing table model: " + configFileName);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
            xmlDoc = dBuilder.parse(stream);

            Logger.log("Root Element: " + xmlDoc.getDocumentElement().getNodeName());

            parseSeatsNumber();
            parsePotInfo();
            parseCardsInfo();
            parsePlayersInfo();
            parseActionInfo();

        } catch (Exception e) {
            Logger.error("Error reading table model file: " + e.toString());
        }
    }

    private void parseActionInfo() {
    	NodeList actionNodes = xmlDoc.getElementsByTagName("action");
        for (int n = 0; n < actionNodes.getLength(); n++) {
            Node actionNode = actionNodes.item(n);
            if (actionNode.getNodeType() == Node.ELEMENT_NODE) {
            	Element element = (Element) actionNode;
                actionInfos[n] = getActionInfoFromElement(element);
            }
        }
	}

	private void parseSeatsNumber() {
        try {
            NodeList seatNode = xmlDoc.getElementsByTagName("seats");
            Node seatNodeValue = seatNode.item(0).getFirstChild();
            seats = parseInt(seatNodeValue.getNodeValue());
        } catch (Exception e) {
            Logger.error("Error reading table number of seats: " + e.toString());
        }
    }

    private void parsePotInfo() {
        try {
            NodeList potNode = xmlDoc.getElementsByTagName("pot");
            Element potElement = (Element) potNode.item(0);
            potInfo = getBoxInfoFromElement(potElement);
        } catch (Exception e) {
            Logger.error("Error reading table pot info: " + e.toString());
        }
    }

    private void parseCardsInfo() {
        try {
            NodeList cardsInfoNode = xmlDoc.getElementsByTagName("cards_info");
            Element cardsInfoValue = (Element) cardsInfoNode.item(0);
            int cardsWidth = parseInt(getTagValue("width", cardsInfoValue));
    		int cardsHeight = parseInt(getTagValue("height", cardsInfoValue));
            
            NodeList boardNodes = xmlDoc.getElementsByTagName("board").item(0).getChildNodes();
            for (int n = 0; n < boardNodes.getLength(); n++) {
                Node boardNode = boardNodes.item(n);
                if (boardNode.getNodeType() == Node.ELEMENT_NODE) {
                    cardsInfoValue = (Element) boardNode;
                    cardInfoList.add(getCardInfoFromElement(cardsInfoValue, cardsWidth, cardsHeight));
                }
            }

        } catch (Exception e) {
            Logger.error("Error reading CardInfo: " + e.toString());
        }
    }

    private void parsePlayersInfo() {
        try {
            NodeList cardsInfoNode = xmlDoc.getElementsByTagName("cards_info");
            Element cardsInfoValue = (Element) cardsInfoNode.item(0);
            int cardsWidth = parseInt(getTagValue("width", cardsInfoValue));
            int cardsHeight = parseInt(getTagValue("height", cardsInfoValue));

            NodeList playersInfoNode = xmlDoc.getElementsByTagName("players_info");
            Element playersInfoValue = (Element) playersInfoNode.item(0);

            //Name Info
            Element nameElement = (Element) playersInfoValue.getElementsByTagName("name").item(0);
            String strNameWidth = getTagValue("width", nameElement);
            String strNameHeight = getTagValue("height", nameElement);
            Logger.log("players_info/name width = " + strNameWidth);
            Logger.log("players_info/name height = " + strNameHeight);

            //Stack Info
            Element stackElement = (Element) playersInfoValue.getElementsByTagName("stack").item(0);
            String strStackWidth = getTagValue("width", stackElement);
            String strStackHeight = getTagValue("height", stackElement);
            Logger.log("players_info/stack width = " + strStackWidth);
            Logger.log("players_info/stack height = " + strStackHeight);

            Logger.log("Reading player info...");
            NodeList cardsNodes = xmlDoc.getElementsByTagName("player");
            for (int n = 0; n < cardsNodes.getLength(); n++) {
                Node nNode = cardsNodes.item(n);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    playersInfoValue = (Element) nNode;

                    String strIndex = getTagValue("index", playersInfoValue);

                    //Name info
                    Element playerNameInfoEl = (Element) playersInfoValue.getElementsByTagName("name").item(0);
                    String strNamePositionX = getTagValue("x", playerNameInfoEl);
                    String strNamePositionY = getTagValue("y", playerNameInfoEl);

                    //Stack info
                    Element playerStackInfoEl = (Element) playersInfoValue.getElementsByTagName("stack").item(0);
                    String strStackPositionX = getTagValue("x", playerStackInfoEl);
                    String strStackPositionY = getTagValue("y", playerStackInfoEl);

                    //Visible Cards info
                    Element playerVCardInfoEl = (Element) playersInfoValue.getElementsByTagName("visible_cards").item(0);
                    String strVCardPositionX = getTagValue("x", playerVCardInfoEl);
                    String strVCardPositionY = getTagValue("y", playerVCardInfoEl);

                    //Card_0 info
                    Element playerCard_0_InfoEl = (Element) playersInfoValue.getElementsByTagName("card_0").item(0);
                    String strCard0PositionX = getTagValue("x", playerCard_0_InfoEl);
                    String strCard0PositionY = getTagValue("y", playerCard_0_InfoEl);

                    //Card_1 info
                    String strCard1PositionX = "0", strCard1PositionY = "0";
                    if (playersInfoValue.getElementsByTagName("card_1") != null) {
                        Element playerCard_1_InfoEl = (Element) playersInfoValue.getElementsByTagName("card_1").item(0);
                        strCard1PositionX = getTagValue("x", playerCard_1_InfoEl);
                        strCard1PositionY = getTagValue("y", playerCard_1_InfoEl);
                    }

                    Logger.log("PlayerInfo[" + strIndex + "]");
                    Logger.log("name info (" + strNamePositionX + "," + strNamePositionY + ")");
                    Logger.log("stack info (" + strStackPositionX + "," + strStackPositionY + ")");

                    //player info
                    PokerPlayerInfo playerInfo = new PokerPlayerInfo();
                    playerInfo.setIndex(Integer.parseInt(strIndex));

                    //name info
                    BoxInfo name = playerInfo.getNameInfo();
					name.setPositionX(Integer.parseInt(strNamePositionX));
                    name.setPositionY(Integer.parseInt(strNamePositionY));
                    name.setWidth(Integer.parseInt(strNameWidth));
                    name.setHeight(Integer.parseInt(strNameHeight));

                    //stack info
                    BoxInfo stack = playerInfo.getStackInfo();
					stack.setPositionX(Integer.parseInt(strStackPositionX));
                    stack.setPositionY(Integer.parseInt(strStackPositionY));
                    stack.setWidth(Integer.parseInt(strStackWidth));
                    stack.setHeight(Integer.parseInt(strStackHeight));

                    //visible cards info
                    CardExInfo visibleCard = playerInfo.getVisibleCardInfo();
					visibleCard.setPositionX(Integer.parseInt(strVCardPositionX));
                    visibleCard.setPositionY(Integer.parseInt(strVCardPositionY));

                    //Card0 info
                    CardInfo card_0 = playerInfo.getCardInfo_0();
					card_0.setPositionX(Integer.parseInt(strCard0PositionX));
                    card_0.setPositionY(Integer.parseInt(strCard0PositionY));
                    card_0.setWidth(cardsWidth);
                    card_0.setHeight(cardsHeight);


                    //Card1 info
                    CardInfo card_1 = playerInfo.getCardInfo_1();
					card_1.setPositionX(Integer.parseInt(strCard1PositionX));
                    card_1.setPositionY(Integer.parseInt(strCard1PositionY));
                    card_1.setWidth(cardsWidth);
                    card_1.setHeight(cardsHeight);

                    pokerPlayerInfoList.add(playerInfo);
                }
            }

        } catch (Exception e) {
            Logger.error("Error reading Player Info: " + e.toString());
        }
    }
    
    private BoxInfo getBoxInfoFromElement(Element element) {
    	int x = parseInt(getTagValue("x", element));
        int y = parseInt(getTagValue("y", element));
        int width = parseInt(getTagValue("width", element));
        int height = parseInt(getTagValue("height", element));
        return new BoxInfo(x, y, width, height);
    }
    
    private ActionInfo getActionInfoFromElement(Element element) {
        return new ActionInfo(getBoxInfoFromElement((element)));
    }

	private CardInfo getCardInfoFromElement(Element cardsInfoValue, int width, int height) {
		int x = parseInt(getTagValue("x", cardsInfoValue));
		int y = parseInt(getTagValue("y", cardsInfoValue));
		
		CardInfo cardInfo = new CardInfo(x, y, width, height, new ImageCutOp());
		cardInfo.setIndex(parseInt(getTagValue("index", cardsInfoValue)));
		return cardInfo;
	}

    
    public int getSeats() {
        return seats;
    }

    public CardInfo getCardInfo(int i) {
        return cardInfoList.get(i);
    }

    public BoxInfo getPotInfo() {
        return potInfo;
    }

    public List<PokerPlayerInfo> getPokerPlayerInfoList() {
        return pokerPlayerInfoList;
    }

    public void setPokerPlayerInfoList(List<PokerPlayerInfo> pokerPlayerInfoList) {
        this.pokerPlayerInfoList = pokerPlayerInfoList;
    }

    public PokerPlayerInfo getPokerPlayerInfo(int index) {
        for(PokerPlayerInfo info : pokerPlayerInfoList)
            if(info.getIndex() == index) 
                return info;

        return null;
    }

	public ActionInfo getActionInfo(int index) {
		return actionInfos[index];
	}
}
