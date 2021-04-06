package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;

/**
 * 召唤生物道具
 * 
 */
public class CallSpriteProp extends Props {

	private int callType;// 0怪物，1NPC

	private String[] callMapLimit;// 召唤限制地图
	/** 使用此物品等级上限 */
	private int useMaxLevel;

	private Integer[] ids = new Integer[BournManager.maxBournLevel];// 各个境界等级召唤的生物

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		int playerClasslevel = player.getClassLevel();
		boolean exist = true;
		int callId = 0;
		if (playerClasslevel >= ids.length) {
			exist = false;
		} else {
			callId = ids[playerClasslevel];
			exist = callId != 0;
		}
		if (!exist) {
			return false;
		}

		// 校验地图
		String currentMapname = game.gi.name;
		boolean foundMap = false;
		for (String map : callMapLimit) {
			if (currentMapname.equals(map)) {
				foundMap = true;
				break;
			}
		}

		if (!foundMap) {
			// 提示玩家指定地图
			StringBuffer sbfBuffer = new StringBuffer();
			for (String map : callMapLimit) {
				Game gg = GameManager.getInstance().getGameByName(map, 0);
				if (gg == null) {
					gg = GameManager.getInstance().getGameByName(map, 1);
					ActivitySubSystem.logger.warn("[地图:" + map + ",国家0] [不存在]");
				}
				if (gg != null) {
					sbfBuffer.append(gg.gi.displayName).append(".");
				} else {
					ActivitySubSystem.logger.warn("[地图:" + map + ",国家1] [不存在]");
				}
			}
			player.sendError(Translate.translateString(Translate.召唤提示, new String[][] { { Translate.STRING_1, sbfBuffer.toString() } }));
			return false;
		}

		Sprite sprite = null;
		switch (callType) {
		case 0:// 怪物
			sprite = MemoryMonsterManager.getMonsterManager().createMonster(callId);
			break;
		case 1:// NPC
			sprite = MemoryNPCManager.getNPCManager().createNPC(callId);
			break;

		default:
			break;
		}
		if (sprite == null) {
			ActivitySubSystem.logger.error("[召唤生物道具错误] [生物不存在] [" + getName() + "] [类型:" + getCallType() + "] [id:" + callId + "]");
			return false;
		}
		if (!super.use(game, player, ae)) {
			return false;
		}
		sprite.setBornPoint(new Point(player.getX(), player.getY()));
		sprite.setTitle(player.getName());
		sprite.setX(player.getX());
		sprite.setY(player.getY());
		game.addSprite(sprite);

		ActivitySubSystem.logger.warn("[召唤生物] [成功] [" + getName() + "] [类型:" + getCallType() + "] [id:" + callId + "] [" + sprite.getName() + "] [(" + sprite.getX() + "," + sprite.getY() + ")] [game:" + game.gi.name + "] [" + player.getLogString() + "]");
		return true;
	}

	public int getCallType() {
		return callType;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	public String[] getCallMapLimit() {
		return callMapLimit;
	}

	public void setCallMapLimit(String[] callMapLimit) {
		this.callMapLimit = callMapLimit;
	}

	public int getUseMaxLevel() {
		return useMaxLevel;
	}

	public void setUseMaxLevel(int useMaxLevel) {
		this.useMaxLevel = useMaxLevel;
	}
}
