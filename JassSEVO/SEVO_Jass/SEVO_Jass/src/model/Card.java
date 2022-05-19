package model;

// Codepart: Oliver Markovic

///https://gitlab.fhnw.ch/bradley.richards/java-projects/-/blob/master/src/poker/version_graphics/model/Card.java
public class Card {

	private final Suit suit;
	private final Rank rank;
	private final int normalvalue;
	private final int trumpfvalue;
	private final int countvalue;
	private final int countvaluetrumpf;
	private final String ressourceName;
	private boolean playable;
	private final int cardID;

	public Card(Suit suit, Rank rank, int normalvalue, int trumpfvalue, int cardID, int countvalue,
			int countvaluetrumpf) {

		this.countvalue = countvalue;
		this.countvaluetrumpf = countvaluetrumpf;
		this.suit = suit;
		this.rank = rank;
		this.normalvalue = normalvalue;
		this.trumpfvalue = trumpfvalue;
		this.cardID = cardID;
		this.playable = false;
		
		this.ressourceName = rank.toString() + "_of_" + suit.toString() + ".png"; // concatinated
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public int getnormalvalue() {
		return normalvalue;
	}

	public int gettrumpfvalue() {
		return trumpfvalue;
	}

	public String getRessourceName() {
		return ressourceName;
	}

	public int getCardID() {
		return cardID;
	}

	@Override //
	public String toString() {
		return rank.toString() + suit.toString();
	}
	
	public boolean isPlayable() {
		return playable;
	}
	
	public void setPlayable (boolean playable) {
		this.playable = playable;
	}

	public int getValue(Suit trumpf) {
		if (this.suit == trumpf) {
			return this.trumpfvalue;

		} else {
			return normalvalue;
		}

	}

	/* Method to compare the winning card with the new selected card on the table.
	 * If the card is better than the current winning card, it will return 1
	 * else it will return 0.
	 * 
	 * Author: Sascha
	 */
	public int compareTo(Card selected, String trumpf, Card stichCard) {
		// checks if winning card is trumpf
		if (this.suit.toString().equalsIgnoreCase(trumpf)) { // equalsIgnore schaut nicht mehr auf gross oder kleinschreibung
															
			// checks if selected Card is trumpf
			if (selected.suit.toString().equalsIgnoreCase(trumpf)) {
				// if selected TrumpfCard > winning TrumpfCard
				if (selected.countvaluetrumpf > this.countvaluetrumpf) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		// if winning card isn't trumpf
		} else {
			// winning card wasn't a trumpfCard
			// checks if selected Card is a trumpf
			if (selected.suit.toString().equalsIgnoreCase(trumpf)) {
				return 1;
				
			// winning card wasn't a trumpfCard but is Stich for current round
			} else if (selected.suit != stichCard.suit) {
				return 0;
				
			} else if (selected.suit == stichCard.suit) {
				if (selected.countvalue > this.countvalue) {
					return 1;
				} else {
					return 0;
				}
				
			}
			else {
				return 0;
			}
		}
	}
}
