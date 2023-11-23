//package AhoiSoftware.SpielServer;
//
//import static org.junit.Assert.*;
//
//import org.GameLogic.Handler.Game;
//import org.NetworkInterface.NetworkServer;
//
//import de.upb.swtpra1819interface.messages.ClientType;
//import AhoiSoftware.SpielServer.GameManagement;
//import org.junit.Test;
//
//
//public class SpielManagementTest {
//	
//	@Test
//	public void createGamesTest() {
//		Game game = new Game("",1,false, null);
//		GameManagement manager = new GameManagement(new NetworkServer((short)9000));
//		assertTrue("Game was not created successful!",manager.createGame(game));
//	}
//	
//	@Test(expected=NullPointerException.class)
//	public void createGamesHandlesNull() {
//		GameManagement manager = new GameManagement(new NetworkServer((short)9000));
//		manager.createGame(null);
//	}
//	
//	@Test
//	public void moveToGameTest() {
//		GameManagement manager = new GameManagement(new NetworkServer((short)9000));
//		assertTrue(manager.moveToGame(0, 0, ClientType.PLAYER));
//	}
//	
//	@Test
//	public void moveToGameHandlesNonExistentData() {
//		GameManagement manager = new GameManagement(new NetworkServer((short)9000));
//		assertFalse(manager.moveToGame(0, 0, ClientType.PLAYER));
//	}
//
//}
