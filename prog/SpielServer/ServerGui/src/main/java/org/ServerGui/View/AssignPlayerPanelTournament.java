package org.ServerGui.View;

import org.ServerGui.Controller.AssignPlayersTournamentSceneController;
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

public class AssignPlayerPanelTournament extends AnchorPane {

	private Label label_playerInfoTournament;
	private Button button_addPlayerTournament;
	private SuperController ctrlTournament;
	private Client playerTournament;
	private String playerInfo;
	private int tournamentID;
	private DataContainer data;
	private AssignPlayersTournamentSceneController ctrl;

	/**
	 * Constructs a Panel with the clients's id, information whether the client is
	 * player or spectator and a button to assign to game.
	 * 
	 * @param player
	 *            player to be represented by panel
	 * @param dataContainer
	 *            dataContainer for lobby data used for panel
	 * @param tournamentID
	 *            id of the game the player can be assigned to
	 */
	public AssignPlayerPanelTournament(Client player, DataContainer dataContainer, int gameId,
			AssignPlayersTournamentSceneController ctrl) {

		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setPrefHeight(50);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(200);
		this.setStyle("-fx-background-color: #6B7074;");
		this.playerTournament = player;
		this.data = dataContainer;
		this.tournamentID = gameId;
		this.ctrl = ctrl;
		playerInfo = player.getClientName();
		createPlayerID(playerInfo);
		createAddButton();

	}

	/**
	 * Creates a label with the player's id and role (player or spectator) an adds
	 * it to panel.
	 * 
	 * @param playerInfo
	 *            player's id and role (player or spectator)
	 */
	private void createPlayerID(String playerInfo) {
		label_playerInfoTournament = new Label();
		label_playerInfoTournament.setMinWidth(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setMinHeight(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setPrefWidth(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setPrefHeight(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setMaxWidth(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setMaxHeight(USE_COMPUTED_SIZE);
		label_playerInfoTournament.setText(playerInfo);
		label_playerInfoTournament.setId("label_playerID");

		this.getChildren().add(label_playerInfoTournament);
		AnchorPane.setLeftAnchor(label_playerInfoTournament, 15.0);
		AnchorPane.setRightAnchor(label_playerInfoTournament, 0.0);
		AnchorPane.setTopAnchor(label_playerInfoTournament, 0.0);
		AnchorPane.setBottomAnchor(label_playerInfoTournament, 0.0);
	}

	/**
	 * Creates a button which assignes the player to the game with id gameID.
	 * 
	 */
	private void createAddButton() {
		button_addPlayerTournament = new Button();
		button_addPlayerTournament.setMinWidth(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setMinHeight(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setPrefWidth(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setPrefHeight(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setMaxWidth(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setMaxHeight(USE_COMPUTED_SIZE);
		button_addPlayerTournament.setText("Client hinzufügen");

		button_addPlayerTournament.setId("textField_playerOrder");

		this.getChildren().add(button_addPlayerTournament);
		AnchorPane.setRightAnchor(button_addPlayerTournament, 5.0);
		AnchorPane.setTopAnchor(button_addPlayerTournament, 4.0);
		AnchorPane.setBottomAnchor(button_addPlayerTournament, 4.0);

		button_addPlayerTournament.setOnMouseClicked(action -> {
			if (button_addPlayerTournament.getText().equals("Client hinzufügen")) {
				// data.getPlayerIngame().add(playerTournament);
				data.getPlayerInLobby().remove(playerTournament);
				this.ctrl.getPlayers().add(this.playerTournament);
				this.ctrl.increasePlayerCount();
				// data.getGamePlayerMap().get(tournamentID).add(playerTournament);
				// ArrayList<Client> players =
				// data.getGameIdMap().get(tournamentID).getPlayers() != null
				// ? (ArrayList<Client>) data.getGameIdMap().get(tournamentID).getPlayers()
				// : new ArrayList<Client>();
				// players.add(playerTournament);
				// data.getGameIdMap().get(tournamentID).setPlayers(players);

				// ctrlTournament.sendMessage(
				// new MoveToGame(playerTournament.getClientId(),
				// playerTournament.getClientType(), tournamentID));
				// ctrlTournament.notifyScene(SceneMapping.LOBBY,
				// new ClientJoinGame(data.getGameIdMap().get(tournamentID), playerTournament));
				// ctrlTournament.notifyScene(SceneMapping.ASSIGN_PLAYERS,
				// "checkStartGameButton");

				this.button_addPlayerTournament.setDisable(true);
			}
		});
	}

	public String getLableGameID() {
		return label_playerInfoTournament.getText();
	}

	public void setSupCtrl(SuperController ctrl) {
		this.ctrlTournament = ctrl;
	}
}
