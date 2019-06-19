public class CardTypeRate {
	private int round;
	private int round_HIJ;
	private int round_HIQ;
	private int round_HAK;
	private int round_SFP;
	private int none_HIJ;
	private int none_HIQ;
	private int none_HAK;
	private int none_SFP;
	private int round_Type;
	private int Xcount;
	private int isSFP;
	private String Score5;
	
	public CardTypeRate() {
		this.round = 0;
//		this.round = 1000;

		/*
		this.round_HIJ = 304;
		this.round_HIQ = 119;
		this.round_HAK = 321;
		this.round_SFP = 256;
		*/
		this.round_SFP = 0;
		
//		this.round_HIK = 147;
//		this.round_HIA = 174;
//		this.round_PAR = 169;
//		this.round_SKF = 87;
//		this.round_AKQ = 430;
		this.none_HIJ = 0;
		this.none_HIQ = 0;
		this.none_HAK = 0;
		this.none_SFP = 0;
		this.round_Type = 0;
		this.Xcount = 0;
		this.Score5 = ""; 
	}

	public void classify(CardHand hand) {
		this.isSFP = 0;
		this.round ++;
		this.round_Type = 0;
		switch (hand.getCardsType()) {
			case 60:
			case 50: 
			case 40:
			case 35: 
			case 30:
			case 20:
				this.round_SFP ++;
				this.none_HIJ ++;
				this.none_HIQ ++;
				this.none_HAK ++;
				this.none_SFP = 0;
				this.isSFP = 1;
				this.round_Type = 4;
				break;
			case 10:{
				if (hand.getPoker3().getPointValue() == Poker.POINTS_VALUES.get("A")) {
					this.round_HAK ++;
					this.none_HIQ ++;
					this.none_HIJ ++;
					this.none_HAK = 0;
					this.none_SFP ++;
					this.round_Type = 3;
				}
				else if (hand.getPoker3().getPointValue() == Poker.POINTS_VALUES.get("K")) {
					this.round_HAK ++;
					this.none_HIJ ++;
					this.none_HIQ ++;
					this.none_HAK = 0;
					this.none_SFP ++;
					this.round_Type = 3;
				}
				else if (hand.getPoker3().getPointValue() == Poker.POINTS_VALUES.get("Q")) {
					this.round_HIQ ++;
					this.none_HIJ ++;
					this.none_HIQ = 0;
					this.none_HAK ++;
					this.none_SFP ++;
					this.round_Type = 2;
				}
				else {
					this.round_HIJ ++;
					this.none_HIJ = 0;
					this.none_HIQ ++;
					this.none_HAK ++;
					this.none_SFP ++;
					this.round_Type = 1;
				}
				break;
			}
		}
		
		if (this.Score5.length() >= 5)
			this.Score5 = "";
		
		this.Score5 += this.isSFP;
	}
	
	public int getRecommandPairPlus() {
		if (this.Xcount > 0) {
			this.Xcount --;
			return 2;
		} else if (this.Xcount == 0) 
			return 1;
		else {
			this.Xcount = 0;
			return 0;
		}
	}
	
	@Override
	public String toString() {
//		double HIJ = (double)this.round_HIJ/(double)this.round;
//		double HIQ = (double)this.round_HIQ/(double)this.round;
//		double HIK = (double)this.round_HIK/(double)this.round;
//		double HIA = (double)this.round_HIA/(double)this.round;
//		double PAR = (double)this.round_PAR/(double)this.round;
		double SFP = (double)this.round_SFP/(double)(this.round == 0 ? 1 : this.round);

		return String.format("%5d%8.3f%4d%4d%6s", this.round,SFP,this.none_SFP,this.isSFP,this.Score5);

		
		//return String.format("%5d%8.5f%8.5f%8.5f%8.5f%8.5f%8.5f%4d%4d%4d%4d%4d%4d", this.round,HIJ,HIQ,HIK,HIA,PAR,SKF,this.none_HIJ,this.none_HIQ,this.none_HIK,this.none_HIA,this.none_PAR,this.none_SKF);
		/*
		return String.format("%5d%4d%4d%4d%4d%4d%8.5f%8.5f%8.5f%8.5f%4d", this.round,this.none_HIJ,this.none_HIQ,this.none_HAK,this.none_SFP,this.round_Type,
				getRate(0.304,this.none_HIJ),getRate(0.119,this.none_HIQ),getRate(0.321,this.none_HAK),getRate(0.256,this.none_SFP),rate_Type);
		*/
	}
	
}
