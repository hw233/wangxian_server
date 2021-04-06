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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.ModifyInfo;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl.NotifyEvent;
import com.xuanzhi.tools.simplejpa.impl.Utils.SimpleIndexImpl;
import com.xuanzhi.tools.statistics.BooleanExpressionUtil;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 此类是模仿SimpleEntityManagerOracleImpl所做，
 * 实现了其所有功能 只是根据mysql 的一些特性做了一定的小改动
 * @author liuyang
 *
 * @param <T>
 */

public class SimpleEntityManagerMysqlImpl<T> implements SimpleEntityManager<T>,Runnable
{
	
	static Logger logger = LoggerFactory.getLogger(SimpleEntityManager.class);

	protected MetaDataEntity mde;
	
	protected ConnectionPool pool = null; 
	
	//nextId lock
	protected ReentrantLock nextIdLock = new ReentrantLock();
	
	//当前可以使用的id和当前已经缓存到内存中的可以用的最大id，每次从数据库中加载sequence，缓存1024个。
	protected long currAvaiableId;
	protected long cachedMaxAvaiableId;
	//000～999 拷贝自SimpleEntityManagerOracleImpl 认为是对多台服务器提供支持
	protected int serverId;
	protected boolean autoSaveEnable = true;
	protected int checkIntervalInSeconds = 30;
	
	protected HashMap<Long,EntityEntry<T>> entityModifedMap = new HashMap<Long,EntityEntry<T>>();
	
	protected ReferenceQueue<T> referenceQueue = new ReferenceQueue<T>();
	
	protected ReentrantLock entityModifyLock = new ReentrantLock();
	
	protected ReentrantLock proxyEntityModifyLock = new ReentrantLock();
	protected HashMap<Long,ArrayList<EntityProxyEntry>> proxyEntityModifedMap = new HashMap<Long,ArrayList<EntityProxyEntry>>();
	
	protected boolean readOnly;
	
	//private String charset = "iso-8859-1";
	
	//private String to_charset = "utf8";
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
	//protected HashMap<String,Integer> column2IndexForSelectObject = null;
	
	protected HashMap<String,HashMap<String,Integer>> sectionColumn2IndexForSelectObject = new HashMap<String,HashMap<String,Integer>>();
	
	protected ReferenceQueue proxyReferenceQueue = new ReferenceQueue();
	protected long proxyCount = 0;
	
	public static class MyLock{
		int count = 0;
	}
	
	//加载同一个对象的同步锁，为了避免同时加载同一个对象
	protected HashMap<Long,MyLock> selectObjectLockMap = new HashMap<Long,MyLock>();
	
	Thread thread;
	
	//模仿oracle 存放各个表的sequence值的数据表的名称
	private final static String  SEQUENCE_TABLE_NAME = "ALL_SEQUENCES";
	
	public static final int SEQUENCE_INCREMENT  = 1024;
	  
	public void setConnectionPool(ConnectionPool pool)
	{
		this.pool = pool;
	}
	
	public static class MyMyInvocationHandlerForMysql implements InvocationHandler{
		
		SimpleEntityManagerMysqlImpl<?> em;
		long id;
		HashMap<String,Object> data = new HashMap<String,Object>();
		
		MyMyInvocationHandlerForMysql(SimpleEntityManagerMysqlImpl<?> em,long id,HashMap<String,Object> data){
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
	
	public static class ModifyInfo{
		public boolean dirty = false;
		String field;
		
		public ModifyInfo(String s){field = s;}
		
		public long lastSaveTime = System.currentTimeMillis();
		MetaDataField metaDataField;
	}
	
	public static class MysqlColumnInfo{
		public String className;
		public String fieldName;
		public String tableName;
		public String columnName;
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
			}
		}
		
		public ModifyInfo[] getInfos(){
			return infos;
		}
		
		public int getInfoSize(){
			return infoSize;
		}
		
		//记录这个对象在各个副表中，是否存在记录，为了的在更新的时候，到底是否要插入还是更新
		private boolean recordExistInSecondTables[] = null;
		/**
		 * 返回true，标识肯定存在。false标识不知道。
		 * @param secondTableIndex
		 * @return
		 */
		public boolean isRecordExistsInSecondTable(int secondTableIndex){
			if(recordExistInSecondTables == null) return false;
			if(secondTableIndex < 0 || secondTableIndex >= recordExistInSecondTables.length) return false;
			return recordExistInSecondTables[secondTableIndex];
		}
		
		public void setRecordExistsInSecondTable(int secondTableNum,int secondTableIndex){
			if(recordExistInSecondTables == null) recordExistInSecondTables = new boolean[secondTableNum];
			recordExistInSecondTables[secondTableIndex] = true;
		}
	}
	
	private  Map<Class<?>,Integer> classInfo = new HashMap<Class<?>, Integer>();
	
	public MysqlSectionManager<T> sectionManager;
	public int maxRowNum = 50000000;
	

	
	/**
	 * 初始化过程，此过程应该在其他系统初始化前执行
	 * 此初始化过程在在收集信息时，同时做检查。
	 * @param cl
	 * @throws Exception
	 */
	void init(Class<?> cl,ArrayList<Class<?>> subClasses,Map<Class<?>,Integer> classInfo) throws Exception{
		
		long startTime = System.currentTimeMillis();
		//添加一个cid 和Class 对应 的map
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
		
		mde.primaryTable = defaultTable.toUpperCase();
		/**
		 * Mysql按照此处的sequenceName插入到模拟sequence表中
		 */
		mde.sequenceName = "SEQ_" + mde.primaryTable;
		
		ArrayList<Field> fieldList = Utils.getPersistentFields(cl);
		
		mde.rootClass = new MetaDataClass();
		mde.rootClass.parent = null;
		mde.rootClass.cl = cl;
		////为metaclass 设置cid added by liuyang at 2012-05-09
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
				//先判断mysqlName属性是否存在 added by liuyang at 2012-05-25
				if(mde.id.simpleColumn.mysqlName().length() > 0)
				{
					mde.id.columnNames = new String[]{mde.id.simpleColumn.mysqlName()};
				}
				else if(mde.id.simpleColumn.name().length() > 0){
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
				//如果version的mysqlName存在
				if(mde.version.simpleColumn.mysqlName().length() > 0)
				{
					mde.version.columnNames = new String[]{mde.version.simpleColumn.mysqlName()};
				}
				else if(mde.version.simpleColumn.name().length() > 0){
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
		
		boolean needSecondTable = false;
		
		for(int i = 0 ; i < fieldList.size() ; i++){
			Field f = fieldList.get(i);
			Class<?> type = f.getType();
			if(Utils.isPrimitiveType(type)){
				
				MetaDataField mf = new MetaDataField();
				mf.field = f;
				//mf.inPrimaryTable = true;
				f.setAccessible(true);
				
				mde.map.put(cl.getName()+"."+f.getName(), mf);
				mf.mdc = mde.rootClass;
				
				mf.simpleColumn = Utils.getSimpleColumn(f);
				
				if(mf.simpleColumn.mysqlName().length() > 0){
					mf.columnNames = new String[]{mf.simpleColumn.mysqlName()};
				}
				else if(mf.simpleColumn.name().length() > 0){
					mf.columnNames = new String[]{mf.simpleColumn.name()};
				}else{
					mf.columnNames = new String[]{f.getName()};
				}
				
				continue;
			}else if(type.isArray() || Utils.isCollectionType(type) || Utils.isEmbeddable(type)){
				needSecondTable = true;
				MetaDataField mf = new MetaDataField();
				mf.field = f;
				mf.inPrimaryTable = false;
				f.setAccessible(true);
				
				mf.simpleColumn = Utils.getSimpleColumn(f);
				mde.map.put(cl.getName()+"."+f.getName(), mf);
				mf.mdc = mde.rootClass;
				
				String columnName = f.getName();
				if(mf.simpleColumn.mysqlName().length() > 0){
					columnName = mf.simpleColumn.mysqlName();
				}
				else if(mf.simpleColumn.name().length() > 0){
					columnName = mf.simpleColumn.name();
				}
				
				mf.columnNames = new String[]{columnName};
				
				
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
			mdc.cid  = classInfo.get(c);
			
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
					if(mf.simpleColumn.mysqlName().length() > 0){
						mf.columnNames = new String[]{mf.simpleColumn.mysqlName()};
					}
					else if(mf.simpleColumn.name().length() > 0){
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
					if(mf.simpleColumn.mysqlName().length() > 0){
						columnName = mf.simpleColumn.mysqlName();
					}
					else if(mf.simpleColumn.name().length() > 0){
						columnName = mf.simpleColumn.name();
					}

					mf.columnNames = new String[]{columnName};
					
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
		
		HashSet<String> indexFieldSet = new HashSet<String> ();
		
		SimpleIndices indices = Utils.getDeclaredIndices(cl);
		//index
		if(indices != null){
			SimpleIndex indexs[] = indices.value();
			for(int i = 0 ; i < indexs.length ; i++){
				String members[] = indexs[i].members();
				for(int j = 0 ; j < members.length; j++){
					indexFieldSet.add(members[j]);
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
						indexFieldSet.add(members[j]);
						
					}
				}
			}
		}
		
		//增加mysql的特殊处理
		//从数据库中加载字段和表的对应信息
		//字典表的存储格式如下：
		// 表字段名  表名    类属性名
		ArrayList<MysqlColumnInfo> tableDirectory = select_table_directory();
		
		int primaryTableRowSize = 0;
		mde.id.tableNameForMysql = mde.primaryTable;
		mde.id.columnNameForMysql = mde.id.columnNames[0].toUpperCase();
		mde.version.tableNameForMysql = mde.primaryTable;
		mde.version.columnNameForMysql = mde.version.columnNames[0].toUpperCase();
		
		primaryTableRowSize+=40;
		
		it = mde.map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = mde.map.get(field);
			Class<?> type = f.field.getType();
			if(Utils.isPrimitiveType(type) && type != String.class){
				f.inPrimaryTable = true;
				f.tableNameForMysql = mde.primaryTable;
				f.columnNameForMysql = f.columnNames[0].toUpperCase();
				
				primaryTableRowSize+=20;
				
			}else if(type == String.class){
				if(indexFieldSet.contains(f.field.getName())){
					f.inPrimaryTable = true;
					f.tableNameForMysql = mde.primaryTable;
					f.columnNameForMysql = f.columnNames[0].toUpperCase();
					
					primaryTableRowSize += f.simpleColumn.length();
				}else if(primaryTableRowSize + f.simpleColumn.length() < 8000){
					f.inPrimaryTable = true;
					f.tableNameForMysql = mde.primaryTable;
					f.columnNameForMysql = f.columnNames[0].toUpperCase();
					
					primaryTableRowSize += f.simpleColumn.length();
				}else {
					f.inPrimaryTable = false;
				}
			}else{
				f.inPrimaryTable = false;
			}
		}
		
		
		
		//每行大小8000，TEXT最多10个
		if(primaryTableRowSize >= 8000){
			System.out.println("[SimpleEntityManager] ["+mde.cl+"] [准备初始化失败] [主表单行的数据将超过8K，超过了Mysql的最大限制]");
			throw new Exception("指定的类["+mde.cl+"][主表单行的数据将超过8K，超过了Mysql的最大限制]");

		}
		
		//存放要拆分的表
		ArrayList<String> secondTables = new ArrayList<String>();
		
		for(int i = 1 ; i < 10000 ; i++){
			String s = mde.primaryTable+"_T"+i;
			for(int j = 0 ; j < tableDirectory.size() ; j++){
				MysqlColumnInfo ci = tableDirectory.get(j);
				if(ci.tableName.equals(s)){
					secondTables.add(s);
					break;
				}
			}
		}
		
		//最后一个拆分的表，此表可能放了不到10个字段，可以加入新的字段
		String lastestSecondTableName = mde.primaryTable+"_T"+(secondTables.size()+1);
		int lastestSecondTableColumnCount = 0;
		if(secondTables.size() > 0){
			String s = secondTables.get(secondTables.size()-1);
			int count = 0;
			for(int j = 0 ; j < tableDirectory.size() ; j++){
				MysqlColumnInfo ci = tableDirectory.get(j);
				if(ci.tableName.equals(s)){
					count++;
				}
			}
			if(count < 10){
				lastestSecondTableName = secondTables.remove(secondTables.size()-1);
				lastestSecondTableColumnCount = count;
			}
		}
		
		it = mde.map.keySet().iterator();
		while(it.hasNext()){
			String field = it.next();
			MetaDataField f = mde.map.get(field);
			if(f.inPrimaryTable == false){
				for(int j = 0 ; j < tableDirectory.size() ; j++){
					MysqlColumnInfo ci = tableDirectory.get(j);
					if(ci.fieldName.equals(f.field.getName()) && ci.className.equals(f.field.getDeclaringClass().getName())){
						f.tableNameForMysql = ci.tableName;
						f.columnNameForMysql = ci.columnName;
					}
				}
				
				if(f.tableNameForMysql == null){
					
					lastestSecondTableColumnCount++;
					f.tableNameForMysql = lastestSecondTableName;
					f.columnNameForMysql = f.columnNames[0].toUpperCase();
					
					if(lastestSecondTableColumnCount == 10){
						secondTables.add(lastestSecondTableName);
						
						lastestSecondTableName = mde.primaryTable+"_T"+(secondTables.size()+1);
						lastestSecondTableColumnCount = 0;
					}
				}
			}
		}
		
		if(lastestSecondTableColumnCount > 0){
			secondTables.add(lastestSecondTableName);
		}
		
		
		//index
		if(indices != null){
			SimpleIndex indexs[] = indices.value();
			for(int i = 0 ; i < indexs.length ; i++){
				String members[] = indexs[i].members();
				for(int j = 0 ; j < members.length; j++){
					MetaDataField f = mde.getMetaDataField(cl, members[j]);
					indexFieldSet.add(members[j]);
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
						indexFieldSet.add(members[j]);
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
		System.out.println("主表：" + mde.primaryTable+"  副表：" + toString(secondTables) + " 序列："+mde.sequenceName);
		System.out.println("主键：" + mde.id.columnNameForMysql+"  版本：" + mde.version.columnNameForMysql);
		
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
					sb.append(f.columnNameForMysql);
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
						sb.append(f.columnNameForMysql);
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
		
		mde.indexListForMySql = indexList;
		mde.secondTablesForMySql = secondTables;
		
		sectionManager = new MysqlSectionManager<T>(this,this.maxRowNum);
		sectionManager.init();
		
		
		//检查各个分区的表结构是否一致，以及即将新产生的表结构是否与以前的分区一致。
		//这一点非常重要，如果某个技术删除了一个字段，那么新产生的分区将和老分区不一致，导致union all失败。
		//并且也会导致一系列非常严重的问题。所以，必须保证各个分区的结构是一致的。
		if(maxRowNum > 0){
			checkTableStruct();
		}
		
		findLiftTimeMethods(mde.rootClass);
		for(int ii = 0 ;  ii< subClasses.size() ; ii++){
			Class<?> c = subClasses.get(ii);
			MetaDataClass mdc = mde.getMetaDataClassByName(c.getName());
			if(mdc == null)
			{
				System.out.println("[SimpleEntityManager] ["+c.getName()+"] [准备初始化失败] [某个类没有找到]");
				throw new Exception("指定的类["+c.getName()+"]没有对应的MetaDataClass");
			}
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
	
	
	void checkTableStruct() throws Exception {
		long startTime = System.currentTimeMillis();
		System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [开始检查各个分区结构是否一致....]");
		
		Connection conn = null;
		try{
			conn = getConnection();
			DatabaseMetaData md = conn.getMetaData();
			
			MysqlSection ss[] = sectionManager.sections;
			
			//检查各个分区是否一致
			HashMap<String,Object[]> primaryTableColumnInfo = null;
			MysqlSection primarySection = null;
			HashMap<String, HashMap<String, Object[]>> primaryColumnInfo4SecondTable = null;
			
			for(int i = 0 ; i < ss.length ; i++){
				MysqlSection section = ss[i];
				
				long ll = System.currentTimeMillis();
				
				ResultSet rs = md.getColumns(null, null, mde.primaryTable.toUpperCase()+"_"+section.getName(), null);
				HashMap<String,Object[]> columnInfo4PrimaryTable = new HashMap<String,Object[]>();
				
				while(rs.next()){
					String columnName = rs.getString(4).toUpperCase();
					String columnType = rs.getString(6);
					int length = rs.getInt(7);
					int scale = 0;
					if(columnType.equals("DECIMAL")){
						scale = rs.getInt(9);
					}
					columnInfo4PrimaryTable.put(columnName, new Object[]{columnType,length,scale});
				}
				rs.close();
				
				//检查副表字段，不存在的字段创建
				HashMap<String, HashMap<String, Object[]>> columnInfo4SecondTable = new HashMap<String, HashMap<String, Object[]>>();
				for(int j = 0 ; j < mde.secondTablesForMySql.size() ; j++){
					String secondTable = mde.secondTablesForMySql.get(j).toUpperCase();
					rs = md.getColumns(null, null, secondTable+"_"+section.getName(), null);
					//columnInfo以后准备使用 故在这里另外声明一个hashmap使用
					HashMap<String, Object[]> map = new HashMap<String,Object[]>();
					while(rs.next()){
						String columnName = rs.getString(4).toUpperCase();
						String columnType = rs.getString(6);
						int length = rs.getInt(7);
						int scale = 0;
						if(columnType.equals("DECIMAL")){
							scale = rs.getInt(9);
						}
						map.put(columnName, new Object[]{columnType,length,scale});
					}
					rs.close();
					
					columnInfo4SecondTable.put(secondTable, map);
				}
				
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [分区:"+section.getName()+"] [获取主表和副表表结构信息] [耗时:"+(System.currentTimeMillis() - ll)+"ms]");
				
				
				
				if(primaryTableColumnInfo == null){
					primaryTableColumnInfo = columnInfo4PrimaryTable;
					primaryColumnInfo4SecondTable = columnInfo4SecondTable;
					primarySection = section;
				}else{
					//检查主表
					
					StringBuffer errorInfo = new StringBuffer();
					if(!isSameTableStruct(mde.primaryTable.toUpperCase()+"_"+primarySection.getName(),primaryTableColumnInfo,
							mde.primaryTable.toUpperCase()+"_"+section.getName(),columnInfo4PrimaryTable,errorInfo)){
						
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [分区:"+section.getName()+"] [检查表结构发现致命错误] [信息："+errorInfo+"] [耗时:"+(System.currentTimeMillis() - startTime)+"ms]");
						throw new Exception("检查表结构发现致命错误，存在两个分区表结构不一致！");
					}
					
					//检查副表
					for(int j = 0 ; j < mde.secondTablesForMySql.size() ; j++){
						String secondTable = mde.secondTablesForMySql.get(j).toUpperCase();
						
						HashMap<String,Object[]> map1 = primaryColumnInfo4SecondTable.get(secondTable);
						HashMap<String,Object[]> map2 = columnInfo4SecondTable.get(secondTable);
						
						errorInfo = new StringBuffer();
						if(!isSameTableStruct(secondTable+"_"+primarySection.getName(),map1,
								secondTable+"_"+section.getName(),map2,errorInfo)){
							
							System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [分区:"+section.getName()+"] [检查表结构发现致命错误] [信息："+errorInfo+"] [耗时:"+(System.currentTimeMillis() - startTime)+"ms]");
							throw new Exception("检查表结构发现致命错误，存在两个分区表结构不一致！");
						}
					}
					
					primaryTableColumnInfo = columnInfo4PrimaryTable;
					primaryColumnInfo4SecondTable = columnInfo4SecondTable;
					primarySection = section;
					
					
				}
			}
			
			//检查是否删除了字段
			Iterator<String> it = mde.map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable){
					//由于mysql中字段名称是区分大小写 故这里为查询方便 大小写和java变量名保持一致
					//String ccc = f.columnNames[0].toUpperCase();
					//Object parameters[] = columnInfo.get(ccc);
					primaryTableColumnInfo.remove(f.columnNameForMysql);
				}else{
					HashMap<String,Object[]> map = primaryColumnInfo4SecondTable.get(f.tableNameForMysql);
					map.remove(f.columnNameForMysql);
				}
			}
			primaryTableColumnInfo.remove(mde.CID);
			primaryTableColumnInfo.remove(mde.CNC);
			primaryTableColumnInfo.remove(mde.id.columnNameForMysql);
			primaryTableColumnInfo.remove(mde.version.columnNameForMysql);
			
			int count = 0;
			for(int j = 0 ; j < mde.secondTablesForMySql.size() ; j++){
				String secondTable = mde.secondTablesForMySql.get(j).toUpperCase();
				HashMap<String,Object[]> map = primaryColumnInfo4SecondTable.get(secondTable);
				map.remove(mde.id.columnNameForMysql+"_SEC_"+(j+1));
				count += map.size();
			}
			
			//存在某个字段被删除了
			if(primaryTableColumnInfo.size() > 0 || count > 0){
				ArrayList<MysqlColumnInfo> tableDirectory = select_table_directory();
				
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [检查是否有字段被删除] [发现有字段被删除，具体信息如下:]");
				it = primaryTableColumnInfo.keySet().iterator();
				while(it.hasNext()){
					String columnName = it.next();
					Object[] pos = primaryTableColumnInfo.get(columnName);
					MysqlColumnInfo ccc = null;
					for(int j = 0 ; j < tableDirectory.size() ;j ++){
						MysqlColumnInfo cc = tableDirectory.get(j);
						if(cc.columnName.equalsIgnoreCase(columnName)){
							ccc = cc;
						}
					}
					if(ccc == null){
						System.out.println("删除的字段："+columnName+ " "+pos[0]+"("+pos[1]+") --.--");
					}else{
						System.out.println("删除的字段："+columnName+ " "+pos[0]+"("+pos[1]+") "+ccc.className+"."+ccc.fieldName+"");
					}
				}
				for(int j = 0 ; j < mde.secondTablesForMySql.size() ; j++){
					String secondTable = mde.secondTablesForMySql.get(j).toUpperCase();
					HashMap<String,Object[]> map = primaryColumnInfo4SecondTable.get(secondTable);
					it = map.keySet().iterator();
					while(it.hasNext()){
						String columnName = it.next();
						Object[] pos = map.get(columnName);
						MysqlColumnInfo ccc = null;
						for(int k = 0 ; k < tableDirectory.size() ;k ++){
							MysqlColumnInfo cc = tableDirectory.get(k);
							if(cc.columnName.equalsIgnoreCase(columnName)){
								ccc = cc;
							}
						}
						if(ccc == null){
							System.out.println("删除的字段："+columnName+ " "+pos[0]+"("+pos[1]+") --.--");
						}else{
							System.out.println("删除的字段："+columnName+ " "+pos[0]+"("+pos[1]+") "+ccc.className+"."+ccc.fieldName+"");
						}
					}
				}
				throw new Exception("发现有字段被删除,将导致新建的分区与老分区结构不一致，导致查询失败！");
			}
			
		

		}catch(Exception e){
			System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [准备检查数据库] [开始检查各个分区结构是否一致，出现异常] [耗时:"+(System.currentTimeMillis() - startTime)+"ms]");
			throw e;
		}
		finally{
                        if(conn != null){
                                conn.close();
                        }
                }
	}
	
	
	static boolean isSameTableStruct(String table1,HashMap<String,Object[]> table1ColumnInfo,
			String table2,HashMap<String,Object[]> table2ColumnInfo,StringBuffer errorInfo){
		boolean same = true;
		Iterator<String> it = table1ColumnInfo.keySet().iterator();
		while(it.hasNext()){
			String columnName = it.next();
			Object[] pos1 = table1ColumnInfo.get(columnName);
			Object[] pos2 = table2ColumnInfo.get(columnName);
			
			//缺少某个字段了
			if(pos2 == null){
				same = false;
				errorInfo.append("表["+table2+"]比表["+table1+"]缺少一个字段["+columnName+"("+pos1[0]+")],");
			}else{
				//类型也变了
				if(pos1[0].equals(pos2[0]) == false){
					same = false;
					errorInfo.append("表["+table2+"."+columnName+"("+pos2[0]+")]和表["+table1+"."+columnName+"("+pos1[0]+")]的字段类型不一致,");
				}
			}
		}
		it = table2ColumnInfo.keySet().iterator();
		while(it.hasNext()){
			String columnName = it.next();
			Object[] pos2 = table2ColumnInfo.get(columnName);
			Object[] pos1 = table1ColumnInfo.get(columnName);
			
			//缺少某个字段了
			if(pos1 == null){
				same = false;
				errorInfo.append("表["+table1+"]比表["+table2+"]缺少一个字段["+columnName+"("+pos2[0]+")],");
			}else{
				//类型也变了
				if(pos1[0].equals(pos2[0]) == false){
					same = false;
					errorInfo.append("表["+table2+"."+columnName+"("+pos2[0]+")]和表["+table1+"."+columnName+"("+pos1[0]+")]的字段类型不一致,");
				}
			}
		}
		return same;
	}
	/**
	 * 检查数据库，创建必要的表，索引，字段
	 */
	void checkDataBase(MysqlSection section,ArrayList<SimpleIndexImpl> indexList,ArrayList<String> secondTables) throws Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [准备检查数据库] [开始收集数据库中的信息....]");
		Connection conn = null;
		try{
			conn = getConnection();
			DatabaseMetaData md = conn.getMetaData();
			
			//检查主表
			ResultSet rs = md.getTables(null, null, mde.primaryTable.toUpperCase()+"_"+section.getName(), null);
			if(rs.next() == false){
				rs.close();
				
				//新加的代码，为了解决不同的分区表，字段的顺序不一致的问题
				ArrayList<String> columnList = new ArrayList<String>();
				if(section.sectionIndex > 1){
					rs = md.getColumns(null, null, mde.primaryTable.toUpperCase()+"_"+section.getNameForPreviousSection(), null);
					while(rs.next()){
						String columnName = rs.getString(4).toUpperCase();
						columnList.add(columnName);
					}
					rs.close();
				}
				String sql = mde.getSqlForCreatePrimaryTableInMysql(section,columnList);
				System.out.println("==================创建主表的SQL===================");
				System.out.println(sql);
				
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
					
					Iterator<String> it = mde.map.keySet().iterator();
					while(it.hasNext()){
						String field = it.next();
						MetaDataField f = mde.map.get(field);
						if(f.inPrimaryTable){
							this.insert_table_directory(f);
						}
					}
					
				}catch(Exception e){
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [主表创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [主表创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				
				stmt.close();
			}else{
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [主表已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				rs.close();
			}
			
			//检查副表
			startTime = System.currentTimeMillis();
			for(int i = 0 ; i < secondTables.size() ; i++){
				String secondTable = secondTables.get(i).toUpperCase();
				rs = md.getTables(null, null, secondTable+"_"+section.getName(), null);
				if(rs.next() == false){
					rs.close();
					
					//新加的代码，为了解决不同的分区表，字段的顺序不一致的问题
					ArrayList<String> columnList = new ArrayList<String>();
					if(section.sectionIndex > 1){
						rs = md.getColumns(null, null, secondTable+"_"+section.getNameForPreviousSection(), null);
						while(rs.next()){
							String columnName = rs.getString(4).toUpperCase();
							columnList.add(columnName);
						}
						rs.close();
					}
					
					String sql = mde.getSqlForCreateSecondTableInMysql(section,i+1,secondTable,columnList);
					
					System.out.println("==================创建副表的SQL===================");
					System.out.println(sql);
					
					Statement stmt = conn.createStatement();
					try{
						stmt.executeUpdate(sql);
						
						Iterator<String> it = mde.map.keySet().iterator();
						while(it.hasNext()){
							String field = it.next();
							MetaDataField f = mde.map.get(field);
							if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
								this.insert_table_directory(f);
							}
						}
						
					}catch(Exception e){
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [副表"+secondTable+"创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						throw e;
					}
					
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [副表"+secondTable+"创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					stmt.close();
				}else{
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [副表"+secondTable+"已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					
					rs.close();
				}
			}
			
			

			
			
			//检查主表字段，不存在的字段创建
			startTime = System.currentTimeMillis();
			rs = md.getColumns(null, null, mde.primaryTable.toUpperCase()+"_"+section.getName(), null);
			HashMap<String,Object[]> columnInfo = new HashMap<String,Object[]>();
			while(rs.next()){
				String columnName = rs.getString(4).toUpperCase();
				String columnType = rs.getString(6);
				int length = rs.getInt(7);
				int scale = 0;
				if(columnType.equals("DECIMAL")){
					scale = rs.getInt(9);
				}
				columnInfo.put(columnName, new Object[]{columnType,length,scale});
			}
			rs.close();
			
			
			/**
			 * 若主表不包含字段CLASS_ID,则创建CLASS_ID字段 类型为BIGINT;
			 * alter table xxx add CLASS_ID BIGINT added by liuyang at 2012-05-09
			 */
			
			if(!columnInfo.containsKey(mde.CID))
			{
				String sql = "alter table " + mde.primaryTable.toUpperCase()+"_" + section.getName()+ " add CLASS_ID BIGINT";
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
				}catch(Exception e){
					System.out.println("[SimpleEntityManager] [正在检查数据库] [分区:"+section.getName()+"] [给主表增加字段[CLASS_ID]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				
				System.out.println("[SimpleEntityManager] [正在检查数据库] [分区:"+section.getName()+"] [给主表增加字段[CLASS_ID]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				stmt.close();
			}
			
			
			//ALTER TABLE TEST MODIFY price NUMBER(10,2); 
			//alter table bm_methods add sysdatestr varchar(40);
			
			boolean addColumnToPrimaryTable = false;
			
			Iterator<String> it = mde.map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable){
					//由于mysql中字段名称是区分大小写 故这里为查询方便 大小写和java变量名保持一致
					//String ccc = f.columnNames[0].toUpperCase();
					//Object parameters[] = columnInfo.get(ccc);
					Object parameters[] = columnInfo.get(f.columnNameForMysql);

					//System.out.println("[debug] ["+mde.cl.getName()+"] columnNameForMysql = "+f.field.getName()+","+f.columnNameForMysql+", parameters="+parameters);

	
					if(parameters == null){
						//需要创建新的字段
						String sql = mde.getSqlForAddColumnInPrimaryTableInMysql(section,f);
						System.out.println("==================在Mysql主表中添加列的SQL===================");
						System.out.println(sql);
						Statement stmt = conn.createStatement();
						try{
							stmt.executeUpdate(sql);
							
							this.insert_table_directory(f);
							
						}catch(Exception e){
							System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [给主表增加字段["+f.columnNames[0]+"]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							throw e;
						}
						
						addColumnToPrimaryTable = true;
						
						System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [给主表增加字段["+f.columnNames[0]+"]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						stmt.close();
					}else{
						if(parameters[0].equals("VARCHAR") && f.field.getType() == java.lang.String.class
								&& f.simpleColumn.length() > ((Integer)parameters[1]).intValue() ){
							
							String sql = "ALTER TABLE "+mde.primaryTable+"_"+section.getName()+" MODIFY COLUMN "+f.columnNameForMysql+" VARCHAR("+f.simpleColumn.length()+")";
							System.out.println("==================在Mysql主表中修改列类型为VARCHAR的SQL===================");
							System.out.println(sql);
							Statement stmt = conn.createStatement();
							try{
								stmt.executeUpdate(sql);
							}catch(Exception e){
								System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [修改主表字段["+f.columnNames[0]+"]增加长度至"+f.simpleColumn.length()+"失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
								throw e;
							}
							
							System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [修改主表字段["+f.columnNames[0]+"]增加长度至"+f.simpleColumn.length()+"成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							stmt.close();
						}
					}
				}
			}
			
			if(addColumnToPrimaryTable){
				//检查主表字段，不存在的字段创建
				startTime = System.currentTimeMillis();
				rs = md.getColumns(null, null, mde.primaryTable.toUpperCase()+"_"+section.getName(), null);
				columnInfo = new HashMap<String,Object[]>();
				while(rs.next()){
					String columnName = rs.getString(4).toUpperCase();
					String columnType = rs.getString(6);
					int length = rs.getInt(7);
					int scale = 0;
					if(columnType.equals("DECIMAL")){
						scale = rs.getInt(9);
					}
					columnInfo.put(columnName, new Object[]{columnType,length,scale});
				}
				rs.close();
			}
			
			
			//检查副表字段，不存在的字段创建
			HashMap<String, HashMap<String, Object[]>> columnInfo4SecondTable = new HashMap<String, HashMap<String, Object[]>>();
			for(int i = 0 ; i < secondTables.size() ; i++){
				String secondTable = secondTables.get(i).toUpperCase();
				startTime = System.currentTimeMillis();
				rs = md.getColumns(null, null, secondTable+"_"+section.getName(), null);
				//columnInfo以后准备使用 故在这里另外声明一个hashmap使用
				HashMap<String, Object[]> map = new HashMap<String,Object[]>();
				while(rs.next()){
					String columnName = rs.getString(4).toUpperCase();
					String columnType = rs.getString(6);
					int length = rs.getInt(7);
					int scale = 0;
					if(columnType.equals("DECIMAL")){
						scale = rs.getInt(9);
					}
					map.put(columnName, new Object[]{columnType,length,scale});
				}
				rs.close();
				
				columnInfo4SecondTable.put(secondTable, map);
			}
				
			it = mde.map.keySet().iterator();
			while(it.hasNext()){
				String field = it.next();
				MetaDataField f = mde.map.get(field);
				if(f.inPrimaryTable == false){
					
					HashMap<String, Object[]> map = columnInfo4SecondTable.get(f.tableNameForMysql);
					
					if(map == null){
						System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [副表字段["+f.columnNameForMysql+"]对应的表["+f.tableNameForMysql+"]结构信息不存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						throw new Exception("副表字段["+f.columnNameForMysql+"]对应的表["+f.tableNameForMysql+"]结构信息不存在");
					}
					Object parameters[] = map.get(f.columnNameForMysql);
					if(parameters == null){
						//需要创建新的字段
						String sql = mde.getSqlForAddColumnInSecondTableInMysql(section,f);
						System.out.println("==================在Mysql副表["+f.tableNameForMysql+"]中添加列的SQL===================");
						System.out.println(sql);
						Statement stmt = conn.createStatement();
						try{
							stmt.executeUpdate(sql);
							
							this.insert_table_directory(f);
							
						}catch(Exception e){
							System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [给副表["+f.tableNameForMysql+"]增加字段["+f.columnNameForMysql+"]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							throw e;
						}
						
						System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [给副表["+f.tableNameForMysql+"]增加字段["+f.columnNameForMysql+"]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						stmt.close();
					}
					
					
				}
			}
		
			startTime = System.currentTimeMillis();
			
			//检查主表中的索引，不存在的创建
			//0放置的类型的索引
			//1放置CID的索引 added by liuyang at 2012-05-09
			//其他字段索引从2开始
			if(indexList != null){
				rs = md.getIndexInfo(null, null, mde.primaryTable.toUpperCase()+"_"+section.getName(), false, true);
				HashMap<String,ArrayList<String>> indexInfo =  new HashMap<String,ArrayList<String>>();
				while(rs.next()){
					String indexName = rs.getString(6);
					int pos = rs.getInt(8);
					String column = rs.getString(9).toUpperCase();
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
								//由于添加了CID字段 所以从i > 1开始 added by liuyang at 2012-05-09
								//if( i > 0){
								if(i > 1){
									MetaDataField f = mde.getMetaDataField(sii.cl, members[j]);//mde.map.get(members[j]);
									
									//if(f.columnNames[0].toUpperCase().equals(al.get(j)) == false){
									//mysql区分大小写 故直接采用java变量名作为列名
									if(f.columnNameForMysql.equalsIgnoreCase(al.get(j)) == false){
										System.out.println("[SimpleEntityManager] ["+f.mdc.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [列名"+f.columnNameForMysql+"不等于数据库存在列名"+al.get(j)+"]");
										b = false;
										break;
									}
								}else{
									if(members[j].equalsIgnoreCase(al.get(j)) == false){
										System.out.println("[SimpleEntityManager] [---] [分区:"+section.getName()+"] [正在检查数据库] [索引成员名"+members[j]+"不等于数据库存在列名"+al.get(j)+"] [j:"+j+"] [i:"+i+"]");
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
					String columnType = null;
					int columnLength = 0; 
					
					
					for(int j = 0 ; j < members.length; j++){
						//if(i > 0){
						//CID和CNC的定义是没有对应metafield的 所以跳过去 added by liuyang at 2012-05-09
						if(i>1){
							MetaDataField f = mde.getMetaDataField(sii.cl, members[j]);
							sb.append(f.columnNameForMysql);
							//判断列类型 若为varchar或者char或者Text 则判断其长度 若大于10个字符 则建索引为10个字符 否则将其全部作为索引
							Object[] columnif = columnInfo.get(f.columnNameForMysql);
							if(columnif == null)
							{
								throw new IllegalStateException(f.field.getName() + "无相关的数据库列信息，请检查逻辑错误！");
							}
							columnType = (String)columnif[0];
							columnLength = (Integer)columnif[1];
							if(columnType.toUpperCase().indexOf("VARCHAR") > -1  || 
									columnType.toUpperCase().indexOf("CHAR")  > -1 || 
									columnType.toUpperCase().indexOf("TEXT")  > -1)
							{
								if(columnLength < 10)
								{
									sb.append("("+columnLength+")");
								}
								else
								{
									sb.append("("+10+")");
								}
							}
						}else{
							sb.append(members[j]);
							//sb.append("("+columnLength+")");
						}
						if(j < members.length - 1){
							sb.append(",");
						}
					}
					
					if(found == false){
						//需要判断字段的类型 然后确认使用索引长度 mysql建议为varchar text 取前10个字符作为索引 若小于10个字符 则取字符串全部作为索引
						
						
						String sql = "CREATE INDEX "+name+" ON "+mde.primaryTable.toUpperCase()+"_"+section.getName()+ " ("+sb.toString()+")";
						//mysql不支持索引压缩 故注掉以下语句
						/*if(sii.compress() > 0){
							sql += " compress " + sii.compress();
						}*/
						System.out.println("==================在Mysql主表中添加索引的SQL===================");
						System.out.println(sql);
						Statement stmt = conn.createStatement();
						try{
							stmt.executeUpdate(sql);
						}catch(Exception e){
							System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [创建索引["+name+"("+sb.toString()+")]失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
							throw e;
						}
						
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [创建索引["+name+"("+sb.toString()+")]成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						stmt.close();
						
					}else{
						System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [分区:"+section.getName()+"] [正在检查数据库] [索引"+name+"已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					}
				}
			}
			/**
			 * Mysql中不存在sequence的概念
			 * 这里在mysql中创建一个名字为all_sequences的表
			 * 然后从这里查询出相关的sequence值 若无相关的sequence值 则创建一条记录记录此sequence值
			 */
			startTime = System.currentTimeMillis();
			//模拟创建sequence
			/**
			 * 先判断是否存在all_sequence表
			 * 若没有则创建
			 */
			rs = md.getTables(null, null, SEQUENCE_TABLE_NAME.toUpperCase(), null);
			if(rs.next() == false){
				rs.close();
				logger.info(SEQUENCE_TABLE_NAME + "表不存在，开始创建……");
				
				String sql = "CREATE TABLE " + SEQUENCE_TABLE_NAME.toUpperCase() + " (id int not null PRIMARY KEY auto_increment,sequence_name varchar(200) not null,currval bigint not null, nextval bigint not null, step bigint not null)";
				System.out.println("==================创建Sequence表的SQL===================");
				System.out.println(sql);
				
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
				}catch(Exception e){
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [" + SEQUENCE_TABLE_NAME + "表创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [" + SEQUENCE_TABLE_NAME  + "表创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				stmt.close();
			}
			/**
			 * 从sequence信息表中查找相关表的sequence信息 若没有 则创建
			 */
			String sql = "select * from " + SEQUENCE_TABLE_NAME.toUpperCase() + " where sequence_name = '" +  mde.sequenceName + "'";
			//String sql =" select * from all_sequences where sequence_name='"+mde.sequenceName.toUpperCase()+"'";
			Statement stmt = conn.createStatement();
			try{
				rs = stmt.executeQuery(sql);
				if(rs.next() == false){
					sql = "insert " + SEQUENCE_TABLE_NAME.toUpperCase() + "(sequence_name,currval,nextval,step) values('"+mde.sequenceName+"'," +0+"," + SEQUENCE_INCREMENT + "," + SEQUENCE_INCREMENT + ")";
					//sql = "create sequence "+mde.sequenceName+" minvalue "+SEQUENCE_INCREMENT+" maxvalue 999999999999999 start with "+SEQUENCE_INCREMENT+" increment by "+SEQUENCE_INCREMENT+" cache 20";
					System.out.println("==================插入Sequence表Sequence信息的SQL===================");
					System.out.println(sql);
					
					stmt.executeUpdate(sql);
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				}else{
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE已经存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				}
			}catch(Exception e){
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [SEQUENCE创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				throw e;
			}
			
			mde.secondTablesForMySql = secondTables;
			
		}catch(Exception e){
			if(conn != null)
				if(!conn.getAutoCommit())
					conn.rollback();
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
	 * 出现此表对应的数据字典表
	 * @return
	 * @throws Exception
	 */
	private void insert_table_directory(MetaDataField f) throws Exception{
		long startTime = System.currentTimeMillis();

		Connection conn = null;
		try{
			conn = getConnection();
		
			String sql = "insert into "+mde.primaryTable.toUpperCase()+"_DIR"+" (CLASSNAME,FIELDNAME,TABLENAME,COLUMNNAME) values (?,?,?,?) ";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, f.field.getDeclaringClass().getName());
			pstmt.setString(2, f.field.getName());
			pstmt.setString(3, f.tableNameForMysql);
			pstmt.setString(4, f.columnNameForMysql);
			
			pstmt.executeUpdate();
			pstmt.close();
			
			System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [插入数据字典表成功] ["+f.columnNameForMysql+","+f.tableNameForMysql+","+f.field.getName()+"] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
		}catch(SQLException e){
			
			System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [插入数据字典表失败] ["+f.columnNameForMysql+","+f.tableNameForMysql+","+f.field.getName()+"] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
			
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					throw e;
				}
			}
		}
	}
	
	/**
	 * 出现此表对应的数据字典表
	 * @return
	 * @throws Exception
	 */
	private ArrayList<MysqlColumnInfo> select_table_directory() throws Exception{
		long startTime = System.currentTimeMillis();

		Connection conn = null;
		try{
			conn = getConnection();
			DatabaseMetaData md = conn.getMetaData();
			
			//检查主表
			ResultSet rs = md.getTables(null, null, mde.primaryTable.toUpperCase()+"_DIR", null);
			if(rs.next() == false){
				rs.close();
				String sql = "create table " +  mde.primaryTable.toUpperCase()+"_DIR (";
				sql += " CLASSNAME VARCHAR(256) ,\n";
				sql += " FIELDNAME VARCHAR(64) ,\n";
				sql += " TABLENAME VARCHAR(64),\n";
				sql += " COLUMNNAME VARCHAR(64) )";

				System.out.println("===============字典表["+mde.primaryTable.toUpperCase()+"_DIR"+"]不存在，创建字典表==============");
				System.out.println("sql: ");
				System.out.println(sql);
				
				Statement stmt = conn.createStatement();
				try{
					stmt.executeUpdate(sql);
				}catch(Exception e){
					System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [字典表创建失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					throw e;
				}
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [字典表创建成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				
				stmt.close();
			}else{
				System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [字典表已存在] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
				rs.close();
			}
			String sql = "select CLASSNAME,FIELDNAME,TABLENAME,COLUMNNAME from " + mde.primaryTable.toUpperCase()+"_DIR";
			
			//System.out.println("trace1============" + sql);
			
			ArrayList<MysqlColumnInfo> al = new ArrayList<MysqlColumnInfo>();
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			//System.out.println("trace2============" + sql);
			while(rs.next()){
				MysqlColumnInfo ci = new MysqlColumnInfo();
				ci.className = rs.getString(1);
				ci.fieldName = rs.getString(2);
				ci.tableName = rs.getString(3);
				ci.columnName = rs.getString(4);
				al.add(ci);
				//System.out.println("trace3============" + sql);
			}
			rs.close();
			
			System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [查询数据字典表成功] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
			
			
			return al;
		}catch(SQLException e){
			
			System.out.println("[SimpleEntityManager] ["+mde.cl.getName()+"] [正在检查数据库] [查询数据字典表失败] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
			
			throw e;
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					throw e;
				}
			}
		}
	}

	/**
	 * 从数据库中获取下一个可以用的SEQ
	 * 本质是模拟oracle 的sequence机制 故这里其实是从存放sequence信息的表中查找相关表的序列号
	 * 首先从存放相关表Sequence信息的sequence信息表中查出有关的sequence 记录
	 * 然后再更新表中原有记录的sequence相关信息
	 * @return
	 * @throws Exception 由于要抛出别的异常信息  故将原来仅抛出sql异常改为普通异常
	 */
	public long select_seq() throws Exception{
		long startTime = System.currentTimeMillis();
		/**
		 * 从ALL_SEQUENCE表中查找相关表的SEQUENCE
		 */
		//String sql = "select "+mde.sequenceName+".NEXTVAL from DUAL";
		String sql  = "select  nextval  from " + SEQUENCE_TABLE_NAME.toUpperCase() + " where sequence_name = '" +  mde.sequenceName + "'";
		Connection conn = null;
		long id = -1L;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			id = rs.getLong(1);
			rs.close();
			sql = "update " + SEQUENCE_TABLE_NAME + " set currval=" + id + ",nextval=" +(id+SEQUENCE_INCREMENT) + " where sequence_name = '" +  mde.sequenceName + "'";
			int updatedNum = stmt.executeUpdate(sql);
			if(updatedNum == 1)
			{
				logger.info("名称为" + mde.sequenceName +"的Sequence值已经更新为 当前值(currval)是" + id + ",下一次的值(nextval)是" +  (id+SEQUENCE_INCREMENT));
			}
			else if(updatedNum == 0)
			{
				logger.warn("名称为" + mde.sequenceName +"的Sequence值未更新，要更新的 当前值(currval)是" + id + ",下一次的值(nextval)是" +  (id+SEQUENCE_INCREMENT));
			}
			else if(stmt.executeUpdate(sql) > 1)
			{	conn.rollback();
				stmt.close();
				throw new java.lang.IllegalStateException("名称为" + mde.sequenceName +"的Sequence在数据表" + SEQUENCE_TABLE_NAME + "中出现多条记录，请删除重复记录，并考虑添加唯一索引");
			}
			conn.commit();
			conn.setAutoCommit(true);
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询序列,成功", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [查询SEQUENCE] [成功] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
			}
			
			return id;
		}catch(SQLException e){
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询序列,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [查询SEQUENCE] [失败] [{}] [maxId:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,sql,System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [查询SEQUENCE] [失败] ["+mde.cl.getName()+"]", e);
			}
			conn.rollback();
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
				if(this.destroy){
					needAutoSaveImmdiately = true;
				}
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
					
					MysqlSection section = sectionManager.insertSectionById(id);
					
					String sql = mde.getSqlForInsertObjectToPrimaryTableInMysql(section);
					String sql2[] = mde.getSqlForInsertObjectToSecondTableInMysql(section);
					
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					
					PreparedStatement pstmt2[] = new PreparedStatement[sql2.length];
					for(int i = 0 ; i < pstmt2.length ; i++){
						pstmt2[i] = conn.prepareStatement(sql2[i]);
					}
					
					id = mde.id.field.getLong(t);
					pstmt.setLong(1, id);
					pstmt.setInt(2, version+1);
					//pstmt.setString(3, t.getClass().getName()); //废弃CNC字段
					//将含有CNC字段的值设置为null
					pstmt.setString(3, null);
					//设置cid的值 added by liuyang at 2012-05-09
					
					pstmt.setInt(4,mdc.cid );
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
						
						
						for(int i = 0 ; i < pstmt2.length ; i++){
							String secondTable = mde.secondTablesForMySql.get(i);
							pstmt2[i].setLong(1, id);
							it2 = mde.map.keySet().iterator();
							parameterIndex = 2;
							while(it2.hasNext()){
								String field = it2.next();
								MetaDataField f = mde.map.get(field);
								if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
									if(MetaDataEntity.contains(mdc, f)){
										Object obj = f.field.get(t);
										totalBytes += this.setPreparedStatementParameterForSecondTable(pstmt2[i], parameterIndex, field, f.columnNameForMysql, obj,f.field.getGenericType());
										parameterIndex++;
									}else{
										totalBytes += this.setPreparedStatementParameterForSecondTable(pstmt2[i], parameterIndex, field, f.columnNameForMysql, null,null);
										parameterIndex ++;
									}
								}
							}
						}
					
					
						pstmt.executeUpdate();
						for(int i = 0 ; i < pstmt2.length ; i++){
							pstmt2[i].executeUpdate();
						}
						
						pstmt.close();
						
						for(int i = 0 ; i < pstmt2.length ; i++){
							pstmt2[i].close();
							
						}
						
						sectionManager.notifyInsertOneRow(section, id);
						
						if(logger.isInfoEnabled()){
							logger.info("[执行SQL] [批量更新或者插入对象] [插入对象，未提交] [commitId:{}] [{}] [id:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{commitId,t.getClass().getName(),id,version,sql,toString(sql2),System.currentTimeMillis()-now});
						}
					}catch(Exception e){
						EntityEntry<T> ee = this.entityModifedMap.get(id);
						if(ee != null){
							ee.saveFailed = true;
							ee.lastSaveFailedTime = startTime;
						}
						
						if(logger.isWarnEnabled()){
							logger.warn("[执行SQL] [批量更新或者插入对象] [插入对象出现异常，此对象30分钟内将不再保存] [commitId:"+commitId+"] ["+t.getClass().getName()+"] [id:"+id+"] [version:"+version+"] [sql:"+sql+"] [sql2:"+toString(sql2)+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
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
					
					MysqlSection section = sectionManager.selectSectionById(id);
					if(section == null){
						throw new Exception("通过Id找不到对应的分区");
					}

					String sql = mde.getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTableInMysql(section,t.getClass(),fields);
					String sql2[] = mde.getSqlForUpdateOneObjectByModifiedFieldsOnSecondTableInMysql(section,t.getClass(),fields);
					String sql2ForInsert[] = mde.getSqlForInsertOneObjectByModifiedFieldsOnSecondTableInMysql(section,t.getClass(),fields);
					
			
					
					mde.version.field.set(t, version+1);
					PreparedStatement pstmt = null;
					PreparedStatement pstmt2[] = new PreparedStatement[sql2.length];
					if(sql != null)
						pstmt = conn.prepareStatement(sql);
					
					for(int i = 0 ; i < sql2.length ; i++){
						if(sql2[i] != null){

							//由于存在这种情况：
							//起初表的横向拆分为 T1 T2 T3
							//但运行一段时间后，程序员增加了字段，导致产生了新的表拆分 T4
							//这时，对于一个对象，他可能在T1,T2,T3中有数据，在T4中无数据
							//而恰巧要更新这个对象的属性，正好在T4中，那么会导致更新失败。
							//
							//为了解决这种情况，在更新T2，T3...时，需要确保对应的数据记录存在。
							
							if(this.checkObjectExistsInSecondTable(section, i, id)){
								pstmt2[i] = conn.prepareStatement(sql2[i]);	
							}else{
								pstmt2[i] = conn.prepareStatement(sql2ForInsert[i]);	
							}
							
						}
					}
					
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
						
						for(int i = 0 ; i < pstmt2.length ; i++){
							if(pstmt2[i] == null) continue;
							String secondTable = mde.secondTablesForMySql.get(i);
							
							int parameterIndex = 1;
							for(int j = 0 ; j < fields.length ; j++){
								MetaDataField f = mde.getMetaDataField(t.getClass(), fields[j]);
								if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
									Object obj = f.field.get(t);
									totalBytes += setPreparedStatementParameterForSecondTable(pstmt2[i],parameterIndex,fields[j],f.columnNameForMysql,obj,f.field.getGenericType());
									parameterIndex += 1;
								}
							}
							pstmt2[i].setLong(parameterIndex, id);
						}
					
						if(pstmt != null)
							pstmt.executeUpdate();
						for(int i = 0 ; i < pstmt2.length ; i++){
							if(pstmt2[i] == null) continue;
							
							pstmt2[i].executeUpdate();
							

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
	
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量更新或者插入对象,提交成功", OperatoinTrackerService.ACTION_WRITE, map.size(),totalBytes, (int)(System.currentTimeMillis()-startTime));
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量更新或者插入对象,提交失败", OperatoinTrackerService.ACTION_WRITE, map.size(), totalBytes,(int)(System.currentTimeMillis()-startTime));
			
			for(int i = 0 ; i < newObjectList.size() ; i++){
				T t = newObjectList.get(i);
				mde.version.field.setInt(t, 0);
			}
			try{
				conn.rollback();
				
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量更新或者插入对象] [失败，rollback成功] [commitId:{}] [{}] [数量:{}] [cost:{}ms]",new Object[]{commitId,mde.cl.getName(),map.size(),System.currentTimeMillis()-startTime});
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
							logger.warn("[垃圾回收] [有数据丢失] [{}] [id:{}] [修改的属性:新对象没有来得及保存就被回收了]",new Object[]{mde.cl.getName(),ee.id});
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
							MyMyInvocationHandlerForMysql handler = (MyMyInvocationHandlerForMysql)Proxy.getInvocationHandler(proxy);
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
		if (f.simpleColumn.urgent() == false || ee.newObject) {
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
		}
		else {
			 //紧急数据，立即保存
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
	 * 通过id，加载对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T select_object(long id) throws Exception{
		long startTime = System.currentTimeMillis();
		
		MysqlSection section = sectionManager.selectSectionById(id);
		if(section == null){
			logger.warn("[执行SQL] [通过ID加载对象] [失败] [{}] [id:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),id,"找不到对应的分区",System.currentTimeMillis()-startTime});
			return null;
		}
		
		String sql = mde.getSqlForSelectObjectByIdInMysql(section); 
		Connection conn = null;
		int totalBytes[] = new int[1];
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询一个对象"+className+",成功", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));
			
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
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询一个对象,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));
			
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
		MyLock lock = null;
		handleNotifyEvent();
		
		EntityEntry<T> ee2 = entityModifedMap.get(id);
		if(ee2 != null){
			T reference = ee2.get();
			if(reference != null){
				return reference;
			}
		}
		
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
								entityModifedMap.put(id, ee);
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
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String whereSql) throws Exception{
		long startTime = System.currentTimeMillis();
		
		MysqlSection sections[] = sectionManager.getSections();
		ArrayList<String> al2 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			String sql = "select "+mde.id.columnNameForMysql+" from " + mde.primaryTable+"_"+sections[i].getName();
			String where = filterWhereSql(sql,"带条件查询ID列表",cl,whereSql,false);
			if(where.length() > 0){
				sql += " where " + where;
			}
			al2.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al2.size() ; i++){
			sb.append("select * from ("+al2.get(i)+") as T"+i+" ");
			if(i < al2.size() -1){
				sb.append(" union all ");
			}
		}
		String sql = sb.toString();
		
		
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

			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));

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
		String orderByFields = "";
		//收集排序字段
		{
			ArrayList<String> al = new ArrayList<String>();
			String ss[] = orderSql.split(",");
			for(int i = 0 ; i < ss.length ; i++){
				String kk[] = ss[i].split(" ");
				if(kk[0].trim().length() > 0){
					String s = kk[0].trim();
					MetaDataField f = mde.getMetaDataField(cl, s);
					if(f != null){
						al.add(f.columnNameForMysql);
					}
				}
			}
			if(al.size() > 0){
				for(int i = 0 ; i < al.size() ; i++){
					orderByFields +=","+al.get(i);
				}
			}
		}
		String orderby = filterOrderBySql("","带条件分页查询ID列表",cl,orderSql,false);
		MysqlSection sections[] = sectionManager.getSections();
		ArrayList<String> al2 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			String sql = "select "+mde.id.columnNameForMysql+orderByFields+" from " + mde.primaryTable+"_"+sections[i].getName();
			String where = filterWhereSql(sql,"带条件分页查询ID列表",cl,whereSql,false);
			if(where.length() > 0){
				sql += " where " + where;
			}
			if(orderby.length() > 0){
				sql += " order by " + orderby;
			}
			sql +=" limit 0,"+end+"";
			
			al2.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al2.size() ; i++){
			sb.append("select * from ("+al2.get(i)+") as T"+i+" ");
			if(i < al2.size() -1){
				sb.append(" union all ");
			}
		}
		String sql = sb.toString();
		
		orderby = filterOrderBySql(sql,"带条件分页查询ID列表",cl,orderSql,false);

		if(orderby.length() > 0){
			sql = "select "+mde.id.columnNameForMysql+" from ("+sql+") T order by " + orderby;
		}
		//这里oracle分页  现在改为mysql分页
		
		//mysql分页语句 added by liuyang at 2012-04-26
		sql = "select * from ( " + sql + ") TT limit " + (start-1) + "," + (end-start);
		
		
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
		
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

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

		where = filterWhereSql(sql,"带条件分页查询对象列表",cl,whereSql,false);
		orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,false);

		MysqlSection sections[] = sectionManager.getSections();
		
		ArrayList<String> al3 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			sql = mde.getSqlForSelectObjectsByNativeConditionsInMysql(sections[i],where,orderby,end,false);
			al3.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al3.size() ; i++){
			sb.append("select * from ("+al3.get(i)+") as D"+i+" ");
			if(i < al3.size() -1){
				sb.append(" union all ");
			}
		}
		sql = sb.toString();
		

		if(orderby.length() > 0){
			sql = "select * from ("+sql+") TS order by " + orderby;
		}
		//这里oracle分页  现在改为mysql分页
		
		//mysql分页语句 added by liuyang at 2012-04-26
		sql = "select * from ( " + sql + ") TD limit " + (start-1) + "," + (end-start);
		
		
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			ArrayList<T> al = new ArrayList<T>();
			ArrayList<Long> al2 = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(mde.id.columnNameForMysql);
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
							entityModifedMap.put(id, ee);
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询对象列表,成功", OperatoinTrackerService.ACTION_READ, retList.size() ,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

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
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"分页"+orderSql+"查询对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

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
	 * 立即删除某一个对象，此方法会操作数据库
	 * 并且会从内存中清楚对应的数据。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void remove(T t) throws Exception{
		
		handleNotifyEvent();
		
		long id = mde.id.field.getLong(t);
		int version = mde.version.field.getInt(t);
		
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
	
	

	/**
	 * 向DataBase插入一个对象
	 * @param t
	 * @throws Exception
	 */
	public void insert_object(T t) throws Exception{
		long startTime = System.currentTimeMillis();
		long id = mde.id.field.getLong(t);
		MysqlSection section = sectionManager.insertSectionById(id);
		
		String sql = mde.getSqlForInsertObjectToPrimaryTableInMysql(section);
		
		String sql2[] = mde.getSqlForInsertObjectToSecondTableInMysql(section);

		Connection conn = null;
		
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
			PreparedStatement pstmt2[] = new PreparedStatement[sql2.length];
			for(int i = 0 ; i < sql2.length ; i++){
				pstmt2[i] = conn.prepareStatement(sql2[i]);
			}
			
			
			version = mde.version.field.getInt(t);
			
			pstmt.setLong(1, id);
			pstmt.setInt(2, version);
			//pstmt.setString(3, t.getClass().getName());废弃CNC字段
			pstmt.setString(3, null);//将CNC值设置为null
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
						//如果是字符串 则将其转成相应字符集后插入
						totalBytes += this.setPreparedStatementParameter(pstmt, parameterIndex, field, cl, f.field.get(t));
						parameterIndex++;
					}else{
						totalBytes += this.setPreparedStatementParameter(pstmt, parameterIndex, field, cl, null);
						parameterIndex++;
					}
				}
			}
			
			
			for(int i = 0 ; i < sql2.length ; i++){
				String secondTable = mde.secondTablesForMySql.get(i);
				pstmt2[i].setLong(1, id);
				it = mde.map.keySet().iterator();
				parameterIndex = 2;
				while(it.hasNext()){
					String field = it.next();
					MetaDataField f = mde.map.get(field);
					if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
						if(MetaDataEntity.contains(mdc, f)){
							this.setPreparedStatementParameterForSecondTable(pstmt2[i], parameterIndex, field, f.columnNameForMysql, f.field.get(t),f.field.getGenericType());
							parameterIndex += 1;
						}else{
							this.setPreparedStatementParameterForSecondTable(pstmt2[i], parameterIndex, field, f.columnNameForMysql, null,null);
							parameterIndex += 1;
						}
					}
				}
			}
			
			pstmt.executeUpdate();
			for(int i = 0 ; i < pstmt2.length ; i++){
				pstmt2[i].executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
		
			pstmt.close();
			for(int i = 0 ; i < pstmt2.length ; i++){
				pstmt2[i].close();
			}
			
			sectionManager.notifyInsertOneRow(section, id);
			
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "插入对象"+t.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes , (int)(System.currentTimeMillis()-startTime));
			
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "插入对象"+t.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes , (int)(System.currentTimeMillis()-startTime));
			
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
	 * 设置第二个表的参数
	 * @param pstmt
	 * @param parameterIndex
	 * @param field
	 * @param columnNames
	 * @param value
	 * @throws Exception
	 */
	int setPreparedStatementParameterForSecondTable(PreparedStatement pstmt,int parameterIndex,String field,String columnName,Object value,Type genericType) throws Exception{
		String s = "";
		if(value != null){
			s = JsonUtil.jsonFromObject(value,genericType);
		}
		
		pstmt.setString(parameterIndex, s);
		parameterIndex++;

	
		return s.length();
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
			//if(s != null)
			//	s = new String(s.getBytes(to_charset),charset);
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
	
	private boolean needAutoSaveImmdiately = false;
	private boolean destroy = false;
	private boolean running = false;

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
	 * 查询有多少个对象，此方法只表示有多少条记录。
	 */
	public long count() throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + mde.primaryTable;
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			
			MysqlSection sections[] = sectionManager.getSections();
			for(int i = 0 ; i < sections.length ; i++){
				sql = "select count(*) from " + mde.primaryTable+"_"+sections[i].getName();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				r += rs.getLong(1);
				rs.close();
			}
			
			stmt.close();
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}
		
			return r;
		}catch(Exception e){
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
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
	 * 查询某个条件下有多少个对象，条件是sql语句中where部分，不包括where
	 * 只支持对本表的查询，不支持多表联合查询。
	 * 
	 * @param whereSql
	 * @return
	 */
	
	public long count(Class<?> cl,String whereSql) throws Exception{
		
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + mde.primaryTable;
		String where = filterWhereSql(sql,"带条件查询个数",cl,whereSql,false);
		if(where.length() > 0){
			sql += " where " + where;
		}
		Connection conn = null;
		long r = 0;
		try{
			conn = getConnection();
			Statement stmt = conn.createStatement();
			MysqlSection sections[] = sectionManager.getSections();
			for(int i = 0 ; i < sections.length ; i++){
				sql = "select count(*) from " + mde.primaryTable+"_"+sections[i].getName();
				if(where.length() > 0){
					sql += " where " + where;
				}
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				r += rs.getLong(1);
				rs.close();
			}
			stmt.close();
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+whereSql+"查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
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
		
		String sql = null;
		long startTime = System.currentTimeMillis();
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
			InvocationHandler handler = new MyMyInvocationHandlerForMysql(this,id,data);
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
				long sqlOperationStartTime = System.currentTimeMillis();
				conn = getConnection();

				int totalBytes[] = new int[1];
				for(int i = 0 ; i < idList2.size() ; i++){
					
					startTime = System.currentTimeMillis();
					//int totalBytes[] = new int[1];
					long id =  idList2.get(i);
					
					MysqlSection section = sectionManager.selectSectionById(id);
					if(section == null) continue;
					
					StringBuffer sb = new StringBuffer();
					sb.append("select ");
					for(int k = 0 ; k < fields.length ; k++){
						MetaDataField mf = mde.getMetaDataField(mde.cl, fields[k]);
						if(mde.id.field.getName().equals(fields[k])){
							mf = mde.id;
						}
						sb.append(mf.columnNameForMysql);
						if(k < fields.length -1){
							sb.append(",");
						}
					}
					sb.append(" from " + mde.primaryTable+"_"+section.getName() + " where " + mde.id.columnNameForMysql+"=?");
					
					sql = sb.toString();
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, id);
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
					pstmt.close();
					/**
					 * 这里 在循环内部往监控并发读写服务中插入数据会造成读写多统计的情况
					 * 改放在循环外部
					 * commented by liuyang  at 2012-05-08
					 */
				//
					
					//OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID查询简单对象"+cl.getName()+",成功", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

				}
				OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID查询简单对象"+cl.getName()+",成功", OperatoinTrackerService.ACTION_READ, idList2.size(),totalBytes[0], (int)(System.currentTimeMillis()-sqlOperationStartTime));

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
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "通过ID列表查询简单对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,0, (int)(System.currentTimeMillis()-startTime));
			
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
						//if(s != null)
						//	s = new String(s.getBytes(charset),to_charset);
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

		InvocationHandler handler = new MyMyInvocationHandlerForMysql(this,id,data);
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
	
	public T construct_object_from_resultset(String sessionId,long id,ResultSet rs,int columnIndex,int totalBytes[]) throws Exception{
		long startTime = System.currentTimeMillis();
		
		MysqlSection section = sectionManager.selectSectionById(id);
		if(section == null){
			section = new MysqlSection();
			section.sectionIndex = 10000000;
		}
		HashMap<String,Integer> column2IndexForSelectObject = sectionColumn2IndexForSelectObject.get(section.getName());
		
		if(column2IndexForSelectObject == null){
			synchronized(section){
				column2IndexForSelectObject = sectionColumn2IndexForSelectObject.get(section.getName());
				if(column2IndexForSelectObject == null){
					HashMap<String,Integer> hh = new HashMap<String,Integer>();
					Iterator<String> it = mde.map.keySet().iterator();
					while(it.hasNext()){
						String field = it.next();
						MetaDataField f = mde.map.get(field);
					
						int k = rs.findColumn(f.columnNameForMysql);
						hh.put(f.columnNameForMysql, k);
			
						k = rs.findColumn(mde.id.columnNameForMysql);
						hh.put(mde.id.columnNameForMysql, k);
						k = rs.findColumn(mde.version.columnNameForMysql);
						hh.put(mde.version.columnNameForMysql, k);
						k = rs.findColumn(mde.CNC);
						hh.put(mde.CNC, k);
						//添加一个CID的列索引号
						k = rs.findColumn(mde.CID);
						hh.put(mde.CID,k);
					}
					column2IndexForSelectObject = hh;
					sectionColumn2IndexForSelectObject.put(section.getName(), column2IndexForSelectObject);
				}
			}
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
		//MetaDataClass mdc = mde.getMetaDataClassByName(className);
		
		T t = null;
		try{
		/*	Class<?> cl = Class.forName(className);
			t = (T)cl.newInstance();*/
			Class<?> cl = mdc.cl;
			//如果不是通过className来得到类 那么通过CID 来得到类
			
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
						String s = rs.getString(column2IndexForSelectObject.get(f.columnNameForMysql));
						if(s != null && s.equals("T")){
							f.field.setBoolean(t, true);
						}else{
							f.field.setBoolean(t, false);
						}
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Byte.TYPE || cl == Byte.class){
						byte b = rs.getByte(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setByte(t, b);
						columnIndex++;
						totalBytes[0]+=1;
					}else if(cl == Short.TYPE || cl == Short.class){
						short b = rs.getShort(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setShort(t, b);
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Integer.TYPE || cl == Integer.class){
						int b = rs.getInt(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setInt(t, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Character.TYPE || cl == Character.class){
						String b = rs.getString(column2IndexForSelectObject.get(f.columnNameForMysql));
						if(b != null && b.length() > 0){
							f.field.setChar(t, b.charAt(0));
						}
						columnIndex++;
						totalBytes[0]+=2;
					}else if(cl == Long.TYPE || cl == Long.class){
						long b = rs.getLong(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setLong(t, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == Float.TYPE || cl == Float.class){
						float b = rs.getFloat(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setFloat(t, b);
						columnIndex++;
						totalBytes[0]+=4;
					}else if(cl == Double.TYPE || cl == Double.class){
						double b = rs.getDouble(column2IndexForSelectObject.get(f.columnNameForMysql));
						f.field.setDouble(t, b);
						columnIndex++;
						totalBytes[0]+=8;
					}else if(cl == String.class){
						String s = rs.getString(column2IndexForSelectObject.get(f.columnNameForMysql));
							
						
						if(s != null){
							//s = new String(s.getBytes(charset),to_charset);
							f.field.set(t, s);
							totalBytes[0]+=s.length()*3;
						}
						columnIndex++;
					}else if(cl == java.util.Date.class || cl == java.sql.Date.class){
						java.util.Date date = rs.getTimestamp(column2IndexForSelectObject.get(f.columnNameForMysql));
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
					String s = rs.getString(column2IndexForSelectObject.get(f.columnNameForMysql));
					columnIndex++;
					if(s != null){
						json.append(s);
						totalBytes[0]+=s.length()*3;
					}
					if(json.length() > 0){
						try{
							//Object obj = JsonUtil.objectFromJson(new String(json.toString().getBytes(charset),to_charset), type);
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
		int version = rs.getInt(column2IndexForSelectObject.get(mde.version.columnNameForMysql));
		mde.version.field.setInt(t,version);
		totalBytes[0]+=4;
		
		if(logger.isDebugEnabled()){
			logger.debug("[通过ResultSet构建对象] [成功] [OK] [sessionId:{}] [{}] [id:{}] [version:{}] [cost:{}ms]",new Object[]{sessionId,t.getClass().getName(),id,version,System.currentTimeMillis()- startTime});
		}
		
		return t;
	}
	//由于存在这种情况：
	//起初表的横向拆分为 T1 T2 T3
	//但运行一段时间后，程序员增加了字段，导致产生了新的表拆分 T4
	//这时，对于一个对象，他可能在T1,T2,T3中有数据，在T4中无数据
	//而恰巧要更新这个对象的属性，正好在T4中，那么会导致更新失败。
	//
	//为了解决这种情况，在更新T2，T3...时，需要确保对应的数据记录存在。
	
	private boolean checkObjectExistsInSecondTable(MysqlSection section,int secondTableIndex,long id){
		
		if(section.isNeedCheckExistBeforeUpdateInSecondTable(secondTableIndex) == false) return true;
		
		EntityEntry<T> ee = entityModifedMap.get(id);
		if(ee.isRecordExistsInSecondTable(secondTableIndex)){
			return true;
		}
		long now = System.currentTimeMillis();
		
		Connection conn = null;
		String sql = "select count(1) from " + mde.secondTablesForMySql.get(secondTableIndex)+"_"+section.getName()+" where "+mde.id.columnNameForMysql+"_SEC_"+(secondTableIndex+1)+"=?";
		
		try{
			conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()){
				count = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			if(count > 0){
				ee.setRecordExistsInSecondTable(mde.secondTablesForMySql.size(), secondTableIndex);
			}
			
			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [检查某个对象是否存在某个副表中] [成功] [{}] [是否存在:{}] [分区:{}] [副表:T{}] [id:{}] [sql:{}] [cost:{}ms]",
						new Object[]{mde.cl.getName(),count>0,section.getName(),secondTableIndex+1,id,sql,System.currentTimeMillis()-now});
			}
			
			return (count>0);
		}catch(Exception e){
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [检查某个对象是否存在某个副表中] [失败] [{}] [是否存在:--] [分区:{}] [副表:T{}] [id:{}] [sql:{}] [cost:{}ms]",
						new Object[]{mde.cl.getName(),section.getName(),secondTableIndex+1,id,sql,System.currentTimeMillis()-now});
				logger.warn("[执行SQL] [检查某个对象是否存在某个副表中] [失败] [出现未知异常] [section:"+section.getName()+"] [id:"+id+"]",e);
			}
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					logger.warn("[执行SQL] [检查某个对象是否存在某个副表中] [失败] [关闭连接出现未知异常] [id:"+id+"]",e);
				}
			}
		}
		return true;
	}
	
	/**
	 * 更新一个对象，只更新有修改的属性，没有修改的属性不更新
	 * @param t
	 * @throws Exception
	 */
	public void update_object(T t,String fields[]) throws Exception{
		long startTime = System.currentTimeMillis();
		
		this.handleNotifyEvent();
		
		Connection conn = null;
		long id = mde.id.field.getLong(t);
		MysqlSection section = sectionManager.selectSectionById(id);
		if(section == null){
			throw new Exception("通过Id找不到对应的分区");
		}
		String sql = mde.getSqlForUpdateOneObjectByModifiedFieldsOnPrimaryTableInMysql(section,t.getClass(),fields);
		String sql2[] = mde.getSqlForUpdateOneObjectByModifiedFieldsOnSecondTableInMysql(section,t.getClass(),fields);
		String sql2Insert[] = mde.getSqlForInsertOneObjectByModifiedFieldsOnSecondTableInMysql(section,t.getClass(),fields);
		
		int version = 0;
		int totalBytes = 0;
		try{
			conn = getConnection();
			
			version = mde.version.field.getInt(t);
			
			conn.setAutoCommit(false);
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2[] = new PreparedStatement[sql2.length];
			if(sql != null)
				pstmt = conn.prepareStatement(sql);
			for(int i = 0 ; i < pstmt2.length ; i++){
				if(sql2[i] != null){
					if(checkObjectExistsInSecondTable(section,i,id)){
						pstmt2[i] = conn.prepareStatement(sql2[i]);
					}else{
						pstmt2[i] = conn.prepareStatement(sql2Insert[i]);
					}
				}
			}
			
			if(pstmt != null){
				int parameterIndex = 1;
				for(int i = 0 ; i < fields.length ; i++){
					MetaDataField f = mde.getMetaDataField(t.getClass(), fields[i]);
					if(f.inPrimaryTable){
						Class<?> cl = f.field.getType();
						//如果是字符串 则将其转成相应字符集后更新
						totalBytes += setPreparedStatementParameter(pstmt, parameterIndex, fields[i], cl, f.field.get(t));
						parameterIndex++;
					}
				}
				pstmt.setLong(parameterIndex, id);
			}
			
			for(int i = 0 ; i < pstmt2.length ; i++){
				if(pstmt2[i] == null) continue;
				String secondTable = mde.secondTablesForMySql.get(i);
				
				int parameterIndex = 1;
				for(int j = 0 ; j < fields.length ; j++){
					MetaDataField f = mde.getMetaDataField(t.getClass(), fields[j]);
					if(f.inPrimaryTable == false && f.tableNameForMysql.equals(secondTable)){
						Object obj = f.field.get(t);
						totalBytes += setPreparedStatementParameterForSecondTable(pstmt2[i],parameterIndex,fields[j],f.columnNameForMysql,obj,f.field.getGenericType());
						parameterIndex += 1;
					}
				}
				pstmt2[i].setLong(parameterIndex, id);
			}
			
			if(pstmt != null)
				pstmt.executeUpdate();
			for(int i = 0 ; i < pstmt2.length ; i++){
				if(pstmt2[i] == null) continue;
				pstmt2[i].executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
			
			if(pstmt != null)
				pstmt.close();
			
			for(int i = 0 ; i < pstmt2.length ; i++){
				if(pstmt2[i] == null) continue;
				pstmt2[i].close();
			}

			mde.version.field.setInt(t, version+1);
		
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "更新对象"+t.getClass().getName()+"部分属性,成功", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes, (int)(System.currentTimeMillis()-startTime));
			
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "更新对象"+t.getClass().getName()+"部分属性,失败", OperatoinTrackerService.ACTION_WRITE, 1,totalBytes, (int)(System.currentTimeMillis()-startTime));
			
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
		
		Connection conn = null;
		long id = mde.id.field.getLong(t);
		MysqlSection section = sectionManager.selectSectionById(id);
		if(section == null){
			throw new Exception("通过Id找不到对应的分区");
		}
		
		String sql = mde.getSqlForRemoveObjectFromPrimaryTableInMysql(section);
		String sql2[] = mde.getSqlForRemoveObjectFromSecondTableInMysql(section);
		
		int version = 0;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			PreparedStatement pstmt2[] = new PreparedStatement[sql2.length];
			for(int i = 0 ; i < sql2.length ; i++){
				pstmt2[i] = conn.prepareStatement(sql2[i]);
			}
			id = mde.id.field.getLong(t);
			version = mde.version.field.getInt(t);
			
			pstmt.setLong(1, id);
			for(int i = 0 ; i < sql2.length ; i++){
				pstmt2[i].setLong(1, id);
			}
			
			int k = pstmt.executeUpdate();
			for(int i = 0 ; i < sql2.length ; i++){
				pstmt2[i].executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);

			mde.version.field.setInt(t, -2);
		
			sectionManager.notifyRemoveOneRow(section);
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "删除对象"+t.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1, 8,(int)(System.currentTimeMillis()-startTime));
			
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "删除对象"+t.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,8, (int)(System.currentTimeMillis()-startTime));
			
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
	 * @param sql
	 * @param description
	 * @param cl
	 * @param whereSql
	 * @param addTablePrefix
	 * @return
	 * @throws Exception
	 */
	
	/**
	 * 批量删除多个对象
	 * @param t
	 * @throws Exception
	 */
	public boolean batch_delete_object(T[] ts) throws Exception{
		long startTime = System.currentTimeMillis();
		
		this.handleNotifyEvent();
		
		long[] deletedIds = new long[ts.length];
		
		Connection conn = null;
		conn = getConnection();
		conn.setAutoCommit(false);
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2[] = null;
		
		int version = 0;
		int deletedCount = 0;
		try{
			for(T t : ts )
			{
				long id = mde.id.field.getLong(t);
				MysqlSection section = sectionManager.selectSectionById(id);
				if(section == null){
					throw new Exception("通过Id找不到对应的分区");
				}
				
				String sql = mde.getSqlForRemoveObjectFromPrimaryTableInMysql(section);
				String sql2[] = mde.getSqlForRemoveObjectFromSecondTableInMysql(section);
				
				pstmt = conn.prepareStatement(sql);
				pstmt2 = new PreparedStatement[sql2.length];
				for(int i = 0 ; i < sql2.length ; i++){
					pstmt2[i] = conn.prepareStatement(sql2[i]);
				}
				id = mde.id.field.getLong(t);
				version = mde.version.field.getInt(t);
				
				pstmt.setLong(1, id);
				for(int i = 0 ; i < sql2.length ; i++){
					pstmt2[i].setLong(1, id);
				}
				
				int k = pstmt.executeUpdate();
				for(int i = 0 ; i < sql2.length ; i++){
					pstmt2[i].executeUpdate();
				}
				
				mde.version.field.setInt(t, -2);
				
				sectionManager.notifyRemoveOneRow(section);
			
				deletedIds[deletedCount++] = id;
			}
			conn.commit();
			conn.setAutoCommit(true);
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量删除对象"+ts.getClass().getName()+",成功", OperatoinTrackerService.ACTION_WRITE, 1, 8*ts.length,(int)(System.currentTimeMillis()-startTime));
			
			if(logger.isInfoEnabled()){
				logger.info("[执行SQL] [批量删除对象] [成功] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,"--","--",System.currentTimeMillis()-startTime});
			}
			return true;
		}catch(Exception e){
			conn.rollback();
			conn.setAutoCommit(true);
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "批量删除对象"+ts.getClass().getName()+",失败", OperatoinTrackerService.ACTION_WRITE, 1,8*ts.length, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.warn("[执行SQL] [删除对象] [失败] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,"--","--",System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [删除对象] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			
			if(pstmt != null)
			{
				pstmt.close();
			}
			
			if(pstmt2 != null)
			{
				if(pstmt2.length > 0)
				{
					for(PreparedStatement p : pstmt2)
					{
						p.close();
					}
				}
				else
				{
					pstmt2 = null;
				}
			}
			
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [删除对象] [关闭连接失败] [{}] [ids:{}] [version:{}] [sql:{}] [sql2:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),deletedIds,version,"--","--",System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [删除对象] [关闭连接失败] ["+mde.cl.getName()+"]", e);
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
	public int batch_delete_by_Ids(Class<?> cl,List<Long>ids) throws Exception{
		
		long startTime = System.currentTimeMillis();
		int k = 0;
		
		Connection conn = null;
		
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			
			Map<MysqlSection,List<Long>> sectionMap = new HashMap<MysqlSection, List<Long>>();
			MysqlSection sections[] = sectionManager.getSections();
			
			for(int i=0; i<sections.length; i++)
			{
				MysqlSection section = sections[i];
				List<Long> sectionIds = new ArrayList<Long>();
				
				Iterator<Long> it = ids.iterator();
				while(it.hasNext())
				{
					long curid = it.next().longValue();
				
					//如果id大于等于当前section的最小id，则确认此id在此区间内 放入section对应的list中
					if(curid >= section.minId && curid <= section.maxId )
					{
						sectionIds.add(curid);
						it.remove();
					}
				}
				
				sectionMap.put(section, sectionIds);
			}
			
		
			
			Iterator<MysqlSection> it = sectionMap.keySet().iterator();
			
			while(it.hasNext()){
				
				MysqlSection keySection = it.next();
				
				List<Long> sectionIds = sectionMap.get(keySection);
				
				String sql = "delete  from " + mde.primaryTable+"_"+keySection.getName();
				//拼装sql id = '' or id or id
				String where = "";
				
				
				for(int j = 0; j < sectionIds.size(); j++)
				{
					
					if(j == 0)
					{
						where += mde.id.columnNameForMysql + " = ? ";
					}
					else
					{
						where += "or " + mde.id.columnNameForMysql + " = ? ";
					}
					
				}
				
				if(where.trim().length() > 0)
				{
					sql += " where " + where;
					PreparedStatement pstmt = conn.prepareStatement(sql);
					for(int j = 0; j < sectionIds.size(); j++)
					{
						pstmt.setLong(j+1, sectionIds.get(j));
					}
					
					
					k+=pstmt.executeUpdate();
					pstmt.close();
				}
				
				
				for(int j = 0; j < mde.secondTablesForMySql.size(); j++)
				{
					String secondTable = mde.secondTablesForMySql.get(j);
					String seconTableId = mde.id.columnNameForMysql +"_SEC_"+(j+1);
					String sql2 = "delete  from " + secondTable+"_"+keySection.getName();
					String where1 = "";
					
					for(int jj = 0; jj < sectionIds.size();jj++)
					{
						if(jj == 0)
						{
							where1 += seconTableId + " = ? " ;
						}
						else
						{
							where1 += " or " + seconTableId + " = ? ";
						}
						
						
					}
					
					
					//保证有where 条件
					if(where1.trim().length() > 0)
					{
						sql2 += " where " + where1;
						PreparedStatement pstmt = conn.prepareStatement(sql2);
						for(int jj = 0; jj < sectionIds.size(); jj++)
						{
							pstmt.setLong(jj+1, sectionIds.get(jj));
						}
						
						pstmt.executeUpdate();
						pstmt.close();
					}
				}
				
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "成功", OperatoinTrackerService.ACTION_WRITE, k, k*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,"--",System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [批量删除] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,"--",System.currentTimeMillis()-startTime});
				}	
			}
		
		}catch(Exception e){
			conn.rollback();
			conn.setAutoCommit(true);
			
			OperatoinTrackerService.operationEnd("为了合服批量删除数据", "失败", OperatoinTrackerService.ACTION_WRITE, k,k*8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [批量删除] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,"--",System.currentTimeMillis()-startTime});
				logger.warn("[执行SQL] [批量删除] [失败] ["+mde.cl.getName()+"]", e);
			}
			throw e;
		}finally{
			
			if(conn != null){
				try{
					conn.close();
					if((System.currentTimeMillis()-startTime) > 1000)
					{
						logger.warn("[执行SQL] [批量删除] [关闭连接] [成功] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}
				}catch(Exception e){
					if(logger.isWarnEnabled()){
						logger.info("[执行SQL] [批量删除] [关闭连接失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),k,"--",System.currentTimeMillis()-startTime});
						logger.warn("[执行SQL] [批量删除] [关闭连接失败] ["+mde.cl.getName()+"]", e);
					}
				}
			}
		}
		return k;
	}
	
	

	
	
	
	
	private String filterWhereSql(String sql,String description,Class<?> cl,String whereSql,boolean addTablePrefix) throws Exception{
		if(whereSql == null || whereSql.trim().length() == 0) return "";
		//whereSql = new String(whereSql.getBytes(to_charset),charset);
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
						where = where.replaceAll(fields[i], mde.primaryTable+"."+f.columnNameForMysql);
					else
						where = where.replaceAll(fields[i], f.columnNameForMysql);
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [{}] [非主表字段"+fields[i]+"不能作为查询条件] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,whereSql,System.currentTimeMillis()-startTime});
					}
					throw new Exception("非主表字段"+fields[i]+"不能作为查询条件");
				}
			}else if(fields[i].equals(mde.id.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.id.columnNameForMysql);
				else
					where = where.replaceAll(fields[i], mde.id.columnNameForMysql);
			}else if(fields[i].equals(mde.version.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.version.columnNameForMysql);
				else
					where = where.replaceAll(fields[i], mde.version.columnNameForMysql);
			}
			else if (fields[i].equals("?")) {
				//参数查询 什么都不做
			}
			else{
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
		//orderBySql = new String(orderBySql.getBytes(to_charset),charset);
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
						where = where.replaceAll(fields[i], mde.primaryTable+"."+f.columnNameForMysql);
					else
						where = where.replaceAll(fields[i], f.columnNameForMysql);
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[执行SQL] [{}] [非主表字段"+fields[i]+"不能作为排序条件] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,orderBySql,System.currentTimeMillis()-startTime});
					}
					throw new Exception("非主表字段"+fields[i]+"不能作为排序条件");
				}
			}else if(fields[i].equals(mde.id.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.id.columnNameForMysql);
				else
					where = where.replaceAll(fields[i], mde.id.columnNameForMysql);
			}else if(fields[i].equals(mde.version.field.getName())){
				if(addTablePrefix)
					where = where.replaceAll(fields[i], mde.primaryTable+"."+mde.version.columnNameForMysql);
				else
					where = where.replaceAll(fields[i], mde.version.columnNameForMysql);
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [{}] [查询字段"+fields[i]+"不是此类的属性] [{}] [count:{}] [sql:{}] [where:{}] [cost:{}ms]",new Object[]{description,mde.cl.getName(),0,sql,orderBySql,System.currentTimeMillis()-startTime});
				}
				throw new Exception("查询字段"+fields[i]+"不是此类的属性");
			}
		}
		return where;
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
	
	public static String toString(String values[]){
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
	
	public static String toString(ArrayList<String> al){
		if(al == null) return "null";
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al.size() ; i++){
			
			sb.append(al.get(i));
			
			if(i < al.size() - 1){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 检查预编译语句的参数值 根据类型进行转换
	 */
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
	
	/**
	 * 设置预编译sql语句的参数值
	 * @param pstmt
	 * @param parameterIndex
	 * @param paramValues
	 * @throws SQLException
	 */
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
	
/**
 * count支持预编译语句 从而提高效率
 */
	public long count(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception{
		long startTime = System.currentTimeMillis();
		String sql = "select count(*) from " + mde.primaryTable;
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
			
			MysqlSection sections[] = sectionManager.getSections();
			for(int i = 0 ; i < sections.length ; i++){
				sql = "select count(*) from " + mde.primaryTable+"_"+sections[i].getName();
				if(where.length() > 0){
					sql += " where " + where;
				}
				PreparedStatement pstmt = conn.prepareStatement(sql);
				setParamValues(pstmt,1,paramValues);
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				r += rs.getLong(1);
				rs.close();
				pstmt.close();
			}
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询个数,成功", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数查询个数] [成功] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询个数,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));
			
			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询个数] [失败] [{}] [count:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),r,sql,System.currentTimeMillis()-startTime});
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
	
		where = filterWhereSql(sql,"带条件分页查询对象列表",cl,preparedWhereSql,false);
		orderby = filterOrderBySql(sql,"带条件分页查询对象列表",cl,orderSql,false);
	
		MysqlSection sections[] = sectionManager.getSections();
		
		ArrayList<String> al3 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			sql = mde.getSqlForSelectObjectsByNativeConditionsInMysql(sections[i],where,orderby,end,true);
			al3.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al3.size() ; i++){
			sb.append("select * from ("+al3.get(i)+") as D"+i+" ");
			if(i < al3.size() -1){
				sb.append(" union all ");
			}
		}
		sql = sb.toString();
		

		if(orderby.length() > 0){
			sql = "select * from ("+sql+") TS order by " + orderby;
		}
		//这里oracle分页  现在改为mysql分页
		
		//mysql分页语句 added by liuyang at 2012-04-26
		//sql = "select * from ( " + sql + ") TD limit " + (start-1) + "," + (end-start);
		sql = "select * from ( " + sql + ") TD limit ?,?";
		
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
			
			for(int i = 0 ; i < sections.length ; i++){
				this.setParamValues(pstmt, (paramValues.length+1)*i+1, paramValues);
				pstmt.setLong((paramValues.length+1)*i+1+paramValues.length, end);
			}
			pstmt.setLong((paramValues.length+1)*sections.length+1, start-1);
			pstmt.setLong((paramValues.length+1)*sections.length+2, end-start);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<T> al = new ArrayList<T>();
			ArrayList<Long> al2 = new ArrayList<Long>();
			while(rs.next()){
				long id = rs.getLong(mde.id.columnNameForMysql);
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
							entityModifedMap.put(id, ee);
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
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询对象列表,成功", OperatoinTrackerService.ACTION_READ, retList.size() ,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数分页查询对象列表] [成功] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:{}] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,retList.size(),sql,System.currentTimeMillis()-startTime});
				}
			}
			return retList;
		}catch(Exception e){
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询对象列表,失败", OperatoinTrackerService.ACTION_READ, 1,totalBytes[0], (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询对象列表] [失败] [sessionId:{}] [{}] [where:{}] [order:{}] [start:{}] [end:{}] [ret:-] [sql:{}] [cost:{}ms]",new Object[]{sessionId,mde.cl.getName(),preparedWhereSql,orderSql,start,end,sql,"-",System.currentTimeMillis()-startTime});
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

	/**
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * @param whereSql
	 * @return
	 */
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception{
		long startTime = System.currentTimeMillis();
		
		MysqlSection sections[] = sectionManager.getSections();
		ArrayList<String> al2 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			String sql = "select "+mde.id.columnNameForMysql+" from " + mde.primaryTable+"_"+sections[i].getName();
			String where = filterWhereSql(sql,"带条件查询ID列表",cl,preparedWhereSql,false);
			if(where.length() > 0){
				sql += " where " + where;
			}
			al2.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al2.size() ; i++){
			sb.append("select * from ("+al2.get(i)+") as T"+i+" ");
			if(i < al2.size() -1){
				sb.append(" union all ");
			}
		}
		String sql = sb.toString();
		
		
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
			
			for(int i = 0 ; i < sections.length ; i++){
				this.setParamValues(pstmt, paramValues.length*i+1, paramValues);
			}
			
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

			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1, 8,(int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
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
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql,long start,long end) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		String orderByFields = "";
		//收集排序字段
		{
			ArrayList<String> al = new ArrayList<String>();
			String ss[] = orderSql.split(",");
			for(int i = 0 ; i < ss.length ; i++){
				String kk[] = ss[i].split(" ");
				if(kk[0].trim().length() > 0){
					String s = kk[0].trim();
					MetaDataField f = mde.getMetaDataField(cl, s);
					if(f != null){
						al.add(f.columnNameForMysql);
					}
				}
			}
			if(al.size() > 0){
				for(int i = 0 ; i < al.size() ; i++){
					orderByFields +=","+al.get(i);
				}
			}
		}
		
		String orderby = filterOrderBySql("","带条件分页查询ID列表",cl,orderSql,false);
		
		MysqlSection sections[] = sectionManager.getSections();
		ArrayList<String> al2 = new ArrayList<String>();
		for(int i = 0 ; i < sections.length ; i++){
			String sql = "select "+mde.id.columnNameForMysql+orderByFields+" from " + mde.primaryTable+"_"+sections[i].getName();
			String where = filterWhereSql(sql,"带条件分页查询ID列表",cl,preparedWhereSql,false);
			if(where.length() > 0){
				sql += " where " + where;
			}
			if(orderby.length() > 0){
				sql += " order by " + orderby;
			}
			sql +=" limit 0,?"; 
			al2.add(sql);
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < al2.size() ; i++){
			sb.append("select * from ("+al2.get(i)+") as T"+i+" ");
			if(i < al2.size() -1){
				sb.append(" union all ");
			}
		}
		String sql = sb.toString();
		
		
		orderby = filterOrderBySql(sql,"带条件分页查询ID列表",cl,orderSql,false);
		
		if(orderby.length() > 0){
			sql = "select "+mde.id.columnNameForMysql+" from ("+sql+") T order by " + orderby;
		}
		//这里oracle分页  现在改为mysql分页
		
		//mysql分页语句 added by liuyang at 2012-04-26
		//sql = "select * from ( " + sql + ") TT limit " + (start-1) + "," + (end-start);
		sql = "select * from ( " + sql + ") TT limit ?,?";

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
			
			for(int i = 0 ; i < sections.length ; i++){
				this.setParamValues(pstmt, (paramValues.length+1)*i+1, paramValues);
				pstmt.setLong((paramValues.length+1)*i+1+paramValues.length, end);
			}
			pstmt.setLong((paramValues.length+1)*sections.length+1, start-1);
			pstmt.setLong((paramValues.length+1)*sections.length+2, end-start);
			
			
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
		
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询ID列表,成功", OperatoinTrackerService.ACTION_READ, r.length, r.length*8,(int)(System.currentTimeMillis()-startTime));

			long costTime = System.currentTimeMillis()-startTime;
			if(costTime < 1000){
				if(logger.isInfoEnabled()){
					logger.info("[执行SQL] [带条件参数分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[执行SQL] [带条件参数分页查询ID列表] [成功] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),toString(r),sql,System.currentTimeMillis()-startTime});
				}	
			}
		
			return r;
		}catch(Exception e){
			
			OperatoinTrackerService.operationEnd("所有SimpleEntityManager综合", "带条件"+preparedWhereSql+"分页"+orderSql+"查询ID列表,失败", OperatoinTrackerService.ACTION_READ, 1,8, (int)(System.currentTimeMillis()-startTime));

			if(logger.isWarnEnabled()){
				logger.info("[执行SQL] [带条件参数分页查询ID列表] [失败] [{}] [ids:{}] [sql:{}] [cost:{}ms]",new Object[]{mde.cl.getName(),"-",sql,System.currentTimeMillis()-startTime});
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
	
	/**
	 * 此功能只会删除父类中定义的索引 还没有实现删除子类索引中的功能 只针对合服用
	 * @param t
	 * @param indexName
	 */
	
	public void dropEntityIndex(Class cl)
	{
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
					
					Iterator<SimpleIndexImpl> it = this.mde.indexListForMySql.iterator();
					while(it.hasNext())
					{
						SimpleIndexImpl indexImpl = it.next();
						if( indexImpl != null)
						{
							if(name.equals(indexImpl.name))
							{
								
								MysqlSection sections[] = sectionManager.getSections();
								
								for(int k=0; k<sections.length; k++)
								{
									MysqlSection section = sections[k];
									String tableName = mde.primaryTable+"_"+section.getName();
									String sql = "drop index " +name + " on "+ tableName;
									Statement stmt = conn.createStatement();
									stmt.executeUpdate(sql);
									conn.commit();
									
									logger.warn("["+thread.getName()+"] [删除索引] [成功] [ok] ["+name+"] ["+tableName+"] ["+sql+"] ["+cl.getName()+"] " +
											"[cost:"+(System.currentTimeMillis()-startTime)+"ms]");
								}
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

