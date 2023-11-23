package org.ServerGui.View;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GamePanelTournament extends AnchorPane {

	private Label label_gameID;
	private Label label_cardsPerPlayer;
	private Label lable_numberOfPlayers;
	private Label label_configWheel;
	private Button button_startTournament;

	public GamePanelTournament(String gameID, int cardsPerPlayer, int numberofPlayers, String configWheel,
			String joinButton) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(50);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(50);
		this.setStyle("-fx-background-color: #6B7074;");

		createGameID(gameID);
		createCardsPerPlayer(cardsPerPlayer);
		createNumberOfPlayers(numberofPlayers);
		createConfigWheel(configWheel);
		createJoinButton(joinButton);
	}

	private void createGameID(String gameName) {
		label_gameID = new Label();
		label_gameID.setMinWidth(USE_COMPUTED_SIZE);
		label_gameID.setMinHeight(USE_COMPUTED_SIZE);
		label_gameID.setPrefWidth(USE_COMPUTED_SIZE);
		label_gameID.setPrefHeight(USE_COMPUTED_SIZE);
		label_gameID.setMaxWidth(USE_COMPUTED_SIZE);
		label_gameID.setMaxHeight(USE_COMPUTED_SIZE);
		label_gameID.setText(gameName);
		label_gameID.setId("label_gameID");

		this.getChildren().add(label_gameID);
		AnchorPane.setLeftAnchor(label_gameID, 4.0);
		AnchorPane.setRightAnchor(label_gameID, 0.0);
		AnchorPane.setTopAnchor(label_gameID, 0.0);
		AnchorPane.setBottomAnchor(label_gameID, 0.0);
	}

	private void createCardsPerPlayer(int cards) {
		label_cardsPerPlayer = new Label();
		label_cardsPerPlayer.setMinWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMinHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setPrefWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setPrefHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMaxWidth(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setMaxHeight(USE_COMPUTED_SIZE);
		label_cardsPerPlayer.setText("Cards per player: " + Integer.toString(cards));

		this.getChildren().add(label_cardsPerPlayer);
		AnchorPane.setLeftAnchor(label_cardsPerPlayer, 200.0);
		AnchorPane.setTopAnchor(label_cardsPerPlayer, 0.0);
		AnchorPane.setBottomAnchor(label_cardsPerPlayer, 0.0);
	}

	private void createNumberOfPlayers(int numPlayers) {
		lable_numberOfPlayers = new Label();
		lable_numberOfPlayers.setMinWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMinHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setPrefWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setPrefHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMaxWidth(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setMaxHeight(USE_COMPUTED_SIZE);
		lable_numberOfPlayers.setText("Number of players: " + Integer.toString(numPlayers));

		this.getChildren().add(lable_numberOfPlayers);
		AnchorPane.setLeftAnchor(lable_numberOfPlayers, 400.0);
		AnchorPane.setTopAnchor(lable_numberOfPlayers, 0.0);
		AnchorPane.setBottomAnchor(lable_numberOfPlayers, 0.0);
	}

	private void createConfigWheel(String config) {
		label_configWheel = new Label();
		label_configWheel.setMinWidth(USE_COMPUTED_SIZE);
		label_configWheel.setMinHeight(USE_COMPUTED_SIZE);
		label_configWheel.setPrefWidth(USE_COMPUTED_SIZE);
		label_configWheel.setPrefHeight(USE_COMPUTED_SIZE);
		label_configWheel.setMaxWidth(USE_COMPUTED_SIZE);
		label_configWheel.setMaxHeight(USE_COMPUTED_SIZE);
		label_configWheel.setText("i");
		label_configWheel.setId("lable_infoGamepanel");

		this.getChildren().add(label_configWheel);
		AnchorPane.setRightAnchor(label_configWheel, 300.0);
		AnchorPane.setTopAnchor(label_configWheel, 4.0);
		AnchorPane.setBottomAnchor(label_configWheel, 4.0);
	}

	private void createJoinButton(String scene) {
		button_startTournament = new Button();
		button_startTournament.setMinWidth(100);
		button_startTournament.setMinHeight(USE_COMPUTED_SIZE);
		button_startTournament.setPrefWidth(USE_COMPUTED_SIZE);
		button_startTournament.setPrefHeight(USE_COMPUTED_SIZE);
		button_startTournament.setMaxWidth(USE_COMPUTED_SIZE);
		button_startTournament.setMaxHeight(USE_COMPUTED_SIZE);
		button_startTournament.setText("Turnier starten");
		button_startTournament.setId("button_startTournament");

		this.getChildren().add(button_startTournament);
		AnchorPane.setRightAnchor(button_startTournament, 4.0);
		AnchorPane.setTopAnchor(button_startTournament, 4.0);
		AnchorPane.setBottomAnchor(button_startTournament, 5.0);
	}

	public String getLableGameID() {
		return label_gameID.getText();
	}
}
