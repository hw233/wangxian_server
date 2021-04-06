package com.fy.engineserver.activity.silvercar;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.RandomTool.RandomType;

public class SilvercarRefreshTimebar implements Callbackable {

	private Player player;
	private int oldColor;
	private NPC npc;
	private BiaoCheNpc biaoCheNpc;

	public SilvercarRefreshTimebar(Player player, int oldColor, NPC npc, BiaoCheNpc biaoCheNpc) {
		this.player = player;
		this.oldColor = oldColor;
		this.npc = npc;
		this.biaoCheNpc = biaoCheNpc;
	}

	@Override
	public synchronized void callback() {
		if (npc instanceof BiaoCheNpc) {
			BiaoCheNpc target = (BiaoCheNpc) npc;
			if (target.getRefreshNPC().size() >= 5) {
				player.sendError(Translate.text_silverCar_020);
				return;
			}
		}
		biaoCheNpc.getRefreshNPC().add(npc.getId());

		short oldSize = biaoCheNpc.getObjectScale();
		int color = 0;
		boolean isBiaoZhangReward = CountryManager.getInstance().isBiaoZhangReward(player);
		if (!biaoCheNpc.isJiazuCar() && isBiaoZhangReward) {// 不是家族且是表彰的,必橙
			color = 4;
		} else {
			color = RandomTool.getResultIndexs(RandomType.groupRandom, SilvercarManager.getInstance().getRefreshRate(), 1).get(0);
		}
		biaoCheNpc.setObjectScale((short) (1000 * SilvercarManager.getInstance().getCarSize()[color]));
		biaoCheNpc.setNameColor(ArticleManager.color_article[color]);
		biaoCheNpc.setGrade(color);
		SilvercarManager.sneerAt(player, color);
		player.sendError(Translate.translateString(Translate.text_silverCar_008, new String[][] { { Translate.STRING_1, "<f color='" + ArticleManager.color_article[oldColor] + "'>" + biaoCheNpc.getName() + "</f>" }, { Translate.STRING_2, "<f color='" + ArticleManager.color_article[color] + "'>" + biaoCheNpc.getName() + "</f>" } }));
		if (SilvercarManager.logger.isWarnEnabled()) {
			SilvercarManager.logger.warn(player.getLogString() + "[在NPC:{}加货] [完成] [原来颜色:{}] [加货后颜色:{}] [大小:{}] [原来大小:{}] [是否是表彰:{}]", new Object[] { npc.getName(), oldColor, color, biaoCheNpc.getObjectScale(), oldSize, isBiaoZhangReward });
		}
		if (npc instanceof BiaoCheNpc) {
			biaoCheNpc.setRefershTimes(biaoCheNpc.getRefershTimes() + 1);
		}
		if (color > oldColor) {
			biaoCheNpc.setMaxColor(color);
		}
		{
			// 统计
			if (color == 3) {// 紫色
				AchievementManager.getInstance().record(player, RecordAction.个人运镖紫BUFF次数);
			} else if (color == 4) {// 橙色
				AchievementManager.getInstance().record(player, RecordAction.个人运镖橙BUFF次数);
			}
		}
	}

}
