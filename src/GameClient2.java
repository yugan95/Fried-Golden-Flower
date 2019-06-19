
public class GameClient2 extends GameClient{
    public static void main(String[] args) {
        new GameClient2().launch();
    }
    
    public GameClient2() {
    	register("Lotus","127.0.0.1");
    }

    @Override
    public boolean getFollowup(CardHand cards) {
    	return true;
    }
    
    @Override
    public int getAnteBet() {
    	return 3;
    }
    
    @Override
    public int getPairPlusBet() {
    	return 1;
    }

}
