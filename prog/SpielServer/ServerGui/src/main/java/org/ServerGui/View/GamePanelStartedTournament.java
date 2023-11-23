package org.ServerGui.View;

import org.ServerGui.Controller.LobbySceneController;
import de.upb.swtpra1819interface.models.Configuration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * <p>
 * Class provides panels for a started tournament containing the tournament's
 * id, the tournaments name, the winner's name, possibility to look up the
 * tournament's configuration and possibility to look up the tournament's
 * scorelist.
 * </p>
 */

public class GamePanelStartedTournament extends AnchorPane {

	private Label label_tournamentName;
	private Label label_tournamentRunning;
	private int tournamentID;
	private Button Button_configWheel;
	private Button button_endTournament;
	private LobbySceneController lobbyCtrl;
	private Configuration config;
	private String tournamentName;
	private VBox vbox_gameList;

	private Button button_showRanking;

	/**
	 * Constructs a panel with the tournament's id, the tournament's name, the
	 * winner's name, a possibility to look up the tournament's configuration and a
	 * possibility to look up the tournaments scorelist for an ended tournament.
	 * 
	 * @param tournamentID
	 *            id of the tournament the panel represents
	 * @param tournamentName
	 *            name of the tournament the panel represents
	 * @param config
	 *            configuration of the tournament the panel represents
	 * @param lobbyCtrl
	 *            controller of the lobby the panel is added to
	 */
	public GamePanelStartedTournament(LobbySceneController lobbyCtrl, String tournamentName, int tournamentID,
			Configuration config) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(USE_COMPUTED_SIZE);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(USE_COMPUTED_SIZE);
		this.setStyle("-fx-background-color: #6B7074;");

		createTournamentName(tournamentName);
		createStartedLabel();
		createConfigWheel(config);
		createEndTournamentButton();
		createGameList();
		createShowRanking();
		this.tournamentID = tournamentID;
		this.lobbyCtrl = lobbyCtrl;
		this.config = config;
		this.tournamentName = tournamentName;
	}

	/**
	 * Creates a label with the tournament's name
	 * 
	 * @param tournamentName
	 *            the tournament's name
	 */
	private void createTournamentName(String tournamentName) {
		label_tournamentName = new Label();
		label_tournamentName.setMinWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setMinHeight(42);
		label_tournamentName.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setPrefHeight(42);
		label_tournamentName.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setMaxHeight(42);
		label_tournamentName.setText(tournamentName);
		label_tournamentName.setId("label_tournamentName");

		this.getChildren().add(label_tournamentName);
		AnchorPane.setLeftAnchor(label_tournamentName, 4.0);
		AnchorPane.setTopAnchor(label_tournamentName, 4.0);
	}

	/**
	 * Creates a box with all games belonging to the tournament
	 * 
	 */
	private void createGameList() {
		vbox_gameList = new VBox();
		vbox_gameList.setMinWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setMinHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setPrefWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setPrefHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setMaxWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setMaxHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setId("label_gameName");

		this.getChildren().add(vbox_gameList);
		AnchorPane.setLeftAnchor(vbox_gameList, 30.0);
		AnchorPane.setRightAnchor(vbox_gameList, 0.0);
		AnchorPane.setTopAnchor(vbox_gameList, 55.0);
		AnchorPane.setBottomAnchor(vbox_gameList, 4.0);
	}

	/**
	 * Creates a label with the tournament's status (started).
	 * 
	 */
	private void createStartedLabel() {
		label_tournamentRunning = new Label();
		label_tournamentRunning.setMinWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setMinHeight(42);
		label_tournamentRunning.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setPrefHeight(42);
		label_tournamentRunning.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setMaxHeight(42);
		label_tournamentRunning.setText("Das Turnier läuft");
		label_tournamentRunning.setId("label_tournamentRunning");

		this.getChildren().add(label_tournamentRunning);
		AnchorPane.setLeftAnchor(label_tournamentRunning, 130.0);
		AnchorPane.setTopAnchor(label_tournamentRunning, 4.0);
	}

	/**
	 * Creates a button with the possibility to look up the tournament's
	 * configuration
	 * 
	 * @param config
	 *            configuration to be displayed
	 */
	private void createConfigWheel(Configuration config) {
		Button_configWheel = new Button();
		Button_configWheel.setMinWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMinHeight(42);
		Button_configWheel.setPrefWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setPrefHeight(42);
		Button_configWheel.setMaxWidth(USE_COMPUTED_SIZE);
		Button_configWheel.setMaxHeight(42);
		Button_configWheel.setText("i");
		Button_configWheel.setId("lable_infoGamepanel");
		Button_configWheel.setOnMouseClicked(action -> {
			this.lobbyCtrl.showConfigEnded(tournamentID);
		});

		this.getChildren().add(Button_configWheel);
		AnchorPane.setRightAnchor(Button_configWheel, 300.0);
		AnchorPane.setTopAnchor(Button_configWheel, 4.0);
	}

	private void createShowRanking() {
		button_showRanking = new Button();
		button_showRanking.setMinWidth(USE_COMPUTED_SIZE);
		button_showRanking.setMinHeight(42);
		button_showRanking.setPrefWidth(USE_COMPUTED_SIZE);
		button_showRanking.setPrefHeight(42);
		button_showRanking.setMaxWidth(USE_COMPUTED_SIZE);
		button_showRanking.setMaxHeight(42);
		button_showRanking.setText("Rangfolge anzeigen");
		button_showRanking.setId("button_ranking");
		button_showRanking.setOnMouseClicked(action -> {
			this.lobbyCtrl.showRanking(tournamentID);
		});

		this.getChildren().add(button_showRanking);
		AnchorPane.setRightAnchor(button_showRanking, 130.0);
		AnchorPane.setTopAnchor(button_showRanking, 4.0);
	}

	// TODO wird das benötigt?
	private void createEndTournamentButton() {
		button_endTournament = new Button();
		button_endTournament.setMinWidth(100);
		button_endTournament.setMinHeight(42);
		button_endTournament.setPrefWidth(USE_COMPUTED_SIZE);
		button_endTournament.setPrefHeight(42);
		button_endTournament.setMaxWidth(USE_COMPUTED_SIZE);
		button_endTournament.setMaxHeight(42);
		button_endTournament.setText("Turnier beenden");
		button_endTournament.setId("button_startGame");

		this.getChildren().add(button_endTournament);
		AnchorPane.setRightAnchor(button_endTournament, 4.0);
		AnchorPane.setTopAnchor(button_endTournament, 4.0);

		button_endTournament.setOnMouseClicked(action -> {
			lobbyCtrl.deleteTournamentID(tournamentID);
			lobbyCtrl.addEndedTournament(this.tournamentID, "", this.tournamentName, this.vbox_gameList);
			/*
			 * lobbyCtrl.lobbyChangeGameState(tournamentID, GameState.IN_PROGRESS); TODO
			 * muss eventuell angepasst werden, ich weiß nicht wie das mit dem Status von
			 * Turnieren ist
			 */
		});
	}

	public String getLableTournamentID() {
		return label_tournamentName.getText();
	}

	public int getTournamentID() {
		return tournamentID;
	}

	public void setTournamentID(int gameID) {
		this.tournamentID = gameID;
	}

	public VBox getVbox_gameList() {
		return vbox_gameList;
	}

	/**
	 * Adds a panel for a game to the panels gamelist
	 * 
	 * @param gameName
	 *            the game's name
	 * @param gameID
	 *            the game's id
	 * @param config
	 *            the game's configuration
	 * @param status
	 *            the number indicates the game's status. 0 for not started, 1 for
	 *            started and 2 for ended.
	 */
	public void addGame(String gameName, int gameID, Configuration config, int status) {
		//if (status == ) {
		GamePanelStartedGame panel = new GamePanelStartedGame(gameID, gameName, config, lobbyCtrl);
		vbox_gameList.getChildren().add(panel);
		//}
	}

	public void removeGame(int gameID) {
		for (Object object : vbox_gameList.getChildren().toArray()) {
			if (((GamePanelStartedGame) object).getGameID() == gameID) {
				this.vbox_gameList.getChildren().remove(object);
			}
		}
	}

	public String getTournamentName() {
		return tournamentName;
	}
}
