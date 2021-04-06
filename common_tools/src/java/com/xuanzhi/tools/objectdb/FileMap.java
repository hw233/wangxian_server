package com.xuanzhi.tools.objectdb;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.Index;
import javax.jdo.annotations.Unique;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.entityfs.support.log.LogAdapterHolder;
import org.entityfs.support.log.StdOutLogAdapter;
import org.entityfs.util.io.ReadWritableFileAdapter;
import org.helidb.Database;
import org.helidb.impl.simple.SimpleDatabase;
import org.helidb.lang.serializer.SerializableSerializer;
import org.helidb.lang.serializer.StringSerializer;
import org.helidb.backend.cache.lru.LruCacheBackend;
import org.helidb.backend.heap.HeapBackend;
import org.helidb.backend.heap.HeapBackendBuilder;

import com.xuanzhi.tools.cache.diskcache.concrete.ByteArrayUtils;

/**
 * 持久化Map
 * 
 */
public class FileMap implements Map {
	
	private String mapFile;
	
	private String name;
	
	private Database<Object, Object> db;
	
    /**
     * 创建一个FileMap的实例，如果文件存在，则从原来的文件读入此map数据
     * @param name	 此map的名称，
     * @param mapFile
     */
	public FileMap(String name, String mapFile) {
		this.name = name;
		this.mapFile = mapFile;
		init();
	}
	
	public void init() {
		LogAdapterHolder lah = 
			  new LogAdapterHolder(
			    new StdOutLogAdapter());

		HeapBackendBuilder<Object, Object> hb = new HeapBackendBuilder<Object, Object>();
		hb.setKeySerializer(new SerializableSerializer());
	    hb.setValueSerializer(new SerializableSerializer());
	    HeapBackend<Object, Object> backend = hb.create(new ReadWritableFileAdapter(new File(mapFile)));
	    
	    LruCacheBackend<Object, Object, Long> lcb = new LruCacheBackend<Object, Object, Long>(backend, false, 64*1024, 64*1024);
	    
		db = new SimpleDatabase<Object, Object, Long>(lcb, lah);
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return db.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return db.containsValue(value);
	}

	public Set entrySet() {
		// TODO Auto-generated method stub
		return db.entrySet();
	}

	public Object get(Object key) {
		// TODO Auto-generated method stub
		return db.get(key);
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return db.isEmpty();
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		return db.keySet();
	}

	public synchronized Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		return db.put(key, value);
	}

	public synchronized void putAll(Map m) {
		// TODO Auto-generated method stub
		db.putAll(m);
	}

	public synchronized Object remove(Object key) {
		// TODO Auto-generated method stub
		return db.remove(key);
	}

	public int size() {
		// TODO Auto-generated method stub
		return db.size();
	}

	public Collection values() {
		// TODO Auto-generated method stub
		return db.values();
	}

	public synchronized void clear() {
		// TODO Auto-generated method stub
		db.clear();
	}

	public String getMapFile() {
		return mapFile;
	}

	public void setMapFile(String mapFile) {
		this.mapFile = mapFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public static void main(String args[]) {
		FileMap map = new FileMap("FileMap-1", "d:/map7.db");
		long time1 = System.currentTimeMillis();
		int num = 5000;
		for(int i=0; i<num; i++) {
			map.put(new String("key1-" + i),new String("value11-" + i));
		}
		long tt = System.currentTimeMillis()-time1;
		System.out.println("[put "+num+" values to map] ["+map.size()+"] ["+tt+"] ["+(System.currentTimeMillis()-time1)+"ms]");
		
//		FileMap map = new FileMap("PersistMap-1", "d:/map.odb");
//		long time1 = System.currentTimeMillis();
//		List<String> keys = map.listKeys();
//		for(int i=0; i<keys.size() && i<101; i++) {
//			System.out.println("["+keys.get(i)+"] ["+(String)map.get(keys.get(i))+"]");
//			//map.remove(keys.get(i));
//		}
//		System.out.println("[read all "+map.size()+ " values] ["+(System.currentTimeMillis()-time1)+"ms]");
	}
}
