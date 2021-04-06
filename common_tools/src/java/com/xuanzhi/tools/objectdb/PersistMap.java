package com.xuanzhi.tools.objectdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Index;
import javax.jdo.annotations.Unique;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.xuanzhi.tools.cache.diskcache.concrete.ByteArrayUtils;

/**
 * 持久化Map
 * 
 */
public class PersistMap {
	
	private String mapFile;
	
	private String name;
	
	private EntityManagerFactory emf = null;
         
    private EntityManager em = null;
	
    /**
     * 创建一个PersistMap的实例，如果文件存在，则从原来的文件读入此map数据
     * @param name	 此map的名称，
     * @param mapFile
     */
	public PersistMap(String name, String mapFile) {
		this.name = name;
		this.mapFile = mapFile;
		init();
	}
	
	public void init() {
		this.emf = Persistence.createEntityManagerFactory(mapFile);
		this.em = emf.createEntityManager();
	}
	
	/**
	 * 删除这个map中的所有元素
	 */
	public void clear() {
		// TODO Auto-generated method stub
		em.clear();
	}

	/**
	 * 是否包含有这个key的记录
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		// TODO Auto-generated method stub
		try {
			TypedQuery<KeyValue> query = em.createQuery("SELECT kv FROM KeyValue kv WHERE kv.name=:name", KeyValue.class);
			KeyValue result = query.setParameter("name", key).getSingleResult();
			if(result != null) {
				return true;
			}
		} catch(Exception e) {
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
			try {
				TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k where k.data=:data", KeyValue.class);
				KeyValue result = query.setParameter("data", data).getSingleResult();
				if(result != null) {
					return true;
				}
			} catch(Exception e) {
				//
			}
		}
		return false;
	}

	/**
	 * 获得所有value
	 * @return
	 */
	public List<Object> listValues() {
		// TODO Auto-generated method stub
		TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k", KeyValue.class);
		List<Object> list = new ArrayList<Object>();
		List<KeyValue> resultList = query.getResultList();
		if(resultList != null) {
			for(int i=0; i<resultList.size(); i++) {
				byte[] data = resultList.get(i).getData();
				try {
					Object obj = ByteArrayUtils.byteArrayToObject(data, 0, data.length);
					list.add(obj);
				} catch(Exception e) {
					e.printStackTrace();
				}
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
		try {
			TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k where k.name=:name", KeyValue.class);
			KeyValue result = query.setParameter("name", key).getSingleResult();
			if(result != null) {
				byte[] data = result.getData();
				try {
					Object obj = ByteArrayUtils.byteArrayToObject(data, 0, data.length);
					return obj;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
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
		try {
			TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k", KeyValue.class);
			List<KeyValue> resultList = query.getResultList();
			if(resultList != null) {
				for(int i=0; i<resultList.size(); i++) {
					list.add(resultList.get(i).getName());
				}
			}
		} catch(Exception e) {
			//
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
		KeyValue kv = new KeyValue();
		kv.setName(key);
		byte data[] = null;
		try {
			data =ByteArrayUtils.objectToByteArray(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kv.setData(data);
		if(!containsKey(key)) {
			em.getTransaction().begin();
			em.persist(kv);
			em.getTransaction().commit();
			return value;
		} else {
			update(kv);
			return value;
		}
	}
	
	private void update(KeyValue kv) {
		try {
			TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k where k.name=:name", KeyValue.class);
			KeyValue result = query.setParameter("name", kv.getName()).getSingleResult();
			if(result != null) {
				result.setData(kv.getData());
				em.getTransaction().begin();
				em.persist(result);
				em.getTransaction().commit();
			}
		} catch(Exception e) {
			e.printStackTrace();
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
			TypedQuery<KeyValue> query = em.createQuery("select k from KeyValue k where k.name=:name", KeyValue.class);
			KeyValue result = query.setParameter("name", key).getSingleResult();
			if(result != null) {
				byte[] data = result.getData();
				try {
					Object obj = ByteArrayUtils.byteArrayToObject(data, 0, data.length);
					em.getTransaction().begin();
					em.remove(result);
					em.getTransaction().commit();
					return obj;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			//
		}
		return null;
	}

	/**
	 * 获得map元素的数量
	 * @return
	 */
	public long size() {
		// TODO Auto-generated method stub
		Query q = em.createQuery("select count(k) from KeyValue k");
		return (Long)q.getSingleResult();
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
	
	@Entity
	public static class KeyValue {
		
		@Unique
		public String name;
		
		@Index
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
		PersistMap map = new PersistMap("PersistMap-1", "d:/map2.odb");
		long time1 = System.currentTimeMillis();
		int num = 1000;
		for(int i=0; i<num; i++) {
			map.put(new String("key-" + i),new String("value1-" + i));
		}
		System.out.println("[put "+num+" values to map] ["+map.size()+"] ["+(System.currentTimeMillis()-time1)+"ms]");
		
//		PersistMap map = new PersistMap("PersistMap-1", "d:/map.odb");
//		long time1 = System.currentTimeMillis();
//		List<String> keys = map.listKeys();
//		for(int i=0; i<keys.size() && i<101; i++) {
//			System.out.println("["+keys.get(i)+"] ["+(String)map.get(keys.get(i))+"]");
//			//map.remove(keys.get(i));
//		}
//		System.out.println("[read all "+map.size()+ " values] ["+(System.currentTimeMillis()-time1)+"ms]");
	}
}
