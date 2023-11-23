package org.GameLogic.Board;

import java.util.Objects;

/*
 * This class is immutable
 */

public class Coordinate {

	private int xCoord;
	private int yCoord;

	public Coordinate(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public static Coordinate of(int x, int y) {
		return new Coordinate(x, y);
	}

	/*
	 * Manipulate coordinates
	 */

	public Coordinate right() {
		return of(xCoord + 1, yCoord);
	}

	public Coordinate left() {
		return of(xCoord - 1, yCoord);
	}

	public Coordinate up() {
		return of(xCoord, yCoord + 1);
	}

	public Coordinate down() {
		return of(xCoord, yCoord - 1);
	}

	public Coordinate next(char dir) {
		switch (dir) {
		case 'r':
			return right();
		case 'l':
			return left();
		case 'u':
			return up();
		case 'd':
			return down();
		default:
			return null;
		}
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
