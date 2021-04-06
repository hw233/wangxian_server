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
import com.xuanzhi.tools.text.DateUtil;

/**
 * ClusterProject 与 DepotProject的不同
 * 
 * ClusterProject 主要是解决下面的问题：
 *    大范围查询数据的时候，查询非常慢的问题。
 *    比如经常有需求要求查询，某一个月内某个渠道的数据，那么这个查询就非常慢。
 *    因为需要扫描一个月的数据。而一个月的数据基本都在几千万行数据，需要把几千万行数据上数据进行累加，
 *    速度肯定快不了。
 *    
 *    一直解决办法就是先将一个数据计算好，放在那里，然后供查询使用。
 *    本质上计算的时间省不掉，但是我们可以在人们需要数据的时候，已经计算好了，
 *    这样人们使用的感受会好很多。但是事先计算也需要时间，所以这个需要设计好，
 *    需要用更多的硬件或算法，来节省事先计算对业务系统的影响。
 * 
 * 如果一个统计项目对应的维度为： A,B,C,D,E   
 * DepotProject的组织形式如下，一共6张表：
 * 		A表，B表，C表，D表，E表，
 * 		Depot表（包含A,B,C,D,E五个字段）
 * 
 * ClusterProject的组织形式如下，一共n张表
 * 		A表，B表，C表，D表，E表，
 * 		Depot0表（包含A,B,C,D,E字段）
 * 		Depot1表（包含A字段）
 * 		Depot2表（包含A,B字段）
 * 		Depot3表（包含A,C字段）
 * 		Depot4表（包含B,C,D字段）
 * 		Depot5表（包含C,D,E字段）
 * 		Depot6表（包含D,E字段）
 * 
 */
public abstract class ClusterProject extends StatProject{

	public static ClusterProject getClusterProject(String className) throws Exception{
		Class cl = Class.forName(className);
		Constructor c = cl.getConstructor(new Class[0]);
		return (ClusterProject)c.newInstance(new Object[0]);
	}
	
	static class __DepotProject extends DepotProject{
		ClusterProject cp = null;
		DimensionArray da;
		__DepotProject(String name,ClusterProject cp,DimensionArray da){
			super(name);
			this.cp = cp;
			this.da = da;
			for(int i = 0 ; i < da.ds.length ; i++){
				this.dList.add(da.ds[i]);
			}
			this.setTableNamePrefix(cp.getTableNamePrefix());
			this.accumulate = cp.accumulate;
			this.data = cp.data;
			this.hashCodeColumnEnabled = cp.hashCodeColumnEnabled;
		}
		
		public boolean isUsingOracle(){
			return true;
		}
		
		/**
		 * 得到序列号的名词
		 * @return
		 */
		public String getSequenceName(){
			return "SEQ_" + cp.getTableName();
		}
		
		public String getTableName(String timeStamp){
			return getTableName();
		}
	
		public String getTableName(){
			return cp.getTableName(cp.assembleDimensionList.indexOf(da));
		}
	
		public void dataConstruct(String yearMonth, String dayStr) {
		}

		public Connection getConnection() throws SQLException {
			return cp.getConnection();
		}
	}
	
	private static class DimensionArray{
		Dimension ds[];
		DimensionArray(Dimension ds[]){
			this.ds = ds;
		}
		
		public boolean contains(Dimension d){
			for(int j = 0 ; j < this.ds.length ; j++){
				if(this.ds[j].getClass() == d.getClass()){
					return true;
				}
			}
			return false;
		}
		
		public boolean contains(Dimension ds[]){
			for(int i = 0 ; i < ds.length ; i++){
				boolean b = false;
				for(int j = 0 ; j < this.ds.length ; j++){
					if(this.ds[j].getClass() == ds[i].getClass()){
						b = true;
						break;
					}
				}
				if(b == false){
					return false;
				}
			}
			return true;
		}
		
		public boolean equals(Dimension ds[]){
			if(this.ds.length != ds.length) return false;
			for(int i = 0 ; i < ds.length ; i++){
				boolean b = false;
				for(int j = 0 ; j < this.ds.length ; j++){
					if(this.ds[j].getClass() == ds[i].getClass()){
						b = true;
						break;
					}
				}
				if(b == false){
					return false;
				}
			}
			for(int i = 0 ; i < this.ds.length ; i++){
				boolean b = false;
				for(int j = 0 ; j < ds.length ; j++){
					if(ds[j].getClass() == this.ds[i].getClass()){
						b = true;
						break;
					}
				}
				if(b == false){
					return false;
				}
			}
			return true;
		}
		
		public boolean equals(DimensionArray da){
			return equals(da.ds);
		}
	}
	
	private ArrayList<DimensionArray> assembleDimensionList = new ArrayList<DimensionArray>();
	
	/**
	 * 在构造函数中，需要制定多个维度组合
	 * @param name
	 */
	protected ClusterProject(String name) {
		super(name);
	}

	protected void addAssembleDimension(Dimension ds[]){
		for(int i = 0 ; i < ds.length ; i++){
			if(!dList.contains(ds[i])){
				throw new java.lang.IllegalArgumentException("指定的维度["+ds[i].getName()+"]不是此项目的维度");
			}
		}
		for(int i = 0 ; i < ds.length ; i++){
			for(int j = i+1; j < ds.length ; j++){
				if(ds[j].equals(ds[i])){
					throw new java.lang.IllegalArgumentException("指定的维度["+ds[i].getName()+"]重复了");
				}
			}
		}
		for(int i = 0 ; i < assembleDimensionList.size() ; i++){
			DimensionArray da = assembleDimensionList.get(i);
			if(da.equals(ds)){
				return;
			}
		}
		assembleDimensionList.add(new DimensionArray(ds));
		
	}
	
	public boolean isUsingOracle(){
		return true;
	}
	
	public void setUsingOracle(boolean b){
	}
	
	public boolean isAccumulate(){
		return this.accumulate;
	}	
	
	public String getTableName(String timeStamp){
		return getTableName();
	}
	
	public String getTableName(){
		return this.getTableNamePrefix() + "DEPOT";
	}
	
	public String getTableName(int index){
		return this.getTableNamePrefix() + "DEPOT"+index;
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
			+" where table_name like '"+this.getTableName()+"%' order by partition_position";
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
	
	/**
	 * 获取所有分区的信息
	 * 返回的信息格式：
	 *     分区1  分区名称    截至月份    行数
	 *     分区2  分区名称    截至月份    行数
	 *     ...
	 * @return
	 * @throws Exception
	 */
	public String[][] getAllPartitionInformation(String tablename) throws Exception{
		Connection conn = getConnection();
		try{
			String sql = "select partition_name,num_rows from USER_TAB_PARTITIONS "
			+" where table_name='"+tablename+"' order by partition_position";
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
		for(int i = 0 ; i < this.assembleDimensionList.size(); i++){
			String tableName = this.getTableName(i);
			String[][] ps = getAllPartitionInformation(tableName);
			String [] lastPartition = ps[ps.length-1];
			Date lastD = DateUtil.parseDate(lastPartition[1], "yyyyMM");
			Date lastD2 = DateUtil.parseDate(ps[ps.length-2][1], "yyyyMM");
			Date currD = DateUtil.parseDate(yearMonth, "yyyyMM");
			
			if(lastD.getTime() > currD.getTime() && currD.getTime() > lastD2.getTime()){
				if(lastPartition[2].equals("0")){
					removePartition(tableName,lastPartition[0]);
					insertPartition(tableName,yearMonth);
					insertPartition(tableName,lastPartition[0]);
				}else{
					throw new Exception("最后一个分区已经有数据，所以在此之前不能再插入新的分区");
				}
			}else if(currD.getTime() > lastD.getTime()){
				insertPartition(tableName,yearMonth);
			}else{
				throw new Exception("不能在倒数第二个分区前，再插入一个分区");
			}
		}
		
	}
	
	private void insertPartition (String tableName,String yearMonth) throws Exception{
		Connection conn = getConnection();
		String partitionName = tableName + "_P_" + yearMonth;
		try{
			String sql = "alter table "+tableName+" add partition "+partitionName+" values less than('"+yearMonth+"')";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}finally{
			if(conn != null)
				try{ conn.close();}catch(SQLException e){e.printStackTrace();}
		}
	}
	
	private void removePartition (String tableName,String yearMonth) throws Exception{
		Connection conn = getConnection();
		String partitionName = tableName + "_P_" + yearMonth;
		try{
			String sql = "alter table "+tableName+" drop partition "+partitionName+"";
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
		throw new java.lang.UnsupportedOperationException("此方法已经不再使用，请使用createTableAndIndexs（）方法");
	}
	
	public String[] constructCreateIndexSQL(String timeStamp){
		throw new java.lang.UnsupportedOperationException("此方法已经不再使用，请使用createTableAndIndexs（）方法");
	}
	/**
	 * 根据时间戳来创建表和索引
	 * 
	 * @param timeStamp
	 */
	public void createTableAndIndexs(String timeStamp)throws Exception{
		check();
		Connection conn = getConnection();
		try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(DepotSQLConstructor.constructSequenceSQLForOracle(this.getTableName()));

			for(int j = 0 ; j < this.assembleDimensionList.size() ; j++){
				DimensionArray da = assembleDimensionList.get(j);
				
				stmt.executeUpdate(DepotSQLConstructor.constructCreateDepotTableSQL(this.getTableName(j), da.ds, this.getStatData(), hashCodeColumnEnabled));
				String indexs[] = DepotSQLConstructor.constructCreateDepotIndexSQL(this.getTableName(j), da.ds, hashCodeColumnEnabled);
				for(int i = 0 ; i < indexs.length ; i++){
					stmt.executeUpdate(indexs[i]);
				}
			}
			
			Dimension ds[] = this.getDimensions();
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
		
		for(int i = 0 ; i < this.assembleDimensionList.size() ; i++){
			DimensionArray da = this.assembleDimensionList.get(i);
			__DepotProject dp = new __DepotProject(name,this,da);
			ArrayList<Dimension> al = new ArrayList<Dimension>();
			for(int j = 0 ; j < ds.length ; j++){
				if(da.contains(ds[j])){
					al.add(ds[j]);
				}
			}
			DepotTool.update(yearMonth, dp, al.toArray(new Dimension[0]), data);
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
	public Map<Map<String,String>,StatData> query(String yearMonth,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByConditionExpression,boolean desc) throws Exception{
		
		ArrayList<Dimension> al = new ArrayList<Dimension>();
		for(int i = 0 ; i < ds.length ; i++){
			Dimension d = ds[i];
			String gs[] = d.getAllGranula();
			for(int j = 0 ; j < gs.length ; j++){
				if(d.getValue(gs[j]) != null || d.isListMark(gs[j])){
					al.add(d);
					break;
				}
			}
		}
		ds = al.toArray(new Dimension[0]);
		
		DimensionArray da = null;
		for(int i = 0 ; i < this.assembleDimensionList.size() ; i++){
			DimensionArray _da = this.assembleDimensionList.get(i);
			if(_da.equals(ds)){
				da = _da;
				break;
			}
		}
		if(da == null){
			for(int i = 0 ; i < this.assembleDimensionList.size() ; i++){
				DimensionArray _da = this.assembleDimensionList.get(i);
				if(_da.contains(ds)){
					if(da == null)
						da = _da;
					else if(da.ds.length > _da.ds.length){
						da = _da;
					}
				}
			}
		}
		
		if(da == null){
			throw new Exception("配置的维度组合中，没有一个维度组合能包含查询的维度组合"+DepotTool.toString(ds)+"，所以无法进行查询，请联系系统管理员！");
		}
		
		__DepotProject dp = new __DepotProject(name,this,da);
		
		Map<Map<String,String>,StatData> resultMap = DepotTool.query(yearMonth, dp, ds, filterConditionExpression, orderByConditionExpression, desc);
		
		return getDisplayResultMap(resultMap);
	}

}
