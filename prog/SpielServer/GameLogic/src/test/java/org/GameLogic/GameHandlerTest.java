package org.GameLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Communication.CommunicationHandlerCallback;
import org.GameLogic.DataStructures.Player;
import org.GameLogic.DataStructures.Tuple;
import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Exceptions.ParserException;
import org.GameLogic.Handler.GameHandler;

import org.junit.Test;

import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.WrongMove;

public class GameHandlerTest {

	private GameHandler gameHandler;
	private final static Configuration STANDARDCONFIGURATION = new Configuration(6, 1, 6, 30, 10, WrongMove.NOTHING, 4,
			SlowMove.POINT_LOSS, 0, 2);
	private CommunicationHandlerCallback chc = new ComHandler();

	public class ComHandler implements CommunicationHandlerCallback {

		@Override
		public void callbackPlayerKicked(Player player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void callbackNextPlayer(Player player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void callbackPlayerTiles(Player player, List<Tile> newHandTiles) {
			// TODO Auto-generated method stub

		}

		@Override
		public void callbackGameFinish(boolean rightfullyFinished) {
			// TODO Auto-generated method stub

		}

	}

	private Client clientPlayer1 = new Client(0, "Name1", ClientType.PLAYER);
	private Client clientSpectator1 = new Client(1, "Name2", ClientType.SPECTATOR);
	private Client clientPlayer2 = new Client(2, "Name3", ClientType.PLAYER);
	private Client clientPlayer3 = new Client(3, "Name4", ClientType.PLAYER);
	private Client clientSpectator2 = new Client(4, "Name5", ClientType.SPECTATOR);

	@Test
	public void testRequestJoinNewClient() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			assertTrue(1 == gameHandler.getPlayers().size());
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestJoinMaxClients() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer1);
			gameHandler.requestJoin(clientPlayer2);
			assertTrue(2 == gameHandler.getPlayers().size());
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestJoinTooMuchClientsNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer1);
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			fail();
		} catch (NotAllowedException ex) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestJoinTwiceSpectatorNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientSpectator1);
			gameHandler.requestJoin(clientSpectator1);
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void TestRequestRemoveClientIsAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestRemove(clientPlayer2);
			assertTrue(0 == gameHandler.getPlayers().size());
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestRemoveClientGameOverAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.endGame();
			gameHandler.requestRemove(clientPlayer1);
			assertTrue(true);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testRequestRemoveSpectatorAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);
			gameHandler.requestJoin(clientSpectator1);
			gameHandler.startGame();
			gameHandler.requestRemove(clientSpectator1);
			assertTrue(gameHandler.getSpectators().size() == 0);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testRequestRemoveSpectatorIsAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);
			gameHandler.requestJoin(clientSpectator2);

			gameHandler.startGame();

			gameHandler.requestRemove(clientSpectator1);

			assertTrue(true);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestRemoveNotExistingClient() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestRemove(clientPlayer1);

			assertTrue(true);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestRemoveClientAndGameOverAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);

			gameHandler.startGame();

			gameHandler.requestRemove(clientPlayer1);

			assertEquals(GameState.ENDED, gameHandler.getGameState());
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testRequestCurrentPayerAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);

			gameHandler.startGame();

			assertTrue(gameHandler.requestCurrentPayer() == gameHandler.getPlayers().get(0));
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestRequestJoinPlayerToStartingGameNotAllowed() {
		Configuration config = new Configuration(6, 1, 6, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();

			gameHandler.requestJoin(clientPlayer1);
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testStartGameOnePlayerNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.startGame();
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void TestStartGameTwoPlayersAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();

			assertTrue(gameHandler.getGameState() == GameState.IN_PROGRESS);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testStartGameEdnedNotAllowed() {
		Configuration config = new Configuration(6, 1, 6, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.endGame();
			gameHandler.startGame();
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);

		}
	}

	@Test
	public void TestRequestJoinSpectatorToStartingGameAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();

			gameHandler.requestJoin(clientSpectator1);
			assertTrue(gameHandler.getSpectators().size() == 1);
		} catch (NotAllowedException e) {
			fail(e.getMessage());
		}
	}
	/*
	@Test
	public void TestEndStartingGameAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.endGame();
			assertTrue(gameHandler.getGameState() == GameState.ENDED);
		} catch (NotAllowedException e) {
			fail();
		}
	}
	*/
	@Test
	public void TestEndEndedGameNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.endGame();
			gameHandler.endGame();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testPauseGameStartingAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.pauseGame();
			assertTrue(gameHandler.getGameState() == GameState.PAUSED);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void TestPauseGameStartedNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.pauseGame();
			assertTrue("Missing exception after pausing not started game", false);
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testResumeGameAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.pauseGame();
			gameHandler.resumeGame();
			assertTrue(gameHandler.getGameState() == GameState.IN_PROGRESS);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testResumeGameNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.resumeGame();
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);

		}
	}

	@Test
	public void testRequestWinnerGameNotFinishedNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.requestWinner();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestWinnerWinnerReturned() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
			gameHandler.endGame();
			assertNotNull(gameHandler.requestWinner());
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testRequestPlayTilesPlayerNotAllowedToSetTiles() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();

		try {
			gameHandler.requestPlayTiles(clientPlayer1, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);

		}

	}

	@Test
	public void testRequestPlayNoTilesNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();

		try {
			gameHandler.requestPlayTiles(clientPlayer2, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestPlayNotCurrentNotAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.startGame();

		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestPlayerTilesAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestPlayerTiles(clientPlayer2);

			Player player1 = gameHandler.getPlayers().get(0);

			assertTrue(player1.getTiles().size() == 0);
		} catch (NotAllowedException e) {
			fail();
		}
	}

	@Test
	public void testRequestPlayerHasNotTilesNotAllowed() {
		Configuration config = new Configuration(6, 1, 3, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);
		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);

			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestTilesNotValidePointLoss() {
		Configuration config = new Configuration(6, 1, 3, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);

		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);

			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.requestPlayerHands().get(clientPlayer3).add(new Tile(0, 70, 1));
		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 70, 1)));

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException e1) {
			fail();
		} catch (ParserException e1) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestPlayerTilesClientNotPlayerAllowed() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestPlayerTiles(clientPlayer2);
			fail();
		} catch (NotAllowedException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestTilesNotValideNotAllowed() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);

			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), new Tile(0, 1, 2)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), new Tile(0, 2, 3)));

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTilesNotStartedNotAllowed() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), new Tile(0, 1, 2)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), new Tile(0, 2, 3)));

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTilesTooManyNotAllowed() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), new Tile(0, 1, 2)));
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), new Tile(0, 2, 3)));
		List<Tile> toBeAddTiles = new ArrayList<Tile>();
		toBeAddTiles.add(new Tile(0, 0, 1));
		toBeAddTiles.add(new Tile(0, 1, 2));
		toBeAddTiles.add(new Tile(0, 2, 3));
		gameHandler.getPlayers().get(0).getTiles().addAll(toBeAddTiles);

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTiles() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 0, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tuple<Coordinate, Tile>> tilesWithCoordinate = new ArrayList<Tuple<Coordinate, Tile>>();
		tilesWithCoordinate.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));

		List<Tile> toBeAddTiles = new ArrayList<Tile>();
		toBeAddTiles.add(new Tile(0, 0, 1));

		assertTrue(gameHandler.requestScore().get(clientPlayer3) == 0);
		gameHandler.getPlayers().get(0).getTiles().addAll(toBeAddTiles);

		try {
			gameHandler.requestPlayTiles(clientPlayer3, tilesWithCoordinate);
			assertTrue(gameHandler.requestScore().get(clientPlayer3) == 1);
		} catch (NotAllowedException e1) {
			fail();
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestGameDataPlayer() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientSpectator1);
			assertTrue(gameHandler.requestGameData(clientPlayer2) instanceof GameDataResponse);
			assertTrue(gameHandler.requestGameData(clientSpectator1) instanceof GameDataResponse);
		} catch (NotAllowedException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTileSwapNotCrrent() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();

		try {
			gameHandler.requestTileSwap(clientPlayer3, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestTileSwapNoTilesToSetTiles() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();

		try {
			gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles);
			fail();

		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTileSwapNoTilesNotAllowedToSetTiles() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.add(new Tile(0, 0, 1));

		try {
			gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestTileSwapNoValidate() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);
		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 1, -2));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(3, 2, 3));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 3, 4));

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.add(new Tile(0, 1, -2));
		toBeSwappedTiles.add(new Tile(3, 2, 3));
		toBeSwappedTiles.add(new Tile(0, 3, 4));

		try {
			gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException | ParserException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestTileSwapTooManyTiles() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.KICK, 4, SlowMove.POINT_LOSS, 0, 3);

		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 1, 2));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(3, 2, 3));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 3, 4));

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.add(new Tile(0, 1, 2));
		toBeSwappedTiles.add(new Tile(3, 2, 3));
		toBeSwappedTiles.add(new Tile(0, 3, 4));

		try {
			gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTileSwapBagSize() {
		Configuration config = new Configuration(3, 1, 4, 30, 10, WrongMove.NOTHING, 4, SlowMove.KICK, 0, 3);

		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 0, 2));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 1, 3));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(1, 0, 4));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(1, 1, 5));

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.add(new Tile(0, 0, 2));
		toBeSwappedTiles.add(new Tile(0, 1, 3));
		toBeSwappedTiles.add(new Tile(1, 0, 4));
		toBeSwappedTiles.add(new Tile(1, 1, 5));

		try {
			gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTileSwapNotStartet() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 1, 2));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 2, 3));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 3, 4));

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.add(new Tile(0, 1, 2));
		toBeSwappedTiles.add(new Tile(0, 2, 3));

		try {
			assertTrue(gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles).size() == 2);
		} catch (NotAllowedException e1) {
			assertTrue(gameHandler.getGameState() == GameState.NOT_STARTED);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestTileSwap() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
			gameHandler.startGame();
		} catch (NotAllowedException e1) {
			fail();
		}

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();
		toBeSwappedTiles.addAll(gameHandler.requestPlayerHands().get(clientPlayer2));

		try {
			assertTrue(gameHandler.requestTileSwap(clientPlayer2, toBeSwappedTiles).size() == gameHandler.getPlayers()
					.get(0).getTiles().size());
		} catch (NotAllowedException e1) {
			fail();
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testRequestPlayerTilesBySpectator() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientSpectator1);

			gameHandler.requestPlayerTiles(clientSpectator1);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		}
	}

	@Test
	public void testRequestPlayerTilesPlayer() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			gameHandler.requestPlayerTiles(clientPlayer2);

			assertTrue(gameHandler.requestPlayerTiles(clientPlayer2) != null);
			assertTrue(gameHandler.requestPlayerTiles(clientPlayer2) instanceof ArrayList<?>);
			assertTrue(gameHandler.requestPlayerTiles(clientPlayer2).size() == 0);

			gameHandler.startGame();
			assertTrue(gameHandler.requestPlayerTiles(clientPlayer2).size() == 6);
		} catch (NotAllowedException e1) {
			fail();
		}
	}

	@Test
	public void testCheckWrongMove() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.KICK, 4, SlowMove.KICK, 0, 3);

		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);

			assertTrue(true);
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 1, 2));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 2, 3));
		gameHandler.getPlayers().get(0).getTiles().add(new Tile(0, 3, 4));

		List<Tile> toBeSwappedTiles = new ArrayList<Tile>();

		try {
			gameHandler.requestTileSwap(clientPlayer3, toBeSwappedTiles);
			fail();
		} catch (NotAllowedException e1) {
			assertTrue(true);
		} catch (ParserException e1) {
			fail();
		}
	}

	@Test
	public void testTurnTimeElapsed() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.POINT_LOSS, 20, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);

			gameHandler.getPlayers().get(0).modifyScore(100);
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.turnTimeElapsed();
		assertTrue(gameHandler.getPlayers().get(0).getScore() == 80);
	}

	@Test
	public void testTurnTimeElapsedKik() {
		Configuration config = new Configuration(6, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.KICK, 20, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer1);

			gameHandler.getPlayers().get(0).modifyScore(100);
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.turnTimeElapsed();
		assertTrue(gameHandler.getPlayers().get(0).getId() == 0);
	}

	@Test
	public void testVisualisationTimeElapsedBagFull() {
		gameHandler = new GameHandler(chc, STANDARDCONFIGURATION);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
		} catch (NotAllowedException e1) {
			fail();
		}

		assertTrue("client3 is current", gameHandler.getPlayers().get(0).getId() == 2);
		assertTrue("client3 has no Tiles in Hand", gameHandler.getPlayers().get(0).getTilesOnHandCount() == 0);
		gameHandler.visualisationTimeElapsed();
		assertFalse("client3 is not current", gameHandler.getPlayers().get(0).getId() == 3);
		assertTrue("client3 has 6 Tiles in Hand", gameHandler.getPlayers().get(1).getTilesOnHandCount() == 6);
	}

	@Test
	public void testVisualisationTimeElapsedBagEmpty() {
		Configuration config = new Configuration(2, 1, 2, 30, 10, WrongMove.NOTHING, 4, SlowMove.KICK, 20, 3);
		gameHandler = new GameHandler(chc, config);

		try {
			gameHandler.requestJoin(clientPlayer2);
			gameHandler.requestJoin(clientPlayer3);
		} catch (NotAllowedException e1) {
			fail();
		}

		gameHandler.requestBag().removeAll(gameHandler.requestBag());
		assertTrue(gameHandler.getPlayers().get(1).getScore() == 0);
		assertTrue(gameHandler.getPlayers().get(0).getScore() == 0);

		gameHandler.requestPlayerHands().get(clientPlayer3).add(new Tile(1, 0, 1));
		gameHandler.visualisationTimeElapsed();
		assertTrue(gameHandler.getPlayers().get(1).getScore() == 0);
		assertTrue(gameHandler.getPlayers().get(0).getScore() == 6);
	}
}
