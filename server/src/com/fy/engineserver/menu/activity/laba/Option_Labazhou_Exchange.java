package com.fy.engineserver.menu.activity.laba;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 八宝粥兑换的超类
 * 
 * 
 */
public abstract class Option_Labazhou_Exchange extends Option {
	public Map<String, Map<Long, Integer>> map = new HashMap<String, Map<Long, Integer>>();

	public static int minLevel = 41;

	@Override
	public void doSelect(Game game, Player player) {

		if (player.getLevel() < minLevel) {
			player.sendError(Translate.translateString(Translate.你的等级不足斩妖除魔, new String[][] { { Translate.COUNT_1, String.valueOf(minLevel) } }));
			return;
		}
		String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		ArticleEntity ae = player.getArticleEntity(getArticleName());

		if (ae == null) {
			player.sendError(Translate.translateString(Translate.你没有兑换所需的物品, new String[][]{{Translate.STRING_1},{getArticleName()}}));
			return;
		}
		if (!canExchange(player, date)) {
			player.sendError(Translate.你今天兑换已经超过了次数);
			return;
		}
		ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "腊八节兑换", true);
		if(aee==null){
			String description = Translate.删除物品不成功;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ActivitySubSystem.logger.isWarnEnabled()) ActivitySubSystem.logger.warn("[腊八节兑换] ["+description+"] [id:"+ae.getId()+"]");
			return;
		}
		String res = exchange(player, date);
		player.sendError(res);
		ActivitySubSystem.logger.warn("[腊八节] " + player.getLogString() + "[使用:" + getArticleName() + "兑换成功] [" + res + "]");

	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public boolean canExchange(Player player, String date) {
		if (!map.containsKey(date)) {
			map.put(date, new HashMap<Long, Integer>());
		}
		Map<Long, Integer> dailyMap = map.get(date);
		if (!dailyMap.containsKey(player.getId())) {
			dailyMap.put(player.getId(), 0);
		}
		if (dailyMap.get(player.getId()) >= getDailyNum()) {
			return false;
		}

		dailyMap.put(player.getId(), dailyMap.get(player.getId()) + 1);
		return true;
	}

	/**
	 * 得到当天还可兑换的数量
	 * @param player
	 * @return
	 */
	public int getLeftExchangeNum(Player player, String date) {
		if (!map.containsKey(date) || !map.get(date).containsKey(player.getId())) {
			return getDailyNum();
		}
		int exchangeNum = map.get(date).get(player.getId());

		return Math.max(getDailyNum() - exchangeNum, 0);

	}

	/** 每天可兑换的数量 */
	abstract int getDailyNum();

	/** 要扣除的物品名称 */
	abstract String getArticleName();

	/** 兑换,子类随机给东西 */
	abstract String exchange(Player player, String date);
}
