package org.ServerGui.Model;

public class Tuple<K, V> {

	private K first;
	private V second;

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
