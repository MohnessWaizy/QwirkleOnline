package controller;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * panel to display a player
 *
 */
public class PlayerPanel extends AnchorPane {

	private Label label_playerName;
	private Label label_playerScore;
	private int playerID;

	public PlayerPanel(int playerID, String playerName) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(80);
		this.setPrefWidth(200);
		this.setPrefHeight(200);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(80);
		this.setStyle("-fx-background-color: grey;");

		createLabel_playerName(playerName);
		this.playerID = playerID;
		createLabel_playerScore();
	}

	/**
	 * Creates the label, that contains the player name.
	 * 
	 * @param playerName Name of the player represented by the panel
	 */

	private void createLabel_playerName(String playerName) {
		label_playerName = new Label();
		label_playerName.setMinWidth(USE_COMPUTED_SIZE);
		label_playerName.setMinHeight(USE_COMPUTED_SIZE);
		label_playerName.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerName.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerName.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerName.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerName.setText(playerName);

		this.getChildren().add(label_playerName);
		this.setLeftAnchor(label_playerName, 5.0);
		this.setTopAnchor(label_playerName, 5.0);
		this.setBottomAnchor(label_playerName, 29.0);
	}

	/**
	 * Creates the label, that contains the player score
	 */

	private void createLabel_playerScore() {
		label_playerScore = new Label();
		label_playerScore.setMinWidth(USE_COMPUTED_SIZE);
		label_playerScore.setMinHeight(USE_COMPUTED_SIZE);
		label_playerScore.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerScore.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerScore.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerScore.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerScore.setText("Punkte: 0");

		this.getChildren().add(label_playerScore);
		this.setLeftAnchor(label_playerScore, 10.0);
		this.setTopAnchor(label_playerScore, 38.0);
		this.setBottomAnchor(label_playerScore, 8.0);
	}

	public String getPlayerName() {
		return label_playerName.getText();
	}

	/**
	 * Updates the score of the player represented by the panel
	 * 
	 * @param score
	 */

	public void updatePlayerScore(int score) {
		label_playerScore.setText("Punkte: " + Integer.toString(score));
	}

	public int getPlayerID() {
		return playerID;
	}
}
