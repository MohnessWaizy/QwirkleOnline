package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import org.NetworkInterface.NetClient;
import de.upb.swtpra1819interface.messages.GameListRequest;
import de.upb.swtpra1819interface.messages.SpectatorJoinRequest;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import model.GameWrapper;
import model.Lobby;
import model.LoginWrapper;
import model.Tuple;
import parser.LobbyParser;

/**
 * <p>
 * The <i>LobbySceneController</i> is the controller for the lobby interface. It
 * delegates the show the game that are able to join or spectate.
 * </p>
 * 
 */
public class LobbySceneController extends AbstractSceneController implements LobbySceneControllerInterface {

	/**
	 * FXML Button to logout from lobby
	 */
	@FXML
	Button button_lobbyLogout;

	/**
	 * FXML Tab to switch to started games
	 */
	@FXML
	Tab tab_started;

	/**
	 * FXML Tab to switch to not started games
	 */
	@FXML
	Tab tab_notStarted;

	/**
	 * FXML Tab to switch to ended games
	 */
	@FXML
	Tab tab_ended;

	/**
	 * FXML Tab to switch to tournaments
	 */
	@FXML
	Tab tab_tournament;

	/**
	 * FXML VBox to hold GamePanelStartedGame instances
	 */
	@FXML
	VBox vbox_started;

	/**
	 * FXML VBox to hold GamePanelNotStartedGame instances
	 */
	@FXML
	VBox vbox_notStarted;

	/**
	 * FXML VBox to hold GamePanelEndedGame instances
	 */
	@FXML
	VBox vbox_ended;

	/**
	 * FXML VBox to hold GamePanelTournament instances
	 */
	@FXML
	VBox vbox_tournament;

	/**
	 * ArrayList holds all GamePanelStartedGame instances
	 */
	ArrayList<GamePanelStartedGame> arrayListStartedTab = new ArrayList<>();

	/**
	 * ArrayList holds all GamePanelNotStartedGame instances
	 */
	ArrayList<GamePanelNotStartedGame> arrayListNotStartedTab = new ArrayList<>();

	/**
	 * ArrayList holds all GamePanelEndedGame instances
	 */
	ArrayList<GamePanelEndedGame> arrayListEndedTab = new ArrayList<>();

	/**
	 * ArrayList holds all GamePanelTournament instances
	 */
	ArrayList<GamePanelTournament> arrayListTournamentTab = new ArrayList<>();

	/**
	 * ArrayList holds all game id's of started games
	 */
	ArrayList<Integer> idStartedGame = new ArrayList<Integer>();

	/**
	 * ArrayList holds all game id's of not started games
	 */
	ArrayList<Integer> idNotStartedGame = new ArrayList<Integer>();

	/**
	 * ArrayList holds all game id's of ended games
	 */
	ArrayList<Integer> idEndedGame = new ArrayList<Integer>();

	/**
	 * ArrayList holds all game id's of tournaments
	 */
	ArrayList<Integer> idTournament = new ArrayList<Integer>();

	/**
	 * Parser to parse messages that are sent form the server
	 */
	private LobbyParser parser;

	/**
	 * Client of NetworkInterface
	 */
	private NetClient netClient;

	/**
	 * Client for intern purpose
	 */
	private Client client;

	/**
	 * Boolean to make sure games can be updated
	 */
	public boolean gameUpdatePossible = true;

	/**
	 * Constructor
	 */
	public LobbySceneController() {
		super();
	}

	/*
	 * @see controller.AbstractSceneController#onSceneSwitch(java.lang.Object)
	 * 
	 * initializing netClient, client and parser with values passes by
	 * LoginSceneController
	 * 
	 * parser is set to parse every 5 seconds messages from server
	 */
	@Override
	public <T> void onSceneSwitch(T message) {
		if (message instanceof LoginWrapper) {
			LoginWrapper lw = (LoginWrapper) message;
			this.netClient = lw.getNetworkClient();
			this.client = lw.getInterfaceClient();
			this.parser = new LobbyParser(this, this.netClient, 1000);
			sendNewRequest();
		} else if (message instanceof Boolean) {
			this.parser = new LobbyParser(this, this.netClient, 1000);
			sendNewRequest();
		} else {
			this.logout();
		}
	}

	/**
	 * Sends new GameListRequest
	 */
	public void sendNewRequest() {
		GameListRequest msg = new GameListRequest();
		this.netClient.sendMsg(msg);
	}

	/*
	 * @see controller.AbstractSceneController#onSceneSwitch()
	 */
	@Override
	public void onSceneSwitch() {
		if (netClient != null) {
			this.parser = new LobbyParser(this, this.netClient, 1000);
		}
	}

	/*
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button_lobbyLogout.setOnAction(action -> {
			this.logout();
		});
	}

	/**
	 * Logout form lobby get to login
	 */
	private void logout() {
		parser.stopParser();
		parser.disconnect();
		switchScene(SceneMapping.LOG_IN);
	}

	/**
	 * @param gameID for game client joins Search corresponding panel and send it's
	 *               game instance with client and netClient in new GameWrapper
	 */
	public void joinGame(int gameID) {
		parser.stopParser();
		Node panel = null;
		Game game = new Game(0, null, null, false, null, null);
		if (searchGameIDStarted(gameID) != null) {
			panel = searchGameIDStarted(gameID);
			panel = (GamePanelStartedGame) panel;
		} else if (searchGameIDNotStarted(gameID) != null) {
			panel = searchGameIDNotStarted(gameID);
			panel = (GamePanelNotStartedGame) panel;
		} else if (searchGameIDEnded(gameID) != null) {
			panel = searchGameIDEnded(gameID);
			panel = (GamePanelEndedGame) panel;
		} else if (searchGameIDTournament(gameID) != null) {
			panel = searchGameIDTournament(gameID);
			panel = (GamePanelTournament) panel;
		}
		if (panel != null) {
			if (panel instanceof GamePanelStartedGame) {
				game = ((GamePanelStartedGame) panel).getGame();
			} else if (panel instanceof GamePanelNotStartedGame) {
				game = ((GamePanelNotStartedGame) panel).getGame();
			} else if (panel instanceof GamePanelEndedGame) {
				game = ((GamePanelEndedGame) panel).getGame();
			} else if (panel instanceof GamePanelTournament) {
				game = ((GamePanelTournament) panel).getGame();
			}
			GameWrapper data = new GameWrapper(this.client, this.netClient, game);
			switchScene(SceneMapping.FIELD, data);
		}
	}

	/**
	 * @param gameID Send SpectatorJoinRequest to the server with the game id
	 */
	public void joinGameRequest(int gameID) {
		parser.getClient().sendMsg(new SpectatorJoinRequest(gameID));
	}

	/**
	 * @param game GamePanelStartedGame
	 */
	public void addStartedGame(Game game) {
		GamePanelStartedGame panel = new GamePanelStartedGame(game, this);
		vbox_started.getChildren().add(panel);
		arrayListStartedTab.add(panel);
	}

	/**
	 * @param game Adds GamePanelNotStartedGame
	 */
	public void addNotStartedGame(Game game) {
		GamePanelNotStartedGame panel = new GamePanelNotStartedGame(game, this);
		vbox_notStarted.getChildren().add(panel);
		arrayListNotStartedTab.add(panel);
	}

	/**
	 * @param game Adds GamePanelEndedGame
	 */
	public void addEndedGame(Game game) {
		GamePanelEndedGame panel = new GamePanelEndedGame(game, this);
		vbox_ended.getChildren().add(panel);
		arrayListEndedTab.add(panel);
	}

	/**
	 * @param game Adds GamePanelTournament
	 */
	public void addTournament(Game game) {
		GamePanelTournament panel = new GamePanelTournament(game, this);
		vbox_tournament.getChildren().add(panel);
		arrayListTournamentTab.add(panel);
	}

	/**
	 * Deletes all game panels
	 */
	public void deleteAllPanels() {

		for (Node panel : vbox_started.getChildren()) {
			int id = ((GamePanelStartedGame) panel).getGameID();
			idStartedGame.add(id);
		}
		for (Node panel : vbox_notStarted.getChildren()) {
			int id = ((GamePanelNotStartedGame) panel).getGameID();
			idNotStartedGame.add(id);
		}
		for (Node panel : vbox_ended.getChildren()) {
			int id = ((GamePanelEndedGame) panel).getGameID();
			idEndedGame.add(id);
		}
		for (Node panel : vbox_tournament.getChildren()) {
			int id = ((GamePanelTournament) panel).getGameID();
			idTournament.add(id);
		}

		for (int id : idStartedGame) {
			deleteStartedGameID(id);
		}
		for (int id : idNotStartedGame) {
			deleteNotStartedGameID(id);
		}
		for (int id : idEndedGame) {
			deleteEndedGameID(id);
		}
		for (int id : idTournament) {
			deleteTournamentGameID(id);
		}
	}

	/**
	 * @param gameID
	 */
	public void deleteStartedGameID(int gameID) {
		vbox_started.getChildren().remove(searchGameIDStarted(gameID));
	}

	/**
	 * @param gameID
	 */
	public void deleteNotStartedGameID(int gameID) {
		vbox_notStarted.getChildren().remove(searchGameIDNotStarted(gameID));
	}

	/**
	 * @param gameID
	 */
	public void deleteEndedGameID(int gameID) {
		vbox_ended.getChildren().remove(searchGameIDEnded(gameID));
	}

	/**
	 * @param gameID
	 */
	public void deleteTournamentGameID(int gameID) {
		vbox_tournament.getChildren().remove(searchGameIDTournament(gameID));
	}

	/**
	 * @param gameID
	 * @return panel
	 */
	public Node searchGameIDStarted(int gameID) {
		for (Node panel : vbox_started.getChildren()) {
			if (((GamePanelStartedGame) panel).getGameID() == gameID) {
				return panel;
			}
		}
		;
		return null;
	}

	/**
	 * @param gameID
	 * @return panel
	 */
	public Node searchGameIDNotStarted(int gameID) {
		for (Node panel : vbox_notStarted.getChildren()) {
			if (((GamePanelNotStartedGame) panel).getGameID() == gameID) {
				return panel;
			}
		}
		;
		return null;
	}

	/**
	 * @param gameID
	 * @return panel
	 */
	public Node searchGameIDEnded(int gameID) {
		for (Node panel : vbox_ended.getChildren()) {
			if (((GamePanelEndedGame) panel).getGameID() == gameID) {
				return panel;
			}
		}
		;
		return null;
	}

	/**
	 * @param gameID
	 * @return panel
	 */
	public Node searchGameIDTournament(int gameID) {
		for (Node panel : vbox_tournament.getChildren()) {
			if (((GamePanelTournament) panel).getGameID() == gameID) {
				return panel;
			}
		}
		;
		return null;
	}

	/**
	 * @param gameID Shows configuration of panel
	 */
	public void showConfigStarted(int gameID) {
		GamePanelStartedGame panel = (GamePanelStartedGame) searchGameIDStarted(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));

	}

	/**
	 * @param gameID Shows configuration of panel
	 */
	public void showConfigNotStarted(int gameID) {
		GamePanelNotStartedGame panel = (GamePanelNotStartedGame) searchGameIDNotStarted(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));
	}

	/**
	 * @param gameID Shows configuration of panel
	 */
	public void showConfigEnded(int gameID) {
		GamePanelEndedGame panel = (GamePanelEndedGame) searchGameIDEnded(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));
	}

	/**
	 * @param gameID Shows configuration of panel
	 */
	public void showConfigTournament(int gameID) {
		GamePanelTournament panel = (GamePanelTournament) searchGameIDTournament(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));
	}

	/*
	 * @see
	 * controller.LobbySceneControllerInterface#updateGames(java.util.Collection)
	 * Updates games that are stored in ArrayLists and sorts them
	 */
	public void updateGames(Collection<Game> cGames) {
		if (this.gameUpdatePossible = true) {
			this.gameUpdatePossible = false;
			ArrayList<Game> games = new ArrayList<Game>(cGames);
			Lobby.setCurrentGames(games);
			deleteAllPanels();
			for (Game game : games) {
				if (game.getConfig() == null) {
					continue;
				}
				if ((game.getGameState() == GameState.PAUSED || game.getGameState() == GameState.IN_PROGRESS)
						&& game.isTournament() == false) {
					addStartedGame(game);
				}
				if (game.getGameState() == GameState.NOT_STARTED && game.isTournament() == false) {
					addNotStartedGame(game);
				}
				if (game.getGameState() == GameState.ENDED && game.isTournament() == false) {
					addEndedGame(game);
				}
				if ((game.getGameState() == GameState.PAUSED || game.getGameState() == GameState.IN_PROGRESS) && game.isTournament() == true) {
					addTournament(game);
				}
				if (game.getGameState() == GameState.NOT_STARTED && game.isTournament() == true) {
					addTournament(game);
				}
				if (game.getGameState() == GameState.ENDED && game.isTournament() == true) {
					addTournament(game);
				}
			}
		this.gameUpdatePossible = true;
		}

	}
}
