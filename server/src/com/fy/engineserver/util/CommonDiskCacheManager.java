package com.fy.engineserver.util;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 一些杂项的配置,存储在diskcache里,不依赖任何其他的类,放在启动的最前面(SystemTime后)
 * 
 * 
 */
public class CommonDiskCacheManager {

	private static CommonDiskCacheManager instance;

	private DiskCache diskCache = null;

	private Map<CommonSubSystem, Object> properties = new Hashtable<CommonSubSystem, Object>();

	private File dataFile;

	public static CommonDiskCacheManager getInstance() {
		return instance;
	}

	public Object getProps(CommonSubSystem commonSubSystem) {
		return properties.get(commonSubSystem);
	}

	public void put(CommonSubSystem commonSubSystem, Object value) {
		properties.put(commonSubSystem, value);
		diskCache.put(commonSubSystem.toString(), (Serializable) value);
	}

	public DiskCache getDiskCache() {
		return diskCache;
	}

	public void setDiskCache(DiskCache diskCache) {
		this.diskCache = diskCache;
	}

	public Map<CommonSubSystem, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<CommonSubSystem, Object> properties) {
		this.properties = properties;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public void init() {
		// diskCache 初始化
		

		long startTime = System.currentTimeMillis();
		diskCache = new DefaultDiskCache(dataFile, null, CommonDiskCacheManager.class.toString(), 100L * 365 * 24 * 3600 * 1000L);
		int num = 0;
		for (CommonSubSystem commonSubSystem : CommonSubSystem.values()) {
			Object obj = diskCache.get(commonSubSystem.toString());
			if (obj != null) {
				properties.put(commonSubSystem, obj);
				num++;
			}
		}
		instance = this;
		System.out.println("[系统初始化] [通用ddc配置] [启动完毕] [配置数量:" + CommonSubSystem.values().length + "] [实际取到值数量:" + num + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
		ServiceStartRecord.startLog(this);
	}
}
