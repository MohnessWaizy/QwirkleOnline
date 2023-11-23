package de.upb.cs.swtpra_04.qwirkle.model.lobby;

import de.upb.cs.swtpra_04.qwirkle.model.game.Player;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used to represent a game within the lobby.
 * It holds data provided by the server to use with the RecyclerView.
 */
public class LobbyGame {

    private int gameId;
    private String name;
    private int maxPlayerCount;
    private int curPlayerCount;
    private Configuration gameConfig;
    private CurrentGameState state;
    private List<Player> players;
    private String type;

    /**
     * Constructor
     *
     * @param id             id of the game
     * @param name           name of the game
     * @param curPlayerCount how many players are in the game right now
     * @param maxPlayerCount how many players are allowed in this game
     * @param state          what state the game currently is in (e.g. running, ended, ...)
     * @param config         configuration of the game
     */
    public LobbyGame(int id, String name, int curPlayerCount, int maxPlayerCount, GameState state, Configuration config) {
        this.gameId = id;
        this.name = name;
        this.maxPlayerCount = maxPlayerCount;
        this.curPlayerCount = curPlayerCount;
        this.type = "";
        this.players = new ArrayList<>();
        this.gameConfig = config;

        switch (state) {
            case NOT_STARTED:
                this.state = CurrentGameState.NOT_STARTED;
                break;
            case IN_PROGRESS:
                this.state = CurrentGameState.RUNNING;
                break;
            case PAUSED:
                this.state = CurrentGameState.PAUSED;
                break;
            case ENDED:
                this.state = CurrentGameState.FINISHED;
                break;
        }
    }

    /**
     * @return configuration of the game
     */
    public Configuration getGameConfig() {
        return gameConfig;
    }

    /**
     * @param gameConfig
     */
    public void setGameConfig(Configuration gameConfig) {
        this.gameConfig = gameConfig;
    }

    /**
     * Constructor
     *
     * @param game Game object from interface class from which all information are extracted
     */
    public LobbyGame(Game game) {
        this.name = game.getGameName();
        this.maxPlayerCount = game.getConfig().getMaxPlayerNumber();
        this.curPlayerCount = game.getPlayers().size();
        this.gameId = game.getGameId();
        this.gameConfig = game.getConfig();

        if (game.isTournament()) {
            this.type = "Tournament";
        } else {
            this.type = "Normal";
        }

        switch (game.getGameState()) {
            case NOT_STARTED:
                this.state = CurrentGameState.NOT_STARTED;
                break;
            case IN_PROGRESS:
            case PAUSED:
                this.state = CurrentGameState.RUNNING;
                break;
            case ENDED:
                this.state = CurrentGameState.FINISHED;
        }

        this.players = new ArrayList<>();
        for (Client client : game.getPlayers()) {
            players.add(new Player(client.getClientId(), client.getClientName()));
        }
    }

    /**
     * @return id of the game
     */
    public int getId() {
        return gameId;
    }

    /**
     * @return name of game
     */
    public String getName() {
        return name;
    }

    /**
     * @return maximum players in the game lobby
     */
    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    /**
     * @return current number of players in the game lobby
     */
    public int getCurPlayerCount() {
        return curPlayerCount;
    }

    /**
     * sets current number of players in the game lobby
     *
     * @param CurPlayerCount
     */
    public void setCurPlayerCount(byte CurPlayerCount) {
        this.curPlayerCount = CurPlayerCount;
    }

    /**
     * @return current game state
     */
    public CurrentGameState getState() {
        return state;
    }

    /**
     * sets current game state
     *
     * @param state
     */
    public void setState(CurrentGameState state) {
        this.state = state;
    }

    /**
     * @return game type
     */
    public String getType() {
        return type;
    }

    /**
     * sets player list
     *
     * @param players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * @return list of players
     */
    public List<Player> getPlayers() {
        return this.players;
    }

}