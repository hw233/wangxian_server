package com.xuanzhi.tools.simplejpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 场景服与逻辑服之间数据交换接口
 * 场景服通知逻辑服数据发生变化要求逻辑服保存，更新数据
 * 场景服从逻辑服获得数据如一个对象，一个id，一些所需数据
 * 这个接口用在场景服中，生成场景服的EntityManager时需要给EntityManager中的nssem赋值
 * 当调用场景服的某些对象的set方法时，会定期调用em的保存更新方法，会自动调用这个接口实现类的方法
 * @author Administrator
 *
 * @param <T>
 */
public interface S2SEntityOption<T> {
	
	boolean isClient();

	boolean update_object(String managerKey, T t,String fields[]) throws Exception;
	
	boolean insert_object(String managerKey, T t) throws Exception;
	
	/**
	 * 实际使用时
	 * map实际数据为map.put(id, new Object[]{reference,modifyFields});
	 * id实际是对象id，对象数组第一位为对象的引用，第二位为对象改变的属性 modifyFields为ArrayList<String>
	 * @param managerKey
	 * @param map
	 * @return
	 * @throws Exception
	 */
	boolean batch_insert_or_update_object(String managerKey, Map<Long,Object[]> map) throws Exception;
	

	public long count(String managerKey) throws Exception;

	public long count(String managerKey, Class<?> cl, String whereSql) throws Exception;

	public long count(String managerKey, Class<?> cl, String preparedWhereSql, Object[] paramValues)
			throws Exception;

//	public void destroy(String managerKey) ;

	public T find(String managerKey, long id) throws Exception;

//	public void flush(String managerKey, T t) throws Exception;

	public List<T> query(String managerKey, Class<?> cl, String whereSql, String orderSql,
			long start, long end) throws Exception;
	
	public List<T> query(String managerKey, Class<?> cl, String preparedWhereSql,
			Object[] paramValues, String orderSql, long start, long end)
			throws Exception;
	
	public <S> List<S> queryFields(String managerKey, Class<S> cl, long[] ids) throws Exception;
	
	public long[] queryIds(String managerKey, Class<?> cl, String whereSql) throws Exception;
	
	public long[] queryIds(String managerKey, Class<?> cl, String preparedWhereSql,
			Object[] paramValues) throws Exception;
	
	public long[] queryIds(String managerKey, Class<?> cl, String whereSql, String orderSql,
			long start, long end) throws Exception;
	
	public long[] queryIds(String managerKey, Class<?> cl, String preparedWhereSql,
			Object[] paramValues, String orderSql, long start, long end)
			throws Exception;
	
	public boolean remove(String managerKey, T t) throws Exception;
//	public boolean isAutoSaveEnabled(String managerKey);

//	public long nextId(String managerKey) throws Exception;
}
