package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivity;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivityLimit;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class ExchangeActivityCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "兑换活动物品检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "exchangeActivity.xls" };
	}

	@Override
	/**
	 * 兑换活动|物品|描述
	 * 兑换活动|活动名|不存在
	 * 兑换活动|菜单名|消耗物品不存在
	 * 兑换活动|菜单名|获得物品不存在
	 * 兑换活动|菜单名|消耗物品颜色不正确
	 * 兑换活动|菜单名|获得物品颜色不正确
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();
		
		ExchangeActivityManager eam = ExchangeActivityManager.getInstance();
		Map<String, List<ExchangeActivity>> menuMap=eam.getMenuMap();
		
		for (Iterator<String> itor = menuMap.keySet().iterator(); itor.hasNext();) {
			String name = itor.next();
			ExchangeActivityLimit eal=eam.getExchangeActivityLimit(name);
			if (eal == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "兑换活动", name, "活动名<font color=red>[" + name + "]</font>不存在" }));
			}
			List<ExchangeActivity> eaList=menuMap.get(name);
			for(ExchangeActivity ea:eaList){
				String menuName=ea.getMenuName();
				String[] costArticleCNNameArr=ea.getCostArticleCNNameArr();
				for(String costName:costArticleCNNameArr){
					Article a = ArticleManager.getInstance().getArticleByCNname(costName);
					if(a==null){
						mailList.add(new SendHtmlToMail(titles, new String[] { "兑换活动", menuName, "消耗物品<font color=red>[" + costName + "]</font>不存在" }));
					}else{
						if(!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()){
							mailList.add(new SendHtmlToMail(titles, new String[] { "兑换活动", menuName, "物品<font color=red>[" + costName + "]</font>颜色不对，错误("+DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue()+")" }));
						}
					}
				}
				String[] gainArticleCNNameArr=ea.getGainArticleCNNameArr();
				for(String gainName:gainArticleCNNameArr){
					Article a = ArticleManager.getInstance().getArticleByCNname(gainName);
					if(a==null){
						mailList.add(new SendHtmlToMail(titles, new String[] { "兑换活动", menuName, "获得物品<font color=red>[" + gainName + "]</font>不存在" }));
					}else{
						if(!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()){
							mailList.add(new SendHtmlToMail(titles, new String[] { "兑换活动", menuName, "物品<font color=red>[" + gainName + "]</font>颜色不对，错误("+DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue()+")" }));
						}
					}
					
				}
			}
		}
				
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
