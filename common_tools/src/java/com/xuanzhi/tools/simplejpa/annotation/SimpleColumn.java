package com.xuanzhi.tools.simplejpa.annotation;


import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Is used to specify the mapped column for a persistent property or field.
 * If no <code>Column</code> annotation is specified, the default values apply.
 *
 * <blockquote><pre>
 *    Example 1:
 *
 *    &#064;Column(name="DESC", nullable=false, length=512)
 *    public String getDescription() { return description; }
 *
 *    Example 2:
 *
 *    &#064;Column(name="DESC",
 *            columnDefinition="CLOB NOT NULL",
 *            table="EMP_DETAIL")
 *    &#064;Lob
 *    public String getDescription() { return description; }
 *
 *    Example 3:
 *
 *    &#064;Column(name="ORDER_COST", updatable=false, precision=12, scale=2)
 *    public BigDecimal getCost() { return cost; }
 *
 * </pre></blockquote>
 *
 *
 * @since Java Persistence 1.0
 */ 
@Target({FIELD}) 
@Retention(RUNTIME)
public @interface SimpleColumn {

    /**
     * 存盘间隔，对于修改特别频繁的字段，需要设置此值
     * 单位为秒,
     */
    int saveInterval() default 0;
    
    /**
     * (Optional) The name of the column. Defaults to 
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) Whether the database column is nullable.
     */
    boolean nullable() default true;

    /**
     * (Optional) The SQL fragment that is used when 
     * generating the DDL for the column.
     * <p> Defaults to the generated SQL to create a
     * column of the inferred type.
     */
    String columnDefinition() default "";

    /**
     * (Optional) The column length. (Applies only if a
     * string-valued column is used.) 默认为256
     */
    int length() default 256;

    /**
     * (Optional) The precision for a decimal (exact numeric) 
     * column. (Applies only if a decimal column is used.)
     * Value must be set by developer if used when generating 
     * the DDL for the column.
     */
    int precision() default 0;

    /**
     * (Optional) The scale for a decimal (exact numeric) column. 
     * (Applies only if a decimal column is used.)
     */
    int scale() default 0;
    
    /**
     * 转为mysql 所制作 mysql列的名字
     */
    String mysqlName() default ""; 
    
    
    /**
     * 标准是否为紧急数据，此数据一旦发生修改，
     * 要立即保存。
     * 具体实现是，通知notifyFieldChange()方法
     * 如果通知的属性是紧急数据，会立即存盘。
     * @return
     */
    boolean urgent() default false;

}
