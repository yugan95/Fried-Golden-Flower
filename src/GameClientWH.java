import java.util.List;

public class GameClientWH extends GameClient{
    public static void main(String[] args) {
        new GameClientWH().launch();
    }

    public GameClientWH() {
    	register("WMH2","127.0.0.1");
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
        if(cards.getCardsType()==CardHand.HAND_TYPE_VALUE_HIC
                && Poker.POINTS_VALUES.get(cards.getPoker3().getPoint())<=12){
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
    	return 1;
//		return Math.random() > 0.4 ? 1 : 0 ;
    }

}
