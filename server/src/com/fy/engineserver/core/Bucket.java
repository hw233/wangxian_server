package com.fy.engineserver.core;

public class Bucket {
	// 阵列中此桶的下标
	BucketMatrix bm;
	int x, y;

	// 桶中的数据
	LivingObject objects[];
	int size;

	public Bucket(BucketMatrix bm, int x, int y) {
		this.bm = bm;
		this.x = x;
		this.y = y;

		objects = null;
		size = 0;
	}

	/**
	 * 注意，此方法返回的是整个桶中的数据数组， 对此数组必须小心，此数组的有效长度为 size ，而不是此数组的length
	 * 
	 * @return
	 */
	public LivingObject[] getLivingObjects() {
		return objects;
	}

	public void add(LivingObject o) {
		
		if(objects == null){
			objects = new LivingObject[4];
		}
		
		if (size < objects.length) {
			objects[size] = o;
			size++;
		} else {
			LivingObject tmpObjects[] = new LivingObject[objects.length * 2];
			System.arraycopy(objects, 0, tmpObjects, 0, objects.length);
			objects = tmpObjects;
			objects[size] = o;
			size++;
		}
	}

	public void clear() {
		if(objects == null) return;
		for(int i = 0; i < size; i++) {
			objects[i] = null;
		}
		size = 0;
	}

	public int size() {
		return size;
	}
}
