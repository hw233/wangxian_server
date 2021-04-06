package com.xuanzhi.tools.statistics.depot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.statistics.Dimension;
import com.xuanzhi.tools.statistics.StatData;
import com.xuanzhi.tools.statistics.StatisticsSQLConstructor;
import com.xuanzhi.tools.statistics.StatisticsTool;

import com.xuanzhi.tools.text.StringUtil;


public class DepotTool {
	
	public static long maxCacheLifeTime = 15 * 60 * 1000L;
	public static int maxCacheElementNum = 81920;
	/**
	 * 用于Cache维度的信息
	 */
	protected static LruMapCache cache = null;

	protected static Logger logger = Logger.getLogger(DepotTool.class);
	
	protected static HashMap<DimensionArray,Lock> lockMap = new HashMap<DimensionArray,Lock>();
	
	public static HashMap<DimensionArray,Lock> getLockMap()
	{
		return (HashMap<DimensionArray,Lock>)lockMap.clone();
	}
	
	public static LruMapCache getCache(){
		return cache;
	}
	
	public static class DimensionID implements Cacheable{
		long id;
		public DimensionID(long id){
			this.id = id;
		}
		public int getSize(){
			return 1;
		}
		
		public String toString(){
			return ""+id;
		}
	}
	
	public static class DimensionArray{
		
		public String tableName;
		public String dimensionStr;
		public DimensionID[] ids;
		
		public DimensionArray(String tableName,Dimension d){
			this.tableName = tableName;
			this.dimensionStr = d.getDimensionUniqueName();
			String gs[] = d.getAllGranula();
			for(int i = 0 ; i < gs.length ; i++){
				this.dimensionStr +=","+d.getValue(gs[i]);
			}
		}
		
		public DimensionArray(String tableName,DimensionID[] ids){
			this.tableName = tableName;
			this.dimensionStr = "" + ids[0].id;
			for(int i = 1 ; i < ids.length ; i++){
				this.dimensionStr +=","+ids[i].id;
			}
			this.ids = ids;
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
		//public StatData data;//第一个等待线程的数据，后续线程在这个线程启动前，可以将数据累加到这个数据对象中，大幅提高效率
	}
	
	/**
	 * 在维度表中查询，给定的维度粒度对应的ID，返回-1表示未能查询得到
	 * @param project
	 * @param d
	 * @return
	 */
	private static long queryDemensionId(DepotProject project,Dimension d,boolean cacheEnabled,boolean withCreate) throws SQLException{
		long startTime = System.currentTimeMillis();
		String tableName = project.getTableNamePrefix()+d.getDimensionUniqueName();
		DimensionArray da = new DimensionArray(tableName,d);
		
		if(cacheEnabled){
			if(cache == null){
				synchronized(DepotTool.class){
					if(cache == null){
						cache = new LruMapCache(maxCacheElementNum,maxCacheLifeTime,false,"Dimension-Cache");
					}
				}
			}
			DimensionID di = (DimensionID)cache.get(da);
			if(di != null) return di.id;
		}
		
		Lock lock = null;
		synchronized(lockMap){
			lock = lockMap.get(da);
			if(lock == null){
				lock = new Lock();
				lockMap.put(da,lock);
			}
			lock.ref++;
		}
		
		synchronized(lock){
			lock.owner = Thread.currentThread().getName();
			Connection conn = null;
			try{
				if(cacheEnabled){
					DimensionID di = (DimensionID)cache.get(da);
					if(di != null) return di.id;
				}
				conn = project.getConnection();
				int parameterIndex = 1;
				PreparedStatement pstmt = null;
				String sql = "SELECT AUTOID FROM " + tableName + " WHERE ";
				String gs[] = d.getAllGranula();
				for(int i = 0 ; i < gs.length ; i++){
					sql += gs[i] + "=?";
					if(i < gs.length -1){
						sql += " and ";
					}
				}
				pstmt = conn.prepareStatement(sql);
				for(int i = 0 ; i < gs.length ; i++){
					pstmt.setString(parameterIndex, d.getValue(gs[i]));
					parameterIndex++;
				}
				ResultSet rs = pstmt.executeQuery();
				long id = -1;
				if(rs.next()){
					id = rs.getLong(1);
					rs.close();
					pstmt.close();
					if(logger.isDebugEnabled()){
						logger.debug("[foundDimensionId] [lock:"+lock.ref+"] ["+tableName+"] ["+d+"] [id:"+id+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
					}
				}else{
					rs.close();
					pstmt.close();
					if(withCreate){
						String senqunceSQL = "select "+project.getSequenceName()+".NEXTVAL from DUAL";
						Statement stmt = conn.createStatement();
						rs = stmt.executeQuery(senqunceSQL);
						rs.next();
						id = rs.getLong(1);
						rs.close();
						stmt.close();
						
						String insertSQL = "INSERT INTO " + tableName + "(AUTOID" ;
						
						if(project.isDirectAppend()){
							insertSQL = "INSERT /*+ append */ INTO " + tableName + "(AUTOID" ;
							
						}
						
						for(int i = 0 ; i < gs.length ; i++){
							insertSQL += "," + gs[i];
						}
						
						String attachs[] = d.getAttachmentNames();
						for(int i = 0 ; i < attachs.length ; i++){
							if(d.getAttachment(attachs[i]) != null){
								insertSQL += "," + attachs[i];
							}
						}
						insertSQL += ") VALUES (?";
						for(int i = 0 ; i < gs.length ; i++){
							insertSQL += ",?";
						}
						for(int i = 0 ; i < attachs.length ; i++){
							if(d.getAttachment(attachs[i]) != null){
								insertSQL += ",?";
							}
						}
						insertSQL += ")";
						
						parameterIndex = 1;
						pstmt = conn.prepareStatement(insertSQL);
						pstmt.setLong(parameterIndex, id);
						parameterIndex++;
						for(int i = 0 ; i < gs.length ; i++){
							pstmt.setString(parameterIndex, d.getValue(gs[i]));
							parameterIndex++;
						}
						for(int i = 0 ; i < attachs.length ; i++){
							if(d.getAttachment(attachs[i]) != null){
								pstmt.setObject(parameterIndex, d.getAttachment(attachs[i]));
								parameterIndex++;
							}
						}
						pstmt.executeUpdate();
						
						pstmt.close();
						
						if(logger.isDebugEnabled()){
							logger.debug("[insertDimensionId] [lock:"+lock.ref+"] ["+tableName+"] ["+d+"] [id:"+id+"] ["+insertSQL+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
					}else{
						if(logger.isDebugEnabled()){
							logger.debug("[missDimensionId] [lock:"+lock.ref+"] ["+tableName+"] ["+d+"] [id:"+id+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
					}
				}
				if(id != -1 && cache != null){
					cache.put(da, new DimensionID(id));
				}
				return id;
			}catch(SQLException e){
				logger.warn("[queryDimensionId] [lock:"+lock.ref+"] ["+tableName+"] ["+d+"] [id:-] [error] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				throw e;
			}finally{
				synchronized(lockMap){
					lock.ref--;
					if(lock.ref == 0){
						lockMap.remove(da);
					}
				}
				lock.owner = null;
				if(conn != null){
					conn.close();
				}
			}
		}
	}
	
	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param yearMonth 月份，用于分区表和分区索引，所以必须填上，此选项表示只能按月查询数据
	 * @param project 数据仓库项目
	 * @param ds 各个维度，每个维度已经设置好值
	 * @param data 统计项
	 * @throws Exception
	 */
	public static void update(String yearMonth,DepotProject project,Dimension []ds,StatData data) throws Exception{
		update(yearMonth,project,ds,data,true);
	}

	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param yearMonth 月份，用于分区表和分区索引，所以必须填上，此选项表示只能按月查询数据
	 * @param project 数据仓库项目
	 * @param ds 各个维度，每个维度已经设置好值
	 * @param data 统计项
	 * @param cacheEnabled 是否启用Cache，Cache可以加快维度的查询
	 * @throws Exception
	 */
	public static void update(DepotProject project,DepotRowData[] rows,boolean cacheEnabled) throws Exception{
		long startTime = System.currentTimeMillis();
		for(int j = 0 ; j < rows.length ; j++){
			Dimension []ds = rows[j].ds;
			for(int i = 0 ; i < ds.length ; i++){
				if(ds[i].isReadyForUpdateData() == false){
					throw new Exception("Dimension ["+ds[i]+"] hasn't ready for update data");
				}
			}
		}
		String tableName = project.getTableName();
		HashMap<DimensionArray,DepotRowData> map = new HashMap<DimensionArray,DepotRowData>();
		for(int j = 0 ; j < rows.length ; j++){
			Dimension []ds = rows[j].ds;
			DimensionID[] ids = new DimensionID[ds.length];
			for(int i = 0 ; i < ds.length ; i++){
				long id = queryDemensionId(project,ds[i],cacheEnabled,true);
				ids[i] = new DimensionID(id);
			}
			DimensionArray da = new DimensionArray(tableName,ids);
			DepotRowData row = map.get(da);
			if(row == null){
				map.put(da, rows[j]);
			}else{
				row.data.add(rows[j].data);
			}
		}
		
		synchronized(project){
			Connection conn = null;
			try{
				conn = project.getConnection();
				
				conn.setAutoCommit(false);
				
				Iterator<DimensionArray> it = map.keySet().iterator();
				while(it.hasNext()){
					
					DimensionArray da = it.next();
					
					DepotRowData row = map.get(da);
					StatData data = row.data;
					Dimension []ds = row.ds;
					String yearMonth = row.yearMonth;
					DimensionID[] ids = da.ids;
					
					StatData fds = null;
					
					String fields[] = data.getAllDataField();
					int parameterIndex = 1;
					PreparedStatement pstmt = null;
					long autoid = -1;	
					
					if(project.isDirectAppend() == false){
						
						String selectSql = "SELECT autoid,";
							
						for(int i = 0 ; i < fields.length ; i++){
							if(i < fields.length - 1)
								selectSql += fields[i] + ",";
							else
								selectSql += fields[i] + "";
						}
						selectSql += " FROM " + tableName + " WHERE YYYYMM=? AND ";
							
						if(project.isHashCodeColumnEnabled()){
								selectSql += StatisticsTool.HASHCODE+"=?";
						}else{
								for(int i = 0 ; i < ds.length ; i++){
									selectSql += ds[i].getDimensionUniqueName() + "=?";
									if(i < ds.length -1)
										selectSql += " AND ";
								}
						}
							
						pstmt = conn.prepareStatement(selectSql);
						
						pstmt.setString(parameterIndex,yearMonth);
						parameterIndex++;
						
						if(project.isHashCodeColumnEnabled()){
							pstmt.setString(parameterIndex,StringUtil.hash(toString(ids)));
							parameterIndex++;
						}else{
								for(int i = 0 ; i < ds.length ; i++){
									pstmt.setLong(parameterIndex, ids[i].id);
									parameterIndex ++;
								}
						}
						ResultSet rs = pstmt.executeQuery();
						
						if(rs.next()){
							fds = data.clone();
							fds.reset();
								
							autoid = rs.getLong("autoid");
								
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
								logger.debug("[UPDATE_BATCH_DATA] ["+tableName+"] ["+toString(ds)+"] [select_exist] ["+fds+"] ["+selectSql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
							}
				
						}else{
							
								if(logger.isDebugEnabled()){
									logger.debug("[UPDATE_BATCH_DATA] ["+tableName+"] ["+toString(ds)+"] [select_not_exist] ["+selectSql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
								}
						}
							
						rs.close();
						pstmt.close();
					}
					
					if(fds == null){ // insert
						
						String sql ="INSERT INTO " + tableName +" (";
						
						if(project.isDirectAppend()){
							 sql ="INSERT /*+ append */ INTO " + tableName +" (";
						}
						
						sql +="autoid,YYYYMM,";
						
						for(int i = 0 ; i < ds.length ; i++){
							sql += ds[i].getDimensionUniqueName() + ",";
						}
						for(int i = 0 ; i < fields.length ; i++){
							sql += fields[i] + ",";
						}
						sql = sql.substring(0,sql.length() - 1);
						
						if(project.isHashCodeColumnEnabled()){
							sql += ","+StatisticsTool.HASHCODE;
						}
						
						sql +=") VALUES(";
						
						sql +=project.getSequenceName()+".nextval,?,";
						
						for(int i = 0 ; i < ds.length ; i++){
							sql +="?,";
						}
						for(int i = 0 ; i < fields.length ; i++){
							sql += "?" + ",";
						}
						sql = sql.substring(0,sql.length() - 1);
						
						if(project.isHashCodeColumnEnabled()){
							sql += ",?";
						}
						
						sql +=")";
						
						parameterIndex = 1;
						pstmt = conn.prepareStatement(sql);
						
						pstmt.setString(parameterIndex, yearMonth);
						parameterIndex++;
						
						for(int i = 0 ; i < ds.length ; i++){
							pstmt.setLong(parameterIndex,ids[i].id);
							parameterIndex++;
						}
						for(int i = 0 ; i < fields.length ; i++){
							Number value = data.getValue(fields[i]);
							pstmt.setObject(parameterIndex,value);
							parameterIndex++;
						}
						
						if(project.isHashCodeColumnEnabled()){
							pstmt.setString(parameterIndex,StringUtil.hash(toString(ids)));
							parameterIndex++;
						}
						
						int r = pstmt.executeUpdate();
						
						if(logger.isDebugEnabled()){
							logger.debug("[UPDATE_BATCH_DATA] ["+tableName+"] ["+toString(ds)+"] [insert] ["+(r>0?"succ":"failed")+"] ["+data+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
						
						pstmt.close();
					}else{ //update
						if(project.isAccumulate()){
							fds.add(data);
						}else{
							fds = data;
						}
						
						String sql ="UPDATE " + tableName +" SET ";
						for(int i = 0 ; i < fields.length ; i++){
							if(i == fields.length - 1)
								sql += fields[i]+"=?";
							else
								sql += fields[i]+"=?,";
						}
						sql += " WHERE YYYYMM=? and autoid=?";
						
						parameterIndex = 1;
						pstmt = conn.prepareStatement(sql);
						
						for(int i = 0 ; i < fields.length ; i++){
							Number value = fds.getValue(fields[i]);
							pstmt.setObject(parameterIndex,value);
							parameterIndex++;
						}
						pstmt.setString(parameterIndex,yearMonth);
						parameterIndex++;
						pstmt.setLong(parameterIndex,autoid);
						parameterIndex++;
						
						int r = pstmt.executeUpdate();
						
						if(logger.isDebugEnabled()){
							logger.debug("[UPDATE_BATCH_DATA] ["+tableName+"] ["+toString(ds)+"] [update] ["+(r>0?"succ":"failed")+"] ["+fds+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}
						
						pstmt.close();
					}
					
				}
				
				conn.commit();
				
			}catch(SQLException e){
				
				for(int i = 0 ; i < rows.length ; i++){
					logger.warn("[UPDATE_BATCH_DATA] ["+tableName+"] ["+toString(rows[i].ds)+"] ["+rows[i].data+"] [error] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
				throw e;
			}finally{
				
				if(conn != null){
					try{
						conn.setAutoCommit(true);
						conn.close();
					}catch(SQLException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * 更新数据，所有的维度都中的粒度都必须设置值，否则抛出异常。
	 * 
	 * 此操作先检查数据库中是否有对应的数据项，如果有，就将新的数据累加到旧的数据上，否则就插入一条新的数据。
	 * 
	 * @param yearMonth 月份，用于分区表和分区索引，所以必须填上，此选项表示只能按月查询数据
	 * @param project 数据仓库项目
	 * @param ds 各个维度，每个维度已经设置好值
	 * @param data 统计项
	 * @param cacheEnabled 是否启用Cache，Cache可以加快维度的查询
	 * @throws Exception
	 */
	public static void update(String yearMonth,DepotProject project,Dimension []ds,StatData data,boolean cacheEnabled) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i].isReadyForUpdateData() == false){
				throw new Exception("Dimension ["+ds[i]+"] hasn't ready for update data");
			}
		}
		String tableName = project.getTableName(yearMonth);
		DimensionID[] ids = new DimensionID[ds.length];
		for(int i = 0 ; i < ds.length ; i++){
			long id = queryDemensionId(project,ds[i],cacheEnabled,true);
			ids[i] = new DimensionID(id);
		}
		
		Lock lock = null;
		DimensionArray da = new DimensionArray(tableName,ids);
		synchronized(lockMap){
			lock = lockMap.get(da);
			if(lock == null){
				lock = new Lock();
				lockMap.put(da,lock);
			}
			lock.ref ++;
		}
		
		synchronized(lock){
			lock.owner = Thread.currentThread().getName();
			Connection conn = null;
			try{
				conn = project.getConnection();
				StatData fds = null;
				String fields[] = data.getAllDataField();
				int parameterIndex = 1;
				PreparedStatement pstmt = null;
				
				String selectSql = "SELECT autoid,";
					
				for(int i = 0 ; i < fields.length ; i++){
					if(i < fields.length - 1)
						selectSql += fields[i] + ",";
					else
						selectSql += fields[i] + "";
				}
				selectSql += " FROM " + tableName + " WHERE YYYYMM=? AND ";
					
				if(project.isHashCodeColumnEnabled()){
						selectSql += StatisticsTool.HASHCODE+"=?";
				}else{
						for(int i = 0 ; i < ds.length ; i++){
							selectSql += ds[i].getDimensionUniqueName() + "=?";
							if(i < ds.length -1)
								selectSql += " AND ";
						}
				}
					
				pstmt = conn.prepareStatement(selectSql);
				
				pstmt.setString(parameterIndex,yearMonth);
				parameterIndex++;
				
				if(project.isHashCodeColumnEnabled()){
					pstmt.setString(parameterIndex,StringUtil.hash(toString(ids)));
					parameterIndex++;
				}else{
						for(int i = 0 ; i < ds.length ; i++){
							pstmt.setLong(parameterIndex, ids[i].id);
							parameterIndex ++;
						}
				}
				ResultSet rs = pstmt.executeQuery();
				long autoid = -1;	
				if(rs.next()){
					fds = data.clone();
					fds.reset();
						
					autoid = rs.getLong("autoid");
						
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
			
				if(fds == null){ // insert
					String sql ="INSERT INTO " + tableName +" (";
					
					if(project.isDirectAppend()){
						 sql ="INSERT /*+ append */ INTO " + tableName +" (";
					}
					
					sql +="autoid,YYYYMM,";
					
					for(int i = 0 ; i < ds.length ; i++){
						sql += ds[i].getDimensionUniqueName() + ",";
					}
					for(int i = 0 ; i < fields.length ; i++){
						sql += fields[i] + ",";
					}
					sql = sql.substring(0,sql.length() - 1);
					
					if(project.isHashCodeColumnEnabled()){
						sql += ","+StatisticsTool.HASHCODE;
					}
					
					sql +=") VALUES(";
					
					sql +=project.getSequenceName()+".nextval,?,";
					
					for(int i = 0 ; i < ds.length ; i++){
						sql +="?,";
					}
					for(int i = 0 ; i < fields.length ; i++){
						sql += "?" + ",";
					}
					sql = sql.substring(0,sql.length() - 1);
					
					if(project.isHashCodeColumnEnabled()){
						sql += ",?";
					}
					
					sql +=")";
					
					parameterIndex = 1;
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(parameterIndex, yearMonth);
					parameterIndex++;
					
					for(int i = 0 ; i < ds.length ; i++){
						pstmt.setLong(parameterIndex,ids[i].id);
						parameterIndex++;
					}
					for(int i = 0 ; i < fields.length ; i++){
						Number value = data.getValue(fields[i]);
						pstmt.setObject(parameterIndex,value);
						parameterIndex++;
					}
					
					if(project.isHashCodeColumnEnabled()){
						pstmt.setString(parameterIndex,StringUtil.hash(toString(ids)));
						parameterIndex++;
					}
					
					int r = pstmt.executeUpdate();
					
					if(logger.isDebugEnabled()){
						logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [insert] ["+(r>0?"succ":"failed")+"] ["+data+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
					}
					
				}else{ //update
					if(project.isAccumulate()){
						fds.add(data);
					}else{
						fds = data;
					}
					
					String sql ="UPDATE " + tableName +" SET ";
					for(int i = 0 ; i < fields.length ; i++){
						if(i == fields.length - 1)
							sql += fields[i]+"=?";
						else
							sql += fields[i]+"=?,";
					}
					sql += " WHERE YYYYMM=? and autoid=?";
					
					parameterIndex = 1;
					pstmt = conn.prepareStatement(sql);
					
					for(int i = 0 ; i < fields.length ; i++){
						Number value = fds.getValue(fields[i]);
						pstmt.setObject(parameterIndex,value);
						parameterIndex++;
					}
					pstmt.setString(parameterIndex,yearMonth);
					parameterIndex++;
					pstmt.setLong(parameterIndex,autoid);
					parameterIndex++;
					
					int r = pstmt.executeUpdate();
					
					if(logger.isDebugEnabled()){
						logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] [update] ["+(r>0?"succ":"failed")+"] ["+fds+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
					}
				}
			}catch(SQLException e){
				
				if(logger.isDebugEnabled()){
					logger.debug("[UPDATEDATA] [lock:"+lock.ref+"] ["+tableName+"] ["+toString(ds)+"] ["+data+"] [error] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
				
				throw e;
			}finally{
				
				synchronized(lockMap){
					lock.ref--;
					if(lock.ref == 0){
						lockMap.remove(da);
					}
					
				}
				lock.owner = null;
				if(conn != null){
					try{
						conn.close();
					}catch(SQLException e){
						e.printStackTrace();
					}
				}
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
	public static Map<Map<String,String>,StatData> query(String yearMonth,DepotProject project,Dimension []ds,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = DepotSQLConstructor.constructQuerySQL(yearMonth,project,ds,filterConditionExpression,orderByConditionExpression,desc);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query] [construct_sql_error] ["+yearMonth+"] ["+project.getName()+"] ["+project.getTableName()+"] ["+toString(ds)+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			throw e;
		}
		
		return execute_query(project.getConnection(),sql,ds,project.getStatData());

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
	public static int count(String yearMonth,DepotProject project,Dimension []ds,
			String filterConditionExpression) throws Exception{
		String sql = null;
		long startTime = System.currentTimeMillis();
		try{
			sql = DepotSQLConstructor.constructCountSQL(yearMonth,project,ds,filterConditionExpression);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[count] [construct_sql_error] ["+yearMonth+"] ["+project.getName()+"] ["+project.getTableName()+"] ["+ds+"] ["+filterConditionExpression+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			throw e;
		}
		
		return execute_count(project.getConnection(),sql);
		
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
	public static Map<Map<String,String>,StatData> queryForPage(String yearMonth,DepotProject project,Dimension []ds,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc,int start,int size) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = DepotSQLConstructor.constructQuerySQLWithPage(yearMonth, project, ds, filterConditionExpression, orderByConditionExpression, desc, start, size);
		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query] [construct_sql_error] ["+yearMonth+"] ["+project.getName()+"] ["+project.getTableName()+"] ["+toString(ds)+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			throw e;
		}
		
		return execute_query(project.getConnection(),sql,ds,project.getStatData());

	}
	
	/**
	 * 
	 * @param yearMonth
	 * @param project
	 * @param d
	 * @param granula
	 * @param ds
	 * @param filterCondition
	 * @return
	 * @throws Exception
	 */
	public static Map<Map<String,String>,Integer> queryGranulaCount(String yearMonth,DepotProject project,Dimension d,String granula,Dimension []ds, String filterCondition) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String sql = null;
		
		try{
			sql = DepotSQLConstructor.constructGranulaDistinctCountSQL(yearMonth,project,d,granula,ds,filterCondition);

		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("[query_granula_count] [construct_sql_error] ["+yearMonth+"] ["+project.getName()+"] ["+project.getTableName()+"] ["+toString(ds)+"] [-] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			}
			throw e;
		}
		
		Map<Map<String,String>,StatData> result = execute_query(project.getConnection(),sql,ds,project.getStatData());
		
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
	
	/**
	 * 执行查询，并且将结果返回。
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
	 * @param conn
	 * @param sql
	 * @param ds
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public static Map<Map<String,String>,StatData> execute_query(Connection conn,String sql,Dimension []ds, StatData template) throws Exception{
		return execute_query(conn,sql,ds,template,0,10000);
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
			logger.warn("[query] [sql_execute_error] ["+toString(ds)+"] ["+template+"] ["+sql+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
			
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
			if(rs.next()){
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


	public static String toString(DimensionID ids[]){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < ids.length ; i++){
			sb.append(ids[i].id);
			if(i < ids.length - 1)
				sb.append(",");
		}
		return sb.toString();
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
