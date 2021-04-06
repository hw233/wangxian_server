package com.fy.engineserver.newtask;

import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.newtask.service.TaskManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 完成的任务(仅对一次性任务)
 * 
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(name = "DeliverTask_PlayerTask", members = { "playerId", "taskId" })

})
public class DeliverTask implements Cacheable,CacheListener{
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 角色ID */
	private long playerId;
	/** 任务名字 */
	private String taskName;
	/** 任务ID */
	private long taskId;
	/** 任务最后接取时间 */
	private long lastGetTime;
	/** 任务最后完成时间 */
	private long lastDeliverTime;
	/** 任务第一次完成时间 */
	private long firstGetTime;
	/** 任务第一次接取时间 */
	private long firstDeliverTime;
	/** 完成次数 */
	private int deliverTimes;

	private transient boolean isDeliver;

	public DeliverTask() {
	}

	public DeliverTask(TaskEntity entity) throws Exception {
		this.id = TaskManager.deliverTaskEm.nextId();
		this.playerId = entity.getPlayerId();
		this.taskName = entity.getTask().getName();
		this.taskId = entity.getTaskId();
		this.lastGetTime = entity.getLastGetTime();
		this.lastDeliverTime = entity.getLastDeliverTime();
		this.firstGetTime = entity.getFirstGetTime();
		this.firstDeliverTime = entity.getFirstDeliverTime();
		this.deliverTimes = entity.getTotalDeliverTimes();
	}
	@SimplePostPersist
	public void saved() {
		DeliverTask dt = DeliverTaskManager.getInstance().tempMap.remove(this.getId());
		if(DeliverTaskManager.logger.isInfoEnabled()) {
			DeliverTaskManager.logger.info("[移除delivertask] [" + dt + "] [" + this + "]");
		}
	}

	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getLastGetTime() {
		return lastGetTime;
	}

	public void setLastGetTime(long lastGetTime) {
		this.lastGetTime = lastGetTime;
	}

	public long getLastDeliverTime() {
		return lastDeliverTime;
	}

	public void setLastDeliverTime(long lastDeliverTime) {
		this.lastDeliverTime = lastDeliverTime;
	}

	public int getDeliverTimes() {
		return deliverTimes;
	}

	public void setDeliverTimes(int deliverTimes) {
		this.deliverTimes = deliverTimes;
	}

	public long getFirstGetTime() {
		return firstGetTime;
	}

	public void setFirstGetTime(long firstGetTime) {
		this.firstGetTime = firstGetTime;
	}

	public long getFirstDeliverTime() {
		return firstDeliverTime;
	}

	public void setFirstDeliverTime(long firstDeliverTime) {
		this.firstDeliverTime = firstDeliverTime;
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

	public boolean isDeliver() {
		return isDeliver;
	}

	public void setDeliver(boolean isDeliver) {
		this.isDeliver = isDeliver;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass()).append("[玩家ID:").append(playerId).append("][任务名字:").append(taskName).append("][任务ID:").append(taskId).append("][最后一次完成时间:").append(lastDeliverTime).append("][总完成次数").append(deliverTimes).append("]");
		return buffer.toString();
	}

	@Override
	public void remove(int type) {
		DeliverTaskManager dm = DeliverTaskManager.getInstance();
		if(dm != null){
			dm.save(this);
		}
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}
}
