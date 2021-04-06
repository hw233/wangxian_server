package com.fy.engineserver.menu;

import java.util.Arrays;
import java.util.Calendar;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.exception.BillFailedException;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 银子加道具兑换工资
 * 需求：足够数量的银子，道具
 * 兑换得到：工资
 * 可根据时间判断活动是否可见
 * 
 * 
 */
public class Option_ExchangeSliver_Salary extends Option implements NeedInitialiseOption,
		NeedCheckPurview {
	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;
	private String endTimeStr;

	/** 消耗的银子和物品 */
	private long needMoney;
	private String costArticleNames;
	private String costArticleColors;
	private String costArticleNums;

	/** 兑换得到的工资 */
	private long wage;

	/* 解析后的数据 */
	private String[] costArticleNameArr;
	private Integer[] costArticleColorArr;
	private Integer[] costArticleNumArr;
	private long startTime;
	private long endTime;

	@Override
	public void doSelect(Game game, Player player) {
		CompoundReturn cr = hasAllcostArticle(player);
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		Long time = (Long)ddc.get("兑换物品活动"+player.getId());
		if(time==null){
			ddc.put("兑换物品活动"+player.getId(), new Long(1));
		}
		time = (Long)ddc.get("兑换物品活动"+player.getId());
		if(!isSameDay(time.longValue(),System.currentTimeMillis())){
			if (!cr.getBooleanValue()) {
				player.sendError(Translate.translateString(Translate.你没有所需道具, new String[][] { { Translate.STRING_1, cr.getStringValue() } }));
				ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [缺少物品:" + cr.toString() + "]");
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, needMoney, CurrencyType.YINZI, ExpenseReasonType.兑换活动, "兑换活动扣除银子", -1);
				if (ActivitySubSystem.logger.isWarnEnabled()) ActivitySubSystem.logger.warn("[兑换活动] [扣钱] [{}] [{}]", new Object[] { player.getLogString(), needMoney });
				removeAllcost(player);
				if(BillingCenter.getInstance().playerSaving(player, wage, CurrencyType.GONGZI, SavingReasonType.兑换活动, "工资")){
					ddc.put("兑换物品活动"+player.getId(), new Long(System.currentTimeMillis()));
				}
				player.sendError((Translate.text_boothsale_010 + "工资" + BillingCenter.得到带单位的银两(wage)) + ",请去工资商店查收!");
			} catch (com.fy.boss.authorize.exception.NoEnoughMoneyException e) {
				if (ActivitySubSystem.logger.isErrorEnabled()) ActivitySubSystem.logger.error("兑换活动扣除银子出错：Player=[{}] needMoney=[{}]", new Object[] { player.getId(), needMoney });
				return;
			} catch (BillFailedException e) {
				if (ActivitySubSystem.logger.isErrorEnabled()) ActivitySubSystem.logger.error("兑换活动扣除银子出错：Player=[{}] needMoney=[{}]", new Object[] { player.getId(), needMoney });
				return;
			}catch (Exception e) {
				ActivitySubSystem.logger.error("[兑换活动加工资异常] [角色ID:{}] [工资:{}]", new Object[] { player.getId(), wage }, e);
			}
		}else{
			player.sendError("您今天已经领取过!");
		}
		
	}

	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
		
		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
		
		return year1==year2&&month1==month2&&day1==day2; 
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
	
	public void addWage(Player player){
		
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

	public long getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}

	public long getWage() {
		return wage;
	}

	public void setWage(long wage) {
		this.wage = wage;
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
			throw new IllegalStateException("[Option_ExchangeArticle] [参数未配置] [costArticleNums:"+costArticleNums+"] [costArticleNames:"+costArticleNames+"] [" + getText() + "]");
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
		return "Option_ExchangeArticle [startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", costArticleNames=" + costArticleNames + ", costArticleColors=" + costArticleColors + ", costArticleNums=" + costArticleNums + ", costArticleNameArr=" + Arrays.toString(costArticleNameArr) + ", costArticleColorArr=" + Arrays.toString(costArticleColorArr) + ", costArticleNumArr=" + Arrays.toString(costArticleNumArr) + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
