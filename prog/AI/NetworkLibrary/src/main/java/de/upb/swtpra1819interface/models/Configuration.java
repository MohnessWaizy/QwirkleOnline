package de.upb.swtpra1819interface.models;



public class Configuration
{
  private int colorShapeCount;
  

  private int tileCount;
  

  private int maxHandTiles;
  

  private long turnTime;
  

  private long timeVisualization;
  

  private WrongMove wrongMove;
  

  private int wrongMovePenalty;
  

  private SlowMove slowMove;
  

  private int slowMovePenalty;
  
  private int maxPlayerNumber;
  

  public Configuration(int colorShapeCount, int tileCount, int maxHandTiles, long turnTime, long timeVisualization, WrongMove wrongMove, int wrongMovePenalty, SlowMove slowMove, int slowMovePenalty, int maxPlayerNumber)
  {
    this.colorShapeCount = colorShapeCount;
    this.tileCount = tileCount;
    this.maxHandTiles = maxHandTiles;
    this.turnTime = turnTime;
    this.timeVisualization = timeVisualization;
    this.wrongMove = wrongMove;
    this.wrongMovePenalty = wrongMovePenalty;
    this.slowMove = slowMove;
    this.slowMovePenalty = slowMovePenalty;
    this.maxPlayerNumber = maxPlayerNumber;
  }
  
  public int getColorShapeCount() {
    return colorShapeCount;
  }
  
  public int getTileCount() {
    return tileCount;
  }
  
  public int getMaxHandTiles() {
    return maxHandTiles;
  }
  
  public long getTurnTime() {
    return turnTime;
  }
  
  public long getTimeVisualization() {
    return timeVisualization;
  }
  
  public WrongMove getWrongMove() {
    return wrongMove;
  }
  
  public int getWrongMovePenalty() {
    return wrongMovePenalty;
  }
  
  public SlowMove getSlowMove() {
    return slowMove;
  }
  
  public int getSlowMovePenalty() {
    return slowMovePenalty;
  }
  
  public int getMaxPlayerNumber() {
    return maxPlayerNumber;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Configuration)) {
      return false;
    }
    Configuration that = (Configuration)o;
    return (getColorShapeCount() == that.getColorShapeCount()) && 
      (getTileCount() == that.getTileCount()) && 
      (getMaxHandTiles() == that.getMaxHandTiles()) && 
      (getTurnTime() == that.getTurnTime()) && 
      (getTimeVisualization() == that.getTimeVisualization()) && 
      (getWrongMovePenalty() == that.getWrongMovePenalty()) && 
      (getSlowMovePenalty() == that.getSlowMovePenalty()) && 
      (getMaxPlayerNumber() == that.getMaxPlayerNumber()) && 
      (getWrongMove() == that.getWrongMove()) && 
      (getSlowMove() == that.getSlowMove());
  }
}
