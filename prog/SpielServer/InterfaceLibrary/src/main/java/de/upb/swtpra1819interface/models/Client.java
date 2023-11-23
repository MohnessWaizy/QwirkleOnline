package de.upb.swtpra1819interface.models;

import java.util.Objects;

public class Client {
	private int clientId;
	private String clientName;
	private ClientType clientType;

	public Client(int clientId, String clientName, ClientType clientType) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.clientType = clientType;
	}

	public int getClientId() {
		return clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Client)) {
			return false;
		}
		Client client = (Client) o;
		return (getClientId() == client.getClientId()) && (Objects.equals(getClientName(), client.getClientName()))
				&& (getClientType() == client.getClientType());
	}

	public int hashCode() {
		return Objects.hash(new Object[] { Integer.valueOf(getClientId()), getClientName(), getClientType() });
	}

	public void setClientType(ClientType type) {
		clientType = type;

	}
}
