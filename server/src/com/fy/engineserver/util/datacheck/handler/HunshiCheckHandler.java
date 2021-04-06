package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.hunshi.HunshiJianDing;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.hunshi.HunshiPropId;
import com.fy.engineserver.hunshi.HunshiSuit;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class HunshiCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "坐骑魂石检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "hunshi.xls" };
	}

	@Override
	/**
	 * 魂石|物品|描述
	 * 魂石|魂石名字|不存在
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();

		HunshiManager hm = HunshiManager.getInstance();

		Map<Integer, HunshiJianDing> jianDingCost = hm.jianDingCost;
		Map<Integer, HunshiJianDing> jianDingCost2 = hm.jianDingCost2;
		// 鉴定材料存在，融合值范围和融合值概率数组长度一致

		Map<String, HunshiPropId> hunshiPropIdMap = hm.hunshiPropIdMap;
		// 魂石存在，属性id在-1-24（两端包含）之间，副属性id自身无重复
		for (String name : hunshiPropIdMap.keySet()) {
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石名字", "<font color=red>[" + name + "]</font>不存在" }));
			} else {
				if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石名字", "物品<font color=red>[" + name + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
				}
			}
			HunshiPropId prop = hunshiPropIdMap.get(name);
			if (prop == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>对应的HunshiPropId不存在" }));
			} else {
				int[] mainIds = prop.getMainPropId();
				if (mainIds == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>没有配置主属性" }));
				} else {
					for (int id : mainIds) {
						if (id > 24) mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>主属性id<font color=red>[" + id + "]</font>不存在" }));
					}
				}
				int[] extraIds = prop.getExtraPropId();
				if (extraIds == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>没有配置副属性" }));
				} else {
					for (int i = 0; i < extraIds.length; i++) {
						int tempid = extraIds[i];
						if (tempid > 24) mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>副属性id<font color=red>[" + tempid + "]</font>不存在" }));
						for (int j = i + 1; j < extraIds.length; j++) {
							if (extraIds[j] == tempid) {
								mailList.add(new SendHtmlToMail(titles, new String[] { "魂石", "魂石属性", "<font color=red>[" + name + "]</font>副属性id<font color=red>[" + tempid + "]</font>重复" }));
							}
						}
					}
				}
			}
		}

		Map<String, HunshiSuit> tempMap = hm.tempMap;
		// 需要的套装石存在，套装名和统计名数组长度一致，属性id存在，属性id和属性值数组长度一致，技能id存在

		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
