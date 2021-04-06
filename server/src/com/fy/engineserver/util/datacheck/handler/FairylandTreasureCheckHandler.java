package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.fairylandTreasure.ArticleForDraw;
import com.fy.engineserver.activity.fairylandTreasure.FairylandBox;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class FairylandTreasureCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "仙界宝箱检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "fairylandTreasure.xls" };
	}

	@Override
	/**
	 * 仙界宝箱|物品|描述
	 * 仙界宝箱|宝箱npcId|不存在
	 * 仙界宝箱|仙界宝箱钥匙名字|不存在
	 * 仙界宝箱|获得道具概率和祈福加成概率|数组长度不一致
	 * 仙界宝箱|获得道具概率和祈福类型|数组长度不一致
	 * 仙界宝箱|抽奖库物品|不存在
	 * 仙界宝箱|抽奖库必现物品|不存在
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();

		FairylandTreasureManager ftm=FairylandTreasureManager.getInstance();

		MemoryNPCManager mnm=(MemoryNPCManager)MemoryNPCManager.getNPCManager();
		LinkedHashMap<Integer, NPCTempalte> templates=mnm.getTemplates();
		List<FairylandBox> fairylandBoxList=ftm.getFairylandBoxList();
		for(FairylandBox fb:fairylandBoxList){
			if(!templates.containsKey(fb.getNpcId())){
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "宝箱npcId", "<font color=red>[" + fb.getNpcId() + "]</font>不存在" }));
			}
			
			Article a = ArticleManager.getInstance().getArticle(fb.getBoxKeyName());
			if (a == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "宝箱钥匙名字", "<font color=red>[" + fb.getBoxKeyName() + "]</font>不存在" }));
			} else {
				if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "宝箱钥匙名字", "物品<font color=red>[" + fb.getBoxKeyName() + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
				}
			}
			
			if (fb.getRates().length != fb.getPrayRates().length) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "数组长度不一致", "获得各类型道具的概率数组长度和祈福加成的数组长度不一致" }));
			}else {
				if(fb.getRates().length!=(ftm.prayTypeMap.size()-1)){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "数组长度不一致", "获得各类型道具的概率数组长度和祈福类型长度不一致" }));
				}
			}
		}

		Map<Integer, List<ArticleForDraw>> drawMap=ftm.getDrawMap();
		for(Integer type:drawMap.keySet()){
			List<ArticleForDraw> afdList= drawMap.get(type);
			if(afdList!=null&&afdList.size()>0){
				for(ArticleForDraw afd:afdList){
					Article a = ArticleManager.getInstance().getArticle(afd.getName());
					if (a == null) {
						mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "抽奖库-"+ftm.prayTypeMap.get(type), "<font color=red>[" + afd.getName() + "]</font>不存在" }));
					} else {
						if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
							mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "抽奖库-"+ftm.prayTypeMap.get(type), "物品<font color=red>[" + afd.getName() + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
						}
					}
				}
			}
		}
		Map<Integer, List<ArticleForDraw>> drawSureShowMap = ftm.getDrawSureShowMap();
		for(Integer type:drawSureShowMap.keySet()){
			List<ArticleForDraw> afdList= drawMap.get(type);
			if(afdList!=null&&afdList.size()>0){
				for(ArticleForDraw afd:afdList){
					Article a = ArticleManager.getInstance().getArticle(afd.getName());
					if (a == null) {
						mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "必现抽奖库-"+ftm.prayTypeMap.get(type), "<font color=red>[" + afd.getName() + "]</font>不存在" }));
					} else {
						if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
							mailList.add(new SendHtmlToMail(titles, new String[] { "仙界宝箱", "必现抽奖库-"+ftm.prayTypeMap.get(type), "物品<font color=red>[" + afd.getName() + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
						}
					}
				}
			}
		}
		
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
