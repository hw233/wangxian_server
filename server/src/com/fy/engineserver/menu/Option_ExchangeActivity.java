package com.fy.engineserver.menu;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

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
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.model.Passport;

/**
 * 兑换活动
 * 需求：一个或多个同类或不同类物品
 * 兑换得到：一个或多个同类或不同类物品，一个称号（非必选项）
 * 可根据时间和渠道判断活动是否可见
 * 
 * 
 */
public class Option_ExchangeActivity extends Option implements NeedInitialiseOption,
		NeedCheckPurview {
	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;
	private String endTimeStr;

	/** 消耗的物品 */
	private String costArticleNames;
	private String costArticleColors;
	private String costArticleNums;

	/** 兑换出的物品 */
	private String exchangeArticleNames;
	private String exchangeArticleColors;
	private String exchangeArticleNums;

	private boolean exchangeBind;
	
	/** 兑换得到称号 */
	private String titleKey;
	private boolean useAtonce;
	
	/** 邮件标题和内容 */
	private String mailTitle;
	private String mailContent;

	/** 参与活动的渠道串，不配的话表示所有渠道都有 */
	String channel;

	/** 解析后的数据 */
	private String[] costArticleNameArr;
	private Integer[] costArticleColorArr;
	private Integer[] costArticleNumArr;
	private String[] exchangeArticleNameArr;
	private Integer[] exchangeArticleColorArr;
	private Integer[] exchangeArticleNumArr;
	private long startTime;
	private long endTime;
	private String[] channels ;

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
			if(!(null == titleKey && "".equals(titleKey))){
				doTitle(player);
			}
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换成功] [" + exchangeArticleNames + "[" + exchangeArticleColors + "]" + "*" + exchangeArticleNums + "]");
		} else {
			player.sendError(Translate.translateString(Translate.物品不存在提示, new String[][] { { Translate.STRING_1, exchangeArticleNames } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [奖励不存在] [" + exchangeArticleNames + "]");
		}
	}

	public void removeAllcost(Player player) {
		for (int i = 0; i < costArticleNameArr.length; i++) {
			String articleName = costArticleNameArr[i];
			int articleColor = costArticleColorArr[i];
			int articleNum = costArticleNumArr[i];
			for (int n = 0; n < articleNum; n++) {
				player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.BOTH, "活动", true);
				ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [扣除物品] [articleName:" + articleName + "] [articleColor:" + articleColor + "]");
			}
		}
	}

	public void doPrize(Player player) {
		Article[] prize = new Article[exchangeArticleNameArr.length];
		ArticleEntity[] ae = new ArticleEntity[exchangeArticleNameArr.length];
		String str1 = "";//用于提示玩家的
		String str2 = "";//用于邮件里的
		try {
			for(int i=0;i<exchangeArticleNameArr.length;i++){
				prize[i] = ArticleManager.getInstance().getArticle(exchangeArticleNameArr[i]);
				ae[i] = ArticleEntityManager.getInstance().createEntity(prize[i], exchangeBind, ArticleEntityManager.活动, player, Integer.valueOf(exchangeArticleColorArr[i]), Integer.valueOf(exchangeArticleNumArr[i]), true);
				int colorValue = ArticleManager.getColorValue(prize[i], exchangeArticleColorArr[i]);
				if(i<exchangeArticleNameArr.length-1){
					str1 += "<f color='" + colorValue + "'>" + exchangeArticleNameArr[i] + "*" + exchangeArticleNumArr[i] + " </f>";
					str2 += exchangeArticleNameArr[i] + "*" + exchangeArticleNumArr[i] + ",";
				}else{
					str1 += "<f color='" + colorValue + "'>" + exchangeArticleNameArr[i] + "*" + exchangeArticleNumArr[i] + "</f>";
					str2 += exchangeArticleNameArr[i] + "*" + exchangeArticleNumArr[i] + ".";
				}
			}
			//MailManager.getInstance().sendMail(player.getId(), ae, ArrayUtils.toPrimitive(exchangeArticleNumArr), Translate.恭喜您获得了奖励, Translate.恭喜您获得了奖励 + str2, 0L, 0L, 0L, "兑换物品活动-" + getText());
			MailManager.getInstance().sendMail(player.getId(), ae, ArrayUtils.toPrimitive(exchangeArticleNumArr), mailTitle, mailContent + str2, 0L, 0L, 0L, "兑换物品活动");
			player.sendError(Translate.translateString(Translate.获得兑换奖励, new String[][] { { Translate.STRING_1, str1 } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励OK]");
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励异常]", e);
		}

	}
	
	public void doTitle(Player player){
		int count = PlayerTitlesManager.getInstance().getKeyByType(titleKey);
		String titleName = PlayerTitlesManager.getInstance().getValueByType(titleKey);
		if (count == -1) {
			ActivitySubSystem.logger.error("[兑换称号活动] " + player.getLogString() + "[兑换称号:" + titleName + "] [失败] [称号不存在]");
		} else {
			PlayerTitlesManager.getInstance().addTitle(player, titleKey, useAtonce);
			if (ActivitySubSystem.logger.isErrorEnabled()) {
				ActivitySubSystem.logger.error("[兑换称号活动] " + player.getLogString() + "[奖励称号:" + titleName + "] [成功]");
			}
		}
	}

	public boolean prizeExist() {
		Article[] prize = new Article[exchangeArticleNameArr.length];
		for(int i=0;i<exchangeArticleNameArr.length;i++){
			prize[i] = ArticleManager.getInstance().getArticle(exchangeArticleNameArr[i]);
			if(prize[i]==null) 
				return false;
		}
		return true;
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

	public String getExchangeArticleNames() {
		return exchangeArticleNames;
	}

	public void setExchangeArticleNames(String exchangeArticleNames) {
		this.exchangeArticleNames = exchangeArticleNames;
	}

	public String getExchangeArticleNums() {
		return exchangeArticleNums;
	}

	public void setExchangeArticleNums(String exchangeArticleNums) {
		this.exchangeArticleNums = exchangeArticleNums;
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

	public String[] getExchangeArticleNameArr() {
		return exchangeArticleNameArr;
	}

	public void setExchangeArticleNameArr(String[] exchangeArticleNameArr) {
		this.exchangeArticleNameArr = exchangeArticleNameArr;
	}

	public Integer[] getExchangeArticleColorArr() {
		return exchangeArticleColorArr;
	}

	public void setExchangeArticleColorArr(Integer[] exchangeArticleColorArr) {
		this.exchangeArticleColorArr = exchangeArticleColorArr;
	}

	public Integer[] getExchangeArticleNumArr() {
		return exchangeArticleNumArr;
	}

	public void setExchangeArticleNumArr(Integer[] exchangeArticleNumArr) {
		this.exchangeArticleNumArr = exchangeArticleNumArr;
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

	public String getExchangeArticleColors() {
		return exchangeArticleColors;
	}

	public void setExchangeArticleColors(String exchangeArticleColors) {
		this.exchangeArticleColors = exchangeArticleColors;
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

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public boolean isUseAtonce() {
		return useAtonce;
	}

	public void setUseAtonce(boolean useAtonce) {
		this.useAtonce = useAtonce;
	}
	
	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String[] getChannels() {
		return channels;
	}

	public void setChannels(String[] channels) {
		this.channels = channels;
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
			if(channel == null || "".equals(channel.trim())){
				Game.logger.warn(getText() + "无渠道串配置");
				return;
			}
			channels = channel.split(",");
		} else {
			throw new IllegalStateException("[Option_ExchangeArticle] [参数未配置] [costArticleNums:"+costArticleNums+"] [costArticleNames:"+costArticleNames+"] [" + getText() + "]");
		}
		if (exchangeArticleNums != null && exchangeArticleNames != null) {
			exchangeArticleNumArr = StringTool.string2Array(exchangeArticleNums, ",", Integer.class);
			exchangeArticleNameArr = StringTool.string2Array(exchangeArticleNames, ",", String.class);
			exchangeArticleColorArr = StringTool.string2Array(exchangeArticleColors, ",", Integer.class);
			if (exchangeArticleNumArr.length != exchangeArticleNameArr.length || exchangeArticleNameArr.length != exchangeArticleColorArr.length) {
				System.out.println(getText() + "参数配置错误");
				throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [" + getText() + "] [exchangeArticleNums:" + exchangeArticleNums + "] [exchangeArticleNames:" + exchangeArticleNames + "] [exchangeArticleColors:" + exchangeArticleColors + "]");
			}
		} else {
			throw new IllegalStateException("[Option_ExchangeArticle] [参数未配置] [exchangeArticleNums:"+exchangeArticleNums+"] [exchangeArticleNames:"+exchangeArticleNames+"] [" + getText() + "]");
		}
	}

	@Override
	public boolean canSee(Player player) {
		boolean rightChannel = false;
		boolean rightTime = false;

		if(channel == null || "".equals(channel.trim())){//无渠道限制
			rightChannel=true;
		}else{
			for (String channel : channels) {
				Passport passport = player.getPassport();
				if (passport != null) {
					String channelName = passport.getLastLoginChannel(); // 获取当前登录渠道
					if (channelName.equals(channel)) {
						rightChannel = true;
						ActivitySubSystem.logger.warn(player.getLogString()
								+ "[渠道正确活动可见]");
					}
				} else {
					ActivitySubSystem.logger.warn(player.getLogString()
							+ "[玩家渠道为空]");
				}
			}
		}

		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			rightTime = true;
		}else{
			long now = SystemTime.currentTimeMillis();
			if (startTime <= now || now <= endTime) {
				rightTime = true;
			}
		}

		return rightChannel && rightTime;
	}

	@Override
	public String toString() {
		return "Option_ExchangeArticle [startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", costArticleNames=" + costArticleNames + ", costArticleColors=" + costArticleColors + ", costArticleNums=" + costArticleNums + ", exchangeArticleName=" + exchangeArticleNames + ", exchangeArticleColor=" + exchangeArticleColors + ", exchangeArticleNum=" + exchangeArticleNums + ", exchangeBind=" + exchangeBind + ", costArticleNameArr=" + Arrays.toString(costArticleNameArr) + ", costArticleColorArr=" + Arrays.toString(costArticleColorArr) + ", costArticleNumArr=" + Arrays.toString(costArticleNumArr) + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
