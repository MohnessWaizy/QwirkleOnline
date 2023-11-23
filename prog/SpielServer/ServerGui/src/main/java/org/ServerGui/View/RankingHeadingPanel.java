package org.ServerGui.View;

import org.ServerGui.Controller.SuperController;
import org.ServerGui.Model.DataContainer;

import de.upb.swtpra1819interface.models.Client;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels containing player name, amount of games won and total
 * score in a specific tournament.
 * </p>
 */

public class RankingHeadingPanel extends AnchorPane {

	private Label label_playerName;
	private Label label_wins;
	private Label label_score;
	private SuperController ctrl;
	private Client player;
	private int gameID;
	private DataContainer data;

	/**
	 * Constructs a Panel with the clients's name, amount of games won and the total
	 * score.
	 * 
	 * @param player
	 *            player to be represented by panel
	 * @param dataContainer
	 *            dataContainer for lobby data used for panel
	 * @param tournamentID
	 *            id of the tournament
	 * @param gamesWon
	 *            player's amount of games won
	 * @param score
	 *            player's total score
	 */
	public RankingHeadingPanel() {

		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(25);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setPrefHeight(40);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(200);
		this.setStyle("-fx-background-color: #6B7074;");
		this.createGamesWon();
		this.createScore();
	}

	/**
	 * Creates a label with the heading "Siege"
	 */
	private void createGamesWon() {
		label_wins = new Label();
		label_wins.setMinWidth(USE_COMPUTED_SIZE);
		label_wins.setMinHeight(USE_COMPUTED_SIZE);
		label_wins.setPrefWidth(USE_COMPUTED_SIZE);
		label_wins.setPrefHeight(USE_COMPUTED_SIZE);
		label_wins.setMaxWidth(USE_COMPUTED_SIZE);
		label_wins.setMaxHeight(USE_COMPUTED_SIZE);
		label_wins.setText("Siege");
		label_wins.setId("label_playerID");

		this.getChildren().add(label_wins);
		AnchorPane.setLeftAnchor(label_wins, 100.0);
		AnchorPane.setRightAnchor(label_wins, 0.0);
		AnchorPane.setTopAnchor(label_wins, 0.0);
		AnchorPane.setBottomAnchor(label_wins, 0.0);
	}

	/**
	 * Creates a label with the heading "Punkte"
	 */
	private void createScore() {
		label_score = new Label();
		label_score.setMinWidth(USE_COMPUTED_SIZE);
		label_score.setMinHeight(USE_COMPUTED_SIZE);
		label_score.setPrefWidth(USE_COMPUTED_SIZE);
		label_score.setPrefHeight(USE_COMPUTED_SIZE);
		label_score.setMaxWidth(USE_COMPUTED_SIZE);
		label_score.setMaxHeight(USE_COMPUTED_SIZE);
		label_score.setText("Punkte");
		label_score.setId("label_score");

		this.getChildren().add(label_score);
		AnchorPane.setLeftAnchor(label_score, 180.0);
		AnchorPane.setRightAnchor(label_score, 0.0);
		AnchorPane.setTopAnchor(label_score, 0.0);
		AnchorPane.setBottomAnchor(label_score, 0.0);
	}
}
