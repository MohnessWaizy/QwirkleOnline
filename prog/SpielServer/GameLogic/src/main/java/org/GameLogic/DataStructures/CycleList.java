package org.GameLogic.DataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list that has the ability to cycle through players indefinitely. Implements
 * List, because we want to use streams and comparable's.
 */

public class CycleList<E> implements List<E> {

	// current element
	private int current;
	// data backed by arrayList
	private ArrayList<E> data;

	/**
	 * Constructs a new CycleList. The current Element is the first Element added.
	 * 
	 * @param capacity
	 *            - Number of elements in this List
	 */
	public CycleList(int capacity) {
		current = 0;

		if (capacity == 0) {
			capacity = 12;
		}

		data = new ArrayList<E>(capacity);
	}

	/**
	 * 
	 * @return Current Element
	 */
	public E current() {
		if (data.size() > 0) {
			return data.get(current);
		} else {
			return null;
		}
	}

	/**
	 * Calculates the next element and sets the current to the next element. Also
	 * checks if the last element is the current and if so, starts at the beginning
	 * again.
	 * 
	 * @return Next Element
	 */
	public E next() {
		if (data.size() > 0) {
			current = (current + 1) % (data.size());
			return data.get(current);
		} else {
			return null;
		}

	}

	/**
	 * Only peeks the next element instead of cycling.
	 * 
	 * @return Next Element
	 */
	public E peekNext() {
		if (data.size() > 0) {
			return data.get((current + 1) % (data.size()));
		} else {
			return null;
		}

	}

	@Override
	public boolean remove(Object o) {
		int index = data.indexOf(o);

		if (index == -1) {
			return false;
		} else {
			this.remove(index);
			return true;
		}

	}

	@Override
	public E remove(int index) {

		if (index >= this.size()) {
			return null;
		}

		E element = data.remove(index);

		if (this.size() == 0) {
			return element;
		}

		if (current == index) {
			if (index == 0) {
				current = this.size() - 1;
			} else {
				current -= 1;
			}
		} else if (current > index) {
			current -= 1;
		}

		/*
		 * When the current player is removed we also go one back, so that next method
		 * does not skip the player after the removed player
		 */

		return element;
	}

	/**
	 * Removes the current element.
	 * 
	 * @return Current element
	 */
	public E removeCurrent() {
		return remove(current);
	}

	@Override
	public boolean add(E e) {
		if (data.size() == 0) {
			current = 0;
		}
		return data.add(e);
	}

	@Override
	public void add(int index, E element) {
		data.add(index, element);
		if (current >= index) {
			current += 1;
		}
	}

	/**
	 * Calling this method does not preserve the current cycle
	 * 
	 * @see java.util.List#addAll(java.util.Collection)
	 */

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return data.addAll(c);
	}

	/**
	 * Calling this method does not preserve the current cycle
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return data.addAll(index, c);
	}

	public void clear() {
		data.clear();
		current = 0;
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public E get(int index) {
		return data.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return data.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return data.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return data.listIterator(index);
	}

	/*
	 * Calling this method does not preserve the current cycle
	 * 
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return data.remove(c);
	}

	/*
	 * Calling this method does not preserve the current cycle
	 * 
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return data.retainAll(c);
	}

	/*
	 * Calling this method does not preserve the current cycle
	 * 
	 * @see java.util.List#set(int, java.lang.Object)
	 */

	@Override
	public E set(int index, E element) {
		return data.set(index, element);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	public ArrayList<E> getData() {
		return this.data;
	}
}
