package com.fy.engineserver.menu.arborday;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

/**
 * 道具兑换元气
 * 
 * 
 */
public class Option_Article_ExchangeYuanqi extends Option implements NeedCheckPurview {

	public static String articleName = "生长药水";

	public static Map<Integer, Integer> prizeYuanqiMap = new HashMap<Integer, Integer>();
	/** 角色使用记录 */
	public static Map<String, Map<Long, Integer>> playerUseRecord = new HashMap<String, Map<Long, Integer>>();
	public static int MAX_TIMES = 3;

	static {
		prizeYuanqiMap.put(70, 1008);
		prizeYuanqiMap.put(110, 4640);
		prizeYuanqiMap.put(150, 7710);
		prizeYuanqiMap.put(190, 13149);
	}

	@Override
	public void doSelect(Game game, Player player) {
		String today = TimeTool.formatter.varChar10.format(new Date());
		if (playerUseRecord.containsKey(today) && playerUseRecord.get(today).containsKey(player.getId()) && playerUseRecord.get(today).get(player.getId()) >= MAX_TIMES) {
			player.sendError("你今天已经用道具浇水达到了" + MAX_TIMES + "次, 明天再来吧");
			return;
		}
		SealManager sealManager = SealManager.getInstance();
		int currentSeal = sealManager.seal.sealLevel;

		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article == null) {
			player.sendError(articleName + "不存在");
			return;
		}
		int hasNum = player.getArticleNum(articleName, article.getColorType(), BindType.BOTH);
		if (hasNum <= 0) {
			player.sendError("所需的物品不足!不能施肥");
			return;
		}
		player.removeArticleByNameColorAndBind(articleName, article.getColorType(), BindType.BOTH, "植树节", true);
		{
			// 发奖励
			int prizeYuanqi = prizeYuanqiMap.get(currentSeal);
			if (prizeYuanqi > 0) {
				// BillingCenter.getInstance().playerSaving(player, prizeLilian, CurrencyType.LILIAN, SavingReasonType.活动奖励, "植树节");
				player.setEnergy(player.getEnergy() + prizeYuanqi);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[植树节] [道具浇水] [成功] [" + player.getLogString() + "] [消耗道具:" + articleName + "] [获得元气:" + prizeYuanqi + "] [总元气:"+player.getEnergy()+"] [new]");
				}
				player.sendError("施肥成功 ,恭喜你使用:" + articleName + ",获得修法值:" + prizeYuanqi);
				// 记录次数
				record(player.getId(), today);
			}
		}
	}

	public synchronized void record(long playerId, String day) {
		if (!playerUseRecord.containsKey(day)) {
			playerUseRecord.put(day, new HashMap<Long, Integer>());
		}
		if (!playerUseRecord.get(day).containsKey(playerId)) {
			playerUseRecord.get(day).put(playerId, 0);
		}
		playerUseRecord.get(day).put(playerId, playerUseRecord.get(day).get(playerId) + 1);
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		long now = SystemTime.currentTimeMillis();
		return ArborDayManager.inCycle(now);
	}
}
