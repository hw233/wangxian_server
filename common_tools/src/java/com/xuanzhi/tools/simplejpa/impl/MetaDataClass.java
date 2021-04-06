package com.xuanzhi.tools.simplejpa.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 标识一个继承关系中的类
 * 
 *     
 *
 */
public class MetaDataClass {

	Class<?> cl;
	
	MetaDataClass parent;
	
	ArrayList<MetaDataClass> children = new ArrayList<MetaDataClass>();
	
	//添加一个对应于className的标识
	Integer cid;
	
	//各种情况下的回调方法
	Method postLoad;
	Method postPersist;
	Method postUpdate;
	Method prePersist;
	Method preUpdate;
	Method preRemove;
	Method postRemove;
	
	
	public boolean isLeaf(){
		return children.size() == 0;
	}
	
	public boolean isRoot(){
		return parent == null;
	}
	
	/*
	 * 保存此类自己特有的属性，父亲类中的属性不在此map中
	 */
	//LinkedHashMap<String,MetaDataField> map = new LinkedHashMap<String,MetaDataField>();

	/*
	 * 保存此类所有的属性，包括父亲类的属性
	 */
	//LinkedHashMap<String,MetaDataField> allFieldMap = new LinkedHashMap<String,MetaDataField>();

	
}
