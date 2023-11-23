package de.upb.swtpra1819interface.models;




public class Tile
{
  private int color;
  

  private int shape;
  

  private int uniqueId;
  


  public Tile(int color, int shape, int uniqueId)
  {
    this.color = color;
    this.shape = shape;
    this.uniqueId = uniqueId;
  }
  
  public int getColor() {
    return color;
  }
  
  public int getShape() {
    return shape;
  }
  
  public int getUniqueId() {
    return uniqueId;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tile)) {
      return false;
    }
    Tile tile = (Tile)o;
    return (getColor() == tile.getColor()) && 
      (getShape() == tile.getShape()) && 
      (getUniqueId() == tile.getUniqueId());
  }
}
