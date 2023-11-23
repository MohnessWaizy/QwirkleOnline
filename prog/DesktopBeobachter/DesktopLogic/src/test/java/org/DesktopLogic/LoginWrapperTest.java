package org.DesktopLogic;

import static org.junit.Assert.*;

import org.NetworkInterface.NetClient;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Game;
import model.LoginWrapper;

public class LoginWrapperTest {
	private Client client;
	private NetClient netClient;
	private Game game;
	private  LoginWrapper loginWrapper;

	@Test
	public void test() {
		loginWrapper = new LoginWrapper(netClient, 100, "Client1", ClientType.PLAYER);
		assertTrue(loginWrapper.getInterfaceClient() instanceof Client);
		
	}

}
