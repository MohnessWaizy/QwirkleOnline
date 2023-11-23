package org.ServerGui.View;

import org.ServerGui.Controller.LobbySceneController;
import org.ServerGui.Controller.SceneMapping;
import de.upb.swtpra1819interface.models.Configuration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * Class provides panels for a not started tournament containing the
 * tournament's id, the tournaments name, the winner's name, possibility to look
 * up the tournament's configuration and possibility to look up the tournament's
 * scorelist.
 * </p>
 */

public class GamePanelNotStartedTournament extends AnchorPane {

	private Label label_tournamentName;
	private int tournamentID;
	private Label label_tournamentNotRunning;
	private Button Button_configWheel;
	private Button button_assignPlayers;
	private Button button_startTournament;
	LobbySceneController lobbyCtrl;
	Configuration config;
	private String gameName;

	/**
	 * Constructs a panel with the tournament's id, the tournament's name, the
	 * winner's name, a possibility to look up the tournament's configuration and a
	 * possibility to look up the tournaments scorelist for an ended tournament.
	 * 
	 * @param tournamentID   id of the tournament the panel represents
	 * @param tournamentName name of the tournament the panel represents
	 * @param config         configuration of the tournament the panel represents
	 * @param lobbyCtrl      controller of the lobby the panel is added to
	 */
	public GamePanelNotStartedTournament(LobbySceneController lobbyCtrl, String tournamentName, int gameID,
			Configuration config) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(50);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(50);
		this.setStyle("-fx-background-color: #6B7074;");

		createTournamentName(tournamentName);
		createNotStartedLabel();
		createConfigWheel(config);
		createStartTournamentButton();
		createAssignPlayersButton();
		this.tournamentID = gameID;
		this.lobbyCtrl = lobbyCtrl;
		this.config = config;
		this.label_tournamentName.setText(tournamentName);
		this.gameName = tournamentName;
	}

	/**
	 * Creates a label with the tournament's name
	 * 
	 * @param tournamentName the tournament's name
	 */
	private void createTournamentName(String gameName) {
		label_tournamentName = new Label();
		label_tournamentName.setMinWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setMinHeight(USE_COMPUTED_SIZE);
		label_tournamentName.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setPrefHeight(USE_COMPUTED_SIZE);
		label_tournamentName.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setMaxHeight(USE_COMPUTED_SIZE);
		label_tournamentName.setText(gameName);
		label_tournamentName.setId("label_gameName");

		this.getChildren().add(label_tournamentName);
		AnchorPane.setLeftAnchor(label_tournamentName, 4.0);
		AnchorPane.setRightAnchor(label_tournamentName, 0.0);
		AnchorPane.setTopAnchor(label_tournamentName, 0.0);
		AnchorPane.setBottomAnchor(label_tournamentName, 0.0);
	}

	/**
	 * Creates a label with the tournament's status (not started).
	 * 
	 */
	private void createNotStartedLabel() {
		label_tournamentNotRunning = new Label();
		label_tournamentNotRunning.setMinWidth(USE_COMPUTED_SIZE);
		label_tournamentNotRunning.setMinHeight(40);
		label_tournamentNotRunning.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentNotRunning.setPrefHeight(40);
		label_tournamentNotRunning.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentNotRunning.setMaxHeight(40);
		label_tournamentNotRunning.setText("Das läuft Turnier noch nicht");
		label_tournamentNotRunning.setId("label_tournamentRunning");

		this.getChildren().add(label_tournamentNotRunning);
		AnchorPane.setLeftAnchor(label_tournamentNotRunning, 130.0);
		AnchorPane.setTopAnchor(label_tournamentNotRunning, 4.0);
	}

	/**
	 * Creates a button with the possibility to look up the tournament's
	 * configuration
	 * 
	 * @param config configuration to be displayed
	 */
	private void createConfigWheel(Configuration config) {
		Button_configWheel = new Button();
		Button_configWheel.setMinWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMinHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setPrefWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setPrefHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setMaxWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMaxHeight(USE_COMPUTED_SIZE);
		Button_configWheel.setText("i");
		Button_configWheel.setId("lable_infoGamepanel");
		Button_configWheel.setOnMouseClicked(action -> {
			this.lobbyCtrl.showConfigEnded(tournamentID);
		});

		this.getChildren().add(Button_configWheel);
		AnchorPane.setRightAnchor(Button_configWheel, 300.0);
		AnchorPane.setTopAnchor(Button_configWheel, 4.0);
		AnchorPane.setBottomAnchor(Button_configWheel, 4.0);
	}

	/**
	 * Creates a button with the possibility to start the tournament
	 *
	 */
	private void createStartTournamentButton() {
		button_startTournament = new Button();
		button_startTournament.setMinWidth(100);
		button_startTournament.setMinHeight(USE_COMPUTED_SIZE);
		button_startTournament.setPrefWidth(USE_COMPUTED_SIZE);
		button_startTournament.setPrefHeight(USE_COMPUTED_SIZE);
		button_startTournament.setMaxWidth(USE_COMPUTED_SIZE);
		button_startTournament.setMaxHeight(USE_COMPUTED_SIZE);
		button_startTournament.setText("Turnier starten");
		button_startTournament.setId("button_startGame");

		this.getChildren().add(button_startTournament);
		AnchorPane.setRightAnchor(button_startTournament, 4.0);
		AnchorPane.setTopAnchor(button_startTournament, 4.0);
		AnchorPane.setBottomAnchor(button_startTournament, 5.0);

		button_startTournament.setOnMouseClicked(action -> {
			lobbyCtrl.deleteTournamentID(tournamentID);
			lobbyCtrl.addStartedTournament(this.gameName, this.tournamentID, this.config);
			/*
			 * lobbyCtrl.lobbyChangeGameState(tournamentID, GameState.IN_PROGRESS); TODO
			 * muss eventuell angepasst werden, ich weiß nicht wie das mit dem Status von
			 * Turnieren ist
			 */
		});
	}

	
	 private void createAssignPlayersButton() {
	 button_assignPlayers = new Button();
	 button_assignPlayers.setMinWidth(100);
	 button_assignPlayers.setMinHeight(USE_COMPUTED_SIZE);
	 button_assignPlayers.setPrefWidth(USE_COMPUTED_SIZE);
	 button_assignPlayers.setPrefHeight(USE_COMPUTED_SIZE);
	 button_assignPlayers.setMaxWidth(USE_COMPUTED_SIZE);
	 button_assignPlayers.setMaxHeight(USE_COMPUTED_SIZE);
	 button_assignPlayers.setText("Spieler hinzufügen");
	 button_assignPlayers.setId("button_assignPlayers");
	 
	 this.getChildren().add(button_assignPlayers);
	 AnchorPane.setRightAnchor(button_assignPlayers, 130.0);
	 AnchorPane.setTopAnchor(button_assignPlayers, 4.0);
	 AnchorPane.setBottomAnchor(button_assignPlayers, 5.0);
	 
	 button_assignPlayers.setOnMouseClicked(action -> {
	 lobbyCtrl.newStage(SceneMapping.ASSIGN_PLAYERS);
	 lobbyCtrl.notifySceneMessage(SceneMapping.ASSIGN_PLAYERS, tournamentID); });
	 } //TODO drin lassen?

	public String getLableTournamentID() {
		return label_tournamentName.getText();
	}

	public int getTournamentID() {
		return tournamentID;
	}

	public void setTournamentID(int gameID) {
		this.tournamentID = gameID;
	}
}
