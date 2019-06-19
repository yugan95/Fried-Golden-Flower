public class PlayCard {
	private String playerID;
	private CardHand cardhand;
	private int anteBets;
	private int pairplusBets;
	private boolean followup;
	private int winAnte;
	private int winPairplus;
	
	public PlayCard(String playerID) {
		this.playerID = playerID;
		this.reset();
	}
	
	public PlayCard() {

	}
	
	public PlayCard(String playerID,String cardhand, String score) {
		this.playerID = playerID;
		this.cardhand = new CardHand(cardhand);
		parseScore(score);
	}
	
	public void reset() {
		this.followup = false;
		this.anteBets = 1;
		this.pairplusBets = 0;
		this.cardhand = null;
		this.winAnte = 0;
		this.winPairplus = 0;
	}

	public void setCards(CardHand cardhand) {
		this.cardhand = cardhand;
	}
	
	public void setScore(int winA, int winP) {
		this.winAnte = winA;
		this.winPairplus = winP;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	
	public String getPlayerID() {
		return this.playerID;
	}

	public CardHand getCardHand() {
		return this.cardhand;
	}
	
	public void followup() {
		this.followup = true;
	}

	public void fold() {
		this.followup = false;
	}

	public void bet(int antebets, int pairplusbets) {
		this.anteBets = antebets;
		this.pairplusBets = pairplusbets;
	}

	public int getAnteBets() {
		return this.anteBets;
	}
	
	public int getPairplusBets() {
		return this.pairplusBets;
	}
	
	public int getAnteScore() {
		return this.winAnte;
	}

	public int getPairplusScore() {
		return this.winPairplus;
	}

	public boolean isFollowup() {
		return this.followup;
	}

	public String toMessage() {
		String followup = this.followup ? "T" : "-";
		String score = String.format("%2d%2d%2s%4d%3d",this.anteBets,this.pairplusBets,followup,this.winAnte,this.winPairplus);

		return this.playerID + " " + cardhand.toMessage() + score;
	}
	
	private void parseScore(String score) {
		String s;
		
		s = score.substring(0,2).trim();
		this.anteBets = Integer.parseInt(s);
		s = score.substring(2,4).trim();
		this.pairplusBets = Integer.parseInt(s);
		s = score.substring(4,6).trim();
		this.followup = s.equals("T") ? true : false;
		s = score.substring(6,10).trim();
		this.winAnte = Integer.parseInt(s);
		s = score.substring(10,13).trim();
		this.winPairplus = Integer.parseInt(s);
	}
	
	@Override
	public String toString() {
		String followup = this.followup ? "T" : "-";
		return this.playerID + "  " + cardhand + " " + this.anteBets + " " + this.pairplusBets + " " + followup;
	}
	
}
