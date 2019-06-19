public class GameRule {
	private static GameRule instance = new GameRule();

	private GameRule(){}

	public static GameRule getInstance() {
		return instance;
	}

	public int rewardAnte(CardHand dealer, CardHand player) {
		CardHand d = dealer;
		CardHand p = player;
		
		if (d.getCardsType() == CardHand.HAND_TYPE_VALUE_HIC && d.getPoker3().getPointValue() <= Poker.POINTS_VALUES.get("J")) {
			return 1;
		}
		else {
			return rule(p,d)*2;
		}
	}
	
	public int rewardPairPlus(CardHand ch) {
		int cardType = ch.getCardsType();
		
		if (cardType == CardHand.HAND_TYPE_VALUE_PAR)
			return 1;
		else if (cardType == CardHand.HAND_TYPE_VALUE_FLU)
			return 3;
		else if (cardType == CardHand.HAND_TYPE_VALUE_32A)
			return 6;
		else if (cardType == CardHand.HAND_TYPE_VALUE_STR)
			return 6;
		else if (cardType == CardHand.HAND_TYPE_VALUE_3KD)
			return 30;
		else if (cardType == CardHand.HAND_TYPE_VALUE_STF)
			return 40;
		else
			return -1;
	}
	
	public int rule(CardHand cardHand1, CardHand cardHand2) {
		CardHand c1 = cardHand1;
		CardHand c2 = cardHand2;

		// Card Type
		if (c1.getCardsType() > c2.getCardsType()) return 1;
		if (c1.getCardsType() < c2.getCardsType()) return -1;
	
		// 3 of a kind compare point
		if (c1.getCardsType() == CardHand.HAND_TYPE_VALUE_3KD) {
			if (c1.getPoker1().getPointValue() > c2.getPoker1().getPointValue()) return 1;
			if (c1.getPoker1().getPointValue() < c2.getPoker1().getPointValue()) return -1;
			return 0;
		}
	
		// straight or straight flush compare point
		if (c1.getCardsType() == CardHand.HAND_TYPE_VALUE_STF || c1.getCardsType() == CardHand.HAND_TYPE_VALUE_STR) {
			if (c1.getPoker3().getPointValue() > c2.getPoker3().getPointValue()) return 1;
			if (c1.getPoker3().getPointValue() < c2.getPoker3().getPointValue()) return -1;
			return 0;
		}
	
		// flush or single compare 3to1 point
		if (c1.getCardsType() == CardHand.HAND_TYPE_VALUE_FLU || c1.getCardsType() == CardHand.HAND_TYPE_VALUE_HIC) {
			if (c1.getPoker3().getPointValue() > c2.getPoker3().getPointValue()) return 1;
			if (c1.getPoker3().getPointValue() < c2.getPoker3().getPointValue()) return -1;
	
			if (c1.getPoker2().getPointValue() > c2.getPoker2().getPointValue()) return 1;
			if (c1.getPoker2().getPointValue() < c2.getPoker2().getPointValue()) return -1;
			
			if (c1.getPoker1().getPointValue() > c2.getPoker1().getPointValue()) return 1;
			if (c1.getPoker1().getPointValue() < c2.getPoker1().getPointValue()) return -1;
			return 0;
		}

		// pair compare pair point
		if (c1.getCardsType() == CardHand.HAND_TYPE_VALUE_PAR) {
			if (c1.getPoker2().getPointValue() > c2.getPoker2().getPointValue()) return 1;
			if (c1.getPoker2().getPointValue() < c2.getPoker2().getPointValue()) return -1;
			
			// get non pair point
			int fgf1Dp = 0;
			int fgf2Dp = 0;
		
			if (c1.getPoker1().getPointValue() == c1.getPoker2().getPointValue()) {
				fgf1Dp = c1.getPoker3().getPointValue();
			} 
			else {
				fgf1Dp = c1.getPoker1().getPointValue();
			}
		
			if (c2.getPoker1().getPointValue() == c2.getPoker2().getPointValue()) {
				fgf2Dp = c2.getPoker3().getPointValue();
			} 
			else {
				fgf2Dp = c2.getPoker1().getPointValue();
			}
		
			// compare non pair point
			if (fgf1Dp > fgf2Dp) return 1;
			if (fgf1Dp < fgf2Dp) return -1;
		
			return 0;
		}
	
		return 0;
	}
}