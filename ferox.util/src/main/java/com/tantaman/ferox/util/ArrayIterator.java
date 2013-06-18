package com.tantaman.ferox.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
	private final T [] array;
	private int cursor = 0;
	
	public ArrayIterator(T [] array) {
		this(array, 0);
	}
	
	public ArrayIterator(T [] array, int start) {
		this.array = array;
		cursor = start;
	}
	
	@Override
	public boolean hasNext() {
		return cursor < array.length;
	}

	@Override
	public T next() {
		return array[cursor++];
	}
	
	@Override
	public ArrayIterator<T> clone() {
		return new ArrayIterator<>(array, cursor);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
