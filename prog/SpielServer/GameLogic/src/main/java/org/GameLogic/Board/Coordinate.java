package org.GameLogic.Board;

/*
 * This class is immutable
 */

public class Coordinate {
	/**
	 * x Coordinate
	 */
	private int xCoord;
	/**
	 * y Coordinate
	 */
	private int yCoord;

	/**
	 * Constructor
	 * 
	 * @param xCoord
	 * @param yCoord
	 */
	public Coordinate(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	/**
	 * Static constructor
	 * 
	 * @param x
	 * @param y
	 * @return Instance of a coordinate
	 */
	public static Coordinate of(int x, int y) {
		return new Coordinate(x, y);
	}

	/*
	 * Manipulate coordinates
	 */

	/**
	 * 
	 * @return Right coordinate in relation to this instance
	 */
	public Coordinate right() {
		return of(xCoord + 1, yCoord);
	}

	/**
	 * 
	 * @return Left coordinate in relation to this instance
	 */
	public Coordinate left() {
		return of(xCoord - 1, yCoord);
	}

	/**
	 * 
	 * @return Upper coordinate in relation to this instance
	 */
	public Coordinate up() {
		return of(xCoord, yCoord + 1);
	}

	/**
	 * 
	 * @return Lower coordinate in relation to this instance
	 */
	public Coordinate down() {
		return of(xCoord, yCoord - 1);
	}

	/*
	 * Getters
	 */

	public int getX() {
		return xCoord;
	}

	public int getY() {
		return yCoord;
	}

	@Override
	public int hashCode() {

		/**
		 * Create better HashCode -> using one int to store x and y
		 */

		final int prime = 31;
		int result = 1;
		result = prime * result + xCoord;
		result = prime * result + yCoord;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return yCoord == other.getY() && xCoord == other.getX();
	}

	@Override
	public String toString() {
		return "Coordinate [xCoord=" + xCoord + ", yCoord=" + yCoord + "]";
	}

}
