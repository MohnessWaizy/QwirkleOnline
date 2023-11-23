package org.ServerGui.Controller;

import org.ServerGui.Model.DataContainer;
import org.ServerGui.Model.GuiCommunication;
import org.ServerGui.Model.Tuple;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.CostumMessages.*;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Game;
import javafx.application.*;

public class NetworkController implements Runnable {

	private SuperController controller;
	private GuiCommunication guiCommunication;
	private DataContainer dataContainer;
	private volatile boolean running = true;

	public NetworkController(SuperController sc, GuiCommunication gc, DataContainer d) {
		this.controller = sc;
		this.guiCommunication = gc;
		this.dataContainer = d;
	}

	public void shutdown() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			Message message = null;
			if ((message = guiCommunication.pollNextMessage()) != null) {
				if (message instanceof ClientConnect) {
					ClientConnect clientConnects = (ClientConnect) message;

					// Remove player from all lists
					dataContainer.getPlayerIngame().remove(clientConnects.getClient());
					dataContainer.getPlayerInLobby().remove(clientConnects.getClient());
					for (ArrayList<Client> clients : dataContainer.getGamePlayerMap().values()) {
						clients.remove(clientConnects.getClient());
					}

					Platform.runLater(() -> controller.notifyScene(SceneMapping.ASSIGN_PLAYERS, clientConnects));
				} else if (message instanceof ClientJoinGame) {
					ClientJoinGame clientJoinGame = (ClientJoinGame) message;
					// Add Player to game and remove from lobby and other games
					dataContainer.getGamePlayerMap().get(clientJoinGame.getGame().getGameId())
							.add(clientJoinGame.getClient());
					dataContainer.getPlayerInLobby().remove(clientJoinGame.getClient());
					if (dataContainer.getPlayerIngame().remove(clientJoinGame.getClient())) {
						for (ArrayList<Client> clients : dataContainer.getGamePlayerMap().values()) {
							clients.remove(clientJoinGame.getClient());
						}
					}
					dataContainer.getPlayerIngame().add(clientJoinGame.getClient());
					if (!clientJoinGame.getGame().isTournament()) {
						Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY, clientJoinGame));
					}
				} else if (message instanceof FinishGame) {
					int tournamentID = 0;
					FinishGame finishGame = (FinishGame) message;
					Game game = finishGame.getGame();

					if (game.isTournament() == true) {
						for (Entry<Integer, ArrayList<Game>> entry : dataContainer.getTournamentGameMap().entrySet()) {
							if (entry.getValue().contains(game)) {
								tournamentID = entry.getKey();
								break;
							}
						}

						final int realTournamentId = tournamentID;
						Hashtable<Client, Tuple<Integer, Integer>> scoreMap = dataContainer
								.getTournamentClientScoreMap().get(tournamentID);
						Client winner = finishGame.getWinner().getClient();
						int gamesWon = scoreMap.get(winner).getFirst();
						scoreMap.get(winner).setFirst(gamesWon + 1);
						Map<Client, Integer> leaderboard = finishGame.getWinner().getLeaderboard();
						for (Entry<Client, Integer> entry : leaderboard.entrySet()) {
							Client client = (Client) entry.getKey();
							int score = (int) entry.getValue();
							int totalScore = scoreMap.get(client).getSecond();
							int newScore = score + totalScore;
							gamesWon = scoreMap.get(client).getFirst();
							scoreMap.put(client, new Tuple<Integer, Integer>(gamesWon, newScore));
						}
						dataContainer.getTournamentClientScoreMap().put(tournamentID, scoreMap);

						Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY,
								new Tuple<FinishGame, Integer>(finishGame, realTournamentId)));
					}

				} else if (message instanceof NewGame) {
					if (((NewGame) message).isTournament() == false) {
						NewGame newGame = (NewGame) message;
						ArrayList<Client> playerList = (ArrayList<Client>) newGame.getGame().getPlayers() != null
								? (ArrayList<Client>) newGame.getGame().getPlayers()
								: new ArrayList<Client>();
						dataContainer.getGamePlayerMap().put(newGame.getGameId(), playerList);
						dataContainer.getGameIdMap().put(newGame.getGameId(), newGame.getGame());
						Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY, newGame));
					} else if (((NewGame) message).isTournament() == true) {
						NewGame newGame = (NewGame) message;
						int tournamentID = newGame.getTournamentId();
						Game game = newGame.getGame();
						ArrayList<Client> playerList = (ArrayList<Client>) newGame.getGame().getPlayers() != null
								? (ArrayList<Client>) newGame.getGame().getPlayers()
								: new ArrayList<Client>();
						dataContainer.getGamePlayerMap().put(newGame.getGameId(), playerList);
						dataContainer.getGameIdMap().put(newGame.getGameId(), newGame.getGame());
						dataContainer.getTournamentGameMap().get(tournamentID).add(game);
						Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY,
								new Tuple<NewGame, Integer>(newGame, tournamentID)));
					}

				} else if (message instanceof NewTournament) {
					NewTournament newTournament = (NewTournament) message;
					int tournamentID = newTournament.getTournamentId();
					dataContainer.getTournamentGameMap().put(tournamentID, new ArrayList<Game>());
					Hashtable<Client, Tuple<Integer, Integer>> scoreMap = new Hashtable<Client, Tuple<Integer, Integer>>();
					ArrayList<Client> players = newTournament.getClients();
					for (Client client : players) {
						scoreMap.put(client, new Tuple<Integer, Integer>(0, 0));
					}
					dataContainer.getTournamentClientScoreMap().put(tournamentID, scoreMap);
					Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY, newTournament));
				} else if (message instanceof Disconnect) {
					Disconnect disconnect = (Disconnect) message;

					ArrayList<Client> toRemove = new ArrayList<Client>();
					for (Client client : dataContainer.getPlayerInLobby()) {
						if (client.getClientId() == disconnect.getClientId()) {
							toRemove.add(client);
						}
					}
					dataContainer.getPlayerInLobby().removeAll(toRemove);
					toRemove.clear();
					for (Client client : dataContainer.getPlayerIngame()) {
						if (client.getClientId() == disconnect.getClientId()) {
							toRemove.add(client);
							for (ArrayList<Client> clients : dataContainer.getGamePlayerMap().values()) {
								ArrayList<Client> toRemoveIntern = new ArrayList<Client>();
								for (Client removeClients : clients) {
									if (removeClients.getClientId() == disconnect.getClientId()) {
										toRemoveIntern.add(removeClients);
									}
								}
								clients.removeAll(toRemoveIntern);
							}
						}
					}
					dataContainer.getPlayerInLobby().removeAll(toRemove);

				} else if (message instanceof GameStartError) {
					GameStartError gameStartError = (GameStartError) message;

				} else if (message instanceof GameAbortError) {
					GameAbortError gameAbortError = (GameAbortError) message;

				} else if (message instanceof GamePausedError) {
					GamePausedError gamePausedError = (GamePausedError) message;

				} else if (message instanceof GameResumeError) {
					GameResumeError gameResumeError = (GameResumeError) message;

				} else if (message instanceof FinishTournament) {
					FinishTournament finishTournament = (FinishTournament) message;
					Platform.runLater(() -> controller.notifyScene(SceneMapping.LOBBY, finishTournament));
				}
			} else {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
