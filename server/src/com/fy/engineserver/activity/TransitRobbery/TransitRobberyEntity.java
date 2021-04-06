package com.fy.engineserver.activity.TransitRobbery;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "currentLevel" })})
public class TransitRobberyEntity implements Cacheable, CacheListener{
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 当前所处于渡劫重数----默认为0 */
	private int currentLevel;
	/** 最后一次渡劫开始时间 */
	private long robberyStartTime;
	/** 最后一次成功渡劫结束时间 */
	private long robberyEndTime;
	/** 强制拉人剩余时间---触发时初始话为24小时(单位：秒)---无需拉人时置为-1*/
	private int robberyLeftTime;
	/** 当前劫通过最大层数 */
	private int passLayer;
	/** 飞升 0表示未飞升  1表示已经飞升 */
	private byte feisheng;
	
	protected synchronized void updateCurrentLevel(){
		this.currentLevel += 1;
//		this.passLayer = 0;
		TransitRobberyEntityManager.em.notifyFieldChange(this, "currentLevel");
//		TransitRobberyEntityManager.em.notifyFieldChange(this, "passLayer");
	}
	
	public int getMaxPassLayer() {
		int result = passLayer % 100;
		
		return result;
	}
	
	protected synchronized void updateStartTime(){
		this.robberyStartTime = System.currentTimeMillis();
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyStartTime");
	}
	/**
	 * 一个人一辈子只用调一次
	 */
	protected synchronized void updateFeiSheng(){
		feisheng = 1;
		TransitRobberyEntityManager.em.notifyFieldChange(this, "feisheng");
		try {
			EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.飞升, 1L});
			EventRouter.getInst().addEvent(evt3);
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家飞升异常] [playerId:" + this.id + "]", e);
		}
	}
	
	protected synchronized void updatePassLayer(int layer){
		passLayer = layer;
		TransitRobberyEntityManager.em.notifyFieldChange(this, "passLayer");
	}
	
	protected synchronized void updateEndTime(){
		this.robberyEndTime = System.currentTimeMillis();
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyEndTime");
	}
	
	protected synchronized void initForceLeftTime(){
		robberyLeftTime = RobberyConstant.ONE_DAY_SECOND;
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyLeftTime");
	}
	/**
	 * 
	 * @param value			如果直接清空则置为-1(适用于强制拉入或者玩家自己主动进入)
	 */
	protected synchronized void updateLeftTime(int value){
		if (robberyLeftTime == -1 && value != -100) {			//原本玩家就不需要再被强制拉入天劫不应该修改强制拉人时间
			return ;
		}
		if(value >= 0 && robberyLeftTime <= value){
			return;
		}
		robberyLeftTime = value;
//		TransitRobberyManager.logger.info("[更新渡劫强制拉人时间成功][之前保存时间=" + robberyLeftTime + "  新传入的时间=" + value);
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyLeftTime");
	}
	
	protected synchronized void resetRobbery(){
		currentLevel = 0;
		robberyStartTime = 0;
		robberyEndTime = 0;
		robberyLeftTime = -100;
		robberyLeftTime = 0;
		TransitRobberyEntityManager.em.notifyFieldChange(this, "currentLevel");
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyStartTime");
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyEndTime");
		TransitRobberyEntityManager.em.notifyFieldChange(this, "robberyLeftTime");
		TransitRobberyEntityManager.em.notifyFieldChange(this, "passLayer");
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
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	@Override
	public void remove(int type) {
		// TODO Auto-generated method stub
		if (type == CacheListener.LIFT_TIMEOUT) {
			TransitRobberyEntityManager.getInstance().notifyRemoveFromCache(this);
		}
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	public long getRobberyStartTime() {
		return robberyStartTime;
	}

	public void setRobberyStartTime(long robberyStartTime) {
		this.robberyStartTime = robberyStartTime;
	}

	public long getRobberyEndTime() {
		return robberyEndTime;
	}

	public void setRobberyEndTime(long robberyEndTime) {
		this.robberyEndTime = robberyEndTime;
	}

	public int getRobberyLeftTime() {
		return robberyLeftTime;
	}

	public void setRobberyLeftTime(int robberyLeftTime) {
		this.robberyLeftTime = robberyLeftTime;
	}

	public byte getFeisheng() {
		return feisheng;
	}

	public void setFeisheng(byte feisheng) {
		this.feisheng = feisheng;
	}

	public int getPassLayer() {
		return passLayer;
	}

	public void setPassLayer(int passLayer) {
		this.passLayer = passLayer;
	}

}
