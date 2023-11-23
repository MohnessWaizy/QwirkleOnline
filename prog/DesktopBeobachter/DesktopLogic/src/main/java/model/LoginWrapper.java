package model;

import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;

/**
 * The class is used to pass multiple variables to another Scene
 * 
 *
 */
public class LoginWrapper {
	
	NetClient networkClient;
	Client interfaceClient;
	
	/**
	 * Constructor of the LoginWrapper class, which saves the NetClient and Client informations
	 * 
	 * @param client The {@link NetClient} created in the Login
	 * @param clientId Client Id passed by the Server
	 * @param clientName Client Name used to log in
	 * @param clientType {@link ClientType}
	 */
	public LoginWrapper(NetClient client, int clientId, String clientName, ClientType clientType) {
		interfaceClient = new Client(clientId, clientName, clientType);
		networkClient = client;
	}


	public NetClient getNetworkClient() {
		return networkClient;
	}


	public void setNetworkClient(NetClient networkClient) {
		this.networkClient = networkClient;
	}


	public Client getInterfaceClient() {
		return interfaceClient;
	}


	public void setInterfaceClient(Client interfaceClient) {
		this.interfaceClient = interfaceClient;
	}

}
