package com.fy.engineserver.util;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 对象集合
 * 
 * 
 * @param <T>
 */

@SimpleEmbeddable
public class ObjectCollection<T> {
	/** 对象集合 */
	private T[] collection;

	/** 空位置数量 */
	private int emptyCount;

	public ObjectCollection(Class<?> clazz, int size) {
		this(clazz, size, null);
	}

	@SuppressWarnings("unchecked")
	public ObjectCollection(Class<?> clazz, int size, T defaultValue) {
		collection = (T[]) Array.newInstance(clazz, size);
		emptyCount = collection.length;
		for (int i = 0; i < collection.length; i++) {
			collection[i] = defaultValue;
		}
	}

	public T[] getCollection() {
		return collection;
	}

	public void put(int index, T t) {
		if (index < 0 || index > collection.length) throw new ArrayIndexOutOfBoundsException(index);
		T old = collection[index];
		if (old == null && t != null) {
			emptyCount--;
		} else if (old != null && t == null) {
			emptyCount++;
		}
		collection[index] = t;
	}

	public T get(int index) {
		if (index < 0 || index > collection.length) throw new ArrayIndexOutOfBoundsException(index);
		return collection[index];
	}

	public T remove(int index) {
		if (index < 0 || index > collection.length) throw new ArrayIndexOutOfBoundsException(index);
		T value = collection[index];
		if (value != null) {
			emptyCount++;
		}
		return value;
	}

	/**
	 * return first index
	 * @param t
	 * @return
	 */
	public int getFirst(T t) {
		for (int i = 0; i < collection.length; i++) {
			T temp = collection[i];
			if (t.equals(temp)) {
				return i;
			}
		}
		return -1;
	}

	public int getLast(T t) {
		for (int i = collection.length - 1; i >= 0; i--) {
			T temp = collection[i];
			if (t.equals(temp)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * return total num
	 * @param t
	 * @return
	 */
	public int get(T t) {
		for (int i = 0; i < collection.length; i++) {
			T temp = collection[i];
			if (t.equals(temp)) {
				collection[i] = null;
				return i;
			}
		}
		return -1;
	}

	public int size() {
		return collection.length;
	}

	public int getEmptyCount() {
		return emptyCount;
	}

	public boolean isFull() {
		return emptyCount <= 0;
	}

	//
	// /**
	// * 扩容
	// * @param num
	// */
	// public void sizeUp(int num) {
	//
	// }


	@Override
	public String toString() {
		return "ObjectCollection [collection=" + Arrays.toString(collection) + ", emptyCount=" + emptyCount + "]";
	}
}
