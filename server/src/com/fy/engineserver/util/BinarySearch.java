package com.fy.engineserver.util;

import java.util.Arrays;

public class BinarySearch<T extends Comparable<T>> {
	private T[] data;// 要排序的数据

	public BinarySearch(T[] data) {
		this.data = data;
	}

	public int search(T key) {
		int low;
		int high;
		int mid;

		if (data == null) return -1;

		low = 0;
		high = data.length - 1;

		while (low <= high) {
			mid = (low + high) / 2;

			if (key.compareTo(data[mid]) < 0) {
				high = mid - 1;
			} else if (key.compareTo(data[mid]) > 0) {
				low = mid + 1;
			} else if (key.compareTo(data[mid]) == 0) {
				return mid;
			}
		}

		return -1;
	}

	private int doSearchRecursively(int low, int high, T key) {
		int mid;
		int result;

		if (low <= high) {
			mid = (low + high) / 2;
			result = key.compareTo(data[mid]);

			if (result < 0) {
				return doSearchRecursively(low, mid - 1, key);
			} else if (result > 0) {
				return doSearchRecursively(mid + 1, high, key);
			} else if (result == 0) {
				return mid;
			}
		}

		return -1;
	}

	public int searchRecursively(T key) {
		if (data == null) return -1;

		return doSearchRecursively(0, data.length - 1, key);
	}

	public static void main(String[] args) {
		Arrays.sort(new Long[] {});
		Integer[] data = { 1, 4, 5, 8, 15, 33, 48, 77, 96 };
		BinarySearch<Integer> binSearch = new BinarySearch<Integer>(data);

//		System.out.println("Key index:" + binSearch.searchRecursively(33));
	}
}
