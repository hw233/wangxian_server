package com.fy.engineserver.menu.fairyBuddha;

import java.util.Map;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.WorshipAward;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FAIRY_AWARD_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_set_worshipAwardLevel extends Option implements NeedCheckPurview {
	private byte career;

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	@Override
	public void doSelect(Game game, Player player) {
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		FairyBuddhaInfo fbi = fbm.getFairyBuddhaMap().get(career);
		if (fbi != null) {
			String key = fbm.getKey(0) + fbm.KEY_膜拜奖励等级 + "_" + fbi.getId();
			FairyBuddhaManager.logger.warn("[" + fbi.getLogString() + "] [仙尊] [膜拜奖励] [key:" + key + "]");
			byte level = 0;
			if (fbm.disk.get(key) != null) {
				level = (Byte) fbm.disk.get(key);
			} else {
				level = (byte) -1;
			}
			FairyBuddhaManager.logger.warn("[" + fbi.getLogString() + "] [仙尊] [膜拜奖励] [level:" + level + "]");
			WorshipAward wa = fbm.getRightWorshipAward();
			if (wa != null) {
				FairyBuddhaManager.logger.warn("[" + fbi.getLogString() + "] [仙尊] [获取当时的膜拜奖励]");
				Map<Byte, String> awardNameMap = wa.getAwardNameMap();
				String[] awardNames = new String[awardNameMap.size()];
				String[] des = new String[awardNameMap.size()];
				long[] prices = new long[awardNameMap.size()];
				for (int i = 0; i < awardNameMap.size(); i++) {
					awardNames[i] = awardNameMap.get((byte) i);
					des[i] = wa.getDesMap().get((byte) i);
					prices[i] = wa.getPriceMap().get((byte) i);
					FairyBuddhaManager.logger.warn("[" + fbi.getLogString() + "] [仙尊] [膜拜奖励" + i + "] [awardNames:" + awardNames[i] + "]");
				}
				FAIRY_AWARD_RES res = new FAIRY_AWARD_RES(GameMessageFactory.nextSequnceNum(), awardNames, des, prices, level);
				FairyBuddhaManager.logger.warn("[" + fbi.getLogString() + "] [仙尊] [设置膜拜奖励] [level:" + level + "] [向客户端发协议:FAIRY_AWARD_RES]");

				player.addMessageToRightBag(res);
			}
		}
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getCurrSoul().getCareer() == career) {
			FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
			FairyBuddhaInfo fbi = fbm.getFairyBuddhaMap().get(career);
			if (fbi != null) {
				if (player.getId() == fbi.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
