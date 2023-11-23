package org.GameLogic.Communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.BasicCommunication.BasicCommunicationHandler;
import org.GameLogic.DataStructures.Player;
import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Handler.GameHandler;
import org.GameLogic.Handler.GameManagement;
import org.NetworkInterface.MessageWithClientId;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.*;

public class CommunicationHandler extends BasicCommunicationHandler implements CommunicationHandlerCallback {

	/**
	 * Send and receive messages
	 */
	private GameManagement gameManagement;
	/**
	 * Mapping for id and client, no need to iterate over every client
	 */
	private Map<Integer, Client> idMapping;
	/**
	 * 
	 */
	private GameHandler gameHandler;
	/**
	 * Game id
	 */
	private int gameId;
	/**
	 * time to sleep
	 */
	private long sleepTime;
	/**
	 * routing of messages
	 */
	private GameRunner gameRunner;

	/**
	 * Creates a <b>CommunicationHandler</b> which will take input messages and
	 * apply them to the game. Same goes for output messages of the game. All in all
	 * this class does operate the GameHandler
	 * 
	 * @param game
	 *            Interface Game instance, here to create the GameHandler
	 * @param gameManagement
	 */
	public CommunicationHandler(Game game, GameManagement gameManagement) {
		super();
		this.idMapping = new HashMap<Integer, Client>();
		this.gameManagement = gameManagement;
		this.gameId = game.getGameId();
		this.gameHandler = new GameHandler(this, game.getConfig());
		this.sleepTime = 5L;
		this.gameRunner = new StandardGameCycle();
	}

	/**
	 * The switch implements all relevant request codes in
	 * {@link org.GameLogic.Util.MessageCodes}. Every request then has a suitable
	 * method to call, which a all in the interface {@link Communication}
	 */
	@Override
	public void run() {

		while (canReceiveMessage()) {
			MessageWithClientId messageWithClientId = pollNextMessage();

			if (messageWithClientId != null) {
				gameRunner.routeMessage(messageWithClientId, gameHandler, gameManagement, idMapping, gameId);
			} else {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void callbackPlayerKicked(Player player) {
		gameManagement.sendMsgToGame(new LeavingPlayer(player.getClient()), gameId);
		idMapping.remove(player.getClient().getClientId());
		gameManagement.moveToLobby(player.getClient(), gameId);
	}

	@Override
	public void callbackNextPlayer(Player player) {
		gameManagement.sendMsgToGame(new CurrentPlayer(player.getClient()), gameId);
	}

	@Override
	public void callbackPlayerTiles(Player player, List<Tile> newHandTiles) {
		gameManagement.sendMsg(new SendTiles(newHandTiles), player.getId());
	}

	@Override
	public void callbackGameFinish(boolean rightfullyFinished) {
		gameManagement.sendMsgToGame(new EndGame(), gameId);

		this.gameRunner = new EndedGameCycle();
		this.sleepTime = 100L;

		try {
			Winner winner = gameHandler.requestWinner();

			if (rightfullyFinished) {
				gameManagement.sendMsgToGame(winner, gameId);
			}

			for (Client client : idMapping.values()) {
				gameManagement.moveToLobby(client, gameId);
			}

			if (rightfullyFinished) {
				gameManagement.notifyOfFinish(winner, gameId);
			}

		} catch (NotAllowedException e) {
			e.printStackTrace();
		}
	}

}
