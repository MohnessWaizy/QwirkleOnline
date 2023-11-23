package AiController;

import java.util.ArrayList;

import org.AILogic.AiLogic;
import org.AILogic.DetailedFindMove;
import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;
import org.GameLogic.Util.Converter;
import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.messages.*;
import de.upb.swtpra1819interface.models.*;
import parser.AiParser;

/**
 * The controller manages the AI. From here, messages are sent to the server
 * using the NetClient and calculations are coordinated.
 */
public class AiController implements AiControllerInterface {

	/*
	 * Client object to send massages to the server
	 */
	private NetClient client;

	/*
	 * clientId is the id assigned by the server
	 */
	private int clientId;

	/*
	 * The logic to make a move in the game
	 */
	private AiLogic logic;

	/*
	 * Converter to convert CoordTiles
	 */
	private Converter conv;

	/*
	 * Last correct move
	 */
	private ArrayList<Tuple<Coordinate, Tile>> lastMove;

	/*
	 * If game is tournament, this variable is true
	 */
	private boolean tournament;

	/**
	 * The login to the server is checked and a parser to process incoming messages
	 * from the server.
	 * 
	 * @param client     NetClient to send messages to the server
	 * @param clientName Name that is displayed in the game
	 */
	public AiController(NetClient client, String clientName) {
		this.client = client;

		/*
		 * First check if connection is established to server. This method will block
		 * thread for several seconds.
		 */
		if (client.checkConnection() == true) {
			/*
			 * Connection is established, so we can send connect request.
			 */
			sendConnectRequest(clientName);

			/*
			 * Parser to process incoming messages from server
			 */
			AiParser parser = new AiParser(client, this, 200);
			conv = new Converter();
		} else {
			System.out.println("Unable to establish connection to server...");
		}
	}

	@Override
	public void sendConnectRequest(String clientName) {
		ConnectRequest con = new ConnectRequest(clientName, ClientType.PLAYER);
		client.sendMsg(con);
		System.out.println("Send connect request to server with client name: " + clientName);
	}

	@Override
	public void loginRequestIsValid(int clientId) {
		System.out.println("Connect to server with ID: " + clientId);
		System.out.println("");
		this.clientId = clientId;
	}

	@Override
	public void loginRequestNotValid() {
		System.err.println("Login request was not accepted...");
		System.out.println("AI is terminated...");
		System.exit(0);
	}

	@Override
	public void gameJoinAccepted(Game game) {
		System.out.println("");
		System.out.println("AI joined game :" + game.getGameName() + ", with gameID: " + game.getGameId());
		/*
		 * Check, if current game is a tournament
		 */
		if (game.isTournament() == true) {
			System.out.println("This is a tournament!");
			tournament = true;
		} else {
			tournament = false;
		}
	}

	@Override
	public void startGame(Configuration config) {
		/*
		 * Create the logic to calculate the moves
		 */
		logic = new AiLogic(config);
		/*
		 * Set the correct strategy
		 */
		logic.setStrategy(new DetailedFindMove());

		System.out.println("");
		System.out.println("Game is running...");

	}

	@Override
	public void stopGame() {
		System.out.println("");
		System.out.println("The game is over...");
	}

	@Override
	public void currentPlayer(int clientId) {
		/*
		 * Check, if the current player id, is id of the AI
		 */
		if (this.clientId == clientId) {
			System.out.println("");
			System.out.println("AI is on turn...");
			onTurn();
		}

	}

	@Override
	public void setTilesOnHand(ArrayList<Tile> tiles) {
		logic.addTilesToHand(tiles);
	}

	@Override
	public void setTilesOnMap(ArrayList<Tuple<Coordinate, Tile>> coordTiles) {
		logic.updateMap(coordTiles);
	}

	@Override
	public void tileSwapIsValid() {
		logic.removeAllTilesFormHand();
	}

	@Override
	public void moveIsValid(boolean isValid) {
		/*
		 * Check, if move is valid
		 */
		if (isValid) {
			System.out.println("A correct move has been made...");
			/*
			 * remove set tiles from hand
			 */
			ArrayList<Tile> tiles = new ArrayList<Tile>();
			for (Tuple<Coordinate, Tile> coordTile : lastMove) {
				tiles.add(coordTile.getSecond());
			}
			logic.removeTilesFromHand(tiles);
		} else {
			System.out.println("A wrong move has been made...");
		}
	}

	/**
	 * Is called, if AI is on turn, and find a valid move or swap tiles
	 */
	private void onTurn() {
		lastMove = logic.findMove();

		/*
		 * Check move
		 */
		if (lastMove == null) {
			/*
			 * Swap all tiles on hand
			 */
			TileSwapRequest sr = new TileSwapRequest(logic.getHand());
			client.sendMsg(sr);
		} else if (lastMove.size() == 0) {
			/*
			 * if slow move gives more points
			 */
			System.out.println("A slow move has been made...");
		} else {
			/*
			 * send tiles to server
			 */
			PlayTiles pt = new PlayTiles(conv.toNetworkTileOnPosition(lastMove));
			client.sendMsg(pt);
		}

	}

	@Override
	public void pauseGame() {
		System.out.println("The game is paused");
	}

	@Override
	public void resumeGame() {
		System.out.println("The game continues");
	}

	@Override
	public void printWinner(Winner winner) {
		/*
		 * Check, if AI is the winner of the current game
		 */
		if (winner.getClient().getClientId() == clientId) {
			System.out.println("This AI won the current game with " + winner.getScore() + " points!");
		} else {
			System.out.println(
					winner.getClient().getClientName() + " won the current game with " + winner.getScore() + " points");
		}

		/*
		 * exit AI, if it is no tournament
		 */
		if (!tournament) {
			client.sendMsg(new LeavingRequest());
			System.out.println("AI is terminated...");
			System.exit(0);
		}
	}
}
