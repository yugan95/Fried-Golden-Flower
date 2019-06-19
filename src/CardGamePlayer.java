
public class CardGamePlayer extends PlayCard{
	private boolean ready;
	private boolean check;
	
	public CardGamePlayer(String playerID) {
		super.setPlayerID(playerID);
		this.reset();
	}
	
	public boolean isReady() {
		return this.ready;
	}
	
	public boolean isCheck() {
		return this.check;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.ready = false;
		this.check = false;
	}

	@Override
	public void followup() {
		super.followup();
		this.check = true;
	}

	@Override
	public void fold() {
		super.fold();
		this.check = true;
	}

	@Override
	public void bet(int antebets, int pairplusbets) {
		super.bet(antebets, pairplusbets);
		this.ready = true;
	}
}
