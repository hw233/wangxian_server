package com.xuanzhi.tools.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 统计工具类，用于组装各种SQL语句。
 * 
 * 包括，创建表的SQL，索引的SQL，插入的SQL，更新的SQL，基于条件和排序的查询SQL
 * 
 *
 */
public class StatisticsSQLConstructor {

	/**
	 * 构建查询结果的所有字段，这些字段包括设置了列表属性的粒度，以及对应的维度的属性，以及所有的统计项。
	 * 
	 * @param ds
	 * @param template
	 * @return
	 */
	private static HashSet<String> constructColumns(Dimension []ds, StatData template){
		HashSet<String> fieldSet = new HashSet<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String gg[] = ds[i].getGranulas();
			boolean b = false;
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isListMark(gg[j])){
					fieldSet.add(gg[j]);
					b = true;
				}
			}
			if(b){
				String names[] = ds[i].getAttachmentNames();
				for(int j = 0 ; j < names.length ; j++){
					fieldSet.add(names[j]);
				}
			}
		}
		
		String dataFields[] = template.getAllDataField();
	
		for(int i = 0 ; i < dataFields.length ;i++){
			fieldSet.add(dataFields[i]);
		}
		return fieldSet;
	}
	
	/**
	 * 构建建表的SQL语句。
	 * 其中所有的粒度类型为VARCHAR(64)，
	 * 维度属性中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型，其他为VARCHAR(64)类型
	 * 统计项中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型。
	 * 
	 * @param tableName
	 * @param ds
	 * @param template
	 * @return
	 */
	public static String constructCreateTableSQL(String tableName,Dimension []ds, StatData template,boolean hashCodeColumnEnabled){

		return constructCreateTableSQL(tableName,null,ds,template,hashCodeColumnEnabled);
	}

	/**
	 * 构建建表的SQL语句。
	 * 其中所有的粒度类型为VARCHAR(64)，
	 * 维度属性中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型，其他为VARCHAR(64)类型
	 * 统计项中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型。
	 * 
	 * @param tableName
	 * @param ds
	 * @param template
	 * @return
	 */
	public static String constructCreateTableSQL(String tableName,String engineType,Dimension []ds, StatData template,boolean hashCodeColumnEnabled){

		String sql = "CREATE TABLE "+tableName+" (\n";
		sql += "autoid INT PRIMARY KEY auto_increment,\n";
		for(int i = 0 ; i < ds.length ;i++){
			String gs[] = ds[i].getAllGranula();
			for(int j = 0 ; j < gs.length; j++){
				if(ds[i].dynamicColumn)
					sql += gs[j]+" VARCHAR("+ds[i].columnLength+") DEFAULT NULL,\n";
				else
					sql += gs[j]+" CHAR("+ds[i].columnLength+") DEFAULT NULL,\n";
			}
			String as[] = ds[i].getAttachmentNames();
			for(int j = 0 ; j < as.length; j++){
				Object o = ds[i].getAttachment(as[j]);
				if((o instanceof Integer) || (o instanceof Short) || (o instanceof Byte)){
					sql += as[j]+" INT DEFAULT 0,\n";
				}else if((o instanceof Float) || (o instanceof Double)){
					sql += as[j]+" FLOAT DEFAULT 0,\n";
				}else{
					sql += as[j]+" CHAR(64) DEFAULT NULL,\n";
				}
			}
		}
		if(hashCodeColumnEnabled){
			sql += StatisticsTool.HASHCODE + " CHAR(32) DEFAULT NULL,\n";
		}
		
		String df[] = template.getAllDataField();
		for(int i = 0 ; i < df.length ; i++){
			Number n = template.getValue(df[i]);
			if(n instanceof Float || n instanceof Double){
				sql += df[i]+" FLOAT DEFAULT 0";
			}else{
				sql += df[i]+" INT DEFAULT 0";
			}
			if(i == df.length - 1){
				if(engineType == null || engineType.trim().length() == 0)
					sql +=") DEFAULT CHARSET=GBK";
				else
					sql +=") ENGINE="+engineType+" DEFAULT CHARSET=GBK";
			}
			else
				sql +=",\n";
		}
		return sql;
	}

	public static String constructSequenceSQLForOracle(String tableName){
		return "create sequence SEQ_"+	tableName +" minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20";
	}
	
	/**
	 * 构建建表的SQL语句。
	 * 其中所有的粒度类型为VARCHAR(64)，
	 * 维度属性中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型，其他为VARCHAR(64)类型
	 * 统计项中，Integer,Short,Byte为INT类型，Float，Double为FLOAT类型。
	 * 
	 * @param tableName
	 * @param ds
	 * @param template
	 * @return
	 */
	public static String constructCreateTableSQLForOracle(String tableName,String tableSpace,Dimension []ds, StatData template,boolean hashCodeColumnEnabled){

		String sql = "CREATE TABLE "+tableName+" (\n";
		sql += "autoid NUMBER not null PRIMARY KEY ,\n";
		for(int i = 0 ; i < ds.length ;i++){
			String gs[] = ds[i].getAllGranula();
			for(int j = 0 ; j < gs.length; j++){
				sql += gs[j]+" VARCHAR2("+ds[i].columnLength+") DEFAULT NULL,\n";
			}
			String as[] = ds[i].getAttachmentNames();
			for(int j = 0 ; j < as.length; j++){
				Object o = ds[i].getAttachment(as[j]);
				if((o instanceof Integer) || (o instanceof Short) || (o instanceof Byte)){
					sql += as[j]+" NUMBER DEFAULT 0,\n";
				}else if((o instanceof Float) || (o instanceof Double)){
					sql += as[j]+" NUMBER(24,4) DEFAULT 0,\n";
				}else{
					sql += as[j]+" VARCHAR2 DEFAULT NULL,\n";
				}
			}
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
				if(tableSpace == null || tableSpace.trim().length() == 0)
					sql +=")";
				else
					sql +=") tablespace " + tableSpace;
			}
			else
				sql +=",\n";
		}
		return sql;
	}
	
	/**
	 * 创建所有的索引，每个索引为返回String数组中的一个。
	 * 这些索引包括：
	 *     各个维度中，创建第一个粒度的索引，第一个粒度和第二个粒度的索引，第一个粒度和第二个粒度和第三个粒度的索引，....
	 * 
	 * 创建索引的时候，每个粒度取前5个字符作为索引，以节约空间。
	 * 
	 * 索引的名称为：
	 *            tableName_granula1_granula2_..._INX
	 * 这里的granula1为粒度名的前5个字符。
	 * 如果索引名超过64（数据库中索引名长度的限制），那么索引名为随机的字符串。
	 *       
	 * @param tableName
	 * @param ds
	 * @return
	 */
	
	public static String[] constructCreateIndexSQLForOracle(String tableName,String tableSpace,Dimension []ds,boolean hashCodeColumnEnabled){
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ;i++){
			int num = ds[i].getNumOfGranula();
			
			String sql = "(";
			String indexName = "";
			for(int j = 0 ; j < num ; j++){
				if(j == num - 1)
					sql += ds[i].getGranula(j) + ")";
				else
					sql += ds[i].getGranula(j) + ",";
				
				if(ds[i].getGranula(j).length()>4)
					indexName +=ds[i].getGranula(j).substring(0,4)+"_";
				else
					indexName +=ds[i].getGranula(j)+"_";
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
			al.add("CREATE INDEX " + indexName+" ON "+tableName+" ("+StatisticsTool.HASHCODE+")");
		}
			
		return al.toArray(new String[0]);
		
	}
	/**
	 * 创建所有的索引，每个索引为返回String数组中的一个。
	 * 这些索引包括：
	 *     各个维度中，创建第一个粒度的索引，第一个粒度和第二个粒度的索引，第一个粒度和第二个粒度和第三个粒度的索引，....
	 * 
	 * 创建索引的时候，每个粒度取前5个字符作为索引，以节约空间。
	 * 
	 * 索引的名称为：
	 *            tableName_granula1_granula2_..._INX
	 * 这里的granula1为粒度名的前5个字符。
	 * 如果索引名超过64（数据库中索引名长度的限制），那么索引名为随机的字符串。
	 *       
	 * @param tableName
	 * @param ds
	 * @return
	 */
	
	public static String[] constructCreateIndexSQL(String tableName,Dimension []ds,boolean hashCodeColumnEnabled){
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ;i++){
			int num = ds[i].getNumOfGranula();
			
			String sql = "(";
			String indexName = "";
			for(int j = 0 ; j < num ; j++){
				if(j == num - 1)
					sql += ds[i].getGranula(j) + ")";
				else
					sql += ds[i].getGranula(j) + ",";
				
				if(ds[i].getGranula(j).length()>5)
					indexName +=ds[i].getGranula(j).substring(0,5)+"_";
				else
					indexName +=ds[i].getGranula(j)+"_";
			}
			if(indexName.length() + tableName.length() + 4 > 64){
				indexName = StringUtil.randomString(64 - 4 - tableName.length());
			}
			indexName = tableName+"_" + indexName + "INX";
			sql = "CREATE INDEX " + indexName + " ON " + tableName + sql;
			
			
			al.add(sql);
		}
		
		if(hashCodeColumnEnabled){
			al.add("CREATE INDEX " + StatisticsTool.HASHCODE+"_INX ON "+tableName+" ("+StatisticsTool.HASHCODE+")");
		}
			/*
			for(int j = 0 ; j < num ; j++){
				String sql = "(";
				String indexName = "";
				for(int k = 0 ; k <= j ; k++){
					if(k == j)
						sql += ds[i].getGranula(k) + ")";
					else
						sql += ds[i].getGranula(k) + ",";
					if(ds[i].getGranula(k).length()>5)
						indexName +=ds[i].getGranula(k).substring(0,5)+"_";
					else
						indexName +=ds[i].getGranula(k)+"_";
				}
				if(indexName.length() + tableName.length() + 4 > 64){
					indexName = StringUtil.randomString(64 - 4 - tableName.length());
				}
				indexName = tableName+"_" + indexName + "INX";
				sql = "CREATE INDEX " + indexName + " ON " + tableName + sql;
				al.add(sql);
			}
			*/
		
		return al.toArray(new String[0]);
		
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
	public static String constructCountSQL(String tableName,Dimension []ds, StatData template,
			String filterConditionExpression) throws Exception{
		
		String sql = "SELECT COUNT(*) FROM (" + constructQuerySQL(tableName,ds,template,filterConditionExpression) + ") AS TTTT";
		
		
		return sql;
	}
	
	
	/**
	 *  得到查询的SQL
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructQuerySQLWithPageOnMysql(String tableName,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByExpression,boolean desc,int start,int size) throws Exception{
		String sql = constructQuerySQL(tableName,ds,template,filterConditionExpression,orderByExpression,desc);
		
		return sql + " LIMIT " + start + " , " + size;
	}
	
	/**
	 *  得到查询的SQL
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructQuerySQLWithPageOnOracle(String tableName,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByExpression,boolean desc,int start,int size) throws Exception{
		String sql = constructQuerySQL(tableName,ds,template,filterConditionExpression,orderByExpression,desc);
		
		return "select * from (select rownum as RRR,OT.* from ("+sql+") OT) where RRR >="+start+" and RRR < "+(start+size)+"";
	}
	
	/**
	 *  得到查询的SQL
	 *  
	 * @param tableName 表名
	 * @param ds 维度信息
	 * @param template 统计数据模板
	 * @param fileterConditionExpression where条件表达式，可以是复杂的语句 可以为null
	 * @param orderByExpression 排序表达式，可以是复杂的表达式，可以为null
	 * @return
	 * @throws Exception 出现错误，比如表达式错误
	 */
	public static String constructQuerySQL(String tableName,Dimension []ds, StatData template,
			String filterConditionExpression,
			String orderByExpression,boolean desc) throws Exception{
		
		HashSet<String> allFieldSet = getAllColumns(ds,template);
		if(filterConditionExpression != null && filterConditionExpression.trim().length() > 0){
			String fields[] = BooleanExpressionUtil.checkExpression(filterConditionExpression);
			
			for(int i = 0 ; i < fields.length ;i++){
				if(allFieldSet.contains(fields[i]) == false){
					throw new Exception("field["+fields[i]+"] in filter condition expression ["+filterConditionExpression+"] is not exists in dimensions and statdata.");
				}
			}
		}
		
		if(orderByExpression != null && orderByExpression.trim().length() > 0){
			String orderSubExp[] = orderByExpression.split(",");
			String newExpression = "";
			for(int i = 0 ; i < orderSubExp.length ; i++){
				String fields[] = null;
				try{
					fields = BooleanExpressionUtil.checkExpression(orderSubExp[i]+" > 0");
				}catch(Exception e){
					throw new Exception("order by expression error for invalid expression of {" + orderSubExp[i]+"}");
				}
				for(int j = 0 ; j < fields.length ;j++){
					if(allFieldSet.contains(fields[j]) == false){
						throw new Exception("field["+fields[j]+"] in order by expression is not exists in dimensions and statdata.");
					}
				}
				
				newExpression += orderSubExp[i] + ",";
			}
			if(newExpression.endsWith(","))
				newExpression = newExpression.substring(0,newExpression.length()-1);
			
			orderByExpression = newExpression;
		}
		
		String sql = constructQuerySQL(tableName,ds,template,filterConditionExpression);
		
		//String sql = "SELECT * FROM (" + constructQuerySQL(tableName,ds,template,filterConditionExpression) + ") AS TTTT";
		
		if(orderByExpression != null && orderByExpression.trim().length() > 0){
			sql = " SELECT * FROM ("+sql+") AS TTTT ORDER BY " + orderByExpression;
			if(desc){
				sql += " DESC";
			}
		}
		
		return sql;
	}
	
	private static HashSet<String> getAllColumns(Dimension[] ds,StatData template) {
		HashSet<String> fieldSet = new HashSet<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String gg[] = ds[i].getAllGranula();
			boolean b = false;
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isGranula(gg[j])){
					fieldSet.add(gg[j]);
					b = true;
				}
			}
			if(b){
				String names[] = ds[i].getAttachmentNames();
				for(int j = 0 ; j < names.length ; j++){
					fieldSet.add(names[j]);
				}
			}
		}
		
		String dataFields[] = template.getAllDataField();
	
		for(int i = 0 ; i < dataFields.length ;i++){
			fieldSet.add(dataFields[i]);
		}
		return fieldSet;
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
	public static String constructQuerySQL(String tableName,Dimension []ds, StatData template,String filterConditionExpression) throws Exception{
		
		String sql = "SELECT ";
		
		ArrayList<String> markListGranulas = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String gg[] = ds[i].getGranulas();
			boolean b = false;
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isListMark(gg[j])){
					markListGranulas.add(gg[j]);
					b = true;
					sql += gg[j] + ",";
				}
			}
			/* for oracle  -- add by myzdf
			if(b){
				String names[] = ds[i].getAttachmentNames();
				for(int j = 0 ; j < names.length ; j++){
					sql += names[j] + ",";
				}
			}*/
		}
		
		String fields[] = template.getAllDataField();
	
		for(int i = 0 ; i < fields.length ;i++){
			sql += " sum("+fields[i]+") AS " + fields[i]+",";
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
		
		sql += " FROM "+tableName+" ";
		
		String conditionStr = "";
		for(int i = 0 ; i < ds.length ; i++){
			String granulas[] = ds[i].getGranulas();
			for(int j = 0 ; j < granulas.length ; j++){
				if(ds[i].isListMark(granulas[j]) == false){
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
					
					if(conditionStr.length() > 0){
						if(canStartWith)
							conditionStr += " and (" + granulas[j] + " like '"+s+"%')";
						else if(canMultiSelect){
							String sss[] = s.split(",");
							s="";
							for(int x = 0 ; x < sss.length ; x++){
								if(x == 0)
									s = granulas[j] + "='" + sss[x]+"'";
								else
									s = s + " or " + granulas[j] + "='" + sss[x]+"'";
							}
							conditionStr += " and (" + s+")";
						}else if(canRangeSelect){
							int xx = s.indexOf("~");
							String s1 = s.substring(0,xx);
							String s2 = s.substring(xx+1);
							s="";
							if(s1.length() > 0)
								s = granulas[j] +" >= '"+s1+"'";
							if(s2.length() > 0){
								if(s.length() > 0) s = s + " and ";
								s += granulas[j] +" <= '"+s2+"'";
							}
							conditionStr += " and (" + s+")";
						}else{
							conditionStr += " and " + granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
						}
					}
					else{
						if(canStartWith)
							conditionStr += "("+granulas[j] + " like '"+s+"%')";
						else if(canMultiSelect){
							String sss[] = s.split(",");
							s="";
							for(int x = 0 ; x < sss.length ; x++){
								if(x == 0)
									s = granulas[j] + "='" + sss[x]+"'";
								else
									s = s + " or " + granulas[j] + "='" + sss[x]+"'";
							}
							conditionStr += "(" + s + ")";
						}else if(canRangeSelect){
							int xx = s.indexOf("~");
							String s1 = s.substring(0,xx);
							String s2 = s.substring(xx+1);
							s="";
							if(s1.length() > 0)
								s = granulas[j] +" >= '"+s1+"'";
							if(s2.length() > 0){
								if(s.length() > 0) s = s + " and ";
								s += granulas[j] +" <= '"+s2+"'";
							}
							conditionStr += " (" + s+")";
						}else
							conditionStr += granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
					}
				}
			}
		}
		
				
		if(filterConditionExpression != null && filterConditionExpression.trim().length() > 0){
			
			HashSet<String> allFieldSet = getAllColumns(ds,template);
			
			fields = BooleanExpressionUtil.checkExpression(filterConditionExpression);
			for(int i = 0 ; i < fields.length ;i++){
				if(allFieldSet.contains(fields[i]) == false){
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
	
	public static String constructGranulaSelectSQL(String tableName,Dimension d,String granula,Dimension []ds) throws Exception{
		String gs[] = d.getAllGranula();
		String sql = "SELECT ";
		for(int i = 0 ; i < gs.length ; i++){
			sql += gs[i]+",";
			if(gs[i].equals(granula)) break;
		}
		if(sql.endsWith(",")) sql = sql.substring(0,sql.length()-1);
		sql +=" FROM " + tableName;
	
		String conditionStr = "";
		
		//此段程序是为了提高查询速度
		//我们是基于某天来group by某个字段的值，这样比在全表上group by要快很多
		//所以，我们假设天的字段名为day，并且日期的格式为yyyyMMdd
		for(int i = 0 ; i < ds.length ; i++){
			if(ds[i].isGranula("day") && !d.isGranula("day")){
				String value = ds[i].getValue("day");
				if(ds[i].isCanRangeSelect() && value != null && value.indexOf("~") >= 0)
					value = null;
				if(value != null && value.trim().length() > 0){
					conditionStr = "day='"+value+"'";
				}else{ //
					String s = DateUtil.formatDate(new Date(),"yyyy_MM");
					if(tableName.endsWith(s)){
						conditionStr = "day='"+DateUtil.formatDate(new Date(),"yyyyMMdd")+"'";
					}else{
						if(tableName.length() > 7){
							String yearMonth = tableName.substring(tableName.length()-7,tableName.length());
							yearMonth = yearMonth.replaceAll("_","")+"01";
							conditionStr = "day='"+yearMonth+"'";
						}
					}
				}
			}
		}
		
		
		for(int i = 0 ; i < gs.length ; i++){
			if(d.getValue(gs[i]) != null){
				if(conditionStr.length() > 0)
					conditionStr += " and " + gs[i] + "='"+d.getValue(gs[i])+"'";
				else
					conditionStr += gs[i] + "='"+d.getValue(gs[i])+"'";
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
	public static String constructGranulaDistinctCountSQL(String tableName,String granula,Dimension []ds,String filterCondition) throws Exception{
		
		String sql = "SELECT ";
		
		ArrayList<String> markListGranulas = new ArrayList<String>();
		for(int i = 0 ; i < ds.length ; i++){
			String gg[] = ds[i].getGranulas();
			for(int j = 0 ; j < gg.length ; j++){
				if(ds[i].isListMark(gg[j])){
					markListGranulas.add(gg[j]);
					sql += gg[j] + ",";
				}
			}
		}
		
		sql += "count(distinct("+granula+")) as " + granula;
		
		sql += " FROM "+tableName;
		
		String conditionStr = "";
		if(filterCondition != null) conditionStr = filterCondition;
		
		for(int i = 0 ; i < ds.length ; i++){
			String granulas[] = ds[i].getGranulas();
			for(int j = 0 ; j < granulas.length ; j++){
				if(ds[i].isListMark(granulas[j]) == false){
					if(conditionStr.length() > 0)
						conditionStr += " and " + granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
					else
						conditionStr += granulas[j] + "='"+ds[i].getValue(granulas[j])+"'";
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
