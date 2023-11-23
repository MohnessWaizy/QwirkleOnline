package parser;

import java.util.List;

import org.NetworkInterface.NetClient;

import controller.FieldSceneController;
import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MessageSignal;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.TileOnPosition;


/**
 * parser for the field.fxml
 *
 */
public class FieldParser extends InterfaceParser {

	private FieldSceneController fieldController;
	private NetClient client;

	/**
	 * @param controller
	 * @param client
	 * @param cycle
	 */
	public FieldParser(FieldSceneController controller, NetClient client, long cycle) {
		fieldController = controller;
		this.client = client;
		setClient(client);
		initClock(cycle);
		startQueueWorker();
	}

	/* (non-Javadoc)
	 * @see parser.InterfaceParser#parseMessage(de.upb.swtpra1819interface.messages.Message)
	 */
	@Override
	public void parseMessage(Message msg) {
		switch (msg.getUniqueId()) {
		case 307:
			MessageSignal messageSignal = (MessageSignal) msg;
			fieldController.updateChat(messageSignal.getClient(), messageSignal.getMessage());
			break;
		case 400:
			StartGame startGame = (StartGame) msg;
			fieldController.startGame(startGame.getClients(), startGame.getConfig());
			break;
		case 401:
			fieldController.endGame();
			break;
		case 402:
			fieldController.abortGame();
			break;
		case 403:
			fieldController.pauseGame();
			break;
		case 404:
			fieldController.resumeGame();
			break;
		case 406:
			LeavingPlayer leavingPlayer = (LeavingPlayer) msg;
			fieldController.playerLeft(leavingPlayer.getClient());
			break;
		case 407:
			Winner winner = (Winner) msg;
			fieldController.showWinner(winner);
			break;
		case 409:
			CurrentPlayer currentPlayer = (CurrentPlayer) msg;
			fieldController.updateCurrentPlayer(currentPlayer.getClient());
			break;
		case 416:
			Update update = (Update) msg;
			fieldController.updateTilesOnField((List<TileOnPosition>) update.getUpdates());
			fieldController.updateNumberTilesInBag(update.getNumberTilesInBag());
			break;
		case 418:
			ScoreResponse scoreResponse = (ScoreResponse) msg;
			fieldController.updateScores(scoreResponse.getScores());
			break;
		case 420:
			TurnTimeLeftResponse turnTimeLeftResponse = (TurnTimeLeftResponse) msg;
			fieldController.updateTurnTime(turnTimeLeftResponse.getTime());
			break;
		case 422:
			TotalTimeResponse totalTimeResponse = (TotalTimeResponse) msg;
			fieldController.updateTotalTime(totalTimeResponse.getTime());
			break;
		case 424:
			BagResponse bagResponse = (BagResponse) msg;
			fieldController.updateBag(bagResponse.getBag());
			break;
		case 426:
			PlayerHandsResponse playerHandsResponse = (PlayerHandsResponse) msg;
			fieldController.updatePlayerHand(playerHandsResponse.getHands());
			break;
		case 499:
			GameDataResponse gameDataResponse = (GameDataResponse) msg;
			fieldController.updateGameData(gameDataResponse);
			break;
		default:
			break;
		}
	}

}
