package org.GameLogic.Communication;

import java.util.List;
import java.util.Map;

import org.CostumMessages.ClientConnect;
import org.CostumMessages.GameAbort;
import org.GameLogic.Board.Coordinate;
import org.GameLogic.DataStructures.Player;
import org.GameLogic.DataStructures.Spectator;
import org.GameLogic.DataStructures.Tuple;
import org.GameLogic.Exceptions.ErrorType;
import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Exceptions.ParserException;
import org.GameLogic.Handler.GameHandler;
import org.GameLogic.Handler.GameManagement;
import org.GameLogic.Util.Converter;
import org.GameLogic.Util.MessageCodes;
import org.NetworkInterface.MessageWithClientId;

import de.upb.swtpra1819interface.messages.AbortGame;
import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MessageSend;
import de.upb.swtpra1819interface.messages.MessageSignal;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.PauseGame;
import de.upb.swtpra1819interface.messages.PlayTiles;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ResumeGame;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.StartTiles;
import de.upb.swtpra1819interface.messages.TileSwapRequest;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * This implementation of GameRunner is meant for a typical game that is either
 * not started, paused or in progress.
 */
public class StandardGameCycle implements GameRunner {

	@Override
	public void routeMessage(MessageWithClientId messageWithClientId, GameHandler gameHandler,
			GameManagement gameManagement, Map<Integer, Client> idMapping, int gameId) {
		Message message = messageWithClientId.getMsg();
		int clientId = messageWithClientId.getClientId();

		switch (message.getUniqueId()) {

		/*
		 * Own codes
		 */

		case MessageCodes.CLIENTCONNECT:
			ClientConnect clientConnect = (ClientConnect) message;

			try {
				// Add Client to the game
				gameHandler.requestJoin(clientConnect.getClient());
				// Add Client to the mapping, will not be added if exception is thrown
				idMapping.put(clientConnect.getClientId(), clientConnect.getClient());
			} catch (NotAllowedException nae) {
				gameManagement.sendMsg(new NotAllowed(nae.getMessage(), MessageCodes.GAMEJOINREQUEST),
						clientConnect.getClientId());
			}

			break;
		case MessageCodes.GAMESTART:
			// GameStart gameStart = (GameStart) message;

			try {

				// notify of game start
				gameHandler.startGame();
				gameManagement.sendMsgToGame(
						new StartGame(gameHandler.getConfig(), Converter.toNetworkClient(gameHandler.getPlayers())),
						gameId);
				// notify Player of their starting tiles

				for (Player player : gameHandler.getPlayers()) {
					gameManagement.sendMsg(new StartTiles(gameHandler.requestPlayerTiles(player.getClient())),
							player.getId());
				}
				// notify of current player
				Player currentPlayer = gameHandler.requestCurrentPayer();
				gameManagement.sendMsgToGame(new CurrentPlayer(currentPlayer.getClient()), gameId);

				// start the gameClock
				gameHandler.startGameClock();
			} catch (NotAllowedException nae) {
				gameManagement.errorOnGameStart(gameId);
			}

			break;
		case MessageCodes.GAMEABORT:
			GameAbort gameAbort = (GameAbort) message;

			try {
				// notify of game end
				gameManagement.sendMsgToGame(new AbortGame(), gameId);
				gameHandler.endGame();
				// Send winner selected by Server
				Map<Client, Integer> scoreBoard = gameHandler.requestScore();
				gameManagement.sendMsgToGame(
						new Winner(gameAbort.getClient(), scoreBoard.get(gameAbort.getClient()), scoreBoard), gameId);

				for (Client client : idMapping.values()) {
					gameManagement.moveToLobby(client, gameId);
				}

			} catch (NotAllowedException nae) {
				gameManagement.errorOnGameAbort(gameId);
			}

			break;
		case MessageCodes.GAMEPAUSED:
			// GamePaused gamePaused = (GamePaused) message;

			try {
				// notify of game pause
				gameManagement.sendMsgToGame(new PauseGame(), gameId);
				gameHandler.pauseGame();
			} catch (NotAllowedException nae) {
				gameManagement.errorOnGameResume(gameId);
			}

			break;
		case MessageCodes.GAMERESUME:
			// GameResume gameResume = (GameResume) message;

			try {
				// notify of game resume
				gameManagement.sendMsgToGame(new ResumeGame(), gameId);
				gameHandler.resumeGame();
			} catch (NotAllowedException nae) {
				gameManagement.errorOnGameResume(gameId);
			}

			break;
		case MessageCodes.DISCONNECT:
			// Disconnect disconnect = (Disconnect) message;

			gameHandler.requestRemove(idMapping.remove(clientId));

			break;

		/*
		 * Interface specified codes
		 */

		case MessageCodes.MESSAGESEND:
			MessageSend messageSend = (MessageSend) message;

			Client sender = idMapping.get(clientId);

			if (sender.getClientType() == ClientType.PLAYER) {
				gameManagement.sendMsgToGame(new MessageSignal(messageSend.getMessage(), sender), gameId);
			} else {
				for (Spectator sendingSpectator : gameHandler.getSpectators()) {
					gameManagement.sendMsg(new MessageSignal(messageSend.getMessage(), sendingSpectator.getClient()),
							sendingSpectator.getId());
				}
			}

			break;
		case MessageCodes.LEAVINGREQUEST:
			// LeavingRequest leavingRequest = (LeavingRequest) message;

			Client leavingClient = idMapping.get(clientId);

			gameHandler.requestRemove(leavingClient);
			gameManagement.sendMsgToGame(new LeavingPlayer(leavingClient), gameId);
			// Send player back to Lobby and remove mapping
			gameManagement.moveToLobby(idMapping.remove(clientId), gameId);

			break;
		case MessageCodes.TILESWAPREQUEST:
			TileSwapRequest tileSwapRequest = (TileSwapRequest) message;

			if (idMapping.get(clientId).getClientType() == ClientType.SPECTATOR) {
				gameManagement.handleAccessDenied(clientId, MessageCodes.PLAYTILES,
						"Keine Berechtigung als Beobachter");
				break;
			}

			try {
				if (!gameHandler.inTurn()) {
					gameManagement.sendMsg(new NotAllowed("Kein Steintausch während der Visualisierungszeit",
							MessageCodes.TILESWAPREQUEST), clientId);
				} else {
					List<Tile> newHandTiles = gameHandler.requestTileSwap(idMapping.get(clientId),
							(List<Tile>) tileSwapRequest.getTiles());

					gameManagement.sendMsg(new TileSwapValid(true), clientId);
					gameManagement.sendMsg(new TileSwapResponse(newHandTiles), clientId);
				}
			} catch (NotAllowedException nae) {
				if (nae.getErrorType() == ErrorType.ValidationError) {
					gameManagement.sendMsg(new TileSwapValid(false), clientId);
				} else {
					gameManagement.handleNotAllowed(clientId, MessageCodes.TILESWAPREQUEST, nae.getMessage());
				}
			} catch (ParserException pe) {
				if (pe.getErrorType() == ErrorType.ValidationError) {
					gameManagement.sendMsg(new TileSwapValid(false), clientId);
				} else {
					gameManagement.handleNotAllowed(clientId, MessageCodes.TILESWAPREQUEST, pe.getMessage());
				}
			}

			break;
		case MessageCodes.PLAYTILES:
			PlayTiles playTiles = (PlayTiles) message;

			if (idMapping.get(clientId).getClientType() == ClientType.SPECTATOR) {
				gameManagement.handleAccessDenied(clientId, MessageCodes.PLAYTILES,
						"Keine Berechtigung als Beobachter");
				break;
			}

			try {
				if (!gameHandler.inTurn()) {
					gameManagement.handleNotAllowed(clientId, MessageCodes.PLAYTILES,
							"Kein Spielzug während der Visualisierungszeit möglich");
				} else {

					List<Tuple<Coordinate, Tile>> tilesWithCoordinte = Converter
							.toGameTileCoord((List<TileOnPosition>) playTiles.getTiles());
					gameHandler.requestPlayTiles(idMapping.get(clientId), tilesWithCoordinte);

					gameManagement.sendMsg(new MoveValid(true, "Valider Zug"), clientId);

					gameManagement.sendMsgToGame(new Update(Converter.toNetworkTileCoord(gameHandler.requestUpdate()),
							gameHandler.requestBag().size()), gameId);
				}
			} catch (NotAllowedException nae) {
				if (nae.getErrorType() == ErrorType.ValidationError) {
					gameManagement.sendMsg(new MoveValid(false, nae.getMessage()), clientId);
				} else {
					gameManagement.handleNotAllowed(clientId, MessageCodes.PLAYTILES, nae.getMessage());
				}
			} catch (ParserException pe) {
				if (pe.getErrorType() == ErrorType.ValidationError) {
					gameManagement.sendMsg(new MoveValid(false, pe.getMessage()), clientId);
				} else {
					gameManagement.handleNotAllowed(clientId, MessageCodes.PLAYTILES, pe.getMessage());
				}
			}

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

			if (idMapping.get(clientId).getClientType() == ClientType.SPECTATOR) {
				gameManagement.sendMsg(new BagResponse(gameHandler.requestBag()), clientId);
			} else {
				gameManagement.handleAccessDenied(clientId, MessageCodes.BAGREQUEST,
						"Spieler darf nicht den Bag einsehen");
			}

			break;
		case MessageCodes.PLAYERHANDSREQUEST:
			// PlayerHandsRequest playerHandsRequest = (PlayerHandsRequest) message;
			gameManagement.sendMsg(new PlayerHandsResponse(gameHandler.requestPlayerHands()), clientId);

			break;
		case MessageCodes.GAMEDATAREQUEST:
			// GameDataRequest gameDataRequest = (GameDataRequest) message;
			gameManagement.sendMsg(gameHandler.requestGameData(idMapping.get(clientId)), clientId);

			break;

		default:
			gameManagement.handleNotAllowed(clientId, message.getUniqueId(), "Nachricht unbekannt");
			break;
		}

	}

}
