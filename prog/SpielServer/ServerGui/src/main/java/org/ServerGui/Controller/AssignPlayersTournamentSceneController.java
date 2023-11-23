package org.ServerGui.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.CostumMessages.ClientConnect;
import org.CostumMessages.NewTournament;
import org.ServerGui.Model.DataContainer;
import org.ServerGui.View.AssignPlayerPanel;
import org.ServerGui.View.AssignPlayerPanelTournament;
import org.ServerGui.View.GamePanelNotStartedGame;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
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

public class AssignPlayersTournamentSceneController extends AbstractSceneController {

	@FXML
	VBox vBox_players;
	@FXML
	Text text_warningMaxPlayersReached;
	@FXML
	Button button_loadPlayers;
	@FXML
	Button button_startTournament;

	private int tournamentID;
	private DataContainer data;
	private List<AssignPlayerPanel> newPanels;
	private ArrayList<Client> players;
	private NewTournament tournament;
	private int playerCount;

	/**
	 * Class constructor. Instantiates new Object and sets its DataContainer.
	 * 
	 * @param dataContainer
	 *            DataContainer to be used by scene
	 */
	public AssignPlayersTournamentSceneController(DataContainer dataContainer) {
		super();
		this.data = dataContainer;
		this.newPanels = new ArrayList<AssignPlayerPanel>();
		this.players = new ArrayList<Client>();
		this.playerCount = 0;
		this.tournament = null;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text_warningMaxPlayersReached.setVisible(false);
		button_loadPlayers.setOnMouseClicked(action -> {
			vBox_players.getChildren().removeAll();
			vBox_players.getChildren().clear();
			for (Client client : data.getPlayerInLobby()) {
				if (client.getClientType() == ClientType.PLAYER) {
					AssignPlayerPanelTournament panel = new AssignPlayerPanelTournament(client, data, tournamentID,
							this);
					panel.setSupCtrl(supCtrl);
					vBox_players.getChildren().add(panel);
				}
			}
			checkPlayerCount();
		});

		button_startTournament.setDisable(true);
		button_startTournament.setOnMouseClicked(action -> {
			for (Client client : this.players) {
				this.tournament.getClients().add(client);
			}
			supCtrl.sendMessage(tournament);
			Stage stage = (Stage) this.button_startTournament.getScene().getWindow();
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

		if (data.getGamePlayerMap().get(tournamentID).size() < 2) {
			this.button_startTournament.setDisable(true);
			return;
		} else {
			int playerCount = 0;
			for (Client c : data.getGamePlayerMap().get(tournamentID)) {
				if (c.getClientType() == ClientType.PLAYER) {
					playerCount++;

					int maxPlayers = ((GamePanelNotStartedGame) this.lobbyCtrl
							.searchGameIDNotStarted(this.tournamentID)).getConfiguration().getMaxPlayerNumber();
					if (playerCount == maxPlayers) {
						text_warningMaxPlayersReached.setVisible(true);
						this.vBox_players.setDisable(true);
					}
				}
			}
			if (playerCount < 2) {
				this.button_startTournament.setDisable(true);
				return;
			}
		}
		this.button_startTournament.setDisable(false);
	}

	/**
	 * Function processes message send by previous scene on scene switch
	 * 
	 * @param message
	 *            message sent by previous scene
	 */
	public <T> void onSceneSwitch(T message) {
		this.vBox_players.getChildren().clear();
		if (message instanceof Integer) {
			this.tournamentID = (Integer) message;
		} else if (message instanceof ClientConnect) {
			Client client = ((ClientConnect) message).getClient();

			AssignPlayerPanel panel = new AssignPlayerPanel(client, data, tournamentID);
			panel.setSupCtrl(supCtrl);

			data.getPlayerInLobby().add(client);
			newPanels.add(panel);
			checkPlayerCount();
		} else if (message instanceof String) {
			String mes = (String) message;
			if (mes.equals("checkStartGameButton")) {
				this.checkPlayerCount();
			}
		} else if (message instanceof NewTournament) {
			this.tournament = (NewTournament) message;
		}
	}

	public void switchScene() {
		checkPlayerCount();
	}

	public ArrayList<Client> getPlayers() {
		return players;
	}

	public void increasePlayerCount() {
		this.playerCount++;
		if (playerCount > 1) {
			this.button_startTournament.setDisable(false);
		}
	}
}
