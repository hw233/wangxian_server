package com.fy.engineserver.activity.loginActivity;

import com.fy.engineserver.sprite.Player;

public abstract class Activity {
	
	/**是否有效**/
	public abstract boolean isEffective();
	
	/**活动奖励**/
	public abstract void doPrizes(Player player,int index);

	/**活动名称**/
	public abstract String getName();
	
	/**开始，关闭**/
	public abstract boolean isOpen();
	
}
