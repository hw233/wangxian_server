package com.xuanzhi.tools.statistics.depot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.xuanzhi.tools.statistics.BooleanExpressionUtil;
import com.xuanzhi.tools.statistics.Dimension;
import com.xuanzhi.tools.statistics.StatData;
import com.xuanzhi.tools.statistics.StatisticsTool;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 统计工具类，用于组装各种SQL语句。
 * 
 * 包括，创建表的SQL，索引的SQL，插入的SQL，更新的SQL，基于条件和排序的查询SQL
 * 
 *
 */
public class DepotSQLConstructor {

	public static String constructSequenceSQLForOracle(String tableName){
		return "create sequence SEQ_"+	tableName +" minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20";
	}
	
	/**
	 * 组装维度表的创建语句
	 * @param tablePrefix
	 * @param d
	 * @return
	 */
	public static String constructCreateDimensionTableSQL(String tablePrefix,Dimension d){
		String tableName = tablePrefix + d.getDimensionUniqueName();
		String sql = "CREATE TABLE "+tableName+" (\n";
		sql += "autoid NUMBER not null PRIMARY KEY ,\n";
		String gs[] = d.getAllGranula();
		String as[] = d.getAttachmentNames();
		for(int j = 0 ; j < gs.length; j++){
			sql += gs[j]+" VARCHAR2("+d.getColumnLength()+") DEFAULT NULL";
			if(as.length == 0){
				if(j == gs.length -1){
					sql += ")";
				}else{
					sql += ",\n";
				}
			}else{
				sql += ",\n";
			}
		}
		
		for(int j = 0 ; j < as.length; j++){
			Object o = d.getAttachment(as[j]);
			if((o instanceof Integer) || (o instanceof Short) || (o instanceof Byte)){
				sql += as[j]+" NUMBER DEFAULT 0";
			}else if((o instanceof Float) || (o instanceof Double)){
				sql += as[j]+" NUMBER(24,4) DEFAULT 0";
			}else{
				sql += as[j]+" VARCHAR2("+d.getColumnLength()+") DEFAULT NULL";
			}
			if(j == as.length -1){
				sql += ")";
			}else{
				sql +=",\n";
			}
		}
		return sql;
	}
	
	/**
	 * 组装维度表的索引语句
	 * @param tablePrefix
	 * @param d
	 * @return
	 */
	public static String constructCreateDimensionIndexSQL(String tablePrefix,Dimension d){
		String tableName = tablePrefix + d.getDimensionUniqueName();
		
		int num = d.getNumOfGranula();
			
		String sql = "(";
		String indexName = "";
		for(int j = 0 ; j < num ; j++){
			if(j == num - 1)
				sql += d.getGranula(j) + ")";
			else
				sql += d.getGranula(j) + ",";
				
			if(d.getGranula(j).length()>8)
				indexName +=d.getGranula(j).substring(0,8)+"_";
			else
				indexName +=d.getGranula(j)+"_";
		}
		
		if(indexName.length() + tableName.length() + 4 > 30){
			if(tableName.length() < 20){
				indexName = StringUtil.randomString(30 - 4 - tableName.length());
				indexName = tableName+"_" + indexName + "INX";
			}else{
				indexName = StringUtil.randomString(6);
				indexName = tableName.substring(0,20)+"_" + indexName + "INX";
			}
		}else{
			indexName = tableName+"_" + indexName + "INX";
		}
			
		sql = "CREATE INDEX " + indexName + " ON " + tableName + sql;
		
		if(num > 1){
			sql += " compress "+(num-1);
		}
		
		return sql;
	}
	
	/**
	 * 创建数据仓库表，数据仓库表,
	 * 此仓库表是分区表，以月份来分区，默认从当月开始，建立36个分区。
	 * 最大分区值为“300001”
	 * 
	 * @param tableName
	 * @param ds
	 * @param template
	 * @param hashCodeColumnEnabled
	 * @return
	 */
	public static String constructCreateDepotTableSQL(String tableName,Dimension []ds, StatData template,boolean hashCodeColumnEnabled){

		String sql = "CREATE TABLE "+tableName+" (\n";
		sql += "autoid NUMBER not null PRIMARY KEY ,\n";
		sql += "YYYYMM VARCHAR2(6) not null,\n";
		for(int i = 0 ; i < ds.length ;i++){
			sql += ds[i].getDimensionUniqueName() + " NUMBER not null,\n";
		}
		if(hashCodeColumnEnabled){
			sql += StatisticsTool.HASHCODE + " VARCHAR2(32) DEFAULT NULL,\n";
		}
		
		String df[] = template.getAllDataField();
		for(int i = 0 ; i < df.length ; i++){
			Number n = template.getValue(df[i]);
			if(n instanceof Float || n instanceof Double){
				sql += df[i]+" NUMBER(24,4) DEFAULT 0";
			}else{
				sql += df[i]+" NUMBER DEFAULT 0";
			}
			if(i == df.length - 1){
					sql +=")";
			}
			else
				sql +=",\n";
		}
		sql += "\nPARTITION BY RANGE(YYYYMM)\n";
		sql +="(\n";
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		int n = 36;
		for(int i = 0  ; i < n ; i++){
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
			date = c.getTime();

			String month = DateUtil.formatDate(date, "yyyyMM");
			String partitionName = tableName + "_P_" + month;
			sql += "    PARTITION " + partitionName + " VALUES LESS THAN ('"+month+"'),\n";
			
		}
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		date = c.getTime();
		String month = DateUtil.formatDate(date, "yyyyMM");
		String partitionName = tableName + "_P_" + month;
		sql += "    PARTITION " + partitionName + " VALUES LESS THAN ('"+month+"')\n";
		sql +=")";
		return sql;
	}
	
	/**
	 * 创建所有的索引，每个索引为返回String数组中的一个。
	 * 这些索引都是本地分区索引：
	 *     各个维度索引
	 *     CODEHASH索引
	 * 
	 * 索引的名称为：
	 *            tableName_dimension_INX
	 * 如果索引名超过64（数据库中索引名长度的限制），那么索引名为随机的字符串。
	 *       
	 * @param tableName
	 * @param ds
	 * @return
	 */
	
	public static String[] constructCreateDepotIndexSQL(String tableName,Dimension []ds,boolean hashCodeColumnEnabled){
		ArrayList<String> al = new ArrayList<String>();
		
		for(int i = 0 ; i < ds.length ;i++){
			String sql = "("+ds[i].getDimensionUniqueName()+")";
			String indexName = ds[i].getDimensionUniqueName();
			
			if(indexName.length() + tableName.length() + 4 > 30){
				if(tableName.length() < 20){
					indexName = StringUtil.randomString(30 - 4 - tableName.length());
					indexName = tableName+"_" + indexName + "INX";
				}else{
					indexName = StringUtil.randomString(6);
					indexName = tableName.substring(0,20)+"_" + indexName + "INX";
				}
			}else{
				indexName = tableName+"_" + indexName + "INX";
			}
			
			sql = "CREATE INDEX " + indexName + " ON " + tableName + sql + " LOCAL";
			al.add(sql);
		}
		
		if(hashCodeColumnEnabled){
			String indexName = tableName+"_" + StatisticsTool.HASHCODE + "_INX";
			if(indexName.length() > 30){
				indexName = StringUtil.randomString(6);
				if(tableName.length() > 20)
					indexName = tableName.substring(0,20)+"_" + indexName + "INX";
				else
					indexName = tableName+"_" + indexName + "INX";
					
			}
			al.add("CREATE INDEX " + indexName+" ON "+tableName+" ("+StatisticsTool.HASHCODE+") LOCAL");
		}
			
		return al.toArray(new String[0]);
		
	}
	
	/**
	 *  得到查询的SQL
	 *  
	 *  关于条件表达式：可以是各个粒度和统计项的名词结合在一起的表达式，或者是带上表名的表达式
	 *  关于StatData的计算表达式：必须是带上表名的表达式
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructQuerySQL(String yearMonth,DepotProject project,Dimension []ds,String filterConditionExpression) 
		throws Exception{
		
		String sql = "SELECT ";
		
		ArrayList<String> markListGranulas = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String gg[] = ds[i].getGranulas();
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isListMark(gg[j])){
					markListGranulas.add(tableName+"."+gg[j]);
					sql += tableName+"."+gg[j] + " AS "+gg[j]+",";
				}
			}
		}
		StatData template = project.getStatData();
		String fields[] = template.getAllDataField();
	
		for(int i = 0 ; i < fields.length ;i++){
			sql += " sum("+project.getTableName()+"."+fields[i]+") AS " + fields[i]+",";
		}
		
		fields = template.getAllExpression();
		for(int i = 0 ; i < fields.length ;i++){
			if(fields[i].indexOf("sum")>=0||fields[i].indexOf("count")>=0){
				sql += fields[i]+",";
			}else{
				sql += " "+fields[i]+",";
			}
		}
		
		if(sql.endsWith(",")){
			sql = sql.substring(0,sql.length()-1);
		}
		
		sql += " FROM " + project.getTableName();
		
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String gg[] = ds[i].getGranulas();
			if(gg.length > 0){
				sql += " , " + tableName;
			}
		}
		
		String conditionStr = "";
		if(yearMonth == null){
			conditionStr = project.getTableName()+".YYYYMM='"+yearMonth+"'";
		}else if(yearMonth.indexOf(",") == -1){
			conditionStr = project.getTableName()+".YYYYMM='"+yearMonth+"'";
		}else{
			String ss[] = yearMonth.split(",");
			for(int i = 0 ; i < ss.length ; i++){
				if(i == 0){
					conditionStr += "("; 
				}
				conditionStr += project.getTableName()+".YYYYMM='"+ss[i]+"'";
				if(i < ss.length -1){
					conditionStr += " or ";
				}
				if(i == ss.length -1){
					conditionStr += ")";
				}
			}
		}
		
		
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String granulas[] = ds[i].getGranulas();
			if(granulas.length > 0)
				conditionStr += " and " + project.getTableName()+"."+ds[i].getDimensionUniqueName()+"="+tableName+".autoid";
			for(int j = 0 ; j < granulas.length ; j++){
				if(ds[i].getValue(granulas[j]) != null){
					boolean canStartWith = false;
					boolean canMultiSelect = false;
					boolean canRangeSelect = false;
					String s = ds[i].getValue(granulas[j]);
					if(ds[i].isCanStartWith() && s != null && s.endsWith("*")){
						s = s.substring(0,s.length()-1);
						if(s.length() > 0 && !s.endsWith("*"))
							canStartWith = true;
					}
					if(ds[i].isCanMultiSelect() && s != null && s.indexOf(",")>0){
						canMultiSelect = true;
					}
					if(ds[i].isCanRangeSelect() && s != null && s.indexOf("~") >= 0)
						canRangeSelect = true;
					
					if(canStartWith)
						conditionStr += " and ("+tableName+"." + granulas[j] + " like '"+s+"%')";
					else if(canMultiSelect){
						String sss[] = s.split(",");
						s="";
						for(int x = 0 ; x < sss.length ; x++){
							if(x == 0)
								s = tableName +"." +granulas[j] + "='" + sss[x]+"'";
							else
								s = s + " or " + tableName+"."+granulas[j] + "='" + sss[x]+"'";
						}
						conditionStr += " and (" + s+")";
					}else if(canRangeSelect){
						int xx = s.indexOf("~");
						String s1 = s.substring(0,xx);
						String s2 = s.substring(xx+1);
						s="";
						if(s1.length() > 0)
							s = tableName +"."+granulas[j] +" >= '"+s1+"'";
						if(s2.length() > 0){
							if(s.length() > 0) s = s + " and ";
							s += tableName+"."+granulas[j] +" <= '"+s2+"'";
						}
						conditionStr += " and (" + s+")";
					}else{
						conditionStr += " and " + tableName+"."+granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
					}
				
					
				}
			}
		}
		
				
		if(filterConditionExpression != null && filterConditionExpression.trim().length() > 0){
			
			fields = BooleanExpressionUtil.checkExpression(filterConditionExpression);
			for(int i = 0 ; i < fields.length ;i++){
				boolean b = false;
				for(int j = 0 ; b == false && j < ds.length ; j++){
					String tableName = project.getDimensionTableName(ds[j]);
					if(ds[j].isGranula(fields[i])){
						b = true;
						filterConditionExpression = StringUtil.replace(filterConditionExpression, fields[i], tableName+"."+fields[i]);
					}
				}
				if(template.isExists(fields[i])){
					b = true;
					filterConditionExpression = StringUtil.replace(filterConditionExpression, fields[i], project.getTableName()+"."+fields[i]);
				}
				
				if(b == false && fields[i].indexOf(".") == -1){
					throw new Exception("Field["+fields[i]+"] in filter condition expression is not exists in dimensions and statdata.");
				}
			}
			
			if(conditionStr.length() > 0)
				conditionStr += " and " + filterConditionExpression;
			else
				conditionStr = filterConditionExpression;
		}
		
		if(conditionStr.length() > 0){
			sql += " WHERE " + conditionStr ;
		}
		
		if(markListGranulas.size() > 0){
			sql += " GROUP BY ";
			for(int i = 0 ; i < markListGranulas.size() ; i++){
				if(i == markListGranulas.size() - 1){
					sql += markListGranulas.get(i) + "";
				}else{
					sql += markListGranulas.get(i) + ",";
				}
			}
		}
	
		return sql;
	}
	
	
	public static String constructQuerySQL(String yearMonth,DepotProject project,Dimension ds[],String filterConditionExpression,
			String orderByExpression,boolean desc)throws Exception{
		
		String sql = constructQuerySQL(yearMonth,project,ds,filterConditionExpression);
		
		if(orderByExpression != null && orderByExpression.trim().length() > 0){
			sql = " SELECT * FROM ("+sql+") AS TTTT ORDER BY " + orderByExpression;
			if(desc){
				sql += " DESC";
			}
		}
		
		return sql;
	}
	
	public static String constructQuerySQLWithPage(String yearMonth,DepotProject project,Dimension []ds,
			String filterConditionExpression,
			String orderByExpression,boolean desc,int start,int size) throws Exception{
		String sql = constructQuerySQL(yearMonth,project,ds,filterConditionExpression,orderByExpression,desc);
		
		return "select * from (select rownum as RRR,OT.* from ("+sql+") OT) where RRR >="+start+" and RRR < "+(start+size)+"";
	}
	
	/**
	 *  得到计数的SQL
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructCountSQL(String yearMonth,DepotProject project,Dimension []ds,
			String filterConditionExpression) throws Exception{
		
		String sql = "SELECT COUNT(*) FROM (" + constructQuerySQL(yearMonth,project,ds,filterConditionExpression) + ") AS TTTT";
		
		return sql;
	}
	
	public static String constructGranulaSelectSQL(DepotProject project,Dimension d,String granula) throws Exception{
		String tableName = project.getDimensionTableName(d);
		String gs[] = d.getAllGranula();
		String sql = "SELECT ";
		for(int i = 0 ; i < gs.length ; i++){
			sql += gs[i]+",";
			if(gs[i].equals(granula)) break;
		}
		if(sql.endsWith(",")) sql = sql.substring(0,sql.length()-1);
		sql +=" FROM " + tableName  ;
		
		String conditionStr = ""; 
		for(int i = 0 ; i < gs.length ; i++){
			if(d.getValue(gs[i]) != null){
				boolean canStartWith = false;
				boolean canMultiSelect = false;
				boolean canRangeSelect = false;
				String s = d.getValue(gs[i]);
				if(d.isCanStartWith() && s != null && s.endsWith("*")){
					s = s.substring(0,s.length()-1);
					if(s.length() > 0 && !s.endsWith("*"))
						canStartWith = true;
				}
				if(d.isCanMultiSelect() && s != null && s.indexOf(",")>0){
					canMultiSelect = true;
				}
				if(d.isCanRangeSelect() && s != null && s.indexOf("~") >= 0)
					canRangeSelect = true;
				
				if(canStartWith)
					if(conditionStr.length() == 0)
						conditionStr = "(" + gs[i] + " like '"+s+"%')";
					else
						conditionStr += " and (" + gs[i] + " like '"+s+"%')";
				else if(canMultiSelect){
					String sss[] = s.split(",");
					s="";
					for(int x = 0 ; x < sss.length ; x++){
						if(x == 0)
							s =  gs[i] + "='" + sss[x]+"'";
						else
							s = s + " or " + gs[i] + "='" + sss[x]+"'";
					}
					if(conditionStr.length() == 0)
						conditionStr += "(" + s+")";
					else
						conditionStr += " and (" + s+")";
				}else if(canRangeSelect){
					int xx = s.indexOf("~");
					String s1 = s.substring(0,xx);
					String s2 = s.substring(xx+1);
					s="";
					if(s1.length() > 0)
						s =  gs[i] +" >= '"+s1+"'";
					if(s2.length() > 0){
						if(s.length() > 0) s = s + " and ";
						s +=  gs[i] +" <= '"+s2+"'";
					}
					if(conditionStr.length() == 0)
						conditionStr += "(" + s+")";
					else
						conditionStr += " and (" + s+")";
				}else{
					if(conditionStr.length() == 0)
						conditionStr += "" +  gs[i] + "='"+d.getValue(gs[i])+"'";
					else
						conditionStr += " and " +  gs[i] + "='"+d.getValue(gs[i])+"'";
				}
				
			}
			
			if(gs[i].equals(granula)) break;
		}
		
		if(conditionStr.length() > 0)
			sql +=  " WHERE " + conditionStr;
		
		sql += " GROUP BY ";
		
		for(int i = 0 ; i < gs.length ; i++){
			sql += gs[i]+",";
			if(gs[i].equals(granula)) break;
		}
		if(sql.endsWith(",")) sql = sql.substring(0,sql.length()-1);
		
		sql += " ORDER BY ";
		
		for(int i = 0 ; i < gs.length ; i++){
			sql += gs[i]+",";
			if(gs[i].equals(granula)) break;
		}
		if(sql.endsWith(",")) sql = sql.substring(0,sql.length()-1);
		
		return sql;
	
	}
	
	/**
	 *  得到查询的SQL
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructGranulaDistinctCountSQL(String yearMonth,DepotProject project,Dimension d,String granula,Dimension []ds,String filterCondition) throws Exception{
		boolean b = false;
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i] == d || ds[i].getName().equals(d.getName())){
				b = true;
				break;
			}
		}
		if(b == false){
			Dimension ds2[] = new Dimension[ds.length+1];
			for(int i = 0 ; i < ds.length ; i++){
				ds2[i] = ds[i];
			}
			ds2[ds.length] = d;
			ds = ds2;
		}
		
		String sql = "SELECT ";
		
		ArrayList<String> markListGranulas = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String gg[] = ds[i].getGranulas();
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isListMark(gg[j])){
					markListGranulas.add(tableName+"."+gg[j]);
					sql += tableName+"."+gg[j] + ",";
				}
			}
		}
		
		sql += "count(distinct("+project.getDimensionTableName(d)+"."+granula+")) as " + granula;
		
		sql += " FROM "+project.getTableName();
		
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String gg[] = ds[i].getGranulas();
			if(gg.length > 0 ||ds[i].getName().equals(d.getName())){
				sql += " , " + tableName;
			}
		}

		String conditionStr = project.getTableName()+".YYYYMM='"+yearMonth+"'";
		if(filterCondition != null) conditionStr += " and (" +filterCondition +")";
		conditionStr += " and " + project.getTableName()+"."+d.getDimensionUniqueName()+"="+project.getDimensionTableName(d)+".autoid";
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String granulas[] = ds[i].getGranulas();
			if(granulas.length > 0)
				conditionStr += " and " + project.getTableName()+"."+ds[i].getDimensionUniqueName()+"="+tableName+".autoid";
		}
		
		
		for(int i = 0 ; i < ds.length ; i++){
			String tableName = project.getDimensionTableName(ds[i]);
			String granulas[] = ds[i].getGranulas();
			for(int j = 0 ; j < granulas.length ; j++){
				if(ds[i].getValue(granulas[j]) != null){
					boolean canStartWith = false;
					boolean canMultiSelect = false;
					boolean canRangeSelect = false;
					String s = ds[i].getValue(granulas[j]);
					if(ds[i].isCanStartWith() && s != null && s.endsWith("*")){
						s = s.substring(0,s.length()-1);
						if(s.length() > 0 && !s.endsWith("*"))
							canStartWith = true;
					}
					if(ds[i].isCanMultiSelect() && s != null && s.indexOf(",")>0){
						canMultiSelect = true;
					}
					if(ds[i].isCanRangeSelect() && s != null && s.indexOf("~") >= 0)
						canRangeSelect = true;
					
					if(canStartWith)
						conditionStr += " and ("+tableName+"." + granulas[j] + " like '"+s+"%')";
					else if(canMultiSelect){
						String sss[] = s.split(",");
						s="";
						for(int x = 0 ; x < sss.length ; x++){
							if(x == 0)
								s = tableName +"." +granulas[j] + "='" + sss[x]+"'";
							else
								s = s + " or " + tableName+"."+granulas[j] + "='" + sss[x]+"'";
						}
						conditionStr += " and (" + s+")";
					}else if(canRangeSelect){
						int xx = s.indexOf("~");
						String s1 = s.substring(0,xx);
						String s2 = s.substring(xx+1);
						s="";
						if(s1.length() > 0)
							s = tableName +"."+granulas[j] +" >= '"+s1+"'";
						if(s2.length() > 0){
							if(s.length() > 0) s = s + " and ";
							s += tableName+"."+granulas[j] +" <= '"+s2+"'";
						}
						conditionStr += " and (" + s+")";
					}else{
						conditionStr += " and " + tableName+"."+granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
					}
				
					
				}
			}
		}
	
		if(conditionStr.length() > 0)
			sql +=  " WHERE " + conditionStr;
		
		if(markListGranulas.size() > 0){
			sql += " GROUP BY ";
			for(int i = 0 ; i < markListGranulas.size() ; i++){
				if(i == markListGranulas.size() - 1){
					sql += markListGranulas.get(i) + "";
				}else{
					sql += markListGranulas.get(i) + ",";
				}
			}
		}
	
		return sql;
	}
}
