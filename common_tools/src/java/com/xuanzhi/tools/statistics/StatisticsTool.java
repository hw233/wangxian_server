package com.xuanzhi.tools.statistics;

import java.sql.*;
import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.text.StringUtil;

public class StatisticsTool {

	//public static boolean cacheEnabled = true;
	public static long maxCacheLifeTime = 15 * 60 * 1000L;
	public static int maxCacheElementNum = 8192;
	
	public static final String HASHCODE = "CODEHASH";
	
	protected static LruMapCache cache = null;
	
	protected static Logger logger = Logger.getLogger(StatisticsTool.class);
	
	protected static HashMap<DimensionArray,Lock> lockMap = new HashMap<DimensionArray,Lock>();
	
	public static HashMap<DimensionArray,Lock> getLockMap(){
		return (HashMap<DimensionArray,Lock>)lockMap.clone();
	}
	
	public static LruMapCache getCache(){
		return cache;
	}
	
	public static class DimensionArray{
		
		public String tableName;
		public String dimensionStr;
		public DimensionArray(Dimension ds[],String tableName){
			this.dimensionStr = StatisticsTool.toString(ds);
			this.tableName = tableName;
		}
		
		public int hashCode(){
			return dimensionStr.hashCode() + tableName.hashCode();
		}
		
		public boolean equals(Object o){
			if(o == this) return true;
			if(o instanceof DimensionArray){
				DimensionArray d = (DimensionArray)o;
				if(tableName.equals(d.tableName) == false) return false;
				if(dimensionStr.equals(d.dimensionStr) == false) return false;
				return true;
			}
			return false;
		}
	}
	
	public static class Lock {
		public int ref = 0;
		public String owner;
		public StatData data;//第一个等待线程的数据，后续线程在这个线程启动前，可以将数据累加到这个数据对象中，大幅提高效率
	}
	
	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param tableName 表的名称，这个表必须已经存在数据库中
	 * @param ds 各个维度信息，必须包含所有的维度，以及各个维度的粒度必须已经设置好值，维度的属性已经设置好值。，否则将抛出Exception异常
	 * @param data 数据，各个统计项必须已经设置好值
	 * @param accumulate 如果对应的各个维度的统计已经存在，那么true表示累加上去，false表示覆盖。
	 */
	public static void update(Connection conn,String tableName,Dimension []ds,StatData data,boolean accumulate) throws Exception{
		update(conn,tableName,ds,data,accumulate,false,false);
	}
	
	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param tableName 表的名称，这个表必须已经存在数据库中
	 * @param ds 各个维度信息，必须包含所有的维度，以及各个维度的粒度必须已经设置好值，维度的属性已经设置好值。，否则将抛出Exception异常
	 * @param data 数据，各个统计项必须已经设置好值
	 * @param accumulate 如果对应的各个维度的统计已经存在，那么true表示累加上去，false表示覆盖。
	 */
	public static void update(Connection conn,String tableName,Dimension []ds,StatData data,boolean accumulate,boolean cacheEnabled,boolean hashCodeColumnEnabled) throws Exception{
		update(conn,tableName,ds,data,accumulate,cacheEnabled,hashCodeColumnEnabled,false);
	}
	
	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param tableName 表的名称，这个表必须已经存在数据库中
	 * @param ds 各个维度信息，必须包含所有的维度，以及各个维度的粒度必须已经设置好值，维度的属性已经设置好值。，否则将抛出Exception异常
	 * @param data 数据，各个统计项必须已经设置好值
	 * @param accumulate 如果对应的各个维度的统计已经存在，那么true表示累加上去，false表示覆盖。
	 */
	public static void update(Connection conn,String tableName,Dimension []ds,StatData data,boolean accumulate,boolean cacheEnabled,boolean hashCodeColumnEnabled,boolean usingOracle) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i].isReadyForUpdateData() == false){
				try{
					conn.close();
				}catch(Exception e){}
				throw new Exception("Dimension ["+ds[i]+"] hasn't ready for update data");
			}
		}
		Lock lock = null;
		DimensionArray da = new DimensionArray(ds,tableName);
		synchronized(lockMap){
			lock = lockMap.get(da);
			if(lock == null){
				lock = new Lock();
				lockMap.put(da,lock);
			}
			
			//提高效率
			if(accumulate){
				if(lock.data != null){//有线程在执行,还有一个线程在等待,lock.data为等待执行线程的数据
					synchronized(lock.data){
						if(lock.data != null){
							lock.data.add(data);
							if(logger.isDebugEnabled()){
								logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [accumulate] [succ] ["+lock.data+"] ["+data+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
							}
							try{
								conn.close();
							}catch(Exception e){}
							return;
						}
					}
				}
				if(lock.ref == 1){//有线程在执行
					lock.data = data;
					lock.ref++;
				}else{
					lock.ref++;
				}
			}else{
				lock.ref ++;
			}
		}
		
		synchronized(lock){
			lock.owner = Thread.currentThread().getName();
			if(lock.data == data){
				synchronized(lock.data){
					lock.data = null;
				}
			}
			if(cacheEnabled){
				if(cache == null){
					synchronized(StatisticsTool.class){
						if(cache == null){
							cache = new LruMapCache(maxCacheElementNum,maxCacheLifeTime,false,"StatData-Cache");
						}
					}
				}
			}
			try{
				StatData fds = null;
				if(cacheEnabled && cache != null){
					fds = (StatData)cache.get(da);
				}
				String fields[] = data.getAllDataField();
				int parameterIndex = 1;
				PreparedStatement pstmt = null;
				if(fds == null){
					String selectSql = "SELECT autoid,";
					
					for(int i = 0 ; i < fields.length ; i++){
						if(i < fields.length - 1)
							selectSql += fields[i] + ",";
						else
							selectSql += fields[i] + "";
					}
					selectSql += " FROM " + tableName + " WHERE ";
					
					if(hashCodeColumnEnabled){
						selectSql += HASHCODE+"=?";
					}else{
						for(int i = 0 ; i < ds.length ; i++){
							String granulas[] = ds[i].getAllGranula();
							for(int j = 0 ; j < granulas.length ; j++){
								if(i == ds.length - 1 && j == granulas.length - 1)
									selectSql += granulas[j] + "=?";
								else
									selectSql += granulas[j] + "=? and ";
							}
						}
					}
					pstmt = conn.prepareStatement(selectSql);
					
					if(hashCodeColumnEnabled){
						pstmt.setString(parameterIndex,StringUtil.hash(toString(ds)));
						parameterIndex++;
					}else{
						for(int i = 0 ; i < ds.length ; i++){
							String granulas[] = ds[i].getAllGranula();
							for(int j = 0 ; j < granulas.length ; j++){
								String value = ds[i].getValue(granulas[j]);
								pstmt.setString(parameterIndex,value);
								parameterIndex ++;
							}
						}
					}
					ResultSet rs = pstmt.executeQuery();
					
					if(rs.next()){
						fds = data.clone();
						fds.reset();
						
						fds.autoid = rs.getInt("autoid");
						
						for(int i = 0 ; i < fields.length ; i++){
							Number o = fds.getValue(fields[i]);
							Object v = null;
							if(o instanceof Byte){
								v = rs.getByte(fields[i]);
							}else if(o instanceof Short){
								v = rs.getShort(fields[i]);
							}else if(o instanceof Integer){
								v = rs.getInt(fields[i]);
							}else if(o instanceof Long){
								v = rs.getLong(fields[i]);
							}else if(o instanceof Float){
								v = rs.getFloat(fields[i]);
							}else if(o instanceof Double){
								v = rs.getDouble(fields[i]);
							}else{
								v = rs.getObject(fields[i]);
							}
								
							if(v != null){
								if(v instanceof Number){
									fds.setValue(fields[i],(Number)v);
								}else if(v instanceof String){
									Integer intV = Integer.parseInt((String)v);
									fds.setValue(fields[i],intV);
								}else{
									throw new Exception("the data field value ["+v+"] is not a number");
								}
							}
						}
						if(logger.isDebugEnabled()){
							logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [select_exist] ["+fds+"] ["+selectSql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
		
					}else{
					
						if(logger.isDebugEnabled()){
							logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [select_not_exist] ["+selectSql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
					}
					
					rs.close();
					pstmt.close();
				}
				if(fds == null){ // insert
					String sql ="INSERT INTO " + tableName +" (";
					if(usingOracle){
						sql +="autoid,";
					}
					for(int i = 0 ; i < ds.length ; i++){
						String granulas[] = ds[i].getAllGranula();
						for(int j = 0 ; j < granulas.length ; j++){
							sql += granulas[j] + ",";
						}
						String attachs[] = ds[i].getAttachmentNames();
						for(int j = 0 ; j < attachs.length ; j++){
							if(ds[i].getAttachment(attachs[j]) != null)
							sql += attachs[j] + ",";
						}
					}
					for(int i = 0 ; i < fields.length ; i++){
						sql += fields[i] + ",";
					}
					sql = sql.substring(0,sql.length() - 1);
					
					if(hashCodeColumnEnabled){
						sql += ","+HASHCODE;
					}
					
					sql +=") VALUES(";
					
					if(usingOracle){
						sql +="SEQ_"+tableName+".nextval,";
					}
					
					for(int i = 0 ; i < ds.length ; i++){
						String granulas[] = ds[i].getAllGranula();
						for(int j = 0 ; j < granulas.length ; j++){
							sql += "?" + ",";
						}
						String attachs[] = ds[i].getAttachmentNames();
						for(int j = 0 ; j < attachs.length ; j++){
							if(ds[i].getAttachment(attachs[j]) != null)
							sql += "?" + ",";
						}
					}
					for(int i = 0 ; i < fields.length ; i++){
						sql += "?" + ",";
					}
					sql = sql.substring(0,sql.length() - 1);
					
					if(hashCodeColumnEnabled){
						sql += ",?";
					}
					
					sql +=")";
					
					parameterIndex = 1;
					pstmt = conn.prepareStatement(sql);
					
					for(int i = 0 ; i < ds.length ; i++){
						String granulas[] = ds[i].getAllGranula();
						for(int j = 0 ; j < granulas.length ; j++){
							String value = ds[i].getValue(granulas[j]);
							if(value != null && value.length() > ds[i].getColumnLength()){
								value = value.substring(0,ds[i].getColumnLength());
							}
							pstmt.setString(parameterIndex,value);
							parameterIndex++;
						}
						String attachs[] = ds[i].getAttachmentNames();
						for(int j = 0 ; j < attachs.length ; j++){
							if(ds[i].getAttachment(attachs[j]) != null){
								Object value = ds[i].getAttachment(attachs[j]);
								pstmt.setObject(parameterIndex,value);
								parameterIndex++;
							}
						}
					}
					for(int i = 0 ; i < fields.length ; i++){
						Number value = data.getValue(fields[i]);
						pstmt.setObject(parameterIndex,value);
						parameterIndex++;
					}
					
					if(hashCodeColumnEnabled){
						pstmt.setString(parameterIndex,StringUtil.hash(toString(ds)));
						parameterIndex++;
					}
					
					int r = pstmt.executeUpdate();
					
					if(logger.isDebugEnabled()){
						logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [insert] ["+(r>0?"succ":"failed")+"] ["+data+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
					}
					
					//if(cacheEnabled && cache != null){
					//	cache.put(da,data);
					//}
				}else{ //update
					int autoid = fds.autoid;
					if(accumulate){
						fds.add(data);
					}
					else{
						fds = data;
						fds.autoid = autoid;
					}
					
					String sql ="UPDATE " + tableName +" SET ";
					for(int i = 0 ; i < fields.length ; i++){
						if(i == fields.length - 1)
							sql += fields[i]+"=?";
						else
							sql += fields[i]+"=?,";
					}
					sql += " WHERE autoid=?";
					
					/*
					for(int i = 0 ; i < ds.length ; i++){
						String granulas[] = ds[i].getAllGranula();
						for(int j = 0 ; j < granulas.length ; j++){
							if(i == ds.length - 1 && j == granulas.length - 1)
								sql += granulas[j] + "=?";
							else
								sql += granulas[j] + "=? and ";
						}
					}
					*/
					
					parameterIndex = 1;
					pstmt = conn.prepareStatement(sql);
					
					for(int i = 0 ; i < fields.length ; i++){
						Number value = fds.getValue(fields[i]);
						pstmt.setObject(parameterIndex,value);
						parameterIndex++;
					}
					
					pstmt.setInt(parameterIndex,autoid);
					
					/*
					for(int i = 0 ; i < ds.length ; i++){
						String granulas[] = ds[i].getAllGranula();
						for(int j = 0 ; j < granulas.length ; j++){
							String value = ds[i].getValue(granulas[j]);
							pstmt.setString(parameterIndex,value);
							parameterIndex++;
						}
					}
					*/
					
					int r = pstmt.executeUpdate();
					
					if(logger.isDebugEnabled()){
						logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [update] ["+(r>0?"succ":"failed")+"] ["+fds+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
					}
					
					if(cacheEnabled && cache != null){
						cache.put(da,fds);
					}
					
				}
			}catch(SQLException e){
				
				if(logger.isDebugEnabled()){
					logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] ["+data+"] [error] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
				
				throw e;
			}finally{
				if(conn != null){
					try{
						conn.close();
					}catch(SQLException e){
						e.printStackTrace();
					}
				}
				synchronized(lockMap){
					lock.ref--;
					if(lock.ref == 0){
						lockMap.remove(da);
					}
				}
				lock.owner = null;
			}
		}
	}
	

	
	
	/**
	 * <pre>
	 * 查询，各个查询的粒度都设置了值，可能有粒度标记为列表.
	 * 查询的结果为一组记录，记录存放在一个map中，map的key值为所有标记为列表值的粒度，对应的具体值。
	 * 比如：
	 *     各个维度的信息如下：
	 *     Operator:   operator='cmcc' node=*
	 *     Product:    product='abc'   
	 *     Channel:    type=*
	 *     Province:   province=*
	 * 
	 * 那么结果Map中的key也应该为一个map，如下：
	 *     node='xxxx' type='yyyy' province='zzzz'    
	 * 
	 * </pre>
	  *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static Map<Map<String,String>,StatData> query(Connection conn,String tableName,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = StatisticsSQLConstructor.constructQuerySQL(tableName,ds,template,filterConditionExpression,orderByConditionExpression,desc);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query] [construct_sql_error] ["+tableName+"] ["+toString(ds)+"] ["+template+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			try{
				conn.close();
			}catch(Exception ex){}
			throw e;
		}
		
		return execute_query(conn,sql,ds,template);

	}
	
	/**
	 * 查询用户状态表，并且对数据项进行求和操作。
	 * 此查询可指定用户维度，对数据可以分页读取。
	 * 
	 * @param yearMonth 查询那个月的数据，格式必须是yyyy_MM  
	 * @param template 统计数据模板，不需要填写数据。
	 * @param filterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null 
	 * @return 返回查询的结果，请参见其他方法的说明
	 */		
	public static int count(Connection conn,String tableName,Dimension []ds, StatData template,
			String filterConditionExpression) throws Exception{
		String sql = null;
		long startTime = System.currentTimeMillis();
		try{
			sql = StatisticsSQLConstructor.constructCountSQL(tableName,ds,template,filterConditionExpression);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[count] [construct_sql_error] ["+tableName+"] ["+ds+"] ["+template+"] ["+filterConditionExpression+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			try{
				conn.close();
			}catch(Exception ex){}
			throw e;
		}
		
		return execute_count(conn,sql);
		
	}
	
	/**
	 * <pre>
	 * 查询，各个查询的粒度都设置了值，可能有粒度标记为列表.
	 * 查询的结果为一组记录，记录存放在一个map中，map的key值为所有标记为列表值的粒度，对应的具体值。
	 * 比如：
	 *     各个维度的信息如下：
	 *     Operator:   operator='cmcc' node=*
	 *     Product:    product='abc'   
	 *     Channel:    type=*
	 *     Province:   province=*
	 * 
	 * 那么结果Map中的key也应该为一个map，如下：
	 *     node='xxxx' type='yyyy' province='zzzz'    
	 * 
	 * </pre>
	  *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static Map<Map<String,String>,StatData> queryForOracle(Connection conn,String tableName,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc,int start,int size) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = StatisticsSQLConstructor.constructQuerySQLWithPageOnOracle(tableName,ds,template,filterConditionExpression,orderByConditionExpression,desc,start,size);
		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query] [construct_sql_error] ["+tableName+"] ["+toString(ds)+"] ["+template+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			try{
				conn.close();
			}catch(Exception ex){}
			throw e;
		}
		
		return execute_query(conn,sql,ds,template);

	}
	
	/**
	 * <pre>
	 * 查询某个粒度的非相同值，比如独立用户数。
	 * 各个查询的粒度都设置了值，可能有粒度标记为列表.
	 * 查询的结果为一组记录，记录存放在一个map中，map的key值为所有标记为列表值的粒度，对应的具体值。
	 * 比如：
	 *     各个维度的信息如下：
	 *     Operator:   operator='cmcc' node=*
	 *     Product:    product='abc'   
	 *     Channel:    type=*
	 *     Province:   province=*
	 * 
	 * 那么结果Map中的key也应该为一个map，如下：
	 *     node='xxxx' type='yyyy' province='zzzz'    
	 * 
	 * </pre>
	  *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static Map<Map<String,String>,Integer> queryGranulaCount(Connection conn,String tableName,String granula,Dimension []ds, StatData template,String filterCondition) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = StatisticsSQLConstructor.constructGranulaDistinctCountSQL(tableName,granula,ds,filterCondition);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query_granula_count] [construct_sql_error] ["+tableName+"] ["+toString(ds)+"] ["+template+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			try{
				conn.close();
			}catch(Exception ex){}
			throw e;
		}
		
		Map<Map<String,String>,StatData> result = execute_query(conn,sql,ds,template);
		
		Map<Map<String,String>,Integer> resultMap = new LinkedHashMap<Map<String,String>,Integer>();
		
		Iterator<Map<String,String>> it = result.keySet().iterator();
		while(it.hasNext()){
			Map<String,String> key = it.next();
			int n = Integer.parseInt(key.get(granula));
			key.remove(granula);
			Map<String,String> newKey = new HashMap<String,String>();
			newKey.putAll(key);
			resultMap.put(newKey,n);
		}
		return resultMap;
	}
	
	public static Map<Map<String,String>,StatData> execute_query(Connection conn,String sql,Dimension []ds, StatData template) throws Exception{
		return execute_query(conn,sql,ds,template,0,0);
	}
	public static Map<Map<String,String>,StatData> execute_query(Connection conn,String sql,Dimension []ds, StatData template,int fetchSize,int maxRows) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		try{
			Statement stmt = conn.createStatement();
			if(fetchSize > 0)
				stmt.setFetchSize(fetchSize);
			if(maxRows > 0)
				stmt.setMaxRows(maxRows);
			Map<Map<String,String>,StatData> resultMap = new LinkedHashMap<Map<String,String>,StatData>();
			ResultSet rs  = stmt.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int cc = md.getColumnCount();
			String columnNames[] = new String[cc];
			for(int i = 0 ; i < cc ; i++){
				columnNames[i] = md.getColumnName(i+1).toLowerCase();//oracle
			}
			
			while(rs.next()){
				StatData sd = template.clone();
				sd.reset();
				Map<String,String> columnMap = new HashMap<String,String>();
				for(int i= 0 ;i < columnNames.length ; i++){
					
					if(sd.isExists(columnNames[i])){
						Number n = sd.getValue(columnNames[i]);
						if((n instanceof Byte) || (n instanceof Short) || (n instanceof Integer)){
							sd.setValue(columnNames[i],rs.getInt(columnNames[i]));
						}else if(n instanceof Long){
							sd.setValue(columnNames[i],rs.getLong(columnNames[i]));
						}else if(n instanceof Float){
							sd.setValue(columnNames[i],rs.getFloat(columnNames[i]));
						}else if(n instanceof Double){
							sd.setValue(columnNames[i],rs.getDouble(columnNames[i]));
						}
						
					}else if(sd.isExpressionExists(columnNames[i])){
						Number n = sd.getValueOfExpression(columnNames[i]);
						if((n instanceof Byte) || (n instanceof Short) || (n instanceof Integer)){
							sd.addExpression(columnNames[i],rs.getInt(columnNames[i]));
						}else if(n instanceof Long){
							sd.addExpression(columnNames[i],rs.getLong(columnNames[i]));
						}else if(n instanceof Float){
							sd.addExpression(columnNames[i],rs.getFloat(columnNames[i]));
						}else if(n instanceof Double){
							sd.addExpression(columnNames[i],rs.getDouble(columnNames[i]));
						}
						
					}else{
					
						for(int j = 0 ; j < ds.length ; j++){
							if(ds[j].isGranula(columnNames[i])){
								String value = rs.getString(columnNames[i]);
								columnMap.put(columnNames[i],value);
							}else if(ds[j].isAttachment(columnNames[i])){
								Object value = rs.getObject(columnNames[i]);
								columnMap.put(columnNames[i],""+value);
							}
						}
						
						if(columnMap.containsKey(columnNames[i]) == false){
							columnMap.put(columnNames[i],""+rs.getObject(columnNames[i]));
						}
					}
				}
				resultMap.put(columnMap,sd);
			}
			rs.close();
			stmt.close();
			
			if(logger.isDebugEnabled()){
				logger.debug("[query] [rs_num:"+resultMap.size()+"] ["+toString(ds)+"] ["+template+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
			}
			
			return resultMap;
		}catch(SQLException e){
			if(logger.isDebugEnabled()){
				logger.debug("[query] [sql_execute_error] ["+toString(ds)+"] ["+template+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int execute_count(Connection conn,String sql) throws Exception{
		
		long startTime = System.currentTimeMillis();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs  = stmt.executeQuery(sql);
			if(rs.next()){//added by frog.2007-10-20
				int count = rs.getInt(1);
				if(logger.isDebugEnabled()){
					logger.debug("[count] ["+count+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
				}
				
				return count;
			}else{
				return 0;
			}
		}catch(SQLException e){
			if(logger.isDebugEnabled()){
				logger.debug("[count] [sql_execute_error] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static String toString(Dimension ds[]){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < ds.length ; i++){
			sb.append(ds[i].toString());
			if(i < ds.length - 1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	
}
