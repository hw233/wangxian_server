package com.fy.engineserver.jiazu;

public class Pair<T, V> {
	private T first;
	private V second;

	public Pair(T a, V b) {
		this.first = a;
		this.second = b;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T obj) {
		this.first = obj;
	}

	public V getSecond() {
		return this.second;
	}

	public void setSecond(V oper) {
		this.second = oper;
	}
}
