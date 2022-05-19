package model;

/**
 * 
 * @author Oliver Markovic
 *
 */
public enum Suit {

	HEARTS("H"), DIAMONDS("D"), CLUBS("C"), SPADES("S");

	private final String sign;

	Suit(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return this.sign.toString();
	}
	
}