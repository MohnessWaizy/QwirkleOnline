package org.ServerGui.View;

import java.util.HashMap;

import org.ServerGui.Controller.LobbySceneController;
import de.upb.swtpra1819interface.models.Configuration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * <p>
 * Class provides panels for an ended tournament containing the tournament's id,
 * the tournaments name, the winner's name, possibility to look up the
 * tournament's configuration and possibility to look up the tournament's
 * scorelist.
 * </p>
 */

public class GamePanelEndedTournament extends AnchorPane {

	private Label label_tournamentName;
	private Label label_tournamentRunning;
	private int tournamentID;
	private Button Button_configWheel;
	private Button Button_scoreList;
	private LobbySceneController lobbyCtrl;
	Configuration config;
	private String gameName;
	private VBox vbox_gameList = new VBox();

	/**
	 * Constructs a panel with the tournament's id, the tournament's name, the
	 * winner's name, a possibility to look up the tournament's configuration and a
	 * possibility to look up the tournaments scorelist for an ended tournament.
	 * 
	 * @param tournamentID
	 *            id of the tournament the panel represents
	 * @param tournamentName
	 *            name of the tournament the panel represents
	 * @param winnerName
	 *            name of the winner of the tournament the panel represents
	 * @param config
	 *            configuration of the tournament the panel represents
	 * @param scoreList
	 *            scorelist of the tournament the panel represents
	 * @param vbox_gameList
	 *            vbox containing gamepanels of games belonging to tournament
	 */
	public GamePanelEndedTournament(LobbySceneController lobbyCtrl, String tournamentName, String winnerName,
			int tournamentID, VBox vBox_gameList) {
		this.setMinWidth(USE_COMPUTED_SIZE);
		this.setMinHeight(50);
		this.setPrefWidth(200);
		this.setPrefHeight(USE_COMPUTED_SIZE);
		this.setMaxWidth(USE_COMPUTED_SIZE);
		this.setMaxHeight(USE_COMPUTED_SIZE);
		this.setStyle("-fx-background-color: #6B7074;");

		createTournamentName(tournamentName);
		createEndedLabel();
		createWinnerName(winnerName);
		//this.vbox_gameList = vbox_gameList;
		createGameList(vBox_gameList);
		this.tournamentID = tournamentID;
		this.lobbyCtrl = lobbyCtrl;
		this.label_tournamentName.setText(tournamentName);
		this.gameName = tournamentName;
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
		label_tournamentName.setMinHeight(40);
		label_tournamentName.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setPrefHeight(40.0);
		label_tournamentName.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentName.setMaxHeight(40.0);
		label_tournamentName.setText(gameName);
		label_tournamentName.setId("label_gameName");

		this.getChildren().add(label_tournamentName);
		AnchorPane.setLeftAnchor(label_tournamentName, 4.0);
		AnchorPane.setTopAnchor(label_tournamentName, 4.0);
	}

	/**
	 * Creates a box with all games belonging to the tournament
	 * 
	 */
	private void createGameList(VBox gameList) {
		/*vbox_gameList = new VBox();
		vbox_gameList.setMinWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setMinHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setPrefWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setPrefHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setMaxWidth(USE_COMPUTED_SIZE);
		vbox_gameList.setMaxHeight(USE_COMPUTED_SIZE);
		vbox_gameList.setId("label_gameName");*/

		this.getChildren().add(vbox_gameList);
		AnchorPane.setLeftAnchor(vbox_gameList, 30.0);
		AnchorPane.setRightAnchor(vbox_gameList, 0.0);
		AnchorPane.setTopAnchor(vbox_gameList, 55.0);
		AnchorPane.setBottomAnchor(vbox_gameList, 4.0);
	}

	/**
	 * Creates a label with the tournament's status (ended).
	 * 
	 */
	private void createEndedLabel() {
		label_tournamentRunning = new Label();
		label_tournamentRunning.setMinWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setMinHeight(42);
		label_tournamentRunning.setPrefWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setPrefHeight(42);
		label_tournamentRunning.setMaxWidth(USE_COMPUTED_SIZE);
		label_tournamentRunning.setMaxHeight(42);
		label_tournamentRunning.setText("Das Turnier wurde beendet");
		label_tournamentRunning.setId("label_tournamentRunning");

		this.getChildren().add(label_tournamentRunning);
		AnchorPane.setLeftAnchor(label_tournamentRunning, 130.0);
		AnchorPane.setTopAnchor(label_tournamentRunning, 4.0);
	}

	/**
	 * Creates a label with the name of the tournament's winner
	 * 
	 * @param winnerName
	 *            the winner's name
	 */
	private void createWinnerName(String winnerName) {
		Label label_winnerName = new Label();
		label_winnerName.setMinWidth(USE_COMPUTED_SIZE);
		label_winnerName.setMinHeight(42);
		label_winnerName.setPrefWidth(USE_COMPUTED_SIZE);
		label_winnerName.setPrefHeight(42);
		label_winnerName.setMaxWidth(USE_COMPUTED_SIZE);
		label_winnerName.setMaxHeight(42);
		label_winnerName.setText("Gewinner:" + winnerName);
		label_winnerName.setId("label_winnerName");

		this.getChildren().add(label_winnerName);
		AnchorPane.setLeftAnchor(label_winnerName, 300.0);
		AnchorPane.setTopAnchor(label_winnerName, 4.0);
	}

	/**
	 * Creates a button with the possibility to look up the tournament's scorelist
	 * 
	 * @param scoreList
	 *            the tournament's scorelist
	 */
	private void createScoreList(HashMap<Integer, Integer> scoreList) {
		Button_scoreList = new Button();
		Button_scoreList.setMinWidth(USE_COMPUTED_SIZE);
		Button_scoreList.setMinHeight(50);
		Button_scoreList.setPrefWidth(USE_COMPUTED_SIZE);
		Button_scoreList.setPrefHeight(50);
		Button_scoreList.setMaxWidth(USE_COMPUTED_SIZE);
		Button_scoreList.setMaxHeight(50);
		Button_scoreList.setText("Rangfolge");
		Button_scoreList.setId("lable_infoGamepanel");
		Button_scoreList.setOnMouseClicked(action -> {
			// this.lobbyCtrl.showScoreListEnded(tournamentID, scoreList);
		});

		this.getChildren().add(Button_scoreList);
		AnchorPane.setLeftAnchor(Button_scoreList, 590.0);
		AnchorPane.setTopAnchor(Button_scoreList, 4.0);
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

	/**
	 * TODO vermutlich nicht benötigt Adds a panel for a game to the panels gamelist
	 * 
	 * @param gameName
	 *            the game's name
	 * @param gameID
	 *            the game's id
	 * @param winnerName
	 *            name of the game's winner
	 * @param config
	 *            the game's configuration
	 */
	public void addGame(String gameName, int gameID, String winnerName, Configuration config) {
		GamePanelEndedGame panel = new GamePanelEndedGame(gameID, gameName, winnerName, config, lobbyCtrl);
		vbox_gameList.getChildren().add(panel);
	}
}
