package de.upb.cs.swtpra_04.qwirkle.model.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.GameState;

import static org.junit.Assert.*;

public class GameDataTest {

    GameData gameData;

    GameState stateProgress = GameState.IN_PROGRESS;
    GameState stateNotStarted = GameState.NOT_STARTED;
    GameState stateEnded = GameState.ENDED;
    GameState statePaused = GameState.PAUSED;
    Boolean isTournament = true;
    Boolean notTournament = false;

    @Test
    public void removeTilesFromHand() {
        List<Tile> tiles1 = new ArrayList<Tile>();
        Client c1 = new Client(1,"One",ClientType.PLAYER);

        Tile t1 = new Tile(1,2);
        Tile t2 = new Tile(1,2);
        Tile t3 = new Tile(1,2);
        Tile t4 = new Tile(1,2);

        tiles1.add(t1);
        tiles1.add(t2);
        tiles1.add(t3);
        tiles1.add(t4);

        List<Player> players = new ArrayList<Player>();
        Player p1 = new Player(1,"One");

        players.add(p1);

        gameData = new GameData(stateProgress,notTournament);
        gameData.setPlayers(players);
        gameData.addPlayedTilesToHand(1,tiles1);
        gameData.removeTilesFromHand(1,tiles1);

        assertTrue(gameData.getPlayers().get(0).getTiles().size() == 0);
    }

    @Test
    public void addPlayedTilesToHand() throws Exception {
        List<Tile> tiles1 = new ArrayList<Tile>();
        Client c1 = new Client(1,"One",ClientType.PLAYER);

        Tile t1 = new Tile(1,2);
        Tile t2 = new Tile(1,2);
        Tile t3 = new Tile(1,2);
        Tile t4 = new Tile(1,2);

        tiles1.add(t1);
        tiles1.add(t2);
        tiles1.add(t3);
        tiles1.add(t4);

        List<Player> players = new ArrayList<Player>();
        Player p1 = new Player(1,"One");

        players.add(p1);

        gameData = new GameData(stateProgress,notTournament);
        gameData.setPlayers(players);
        gameData.addPlayedTilesToHand(1,tiles1);
        assertTrue(gameData.getPlayers().get(0).getTiles().size() == 4);
    }

    @Test
    public void getPlayerById(){
        List<Player> players = new ArrayList<Player>();
        Player p1 = new Player(1,"One");

        players.add(p1);

        gameData = new GameData(stateProgress,notTournament);
        gameData.setPlayers(players);

        assertTrue(gameData.getPlayerById(1) == p1);
    }


    @Test
    public void setPlayersFromClients() {
        List<Player> players = new ArrayList<Player>();

        Client c1 = new Client(1,"One",ClientType.PLAYER);
        Client c2 = new Client(2,"Two",ClientType.PLAYER);
        Client c3 = new Client(3,"Three",ClientType.PLAYER);

        ArrayList<Client> clients = new ArrayList<Client>();

        clients.add(c1);
        clients.add(c2);
        clients.add(c3);

        gameData = new GameData(stateProgress,notTournament);
        gameData.setPlayersFromClients(clients);

        assertTrue(gameData.getPlayers().size() == 3);

    }
}