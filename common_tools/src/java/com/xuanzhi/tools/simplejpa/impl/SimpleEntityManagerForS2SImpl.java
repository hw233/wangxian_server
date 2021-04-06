package com.xuanzhi.tools.simplejpa.impl;

import java.lang.annotation.Annotation;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.mem.OperatoinTrackerService;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostLoad;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostRemove;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostUpdate;
import com.xuanzhi.tools.simplejpa.annotation.SimplePrePersist;
import com.xuanzhi.tools.simplejpa.annotation.SimplePreRemove;
import com.xuanzhi.tools.simplejpa.annotation.SimplePreUpdate;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.EntityEntry;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.EntityProxyEntry;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.ModifyInfo;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.MyLock;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.MyMyInvocationHandler;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.NotifyEvent;
import com.xuanzhi.tools.simplejpa.impl.Utils.SimpleIndexImpl;
import com.xuanzhi.tools.text.StringUtil;

public class SimpleEntityManagerForS2SImpl<T> implements SimpleEntityManager<T>,Runnable{
	
	static Logger logger = LoggerFactory.getLogger(SimpleEntityManager.class);

	//
	public MetaDataEntity mde;
	
	protected ReentrantLock entityModifyLock = new ReentrantLock();
	protected ReferenceQueue<T> referenceQueue = new ReferenceQueue<T>();
	public HashMap<Long,EntityEntry<T>> entityModifedMap = new HashMap<Long,EntityEntry<T>>();
	
	//记录所有的Proxy的弱引用
	protected long proxyCount = 0;
	protected ReentrantLock proxyEntityModifyLock = new ReentrantLock();
	protected ReferenceQueue proxyReferenceQueue = new ReferenceQueue();
	protected HashMap<Long,ArrayList<EntityProxyEntry>> proxyEntityModifedMap = new HashMap<Long,ArrayList<EntityProxyEntry>>();

	//通知消息队列
	protected Object notifyQueueLock = new Object();
	protected ArrayList<NotifyEvent<T>> notifyQueue = new ArrayList<NotifyEvent<T>>(4096);

	protected Object savingConfilictLock = new Object();
	protected ArrayList<T> savingConfilictList = new ArrayList<T>();

	protected Object flushingConfilictLock = new Object();
	protected ArrayList<T> flushingConfilictList = new ArrayList<T>();
	//000～999
	protected int serverId;

	//加载同一个对象的同步锁，为了避免同时加载同一个对象
	protected HashMap<Long,MyLock> selectObjectLockMap = new HashMap<Long,MyLock>();

//	protected ConnectionPool pool = null; 
//	  
//	public void setConnectionPool(ConnectionPool pool){
//		this.pool = pool;
//	}

	public S2SEntityOption nssem;
	
	//用于场景服发到逻辑服后，逻辑服查找对应的manager去处理数据存储
	public String managerKey;
	
	private  Map<Class<?>,Integer> classInfo = new HashMap<Class<?>, Integer>();

	Thread thread;
	
	public static class NotifyEvent<T>{
		T t;
		String field;
		boolean newObject;
		
		public NotifyEvent(T t,String field,boolean newObject){
			this.t = t;
			this.field = field;
			this.newObject = newObject;
		}
	}

	/**
	 * 初始化过程，此过程应该在其他系统初始化前执行
	 * 此初始化过程在在收集信息时，同时做检查。
	 * @param cl
	 * @throws Exception
	 */
	void init(Class<?> cl,ArrayList<Class<?>> subClasses,Map<Class<?>,Integer> classInfo) throws Exception{
		
		long startTime = System.currentTimeMillis();
		this.classInfo = classInfo;
		System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化] [开始收集JPA的注释....]");
		try{
			cl.getConstructor(new Class[0]);
			for(int i = 0 ; i < subClasses.size() ; i++){
				Class<?> c = subClasses.get(i);
				c.getConstructor(new Class[0]);
			}
		}catch(Exception e){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [指定的类或者其子类没有公共的无参数的构造函数]");
			throw new Exception("指定的类["+cl.getName()+"]或者其子类没有公共的无参数的构造函数");
		}

		for(int i = 0 ; i < subClasses.size() ; i++){
			Class<?> c = subClasses.get(i);
			if(cl.isAssignableFrom(c) == false){
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [指定的类"+cl.getName()+"不是某个子类"+c.getName()+"的父类]");
				throw new Exception("指定的类"+cl.getName()+"不是某个子类"+c.getName()+"的父类");
			}
		}
		
		if(Utils.isEntity(cl) == false){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [指定的类"+cl.getName()+"不是Entity]");
			throw new Exception("指定的类["+cl.getName()+"]不是Entity");
		}
		
		for(int i = 0 ; i < subClasses.size() ; i++){
			Class<?> c = subClasses.get(i);
			if(Utils.isEntity(c) == false){
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [指定的类"+c.getName()+"不是Entity]");
				throw new Exception("指定的类["+c.getName()+"]不是Entity");
			}
		}
		
		
		
		Annotation annotations[] = cl.getAnnotations();
		
		SimpleEntity entity = null;
		for(int i = 0 ; i < annotations.length ; i++){
			Annotation a = annotations[i];
			if(a instanceof SimpleEntity){
				entity = (SimpleEntity)a;
			}
		}
		if(entity == null){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [指定的类不是Entity]");
			throw new Exception("指定的类["+cl.getName()+"]不是Entity");
		}
		
		String defaultTable = entity.name();
		if(defaultTable == null || defaultTable.length() == 0){
			defaultTable = cl.getName();
			int k = defaultTable.lastIndexOf(".");
			if(k >= 0){
				defaultTable = defaultTable.substring(k+1);
			}
		}
		mde = new MetaDataEntity();
		mde.cl = cl;
		
		mde.primaryTable = defaultTable;
		mde.sequenceName = "SEQ_" + mde.primaryTable;
		
		ArrayList<Field> fieldList = Utils.getAllNotFinalFields(cl);
		
		mde.rootClass = new MetaDataClass();
		mde.rootClass.parent = null;
		mde.rootClass.cl = cl;
		//为metaclass 设置cid added by liuyang at 2012-05-09
		mde.rootClass.cid = classInfo.get(cl);
		if(mde.rootClass.cid == null)
		{
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [某个类没有配置id]");
			throw new Exception("指定的类["+cl.getName()+"]没有配置id");
		}
		//Id and Version
		for(int i = 0 ; i < fieldList.size() ; i++){
			Field f = fieldList.get(i);
			boolean idDefine = false;
			boolean versionDefine = false;
			
			Annotation as[] = f.getAnnotations();
			for(int j = 0 ; j < as.length ; j++){
				if(as[j] instanceof SimpleId){
					idDefine = true;
				}
				if(as[j] instanceof SimpleVersion){
					versionDefine = true;
				}
			}
			
			if(idDefine == true){
				if(mde.id != null){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [存在多个Id字段的定义]");
					throw new Exception("指定的类["+cl.getName()+"]存在多个Id字段的定义");
				}
				
				if( f.getType() != Long.TYPE && f.getType() != Long.class){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Id字段"+f.getName()+"的类型["+f.getType()+"]不是Long]");
					throw new Exception("指定的类["+cl.getName()+"]Id字段"+f.getName()+"的类型["+f.getType()+"]不是Long");
				}
				
				mde.id = new MetaDataField();
				mde.id.field = f;
				f.setAccessible(true);
				mde.id.inPrimaryTable = true;
				mde.id.simpleColumn = Utils.getSimpleColumn(f);
				if(mde.id.simpleColumn.name().length() > 0){
					mde.id.columnNames = new String[]{mde.id.simpleColumn.name()};
				}else{
					mde.id.columnNames = new String[]{f.getName()};
				}
				mde.id.mdc = mde.rootClass;
				
			}
			
			if(versionDefine == true){
				if(mde.version != null){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [存在多个Version字段的定义]");
					throw new Exception("指定的类["+cl.getName()+"]存在多个Version字段的定义");
				}
				
				if( f.getType() != Integer.TYPE && f.getType() != Integer.class){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Version字段"+f.getName()+"的类型不是Integer]");
					throw new Exception("指定的类["+cl.getName()+"]Version字段"+f.getName()+"的类型不是Integer");
				}
				
				mde.version = new MetaDataField();
				mde.version.field = f;
				f.setAccessible(true);
				mde.version.inPrimaryTable = true;
				
				mde.version.simpleColumn = Utils.getSimpleColumn(f);
				if(mde.version.simpleColumn.name().length() > 0){
					mde.version.columnNames = new String[]{mde.version.simpleColumn.name()};
				}else{
					mde.version.columnNames = new String[]{f.getName()};
				}
				mde.version.mdc = mde.rootClass;
			}
		}
		if(mde.id == null){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Id字段不存在]");
			throw new Exception("指定的类["+cl.getName()+"]Id字段不存在");
		}
		if(mde.version == null){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Version字段不存在]");
			throw new Exception("指定的类["+cl.getName()+"]Version字段不存在");
		}
		
		fieldList.remove(mde.id.field);
		fieldList.remove(mde.version.field);
		
		for(int i = 0 ;  i< subClasses.size() ; i++){
			Class<?> c = subClasses.get(i);
			ArrayList<Field> fields = Utils.getDeclaredFields(c);
			for(int j = 0 ; j < fields.size() ; j++){
				Field f = fields.get(j);
				boolean idDefine = false;
				boolean versionDefine = false;
				
				Annotation as[] = f.getAnnotations();
				for(int k = 0 ; k < as.length ; k++){
					if(as[k] instanceof SimpleId){
						idDefine = true;
					}
					if(as[k] instanceof SimpleVersion){
						versionDefine = true;
					}
				}
				
				if(idDefine == true){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [子类"+c.getName()+"中不能定义Id]");
					throw new Exception("指定的类["+cl.getName()+"]存在多个Id字段的定义,子类"+c.getName()+"中不能定义Id");
				}
				
				if(versionDefine == true){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [子类"+c.getName()+"中不能定义Version]");
					throw new Exception("指定的类["+cl.getName()+"]存在多个Version字段的定义,子类"+c.getName()+"中不能定义Version");
					
				}
			}
		}
		
		
		//second table
		boolean needSecondTable = false;
		for(int i = 0 ; i < fieldList.size() ; i++){
			Field f = fieldList.get(i);
			Class<?> type = f.getType();
			if(Utils.isPrimitiveType(type)){
				
				MetaDataField mf = new MetaDataField();
				mf.field = f;
				mf.inPrimaryTable = true;
				f.setAccessible(true);
				mf.simpleColumn = Utils.getSimpleColumn(f);
				if(mf.simpleColumn.name().length() > 0){
					mf.columnNames = new String[]{mf.simpleColumn.name()};
				}else{
					mf.columnNames = new String[]{f.getName()};
				}
				mde.map.put(cl.getName()+"."+f.getName(), mf);
				mf.mdc = mde.rootClass;
				
				continue;
			}else if((f.getModifiers() & Modifier.TRANSIENT) != 0){
				//由于针对数据库的simpleMF对transient不感兴趣不能使用已有的机制进行保存，
				//而我们这个类是对simpleMF的一个改写，主要用来进行数据在网络之间传输，而不是进行数据库存储，
				//在这里加这段代码的用意是既使用simpleMF的机制又让它能够对transient感兴趣
				MetaDataField mf = new MetaDataField();
				mf.field = f;
				f.setAccessible(true);
				mf.simpleColumn = Utils.getSimpleColumn(f);
				if(mf.simpleColumn.name().length() > 0){
					mf.columnNames = new String[]{mf.simpleColumn.name()};
				}else{
					mf.columnNames = new String[]{f.getName()};
				}
				mde.map.put(cl.getName()+"."+f.getName(), mf);
				mf.mdc = mde.rootClass;
				
				continue;
			}else if(type.isArray() || Utils.isCollectionType(type) || Utils.isEmbeddable(type)){
				needSecondTable = true;
				
				MetaDataField mf = new MetaDataField();
				mf.field = f;
				mf.inPrimaryTable = false;
				f.setAccessible(true);
				mf.simpleColumn = Utils.getSimpleColumn(f);
				String columnName = f.getName();
				if(mf.simpleColumn.name().length() > 0){
					columnName = mf.simpleColumn.name();
				}
				int len = mf.simpleColumn.length();
				int k = len/4000;
				if(k * 4000 < len) k++;
				mf.columnNames = new String[k];
				for(int j = 0 ; j < k ; j++){
					mf.columnNames[j] = columnName+"_" + (j+1);
				}
				
				mde.map.put(cl.getName()+"."+f.getName(), mf);
				mf.mdc = mde.rootClass;
				
				if(type.isArray()){
					Utils.checkArray(cl,f,type);
				}else if(Utils.isCollectionType(type)){
					Utils.checkCollectionType(cl,f,f.getGenericType());
				}else if(Utils.isEmbeddable(type)){
					Utils.checkEmbeddable(cl,f,type,new HashSet<Class>());
				}
				
			}else if(Utils.isEntity(type)){
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含其他Entity["+type.getName()+"]]");
				throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含其他Entity["+type.getName()+"]]");
			}else{
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
				throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
			}
		}
		
		ArrayList<MetaDataClass> subMetaDataClassList = new ArrayList<MetaDataClass>();
		for(int i = 0 ;  i< subClasses.size() ; i++){
			Class<?> c = subClasses.get(i);
			ArrayList<Field> fields = Utils.getDeclaredFields(c);
			
			MetaDataClass mdc = new MetaDataClass();
			mdc.cl = c;
			mdc.cid = classInfo.get(c);
			
			if(mdc.cid == null)
			{
				System.out.println("[SimpleEntityManager] ["+mdc.cl.getName()+"] [准备初始化失败] [某个类没有配置id]");
				throw new Exception("指定的类["+mdc.cl.getName()+"]没有配置id");
			}
			subMetaDataClassList.add(mdc);
			
			for(int j = 0 ; j < fields.size() ; j++){
				Field f = fields.get(j);
				Class<?> type = f.getType();
				if(Utils.isPrimitiveType(type)){
					
					MetaDataField mf = new MetaDataField();
					mf.field = f;
					mf.inPrimaryTable = true;
					f.setAccessible(true);
					mf.simpleColumn = Utils.getSimpleColumn(f);
					if(mf.simpleColumn.name().length() > 0){
						mf.columnNames = new String[]{mf.simpleColumn.name()};
					}else{
						mf.columnNames = new String[]{f.getName()};
					}
					mde.map.put(c.getName()+"."+f.getName(), mf);
					mf.mdc = mdc;
					
					continue;
				}else if((f.getModifiers() & Modifier.TRANSIENT) != 0){
					//由于针对数据库的simpleMF对transient不感兴趣不能使用已有的机制进行保存，
					//而我们这个类是对simpleMF的一个改写，主要用来进行数据在网络之间传输，而不是进行数据库存储，
					//在这里加这段代码的用意是既使用simpleMF的机制又让它能够对transient感兴趣
					MetaDataField mf = new MetaDataField();
					mf.field = f;
					f.setAccessible(true);
					mf.simpleColumn = Utils.getSimpleColumn(f);
					if(mf.simpleColumn.name().length() > 0){
						mf.columnNames = new String[]{mf.simpleColumn.name()};
					}else{
						mf.columnNames = new String[]{f.getName()};
					}
					mde.map.put(cl.getName()+"."+f.getName(), mf);
					mf.mdc = mdc;
					
					continue;
				}else if(type.isArray() || Utils.isCollectionType(type) || Utils.isEmbeddable(type)){
					needSecondTable = true;
					
					MetaDataField mf = new MetaDataField();
					mf.field = f;
					mf.inPrimaryTable = false;
					f.setAccessible(true);
					mf.simpleColumn = Utils.getSimpleColumn(f);
					String columnName = f.getName();
					if(mf.simpleColumn.name().length() > 0){
						columnName = mf.simpleColumn.name();
					}
					int len = mf.simpleColumn.length();
					int k = len/4000;
					if(k * 4000 < len) k++;
					mf.columnNames = new String[k];
					for(int l = 0 ; l < k ; l++){
						mf.columnNames[l] = columnName+"_" + (l+1);
					}
					
					mde.map.put(c.getName()+"."+f.getName(), mf);
					mf.mdc = mdc;
					
					if(type.isArray()){
						Utils.checkArray(c,f,type);
					}else if(Utils.isCollectionType(type)){
						Utils.checkCollectionType(c,f,f.getGenericType());
					}else if(Utils.isEmbeddable(type)){
						Utils.checkEmbeddable(c,f,type,new HashSet<Class>());
					}
					
				}else if(Utils.isEntity(type)){
					System.out.println("[SimpleEntityManager] ["+c.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含其他Entity["+type.getName()+"]]");
					throw new Exception("指定的类["+c.getName()+"]Entity的某个字段["+f.getName()+"]包含其他Entity["+type.getName()+"]]");
				}else{
					if((f.getModifiers() & Modifier.TRANSIENT) == 0){
						System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
						throw new Exception("指定的类["+c.getName()+"]Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
					}
				}
			}
			
		}
		
		
		if(needSecondTable){
			mde.secondTable = mde.primaryTable + "_SEC";
		}
		
		int loopCount = 0;
		while(subMetaDataClassList.isEmpty() == false){
			MetaDataClass mdc = subMetaDataClassList.remove(0);
			
			Class<?> superClass = mdc.cl.getSuperclass();
			MetaDataClass smdc = mde.getMetaDataClassByName(superClass.getName());
			//smdc.cid = this.classInfo.get(superClass);
			if(smdc != null){
				smdc.children.add(mdc);
				mdc.parent = smdc;
			}else{
				subMetaDataClassList.add(mdc);
			}
			loopCount ++;
			if(loopCount > 100000){
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [存在某个之类"+mdc.cl.getName()+"，其直接父类不在配置表中！]");
				throw new Exception("存在某个之类"+mdc.cl.getName()+"，其直接父类不在配置表中！]");
			}
		}
		
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("RR");
		keywords.add("SELECT");
		keywords.add("INDEX");
		keywords.add("TABLE");
		keywords.add("VIEW");
		keywords.add("FROM");
		keywords.add("WHERE");
		keywords.add("AND");
		keywords.add("OR");
		keywords.add("NOT");
		keywords.add("IN");
		keywords.add("LEFT");
		keywords.add("RIGHT");
		keywords.add("JOIN");
		keywords.add("ON");
		keywords.add("VALUES");
		
		
		HashMap<String,MetaDataField> map = new HashMap<String,MetaDataField>();
		Iterator<String> it = mde.map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = mde.map.get(field);
			for(int i = 0 ; i < f.columnNames.length ; i++){
				MetaDataField ff = map.get(f.columnNames[i].toUpperCase());
				if(ff != null){
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [存在两个属性冲突["+f.field.getDeclaringClass().getName()+"."+f.field.getName()
							+"]<>["+ff.field.getDeclaringClass().getName()+"."+ff.field.getName()+"]！");
					throw new Exception("存在两个属性冲突["+f.field.getDeclaringClass().getName()+"."+f.field.getName()
							+"]<>["+ff.field.getDeclaringClass().getName()+"."+ff.field.getName()+"]！");
				}
				map.put(f.columnNames[i].toUpperCase(), f);
				
				if(f.columnNames[i].length() > 30){
					System.out.println("[SimpleEntityManager] ["+f.field.getDeclaringClass().getName()+"] [准备初始化失败] [存在某个字段"+f.columnNames[i]+"名字太长]");
					throw new Exception("存在某个字段"+f.columnNames[i]+"名字太长！");
				}
				
				if(f.columnNames[i].toUpperCase().equalsIgnoreCase(mde.id.columnNames[0].toUpperCase()+"_SEC")){
					System.out.println("[SimpleEntityManager] ["+f.field.getDeclaringClass().getName()+"] [准备初始化失败] [存在某个字段"+f.columnNames[i]+"与ID字段重复]");
					throw new Exception("存在某个字段"+f.columnNames[i]+"与ID字段重复");
				}
				
				if(keywords.contains(f.columnNames[i].toUpperCase())){
					System.out.println("[SimpleEntityManager] ["+f.field.getDeclaringClass().getName()+"] [准备初始化失败] [存在某个字段"+f.columnNames[i]+"与关键字重复]");
					throw new Exception("存在某个字段"+f.columnNames[i]+"与关键字重复");
				}
			}
			
		}
		
		SimpleIndices indices = Utils.getDeclaredIndices(cl);
		//index
		if(indices != null){
			SimpleIndex indexs[] = indices.value();
			for(int i = 0 ; i < indexs.length ; i++){
				String members[] = indexs[i].members();
				for(int j = 0 ; j < members.length; j++){
					MetaDataField f = mde.getMetaDataField(cl, members[j]);
					if(f == null){
						System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个索引["+indexs[i].name()+"]包含不存在的字段["+members[j]+"]]");
						throw new Exception("指定的类["+cl.getName()+"][Entity的某个索引["+indexs[i].name()+"]包含不存在的字段["+members[j]+"]]");
					}else if(f.inPrimaryTable == false){
						System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个索引["+indexs[i].name()+"]包含非简单类型的字段["+members[j]+"]]");
						throw new Exception("指定的类["+cl.getName()+"][Entity的某个索引["+indexs[i].name()+"]包含非简单类型的字段["+members[j]+"]]");
					}
				}
			}
		}
		
		for(int ii = 0 ;  ii< subClasses.size() ; ii++){
			Class<?> c = subClasses.get(ii);
			indices = Utils.getDeclaredIndices(c);
			//index
			if(indices != null){
				SimpleIndex indexs[] = indices.value();
				for(int i = 0 ; i < indexs.length ; i++){
					String members[] = indexs[i].members();
					for(int j = 0 ; j < members.length; j++){
						MetaDataField f = mde.getMetaDataField(c, members[j]);
						if(f == null){
							System.out.println("[SimpleEntityManager] ["+c.getName()+"] [准备初始化失败] [Entity的某个索引["+indexs[i].name()+"]包含不存在的字段["+members[j]+"]]");
							throw new Exception("指定的类["+c.getName()+"][Entity的某个索引["+indexs[i].name()+"]包含不存在的字段["+members[j]+"]]");
						}else if(f.inPrimaryTable == false){
							System.out.println("[SimpleEntityManager] ["+c.getName()+"] [准备初始化失败] [Entity的某个索引["+indexs[i].name()+"]包含非简单类型的字段["+members[j]+"]]");
							throw new Exception("指定的类["+c.getName()+"][Entity的某个索引["+indexs[i].name()+"]包含非简单类型的字段["+members[j]+"]]");
						}
					}
				}
			}
		}
		
		System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化] [收集JPA的注释完成] [耗时:"+(System.currentTimeMillis()-startTime)+"ms]");
		startTime = System.currentTimeMillis();
		System.out.println("=============== "+cl.getName()+" ==============");
		System.out.println("主表：" + mde.primaryTable+"  副表：" + mde.secondTable + " 序列："+mde.sequenceName);
		System.out.println("主键：" + mde.id.columnNames[0]+"  版本：" + mde.version.columnNames[0]);
		
		ArrayList<SimpleIndexImpl> indexList = new ArrayList<SimpleIndexImpl>();
		
		indexList.add(new SimpleIndexImpl(mde.primaryTable+"_CNC_INX",new String[]{mde.CNC},1,cl));
		
		//为CID字段添加索引
		indexList.add(new SimpleIndexImpl(mde.primaryTable+"_CID_INX",new String[]{mde.CID},1,cl));
				
		indices = Utils.getDeclaredIndices(cl);
		if(indices != null){
			SimpleIndex indexs[] = indices.value();
			for(int i = 0 ; i < indexs.length ; i++){
				String members[] = indexs[i].members();
				String name = indexs[i].name();
				if(name.length() == 0){
					StringBuffer sb = new StringBuffer();
					sb.append(mde.primaryTable);
					for(int j = 0 ; j < members.length; j++){
						MetaDataField f = mde.getMetaDataField(cl,members[j]);
						if(sb.length() + f.field.getName().length() + 1 < 27){
							sb.append("_"+f.field.getName());
						}
					}
					sb.append("_INX");
					name = sb.toString();
				}
				StringBuffer sb = new StringBuffer();
				for(int j = 0 ; j < members.length; j++){
					MetaDataField f = mde.getMetaDataField(cl,members[j]);
					sb.append(f.columnNames[0]);
					if(j < members.length - 1){
						sb.append(",");
					}
				}
				
				indexList.add(new SimpleIndexImpl(name,members,indexs[i].compress(),cl));
				
				System.out.println("索引：" + name+"("+sb.toString()+")");
			}
		}
		for(int ii = 0 ;  ii< subClasses.size() ; ii++){
			Class<?> c = subClasses.get(ii);
			indices = Utils.getDeclaredIndices(c);
			if(indices != null){
				SimpleIndex indexs[] = indices.value();
				for(int i = 0 ; i < indexs.length ; i++){
					String members[] = indexs[i].members();
					String name = indexs[i].name();
					if(name.length() == 0){
						StringBuffer sb = new StringBuffer();
						sb.append(mde.primaryTable);
						for(int j = 0 ; j < members.length; j++){
							MetaDataField f = mde.getMetaDataField(c,members[j]);
							if(sb.length() + f.field.getName().length() + 1 < 27){
								sb.append("_"+f.field.getName());
							}
						}
						sb.append("_INX");
						name = sb.toString();
					}
					StringBuffer sb = new StringBuffer();
					for(int j = 0 ; j < members.length; j++){
						MetaDataField f =mde.getMetaDataField(c,members[j]);
						sb.append(f.columnNames[0]);
						if(j < members.length - 1){
							sb.append(",");
						}
					}
					indexList.add(new SimpleIndexImpl(name,members,indexs[i].compress(),c));
					
					System.out.println("索引：" + name+"("+sb.toString()+")");
				}
			}
		}
		
		/* 打印信息，暂时关闭
		System.out.print("主表字段：["+mde.id.columnNames[0]+" NUMBER(38,0)],["+mde.version.columnNames[0]+" NUMBER(19,0)]");
		Iterator<String> it = mde.map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = mde.map.get(field);
			if(f.inPrimaryTable){
				Class<?> clazz = f.field.getType();
				String dType = "";
				if(clazz == Boolean.TYPE || clazz == Boolean.class){
					dType = "CHAR(1)";
				}else if(clazz == Byte.TYPE || clazz == Byte.class){
					dType = "NUMBER(4,0)";
				}else if(clazz == Short.TYPE || clazz == Short.class){
					dType = "NUMBER(6,0)";
				}else if(clazz == Integer.TYPE || clazz == Integer.class){
					dType = "NUMBER(19,0)";
				}else if(clazz == Character.TYPE || clazz == Character.class){
					dType = "CHAR(1)";
				}else if(clazz == Long.TYPE || clazz == Long.class){
					dType = "NUMBER(38,0)";
				}else if(clazz == Float.TYPE || clazz == Float.class){
					int precision = 19;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 6;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					dType = "NUMBER("+precision+","+scale+")";
				}else if(clazz == Double.TYPE || clazz == Double.class){
					int precision = 38;
					if(f.simpleColumn.precision() > 0){
						precision = f.simpleColumn.precision();
					}
					int scale = 10;
					if(f.simpleColumn.scale() > 0){
						scale = f.simpleColumn.scale();
					}
					dType = "NUMBER("+precision+","+scale+")";
				}else if(clazz == String.class){
					dType = "VARCHAR2";
				}else if(clazz == java.util.Date.class || clazz == java.sql.Date.class){
					dType = "DATE";
				}
				System.out.print(",["+f.columnNames[0]+" "+dType+"]");
			}
		}
		System.out.println("");
		
		if(mde.secondTable != null){
			System.out.print("副表字段：["+mde.id.columnNames[0]+" NUMBER(38,0)]");
			it = mde.map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable == false){
					for(int i = 0 ; i < f.columnNames.length ; i++){
						System.out.print(",["+f.columnNames[i]+" VARCHAR2]");
					}
				}
			}
			System.out.println("");
		}
		*/
		
		checkDataBase(indexList);
		findLiftTimeMethods(mde.rootClass);
		for(int ii = 0 ;  ii< subClasses.size() ; ii++){
			Class<?> c = subClasses.get(ii);
			MetaDataClass mdc = mde.getMetaDataClassByName(c.getName());
			findLiftTimeMethods(mdc);
		}
		
		System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化完成] [总耗时："+(System.currentTimeMillis() - startTime)+"ms]");
		
		thread = new Thread(this,"Thread-"+cl.getName()+"-EntityManager");
		thread.start();
	}
	
	void findLiftTimeMethods(MetaDataClass mdc){
		Method methods[] = mdc.cl.getDeclaredMethods();
		for(int i = 0 ; i < methods.length ; i++){
			if(methods[i].getParameterTypes() == null || methods[i].getParameterTypes().length == 0){
				Annotation aaaa[] = methods[i].getAnnotations();
				for(int j = 0 ; j < aaaa.length ; j++){
					if(aaaa[j] instanceof SimplePostLoad){
						mdc.postLoad = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePostPersist){
						mdc.postPersist = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePostUpdate){
						mdc.postUpdate = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePrePersist){
						mdc.prePersist = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePreUpdate){
						mdc.preUpdate = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePreRemove){
						mdc.preRemove = methods[i];
						methods[i].setAccessible(true);
					}else if(aaaa[j] instanceof SimplePostRemove){
						mdc.postRemove = methods[i];
						methods[i].setAccessible(true);
					}
				}
			}
		}
	}
	
	/**
	 * 检查数据库，创建必要的表，索引，字段
	 * 因为这个类是服务器与服务器之间的传输，不是服务器和数据库之间的传输，所以忽略这个操作
	 */
	void checkDataBase(ArrayList<SimpleIndexImpl> indexList) throws Exception{
		
	}
	
	/**
	 * 主要实现场景服与逻辑服的数据通知
	 * 场景服数据需要通过逻辑服才可以存储
	 * 因此当场景服数据发生变化时，需要通知逻辑服有数据发生了变化
	 * 所有场景服的数据发生数据改变时如果想要存储到数据库中，都需要调用这个方法
	 * 此方法会异步发送协议到逻辑服，逻辑服根据需要修改数据并保存（有些数据只需要修改并不需要保存）
	 */
	public void notifyFieldChange(T t, String field) {
		NotifyEvent<T> e = new NotifyEvent<T>(t,field,false);
		synchronized(notifyQueueLock){
			notifyQueue.add(e);
		}
	}

	public void notifyNewObject(T t) {
		NotifyEvent<T> e = new NotifyEvent<T>(t,null,true);
		synchronized(notifyQueueLock){
			notifyQueue.add(e);
		}
	}
	
	/**
	 * 处理所有的NotifyEvent
	 */
	private void handleNotifyEvent(){
		if(notifyQueue.size() <= 0) return;
		long startTime = System.currentTimeMillis();
		ArrayList<NotifyEvent<T>> al = null;
		synchronized(notifyQueueLock){
			if(notifyQueue.size() <=  0) return;
			al = notifyQueue;
			notifyQueue = new ArrayList<NotifyEvent<T>>(512);
		}
		for(int i = 0 ; i < al.size() ; i++){
			NotifyEvent<T> e = al.get(i);
			try{
				if(e.newObject){
					this.doNotifyNewObject(e.t);
				}else{
					this.doNotifyFieldChange(e.t, e.field);
				}
			}catch(Exception ex){
				
				if(logger.isWarnEnabled()){
					//logger.warn("[] [处理通知事件] [出现异常] ["+mde.cl.getName()+"] [T:"+e.t+"] [field:"+e.field+"] [newObject:"+e.newObject+"]",ex);
				}
			}
		}
		
		if(logger.isInfoEnabled()){
			//logger.info("[处理通知事件] [成功] ["+mde.cl.getName()+"] [处理数量:"+al.size()+"] [新队列中数量:"+notifyQueue.size()+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
		}
		
	}
	
	/**
	 * 应用层通知EntityManager，新产生了一个对象
	 * 
	 * 此方法将是EntityManager对新的对象产生弱引用
	 * 并在某个时间后插入到数据库中。
	 * 
	 * 新的对象，必须满足两个条件 
	 * 1. contains()方法返回false
	 * 2. version == 0
	 * 
	 * @param t
	 * @param field
	 */
	private void doNotifyNewObject(T t){
		MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
		if(mdc == null){
			if(logger.isWarnEnabled()){
				logger.warn("[通知新的对象] [对象类不在Entity配置文件中] [{}] [class:{}]",new Object[]{mde.cl.getName(),t.getClass().getName()});
			}
			return;
		}
		
		Long id = -1L;
		int version = 0;
		try {
			id = (Long)mde.id.field.get(t);
			version = mde.version.field.getInt(t);
		} catch (Exception e) {
			if(logger.isWarnEnabled()){
				logger.warn("[通知新的对象] [获取Id或者version出现异常] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
				logger.warn("[通知新的对象] [获取Id或者version出现异常] ["+mde.cl.getName()+"]",e);
			}
			return;
		}
		boolean b = contains(id);
		if(b == false && version == 0){
			entityModifyLock.lock();
			try{
				EntityEntry<T> ee = entityModifedMap.get(id);
				if(ee != null){
					if(logger.isWarnEnabled()){
						logger.warn("[通知新的对象] [不应该出现的情况，存在对应Entity的引用数据] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
					}
					return;
				}
				ee = new EntityEntry<T>(id,t,referenceQueue);
				ee.newObject =  true;
				entityModifedMap.put(id, ee);
			}finally{
				entityModifyLock.unlock();
			}
			
			
			
		}else{
			if(logger.isWarnEnabled()){
				logger.warn("[通知新的对象] [传入的对象不是新的对象] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
			}
		}
	}
	
	/**
	 * 应用层通知EntityManager，某个属性发生了变化
	 * 如果是某个Embeddable对象发生了变化，需要通知引用此对象的属性发生了变化，
	 * 无论是直接引用，还是通过List，数组，Map引用。
	 * 
	 * 如果某个属性发生了变化，但是没有通知EntityManager，
	 * 那么此属性的变化将不会保存到数据库中。
	 * 
	 * SimpleEntityManager没有采用Enhence机制来跟踪属性的变化，
	 * 主要是考虑到Enhence的实现比较复杂，短时间内确实无法实现。
	 * 
	 * 以后的版本中，是否加入Enhence，还看情况而定。
	 * 
	 * @param t
	 * @param field
	 */
	private void doNotifyFieldChange(T t,String field){
		checkReferenceQueue();
		
		Long id = -1L;
		try {
			id = (Long)mde.id.field.get(t);
		} catch (Exception e) {
			if(logger.isWarnEnabled()){
				logger.warn("[通知属性变化] [获取Id出现异常] ["+mde.cl.getName()+"] [id:"+id+"] [field:"+field+"]",e);
			}
			return;
		}

		MetaDataField f = mde.getMetaDataField(t.getClass(),field);
		if(f == null){
			if(logger.isWarnEnabled()){
				logger.warn("[通知属性变化] [属性不存在,应该是程序员编程错误导致] [{}] [id:{}] [field:{}]",new Object[]{mde.cl.getName(),id,field});
			}
			return;
		}
		
		//修改所有的Proxy对象上的对应数据
		if(proxyEntityModifedMap.containsKey(id)){
			
			Object value = null;
			try{
				value = f.field.get(t);
			}catch(Exception e){
				if(logger.isWarnEnabled()){
					logger.warn("[通知属性变化] [获取属性的值，出现异常] ["+mde.cl.getName()+"] [id:"+id+"] [field:"+field+"]",e);
				}
				return;
			}
			this.proxyEntityModifyLock.lock();
			try{
				ArrayList<EntityProxyEntry> al = proxyEntityModifedMap.get(id);
				if(al != null){
					for(int i = 0 ; i < al.size() ; i++){
						EntityProxyEntry epe = al.get(i);
						Object proxy = epe.get();
						if(proxy != null){
							MyS2SInvocationHandler handler = (MyS2SInvocationHandler)Proxy.getInvocationHandler(proxy);
							if(handler.data.containsKey(field)){
								handler.data.put(field, value);
							}
						}
					}
				}
			}finally{
				proxyEntityModifyLock.unlock();
			}
		}
		
		EntityEntry<T> ee = entityModifedMap.get(id);
		if(ee == null){

			if(logger.isDebugEnabled()){
				logger.debug("[通知属性变化] [不存在对应Entity的引用数据，可能是新的对象] [{}] [id:{}] [field:{}]",new Object[]{mde.cl.getName(),id,field});
			}
		
			return;
		}
		T reference = ee.get();
		
		if(reference == null){
			if(logger.isWarnEnabled()){
				logger.warn("[通知属性变化] [不应该出现的情况，对应的Entity已经被回收] [{}] [id:{}] [field:{}]",new Object[]{mde.cl.getName(),id,field});
			}
			return ;
		}
		
		if(reference != t){
			if(logger.isWarnEnabled()){
				logger.warn("[通知属性变化] [不应该出现的情况，引用的对象和参数传入的不是同一个对象] [{}] [id:{}] [field:{}]",new Object[]{mde.cl.getName(),id,field});
			}
			return ;
		}
		
		if(f.simpleColumn.urgent() == false || ee.newObject){
			ModifyInfo mi = ee.getModifyInfo(field);
			if(mi != null && mi.dirty){
				return;
			}
			
			entityModifyLock.lock();
			try{
				mi = ee.getModifyInfo(field);
				if(mi == null){
					mi = new ModifyInfo(field);
					ee.addModifyInfo(mi);
				}
				mi.dirty = true;
				
			}finally{
				entityModifyLock.unlock();
			}
		}else{ //紧急数据，立即保存
			try{
				ModifyInfo mi = ee.getModifyInfo(field);
				if(mi != null){
					mi.dirty =false;
				}
				
				update_object(t,new String[]{field});
			
			}catch(Exception e){
				ModifyInfo mi = ee.getModifyInfo(field);
				if(mi != null){
					mi.dirty =true;
				}
				if(logger.isWarnEnabled()){
					logger.warn("[保存紧急数据] [失败，出现未知异常] ["+t.getClass().getName()+"] [id:"+id+"] [field:"+field+"]",e);
				}
			}
			
			
		}
	}
	
	/**
	 * 清除被垃圾回收器回收的Entity对应的数据
	 * 如果发现某个Entity被回收后，有某个属性修改标记为true，
	 * 表明有数据丢失。
	 * 
	 * 设计上要求，应用层在准备释放某个Entity时，必须调用一次save方法。
	 * 
	 */
	protected void checkReferenceQueue(){
		EntityEntry<T> ee = null;
		while( (ee = (EntityEntry<T>)referenceQueue.poll()) != null){
			
			ObjectTrackerService.objectDestroy(ee.objectClass);
			
			entityModifyLock.lock();
			try{
				EntityEntry<T> ee2 = entityModifedMap.get(ee.id);
				if(ee2 != null && ee2.get() != null){
					if(logger.isWarnEnabled()){
						logger.warn("[垃圾回收] [试图回收存在实际引用的Entity] [{}] [id:{}] [可能是Java垃圾回收器回收了旧对象没有及时通知ReferenceQueue,期间重新加载了对应的对象]",new Object[]{mde.cl.getName(),ee.id});
					}
				}else{
					entityModifedMap.remove(ee.id);
					
					StringBuffer sb = null;
//					Iterator<String> it = ee.map.keySet().iterator();
//					while(it.hasNext()){
//						String key = it.next();
//						ModifyInfo mi = ee.map.get(key);
//						if(mi != null && mi.dirty){
//							if(sb == null){
//								sb = new StringBuffer();
//							}
//							sb.append(key+",");
//						}
//					}
					ModifyInfo infos[] = ee.getInfos();
					int size = ee.getInfoSize();
					for(int i = 0 ; i < size; i++){
						ModifyInfo mi = infos[i];
						if(mi != null && mi.dirty){
							if(sb == null){
								sb = new StringBuffer();
							}
							sb.append(mi.field+",");
						}
					}
					
					if(ee.newObject){
						if(logger.isWarnEnabled()){
							logger.warn("[垃圾回收] [有数据丢失] [{}] [id:{}] [修改的属性:新对象没有来得及保存就被回收了]",new Object[]{mde.cl.getName(),ee.id,sb});
						}
					}else if(sb != null){
						if(logger.isWarnEnabled()){
							logger.warn("[垃圾回收] [有数据丢失] [{}] [id:{}] [修改的属性:{}]",new Object[]{mde.cl.getName(),ee.id,sb});
						}
					}
				}
			}finally{
				entityModifyLock.unlock();
			}
		}
		
		//垃圾回收站回收Proxy时清理数据
		EntityProxyEntry epe = null;
		while( (epe = (EntityProxyEntry)proxyReferenceQueue.poll()) != null){
			proxyEntityModifyLock.lock();
			proxyCount --;
			try{
				ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(epe.id);
				if(al != null){
					int index = al.indexOf(epe);
					if(index >= 0){
						EntityProxyEntry lastE = al.remove(al.size() - 1);
						if(lastE != epe){
							al.set(index, lastE);
						}
					}
					if(al.size() == 0){
						proxyEntityModifedMap.remove(epe.id);
					}
				}
			}finally{
				proxyEntityModifyLock.unlock();
			}
		}
	}
	
	public static class MyS2SInvocationHandler implements InvocationHandler{
		
		SimpleEntityManagerForS2SImpl<?> em;
		long id;
		HashMap<String,Object> data = new HashMap<String,Object>();
		
		MyS2SInvocationHandler(SimpleEntityManagerForS2SImpl<?> em,long id,HashMap<String,Object> data){
			this.em = em;
			this.id = id;
			this.data = data;
		}
		
		public Object invoke(Object proxy,
	              Method method,
	              Object[] args)
	              throws Throwable{
			String m = method.getName();
			if(m.startsWith("is")){
				m = m.substring(2);
			}else if(m.startsWith("get")){
				m = m.substring(3);
			}
			String field = Character.toLowerCase(m.charAt(0)) + m.substring(1);
			
			EntityEntry<?> ee = em.entityModifedMap.get(id);
			if(ee != null && ee.get() != null){
				Object o = ee.get();
				MetaDataField mf = em.mde.getMetaDataField(em.mde.cl, field);
				if(em.mde.id.field.getName().equals(field)){
					mf = em.mde.id;
				}
				Object ret = mf.field.get(o);
				data.put(field, ret);
				return ret;
			}else{
				return data.get(field);
			}
		}
	}

	public void save(T t) throws Exception {
		//long startTime = System.currentTimeMillis();
		handleNotifyEvent();
		
		MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
		if(mdc == null){
			if(logger.isWarnEnabled()){
				logger.warn("[保存对象] [不是预定义的Entity] ["+t.getClass().getName()+"]");
			}
			throw new java.lang.IllegalArgumentException("类["+t.getClass().getName()+"]不是预定义的Entity");
		}
		
		long id = mde.id.field.getLong(t);
		int version = mde.version.field.getInt(t);
		EntityEntry<T> ee = null;
		entityModifyLock.lock();
		try{
			ee = entityModifedMap.get(id);
			if(ee == null){
				ee = new EntityEntry<T>(id,t,referenceQueue);
				entityModifedMap.put(id, ee);
			}
			if(ee.savingLock){
				
				synchronized(savingConfilictLock){
					savingConfilictList.add(t);
				}
				
				if(logger.isWarnEnabled()){
					logger.warn("[保存对象] [保存冲突，有线程正在保存此对象，暂不保存防止数据覆盖] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
				}
				return;
			}
			ee.savingLock = true;
		}finally{
			entityModifyLock.unlock();
		}
		
		try{
			version = mde.version.field.getInt(t);
			if(version == 0){
				mde.version.field.setInt(t, version+1);
				
				entityModifyLock.lock();
				try{
					ModifyInfo infos[] = ee.getInfos();
					int size = ee.getInfoSize();
					for(int i = 0 ; i < size; i ++){
						ModifyInfo mi = infos[i];
						if(mi != null && mi.dirty){
							mi.dirty = false;
						}
					}

				}finally{
					entityModifyLock.unlock();
				}
				
				try{
					this.insert_object(t);
					ee.newObject = false;
				}catch(Exception e){
					mde.version.field.setInt(t, version);
					if(logger.isWarnEnabled()){
						logger.warn("[保存对象] [失败，出现未知异常,恢复插入前状态] ["+t.getClass().getName()+"] [id:"+id+"] [version:"+version+"]",e);
					}
					throw e;
				}
			}else if(version > 0){
			
				ArrayList<String> al = new ArrayList<String>();
				entityModifyLock.lock();
				try{
					ModifyInfo infos[] = ee.getInfos();
					int size = ee.getInfoSize();
					for(int i = 0 ; i < size; i ++){
						ModifyInfo mi = infos[i];
						if(mi != null && mi.dirty){
							al.add(mi.field);
							mi.dirty = false;
						}
					}
					
				}finally{
					entityModifyLock.unlock();
				}
				
				if(al.size() == 0){
					if(logger.isWarnEnabled()){
						logger.warn("[保存对象] [无数据修改] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
					}
				}else{
					try{
						this.update_object(t, al.toArray(new String[0]));
					}catch(Exception e){
						entityModifyLock.lock();
						try{
							for(int i = 0 ; i < al.size() ; i++){
								String field = al.get(i);
								ModifyInfo mi = ee.getModifyInfo(field);
								if(mi != null){
									mi.dirty = true;
								}
							}
						}finally{
							entityModifyLock.unlock();
						}
						
						if(logger.isWarnEnabled()){
							logger.warn("[保存对象] [失败，出现未知异常，恢复保存前状态] ["+t.getClass().getName()+"] [id:"+id+"] [version:"+version+"]",e);
						}
						throw e;
					}
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[保存对象] [非法的版本信息] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
				}
				throw new Exception("非法的版本信息");
			}
		}finally{
			entityModifyLock.lock();
			try{
				ee.savingLock = false;
			}finally{
				entityModifyLock.unlock();
			}
			
		}
	}
	
	/**
	 * 启动或者关闭自动保存机制。
	 * 如果启动，需要设置保存线程检测的时间间隔。单位为秒。
	 * 
	 * @param enable
	 * @param checkIntervalInSeconds
	 */
	public void setAutoSave(boolean enable, int checkIntervalInSeconds) {
		autoSaveEnable = enable;
		this.checkIntervalInSeconds = checkIntervalInSeconds;
	}
	
	private boolean needAutoSaveImmdiately = false;
	private boolean destroy = false;
	private boolean running = false;
	
	protected boolean autoSaveEnable = true;
	protected int checkIntervalInSeconds = 30;
	
	public long repIdleTime = System.currentTimeMillis();

	public void run() {
		running = true;
		while(Thread.currentThread().isInterrupted() == false && autoSaveEnable){
			try{
				if(needAutoSaveImmdiately == false){
					synchronized(this){
						wait(checkIntervalInSeconds * 1000L);
					}
				}
				
				needAutoSaveImmdiately = false;
				
				boolean copyDestroy = destroy;
				
				if(copyDestroy)
					autoSave(true);
				else
					autoSave(false);
				
//				if(System.currentTimeMillis() - repIdleTime > 1000 * 5) {
//					repIdleTime = System.currentTimeMillis();
//					pool.reapIdleConnections();
//				}
				
				if(needAutoSaveImmdiately == false && copyDestroy == true){
					break;
				}
			}catch(Throwable e){
				logger.warn("[定期保存数据线程] ["+Thread.currentThread().getName()+"] [出现异常]",e);
			}
		}
		logger.warn("[定期保存数据线程] ["+Thread.currentThread().getName()+"] [autoSaveEnable:"+autoSaveEnable+"] [停止运行]");
		running = false;
	}

	
	/**
	 * 自动保存数据
	 */
	void autoSave(boolean ignoreSaveInterval) throws Exception{
		long startTime = System.currentTimeMillis();
		LinkedHashMap<Long,Object[]> mapForModify = new LinkedHashMap<Long,Object[]>();
		
		handleNotifyEvent();
		
		if(this.savingConfilictList.size() > 0){
			ArrayList<T> al = null;
			synchronized(savingConfilictLock){
				al = savingConfilictList;
				savingConfilictList = new ArrayList<T>();
			}
			for(int i = 0 ; i < al.size() ; i++){
				T t = al.get(i);
				try{
					save(t);
				}catch(Exception e){
					long id = mde.id.field.getLong(t);
					if(logger.isWarnEnabled()){
						logger.warn("[处理保存冲突的对象] [出现未知异常] ["+mde.cl.getName()+"] [class:"+t.getClass().getName()+"] [id:"+id+"]",e);
					}
				}
			}
		}
		
		if(this.flushingConfilictList.size() > 0){
			ArrayList<T> al = null;
			synchronized(flushingConfilictLock){
				al = flushingConfilictList;
				flushingConfilictList = new ArrayList<T>();
			}
			for(int i = 0 ; i < al.size() ; i++){
				T t = al.get(i);
				try{
					flush(t);
				}catch(Exception e){
					long id = mde.id.field.getLong(t);
					if(logger.isWarnEnabled()){
						logger.warn("[处理刷新冲突的对象] [出现未知异常] ["+mde.cl.getName()+"] [class:"+t.getClass().getName()+"] [id:"+id+"]",e);
					}
				}
			}
		}
		
		
		entityModifyLock.lock();
		try{
			Iterator<Long> it = entityModifedMap.keySet().iterator();
			while(it.hasNext()){
				long id = it.next();
				EntityEntry<T> ee = entityModifedMap.get(id);
				T reference = ee.get();
				ArrayList<String> modifyFields =  null;
				if(ee != null && reference != null){
					int version = mde.version.field.getInt(reference);
					
					//对于存盘失败的对象，30分钟内不再尝试存盘
					if(ee.saveFailed && startTime - ee.lastSaveFailedTime < 1800*1000){
						continue;
					}
					//对于正在保存的对象，不保存
					if(ee.savingLock){
						continue;
					}
					
					ModifyInfo infos[] = ee.getInfos();
					int size = ee.getInfoSize();
					for(int i = 0 ; i < size; i++){
						ModifyInfo mi = infos[i];
						if(mi != null && mi.dirty){
							if(mi.metaDataField == null){
								mi.metaDataField = mde.getMetaDataField(reference.getClass(), mi.field);
							}
							MetaDataField f = mi.metaDataField;
							if(f != null && (ignoreSaveInterval || startTime - mi.lastSaveTime >= Math.max(f.simpleColumn.saveInterval(),checkIntervalInSeconds)*1000L)){
								if(modifyFields == null){
									modifyFields = new ArrayList<String>();
								}
								modifyFields.add(mi.field);
								mi.dirty = false;
							}
						}
					}
					
					
//					Iterator<String> it2 = ee.map.keySet().iterator();
//					
//					while(it2.hasNext()){
//						String field = it2.next();
//						ModifyInfo mi = ee.map.get(field);
//						if(mi.metaDataField == null){
//							mi.metaDataField = mde.getMetaDataField(reference.getClass(), field);
//						}
//						MetaDataField f = mi.metaDataField;
//						if(f != null && mi != null){
//							if(mi.dirty 
//							&& (ignoreSaveInterval || startTime - mi.lastSaveTime >= f.simpleColumn.saveInterval()*1000L) ){
//								modifyFields.add(field);
//								mi.dirty = false;
//							}
//						}
//					}
					if(version == 0 || (modifyFields != null && modifyFields.size() > 0) ){
						if(modifyFields == null) modifyFields = new ArrayList<String>();
						mapForModify.put(id, new Object[]{reference,modifyFields});
						if(mapForModify.size() >= 5000){
							needAutoSaveImmdiately = true;
							break;
						}
					}
				}else{
					if(ee != null){
						if(reference == null){
							logger.warn("[批量保存，em有对象没有强引用，对象属性不被更新] [id:"+id+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
						}
					}
				}
			}
			
		
		}finally{
			entityModifyLock.unlock();
		}
		
		
		while(mapForModify.isEmpty() == false){
			
			LinkedHashMap<Long,Object[]> map = new LinkedHashMap<Long,Object[]>();
			
			Iterator<Long> keys = mapForModify.keySet().iterator();
			while(keys.hasNext()){
				
				long id = keys.next();
				map.put(id, mapForModify.get(id));
				
				keys.remove();
				
				if( map.size() >= 50) break;
			}
			
			this.entityModifyLock.lock();
			try{
				//将要存盘的全部标记为false
				Iterator<Long> it = map.keySet().iterator();
				while(it.hasNext()){
					Long id = it.next();
					EntityEntry<T> ee = entityModifedMap.get(id);
					if(ee != null){
						ee.savingLock = true;
					}
				}
			}finally{
				entityModifyLock.unlock();
			}
			
			//新的对象，用于失败的情况下，恢复版本信息
			ArrayList<T> newObjectList = new ArrayList<T>();
			
			Iterator<Long> itt = map.keySet().iterator();
			while(itt.hasNext()){
				Long id = itt.next();
				Object objs[] = map.get(id);
				T t = (T)objs[0];
				int version = mde.version.field.getInt(t);
				if(version == 0){
					mde.version.field.setInt(t,version+1);
					newObjectList.add(t);
				}
			}
			
			try{
				if(map.size() > 0){
					batch_insert_or_update_object(map);
					
					Iterator<Long> it = map.keySet().iterator();
					while(it.hasNext()){
						Long id = it.next();
						Object[] objs = map.get(id);
						ArrayList<String> modifyFields = (ArrayList<String>)objs[1];
						EntityEntry<T> ee = entityModifedMap.get(id);
						if(ee != null && modifyFields != null){
							ee.newObject = false;
							for(int i = 0 ; i < modifyFields.size() ; i++){
								String field = modifyFields.get(i);
								ModifyInfo mi = ee.getModifyInfo(field);
								if(mi != null){
									mi.lastSaveTime = startTime;
								}
							}
						}
					}
					
				}
			}catch(Exception e){
				
				needAutoSaveImmdiately = false;
				
				Iterator<Long> it = map.keySet().iterator();
				while(it.hasNext()){
					Long id = it.next();
					Object[] objs = map.get(id);
					ArrayList<String> modifyFields = (ArrayList<String>)objs[1];
					EntityEntry<T> ee = entityModifedMap.get(id);
					if(ee != null && modifyFields != null){
						for(int i = 0 ; i < modifyFields.size() ; i++){
							String field = modifyFields.get(i);
							ModifyInfo mi = ee.getModifyInfo(field);
							if(mi != null){
								mi.dirty = true;
							}
						}
					}
				}
				
				for(int i = 0 ; i < newObjectList.size() ; i++){
					T t = newObjectList.get(i);
					mde.version.field.setInt(t, 0);
				}
				
				logger.warn("[批量保存失败，恢复保存前的状态] ["+mde.cl.getName()+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]",e);
			}finally{
				this.entityModifyLock.lock();
				try{
					//将要存盘的全部标记为false
					Iterator<Long> it = map.keySet().iterator();
					while(it.hasNext()){
						Long id = it.next();
						EntityEntry<T> ee = entityModifedMap.get(id);
						if(ee != null){
							ee.savingLock = false;
						}
					}
				}finally{
					entityModifyLock.unlock();
				}
			}
		}
	}

	/**
	 * 更新一个对象，只更新有修改的属性，没有修改的属性不更新
	 * 因为是服务器对服务器的数据同步，所以用协议通知逻辑服务器数据的变化的属性及值
	 * @param t
	 * @throws Exception
	 */
	public boolean update_object(T t,String fields[]) throws Exception{
		if(nssem.isClient()){
			boolean success = nssem.update_object(managerKey, t, fields);
			
			if(success){
				return success;
			}
			throw new Exception("update_object异常，从逻辑服返回来的值为false");
		}
		throw new Exception("update_object异常nssem.isClient()为非客户端模式");
	}

	/**
	 * 向DataBase插入一个对象
	 * @param t
	 * @throws Exception
	 */
	public boolean insert_object(T t) throws Exception{
		if(nssem.isClient()){
			boolean success = nssem.insert_object(managerKey, t);
			if(success){
				return success;
			}
			throw new Exception("insert_object异常，从逻辑服返回来的值为false");
		}
		throw new Exception("insert_object异常nssem.isClient()为非客户端模式");
	}

	
	/**
	 * 批量保存或者更新数据，新的对象则插入到数据库中，已有的数据，将变化保存到数据库中
	 * map实际数据为map.put(id, new Object[]{reference,modifyFields});
	 * @param t
	 * @throws Exception
	 */
	public void batch_insert_or_update_object(LinkedHashMap<Long,Object[]> map) throws Exception{
		if(nssem.isClient()){
			boolean success = nssem.batch_insert_or_update_object(managerKey, map);
			if(!success){
				throw new Exception("batch_insert_or_update_object异常，逻辑服返回值为false");
			}
			return;
		}
		throw new Exception("batch_insert_or_update_object异常nssem.isClient()为非客户端模式");
	}

	public void batchSave(T[] ts) throws Exception {
		for(int i = 0 ; i < ts.length ; i++){
			save(ts[i]);
		}
	}

	public boolean contains(long id) {
		entityModifyLock.lock();
		try{
			EntityEntry<T> ee = entityModifedMap.get(id);
			if(ee == null){
				return false;
			}
			T reference = ee.get();
			
			if(reference == null){
				return false;
			}
			
			return true;
		}finally{
			entityModifyLock.unlock();
		}
	}

	public long count() throws Exception {
		if(nssem.isClient()){
			return nssem.count(managerKey);
		}
		throw new Exception("没有从逻辑服获得count");
	}

	public long count(Class<?> cl, String whereSql) throws Exception {
		if(nssem.isClient()){
			return nssem.count(managerKey,cl,whereSql);
		}
		throw new Exception("没有从逻辑服获得count");
	}

	public long count(Class<?> cl, String preparedWhereSql, Object[] paramValues)
			throws Exception {
		if(nssem.isClient()){
			return nssem.count(managerKey,cl,preparedWhereSql,paramValues);
		}
		throw new Exception("没有从逻辑服获得count");
	}

	public void destroy() {
		if(this.autoSaveEnable == false) return;
		
		long startTime = System.currentTimeMillis();
		destroy = true;
		synchronized(this){
			notify();
		}
		try {
			System.out.print("[SimpleEntityManager] ["+mde.cl.getName()+"] [等待销毁] [");
			while(running){
				thread.join(1000L);
				System.out.print(".");
			}
			System.out.println("] [销毁完成] [耗时："+(System.currentTimeMillis() - startTime)+"ms]");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	/**
	 * 调用这个方法很容易出错，会造成没有保存数据的风险。建议不知道这个方法如何使用的不要轻易调用。
	 * 因为这个方法直接从逻辑服查询到最新的t，并不理会现有内存中是否有旧的t,如果场景服内还存在t的其他引用，需要用最新的t代替旧的t，如果旧的t中有没有保存的属性的话，就会丢失
	 * 避免出错的方式就是严格按照顺序，必须先保存确认已经把数据更新到逻辑服后才能从逻辑服取数据，然后替换
	 */
	public T findFromLogicServer(long id) throws Exception{
		T t = null;
		if(nssem.isClient()){
			t = (T) nssem.find(managerKey, id);
		}
		if(t != null){
			entityModifyLock.lock();
			try{
//				EntityEntry<T> ee = entityModifedMap.get(id);
//				if(ee == null){
				
//				//这个地方很容易出错，因为从逻辑服查询到最新的t，但场景服内还存在t的其他引用，这样的话需要用最新的t代替旧的t，如果旧的t中有没有保存的属性的话，就会丢失
//				//避免出错的方式就是严格按照顺序，必须先保存确认已经把数据更新到逻辑服后才能从逻辑服取数据，然后替换
				EntityEntry<T> ee = new EntityEntry<T>(id,t,referenceQueue);
				entityModifedMap.put(id, ee);
				return t;
				
//				}else{
//					//这个地方很容易出错，因为从逻辑服查询到最新的t，但场景服内还存在t的其他引用，这样的话需要用最新的t代替旧的t，如果旧的t中有没有保存的属性的话，就会丢失
//					//避免出错的方式就是严格按照顺序，必须先保存确认已经把数据更新到逻辑服后才能从逻辑服取数据，然后替换
//					T reference = ee.get();
//					if(reference == null){
//						checkReferenceQueue();
//						ee = new EntityEntry<T>(id,t,referenceQueue);
//						EntityEntry<T> oldEE = entityModifedMap.put(id, ee);
//						if(oldEE != null){
//							ObjectTrackerService.objectDestroy(oldEE.objectClass);
//						}
//						return t;
//					}else{
//						return reference;
//					}
//				}
			}finally{
				entityModifyLock.unlock();
			}
		}
		return t;
	}
	public T find(long id) throws Exception {
		
		handleNotifyEvent();
		
		EntityEntry<T> ee1 = entityModifedMap.get(id);
		if(ee1 != null){
			T reference = ee1.get();
			if(reference != null){
				return reference;
			}
		}
		
		
		MyLock lock = null;
		
		//检查内存中是否有，有则直接返回
		//内存中没有，准备从数据库中查询，查询前先获得对应此对象的临时查询锁，并计数
		entityModifyLock.lock();
		try{
			EntityEntry<T> ee = entityModifedMap.get(id);
			if(ee != null){
				T reference = ee.get();
				if(reference != null){
					return reference;
				}
			}
			
			lock = this.selectObjectLockMap.get(id);
			if(lock == null){
				lock = new MyLock();
				selectObjectLockMap.put(id, lock);
			}
			lock.count++;
			
		}finally{
			entityModifyLock.unlock();
		}
		
		try{
			//同步
			synchronized(lock){
				//先检查内存中是否有，可能存在同时加载的情况
				entityModifyLock.lock();
				try{
					EntityEntry<T> ee = entityModifedMap.get(id);
					if(ee != null){
						T reference = ee.get();
						if(reference != null){
							return reference;
						}
					}
				}finally{
					entityModifyLock.unlock();
				}
				
				T t = null;
				if(nssem.isClient()){
					t = (T) nssem.find(managerKey, id);
				}
				
				if(t != null){
					entityModifyLock.lock();
					try{
						EntityEntry<T> ee = entityModifedMap.get(id);
						if(ee == null){
							ee = new EntityEntry<T>(id,t,referenceQueue);
							entityModifedMap.put(id, ee);
							return t;
						}else{
							T reference = ee.get();
							if(reference == null){
								checkReferenceQueue();
								ee = new EntityEntry<T>(id,t,referenceQueue);
								EntityEntry<T> oldEE = entityModifedMap.put(id, ee);
								if(oldEE != null){
									ObjectTrackerService.objectDestroy(oldEE.objectClass);
								}
								return t;
							}else{
								return reference;
							}
						}
					}finally{
						entityModifyLock.unlock();
					}
				}else{
					return null;
				}
				
			}
		}finally{
			entityModifyLock.lock();
			try{
				lock.count--;
				if(lock.count <= 0){
					selectObjectLockMap.remove(id);
				}
			}finally{
				entityModifyLock.unlock();
			}
		}
	}

	/**
	 * flush把对象的所有属性包括final,transient,static属性传到逻辑服进行保存
	 * 逻辑服自己去判断哪些属性该保存，哪些不保存只是改变值
	 * 这样可以避免，有些值不能传过去的问题，不会遗漏属性
	 */
	public void flush(T t) throws Exception {
		long startTime = System.currentTimeMillis();
		
		handleNotifyEvent();
		
		
		MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
		if(mdc == null){
			if(logger.isWarnEnabled()){
				logger.warn("[刷新对象] [不是预定义的Entity] ["+t.getClass().getName()+"]");
			}
			throw new java.lang.IllegalArgumentException("类["+t.getClass().getName()+"]不是预定义的Entity");
		}
		
		long id = mde.id.field.getLong(t);
		int version = mde.version.field.getInt(t);
		
		if(version == 0){
			save(t);
			return;
		}
		
		EntityEntry<T> ee = null;
		entityModifyLock.lock();
		try{
			ee = entityModifedMap.get(id);
			if(ee == null){
				ee = new EntityEntry<T>(id,t,referenceQueue);
				entityModifedMap.put(id, ee);
			}
			if(ee.savingLock){
				synchronized(flushingConfilictLock){
					flushingConfilictList.add(t);
				}
				if(logger.isWarnEnabled()){
					logger.warn("[保存对象] [保存冲突，有线程正在保存此对象，暂不保存防止数据覆盖] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
				}
				return;
			}
			ee.savingLock = true;
		}finally{
			entityModifyLock.unlock();
		}
		
		try{
			ArrayList<String> alDirty = new ArrayList<String>();
			entityModifyLock.lock();
			try{
					Iterator<String> it = mde.map.keySet().iterator();
					while(it.hasNext()){
						String field = it.next();
						ModifyInfo mi = ee.getModifyInfo(field);
						if(mi != null){
							if(mi.dirty){
								alDirty.add(field);
								mi.dirty = false;
							}
							
						}
					}
			}finally{
				entityModifyLock.unlock();
			}
			ArrayList<Field> al = Utils.getAllNotFinalFields(t.getClass());
			ArrayList<String> fieldList = new ArrayList<String>();
			
			for(int i = 0 ; i < al.size() ;i++){
				MetaDataField mf = mde.getMetaDataField(t.getClass(), al.get(i).getName());
				if(mf != null){
					fieldList.add(al.get(i).getName());
				}
			}
			String fields[] = fieldList.toArray(new String[0]);
				
			try{
				this.update_object(t, fields);
			}catch(Exception e){
				entityModifyLock.lock();
				try{
					
						for(int i = 0 ; i < alDirty.size() ; i++){
							String field = alDirty.get(i);
							ModifyInfo mi = ee.getModifyInfo(field);
							if(mi != null){
								mi.dirty = true;
							}
						}
					
				}finally{
					entityModifyLock.unlock();
				}
				
				if(logger.isWarnEnabled()){
					logger.warn("[刷新对象] [失败，出现异常，恢复刷新前状态] ["+mde.cl.getName()+"] [id:"+id+"] [version:"+version+"] [耗时:"+(System.currentTimeMillis() - startTime)+"ms]",e);
				}
	
				throw e;
			}
		}finally{
			entityModifyLock.lock();
			try{
				ee.savingLock = false;
			}finally{
				entityModifyLock.unlock();
			}
		}

		
	}

	public boolean isAutoSaveEnabled() {
		return autoSaveEnable;
	}

	public long nextId() throws Exception {
//		if(nssem.isClient()){
//			return nssem.nextId(managerKey);
//		}
		throw new Exception("没有从逻辑服获得新id");
	}

	public List<T> query(Class<?> cl, String whereSql, String orderSql,
			long start, long end) throws Exception {
		long startTime = System.currentTimeMillis();
		List<T> al = null;
		if(nssem.isClient()){
			al = (List<T>) nssem.query(managerKey, cl, whereSql, orderSql, start, end);
		}
		if(al == null){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [带条件分页查询对象列表] [失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [cost:{}ms]",new Object[]{mde.cl.getName(),whereSql,orderSql,start,end,System.currentTimeMillis()-startTime});
			}
			throw new Exception("返回来的值为空");
		}
		try{
			entityModifyLock.lock();
			ArrayList<T> retList = new ArrayList<T>();
			ArrayList<T> postLoadAl = new ArrayList<T>();
			try{
				for(T t: al){
					long id = mde.id.field.getLong(t);
					EntityEntry<T> ee = entityModifedMap.get(id);
					if(ee == null){
						ee = new EntityEntry<T>(id,t,referenceQueue);
						entityModifedMap.put(id, ee);
						retList.add(t);
						postLoadAl.add(t);
					}else{
						T reference = ee.get();
						if(reference == null){
							checkReferenceQueue();
							ee = new EntityEntry<T>(id,t,referenceQueue);
							EntityEntry<T> oldEE = entityModifedMap.put(id, ee);
							if(oldEE != null){
								ObjectTrackerService.objectDestroy(oldEE.objectClass);
							}
							retList.add(t);
							postLoadAl.add(t);
						}else{
							retList.add(reference);
						}
					}
				}
			}finally{
				entityModifyLock.unlock();
			}
			
			for(T t : postLoadAl){
				MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
				while(mdc != null){
					if(mdc.postLoad != null){
						try{
							mdc.postLoad.invoke(t, new Object[]{});
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.info("[执行SQL] [带条件分页查询对象列表] [调用postLoad方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}
			}
			

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件分页查询对象列表] [成功] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),whereSql,orderSql,start,end,retList.size(),costTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件分页查询对象列表] [成功] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),whereSql,orderSql,start,end,retList.size(),costTime});
				}
			}
			return retList;
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件分页查询对象列表] [失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [cost:{}ms]",new Object[]{mde.cl.getName(),whereSql,orderSql,start,end,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件分页查询对象列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}
	}

	public List<T> query(Class<?> cl, String preparedWhereSql,
			Object[] paramValues, String orderSql, long start, long end)
			throws Exception {
		long startTime = System.currentTimeMillis();
		List<T> al = null;
		if(nssem.isClient()){
			al = (List<T>) nssem.query(managerKey, cl, preparedWhereSql, paramValues, orderSql, start, end);
		}
		if(al == null){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL1] [带条件分页查询对象列表] [失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,start,end,System.currentTimeMillis()-startTime});
			}
			throw new Exception("返回来的值为空");
		}
		try{
			entityModifyLock.lock();
			ArrayList<T> retList = new ArrayList<T>();
			ArrayList<T> postLoadAl = new ArrayList<T>();
			try{
				for(T t: al){
					long id = mde.id.field.getLong(t);
					EntityEntry<T> ee = entityModifedMap.get(id);
					if(ee == null){
						ee = new EntityEntry<T>(id,t,referenceQueue);
						entityModifedMap.put(id, ee);
						retList.add(t);
						postLoadAl.add(t);
					}else{
						T reference = ee.get();
						if(reference == null){
							checkReferenceQueue();
							ee = new EntityEntry<T>(id,t,referenceQueue);
							EntityEntry<T> oldEE = entityModifedMap.put(id, ee);
							if(oldEE != null){
								ObjectTrackerService.objectDestroy(oldEE.objectClass);
							}
							retList.add(t);
							postLoadAl.add(t);
						}else{
							retList.add(reference);
						}
					}
				}
			}finally{
				entityModifyLock.unlock();
			}
			
			for(T t : postLoadAl){
				MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
				while(mdc != null){
					if(mdc.postLoad != null){
						try{
							mdc.postLoad.invoke(t, new Object[]{});
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.info("[执行SQL] [带条件分页查询对象列表] [调用postLoad方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}
			}
			

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL1] [带条件分页查询对象列表] [成功] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),costTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL1] [带条件分页查询对象列表] [成功] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),costTime});
				}
			}
			return retList;
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL1] [带条件分页查询对象列表] [失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,start,end,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL1] [带条件分页查询对象列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}
	}

	public <S> List<S> queryFields(Class<S> cl, long[] ids) throws Exception {

		long startTime = System.currentTimeMillis();
	
		if(cl.isInterface() == false){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，简单类型必须定义成接口] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
			}
			throw new java.lang.IllegalArgumentException("简单类型必须定义成接口");
		}
		
		Method[] methods = cl.getDeclaredMethods();
		if(methods.length == 0){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，简单类型必须定义成接口且至少有一个方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
			}
			throw new java.lang.IllegalArgumentException("简单类型必须定义成接口且至少有一个方法");
		}
		
		String[] fields = new String[methods.length];
		Class<?>[] rTypes = new Class<?>[methods.length];
		for(int i = 0 ; i < methods.length ; i++){
			Method m = methods[i];
			Class<?> types[] = m.getParameterTypes();
			if(types != null && types.length > 0){
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"需要参数] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"需要参数");
			}
			
			Class<?> rType = m.getReturnType();
			rTypes[i] = rType;
			if(!Utils.isPrimitiveType(rType)){
				
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型不是简单类型] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型不是简单类型");
			}

			if(m.getName().startsWith("is")){
				if (rType != Boolean.class && rType != Boolean.TYPE){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"以is开头但返回值不是bool] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"以is开头但返回值不是bool");
				}
				
				String field = m.getName().substring(2);
				if(field.length() == 0){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，不能存在is()方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("不能存在is方法");
				}
				field = Character.toLowerCase(field.charAt(0)) + field.substring(1);
				fields[i] = field;
				
				MetaDataField mf = mde.getMetaDataField(mde.cl, field);
				if(mde.id.field.getName().equals(field)){
					mf = mde.id;
				}
				
				if(mf == null ){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"找不到对应的属性] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"找不到对应的属性");
				}
				
				if(mf.field.getType() != rType){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型与超类对应的属性类型不一样] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型与超类对应的属性类型不一样");
				}
				
				
			}else if(m.getName().startsWith("get")){
				String field = m.getName().substring(3);
				if(field.length() == 0){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，不能存在get()方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("不能存在is方法");
				}
				
				field = Character.toLowerCase(field.charAt(0)) + field.substring(1);
				fields[i] = field;
				
				MetaDataField mf = mde.getMetaDataField(mde.cl, field);
				if(mde.id.field.getName().equals(field)){
					mf = mde.id;
				}
				
				if(mf == null ){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"找不到对应的属性] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"找不到对应的属性");
				}
				
				if(mf.field.getType() != rType){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型与超类对应的属性类型不一样] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型与超类对应的属性类型不一样");
				}
				
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"不是以is或者get开头] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"不是以is或者get开头");
			}
		}
		HashMap<Long,S> resultMap = new HashMap<Long,S>();
		//List<S> resultList = new ArrayList<S>();
//		ArrayList<Long> idList = new ArrayList<Long>();
//		proxyEntityModifyLock.lock();
//		try{
//			for(int i = 0 ; i < ids.length ; i++){
//				ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(ids[i]);
//				boolean found = false;
//				if(al != null){
//					for(int j = 0 ; j < al.size() ; j++){
//						EntityProxyEntry epe = al.get(j);
//						Object o = epe.get();
//						if(o != null && epe.interfaceCl == cl){
//							resultMap.put(epe.id, (S)o);
//							found = true;
//							break;
//						}
//					}
//				}
//				if(found == false){
//					idList.add(ids[i]);
//				}
//			}
//		}finally{
//			proxyEntityModifyLock.unlock();
//		}
//		
//		ArrayList<T> tList = new ArrayList<T>();
//		ArrayList<Long> idList2 = new ArrayList<Long>();
//		for(int i = 0 ; i < idList.size() ; i++){
//			long id = idList.get(i);
//			if(this.contains(id)){
//				T t = this.find(id);
//				if(t != null){
//					tList.add(t);
//				}else{
//					idList2.add(id);
//				}
//			}else{
//				idList2.add(id);
//			}
//		}
//		for(T t : tList){
//			long id = mde.id.field.getLong(t);
//			HashMap<String,Object> data = new HashMap<String,Object>();
//			for(int i = 0 ; i < fields.length ; i++){
//				MetaDataField mf = mde.getMetaDataField(mde.cl, fields[i]);
//				if(mde.id.field.getName().equals(fields[i])){
//					mf = mde.id;
//				}
//				Object o = mf.field.get(t);
//				data.put(fields[i], o);
//			}
//			InvocationHandler handler = new MyS2SInvocationHandler(this,id,data);
//			S s = null;
//			try{
//				s = (S)Proxy.newProxyInstance(cl.getClassLoader(),new Class[] {cl},handler);
//				proxyEntityModifyLock.lock();
//				try{
//					ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(id);
//					if(al == null){
//						al = new ArrayList<EntityProxyEntry>();
//						proxyEntityModifedMap.put(id, al);
//					}
//					al.add(new EntityProxyEntry(id,cl,s,this.proxyReferenceQueue));
//					proxyCount++;
//				}finally{
//					proxyEntityModifyLock.unlock();
//				}
//			}catch(Exception e){
//				if(logger.isWarnEnabled()){
//					logger.warn("[通过ResultSet构建简单对象] [失败] [创建简单对象Proxy出现异常] [{}] [id:{}] [cost:{}ms]",new Object[]{cl.getName(),id,System.currentTimeMillis()- startTime});
//				}
//				throw e;
//			}
//			resultMap.put(id, s);
//		}
		
		try{
//			if(idList2.size() > 0){
				List<S> list = null;
				if(nssem.isClient()){
//					long[] idss = new long[idList2.size()];
//					for(int i = 0; i < idss.length; i++){
//						idss[i] = idList2.get(i);
//					}
//					list = (List<S>) nssem.queryFields(managerKey, cl, idss);
					list = (List<S>) nssem.queryFields(managerKey, cl, ids);
				}else{
					throw new Exception("queryFields异常，nssem.isClient()为非客户端模式");
				}
//				if(list != null){
//					for(int i = 0 ; i < list.size() ; i++){
//						S t = list.get(i);
//						if(t != null){
//							resultMap.put(idList2.get(i), t);
//						}
//					}
//				}
//			}
//			ArrayList<S> resultList = new ArrayList<S>();
//			for(int i = 0 ; i < ids.length ; i++){
//				S s = resultMap.get(ids[i]);
//				if(s != null){
//					resultList.add(s);
//				}
//			}
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [通过ID列表查询简单对象] [成功] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),(list != null ? list.size():"list==null"),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表查询简单对象] [成功] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),(list != null ? list.size():"list==null"),System.currentTimeMillis()-startTime});
				}
			}
			if(list != null){
				return list;
			}else{
				return new ArrayList<S>();
			}
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID列表查询简单对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,0, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表查询简单对象] [失败，出现异常] [{}] [简单对象:{}] [ids:{}] [ret:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [通过ID列表查询简单对象] [失败，出现异常] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}
	}

	public long[] queryIds(Class<?> cl, String whereSql) throws Exception {
		if(nssem.isClient()){
			return nssem.queryIds(managerKey, cl, whereSql);
		}
		throw new Exception("queryIds异常，nssem.isClient()为非客户端模式");
	}

	public long[] queryIds(Class<?> cl, String preparedWhereSql,
			Object[] paramValues) throws Exception {
		if(nssem.isClient()){
			return nssem.queryIds(managerKey, cl, preparedWhereSql, paramValues);
		}
		throw new Exception("queryIds异常，nssem.isClient()为非客户端模式");
	}

	public long[] queryIds(Class<?> cl, String whereSql, String orderSql,
			long start, long end) throws Exception {
		if(nssem.isClient()){
			return nssem.queryIds(managerKey, cl, whereSql, orderSql, start, end);
		}
		throw new Exception("queryIds异常，nssem.isClient()为非客户端模式");
	}

	public long[] queryIds(Class<?> cl, String preparedWhereSql,
			Object[] paramValues, String orderSql, long start, long end)
			throws Exception {
		if(nssem.isClient()){
			return nssem.queryIds(managerKey, cl, preparedWhereSql, paramValues, orderSql, start, end);
		}
		throw new Exception("queryIds异常，nssem.isClient()为非客户端模式");
	}

	public void remove(T t) throws Exception {
		if(nssem.isClient()){		
			long id = mde.id.field.getLong(t);
			int version = mde.version.field.getInt(t);
			
			handleNotifyEvent();
			
			MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
			while(mdc != null){
				if(mdc.preRemove != null){
					try{
						mdc.preRemove.invoke(t, new Object[]{});
					}catch(Exception e){
						if(logger.isWarnEnabled()){
							logger.info("[执行SQL] [删除对象] [调用preRemove方法出现异常]",e);
						}
					}
					break;
				}
				mdc = mdc.parent;
			}
			
			EntityEntry<T> ee = null;
			entityModifyLock.lock();
			try{
				ee = entityModifedMap.get(id);
				if(ee == null){
					ee = new EntityEntry<T>(id,t,referenceQueue);
					entityModifedMap.put(id, ee);
				}
				if(ee.savingLock){
					if(logger.isWarnEnabled()){
						logger.warn("[删除对象] [保存冲突，有线程正在保存此对象，暂不删除已防止删除后仍被保存] [{}] [id:{}] [version:{}]",new Object[]{mde.cl.getName(),id,version});
					}
					throw new java.util.concurrent.RejectedExecutionException("删除冲突，有线程正在保存此对象，暂不删除已防止删除后仍被保存");
				}
				ee.savingLock = true;
			}finally{
				entityModifyLock.unlock();
			}
			
			try{
				boolean b = nssem.remove(managerKey, t);
				if(b){
					mdc = mde.getMetaDataClassByName(t.getClass().getName());
					while(mdc != null){
						if(mdc.postRemove != null){
							try{
								mdc.postRemove.invoke(t, new Object[]{});
							}catch(Exception e){
								if(logger.isWarnEnabled()){
									logger.info("[删除对象] [调用postRemove方法出现异常]",e);
								}
							}
							break;
						}
						mdc = mdc.parent;
					}
				}
			}finally{
				entityModifyLock.lock();
				ee.savingLock = false;
				try{
					entityModifedMap.remove(id);
				}finally{
					entityModifyLock.unlock();
				}
			}
		
		}else{
			throw new Exception("remove异常，nssem.isClient()为非客户端模式");	
		}
//		nssem.remove(managerKey, t);
	}
	
	public static String toString(long values[]){
		if(values == null) return "null";
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			
			sb.append(values[i]);
			
			if(i < values.length - 1){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#batch_delete_by_Ids(java.lang.Class, java.util.List)
	 */
	public int batch_delete_by_Ids(Class<?> cl, List<Long> ids)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#countUnSavedNewObjects()
	 */
	public long countUnSavedNewObjects() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#dropEntityIndex(java.lang.Class)
	 */
	public void dropEntityIndex(Class cl) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#getBatchSaveOrUpdateSize()
	 */
	public int getBatchSaveOrUpdateSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#releaseReference()
	 */
	public void releaseReference() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#remove(T[])
	 */
	public void remove(T[] ts) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#setBatchSaveOrUpdateSize(int)
	 */
	public void setBatchSaveOrUpdateSize(int batchSize) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.simplejpa.SimpleEntityManager#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

}
