package org.ServerGui.View;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels for displaying a player's score. Panels are used in
 * ScoreList scene.
 * </p>
 */

public class ScorePanel extends AnchorPane {

	private Label label_playerID;
	private Label label_score;

	/**
	 * Constructs a panel with the player's name and score.
	 * 
	 * @param playerID
	 *            the player's id
	 * @param playerName
	 *            the player's name
	 */
	public ScorePanel(int playerID, int score) {

		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(40);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setPrefHeight(40);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(50);
		this.setStyle("-fx-background-color: #6B7074;");

		createPlayerID(playerID);
		createScore(score);
	}

	/**
	 * Creates a label with the player's score.
	 * 
	 * @param score
	 *            the player's score
	 */
	private void createScore(int score) {
		label_score = new Label();
		label_score.setMinWidth(USE_COMPUTED_SIZE);
		label_score.setMinHeight(USE_COMPUTED_SIZE);
		label_score.setPrefWidth(USE_COMPUTED_SIZE);
		label_score.setPrefHeight(USE_COMPUTED_SIZE);
		label_score.setMaxWidth(USE_COMPUTED_SIZE);
		label_score.setMaxHeight(USE_COMPUTED_SIZE);
		label_score.setText(Integer.toString(score));
		label_score.setId("label_score");

		this.getChildren().add(label_score);
		AnchorPane.setLeftAnchor(label_score, 0.0);
		AnchorPane.setRightAnchor(label_score, 5.0);
		AnchorPane.setTopAnchor(label_score, 0.0);
		AnchorPane.setBottomAnchor(label_score, 0.0);
	}

	/**
	 * Creates a label with the player's id.
	 * 
	 * @param playerID
	 *            the player's id
	 */
	private void createPlayerID(int playerID) {
		label_playerID = new Label();
		label_playerID.setMinWidth(USE_COMPUTED_SIZE);
		label_playerID.setMinHeight(USE_COMPUTED_SIZE);
		label_playerID.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerID.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerID.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerID.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerID.setText(Integer.toString(playerID));
		label_playerID.setId("label_PlayerID");

		this.getChildren().add(label_playerID);
		AnchorPane.setLeftAnchor(label_playerID, 5.0);
		AnchorPane.setRightAnchor(label_playerID, 0.0);
		AnchorPane.setTopAnchor(label_playerID, 0.0);
		AnchorPane.setBottomAnchor(label_playerID, 0.0);
	}

}
