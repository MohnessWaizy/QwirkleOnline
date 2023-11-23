package controller;

import java.util.ArrayList;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * game panel to display a not started game in the lobby GUI
 *
 */
public class GamePanelNotStartedGame extends AnchorPane {

	/**
	 * Label to hold the game id
	 */
	private Label label_gameID;

	/**
	 * Label to hold the game name
	 */
	private Label label_gameName;

	/**
	 * Label to hold the amount of tiles per player
	 */
	private Label label_cardsPerPlayer;

	/**
	 * Label to hold the maximum amount of players
	 */
	private Label lable_numberOfPlayers;

	/**
	 * Button to hold the configuration
	 */
	private Button Button_configWheel;

	/**
	 * Button to join a game
	 */
	private Button label_joinButton;

	/**
	 * Integer holds game id
	 */
	private int gameID;

	/**
	 * String holds game name
	 */
	private String gameName;

	/**
	 * Configuration holds game configuration
	 */
	private Configuration configuration;

	/**
	 * LobbySceneController holds reference
	 */
	private LobbySceneController controller;

	/**
	 * ArrayList of Client in the game
	 */
	private ArrayList<Client> playerList;

	/**
	 * Game holds game information
	 */
	private Game game;

	/**
	 * Constructor
	 * 
	 * @param game
	 * @param controller
	 */
	public GamePanelNotStartedGame(Game game, LobbySceneController controller) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(50);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(50);
		this.setStyle("-fx-background-color: #6B7074;");
		this.controller = controller;
		this.gameID = game.getGameId();
		this.configuration = game.getConfig();
		this.gameName = game.getGameName();
		this.playerList = (ArrayList<Client>) game.getPlayers();
		this.game = game;

		createGameID(gameID);
		createGameName(gameName);
		createCardsPerPlayer(configuration.getMaxHandTiles());
		createNumberOfPlayers(configuration.getMaxPlayerNumber());
		createConfigWheel(configuration);
		createJoinButton(gameID);
	}

	/**
	 * Creates label_gameID in the game panel
	 * 
	 * @param gameID
	 */
	private void createGameID(int gameID) {
		label_gameID = new Label();
		label_gameID.setMinWidth(USE_COMPUTED_SIZE);
		label_gameID.setMinHeight(USE_COMPUTED_SIZE);
		label_gameID.setPrefWidth(USE_COMPUTED_SIZE);
		label_gameID.setPrefHeight(USE_COMPUTED_SIZE);
		label_gameID.setMaxWidth(USE_COMPUTED_SIZE);
		label_gameID.setMaxHeight(USE_COMPUTED_SIZE);
		label_gameID.setText(Integer.toString(gameID));
		label_gameID.setId("label_gameID");

		this.getChildren().add(label_gameID);
		this.setLeftAnchor(label_gameID, 4.0);
		this.setRightAnchor(label_gameID, 0.0);
		this.setTopAnchor(label_gameID, 0.0);
		this.setBottomAnchor(label_gameID, 0.0);
	}

	/**
	 * Creates label_gameName in the game panel
	 * 
	 * @param gameName
	 */
	private void createGameName(String gameName) {
		label_gameName = new Label();
		label_gameName.setMinWidth(USE_COMPUTED_SIZE);
		label_gameName.setMinHeight(USE_COMPUTED_SIZE);
		label_gameName.setPrefWidth(USE_COMPUTED_SIZE);
		label_gameName.setPrefHeight(USE_COMPUTED_SIZE);
		label_gameName.setMaxWidth(USE_COMPUTED_SIZE);
		label_gameName.setMaxHeight(USE_COMPUTED_SIZE);
		label_gameName.setText(gameName);
		label_gameName.setId("label_gameID");

		this.getChildren().add(label_gameName);
		this.setLeftAnchor(label_gameName, 50.0);
		this.setRightAnchor(label_gameName, 0.0);
		this.setTopAnchor(label_gameName, 0.0);
		this.setBottomAnchor(label_gameName, 0.0);
	}

	/**
	 * Creates label_cardsPerPlayer in the game panel
	 * 
	 * @param cards
	 */
	private void createCardsPerPlayer(int cards) {
		label_cardsPerPlayer = new Label();
		label_cardsPerPlayer.setMinWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMinHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setPrefWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setPrefHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMaxWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMaxHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setText("Steine pro Spieler: " + Integer.toString(cards));

		this.getChildren().add(label_cardsPerPlayer);
		this.setLeftAnchor(label_cardsPerPlayer, 250.0);
		this.setTopAnchor(label_cardsPerPlayer, 0.0);
		this.setBottomAnchor(label_cardsPerPlayer, 0.0);
	}

	/**
	 * Creates lable_numberOfPlayers in the game panel
	 * 
	 * @param numPlayers
	 */
	private void createNumberOfPlayers(int numPlayers) {
		lable_numberOfPlayers = new Label();
		lable_numberOfPlayers.setMinWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMinHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setPrefWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setPrefHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMaxWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMaxHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setText("Maximale Anzahl Spieler: " + Integer.toString(numPlayers));

		this.getChildren().add(lable_numberOfPlayers);
		this.setLeftAnchor(lable_numberOfPlayers, 400.0);
		this.setTopAnchor(lable_numberOfPlayers, 0.0);
		this.setBottomAnchor(lable_numberOfPlayers, 0.0);
	}

	/**
	 * Creates Button_configWheel in the game panel
	 * 
	 * @param config
	 */
	private void createConfigWheel(Configuration config) {
		Button_configWheel = new Button();
		Button_configWheel.setMinWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMinHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setPrefWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setPrefHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setMaxWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMaxHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setText("i");
		Button_configWheel.setId("lable_infoGamepanel");
		Button_configWheel.setOnMouseClicked(action -> {
			controller.showConfigNotStarted(gameID);
		});

		this.getChildren().add(Button_configWheel);
		this.setLeftAnchor(Button_configWheel, 600.0);
		this.setTopAnchor(Button_configWheel, 4.0);
		this.setBottomAnchor(Button_configWheel, 4.0);
	}

	/**
	 * Creates label_joinButton in the game panel
	 * 
	 * @param gameID
	 */
	private void createJoinButton(int gameID) {
		label_joinButton = new Button();
		label_joinButton.setMinWidth(100);
		label_joinButton.setMinHeight(USE_COMPUTED_SIZE);
		label_joinButton.setPrefWidth(USE_COMPUTED_SIZE);
		label_joinButton.setPrefHeight(USE_COMPUTED_SIZE);
		label_joinButton.setMaxWidth(USE_COMPUTED_SIZE);
		label_joinButton.setMaxHeight(USE_COMPUTED_SIZE);
		label_joinButton.setText("Beitreten");
		label_joinButton.setId("button_join");
		label_joinButton.setOnAction(action -> {
			controller.joinGameRequest(gameID);
		});

		this.getChildren().add(label_joinButton);
		this.setRightAnchor(label_joinButton, 4.0);
		this.setTopAnchor(label_joinButton, 4.0);
		this.setBottomAnchor(label_joinButton, 5.0);
	}

	/**
	 * @return text of label_gameID
	 */
	public String getLableGameID() {
		return label_gameID.getText();
	}

	/**
	 * @return game id
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * @param gameID
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * @return game configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return game name
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @return client list in the game
	 */
	public ArrayList<Client> getPlayerList() {
		return playerList;
	}

	/**
	 * @param playerList
	 */
	public void setPlayerList(ArrayList<Client> playerList) {
		this.playerList = playerList;
	}

	/**
	 * @return game
	 */
	public Game getGame() {
		return game;
	}

}
