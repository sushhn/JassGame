package model;

public enum Suit {

	HEARTS("hearts"), DIAMONDS("diamonds"), CLUBS("clubs"), SPADES("spades");

	private final String sign;

	Suit(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return this.sign.toString();
	}
	
}