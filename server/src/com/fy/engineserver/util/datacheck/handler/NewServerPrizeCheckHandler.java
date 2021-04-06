package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.NewPlayerPrize;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class NewServerPrizeCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "新服礼包检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "newServerPrize.xls" };
	}

	@Override
	/**
	 * 新服礼包|礼包分组|描述
	 * 平台-服务器|分组名|不存在
	 * 分组名|物品|不存在
	 * 分组名|物品|颜色不正确
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "新服礼包", "礼包分组", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();
		
		ActivityManager am = ActivityManager.getInstance();
		List<NewPlayerPrize> newPlayerPrizes=am.getNewPlayerPrizes();
		Map<String, List<ActivityProp>> prizeMap=am.getPrizeMap();
		for(NewPlayerPrize prize:newPlayerPrizes){
			if (!prizeMap.containsKey(prize.getPrizeGroupName())) {
				mailList.add(new SendHtmlToMail(titles, new String[] { prize.getPlatform()+"-"+prize.getServerName(), "分组名", "<font color=red>[" + prize.getPrizeGroupName() + "]</font>不存在" }));
			}
		}
		for(Iterator<String> it=prizeMap.keySet().iterator();it.hasNext();){
			String prizeGroup=it.next();
			List<ActivityProp> activityProps=prizeMap.get(prizeGroup);
			for(ActivityProp ap:activityProps){
				Article article = ArticleManager.getInstance().getArticle(ap.getArticleCNName());
				if (article == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "分组名["+prizeGroup+"]", "物品", "<font color=red>[" + ap.getArticleCNName() + "]</font>不存在" }));
				}
			}
		}
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
