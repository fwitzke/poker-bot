package br.poker.model.table.structure;

public class PokerPlayerInfo {
    private int index;
    private BoxInfo nameInfo, stackInfo;
    private CardInfo cardInfo_0, cardInfo_1;
    private CardExInfo visibleCardInfo;

    public PokerPlayerInfo() {
        nameInfo = new BoxInfo();
        stackInfo = new BoxInfo();
        cardInfo_0 = new CardInfo();
        cardInfo_1 = new CardInfo();
        visibleCardInfo = new CardExInfo();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CardInfo getCardInfo_0() {
        return cardInfo_0;
    }

    public void setCardInfo_0(CardInfo cardInfo_0) {
        this.cardInfo_0 = cardInfo_0;
    }

    public CardInfo getCardInfo_1() {
        return cardInfo_1;
    }

    public void setCardInfo_1(CardInfo cardInfo_1) {
        this.cardInfo_1 = cardInfo_1;
    }

    public BoxInfo getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(BoxInfo nameInfo) {
        this.nameInfo = nameInfo;
    }

    public BoxInfo getStackInfo() {
        return stackInfo;
    }

    public void setStackInfo(BoxInfo stackInfo) {
        this.stackInfo = stackInfo;
    }

    public CardExInfo getVisibleCardInfo() {
        return visibleCardInfo;
    }

    public void setVisibleCardInfo(CardExInfo visibleCardInfo) {
        this.visibleCardInfo = visibleCardInfo;
    }

    public String toString() {
        return "[PokerTablePlayerInfo: "
                + " |Name_ " + getNameInfo().toString()
                + " |Stack_ " + getStackInfo().toString()
                + "]";
    }
}
