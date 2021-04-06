package com.fy.engineserver.menu.vip;

import java.util.Calendar;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.VipManager;

/**
 * 转盘摇奖
 */
public class Lottery_Option extends Option implements NeedCheckPurview {

	private Player player;

	public Lottery_Option() {
		// TODO Auto-generated constructor stub
	}

	public Lottery_Option(Player player) {
		this.player = player;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// 弹出转盘
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[VIP转盘] [通知玩家打开转盘] [前] [" + player.getName() + "]");
		}
		VipManager.getInstance().openLottery(player);
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[VIP转盘] [通知玩家打开转盘] [后] [" + player.getName() + "]");
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		return player.getVipAgent().hasRecorded() && player.getVipAgent().canLottery(now);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
