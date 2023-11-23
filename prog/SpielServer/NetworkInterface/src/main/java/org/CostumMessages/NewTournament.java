package org.CostumMessages;

import java.util.ArrayList;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;

/**
 * This class is used to signal the lobby that a new tournament was created.
 *
 */
public class NewTournament extends Message {
	private static int tournamentIdCount = 0;
	private int tournamentId;
	private Configuration config;
	private String tournamentName;
	private ArrayList<Client> clients;

	public NewTournament(Configuration config, String tournamentName, ArrayList<Client> clients) {
		super(2140);
		this.tournamentId = tournamentIdCount;
		tournamentIdCount++;
		this.config = config;
		this.tournamentName = tournamentName;
		this.clients = clients;
	}

	public Configuration getConfig() {
		return config;
	}

	public String getTournamentName() {
		return tournamentName;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public int getTournamentId() {
		return tournamentId;
	}

}
