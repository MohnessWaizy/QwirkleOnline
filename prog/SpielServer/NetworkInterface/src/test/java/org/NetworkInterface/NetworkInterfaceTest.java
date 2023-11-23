package org.NetworkInterface;

import static org.junit.Assert.*;
import de.upb.swtpra1819interface.parser.*;
import de.upb.swtpra1819interface.messages.*;
import org.junit.Test;

public class NetworkInterfaceTest {
	// IMPORTANT: Only one test can be enabled on the same time or the loading times
	// will cause all tests to fail.

	// @Test
	public void messageAddedToMessageQueue() {
		NetworkServer server = new NetworkServer((short) 9000);

		try {
			server.addToMsgQueue(new Parser().serialize(new NotAllowed("test", 0)), 1);
		} catch (ParsingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertEquals(new NotAllowed("", 0).getUniqueId(), server.getNextMsg().getMsg().getUniqueId());
		server.shutdown();

	}

	// @Test
	public void MessageFromClientToServer() throws InterruptedException {
		NetworkServer server = new NetworkServer((short) 9001);
		Client client = new Client((short) 9001, "localhost");

		client.sendMsg(new NotAllowed("test", 0));

		Thread.sleep(1900);

		MessageWithClientId msg = server.getNextMsg();

		if (msg != null) {
			assertEquals(new NotAllowed("", 0).getUniqueId(), msg.getMsg().getUniqueId());
		} else {
			fail("nullpointer");
		}

		server.shutdown();

	}

    //@Test
	public void MessageFromServerToClient() {
		NetworkServer server = new NetworkServer((short) 9002);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client = new Client((short) 9002, "localhost");

		server.sendMsg(new NotAllowed("test", 0));

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Message msg = client.getNextMsg();
		if (msg != null) {
			assertEquals(new NotAllowed("", 0).getUniqueId(), msg.getUniqueId());
		} else {
			fail("nullpointer");
		}

		server.shutdown();

	}

	@Test
	public void kickFromServer() {
		NetworkServer server = new NetworkServer((short) 9003);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client = new Client((short) 9003, "localhost");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		client.sendMsg(new NotAllowed("", 0));

		assertTrue(server.kick(0));
	}

}
