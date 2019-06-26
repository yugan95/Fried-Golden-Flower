import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardHand implements Comparable<CardHand>{
	public static Map<Integer, String> HAND_TYPES = new HashMap<Integer, String>();
	public static int HAND_TYPE_VALUE_HIC = 10;	//High Card
	public static int HAND_TYPE_VALUE_PAR = 20;	//One Pair
	public static int HAND_TYPE_VALUE_FLU = 30; //Flush
	public static int HAND_TYPE_VALUE_32A = 35; //Straight A-3-2
	public static int HAND_TYPE_VALUE_STR = 40;	//Straight
	public static int HAND_TYPE_VALUE_3KD = 50; //3 of a Kind
	public static int HAND_TYPE_VALUE_STF = 60; //Straight Flush

	static {
	HAND_TYPES.put(10, "HIGH CARD     ");
	HAND_TYPES.put(20, "ONE PAIR      ");
	HAND_TYPES.put(30, "FLUSH         ");
	HAND_TYPES.put(35, "STRAIGHT 32A  ");
	HAND_TYPES.put(40, "STRAIGHT      ");
	HAND_TYPES.put(50, "3 of A KIND   ");
	HAND_TYPES.put(60, "STRAIGHT FLUSH");
	}

	private Poker poker1;
	private Poker poker2;
	private Poker poker3;
	private int cardsType;

	public static void main(String[] args) {
		List<CardHand> list = new ArrayList<CardHand>();
		List<Poker> cards = new ArrayList<Poker>();
		
		for (String type : Poker.TYPES) {
			for (String point : Poker.POINTS) {
				String tp = type + point;
				Poker poker = new Poker(tp);
				cards.add(poker);
			}
		}
		
		for (int i = 0; i < 100; i++) {
			Collections.shuffle(cards);

			Poker poker1 = cards.get(0);
			Poker poker2 = cards.get(1);
			Poker poker3 = cards.get(2);

			CardHand cardHand = new CardHand(poker1, poker2, poker3);
			list.add(cardHand);
		}
		
		Collections.sort(list);

		for (CardHand ch : list) {
			System.out.println(ch);
		}
	}

	public CardHand(String tps) {
		Poker p1 = new Poker(tps.substring(0,2));
		Poker p2 = new Poker(tps.substring(3,5));
		Poker p3 = new Poker(tps.substring(6,8));
		
		List<Poker> pokers = new ArrayList<Poker>();
		pokers.add(p1);
		pokers.add(p2);
		pokers.add(p3);
		Collections.sort(pokers);

		this.poker1 = pokers.get(0);
		this.poker2 = pokers.get(1);
		this.poker3 = pokers.get(2);

		this.cardsType = getCardsTypeFun();
	}
	
	public CardHand(Poker poker1, Poker poker2, Poker poker3) {
		List<Poker> pokers = new ArrayList<Poker>();
		pokers.add(poker1);
		pokers.add(poker2);
		pokers.add(poker3);
		Collections.sort(pokers);

		this.poker1 = pokers.get(0);
		this.poker2 = pokers.get(1);
		this.poker3 = pokers.get(2);

		this.cardsType = getCardsTypeFun();
	}
	
	public String toMessage() {
		return this.poker3.toString() + this.poker2 + this.poker1;
	}
	
	
	public int getCardsTypeFun() {
		// is 3 of a kind
		if (this.poker1.getPointValue() == this.poker2.getPointValue()
				&& this.poker2.getPointValue() == this.poker3.getPointValue()) {
			return HAND_TYPE_VALUE_3KD;
		}
		
		// is straight
		if (this.poker1.getTypeValue() == this.poker2.getTypeValue()
				&& this.poker2.getTypeValue() == this.poker3.getTypeValue()) {
			// is straight flush
			if (this.poker3.getPointValue() - this.poker2.getPointValue() == 1
					&& this.poker2.getPointValue() - this.poker1.getPointValue() == 1) {
				return HAND_TYPE_VALUE_STF;
			}
			// is flush
			else {
				return HAND_TYPE_VALUE_FLU;
			}
		}

		// is pair
		if (this.poker1.getPointValue() == this.poker2.getPointValue()
				|| this.poker2.getPointValue() == this.poker3.getPointValue()
				|| this.poker1.getPointValue() == this.poker3.getPointValue()) {
			return HAND_TYPE_VALUE_PAR;
		}
		
		// is straight
		if (this.poker3.getPointValue() - this.poker2.getPointValue() == 1
				&& this.poker2.getPointValue() - this.poker1.getPointValue() == 1) {
			return HAND_TYPE_VALUE_STR;
		}
		
		// is straight 3-2-A
		if (this.poker3.getPointValue() == Poker.POINTS_VALUES.get("A") 
				&& this.poker2.getPointValue() == Poker.POINTS_VALUES.get("3") 
				&& this.poker1.getPointValue() == Poker.POINTS_VALUES.get("2")) {
			return HAND_TYPE_VALUE_32A;
		}

		// is single
		return HAND_TYPE_VALUE_HIC;
	}

	// get card hand win rate.
	public double getChanceofWinning() {
		if (cardsType == CardHand.HAND_TYPE_VALUE_PAR) {
			if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("A"))
				return 0.900;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("K"))
				return 0.887;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("Q"))
				return 0.874;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("J"))
				return 0.861;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("T"))
				return 0.848;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("9"))
				return 0.835;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("8"))
				return 0.822;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("7"))
				return 0.809;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("6"))
				return 0.796;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("5"))
				return 0.783;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("4"))
				return 0.77;
			else if (this.poker2.getPointValue() == Poker.POINTS_VALUES.get("3"))
				return 0.757;
			else
				return 0.744;
		}
		else if (cardsType == CardHand.HAND_TYPE_VALUE_FLU)
			return 0.9133;
		else if (cardsType == CardHand.HAND_TYPE_VALUE_32A)
			return 0.9629;
		else if (cardsType == CardHand.HAND_TYPE_VALUE_STR)
			return 0.9629;
		else if (cardsType == CardHand.HAND_TYPE_VALUE_3KD)
			return 0.9955;
		else if (cardsType == CardHand.HAND_TYPE_VALUE_STF)
			return 0.9978;
		else {
			if (this.poker3.getPointValue() == Poker.POINTS_VALUES.get("A"))
				return 0.5701;
			else if (this.poker3.getPointValue() == Poker.POINTS_VALUES.get("K"))
				return 0.4235;
			else if (this.poker3.getPointValue() == Poker.POINTS_VALUES.get("Q"))
				return 0.304;
			else
				return 0.29;
		}
	}

	@Override
	public String toString() {
		return HAND_TYPES.get(this.cardsType) + " [" + this.poker3 + "," + this.poker2 + ","
			+ this.poker1 + "]";
	}

	@Override
	public int compareTo(CardHand o) {
		CardHand cardHand = (CardHand) o;
		return GameRule.getInstance().rule(cardHand, this);
	}

	public Poker getPoker1() {
		return poker1;
	}

	public void setPoker1(Poker poker1) {
		this.poker1 = poker1;
	}

	public Poker getPoker2() {
	return poker2;
	}

	public void setPoker2(Poker poker2) {
		this.poker2 = poker2;
	}

	public Poker getPoker3() {
		return poker3;
	}

	public void setPoker3(Poker poker3) {
		this.poker3 = poker3;
	}

	public int getCardsType() {
		return cardsType;
	}
}
