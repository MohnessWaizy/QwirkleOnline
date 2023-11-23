package org.ServerGui.Model;

import java.util.ArrayList;
import java.util.Hashtable;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Game;

public class DataContainer {

	private ArrayList<Client> playerIngame;
	private ArrayList<Client> playerInLobby;
	private Hashtable<Integer, ArrayList<Client>> gameToPlayerMapping;
	private Hashtable<Integer, Game> idToGameMapping;
	private Hashtable<Integer, ArrayList<Game>> idToTournamentMapping;
	private Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>> tournamentClientScoreMap;

	public DataContainer() {
		playerIngame = new ArrayList<Client>();
		playerInLobby = new ArrayList<Client>();
		gameToPlayerMapping = new Hashtable<Integer, ArrayList<Client>>();
		idToGameMapping = new Hashtable<Integer, Game>();
		idToTournamentMapping = new Hashtable<Integer, ArrayList<Game>>();
		tournamentClientScoreMap = new Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>>();
	}

	public Hashtable<Integer, Game> getGameIdMap() {
		return idToGameMapping;
	}

	public ArrayList<Client> getPlayerIngame() {
		return playerIngame;
	}

	public void setPlayerIngame(ArrayList<Client> playerIngame) {
		this.playerIngame = playerIngame;
	}

	public ArrayList<Client> getPlayerInLobby() {
		return playerInLobby;
	}

	public void setPlayerInLobby(ArrayList<Client> playerInLobby) {
		this.playerInLobby = playerInLobby;
	}

	public Hashtable<Integer, ArrayList<Client>> getGamePlayerMap() {
		return gameToPlayerMapping;
	}

	public Hashtable<Integer, ArrayList<Game>> getTournamentGameMap() {
		return idToTournamentMapping;
	}

	public void setTournamentGameMap(Hashtable<Integer, ArrayList<Game>> tournamentGameMap) {
		this.idToTournamentMapping = tournamentGameMap;
	}

	public Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>> getTournamentClientScoreMap() {
		return tournamentClientScoreMap;
	}

	public void setTournamentClientScoreMap(
			Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>> tournamentClientScoreMap) {
		this.tournamentClientScoreMap = tournamentClientScoreMap;
	}
}
