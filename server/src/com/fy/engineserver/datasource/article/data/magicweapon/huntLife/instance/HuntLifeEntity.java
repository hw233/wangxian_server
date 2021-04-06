package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance;

import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
/**
 * 命格面板
 * @author Administrator
 *
 */
@SimpleEntity
public class HuntLifeEntity {
	@SimpleId
	private long id;			//角色id	(命格锁定在人物面板上  只有玩家穿上法宝才能激活命格属性)
	@SimpleVersion
	private int version;
	/**  上一次免费抽取时间(间隔一定时间可获得免费抽取次数)  */
	private long lastTaskFreeTime;
	/** 命格镶嵌记录（同一类型属性只能镶嵌一个） //0气血  1攻击  2外防  3内防  4暴击   5命中   6闪避   7破甲   8精准   9免暴  */
	private long[] huntDatas = new long[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	private transient volatile boolean hasLoadAllAttr = false;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLastTaskFreeTime() {
		return lastTaskFreeTime;
	}
	public void setLastTaskFreeTime(long lastTaskFreeTime) {
		this.lastTaskFreeTime = lastTaskFreeTime;
		HuntLifeEntityManager.em.notifyFieldChange(this, "lastTaskFreeTime");
	}
	public long[] getHuntDatas() {
		return huntDatas;
	}
	public void setHuntDatas(long[] huntDatas) {
		this.huntDatas = huntDatas;
	}
	public boolean isHasLoadAllAttr() {
		return hasLoadAllAttr;
	}
	public void setHasLoadAllAttr(boolean hasLoadAllAttr) {
		this.hasLoadAllAttr = hasLoadAllAttr;
	}
	
	public boolean isOpenHunt(){
		if(huntDatas != null){
			for(long id : huntDatas){
				if(id > 0){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
