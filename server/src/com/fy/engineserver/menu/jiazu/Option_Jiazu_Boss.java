package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 召唤家族BOSS
 * 
 * 
 */
public class Option_Jiazu_Boss extends Option implements NeedCheckPurview {
	final static int maxTimes = 2;

	@Override
	public void doSelect(Game game, Player player) {
		long now = SystemTime.currentTimeMillis();
		if (JiazuSubSystem.logger.isInfoEnabled()) {
			JiazuSubSystem.logger.info(player.getLogString() + "[点击召唤BOSS]");
		}
		if (!player.inSelfSeptStation()) {
			return;
		}
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(player.getJiazuId());
		if (septStation == null) {
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		if (jiazu == null || jiazuMember == null) {
			player.sendError(Translate.text_jiazu_031);
			return;
		}
		try {
			boolean hasPermission = JiazuTitle.hasPermission(jiazuMember.getTitle(), JiazuFunction.call_BOSS);
			if (!hasPermission) {
				player.sendError(Translate.text_jiazu_047);
				return;
			}

			{
				// 召唤次数判断
				boolean lastInsameCycle = TimeTool.instance.isSame(jiazu.getLastCallbossTime(), now, TimeDistance.DAY, 7);
				if (!lastInsameCycle) {
					jiazu.setCurrentWeekCallbossTimes(0);
				}
				if (lastInsameCycle) {
					if (jiazu.getCurrentWeekCallbossTimes() >= maxTimes) {
						player.sendError(Translate.translateString(Translate.text_jiazu_176, new String[][] { { Translate.COUNT_1, String.valueOf(maxTimes) } }));
						return;
					}
				}
			}

			int level = jiazu.getLevel();
			int bossId = SeptStationMapTemplet.getInstance().getJiazuBossIds()[level];
			Point point = SeptStationMapTemplet.getInstance().getBossPoint();
			if (point == null) {
				player.sendError(Translate.text_jiazu_109);
				return;
			}
			if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
				player.sendError(Translate.家族资金不足封印);
				return ;
			}
			{
				// 钱是否够
				long cost = SeptStationMapTemplet.getInstance().getJiazuBossCost()[level];
				synchronized (jiazu) {
					if (jiazu.getJiazuMoney() < cost) {
						player.sendError(Translate.text_jiazu_107);
						if (JiazuSubSystem.logger.isWarnEnabled()) {
							JiazuSubSystem.logger.warn(player.getLogString() + "[召唤家族BOSS] [bossID:" + bossId + "] [消耗家族资金:" + cost + "] [资金不足] [家族等级:" + level + "] [已有资金:" + jiazu.getJiazuMoney() + "]");
						}
						return;
					} else {
						jiazu.setJiazuMoney(jiazu.getJiazuMoney() - cost);
						if (JiazuSubSystem.logger.isWarnEnabled()) {
							JiazuSubSystem.logger.warn(player.getLogString() + "[召唤家族BOSS] [bossID:" + bossId + "] [消耗家族资金:" + cost + "] [资金足够]");
						}
						try {
							if (JiazuManager2.instance.noticeNum.get(jiazu.getLevel()) != null) {
								int num = JiazuManager2.instance.noticeNum.get(jiazu.getLevel());
								if (jiazu.getJiazuMoney() < num) {
									JiazuManager2.instance.noticeJizuMondeyNotEnough(jiazu);
								}
							}
						} catch (Exception e) {
							JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + jiazu.getLogString() + "]", e);
						}
					}
				}
			}

			// 记录召唤次数和时间
			jiazu.setCurrentWeekCallbossTimes(jiazu.getCurrentWeekCallbossTimes() + 1);
			jiazu.setLastCallbossTime(now);

			Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			if (monster != null && monster instanceof BossMonster) {
				monster.setX(point.getX());
				monster.setY(point.getY());
				((BossMonster) monster).setJiazuId(jiazu.getJiazuID());
				monster.setGameNames(septStation.getGame().gi);
				septStation.getGame().addSprite(monster);
				ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.召唤BOSS, new String[][] { { Translate.STRING_1, player.getName(), player.getName() }, { Translate.STRING_2, monster.getName() } }), "");

				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn("[家族:" + jiazu.getName() + "] [召唤家族BOSS] [成功] [BOSS:" + bossId + "] [位置:" + point.toString() + "]");
				}
			} else {
				JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [召唤家族BOSS] [失败] [BOSS不存在:" + bossId + "]");
			}
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "] [召唤家族BOSS] [失败]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (!player.inSelfSeptStation()) {
			return false;
		}
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(player.getJiazuId());
		if (septStation == null) {
			return false;
		}

		return true;
	}
}
