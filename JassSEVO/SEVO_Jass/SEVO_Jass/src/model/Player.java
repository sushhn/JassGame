package model;


import java.util.ArrayList;

 	/*
 	 * Author: Egzon
 	 */
public class Player {
	
	public static final int HAND_SIZE = 9;
	private final String playerName; // This is the ID
	private boolean isConnected = false;

	private  ArrayList<Card> cards = new ArrayList<>();
	private ArrayList<Card> playedCards = new ArrayList<>();
	

	public Player(String userName) {
		this.playerName = userName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void setCards(ArrayList<Card> cardList) {
		this.cards = cardList;
	}

	public void addCard(Card card) {
		if (cards.size() < HAND_SIZE)
			cards.add(card);
	}

	public void discardHand() {
		cards.clear();
	}

	public int getNumCards() {
		return cards.size();
	}
	 public void setIsConnected(boolean isConnected) {
		 this.isConnected = isConnected;
	 }

	 public boolean getIsConnected() {
		 return isConnected;
	 }
	
	 
	
	/* Takes parameters from "setDisabledCards"
	 * If first card in new Stich is trumpf, this method will check which cards are legal to play
	 * 
	 * Author: Egzon & Sascha
	 */
	
	public void setDisabledCardsTrumpfRound(String trumpf, int startPosition, Card stichCard) {
		
		boolean isTrumpfJack = false;
		
		for (int i = startPosition; i< cards.size(); i++) {
			
			Card currentCard = cards.get(i);
			
			// card was already played -> ++ in loop
			if (cardWasPlayed(currentCard.getCardID())) {
				continue;
			}
			
			currentCard.setPlayable(true);
			
			// will check if there is a trumpf Jack in the players hand
			if(currentCard.getRank() == Rank.Jack && currentCard.getSuit().toString().equalsIgnoreCase(trumpf)) {
				isTrumpfJack = true;
			}
			
			// if card is not a trumpf, it will be disabled to play
			if (!currentCard.getSuit().toString().equalsIgnoreCase(trumpf)) {
				currentCard.setPlayable(false);
			}
		}
		
		// all cards were disabled except the trumpf -> set all all to playable
		if (allCardsDisabled(isTrumpfJack)) {
			
			this.disableCards(false);
		}
	}
	
	/* if first card in new Stich is non trumpf, this method will check wich cards are legal to play.
	 * same principle as getDisabledCardsTrumpfRound
	 * 
	 * Author: Egzon & Sascha
	 */
	public void setDisabledCardsNormalRound(String trumpf, int startPosition, Card stichCard) {
		
		boolean isTrumpfJack = false;
		int countTrumpfCards = 0;
		
		for (int i = startPosition; i< cards.size(); i++) {
			
			Card currentCard = cards.get(i);
			
			if (cardWasPlayed(currentCard.getCardID())) {
				
				continue;
			}
			
			currentCard.setPlayable(true);
			
			// will check if there is a trumpf Jack in the players hand
			if(currentCard.getRank() == Rank.Jack && currentCard.getSuit().toString().equalsIgnoreCase(trumpf)) {
					isTrumpfJack = true;
			}
			
			// counts the trumpf card in players hand to evaluate if he can't play anything besides trumpf
			if (currentCard.getSuit().toString().equalsIgnoreCase(trumpf)){
				countTrumpfCards++;
			}
		 
			if (currentCard.getSuit().toString() == stichCard.getSuit().toString()) {
				// do nothing
				
			} else {
				
				// adds cards with Suit different as randomTrumpfChosen to the disabledCards
				if (!currentCard.getSuit().toString().equalsIgnoreCase(trumpf)) {
					
					currentCard.setPlayable(false);
				}				
			}
		}
		
		if (cardsEnableStichSuit(countTrumpfCards)) {
			this.disableCards(false);
		}
			
		if (allCardsDisabled(isTrumpfJack)) {
			this.disableCards(false);
		}
	}
	
	/* Takes parameter from method "setPlayableCardStatus" in JassGameModel.
	 * loops through all cards of the chosen player
	 * if the card was already played -> disable to play them
	 * if not played -> goes into method "setDisabledCards"
	 * 
	 */
	public void setCardsStatus (Card stichCard, String trumpf, int winnerOfStich, int positionTableCard) {
		
		for (Card card: this.cards) {
			
			if (this.cardWasPlayed(card.getCardID()) == true) {
				
				card.setPlayable(false);
			}
			else {
				
				setDisabledCards(stichCard, trumpf, winnerOfStich, positionTableCard);
			}
		}
	}
	
	// disables the cards which are not legal to play
	// or enables them if "isTrumpf" or "isTrumpfJack" is true
	//							     (false)
	public void disableCards(boolean disable) {
		
		for (Card card: this.cards) {
			
			//                 (true)
			card.setPlayable(!disable);
			
			if (cardWasPlayed(card.getCardID())) {
				
				card.setPlayable(false);
			}
			
		}
	}
	
	// adds the played cards to the ArrayList<Card> playedCards for each player
	public void addPlayedCard (Card card) {
		this.playedCards.add(card);
	}
	
	// checks if the card was already played
	private boolean cardWasPlayed (int cardId) {
		
		for (Card card: this.playedCards) {
			
			if (card.getCardID() == cardId) return true;
		}
		
		return false;
	}
	
	// (in a non Trumpf round) checks if cards are not playable, except the Trumpf cards
	private boolean cardsEnableStichSuit (int countTrumpf) {
		
		int disabledCards = 0;
		
		for (Card card: this.cards) {
			
			if (!card.isPlayable()) disabledCards++;
		}
		
		if (disabledCards == cards.size() - countTrumpf) {
				return true;
			}
			else {
				return false;
			}
	}
	
	// (in a Trumpf round) checks if no card is playable or only the Jack trumpf
	private boolean allCardsDisabled (boolean trumpfJack) {
		
		int disabledCards = 0;
		
		for (Card card: this.cards) {
			
			if (!card.isPlayable()) disabledCards++;
		}
		
		// all cards disabled except trumpf Jack -> enable all to play
		if (trumpfJack) {
			if (disabledCards == cards.size() - 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			// all cards disable -> enable them to play
			if (disabledCards == cards.size()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	
	/* Takes parameters from method "setCardStatus"
	 * If the stichCard (first selected card in the Stich) is a trumpf, it will go into
	 * method "setDisabledCardsTrumpfRound".
	 * else (non trumpf stichCard) -> setDisabledCardsNormalRound
	 */
	private void setDisabledCards (Card stichCard, String trumpf, int winnerOfStich, int positionTableCard) {
		
		// if stichCard is trumpf
		if (trumpf.equalsIgnoreCase(stichCard.getSuit().toString())) {

			this.setDisabledCardsTrumpfRound(trumpf, 0, stichCard);
		}
		// if stichCard is NON trumpf
		else {

			this.setDisabledCardsNormalRound(trumpf, 0, stichCard);
		}
	}
}
