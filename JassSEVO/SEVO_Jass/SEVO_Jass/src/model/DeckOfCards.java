package model;

import java.util.ArrayList;
import java.util.Collections;
import model.Suit;

/** Author: Oliver
 * 
 * This class represents a deck of playing cards. When initially created, the
 * deck is filled and shuffled.
 */
public class DeckOfCards {

	private ArrayList<Card> cards = new ArrayList<>();
	private int cardsRemaining = 36; 

	// We only ever have one deck of cards, so we do not set an ID attribute.
	 
	public DeckOfCards() {
		shuffle();
	}

	// Shuffles the cards, so each player gets different cards
	public void shuffle() {
	       
        this.cards = initCardArray();
        Collections.shuffle(this.cards);
       
    }
	
	// How many cards are left in the deck?
	public int getCardsRemaining() {
		return cardsRemaining;
	}

	public void setCardsRemaining(int cardsRemaining) {
		this.cardsRemaining = cardsRemaining;
	}

	
	// Gather all 36 cards, and shuffle them
	public ArrayList<Card> initCardArray() {
		
		// value of NON trumpf
		int normalvalue = 0;
		// value of trumpf 
		int trumpfvalue = 0;
		// ranking of NON trumpf 0-9 with the best card at 9
		int countvalue = 0;
		// ranking of trumpf 0-9 with the best card at 9
		int countvaluetrumpf = 0;

		// Remove all cards
		cards.clear();

		// Add all 36 cards	
		int cardCount = 0;
		for (Suit sColor : Suit.values()) {

			for (Rank rank : Rank.values()) {

				if (

				rank == Rank.Six) {
					normalvalue = 0;
					trumpfvalue = 0;
					countvalue = 1;
					countvaluetrumpf = 1;
				}

				else if (

				rank == Rank.Seven) {
					normalvalue = 0;
					trumpfvalue = 0;
					countvalue = 2;
					countvaluetrumpf = 2;
					
				} else if (

				rank == Rank.Eight) {
					normalvalue = 0;
					trumpfvalue = 0;
					countvalue = 3;
					countvaluetrumpf = 3;
					
				} else if (

				rank == Rank.Nine) {
					normalvalue = 0;
					trumpfvalue = 14;
					countvalue = 4;
					countvaluetrumpf = 8;
					
				} else if (

				rank == Rank.Ten) {
					normalvalue = 10;
					trumpfvalue = 10;
					countvalue = 5;
					countvaluetrumpf = 4;
					
				} else if (

				rank == Rank.Jack) {
					normalvalue = 2;
					trumpfvalue = 20;
					countvalue = 6;
					countvaluetrumpf = 9;
					
				} else if (

				rank == Rank.Queen) {
					normalvalue = 3;
					trumpfvalue = 3;
					countvalue = 7;
					countvaluetrumpf = 5;
					
				} else if (

				rank == Rank.King) {
					normalvalue = 4;
					trumpfvalue = 4;
					countvalue = 8;
					countvaluetrumpf = 6;
					
				} else if (

				rank == Rank.Ace) {
					normalvalue = 11;
					trumpfvalue = 11;
					countvalue = 9;
					countvaluetrumpf = 7;
				}

				Card card = new Card(sColor, rank, normalvalue, trumpfvalue, cardCount, countvalue, countvaluetrumpf);
				cards.add(card);
				cardCount++;
			}
		}
		return cards;
	}

	
	public Card dealCard() {
		Card card = (cards.size() > 0) ? cards.remove(cards.size() - 1) : null;
		return card;
	}

	
	
}
