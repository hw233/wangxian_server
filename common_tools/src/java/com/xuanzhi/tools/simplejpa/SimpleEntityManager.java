package com.xuanzhi.tools.simplejpa;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 管理某个Entity的Manager，类似我们以前的DAO类。
 * 
 * 此类支持自动保存机制，但对象发生修改后，会自动保存修改。
 * 目前的版本，需要应用程序通知EntityManager，哪个属性发生的变化。
 * 请参见此方法：notifyFieldChange();
 * 
 * 另外，此类还支持产生Id，会保证id不会重复。
 * 请参见此方法：nextId();
 * 
 * 第一次产生此SimpleEntityManager时，
 * 会检查对应的T，在数据库中对应的表，是否已经创建，
 * 如果没有创建，则会创建对应的表。
 * 如果某个字段没有创建，则会创建对应的字段。
 * 如果某个索引没有创建，则会创建对应的索引。
 * 
 * 在此过程中，如果发生异常，将会向调用者抛出异常。
 * 所有创建表，字段，索引的日志，都会打印到stdout中。
 * 
 * 关于Entity的描述：
 * 
 * 此实体类是简单的JPA实现，不支持大部分JPA的功能。
 * 
 * 不支持如下功能：
 * 1. 不支持Entity之间的关系，一个Entity中不能包含另外一个Entity的引用，包括数组，集合引用。
 *    应用程序可以通过id来引用对应的Entity
 *    
 * 2. 不支持Enhance功能，也不支持类似的merge，detach类似的功能。
 *    此实现只是简单的将对象模型，对应到关系数据库中的模型，
 *    中间不做任何的多余的工作，包括跟踪变化，回滚，cache等都不支持。
 * 
 * 3. 不支持对Embedable作为条件的查询，类似这样的查询不存在.
 *    select a from Player a where a.b.c = 'xxxx'
 *    
 * 4. 不支持JPQL语言查询   
 *    
 * 支持如下功能：
 * 1. 基本的数据类型包括int,byte,short,long,char,boolean,String,float,double,java.util.Date,
 *    以及他们的包裹类Integer,Byte,Short,...
 *    这些类型都映射到数据库中的一个字段，这些字段可以作为查询条件，可以作为索引字段
 *    String的默认最大长度为256，最大可以设置的长度为4000
 *    如果某个String要存储超过4000，请用String[]。
 *    
 *    注意，oracle中对索引字段有严格要求，多个字段联合索引时，所有字段的最大长度之和不能超过6000.
 *    所以对于作为索引的字段，最好能设置较小的最大长度，以提高性能。
 * 
 * 2. 基本数据类型的数组，包括int[],byte[],short[],....，包括多维数组
 * 	  包括这些类型的集合类，
 *    都会转化json字符串存储在数据库中，这类数据都不可以作为查询条件。
 *    
 * 
 * 3. 支持在Entity中包含Embeddable，以及Embeddable的数组，集合，Map。
 *    所有这些都会转化为json字符串，存储在数据库中
 *    所以这种实现，是不支持利用Embeddable中的属性作为查询条件的。
 *            
 * 4. 所有json串都是以varchar2的形式储存，如果json的大小超过4000，那么会自动拆分到
 *    2个字段，甚至更多的字段存储。不采用lob字段。
 *    此做法的假设是，不存在某个字段需要存储大量数据。
 *    最多拆分16次，超过16次的json，将储存失败。
    
 * 5. 所有Entity，都对应两张表，一张表用于存储简单的数据类型，可以作为查询条件。
 *    另外一张表存储json串，数据量大，不支持查询条件。
 *    这么设计是为了提高查询效率，减少对数据库cache的占用。      
 * 
 * 6. 支持有继承关系的多个类，放在一个EntityManager中管理，这些类用一个主表和副表存储他们的数据。
 *    也支持有继承关系的类，采用多个EntityManager管理，多个EntityManager管理，
 *    需要特别注意Id的问题。
 *    
 *    建议，有继承关系的多个类，都放置在一个EntityManager中管理。
 *    具体可以参考Factory的配置文件。
 * 
 * 7. 关于多态的json串的问题，请使用json的标准注释：
 *    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
 *    在接口类或者抽象类上声明，子类不用重复声明。
 * 
 * 必要的限制条件：
 * 1. 所有Entity的Id都为long，19为，格式为1 + 三位服务器id + 15位数
 *    Id的取值从1 ～ 999999999999999。
 *    所以Id的值，必须来自nextId()方法。
 *    
 * 2. 所有的Entity必须包含Version字段，类型为int。新对象值为0，设置为负数标识要被删除。
 *    version每次保存，都会自动+1。
 *    
 *    version值不能由应用层来设置，尤其是对新的对象，如果设置version的值，
 *    会导致新的对象无法保存。
 *    
 * 3. 所有的Entity都必须有公共的无参数的构造函数。Embeddable也必须有公共的无参数的构造函数。
 * 
 * 4. 对于大数据量的字段，必须指定最大的字符长度。String默认为255，JSON默认为4000，
 *    对于作为索引的字符，请设置较小的值。
 * 
 * 5. 但某个Entity的某个属性发生变化时，需要显示的调用notifyFieldChange()方法。
 *    此方法只是做了一个标记。有底层的线程定期的存储。
 *    
 * 6. 对于新的对象，需要调用notifyNewObject()方法或者save()方法，才能保存数据。
 *    notifyNewObject()只是做了一个标记。有底层的线程定期的存储。
 *    而save()方法，会立即保存。
 *    
 * 并发性：
 * 		此接口中所有的方法都是线程安全的，
 * 		都是对象级别进行加锁，也就是说，
 * 		同一个对象，不会重复加载，也不会重复保存。
 * 		EntityManager永远不会允许两个线程同时加载同一个对象，同时保存同一个对象。
 * 
 * 		EntityManager永远不会返回一个对象的两个实例给应用层。
 *    
 *
 * @param <T>
 */
public interface SimpleEntityManager<T> {

	/**
	 * 获取一个新的Id，返回的Id会保证从未被此Entity使用过
	 * 
	 * 此方法会从数据库中的SEQUENCE中获取。
	 * 会采用cache机制，预先加载一批存放于内存中。
	 * 所以此方法效率非常高。
	 * 
	 * 格式规则：　1 + 三位serverid + 15位该对象id
	 * 所以对象的id最大值为 999999999999999L。
	 * 如果操作此值，此方法将抛出异常。
	 * 
	 * @return
	 */
	public long nextId() throws Exception;
	
	/**
	 * 判断自动保存机制是否生效，默认是生效的
	 * 
	 * 如果生效，SimpleEntityManager内部将有一个线程，定期扫描所有的Entity，
	 * 看看是否要保存某些Entity。
	 * 
	 * @return
	 */
	public boolean isAutoSaveEnabled();
	
	/**
	 * 启动或者关闭自动保存机制。
	 * 如果启动，需要设置保存线程检测的时间间隔。单位为秒。
	 * 
	 * 默认为30秒钟检查一次。
	 * 
	 * @param enable
	 * @param checkIntervalInSeconds
	 */
	public void setAutoSave(boolean enable, int checkIntervalInSeconds);
	
	/**
	 * 应用层通知EntityManager，某个属性发生了变化
	 * 
	 * 如果是某个Embeddable对象发生了变化，需要通知引用此对象的Field发生了变化，
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
	 * 此方法一般情况下立即返回，
	 * 但是如果指定的数据含有urgent为true，
	 * 那么此方法会立即将数据插入数据库中，然后才返回。
	 * 
	 * @param t
	 * @param field
	 */
	public void notifyFieldChange(T t,String field);
	
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
	public void notifyNewObject(T t);
	
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
	public T find(long id) throws Exception;
	
	/**
	 * 判断某个id对应的对象是否存在于内存中。
	 * 即已经加载并且仍然在内存中。
	 * 
	 * @param id
	 * @return
	 */
	public boolean contains(long id);
	
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
	 * @param t
	 * @throws SQLException
	 */
	public void save(T t) throws Exception;
	
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
	public void batchSave(T[] ts) throws Exception;
	
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
	public void flush(T t) throws Exception;
	
	/**
	 * 立即删除某一个对象，此方法会操作数据库
	 * 并且会从内存中清楚对应的数据。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void remove(T t) throws Exception;
	
	/**
	 * 批量删除对象，此方法会操作数据库
	 * 并且会从内存中清楚对应的数据。
	 * 
	 * @param t
	 * @throws SQLException
	 */
	public void remove(T[] ts) throws Exception;
	
	
	/**
	 * 为了合服专门写的批量删除
	 * 此方法只是单纯为合服使用没有清理过任何simplejpa相关的底层缓存
	 * 适合从不加载对象 直接想通过id删除数据使用，如果通过id加载过对象，那么对于应用层造成的结果没有测试，不可预知，需慎重使用
	 */
	//public int batch_delete_by_Ids(Class<?> cl,long[]ids) throws Exception;
	
	
	/**
	 * 为了合服专门写的批量删除
	 * 此方法只是单纯为合服使用没有清理过任何simplejpa相关的底层缓存
	 * 适合从不加载对象 直接想通过id删除数据使用，如果通过id加载过对象，那么对于应用层造成的结果没有测试，不可预知，需慎重使用
	 */
	public int batch_delete_by_Ids(Class<?> cl,List<Long>ids) throws Exception;
	
	/**
	 * 销毁EntityManager，
	 * 此方法会将没有保存的数据全部保存到数据库中
	 * 并释放所有的数据库连接
	 * 
	 * 此方法将等待保存线程结束，才返回
	 * 并且每1秒钟会在stdout上打印一个点
	 */
	public void destroy();
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//查询接口
	
	/**
	 * 查询有多少个对象，此方法只表示有多少条记录。
	 */
	public long count() throws Exception;

	
	
	/**
	 * 查询某个条件下有多少个对象，条件是sql语句中where部分，不包括where
	 * 只支持对本表的查询，不支持多表联合查询。
	 * 
	 * 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。
	 * 
	 * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 * 
	 * @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。
	 * @param whereSql
	 * 
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 * 
	 */
	
	public long count(Class<?> cl,String whereSql) throws Exception;
	
	
	/**
	 * 查询某个条件下有多少个对象，条件是sql语句中where部分，不包括where
	 * 只支持对本表的查询，不支持多表联合查询。
	 * 
	 * 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。
	 * 
	 * whereSql例子：
	 *   level > ? and hp = ? or dirty = ?
	 *   lastRequestTime > to_date(?,'yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date(?,'yyyy-mm-dd hh24:mi:ss')
	 * paramValues例子：
	 *   Object[]{2,3,"T","2011-02-13","2011-02-14"}
	 * 
	 * 类型对应表：
	 *   Byte,Short,Integer <----> setInt
	 *   Long    <----> setLong
	 *   Float   <----> setFloat
	 *   Double  <----> setDouble
	 *   String  <----> String
	 *   Date    <----> setTimeStamp
	 * 
	 * 不支持 Boolean，Charater，以及其他任何类型
	 * 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 * 
	 * @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。
	 * @param whereSql
	 * 
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 * 
	 */
	public long count(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception;
	
	/**
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * 
	 *  * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 */
	public long[] queryIds(Class<?> cl,String whereSql) throws Exception;
	
	
	/**
	 * 查询满足某个条件的所有的Ids，
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * 
	 * whereSql例子：
	 *   level > ? and hp = ? or dirty = ?
	 *   lastRequestTime > to_date(?,'yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date(?,'yyyy-mm-dd hh24:mi:ss')
	 * paramValues例子：
	 *   Object[]{2,3,"T","2011-02-13","2011-02-14"}
	 * 
	 * 类型对应表：
	 *   Byte,Short,Integer <----> setInt
	 *   Long    <----> setLong
	 *   Float   <----> setFloat
	 *   Double  <----> setDouble
	 *   String  <----> String
	 *   Date    <----> setTimeStamp
	 * 
	 * 不支持 Boolean，Charater，以及其他任何类型
	 * 
	 * * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 */
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues) throws Exception;
	
	/**
	 * 分页查询，查询满足某个条件的所有的Ids
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 *  * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 */
	public long[] queryIds(Class<?> cl,String whereSql,String orderSql,long start,long end) throws Exception;
	
	
	/**
	 * 分页查询，查询满足某个条件的所有的Ids
	 * 如果whereSql为null，表示要查询所有的Ids，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 *  * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 * 
	 * 在mysql中以上例子中的to_date为oracle 特定函数  故 在mysql中 Date类型 对应的字段是DateTime类型
	 * 要转型 请使用STR_TO_DATE('','%Y-%m-%d %H:%i:%S') 具体见
	 * http://dev.mysql.com/doc/refman/5.1/en/date-and-time-functions.html#function_str-to-date
	 * 
	 * comment added by liuyang at 2012-04-26
	 */
	public long[] queryIds(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql,long start,long end) throws Exception;
	
	/**
	 * 分页查询，查询满足某个条件的所有的Entity
	 * 如果whereSql为null，表示要查询所有的Entity，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * 如果end - start > 10000，将抛出IllegalArgumentException
	 * 
	 *  * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 */
	public List<T> query(Class<?> cl,String whereSql,String orderSql,long start,long end) throws Exception;
	
	
	/**
	 * 分页查询，查询满足某个条件的所有的Entity
	 * 如果whereSql为null，表示要查询所有的Entity，此方法慎用。
	 * start开始记录，从1开始。
	 * end结束记录，从1开始，但是返回的记录中不包含此记录
	 * 即返回的记录是 [start,end)
	 * 
	 * 如果end - start > 10000，将抛出IllegalArgumentException
	 * 
	 *  * whereSql例子：
	 *   level > 5 and hp = 3 or dirty = 'T'
	 *   lastRequestTime > to_date('2012-04-01 00:13:12','yyyy-mm-dd hh24:mi:ss') and lastRequestTime < to_date('2012-04-02 00:13:12','yyyy-mm-dd hh24:mi:ss') 
	 * 需要注意的是：
	 *   boolean 类型 对应的字段为CHAR(1)，'T'标识true，其他标识false
	 *   Date类型，对应的字段为DATE类型，需要用to_date('','yyyy-mm-dd hh24:mi:ss')来转型
	 *   所有的字段，都必须是类的Field名字，大小写敏感。
	 *
	 * orderbySql例子：
	 *   level,name desc
	 *   level,name
	 *   leve asc,map2 desc,name
	 *   所有的字段，都必须是类的Field名字，大小写敏感。   
	 *  @param cl 所有where语句和orderby语句中出现的属性，都是指定类cl的属性。  
	 * @param whereSql
	 * @return
	 */
	public List<T> query(Class<?> cl,String preparedWhereSql,Object[] paramValues,String orderSql,long start,long end) throws Exception;
	
	/**
	 * 查询一些字段，而非所有的字段。
	 * 这些字段都是SuperClass的字段，不是SuperClass的字段此接口不能查询。
	 * 并且这些字段，只能是简单的字段，数组，集合，Embeddable都不能查询。
	 * 字段类型包括：
	 * 		boolean,byte,char,short,int,long,String,Date,float,double
	 *      以及他们的包裹类。
	 * 举例：
	 * 原本的类Player，很复杂，很大
	 * public class Player{
	 * 		private long id;
	 * 		private String name;
	 * 		private int level;
	 * 		private int money;
	 * 		......
	 * 		......
	 * }
	 * 如果我们只需要加载 name,level,money
	 * 我们可以定义一个接口:
	 * public interface PlayerInfo{
	 * 		public long getId();
	 * 		public int getLevel();
	 * 		public int getMoney();
	 * 		public String getName();
	 * }
	 * 
	 * 那么此方法就可以返回此接口的实现
	 * 此实现保证如下机制：
	 * 
	 * 此实现保证，接口返回的数据与真正的对象上的数据是同步的。
	 * 如下操作保证同步：
	 * 0. 如果对应的接口和id，已经有代理实例在内存中，返回此代理实例。
	 * 1. 如果实体对象在内存中，拷贝实体对象数据
	 * 2. 如果实体对象不在内存中，查询数据库获得数据
	 * 3. 如果实体对象某属性发生变化，修改对应的代理实例中拷贝的数据。
	 * 
	 * 此接口中的方法执行如下逻辑：
	 * 1. 如果实体对象不在内存中，采用拷贝的数据
	 * 2. 如果实体对象在内存中，就采用实体对象上的数据
	 * 
	 * 注意此接口返回的对象数目，与参数ids的数目未必一致，顺序也未必一致。
	 * 
	 * @param <S> 定义要查询字段的简单的接口，此接口所含有的方法必须和SuperClass中的字段对应起来，必须同名同类型
	 * @param cl
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public <S> List<S> queryFields(Class<S> cl,long ids[]) throws Exception;
	
	
	/**
	 * 设置只读模式，不进行更新和插入
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly);
	
	/**
	 * 统计尚未插入到数据库中的新对象数量 新对象的标准是newObject为true 并且savingLock是false
	 */
	public long countUnSavedNewObjects();
	
	public void releaseReference();
	/**
	 * 删除索引
	 * @param cl
	 */
	public void dropEntityIndex(Class cl);
	
	public void setBatchSaveOrUpdateSize(int batchSize);
	
	public int getBatchSaveOrUpdateSize();
	
	/**
	 * 批量保存或者更新数据，新的对象则插入到数据库中，已有的数据，将变化保存到数据库中
	 * 
	 * @param t
	 * @throws Exception
	 */
	public void batch_insert_or_update_object(LinkedHashMap<Long,Object[]> map) throws Exception;
	
	
}
