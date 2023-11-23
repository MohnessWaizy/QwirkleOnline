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

public class RankingPanel extends AnchorPane {

	private Label label_playerName;
	private Label label_gamesWon;
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
	public RankingPanel(Client player, DataContainer dataContainer/* , int tournamentId */, int gamesWon, int score) {

		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(25);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setPrefHeight(40);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(200);
		this.setStyle("-fx-background-color: #6B7074;");
		this.player = player;
		this.data = dataContainer;
		// this.gameID = tournamentId;
		this.createPlayerName(player.getClientName());
		this.createGamesWon(gamesWon);
		this.createScore(score);
	}

	/**
	 * Creates a label with the player's name.
	 * 
	 * @param playerName
	 *            the player's name
	 */
	private void createPlayerName(String playerName) {
		label_playerName = new Label();
		label_playerName.setMinWidth(USE_COMPUTED_SIZE);
		label_playerName.setMinHeight(USE_COMPUTED_SIZE);
		label_playerName.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerName.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerName.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerName.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerName.setText(playerName);
		label_playerName.setId("label_playerName");

		this.getChildren().add(label_playerName);
		AnchorPane.setLeftAnchor(label_playerName, 15.0);
		AnchorPane.setRightAnchor(label_playerName, 0.0);
		AnchorPane.setTopAnchor(label_playerName, 0.0);
		AnchorPane.setBottomAnchor(label_playerName, 0.0);
	}

	/**
	 * Creates a label with the amount of games the player won.
	 * 
	 * @param gamesWon
	 *            amount of games the player won
	 */
	private void createGamesWon(int gamesWon) {
		label_gamesWon = new Label();
		label_gamesWon.setMinWidth(USE_COMPUTED_SIZE);
		label_gamesWon.setMinHeight(USE_COMPUTED_SIZE);
		label_gamesWon.setPrefWidth(USE_COMPUTED_SIZE);
		label_gamesWon.setPrefHeight(USE_COMPUTED_SIZE);
		label_gamesWon.setMaxWidth(USE_COMPUTED_SIZE);
		label_gamesWon.setMaxHeight(USE_COMPUTED_SIZE);
		label_gamesWon.setText(((Integer) gamesWon).toString());
		label_gamesWon.setId("label_playerID");

		this.getChildren().add(label_gamesWon);
		AnchorPane.setLeftAnchor(label_gamesWon, 100.0);
		AnchorPane.setRightAnchor(label_gamesWon, 0.0);
		AnchorPane.setTopAnchor(label_gamesWon, 0.0);
		AnchorPane.setBottomAnchor(label_gamesWon, 0.0);
	}

	/**
	 * Creates a label with the player's score from all played games.
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
		label_score.setText(((Integer) score).toString());
		label_score.setId("label_score");

		this.getChildren().add(label_score);
		AnchorPane.setLeftAnchor(label_score, 180.0);
		AnchorPane.setRightAnchor(label_score, 0.0);
		AnchorPane.setTopAnchor(label_score, 0.0);
		AnchorPane.setBottomAnchor(label_score, 0.0);
	}

	public int getGameID() {
		return this.gameID;
	}

	public void setSupCtrl(SuperController ctrl) {
		this.ctrl = ctrl;
	}
}
