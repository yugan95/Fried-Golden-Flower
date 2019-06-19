import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
	public static Map<String, Player> PLAYERS = new HashMap<String, Player>();
	
	static {
		for (int i = 0; i < 30; i++) {
			int num = i + 1;
			String numCode = num < 10 ? "0" + num : num + "";
			String playerID = "PLAYER" + numCode;
			PLAYERS.put(playerID, new Player(playerID));
		}
	}

	private String playerName;
	private String playerID;
	private boolean logon;
	private int score;
	private int scorepp;
	private int count;
	private CardTypeRate ctr;
	private List<String> gameLogs = new ArrayList<String>();
	
	public static String FreePlayID() {
		for (int i = 0; i < 30; i++) {
			int num = i + 1;
			String numCode = num < 10 ? "0" + num : num + "";
			String id = "PLAYER" + numCode;
			if (! PLAYERS.get(id).logon) {
				return id;
			}
		}
		return "";
	}
	
	// logout
	public void logout() {
		this.logon = false;
		this.playerName = "";
	}
	
	//set player name
	public String getPlayerName() {
		return playerName;
	}

	// login
	public void logon(String playerName) {
		this.logon = true;
		this.playerName = playerName;
	}
	
	public CardTypeRate getCardTypeRate() {
		return this.ctr;
	}
	
	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public int getScorepp() {
		return scorepp;
	}
	
	public void setScorepp(int score) {
		this.scorepp = score;
	}

	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
		
	public List<String> getGameLogs() {
		return gameLogs;
	}
	
	public void setGameLogs(List<String> gameLogs) {
		this.gameLogs = gameLogs;
	}

	public Player(String playerID) {
		this.score = 0;
		this.count = 0;
		this.scorepp = 0;
		this.playerID = playerID;
		this.ctr = new CardTypeRate();
	}
}
