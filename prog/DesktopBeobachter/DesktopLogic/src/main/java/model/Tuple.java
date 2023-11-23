package model;

/**
 *
 * @param <K> first parameter
 * @param <V> second parameter
 */
public class Tuple<K, V> {

	private K first;
	private V second;

	/**
	 * constructor
	 * 
	 * @param kArg first parameter
	 * @param vArg second parameter
	 */
	public Tuple(K kArg, V vArg) {
		first = kArg;
		second = vArg;
	}

	public void setFirst(K first) {
		this.first = first;
	}

	public void setSecond(V second) {
		this.second = second;
	}

	public K getFirst() {
		return first;
	}

	public V getSecond() {
		return second;
	}

}
