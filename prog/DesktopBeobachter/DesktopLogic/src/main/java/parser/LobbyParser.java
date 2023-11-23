package parser;

import controller.LobbySceneController;
import de.upb.swtpra1819interface.messages.GameListResponse;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.SpectatorJoinAccepted;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

import java.util.Collection;

import org.NetworkInterface.NetClient;

/**
 * parser for the lobby
 *
 */
public class LobbyParser extends InterfaceParser {

	private LobbySceneController lobbyController;
	private NetClient netClient;
	private int gameID;
	private Parser parser;

	/**
	 * @param controller reference to LobbySceneController
	 * @param netClient
	 * @param cycle      endurance of the clock
	 */
	public LobbyParser(LobbySceneController controller, NetClient netClient, long cycle) {
		lobbyController = controller;
		this.netClient = netClient;
		setClient(netClient);
		initClock(cycle);
		startQueueWorker();
		parser = new Parser();

	}

	/*
	 * 
	 * @see parser.InterfaceParser#parseMessage(de.upb.swtpra1819interface.messages.
	 * Message) Parses messages that are sent form the server
	 */
	@Override
	public void parseMessage(Message msg) {
		switch (msg.getUniqueId()) {
		case 301:
			GameListResponse glr = (GameListResponse) msg;
			Collection<Game> games = glr.getGames();
			if (lobbyController.gameUpdatePossible == true) {
				lobbyController.updateGames(games);
			}
			break;
		case 305:
			SpectatorJoinAccepted gID = (SpectatorJoinAccepted) msg;
			int gameID = gID.getGameId().getGameId();
			lobbyController.joinGame(gameID);
			break;
		case 920:
			break;
		default:
			break;
		}
	}

	/*
	 * @see parser.InterfaceParser#getAllQueueMessages() Parse messages and send new
	 * GameListRequest to server
	 */
	public void getAllQueueMessages() {
		super.getAllQueueMessages();
		lobbyController.sendNewRequest();
	}

	/**
	 * @param gameID
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
}
