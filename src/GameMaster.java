import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class GameMaster {
	public List<Poker> cards;	// Game Cards
	public List<CardGamePlayer> gameplayers;	//Players
	private CardHand dealer;	// dealer's card
	private int playerNums;	// Player Numbers
	private int round;	// rounds
	private int maxPlayerNums;
    private GameLog loger;
	private CardTypeRate dealerRate;
	private int Xcount;
    
	public static void main(String[] args) {
		GameMaster master = new GameMaster(6);
		master.playerLogon();
		
		for (int i = 1; i <= 1000; i++) {
			master.newGame();
			master.playerReady();
			master.deal();
			master.playerRaise();
			master.determine();
		}
		master.scoreList();
		master.rateList();
	}

	public GameMaster(int maxPlayerNums) {
		this.gameplayers = new ArrayList<CardGamePlayer>();
		this.maxPlayerNums = maxPlayerNums;
		this.playerNums = maxPlayerNums;
    	this.loger = GameLog.getLogWriter();
    	this.dealerRate = new CardTypeRate();
		this.round = 0;
		System.out.println("=======Three Cards Game, " + this.maxPlayerNums + " Player(s).=======");

		cards = new ArrayList<Poker>();
		// Loading all cards
		for (String type : Poker.TYPES) {
			for (String point : Poker.POINTS) {
				String tp = type + point;
				Poker poker = new Poker(tp);
				cards.add(poker);
			}
		}
		
		/*
		String c = "";
		for (Poker p : cards) {
			c += p;
		}
		loger.log(c);
		*/
	}	
	
	public int getPlayerNum() {
		return this.playerNums;
	}

	public void playerLogon() {
		for (int i = 1; i <= this.playerNums; i++) {
			String pn = i < 10 ? "0" + i : i + "";
			String playerID = "PLAYER" + pn;

			CardGamePlayer player = new CardGamePlayer(playerID);
			Player.PLAYERS.get(playerID).logon("");
			gameplayers.add(player);
		}
	}
	
	public void newGame() {
		shuffle();
		
		for (CardGamePlayer player : gameplayers) {
			player.reset();
		}
	}
	
	public void playerReady() {
		for (CardGamePlayer player : gameplayers) {
			//double x = Math.random();
			//int pp = 0;
			//int ab = 1;
			if (player.getPlayerID().equals("PLAYER02")) {
				int recommand = Player.PLAYERS.get(player.getPlayerID()).getCardTypeRate().getRecommandPairPlus();
				if (recommand > 1)
					player.bet(6,2);
				else if (recommand > 0)
					player.bet(3, 1);
				else
					player.bet(1, 0);
			}
			else if (player.getPlayerID().equals("PLAYER01"))
				player.bet(3,1);
			else if (player.getPlayerID().equals("PLAYER03"))
				player.bet(3,1);
			else if (player.getPlayerID().equals("PLAYER04"))
				player.bet(3,0);
			else if (player.getPlayerID().equals("PLAYER05"))
				player.bet(3,0);
			else {
				int pp = Math.random() > 0.4 ? (Math.random() > 0.8 ? 2: 1) : 0 ;
				int ab = (int)(1+Math.random()*6); // get random 1-5
				player.bet(ab, pp);
			}
				
		}
	}
	
	public void playerRaise() {
		//double bet;
		for (CardGamePlayer gameplayer : gameplayers) {
			if (gameplayer.getPlayerID().equals("PLAYER02")) {
				CardHand hand = gameplayer.getCardHand();
				double x = hand.getWinExpectation();
				
				if (x >= 1.0)
					gameplayer.followup();
				else if (x <= 0.0)
					gameplayer.fold();
				else {
					double bet = Math.random() *1.6 + x;
					if (bet >= 1.0)
						gameplayer.followup();
					else
						gameplayer.fold();
				}
			}
			else
				gameplayer.followup();
			/*
			bet = Math.random();
			CardHand hand = gameplayer.getCardHand();
			if (hand.getChanceofWinning() < 0.30) {
				if (bet > 0.3) gameplayer.followup();
			}
			else if (hand.getChanceofWinning() < 0.40) {
				if (bet > 0.4) gameplayer.followup();
			}
			else if (hand.getChanceofWinning() < 0.50) {
				if (bet > 0.5) gameplayer.followup();
			}
			else if (hand.getChanceofWinning() < 0.60) {
				if (bet > 0.6) gameplayer.followup();
			}
			else if (hand.getChanceofWinning() < 0.70) {
				if (bet > 0.9) gameplayer.followup();
			}
			
			gameplayer.fold();
			*/
		}
	}
	
	private void shuffle() {
		round ++;

		// Shuffle
		Collections.shuffle(cards);
		
		String c = ""; 
		
		/*
		//System.out.println("\n\n" + "============Rounds (" + round + ") Shuffling==========");
		for (Poker p : cards) {
			c += p;
			//System.out.print(p +",");
		}
		loger.log(c);
		*/
		//System.out.println("");
	}
	
	public void deal() {
		int k = 0;
		Poker poker1 = null;
		Poker poker2 = null;
		Poker poker3 = null;
		
		//System.out.println("\n\n" + "============Rounds (" + round + ") Dealing==========");
		
		for (CardGamePlayer player : gameplayers) {
			// player is ready.
			if (player.isReady()) {
				for (int j = 0; j < 3; j++) {
					if (j == 0) {
						poker1 = (Poker) this.cards.get(k);
					}
					if (j == 1) {
						poker2 = (Poker) this.cards.get(k);
					}
					if (j == 2) {
						poker3 = (Poker) this.cards.get(k);
					}
					
					k++;
				}

				CardHand cardhand = new CardHand(poker1, poker2, poker3);
				player.setCards(cardhand);
				Player.PLAYERS.get(player.getPlayerID()).getCardTypeRate().classify(cardhand);
			}

		}

		poker1 = (Poker) this.cards.get(k++);
		poker2 = (Poker) this.cards.get(k++);
		poker3 = (Poker) this.cards.get(k++);
		dealer = new CardHand(poker1, poker2, poker3);
		dealerRate.classify(dealer);
		// print all players.
		/*
		System.out.println(dealer);
		for (CardHand hand : playersCards) {
			System.out.println(hand);
		}
		*/
	}

	public void determine() {
		//System.out.println("\n\n" + "============Rounds (" + this.round + ") Balance==========");
		//System.out.println("DEALER    " + dealer);
		//loger.log(String.format("%-4d DEALER    %-3d%s  %s", this.round,dealer.getCardsType(),dealer,dealerRate));
		
		int score1, score2; 
		int currentScore;
		int currentCount;
		int currentScorepp;
		int gameScore = 0;
		String win;
		
		for (CardGamePlayer gameplayer : gameplayers) {
			score2 = GameRule.getInstance().rewardPairPlus(gameplayer.getCardHand())*gameplayer.getPairplusBets();
			if (gameplayer.isFollowup())
				score1 = GameRule.getInstance().rewardAnte(dealer, gameplayer.getCardHand())*gameplayer.getAnteBets();
			else
				score1 = -1*gameplayer.getAnteBets();
			
			currentScore = Player.PLAYERS.get(gameplayer.getPlayerID()).getScore();
			currentCount = Player.PLAYERS.get(gameplayer.getPlayerID()).getCount();
			currentScorepp = Player.PLAYERS.get(gameplayer.getPlayerID()).getScorepp();
			
			currentCount ++;
			currentScore += score1;
			currentScore += score2;
			currentScorepp += score2;
			
			gameScore += score1;
			gameScore += score2;
			
			Player.PLAYERS.get(gameplayer.getPlayerID()).setScore(currentScore);
			Player.PLAYERS.get(gameplayer.getPlayerID()).setCount(currentCount);
			Player.PLAYERS.get(gameplayer.getPlayerID()).setScorepp(currentScorepp);
			
			if (score1 > 0) {
				win = " W";
			}
			else{
				win = " L";
			}
			
			if (score2 > 0) {
				win += " W";
			}
			else if (score2 == 0){
				win += " -";
			}
			else {
				win += " L";
			}
			
			if (gameplayer.isFollowup()) {
				win += " T";
			}
			else {
				win += " -";
			}
			
			//System.out.println(gameplayer + win + String.format(" %3d%3d",score1,score2));
			//loger.log(String.format("%-4d %s  %-3d%s  %s%3d%3d", this.round, gameplayer.getPlayerID(),gameplayer.getCardHand().getCardsType(), gameplayer.getCardHand(),Player.PLAYERS.get(gameplayer.getPlayerID()).getCardTypeRate(),gameplayer.getPairplusBets(),score2));
			//if (gameplayer.getPlayerID().equals("PLAYER01"))
			loger.log(String.format("%s  %s%3d%3d", gameplayer.getPlayerID(),Player.PLAYERS.get(gameplayer.getPlayerID()).getCardTypeRate(),gameplayer.getPairplusBets(),score2));
		}
		
		//System.out.println(String.format("TOTAL SCORE: %3d",gameScore));
	}
	
	public void scoreList() {
		System.out.println("\n\n==========Player(s) Score List:=========="); 
		for (int j = 1; j <= this.playerNums; j++) {
			String pn = j < 10 ? "0" + j : j + "";
			String playerID = "PLAYER" + pn;
			int currentScore = Player.PLAYERS.get(playerID).getScore();
			int currentCount = Player.PLAYERS.get(playerID).getCount();
			int currentScorepp = Player.PLAYERS.get(playerID).getScorepp();
			System.out.println(playerID + String.format("%12s", Player.PLAYERS.get(playerID).getPlayerName())+ String.format("%4d", currentCount) + String.format("%7d",currentScore) + String.format("%7d",currentScorepp));
		}
	}

	public void rateList() {
		System.out.println("\n\n==========Player(s) Card Type Rate List:=========="); 
		System.out.println("DEALER   " + dealerRate);
		for (int j = 1; j <= this.playerNums; j++) {
			String pn = j < 10 ? "0" + j : j + "";
			String playerID = "PLAYER" + pn;
			System.out.println(playerID + " " + Player.PLAYERS.get(playerID).getCardTypeRate());
		}
	}
}
