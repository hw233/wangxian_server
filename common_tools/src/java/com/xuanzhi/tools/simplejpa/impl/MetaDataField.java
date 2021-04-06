package com.xuanzhi.tools.simplejpa.impl;

import java.lang.reflect.Field;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;

/**
 * 标识一个属性
 * 
 *
 */
public class MetaDataField {

	//属性
	public Field field;
	
	//字段所在的类
	public MetaDataClass mdc;
	
	//此Field声明的注释
	public SimpleColumn simpleColumn;
	
	//此字段是否保存在主表中，一个entity对应两个表
	//主表用于保存简单类型的字段，可用于建索引，作为查询的条件
	//副表用于保存json对象的字段，不可见索引，也不可用作查询条件
	public boolean inPrimaryTable;
	
	//对应数据库中表的字段，此字段是有顺序的
	//对于超过4000个字符的json串，
	//前4000保存在columnNames[0]中
	//第二个4000保存在columnNames[1]中
	//以此类推
	//此变量值用于oracle版本
	public String columnNames[];
	
	
	//对应数据库中表的字段，此字段是有顺序的
	//对于超过4000个字符的json串，
	//前4000保存在columnNames[0]中
	//第二个4000保存在columnNames[1]中
	//以此类推
	//此变量值用于mysql版本
	//mysql有单行8k的限制，所以一字段可能要拆分成多个表中的多个字段存储
	public String tableNameForMysql;
	public String columnNameForMysql;


}
