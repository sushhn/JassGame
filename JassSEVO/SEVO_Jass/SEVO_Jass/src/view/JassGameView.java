package view;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import client_server.ChatMessage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Card;
import model.JassGameModel;

/*
 * Author: Oliver
 */
public class JassGameView {

	public Stage stage;
	public Scene scene;
	public JassGameModel model;

	private int numPlayers = 0;

	private Text textPlayerScore;						//link to jassgamecontroller for scores of team 1 & 3
	private Text textPlayerScore2;						//link to jassgamecontroller for scores of team 2 & 4
	private Text textWinnerName;						//link to jasssgamecontroller for winner
	private Text txtPlayerNr;							//link to updatefromserver method and updateplayersarray						
	private Text txtTrumpf;								//?
	private Text txtNumPlayers;							//link to updatefromserver
	private Text txtTurnPlayer;							//link to updatefromserver and updateview

	private Button btnConnect;
	private Button btnNewGame;
	private Button btnRules;

	private Button[] buttons;
	private Button[] tableCards;

	private String playerNr = "0";						//link to updatefromserver

	private Text[] playersArray = new Text[4];

	public JassGameView(Stage primaryStage, JassGameModel model) {
		this.stage = primaryStage;
		this.model = model;
		// source: https://stackoverflow.com/questions/36109129/javafx-geticon
		primaryStage.getIcons().add(new Image("/resources/jass_bouchon_0.png"));
		primaryStage.setResizable(false);
	}

	public Button[] getButtons() {
		return this.buttons;
	}

	public Button[] getTableCards() {
		return this.tableCards;
	}

	public int getNumPlayers() {
		return this.numPlayers;
	}

	public Button getButtonConnect() {						//Link to the JassGameController class in the method bindUIElements
		return this.btnConnect;
	}

	public Button getButtonNewGame() {						//Link to the JassGameController class in the method bindUIElements
		return this.btnNewGame;
	}

	

	public void showView() {

		//The BorderPane is used as the main layer everything else is packed into the borderPane
		
		BorderPane bP = addBorderPane();

		this.scene = new Scene(bP, 900, 685);
		this.stage.setTitle("SEVO Jass");
		this.stage.setScene(this.scene);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // link 
																						// to css for hover and image
		this.stage.show();
	}

	// if all 4 clients are connected, it will show 4 players connected
	// on the top right of the gui
	public void updateView(int numPlayers) {

		this.numPlayers = numPlayers;
		this.txtNumPlayers.setText(new Integer(this.numPlayers).toString()); // Integer is crossed because it is maybe not more
																			// available in a new java version (depreciated?)
																			//other method to do a tostring
																			// takes the txtNumPlayers.setText("4"); from the updateFromServerMethod
	}

	public BorderPane addBorderPane() {										//Method to instatiate the borderpane


		BorderPane borderPane = new BorderPane();

		HBox hbox = addTopPane();											//Create a hbox for the top infos
		borderPane.setTop(hbox);

		VBox vBox = addRightPane();											//Vbox for the three buttons
		borderPane.setRight(vBox);

		GridPane centerPane = addCenterPane();								//Gridpane for the playedcards
		borderPane.setCenter(centerPane);

		HBox bottomPane = addBottomPane();									//Hbox at bottom for scores etc.
		borderPane.setBottom(bottomPane);

		return borderPane;
	}

	public HBox addTopPane() {												// method to instantiate the hbox

		HBox hbox = new HBox();											//Create the Hbox for inserting it above into the borderPane	
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #556B2F;");

		Text txtPlayerLabel = new Text("You are: ");					//Add text 
		txtPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtPlayerLabel.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtPlayerLabel);							//Add to Hbox

		Text txtPlayerName = new Text("Player ");						//Add preDefined "Player" text
		txtPlayerName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtPlayerName.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtPlayerName);							//Add to Hbox

		txtPlayerNr = new Text(this.playerNr);							//Link to updateFromServer Method from Visnuciry to increase the number initial value is "0"
		txtPlayerNr.setFont(Font.font("Arial", FontWeight.BOLD, 14));	// and to assign correct values and not 2 times the same number
		txtPlayerNr.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtPlayerNr);							//add to Hbox

		Text txtSeparator = new Text("  |  ");							//nice to make a visible wall
		txtSeparator.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtSeparator.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtSeparator);							//add to hbox

		Text txtTurn = new Text("It is the turn of: ");
		txtTurn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtTurn.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtTurn);

		this.txtTurnPlayer = new Text("Player 1");						//Link to updateFromServer Method from Visnuciry in the KeyFrame 2
		this.txtTurnPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		this.txtTurnPlayer.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(this.txtTurnPlayer);						//Add to Hbox

		Text txtSeparator2 = new Text("  |  ");							//nice to make a visible wall
		txtSeparator2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtSeparator2.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtSeparator2);							//add to Hbox

		Text txtNumPlayersLabel = new Text("Number of players connected: ");	//Create new text for players connected
		txtNumPlayersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtNumPlayersLabel.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtNumPlayersLabel);

		this.txtNumPlayers = new Text(new Integer(this.numPlayers).toString());	//if 4 players are connected it goes to the updateFromServer method
		this.txtNumPlayers.setFont(Font.font("Arial", FontWeight.BOLD, 14));	// and puts in a "4" to all players it does not count up and down
		this.txtNumPlayers.setFill(Color.ANTIQUEWHITE);							// txtNumPlayers.setText("4");
		hbox.getChildren().add(this.txtNumPlayers);

		return hbox;
	}

	public VBox addRightPane() {											//Method for the three buttons on the right

		VBox vbox = new VBox();												//Create the Vbox for inserting it above into the BorderPane
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(12);

		this.btnConnect = new Button("Connect");							//Create the button and link to updateFromServer method from Visnuciry						
		this.btnConnect.setPrefSize(140, 28);								//if Button is pressed it goes to the method updateFromServer and sets it disabled
		btnConnect.setFont(Font.font("Arial", FontWeight.BOLD, 15));		//Link to public Button getButtonConnect() method from above

		this.btnNewGame = new Button("Start Game");							//Goes into the method of updateFromServer from Visnuciry and looks into the ChatMessage Ready_To_Play
		this.btnNewGame.setPrefSize(140, 28);								//Player on position 0 can only press the button to Start the game and if it is pressed it gets disabled
		btnNewGame.setFont(Font.font("Arial", FontWeight.BOLD, 15));		//Link to public Button getButtonNewGame() method from above

		this.btnRules = new Button("Rules");								//Create the Rules button
		this.btnRules.setPrefSize(140, 28);
		this.btnNewGame.setDisable(true);									//Set Startgamebutton on disabled for all players
		btnRules.setFont(Font.font("Arial", FontWeight.BOLD, 15));

		// will redirect to the swisslos website with the rules
		btnRules.setOnAction(click -> {										//ActionEvent for Rules Button
			try {
				// source:
				// https://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
				Desktop.getDesktop().browse(
						new URL("https://www.swisslos.ch/de/jass/informationen/jass-regeln/jass-grundlagen.html")
								.toURI());
			} catch (Exception e) {
			}
		});

		vbox.getChildren().addAll(this.btnConnect, this.btnNewGame, this.btnRules);		//Add all three buttons to the Vbox

		return vbox;
	}

	private GridPane addCenterPane() {										//Method for the Gridpane where Cards are played

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setMaxWidth(700);
		grid.setGridLinesVisible(false);									//Was used to see how everything looks in the gridpane

		// Header in Zeile 1
		Text txtTable = new Text("Table  -  Current trumpf:");				//Text for the trumpf
		txtTable.setFont(Font.font("Arial", FontWeight.BOLD, 17));
		txtTable.setFill(Color.ANTIQUEWHITE);
		grid.add(txtTable, 0, 0);											//Add to grid

		txtTrumpf = new Text(" ");											//Blank space for the trumpf is used in the updateFromServer method from Visnuciry
		txtTrumpf.setFont(Font.font("Arial", FontWeight.BOLD, 17));
		txtTrumpf.setFill(Color.ANTIQUEWHITE);
		grid.add(txtTrumpf, 1, 0);											// add to grid

		this.playersArray[0] = new Text("     Player 1"); 					// Player 1 name
		this.playersArray[0].setFont(Font.font("Arial", FontWeight.BOLD, 12));//Link to public void updatePlayersArray method
		this.playersArray[0].setFill(Color.ANTIQUEWHITE);					//Before connect is pressed, all have as a Name Player 1 - 4
		grid.add(this.playersArray[0], 2, 7);								//Player 1 gets to 1: playerunputname

		this.playersArray[1] = new Text("     Player 2"); 					// Player 2 name
		this.playersArray[1].setFont(Font.font("Arial", FontWeight.BOLD, 12));
		this.playersArray[1].setFill(Color.ANTIQUEWHITE);
		grid.add(this.playersArray[1], 3, 5);

		this.playersArray[2] = new Text("     Player 3"); 					// Player 3 name
		this.playersArray[2].setFont(Font.font("Arial", FontWeight.BOLD, 12));
		this.playersArray[2].setFill(Color.ANTIQUEWHITE);
		grid.add(this.playersArray[2], 2, 3);

		this.playersArray[3] = new Text("     Player 4"); 					// Player 4 name
		this.playersArray[3].setFont(Font.font("Arial", FontWeight.BOLD, 12));
		this.playersArray[3].setFill(Color.ANTIQUEWHITE);
		grid.add(this.playersArray[3], 1, 5);

		tableCards = new Button[4];											//Create four buttons for the cards on the table

		ImageView iv1 = getBackImage(); // Player 1 card table				//Cardposition for Player1
		tableCards[0] = new Button();
		tableCards[0].setDisable(true);										//At the begining all cards on the table are disabled (transparent)
		tableCards[0].setOpacity(1);
		tableCards[0].setGraphic(iv1);										//Set the Backimage of the card
		grid.add(tableCards[0], 2, 6);										//Add to grid

		ImageView iv2 = getBackImage(); // Player 2 card table				//Cardposition for Player2
		tableCards[1] = new Button();
		tableCards[1].setDisable(true);										//At the begining all cards on the table are disabled (transparent)
		tableCards[1].setOpacity(1);
		tableCards[1].setGraphic(iv2);										//Set the Backimage of the card
		grid.add(tableCards[1], 3, 4);										//Add to grid

		ImageView iv3 = getBackImage(); // Player 3 card table				//Cardosition for Player3
		tableCards[2] = new Button();
		tableCards[2].setDisable(true);										//At the begining all cards on the table are disabled (transparent)
		tableCards[2].setOpacity(1);
		tableCards[2].setGraphic(iv3);										//Set the Backimage of the card
		grid.add(tableCards[2], 2, 2);										//Add to grid

		ImageView iv4 = getBackImage(); // Player 4 card table				Cardposition for Player4
		tableCards[3] = new Button();
		tableCards[3].setDisable(true);										//At the begining all cards on the table are disabled (transparent)
		tableCards[3].setOpacity(1);
		tableCards[3].setGraphic(iv4);										//Set the Backimage of the card
		grid.add(tableCards[3], 1, 4);										//Add to grid

		// Header Hand in Zeile 3
		Text txtHand = new Text("Your cards:");								//Create text for the hand cards
		txtHand.setFont(Font.font("Arial", FontWeight.BOLD, 18.5));
		txtHand.setFill(Color.ANTIQUEWHITE);
		grid.add(txtHand, 0, 8, 4, 1);										//add to grid

		// Karten auf Hand in Zeile 4
		FlowPane fp = addHandPane();
		grid.add(fp, 0, 9, 4, 1);

		return grid;
	}

	private FlowPane addHandPane() {					//Flowpane for Cards in the hand

		FlowPane flow = new FlowPane();

		flow.setPadding(new Insets(5, 0, 5, 0));
		flow.setVgap(4);
		flow.setHgap(3);
		flow.setPrefWrapLength(800); // preferred width allows for two columns
		flow.setOrientation(Orientation.VERTICAL);

		buttons = new Button[9];						//9 buttons for 9 cards in the hand

		// takes the images from the resource folder and displays them for each card
		for (int i = 0; i < 9; i++) {							//goes thorugh each of the 9 buttons and adds to each button a card

			Image imageCard = new Image(JassGameView.class.getResourceAsStream("/resources/back.png"));

			ImageView imgView = new ImageView(imageCard);		//adds the back image for each of the 9 buttons

			imgView.setFitWidth(58);
			imgView.setPreserveRatio(true);

			buttons[i] = new Button();
			buttons[i].setGraphic(imgView);
			buttons[i].setDisable(true);
			flow.getChildren().add(buttons[i]);
		}

		return flow;
	}

	private HBox addBottomPane() {									//Method to display the scores and winners of the game

		HBox hbox = new HBox();										//Hbox to put in all the informations
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(50);
		hbox.setStyle("-fx-background-color: #556B2F;");

		Text txtPlayerLabel = new Text("Player 1 & 3 score: ");		// Text to see team 1 & 3
		txtPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtPlayerLabel.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtPlayerLabel);						// add to hbox

		this.textPlayerScore = new Text("0");						// the score is set to 0 at the beginning //Link to jassgamecontroller
		textPlayerScore.setFont(Font.font("Arial", FontWeight.BOLD, 14));	//set the score
		textPlayerScore.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(this.textPlayerScore);				//add to hbox

		Text txtPlayerLabel2 = new Text("Player 2 & 4 score: ");	//Text to see team 2 & 4
		txtPlayerLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtPlayerLabel2.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtPlayerLabel2);					//add to hbox

		this.textPlayerScore2 = new Text("0");						// the score is set to 0 at the beginning //Link to jassgamecontroller
		textPlayerScore2.setFont(Font.font("Arial", FontWeight.BOLD, 14));	//Set the score
		textPlayerScore2.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(this.textPlayerScore2);				//add to hbox

		Text txtWinnerLabel = new Text("The winners are: ");		//text for the winnerteam //Link to JassGameController it calculates the winner after 9 rounds so the value can be taken and inputed
		txtWinnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		txtWinnerLabel.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(txtWinnerLabel);						//add to hbox

		this.textWinnerName = new Text("");							//Set the calculated winner from jassgamecontroller
		textWinnerName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		textWinnerName.setFill(Color.ANTIQUEWHITE);
		hbox.getChildren().add(textWinnerName);						//add to hbox

		return hbox;
	}

	private ImageView getBackImage() {								//the method to assgin the backimage

		Image imageCard = new Image(JassGameView.class.getResourceAsStream("/resources/back.png"));
		ImageView imgView = new ImageView(imageCard);

		imgView.setFitWidth(58);
		imgView.setPreserveRatio(true);

		return imgView;
	}

	// sets all images for the cards in each players hand
	public void updateCard(ArrayList<Card> cards) {

		int i = 0;

		for (Card card : cards) {
			String resourceCompleteName = "/resources/" + card.getRessourceName();
			Image imageCard = new Image(resourceCompleteName);
			ImageView imgView = new ImageView(imageCard);

			imgView.setFitWidth(58);
			imgView.setPreserveRatio(true);

			buttons[i].setGraphic(imgView);
			buttons[i].setUserData(card);
			i++;
		}
	}

	// sets all images for the cards in the middle of the table
	public void showTableCard(int position, Card card) {
		String resourceCompleteName = "/resources/" + card.getRessourceName();
		Image img = new Image(resourceCompleteName);
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(58);
		imgView.setPreserveRatio(true);

		tableCards[position].setGraphic(imgView);
	}

	/*
	 * Returns the "back" Image of the cards in the players hand if the was already
	 * played --> updates in method "playButtonPressed" at the end of it
	 */
	public void disablePlayedCard(int cardID) {

		for (int i = 0; i < buttons.length; i++) {
			Card cardSelected = (Card) buttons[i].getUserData();
			if (cardSelected.getCardID() == cardID) {

				Image imageCard = new Image(JassGameView.class.getResourceAsStream("/resources/back.png"));
				ImageView imgView = new ImageView(imageCard);

				imgView.setFitWidth(58);
				imgView.setPreserveRatio(true);

				buttons[i].setGraphic(imgView);
			}
		}
	}

	// resets the image of the played cards from the table after each round
	public void resetTableCards() {

		tableCards[0].setGraphic(getBackImage());
		tableCards[1].setGraphic(getBackImage());
		tableCards[2].setGraphic(getBackImage());
		tableCards[3].setGraphic(getBackImage());

	}

	public void setTextPlayerScore(int score) {								//Link to jassgamecontroller in evaluate roundwinner are the scores assigned
		this.textPlayerScore.setText(Integer.toString(score));				//Score is parsed from integer to a string so it can be put in in the hbox
	}

	public Text getTextPlayerScore() {			//not used?
		return this.textPlayerScore;
	}

	public void setTextwinnerName(String winner) {							//Link to jassgamecontroller after 9 rounds are played it calculates the winner
		this.textWinnerName.setText(winner);
	}

	public Text getTextWinnerName() {
		return this.textWinnerName;
	}

	public void setTextPlayerScore2(int score) {							//Link to jassgamecontroller in evaluate roundwinner are the scores assigned
		this.textPlayerScore2.setText(Integer.toString(score));				////Score is parsed from integer to a string so it can be put in in the hbox
	}

	public Text getTextPlayerScore2() {
		return this.textPlayerScore2;
	}

	public void setTrumpf(String trumpf) {
		txtTrumpf.setText(trumpf);
	}

	public Stage getStage() {
		return this.stage;
	}

	// sets the playable cards for the player
	public void updateButtonStatus(ArrayList<Card> cardList) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDisable(!cardList.get(i).isPlayable());
		}
	}

	// disables all buttons at the end of the Stich so no cards can be played if clicking very fast on it
	// KeyFrame kf0
	public void disableAllButtons() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDisable(true);
		}
	}

	// sets the chosen username and his position on the table
	public void updatePlayersArray(String[] playerNames) {

		for (int i = 0; i < this.playersArray.length; i++) {

			this.playersArray[i].setText("     " + Integer.toString(i + 1) + ": " + playerNames[i].substring(0, 10)); // only			//1 : PlayerNameinput for example it looks like 1: OliJassKing
																														// the
																														// first
																														// 10
																														// characters
																														// are
																														// displayed

			if (model.getPlayerName().equalsIgnoreCase(playerNames[i])) {												//takes the playernames from the model
				model.setPositionOfPlayer(i);																			//sets for each name the position of player 
				txtPlayerNr.setText(Integer.toString(i + 1));
			}
		}
	}

	/*
	 * Author: Visnuciry
	 *
	 */

	// Method to update client with the parameters of the current messageType and
	// number of players
	public void updateFromServer(int messageType, String playerCount) {

		// Stores the cards played during stich
		int numCardsPlayed = model.getCardsPlayedCurrentStich();

		Platform.runLater(new Runnable() {

			public void run() {

				// switch statement with the messageType as parameter, for this message --> do
				// this, break out of switch statement
				switch (messageType) {

				// Add player number to text txtPlayerNr, sets the position of the player of the
				// player as the playerCount but minus 1
				// because playerCount counts 1 untill 4 and PoitionOfPlayer counts from 0 to 3.
				// If connected, disable the "Connect" button
				case ChatMessage.CONNECTED:
					txtPlayerNr.setText(playerCount);
					model.setPositionOfPlayer(Integer.parseInt(playerCount) - 1);
					btnConnect.setDisable(true);
					break;

				/*
				 * Stores the playerCount splitted with a comma, first entry is the trumpf, 
				 * activates the "New game" button for the first player
				 */
				case ChatMessage.READY_TO_PLAY:
					// Source:
					// https://stackoverflow.com/questions/14414582/java-split-string-to-array
					String[] msgSegments = playerCount.split(",");
					model.setTrumpf(msgSegments[0]);					// sets the trumpf from the server clas where randomtrumpf is defined

					String[] playerNames = new String[4];

					for (int i = 1; i < msgSegments.length; i++) {

						String[] tmpArray = msgSegments[i].split("-");

						playerNames[i - 1] = tmpArray[1];

					}

					txtTrumpf.setText(msgSegments[0]);									//Sets the trumpf which is defined in the server

					updatePlayersArray(playerNames);

					txtNumPlayers.setText("4");

					if (model.getPositionOfPlayer() == 0) {
						btnNewGame.setDisable(false);
					}

					break;

					/*
					 * store cardlist for each player of tempArray[i] in the method getCardListFromMessage in cardsToDisplay
					 * set the stored cardlist as cards for each player
					 * update card appearance
					 * disable cards of the player with model.setPlayableCardStatus(null);
					 * update buttons which card is playable
					 * disable "New game" button
					 */
				case ChatMessage.SEND_CARDS:

					ArrayList<Card> cardsToDisplay = model.getCardListFromMessage(playerCount);
					model.setCardsOfPlayer(cardsToDisplay);
					updateCard(cardsToDisplay);
					model.setPlayableCardStatus(null);
					updateButtonStatus(model.getPlayer(model.getPositionOfPlayer()).getCards());
					btnNewGame.setDisable(true);
					break;

					/*
					 * display the cards on the table for the player which played one card and the username of the player
					 * set clicked card as tablecard
					 */
				case ChatMessage.DISPLAY_CARD:
					Card card = model.getDeck().initCardArray().get(Integer.parseInt(playerCount));
					showTableCard(model.getPositionTableCard(), card);
					tableCards[model.getPositionTableCard()].setUserData(card);
					tableCards[model.getPositionTableCard()].setDisable(false);
					tableCards[model.getPositionTableCard()].fire();
					txtTurnPlayer.setText("Player : " + Integer.toString(model.getPositionTableCard() + 1));
					break;

				default:
					break;
				}
			}
		});
		
		if (messageType != ChatMessage.DISPLAY_CARD || numCardsPlayed != 3)
			return;

		// https://docs.oracle.com/javase/8/javafx/api/javafx/animation/KeyFrame.html
		// time to disable all cards is 0sec
		final KeyFrame kf0 = new KeyFrame(Duration.seconds(0), e -> disableAllButtons());
		// time to reset the card which where played on the table 2sec
		final KeyFrame kf1 = new KeyFrame(Duration.seconds(2), e -> resetTableCards());
		// time to set the new turn of the player (winner of Stich)
		final KeyFrame kf2 = new KeyFrame(Duration.seconds(2),
				e -> txtTurnPlayer.setText("Player : " + Integer.toString(model.getWinnerOfStich() + 1)));
		// player who won the Stich can play all cards remaining in his hand
		final KeyFrame kf3 = new KeyFrame(Duration.seconds(2.1), e -> model.setPlayableCardStatus(null));
		// updates the cards remaining for the specific player and his position
		final KeyFrame kf4 = new KeyFrame(Duration.seconds(2.2),
				e -> updateButtonStatus(model.getPlayer(model.getPositionOfPlayer()).getCards()));
		// what should it do first?
		final Timeline timeline = new Timeline(kf0, kf1, kf2, kf3, kf4);

		Platform.runLater(timeline::play);

	}
}
