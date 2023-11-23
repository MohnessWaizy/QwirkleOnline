package de.upb.cs.swtpra_04.qwirkle.model.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;

/**
 * Holds intern representation of the current game.
 */
public class GameData {

    public static final int NOT_STARTED = 1;
    public static final int IN_PROGRESS = 2;
    public static final int PAUSED = 3;
    public static final int ENDED = 4;

    private int state;
    private boolean isTournament;
    private List<Player> players;
    private Player activePlayer;
    private Bag bag;
    private Configuration config;

    private List<GameDataListener> listeners = new ArrayList<>();

    /**
     * Constructor
     * Extracts needed information from LobbyGame object
     *
     * @param state        State of the game; e.g. in_progress, paused, etc.
     * @param isTournament Type of the game
     */
    public GameData(GameState state, boolean isTournament) {

        this.isTournament = isTournament;

        switch (state) {
            case NOT_STARTED:
                this.state = NOT_STARTED;
                break;
            case IN_PROGRESS:
                this.state = IN_PROGRESS;
                break;
            case PAUSED:
                this.state = PAUSED;
                break;
            case ENDED:
                this.state = ENDED;
                break;
        }
    }

    /**
     * Updates the tiles every player holds at the moment
     *
     * @param hands HashMap of Hands with Client as Key
     */
    public void updatePlayerHands(HashMap<Client, ArrayList<Tile>> hands) {
        // iterate over every entry in HashMap, find matching player in playerlist, and update the list in player object
        // creates new players for clients who were not yet in the game
        List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> newTileList;
        boolean foundPlayer;

        for (Map.Entry<Client, ArrayList<Tile>> entry : hands.entrySet()) {
            Client entryClient = entry.getKey();            // current Client
            ArrayList<Tile> entryList = entry.getValue();   // current Tile list (interface type)

            newTileList = new ArrayList<>();    // new empty Tile list (our type)
            foundPlayer = false;                // check if player was found (already in the game)

            // add all tiles to the new tile list
            for (Tile t : entryList) {
                newTileList.add(new de.upb.cs.swtpra_04.qwirkle.model.game.Tile(t));
            }

            // find player in player list and update his tiles
            for (Player curPlayer : players) {
                if (entryClient.getClientId() == curPlayer.getId()) {
                    foundPlayer = true;
                    curPlayer.setTiles(newTileList);
                    break;
                }
            }
            // if no matching player found for an entry, create a new player for it
            if (!foundPlayer) {
                Player newPlayer = new Player(entryClient.getClientId(), entryClient.getClientName(), newTileList);
                players.add(newPlayer);
            }

        }
        notifyOnUpdate();
    }

    /**
     *  Removes tiles from the players' hands
     * @param clientId
     * @param removedTiles
     */
    public void removeTilesFromHand(int clientId, List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> removedTiles) {
        List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> playerHand = new ArrayList<de.upb.cs.swtpra_04.qwirkle.model.game.Tile>();
        for (Player player : players) {
            if (player.getId() == clientId) {
                playerHand = player.getTiles();
                break;
            }
        }
        for (de.upb.cs.swtpra_04.qwirkle.model.game.Tile removeTile : removedTiles) {
            playerHand.remove(removeTile);
        }
    }

    /**
     * Adds tiles to players' hands
     *
     * @param clientId
     * @param tiles
     */
    public void addPlayedTilesToHand(int clientId, List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> tiles) {
        List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> playerHand;
        for (Player player : players) {
            if (player.getId() == clientId) {
                playerHand = player.getTiles();
                playerHand.addAll(tiles);
                player.setTiles(playerHand);
                break;
            }
        }

    }

    public Configuration getConfig() {
        return this.config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    /**
     * Updates the scores of every player
     *
     * @param scores HashMap of Scores with Client as Key
     */
    public void updateScore(HashMap<Client, Integer> scores) {
        // iterate over every player and update score
        for (Map.Entry<Client, Integer> entry : scores.entrySet()) {
            int updatedScore = entry.getValue();
            this.getPlayerById(entry.getKey().getClientId()).setScore(updatedScore);
        }
        notifyOnUpdate();
    }

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Handles the event of a leaving player
     *
     * @param id ID of the leaving player
     */
    public void playerLeft(int id) {
        if (players != null) {
            Player playerRemove = null;
            for (Player player : players) {
                if (player.getId() == id) {
                    players.remove(player);
                    return;
                }
            }
            players.remove(playerRemove.getId());
        }
    }

    public int getState() {
        return this.state;
    }

    public void start() {
        if (state == NOT_STARTED) {
            state = IN_PROGRESS;
            notifyOnUpdate();
        }
    }

    public void pause() {
        if (state == IN_PROGRESS) {
            state = PAUSED;
            notifyOnUpdate();
        }
    }

    public void resume() {
        if (state == PAUSED) {
            state = IN_PROGRESS;
            notifyOnUpdate();
        }
    }

    public void end() {
        if (state == IN_PROGRESS || state == PAUSED) {
            state = ENDED;
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPlayersFromClients(List<Client> clients) {
        this.players = new ArrayList<>();
        for (Client client : clients) {
            this.players.add(new Player(client.getClientId(), client.getClientName()));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * sets currently active player as inactive and the player of given client as active
     *
     * @param client client of new active player
     */
    public void setActivePlayer(Client client) {
        if (getActivePlayer() != null) {
            getActivePlayer().setActive(false);
        }

        if (players != null) {
            for (Player player : getPlayers()) {
                if (player.getId() == client.getClientId()) {
                    activePlayer = player;
                    // used for gui changes in hands fragment
                    player.setActive(true);
                }
            }
        }
        notifyOnUpdate();
    }

    public interface GameDataListener {
        public void onGameDataUpdate();
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return this.bag;
    }

    public void listenToGameData(GameDataListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Notifies every listener of an update to the data.
     */
    private void notifyOnUpdate() {
        for (GameDataListener L : listeners) {
            L.onGameDataUpdate();
        }
    }
}
