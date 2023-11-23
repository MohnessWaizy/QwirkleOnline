package parser;

import java.util.ArrayList;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;
import org.GameLogic.Util.Converter;
import org.NetworkInterface.NetClient;

import AiController.AiController;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.GameJoinAccepted;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.SendTiles;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.StartTiles;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * A parser for processing incoming messages from the server
 */
public class AiParser extends InterfaceParser {

	private AiController contrl;
	private Converter conv;

	/**
	 * Initialise the parser.
	 * 
	 * @param client NetClient to send messages to the server.
	 * @param contrl Controller to control the AI
	 * @param cycle  long to set the clock
	 */
	public AiParser(NetClient client, AiController contrl, long cycle) {
		this.contrl = contrl;
		super.setClient(client);
		super.initClock(cycle);
		super.startQueueWorker();
		conv = new Converter();

	}

	@Override
	public void parseMessage(Message msg) {

		switch (msg.getUniqueId()) {
		/*
		 * Notifies the AI that the ConnectRequest (100) was successful.
		 */
		case 101:
			ConnectAccepted ca = (ConnectAccepted) msg;
			contrl.loginRequestIsValid(ca.getClientId());
			break;

		/*
		 * 
		 */
		case 920:
			contrl.loginRequestNotValid();
			break;

		/*
		 * Notifies the AI ​​that it has been assigned to a game. No request necessary
		 */
		case 303:
			GameJoinAccepted gja = (GameJoinAccepted) msg;
			contrl.gameJoinAccepted(gja.getGame());
			break;

		/*
		 * Notifies the AI that the game start and sends the configuration as well as a
		 * list of all players.
		 */
		case 400:
			StartGame sg = (StartGame) msg;
			contrl.startGame(sg.getConfig());
			break;

		/*
		 * Notifies that AI about the regular end of the game, which is conventionally
		 * caused the victory of a player or by the fact that only one player is
		 * involved in the game.
		 */
		case 401:
			contrl.stopGame();
			break;

		/*
		 * Notifies the AI about the termination of the game by the game host
		 */
		case 402:
			contrl.stopGame();
			break;

		/*
		 * Notifies the AI that the game host has paused the game.
		 */
		case 403:
			contrl.pauseGame();
			break;

		/*
		 * Notifies the AI that the game host is resuming the game.
		 */
		case 404:
			contrl.resumeGame();
			break;

		/*
		 * Player left the game
		 */
		case 406:
			break;

		/*
		 * winner of the current game
		 */
		case 407:
			Winner w = (Winner) msg;
			contrl.printWinner(w);
			break;

		/*
		 * Sends the player the first hand with the configured number of tiles.
		 */
		case 408:
			StartTiles st = (StartTiles) msg;
			contrl.setTilesOnHand(new ArrayList<Tile>(st.getTiles()));
			break;

		/*
		 * Notifies the AI which player's turn it is.
		 */
		case 409:
			CurrentPlayer cp = (CurrentPlayer) msg;
			contrl.currentPlayer(cp.getClient().getClientId());
			break;

		/*
		 * Fills the player's hand with tiles from the bag.
		 */
		case 410:
			SendTiles sendTiles = (SendTiles) msg;
			contrl.setTilesOnHand(new ArrayList<Tile>(sendTiles.getTiles()));
			break;

		/*
		 * Notifies the AI that the TileSwapRequest (411) and informs if the swap is
		 * valid.
		 */
		case 412:
			TileSwapValid tsv = (TileSwapValid) msg;
			if (tsv.isValidation()) {
				System.out.println("AI swap tiles...");
				contrl.tileSwapIsValid();
			} else {
				System.out.println("AI swap was not valid!");
			}
			break;

		/*
		 * The server swap the tiles of a TileSwapRequest (411) and sends new tiles to
		 * the player.
		 */
		case 413:
			TileSwapResponse tsr = (TileSwapResponse) msg;
			contrl.setTilesOnHand(new ArrayList<Tile>(tsr.getTiles()));
			break;

		/*
		 * The server informs the player if a PlayTiles (414) corresponds to a valid
		 * move.
		 */
		case 415:
			MoveValid mv = (MoveValid) msg;
			contrl.moveIsValid(mv.isValidation());
			break;

		/*
		 * The server informs which new tiles have been placed on the map and where.
		 */
		case 416:
			Update up = (Update) msg;
			/*
			 * Get List with TileOnPosition and covert it to ArrayList with Tuple with
			 * Coordinate and Tile
			 */
			ArrayList<Tuple<Coordinate, Tile>> coordTiles = conv
					.toGameCoordTile(new ArrayList<TileOnPosition>(up.getUpdates()));

			contrl.setTilesOnMap(coordTiles);
			break;

		default:
			System.err.println("unknown message");
			break;
		}
	}

	@Override
	public void timeElapsed() {
	}

}
