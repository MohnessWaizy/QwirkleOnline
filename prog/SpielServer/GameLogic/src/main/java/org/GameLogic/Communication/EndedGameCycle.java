package org.GameLogic.Communication;

import java.util.Map;

import org.CostumMessages.ClientConnect;

import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Handler.GameHandler;
import org.GameLogic.Handler.GameManagement;
import org.GameLogic.Util.MessageCodes;

import org.NetworkInterface.MessageWithClientId;

import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;

/**
 * After the game is finished, spectators have the possibility to see the game.
 * All relevant information can be requested. This class does permit the ability
 * to send messages to the game that manipulate the game
 * state(TileSwapRequest/PlayTiles).
 */
public class EndedGameCycle implements GameRunner {

	@Override
	public void routeMessage(MessageWithClientId messageWithClientId, GameHandler gameHandler,
			GameManagement gameManagement, Map<Integer, Client> idMapping, int gameId) {
		Message message = messageWithClientId.getMsg();
		int clientId = messageWithClientId.getClientId();

		if (clientId != -1 && !isSpectator(idMapping.get(clientId))) {
			gameManagement.handleAccessDenied(clientId, message.getUniqueId(),
					"Spieler kann kein beendetes Spiel beobachten");

			return;
		}

		switch (message.getUniqueId()) {

		/*
		 * Own codes
		 */

		case MessageCodes.CLIENTCONNECT:
			ClientConnect clientConnect = (ClientConnect) message;

			try {
				// Only spectators
				if (isSpectator(clientConnect.getClient())) {
					int connectingClient = clientConnect.getClientId();
					// Add Client to the game => No exception means successfully added
					gameHandler.requestJoin(clientConnect.getClient());
					// Add Client to the mapping, will not be added if exception is thrown
					idMapping.put(connectingClient, clientConnect.getClient());
					gameManagement.sendMsg(gameHandler.requestWinner(), connectingClient);
				} else {
					gameManagement.sendMsg(new NotAllowed("Klient kein Beobachter", MessageCodes.CLIENTCONNECT),
							clientId);
				}
			} catch (NotAllowedException nae) {
				gameManagement.sendMsg(new NotAllowed(nae.getMessage(), MessageCodes.GAMEJOINREQUEST),
						clientConnect.getClientId());
			}

			break;
		/*
		 * Offical requests
		 */

		case MessageCodes.LEAVINGREQUEST:
			// LeavingRequest leavingRequest = (LeavingRequest) message;

			Client leavingClient = idMapping.get(clientId);

			/*
			 * If a client is in the mapping, he must be in the game
			 */
			gameHandler.requestRemove(leavingClient);
			gameManagement.sendMsgToGame(new LeavingPlayer(leavingClient), gameId);
			// Send player back to Lobby and remove mapping
			gameManagement.moveToLobby(idMapping.remove(clientId), gameId);

			break;

		case MessageCodes.SCOREREQUEST:
			// ScoreRequest scoreRequest = (ScoreRequest) message;

			gameManagement.sendMsg(new ScoreResponse(gameHandler.requestScore()), clientId);

			break;
		case MessageCodes.TURNTIMELEFTREQUEST:
			// TurnTimeLeftRequest turnTimeLeftRequest = (TurnTimeLeftRequest) message;

			gameManagement.sendMsg(new TurnTimeLeftResponse(gameHandler.requestTurnTimeLeft()), clientId);

			break;
		case MessageCodes.TOTALTIMEREQUEST:
			// TotalTimeRequest totalTimeRequest = (TotalTimeRequest) message;

			gameManagement.sendMsg(new TotalTimeResponse(gameHandler.requestTotalTime()), clientId);

			break;
		case MessageCodes.BAGREQUEST:
			// BagRequest bagRequest = (BagRequest) message;

			gameManagement.sendMsg(new BagResponse(gameHandler.requestBag()), clientId);

			break;
		case MessageCodes.PLAYERHANDSREQUEST:
			// PlayerHandsRequest playerHandsRequest = (PlayerHandsRequest) message;

			gameManagement.sendMsg(new PlayerHandsResponse(gameHandler.requestPlayerHands()), clientId);

			break;
		case MessageCodes.GAMEDATAREQUEST:
			Client gameDataClient = idMapping.get(clientId);
			gameManagement.sendMsg(gameHandler.requestGameData(gameDataClient), clientId);

			break;

		default:
			gameManagement.handleNotAllowed(clientId, message.getUniqueId(),
					"Nachricht für Spielzustand beendet nicht erlaubt");
			break;
		}

	}

	private boolean isSpectator(Client client) {
		return client != null && client.getClientType() == ClientType.SPECTATOR;
	}
}
