//package org.GameLogic;
//
//import static org.junit.Assert.*;
//
//import org.GameLogic.DataStructures.Lobby;
//import org.GameLogic.DataStructures.Player;
//import org.GameLogic.Exceptions.ParserException;
////import org.GameLogic.DataStructures.GameConfiguration;
//import org.GameLogic.Handler.Game;
//import org.junit.Test;
//
//public class LobbyTest {
//	@Test
//	public void generalTests() throws ParserException {
//		
//	}
//	
//	@Test
//	public void gameListRequest() throws ParserException {
//		Lobby l = new Lobby();
//		GameConfiguration gc = new GameConfiguration("no");
//		
//		Game g = new Game(null, 0, false, gc);
//		
//		l.addGame(g);
//		
//		assertEquals(g, l.gameListRequest().get(0));
//		
//		l.addGame(g);
//		
//		assertEquals(1, l.gameListRequest().size());
//		
//		Game g1 = new Game("1", 0, false, gc);
//		l.addGame(g1);
//		
//		assertEquals(1, l.gameListRequest().size());
//	}
//	
//	@Test
//	public void gameJoinRequest() throws ParserException {
//		Lobby l = new Lobby();
//		GameConfiguration gc = new GameConfiguration("no");
//		
//		Game g = new Game("1", 0, false, gc);
//		Game g1 = new Game("2", 1, false, gc);
//		Player p = new Player(0, "Tim");
//		
//		l.addGame(g);
//		l.addGame(g1);
//		
//		assertEquals(p, l.gameListRequest().get(0).getPlayers().get(0));
//	}
//	
//	@Test
//	public void spectatorJoinRequest() {
//		// Todo: look at Lobby.spectatorJoinRequest.
//	}
//}
