package com.fy.engineserver.authority;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 权利组记录某个玩家对于一些类型的使用次数/时间等等，并且随着VIP等级变化，道具的使用，可以增长，但是在一定周期内会清除
 * 
 */
@SimpleEmbeddable
public class Authority {
	
	/**
	 * 权利组类型
	 */
	public int type;
	
	/**
	 * 上次刷新时间，可能是小时，天，周，月中的一种
	 */
	public int lastRefreshItem;
	
	/**
	 * 上次刷新的天，一年中的天
	 */
	public int lastRefreshDay;
	
	/**
	 * 本周期内已经行使的次数
	 */
	public int usedNum;
	
	/**
	 * 其他类型额外增加的次数，例如使用道具后增加的次数，这个次数只在本周期内增加，下一个周期此值将清0
	 */
	public int otherAdd;
	
	/**
	 * 累计的次数
	 */
	public int accumulateNum;
	
	public transient AuthorityConfig config;
	
	public transient Player owner;
	
	/**
	 * 是否还能执行
	 * @return
	 */
	public synchronized boolean canUse() throws TotalNumAccessedException,MaxNumAccessedException {
		if(usedNum >= this.getConfig().maxNum[owner.getVipLevel()]) {
			throw new MaxNumAccessedException("已达使用次数上限");
		}
		if(usedNum >= getTotalNum()) {
			throw new TotalNumAccessedException("已经没有剩余未行使次数了");
		}
		return true;
	}
	
	/**
	 * 增加一次执行
	 */
	public synchronized void addUsed(Player p) {
		usedNum++;
		if(this.owner == null){
			this.owner = p;
		}
	}
	
	/**
	 * 得到当前总的允许执行次数
	 * @return
	 */
	public int getTotalNum() {
		int vipAdd = getVipAddNum();
		if(AuthorityAgent.logger.isDebugEnabled())
			AuthorityAgent.logger.debug("[获得当前允许次数] [{}] [defaultNum:{}] [vipAdd:{}] [otherAdd:{}]",new Object[]{(this.owner==null?"NULL":this.owner.getId()),this.getConfig().defaultNum,vipAdd,otherAdd});
		return this.getConfig().defaultNum+vipAdd+otherAdd+accumulateNum;
	}
	
	public int getVipAddNum() {
		return  this.getConfig().vipAddNum[owner.getVipLevel()];
	}
	
	/**
	 * 得到已经执行了的次数
	 * @return
	 */
	public int getUsedNum() {
		return usedNum;
	}

	public AuthorityConfig getConfig() {
		if(config == null){
			config = AuthorityAgent.getInstance().getAuthorityConfig(type);
			if(AuthorityAgent.logger.isDebugEnabled())
				AuthorityAgent.logger.debug("[此时config为空，重新获得config:"+(config==null?"NULL":config)+"]");
		}
		return config;
	}

	public int getAccumulateNum() {
		return accumulateNum;
	}

	public void setAccumulateNum(int accumulateNum) {
		this.accumulateNum = accumulateNum;
	}

	public void setConfig(AuthorityConfig config) {
		this.config = config;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
