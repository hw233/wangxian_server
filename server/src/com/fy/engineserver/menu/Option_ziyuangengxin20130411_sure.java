/**
 * 
 */
package com.fy.engineserver.menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.everylogin.LoginActivityManager;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * @author Administrator
 * 
 */
public class Option_ziyuangengxin20130411_sure extends Option {

	/**
	 * 4月8日资源更新补偿方案
	 */
	public Option_ziyuangengxin20130411_sure() {
		// TODO Auto-generated constructor stub
	}

	public static String limitServers[] = {};

	public static String startTime = "2013-04-09 00:00:00";

	public static String endTime = "2013-04-13 23:59:59";

	@Override
	public void doSelect(Game game, Player player) {
		try {
			int plevel = player.getLevel();
			if (player.getLevel() < 10) {
				player.sendError("等级太低，低级必须高于10级！");
				return;
			}
			if (player.getKnapsack_common().getEmptyNum() == 0) {
				player.sendError("您的背包空间不足!");
				return;
			}
			if (LoginActivityManager.getInstance().getDdc().get(player.getId() + "20130411") != null) {
				player.sendError("您已经领取过!");
				return;
			}
			String articlenames = "";
			if (plevel < 81) {
				articlenames = "诚意锦囊(白玉泉)";
			} else if (plevel < 161) {
				articlenames = "诚意锦囊(金浆醒)";
			} else if (plevel >= 161) {
				articlenames = "诚意锦囊(香桂郢酒)";
			}

			Article a = ArticleManager.getInstance().getArticle(articlenames);
			if (a != null) {
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.资源更新补偿, player, a.getColorType(), 1, true);
					player.putToKnapsacks(ae, "4月11日资源更新补偿");
					player.sendError("恭喜您获得了" + articlenames + "礼包！");
					LoginActivityManager.getInstance().getDdc().put(player.getId() + "20130411", 1);
					CoreSubSystem.logger.warn("[4月11日资源更新补偿方案] [成功] [" + player.getLogString() + "]");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				CoreSubSystem.logger.warn("[4月11日资源更新补偿方案] [失败] [物品不存在] [物品：" + articlenames + "] [" + player.getLogString() + "]");
			}

		} catch (Exception e) {
			e.printStackTrace();
			CoreSubSystem.logger.warn("[4月8日资源更新补偿方案] [异常] [" + player.getLogString() + "]", e);
			return;
		}

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取奖励;
	}

	public void setOId(int oid) {
	}

	public String toString() {
		return "4月8日资源更新补偿方案sure";
	}

	public String[] getLimitServers() {
		return limitServers;
	}

	public void setLimitServers(String[] limitServers) {
		this.limitServers = limitServers;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
