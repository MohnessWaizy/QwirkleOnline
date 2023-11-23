package org.ServerGui.View;

import java.util.ArrayList;

import org.CostumMessages.ClientJoinGame;
import org.CostumMessages.MoveToGame;
import org.ServerGui.Controller.SceneMapping;
import org.ServerGui.Controller.SuperController;
import org.ServerGui.Model.DataContainer;

import de.upb.swtpra1819interface.models.Client;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels containing playerID and possibility to assign player to
 * a specific game. The panels are used in AssignPlayers scene.
 * </p>
 */

public class AssignPlayerPanel extends AnchorPane {

	private Label label_playerInfo;
	private Button button_addPlayer;
	private SuperController ctrl;
	private Client player;
	private String playerInfo;
	private int gameID;
	private DataContainer data;

	/**
	 * Constructs a Panel with the clients's id, information whether the client is
	 * player or spectator and a button to assign to game.
	 * 
	 * @param player        player to be represented by panel
	 * @param dataContainer dataContainer for lobby data used for panel
	 * @param gameID        id of the game the player can be assigned to
	 */
	public AssignPlayerPanel(Client player, DataContainer dataContainer, int gameId) {

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
		playerInfo = player.getClientType().toString() + " - " + player.getClientName();
		createPlayerID(playerInfo);
		createAddButton();
	}

	/**
	 * Creates a label with the player's id and role (player or spectator) an adds
	 * it to panel.
	 * 
	 * @param playerInfo player's id and role (player or spectator)
	 */
	private void createPlayerID(String playerInfo) {
		label_playerInfo = new Label();
		label_playerInfo.setMinWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setMinHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerInfo.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerInfo.setText(playerInfo);
		label_playerInfo.setId("label_playerID");

		this.getChildren().add(label_playerInfo);
		AnchorPane.setLeftAnchor(label_playerInfo, 15.0);
		AnchorPane.setRightAnchor(label_playerInfo, 0.0);
		AnchorPane.setTopAnchor(label_playerInfo, 0.0);
		AnchorPane.setBottomAnchor(label_playerInfo, 0.0);
	}

	/**
	 * Creates a button which assignes the player to the game with id gameID.
	 * 
	 */
	private void createAddButton() {
		button_addPlayer = new Button();
		button_addPlayer.setMinWidth(USE_COMPUTED_SIZE);
		button_addPlayer.setMinHeight(USE_COMPUTED_SIZE);
		button_addPlayer.setPrefWidth(USE_COMPUTED_SIZE);
		button_addPlayer.setPrefHeight(USE_COMPUTED_SIZE);
		button_addPlayer.setMaxWidth(USE_COMPUTED_SIZE);
		button_addPlayer.setMaxHeight(USE_COMPUTED_SIZE);
		button_addPlayer.setText("Client hinzufügen");

		button_addPlayer.setId("textField_playerOrder");

		this.getChildren().add(button_addPlayer);
		AnchorPane.setRightAnchor(button_addPlayer, 5.0);
		AnchorPane.setTopAnchor(button_addPlayer, 4.0);
		AnchorPane.setBottomAnchor(button_addPlayer, 4.0);

		button_addPlayer.setOnMouseClicked(action -> {
			if (button_addPlayer.getText().equals("Client hinzufügen")) {
				data.getPlayerIngame().add(player);
				data.getPlayerInLobby().remove(player);

				data.getGamePlayerMap().get(gameID).add(player);
				ArrayList<Client> players = data.getGameIdMap().get(gameID).getPlayers() != null
						? (ArrayList<Client>) data.getGameIdMap().get(gameID).getPlayers()
						: new ArrayList<Client>();
				players.add(player);
				data.getGameIdMap().get(gameID).setPlayers(players);

				ctrl.sendMessage(new MoveToGame(player.getClientId(), player.getClientType(), gameID));
				ctrl.notifyScene(SceneMapping.LOBBY, new ClientJoinGame(data.getGameIdMap().get(gameID), player));
				ctrl.notifyScene(SceneMapping.ASSIGN_PLAYERS, "checkStartGameButton");

				this.button_addPlayer.setDisable(true);
			}
		});
	}

	public String getLableGameID() {
		return label_playerInfo.getText();
	}

	public void setSupCtrl(SuperController ctrl) {
		this.ctrl = ctrl;
	}
}
