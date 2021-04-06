package com.fy.engineserver.menu;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.everylogin.LoginActivityDoPrizeByNotice;
import com.fy.engineserver.activity.everylogin.LoginActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 登录领取提示
 * 
 * 
 */
public class Option_Login_Commit extends Option {

	public Option_Login_Commit() {
		super();
	}

	// public long startTime;
	// public long endTime;
	// public String prizeName;
	// public int prizeNum;
	// public Platform []platforms;
	// public Set<String> servers = new HashSet<String>();
	LoginActivityDoPrizeByNotice loginNotice;

	public Option_Login_Commit(LoginActivityDoPrizeByNotice loginNotice) {
		this.loginNotice = loginNotice;
	}

	// public Option_Login_Commit(long startTime, long endTime, String prizeName, int prizeNum, Set<String> servers,Platform [] platforms){
	// this.startTime = startTime;
	// this.endTime = endTime;
	// this.prizeName = prizeName;
	// this.prizeNum = prizeNum;
	// this.servers = servers;
	// this.platforms = platforms;
	// }
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game, Player player) {
		if (loginNotice != null) {
			CompoundReturn cr = loginNotice.doPrize(player,0);
			if (cr.getBooleanValue()) {// 奖励成功
				LoginActivityManager.getInstance().notifyPlayerLogin(loginNotice, player);
				player.sendError(cr.getStringValue());
				ActivitySubSystem.logger.error("[连续登陆活动] [" + player.getLogString() + "] [获得奖励] [" + loginNotice.getName() + "] [" + cr.getStringValue() + "]");
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取奖励;
	}

	public void setOId(int oid) {
	}

	public String toString() {
		return Translate.text_4857;
	}
}
