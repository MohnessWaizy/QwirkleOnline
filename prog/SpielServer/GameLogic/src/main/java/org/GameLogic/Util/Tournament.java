package org.GameLogic.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;

public class Tournament {
	
	private Set<Game> games;
	private List<Client> clientsNotInGame;
	private String tournamentName;
	private Configuration config;
	
	public Tournament(String tournamentName, Configuration config) {
		this.games = new HashSet<Game>();
		this.clientsNotInGame = new ArrayList<Client>();
		this.tournamentName = tournamentName;
		this.config = config;
	}
	
	public String getTournamentName() {
		return tournamentName;
	}

	public Configuration getConfig() {
		return config;
	}

	public Set<Game> getGames() {
		return games;
	}
	
	public List<Client> getClients(){
		return clientsNotInGame;
	}

	public void setClientsNotInGame(List<Client> clientsNotInGame) {
		this.clientsNotInGame = clientsNotInGame;
	}

}
