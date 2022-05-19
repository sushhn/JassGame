package model;

import java.util.ArrayList;

public class JassGameModel {
	
	private int positionTableCard = 0;
	private int winnersPosition = 0;
	private int winnerOfStich = 0;
	private int positionOfPlayer=0;
	private int cardsPlayedCurrentStich = 0;
	
	private String trumpf;
	private String playerName;
	
	private Card currentStich=null;
	
	private DeckOfCards deck;
	
	// Creates fixed amount of 4 players
	Player[] players = { 
			new Player("Player 1"), 
			new Player("Player 2"), 
			new Player("Player 3"),
			new Player("Player 4")
			};

	/**
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	// Creates new deck of cards
	public JassGameModel() {
		deck = new DeckOfCards();
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	public DeckOfCards getDeck() {
		return deck;
	}

	
	/* Will be used for the client/server that the cards will be
	 * distributed correctly to each player.
	 * 
	 * Author: Visnuciry
	 */
	public String getDistributedCardsMessage () {
    
    StringBuffer sbMessage = new StringBuffer();
   
    for (int i=0; i < players.length; i++) {
       
        for (Card card: players[i].getCards()) {
           
            sbMessage.append(card.getCardID());
            sbMessage.append(",");
        }
       
        sbMessage.append("=");
    }
    return sbMessage.toString();
	}
	
	
	/* cardlist for each player of tempArray[i]
	 * 
	 * Author: Visnuciry
	 */
	public ArrayList<Card> getCardListFromMessage (String message) {
		   
        ArrayList<Card> playerCardList = new ArrayList<>();
        ArrayList<Card> completeCardList = deck.initCardArray();
       
        String [] tempArray = message.split(",");
       
        for (int i=0; i < tempArray.length; i++) {
           
            Card currentCard = completeCardList.get(Integer.parseInt(tempArray[i]));
            playerCardList.add(currentCard);
        }
       
        return playerCardList;
    }
	
	
	/* ______________________________________
	 * From here
	 * Author: Sascha 
	 */
	
	/* PositionTableCard is the position for each player from 0-3
	 * Player 1 = 0, Player 2 = 1, Player 3 = 2, Player 4 = 3.
	 */
	public void setPositionTableCard(int positionTableCard) {
		this.positionTableCard = positionTableCard;
	}
	
	public int getPositionTableCard() {
		return positionTableCard;
	}
	
	
	/* The winnersTurn is the current winning card/position of the Stich 
	 */
	public void setWinnersPosition(int winnersPosition) {
		this.winnersPosition = winnersPosition;
		
	}
	public int getWinnersPosition() {
		return winnersPosition;
	}

	
	/* If everyone played a card in the Stich
	 * the position will reset to 0
	 */
	public void updatePositionTableCard() {
		this.positionTableCard++;
		if(this.positionTableCard == 4) {
			this.positionTableCard = 0;
		}
	}
	
	// sets the cards for each player and sends it to the server/client
	public void setCardsOfPlayer(ArrayList<Card> cardList) {
		this.getPlayers()[this.positionOfPlayer].setCards(cardList);
	}
	
	/* The winner of the Stich will be set as the new positionTableCard
	 * which will be 0. 
	 */
	public void setWinnerOfStich(int winnerOfStich) {
		this.winnerOfStich = winnerOfStich;
		this.positionTableCard = winnerOfStich;
	}

	public int getWinnerOfStich() {
		return winnerOfStich;
	}
	
	/* Will set the randomTrumpf from Server 
	 */
	public void setTrumpf(String trumpf) {
		this.trumpf = trumpf;
	}

	public String getTrumpf() {
		return trumpf;
	}
	
	
	/* Position of player is + positionOfPlayer
	 * Player 1 = pos 0
	 * Player 2 = pos 1
	 * Player 3 = pos 2
	 * Player 4 = pos 3 
	 */
	public void setPositionOfPlayer(int positionOfPlayer) {
		this.positionOfPlayer = positionOfPlayer;
	}

	public int getPositionOfPlayer() {
		return positionOfPlayer;
	}

	
	/* Loops through the players and will check for the player who's turn is now
	 * All other player's cards will be disabled.
	 * Takes parameter stichCard of method "playButtonPressed"
	 * 
	 * Author: Egzon
	 */
	public void setPlayableCardStatus (Card stichCard) {
	
		for (int i=0; i < players.length; i++) {
			
			if (i == this.positionTableCard) {	
				
				// it's the correct player's turn, check which cards are legal to play
				if (stichCard != null) {
				
					players[i].setCardsStatus(stichCard, trumpf, winnerOfStich, positionTableCard);
				}
				else {
					players[i].disableCards(false);
				}
			} 
			else {
			
				// not players turn -> disable all cards
				players[i].disableCards(true);
			}
		}
	}
	
	public int getCardsPlayedCurrentStich () {
		return this.cardsPlayedCurrentStich;
	}
	
	public void resetCardsPlayedCurrentStich() {
		this.cardsPlayedCurrentStich = 0;
	}
	public void updateCardsPlayedCurrentStich () {
		this.cardsPlayedCurrentStich++;
	}

	public Card getCurrentStich() {
		return currentStich;
	}

	public void setCurrentStich(Card currentStich) {
		this.currentStich = currentStich;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
