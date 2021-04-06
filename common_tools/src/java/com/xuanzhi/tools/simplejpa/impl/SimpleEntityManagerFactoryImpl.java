package com.xuanzhi.tools.simplejpa.impl;

import java.io.InputStream;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.dbpool.ResettingPoolObserver;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.XmlUtil;


/**
 * 获取EntityManager的Factory类
 * 此类需要一个配置文件simpleEMF.xml,可以将此配置文件放入到某个路径下，此路径在classpath内。
 * 或者通过 System.setProperty("simpleEMF.config","/.../.../simpleEMF.xml")设置。
 * 比如在java命令行中  -DsimpleEMF.config=/home/game/simpleEMF.xml
 * 
 * 下面是此xml结构
 * 
 * <?xml version='1.0' encoding='utf-8'>
 * <persistence-units>
 * 		<!-- 服务器ID -->
 * 		<serverId>100</serverId>
 *
 *		<entity-manager super-class='' checkIntervalInSeconds=''>
 *			<class></class>
 *		</entity-manager>	
 *
 *		<entity-manager super-class='' checkIntervalInSeconds=''>
 *		</entity-manager>	
 *
 *
 * 		<database name='test-pool' type='oracle|mysql|server2server'>  
 * 			<driver>oracle.jdbc.driver.OracleDriver</driver>
 * 			<url>jdbc:oracle:thin:@116.213.142.183:1521:ora183</url>
 * 			<username>simplejpatest</username>
 * 			<password>simplejpatest</password>
 * 			<maxConn>30</maxConn>
 * 			<timeoutSeconds>300</timeoutSeconds>
 * 			<checkoutSeconds>300</checkoutSeconds>
 * 		</database>
 * 
 * </persistence-units>
 * 
 *
 */
public class SimpleEntityManagerFactoryImpl {
	
	static Logger logger = LoggerFactory.getLogger(SimpleEntityManagerFactory.class);
	
	public static final String CONFIG_KEY = "simpleEMF.config";
	public static final String CONFIG_FILE = "simpleEMF.xml";
	
	static boolean initialized = false;
	static boolean initialize_success = false;
	static HashMap<String,SimpleEntityManager<?>> emMap = new HashMap<String,SimpleEntityManager<?>>();
	static ConnectionPool pool = null;
	static int serverId;
	static String serverName;
	public static ArrayList<EntityManagerInfo> eiList = new 	ArrayList<EntityManagerInfo>();
	static String userName;
	
	private static Map<Integer,String> cidInfo = new HashMap<Integer, String>();
	
	//private static Map<Integer,String> classInfo = new HashMap<Integer, String>();
	
	/**
	 * 加一个变量dbType用来判断存储数据库类型，为了方便添加新的创建 和mysql相关的SimpleEntityManager方法所加，可能以后会进行改进
	 * modified by liuyang at 2012-04-25
	 * @author yuwenbao
	 *
	 */
	private static String dbType = "oracle";
	
	public static String getDbType()
	{
		return dbType;
	}
	
	public static class EntityManagerInfo{
		public Class<?> cl;
		public ArrayList<Class<?>> subClass = new ArrayList<Class<?>>();
		public int checkIntervalInSeconds;
		public int maxRowPerTableForMysql = 50000000;
		public Map<Class<?>,Integer> classInfo = new HashMap<Class<?>,Integer>();
	}
	
	public static <T> SimpleEntityManager<T> getSimpleEntityManager(Class<T> cl){
		if(initialized == false){
			synchronized(SimpleEntityManagerFactoryImpl.class){
				if(initialized ==false){
					try{
						init();
					}catch(RuntimeException e){
						throw e;
					}catch(Exception e){
						throw new RuntimeException("初始化失败，出现异常",e);
					}
				}
			}
		}
		if(initialize_success == false){
			throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，请先解决错误！");
		}
		EntityManagerInfo info = null;
		for(EntityManagerInfo ei : eiList){
			if(ei.cl == cl){
				info = ei;
			}
		}
		if(info == null){
			throw new java.lang.IllegalArgumentException("指定的类"+cl.getName()+"不是配置中的EntityManager的super-class！");
		}
		
		SimpleEntityManager<T> em = (SimpleEntityManager<T>)emMap.get(info.cl.getName());
		if(em != null) return em;
		synchronized(cl){
			em = (SimpleEntityManager<T>)emMap.get(info.cl.getName());
			if(em != null) return em;
			//em = createSimpleEntityManager(cl,info);
			
				/**
				 * 根据数据库类型来判断实例化哪种SimpleEntityManager
				 * 目前只支持oracle 和 mysql，若数据库类型不为这两种 则报错 提示类型不支持
				 * modified by liuyang  at 2012-04-25
				 */
				if(dbType.equalsIgnoreCase("mysql"))
				{
					em = createMysqlSimpleEntityManager(cl,info);
				}
				else if	(dbType.equalsIgnoreCase("oracle"))
				{
					em = createSimpleEntityManager(cl,info);
				}
				else if(dbType.equalsIgnoreCase("server2server")){
					em = createSimpleEntityManagerForS2S(cl,info);
				}
				else 
				{
					throw new java.lang.IllegalArgumentException(dbType+"数据库类型目前暂不支持！");
				}
				
			if(em != null){
				emMap.put(info.cl.getName(),em);
			}
			return em;
		}
	}
	
	/**
	 * 创建一个方法 返回SimpleEntityManager的mysql实现
	 * added by liuyang at 2012-04-25
	 * @param cl
	 * @param info
	 * @return
	 */
	private static <T> SimpleEntityManager<T> createMysqlSimpleEntityManager(Class<T> cl,EntityManagerInfo info){
		SimpleEntityManagerMysqlImpl<T> em = new SimpleEntityManagerMysqlImpl<T>();
		em.setAutoSave(true, info.checkIntervalInSeconds);
		em.maxRowNum = info.maxRowPerTableForMysql;
		em.serverId = serverId;
		em.setConnectionPool(pool);
		try{
			em.init(cl, info.subClass, info.classInfo);
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException("初始化失败SimpleEntityManager<"+cl.getName()+">失败，出现异常",e);
		}
		
		return em;
	}
	
	private static <T> SimpleEntityManager<T> createSimpleEntityManager(Class<T> cl,EntityManagerInfo info){
		SimpleEntityManagerOracleImpl<T> em = new SimpleEntityManagerOracleImpl<T>();
		em.setAutoSave(true, info.checkIntervalInSeconds);
		em.serverId = serverId;
		em.username = userName;
		em.setConnectionPool(pool);
		try{
			em.init(cl,info.subClass,info.classInfo);
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException("初始化失败SimpleEntityManager<"+cl.getName()+">失败，出现异常",e);
		}
		
		return em;
	}
	
	private static <T> SimpleEntityManager<T> createSimpleEntityManagerForS2S(Class<T> cl,EntityManagerInfo info){
		SimpleEntityManagerForS2SImpl<T> em = new SimpleEntityManagerForS2SImpl<T>();
		em.setAutoSave(true, info.checkIntervalInSeconds);
		em.serverId = serverId;
		//em.nssem = sse;
		//em.managerKey = managerKey;
		//em.setConnectionPool(pool);
		try{
			em.init(cl,info.subClass,info.classInfo);
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException("初始化失败SimpleEntityManager<"+cl.getName()+">失败，出现异常",e);
		}
		
		return em;
	}
	
	private static void init() throws Exception{
		if(initialized) return;
		initialized =  true;
		Document dom = null;
		String filePath = System.getProperty(CONFIG_KEY);
		if(filePath == null){
			URL url = null;
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if(classLoader != null) {
				 url = classLoader.getResource(CONFIG_FILE);
			}
			if(url == null && classLoader != null) {
				 url = classLoader.getResource("main/conf/"+CONFIG_FILE);
			}
			if(url == null){
				url = SimpleEntityManagerFactoryImpl.class.getClassLoader().getResource("conf/"+CONFIG_FILE);
			}
			if(url == null){
				url = SimpleEntityManagerFactoryImpl.class.getClassLoader().getResource("main/conf/"+CONFIG_FILE);
			}
			if(url == null){
				url = ClassLoader.getSystemResource(CONFIG_FILE);
			}
			if(url == null){	 
				throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，找不到配置文件["+CONFIG_FILE+"]，请先检查配置！");
			}
			
			InputStream input = url.openStream();
			dom = XmlUtil.load(input);
			input.close();
		}else{
			dom = XmlUtil.load(filePath);
		}
		Element root = dom.getDocumentElement();
		
		serverId = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(root, "serverId"), -1);

		if(serverId == -1 || serverId > 999){
			throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，没有正确配置serverId的值！");
		}
		serverName = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "serverName"), ""+serverId ,null);
		
		Element eles[] = XmlUtil.getChildrenByName(root, "entity-manager");
		
		for(int i = 0 ; i < eles.length ; i++){
			EntityManagerInfo ei = new EntityManagerInfo();
			ei.checkIntervalInSeconds = XmlUtil.getAttributeAsInteger(eles[i], "checkIntervalInSeconds", 30);
			
			ei.maxRowPerTableForMysql = XmlUtil.getAttributeAsInteger(eles[i], "maxRowPerTableForMysql", 50000000);
			
			String className = XmlUtil.getAttributeAsString(eles[i], "super-class", null);
			
			//配置文件中添加id 属性用来在将来将数据库记录转换成具体的类别时使用
			int scid = XmlUtil.getAttributeAsInteger(eles[i], "id", -1);
			
			if(cidInfo.containsKey(scid))
			{
				throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，super-class "+className+"的id值"+scid+"已存在，id值不能重复！");
			}
			if(scid < 0)
			{
				throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，super-class"+className+"的id值"+scid+"不合法，id必须为正整数！");
			}
			
			
			//将超类id 放入到Map中 以供使用
			cidInfo.put(scid, className);
			
			try{
				ei.cl = Class.forName(className);
				
				if(ei.classInfo.containsKey(ei.cl))
				{
					throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，id值"+scid+"对应的类"+className+"已存在！");
				}
				
				//将Class 信息和对应的类标识放入map中以供以后使用
				ei.classInfo.put(ei.cl,scid);
				
			}catch(Exception e){
				e.printStackTrace(System.out);
				throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，配置的Entity["+className+"]类找不到定义！");
			}
			Element subEs[] = XmlUtil.getChildrenByName(eles[i], "class");
			for(int j = 0 ; j < subEs.length ; j++){
				try{
					
					String subClassName = XmlUtil.getValueAsString(subEs[j], null);
					//配置文件中添加id 属性用来在将来将数据库记录转换成具体的类别时使用 id必须为正整数
					int cid = XmlUtil.getAttributeAsInteger(subEs[j], "id", -1);
					
					if(cid < 0)
					{
						throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，class"+subClassName+"的id值"+cid+"不合法，id必须为正整数！");
					}
					
					if(cidInfo.containsKey(cid))
					{
						throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，class"+subClassName+"的id值"+cid+"已存在，id值不能重复！");
					}
					
					
					
					
					cidInfo.put(cid,subClassName);
					
					Class<?> cl = Class.forName(subClassName);
					ei.subClass.add(cl);
					
					//将Class 信息和对应的类标识放入map中以供以后使用
					if(ei.classInfo.containsKey(cl))
					{
						throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，id值"+cid+"对应的类"+subClassName+"已存在！");
					}
					ei.classInfo.put(cl,cid);
					
				}catch(Exception e){
					e.printStackTrace(System.out);
					throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，配置的Entity["+className+"]类找不到定义！");
				}
			}
			eiList.add(ei);

		}
		
		Element database = XmlUtil.getChildByName(root, "database");
		String name = XmlUtil.getAttributeAsString(database, "name", "name",null);
		
		/**
		 * 由于需要添加对mysql数据库的支持，故对原有加载属性值若没有默认为oracle 的做法进行改动 改为没有默认值的加载方法
		 * modified by liuyang at 2012-04-25
		 */
		//String type = XmlUtil.getAttributeAsString(database, "type", "oracle",null);
		String type = XmlUtil.getAttributeAsString(database, "type", null);
		
		/**
		 * 这里改动为若配置文件中没有配置oracle 则去加载mysql，若两者都没有 则抛出异常
		 * modified by liuyang at 2012-04-25
		 */
		if(type != null && type.equalsIgnoreCase("oracle") == false){
			System.out.println("系统未找到oracle数据库，开始找寻mysql数据库……");
			if(type == null || type.equalsIgnoreCase("mysql") == false)
			{
				System.out.println("系统未找到oracle数据库和mysql数据库……开始查找是不是分布式用的场景服");
				if(type.equalsIgnoreCase("server2server") == false){
					throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，配置的数据库类型["+type+"]目前不支持！");
				}
			}
		}
		
		/**
		 * 为了方便添加
		 */
		/*
		<driver></driver>
		 * 			<url></url>
		 * 			<username></username>
		 * 			<password></password>
		 * 			<maxConn></maxConn>
		 * 			<timeoutSeconds></timeoutSeconds>
		 * 			<checkoutSeconds></checkoutSeconds>
		 */
		
		/**
		 * 这里需要改为加载配置文件的数据库驱动  若type为mysql 则加载mysql驱动 和url 若是oracle 则加载oracle 驱动和url 若两者皆不是，则抛出异常
		 * modified by liuyang at 2012-04-25
		 */
		//String driver = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "driver"), "oracle.jdbc.driver.OracleDriver", null);
		String driver = null;
		String url = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "url"), null);
		
		if(type.equalsIgnoreCase("oracle"))
		{
			driver = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "driver"), "oracle.jdbc.driver.OracleDriver", null);
			if(driver == null || !driver.trim().equals("oracle.jdbc.driver.OracleDriver") )
			{
				throw new java.lang.IllegalArgumentException("数据库驱动:" + driver + "与数据库类型" + type + "不匹配！");
			}
			if(url == null || url.matches("^(jdbc:oracle:).+") == false)
			{
				throw new java.lang.IllegalArgumentException("数据库url:" + url + "与数据库类型" + type + "不匹配！");
			}
		}
		else if(type.equalsIgnoreCase("mysql"))
		{
			driver = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "driver"), "com.mysql.jdbc.Driver", null);
			if(driver == null || !driver.trim().equals("com.mysql.jdbc.Driver") )
			{
				throw new java.lang.IllegalArgumentException("数据库驱动:" + driver + "与数据库类型" + type + "不匹配！");
			}
			if(url == null || url.matches("^(jdbc:mysql://).+") == false )
			{
				throw new java.lang.IllegalArgumentException("数据库url:" + url + "与数据库类型" + type + "不匹配！");
			}
		}else if(type.equalsIgnoreCase("server2server"))
		{
			//nothing to do
		}
		else
		{
			throw new java.lang.IllegalArgumentException("出现未知的数据类型" + type + "！");
		}
		
		
		dbType = type;	//为了方便判断数据库类型所加的一个临时性的变量 ，以后可能会改进
		
		if(type.equalsIgnoreCase("oracle") || type.equalsIgnoreCase("mysql")){
			String username = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "username"), null);
			String password = XmlUtil.getValueAsString(XmlUtil.getChildByName(database, "password"), null);
			 
			userName = username;
			
			int maxConn = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(database, "maxConn"),100);
			int timeoutSeconds = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(database, "timeoutSeconds"),300);
			int checkoutSeconds = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(database, "checkoutSeconds"),300);
			
			
			try{
				Driver d= (Driver)Class.forName(driver).newInstance();
				pool = new ConnectionPool(d,name,url,username,password,maxConn,timeoutSeconds,checkoutSeconds);
				pool.setDumpLimit(60000);
				pool.addObserver(new ResettingPoolObserver());
				pool.setCacheStatements(false);
				
				System.out.println("SimpleJPA Database URL:" + url);
			}catch(Exception e){
				e.printStackTrace(System.out);
				throw new java.lang.IllegalArgumentException("SimpleEntityManagerFactory初始化失败，创建数据库连接池出现异常！");
				
			}
		}else if(type.equalsIgnoreCase("server2server")){
			//nothing to do
		}
		
		initialize_success = true;
	}
	
	
	

	
}
