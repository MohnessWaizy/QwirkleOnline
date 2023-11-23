package clock;

public interface Clockable {

	/**
	 * Is called when visualization time elapsed.
	 */
	public void visualisationTimeElapsed();

	/**
	 * Is called when turn time elapsed.
	 */
	public void turnTimeElapsed();

}
