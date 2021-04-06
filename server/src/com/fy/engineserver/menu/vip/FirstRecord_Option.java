package com.fy.engineserver.menu.vip;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.VipManager;
import com.fy.engineserver.vip.data.VipAgent;
import com.fy.engineserver.vip.vipinfo.VipInfoRecordManager;

/**
 * 记录VIP信息
 */
public class FirstRecord_Option extends Option implements NeedCheckPurview {

	private Player player;

	public FirstRecord_Option() {
		// TODO Auto-generated constructor stub
	}

	public FirstRecord_Option(Player player) {
		this.player = player;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[VIP] [初次记录信息] [前] [" + player.getName() + "]");
		}
		VipInfoRecordManager.getInstance().gatherVipInfo(player);
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[VIP] [初次记录信息] [后] [" + player.getName() + "]");
		}
	}

	@Override
	public boolean canSee(Player player) {
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return false;
		}
		VipAgent agent = player.getVipAgent();
		return agent.getLastRecordtime() <= 0 && VipInfoRecordManager.getInstance().isGather(player) && agent.hasAuthority();
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
