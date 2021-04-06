package com.xuanzhi.tools.simplejpa.impl;

import java.io.InputStream;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl;
import com.xuanzhi.tools.text.XmlUtil;

public class SimpleEntityManagerFactoryHelper {

	public static int getServerId(){
		return SimpleEntityManagerFactoryImpl.serverId;
	}
	public static String getServerName(){
		return SimpleEntityManagerFactoryImpl.serverName;
	}
	
	public static String[] getAllEntityClassName(){
		return SimpleEntityManagerFactoryImpl.emMap.keySet().toArray(new String[0]);
	}
	
	public static SimpleEntityManager<?> getSimpleEntityManagerImpl(String className){
		return SimpleEntityManagerFactoryImpl.emMap.get(className);
	}
	
	public static ConnectionPool getPool(){
		return SimpleEntityManagerFactoryImpl.pool;
	}
	
	public static int getEntityNum(SimpleEntityManagerOracleImpl<?> impl){
		return impl.entityModifedMap.size();
	}
	
	public static long getEntityProxyNum(SimpleEntityManagerOracleImpl<?> impl){
		return impl.proxyCount;
	}
	
	public static int getInsertLockNum(SimpleEntityManagerOracleImpl<?> impl){
		return 0;
	}
	
	public static int getSelectLockNum(SimpleEntityManagerMysqlImpl<?> impl){
		return impl.selectObjectLockMap.size();
	}
	
	
	public static int getEntityNum(SimpleEntityManagerMysqlImpl<?> impl){
		return impl.entityModifedMap.size();
	}
	
	public static long getEntityProxyNum(SimpleEntityManagerMysqlImpl<?> impl){
		return impl.proxyCount;
	}
	
	public static int getInsertLockNum(SimpleEntityManagerMysqlImpl<?> impl){
		return 0;
	}
	
	public static int getSelectLockNum(SimpleEntityManagerOracleImpl<?> impl){
		return impl.selectObjectLockMap.size();
	}
	
	
	public static MetaDataField[] getAllMetaDataField(SimpleEntityManagerOracleImpl<?> impl){
		return (MetaDataField[])impl.mde.map.values().toArray(new MetaDataField[0]);
	}
	
	public static MetaDataField[] getAllMetaDataField(SimpleEntityManagerMysqlImpl<?> impl){
		return (MetaDataField[])impl.mde.map.values().toArray(new MetaDataField[0]);
	}
	
	public static MysqlSectionManager<?> getMysqlSectionManager(SimpleEntityManagerMysqlImpl<?> impl){
		return impl.sectionManager;
	}
}
