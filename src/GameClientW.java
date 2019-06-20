import java.util.List;

public class GameClientW extends GameClient{
    public static void main(String[] args) {
        new GameClientW().launch();
    }

    public GameClientW() {
    	register("WMH","127.0.0.1");
    }

    @Override
    public void processScore(String playerID, GameScore score) {
    	List<PlayCard> players = score.getPlayerList();
    	System.out.println(playerID);
    	System.out.println("\nDEALER    " + score.getDealerCards().toString());
    	for (PlayCard p : players) {
    		String result = p.getAnteScore() > 0 ? " W": " L";
    		result += (p.getPairplusScore() > 0) ? " W" : ((p.getPairplusScore() == 0) ? " -" : " L");  
    		System.out.println(p+result);
    	}
    }

    @Override
    public boolean getFollowup(CardHand cards) {
    	return true;
    }
    
    @Override
    public int getAnteBet() {
    	return 3;
//		return (int)(1+Math.random()*6);
    }
    
    @Override
    public int getPairPlusBet() {
    	return 1;
//		return Math.random() > 0.4 ? 1 : 0 ;
    }

}
