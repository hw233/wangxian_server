package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class ActivenessCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "签到活跃度物品检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "activeness.xls" };
	}

	@Override
	/**
	 * 签到活跃度|物品|描述
	 * 签到|物品名|不存在
	 * 活跃度领奖|物品名|不存在
	 * 活跃度抽奖|物品名|不存在
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "签到活跃度", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();
		
		ActivenessManager am = ActivenessManager.getInstance();
		String[] signAwardNames = am.getSignAwardNameFtr();
//		String[] awardNames = am.getAwardNameFtr();
//		String[] lotteryNames = am.getLotteryNamesFtr();
		for(String name:signAwardNames){
			Article article = ArticleManager.getInstance().getArticleByCNname(name);
			if (article == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "签到领奖", "领奖物品", "<font color=red>[" + name + "]</font>不存在" }));
			}
		}
//		for(String name:awardNames){
//			Article article = ArticleManager.getInstance().getArticleByCNname(name);
//			if (article == null) {
//				mailList.add(new SendHtmlToMail(titles, new String[] { "活跃度领奖", "领奖物品", "<font color=red>[" + name + "]</font>不存在" }));
//			}
//		}
//		for(String name:lotteryNames){
//			Article article = ArticleManager.getInstance().getArticleByCNname(name);
//			if (article == null) {
//				mailList.add(new SendHtmlToMail(titles, new String[] { "活跃度抽奖", "抽奖物品", "<font color=red>[" + name + "]</font>不存在" }));
//			}
//		}
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
