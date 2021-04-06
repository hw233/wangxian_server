package com.fy.engineserver.activity.fairylandTreasure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fy.engineserver.activity.pickFlower.FlushPoint;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;

public class FairylandTreasureEntity {
	// 是否有效，时间到就没效。true(有效)
	private boolean effect;
	private long invalidTime;// 本次活动无效时间，时间到无效
	public long lastHeartBeatTime;

	public Map<FlushPoint, FairylandTreasure> map = new HashMap<FlushPoint, FairylandTreasure>();

	public FairylandTreasureEntity(int lastingTime) throws Exception {
		// 设置时间 关闭时间 状态等
		this.effect = true;
		this.invalidTime = SystemTime.currentTimeMillis() + lastingTime * 60 * 1000;

		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		List<FlushPoint> list = ftm.pointList;
		int length = list.size();

		for (int i = 0; i < ftm.getFairylandBoxList().size(); i++) {
			FairylandTreasureManager.logger.warn("["+ftm.getFairylandBoxList().get(i)+"] [刷新个数:"+ftm.getFairylandBoxList().get(i).getNum()+"]");
			
			for (int j = 0; j < ftm.getFairylandBoxList().get(i).getNum(); j++) {
				int random = ftm.random.nextInt(length);
				FlushPoint p = list.get(random);
				// //周围10个点内随机
				// int addX = ftm.random.nextInt(2);
				// int randomX = ftm.random.nextInt(10);
				// if (addX == 0) {
				// p.x = p.x - randomX;
				// } else {
				// p.x = p.x + randomX;
				//
				// }
				// int addY = ftm.random.nextInt(2);
				// int randomY = ftm.random.nextInt(10);
				// if (addX == 0) {
				// p.y = p.y - randomY;
				// } else {
				// p.y = p.y + randomY;
				//
				// }
				FairylandTreasure f = map.get(p);
				if (f == null) {
					FairylandTreasure fairylandTreasure = FairylandTreasureManager.getInstance().createFairylandTreasure(p);
					fairylandTreasure.setLevel(i);
					fairylandTreasure.fairylandTreasureEntity = this;
					randomNpc(p, fairylandTreasure, i);
					fairylandTreasure.appperUpdate();
					map.put(p, fairylandTreasure);
				}else{
					j--;
//					FairylandTreasureManager.logger.warn("[("+p.x+","+p.y+")已存在不刷新]");
				}
			}

		}
		FairylandTreasureManager.logger.warn("[生成仙界宝箱活动实体成功]");
	}

	// 随机刷出等级的npc 设置物品
	public FairylandTreasureNpc randomNpc(FlushPoint point, FairylandTreasure fairylandTreasure, int i) {

		int npcId = FairylandTreasureManager.getInstance().getFairylandBoxList().get(i).getNpcId();

		FairylandTreasureNpc npc = (FairylandTreasureNpc) MemoryNPCManager.getNPCManager().createNPC(npcId);
		// npc.setNameColor(ArticleManager.color_article[npcIndex]);
		if (npc == null) {

			FairylandTreasureManager.logger.warn("[仙界宝箱] [" + i + "] [npcId:" + npcId + "] [npc为空]");
		}
		npc.setFairylandTreasure(fairylandTreasure);

		npc.setX(point.x);
		npc.setY(point.y);
		ResourceManager.getInstance().getAvata(npc);
		fairylandTreasure.game.addSprite(npc);
		fairylandTreasure.npc = npc;
		FairylandTreasureManager.logger.warn("[生成npc成功] [" + i + "] [npcId:" + npcId + "] [地图:" + (fairylandTreasure.game != null ? fairylandTreasure.game.gi.displayName : "null") + "] [npcName:" + npc.getName() + "][位置:(" + npc.getX() + "," + npc.getY() + ")]");

		return npc;
	}

	public void heartBeat() {

		long now = SystemTime.currentTimeMillis();

		if (effect == true && now >= invalidTime) {
			effect = false;
			return;
		}

		// flushBoxNpc();
	}

	public void fairylandTreasureEnd() {
		try {
			for (Entry<FlushPoint, FairylandTreasure> en : map.entrySet()) {

				FairylandTreasure f = en.getValue();
				if (f != null && !f.isDisappear()) {
					if (f.npc != null) {
						f.game.removeSprite(f.npc);
					} else {
						FairylandTreasureManager.logger.warn("[仙界宝箱结束删除npc错误] [npc null]");
					}
				}
			}

			FairylandTreasureManager.logger.warn("[仙界宝箱结束删除npc完成]");
		} catch (Exception e) {
			FairylandTreasureManager.logger.warn("[仙界宝箱结束异常]", e);
		}
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public long getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(long invalidTime) {
		this.invalidTime = invalidTime;
	}

}
