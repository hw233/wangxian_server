package com.fy.engineserver.soulpith.module;

import com.fy.engineserver.util.SimpleKey;

/***
 * 灵髓升级所需经验
 * 
 * @date on create 2016年3月23日 上午9:48:33
 */
public class SoulLevelupExpModule {
	@SimpleKey
	private int level;
	
	private long needExp;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getNeedExp() {
		return needExp;
	}

	public void setNeedExp(long needExp) {
		this.needExp = needExp;
	}
}
