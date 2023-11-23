package org.ServerGui.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.CostumMessages.ChangeGameState;
import org.CostumMessages.ClientJoinGame;
import org.CostumMessages.FinishGame;
import org.CostumMessages.FinishTournament;
import org.CostumMessages.NewGame;
import org.CostumMessages.NewTournament;
import org.CostumMessages.Shutdown;
import org.ServerGui.Model.Tuple;
import org.ServerGui.View.GamePanelEndedGame;
import org.ServerGui.View.GamePanelEndedTournament;
import org.ServerGui.View.GamePanelNotStartedGame;
import org.ServerGui.View.GamePanelNotStartedTournament;
import org.ServerGui.View.GamePanelStartedGame;
import org.ServerGui.View.GamePanelStartedTournament;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <p>
 * Controller for scene Lobby. Used to display games and tournaments.
 * </p>
 */

public class LobbySceneController extends AbstractSceneController {

	@FXML
	Button button_terminateServer;
	@FXML
	Tab tab_started;
	@FXML
	Tab tab_notStarted;
	@FXML
	Tab tab_ended;
	@FXML
	Tab tab_tournament;
	@FXML
	VBox vbox_started;
	@FXML
	VBox vbox_notStarted;
	@FXML
	VBox vbox_ended;
	@FXML
	VBox vbox_tournament;
	@FXML
	Button button_createGame;
	@FXML
	Button button_addTournament;

	ArrayList<GamePanelStartedGame> arrayListStartedTab = new ArrayList<>();
	ArrayList<GamePanelNotStartedGame> arrayListNotStartedTab = new ArrayList<>();
	ArrayList<GamePanelEndedGame> arrayListEndedTab = new ArrayList<>();
	ArrayList<GamePanelNotStartedTournament> arrayListNotStartedTournament = new ArrayList<>();
	ArrayList<GamePanelStartedTournament> arrayListStartedTournament = new ArrayList<>();
	ArrayList<GamePanelEndedTournament> arrayListEndedTournament = new ArrayList<>();

	public LobbySceneController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SuperController.setLobbySceneController(this);

		this.button_createGame.setOnMouseClicked(action -> {
			addSubStage(SceneMapping.SAVED_OR_NEW_GAME);
		});

		this.button_addTournament.setOnMouseClicked(action -> {
			addSubStage(SceneMapping.SAVED_OR_NEW_TOURNAMENT);
		});

		this.button_terminateServer.setOnMouseClicked(action -> {
			Stage stage = (Stage) this.button_terminateServer.getScene().getWindow();
			stage.close();
			supCtrl.sendMessage(new Shutdown());
		});
	}

	/**
	 * Function adds panel for a started game
	 * 
	 * @param gameID
	 *            the new game's ID
	 * @param gameName
	 *            the new game's name
	 * @param configWheel
	 *            the new game's configuration
	 */
	public void addStartedGame(int gameID, String gameName, Configuration configWheel) {
		GamePanelStartedGame panel = new GamePanelStartedGame(gameID, gameName, configWheel, this);
		vbox_started.getChildren().add(panel);
		arrayListStartedTab.add(panel);
	}

	/**
	 * Function adds panel for a not started game
	 * 
	 * @param gameID
	 *            the new game's ID
	 * @param gameName
	 *            the new game's name
	 * @param configWheel
	 *            the new game's configuration
	 */
	public void addNotStartedGame(int gameID, String gameName, Configuration configWheel) {
		GamePanelNotStartedGame panel = new GamePanelNotStartedGame(gameID, gameName, configWheel, this);
		vbox_notStarted.getChildren().add(panel);
		arrayListNotStartedTab.add(panel);
	}

	/**
	 * Function adds panel for not started tournament
	 * 
	 * @param tournamentName
	 *            name of created tournament
	 * @param tournamentID
	 *            unique id to identify tournament
	 * @param cardsPerPlayer
	 *            amount of cards each player gets TODO
	 * @param numberOfPlayers
	 *            max number of players allowed in game TODO
	 * @param config
	 *            configuration for tournament games
	 * @joinButton TODO
	 */
	public void addNotStartedTournament(String tournamentName, int tournamentID, Configuration config) {
		GamePanelNotStartedTournament panel = new GamePanelNotStartedTournament(this, tournamentName, tournamentID,
				config);
		vbox_tournament.getChildren().add(panel);
		arrayListNotStartedTournament.add(panel);
	}

	/**
	 * Function adds panel for started tournament
	 * 
	 * @param tournamentName
	 *            name of created tournament
	 * @param tournamentID
	 *            unique id to identify tournament
	 * @param config
	 *            configuration for tournament games
	 * @joinButton TODO
	 */
	public void addStartedTournament(String tournamentName, int gameID, Configuration config) {
		GamePanelStartedTournament panel = new GamePanelStartedTournament(this, tournamentName, gameID, config);
		vbox_tournament.getChildren().add(panel);
		arrayListStartedTournament.add(panel);
	}

	/**
	 * Function adds panel for an ended game
	 * 
	 * @param gameID
	 *            the new game's ID
	 * @param gameName
	 *            the new game's name
	 * @param gameWinner
	 *            name of the game's winner
	 * @param configWheel
	 *            the new game's configuration
	 */
	public void addEndedGame(int gameID, String gameName, String gameWinner, Configuration configWheel) {
		GamePanelEndedGame panel = new GamePanelEndedGame(gameID, gameName, gameWinner, configWheel, this);
		vbox_ended.getChildren().add(panel);
		arrayListEndedTab.add(panel);
	}

	/**
	 * Function adds panel for ended tournament to lobby
	 * 
	 * @param tournamentID
	 *            the new tournament's ID
	 * @param tournamentName
	 *            the new game's name
	 * @param winnerName
	 *            name of the tournament's winner
	 * @param config
	 *            the new game's configuration
	 * @param scoreList
	 *            list of players and their scores
	 */
	public void addEndedTournament(int tournamentID, String tournamentName, String winnerName, VBox vbox_gameList) {
		/*
		 * TODO Der Parameter scoreList wird benötigt, um die Reihenfolge anzuzeigen.
		 * Beim Empfangen der FinishGame Nachricht muss das WinnerObjekt entsprechend
		 * ausgelesen werden und in die scoreList dann id -> score eingetragen werden.
		 * Diese Liste wird dann hier übergeben
		 */
		GamePanelEndedTournament panel = new GamePanelEndedTournament(this, tournamentName, winnerName, tournamentID,
				vbox_gameList);
		vbox_tournament.getChildren().add(panel);
		arrayListEndedTournament.add(panel);
	}

	/**
	 * Function removes started game from lobby list
	 * 
	 * @param gameID
	 *            ID of the game to be deleted
	 */
	public void deleteStartedGameID(int gameID) {
		vbox_started.getChildren().remove(searchGameIDStarted(gameID));
	}

	/**
	 * Function removes not started game from lobby list
	 * 
	 * @param gameID
	 *            ID of the game to be deleted
	 */
	public void deleteNotStartedGameID(int gameID) {
		vbox_notStarted.getChildren().remove(searchGameIDNotStarted(gameID));
	}

	/**
	 * Function removes ended game from lobby list
	 * 
	 * @param gameID
	 *            ID of the game to be deleted
	 */
	public void deleteEndedGameID(int gameID) {
		vbox_ended.getChildren().remove(searchGameIDEnded(gameID));
	}

	/**
	 * Function removes torunament from lobby list
	 * 
	 * @param tournamentID
	 *            ID of the game to be deleted
	 */
	public void deleteTournamentID(int tournamentID) {
		vbox_tournament.getChildren().remove(searchTournamentID(tournamentID));
	}

	public void lobbyChangeGameState(int gId, GameState gs) {
		supCtrl.sendMessage(new ChangeGameState(gId, gs));
	}

	public void lobbyChangeGameState(int gameID, GameState gameState, Client client) {
		supCtrl.sendMessage(new ChangeGameState(gameID, gameState, client));
	}

	/**
	 * Function searches started game with given id
	 * 
	 * @param gameID
	 *            ID of the game to be found
	 * 
	 * @return panel belonging to game with given id
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
	 * Function searches not started game with given id
	 * 
	 * @param gameID
	 *            ID of the game to be found
	 * 
	 * @return panel belonging to game with given id
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
	 * Function searches ended game with given id
	 * 
	 * @param gameID
	 *            ID of the game to be found
	 * 
	 * @return panel belonging to game with given id
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
	 * Function searches tournament with given id
	 * 
	 * @param tournamentID
	 *            ID of the game to be found
	 * 
	 * @return panel belonging to game with given id
	 */
	public Node searchTournamentID(int tournamentID) {
		for (Node panel : vbox_tournament.getChildren()) {
			if (panel instanceof GamePanelStartedTournament) {
				if (((GamePanelStartedTournament) panel).getTournamentID() == tournamentID) {
					return panel;
				}
			}

			if (panel instanceof GamePanelEndedTournament) {
				if (((GamePanelEndedTournament) panel).getTournamentID() == tournamentID) {
					return panel;
				}
			}
		}
		;
		return null;
	}

	/**
	 * Function creates new Stage with given scene
	 * 
	 * @param sm
	 *            scene to be displayed on new stage
	 */
	public void newStage(SceneMapping sm) {
		this.addSubStage(sm);
	}

	/**
	 * Function processes message send by previous scene on scene switch
	 * 
	 * @param message
	 *            message sent by previous scene
	 */
	public <T> void reciveMessage(T message) {
		if (message instanceof NewGame) {
			NewGame newGame = (NewGame) message;
			if (newGame.getGame() == null)
				return;
			else if (newGame.getGame().isTournament() == false) {
				switch (newGame.getGame().getGameState()) {
				case NOT_STARTED:
					this.addNotStartedGame(newGame.getGameId(), newGame.getGameName(), newGame.getConfig());
					break;
				case PAUSED:
					this.addStartedGame(newGame.getGameId(), newGame.getGameName(), newGame.getConfig());
					break;
				case IN_PROGRESS:
					this.addStartedGame(newGame.getGameId(), newGame.getGameName(), newGame.getConfig());
					break;
				default:
					break;
				}
			} else if (newGame.getGame().isTournament() == true) {
			}
		} else if (message instanceof NewTournament) {
			NewTournament newTournament = (NewTournament) message;
			this.addStartedTournament(newTournament.getTournamentName(), newTournament.getTournamentId(),
					newTournament.getConfig());
		} else if (message instanceof Tuple<?, ?>) {
			if (((Tuple) message).getFirst() instanceof NewGame) {
				Tuple<NewGame, Integer> tuple = (Tuple<NewGame, Integer>) message;
				NewGame newGame = (NewGame) tuple.getFirst();
				Game game = newGame.getGame();
				int tournamentID = (int) tuple.getSecond();
				Node tournamentPanel = this.searchTournamentID(tournamentID);

				if (tournamentPanel instanceof GamePanelStartedTournament) {
					GamePanelStartedTournament panel = (GamePanelStartedTournament) tournamentPanel;
					switch (newGame.getGame().getGameState()) {
					case NOT_STARTED:
						panel.addGame(game.getGameName(), game.getGameId(), game.getConfig(), 0);
						break;
					case PAUSED:
						panel.addGame(game.getGameName(), game.getGameId(), game.getConfig(), 1);
						break;
					case IN_PROGRESS:
						panel.addGame(game.getGameName(), game.getGameId(), game.getConfig(), 1);
						break;
					default:
						break;
					}
				}

				if (tournamentPanel instanceof GamePanelEndedTournament) {
					// hier Fehlerbehandlung? TODO
				}

				if (tournamentPanel instanceof GamePanelNotStartedTournament) {
					// hier Fehlerbehandlung? TODO
				}
			} else if (((Tuple) message).getFirst() instanceof FinishGame) {
				FinishGame finishGame = (FinishGame) ((Tuple) message).getFirst();
				int tournamentID = (int) ((Tuple) message).getSecond();
				Game game = finishGame.getGame();
				Node tournamentPanel = this.searchTournamentID(tournamentID);

				if (tournamentPanel instanceof GamePanelStartedTournament) {
					GamePanelStartedTournament panel = (GamePanelStartedTournament) tournamentPanel;
					panel.removeGame(game.getGameId());
					panel.addGame(game.getGameName(), game.getGameId(), game.getConfig(), 2);
				}

				if (tournamentPanel instanceof GamePanelEndedTournament) {
					// hier Fehlerbehandlung? TODO
				}

				if (tournamentPanel instanceof GamePanelNotStartedTournament) {
					// hier Fehlerbehandlung? TODO
				}
			}
		} else if (message instanceof FinishGame) {
			FinishGame finishGame = (FinishGame) message;
			Game game = finishGame.getGame();
			this.deleteStartedGameID(finishGame.getGameId());
			this.addEndedGame(finishGame.getGameId(), game.getGameName(),
					finishGame.getWinner().getClient().getClientName(), game.getConfig());
		} else if (message instanceof ClientJoinGame) {
			ClientJoinGame clientJoinGame = (ClientJoinGame) message;
			this.deleteNotStartedGameID(clientJoinGame.getGame().getGameId());
			this.addNotStartedGame(clientJoinGame.getGame().getGameId(), clientJoinGame.getGame().getGameName(),
					clientJoinGame.getGame().getConfig());
		} else if (message instanceof FinishTournament) {
			FinishTournament finishTournament = (FinishTournament) message;
			Client winner = finishTournament.getWinner();
			int tournamentID = finishTournament.getTournamentId();
			GamePanelStartedTournament panel = (GamePanelStartedTournament) this.searchTournamentID(tournamentID);
			VBox gameList = panel.getVbox_gameList();
			String tournamentName = panel.getTournamentName();
			this.deleteTournamentID(tournamentID);
			this.addEndedTournament(tournamentID, tournamentName, winner.getClientName(), gameList);
		}
	}

	/**
	 * Function opens new stage to display a started game's configuration
	 * 
	 * @param gameID
	 *            id belonging to game whose configuration is to be shown
	 */
	public void showConfigStarted(int gameID) {
		GamePanelStartedGame panel = (GamePanelStartedGame) searchGameIDStarted(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));

	}

	/**
	 * Function opens new stage to display a not started game's configuration
	 * 
	 * @param gameID
	 *            id belonging to game whose configuration is to be shown
	 */
	public void showConfigNotStarted(int gameID) {
		GamePanelNotStartedGame panel = (GamePanelNotStartedGame) searchGameIDNotStarted(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));

	}

	/**
	 * Function opens new stage to display an ended game's configuration
	 * 
	 * @param gameID
	 *            id belonging to game whose configuration is to be shown
	 */
	public void showConfigEnded(int gameID) {
		GamePanelEndedGame panel = (GamePanelEndedGame) searchGameIDEnded(gameID);
		this.addSubStage(SceneMapping.CONFIG);
		notifySceneMessage(SceneMapping.CONFIG,
				new Tuple<String, Configuration>(panel.getGameName(), panel.getConfiguration()));

	}

	/**
	 * Function opens new stage to display an ended game's scorelist
	 * 
	 * @param gameID
	 *            id belonging to game whose scorelist is to be shown
	 */
	public void showRanking(int tournamentID) {
		Hashtable<Client, Tuple<Integer, Integer>> scoreList = this.getSupCtrl().getData().getTournamentClientScoreMap()
				.get(tournamentID);
		this.addSubStage(SceneMapping.RANKING);
		notifySceneMessage(SceneMapping.RANKING, scoreList);
	}
}
