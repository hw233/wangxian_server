package com.fy.engineserver.newtask;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "playerId" }), @SimpleIndex(members = { "taskId" })})
public class NewDeliverTask implements Cacheable,CacheListener{
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 角色id */
	private long playerId;
	/** 任务ID--如果是序列任务只更新taskid */
	private long taskId;			
	/** 是否已经交付 */
	private transient boolean isDeliver;
//	/** 任务type(主线，支线) */
//	private byte taskType;
	
	public NewDeliverTask(){}
	
	/**
	 * 
	 * @param playerId
	 * @param taskId
	 * @throws Exception
	 */
	public NewDeliverTask(long playerId, long taskId) throws Exception{
		this.id = NewDeliverTaskManager.getInstance().em.nextId();
		this.setPlayerId(playerId);
		this.taskId = taskId; 
		this.isDeliver = true;
//		this.taskType = taskType;
	}
	
	public NewDeliverTask(long playerId, long taskId, long id, int version) throws Exception{
		this.id = id;
		this.version = version;
		this.setPlayerId(playerId);
		this.taskId = taskId; 
//		this.taskType = taskType;
	}
	
	@Override
	public String toString() {
		return "NewDeliverTask [id=" + id + ", version=" + version + ", playerId=" + getPlayerId() + ", taskId=" + taskId + ", isDeliver=" + isDeliver + "]";
	}
	
	/**
	 * 完成有前置任务的时候时更新
	 * @param taskId
	 */
	protected void updateTaskId(long taskId) {
		this.taskId = taskId;
		NewDeliverTaskManager.getInstance().em.notifyFieldChange(this, "taskId");
	}
	
	@Override
	public void remove(int arg0) {
		// TODO Auto-generated method stub
		NewDeliverTaskManager dm = NewDeliverTaskManager.getInstance();
		if(dm != null){
			dm.save(this);
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
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

	public long getPlayerId() {
		return playerId;
	}

	public long getTaskId() {
		return taskId;
	}

	public boolean isDeliver() {
		return isDeliver;
	}

	public void setDeliver(boolean isDeliver) {
		this.isDeliver = isDeliver;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

//	public byte getTaskType() {
//		return taskType;
//	}

}
