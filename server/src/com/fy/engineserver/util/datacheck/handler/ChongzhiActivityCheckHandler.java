package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

/**
 * 充值活动检查
 * 
 * 
 */
public class ChongzhiActivityCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "充值活动检查-周月反馈";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "activity_newChongZhi.xls" };
	}

	@Override
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "活动ID", "数据类型", "物品名称", "结果描述" };
		List<SendHtmlToMail> list = new LinkedList<SendHtmlToMail>();
		ArrayList<WeekAndMonthChongZhiActivity> activities = NewChongZhiActivityManager.weekMonthDatas;
		for (WeekAndMonthChongZhiActivity activity : activities) {
			String[] buyPropNames = activity.getBuyPropNames();
			for (String buyPropName : buyPropNames) {
				Article article = ArticleManager.getInstance().getArticle(buyPropName);
				if (article == null) {
					list.add(new SendHtmlToMail(titles, new String[] { String.valueOf(activity.getDataID()), "购买的物品", buyPropName, "不存在" }));
				}
			}
			String[] rewardPropNames = activity.getRewardPropNames();

			for (String prizePropName : rewardPropNames) {
				Article article = ArticleManager.getInstance().getArticle(prizePropName);
				if (article == null) {
					list.add(new SendHtmlToMail(titles, new String[] { String.valueOf(activity.getDataID()), "奖励的物品", prizePropName, "不存在" }));
				}
			}
		}

		return cr.setBooleanValue(list.size() > 0).setObjValue(list.toArray(new SendHtmlToMail[0]));
	}

}
