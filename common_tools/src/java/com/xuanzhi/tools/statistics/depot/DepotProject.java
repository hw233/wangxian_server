package com.xuanzhi.tools.statistics.depot;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.xuanzhi.tools.statistics.Dimension;
import com.xuanzhi.tools.statistics.StatData;
import com.xuanzhi.tools.statistics.StatProject;
import com.xuanzhi.tools.statistics.StatisticsSQLConstructor;
import com.xuanzhi.tools.statistics.StatisticsTool;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 数据仓库统计项目，此实现与StatProject的区别在于：
 * 
 * StatProject的存储方式如下：
 * 		以一个表来完成存储。
 *   粒度a1，粒度a2，粒度b1，粒度b2，粒度c1，统计项s1，统计项s2,统计项s3
 *   
 *   其中，a1,a2是维度A的粒度，b1，b2是维度B的粒度，c1是维度C的粒度
 *   
 *   
 * DepotProject的存储方式如下：
 * 		用多个表来完成存储，每个维度一个表
 *    维度A的维度表：
 *    		id   粒度a1，粒度a2
 *    维度B的维度表：
 *    		id   粒度b1，粒度b2
 *    
 *    维度C的维度表：
 *    		id   粒度c1
 *    统计表
 *          月份，维度A id,维度B id,维度C id,统计项s1，统计项s2,统计项s3
 *    
 * 这样的实现，节省了数据库的存储空间。同时，加速了查询条件的选择。
 * 其中，统计表中多了一个列，就是“月份”，其表达方式是yyyyMM，字符型，用于做范围分区。     
 *   
 */
public abstract class DepotProject extends StatProject {

	public static DepotProject getDepotProject(String className) throws Exception{
		Class cl = Class.forName(className);
		Constructor c = cl.getConstructor(new Class[0]);
		return (DepotProject)c.newInstance(new Object[0]);
	}

	protected boolean directAppend = false;
	
	public boolean isDirectAppend(){
		return this.directAppend;
	}
	
	public void setDirectAppend(boolean b){
		this.directAppend = b;
	}
	
	protected DepotProject(String name){
		super(name);
	}
	
	public boolean isUsingOracle(){
		return true;
	}
	
	public void setUsingOracle(boolean b){
	}
	
	public boolean isAccumulate(){
		return this.accumulate;
	}	
	
	/**
	 * 得到序列号的名词
	 * @return
	 */
	public String getSequenceName(){
		return "SEQ_" + getTableName();
	}
	
	public String getTableName(String timeStamp){
		return getTableName();
	}
	
	public String getTableName(){
		return this.getTableNamePrefix() + "DEPOT";
	}
	
	public String getDimensionTableName(Dimension d){
		return this.getTableNamePrefix() + d.getDimensionUniqueName();
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
	 * 返回一个关于每个表的信息。
	 * 返回的数据结构：
	 * 		表1：   表名  行数 是否分区  最后分析时间
	 *      表2：   表名  行数 是否分区  最后分析时间
	 *      表3：   表名  行数 是否分区  最后分析时间 
	 * @return
	 * @throws Exception
	 */
	public String[][] getAllTableInformation() throws Exception{
		Connection conn = getConnection();
		try{
			String sql = "select table_name,num_rows, partitioned,last_analyzed from USER_TABLES where table_name like '"+this.getTableNamePrefix()+"%'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<String[]> al = new ArrayList<String[]>();
			while(rs.next()){
				String s[] = new String[4];
				for(int i = 0 ; i < s.length ;i++)
					s[i] = rs.getString(i+1);
				
				al.add(s);
			}
			rs.close();
			stmt.close();
			return (String[][])al.toArray(new String[0][]);
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	/**
	 * 获取所有分区的信息
	 * 返回的信息格式：
	 *     分区1  分区名称    截至月份    行数
	 *     分区2  分区名称    截至月份    行数
	 *     ...
	 * @return
	 * @throws Exception
	 */
	public String[][] getAllPartitionInformation() throws Exception{
		Connection conn = getConnection();
		try{
			String sql = "select partition_name,num_rows from USER_TAB_PARTITIONS "
			+" where table_name='"+this.getTableName()+"' order by partition_position";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<String[]> al = new ArrayList<String[]>();
			while(rs.next()){
				String s[] = new String[3];
				s[0] = rs.getString(1);
				s[2] = rs.getString(2);
				s[1] = s[0].substring(s[0].length()-6);
				al.add(s);
			}
			rs.close();
			stmt.close();
			return (String[][])al.toArray(new String[0][]);
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	public void addPartition(String yearMonth) throws Exception{
		String[][] ps = getAllPartitionInformation();
		String [] lastPartition = ps[ps.length-1];
		Date lastD = DateUtil.parseDate(lastPartition[1], "yyyyMM");
		Date lastD2 = DateUtil.parseDate(ps[ps.length-2][1], "yyyyMM");
		Date currD = DateUtil.parseDate(yearMonth, "yyyyMM");
		
		if(lastD.getTime() > currD.getTime() && currD.getTime() > lastD2.getTime()){
			if(lastPartition[2].equals("0")){
				removePartition(lastPartition[0]);
				insertPartition(yearMonth);
				insertPartition(lastPartition[0]);
			}else{
				throw new Exception("最后一个分区已经有数据，所以在此之前不能再插入新的分区");
			}
		}else if(currD.getTime() > lastD.getTime()){
			insertPartition(yearMonth);
		}else{
			throw new Exception("不能在倒数第二个分区前，再插入一个分区");
		}
	}
	
	private void insertPartition (String yearMonth) throws Exception{
		Connection conn = getConnection();
		String partitionName = getTableName() + "_P_" + yearMonth;
		try{
			String sql = "alter table "+this.getTableName()+" add partition "+partitionName+" values less than('"+yearMonth+"')";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	private void removePartition (String yearMonth) throws Exception{
		Connection conn = getConnection();
		String partitionName = getTableName() + "_P_" + yearMonth;
		try{
			String sql = "alter table "+this.getTableName()+" drop partition "+partitionName+"";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
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
		StringBuffer sb = new StringBuffer();
		Dimension ds[] = this.getDimensions();
		String s = DepotSQLConstructor.constructCreateDepotTableSQL(tableName, ds, this.getStatData(), hashCodeColumnEnabled);
		sb.append(s + "\n\n");
		for(int i = 0 ; i < ds.length ; i++){
			s = DepotSQLConstructor.constructCreateDimensionTableSQL(this.getTableNamePrefix(), ds[i]);
			sb.append(s + "\n\n");
		}
		return sb.toString();
		
	}
	
	public String[] constructCreateIndexSQL(String timeStamp){
		String tableName = getTableName(timeStamp);
		ArrayList<String> al = new ArrayList<String>();
		Dimension ds[] = this.getDimensions();
		String ss[] = DepotSQLConstructor.constructCreateDepotIndexSQL(tableName, ds, hashCodeColumnEnabled);
		for(int i = 0 ; i < ss.length ; i++){
			al.add(ss[i]);
		}
		for(int i = 0 ; i < ds.length ; i++){
			al.add(DepotSQLConstructor.constructCreateDimensionIndexSQL(getTableNamePrefix(), ds[i]));
		}
		return al.toArray(new String[0]);
	}
	/**
	 * 根据时间戳来创建表和索引
	 * 
	 * @param timeStamp
	 */
	public void createTableAndIndexs(String timeStamp)throws Exception{
		check();
		String tableName = getTableName(timeStamp);
		Dimension ds[] = this.getDimensions();
		Connection conn = getConnection();
		try{
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(DepotSQLConstructor.constructSequenceSQLForOracle(tableName));
			stmt.executeUpdate(DepotSQLConstructor.constructCreateDepotTableSQL(tableName, ds, this.getStatData(), hashCodeColumnEnabled));
			String indexs[] = DepotSQLConstructor.constructCreateDepotIndexSQL(tableName, ds, hashCodeColumnEnabled);
			for(int i = 0 ; i < indexs.length ; i++){
				stmt.executeUpdate(indexs[i]);
			}
			
			for(int i = 0 ; i < ds.length ; i++){
				stmt.executeUpdate(DepotSQLConstructor.constructCreateDimensionTableSQL(this.getTableNamePrefix(), ds[i]));
				stmt.executeUpdate(DepotSQLConstructor.constructCreateDimensionIndexSQL(this.getTableNamePrefix(), ds[i]));
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
	public void update(String yearMonth,Dimension []ds,StatData data) throws Exception{
		check();
		checkDimension(ds,data);

		DepotTool.update(yearMonth, this, ds, data);
		
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
	public void update(DepotRowData [] rows) throws Exception{
		check();
		for(int i = 0 ; i < rows.length ; i++){
			checkDimension(rows[i].ds,rows[i].data);
		}

		DepotTool.update(this,rows,true);
		
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
		Map<Map<String,String>,StatData> resultMap = DepotTool.query(yearMonth, this, ds, filterConditionExpression, orderByConditionExpression, desc);
		return getDisplayResultMap(resultMap);
	}
}
