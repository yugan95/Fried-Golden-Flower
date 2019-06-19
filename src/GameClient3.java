
public class GameClient3 extends GameClient{
    public static void main(String[] args) {
        new GameClient3().launch();
    }
    
    public GameClient3() {
    	register("Peony","127.0.0.1");
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
