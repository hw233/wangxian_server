package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance;

import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 命格道具的经验记录
 * @author Administrator
 *
 */
@SimpleEntity
public class HuntArticleExtraData {
	@SimpleId
	private long id;			//物品id
	@SimpleVersion
	private int version;
	/** 剩余经验(升级此经验需要清0) */
	private long exps;
	/** 等级 */
	private int articleLevel;
	
	public String getlogString() {
		return "level:" + articleLevel + "，exps:" + exps;
	}
	
	public long getExps() {
		return exps;
	}
	public void setExps(long exps) {
		this.exps = exps;
		HuntLifeEntityManager.em_ae.notifyFieldChange(this, "exps");
	}
	public int getLevel() {
		if (articleLevel <= 0) {
			articleLevel = 1;
		}
		return articleLevel;
	}
	public void setLevel(int level) {
		this.articleLevel = level;
		HuntLifeEntityManager.em_ae.notifyFieldChange(this, "articleLevel");
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
