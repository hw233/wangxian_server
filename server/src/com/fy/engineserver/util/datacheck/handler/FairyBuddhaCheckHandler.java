package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.fairyBuddha.DefaultFairyNpcData;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.ThankAward;
import com.fy.engineserver.activity.fairyBuddha.VoteAward;
import com.fy.engineserver.activity.fairyBuddha.WeekdayAward;
import com.fy.engineserver.activity.fairyBuddha.WorshipAward;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.playerTitles.PlayerTitlesManager.PlayerTitleTemplate;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class FairyBuddhaCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "仙尊检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "fairyBuddha.xls" };
	}

	@Override
	/**
	 * 仙尊|物品|描述
	 * 仙尊|投票奖励物品|不存在
	 * 仙尊|答谢奖励物品|不存在
	 * 仙尊|膜拜奖励物品|不存在
	 * 仙尊|仙尊npc|不存在
	 * 仙尊|默认npc摆放地图|不存在
	 * 仙尊|仙尊奖励称号|不存在
	 * 仙尊|仙尊坐骑|不存在
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();

		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();

		List<VoteAward> voteAwardList = fbm.getVoteAwardList();
		for (VoteAward va : voteAwardList) {
			ActivityProp[] props = va.getProps();
			for (ActivityProp prop : props) {
				String articleCNName = prop.getArticleCNName();
				Article a = ArticleManager.getInstance().getArticleByCNname(articleCNName);
				if (a == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "投票奖励物品", "<font color=red>[" + articleCNName + "]</font>不存在" }));
				} else {
					if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
						mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "投票奖励物品", "物品<font color=red>[" + articleCNName + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
					}
				}
			}
		}

		List<ThankAward> thankAwardList = fbm.getThankAwardList();
		for (ThankAward ta : thankAwardList) {
			String[] articleCNNameStr = ta.getArticleCNNames();
			for (String name : articleCNNameStr) {
				Article a = ArticleManager.getInstance().getArticleByCNname(name);
				if (a == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "答谢奖励物品", "<font color=red>[" + name + "]</font>不存在" }));
				} else {
					if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
						mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "答谢奖励物品", "物品<font color=red>[" + name + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
					}
				}
			}
		}

		List<WorshipAward> worshipAwardList = fbm.getWorshipAwardList();
		for (WorshipAward wa : worshipAwardList) {
			Map<Byte, Map<Byte, WeekdayAward>> levelAward = wa.getLevelAward();
			if (null != levelAward && levelAward.size() > 0) {
				for (Byte key : levelAward.keySet()) {
					Map<Byte, WeekdayAward> weekdayAwardMap = levelAward.get(key);
					if (null != weekdayAwardMap && weekdayAwardMap.size() > 0) {
						for (Byte key1 : weekdayAwardMap.keySet()) {
							WeekdayAward wda = weekdayAwardMap.get(key1);
							ActivityProp[] props = wda.getProps();
							if(props!=null){
								for (ActivityProp prop : props) {
									String articleCNName = prop.getArticleCNName();
									Article a = ArticleManager.getInstance().getArticleByCNname(articleCNName);
									if (a == null) {
										mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "膜拜奖励物品", "<font color=red>[" + articleCNName + "]</font>不存在" }));
									} else {
										if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
											mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "膜拜奖励物品", "物品<font color=red>[" + articleCNName + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		List<DefaultFairyNpcData> defaultNpcList = fbm.getDefaultNpcList();
		for (DefaultFairyNpcData dfnd : defaultNpcList) {
			int npcId = dfnd.getNpcId();
			LinkedHashMap<Integer, NPCTempalte> templates = ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).getTemplates();
			if (templates != null) {
				if (!templates.keySet().contains(npcId)) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "仙尊npc", "npcID<font color=red>[" + npcId + "]</font>不存在" }));
				}
			}

			String mapName = dfnd.getMapName();
			Game game = GameManager.getInstance().getGameByName(mapName, CountryManager.国家A);
			if (game == null) {
				game = GameManager.getInstance().getGameByName(mapName, CountryManager.中立);
			}
			if (game == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "默认npc摆放地图", "地图<font color=red>[" + mapName + "]</font>不存在" }));
			}

			String title = dfnd.getTitle();
			boolean findTitle = false;
			List<PlayerTitleTemplate> list = PlayerTitlesManager.getInstance().getList();
			for (PlayerTitleTemplate ptt : list) {
				if (ptt.getTitleName().equals(title)) {
					findTitle = true;
				}
			}
			if (!findTitle) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "仙尊奖励称号", "称号<font color=red>[" + title + "]</font>不存在" }));
			}

			String horseCNName = dfnd.getHorseCNName();
			Article a = ArticleManager.getInstance().getArticleByCNname(horseCNName);
			if (a == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "仙尊坐骑", "<font color=red>[" + horseCNName + "]</font>不存在" }));
			} else {
				if (!DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getBooleanValue()) {
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙尊", "仙尊坐骑", "物品<font color=red>[" + horseCNName + "]</font>颜色不对，错误(" + DataCheckManager.getInstance().isRightColorOfArticle(a.getName(), a.getColorType()).getStringValue() + ")" }));
				}
			}
		}

		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
