package com.xuanzhi.tools.objectdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.Index;
import javax.jdo.annotations.Unique;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.xuanzhi.tools.cache.diskcache.concrete.ByteArrayUtils;

/**
 * 持久化Map
 * 
 */
public class DBMap implements Runnable {
	
	protected String mapFile;
	
	protected String name;

	protected ObjectContainer  db = null;
	
	protected static int ID = 0;
	
	protected HashMap<String, Serializable> cacheMap = new HashMap<String, Serializable>();
	
	protected List<KeyValue> memobjs = Collections.synchronizedList(new LinkedList<KeyValue>());
	
	protected long loopCheckTime = 100;
	
	protected long maxObjectNum = 10000;
	
	protected boolean checking = false;
	
    /**
     * 创建一个PersistMap的实例，如果文件存在，则从原来的文件读入此map数据
     * @param name	 此map的名称，
     * @param mapFile
     */
	public DBMap(String name, String mapFile) {
		this.name = name;
		this.mapFile = mapFile;
		init();
	}
	
	public void init() {
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(KeyValue.class).objectField("name").indexed(true);
		conf.common().objectClass(KeyValue.class).objectField("data").indexed(true);
		db = Db4oEmbedded.openFile(conf, mapFile);
		Thread t = new Thread(this, "DBMap-" + (ID++));
		t.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				while(memobjs.size() > 0 || checking) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(db != null) {
					db.close();
				}
			}
		});
	}

	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(loopCheckTime);
				checking = true;
				hanleMemData();
				checking = false;
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	private void hanleMemData() {
		//处理memobjs中的keyvalue
		int memsize = memobjs.size();
		for(int i=0; i<memsize; i++) {
			KeyValue kv = memobjs.remove(0);
			cacheMap.put(kv.getName(), kv.getData());
		}
		if(cacheMap.size() > 0) {
			String keys[] = cacheMap.keySet().toArray(new String[0]);
			for(int i=0; i<keys.length; i++) {
				KeyValue kv = new KeyValue();
				kv.setName(keys[i]);
				kv.setData((byte[])cacheMap.get(keys[i]));
				if(!containsKey(kv.getName())) {
					db.store(kv);
				} else {
					Query query = db.query();
			        query.constrain(KeyValue.class);
			        query.descend("name").constrain(kv.getName()).equal();
					ObjectSet result = query.execute();
					if(result.hasNext()) {
						KeyValue kkk = (KeyValue)result.next();
						kkk.setData(kv.getData());
						db.store(kkk);
					}
				}
			}
			db.commit();
			cacheMap.clear();
		}
	}
	
	/**
	 * 删除这个map中的所有元素
	 */
	public void clear() {
		// TODO Auto-generated method stub
		try {
			ObjectSet result = db.queryByExample(KeyValue.class);
			while(result.hasNext()) {
				KeyValue kv = (KeyValue)result.next();
				db.delete(kv);
			}
		} finally {
			db.commit();
		}
	}

	/**
	 * 是否包含有这个key的记录
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		// TODO Auto-generated method stub
		Query query = db.query();
        query.constrain(KeyValue.class);
        query.descend("name").constrain(key).equal();
        ObjectSet result = query.execute();
		if(result.hasNext()) {
			return true;
		}
		return false;
	}

	/**
	 * 是否包含此value，高频率慎用，此方法因为涉及序列化对象，所以效率并不高
	 * @param value
	 * @return
	 */
	public boolean containsValue(Serializable value) {
		// TODO Auto-generated method stub
		byte data[] = null;
		try {
			data = ByteArrayUtils.objectToByteArray(value);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(data != null) {
			Query query = db.query();
	        query.constrain(KeyValue.class);
	        query.descend("name").constrain(data).equal();
			ObjectSet result = query.execute();
			if(result.hasNext()) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 获得所有value
	 * @return
	 */
	public List<Object> listValues() {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<Object>();
		Query query = db.query();
        query.constrain(KeyValue.class);
		ObjectSet result = query.execute();
		while(result.hasNext()) {
			KeyValue kv = (KeyValue)result.next();
			byte[] data = kv.getData();
			try {
				Object obj = ByteArrayUtils.byteArrayToObject(data, 0, data.length);
				list.add(obj);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 获得对应key的value
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		// TODO Auto-generated method stub
		Query query = db.query();
        query.constrain(KeyValue.class);
        query.descend("name").constrain(key).equal();
		ObjectSet result = query.execute();
		if(result.hasNext()) {
			KeyValue kv = (KeyValue)result.next();
			byte[] data = kv.getData();
			try {
				Object obj = ByteArrayUtils.byteArrayToObject(data, 0, data.length);
				return obj;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获得所有key
	 * @return
	 */
	public List<String> listKeys() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		Query query = db.query();
        query.constrain(KeyValue.class);
		ObjectSet result = query.execute();
		while(result.hasNext()) {
			KeyValue kv = (KeyValue)result.next();
			list.add(kv.getName());
		}
		return list;
	}

	/**
	 * 把某对象放入map中
	 * @param key
	 * @param value
	 * @return null if failed store
	 */
	public Serializable put(String key, Serializable value) {
		// TODO Auto-generated method stub
		while(memobjs.size() > this.maxObjectNum) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte data[] = null;
		try {
			data =ByteArrayUtils.objectToByteArray(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KeyValue kv = new KeyValue();
		kv.setName(key);
		kv.setData(data);
		this.memobjs.add(kv);
		return value;
	}
	
	private void update(KeyValue kv) {
		try {
			ObjectSet result = db.queryByExample(kv);
			if(result.hasNext()) {
				KeyValue kkk = (KeyValue)result.next();
				kkk.setData(kv.getData());
				db.store(kkk);
			}
		} finally {
			db.commit();
		}
	}

	/**
	 * 删除某个key
	 * @param key
	 * @return
	 */
	public Object remove(String key) {
		// TODO Auto-generated method stub
		try {
			KeyValue proto = new KeyValue();
			proto.setName(key);
			proto.setData(null);
			ObjectSet result = db.queryByExample(proto);
			if(result.hasNext()) {
				KeyValue kv = (KeyValue)result.next();
				db.delete(kv);
				return kv;
			}
		} finally {
			db.commit();
		}
		return null;
	}

	/**
	 * 获得map元素的数量
	 * @return
	 */
	public long size() {
		// TODO Auto-generated method stub
		ObjectSet result = db.queryByExample(KeyValue.class);
		int num = 0;
		while(result.hasNext()) {
			result.next();
			num++;
		}
		return num;
	}
	
	public ObjectContainer getDatabase() {
		return db;
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
	
	public static class KeyValue {
		
		public String name;
		
		public byte[] data;


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

	}
	
	
	public static void main(String args[]) {
		DBMap map = new DBMap("DBMap-1", "d:/dbmap5.db");
		long time1 = System.currentTimeMillis();
		int num = 100000;
		for(int i=0; i<num; i++) {
			map.put(new String("key1-" + i),new String("value1-" + i));
		}
		long ss = System.currentTimeMillis();
		System.out.println("[put "+num+" values to map] ["+map.size()+"] ["+(ss-time1)+"ms] ["+(System.currentTimeMillis()-time1)+"ms]");
		
//		DBMap map = new DBMap("PersistMap-1", "d:/map.odb");
//		long time1 = System.currentTimeMillis();
//		List<String> keys = map.listKeys();
//		for(int i=0; i<keys.size() && i<101; i++) {
//			System.out.println("["+keys.get(i)+"] ["+(String)map.get(keys.get(i))+"]");
//			//map.remove(keys.get(i));
//		}
//		System.out.println("[read all "+map.size()+ " values] ["+(System.currentTimeMillis()-time1)+"ms]");
		
//		DBMap map = new DBMap("DBMap-1", "d:/dbmap3.db");
//		ObjectContainer db = map.getDatabase();
//		long time1 = System.currentTimeMillis();
//		int num = 100000;
//		for(int i=0; i<num; i++) {
//			Point p = new Point();
//			p.name = "name-" + i;
//			p.x = i;
//			p.y = i+1;
//			db.store(p);
//		}
//		db.commit();
//		long ss = System.currentTimeMillis();
//		System.out.println("[put "+num+" values to map] ["+map.size()+"] ["+(ss-time1)+"ms] ["+(System.currentTimeMillis()-time1)+"ms]");
//		map.close();
	}
	
	public static class Point {
		String name;
		int x;
		int y;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
		
	}


}
