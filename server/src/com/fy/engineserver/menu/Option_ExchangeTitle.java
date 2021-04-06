package com.fy.engineserver.menu;


import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;


/**
 * 兑换活动
 * 用一个物品兑换得到一个称号
 * 可根据时间判断活动是否可见
 * 
 */
public class Option_ExchangeTitle extends Option implements NeedInitialiseOption,NeedCheckPurview{
	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;
	private String endTimeStr;
	
	private String articleName;
	private int articleColor;
	private String titleKey;
	private boolean useAtonce;
	
	/* 解析后的数据 */
	private long startTime;
	private long endTime;
	
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}


	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isUseAtonce() {
		return useAtonce;
	}

	public void setUseAtonce(boolean useAtonce) {
		this.useAtonce = useAtonce;
	}

	@Override
	public void doSelect(Game game, Player player) {
		int num =  player.getArticleNum(articleName, articleColor, BindType.BOTH);
		if (num < 0) {
			player.sendError(Translate.text_473 + ArticleManager.color_article_Strings[articleColor] + articleName);
			if (ActivitySubSystem.logger.isErrorEnabled()) {
				ActivitySubSystem.logger.error(player.getLogString() + "[兑换物品活动] [背包中没有所需物品:" + articleName + "]");
			}
			return;
		}
		ArticleEntity removeFromKnapsacks = player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.BOTH, "运营活动", true);
		if (removeFromKnapsacks == null) {
			player.sendError(Translate.text_473 + ArticleManager.color_article_Strings[articleColor] + articleName);
			if (ActivitySubSystem.logger.isErrorEnabled()) {
				ActivitySubSystem.logger.error(player.getLogString() + "[兑换物品活动] [兑换时没有所需物品:" + articleName + "]");
			}
			return;
		}
		
		int count = PlayerTitlesManager.getInstance().getKeyByType(titleKey);
		String titleName = PlayerTitlesManager.getInstance().getValueByType(titleKey);
		if (count == -1) {
			ActivitySubSystem.logger.error("[兑换物品活动] " + player.getLogString() + "[兑换称号:" + titleName + "] [失败] [称号不存在]");
		} else {
			PlayerTitlesManager.getInstance().addTitle(player, titleKey, useAtonce);
			if (ActivitySubSystem.logger.isErrorEnabled()) {
				ActivitySubSystem.logger.error("[兑换物品活动] " + player.getLogString() + "[奖励称号:" + titleName + "] [成功]");
			}
		}
		
	}
	@Override
	public void init() {
		if (startTimeStr == null || "".equals(startTimeStr)) {
			System.out.println(getText() + "无时间配置");
			return;
		}
		startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
	}
	
	@Override
	public boolean canSee(Player player) {
		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			return true;
		}
		long now = SystemTime.currentTimeMillis();
		return startTime <= now && now <= endTime;
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public String toString() {
		return "Option_ExchangeArticle [startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
