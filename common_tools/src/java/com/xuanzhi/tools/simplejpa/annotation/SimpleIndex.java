package com.xuanzhi.tools.simplejpa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引的标识，此标识声明在Entity类上。
 * 
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleIndex {

	/**
	 * 索引的名字，默认为 TABLE_COLUMN1_COLUMN2_COLUMN3_..._INX
	 * Oracle的索引的名字不能超过32字符，所以必要的情况下，
	 * 需要制定索引的名字。
	 * 
	 * 并且Oracle要求索引的名字唯一。
	 * 
	 * @return
	 * 
	 * Mysql 索引名称最大长度为64  比oracle 限制大 故这里保持和oracle建索引限制一致
	 * comment  added  by liuyang at 2012-04-25
	 */
	  public String name() default "";
	  
	  /**
	   * 索引作用于哪些字段上，这些字段必须是简单的数据类型，包括
	   * int,long,byte,short,boolean,String,float,double,Date
	   * 如果是其他类型，将报错
	   * @return
	   */
	  
	  public java.lang.String[] members() default {};
	  
	  /**
	   * 压缩信息，对于只有少数取值的索引，压缩会提高效率。
	   * 0标识不压缩，k>=1时标识前几个索引采用压缩。
	   * 对于取值都不同的索引，不要采用压缩，否则会降低整体性能。
	   * 
	   * @return
	   * 
	   * 对于mysql来说不支持压缩索引，故这里的属性不生效
	   * 将来会将此标签进行改进 添加一个属性表示是哪种数据库的索引 以便采用不同的索引特性
	   */
	  public int compress() default 0;

}
