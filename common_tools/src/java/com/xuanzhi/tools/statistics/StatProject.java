package com.xuanzhi.tools.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.*;

/**
 * 描述一个统计项目，
 * 一个统计项目中，将确切的描述各个维度的信息，已经统计项的信息。
 * 并且提供自动生成表结构的方法。
 * 
 * 子类必须有无参数的公共构造函数，而且必须设置维度信息，统计项信息，
 * 表名的前缀，统计项目的名称等。
 * 
 *
 */
public abstract class StatProject {

	
	public static StatProject getStatProject(String className) throws Exception{
		Class cl = Class.forName(className);
		Constructor c = cl.getConstructor(new Class[0]);
		return (StatProject)c.newInstance(new Object[0]);
	}
	
	/**
	 * 是否产生一个新的字段，用于储存所有维度的MD5值
	 * 
	 * 此值为了解决多个索引的问题，数据库在多个索引的情况下，
	 * 可能不能正确的选择索引，或者说各个索引值相对集中的时候，
	 * 索引的效果不好
	 * 
	 * 用所有维度的md5来做索引，效果会非常好，极大的提高行数据的查询，更新速度。
	 */
	protected boolean hashCodeColumnEnabled = false;
	
	private String tableNamePrefix;
	
	protected boolean usingOracle = false;
	protected String tableSpace = null;
	
	// 统计项
	protected StatData data;
	
	protected ArrayList<Dimension> dList = new ArrayList<Dimension>();
	
	protected boolean accumulate = true;
	
	protected String name;
	
	protected StatProject(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isUsingOracle(){
		return usingOracle;
	}
	
	public void setUsingOracle(boolean b){
		usingOracle = b;
	}
	
	public boolean isHashCodeColumnEnabled(){
		return hashCodeColumnEnabled;
	}
	
	public String getTableNamePrefix(){
		return tableNamePrefix;
	}
	
	protected void setTableNamePrefix(String prefix){
		this.tableNamePrefix = prefix;
	}
	/**
	 * 项目的统计项
	 * @return
	 */
	public StatData getStatData(){
		return data;
	}
	
	public String getSelectGranulaProjectClassName(){
		return this.getClass().getName();
	}
	/**
	 * 项目的维度信息
	 * @return
	 */
	public Dimension[] getDimensions(){
		return dList.toArray(new Dimension[0]);
	}

	/**
	 * 获得不能用于累加的粒度，某些粒度必须展开或者制定确定的值，
	 * 因为这个粒度下的统计项不能累加。默认返回长度为0的数组
	 * @return
	 */
	public String[] getUnAccumulateGranulas(){
		return new String[0];
	}
	/**
	 * 项目的表
	 * @param timeStamp
	 * @return
	 */
	public String getTableName(String timeStamp){
		String s = tableNamePrefix + timeStamp;
		if(usingOracle && s.length() > 26){
			throw new IllegalArgumentException("table name is too long than 26");
		}
		return s;
	}
	
	/**
	 * 查询快捷链接保存文件，默认为null，
	 * 子类可以设置相应的值，在查询页面中，会出现相关的查询快捷链接
	 * @return
	 */
	public File getQuickLinkFile(){
		return null;
	}
	
	protected void check(){
		if(tableNamePrefix == null) throw new IllegalStateException("table name prefix not set");
		if(data == null) throw new IllegalStateException("StatData not set");
		if(dList.size() == 0 ) throw new IllegalStateException("Dimensions not set");

	}
	
	public void checkDimension(Dimension ds[],StatData sd){
		for(int i = 0 ; i < ds.length ; i++){
			boolean b = false;
			for(int j = 0 ; j < dList.size() ; j++){
				Dimension d = dList.get(j);
				if(d.getClass() == ds[i].getClass()){
					b = true;
				}
			}
			if(b == false){
				throw new IllegalArgumentException("Dimension ["+ds[i].getName()+"] is invalid for this project ["+name+"] .");
			}
		}
		
		if(sd.getClass() != data.getClass()){
			throw new IllegalArgumentException("StatData ["+sd+"] is invalid for this project ["+name+"] .");
		}
	}
	
	public Dimension find(Dimension ds[],Class cl){
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i].getClass().equals(cl))
				return ds[i];
		}
		return null;
	}
	
	/**
	 * 得到所有已经创建了的表名
	 * @return
	 */
	public String[] getAllTables() throws Exception{
		check();
		ArrayList<String> al = new ArrayList<String>();
		Connection conn = getConnection();
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null,null,getTableNamePrefix()+"%",null);
			while(rs.next()){
				String t = rs.getString(3);
				al.add(t);
			}
			rs.close();
			return (String[])al.toArray(new String[0]);
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	/**
	 * 得到制定表上创建的索引
	 * @return String[] 表示一个索引 String[][0]为索引的名称，String[][0]为索引的字段
	 */
	public String[][] getIndexs(String tableName) throws Exception{
		check();
		ArrayList<String[]> al = new ArrayList<String[]>();
		HashMap<String,String> map = new HashMap<String,String>();
		Connection conn = getConnection();
		try{
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getIndexInfo(null,null,tableName,false,true);
			while(rs.next()){
				String s[] = new String[2];
				s[0] = rs.getString(6);
				s[1] = rs.getString(9);
				String ss = map.get(s[0]);
				if(ss == null){
					map.put(s[0],s[1]);
				}else{
					map.put(s[0],ss+","+s[1]);
				}
			}
			rs.close();
			
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String s[] = new String[2];
				s[0] = it.next();
				s[1] = map.get(s[0]);
				al.add(s);
			}
			
			return (String[][])al.toArray(new String[0][]);
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	public String constructCreateTableSQL(String timeStamp){
		String tableName = getTableName(timeStamp);
		
		if(isUsingOracle()){
			return StatisticsSQLConstructor.constructCreateTableSQLForOracle(tableName,tableSpace,this.getDimensions(),this.getStatData(),hashCodeColumnEnabled);
		}else{
			return StatisticsSQLConstructor.constructCreateTableSQL(tableName,this.getDimensions(),this.getStatData(),hashCodeColumnEnabled);
		}
	}
	
	public String[] constructCreateIndexSQL(String timeStamp){
		String tableName = getTableName(timeStamp);
		if(isUsingOracle()){
			return StatisticsSQLConstructor.constructCreateIndexSQLForOracle(tableName,tableSpace,this.getDimensions(),hashCodeColumnEnabled);
		}else{
			return StatisticsSQLConstructor.constructCreateIndexSQL(tableName,this.getDimensions(),hashCodeColumnEnabled);
		}
	}
	/**
	 * 根据时间戳来创建表和索引
	 * 
	 * @param timeStamp
	 */
	public void createTableAndIndexs(String timeStamp)throws Exception{
		check();
		
		String tableName = getTableName(timeStamp);
		
		String sequence = null;
		String sql = null; 
		String indexs[] = null;
		if(isUsingOracle()){
			sequence = StatisticsSQLConstructor.constructSequenceSQLForOracle(tableName);
			sql = StatisticsSQLConstructor.constructCreateTableSQLForOracle(tableName,tableSpace,this.getDimensions(),this.getStatData(),hashCodeColumnEnabled);
			indexs = StatisticsSQLConstructor.constructCreateIndexSQLForOracle(tableName,tableSpace,this.getDimensions(),hashCodeColumnEnabled);
		}else{
			sql = StatisticsSQLConstructor.constructCreateTableSQL(tableName,this.getDimensions(),this.getStatData(),hashCodeColumnEnabled);
			indexs = StatisticsSQLConstructor.constructCreateIndexSQL(tableName,this.getDimensions(),hashCodeColumnEnabled);
		}
		
		Connection conn = getConnection();
		try{
			
			Statement stmt = conn.createStatement();
			
			if(sequence != null){
				stmt.executeUpdate(sequence);
			}
			
			stmt.executeUpdate(sql);
			
			
			for(int i = 0 ; i < indexs.length ; i++){
				stmt.executeUpdate(indexs[i]);
			}
			
			stmt.close();
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
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
	public void update(String timeStamp,Dimension []ds,StatData data) throws Exception{
		check();
		checkDimension(ds,data);
	
		String tableName = getTableName(timeStamp);
		
		StatisticsTool.update(getConnection(),tableName,ds,data,accumulate);
		
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
	public Map<Map<String,String>,StatData> query(String yearMonth,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc) throws Exception{
		
		Map<Map<String,String>,StatData> resultMap = StatisticsTool.query(this.getConnection(),getTableName(yearMonth),ds,template,
				filterConditionExpression,orderByConditionExpression,desc);
		
		return getDisplayResultMap(resultMap);
	}
	/**
	 * 用于显示的统计项
	 * @return
	 */
	public StatData getDisplayStatData(){
		return data;
	}
	
	/**
	 * 用于显示的查询结果，每次查询结果都会过这个方法，可以修改查询结果
	 * @param resultMap
	 * @return
	 */
	public Map<Map<String,String>,StatData> getDisplayResultMap(Map<Map<String,String>,StatData> resultMap){
		return resultMap;
	}
	
	
	/**
	 * 数据构造，此方法定义为构造此项目统计表需要的数据
	 * 至于怎么构造数据，从什么地方获取数据来构造，都是有子类来定义，实现
	 * 生成那个月份，那天的数据
	 * @param yearMonth 格式为yyyy_MM
	 * @param dayStr 格式为yyyyMMdd
	 */
	public abstract void dataConstruct(String yearMonth,String dayStr);
	
	/**
	 * 获取一个新的数据库链接，由于此类存在多个对象（每次查询都创建新的对象），
	 * 所以，此方法应该从另外一个地方获取一个连接池，并取连接池中的一个链接，返回。
	 * 千万不要在这个类中保留链接。
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection() throws SQLException;
}
