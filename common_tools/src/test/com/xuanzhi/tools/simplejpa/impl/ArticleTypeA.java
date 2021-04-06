package com.xuanzhi.tools.simplejpa.impl;

import java.util.HashMap;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
@SimpleEntity
public class ArticleTypeA extends TTAbstractArticleEntity{

	@SimpleColumn(name="nameA")
	public String name;
	
	public int xxxx;
	
	@SimpleColumn(name="mapA")
	private HashMap<Long,Long> map = new HashMap<Long,Long>();
	
	public int count;
}
