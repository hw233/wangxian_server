package com.fy.engineserver.menu.activity.laba;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 用经验兑换腊八粥
 * 
 * 
 */
public class Option_ExpExchangeLabazhou extends Option {

	public Map<String, Map<Long, Integer>> map = new HashMap<String, Map<Long, Integer>>();

	public static long needExp = 200000;
	public static int dailyNum = 3;

	public static String articleName = "腊八粥";

	public static int minLevel = 41;

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getLevel() < minLevel) {
			player.sendError(Translate.translateString(Translate.你的等级不足斩妖除魔, new String[][] { { Translate.COUNT_1, String.valueOf(minLevel) } }));
			return;
		}
		if (player.getKnapsack_common().getEmptyNum() <= 0) {
			player.sendError("你的包裹满了,不能兑换");
			ActivitySubSystem.logger.error(player.getLogString() + "[腊八粥] [兑换" + articleName + "失败] [背包已满] ");
			return;
		}
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article == null) {
			ActivitySubSystem.logger.error(player.getLogString() + "[腊八粥] [腊八节道具不存在:" + articleName + "]");
			return;
		}
		if (!canExchange(player)) {
			player.sendError("你今天兑换次数已满,明天再来吧!");
			ActivitySubSystem.logger.error(player.getLogString() + "[腊八粥] [兑换" + articleName + "失败] [兑换次数已满] ");
			return;
		}
		boolean subExpSucc = player.subExp(needExp, "腊八节");
		if (!subExpSucc) {
			player.sendError("你的经验不足,不能兑换");
			ActivitySubSystem.logger.error(player.getLogString() + "[腊八粥] [兑换" + articleName + "失败] [经验不足] ");
			return;
		}
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.LABA_ACTIVITY, player, article.getColorType(), 1, true);
			player.putToKnapsacks(ae, "腊八活动");
			player.sendError("恭喜你消耗经验:" + needExp + ",获得了" + articleName);
			ActivitySubSystem.logger.error(player.getLogString() + "[腊八粥] [兑换" + articleName + "/" + ae.getId() + "成功] ");
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[腊八粥] [创建物品异常] " + player.getLogString(), e);

		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public boolean canExchange(Player player) {
		String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		if (!map.containsKey(date)) {
			map.put(date, new HashMap<Long, Integer>());
		}
		Map<Long, Integer> dailyMap = map.get(date);
		if (!dailyMap.containsKey(player.getId())) {
			dailyMap.put(player.getId(), 0);
		}
		if (dailyMap.get(player.getId()) >= dailyNum) {
			return false;
		}

		dailyMap.put(player.getId(), dailyMap.get(player.getId()) + 1);
		return true;
	}

}
