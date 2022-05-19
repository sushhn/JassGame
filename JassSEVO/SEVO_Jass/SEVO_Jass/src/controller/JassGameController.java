package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import client_server.ChatMessage;
import client_server.Client;
import model.Card;
import model.DeckOfCards;
import model.JassGameModel;
import model.Player;
import view.JassGameView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class JassGameController {

	private JassGameModel model;
	private JassGameView view;

	private int score = 0;
	private int countingRounds = 0;
	private int scoreTeam1;
	private int scoreTeam2;
	
	private Card stichCard=null;
	private Card winningCard = null;
	
	private Client client;
	
	
	public JassGameController(JassGameModel model, JassGameView view) {
		
		this.model = model;
		this.view = view;
	}

	/* If the specific Button is pressed, it will start the Event of the Button
	 * 
	 * Author: Oliver
	 */
	public void bindUIEelements() {
		
		this.view.getButtonConnect().setOnAction(e -> connectToServer());			//Link to JassGameView | 
		this.view.getButtonNewGame().setOnAction(e -> updateCardView());			//Link to JassGameView | 
		this.view.getStage().setOnHidden(e -> disconnectClient());

		Button[] tableCards = view.getTableCards();
		for (int i = 0; i < tableCards.length; i++) {
			tableCards[i].setOnAction(e -> playButtonPressed(e));
		}
		
		Button[] buttons = view.getButtons();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnAction(e -> sendDisplayCardMessage(e));
		}
	}
	
	
	 /* Deal each player 9 cards
	  * Source: Basic concept was from the Poker project, adapted to our own Jass Game
	  * Author: Oliver
	  */
	private void deal() {

		int cardsRequired = model.getPlayers().length * Player.HAND_SIZE;
		DeckOfCards deck = model.getDeck();
		if (cardsRequired <= deck.getCardsRemaining()) {
			for (int i = 0; i < model.getPlayers().length; i++) {
				Player p = model.getPlayer(i);
				p.discardHand();
				for (int j = 0; j < Player.HAND_SIZE; j++) {
					Card card = deck.dealCard();
					p.addCard(card);
				}
			}
		}
	}
	
	/* If the user closes his Game window, the client will be disconnected.
	 * If the user doesn't input a name or more than 9 characters for the name, he will be disconnected too
	 * 
	 * Author: Visnuciry 
	 */
	private void disconnectClient () {
		
        if (this.client == null || this.client != null ) {
        	
            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
        }
    }
	
	
	/* If the user clicks the connect Button, he will be asked to input his name.
	 * After successful input, the user will be connected to the game.
	 * 
	 * Author: Visnuciry 
	 */
	private void connectToServer() {
         
	        if(client==null) {
	        	
	            //resource: http://www.java2s.com/Tutorial/Java/0240__Swing/SettingJOptionPanebuttonlabelstoFrench.htm
	            UIManager.put("OptionPane.cancelButtonText", "Cancel");
	            UIManager.put("OptionPane.okButtonText", "Confirm");
	            
	            ImageIcon icon = new ImageIcon("jass_bouchon_0.png");
	            ImageIcon warning = new ImageIcon("warning.png");
	            // resource: https://stackoverflow.com/questions/438463/joptionpane-wont-show-its-dialog-on-top-of-other-windows
	            final JDialog dialog = new JDialog();
	            dialog.setAlwaysOnTop(true);  
	            
	            String response = (String) JOptionPane.showInputDialog(dialog,  "Please enter your name!\n",
	                    "Connect to SEVO Jass Game", JOptionPane.INFORMATION_MESSAGE, icon, null, "");
	            String checkName = response;
	            
	            if(checkName == null || checkName.isEmpty()) {
	            	JOptionPane.showMessageDialog(null, "You did not enter a name!\nYou were not connected.", "Warning!", JOptionPane.INFORMATION_MESSAGE, warning);
	            	disconnectClient();
	             }
	            
	            if(checkName.length()>9) {
	            	JOptionPane.showMessageDialog(null, "Please enter a name with less than 10 characters.", "Warning!", JOptionPane.INFORMATION_MESSAGE, warning);
	            	disconnectClient();
	            }
	            // 3 different random from 0-1000 to make sure that no user has the same name
	            Random randomSafe = new Random();
	            int safe = randomSafe.nextInt(1000) + 1;
	            int safer = randomSafe.nextInt(1000) + 1;
	            int safest = randomSafe.nextInt(1000) + 1;
	            
	            String addToName = "              " + Integer.toString(safe) + Integer.toString(safer)+ Integer.toString(safest);
	            String name = checkName + addToName;
	            
	            
	            model.setPlayerName(name);  // set the personal chosen name of the player
	      
	          
	        String hostName = "localhost";
	             
	            try {
	                InetAddress addr = InetAddress.getLocalHost();
	                hostName = addr.getHostAddress();
	            } catch (UnknownHostException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	         
	            this.client = new Client(hostName, 1500, name, this.view);
	          
	            if (!this.client.start())
	                return;
	        }
	        view.updateView(1);
	    }

	
	/* If 4 users are connected to the game, the first player can press the "Start Game" Button.
	 * This will distribute 9 cards to each player in the game.
	 * 
	 * Author: Visnuciry
	 */
	
	private void updateCardView() {
		//waits until 4 players are connected
	        deal();
	       
	        String cardsMessage = model.getDistributedCardsMessage();
	       
	        if (client != null) {
	            client.sendMessage(new ChatMessage(ChatMessage.DISTRIBUTE_CARDS, cardsMessage));
	        }	     
	    }
	
	
	/* If a card was clicked, this will send the message to display it
	 * 
	 * Author: Visnuciry
	 */
	private void sendDisplayCardMessage(ActionEvent e) {
		Button btnPressed = (Button) e.getSource();
		Card cardSelected = (Card) btnPressed.getUserData();
		
		if(client != null) {
			client.sendMessage(new ChatMessage(ChatMessage.DISPLAY_CARD, Integer.toString(cardSelected.getCardID())));
		}
	}
	
	
	/* The current player to play a card has to click on the card Button in his hand.
	 * It will show only legal playable cards to the user.
	 * The method will evaluate the value of the Card (trumpf oder non trumpf value).
	 * The position of the winner will be stored for each Stich and will be turned into 
	 * the winner of Stich at the end of the Stich.
	 * It sets the winner of Stich as the first player to play a card in the new Stich.
	 * 
	 * Author: Sascha	
	 */
	
	private void playButtonPressed(ActionEvent e) {


		// counts how many cards were played during each stich
		model.updateCardsPlayedCurrentStich();
		
		// disables the table cards
		int currentPosition = model.getPositionTableCard();
		view.getTableCards()[currentPosition].setDisable(true);
		
		int playerPosition = model.getPositionOfPlayer();

		Button btnPressed = (Button) e.getSource();
		Card cardSelected = (Card) btnPressed.getUserData();

		/* the first played card in each Stich round will be
		 * the "stichCard" and will be used to compare the next
		 * cards to it (legal moves)
		 */
		if(model.getCardsPlayedCurrentStich() == 1) {
			model.setCurrentStich(cardSelected);
			stichCard = model.getCurrentStich();
		}
		
		// adds the played cards of each player 
		model.getPlayer(playerPosition).addPlayedCard(cardSelected);

		// will turn the first played card into the winning Card
		if (winningCard == null) {
			winningCard = cardSelected;
			model.setWinnersPosition(model.getPositionTableCard());

		} else {

			/*
			 * if winningCard != null, this will check if the selected Card is better than
			 * the winningCard (method "compareTo" in class "Card". if compareTo returns 1
			 * --> the selected Card will be the new winning Card --> the position of the
			 * current winner is this card Position.
			 */
			if (winningCard.compareTo(cardSelected, model.getTrumpf(), stichCard) > 0) {
				winningCard = cardSelected;
				model.setWinnersPosition(model.getPositionTableCard());
			}
		}

		// checks if the selected Card is a trumpf and returns the trumpf value of the
		// Card (takes the values of class "DeckOfCards")

		if (model.getTrumpf().equalsIgnoreCase(cardSelected.getSuit().toString())) {

			cardSelected.gettrumpfvalue();
			score += cardSelected.gettrumpfvalue();
		}
		
		// if non trumpf, it will return the normal value of the Card
		else {
			cardSelected.getnormalvalue();
			score += cardSelected.getnormalvalue();
		}

		// adds +1 for each round until positionTableCard is 4
		// --> reset to 0
		model.updatePositionTableCard();
		
		// uses method on line 311 to check who won the stich round
		evaluateRoundWinner();
		
		// if played cards in this Stich round is less than 4 then
		// it will check for legal moves for each player
		// Author: Egzon & Sascha
		if (model.getCardsPlayedCurrentStich() < 4) {
		  model.setPlayableCardStatus(stichCard);
		  view.updateButtonStatus(model.getPlayer(model.getPositionOfPlayer()).getCards());
		}
		
		// The winner after 9 Stich rounds will be displayed at the bottom right of the Game window.
		if (countingRounds == 9) {
			if (scoreTeam1 > scoreTeam2) {
				String winner1 = "Player 1 & 3!";
				view.setTextwinnerName(winner1);
			} else {
				String winner2 = "Player 2 & 4";
				view.setTextwinnerName(winner2);
			}
			
			ImageIcon joker = new ImageIcon("joker.png");
			JOptionPane.showMessageDialog(null, "Thanks for playing SEVO Jass!\n"
					+ "The results are at the bottom.", "The End!", 
					JOptionPane.INFORMATION_MESSAGE, joker);
		}
		
		view.disablePlayedCard(cardSelected.getCardID());
	}
	
	
	/* Evaluates for each Stich who has the current winning position/card.
	 * This team will be granted the Stich score and will be added to
	 * their current total score.
	 * At the last round, the winning Stich team will get an additional
	 * 5 points. 
	 * 
	 * Author: Sascha
	 */
	private void evaluateRoundWinner() {
		
		if(model.getCardsPlayedCurrentStich() != 4) {
			return;
		}
		
		// resets the count of played cards to 0 after each stich round
		model.resetCardsPlayedCurrentStich();
		
		countingRounds++;
			
			// if winner is Player 1 or Player 3
			if(model.getWinnersPosition() == 0 || model.getWinnersPosition() == 2) {
				
				// checks if they are in the last Stich (Round 9)
				if(countingRounds == 9) {
					
					scoreTeam1+= score + 5;
					view.setTextPlayerScore(scoreTeam1);
					
				} else {
					scoreTeam1 = (scoreTeam1 + score);
					view.setTextPlayerScore(scoreTeam1);
				
					score=0;	// reset score after score granted to winning round team
				}
			}
			
			// if winner is Player 2 or Player 4
			if(model.getWinnersPosition() == 1 || model.getWinnersPosition() == 3) {
			
				// checks if they are in the last Stich (Round 9)
				if (countingRounds == 9){
					
					scoreTeam2+= score + 5;
					view.setTextPlayerScore2(scoreTeam2);
							
				} else {
					scoreTeam2 = (scoreTeam2 + score);
					view.setTextPlayerScore2(scoreTeam2);
				
					score=0;	// reset score after score granted to winning round team
				}
			}
			
			// the new winner of the Stich is the current position of winner
			model.setWinnerOfStich(model.getWinnersPosition());
			
			winningCard=null;	// reset value of winningCard after each round
			
	}
	
}
