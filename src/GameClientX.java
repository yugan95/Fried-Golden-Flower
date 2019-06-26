import java.util.List;

public class GameClientX extends GameClient{
    public static void main(String[] args) {
        new GameClientX().launch();
    }
    
    public GameClientX() {
    	register("Xerry","127.0.0.1");
    }

    @Override
    public void processScore(GameScore score) {
    	List<PlayCard> players = score.getPlayerList();
    	System.out.println("\nDEALER    " + score.getDealerCards().toString());
    	for (PlayCard p : players) {
    		String result = p.getAnteScore() > 0 ? " W": " L";
    		result += (p.getPairplusScore() > 0) ? " W" : ((p.getPairplusScore() == 0) ? " -" : " L");  
    		System.out.println(p+result);
    	}
    }

    @Override
    public boolean getFollowup(CardHand cards) {
		double bet;
		
		bet = Math.random();
		if (cards.getChanceofWinning() < 0.30) {
			if (bet > 0.3) return false;
		}
		else if (cards.getChanceofWinning() < 0.40) {
			if (bet > 0.5) return false;
		}
		else if (cards.getChanceofWinning() < 0.50) {
			if (bet > 0.7) return false;
		}
		else if (cards.getChanceofWinning() < 0.60) {
			if (bet > 0.8) return false;
		}

    	return true;
    }
    
    @Override
    public int getAnteBet() {
    	return 1;
//		return (int)(1+Math.random()*6);
    }
    
    @Override
    public int getPairPlusBet() {
    	return 1;
//		return Math.random() > 0.4 ? 1 : 0 ;
    }

}
