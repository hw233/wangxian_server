package com.xuanzhi.tools.cache.diskcache.concrete;

import java.io.File;

import com.xuanzhi.tools.cache.diskcache.DiskCache;

public abstract class AbstractDiskCache implements DiskCache{

	abstract void reportBadBlock(DataBlock db,int type);
	
	abstract void addCurrentMemorySize(int size);
	
	public abstract String getName();
	
	public abstract void destory();
	
	public abstract File getDataFile();
	
	
	public abstract int getFreeBlockNum();
	public abstract int getDataBlockNum();
	public abstract DataBlock[] getDataBlocks();
}
