package com.xuanzhi.tools.text;

/**
 * 
 */
public class ObjectScale {
	public static long sizeOf(Class clazz) {
		long size = 0;
		Object[] objects = new Object[100];
		try {
			Object primer = clazz.newInstance();
			long startingMemoryUse = getUsedMemory();
			for (int i = 0; i < objects.length; i++) {
				objects[i] = clazz.newInstance();
			}
			long endingMemoryUse = getUsedMemory();
			float approxSize = (endingMemoryUse - startingMemoryUse) / 100f;
			size = Math.round(approxSize);
		} catch (Exception e) {
			System.out.println("WARNING:couldn't instantiate" + clazz);
			e.printStackTrace();
		}
		return size;
	}

	private static long getUsedMemory() {
		gc();
		long totalMemory = Runtime.getRuntime().totalMemory();
		gc();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long usedMemory = totalMemory - freeMemory;
		return usedMemory;
	}

	private static void gc() {
		try {
			System.gc();
			Thread.currentThread().sleep(100);
			System.runFinalization();
			Thread.currentThread().sleep(100);
			System.gc();
			Thread.currentThread().sleep(100);
			System.runFinalization();
			Thread.currentThread().sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Class clazz = Class.forName(args[0]);
			System.out.println(sizeOf(clazz));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
