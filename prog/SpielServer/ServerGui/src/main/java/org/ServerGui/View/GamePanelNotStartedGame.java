package org.ServerGui.View;

import org.ServerGui.Controller.LobbySceneController;
import org.ServerGui.Controller.SceneMapping;

import de.upb.swtpra1819interface.models.Configuration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels containing the game's id, the game's name, possibilty
 * to look up configuration and the winner's name for a not started game.
 * </p>
 */

public class GamePanelNotStartedGame extends AnchorPane {

	private Label label_gameID;
	private Label label_gameName;
	private Button Button_configWheel;
	private Button button_startGame;
	private LobbySceneController controller;
	private int gameID;
	private Configuration configuration;
	private String gameName;

	/**
	 * Constructs a panel with the game's id, the game's name and the possibilty to
	 * look up game's configuration.
	 * 
	 * @param gameID
	 *            id of the game represented by panel
	 * @param gameName
	 *            name of the game represented by panel
	 * @param config
	 *            configuration of the game represented by panel
	 * @param controller
	 *            LobbySceneController of the Lobby the panel will be added to
	 */
	public GamePanelNotStartedGame(int gameID, String gameName, Configuration configWheel,
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
		this.configuration = configWheel;
		this.gameName = gameName;

		createGameID(gameID);
		createGameName(gameName);
		createConfigWheel(configWheel);
		createStartGameButton();
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
	 * Creates a button which provides possibility to look up the game's
	 * configuration.
	 * 
	 * @param config
	 *            configuration to be displayed
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
		this.setRightAnchor(Button_configWheel, 300.0);
		this.setTopAnchor(Button_configWheel, 4.0);
		this.setBottomAnchor(Button_configWheel, 4.0);
	}

	/**
	 * Creates a button which provides possibility to start the game after assigning
	 * players and spectators.
	 * 
	 */
	private void createStartGameButton() {
		button_startGame = new Button();
		button_startGame.setMinWidth(100);
		button_startGame.setMinHeight(USE_COMPUTED_SIZE);
		button_startGame.setPrefWidth(USE_COMPUTED_SIZE);
		button_startGame.setPrefHeight(USE_COMPUTED_SIZE);
		button_startGame.setMaxWidth(USE_COMPUTED_SIZE);
		button_startGame.setMaxHeight(USE_COMPUTED_SIZE);
		button_startGame.setText("Spiel starten");
		button_startGame.setId("button_startGame");

		// if(Integer.parseInt(lable_numberOfPlayers.getText().substring(19)) < 2) {
		// button_startGame.setDisable(true);
		// }

		this.getChildren().add(button_startGame);
		AnchorPane.setRightAnchor(button_startGame, 4.0);
		AnchorPane.setTopAnchor(button_startGame, 4.0);
		AnchorPane.setBottomAnchor(button_startGame, 5.0);

		button_startGame.setOnMouseClicked(action -> {
			controller.newStage(SceneMapping.ASSIGN_PLAYERS);
			controller.notifySceneMessage(SceneMapping.ASSIGN_PLAYERS, gameID);
		});
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