package com.fy.engineserver.menu;

import java.util.Arrays;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

/**
 * 用物品兑换物品
 * 
 * 
 */
public class Option_ExchangeArticle extends Option implements NeedInitialiseOption,
		NeedCheckPurview {
	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;
	private String endTimeStr;

	/** 消耗的物品 */
	private String costArticleNames;
	private String costArticleColors;
	private String costArticleNums;

	/** 兑换出的物品 */
	private String exchangeArticleName;
	private String exchangeArticleColor;
	private String exchangeArticleNum;

	private boolean exchangeBind;

	/* 解析后的数据 */
	private String[] costArticleNameArr;
	private Integer[] costArticleColorArr;
	private Integer[] costArticleNumArr;
	private long startTime;
	private long endTime;

	@Override
	public void doSelect(Game game, Player player) {
		CompoundReturn cr = hasAllcostArticle(player);
		if (!cr.getBooleanValue()) {
			player.sendError(Translate.translateString(Translate.你没有所需道具, new String[][] { { Translate.STRING_1, cr.getStringValue() } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [缺少物品:" + cr.toString() + "]");
			return;
		}
		if (prizeExist()) {
			removeAllcost(player);
			doPrize(player);
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换成功] [" + exchangeArticleName + "[" + exchangeArticleColor + "]" + "*" + exchangeArticleNum + "]");
		} else {
			player.sendError(Translate.translateString(Translate.物品不存在提示, new String[][] { { Translate.STRING_1, exchangeArticleName } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [奖励不存在] [" + exchangeArticleName + "]");
		}
	}

	public void removeAllcost(Player player) {
		for (int i = 0; i < costArticleNameArr.length; i++) {
			String articleName = costArticleNameArr[i];
			int articleColor = costArticleColorArr[i];
			int articleNum = costArticleNumArr[i];
			for (int n = 0; n < articleNum; n++) {
				player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.BOTH, "活动" , true);
				ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [扣除物品] [articleName:" + articleName + "] [articleColor:" + articleColor + "]");
			}
		}
	}

	public void doPrize(Player player) {
		Article prize = ArticleManager.getInstance().getArticle(exchangeArticleName);
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(prize, exchangeBind, ArticleEntityManager.活动, player, Integer.valueOf(exchangeArticleColor), Integer.valueOf(exchangeArticleNum), true);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { Integer.valueOf(exchangeArticleNum) }, Translate.系统, Translate.兑换活动奖励, 0L, 0L, 0L, "兑换物品活动");
			int colorValue = ArticleManager.getColorValue(prize, Integer.valueOf(exchangeArticleColor));
			player.sendError(Translate.translateString(Translate.获得兑换奖励, new String[][] { { Translate.STRING_1, "<f color='" + colorValue + "'>" + exchangeArticleName + "*" + Integer.valueOf(exchangeArticleNum) + "</f>" } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励OK]");
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励异常]", e);
		}

	}

	public boolean prizeExist() {
		Article prize = ArticleManager.getInstance().getArticle(exchangeArticleName);
		return prize != null;
	}

	public CompoundReturn hasAllcostArticle(Player player) {
		StringBuffer notice = new StringBuffer();
		boolean enough = true;
		for (int i = 0; i < costArticleNameArr.length; i++) {
			String articleName = costArticleNameArr[i];
			int articleColor = costArticleColorArr[i];
			int articleNum = costArticleNumArr[i];
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article == null) {
				enough = false;
				notice.append("[" + articleName + " not exist!]");
				continue;
			}
			int hasNum = player.getArticleNum(articleName, articleColor, BindType.BOTH);
			if (hasNum < articleNum) {
				enough = false;
				int colorValue = ArticleManager.getColorValue(article, articleColor);
				notice.append("<f color='" + colorValue + "'>" + articleName + "</f>*" + (articleNum - hasNum) + ".");
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(enough).setStringValue(notice.toString());
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getCostArticleNames() {
		return costArticleNames;
	}

	public void setCostArticleNames(String costArticleNames) {
		this.costArticleNames = costArticleNames;
	}

	public String getCostArticleNums() {
		return costArticleNums;
	}

	public void setCostArticleNums(String costArticleNums) {
		this.costArticleNums = costArticleNums;
	}

	public String getExchangeArticleName() {
		return exchangeArticleName;
	}

	public void setExchangeArticleName(String exchangeArticleName) {
		this.exchangeArticleName = exchangeArticleName;
	}

	public String getExchangeArticleNum() {
		return exchangeArticleNum;
	}

	public void setExchangeArticleNum(String exchangeArticleNum) {
		this.exchangeArticleNum = exchangeArticleNum;
	}

	public String[] getCostArticleNameArr() {
		return costArticleNameArr;
	}

	public void setCostArticleNameArr(String[] costArticleNameArr) {
		this.costArticleNameArr = costArticleNameArr;
	}

	public Integer[] getCostArticleNumArr() {
		return costArticleNumArr;
	}

	public void setCostArticleNumArr(Integer[] costArticleNumArr) {
		this.costArticleNumArr = costArticleNumArr;
	}

	public String getCostArticleColors() {
		return costArticleColors;
	}

	public void setCostArticleColors(String costArticleColors) {
		this.costArticleColors = costArticleColors;
	}

	public Integer[] getCostArticleColorArr() {
		return costArticleColorArr;
	}

	public void setCostArticleColorArr(Integer[] costArticleColorArr) {
		this.costArticleColorArr = costArticleColorArr;
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

	public String getExchangeArticleColor() {
		return exchangeArticleColor;
	}

	public void setExchangeArticleColor(String exchangeArticleColor) {
		this.exchangeArticleColor = exchangeArticleColor;
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

	public boolean isExchangeBind() {
		return exchangeBind;
	}

	public void setExchangeBind(boolean exchangeBind) {
		this.exchangeBind = exchangeBind;
	}

	@Override
	public void init() throws Exception {
		if (costArticleNums != null && costArticleNames != null) {
			costArticleNumArr = StringTool.string2Array(costArticleNums, ",", Integer.class);
			costArticleNameArr = StringTool.string2Array(costArticleNames, ",", String.class);
			costArticleColorArr = StringTool.string2Array(costArticleColors, ",", Integer.class);
			if (costArticleNumArr.length != costArticleNameArr.length || costArticleNameArr.length != costArticleColorArr.length) {
				System.out.println(getText() + "参数配置错误");
				throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [" + getText() + "] [costArticleNums:" + costArticleNums + "] [costArticleNames:" + costArticleNames + "] [costArticleColors:" + costArticleColors + "]");
			}
			if (startTimeStr == null || "".equals(startTimeStr)) {
				System.out.println(getText() + "无时间配置");
				return;
			}
			startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
			endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		} else {
			throw new IllegalStateException("[Option_ExchangeArticle] [参数未配置] [" + getText() + "]");
		}
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			return true;
		}
		long now = SystemTime.currentTimeMillis();
		return startTime <= now && now <= endTime;
	}

	@Override
	public String toString() {
		return "Option_ExchangeArticle [startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", costArticleNames=" + costArticleNames + ", costArticleColors=" + costArticleColors + ", costArticleNums=" + costArticleNums + ", exchangeArticleName=" + exchangeArticleName + ", exchangeArticleColor=" + exchangeArticleColor + ", exchangeArticleNum=" + exchangeArticleNum + ", exchangeBind=" + exchangeBind + ", costArticleNameArr=" + Arrays.toString(costArticleNameArr) + ", costArticleColorArr=" + Arrays.toString(costArticleColorArr) + ", costArticleNumArr=" + Arrays.toString(costArticleNumArr) + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
