package com.fy.engineserver.menu.fairyBuddha;

import java.util.Map;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_worship extends Option implements NeedCheckPurview, NeedRecordNPC {
	private byte career;
	private NPC npc;

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.仙尊)) {
			player.sendError(Translate.合服功能关闭提示);
			return;
		}
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		Map<Byte, FairyBuddhaInfo> fairyBuddhaMap = fbm.getFairyBuddhaMap();
		String result = "";
		if (fairyBuddhaMap != null && fairyBuddhaMap.size() > 0) {
			FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
			if (fbi != null) {
				String key = fbm.getKey(0) + fbm.KEY_答谢奖励等级 + fbi.getId();
				FairyBuddhaManager.logger.warn("[仙尊] [可以膜拜] [当前有仙尊]");
				result = fbm.doWorship(player, career, fbi.getId());
				String windowDes = "<f color='0xFFFF00'>"+Translate.当前仙尊宣言 + ":</f>\n" + fbi.getDeclaration() + "\n<f color='0xFFFF00'>" + Translate.被膜拜次数 + ":</f>" + fbi.getBeWorshipped();
				WindowManager.getInstance().getWindowMap().get(npc.getWindowId()).setDescriptionInUUB(windowDes);
			} else {
				FairyBuddhaManager.logger.warn("[仙尊] [不能膜拜] [当前无仙尊]");
				result = fbm.doWorship(player, career, 1l);
			}
		} else {
			result = Translate.无仙尊提示;
		}
		player.sendError(result);
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getCurrSoul().getCareer() == career) {
			return true;
		}
		return false;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}
}
