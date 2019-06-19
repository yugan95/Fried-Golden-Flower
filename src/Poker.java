import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poker implements Comparable<Poker> {

	public static String[] TYPES = { "S", "H", "D", "C" }; // Color
	public static String[] POINTS = { "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A" }; // Point
	
	
	public static Map<String, Integer> TYPES_VALUES = new HashMap<String, Integer>(); // Color
	public static Map<String, Integer> POINTS_VALUES = new HashMap<String, Integer>(); // Point

	static {
		TYPES_VALUES.put("S", 1);
		TYPES_VALUES.put("H", 2);
		TYPES_VALUES.put("D", 3);
		TYPES_VALUES.put("C", 4);
		
		POINTS_VALUES.put("2", 2);
		POINTS_VALUES.put("3", 3);
		POINTS_VALUES.put("4", 4);
		POINTS_VALUES.put("5", 5);
		POINTS_VALUES.put("6", 6);
		POINTS_VALUES.put("7", 7);
		POINTS_VALUES.put("8", 8);
		POINTS_VALUES.put("9", 9);
		POINTS_VALUES.put("T", 10);
		POINTS_VALUES.put("J", 11);
		POINTS_VALUES.put("Q", 12);
		POINTS_VALUES.put("K", 13);
		POINTS_VALUES.put("A", 14);
	}

	private String type;
	private String point;

	public static void main(String[] args) {
		List<Poker> ps = new ArrayList<Poker>();

		for (int i = 0; i < 15; i++) {
			int j = (int) (Math.random() * 4);
			int k = (int) (Math.random() * 13);
			String tp = TYPES[j] + POINTS[k];
			ps.add(new Poker(tp));
		}
		
		Collections.sort(ps);
		for (Poker p : ps) {
			System.out.println(p);
		}
	}

	public Poker(String tp) {
		this.type = tp.substring(0, 1); // set Color
		this.point = tp.substring(1); // set Point
	}

	// Get Color Value
	public int getTypeValue() {
		return TYPES_VALUES.get(this.type);
	}
	
	// Get Point Value
	public int getPointValue() {
		return POINTS_VALUES.get(this.point);
	}

	@Override
	public String toString() {
		String rs = this.getType() + this.getPoint();
		if (rs.length() == 2) rs = rs + " ";
		return rs;
	}

	@Override
	public int compareTo(Poker o) {
		// Point GT Color 
		if (o.getPointValue() > this.getPointValue())
			return -1;
		if (o.getPointValue() < this.getPointValue())
			return 1;

		if (o.getTypeValue() > this.getTypeValue())
			return -1;
		if (o.getTypeValue() < this.getTypeValue())
			return 1;
		
		return 0;
	}

	public String getType() {
		return type;
	}

	public String getPoint() {
		return point;
	}
}



