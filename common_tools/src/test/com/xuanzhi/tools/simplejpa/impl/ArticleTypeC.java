package com.xuanzhi.tools.simplejpa.impl;

import java.util.HashMap;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
@SimpleEntity
public class ArticleTypeC extends TTAbstractArticleEntity{

	@SimpleColumn(name="nameC")
	public String name;
	
	@SimpleColumn(name="xxxxC")
	public int xxxx;
	
	@SimpleColumn(name="mapC")
	private HashMap<Long,Long> map = new HashMap<Long,Long>();
}
