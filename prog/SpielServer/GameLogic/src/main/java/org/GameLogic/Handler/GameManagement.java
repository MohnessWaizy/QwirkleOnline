package org.GameLogic.Handler;

import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.NetworkInterface.MessageWithClientId;
import org.NetworkInterface.NetworkServer;
import org.ConfigManager.ConfigManager;
import org.CostumMessages.ClientConnect;
import org.CostumMessages.FinishGame;
import org.CostumMessages.GameAbort;
import org.CostumMessages.GameAbortError;
import org.CostumMessages.GamePaused;
import org.CostumMessages.GamePausedError;
import org.CostumMessages.GameResume;
import org.CostumMessages.GameResumeError;
import org.CostumMessages.GameStart;
import org.CostumMessages.GameStartError;
import org.GameLogic.Communication.CommunicationHandler;
import org.ServerGui.Controller.SuperController;
import org.ServerGui.Model.GuiCommunication;
import org.GameLogic.Util.MessageCodes;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.*;

/**
 * 
 * Central class of the game server. All messages are routed here and every game
 * is created and managed in this class.
 *
 */
public class GameManagement {
	private NetworkServer server = null;
	private Lobby lobby = null;
	private HashMap<Integer, Integer> clientIdToGameId = null;
	private HashMap<Integer, HashSet<Integer>> gameIdToClientId = null;
	private HashMap<Integer, Thread> gameMap = null;
	private HashMap<Thread, CommunicationHandler> threadMap = null;
	private HashMap<Integer, Client> players = null;
	private HashSet<Integer> playerspectators = null;
	private static int gameIdCount = 0;
	private volatile boolean running = true;

	/**
	 * @param server
	 *            Gets a reference to the NetworkServer that handles all
	 *            connections.
	 * @param data
	 *            Gets a reference to the GuiData.
	 */
	public GameManagement(NetworkServer server, SuperController controller) {
		this.server = server;

		this.clientIdToGameId = new HashMap<Integer, Integer>();
		this.gameIdToClientId = new HashMap<Integer, HashSet<Integer>>();
		this.gameMap = new HashMap<Integer, Thread>();
		this.threadMap = new HashMap<Thread, CommunicationHandler>();
		this.players = new HashMap<Integer, Client>();
		this.playerspectators = new HashSet<Integer>();

		GuiCommunication guiCommunication = controller.getGuiCommunication();
		// create Lobby
		this.lobby = new Lobby(this, guiCommunication);
		controller.setLobby(lobby);
		Thread lobbyThread = new Thread(lobby);
		lobbyThread.start();
		lobbyThread.setName("Lobby");
		gameMap.put(-1, lobbyThread);
		gameIdToClientId.put(-1, new HashSet<Integer>());

	}

	/**
	 * Callbacks from CommunicationHandler
	 **/
	public void errorOnGameStart(int gameId) {
		lobby.addToMessageQueue(new MessageWithClientId(new GameStartError(gameId), -1));
	}

	public void errorOnGameAbort(int gameId) {
		lobby.addToMessageQueue(new MessageWithClientId(new GameAbortError(gameId), -1));
	}

	public void errorOnGamePaused(int gameId) {
		lobby.addToMessageQueue(new MessageWithClientId(new GamePausedError(gameId), -1));
	}

	public void errorOnGameResume(int gameId) {
		lobby.addToMessageQueue(new MessageWithClientId(new GameResumeError(gameId), -1));
	}

	public void notifyOfFinish(Winner winner, int gameId) {
		System.out.println("notifyOnFinishGame");
		lobby.addToMessageQueue(new MessageWithClientId(new FinishGame(winner, gameId), -1));
	}

	/**
	 * Generates a new Id for a game.
	 * 
	 * @return returns a new Id.
	 */
	public int getNewId() {
		gameIdCount++;
		// handle overflow into lobby id
		if (gameIdCount == -1) {
			throw new RuntimeException("Not enough GameIds available.");
		}
		return gameIdCount;

	}

	/**
	 * Main entry point for the server.
	 * 
	 * @param args
	 *            Console arguments
	 */
	public static void main(String[] args) {
		Thread guiThread = new Thread() {
			@Override
			public void run() {
				try {
					javafx.application.Application.launch(SuperController.class);
				} catch (NullPointerException npe) {

				}
			}
		};
		guiThread.start();
		guiThread.setName("Gui Thread");
		SuperController controller = SuperController.waitForSuperController();

		GameManagement management = new GameManagement(new NetworkServer(33100), controller);

		while (management.isRunning()) {
			management.route();
			management.update();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			guiThread.join();
		} catch (InterruptedException e) {
			// we don't interrupt threads so should never occur.
		}
		management.shutdown();
	}

	/**
	 * 
	 * handles Exception: send Error message and increases kickCounter/kicks Client
	 * 
	 * @param clientId
	 *            Id of the Client that caused the exception
	 * @param messageId
	 *            Id of the Message that caused the exception
	 * @param msg
	 *            Description of the Exception cause
	 */
	public void handleNotAllowed(int clientId, int messageId, String msg) {
		server.sendMsg(new NotAllowed(msg, messageId), clientId);
		try {
			if (server.getKickMap().get(clientId) > 5) {
				server.kick(clientId);
			} else {
				server.getKickMap().put(clientId, server.getKickMap().get(clientId) + 1);
			}
		} catch (NullPointerException e) {
			// Client isn't connected so we don't need to do anything.
		}

	}

	/**
	 * 
	 * handles Exception: send Error message and increases kickCounter/kicks Client
	 * 
	 * @param clientId
	 *            Id of the Client that caused the exception
	 * @param messageId
	 *            Id of the Message that caused the exception
	 * @param msg
	 *            Description of the Exception cause
	 */
	public void handleAccessDenied(int clientId, int messageId, String msg) {
		server.sendMsg(new AccessDenied(msg, messageId), clientId);
		try {
			if (server.getKickMap().get(clientId) > 5) {
				server.kick(clientId);
			} else {
				server.getKickMap().put(clientId, server.getKickMap().get(clientId) + 1);
			}
		} catch (NullPointerException e) {
			// Client isn't connected so we don't need to do anything.
		}
	}

	/**
	 * Routes the messages of all clients to the correct game and handles new
	 * connections.
	 */
	private void route() {

		MessageWithClientId msg = server.getNextMsg();
		if (msg != null) {
			if (msg.getMsg().getUniqueId() == MessageCodes.DISCONNECT) {

				// handle special case disconnect
				Client clientToRemove = players.remove(msg.getClientId());
				if (clientToRemove != null) {
					int gameId = clientIdToGameId.remove(msg.getClientId());
					gameIdToClientId.get(gameId).remove(msg.getClientId());
					if (gameId != -1) {
						threadMap.get(gameMap.get(gameId)).addToMessageQueue(msg);
						if (clientToRemove.getClientType() == ClientType.PLAYER
								&& !lobby.removeClientFromGame(clientToRemove, gameId)) {
							throw new RuntimeException("Something went wrong with updating players");
						}
					} else {
						lobby.addToMessageQueue(msg);
					}

					playerspectators.remove(msg.getClientId());
				}

			} else if (clientIdToGameId.get(msg.getClientId()) == null) {
				if (msg.getMsg().getUniqueId() == MessageCodes.CONNECTREQUEST) {
					// handle logging in
					clientIdToGameId.put(msg.getClientId(), -1);
					gameIdToClientId.get(-1).add(msg.getClientId());

					// save players for type identification
					players.put(msg.getClientId(),
							new Client(msg.getClientId(), ((ConnectRequest) msg.getMsg()).getClientName(),
									((ConnectRequest) msg.getMsg()).getClientType()));

					lobby.addToMessageQueue(new MessageWithClientId(
							new ClientConnect(msg.getClientId(), (ConnectRequest) msg.getMsg()), -1));

					sendMsg(new ConnectAccepted(msg.getClientId()), msg.getClientId());
				} else {
					// not logged in
					handleAccessDenied(msg.getClientId(), msg.getMsg().getUniqueId(), "Nicht angemeldet.");
				}
			} else if (clientIdToGameId.get(msg.getClientId()) == -1) {
				// Client is in lobby
				int messageId = msg.getMsg().getUniqueId();
				if (messageId == MessageCodes.GAMELISTREQUEST || messageId == MessageCodes.GAMEJOINREQUEST
						|| messageId == MessageCodes.SPECTATORJOINREQUEST) {
					// redirect messages to lobby
					lobby.addToMessageQueue(msg);
				} else {
					if (messageId == MessageCodes.LEAVINGREQUEST || messageId == MessageCodes.SCOREREQUEST
							|| messageId == MessageCodes.TURNTIMELEFTREQUEST
							|| messageId == MessageCodes.TOTALTIMEREQUEST || messageId == MessageCodes.GAMEDATAREQUEST
							|| (messageId == MessageCodes.TILESWAPREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.PLAYER)
							|| (messageId == MessageCodes.PLAYTILES
									&& players.get(msg.getClientId()).getClientType() == ClientType.PLAYER)
							|| (messageId == MessageCodes.BAGREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.SPECTATOR)
							|| (messageId == MessageCodes.PLAYERHANDSREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.SPECTATOR)) {
						// handle not allowed actions
						handleNotAllowed(msg.getClientId(), messageId,
								"Diese Aktion kann in der Lobby nicht ausgeführt werden.");
					} else if ((messageId == MessageCodes.PLAYTILES
							&& players.get(msg.getClientId()).getClientType() == ClientType.SPECTATOR)
							|| (messageId == MessageCodes.TILESWAPREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.SPECTATOR)
							|| (messageId == MessageCodes.BAGREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.PLAYER)
							|| (messageId == MessageCodes.PLAYERHANDSREQUEST
									&& players.get(msg.getClientId()).getClientType() == ClientType.PLAYER)) {

						// handle everything that clients are not allowed to to
						handleAccessDenied(msg.getClientId(), messageId,
								"Diese Aktion kann von diesem Client nicht ausgeführt werden.");
					} else {
						// handle everything else
						server.sendMsg(new ParsingError("Error.", messageId), msg.getClientId());
					}

				}

			} else {
				// Client is in a game
				int messageId = msg.getMsg().getUniqueId();
				if (messageId == MessageCodes.MESSAGESEND || messageId == MessageCodes.LEAVINGREQUEST
						|| messageId == MessageCodes.TILESWAPREQUEST || messageId == MessageCodes.PLAYTILES
						|| messageId == MessageCodes.SCOREREQUEST || messageId == MessageCodes.TURNTIMELEFTREQUEST
						|| messageId == MessageCodes.TOTALTIMEREQUEST || messageId == MessageCodes.BAGREQUEST
						|| messageId == MessageCodes.PLAYERHANDSREQUEST || messageId == MessageCodes.GAMEDATAREQUEST) {

					threadMap.get(gameMap.get(clientIdToGameId.get(msg.getClientId()))).addToMessageQueue(msg);

				} else if (messageId == MessageCodes.CONNECTREQUEST || messageId == MessageCodes.GAMELISTREQUEST
						|| (messageId == MessageCodes.GAMEJOINREQUEST
								&& players.get(msg.getClientId()).getClientType() == ClientType.PLAYER)
						|| (messageId == MessageCodes.SPECTATORJOINREQUEST)) {

					// handle not allowed actions
					server.sendMsg(
							new NotAllowed("Diese Aktion kann in einem Spiel nicht ausgeführt werden.", messageId),
							msg.getClientId());

				} else if ((messageId == MessageCodes.GAMEJOINREQUEST
						&& players.get(msg.getClientId()).getClientType() == ClientType.SPECTATOR)) {

					// handle everything that clients are not allowed to to
					server.sendMsg(
							new AccessDenied("Diese Aktion kann von diesem Client nicht ausgeführt werden.", messageId),
							msg.getClientId());

				} else {
					// handle everything else
					server.sendMsg(new ParsingError("Error.", messageId), msg.getClientId());
					if (server.getKickMap().get(msg.getClientId()) > 5) {
						server.kick(msg.getClientId());
					} else {
						server.getKickMap().put(msg.getClientId(), server.getKickMap().remove(msg.getClientId()) + 1);
					}
				}
			}
		} else {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Cleans up all finished threads.
	 */
	private void update() {
		try {
			Set<Integer> toRemoveKey = new HashSet<>();
			Set<Thread> toRemoveVal = new HashSet<>();
			Iterator<Entry<Integer, Thread>> it = gameMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Thread> pair = (Map.Entry<Integer, Thread>) it.next();
				if (pair.getKey() != -1) {
					if (!pair.getValue().isAlive()) {
						toRemoveKey.add(pair.getKey());
						toRemoveVal.add(pair.getValue());
					}
				}
			}
			gameMap.keySet().removeAll(toRemoveKey);
			gameIdToClientId.keySet().removeAll(toRemoveKey);
			threadMap.keySet().removeAll(toRemoveVal);
		} catch (ConcurrentModificationException e) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// We are not interrupting Threads so this shouldn't happen
			}
			update();
		}

	}

	/**
	 * Sends a message to a client.
	 * 
	 * @param msg
	 *            Message that should be send.
	 * @param clientId
	 *            Receiver identification.
	 */
	public void sendMsg(Message msg, int clientId) {
		server.sendMsg(msg, clientId);
	}

	/**
	 * Sends a message to all clients in the game.
	 * 
	 * @param msg
	 *            Message that should be send.
	 * @param gameID
	 *            Id of the game the message should be send to.
	 */
	public void sendMsgToGame(Message msg, int gameId) {
		for (int ids : gameIdToClientId.get(gameId)) {
			server.sendMsg(msg, ids);
		}

	}

	/**
	 * Starts the game.
	 * 
	 * @param gameId
	 *            Id of the game started.
	 */
	public void startGame(int gameId) {
		threadMap.get(gameMap.get(gameId)).addToMessageQueue(new MessageWithClientId(new GameStart(), -1));
	}

	/**
	 * Aborts the game.
	 * 
	 * @param gameId
	 *            Id of the game aborted
	 */
	public void abortGame(int gameId, Client winner) {
		threadMap.get(gameMap.get(gameId)).addToMessageQueue(new MessageWithClientId(new GameAbort(winner), -1));
	}

	/**
	 * Pauses the game.
	 * 
	 * @param gameId
	 *            Id of the game paused
	 */
	public void pauseGame(int gameId) {
		threadMap.get(gameMap.get(gameId)).addToMessageQueue(new MessageWithClientId(new GamePaused(), -1));
	}

	/**
	 * Resumes the game.
	 * 
	 * @param gameId
	 *            Id of the game resumed
	 */
	public void resumeGame(int gameId) {
		threadMap.get(gameMap.get(gameId)).addToMessageQueue(new MessageWithClientId(new GameResume(), -1));
	}

	/**
	 * Creates a new game without players
	 * 
	 * @param game
	 *            Information needed to create a game
	 * @return Returns the Id of the new Game
	 */
	public int createGame(Game game) {
		if (game == null || game.getGameName().equals("") || game.getConfig() == null) {
			return -1;
		}
		int id = getNewId();
		game.setGameId(id);
		CommunicationHandler communicationHandler = new CommunicationHandler(game, this);
		Thread gameThread = new Thread(communicationHandler);
		gameThread.start();
		gameThread.setName("game" + id);
		gameMap.put(id, gameThread);
		gameIdToClientId.put(id, new HashSet<Integer>());
		threadMap.put(gameThread, communicationHandler);
		return id;
	}

	/**
	 * 
	 * Moves a client from the lobby to a game
	 * 
	 * @param game
	 *            Object of the game the client should be moved to
	 * @param client
	 *            Object of the client that should be moved
	 * @return whether the move was a success
	 */
	public boolean moveToGame(Game game, Client client) {
		if (game == null || client == null || clientIdToGameId.get(client.getClientId()) == null
				|| clientIdToGameId.get(client.getClientId()) != -1) {
			return false;
		} else {
			clientIdToGameId.remove(client.getClientId());
			gameIdToClientId.get(-1).remove(client.getClientId());

			clientIdToGameId.put(client.getClientId(), game.getGameId());
			gameIdToClientId.get(game.getGameId()).add(client.getClientId());
			threadMap.get(gameMap.get(game.getGameId()))
					.addToMessageQueue(new MessageWithClientId(new ClientConnect(client), -1));
			if (client.getClientType() == ClientType.PLAYER) {
				sendMsg(new GameJoinAccepted(game), client.getClientId());
			} else {
				sendMsg(new SpectatorJoinAccepted(game), client.getClientId());
				if (players.get(client.getClientId()).getClientType() == ClientType.PLAYER) {
					playerspectators.add(client.getClientId());
					players.get(client.getClientId()).setClientType(ClientType.SPECTATOR);
				}
			}
			return true;
		}
	}

	/**
	 * 
	 * Moves a client from the game to a lobby
	 * 
	 * @param client
	 *            Object of the client that should be moved
	 * @param oldGameId
	 *            Id of the old game
	 * @return whether the move was a success
	 */
	public boolean moveToLobby(Client client, int oldGameId) {
		if (client == null) {
			return false;
		} else {
			clientIdToGameId.remove(client.getClientId());
			gameIdToClientId.get(oldGameId).remove(client.getClientId());

			clientIdToGameId.put(client.getClientId(), -1);
			gameIdToClientId.get(-1).add(client.getClientId());

			if (client.getClientType() == ClientType.PLAYER && !lobby.removeClientFromGame(client, oldGameId)) {
				// throw new RuntimeException("Something went wrong with updating players");
			}

			if (client.getClientType() == ClientType.SPECTATOR && playerspectators.contains(client.getClientId())) {
				client.setClientType(ClientType.PLAYER);
				players.get(client.getClientId()).setClientType(ClientType.PLAYER);
				playerspectators.remove(client.getClientId());
			}

			lobby.addToMessageQueue(new MessageWithClientId(new ClientConnect(client), -1));
			return true;
		}
	}

	/**
	 * Is called when the server is shutting down.
	 */
	private void shutdown() {
		try {
			lobby.shutdown();
			gameMap.remove(-1).join();
		} catch (InterruptedException e) {
			// We don't interrupt threads so should never occur.
		}
		Iterator<Entry<Integer, Thread>> it = gameMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Thread> pair = (Map.Entry<Integer, Thread>) it.next();
			CommunicationHandler game = threadMap.get(pair.getValue());
			game.shutdown();
			try {
				pair.getValue().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		server.shutdown();
	}

	public boolean isRunning() {
		return running;
	}

	public void notRunning() {
		this.running = false;
	}

	/**
	 * @see ConfigManager
	 */
	public boolean saveConfig(File file, Configuration config) {
		return ConfigManager.saveConfig(file, config);
	}

	/**
	 * @see ConfigManager
	 */
	public Configuration loadConfig(File file) {
		return ConfigManager.loadConfig(file);
	}

}
