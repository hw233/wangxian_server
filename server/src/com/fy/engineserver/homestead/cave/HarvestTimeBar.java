package com.fy.engineserver.homestead.cave;

import java.util.Date;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.cave.activity.CaveHarvestActivityManager;
import com.fy.engineserver.menu.cave.activity.CaveHarvestActivityManager.CaveHarvestActivityData;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

public class HarvestTimeBar implements Callbackable {

	Cave cave;
	CaveNPC npc;
	Player player;
	CaveHarvestActivityData activityData;

	public HarvestTimeBar(Cave cave, CaveNPC npc, Player player, CaveHarvestActivityData activityData) {
		this.cave = cave;
		this.npc = npc;
		this.player = player;
		this.activityData = activityData;
	}

	@Override
	public void callback() {
		String timeStr = TimeTool.formatter.varChar10.format(new Date());
		// TODO 判断钱,扣钱,通知摘取完成,给奖励
		boolean canPickup = CaveHarvestActivityManager.canPickup(player, timeStr);
		if(canPickup){
			if(activityData!=null){
				cave.setRewardTimes(activityData.getTimes()+1);
			}
		}
		CompoundReturn cr = cave.pickFruit(player, npc.getId());
		String failreason = Translate.text_cave_001;
		if (!cr.getBooleanValue()) {
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_002;
				break;
			case 2:

				failreason = Translate.text_cave_021;
				break;
			case 3:
				failreason = Translate.text_cave_050;

				break;
			case 4:
				failreason = Translate.text_cave_050;

				break;
			case 5:
				failreason = Translate.text_cave_051;

				break;
			case 6:
				failreason = Translate.text_cave_052;

				break;
			case 7:
				failreason = Translate.text_cave_053;
				break;
			case 8:
				failreason = Translate.text_cave_099;
				break;

			default:
				break;
			}
		}
		if (!Translate.text_cave_001.equals(failreason)) {
			player.sendError(failreason);
		} else {
			try {
				if(activityData!=null){
					CompoundReturn doDeduct = activityData.doDeduct(player);
					if (!doDeduct.getBooleanValue()) {
						player.sendError(doDeduct.getStringValue());
						return;
					}
					if (canPickup && activityData!=null) {
						activityData.doPrize(player);
						CaveHarvestActivityManager.noticePickup(player, timeStr);
					} 
				}
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[庄园剪刀活动] [金剪刀2] [" + player.getLogString() + "] [异常]", e);
			}
		}
	}

}
