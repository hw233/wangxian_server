package com.xuanzhi.tools.simplejpa.impl;

import java.lang.annotation.Annotation;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.mem.OperatoinTrackerService;
import com.xuanzhi.tools.queue.DefaultQueue;
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
import com.xuanzhi.tools.simplejpa.impl.Utils.SimpleIndexImpl;
import com.xuanzhi.tools.statistics.BooleanExpressionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;

public class SimpleEntityManagerOracleImpl<T> implements SimpleEntityManager<T>,Runnable{
	
	
	public static class ModifyInfo{
		String field;
		
		public ModifyInfo(String s){
			field = s;
		}
		
		public boolean dirty = false;
		public long lastSaveTime = System.currentTimeMillis();
		MetaDataField metaDataField;
	}
	
	/**
	 * 记录某个Entity，哪些属性发生了变化
	 * 此类是一个弱引用，但对应的Entity被GC回收后，
	 * 自动清空对应的数据
	 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
	 *
	 * @param <T>
	 */
	public static class EntityEntry<T> extends WeakReference<T>{
		long id;
		
		//标记为新的对象，为了清理时，如果有此标记，说明有数据丢失
		boolean newObject = false;
		
		//HashMap<String,ModifyInfo> map = new HashMap<String,ModifyInfo>();
		
		//此对象的类，为了统计
		Class<?> objectClass;
		
		//
		boolean saveFailed = false;
		long lastSaveFailedTime = 0;
		
		//表明引用的对象是否真正存盘，保证同一个对象，不能同时被存盘，否则会出现数据覆盖现象
		boolean savingLock = false;
		
		public EntityEntry(long id,T referent, ReferenceQueue<T> q) {
			super(referent, q);
			objectClass = referent.getClass();
			this.id = id;
			
			ObjectTrackerService.objectCreate(objectClass);
		}
		
		private ModifyInfo infos[];
		private int infoSize = 0;
		
		
		
		public ModifyInfo getModifyInfo(String field){
			if(infoSize <= 0 || infos == null) return null;
			ModifyInfo temp[] = infos;
			int size = infoSize;
			for(int i = 0 ;i < size && i < temp.length; i++){
				if(temp[i] != null && temp[i].field.equals(field)){
					return temp[i];
				}
			}
			return null;
		}
		
		public void addModifyInfo(ModifyInfo m){
			if(infos == null){
				infos = new ModifyInfo[4];
				infos[0] = m;
				infoSize = 1;
			}else if(infoSize < infos.length){
				infos[infoSize] = m;
				infoSize++;
			}else if(infoSize == infos.length){
				ModifyInfo temp[] = new ModifyInfo[infos.length*2];
				System.arraycopy(infos, 0, temp, 0, infoSize);
				infos = temp;
				infos[infoSize] = m;
				infoSize++;
			}else{
				throw new java.lang.RuntimeException("internal error: infoSize("+infoSize+") > infos.length("+infos.length+")");
			}
		}
		
		public ModifyInfo[] getInfos(){
			return infos;
		}
		
		public int getInfoSize(){
			return infoSize;
		}
	}
	
	/**
	 * 记录某个Entity的Proxy对象
	 * 此类是一个弱引用，当对应的Entity的Proxy被GC回收后，
	 * 自动清空对应的数据
	 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
	 *
	 * @param <T>
	 */
	public static class EntityProxyEntry extends WeakReference{
		long id;
		Class interfaceCl;
		public EntityProxyEntry(long id,Class interfaceCl,Object referent, ReferenceQueue q) {
			super(referent, q);
			this.interfaceCl = interfaceCl;
			this.id = id;
		}
	}
	
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
	
	//通知消息队列
	protected Object notifyQueueLock = new Object();
	protected ArrayList<NotifyEvent<T>> notifyQueue = new ArrayList<NotifyEvent<T>>(4096);

	protected Object savingConfilictLock = new Object();
	protected ArrayList<T> savingConfilictList = new ArrayList<T>();

	protected Object flushingConfilictLock = new Object();
	protected ArrayList<T> flushingConfilictList = new ArrayList<T>();
	
	static Logger logger = LoggerFactory.getLogger(SimpleEntityManager.class);
	
	public static final int SEQUENCE_INCREMENT  = 1024;
	
	//
	protected MetaDataEntity mde;
	
	//nextId lock
	protected ReentrantLock nextIdLock = new ReentrantLock();
	//当前可以使用的id和当前已经缓存到内存中的可以用的最大id，每次从数据库中加载sequence，缓存1024个。
	protected long currAvaiableId;
	protected long cachedMaxAvaiableId;
	//000～999
	protected int serverId;
	protected String username;
	
	protected boolean autoSaveEnable = true;
	protected int checkIntervalInSeconds = 30;

	
	protected ReentrantLock entityModifyLock = new ReentrantLock();
	protected ReferenceQueue<T> referenceQueue = new ReferenceQueue<T>();
	protected HashMap<Long,EntityEntry<T>> entityModifedMap = new HashMap<Long,EntityEntry<T>>();
	
	//记录所有的Proxy的弱引用
	protected long proxyCount = 0;
	protected ReentrantLock proxyEntityModifyLock = new ReentrantLock();
	protected ReferenceQueue proxyReferenceQueue = new ReferenceQueue();
	protected HashMap<Long,ArrayList<EntityProxyEntry>> proxyEntityModifedMap = new HashMap<Long,ArrayList<EntityProxyEntry>>();
	
	private  Map<Class<?>,Integer> classInfo = new HashMap<Class<?>, Integer>();
	
	protected boolean readOnly;
	
	public static class MyLock{
		int count = 0;
	}
	
	//加载同一个对象的同步锁，为了避免同时加载同一个对象
	protected HashMap<Long,MyLock> selectObjectLockMap = new HashMap<Long,MyLock>();
	
	
	protected ConnectionPool pool = null; 
  
	public void setConnectionPool(ConnectionPool pool){
		this.pool = pool;
	}
	
	Thread thread;
	
	private boolean disableCheckDataBase = false;
	
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
		mde.username = username;
		
		mde.primaryTable = defaultTable;
		mde.sequenceName = "SEQ_" + mde.primaryTable;
		
		ArrayList<Field> fieldList = Utils.getPersistentFields(cl);
		
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
					System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
					throw new Exception("指定的类["+c.getName()+"]Entity的某个字段["+f.getName()+"]包含不能识别的类型["+type.getName()+"]]");
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
		mde.indexListForOracle = indexList;
		if(!disableCheckDataBase)
		{
			checkDataBase(mde.indexListForOracle);
		}
		findLiftTimeMethods(mde.rootClass);
		for(int ii = 0 ;  ii< subClasses.size() ; ii++){
			Class<?> c = subClasses.get(ii);
			MetaDataClass mdc = mde.getMetaDataClassByName(c.getName());
			findLiftTimeMethods(mdc);
		}
		
		System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化完成] [总耗时："+(System.currentTimeMillis() - startTime)+"ms]");
		
		thread = new Thread(this,"Thread-"+cl.getName()+"-EntityManager"+"-"+mde.username+"-"+System.currentTimeMillis());
		thread.start();
	}
	
	void checkPrimaryTableAndSecondTableSameRow(){
		
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
	 */
	void checkDataBase(ArrayList<SimpleIndexImpl> indexList) throws Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [开始收集数据库中的信息....]");
		Connection conn = null;
		try{
			conn = getConnection();
			DatabaseMetaData md = conn.getMetaData();
			
			//检查主表
			ResultSet rs = md.getTables(null, null, mde.primaryTable.toUpperCase(), null);
			if(rs.next() == false){
				rs.close();
				String sql = mde.getSqlForCreatePrimaryTable();
				System.out.println("==================创建主表的SQL===================");
				System.out.println(sql);
				
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
					
				}catch(Exception e){
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [主表创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [主表创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				
				stmt.close();
			}else{
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [主表已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				rs.close();
			}
			
			//检查副表
			startTime = System.currentTimeMillis();
			if(mde.secondTable != null){
				rs = md.getTables(null, null, mde.secondTable.toUpperCase(), null);
				if(rs.next() == false){
					rs.close();
					String sql = mde.getSqlForCreateSecondTable();
					System.out.println("==================创建副表的SQL===================");
					System.out.println(sql);
					
					Statement stmt = conn.createStatement();
					try{
						stmt.executeUpdate(sql);
					}catch(Exception e){
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [副表创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						throw e;
					}
					
					//检查主表是否已经存在数据
	
					sql = "select count(1) from "+mde.primaryTable;
					rs = stmt.executeQuery(sql);
					long count = 0;
					if(rs.next()){
						count = rs.getLong(1);
					}
					rs.close();
					if(count > 0){
						sql = "insert into " + mde.secondTable + " ("+mde.id.columnNames[0]+")_SEC select "+mde.id.columnNames[0]+" from "+mde.primaryTable;
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [需要从主表中导入数据到副表中........] ["+sql+"]");
						long ll = System.currentTimeMillis();
						int r = stmt.executeUpdate(sql);
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [从主表中导入数据到副表中完毕] [导入行数:"+r+"] [耗时:"+(System.currentTimeMillis() - ll)+"ms]");
					}
					
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [副表创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					stmt.close();
				}else{
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [副表已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					
					rs.close();
				}
			}
			
			//检查主表字段，不存在的字段创建
			startTime = System.currentTimeMillis();
			rs = md.getColumns(null, null, mde.primaryTable.toUpperCase(), null);
			HashMap<String,Object[]> columnInfo = new HashMap<String,Object[]>();
			while(rs.next()){
				String columnName = rs.getString(4);
				String columnType = rs.getString(6);
				int length = rs.getInt(7);
				int scale = 0;
				if(columnType.equals("NUMBER")){
					scale = rs.getInt(9);
				}
				columnInfo.put(columnName, new Object[]{columnType,length,scale});
			}
			rs.close();
			
			/**
			 * 若主表不包含字段CLASS_ID,则创建CLASS_ID字段 类型为number(20);
			 * alter table xxx add CLASS_ID number(20) added by liuyang at 2012-05-09
			 */
			
			if(!columnInfo.containsKey(mde.CID))
			{
				String sql = "alter table " + mde.primaryTable.toUpperCase() + " add CLASS_ID NUMBER(20)";
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
				}catch(Exception e){
					System.out.println("[SimpleEntityManager][正在检查数据库] [给主表增加字段[CLASS_ID]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				
				System.out.println("[SimpleEntityManager][正在检查数据库] [给主表增加字段[CLASS_ID]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				stmt.close();
			}
			
			
			//ALTER TABLE TEST MODIFY price NUMBER(10,2); 
			//alter table bm_methods add sysdatestr varchar(40);
	
			Iterator<String> it = mde.map.keySet().iterator();
			
			
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable){
					String ccc = f.columnNames[0].toUpperCase();
					Object parameters[] = columnInfo.get(ccc);
	
					if(parameters == null){
						//需要创建新的字段
						String sql = mde.getSqlForAddColumnInPrimaryTable(f);
						Statement stmt = conn.createStatement();
						try{
							stmt.executeUpdate(sql);
						}catch(Exception e){
							System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [给主表增加字段["+f.columnNames[0]+"]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							throw e;
						}
						
						System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [给主表增加字段["+f.columnNames[0]+"]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						stmt.close();
					}else{
						if(parameters[0].equals("VARCHAR2") && f.field.getType() == java.lang.String.class
								&& f.simpleColumn.length() > ((Integer)parameters[1]).intValue() ){
							
							String sql = "ALTER TABLE "+mde.primaryTable+" MODIFY "+f.columnNames[0]+" VARCHAR2("+f.simpleColumn.length()+")";
							Statement stmt = conn.createStatement();
							try{
								stmt.executeUpdate(sql);
							}catch(Exception e){
								System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [修改主表字段["+f.columnNames[0]+"]增加长度至"+f.simpleColumn.length()+"失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
								throw e;
							}
							
							System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [修改主表字段["+f.columnNames[0]+"]增加长度至"+f.simpleColumn.length()+"成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							stmt.close();
						}
					}
				}
			}
			
			//检查副表字段，不存在的字段创建
			if(mde.secondTable != null){
				startTime = System.currentTimeMillis();
				rs = md.getColumns(null, null, mde.secondTable.toUpperCase(), null);
				columnInfo = new HashMap<String,Object[]>();
				while(rs.next()){
					String columnName = rs.getString(4);
					String columnType = rs.getString(6);
					int length = rs.getInt(7);
					int scale = 0;
					if(columnType.equals("NUMBER")){
						scale = rs.getInt(9);
					}
					columnInfo.put(columnName, new Object[]{columnType,length,scale});
				}
				rs.close();
				
				it = mde.map.keySet().iterator();
				while(it.hasNext()){
					String field = it.next();
					MetaDataField f = mde.map.get(field);
					if(f.inPrimaryTable == false){
						for(int i = 0 ; i < f.columnNames.length ; i++){
							String ccc = f.columnNames[i].toUpperCase();
							Object parameters[] = columnInfo.get(ccc);
							if(parameters == null){
								//需要创建新的字段
								String sql = mde.getSqlForAddColumnInSecondTable(f, ccc);
								Statement stmt = conn.createStatement();
								try{
									stmt.executeUpdate(sql);
								}catch(Exception e){
									System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [给副表增加字段["+f.columnNames[i]+"]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
									throw e;
								}
								
								System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [正在检查数据库] [给副表增加字段["+f.columnNames[i]+"]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
								stmt.close();
							}
						}
					}
				}
			}
			startTime = System.currentTimeMillis();
			
			//检查主表中的索引，不存在的创建
			//0放置类型的索引
			//1放置CID的索引 added by liuyang at 2012-05-09
			//其他字段索引从2开始
			if(indexList != null){
				rs = md.getIndexInfo(null, null, mde.primaryTable.toUpperCase(), false, true);
				HashMap<String,ArrayList<String>> indexInfo =  new HashMap<String,ArrayList<String>>();
				while(rs.next()){
					String indexName = rs.getString(6);
					int pos = rs.getInt(8);
					String column = rs.getString(9);
					if(indexName != null){
						ArrayList<String> al = indexInfo.get(indexName);
						if(al == null){
							al = new ArrayList<String>();
							indexInfo.put(indexName, al);
						}
						while(al.size() < pos){
							al.add("");
						}
						al.set(pos-1, column);
					}
				}

				for(int i = 0 ; i < indexList.size() ; i++){
					SimpleIndexImpl sii = indexList.get(i);
					String members[] = sii.members();
					String name = sii.name();
					
					boolean found = false;
					
					Iterator<ArrayList<String>> it2 = indexInfo.values().iterator();
					while(it2.hasNext()){
						ArrayList<String> al = it2.next();
						if(al.size() == members.length){
							boolean b = true;
							for(int j = 0 ; j < al.size() ; j++){
								//if(i > 0){
								//CID和CNC的定义是没有对应metafield的 所以跳过去 added by liuyang at 2012-05-09
								if(i>1){
									MetaDataField f = mde.getMetaDataField(sii.cl, members[j]);//mde.map.get(members[j]);
									
									if(f.columnNames[0].toUpperCase().equals(al.get(j)) == false){
										b = false;
										break;
									}
								}else{
									if(members[j].toUpperCase().equals(al.get(j)) == false){
										b = false;
										break;
									}
								}
							}
							if(b == true){
								found = true;
								break;
							}
						}
					}
					StringBuffer sb = new StringBuffer();
					for(int j = 0 ; j < members.length; j++){
						//由于添加了CID字段 所以从i > 1开始 added by liuyang at 2012-05-09
						//if( i > 0){
						if(i > 1){
							MetaDataField f = mde.getMetaDataField(sii.cl, members[j]);
							sb.append(f.columnNames[0]);
						}else{
							sb.append(members[j]);
						}
						if(j < members.length - 1){
							sb.append(",");
						}
					}
					
					if(found == false){
						String sql = "CREATE INDEX "+name+" ON "+mde.primaryTable+ "("+sb.toString()+")";
						if(sii.compress() > 0){
							sql += " compress " + sii.compress();
						}
						Statement stmt = conn.createStatement();
						try{
							stmt.executeUpdate(sql);
						}catch(Exception e){
							System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [创建索引["+name+"("+sb.toString()+")]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							throw e;
						}
						
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [创建索引["+name+"("+sb.toString()+")]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						stmt.close();
						
					}else{
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [索引"+name+"已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					}
				}
			}
			startTime = System.currentTimeMillis();
			//SEQUENCE
			//select * from all_sequences where sequence_name='SEQ_PLAYER'
			String sql =" select * from all_sequences where sequence_name='"+mde.sequenceName.toUpperCase()+"'";
			Statement stmt = conn.createStatement();
			try{
				rs = stmt.executeQuery(sql);
				if(rs.next() == false){
					sql = "create sequence "+mde.sequenceName+" minvalue "+SEQUENCE_INCREMENT+" maxvalue 999999999999999 start with "+SEQUENCE_INCREMENT+" increment by "+SEQUENCE_INCREMENT+" cache 20";
					stmt.executeUpdate(sql);
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				}else{
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE已经存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				}
			}catch(Exception e){
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				throw e;
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(conn != null){
				conn.close();
			}
		}
	}
	
	/**
	 * 获取一个新的Id，返回的Id会保证从未被此Entity使用过
	 * 
	 * 此方法会从数据库中的SEQUENCE中获取。
	 * 会采用cache机制，预先加载一批存放于内存中。
	 * 所以此方法效率非常高。
	 * 
	 * 格式规则：　1 + 三位serverid + 15位该对象id
	 * 
	 * @return
	 */
	public long nextId() throws Exception{
		nextIdLock.lock();
		try{
			if(SEQUENCE_INCREMENT <= cachedMaxAvaiableId && currAvaiableId < cachedMaxAvaiableId){
				long id = currAvaiableId;
				currAvaiableId++;
				return 1000000000000000000L + serverId * 1000000000000000L + id;
			}
			
			long id = select_seq();
			if(id < SEQUENCE_INCREMENT){
				throw new Exception("SEQ小于设置的最小步长"+SEQUENCE_INCREMENT+"，请检查配置");
			}
			if(id > 999999999999999L){
				throw new Exception("SEQ大于设置的最大值999999999999999L，请检查配置");
			}
			
			cachedMaxAvaiableId = id;
			currAvaiableId = cachedMaxAvaiableId - SEQUENCE_INCREMENT + 1;
			
			id = currAvaiableId;
			currAvaiableId++;
			return 1000000000000000000L + serverId * 1000000000000000L + id;
		}finally{
			nextIdLock.unlock();
		}
	}
	
	
	/**
	 * 判断自动保存机制是否生效
	 * 
	 * 如果生效，SimpleEntityManager内部将有一个线程，定期扫描所有的Entity，
	 * 看看是否要保存某些Entity。
	 * 
	 * @return
	 */
	public boolean isAutoSaveEnabled(){
		return autoSaveEnable;
	}
	
	/**
	 * 启动或者关闭自动保存机制。
	 * 如果启动，需要设置保存线程检测的时间间隔。单位为秒。
	 * 
	 * @param enable
	 * @param checkIntervalInSeconds
	 */
	public void setAutoSave(boolean enable, int checkIntervalInSeconds){
		autoSaveEnable = enable;
		this.checkIntervalInSeconds = checkIntervalInSeconds;
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
	public void notifyNewObject(T t){
		NotifyEvent<T> e = new NotifyEvent<T>(t,null,true);
		synchronized(notifyQueueLock){
			notifyQueue.add(e);
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
	public void notifyFieldChange(T t,String field){
		NotifyEvent<T> e = new NotifyEvent<T>(t,field,false);
		synchronized(notifyQueueLock){
			notifyQueue.add(e);
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
							MyMyInvocationHandler handler = (MyMyInvocationHandler)Proxy.getInvocationHandler(proxy);
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
			int version = 0;
			try {
				 version = mde.version.field.getInt(t);
			}catch (Exception e) {
				if(logger.isWarnEnabled()){
					logger.warn("[通知属性变化] [获取version属性的值，出现异常] ["+mde.cl.getName()+"] [id:"+id+"] [field:"+field+"]",e);
				}
			}
			if(version == 0){
				if(logger.isDebugEnabled()){
					logger.debug("[通知属性变化] [不存在对应Entity的引用数据，可能是新的对象] [{}] [id:{}] [field:{}] [version:{}]",new Object[]{mde.cl.getName(),id,field,version});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.debug("[通知属性变化] [不应该出现的情况,不存在对应Entity的引用数据，但又不是新的对象，可能是SimpleJPA丢失了不应该丢失的引用数据] [{}] [id:{}] [field:{}] [version:{}]",new Object[]{mde.cl.getName(),id,field,version});
				}
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
					logger.warn("[] [处理通知事件] [出现异常] ["+mde.cl.getName()+"] [T:"+e.t+"] [field:"+e.field+"] [newObject:"+e.newObject+"]",ex);
				}
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("[处理通知事件] [成功] ["+mde.cl.getName()+"] [处理数量:"+al.size()+"] [新队列中数量:"+notifyQueue.size()+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
		}
		
	}
	
	/**
	 * 通过Id查询指定的Entity，
	 * 如果Entity存在于内存中，直接返回。
	 * 否则，将会查询数据库，并且返回对应Entity
	 * 
	 * 如果查询不到，返回null。
	 * 如果查询过程中，发生了异常，比如SQLException，将抛出异常
	 * 
	 * 此方法是线程安全的，对于同一个id，此方法会保证只返回同一个对象。
	 * SimpleEntityManager采用WeakReference方法引用已经返回的对象。
	 * 当返回的对象被GC回收后，SimpleEntityManager也将清除响应的数据。
	 * 
	 * 这些数据包括：
	 *     记录此对象各个属性的修改标记，以及各个属性上一次保存的时间。
	 *     
	 * @param id
	 * @return
	 */
	public T find(long id) throws Exception{
		
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
				
				T t = select_object(id);
				
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
	 * 判断某个id对应的对象是否存在于内存中。
	 * 即已经加载并且仍然在内存中。
	 * 
	 * @param id
	 * @return
	 */
	public boolean contains(long id){
		
		this.handleNotifyEvent();
		
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
	
	
	
	/**
	 * 立即保存某个Entity，此方法会操作数据库。
	 * 如果此Entity是新的，那么就插入到表中。
	 * 如果此Entity是通过find或者query返回的，那么就更新到表中。
	 * 更新那些字段，是有应该程序设置的，参见属性修改方法。
	 * 
	 * 此方法不需要应用层经常调用，
	 * SimpleEntityManager内部有一个线程，定期检查那些Entity需要保存，
	 * 并且会自动保存。
	 * 
	 * 只有一种情况，需要外部调用此方法，就是此Entity即将从内存中清楚，
	 * 比如从应用层的cache中移除，或者系统准备重启。
	 * 
	 * 如果出现底层的异常，会抛出来。
	 * 
	 * 注意此方法可能会非常耗时，比如某个json串的字段，很长。
	 * 会导致创建新的字段出来，以保证数据能存入到数据库中。
	 * 
	 * 此方法有一个bug，需要特别注意：
	 * 此bug在下面的情况下，可能会丢失数据。
	 * 1. 上层应用没有缓存对象，所有对象都是现用现取，用完就丢弃。
	 *    这样上层应用必须是调用save方法保存数据
	 * 2. 但同一个对象，同时被两个线程同时保持时，会出现问题。
	 *    也就是有两个线程，对用一个对象，同时调用此方法。
	 *    其中一个线程，会认为另外一个线程正在保存，
	 *    导致冲突，就不再保存，直接返回。
	 *    
	 * 所以，上层在使用此方法时，应尽量避免这种情况，因为这种情况必然导致数据冲突或者数据丢失。
	 * 我们选择了丢失数据。当然，所谓丢失，只是理论上的，实际是否丢失，无法确认。
	 * 这依据冲突的时机。   
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void save(T t) throws Exception{
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
//					Iterator<String> it = ee.map.keySet().iterator();
//					while(it.hasNext()){
//						String field = it.next();
//						ModifyInfo mi = ee.map.get(field);
//						if(mi != null && mi.dirty){
//							mi.dirty = false;
//						}
//					}
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
					
//					Iterator<String> it = ee.map.keySet().iterator();
//					while(it.hasNext()){
//						String field = it.next();
//						ModifyInfo mi = ee.map.get(field);
//						if(mi != null && mi.dirty){
//							al.add(field);
//							mi.dirty = false;
//						}
//					}
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
	 * 立即批量保存某些Entity，此方法会操作数据库。
	 * 如果此Entity是新的，那么就插入到表中。
	 * 如果此Entity是通过find或者query返回的，那么就更新到表中。
	 * 更新那些字段，是有应该程序设置的，参见属性修改方法。
	 * 
	 * 此方法不需要应用层经常调用，
	 * SimpleEntityManager内部有一个线程，定期检查那些Entity需要保存，
	 * 并且会自动保存。
	 * 
	 * 只有一种情况，需要外部调用此方法，就是此Entity即将从内存中清楚，
	 * 比如从应用层的cache中移除，或者系统准备重启。
	 * 
	 * 如果出现底层的异常，会抛出来。
	 * 
	 * 注意此方法可能会非常耗时，比如某个json串的字段，很长。
	 * 会导致创建新的字段出来，以保证数据能存入到数据库中。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void batchSave(T[] ts) throws Exception{
		for(int i = 0 ; i < ts.length ; i++){
			save(ts[i]);
		}
	}
	
	/**
	 * 立即保存某个Entity，此方法会操作数据库。
	 * 如果此Entity是新的，那么就插入到表中。
	 * 如果此Entity是通过find返回的，那么就更新到表中。
	 * flush会强制保存所有的字段，无论这些字段是否修改。
	 * 
	 * 此方法不需要应用层经常调用，
	 * SimpleEntityManager内部有一个线程，定期检查那些Entity需要保存，
	 * 并且会自动保存。
	 * 
	 * 只有一种情况，需要外部调用此方法，就是此Entity即将从内存中清楚，
	 * 比如从应用层的cache中移除，或者系统准备重启。
	 * 如果出现底层的异常，会抛出来。
	 * 
	 * 注意此方法可能会非常耗时，比如某个json串的字段，很长。
	 * 会导致创建新的字段出来，以保证数据能存入到数据库中。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void flush(T t) throws Exception{
		
		
		
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
			ArrayList<Field> al = Utils.getPersistentFields(t.getClass());
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
	
	/**
	 * 立即删除某一个对象，此方法会操作数据库
	 * 并且会从内存中清楚对应的数据。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void remove(T t) throws Exception{
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
			boolean b = delete_object(t);
			
			if(b){
				mdc = mde.getMetaDataClassByName(t.getClass().getName());
				while(mdc != null){
					if(mdc.postRemove != null){
						try{
							mdc.postRemove.invoke(t, new Object[]{});
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.info("[执行SQL] [删除对象] [调用postRemove方法出现异常]",e);
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
	}
	
	
	
	/**
	 * 批量删除对象，此方法会操作数据库
	 * 并且会从内存中清除对应的数据。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void remove(T[] ts) throws Exception{
		
		for(T t :  ts)
		{
			if(t == null)
			{
				logger.warn("[批量删除对象] [失败] [数组中出现为null的对象]");
				return;
			}
			
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
		}
		
		try{
			boolean b = batch_delete_object(ts);
			
			if(b){
				for(T t : ts)
				{
					MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
					while(mdc != null){
						if(mdc.postRemove != null){
							try{
								mdc.postRemove.invoke(t, new Object[]{});
							}catch(Exception e){
								if(logger.isWarnEnabled()){
									logger.info("[执行SQL] [删除对象] [调用postRemove方法出现异常]",e);
								}
							}
							break;
						}
						mdc = mdc.parent;
					}
				}
			}
		}finally{
			
			for(T t : ts)
			{
				entityModifyLock.lock();
				long id = mde.id.field.getLong(t);
				EntityEntry<T> ee = entityModifedMap.get(id);
				if(ee != null)
					ee.savingLock = false;
				try{
					entityModifedMap.remove(id);
				}finally{
					entityModifyLock.unlock();
				}
			}
			
		}
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//查询接口
	
	/**
	 * 查询有多少个对象，此方法只表示有多少条记录。
	 */
	public long count() throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + username + "." + mde.primaryTable;
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			r = rs.getLong(1);
			rs.close();
			stmt.close();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,});
				}
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [查询个数] [失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [查询个数] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [查询个数] [关闭连接失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [查询个数] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 查询有多少个特定类型的对象，此方法只表示有多少条记录。
	 */
	public long count(String className) throws Exception{
		long startTime = System.currentTimeMillis();
		
		MetaDataClass mdc = mde.getMetaDataClassByName(className);
		if(mdc == null)
		{
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [查询特定类型对象个数] [失败] [类名对应的MetaDataClass不存在] ["+className+"]");
			}
			throw new Exception("类名对应的MetaDataClass不存在");
		}
		String sql = "select count(*) from " + username + "."+ mde.primaryTable + " where " + mde.CNC + "='"+className+"' or "+mde.CID+
				"="+mdc.cid;
		
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			r = rs.getLong(1);
			rs.close();
			stmt.close();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询特定类型对象个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [查询特定类型对象个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [查询特定类型对象个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询特定类型对象个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [查询特定类型对象个数] [失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [查询特定类型对象个数] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [查询特定类型对象个数] [关闭连接失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [查询特定类型对象个数] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	private String filterWhereSql(String sql,String description,Class<?> cl,String whereSql,boolean addTablePrefix) throws Exception{
		if(whereSql == null || whereSql.trim().length() == 0) return "";
		long startTime = System.currentTimeMillis();
		String fields[] = null;
		try{
			fields = BooleanExpressionUtil.checkExpression(whereSql.replaceAll(" like ", " @ ").replaceAll(" LIKE ", " @ "));
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [{}] [条件表达式语法错误] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,whereSql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件查询个数] [条件表达式语法错误] ["+mde.cl.getName()+"]",e);
			}
			throw e;
		}
		String where = whereSql;
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField f = mde.getMetaDataField(cl, fields[i]);
			if(f != null ){
				if(f.inPrimaryTable){
					if(addTablePrefix)
						where = where.replaceAll(fields[i], mde.primaryTable+"."+f.columnNames[0]);
					else
						where = where.replaceAll(fields[i], f.columnNames[0]);
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [{}] [非主表字段"+fields[i]+"不能作为查询条件] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,whereSql,System.currentTimeMillis()-startTime});
					}
					throw new Exception("非主表字段"+fields[i]+"不能作为查询条件");
				}
			}else if(fields[i].equals(mde.id.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.id.columnNames[0]);
				else
					where = where.replaceAll(fields[i], mde.id.columnNames[0]);
			}else if(fields[i].equals(mde.version.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.version.columnNames[0]);
				else
					where = where.replaceAll(fields[i], mde.version.columnNames[0]);
			}else if(fields[i].equals("?")){
				//参数查询
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [{}] [查询字段"+fields[i]+"不是此类的属性] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,whereSql,System.currentTimeMillis()-startTime});
				}
				throw new Exception("查询字段"+fields[i]+"不是此类的属性");
			}
		}
		return where;
	}
	
	private String filterOrderBySql(String sql,String description,Class<?> cl,String orderBySql,boolean addTablePrefix) throws Exception{
		if(orderBySql == null || orderBySql.trim().length() == 0) return "";
		long startTime = System.currentTimeMillis();
		String fields[] = null;
		ArrayList<String> al = new ArrayList<String>();
		String ss[] = orderBySql.split(",");
		for(int i = 0 ; i < ss.length ; i++){
			String kk[] = ss[i].split(" ");
			al.add(kk[0].trim());
		}
		fields = al.toArray(new String[0]);
		
		String where = orderBySql;
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField f = mde.getMetaDataField(cl, fields[i]);
			if(f != null ){
				if(f.inPrimaryTable){
					if(addTablePrefix)
						where = where.replaceAll(fields[i], mde.primaryTable+"."+f.columnNames[0]);
					else
						where = where.replaceAll(fields[i], f.columnNames[0]);
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [{}] [非主表字段"+fields[i]+"不能作为排序条件] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,orderBySql,System.currentTimeMillis()-startTime});
					}
					throw new Exception("非主表字段"+fields[i]+"不能作为排序条件");
				}
			}else if(fields[i].equals(mde.id.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.id.columnNames[0]);
				else
					where = where.replaceAll(fields[i], mde.id.columnNames[0]);
			}else if(fields[i].equals(mde.version.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.version.columnNames[0]);
				else
					where = where.replaceAll(fields[i], mde.version.columnNames[0]);
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [{}] [查询字段"+fields[i]+"不是此类的属性] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,orderBySql,System.currentTimeMillis()-startTime});
				}
				throw new Exception("查询字段"+fields[i]+"不是此类的属性");
			}
		}
		return where;
	}
	
	/**
	 * 查询某个条件下有多少个对象，条件是sql语句中where部分，不包括where
	 * 只支持对本表的查询，不支持多表联合查询。
	 * 
	 * @param whereSql
	 * @return
	 */
	
	public long count(Class<?> cl,String whereSql) throws Exception{
		
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + username + "."+ mde.primaryTable;
		String where = filterWhereSql(sql,"带条件查询个数",cl,whereSql,false);
		if(where.length() > 0){
			sql += " where " + where;
		}
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			r = rs.getLong(1);
			rs.close();
			stmt.close();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件查询个数] [失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件查询个数] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件查询个数] [关闭连接失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件查询个数] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}

	}
	
	private void checkParamValueTypes(Object[] paramValues) throws Exception{
		for(int i = 0 ; paramValues != null && i < paramValues.length ; i++){
			Class<?> type = paramValues[i].getClass();
			if(type == Byte.class || type == Byte.TYPE){
			}else if(type == Short.class || type == Short.TYPE){
			}else if(type == Integer.class || type == Integer.TYPE){
			}else if(type == Long.class || type == Long.TYPE){
			}else if(type == Float.class || type == Float.TYPE){
			}else if(type == Double.class || type == Double.TYPE){
			}else if(type == String.class){
			}else if(type == java.util.Date.class){
			}else if(type == java.sql.Date.class){
			}else{
				throw new Exception("参数类型["+type.getName()+"]不支持");
			}
		}
	}
	
	private void setParamValues(PreparedStatement pstmt,int parameterIndex,Object[] paramValues) throws SQLException{
		for(int i = 0 ; paramValues != null && i < paramValues.length ; i++){
			Class<?> type = paramValues[i].getClass();
			if(type == Byte.class || type == Byte.TYPE){
				pstmt.setInt(parameterIndex, ((Byte)paramValues[i]).intValue());
				parameterIndex++;
			}else if(type == Short.class || type == Short.TYPE){
				pstmt.setInt(parameterIndex, ((Short)paramValues[i]).intValue());
				parameterIndex++;
			}else if(type == Integer.class || type == Integer.TYPE){
				pstmt.setInt(parameterIndex, ((Integer)paramValues[i]).intValue());
				parameterIndex++;
			}else if(type == Long.class || type == Long.TYPE){
				pstmt.setLong(parameterIndex, ((Long)paramValues[i]).longValue());
				parameterIndex++;
			}else if(type == Float.class || type == Float.TYPE){
				pstmt.setFloat(parameterIndex, ((Float)paramValues[i]).floatValue());
				parameterIndex++;
			}else if(type == Double.class || type == Double.TYPE){
				pstmt.setDouble(parameterIndex, ((Double)paramValues[i]).doubleValue());
				parameterIndex++;
			}else if(type == String.class){
				pstmt.setString(parameterIndex, (String)paramValues[i]);
				parameterIndex++;
			}else if(type == java.util.Date.class){
				pstmt.setTimestamp(parameterIndex,new java.sql.Timestamp(((java.util.Date)paramValues[i]).getTime()));
				parameterIndex++;
			}else if(type == java.sql.Date.class){
				pstmt.setTimestamp(parameterIndex,new java.sql.Timestamp(((java.sql.Date)paramValues[i]).getTime()));
				parameterIndex++;
			}
		}
	}
	public long count(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + username + "."+ mde.primaryTable;
		String where = filterWhereSql(sql,"带条件查询个数",cl,preparedWhereSql,false);
		if(where.length() > 0){
			sql += " where " + where;
		}
		
		if(paramValues == null) paramValues = new Object[0];
		
		try{
			checkParamValueTypes(paramValues);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询个数] [失败] ["+e.getMessage()+"] [{}] [count:{}] [where:{}] [cost:{}ms]",new Object[]{cl.getName(),0,preparedWhereSql,System.currentTimeMillis()-startTime});
			}
			throw e;
		}
		
		
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			setParamValues(pstmt,1,paramValues);
			
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			r = rs.getLong(1);
			rs.close();
			pstmt.close();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数查询个数] [成功] [{}] [count:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数查询个数] [成功] [{}] [count:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询个数] [失败] [{}] [count:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,toString(paramValues),System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件参数查询个数] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件参数查询个数] [关闭连接失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件参数查询个数] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String whereSql) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select "+mde.id.columnNames[0]+" from "+ username + "." + mde.primaryTable;
		String where = filterWhereSql(sql,"带条件查询ID列表",cl,whereSql,false);
		if(where.length() > 0){
			sql += " where " + where;
		}
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Long> al = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(1);
				al.add(id);
			}
			rs.close();
			stmt.close();
			
			long r[] = new long[al.size()];
			for(int i = 0 ; i < al.size() ; i++){
				r[i] = al.get(i);
			}

			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件查询ID列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件查询ID列表] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件查询ID列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	/**
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select "+mde.id.columnNames[0]+" from " + username + "."+ mde.primaryTable;
		String where = filterWhereSql(sql,"带条件查询ID列表",cl,preparedWhereSql,false);
		if(where.length() > 0){
			sql += " where " + where;
		}
		if(paramValues == null) paramValues = new Object[0];
		
		try{
			checkParamValueTypes(paramValues);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询ID列表] [失败] ["+e.getMessage()+"] [{}] [count:{}] [where:{}] [cost:{}ms]",new Object[]{cl.getName(),0,preparedWhereSql,System.currentTimeMillis()-startTime});
			}
			throw e;
		}
	
		Connection conn = null;
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			this.setParamValues(pstmt, 1, paramValues);
			
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Long> al = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(1);
				al.add(id);
			}
			rs.close();
			pstmt.close();
			
			long r[] = new long[al.size()];
			for(int i = 0 ; i < al.size() ; i++){
				r[i] = al.get(i);
			}

			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,toString(paramValues),System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件参数查询ID列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件参数查询ID列表] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件参数查询ID列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	/**
	 * 分页查询，查询满足某个条件的所有的Ids
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String whereSql,String orderSql,long start,long end) throws Exception{
		
		long startTime = System.currentTimeMillis();
		String sql = "select "+mde.id.columnNames[0]+" from " + username + "."+ mde.primaryTable;
		String where = filterWhereSql(sql,"带条件分页查询ID列表",cl,whereSql,false);
		String orderby = filterOrderBySql(sql,"带条件分页查询ID列表",cl,orderSql,false);
		
		if(where.length() > 0){
			sql += " where " + where;
		}
		if(orderby.length() > 0){
			sql += " order by " + orderby;
		}
		sql = "select * from (select T.*,rownum RR from ("+sql+") T) where RR >= "+start+" and RR < "+end+"";
		
		
		
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Long> al = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(1);
				al.add(id);
			}
			rs.close();
			stmt.close();
			
			long r[] = new long[al.size()];
			for(int i = 0 ; i < al.size() ; i++){
				r[i] = al.get(i);
			}
		
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件分页查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件分页查询ID列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件分页查询ID列表] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件分页查询ID列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 分页查询，查询满足某个条件的所有的Ids
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql,long start,long end) throws Exception{
		
		long startTime = System.currentTimeMillis();
		String sql = "select "+mde.id.columnNames[0]+" from " + username + "."+ mde.primaryTable;
		String where = filterWhereSql(sql,"带条件分页查询ID列表",cl,preparedWhereSql,false);
		String orderby = filterOrderBySql(sql,"带条件分页查询ID列表",cl,orderSql,false);
		
		if(where.length() > 0){
			sql += " where " + where;
		}
		if(orderby.length() > 0){
			sql += " order by " + orderby;
		}
		sql = "select * from (select T.*,rownum RR from ("+sql+") T) where RR >= ? and RR < ?";
		
		if(paramValues == null) paramValues = new Object[0];
		
		try{
			checkParamValueTypes(paramValues);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询ID列表] [失败] ["+e.getMessage()+"] [{}] [count:{}] [where:{}] [cost:{}ms]",new Object[]{cl.getName(),0,preparedWhereSql,System.currentTimeMillis()-startTime});
			}
			throw e;
		}
		
	
		
		Connection conn = null;
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			Object newValues[] = new Object[paramValues.length+2];
			System.arraycopy(paramValues, 0, newValues, 0, paramValues.length);
			newValues[paramValues.length] = start;
			newValues[paramValues.length+1] = end;
			
			this.setParamValues(pstmt, 1, newValues);
			
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Long> al = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(1);
				al.add(id);
			}
			rs.close();
			pstmt.close();
			
			long r[] = new long[al.size()];
			for(int i = 0 ; i < al.size() ; i++){
				r[i] = al.get(i);
			}
		
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,toString(paramValues),System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件参数分页查询ID列表] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件参数分页查询ID列表] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件参数分页查询ID列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	/**
	 * 分页查询，查询满足某个条件的所有的Entity
	 * 如果whereSql为null，表示要查询所有的Entity，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * 如果end - start > 10000，将抛出IllegalArgumentException
	 * 
	 * @param whereSql
	 * @return
	 */
	public List<T> query(Class<?> cl,String whereSql,String orderSql,long start,long end) throws Exception{
		long startTime = System.currentTimeMillis();
		String sessionId = StringUtil.randomString(10);
		if(start < 1 ) throw new IllegalArgumentException("分页必须从1开始，小于1的数没有意义");
		if(end - start > 10000 ) throw new IllegalArgumentException("超过最大个数限制，每次最多可以加载10000个对象");
		int totalBytes[] = new int[1];
		String sql = "";
		String where = "";
		String orderby = "";
		if(mde.secondTable == null){
			where = filterWhereSql(sql,"带条件分页查询对象列表",cl,whereSql,false);
			orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,false);
		}else{
			where = filterWhereSql(sql,"带条件分页查询对象列表",cl,whereSql,true);
			orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,true);
		}
		sql = mde.getSqlForSelectObjectsByNativeConditions(where,orderby);
		
		sql = "select * from (select TT.*,rownum RR from ("+sql+") TT) where RR >= "+start+" and RR < "+end+"";
		
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			ArrayList<T> al = new ArrayList<T>();
			ArrayList<Long> al2 = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(mde.id.columnNames[0]);
				try{
					T t = this.construct_object_from_resultset(sessionId,id, rs, 2,totalBytes);
					if(t != null){
						al.add(t);
						al2.add(id);
					}
				}catch(Exception e){
					rs.close();
					stmt.close();
					throw e;
				}
			}
			rs.close();
			stmt.close();

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
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询对象列表,成功", OperatoinTrackerService.ACTION_READ, retList.size() ,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),whereSql,orderSql,start,end,retList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),whereSql,orderSql,start,end,retList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}
			return retList;
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件分页查询对象列表] [失败] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),whereSql,orderSql,start,end,sql,"-",System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件分页查询对象列表] [失败] [sessionId:"+sessionId+"] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件分页查询对象列表] [关闭连接失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),whereSql,orderSql,start,end,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件分页查询对象列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}

	
	
	/**
	 * 分页查询，查询满足某个条件的所有的Entity
	 * 如果whereSql为null，表示要查询所有的Entity，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * 如果end - start > 10000，将抛出IllegalArgumentException
	 * 
	 * @param whereSql
	 * @return
	 */
	public List<T> query(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql,long start,long end) throws Exception{
		long startTime = System.currentTimeMillis();
		String sessionId = StringUtil.randomString(10);
		if(start < 1 ) throw new IllegalArgumentException("分页必须从1开始，小于1的数没有意义");
		if(end - start > 10000 ) throw new IllegalArgumentException("超过最大个数限制，每次最多可以加载10000个对象");
		int totalBytes[] = new int[1];
		String sql = "";
		String where = "";
		String orderby = "";
		if(mde.secondTable == null){
			where = filterWhereSql(sql,"带条件分页查询对象列表",cl,preparedWhereSql,false);
			orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,false);
		}else{
			where = filterWhereSql(sql,"带条件分页查询对象列表",cl,preparedWhereSql,true);
			orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,true);
		}
		sql = mde.getSqlForSelectObjectsByNativeConditions(where,orderby);
		
		sql = "select * from (select TT.*,rownum RR from ("+sql+") TT) where RR >= ? and RR < ?";
		
		if(paramValues == null) paramValues = new Object[0];
		
		try{
			checkParamValueTypes(paramValues);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询对象列表] [失败] ["+e.getMessage()+"] [{}] [count:{}] [where:{}] [cost:{}ms]",new Object[]{cl.getName(),0,preparedWhereSql,System.currentTimeMillis()-startTime});
			}
			throw e;
		}
		
	
		
		Connection conn = null;
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			Object newValues[] = new Object[paramValues.length+2];
			System.arraycopy(paramValues, 0, newValues, 0, paramValues.length);
			newValues[paramValues.length] = start;
			newValues[paramValues.length+1] = end;
			
			this.setParamValues(pstmt, 1, newValues);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<T> al = new ArrayList<T>();
			ArrayList<Long> al2 = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(mde.id.columnNames[0]);
				try{
					T t = this.construct_object_from_resultset(sessionId,id, rs, 2,totalBytes);
					if(t != null){
						al.add(t);
						al2.add(id);
					}
				}catch(Exception e){
					rs.close();
					pstmt.close();
					throw e;
				}
			}
			rs.close();
			pstmt.close();

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
								logger.info("[执行SQL] [带条件参数分页查询对象列表] [调用postLoad方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}
			}
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询对象列表,成功", OperatoinTrackerService.ACTION_READ, retList.size() ,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}
			return retList;
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询对象列表] [失败] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,sql,toString(paramValues),System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件参数分页查询对象列表] [失败] [sessionId:"+sessionId+"] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件参数分页查询对象列表] [关闭连接失败] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,start,end,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件参数分页查询对象列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	public List<T> query(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql) throws Exception{
		long startTime = System.currentTimeMillis();
		String sessionId = StringUtil.randomString(10);
		int totalBytes[] = new int[1];
		String sql = "";
		String where = "";
		String orderby = "";
		if(mde.secondTable == null){
			where = filterWhereSql(sql,"带条件无分页查询对象列表",cl,preparedWhereSql,false);
			orderby = filterOrderBySql(sql,"带条件无分页查询对象列表",cl,orderSql,false);
		}else{
			where = filterWhereSql(sql,"带条件无分页查询对象列表",cl,preparedWhereSql,true);
			orderby = filterOrderBySql(sql,"带条件无分页查询对象列表",cl,orderSql,true);
		}
		sql = mde.getSqlForSelectObjectsByNativeConditions(where,orderby);
		
		
		if(paramValues == null) paramValues = new Object[0];
		
		try{
			checkParamValueTypes(paramValues);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询对象列表] [失败] ["+e.getMessage()+"] [{}] [count:{}] [where:{}] [cost:{}ms]",new Object[]{cl.getName(),0,preparedWhereSql,System.currentTimeMillis()-startTime});
			}
			throw e;
		}
		
	
		
		Connection conn = null;
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			
			this.setParamValues(pstmt, 1, paramValues);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<T> al = new ArrayList<T>();
			ArrayList<Long> al2 = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(mde.id.columnNames[0]);
				try{
					T t = this.construct_object_from_resultset(sessionId,id, rs, 2,totalBytes);
					if(t != null){
						al.add(t);
						al2.add(id);
					}
				}catch(Exception e){
					rs.close();
					pstmt.close();
					throw e;
				}
			}
			rs.close();
			pstmt.close();

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
								logger.info("[执行SQL] [带条件参数无分页查询对象列表] [调用postLoad方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}
			}
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询对象列表,成功", OperatoinTrackerService.ACTION_READ, retList.size() ,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数无分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [ret:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,retList.size(),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数无分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [ret:{}] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,retList.size(),sql,toString(paramValues),System.currentTimeMillis()-startTime});
				}
			}
			return retList;
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"无分页"+orderSql+"查询对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数无分页查询对象列表] [失败] [sessionId:{}] [{}] [where:{}] [order:{}] [ret:-] [sql:{}] [param:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,sql,toString(paramValues),System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [带条件参数无分页查询对象列表] [失败] [sessionId:"+sessionId+"] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [带条件参数无分页查询对象列表] [关闭连接失败] [{}] [where:{}] [order:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),preparedWhereSql,orderSql,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [带条件参数无分页查询对象列表] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	
	
	
	
	private boolean needAutoSaveImmdiately = false;
	private boolean destroy = false;
	private boolean running = false;
	
	public long repIdleTime = System.currentTimeMillis();
	
	public void run() {
		running = true;
		while(Thread.currentThread().isInterrupted() == false && autoSaveEnable && !readOnly){
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
				
				if(System.currentTimeMillis() - repIdleTime > 1000 * 5) {
					repIdleTime = System.currentTimeMillis();
					pool.reapIdleConnections();
				}
				
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
	 * 销毁EntityManager，
	 * 此方法会将没有保存的数据全部保存到数据库中
	 * 并释放所有的数据库连接
	 */
	public void destroy(){
		
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
	
	/**
	 * 自动保存数据
	 * 
	 * 
	 */
	private int batchSaveOrUpdateSize = 50;
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
					
					if(e instanceof SQLIntegrityConstraintViolationException)
					{
						al.remove(i);
						i--;
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
				
				if( map.size() >= batchSaveOrUpdateSize) break;
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
					
					if(ee.saveFailed && e instanceof SQLIntegrityConstraintViolationException)
					{
						it.remove();
						logger.warn("[批量保存失败,有主键冲突] [删除冲突的数据] ["+mde.cl.getName()+"] [id:"+id+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]",e);
					}
					else
					{
						
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
	 * 此方法将内存中所有的对象，与数据库中对比，检查哪些字段发生的变化，但是没有设置修改标记
	 * 并将信息返回
	 * 
	 * 此方法可能非常耗费系统资源。
	 * 
	 * 返回值：
	 *     类名，ID，属性，属性类型，数据库中的值，内存中的值
	 * @return
	 * @throws Exception
	 */
	
	public ArrayList<String[]> checkAllObjectsFieldModify() throws Exception{
		ArrayList<String[]> retList = new ArrayList<String[]>();
		long startTime = System.currentTimeMillis();
		Long ids[] = entityModifedMap.keySet().toArray(new Long[0]);
		if(logger.isWarnEnabled()){
			logger.warn("[检查字段修改状态] [内存中一共有"+ids.length+"个对象] [开始...] [{}]",new Object[]{mde.cl.getName()});
		}
		
		int count = 0;
		for(int i = 0 ; count < 1024 && i < ids.length ; i++){
			if(this.contains(ids[i])){
				try {
					T newT = find(ids[i]);
					T oldT = select_object(ids[i]);
					EntityEntry<T> ee = entityModifedMap.get(ids[i]);
					ArrayList<Field> al = Utils.getPersistentFields(newT.getClass());
					boolean hasError = false;
					for(int j = 0 ; j < al.size() ; j++){
						Field f = al.get(j);
						if(f.equals(mde.version.field)) continue;
						f.setAccessible(true);
						ModifyInfo mi = ee.getModifyInfo(f.getName());
						if(mi == null || mi.dirty == false){
							Class<?> cl = f.getType();
							if(Utils.isPrimitiveType(cl)){
								Object newO = f.get(newT);
								Object oldO = f.get(oldT);
								if(newO != null && newO.equals(oldO) ==false){
									//有修改为通知
									retList.add(new String[]{
											newT.getClass().getName(),
											String.valueOf(ids[i]),
											f.getName(),
											f.getGenericType().toString(),
											oldO != null? oldO.toString():"null",
											newO.toString()		
									});
									hasError = true;
									
									if(logger.isWarnEnabled()){
										logger.warn("[检查字段修改状态] [检查第"+(i+1)+"/"+ids.length+"个] [存在修改不通知] [{}] [id:{}] [{}] [{}] [{}] [{}] [已耗时：{}]",new Object[]{newT.getClass().getName(),
												String.valueOf(ids[i]),
												f.getName(),
												f.getGenericType().toString(),
												oldO != null? oldO.toString():"null",
												newO.toString()	,System.currentTimeMillis() - startTime});
									}
									
								}else if(newO == null && oldO != null){
									//有修改为通知
									retList.add(new String[]{
											newT.getClass().getName(),
											String.valueOf(ids[i]),
											f.getName(),
											f.getGenericType().toString(),
											oldO.toString(),
											"null"
									});
									hasError = true;
									if(logger.isWarnEnabled()){
										logger.warn("[检查字段修改状态] [检查第"+(i+1)+"/"+ids.length+"个] [存在修改不通知] [{}] [id:{}] [{}] [{}] [{}] [{}] [已耗时：{}]",new Object[]{newT.getClass().getName(),
												String.valueOf(ids[i]),
												f.getName(),
												f.getGenericType().toString(),
												oldO.toString(),
												"null"	,System.currentTimeMillis() - startTime});
									}
								}
							}else{
								String newO = JsonUtil.jsonFromObject(f.get(newT),f.getGenericType());
								String oldO = JsonUtil.jsonFromObject(f.get(oldT),f.getGenericType());
								if(newO.equals(oldO) == false){
									//有修改为通知
									retList.add(new String[]{
											newT.getClass().getName(),
											String.valueOf(ids[i]),
											f.getName(),
											f.getGenericType().toString(),
											oldO,
											newO
									});
									hasError = true;
									if(logger.isWarnEnabled()){
										logger.warn("[检查字段修改状态] [检查第"+(i+1)+"/"+ids.length+"个] [存在修改不通知] [{}] [id:{}] [{}] [{}] [{}] [{}] [已耗时：{}]",new Object[]{newT.getClass().getName(),
												String.valueOf(ids[i]),
												f.getName(),
												f.getGenericType().toString(),
												oldO,
												newO,System.currentTimeMillis() - startTime});
									}
								}
							}
						}
						
						
					}
					if(hasError) count++;
				} catch (Exception e) {
					throw e;
				}
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[检查字段修改状态] [内存中一共有"+retList.size()+"个对象存在修改不通知] [结束] [{}] [耗时：{}]",new Object[]{mde.cl.getName(),System.currentTimeMillis() - startTime});
		}
		return retList;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// 
	// 以下是底层数据库操作
	//
	
	
	
	
	public Connection getConnection() throws SQLException{
		if(pool != null){
			return pool.getConnection();
		}else{
			throw new SQLException("connection pool is null");
		}
	}
	
	/**
	 * 设置第二个表的参数
	 * @param pstmt
	 * @param parameterIndex
	 * @param field
	 * @param columnNames
	 * @param value
	 * @throws Exception
	 */
	int setPreparedStatementParameterForSecondTable(PreparedStatement pstmt,int parameterIndex,String field,String columnNames[],Object value,Type genericType) throws Exception{
		String s = "";
		if(value != null){
			s = JsonUtil.jsonFromObject(value,genericType);
		}
		String columnValues[] = new String[columnNames.length];
		int index = 0;
		char chars[] = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		int currentLen = 0;
		
		int totalLen = 0;
		for(int i = 0 ; i < chars.length ; i++){
			int c = 0;
			if(chars[i] >= 0 && chars[i] <= 255){
				c= 1;
			}else{
				c=3;
			}
			totalLen += c;
			if(currentLen + c >= 4000){
				if(index >= columnValues.length){
					throw new java.lang.IllegalArgumentException("属性["+field+"]转化为JSON串后的长度[>"+totalLen+"]超过预先定义的最大长度["+(columnNames.length * 4000)+"]");
				}
				columnValues[index] = sb.toString();
				index++;

				sb = new StringBuffer();
				currentLen = 0;
			}
			
			sb.append(chars[i]);
			currentLen += c;
		}
		if(index >= columnValues.length){
			if(index >= columnValues.length){
				throw new java.lang.IllegalArgumentException("属性["+field+"]转化为JSON串后的长度[>"+totalLen+"]超过预先定义的最大长度["+(columnNames.length * 4000)+"]");
			}
		}
		columnValues[index] = sb.toString();
		index++;
		for(int i = index ; i < columnValues.length ; i++){
			columnValues[i] = "";
		}
		
		for(int j = 0 ; j < columnNames.length ; j++){
			pstmt.setString(parameterIndex, columnValues[j]);
			parameterIndex++;
		}
		return totalLen;
	}
	
	/**
	 * 设置第一个表的参数
	 * 
	 * @param pstmt
	 * @param parameterIndex
	 * @param field
	 * @param cl
	 * @param value
	 * @throws Exception
	 */
	
	int setPreparedStatementParameter(PreparedStatement pstmt,int parameterIndex,String field,Class<?> cl,Object value) throws Exception{
		int totalLen = 0;
		if(cl == Boolean.TYPE || cl == Boolean.class){
			//我们以CHAR（1）标识boolean，T标识true，其他标识false
			Boolean s = (Boolean)value;
			if(s != null && s.booleanValue()){
				pstmt.setString(parameterIndex, "T");
			}else{
				pstmt.setString(parameterIndex, "F");
			}
			parameterIndex++;
			totalLen = 1;
		}else if(cl == Byte.TYPE || cl == Byte.class){
			Byte s = (Byte)value;
			if(s == null) s = 0;
			pstmt.setByte(parameterIndex, s.byteValue());
			parameterIndex++;
			totalLen = 1;
		}else if(cl == Short.TYPE || cl == Short.class){
			Short s = (Short)value;
			if(s == null) s = 0;
			pstmt.setShort(parameterIndex, s.shortValue());
			parameterIndex++;
			totalLen = 2;
		}else if(cl == Integer.TYPE || cl == Integer.class){
			Integer s = (Integer)value;
			if(s == null) s = 0;
			pstmt.setInt(parameterIndex, s.intValue());
			parameterIndex++;
			totalLen =4;
		}else if(cl == Character.TYPE || cl == Character.class){
			String s = (String)value;
			pstmt.setString(parameterIndex, s);
			parameterIndex++;
			totalLen = 1;
		}else if(cl == Long.TYPE || cl == Long.class){
			Long s = (Long)value;
			if(s == null) s = 0L;
			pstmt.setLong(parameterIndex, s.longValue());
			parameterIndex++;
			totalLen = 8;
		}else if(cl == Float.TYPE || cl == Float.class){
			Float s = (Float)value;
			if(s == null) s = 0f;
			pstmt.setFloat(parameterIndex, s.floatValue());
			parameterIndex++;
			totalLen = 4;
		}else if(cl == Double.TYPE || cl == Double.class){
			Double s = (Double)value;
			if(s == null) s = 0.0;
			pstmt.setDouble(parameterIndex, s.doubleValue());
			parameterIndex++;
			totalLen = 8;
		}else if(cl == String.class){
			String s = (String)value;
			pstmt.setString(parameterIndex, s);
			parameterIndex++;
			if(s != null)
				totalLen = s.length() * 3;
			else
				totalLen = 1;
		}else if(cl == java.util.Date.class || cl == java.sql.Date.class){
			java.util.Date s = (java.util.Date)value;
			if(s == null) s = new java.util.Date();
			pstmt.setString(parameterIndex, DateUtil.formatDate(s, "yyyy-MM-dd HH:mm:ss"));
			parameterIndex++;
			
			totalLen = 8;
		}
		else{
			throw new java.lang.IllegalArgumentException("属性["+field+"]的类型["+cl.getName()+"]不是事先定义好的");
		}
		return totalLen;
	}
	
	
	
	
	
	
	/**
	 * 批量保存或者更新数据，新的对象则插入到数据库中，已有的数据，将变化保存到数据库中
	 * 
	 * @param t
	 * @throws Exception
	 */
	public void batch_insert_or_update_object(LinkedHashMap<Long,Object[]> map) throws Exception{
		long startTime = System.currentTimeMillis();
		String commitId = StringUtil.randomString(10);
		Connection conn = null;
		
		this.handleNotifyEvent();
		
		//新的对象，用于失败的情况下，恢复版本信息
		ArrayList<T> newObjectList = new ArrayList<T>();
		
		int totalBytes = 0;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			
			Iterator<Long> it = map.keySet().iterator();
			while(it.hasNext()){
				long now  = System.currentTimeMillis();
				Long id = it.next();
				Object objs[] = map.get(id);
				T t = (T)objs[0];
				MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
				ArrayList<String> al = (ArrayList<String>)objs[1];
				String fields[] = al.toArray(new String[0]);
				
				int version = mde.version.field.getInt(t);
				//插入同步
				boolean insertFlag = false;
				if(version == 0){
					insertFlag = true;
					mde.version.field.setInt(t,version+1);
				}
				
				if(insertFlag){
					newObjectList.add(t);
					
					
					String sql = mde.getSqlForInsertObjectToPrimaryTable();
					String sql2 = mde.getSqlForInsertObjectToSecondTable();
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					PreparedStatement pstmt2 = null;
					if(sql2 != null)
						pstmt2 = conn.prepareStatement(sql2);
					
					id = mde.id.field.getLong(t);
					pstmt.setLong(1, id);
					pstmt.setInt(2, version+1);
					//pstmt.setString(3, t.getClass().getName()); //废弃CNC字段
					//将含有CNC字段的值设置为null,因为oracle 中会将0长度字符串自动识别为null值，为了防止报错 将null改为 1长度空格字符串
					if(mdc != null)
					{
						if(mdc.cid != null)
						{
							pstmt.setString(3, " ");
					//设置cid的值 added by liuyang at 2012-05-09
						
							pstmt.setInt(4,mdc.cid );
						}
						else
						{
							//mdc.cid = classInfo.get(t.getClass());
							pstmt.setString(3, t.getClass().getName());
							pstmt.setInt(4,-100);
						}
					}
					else
					{
						pstmt.setString(3, t.getClass().getName());
						pstmt.setInt(4,-10);
					}
					//由于添加了CID字段 故索引值往后加1
					int parameterIndex = 5;
					Iterator<String> it2 = mde.map.keySet().iterator();
					try{
					
						while(it2.hasNext()){
							String field = it2.next();
							MetaDataField f = mde.map.get(field);
							if(f.inPrimaryTable){
								Class<?> cl = f.field.getType();
								if(MetaDataEntity.contains(mdc, f)){
									totalBytes += setPreparedStatementParameter(pstmt, parameterIndex, field, cl, f.field.get(t));
									parameterIndex++;
								}else{
									totalBytes += this.setPreparedStatementParameter(pstmt, parameterIndex, field, cl, null);
									parameterIndex++;
								}
							}
						}
						
						
						if(pstmt2 != null){
							pstmt2.setLong(1, id);
							it2 = mde.map.keySet().iterator();
							parameterIndex = 2;
							while(it2.hasNext()){
								String field = it2.next();
								MetaDataField f = mde.map.get(field);
								if(f.inPrimaryTable == false){
									if(MetaDataEntity.contains(mdc, f)){
										Object obj = f.field.get(t);
										totalBytes += this.setPreparedStatementParameterForSecondTable(pstmt2, parameterIndex, field, f.columnNames, obj,f.field.getGenericType());
										parameterIndex += f.columnNames.length;
									}else{
										totalBytes += this.setPreparedStatementParameterForSecondTable(pstmt2, parameterIndex, field, f.columnNames, null,null);
										parameterIndex += f.columnNames.length;
									}
								}
							}
						}
					
					
						pstmt.executeUpdate();
						if(pstmt2 != null){
							pstmt2.executeUpdate();
						}
						
						if(pstmt != null)
						{
							pstmt.close();
						}
						
						if(pstmt2 != null)
						{
							pstmt2.close();
						}
						
						if(logger.isInfoEnabled()){
							logger.info("[执行SQL] [批量更新或者插入对象] [插入对象，未提交] [commitId:{}] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{commitId,t.getClass().getName(),id,version,sql,sql2,System.currentTimeMillis()-now});
						}
					}catch(Exception e){
						EntityEntry<T> ee = this.entityModifedMap.get(id);
						if(ee != null){
							ee.saveFailed = true;
							ee.lastSaveFailedTime = startTime;
						}
						
						if(logger.isWarnEnabled()){
							logger.warn("[执行SQL] [批量更新或者插入对象] [插入对象出现异常，此对象30分钟内将不再保存] [commitId:"+commitId+"] ["+t.getClass().getName()+"] [id:"+id+"] [mde:"+(mde == null? "" : mde.username)+"] [readonly:"+readOnly+"] [version:"+version+"] [sql:"+sql+"] [sql2:"+sql2+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						}
						throw e;
					}
				}else{
					//为了减少PreparedStatement种类的个数
					java.util.Arrays.sort(fields, new Comparator<String>(){
						public int compare(String o1, String o2) {
							return o1.compareTo(o2);
						}
					});
					
					String sql = mde.getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTable(t.getClass(),fields);
					String sql2 = mde.getSqlForUpdateOneObjectByModifiedFieldsOnSecondTable(t.getClass(),fields);
					mde.version.field.set(t, version+1);
					PreparedStatement pstmt = null;
					PreparedStatement pstmt2 = null;
					if(sql != null)
						pstmt = conn.prepareStatement(sql);
					if(sql2 != null)
						pstmt2 = conn.prepareStatement(sql2);
					try{
					
						if(pstmt != null){
							int parameterIndex = 1;
							for(int i = 0 ; i < fields.length ; i++){
								MetaDataField f = mde.getMetaDataField(t.getClass(), fields[i]);
								if(f.inPrimaryTable){
									Class<?> cl = f.field.getType();
									totalBytes += setPreparedStatementParameter(pstmt, parameterIndex, fields[i], cl, f.field.get(t));
									parameterIndex++;
								}
							}
							pstmt.setLong(parameterIndex, id);
						}
						
						if(pstmt2 != null){
							
							int parameterIndex = 1;
							for(int i = 0 ; i < fields.length ; i++){
								MetaDataField f = mde.getMetaDataField(t.getClass(), fields[i]);
								if(f.inPrimaryTable == false){
									Object obj = f.field.get(t);
									totalBytes += setPreparedStatementParameterForSecondTable(pstmt2,parameterIndex,fields[i],f.columnNames,obj,f.field.getGenericType());
									parameterIndex += f.columnNames.length;
								}
							}
							pstmt2.setLong(parameterIndex, id);
						}
					
						if(pstmt != null)
							pstmt.executeUpdate();
						if(pstmt2 != null){
							pstmt2.executeUpdate();
						}
						
						if(pstmt != null)
						{
							pstmt.close();
						}
						
						if(pstmt2 != null)
						{
							pstmt2.close();
						}
						
						if(logger.isInfoEnabled()){
							logger.info("[执行SQL] [批量更新或者插入对象] [更新对象，未提交] [commitId:{}] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{commitId,t.getClass().getName(),id,version,sql,sql2,System.currentTimeMillis()-now});
						}
					}catch(Exception e){
						
						EntityEntry<T> ee = this.entityModifedMap.get(id);
						if(ee != null){
							ee.saveFailed = true;
							ee.lastSaveFailedTime = startTime;
						}
						
						if(logger.isWarnEnabled()){
							logger.warn("[执行SQL] [批量更新或者插入对象] [更新对象出现异常，此对象30分钟内将不再保存] [commitId:"+commitId+"] ["+t.getClass().getName()+"] [id:"+id+"] [version:"+version+"] [sql:"+sql+"] [sql2:"+sql2+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
						}
						throw e;
					}
					
					
				}
				
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		
			for(int i = 0 ; i < newObjectList.size() ; i++){
				T t = newObjectList.get(i);
				MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
				while(mdc != null){
					if(mdc.postPersist != null){
						try{
							mdc.postPersist.invoke(t, new Object[]{});
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.info("[执行SQL] [插入新的对象] [调用postPersist方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}

			}
	
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量更新或者插入对象,提交成功", OperatoinTrackerService.ACTION_WRITE, map.size(),totalBytes, (int)(System.currentTimeMillis()-startTime));
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 5000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [批量更新或者插入对象] [提交成功] [commitId:{}] [{}] [数量:{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),map.size(),System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量更新或者插入对象] [提交成功] [commitId:{}] [{}] [数量:{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),map.size(),System.currentTimeMillis()-startTime});
				}
			}
			
			
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量更新或者插入对象,提交失败", OperatoinTrackerService.ACTION_WRITE, map.size(), totalBytes,(int)(System.currentTimeMillis()-startTime));
			
			for(int i = 0 ; i < newObjectList.size() ; i++){
				T t = newObjectList.get(i);
				mde.version.field.setInt(t, 0);
			}
			try{
				conn.rollback();
				
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback成功] [commitId:{}] [{}] [数量:{}] [map:{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),map.size(),map,System.currentTimeMillis()-startTime});
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback成功] [commitId:"+commitId+"] ["+mde.cl.getName()+"]", e);
				}
			}catch(Exception ex){
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback失败] [commitId:{}] [{}] [数量:{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),map.size(),System.currentTimeMillis()-startTime});
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback失败，sql异常] [commitId:"+commitId+"] ["+mde.cl.getName()+"]", e);
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback失败，rollback异常] [commitId:"+commitId+"] ["+mde.cl.getName()+"]", ex);
				}
			}
			
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [批量更新或者插入对象] [关闭连接失败] [commitId:{}] [{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [批量更新或者插入对象] [关闭连接失败] [commitId:"+commitId+"] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
		
	}
	
	/**
	 * 更新一个对象，只更新有修改的属性，没有修改的属性不更新
	 * @param t
	 * @throws Exception
	 */
	public void update_object(T t,String fields[]) throws Exception{
		long startTime = System.currentTimeMillis();
		
		this.handleNotifyEvent();
		
		String sql = mde.getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTable(t.getClass(),fields);
		String sql2 = mde.getSqlForUpdateOneObjectByModifiedFieldsOnSecondTable(t.getClass(),fields);
		Connection conn = null;
		long id = -1;
		int version = 0;
		int totalBytes = 0;
		try{
			conn = getConnection();
			
			id = mde.id.field.getLong(t);
			version = mde.version.field.getInt(t);
			
			conn.setAutoCommit(false);
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			if(sql != null)
				pstmt = conn.prepareStatement(sql);
			if(sql2 != null)
				pstmt2 = conn.prepareStatement(sql2);
			
			if(pstmt != null){
				int parameterIndex = 1;
				for(int i = 0 ; i < fields.length ; i++){
					MetaDataField f = mde.getMetaDataField(t.getClass(), fields[i]);
					if(f.inPrimaryTable){
						Class<?> cl = f.field.getType();
						totalBytes += setPreparedStatementParameter(pstmt, parameterIndex, fields[i], cl, f.field.get(t));
						parameterIndex++;
					}
				}
				pstmt.setLong(parameterIndex, id);
			}
			
			if(pstmt2 != null){
				
				int parameterIndex = 1;
				for(int i = 0 ; i < fields.length ; i++){
					MetaDataField f = mde.getMetaDataField(t.getClass(), fields[i]);
					if(f.inPrimaryTable == false){
						Object obj = f.field.get(t);
						totalBytes += setPreparedStatementParameterForSecondTable(pstmt2,parameterIndex,fields[i],f.columnNames,obj,f.field.getGenericType());
						parameterIndex += f.columnNames.length;
					}
				}
				pstmt2.setLong(parameterIndex, id);
			}
			
			if(pstmt != null)
				pstmt.executeUpdate();
			if(pstmt2 != null){
				pstmt2.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
			
			if(pstmt != null)
				pstmt.close();
			if(pstmt2 != null){
				pstmt2.close();
			}

			mde.version.field.setInt(t, version+1);
		
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "更新对象"+t.getClass().getName()+"部分属性,成功", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes, (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [更新对象] [成功] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [更新对象] [成功] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}
			}
			
			
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "更新对象"+t.getClass().getName()+"部分属性,失败", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [更新对象] [失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [更新对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [插入新的对象] [关闭连接失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [插入新的对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
		
	}
	
	/**
	 * 删除一个对象
	 * @param t
	 * @throws Exception
	 */
	public boolean delete_object(T t) throws Exception{
		long startTime = System.currentTimeMillis();
		
		this.handleNotifyEvent();
		
		String sql = mde.getSqlForRemoveObjectFromPrimaryTable();
		String sql2 = mde.getSqlForRemoveObjectFromSecondTable();
		Connection conn = null;
		long id = -1;
		int version = 0;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			PreparedStatement pstmt2 = null;
			if(sql2 != null)
				pstmt2 = conn.prepareStatement(sql2);
			id = mde.id.field.getLong(t);
			version = mde.version.field.getInt(t);
			
			pstmt.setLong(1, id);
			if(pstmt2 != null){
				pstmt2.setLong(1, id);
			}
			
			int k = pstmt.executeUpdate();
			if(pstmt2 != null){
				pstmt2.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);

			mde.version.field.setInt(t, -2);
		
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "删除对象"+t.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
			if(k > 0){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [删除对象] [成功] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}
				return true;
			}else{
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [删除对象] [数据库中不存在此对象] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}
				return false;
			}
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "删除对象"+t.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [删除对象] [失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [删除对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [删除对象] [关闭连接失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [删除对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 批量删除数据
	 * 
	 */
	public boolean batch_delete_object(T[] ts) throws Exception{
		long startTime = System.currentTimeMillis();
		
		this.handleNotifyEvent();
		
		String sql = mde.getSqlForRemoveObjectFromPrimaryTable();
		String sql2 = mde.getSqlForRemoveObjectFromSecondTable();
		Connection conn = null;
		conn = getConnection();
		conn.setAutoCommit(false);
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		long id = -1;
		int version = 0;
		
		long[] deletedIds = new long[ts.length];
		int deletedCount = 0;
		try{
			for(T t : ts)
			{
				pstmt = conn.prepareStatement(sql);
				pstmt2	 = null;
				if(sql2 != null)
					pstmt2 = conn.prepareStatement(sql2);
				id = mde.id.field.getLong(t);
				version = mde.version.field.getInt(t);
				
				pstmt.setLong(1, id);
				if(pstmt2 != null){
					pstmt2.setLong(1, id);
				}
				
				int k = pstmt.executeUpdate();
				if(pstmt2 != null){
					pstmt2.executeUpdate();
				}
				mde.version.field.setInt(t, -2);
				deletedIds[deletedCount++] = id;
			}
					
					
			conn.commit();
			conn.setAutoCommit(true);

		
		
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量删除对象"+ts.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1, 8*ts.length,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [批量删除对象] [成功] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,sql,sql2,System.currentTimeMillis()-startTime});
			}
			return true;
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量删除对象"+ts.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,8*ts.length, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [批量删除对象] [失败] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,sql,sql2,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [批量删除对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [批量删除对象] [关闭连接失败] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,sql,sql2,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [批量删除对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	
	public int batch_delete_by_Ids(Class<?> cl,long[]ids) throws Exception{
		long startTime = System.currentTimeMillis();
		int k = 0;
		Arrays.sort(ids);
		long min = ids[0];
		long max = ids[ids.length-1];
		
		String sql = mde.getSqlForBatchRemoveObjectFromPrimaryTable();
		String sql2 = mde.getSqlForBatchRemoveObjectFromSecondTable();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,min);
			pstmt.setLong(2, max);
			
			
			if(sql2 != null)
			{
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setLong(1,min);
				pstmt2.setLong(2, max);
					
			}
				
			k = pstmt.executeUpdate();
			if(sql2 != null)
				pstmt2.executeUpdate();
			
			
			
			conn.commit();
			conn.setAutoCommit(true);
		
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "成功", OperatoinTrackerService.ACTION_WRITE, ids.length, ids.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,sql,System.currentTimeMillis()-startTime});
				}	
			}
		
		}catch(Exception e){
			conn.rollback();
			conn.setAutoCommit(true);
			
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "失败", OperatoinTrackerService.ACTION_WRITE, ids.length,ids.length*8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [批量删除] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms] [异常:]",new Object[]{mde.cl.getName(),"-","--",System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [批量删除] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(pstmt != null)
			{
				pstmt.close();
			}
			
			if(pstmt2 != null)
			{
				pstmt2.close();
			}
			
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [批量删除] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-","--",System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [批量删除] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
		return k;
		
		
	}
	
	
	public int batch_delete_by_Ids(Class<?> cl,List<Long>ids) throws Exception{
		long startTime = System.currentTimeMillis();
		int k = 0;
		
		String sql = mde.getSqlForBatchRemoveObjectFromPrimaryTable(ids);
		String sql2 = mde.getSqlForBatchRemoveObjectFromSecondTable(ids);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sql);
		
			for(int i=0; i<ids.size(); i++)
			{
				pstmt.setLong(i+1, ids.get(i));
			}
			
			
			if(sql2 != null)
			{
				pstmt2 = conn.prepareStatement(sql2);
				for(int i=0; i<ids.size(); i++)
				{
					pstmt2.setLong(i+1, ids.get(i));
				}	
			}
				
			
			
			k = pstmt.executeUpdate();
			if(sql2 != null)
				pstmt2.executeUpdate();
			
			
			
			conn.commit();
			conn.setAutoCommit(true);
		
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "成功", OperatoinTrackerService.ACTION_WRITE, k, k*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,sql,System.currentTimeMillis()-startTime});
				}	
			}
		
		}catch(Exception e){
			k=0;
			conn.rollback();
			conn.setAutoCommit(true);
			
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "失败", OperatoinTrackerService.ACTION_WRITE, k,k*8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.error("[执行SQL] [批量删除] [失败] [{}] [ids:{}] [sql:{}] [cl:] [cost:{}ms] [异常:{}]",new Object[]{mde.cl.getName(),ids,sql,mde.cl.getName(),System.currentTimeMillis()-startTime,e});
			//	logger.warn("[执行SQL] [批量删除] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(pstmt != null)
			{
				pstmt.close();
			}
			
			if(pstmt2 != null)
			{
				pstmt2.close();
			}
			
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [批量删除] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-","--",System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [批量删除] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
		return k;
	}
	
	
	
	
	
	
	
	/**
	 * 向DataBase插入一个对象
	 * @param t
	 * @throws Exception
	 */
	public void insert_object(T t) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = mde.getSqlForInsertObjectToPrimaryTable();
		String sql2 = mde.getSqlForInsertObjectToSecondTable();
		Connection conn = null;
		long id = -1;
		int version = 0;
		int totalBytes = 0;
		MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
		
		if(mdc == null){
			throw new java.lang.IllegalArgumentException("类["+t.getClass().getName()+"]不是预定义的Entity");
		}
		
		while(mdc != null){
			if(mdc.prePersist != null){
				try{
					mdc.prePersist.invoke(t, new Object[]{});
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [插入新的对象] [调用prePersist方法出现异常]",e);
					}
				}
				break;
			}
			mdc = mdc.parent;
		}
		mdc = mde.getMetaDataClassByName(t.getClass().getName());
		
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			PreparedStatement pstmt2 = null;
			if(sql2 != null)
				pstmt2 = conn.prepareStatement(sql2);
			
			id = mde.id.field.getLong(t);
			version = mde.version.field.getInt(t);
			
			pstmt.setLong(1, id);
			pstmt.setInt(2, version);
			//pstmt.setString(3, t.getClass().getName());废弃CNC字段
			pstmt.setString(3, " ");//将CNC值设置为null oracle中无法插入0长度的字符串，为了防止报错 将null 改为1长度的空格字符串
			//设置CID值
			pstmt.setInt(4, mdc.cid);
			//相应的parameterIndex加1
			int parameterIndex = 5;
			Iterator<String> it = mde.map.keySet().iterator();
			
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable){
					Class<?> cl = f.field.getType();
					if(MetaDataEntity.contains(mdc, f)){
						totalBytes += this.setPreparedStatementParameter(pstmt, parameterIndex, field, cl, f.field.get(t));
						parameterIndex++;
					}else{
						totalBytes += this.setPreparedStatementParameter(pstmt, parameterIndex, field, cl, null);
						parameterIndex++;
					}
				}
			}
			
			
			if(pstmt2 != null){
				pstmt2.setLong(1, id);
				it = mde.map.keySet().iterator();
				parameterIndex = 2;
				while(it.hasNext()){
					String field = it.next();
					MetaDataField f = mde.map.get(field);
					if(f.inPrimaryTable == false){
						if(MetaDataEntity.contains(mdc, f)){
							Object obj = f.field.get(t);
							this.setPreparedStatementParameterForSecondTable(pstmt2, parameterIndex, field, f.columnNames, obj,f.field.getGenericType());
							parameterIndex += f.columnNames.length;
						}else{
							this.setPreparedStatementParameterForSecondTable(pstmt2, parameterIndex, field, f.columnNames, null,null);
							parameterIndex += f.columnNames.length;
						}
					}
				}
			}
			
			pstmt.executeUpdate();
			if(pstmt2 != null){
				pstmt2.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
		
			pstmt.close();
			if(pstmt2 != null)
				pstmt2.close();
			
			
			while(mdc != null){
				if(mdc.postPersist != null){
					try{
						mdc.postPersist.invoke(t, new Object[]{});
					}catch(Exception e){
						if(logger.isWarnEnabled()){
							logger.info("[执行SQL] [插入新的对象] [调用postPersist方法出现异常]",e);
						}
					}
					break;
				}
				mdc = mdc.parent;
			}
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "插入对象"+t.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes , (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [插入新的对象] [成功] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [插入新的对象] [成功] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				}	
			}
			
			
		}catch(Exception e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "插入对象"+t.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes , (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [插入新的对象] [失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [插入新的对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [插入新的对象] [关闭连接失败] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,version,sql,sql2,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [插入新的对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 通过id，加载对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T select_object(long id) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = mde.getSqlForSelectObjectById(); 
		Connection conn = null;
		int totalBytes[] = new int[1];
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			T t = null;
			if(rs.next()){
				try{
					t = construct_object_from_resultset("",id,rs,1,totalBytes);
				}catch(Exception e){
					rs.close();
					pstmt.close();
					throw e;
				}
			}
			rs.close();
			pstmt.close();
			if(t != null){
				MetaDataClass mdc = mde.getMetaDataClassByName(t.getClass().getName());
				while(mdc != null){
					if(mdc.postLoad != null){
						try{
							mdc.postLoad.invoke(t, new Object[]{});
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.info("[执行SQL] [通过ID加载对象] [调用postLoad方法出现异常]",e);
							}
						}
						break;
					}
					mdc = mdc.parent;
				}
			}
			String className = null;
			if(t != null)
				className = t.getClass().getName();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询一个对象"+className+",成功", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(t != null){
					if(logger.isInfoEnabled()){
						logger.info("[执行SQL] [通过ID加载对象] [成功] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
					}
				}else{
					if(logger.isInfoEnabled()){
						logger.info("[执行SQL] [通过ID加载对象] [对象不存在] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
					}
				}
			}else{
				if(t != null){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID加载对象] [成功] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
					}
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID加载对象] [对象不存在] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
					}
				}
			}
			
			return t;
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询一个对象,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID加载对象] [失败] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [通过ID加载对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID加载对象] [关闭连接失败] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [通过ID加载对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 从一个结果集中构建一个对象，必须保证结果集是合适的结果集，否则会抛出SQL底层的异常
	 * 此方法在任何异常的情况下，都会抛出对应的异常。
	 * 否则将返回对应的对象。
	 * 
	 * 
	 * @param id
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	protected HashMap<String,Integer> column2IndexForSelectObject = null;
	
	public T construct_object_from_resultset(String sessionId,long id,ResultSet rs,int columnIndex,int totalBytes[]) throws Exception{
		long startTime = System.currentTimeMillis();
		if(column2IndexForSelectObject == null){
			HashMap<String,Integer> hh = new HashMap<String,Integer>();
			Iterator<String> it = mde.map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				for(int i = 0 ; i < f.columnNames.length ; i++){
					int k = rs.findColumn(f.columnNames[i]);
					hh.put(f.columnNames[i], k);
				}
				int k = rs.findColumn(mde.id.columnNames[0]);
				hh.put(mde.id.columnNames[0], k);
				k = rs.findColumn(mde.version.columnNames[0]);
				hh.put(mde.version.columnNames[0], k);
				k = rs.findColumn(mde.CNC);
				hh.put(mde.CNC, k);
				//添加一个CID的列索引号
				k = rs.findColumn(mde.CID);
				hh.put(mde.CID,k);
			}
			column2IndexForSelectObject = hh;
		}
		
		String className = rs.getString(mde.CNC);
		Integer cid = rs.getInt(mde.CID);
		MetaDataClass mdc = null;
		/**
		 * 为了保持向下兼容，支持className 和CID MetaClass 查找
		 */
		if(className != null && className.trim().length() > 0)
			 mdc = mde.getMetaDataClassByName(className);
		
		if(cid != null && mdc == null)
			mdc = mde.getMetaDataClassByCID(cid);
		
		if(mdc == null){
			if(logger.isWarnEnabled()){
				logger.warn("[通过ResultSet构建对象] [失败] [Java类丢失] [sessionId:{}] [{}] [id:{}] [className:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),id,className,System.currentTimeMillis()- startTime});
			}
			return null;
		}
		
		T t = null;
		try{
			
			Class<?> cl = mdc.cl;
			
			
			t = (T)cl.newInstance();
		}catch(Exception e){
			
			if(logger.isWarnEnabled()){
				logger.warn("[通过ResultSet构建对象] [失败] [创建对象出现异常] [sessionId:{}] [{}] [id:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),id,System.currentTimeMillis()- startTime});
			}
			
			throw e;
		}
		mde.id.field.setLong(t, id);
		
		
		Iterator<String> it = mde.map.keySet().iterator();
		
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = mde.map.get(field);
			if(MetaDataEntity.contains(mdc, f)){
				if(f.inPrimaryTable){
					Class<?> cl = f.field.getType();
					if(cl == Boolean.TYPE || cl == Boolean.class){
						//我们以CHAR（1）标识boolean，T标识true，其他标识false
						String s = rs.getString(column2IndexForSelectObject.get(f.columnNames[0]));
						if(s != null && s.equals("T")){
							f.field.setBoolean(t, true);
						}else{
							f.field.setBoolean(t, false);
						}
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Byte.TYPE || cl == Byte.class){
						byte b = rs.getByte(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setByte(t, b);
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Short.TYPE || cl == Short.class){
						short b = rs.getShort(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setShort(t, b);
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Integer.TYPE || cl == Integer.class){
						int b = rs.getInt(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setInt(t, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Character.TYPE || cl == Character.class){
						String b = rs.getString(column2IndexForSelectObject.get(f.columnNames[0]));
						if(b != null && b.length() > 0){
							f.field.setChar(t, b.charAt(0));
						}
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Long.TYPE || cl == Long.class){
						long b = rs.getLong(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setLong(t, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == Float.TYPE || cl == Float.class){
						float b = rs.getFloat(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setFloat(t, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Double.TYPE || cl == Double.class){
						double b = rs.getDouble(column2IndexForSelectObject.get(f.columnNames[0]));
						f.field.setDouble(t, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == String.class){
						String s = rs.getString(column2IndexForSelectObject.get(f.columnNames[0]));
						if(s != null){
							f.field.set(t, s);
							totalBytes[0]+=s.length()*3;
						}
						columnIndex++;
					}else if(cl == java.util.Date.class || cl == java.sql.Date.class){
						java.util.Date date = rs.getTimestamp(column2IndexForSelectObject.get(f.columnNames[0]));
						if(cl == java.util.Date.class){
							f.field.set(t, date);
						}else{
							f.field.set(t, new java.sql.Date(date.getTime()));
						}
						columnIndex++;
						totalBytes[0]+=8;
					}else{
						if(logger.isWarnEnabled()){
							logger.warn("[通过ResultSet构建对象] [失败] [某属性类型未知] [sessionId:{}] [{}] [id:{}] [field:{}] [type:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),id,field,cl.getName(),System.currentTimeMillis()- startTime});
						}
						throw new java.lang.IllegalArgumentException("属性["+field+"]的类型["+cl.getName()+"]不是事先定义好的");
					}
				}else{
					Type type = f.field.getGenericType();
					StringBuffer json = new StringBuffer();
					for(int i = 0 ; i < f.columnNames.length ; i++){
						String s = rs.getString(column2IndexForSelectObject.get(f.columnNames[i]));
						columnIndex++;
						if(s != null){
							json.append(s);
							totalBytes[0]+=s.length()*3;
						}
					}
					if(json.length() > 0){
						try{
							Object obj = JsonUtil.objectFromJson(json.toString(), type);

							f.field.set(t, obj);
						}catch(Exception e){
							if(logger.isWarnEnabled()){
								logger.warn("[通过ResultSet构建对象] [失败] [从JSON转化属性出现异常] [sessionId:{}] [{}] [id:{}] [field:{}] [type:{}] [json:{}] [cost:{}ms]",new Object[]{sessionId,t.getClass().getName(),id,field,type,json,System.currentTimeMillis()- startTime});
								logger.warn("[通过ResultSet构建对象] [失败] [从JSON转化属性出现异常] [sessionId:"+sessionId+"] ["+t.getClass().getName()+"] [id:"+id+"]",e);
							}
							//throw e;
						}
					}
				}
			}else{
				columnIndex++;
			}
		}
		int version = rs.getInt(column2IndexForSelectObject.get(mde.version.columnNames[0]));
		mde.version.field.setInt(t,version);
		totalBytes[0]+=4;
		
		if(logger.isDebugEnabled()){
			logger.debug("[通过ResultSet构建对象] [成功] [OK] [sessionId:{}] [{}] [id:{}] [version:{}] [cost:{}ms]",new Object[]{sessionId,t.getClass().getName(),id,version,System.currentTimeMillis()- startTime});
		}
		
		return t;
	}
	
	public static class MyMyInvocationHandler implements InvocationHandler{
		
		SimpleEntityManagerOracleImpl<?> em;
		long id;
		HashMap<String,Object> data = new HashMap<String,Object>();
		
		MyMyInvocationHandler(SimpleEntityManagerOracleImpl<?> em,long id,HashMap<String,Object> data){
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
	
	public <S> S construct_simple_object_from_resultset(String sessionId,long id,String[] fields,Class<?>[] rTypes,ResultSet rs,Class<S> clazz,int totalBytes[]) throws Exception{
		long startTime = System.currentTimeMillis();
		
		
		int columnIndex = 1;
		
		HashMap<String,Object> data = new HashMap<String,Object>(); 
		for(int i = 0 ; i < fields.length ; i++){
					String f = fields[i];
					Class<?> cl = rTypes[i];

					if(cl == Boolean.TYPE || cl == Boolean.class){
						//我们以CHAR（1）标识boolean，T标识true，其他标识false
						String s = rs.getString(columnIndex);
						if(s != null && s.equals("T")){
							data.put(f, true);
						}else{
							data.put(f, false);
						}
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Byte.TYPE || cl == Byte.class){
						byte b = rs.getByte(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Short.TYPE || cl == Short.class){
						short b = rs.getShort(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Integer.TYPE || cl == Integer.class){
						int b = rs.getInt(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Character.TYPE || cl == Character.class){
						String b = rs.getString(columnIndex);
						if(b != null && b.length() > 0){
							data.put(f, b.charAt(0));
						}else{
							data.put(f, ' ');
						}
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Long.TYPE || cl == Long.class){
						long b = rs.getLong(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == Float.TYPE || cl == Float.class){
						float b = rs.getFloat(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Double.TYPE || cl == Double.class){
						double b = rs.getDouble(columnIndex);
						data.put(f, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == String.class){
						String s = rs.getString(columnIndex);
						if(s != null){
							data.put(f, s);
							totalBytes[0]+=s.length()*3;
						}else{
							data.put(f, null);
						}
						columnIndex++;
					}else if(cl == java.util.Date.class || cl == java.sql.Date.class){
						java.util.Date date = rs.getTimestamp(columnIndex);
						if(cl == java.util.Date.class){
							data.put(f, data);
						}else{
							data.put(f, new java.sql.Date(date.getTime()));
						}
						columnIndex++;
						totalBytes[0]+=8;
					}else{
						if(logger.isWarnEnabled()){
							logger.warn("[通过ResultSet构建简单对象] [失败] [某属性类型未知] [sessionId:{}] [{}] [id:{}] [field:{}] [type:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),id,f,cl.getName(),System.currentTimeMillis()- startTime});
						}
						throw new java.lang.IllegalArgumentException("属性["+f+"]的类型["+cl.getName()+"]不是事先定义好的");
					}
		}
		totalBytes[0]+=4;

		InvocationHandler handler = new MyMyInvocationHandler(this,id,data);
		S t = null;
		try{
			t = (S)Proxy.newProxyInstance(clazz.getClassLoader(),new Class[] {clazz},handler);
			proxyEntityModifyLock.lock();
			try{
				ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(id);
				if(al == null){
					al = new ArrayList<EntityProxyEntry>();
					proxyEntityModifedMap.put(id, al);
				}
				al.add(new EntityProxyEntry(id,clazz,t,this.proxyReferenceQueue));
				proxyCount++;
			}finally{
				proxyEntityModifyLock.unlock();
			}
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.warn("[通过ResultSet构建简单对象] [失败] [创建简单对象Proxy出现异常] [sessionId:{}] [{}] [id:{}] [cost:{}ms]",new Object[]{sessionId,clazz.getName(),id,System.currentTimeMillis()- startTime});
			}
			throw e;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("[通过ResultSet构建简单对象] [成功] [OK] [sessionId:{}] [{}] [id:{}] [cost:{}ms]",new Object[]{sessionId,clazz.getName(),id,System.currentTimeMillis()- startTime});
		}
		
		return t;
		
	}
	/**
	 * 查询一些字段，而非所有的字段。
	 * 这些字段都是SuperClass的字段，SuperClass的字段此接口不能查询。
	 * 并且这些字段，只能是简单的字段，数组，集合，Embeddable都不能查询。
	 * 
	 * @param <S>
	 * @param cl
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <S> List<S> queryFields(Class<S> cl,long ids[]) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = null;
	
		if(cl.isInterface() == false){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，简单类型必须定义成接口] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
			}
			throw new java.lang.IllegalArgumentException("简单类型必须定义成接口");
		}
		
		Method[] methods = cl.getDeclaredMethods();
		if(methods.length == 0){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，简单类型必须定义成接口且至少有一个方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
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
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"需要参数] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"需要参数");
			}
			
			Class<?> rType = m.getReturnType();
			rTypes[i] = rType;
			if(!Utils.isPrimitiveType(rType)){
				
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型不是简单类型] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型不是简单类型");
			}

			if(m.getName().startsWith("is")){
				if (rType != Boolean.class && rType != Boolean.TYPE){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"以is开头但返回值不是bool] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"以is开头但返回值不是bool");
				}
				
				String field = m.getName().substring(2);
				if(field.length() == 0){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，不能存在is()方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
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
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"找不到对应的属性] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"找不到对应的属性");
				}
				
				if(mf.field.getType() != rType){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型与超类对应的属性类型不一样] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型与超类对应的属性类型不一样");
				}
				
				
			}else if(m.getName().startsWith("get")){
				String field = m.getName().substring(3);
				if(field.length() == 0){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，不能存在get()方法] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
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
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"找不到对应的属性] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"找不到对应的属性");
				}
				
				if(mf.field.getType() != rType){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"返回类型与超类对应的属性类型不一样] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
					}
					throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"返回类型与超类对应的属性类型不一样");
				}
				
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表加载简单对象] [失败，有方法"+m.getName()+"不是以is或者get开头] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
				}
				throw new java.lang.IllegalArgumentException("有方法"+m.getName()+"不是以is或者get开头");
			}
		}
		HashMap<Long,S> resultMap = new HashMap<Long,S>();
		//List<S> resultList = new ArrayList<S>();
		ArrayList<Long> idList = new ArrayList<Long>();
		proxyEntityModifyLock.lock();
		try{
			for(int i = 0 ; i < ids.length ; i++){
				ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(ids[i]);
				boolean found = false;
				if(al != null){
					for(int j = 0 ; j < al.size() ; j++){
						EntityProxyEntry epe = al.get(j);
						Object o = epe.get();
						if(o != null && epe.interfaceCl == cl){
							resultMap.put(epe.id, (S)o);
							found = true;
							break;
						}
					}
				}
				if(found == false){
					idList.add(ids[i]);
				}
			}
		}finally{
			proxyEntityModifyLock.unlock();
		}
		
		ArrayList<T> tList = new ArrayList<T>();
		ArrayList<Long> idList2 = new ArrayList<Long>();
		for(int i = 0 ; i < idList.size() ; i++){
			long id = idList.get(i);
			if(this.contains(id)){
				T t = this.find(id);
				if(t != null){
					tList.add(t);
				}else{
					idList2.add(id);
				}
			}else{
				idList2.add(id);
			}
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		for(int i = 0 ; i < fields.length ; i++){
			MetaDataField mf = mde.getMetaDataField(mde.cl, fields[i]);
			if(mde.id.field.getName().equals(fields[i])){
				mf = mde.id;
			}
			sb.append(mf.columnNames[0]);
			if(i < fields.length -1){
				sb.append(",");
			}
		}
		sb.append(" from " + username + "."+ mde.primaryTable + " where " + mde.id.columnNames[0]+"=?");
		
		sql = sb.toString();
		
		
		Connection conn = null;
		
		
		for(T t : tList){
			long id = mde.id.field.getLong(t);
			HashMap<String,Object> data = new HashMap<String,Object>();
			for(int i = 0 ; i < fields.length ; i++){
				MetaDataField mf = mde.getMetaDataField(mde.cl, fields[i]);
				if(mde.id.field.getName().equals(fields[i])){
					mf = mde.id;
				}
				Object o = mf.field.get(t);
				data.put(fields[i], o);
			}
			InvocationHandler handler = new MyMyInvocationHandler(this,id,data);
			S s = null;
			try{
				s = (S)Proxy.newProxyInstance(cl.getClassLoader(),new Class[] {cl},handler);
				proxyEntityModifyLock.lock();
				try{
					ArrayList<EntityProxyEntry> al = this.proxyEntityModifedMap.get(id);
					if(al == null){
						al = new ArrayList<EntityProxyEntry>();
						proxyEntityModifedMap.put(id, al);
					}
					al.add(new EntityProxyEntry(id,cl,s,this.proxyReferenceQueue));
					proxyCount++;
				}finally{
					proxyEntityModifyLock.unlock();
				}
			}catch(Exception e){
				if(logger.isWarnEnabled()){
					logger.warn("[通过ResultSet构建简单对象] [失败] [创建简单对象Proxy出现异常] [{}] [id:{}] [cost:{}ms]",new Object[]{cl.getName(),id,System.currentTimeMillis()- startTime});
				}
				throw e;
			}
			resultMap.put(id, s);
		}
		
		try{
			if(idList2.size() > 0){
				
				
				conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				int totalBytes[] = new int[1];
				for(int i = 0 ; i < idList2.size() ; i++){
					//添加时间 为了精确统计正确的操作时间 
					long sqlOperationStartTime = System.currentTimeMillis();
					pstmt.setLong(1, idList2.get(i));
					ResultSet rs = pstmt.executeQuery();
					S t = null;
					if(rs.next()){
						try{
							t = construct_simple_object_from_resultset("",idList2.get(i),fields,rTypes,rs,cl,totalBytes);
						}catch(Exception e){
							rs.close();
							pstmt.close();
							throw e;
						}
					}
					rs.close();
					if(t != null){
						resultMap.put(idList2.get(i), t);
					}
					
					
					//OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID查询简单对象"+cl.getName()+",成功", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));
					if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID查询简单对象"+cl.getName()+",成功", OperatoinTrackerService.ACTION_READ, idList2.size(),totalBytes[0], (int)(System.currentTimeMillis()-sqlOperationStartTime));
				}
				pstmt.close();
				
			}
			ArrayList<S> resultList = new ArrayList<S>();
			for(int i = 0 ; i < ids.length ; i++){
				S s = resultMap.get(ids[i]);
				if(s != null){
					resultList.add(s);
				}
			}
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [通过ID列表查询简单对象] [成功] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),resultList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [通过ID列表查询简单对象] [成功] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),resultList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}
			
			return resultList;
		}catch(Exception e){
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID列表查询简单对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,0, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [通过ID列表查询简单对象] [失败，出现异常] [{}] [简单对象:{}] [ids:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),0,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [通过ID列表查询简单对象] [失败，出现异常] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [通过ID列表查询简单对象] [关闭连接失败] [{}] [简单对象:{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),cl.getName(),toString(ids),sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [通过ID列表查询简单对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 创建SEQ
	 * @throws Exception
	 */
	public void create_seq() throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "create sequence "+mde.sequenceName+" minvalue "+SEQUENCE_INCREMENT+" maxvalue 999999999999999 start with "+SEQUENCE_INCREMENT+" increment by "+SEQUENCE_INCREMENT+" cache 20";
		Connection conn = null;
		long id = -1L;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();

			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [创建SEQUENCE] [成功] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
			}

		}catch(SQLException e){
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [创建SEQUENCE] [失败] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [创建SEQUENCE] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [创建SEQUENCE] [关闭连接失败] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [创建SEQUENCE] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	/**
	 * 从数据库中获取下一个可以用的SEQ
	 * 为了实现cache id
	 * 创建seq时，必须正确设置最小值和步长。
	 * @return
	 * @throws SQLException
	 */
	public long select_seq() throws SQLException{
		long startTime = System.currentTimeMillis();
		String sql = "select "+mde.sequenceName+".NEXTVAL from DUAL";
		Connection conn = null;
		long id = -1L;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			id = rs.getLong(1);
			rs.close();
			stmt.close();
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询序列,成功", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [查询SEQUENCE] [成功] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
			}
			
			return id;
		}catch(SQLException e){
			
			if(OperatoinTrackerService.isEnabled())OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询序列,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [查询SEQUENCE] [失败] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [查询SEQUENCE] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [查询SEQUENCE] [关闭连接失败] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [查询SEQUENCE] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 组装字符串，为了打印
	 * @param fields
	 * @param values
	 * @return
	 */
	public static String toString(String fields[],Object values[]){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < fields.length ; i++){
			if(i < values.length){
				if(values[i] instanceof String){
					sb.append("\""+fields[i]+"\":\""+values[i]+"\"");
				}else{
					sb.append("\""+fields[i]+"\":"+values[i]);
				}
			}else{
				sb.append("\""+fields[i]+"\":-");
			}
			if(i < fields.length -1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 组装字符串，为了打印
	 * @param fields
	 * @param values
	 * @return
	 */
	public static String toString(String values[]){
		if(values == null) return "null";
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < values.length ; i++){
			if(values[i] instanceof String){
				sb.append("\""+values[i]+"\"");
			}else{
				sb.append(values[i]);
			}
			if(i < values.length - 1){
				sb.append(",");
			}
		}
		return sb.toString();
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
	
	public static String toString(Object values[]){
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

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
	
	public long countUnSavedNewObjects()
	{
		long startTime = System.currentTimeMillis();
		long count = 0l;
		long totalSize = entityModifedMap.size(); 
		entityModifyLock.lock();
		long curId = 0l;
		try{
			
			Iterator<Long> it = entityModifedMap.keySet().iterator();
			while(it.hasNext()){
				long id = it.next();
				curId = id;
				EntityEntry<T> ee = entityModifedMap.get(id);
				T reference = ee.get();
				if(ee != null && reference != null){
					
					if(ee.newObject == true && ee.savingLock == false)
					{
						count += 1l;
					}
				}
			}
		}
		catch(Exception e)
		{
			if(logger.isWarnEnabled()){
				logger.warn("[统计尚未插入到数据库中的对象数量] [失败] [出现异常] [count:{}] [totalCount:{}] [curId:{}] [cost:{}ms]",new Object[]{count,totalSize,curId,System.currentTimeMillis()-startTime});
			}
			return 0l;
		}
		finally{
			entityModifyLock.unlock();
		}
		
		if(logger.isInfoEnabled()){
			logger.info("[统计尚未插入到数据库中的对象数量] [成功] [ok] [count:{}] [totalCount:{}] [cost:{}ms]",new Object[]{count,totalSize,System.currentTimeMillis()-startTime});
		}
		return count;
		
	}
	
	public void releaseReference()
	{
		checkReferenceQueue();
	}


	public void dropEntityIndex(Class cl) {
		// TODO Auto-generated method stub
		/**
		 * 删除索引
		 */

		long startTime = System.currentTimeMillis();
		SimpleIndices indices = Utils.getDeclaredIndices(cl);
		if(indices != null){
			Connection conn = null;
			try
			{
				conn = getConnection();
				conn.setAutoCommit(false);
				
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
					
					Iterator<SimpleIndexImpl> it = this.mde.indexListForOracle.iterator();
					while(it.hasNext())
					{
						SimpleIndexImpl indexImpl = it.next();
						if( indexImpl != null)
						{
							if(name.equals(indexImpl.name))
							{
								String tableName = mde.primaryTable;
								String sql = "drop index " +name;
								Statement stmt = conn.createStatement();
								stmt.executeUpdate(sql);
								conn.commit();
								
								logger.warn("["+thread.getName()+"] [删除索引] [成功] [ok] ["+name+"] ["+tableName+"] ["+sql+"] ["+cl.getName()+"] " +
										"[cost:"+(System.currentTimeMillis()-startTime)+"ms]");
								it.remove();
							}
						}
					}
					
				}
			}
			catch(Exception e)
			{
				logger.warn("["+thread.getName()+"] [删除索引] [失败] [未知异常] [--] [--] [--] ["+cl.getName()+"] " +
						"[cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			finally
			{
				if(conn != null)
				{
					try
					{
						conn.close();
					}
					catch(Exception e)
					{
						logger.error("[删除索引] [关闭连接] [失败] [出现异常]",e);
					}
				}
			}
		
		}
		
	
		
	}


	public void setBatchSaveOrUpdateSize(int batchSize) {
		// TODO Auto-generated method stub
		this.batchSaveOrUpdateSize = batchSize;
	}

	public int getBatchSaveOrUpdateSize() {
		// TODO Auto-generated method stub
		return batchSaveOrUpdateSize;
	}
	
	
}
