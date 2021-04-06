package com.xuanzhi.tools.simplejpa.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.xuanzhi.tools.simplejpa.impl.Utils.SimpleIndexImpl;

/**
 * 标识一个Entity
 * 每个Entity中必须包含：
 * 1. Id
 *    作为此Entity的唯一标识，在数据库中为PrimaryKey
 * 2. Version
 *    作为此Entity的版本信息，0标识新创建的Entity，>0标识已经创建的Entity，<0标识即将删除的Entity
 *    
 *
 */
public class MetaDataEntity {

	//继承关系中根目录的类
	public Class<?> cl;
	
	String sequenceName;
	String primaryTable;
	String secondTable;
	
	public MetaDataField id;
	MetaDataField version;
	String CNC = "CLASS_NAME_CNC";
	
	//添加相对应的类名的ID
	String CID = "CLASS_ID";
	
	String username;
	//添加当前的schema
	String curSchema = "";
	//所有Entity的字段，但是不包括id和version
	//但是包括所有子类的属性
	//对应数据库中所有的字段
	//key = className.field
	//存放的顺序是：
	
	ArrayList<String> secondTablesForMySql = null;
	
	ArrayList<SimpleIndexImpl> indexListForMySql = null;
	
	ArrayList<SimpleIndexImpl> indexListForOracle = null;
	
	LinkedHashMap<String,MetaDataField> map = new LinkedHashMap<String,MetaDataField>();
	
	public MetaDataField getMetaDataField(Class<?> cl,String field){
		MetaDataClass mdc = getMetaDataClassByName(cl.getName());
		if(mdc == null) return null;
		MetaDataClass p = mdc;
		while(p != null){
			MetaDataField f = map.get(p.cl.getName()+"."+field);
			if(f != null) return f;
			p = p.parent;
		}
		return null;
	}
	
	//继承关系中根目录的类
	MetaDataClass rootClass;
	
	public MetaDataClass getMetaDataClassByName(String className){
		return findMetaDataClassByName(rootClass,className);
	}
	/**
	 * 通过一个类型，获取内存中对应的MetaDataClass，如果没有返回null
	 * @param rootClass
	 * @param className
	 * @return
	 */
	public static MetaDataClass findMetaDataClassByName(MetaDataClass rootClass,String className){
		if(rootClass.cl.getName().equals(className)) return rootClass;
		for(int i = 0 ; i < rootClass.children.size(); i++){
			MetaDataClass dc = findMetaDataClassByName(rootClass.children.get(i),className);
			if(dc != null) return dc;
		}
		return null;
	}
	
	/**
	 * 通过一个在配置文件中自定义的id号 找到对应的MetaClass
	 * added  by liuyang at 2012-05-09
	 */
	public MetaDataClass getMetaDataClassByCID(Integer cid)
	{
		if(rootClass.cid.intValue() == cid.intValue())
		{
			return rootClass;
		}
		else
		{
			return findMetaDataClassByCID(rootClass, cid);
		}
	}
	
	/**
	 * 通过id号找到对应的MetaClass
	 * added by liuyang at 2012-05-09
	 */
	public static MetaDataClass findMetaDataClassByCID(MetaDataClass rootClass,Integer cid)
	{
		if(rootClass.cid.intValue() == cid.intValue())
		{
			return rootClass;
		}
		else
		{
			for(int i = 0 ; i < rootClass.children.size(); i++){
				MetaDataClass dc = findMetaDataClassByCID(rootClass.children.get(i),cid);
				if(dc != null) return dc;
			}
			return null;
		}
	} 
	
	/**
	 * 判断某个属性是否属于某个类，属性属于某个类是指此属性是此类声明的，或者是此类的父类声明的
	 * @param c
	 * @param f
	 * @return
	 */
	public static boolean contains(MetaDataClass c,MetaDataField f){
		MetaDataClass p = c;
		while(p != null){
			if(p == f.mdc) return true;
			p = p.parent;
		}
		return false;
	}
	

	String select_object_by_id_sql = null;
	
	String insert_object_to_primary_sql = null;
	
	String insert_object_to_second_sql = null;
	
	String remove_object_from_primary_sql = null;
	
	String remove_object_from_second_sql = null;
	
	//组装查询的sql
	public String getSqlForSelectObjectById(){
		
		if(select_object_by_id_sql != null){
			return select_object_by_id_sql;
		}
		//标识副表不存在
		if(secondTable == null){
			StringBuffer sql = new StringBuffer();

			sql.append("select * from " + username + "."   + primaryTable + " where " + id.columnNames[0] + " = ?");
			select_object_by_id_sql = sql.toString();
			
			return select_object_by_id_sql;
		}else{
			StringBuffer sql = new StringBuffer();
			
			sql.append("select T1.*,T2.* from "  + username + "." +primaryTable+" T1 left join "+ username + "."+secondTable+" T2 on T1."+id.columnNames[0]+"=T2."+id.columnNames[0]+"_SEC where T1."+id.columnNames[0]+"=?");
			
			select_object_by_id_sql = sql.toString();
			
			return select_object_by_id_sql;
			
		}
	}
	
	//组装查询的sql
	public String getSqlForSelectObjectByIdInMysql(MysqlSection section){
		//标识副表不存在
		if(this.secondTablesForMySql.size() == 0){
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from " + primaryTable +"_"+section.getName()+ " where " + id.columnNameForMysql + " = ?");
			return sql.toString();
		}else{
			StringBuffer sql = new StringBuffer();

			sql.append("select T0.*");
			for(int i = 0 ; i < this.secondTablesForMySql.size() ; i++){
				sql.append(",T"+(i+1)+".*");
			}
			sql.append(" from "+primaryTable+"_"+section.getName()+" T0 ");
			for(int i = 0 ; i < this.secondTablesForMySql.size() ; i++){
				String secondTable = secondTablesForMySql.get(i);
				sql.append("left join "+secondTable+"_"+section.getName()+" T"+(i+1)+" on T0."+id.columnNameForMysql+"=T"+(i+1)+"."+id.columnNameForMysql+"_SEC_"+(i+1)+" ");
			}
			sql.append(" where T0."+id.columnNameForMysql+"=?");
			
			return sql.toString();

			
		}
	}
	
	public String getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTable(Class<?> cl,String fields[]){
		StringBuffer sql = new StringBuffer();
		sql.append("update " + username + "." + this.primaryTable + " set ");
		boolean needSet = false;
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField f = this.getMetaDataField(cl, fields[i]);
			if(f == null){
				throw new java.lang.IllegalArgumentException("["+fields[i]+"] is not a modified field of class["+cl.getName()+"]");
			}
			if(f.inPrimaryTable){
				if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
					sql.append(f.columnNames[0] + "=to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
				}else{
					sql.append(f.columnNames[0] + "=?,");
				}
				needSet = true;
			}
		}
		if(needSet == false) return null;
		sql.append(version.columnNames[0]+"="+version.columnNames[0]+"+1");
		
		sql.append(" where " + id.columnNames[0] +"=?");
		
		return sql.toString();
	}
	
	public String getSqlForUpdateOneObjectByModifiedFieldsOnSecondTable(Class<?> cl,String fields[]){
		if(this.secondTable == null) return null;
		StringBuffer sql = new StringBuffer();
		sql.append("update " + username + "."+ this.secondTable + " set ");
		boolean needSet = false;
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField f = this.getMetaDataField(cl, fields[i]);
			if(f == null){
				throw new java.lang.IllegalArgumentException("["+fields[i]+"] is not a field of class["+cl.getName()+"]");
			}
			if(f.inPrimaryTable == false){

				for(int j = 0 ; j < f.columnNames.length ; j++){
					sql.append(f.columnNames[j]+"=?,");
				}
				needSet = true;
			}
		}
		if(needSet == false) return null;
		
		String s = sql.toString();
		s = s.substring(0,s.length()-1);
		sql = new StringBuffer();
		sql.append(s);
		
		sql.append(" where " + id.columnNames[0] +"_SEC=?");
		
		return sql.toString();
	}
	
	/**
	 * 以下方法是拼装在mysql表中执行update操作的sql语句方法
	 * 将oracle 特有的to_date方法改为mysql中的str_to_date方法
	 * added by liuyang at 2012-04-26
	 */
	public String getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTableInMysql(MysqlSection section,Class<?> cl,String fields[]){
		StringBuffer sql = new StringBuffer();
		sql.append("update " + this.primaryTable+"_"+section.getName() + " set ");
		boolean needSet = false;
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField f = this.getMetaDataField(cl, fields[i]);
			if(f == null){
				throw new java.lang.IllegalArgumentException("["+fields[i]+"] is not a modified field of class["+cl.getName()+"]");
			}
			if(f.inPrimaryTable){
				if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
					sql.append(f.columnNames[0] + "=str_to_date(?,'%Y-%m-%d %H:%i:%S'),");
				}else{
					sql.append(f.columnNames[0] + "=?,");
				}
				needSet = true;
			}
		}
		if(needSet == false) return null;
		sql.append(version.columnNameForMysql+"="+version.columnNameForMysql+"+1");
		
		sql.append(" where " + id.columnNameForMysql +"=?");
		
		return sql.toString();
	}
	
	public String[] getSqlForInsertOneObjectByModifiedFieldsOnSecondTableInMysql(MysqlSection section,Class<?> cl,String fields[]){
		String sqls[] = new String[secondTablesForMySql.size()];
		for(int i = 0 ; i < this.secondTablesForMySql.size() ;i++){
			String secondTable = secondTablesForMySql.get(i);
			ArrayList<MetaDataField> al = null;
			for(int j = 0 ; j < fields.length ; j++){
				MetaDataField f = getMetaDataField(cl, fields[j]);
				if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
					if(al == null) al = new ArrayList<MetaDataField>();
					al.add(f);
				}
			}
			
			if(al != null && al.size() > 0){
				StringBuffer sql = new StringBuffer();
				sql.append("insert into " + secondTable+"_"+section.getName() + "(");
				for(int j = 0 ; j < al.size() ; j++){
					MetaDataField f = al.get(j);
					sql.append(f.columnNameForMysql+",");
				}
				sql.append(id.columnNameForMysql +"_SEC_"+(i+1) + ") values (");
				for(int j = 0 ; j < al.size() ; j++){
					sql.append("?,");
				}
				sql.append("?)");
				
				sqls[i] = sql.toString();
			}		
		}
		
		return sqls;
	}
	
	public String[] getSqlForUpdateOneObjectByModifiedFieldsOnSecondTableInMysql(MysqlSection section,Class<?> cl,String fields[]){
		String sqls[] = new String[secondTablesForMySql.size()];
		for(int i = 0 ; i < this.secondTablesForMySql.size() ;i++){
			String secondTable = secondTablesForMySql.get(i);
			ArrayList<MetaDataField> al = null;
			for(int j = 0 ; j < fields.length ; j++){
				MetaDataField f = getMetaDataField(cl, fields[j]);
				if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
					if(al == null) al = new ArrayList<MetaDataField>();
					al.add(f);
				}
			}
			
			if(al != null && al.size() > 0){
				StringBuffer sql = new StringBuffer();
				sql.append("update " + secondTable+"_"+section.getName() + " set ");
				for(int j = 0 ; j < al.size() ; j++){
					MetaDataField f = al.get(j);
					sql.append(f.columnNameForMysql+"=?,");
				}
				
				String s = sql.toString();
				s = s.substring(0,s.length()-1);
				sql = new StringBuffer();
				sql.append(s);
				
				sql.append(" where " + id.columnNameForMysql +"_SEC_"+(i+1)+"=?");
				
				sqls[i] = sql.toString();
			}		
		}
		
		return sqls;
	}
	
	
	/**
	 * 
	 */
	public String getSqlForSelectObjectsByNativeConditions(String whereSql,String orderBy){
		//标识副表不存在
		if(secondTable == null){
			StringBuffer sql = new StringBuffer();
//			sql.append("select " + id.columnNames[0]+",");
//			
//			Iterator<String> it = map.keySet().iterator();
//			while(it.hasNext()){
//				String field = it.next();
//				MetaDataField f = map.get(field);
//				if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
//					sql.append("to_char("+f.columnNames[0]+",'yyyy-mm-dd hh24:mi:ss'),");
//				}else{
//					sql.append(f.columnNames[0]+",");
//				}
//			}
//			sql.append(version.columnNames[0]+","+CNC);
//			
//			sql.append(" from " + primaryTable);
		
			sql.append("select * from " + username + "." + primaryTable);
			if(whereSql.length() > 0){
				sql.append(" where " + whereSql);
			}
			if(orderBy != null && orderBy.length() > 0){
				sql.append(" order by "  + orderBy);
			}
			
			return sql.toString();
			
		}else{
			StringBuffer sql = new StringBuffer();
			
//			sql.append("select " + id.columnNames[0]+",");
//			Iterator<String> it = map.keySet().iterator();
//			while(it.hasNext()){
//				String field = it.next();
//				MetaDataField f = map.get(field);
//				if(f.inPrimaryTable){
//					if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
//						sql.append("to_char("+f.columnNames[0]+",'yyyy-mm-dd hh24:mi:ss') as "+f.columnNames[0]+",");
//					}else{
//						sql.append(f.columnNames[0]+",");
//					}
//				}else{
//					//
//					//for(int i = 0 ; i < f.columnNames.length ; i++){
//					//	sql.append(secondTable+"."+f.columnNames[i]+",");
//					//}
//				}
//			}
//			sql.append(version.columnNames[0]+","+CNC);
//			sql.append(" from " + primaryTable);
			
			sql.append("select * from "+ username + "." + primaryTable);
			
			if(whereSql.length() > 0){
				sql.append(" where " + whereSql);
			}
			if(orderBy.length() > 0){
				sql.append(" order by "  + orderBy);
			}
			
			StringBuffer newSql = new StringBuffer();
//			newSql.append("select T."+id.columnNames[0]+",");
//			it = map.keySet().iterator();
//			while(it.hasNext()){
//				String field = it.next();
//				MetaDataField f = map.get(field);
//				if(f.inPrimaryTable){
//					newSql.append("T."+f.columnNames[0]+",");
//				}else{
//					for(int i = 0 ; i < f.columnNames.length ; i++){
//						newSql.append(secondTable+"."+f.columnNames[i]+",");
//					}
//				}
//			}
//			newSql.append("T."+version.columnNames[0]+",T."+CNC);
			
			newSql.append(" select T1.*,T2.* from ( "+sql+" ) T1 left join "+ username + "." + secondTable + " T2 on T1."+id.columnNames[0] + " = T2."+id.columnNames[0]+"_SEC");
			
			return newSql.toString();
		}
	}
	
	public String getSqlForSelectObjectsByNativeConditionsInMysql(MysqlSection section,String whereSql,String orderBy,long size,boolean paramFlag){
		//标识副表不存在
		if(this.secondTablesForMySql.size() == 0){
			StringBuffer sql = new StringBuffer();
		
			sql.append("select * from "+ primaryTable+"_"+section.getName());
			if(whereSql.length() > 0){
				sql.append(" where " + whereSql);
			}
			if(orderBy != null && orderBy.length() > 0){
				sql.append(" order by "  + orderBy);
			}
			
			if(paramFlag){
				sql.append(" limit 0,?");
			}else{
				sql.append(" limit 0,"+size+"");
			}
			
			return sql.toString();
			
		}else{
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from " + primaryTable+"_"+section.getName());
			
			if(whereSql.length() > 0){
				sql.append(" where " + whereSql);
			}
			if(orderBy != null && orderBy.length() > 0){
				sql.append(" order by "  + orderBy);
			}
			if(paramFlag){
				sql.append(" limit 0,?");
			}else{
				sql.append(" limit 0,"+size+"");
			}
			
			StringBuffer newSql = new StringBuffer();
			newSql.append("select T0.*");
			for(int i = 0 ; i < this.secondTablesForMySql.size() ; i++){
				newSql.append(",T"+(i+1)+".*");
			}
			newSql.append(" from ("+sql+") T0 ");
			for(int i = 0 ; i < this.secondTablesForMySql.size() ; i++){
				String secondTable = secondTablesForMySql.get(i);
				newSql.append("left join "+secondTable+"_"+section.getName()+" T"+(i+1)+" on T0."+id.columnNameForMysql+"=T"+(i+1)+"."+id.columnNameForMysql+"_SEC_"+(i+1)+" ");
			}
			
			return newSql.toString();
		}
	}
	
	
	public String getSqlForInsertObjectToPrimaryTable(){
		if(insert_object_to_primary_sql != null){
			return insert_object_to_primary_sql;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into "+ username + "." + primaryTable+"(");
		sql.append(id.columnNames[0]);
		sql.append(","+version.columnNames[0]+","+CNC + "," + CID);
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				sql.append(","+ f.columnNames[0]);
			}
		}
		sql.append(") values (");
		sql.append("?");
		sql.append(",?,?");
		//设置CID的值
		sql.append(",?");
		it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
					sql.append(",to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				}else{
					sql.append(",?");
				}
			}
		}
		sql.append(")");
		insert_object_to_primary_sql = sql.toString();
		return insert_object_to_primary_sql;
	}
	
	public String getSqlForInsertObjectToSecondTable(){
		if(insert_object_to_second_sql != null){
			return insert_object_to_second_sql;
		}
		if(this.secondTable == null) return null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into "+ username + "." + secondTable+"(");
		sql.append(id.columnNames[0]+"_SEC");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable == false){
				for(int i = 0 ; i < f.columnNames.length ; i++){
					sql.append(","+ f.columnNames[i]);
				}
			}
		}
		sql.append(") values (");
		sql.append("?");
		it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable == false){
				for(int i = 0 ; i < f.columnNames.length ; i++){
					sql.append(",?");
				}
			}
		}
		sql.append(")");
		insert_object_to_second_sql = sql.toString();
		return insert_object_to_second_sql;
		
	}
	
	/**
	 * 以下方法是在mysql表中 插入数据
	 * added by liuyang at 2012-04-26
	 * @return
	 */
	public String getSqlForInsertObjectToPrimaryTableInMysql(MysqlSection section){

		StringBuffer sql = new StringBuffer();
		sql.append("insert into " + primaryTable+"_"+section.getName()+"(");
		sql.append(id.columnNameForMysql);
		//sql.append(","+version.columnNameForMysql+","+CNC);
		sql.append(","+version.columnNameForMysql+","+CNC + "," + CID);
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				sql.append(","+ f.columnNameForMysql);
			}
		}
		sql.append(") values (");
		sql.append("?");
		sql.append(",?,?");
		//设置CID的值
		sql.append(",?");
		it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				if(f.field.getType() == java.util.Date.class || f.field.getType() == java.sql.Date.class){
					sql.append(",str_to_date(?,'%Y-%m-%d %H:%i:%S')");
				}else{
					sql.append(",?");
				}
			}
		}
		sql.append(")");
		return sql.toString();
	}
	
	
	public String[] getSqlForInsertObjectToSecondTableInMysql(MysqlSection section){
		
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < secondTablesForMySql.size() ; i++){
			String secondTable = secondTablesForMySql.get(i);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into " + secondTable+"_"+section.getName()+"(");
			sql.append(id.columnNameForMysql+"_SEC_"+(i+1));
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = map.get(field);
				if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
					sql.append("," + f.columnNameForMysql);
				}
			}
			sql.append(") values (");
			sql.append("?");
			it = map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = map.get(field);
				if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
					sql.append(",?");
				}
			}
			sql.append(")");
			
			al.add(sql.toString());
		}
		return al.toArray(new String[0]);
	}
	
	public String getSqlForRemoveObjectFromPrimaryTable(){
		if(remove_object_from_primary_sql != null){
			return remove_object_from_primary_sql;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from "+ username + "." + primaryTable+" where "+id.columnNames[0]+" = ? ");
		remove_object_from_primary_sql = sql.toString();
		return remove_object_from_primary_sql;
	}
	
	public String getSqlForRemoveObjectFromSecondTable(){
		if(remove_object_from_second_sql != null){
			return remove_object_from_second_sql;
		}
		
		if(this.secondTable == null) return null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from "+ username + "." + secondTable+" where "+id.columnNames[0]+"_SEC = ? ");
		remove_object_from_second_sql = sql.toString();
		return remove_object_from_second_sql;
	}
	
	public String getSqlForBatchRemoveObjectFromPrimaryTable(){
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from "+ username + "." + primaryTable+" where "+id.columnNames[0]+" >= ? and " + id.columnNames[0]+ "<= ?" );
		return sql.toString();
	}
	
	public String getSqlForBatchRemoveObjectFromSecondTable(){
		
		if(this.secondTable == null) return null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from "+ username + "." + secondTable+" where "+id.columnNames[0]+"_SEC >= ?  and " + id.columnNames[0]+ "_SEC  <= ?");
		return sql.toString();
	}
	
	
	public String getSqlForBatchRemoveObjectFromPrimaryTable(List<Long> ids){
		
		StringBuffer sql = new StringBuffer();
		String where = "";
		for(int i=0; i<ids.size(); i++)
		{
			if(i == 0)
			{
				where += id.columnNames[0]+" = ? ";
			}
			else
			{
				where += " or " + id.columnNames[0]+" = ? ";
			}
			
		}
		
		sql.append("delete from "+ username + "." + primaryTable+" where "+where );
			
		return sql.toString();
	}
	
	public String getSqlForBatchRemoveObjectFromSecondTable(List<Long> ids){
		
		if(this.secondTable == null) return null;
		
		StringBuffer sql = new StringBuffer();
		
		String where = "";
		for(int i=0; i<ids.size(); i++)
		{
			if(i == 0)
			{
				where += id.columnNames[0]+"_SEC = ? ";
			}
			else
			{
				where += " or " + id.columnNames[0]+"_SEC = ? ";
			}
			
		}
		
		sql.append("delete from "+ username + "." + secondTable+" where "+where);
			
		return sql.toString();
	}
	
	
	
	/**
	 * 以下方法是拼装从mysql数据库表中删除记录的语句
	 * added by liuyang
	 * @param f
	 * @return
	 */
	public String getSqlForRemoveObjectFromPrimaryTableInMysql(MysqlSection section){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from " + primaryTable+"_"+section.getName()+" where "+id.columnNameForMysql+" = ? ");
		return sql.toString();
	}
	
	public String[] getSqlForRemoveObjectFromSecondTableInMysql(MysqlSection section){
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < this.secondTablesForMySql.size() ; i++){
			String secondTable = secondTablesForMySql.get(i);
			StringBuffer sql = new StringBuffer();
			sql.append("delete from " + secondTable+"_"+section.getName()+" where "+id.columnNameForMysql+"_SEC_"+(i+1)+" = ? ");
			al.add(sql.toString());
		}
		return al.toArray(new String[0]);
		
	}
	
	public String getSqlForAddColumnInPrimaryTable(MetaDataField f ){
		if(f == null || f.inPrimaryTable == false) return null;
		StringBuffer sql = new StringBuffer();
		sql.append("alter table "+ username + "."+primaryTable+" add ");
		Class<?> clazz = f.field.getType();
		if(clazz == Boolean.TYPE || clazz == Boolean.class){
			sql.append(""+f.columnNames[0]+" CHAR(1) DEFAULT NULL,\n");
		}else if(clazz == Byte.TYPE || clazz == Byte.class){
			sql.append(""+f.columnNames[0]+" NUMBER(4,0),\n");
		}else if(clazz == Short.TYPE || clazz == Short.class){
			sql.append(""+f.columnNames[0]+" NUMBER(6,0),\n");
		}else if(clazz == Integer.TYPE || clazz == Integer.class){
			sql.append(""+f.columnNames[0]+" NUMBER(19,0),\n");
		}else if(clazz == Character.TYPE || clazz == Character.class){
			sql.append(""+f.columnNames[0]+" CHAR(1) DEFAULT NULL,\n");
		}else if(clazz == Long.TYPE || clazz == Long.class){
			sql.append(""+f.columnNames[0]+" NUMBER(38,0),\n");
		}else if(clazz == Float.TYPE || clazz == Float.class){
			int precision = 19;
			if(f.simpleColumn.precision() > 0){
				precision = f.simpleColumn.precision();
			}
			int scale = 6;
			if(f.simpleColumn.scale() > 0){
				scale = f.simpleColumn.scale();
			}
			sql.append(""+f.columnNames[0]+" NUMBER("+precision+","+scale+"),\n");
		}else if(clazz == Double.TYPE || clazz == Double.class){
			int precision = 38;
			if(f.simpleColumn.precision() > 0){
				precision = f.simpleColumn.precision();
			}
			int scale = 10;
			if(f.simpleColumn.scale() > 0){
				scale = f.simpleColumn.scale();
			}
			sql.append(""+f.columnNames[0]+" NUMBER("+precision+","+scale+"),\n");
		}else if(clazz == String.class){
			int len = f.simpleColumn.length();
			if(len <= 0) len = 4000;
			if(len > 4000) len = 4000;
			sql.append(""+f.columnNames[0]+" VARCHAR2("+len+"),\n");
		}else if(clazz == java.util.Date.class || clazz == java.sql.Date.class){
			sql.append(""+f.columnNames[0]+" DATE,\n");
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		return s;
	}
	
	public String getSqlForAddColumnInSecondTable(MetaDataField f ,String columnName){
		if(f== null || f.inPrimaryTable) return null;
		StringBuffer sql = new StringBuffer();
		sql.append("alter table "+ username + "."+this.secondTable+" add " + columnName + " VARCHAR(4000)");
		return sql.toString();
	}
	
	/**
	 * 以下的方法是用来产生在mysql数据库中为表添加列的SQL
	 * @return
	 * @author liuyang 
	 *added by liuyang at 2012-04-25
	 */
	public String getSqlForAddColumnInPrimaryTableInMysql(MysqlSection seciton,MetaDataField f ){
		if(f == null || f.inPrimaryTable == false) return null;
		StringBuffer sql = new StringBuffer();
		sql.append("alter table "+primaryTable+"_"+seciton.getName()+" add ");
		Class<?> clazz = f.field.getType();
		if(clazz == Boolean.TYPE || clazz == Boolean.class){
			sql.append(""+f.columnNameForMysql+" CHAR(1) DEFAULT NULL,\n");
		}else if(clazz == Byte.TYPE || clazz == Byte.class){
			sql.append(""+f.columnNameForMysql+" DECIMAL(4,0),\n");
		}else if(clazz == Short.TYPE || clazz == Short.class){
			sql.append(""+f.columnNameForMysql+" DECIMAL(6,0),\n");
		}else if(clazz == Integer.TYPE || clazz == Integer.class){
			sql.append(""+f.columnNameForMysql+" DECIMAL(19,0),\n");
		}else if(clazz == Character.TYPE || clazz == Character.class){
			sql.append(""+f.columnNameForMysql+" CHAR(1) DEFAULT NULL,\n");
		}else if(clazz == Long.TYPE || clazz == Long.class){
			sql.append(""+f.columnNameForMysql+" DECIMAL(38,0),\n");
		}else if(clazz == Float.TYPE || clazz == Float.class){
			int precision = 19;
			if(f.simpleColumn.precision() > 0){
				precision = f.simpleColumn.precision();
			}
			int scale = 6;
			if(f.simpleColumn.scale() > 0){
				scale = f.simpleColumn.scale();
			}
			sql.append(""+f.columnNameForMysql+" DECIMAL("+precision+","+scale+"),\n");
		}else if(clazz == Double.TYPE || clazz == Double.class){
			int precision = 38;
			if(f.simpleColumn.precision() > 0){
				precision = f.simpleColumn.precision();
			}
			int scale = 10;
			if(f.simpleColumn.scale() > 0){
				scale = f.simpleColumn.scale();
			}
			sql.append(""+f.columnNameForMysql+" DECIMAL("+precision+","+scale+"),\n");
		}else if(clazz == String.class){
			int len = f.simpleColumn.length();
			if(len <= 0) len = 4000;
			if(len > 4000) len = 4000;
			sql.append(""+f.columnNameForMysql+" VARCHAR("+len+"),\n");
		}else if(clazz == java.util.Date.class || clazz == java.sql.Date.class){
			sql.append(""+f.columnNameForMysql+" DATETIME,\n");
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		return s;
	}
	
	public String getSqlForAddColumnInSecondTableInMysql(MysqlSection section,MetaDataField f){
		if(f== null || f.inPrimaryTable) return null;
		StringBuffer sql = new StringBuffer();
		sql.append("alter table "+f.tableNameForMysql+"_"+section.getName()+" add " + f.columnNameForMysql + " TEXT("+f.simpleColumn.length()+")");
		return sql.toString();
	}
	
	public String getSqlForCreatePrimaryTable(){
		StringBuffer sql = new StringBuffer();
		sql.append("create table "+ username + "." + this.primaryTable + " (\n");
		sql.append(""+id.columnNames[0]+" NUMBER(38,0) not null PRIMARY KEY ,\n");
		sql.append(""+version.columnNames[0]+" NUMBER(19,0) not null,\n");
		sql.append(""+CNC+" VARCHAR2(256),\n");
		//添加一个字段标明为在配置文件中定义的类标识
		sql.append(""+CID+" NUMBER(20),\n");
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				Class<?> clazz = f.field.getType();
				if(clazz == Boolean.TYPE || clazz == Boolean.class){
					sql.append(""+f.columnNames[0]+" CHAR(1) DEFAULT NULL,\n");
				}else if(clazz == Byte.TYPE || clazz == Byte.class){
					sql.append(""+f.columnNames[0]+" NUMBER(4,0),\n");
				}else if(clazz == Short.TYPE || clazz == Short.class){
					sql.append(""+f.columnNames[0]+" NUMBER(6,0),\n");
				}else if(clazz == Integer.TYPE || clazz == Integer.class){
					sql.append(""+f.columnNames[0]+" NUMBER(19,0),\n");
				}else if(clazz == Character.TYPE || clazz == Character.class){
					sql.append(""+f.columnNames[0]+" CHAR(1) DEFAULT NULL,\n");
				}else if(clazz == Long.TYPE || clazz == Long.class){
					sql.append(""+f.columnNames[0]+" NUMBER(38,0),\n");
				}else if(clazz == Float.TYPE || clazz == Float.class){
					int precision = 19;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 6;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					sql.append(""+f.columnNames[0]+" NUMBER("+precision+","+scale+"),\n");
				}else if(clazz == Double.TYPE || clazz == Double.class){
					int precision = 38;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 10;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					sql.append(""+f.columnNames[0]+" NUMBER("+precision+","+scale+"),\n");
				}else if(clazz == String.class){
					int len = f.simpleColumn.length();
					if(len <= 0) len = 4000;
					if(len > 4000) len = 4000;
					sql.append(""+f.columnNames[0]+" VARCHAR2("+len+"),\n");
				}else if(clazz == java.util.Date.class || clazz == java.sql.Date.class){
					sql.append(""+f.columnNames[0]+" DATE,\n");
				}
			}
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		s += ")";
		return s;
	}
		
	public String getSqlForCreateSecondTable(){
		if(this.secondTable == null) return null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("create table "+ username + "." + this.secondTable + "(\n");
		sql.append(""+id.columnNames[0]+"_SEC NUMBER(38,0) not null PRIMARY KEY ,\n");
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable == false){
				for(int i = 0 ; i < f.columnNames.length ; i++){
					sql.append(""+f.columnNames[i]+" VARCHAR(4000) DEFAULT NULL,\n");
				}
			}
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		s += ")";
		return s;
	}
	
	
	/**
	 * 以下方法是产生在Mysql建表的SQL
	 * Mysql的表名在linux下是区分大小写的 故这里表名统一为大写
	 * 同时 由于mysql对于字段名称也区分大小写 故这里为了java方便查询比较  java命为什么名称则列名为什么名称 
	 * 无法固定varchar长度的采用 text文本类型 
	 * text和varchar 本质是一样的 对于innodb引擎 只有前768字节被保存在表中其余的分散在各个块儿上
	 * added by liuyang at 2012-04-25
	 * 
	 * 
	 * 增加了创建表的此段的顺序。
	 * 因为目前mysql默认实现会根据行数拆分表。然后查询的时候用union all 合并多个表的结果集。
	 * 这里假设了表的结构是一样的，所谓的结构是指所有字段的顺序都是一样的。
	 * 
	 * 但是我们在实际运营中可能会增加字段，这样对于老表，新的字段就会在表的最后面，
	 * 但是新建的表，对于新加的字段，可能在表中间。就会出现表结构不一样。
	 * 导致最终查询出现错误。
	 * 解决这个问题的办法是：在创建新表的时候，把上一个分区的表字段顺序传递进来。
	 * 新表按照老表的字段顺序创建表  ----- myzdf 2013-01-28
	 * @return
	 */
	
	public String getSqlForCreatePrimaryTableInMysql(MysqlSection section,ArrayList<String> previousSectionTableColumnList){
		StringBuffer sql = new StringBuffer();
		sql.append("create table " + this.primaryTable.toUpperCase()+"_" +section.getName() + " (\n");
		sql.append(""+id.columnNameForMysql.toUpperCase()+" BIGINT not null PRIMARY KEY,\n");
		sql.append(""+version.columnNameForMysql+" DECIMAL(19,0) not null,\n");
		sql.append(""+CNC+" VARCHAR(256) ,\n");
		
		//添加一个字段标明为在配置文件中定义的类标识
		sql.append(""+CID+" BIGINT,\n");
		
		ArrayList<String> fieldList = new ArrayList<String>();
		//按照老表的顺序组织新表中，字段的顺序
		for(String columnNameForMysql : previousSectionTableColumnList){
			String mf = null;
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = map.get(field);
				if(f.columnNameForMysql.equals(columnNameForMysql)){
					mf = field;
				}
			}
			if(mf != null){
				fieldList.add(mf);
			}
		}
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			if(!fieldList.contains(field)){
				fieldList.add(field);
			}
		}
		
		for(String field : fieldList ){
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable){
				Class<?> clazz = f.field.getType();
				if(clazz == Boolean.TYPE || clazz == Boolean.class){
					//这里需要注意，mysql中若同一数据表中存在varchar字段，则char字段自动变为varchar
					sql.append(""+f.columnNameForMysql+" CHAR(1) DEFAULT NULL,\n");
				}else if(clazz == Byte.TYPE || clazz == Byte.class){
					sql.append(""+f.columnNameForMysql+" DECIMAL(4,0),\n");
				}else if(clazz == Short.TYPE || clazz == Short.class){
					sql.append(""+f.columnNameForMysql+" DECIMAL(6,0),\n");
				}else if(clazz == Integer.TYPE || clazz == Integer.class){
					sql.append(""+f.columnNameForMysql+" DECIMAL(19,0),\n");
				}else if(clazz == Character.TYPE || clazz == Character.class){
					sql.append(""+f.columnNameForMysql+" CHAR(1) DEFAULT NULL,\n");
				}else if(clazz == Long.TYPE || clazz == Long.class){
					sql.append(""+f.columnNameForMysql+" DECIMAL(38,0),\n");
				}else if(clazz == Float.TYPE || clazz == Float.class){
					int precision = 19;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 6;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					sql.append(""+f.columnNameForMysql+" DECIMAL("+precision+","+scale+"),\n");
				}else if(clazz == Double.TYPE || clazz == Double.class){
					int precision = 38;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 10;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					sql.append(""+f.columnNameForMysql+" DECIMAL("+precision+","+scale+"),\n");
				}else if(clazz == String.class){
					int len = f.simpleColumn.length();
					//mysql 对于无法确定的字符串长度，采用text类型   故len可保持不变
					if(len <= 0) len = 4000;
					if(len > 4000) len = 4000;
					sql.append(""+f.columnNameForMysql+" VARCHAR("+len+"),\n");
				}else if(clazz == java.util.Date.class || clazz == java.sql.Date.class){
					/**
					 * 这里oracle 的Date 其实对应的是mysql 的DateTime类型 故这里修改为DateTime类型
					 * mysql的Date类型默认格式为YYYY-MM-DD 故不采用
					 */
					sql.append(""+f.columnNameForMysql+" DATETIME,\n");
				}
			}
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		s += ")";
		//默认优先采用innodb的存储格式 因为这种格式目前是mysql最优秀的格式 同时也支持事务
		s += "ENGINE=InnoDB";
		return s;
	}
	/**
	 * 这里mysql 的行的长度是有限制的 由于一行不包括text之类的类型的整体长度 为65535  如果使用varchar(4000) 那么等于一个列占用4000*3（utf8环境下）= 12000字节
	 * 那么如果列数多了 就会报错 故这里采用text类型
	 * 
	 *  * 增加了创建表的此段的顺序。
	 * 因为目前mysql默认实现会根据行数拆分表。然后查询的时候用union all 合并多个表的结果集。
	 * 这里假设了表的结构是一样的，所谓的结构是指所有字段的顺序都是一样的。
	 * 
	 * 但是我们在实际运营中可能会增加字段，这样对于老表，新的字段就会在表的最后面，
	 * 但是新建的表，对于新加的字段，可能在表中间。就会出现表结构不一样。
	 * 导致最终查询出现错误。
	 * 解决这个问题的办法是：在创建新表的时候，把上一个分区的表字段顺序传递进来。
	 * 新表按照老表的字段顺序创建表  ----- myzdf 2013-01-28
	 * @return
	 */
	public String getSqlForCreateSecondTableInMysql(MysqlSection section,int secondTableIndex,String secondTableName,ArrayList<String> previousSectionTableColumnList){
		if(secondTableName == null) return null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("create table " + secondTableName +"_" + section.getName() + "(\n");
		sql.append(""+id.columnNameForMysql+"_SEC_"+secondTableIndex+" BIGINT not null PRIMARY KEY  ,\n");
		
		
		ArrayList<String> fieldList = new ArrayList<String>();
		//按照老表的顺序组织新表中，字段的顺序
		for(String columnNameForMysql : previousSectionTableColumnList){
			String mf = null;
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = map.get(field);
				if(f.columnNameForMysql.equals(columnNameForMysql) && f.tableNameForMysql.equals(secondTableName)){
					mf = field;
				}
			}
			if(mf != null){
				fieldList.add(mf);
			}
		}
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			if(!fieldList.contains(field)){
				fieldList.add(field);
			}
		}
		
		for(String field : fieldList){
			MetaDataField f = map.get(field);
			if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTableName)){
				//for(int i = 0 ; i < f.columnNames.length ; i++){
				//	sql.append(""+f.columnNames[i]+" TEXT(4000) DEFAULT NULL,\n");
				//}
				sql.append(f.columnNameForMysql + " TEXT("+f.simpleColumn.length()+") DEFAULT NULL,\n");
			}
		}
		String s = sql.toString();
		s = s.substring(0,s.length()-2);
		s += ")";
		s += "ENGINE=InnoDB";
		return s;
	}
}
