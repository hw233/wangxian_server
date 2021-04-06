package com.fy.engineserver.smith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * 
 * @version 创建时间：May 21, 2013 10:11:05 AM
 * 
 */
public class RelationShipHelper {
	
	public static Logger logger = LoggerFactory.getLogger(RelationShipHelper.class);
	
	public static String content = Translate.打金网络限制提示;

	/**
	 * 检查是否因打金行为封禁，如果是，那么返回true，并且对玩家进行消息提示
	 * 
	 * @param player
	 * @return
	 */
	public static boolean checkForbidAndSendMessage(Player player) {
		if(player == null) {
			return false;
		}
		if(true) {
			return false;
		}
		boolean forbidByMoney = false;
		boolean forbidByArticle = false;
		MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
		ArticleRelationShipManager ars = ArticleRelationShipManager.getInstance();
		if (msm != null && msm.isForbid(player.getId())) {
			forbidByMoney = true;
		}
		if (ars != null && ars.isForbid(player.getId())) {
			forbidByArticle = true;
		}
		if (forbidByMoney || forbidByArticle) {
			//player.send_HINT_REQ(content, (byte)0);
			logger.warn("[玩家因打金行为被限制] ["+player.getLogString()+"] [forbidByMoney:"+forbidByMoney+"] [forbidByArticle:"+forbidByArticle+"]");
			return true;
		}
		return false;
	}
	
	/**
	 * 只检查是否被打金网络限制
	 * @param player
	 * @return
	 */
	public static boolean checkForbid(Player player) {
		if(player == null) {
			return false;
		}
		if(true) {
			return false;
		}
		boolean forbidByMoney = false;
		boolean forbidByArticle = false;
		MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
		ArticleRelationShipManager ars = ArticleRelationShipManager.getInstance();
		if (msm != null && msm.isForbid(player.getId())) {
			forbidByMoney = true;
		}
		if (ars != null && ars.isForbid(player.getId())) {
			forbidByArticle = true;
		}
		if (forbidByMoney || forbidByArticle) {
			if(logger.isDebugEnabled()) {
				logger.debug("[玩家因打金行为被限制] ["+player.getLogString()+"] [forbidByMoney:"+forbidByMoney+"] [forbidByArticle:"+forbidByArticle+"]");
			}
			return true;
		}
		return false;
	}
}
