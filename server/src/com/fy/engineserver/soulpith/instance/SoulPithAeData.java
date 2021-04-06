package com.fy.engineserver.soulpith.instance;

import com.fy.engineserver.soulpith.SoulPithConstant;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
/**
 * 灵髓道具属性
 * 
 * @date on create 2016年3月16日 下午3:18:00
 */
@SimpleEntity
public class SoulPithAeData {
	@SimpleId
	private long id;			//物品id
	@SimpleVersion
	private int version;
	/** 灵髓等级 */
	private int pithLevel;
	/** 当前经验 */
	private long exps;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPithLevel() {
		if (pithLevel >= SoulPithConstant.SOUL_MAX_LEVEL) {
			return  SoulPithConstant.SOUL_MAX_LEVEL;
		}
		return pithLevel;
	}
	public void setPithLevel(int pithLevel) {
		this.pithLevel = pithLevel;
		SoulPithEntityManager.em_ae.notifyFieldChange(this, "pithLevel");
	}
	public long getExps() {
		return exps;
	}
	public void setExps(long exps) {
		this.exps = exps;
		SoulPithEntityManager.em_ae.notifyFieldChange(this, "exps");
	}
}
