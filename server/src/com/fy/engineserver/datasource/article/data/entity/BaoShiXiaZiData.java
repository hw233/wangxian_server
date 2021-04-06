package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class BaoShiXiaZiData {

	@SimpleId
	private long id;//articleEntityId			
	@SimpleVersion
	private int version;
	
	private int[] colors = new int[1];//格子颜色
	private long[] ids = new long[1];//格子内宝石id
	private String[] names = new String[1];//格子内宝石名字

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int[] getColors() {
		return this.colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
		ArticleEntityManager.baoShiXiLianEM.notifyFieldChange(this, "colors");
	}

	public long[] getIds() {
		return this.ids;
	}

	public void setIds(long[] ids) {
		this.ids = ids;
		ArticleEntityManager.baoShiXiLianEM.notifyFieldChange(this, "ids");
	}

	public String[] getNames() {
		return this.names;
	}

	public void setNames(String[] names) {
		this.names = names;
		ArticleEntityManager.baoShiXiLianEM.notifyFieldChange(this, "names");
	}
	
}
