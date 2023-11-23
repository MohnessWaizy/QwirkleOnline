package de.upb.cs.swtpra_04.qwirkle.model.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data to represent a player
 */
public class Player {

    private long id;
    private String name;
    private List<Tile> tiles;
    private int score;
    private boolean active;

    /**
     * Constructor
     *
     * @param id   id of the Player
     * @param name name of the Player
     */
    public Player(long id, String name) {
        this.id = id;
        this.name = name;
        this.tiles = new ArrayList<>();

        score = 0;
        active = false;
    }

    /**
     * Constructor
     *
     * @param id    id of the Player
     * @param name  name of the Player
     * @param tiles tiles the player currently holds
     */
    public Player(long id, String name, List<Tile> tiles) {
        this.id = id;
        this.name = name;
        this.tiles = tiles;

        score = 0;
        active = false;
    }

    public long getId() {
        return id;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
