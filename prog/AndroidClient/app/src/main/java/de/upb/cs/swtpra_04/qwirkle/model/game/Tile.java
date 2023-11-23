package de.upb.cs.swtpra_04.qwirkle.model.game;

/**
 * Base Class for Tiles which holds universal information
 */
public class Tile {

    private int shape;
    private int color;
    private int id;

    /**
     * Constructor
     *
     * @param shape Shape of the Tile
     * @param color Color of the Tile
     */
    public Tile(int shape, int color) {
        this.shape = shape;
        this.color = color;
    }

    /**
     * Constructor
     *
     * @param interfaceTile Tile object of interface Type
     */
    public Tile(de.upb.swtpra1819interface.models.Tile interfaceTile) {
        this.shape = interfaceTile.getShape();
        this.color = interfaceTile.getColor();
        this.id = interfaceTile.getUniqueId();
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Helper method to construct Tile object of interface type
     *
     * @return
     */
    public de.upb.swtpra1819interface.models.Tile toInterfaceTile() {
        return new de.upb.swtpra1819interface.models.Tile(
                this.getColor(), this.getShape(), this.getId());
    }
}
