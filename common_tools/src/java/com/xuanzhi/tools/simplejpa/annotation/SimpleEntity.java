package com.xuanzhi.tools.simplejpa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 实体类标识。
 * 
 * 此实体类是简单的JPA实现，不支持大部分JPA的功能。
 * 
 * 不支持如下功能：
 * 1. 不支持Entity之间的关系，一个Entity中不能包含另外一个Entity的引用，包括数组，集合引用。
 *    应用程序可以通过id来引用对应的Entity
 *    
 * 2. 不支持Enhence功能，也不支持类似的merge，detach类似的功能。
 *    此实现只是简单的将对象模型，对应到关系数据库中的模型，
 *    中间不做任何的多余的工作，包括跟踪变化，回滚，cache等。
 * 
 * 3. 不支持对Embedable作为条件的查询，类似这样的查询不存在.
 *    select a from Player a where a.b.c = 'xxxx'
 *    
 * 4. 不支持JPQL语言查询   
 *    
 * 支持如下功能：
 * 1. 基本的数据类型包括int,byte,short,long,char,boolean,String,float,double,java.util.Date,
 *    以及他们的包裹类Integer,Byte,Short,...
 *    这些类型都映射到数据库中的一个字段
 * 
 * 2. 基本数据类型的数组，包括int[],byte[],short[],....，包括多维数组
 *    都会转化json字符串存储在数据库中
 *    
 * 
 * 3. 支持在Entity中包含Embeddable，以及Embeddable的数组，集合，Map。
 *    所有这些都会转化为json字符串，存储在数据库中
 *    所以这种实现，是不支持利用Embeddable中的属性作为查询条件的。
 *            
 * 4. 所有json串都是以varchar2的形式储存，如果json的大小超过4000，那么会自动拆分到
 *    2个字段，甚至更多的字段存储。不采用lob字段。
 *    此做法的假设是，不存在某个字段需要存储大量数据。
 *    最多拆分10次，超过10次的json，将储存失败。
    
 * 5. 所有Entity，都对应两张表，一张表用于存储简单的数据类型，可以作为查询条件。
 *    另外一张表存储json串，数据量大，不支持查询条件。
 *    这么设计是为了提高查询效率，减少对数据库cache的占用。      
 * 
 * 限制：
 * 1. 所有Entity的Id都为long，19为，格式为1 + 三位服务器id + 15位数
 *    Id的取值从1 ～ 999999999999999
 * 2. 所有的Entity必须包含Version字段，类型为int。新对象值为0，设置为负数标识要被删除。
 *    version每次保存，都会自动+1
 *    version值不能有应用层来设置，尤其是对新的对象，如果设置version的值，
 *    会导致新的对象无法保存。
 *    
 * 3. 所有的Entity都必须有公共的无参数的构造函数。
 * 4. 对于大数据量的字段，必须指定最大的字符长度。
 *       
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleEntity {

	/**
	 * 此Entity的名字，对应到数据库中表的名字
	 * 如果没有指定，将采用此类的具体的名字，不含包的名称。
	 * 比如com.mieshi.gameserver.sprite.Player对应的名字为Player
	 * 
	 * 如果整体设计上，存在表前缀，那么正在的表名，应该是前缀+Player
	 * @return
	 */
	String name() default "";
}
