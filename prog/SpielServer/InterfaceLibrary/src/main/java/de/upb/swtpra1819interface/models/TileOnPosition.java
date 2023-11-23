package de.upb.swtpra1819interface.models;

import java.util.Objects;










public class TileOnPosition
{
  private int coordX;
  private int coordY;
  private Tile tile;
  
  public TileOnPosition(int coordX, int coordY, Tile tile)
  {
    this.coordX = coordX;
    this.coordY = coordY;
    this.tile = tile;
  }
  
  public int getCoordX() {
    return coordX;
  }
  
  public int getCoordY() {
    return coordY;
  }
  
  public Tile getTile() {
    return tile;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TileOnPosition)) {
      return false;
    }
    TileOnPosition that = (TileOnPosition)o;
    return (getCoordX() == that.getCoordX()) && 
      (getCoordY() == that.getCoordY()) && 
      (Objects.equals(getTile(), that.getTile()));
  }
}
