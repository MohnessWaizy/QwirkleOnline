package org.ServerGui.View;

import org.ServerGui.Controller.SelectWinnerSceneController;
import org.ServerGui.Controller.SuperController;
import org.ServerGui.Model.DataContainer;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * <p>
 * Class provides panels for displaying a player's and the possibility to select
 * the player as the winner of an aborted game.
 * </p>
 */

public class SelectWinnerPanel extends AnchorPane {

	private Label label_playerInfo;
	private Button button_selectAsWinner;
	private SuperController ctrl;
	private Client player;
	private int gameID;
	private DataContainer data;
	private SelectWinnerSceneController selectCtrl;

	/**
	 * Constructs a panel with the player's id and the possibility to select the
	 * player as the winner.
	 * 
	 * @param player
	 *            the player represented by the panel
	 * @param dataContainer
	 *            the DataContainer used by scene
	 * @param gameID
	 *            id of the game whose winner needs to be selected
	 * @param selectCtrl
	 *            controller of the scene loading and displaying the panel
	 */
	public SelectWinnerPanel(Client player, DataContainer dataContainer, int gameId,
			SelectWinnerSceneController selectCtrl) {

		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(25);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setPrefHeight(40);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(200);
		this.setStyle("-fx-background-color: #6B7074;");
		this.player = player;
		this.data = dataContainer;
		this.gameID = gameId;
		this.selectCtrl = selectCtrl;

		createPlayerID(player.getClientName());
		createSelectButton();
	}

	/**
	 * Creates a label with the player's id
	 * 
	 * @param playerName
	 *            the player's name
	 */
	private void createPlayerID(String playerName) {
		label_playerInfo = new Label();
		label_playerInfo.setMinWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setMinHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setText(playerName);
		label_playerInfo.setId("label_playerID");

		this.getChildren().add(label_playerInfo);
		AnchorPane.setLeftAnchor(label_playerInfo, 15.0);
		AnchorPane.setRightAnchor(label_playerInfo, 0.0);
		AnchorPane.setTopAnchor(label_playerInfo, 0.0);
		AnchorPane.setBottomAnchor(label_playerInfo, 0.0);
	}

	/**
	 * Creates a button with the possibility to select player as the winner.
	 * 
	 */
	private void createSelectButton() {
		button_selectAsWinner = new Button();
		button_selectAsWinner.setMinWidth(USE_COMPUTED_SIZE);
		button_selectAsWinner.setMinHeight(USE_COMPUTED_SIZE);
		button_selectAsWinner.setPrefWidth(USE_COMPUTED_SIZE);
		button_selectAsWinner.setPrefHeight(USE_COMPUTED_SIZE);
		button_selectAsWinner.setMaxWidth(USE_COMPUTED_SIZE);
		button_selectAsWinner.setMaxHeight(USE_COMPUTED_SIZE);
		button_selectAsWinner.setText("Als Gewinner auswählen");
		button_selectAsWinner.setId("button_selectAsWinner");

		this.getChildren().add(button_selectAsWinner);
		AnchorPane.setRightAnchor(button_selectAsWinner, 5.0);
		AnchorPane.setTopAnchor(button_selectAsWinner, 4.0);
		AnchorPane.setBottomAnchor(button_selectAsWinner, 4.0);

		button_selectAsWinner.setOnMouseClicked(action -> {
			String gameName = ((GamePanelStartedGame) this.selectCtrl.getLobbyCtrl().searchGameIDStarted(this.gameID))
					.getGameName();
			Configuration config = ((GamePanelStartedGame) this.selectCtrl.getLobbyCtrl()
					.searchGameIDStarted(this.gameID)).getConfiguration();
			this.selectCtrl.getLobbyCtrl().deleteStartedGameID(gameID);
			this.selectCtrl.getLobbyCtrl().addEndedGame(this.gameID, gameName, player.getClientName(), config);
			this.selectCtrl.getLobbyCtrl().lobbyChangeGameState(gameID, GameState.ENDED, this.player);
			Stage stage = this.selectCtrl.getStage();
			stage.close();
			/*
			 * TODO kann weg? if
			 * (button_selectAsWinner.getText().equals("Client hinzufügen")) {
			 * data.getPlayerIngame().add(player); data.getPlayerInLobby().remove(player);
			 * 
			 * data.getGamePlayerMap().get(gameID).add(player); ArrayList<Client> players =
			 * data.getGameIdMap().get(gameID).getPlayers() != null ? (ArrayList<Client>)
			 * data.getGameIdMap().get(gameID).getPlayers() : new ArrayList<Client>();
			 * players.add(player); data.getGameIdMap().get(gameID).setPlayers(players);
			 * 
			 * ctrl.sendMessage(new MoveToGame(player.getClientId(), player.getClientType(),
			 * gameID)); ctrl.notifyScene(SceneMapping.LOBBY, new
			 * ClientJoinGame(data.getGameIdMap().get(gameID), player));
			 * ctrl.notifyScene(SceneMapping.ASSIGN_PLAYERS, "checkStartGameButton");
			 * 
			 * // this.button_addPlayer.setText("Client entfernen");
			 * this.button_selectAsWinner.setText("Client hinzugefügt");
			 * this.button_selectAsWinner.setDisable(true); } // else if
			 * (button_addPlayer.getText().equals("Client entfernen")) { //
			 * data.getPlayerInLobby().add(player); //
			 * data.getPlayerIngame().remove(player); // //
			 * data.getGameIdMap().get(gameID).getPlayers().remove(player); //
			 * data.getGamePlayerMap().get(gameID).remove(player); // //
			 * ctrl.sendMessage(new MoveToGame(player.getClientId(), player.getClientType(),
			 * -1)); // this.button_addPlayer.setText("Client hinzufügen"); // }
			 */
		});
	}

	public String getLableGameID() {
		return label_playerInfo.getText();
	}

	public void setSupCtrl(SuperController ctrl) {
		this.ctrl = ctrl;
	}
}
