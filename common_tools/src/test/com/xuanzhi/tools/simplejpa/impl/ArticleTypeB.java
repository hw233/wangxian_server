package com.xuanzhi.tools.simplejpa.impl;

import java.util.HashMap;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
@SimpleEntity
public class ArticleTypeB extends TTAbstractArticleEntity{

	@SimpleColumn(name="nameB")
	public String name;
	
	@SimpleColumn(name="xxxxB")
	public int xxxx;
	
	@SimpleColumn(name="mapB")
	private HashMap<Long,Long> map = new HashMap<Long,Long>();
	
	public int yyyy;
	
	public String xxxxx;
}
