package org.ServerGui.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.ServerGui.Model.DataContainer;
import org.ServerGui.View.AssignPlayerPanel;
import org.ServerGui.View.SelectWinnerPanel;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * <p>
 * Controller for scene SelectWinner. Used to offer possibility to select an
 * aborted game's winner.
 * </p>
 */

public class SelectWinnerSceneController extends AbstractSceneController {

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

	public SelectWinnerSceneController(DataContainer dataContainer) {
		super();
		this.data = dataContainer;
		this.newPanels = new ArrayList<AssignPlayerPanel>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * TODO muss angepasst werden, sodass nur ncoh Spieler aus dem Spiel geladen
		 * werden
		 */
		button_loadPlayers.setOnMouseClicked(action -> {
			vBox_players.getChildren().removeAll();
			vBox_players.getChildren().clear();
			ArrayList<Client> playersInGame = this.data.getGamePlayerMap().get(this.gameID);
			for (Client client : playersInGame) {
				if (client.getClientType() == ClientType.PLAYER) {
					SelectWinnerPanel panel = new SelectWinnerPanel(client, this.data, this.gameID, this);
					panel.setSupCtrl(this.supCtrl);
					this.vBox_players.getChildren().add(panel);
				}
			}
		});
	}

	/**
	 * Function processes message send by previous scene on scene switch
	 * 
	 * @param message
	 *            message sent by previous scene
	 */
	public <T> void reciveMessage(T message) {
		/* TODO das entsprechende hier tun */
		if (message instanceof Integer) {
			this.gameID = (Integer) message;
		}
		/*
		 * TODO ich schätze mal dieser Teil kann weg? else if (message instanceof
		 * ClientConnect) { Client client = ((ClientConnect) message).getClient();
		 * 
		 * AssignPlayerPanel panel = new AssignPlayerPanel(client, data, gameID, false);
		 * panel.setSupCtrl(supCtrl);
		 * 
		 * data.getPlayerInLobby().add(client); newPanels.add(panel);
		 * checkPlayerCount(); } else if (message instanceof String) { String mes =
		 * (String) message; if(mes.equals("checkStartGameButton")) {
		 * this.checkPlayerCount(); } }
		 */
	}

	/*
	 * public void switchScene() { checkPlayerCount(); }
	 */

	public Stage getStage() {
		return (Stage) this.button_loadPlayers.getScene().getWindow();
	}

}
