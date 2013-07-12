package com.tantaman.ferox.util;

import java.io.Serializable;

public class Pair<FIRST_T, SECOND_T> implements IPair<FIRST_T, SECOND_T>, Serializable {
	private static final long serialVersionUID = 1L;
	private final FIRST_T first;
	private final SECOND_T second;
	
	public Pair(FIRST_T first, SECOND_T second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public FIRST_T getFirst() {
		return first;
	}

	@Override
	public SECOND_T getSecond() {
		return second;
	}

}
