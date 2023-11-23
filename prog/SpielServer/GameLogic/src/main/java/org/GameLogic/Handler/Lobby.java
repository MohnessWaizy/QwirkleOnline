package org.GameLogic.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.GameLogic.Util.MessageCodes;
import org.GameLogic.Util.Tournament;
import org.NetworkInterface.MessageWithClientId;
import org.ServerGui.Model.GuiCommunication;

import de.upb.swtpra1819interface.messages.AccessDenied;
import de.upb.swtpra1819interface.messages.GameJoinRequest;
import de.upb.swtpra1819interface.messages.GameListResponse;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.SpectatorJoinRequest;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;

import org.BasicCommunication.BasicCommunicationHandler;
import org.CostumMessages.ChangeGameState;
import org.CostumMessages.ClientConnect;
import org.CostumMessages.ClientJoinGame;
import org.CostumMessages.MoveToGame;
import org.CostumMessages.NewGame;
import org.CostumMessages.NewTournament;
import org.CostumMessages.Disconnect;
import org.CostumMessages.FinishGame;
import org.CostumMessages.FinishTournament;
import org.CostumMessages.GameAbortError;
import org.CostumMessages.GamePausedError;
import org.CostumMessages.GameResumeError;
import org.CostumMessages.GameStartError;

/**
 * 
 * Lobby of the game server
 *
 */
public class Lobby extends BasicCommunicationHandler {

	private GameManagement gameManagement;
	private Map<Integer, Client> idMapping;
	private Map<Integer, Game> gameMapping;
	private Set<Client> clientsInLobby;
	private GuiCommunication guiCommunication;
	private Map<Integer, Tournament> tournamentMapping;

	public Lobby(GameManagement gameManagement, GuiCommunication guiCommunication) {
		this.gameManagement = gameManagement;
		this.guiCommunication = guiCommunication;

		this.clientsInLobby = new HashSet<Client>();
		this.idMapping = new HashMap<Integer, Client>();
		this.gameMapping = new HashMap<Integer, Game>();
		this.tournamentMapping = new HashMap<Integer, Tournament>();
	}

	/**
	 * 
	 * Joins a client as a player to a game
	 * 
	 * @param gameJoinRequest
	 *            gameJoinRequest Request that should be handled
	 * @param clientId
	 */
	public void gameJoin(GameJoinRequest gameJoinRequest, int clientId) {
		Game game = gameMapping.get(gameJoinRequest.getGameId());
		if (idMapping.get(clientId).getClientType() == ClientType.PLAYER && game.isTournament() == false
				&& clientsInLobby.contains(idMapping.get(clientId)) && game.getGameState() == GameState.NOT_STARTED
				&& game.getConfig().getMaxPlayerNumber() > getClientsWithoutSpectators(game.getPlayers()).size()) {
			gameMapping.get(gameJoinRequest.getGameId()).getPlayers().add(idMapping.get(clientId));
			if (!gameManagement.moveToGame(gameMapping.get(gameJoinRequest.getGameId()), idMapping.get(clientId))) {
				throw new RuntimeException("wrong usage, not correct");
			} else {
				Client playerToRemove = idMapping.remove(clientId);
				guiCommunication.addToMessageQueue(
						new ClientJoinGame(gameMapping.get(gameJoinRequest.getGameId()), playerToRemove));
				clientsInLobby.remove(playerToRemove);
			}
		} else {
			if (idMapping.get(clientId).getClientType() == ClientType.PLAYER) {
				gameManagement.sendMsg(new NotAllowed(
						"Client nicht in der Lobby oder Game schon gestartet/beendet oder Game voll oder Tournament",
						MessageCodes.GAMEJOINREQUEST), clientId);
			} else {
				gameManagement.sendMsg(new AccessDenied("Not a Player", MessageCodes.GAMEJOINREQUEST), clientId);
			}
		}
	}

	/**
	 * 
	 * Joins a client as a spectator to a game
	 * 
	 * @param spectatorJoinRequest
	 *            Request that should be handled
	 * @param clientId
	 */
	public void spectatorJoin(SpectatorJoinRequest spectatorJoinRequest, int clientId) {
		if (clientsInLobby.contains(idMapping.get(clientId))) {
			Client spectatorClient = idMapping.get(clientId);
			spectatorClient.setClientType(ClientType.SPECTATOR);
			if (!gameManagement.moveToGame(gameMapping.get(spectatorJoinRequest.getGameId()), spectatorClient)) {
				throw new RuntimeException("wrong usage, not correct");
			} else {
				Client spectatorToRemove = idMapping.remove(clientId);
				Game game = gameMapping.get(spectatorJoinRequest.getGameId());
				guiCommunication.addToMessageQueue(new ClientJoinGame(game, spectatorClient));
				clientsInLobby.remove(spectatorToRemove);
			}
		} else {
			gameManagement.sendMsg(new NotAllowed("Client nicht in der Lobby", MessageCodes.GAMEJOINREQUEST), clientId);
		}
	}

	/**
	 * 
	 * Removes all Spectators from a list of Clients
	 * 
	 * @param clientsList
	 *            List of players of a game
	 * @return list of Players of a game without the Spectators
	 */
	private HashSet<Client> getClientsWithoutSpectators(Collection<Client> clientsList) {

		HashSet<Client> clearedList = new HashSet<Client>();
		HashSet<Client> clients = new HashSet<Client>(clientsList);
		for (Client j : clients) {
			if (j.getClientType() == ClientType.PLAYER) {
				clearedList.add(j);
			}
		}
		return clearedList;
	}

	/**
	 * 
	 * Creates a new tournament and starts the first round
	 * 
	 * @param newTournament
	 *            Message object of a new tournament
	 */
	private void createNewTournament(NewTournament newTournament) {
		if (tournamentMapping.get(newTournament.getTournamentId()) != null) {
			throw new RuntimeException("Such a tournament already exists");
		} else {
			guiCommunication.addToMessageQueue(newTournament);
			Tournament tournament = new Tournament(newTournament.getTournamentName(), newTournament.getConfig());
			tournament.setClientsNotInGame(new ArrayList<Client>(newTournament.getClients()));
			tournamentMapping.put(newTournament.getTournamentId(), tournament);
			createGamesAndMoveClientsForTournament(newTournament.getTournamentId());
		}
	}

	/**
	 * 
	 * Creates a new round of a tournament if needed
	 * 
	 * @param tournamentId
	 *            Id of the tournament
	 */
	private void nextTournamentRound(int tournamentId) {
		if (tournamentMapping.get(tournamentId).getClients().size() < 2) {
			guiCommunication.addToMessageQueue(
					new FinishTournament(tournamentId, tournamentMapping.get(tournamentId).getClients().get(0)));
			tournamentMapping.remove(tournamentId);
		} else {
			createGamesAndMoveClientsForTournament(tournamentId);
		}
	}

	/**
	 * 
	 * Creates the right number of games for a tournament. If the number of players
	 * are uneven a 1 vs 1 vs 1 is created.
	 * 
	 * @param tournamentId
	 *            Id of the tournament
	 */
	private void createGamesAndMoveClientsForTournament(int tournamentId) {
		int clientCount = tournamentMapping.get(tournamentId).getClients().size();
		int clientIndex = 0;
		for (int i = 0; i < Math.floor(clientCount / 2); i++) {
			// create message for GUI
			NewGame newGame = new NewGame("TournamentId:" + tournamentId + " Game " + i, true,
					tournamentMapping.get(tournamentId).getConfig());
			// create CommunciationHandler for game
			newGame.setGameId(gameManagement.createGame(newGame.getGame()));
			// bindings
			gameMapping.put(newGame.getGameId(), newGame.getGame());
			// safety for null values
			if (gameMapping.get(newGame.getGameId()).getPlayers() == null) {
				gameMapping.get(newGame.getGameId()).setPlayers(new ArrayList<Client>());
			}
			// set tournament
			newGame.setTournamentId(tournamentId);
			guiCommunication.addToMessageQueue(newGame);
			// add game to t-map
			tournamentMapping.get(tournamentId).getGames().add(newGame.getGame());
			for (int j = 0; j < 2; j++) {
				// move Clients into the game
				Client movedClient = tournamentMapping.get(tournamentId).getClients().get(clientIndex);

				if (!gameManagement.moveToGame(gameMapping.get(newGame.getGameId()), movedClient)) {
					clientCount--;
				} else {
					Client clientToRemove = idMapping.remove(movedClient.getClientId());
					guiCommunication
							.addToMessageQueue(new ClientJoinGame(gameMapping.get(newGame.getGameId()), movedClient));
					clientsInLobby.remove(clientToRemove);
				}

				// handle uneven client count
				clientIndex++;
				if (clientIndex + 1 == clientCount) {
					j--;
				} else if (clientIndex >= clientCount) {
					break;
				}
			}

			// autostart each game
			gameMapping.get(newGame.getGameId()).setGameState(GameState.IN_PROGRESS);
			gameManagement.startGame(newGame.getGameId());
		}
		tournamentMapping.get(tournamentId).getClients().clear();
	}

	/**
	 * 
	 * Removes a client from a game
	 * 
	 * @param client
	 *            Client to be removed
	 * @param gameId
	 *            Id of the game to remove from
	 * @return whether the removal was a success
	 */
	public boolean removeClientFromGame(Client client, int gameId) {
		return (gameMapping.get(gameId).getPlayers().remove(client));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (canReceiveMessage()) {
			MessageWithClientId messageWithClientId = pollNextMessage();

			if (messageWithClientId != null) {
				Message message = messageWithClientId.getMsg();
				int clientId = messageWithClientId.getClientId();

				switch (message.getUniqueId()) {
				case MessageCodes.GAMELISTREQUEST:
					HashSet<Game> games = new HashSet<Game>(gameMapping.values());

					for (Game i : games) {
						i.setPlayers(getClientsWithoutSpectators(i.getPlayers()));
					}

					gameManagement.sendMsg(new GameListResponse(games), clientId);
					break;
				case MessageCodes.GAMEJOINREQUEST:
					gameJoin((GameJoinRequest) message, clientId);
					break;
				case MessageCodes.SPECTATORJOINREQUEST:
					spectatorJoin((SpectatorJoinRequest) message, clientId);
					break;
				case MessageCodes.CLIENTCONNECT:
					Client connectClient = ((ClientConnect) message).getClient();
					idMapping.put(connectClient.getClientId(), connectClient);
					clientsInLobby.add(connectClient);
					guiCommunication.addToMessageQueue(message);
					break;
				case MessageCodes.NEWGAME:
					NewGame newGame = (NewGame) message;
					newGame.setGameId(gameManagement.createGame(newGame.getGame()));
					if (newGame.getGameId() != -1) {
						gameMapping.put(newGame.getGameId(), newGame.getGame());
						gameMapping.get(newGame.getGameId()).setPlayers(new ArrayList<Client>());
						guiCommunication.addToMessageQueue(newGame);
					} else {
						throw new RuntimeException("wrong usage of Newgame, missing data");
					}
					break;
				case MessageCodes.CHANGEGAMESTATE:
					ChangeGameState changeGameState = (ChangeGameState) message;
					switch (changeGameState.getState()) {
					case IN_PROGRESS:
						if (gameMapping.get(changeGameState.getGameId()).getGameState() == GameState.NOT_STARTED) {
							gameManagement.startGame(changeGameState.getGameId());
						} else if (gameMapping.get(changeGameState.getGameId()).getGameState() == GameState.PAUSED) {
							gameManagement.resumeGame(changeGameState.getGameId());
						}
						gameMapping.get(changeGameState.getGameId()).setGameState(GameState.IN_PROGRESS);
						break;
					case PAUSED:
						gameMapping.get(changeGameState.getGameId()).setGameState(GameState.PAUSED);
						gameManagement.pauseGame(changeGameState.getGameId());
						break;
					case ENDED:
						gameMapping.get(changeGameState.getGameId()).setGameState(GameState.ENDED);
						gameManagement.abortGame(changeGameState.getGameId(), changeGameState.getClient());
					default:
						break;
					}

					break;
				case MessageCodes.SHUTDOWN:
					gameManagement.notRunning();
					break;
				case MessageCodes.MOVETOGAME:
					MoveToGame moveToGame = (MoveToGame) message;
					Client movedClient = idMapping.get(moveToGame.getClientId());
					movedClient.setClientType(moveToGame.getClientType());

					if (!gameManagement.moveToGame(gameMapping.get(moveToGame.getGameId()), movedClient)) {
						throw new RuntimeException("wrong usage, not correct");
					} else {

						Client clientToRemove = idMapping.remove(movedClient.getClientId());
						if (clientToRemove.getClientType() != ClientType.SPECTATOR) {
							gameMapping.get(moveToGame.getGameId()).getPlayers().add(clientToRemove);
						}

						guiCommunication.addToMessageQueue(
								new ClientJoinGame(gameMapping.get(moveToGame.getGameId()), movedClient));
						clientsInLobby.remove(clientToRemove);
					}

					break;
				case MessageCodes.FINISHGAME:
					FinishGame finishGame = (FinishGame) message;
					finishGame.setGame(gameMapping.get(finishGame.getGameId()));
					guiCommunication.addToMessageQueue(finishGame);

					gameMapping.get(finishGame.getGameId()).setGameState(GameState.ENDED);
					if (gameMapping.get(finishGame.getGameId()).isTournament()) {
						for (Map.Entry<Integer, Tournament> entry : tournamentMapping.entrySet()) {
							Tournament tournament = entry.getValue();
							Set<Game> toRemove = new HashSet<Game>();
							for (Game game : tournament.getGames()) {
								if (game.getGameId() == finishGame.getGameId()) {
									toRemove.add(game);
									entry.getValue().getClients().add(finishGame.getWinner().getClient());
								}
							}
							entry.getValue().getGames().removeAll(toRemove);
							if (entry.getValue().getGames().isEmpty()) {
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
								}
								nextTournamentRound(entry.getKey());
							}
						}
					}

					break;
				case MessageCodes.DISCONNECT:
					Disconnect disconnect = (Disconnect) message;
					guiCommunication.addToMessageQueue(message);

					clientsInLobby.remove(idMapping.remove(disconnect.getClientId()));
					break;

				case MessageCodes.GAMESTARTERROR:
					GameStartError gameStartError = (GameStartError) message;
					guiCommunication.addToMessageQueue(message);
					gameMapping.get(gameStartError.getGameId()).setGameState(GameState.NOT_STARTED);
					break;
				case MessageCodes.GAMEABORTERROR:
					GameAbortError gameAbortError = (GameAbortError) message;
					guiCommunication.addToMessageQueue(message);
					gameMapping.get(gameAbortError.getGameId()).setGameState(GameState.IN_PROGRESS);
					break;
				case MessageCodes.GAMEPAUSEDERROR:
					GamePausedError gamePausedError = (GamePausedError) message;
					guiCommunication.addToMessageQueue(message);
					gameMapping.get(gamePausedError.getGameId()).setGameState(GameState.IN_PROGRESS);
					break;
				case MessageCodes.GAMERESUMEERROR:
					GameResumeError gameResumeError = (GameResumeError) message;
					guiCommunication.addToMessageQueue(message);
					gameMapping.get(gameResumeError.getGameId()).setGameState(GameState.PAUSED);
					break;
				case MessageCodes.NEWTOURNAMENT:
					NewTournament newTournament = (NewTournament) message;
					createNewTournament(newTournament);
				}
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
