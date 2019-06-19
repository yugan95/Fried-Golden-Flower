import java.util.ArrayList;
import java.util.List;

public class GameScore {
	private List<PlayCard> players;
	private CardHand dealer;
	private int round;
	
	public GameScore(String score) {
		players = new ArrayList<PlayCard>();
		String s, line, type;
		s = score;
		
		while (s.length() > 0) {
			// get one line 
			line = s.substring(0,s.indexOf("\n"));

			if (line.length() > 6 ) {
				// get line type.
				type = line.substring(0, 6);
			}
			else 
				type = "";
			
			// round
			if (type.equals("ROUND:")) {
				round = Integer.parseInt(line.substring(9));
			}
			// dealer's cards
			else if (type.equals("DEALER")) {
				dealer = new CardHand(line.substring(9,18));
			}
			// player's cards.
			else if (type.equals("PLAYER")) {
				PlayCard playcard = new PlayCard(line.substring(0,8),line.substring(9,18),line.substring(18));
				players.add(playcard);
			}

			// next line.
			if (s.indexOf("\n") > 0) {
				s = s.substring(s.indexOf("\n")+1);
			}
			else
				s = "";
		}
	}

	public int getRound() {
		return this.round;
	}

	public CardHand getDealerCards() {
		return this.dealer;
	}

	public List<PlayCard> getPlayerList() {
		return this.players;
	}
}
