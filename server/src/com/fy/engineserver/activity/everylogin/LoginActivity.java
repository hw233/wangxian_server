package com.fy.engineserver.activity.everylogin;

import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 登陆活动
 * 
 */
public abstract class LoginActivity {

	public abstract CompoundReturn doPrize(Player player,int days);
	
	public abstract boolean open();

	/** 唯一 */
	public abstract String getName();

	public abstract boolean timeFit();
	
	public abstract Platform[] getPlatform();
}
