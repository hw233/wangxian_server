package com.fy.engineserver.jiazu2.instance;

import com.fy.engineserver.jiazu2.manager.BiaocheEntityManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 镖车强化记录
 * 
 *
 */
@SimpleEntity
public class BiaoCheEntity implements Cacheable, CacheListener{
	@SimpleId
	private long id;		//jiazuId
	@SimpleVersion
	private int version;
	
	public BiaoCheEntity() {
	}
	
	public BiaoCheEntity(long id) {
		super();
		this.id = id;
	}

	//镖车强化类型
	private byte[] strongerType;
	//强化类型对应等级
	private int[] strongerLevel;
	//强化类型对应进度
	private int[] process;
	
	@SimplePostPersist
	public void saved() {
		BiaoCheEntity dt = BiaocheEntityManager.instance.tempCache.remove(this.getId());
		if(JiazuManager2.logger.isDebugEnabled()) {
			JiazuManager2.logger.debug("[移除BiaoCheEntity] [" + dt + "] [" + this + "]");
		}
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

	public byte[] getStrongerType() {
		return strongerType;
	}

	public void setStrongerType(byte[] strongerType) {
		this.strongerType = strongerType;
		BiaocheEntityManager.em.notifyFieldChange(this, "strongerType");
	}

	public int[] getStrongerLevel() {
		return strongerLevel;
	}

	public void setStrongerLevel(int[] strongerLevel) {
		this.strongerLevel = strongerLevel;
		BiaocheEntityManager.em.notifyFieldChange(this, "strongerLevel");
	}

	@Override
	public void remove(int paramInt) {
		// TODO Auto-generated method stub
		if (paramInt == CacheListener.LIFT_TIMEOUT) {
			BiaocheEntityManager.instance.notifyRemoveFromCache(this);
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[] getProcess() {
		return process;
	}

	public void setProcess(int[] process) {
		this.process = process;
		BiaocheEntityManager.em.notifyFieldChange(this, "process");
	}

}
