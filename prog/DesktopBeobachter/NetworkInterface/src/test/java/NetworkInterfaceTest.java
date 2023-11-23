//import static org.junit.Assert.*;
//import de.upb.swtpra1819interface.parser.*;
//import de.upb.swtpra1819interface.messages.*;
//import org.junit.Test;
//
//public class NetworkInterfaceTest {
//	//IMPORTANT: Only one test can be enabled on the same time or the loading times will cause all tests to fail.
//
//	// @Test
//	public void messageAddedToMessageQueue() {
//		NetworkServer server = new NetworkServer((short) 9000);
//
//		try {
//			server.addToMsgQueue(new Parser().serialize(new NotAllowed("test")), 1);
//		} catch (ParsingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		assertEquals(new NotAllowed("").getUniqueID(), server.getNextMsg().msg.getUniqueID());
//		server.shutdown();
//
//	}
//
//	// @Test
//	public void MessageFromClientToServer() throws InterruptedException {
//		NetworkServer server = new NetworkServer((short) 9001);
//		Client client = new Client((short) 9001, "localhost");
//
//		client.sendMsg(new NotAllowed("test"));
//
//		Thread.sleep(1900);
//
//		MessageWithClientId msg = server.getNextMsg();
//
//		if (msg != null) {
//			assertEquals(new NotAllowed("").getUniqueID(), msg.msg.getUniqueID());
//		} else {
//			fail("nullpointer");
//		}
//
//		server.shutdown();
//
//	}
//
//	// @Test
//	public void MessageFromServerToClient() {
//		NetworkServer server = new NetworkServer((short) 9002);
//		Client client = new Client((short) 9002, "localhost");
//
//		server.sendMsg(new NotAllowed("test"));
//
//		try {
//			Thread.sleep(700);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		Message msg = client.getNextMsg();
//		if (msg != null) {
//			assertEquals(new NotAllowed("").getUniqueID(), msg.getUniqueID());
//		} else {
//			fail("nullpointer");
//		}
//
//		server.shutdown();
//
//	}
//
//	@Test
//	public void kickFromServer() {
//		NetworkServer server = new NetworkServer((short) 9003);
//		Client client = new Client((short) 9003, "localhost");
//
//		client.sendMsg(new NotAllowed(""));
//
//		assertTrue(server.kick((long) 1));
//	}
//
//}
