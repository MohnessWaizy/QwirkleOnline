package org.ServerGui.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.CostumMessages.ClientConnect;
import org.ServerGui.Model.DataContainer;
import org.ServerGui.View.AssignPlayerPanel;
import org.ServerGui.View.GamePanelNotStartedGame;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * <p>
 * When the host starts a game players and spectators need to be assigned via
 * the AssignPlayer scene. This class implements the specific controller for
 * AssignPlayer. Every Scene created needs its own Controller for its GUI.
 * </p>
 */

public class AssignPlayersSceneController extends AbstractSceneController {

	@FXML
	VBox vBox_players;
	@FXML
	Text text_warningMaxPlayersReached;
	@FXML
	Button button_loadPlayers;
	@FXML
	Button button_startGame;

	private int gameID;
	private DataContainer data;
	private List<AssignPlayerPanel> newPanels;

	/**
	 * Class constructor. Instantiates new Object and sets its DataContainer.
	 * 
	 * @param dataContainer
	 *            DataContainer to be used by scene
	 */
	public AssignPlayersSceneController(DataContainer dataContainer) {
		super();
		this.gameID = -1;
		this.data = dataContainer;
		this.newPanels = new ArrayList<AssignPlayerPanel>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text_warningMaxPlayersReached.setVisible(false);
		button_loadPlayers.setOnMouseClicked(action -> {
			vBox_players.getChildren().removeAll();
			vBox_players.getChildren().clear();
			for (Client client : data.getPlayerInLobby()) {
				AssignPlayerPanel panel = new AssignPlayerPanel(client, data, gameID);
				panel.setSupCtrl(supCtrl);
				vBox_players.getChildren().add(panel);
			}
			checkPlayerCount();
		});

		button_startGame.setDisable(true);
		button_startGame.setOnMouseClicked(action -> {
			GamePanelNotStartedGame gamePanel = (GamePanelNotStartedGame) lobbyCtrl.searchGameIDNotStarted(gameID);
			Configuration config = gamePanel.getConfiguration();
			String gameName = gamePanel.getGameName();

			lobbyCtrl.addStartedGame(gameID, gameName, config);
			lobbyCtrl.deleteNotStartedGameID(gameID);
			lobbyCtrl.lobbyChangeGameState(gameID, GameState.IN_PROGRESS);
			Stage stage = (Stage) this.button_startGame.getScene().getWindow();
			stage.close();
		});
	}

	/**
	 * Function checks whether there are neither too little players nor too less. In
	 * case of wrong amount gui elements that shouldn't be used anymore are
	 * disabled.
	 */
	private void checkPlayerCount() {
		if (data.getGamePlayerMap() == null) {
			return;
		}

		if (data.getGamePlayerMap().size() == 0) {
			return;
		}

		if (data.getGamePlayerMap().contains(gameID) && data.getGamePlayerMap().get(gameID).size() < 2) {
			this.button_startGame.setDisable(true);
			return;
		} else {
			int playerCount = 0;
			for (Client c : data.getGamePlayerMap().get(gameID)) {
				if (c.getClientType() == ClientType.PLAYER) {
					playerCount++;

					int maxPlayers = ((GamePanelNotStartedGame) this.lobbyCtrl.searchGameIDNotStarted(this.gameID))
							.getConfiguration().getMaxPlayerNumber();
					if (playerCount == maxPlayers) {
						text_warningMaxPlayersReached.setVisible(true);
						this.vBox_players.setDisable(true);
					}
				}
			}
			if (playerCount < 2) {
				this.button_startGame.setDisable(true);
				return;
			}
		}
		this.button_startGame.setDisable(false);
	}

	/**
	 * Function processes message send by previous scene on scene switch
	 * 
	 * @param message
	 *            message sent by previous scene
	 */
	public <T> void reciveMessage(T message) {
		this.vBox_players.getChildren().clear();
		if (message instanceof Integer) {
			this.gameID = (Integer) message;
		} else if (message instanceof ClientConnect) {
			Client client = ((ClientConnect) message).getClient();

			AssignPlayerPanel panel = new AssignPlayerPanel(client, data, gameID);
			panel.setSupCtrl(supCtrl);

			data.getPlayerInLobby().add(client);
			newPanels.add(panel);
			checkPlayerCount();
		} else if (message instanceof String) {
			String mes = (String) message;
			if (mes.equals("checkStartGameButton")) {
				this.checkPlayerCount();
			}
		}
	}

	/*
	 * public void switchScene() { checkPlayerCount(); }
	 */
}
