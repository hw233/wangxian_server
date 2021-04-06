package com.fy.engineserver.activity.levelUpReward.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.levelUpReward.LevelUpRewardEntityManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 冲级返利活动entity
 *
 */
@SimpleEntity
public class LevelUpRewardEntity implements Cacheable, CacheListener{
	@SimpleId
	private long id;		//playerId
	@SimpleVersion
	private int version;
	/** 玩家购买的冲级返利信息 */
	private List<RewardInfo> rewardInfo = new ArrayList<RewardInfo>();
	
	@Override
	public String toString() {
		return "LevelUpRewardEntity [id=" + id + ", version=" + version + ", rewardInfo=" + rewardInfo + "]";
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
	public List<RewardInfo> getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(List<RewardInfo> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	@SimplePostPersist
	public void saved() {
		LevelUpRewardEntity dt = LevelUpRewardEntityManager.instance.tempCache.remove(this.getId());
	}
	@Override
	public void remove(int paramInt) {
		// TODO Auto-generated method stub
		if (paramInt == CacheListener.LIFT_TIMEOUT) {
			LevelUpRewardEntityManager.instance.notifyRemoveFromCache(this);
		}
		
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
