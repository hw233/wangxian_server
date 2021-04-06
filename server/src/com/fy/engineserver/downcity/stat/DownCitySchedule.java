package com.fy.engineserver.downcity.stat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
/**
 * 副本消耗信息
 * 
 *
 */
public class DownCitySchedule implements Serializable,Cacheable, CacheListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 397467624872689053L;

	protected String downCityId;
	
	/**
	 * 副本的名字，必须唯一
	 */
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 副本开启时间
	 */
	protected long startTime;
	
	/**
	 * 副本通关时间
	 */
	protected long tongguanTime;
	
	/**
	 * 副本人员信息
	 */
	HashMap<Long,DownCityPlayerInfo> downCityPlayerInfos = new HashMap<Long,DownCityPlayerInfo>();
	
	/**
	 * 副本消耗信息
	 */
	ArrayList<DownCityConsumeInfo> downCityConsumeInfos = new ArrayList<DownCityConsumeInfo>();
	
	/**
	 * 副本产出信息
	 */
	ArrayList<DownCityOutputInfo> downCityOutputInfos = new ArrayList<DownCityOutputInfo>();
	
	private boolean dirty;
	
	private long lastUpdateTime = 0;

	public String getDownCityId() {
		return downCityId;
	}

	public void setDownCityId(String downCityId) {
		this.downCityId = downCityId;
		setDirty(true);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
		setDirty(true);
	}

	public long getTongguanTime() {
		return tongguanTime;
	}

	public void setTongguanTime(long tongguanTime) {
		this.tongguanTime = tongguanTime;
		setDirty(true);
	}

	public HashMap<Long, DownCityPlayerInfo> getDownCityPlayerInfos() {
		return downCityPlayerInfos;
	}

	public void setDownCityPlayerInfos(
			HashMap<Long, DownCityPlayerInfo> downCityPlayerInfos) {
		this.downCityPlayerInfos = downCityPlayerInfos;
		setDirty(true);
	}

	public ArrayList<DownCityConsumeInfo> getDownCityConsumeInfos() {
		return downCityConsumeInfos;
	}

	public void setDownCityConsumeInfos(
			ArrayList<DownCityConsumeInfo> downCityConsumeInfos) {
		this.downCityConsumeInfos = downCityConsumeInfos;
		setDirty(true);
	}

	public ArrayList<DownCityOutputInfo> getDownCityOutputInfos() {
		return downCityOutputInfos;
	}

	public void setDownCityOutputInfos(
			ArrayList<DownCityOutputInfo> downCityOutputInfos) {
		this.downCityOutputInfos = downCityOutputInfos;
		setDirty(true);
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	
	public void remove(int type) {
		DownCityScheduleManager mm = DownCityScheduleManager.getInstance();
		if(type != CacheListener.MANUAL_REMOVE || type == CacheListener.LIFT_TIMEOUT || type == CacheListener.SIZE_OVERFLOW){
			mm.serializeDownCitySchedule(this);
		}
	}

}
