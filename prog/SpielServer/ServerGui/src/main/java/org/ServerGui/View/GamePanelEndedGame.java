package org.ServerGui.View;

import org.ServerGui.Controller.LobbySceneController;

import de.upb.swtpra1819interface.models.Configuration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels containing the game's id, the game's name, possibilty
 * to look up configuration and the winner's name for an ended game.
 * </p>
 */

public class GamePanelEndedGame extends AnchorPane {

	private Label label_gameID;
	private Label label_gameName;
	private Label label_winner;
	private Button button_configWheel;
	private LobbySceneController controller;
	private int gameID;
	private Configuration configuration;
	private String gameName;
	private String gameWinner;

	/**
	 * Constructs a panel with the game's id, the game's name, possibilty to look up
	 * game's configuration and the winner's name.
	 * 
	 * @param gameID
	 *            id of the game represented by panel
	 * @param gameName
	 *            name of the game represented by panel
	 * @param gameWinner
	 *            winner of the game represented by panel
	 * @param config
	 *            configuration of the game represented by panel
	 * @param controller
	 *            LobbySceneController of the Lobby the panel will be added to
	 */
	public GamePanelEndedGame(int gameID, String gameName, String gameWinner, Configuration config,
			LobbySceneController controller) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(50);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(50);
		this.setStyle("-fx-background-color: #6B7074;");
		this.controller = controller;
		this.gameID = gameID;
		this.configuration = config;
		this.gameName = gameName;
		this.gameWinner = gameWinner;

		createGameID(gameID);
		createGameName(gameName);
		createConfigWheel(config);
		createWinner();
	}

	/**
	 * Creates a label with the game's id.
	 * 
	 * @param gameID
	 *            the game's id
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
	 * Creates a label with the game's name.
	 * 
	 * @param gameName
	 *            the game's name
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
	 * Creates a label with the winner's name.
	 * 
	 * @param tournamentName
	 *            the game's name
	 */
	private void createWinner() {
		label_winner = new Label();
		label_winner.setMinWidth(USE_COMPUTED_SIZE);
		label_winner.setMinHeight(USE_COMPUTED_SIZE);
		label_winner.setPrefWidth(USE_COMPUTED_SIZE);
		label_winner.setPrefHeight(USE_COMPUTED_SIZE);
		label_winner.setMaxWidth(USE_COMPUTED_SIZE);
		label_winner.setMaxHeight(USE_COMPUTED_SIZE);
		label_winner.setText("Gewinner: " + this.gameWinner);

		this.getChildren().add(label_winner);
		AnchorPane.setLeftAnchor(label_winner, 180.0);
		AnchorPane.setRightAnchor(label_winner, 0.0);
		AnchorPane.setTopAnchor(label_winner, 0.0);
		AnchorPane.setBottomAnchor(label_winner, 0.0);
	}

	/**
	 * Creates a button which provides possibility to look up the game's
	 * configuration.
	 * 
	 * @param config
	 *            configuration to be displayed
	 */
	private void createConfigWheel(Configuration config) {
		button_configWheel = new Button();
		button_configWheel.setMinWidth(USE_COMPUTED_SIZE);
		button_configWheel.setMinHeight(USE_COMPUTED_SIZE);
		button_configWheel.setPrefWidth(USE_COMPUTED_SIZE);
		button_configWheel.setPrefHeight(USE_COMPUTED_SIZE);
		button_configWheel.setMaxWidth(USE_COMPUTED_SIZE);
		button_configWheel.setMaxHeight(USE_COMPUTED_SIZE);
		button_configWheel.setText("i");
		button_configWheel.setId("lable_infoGamepanel");
		button_configWheel.setOnMouseClicked(action -> {
			controller.showConfigEnded(gameID);
		});

		this.getChildren().add(button_configWheel);
		this.setRightAnchor(button_configWheel, 300.0);
		this.setTopAnchor(button_configWheel, 4.0);
		this.setBottomAnchor(button_configWheel, 4.0);
	}

	/**
	 * @return
	 */
	public String getLableGameID() {
		return label_gameID.getText();
	}

	/**
	 * @return
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
	 * @return
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
	 * @return
	 */
	public String getGameName() {
		return gameName;
	}

}