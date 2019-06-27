import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameClientW extends GameClient{
    public static void main(String[] args) {
        new GameClientW().launch();
    }

    public GameClientW() {
    	register("WMH","127.0.0.1");
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
        int poker3 = Poker.POINTS_VALUES.get(cards.getPoker3().getPoint());
        int poker2 = Poker.POINTS_VALUES.get(cards.getPoker2().getPoint());

        if(cards.getCardsType()==CardHand.HAND_TYPE_VALUE_HIC
                && (poker3<12 || (poker3==12 && poker2<6))){
            return false;
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
    	return 0;
//		return Math.random() > 0.4 ? 1 : 0 ;
    }

}
