import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


public class GameService {
	public List<Poker> cards;	// Game Cards
	public List<CardGamePlayer> players;	//Players
	private CardHand dealer;
	private int playerNums;	// Player Numbers
	private int round;	// rounds
	private int maxPlayerNums;
	private int maxRound;
	
	
	public static void main(String[] args) {
		int playerNum = 5;
		GameService service = new GameService(playerNum,50);
		for (int i = 1; i <= playerNum; i++)
			service.playerLogon("PLAYER0" + i,"");
		
		for (int i = 1; i <= 100; i++) {
			service.newGame();
			for (int j = 1; j <= playerNum; j++)
				service.playerBetting("PLAYER0" + j,2,1);
			service.deal();
			for (int j = 1; j <= playerNum; j++)
				service.playerFollowup("PLAYER0" + j,true);
			service.determine();
		}
		service.scoreList();
	}

	public GameService(int maxPlayerNums, int maxRound) {
		players = new ArrayList<CardGamePlayer>();
		this.maxPlayerNums = maxPlayerNums;
		this.maxRound = maxRound;
		this.playerNums = 0;
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
	}	
	
	// is game finished.
	public boolean hasNextGame() {
		if (round < maxRound) 
			return true;
		else
			return false;
	}
	
	public int getPlayerNum() {
		return this.playerNums;
	}

	// player login
	public boolean playerLogon(String playerID, String playerName) {
		//is max player numbers.
		if (players.size() < maxPlayerNums) {
			CardGamePlayer player = new CardGamePlayer(playerID);
			Player.PLAYERS.get(playerID).logon(playerName);
			players.add(player);
			this.playerNums ++;
		}
		
		// ready to play
		return playersReady();
	}
	
	// is ready to play
	public synchronized boolean playersReady() {
		if (players.size() == maxPlayerNums)
			return true;
		else
			return false;
	}
	
	// transfer player's score to message.
	public void playerState() {
		System.out.println("==========Player States:==========");
		for (CardGamePlayer player : players) {
			String ready = (player.isReady()) ? "T" : "F";
			String check = (player.isCheck()) ? "T" : "F";
			System.out.println(String.format("%s%12s%3s%3s", player.getPlayerID(),Player.PLAYERS.get(player.getPlayerID()).getPlayerName(),ready,check));
		}
	}

	// restart game
	public void restartGame() {
		this.round = 0;
		for (CardGamePlayer player : players) {
			Player.PLAYERS.get(player.getPlayerID()).setScore(0);
			Player.PLAYERS.get(player.getPlayerID()).setCount(0);
		}
	}
	
	// player logout
	public void playerLogout(String playerID) {
		Iterator<CardGamePlayer> it = players.iterator();
		
		while(it.hasNext()){
			CardGamePlayer x = it.next();
		    if(playerID.equals(x.getPlayerID())){
				Player.PLAYERS.get(playerID).logout();
		        it.remove();
				this.playerNums --;
		    }
		}	
	}

	// player betting
	public synchronized boolean playerBetting(String playerID, int ante, int pairplus) {
		boolean allReady = true;
		
		for (CardGamePlayer player : players) {
			try {
				Thread.sleep(50);
				if (playerID.equals(player.getPlayerID())) {
					player.bet(ante, pairplus);
				}
				else
					// check all player bet.
					allReady = allReady && player.isReady();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return allReady;
	}
	
	// player follow up
	public synchronized boolean playerFollowup(String playerID, boolean follow) {
		boolean allCheck = true;
		for (CardGamePlayer player : players) {
			if (playerID.equals(player.getPlayerID())) {
				if (follow)
					player.followup();
				else
					player.fold();
			}
			else
				// check all player follow/fold
				allCheck = allCheck && player.isCheck();
		}
		
		return allCheck;
	}
	
	// transfer player's cards to message.
	public String getCards(String playerID) {
		for (CardGamePlayer player : players) {
			if (playerID.equals(player.getPlayerID())) {
				return player.getCardHand().toMessage();
			}
		}
		
		return "";
	}
	
	// transfer player's score to message.
	public String getScore(String playerID) {
		for (CardGamePlayer player : players) {
			if (playerID.equals(player.getPlayerID())) {
				return String.format("%3d%3d",player.getAnteScore(),player.getPairplusScore());
			}
		}
		
		return "";
	}
	
	// new card game.
	public void newGame() {
		round ++;
		Collections.shuffle(cards);

		for (CardGamePlayer player : players) {
			player.reset();
		}
	}
	
	// deal cards to players.
	public void deal() {
		int k = 0;
		Poker poker1 = null;
		Poker poker2 = null;
		Poker poker3 = null;
		
		for (CardGamePlayer player : players) {
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
			}

		}

		// dealer get cards.
		poker1 = (Poker) this.cards.get(k++);
		poker2 = (Poker) this.cards.get(k++);
		poker3 = (Poker) this.cards.get(k++);
		dealer = new CardHand(poker1, poker2, poker3);
		
		// print all players.
		/*
		System.out.println(dealer);
		for (CardHand hand : playersCards) {
			System.out.println(hand);
		}
		*/
	}

	// determine card game.
	public void determine() {
		System.out.println("\n" + "============Rounds (" + this.round + ") Balance==========");
		System.out.println("DEALER    " + dealer);
		
		int score1, score2; 
		int currentScore;
		int currentCount;
		int gameScore = 0;
		
		String win;
		for (CardGamePlayer player : players) {
			// player follow up score.
			if (player.isFollowup()) {
				score1 = GameRule.getInstance().rewardAnte(dealer, player.getCardHand())*player.getAnteBets();
				score2 = GameRule.getInstance().rewardPairPlus(player.getCardHand())*player.getPairplusBets();
			}
			// player fold score.
			else {
				score1 = -1*player.getAnteBets();
				score2 = -1*player.getPairplusBets();
			}
			
			// get player's total score.
			player.setScore(score1, score2);
			currentScore = Player.PLAYERS.get(player.getPlayerID()).getScore();
			currentCount = Player.PLAYERS.get(player.getPlayerID()).getCount();
			
			// calculate new score.
			currentCount ++;
			currentScore += score1;
			currentScore += score2;
			
			gameScore += score1;
			gameScore += score2;

			// set score to player's state.
			Player.PLAYERS.get(player.getPlayerID()).setScore(currentScore);
			Player.PLAYERS.get(player.getPlayerID()).setCount(currentCount);
			
			// transfer score state.
			if (score1 > 0) {
				win = " W";
			}
			else if (score1 == 0){
				win = " -";
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
			
			if (player.isFollowup()) {
				win += " T";
			}
			else {
				win += " -";
			}
			
			System.out.println(player + win + String.format(" %3d%3d",score1,score2));
		}
		
		System.out.println(String.format("TOTAL SCORE: %3d",gameScore));
		
		
	}
	
	// transfer game score to message code.
	public String balanceMessage() {
		StringBuilder message = new StringBuilder();
		
		message.append("ROUND:   " +  this.round + "\n");
		message.append("DEALER   ");
		message.append(dealer.toMessage() + "\n");
		
		for (CardGamePlayer player : players) {
			message.append(player.toMessage() + "\n");
		}
		
		return message.toString();
	}
	
	// player's score list
	public void scoreList() {
		System.out.println("\n==========Player(s) Score List:=========="); 
		for (int j = 1; j <= this.playerNums; j++) {
			String pn = j < 10 ? "0" + j : j + "";
			String playerID = "PLAYER" + pn;
			int currentScore = Player.PLAYERS.get(playerID).getScore();
			int currentCount = Player.PLAYERS.get(playerID).getCount();
			System.out.println(playerID + String.format("%12s", Player.PLAYERS.get(playerID).getPlayerName())+ String.format("%4d%7d", currentCount,currentScore));
		}
		System.out.println(""); 
	}
}
