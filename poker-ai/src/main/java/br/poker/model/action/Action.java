package br.poker.model.action;

public abstract class Action {
    protected int value;
    
    public Action() {
    	this.value = 0;
    }
    
    public Action(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final Action CHECK = new Check();
    public static final Action FOLD = new Fold();
	public static final Action POST_SB = new PostSmallBlind();
	public static final Action POST_BB = new PostBigBlind();
	public static final Action WINS_POT = new WinsPot();

	public static Action CALL(int value) {
		return new Call(value);
	}

	public static Action RAISE(int value) {
		return new Raise(value);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + (value>0?value:"");
	}
	
	@Override
	public boolean equals(Object obj) {
		Action other = (Action)obj;
		return other.getClass() == getClass() && other.getValue() == getValue();
	}
}
