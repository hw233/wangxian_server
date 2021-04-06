package com.xuanzhi.tools.simplejpa;

import java.util.ArrayList;

import com.xuanzhi.tools.simplejpa.impl.S2SEntityOption;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerFactoryImpl;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerFactoryImpl.EntityManagerInfo;


/**
 * 获取EntityManager的Factory类
 * 在第一次获取某个类的EntityManager时，会检查数据库
 * 创建必要的表，索引和字段。
 * 
 * 所以，最好的方法是在系统启动前，将所有的类都获取一遍EntityManager
 * 
 * 此类需要一个配置文件simpleEMF.xml,可以将此配置文件放入到某个路径下，此路径在classpath内。
 * 或者通过 System.setProperty("simpleEMF.config","/.../.../simpleEMF.xml")设置。
 * 比如在java命令行中  -DsimpleEMF.config=/home/game/simpleEMF.xml
 * 
 * 下面是此xml结构
 * <?xml version='1.0' encoding='utf-8'>
 * <persistence-units>
 * 
 * 		<serverId>100</serverId>
 *		
 * *	<entity-manager super-class='a.A' checkIntervalInSeconds=''>
 *			<class>a.A.C</class>
 *			<class>c.C.D</class>
 *			<class>c.E</class>
 *		</entity-manager>	
 *
 *		<entity-manager super-class='a.DD' checkIntervalInSeconds=''>
 *		</entity-manager>	
 *
 * 		<database name='test-pool' type='oracle'>
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
public abstract class SimpleEntityManagerFactory {
	
	/**
	 * 通过指定的超类获得对应的EntityManager
	 * T 必须是配置文件中，指定某个EntityManager的super-class
	 * 
	 * @param <T>
	 * @param cl
	 * @return
	 */
	public static <T> SimpleEntityManager<T> getSimpleEntityManager(Class<T> cl){
		return SimpleEntityManagerFactoryImpl.getSimpleEntityManager(cl);
	}
	
	public static String getDbType()
	{
		return SimpleEntityManagerFactoryImpl.getDbType();
	}
	
	/////////////目前下面的代码只有卧虎藏龙用，其他项目的代码用的是上面注释掉的
	
	/**
	 * 通过指定的超类获得对应的EntityManager
	 * T 必须是配置文件中，指定某个EntityManager的super-class
	 * sse用于分布式场景服向逻辑服同步数据必须要填写，不能为空
	 * managerKey为处理这个T的管理器，比如defaultPlayerManager，这个也不能为空
	 * 
	 * @param <T>
	 * @param cl
	 * @return
	 */
//	public static <T> SimpleEntityManager<T> getSimpleEntityManager(Class<T> cl,S2SEntityOption sse,String managerKey){
//		return SimpleEntityManagerFactoryImpl.getSimpleEntityManager(cl,sse,managerKey);
//	}
//	
//	public static String getDbType()
//	{
//		return SimpleEntityManagerFactoryImpl.getDbType();
//	}
	
	public static ArrayList<EntityManagerInfo> getEiList(){
		return SimpleEntityManagerFactoryImpl.eiList;
	}
}
