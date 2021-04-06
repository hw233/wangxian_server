package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.xianling.BoardPrize;
import com.fy.engineserver.activity.xianling.ScorePrize;
import com.fy.engineserver.activity.xianling.TimedTask;
import com.fy.engineserver.activity.xianling.XianLingLevelData;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.activity.xianling.XianLingSkill;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class XianLingCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "仙灵检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "xianling.xls" };
	}

	@Override
	/**
	 * 仙灵|物品|描述
	 * 仙灵|关卡等级为0或1|等级异常
	 * 仙灵|关卡对应的地图信息不存在|不存在
	 * 仙灵|怪物数组与概率数组长度不一致|不一致
	 * 仙灵|排行奖励名次区间错误|区间错误
	 * 仙灵|排行奖励|物品不存在
	 * 仙灵|积分奖励|物品不存在
	 * 仙灵|限时任务|目标id在关卡中不存在
	 * 仙灵|限时任务奖励|物品不存在
	 * 仙灵|捕捉技能|物品不存在
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "物品", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();

		XianLingManager xm = XianLingManager.instance;

		List<XianLingLevelData> levelDatas = xm.levelDatas;
		for(XianLingLevelData data:levelDatas){
			if(data.getType()>1||data.getType()<0){
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "关卡等级为0或1", "关卡"+data.getLevel()+"等级<font color=red>[" + data.getType() + "]</font>异常" }));
			}
			GameInfo gi = GameManager.getInstance().getGameInfo(data.getGameName());
			if (gi == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "关卡对应的地图信息不存在", "关卡"+data.getLevel()+"对应地图<font color=red>[" + data.getGameName() + "]</font>异常" }));
			}
			if(data.getMonsterCategoryIds().length != data.getRates().length){
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "怪物数组与概率数组长度不一致", "关卡"+data.getLevel()+"怪物数组长度<font color=red>[" + data.getMonsterCategoryIds().length + "]</font>，概率数组长度<font color=red>[" + data.getRates().length + "]</font>" }));
			}

		}
		
		List<BoardPrize> boardPrizes = xm.getBoardPrizes();
		for(BoardPrize bp:boardPrizes){
			if(bp.getStartIndex()>bp.getEndIndex()){
				mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "排行奖励名次区间错误", "排名起始值大于结束值" }));
			}
			for(ActivityProp ap:bp.getPrizeProps()){
				Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
				if(a == null){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "排行奖励", "物品<font color=red>[" + ap.getArticleCNName() + "]</font>不存在" }));
				}
			}
		}
		List<ScorePrize> scorePrizes = xm.getScorePrizes();
		for(ScorePrize sp:scorePrizes){
			for(ActivityProp ap:sp.getPrizeProps()){
				Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
				if(a == null){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "积分奖励", "物品<font color=red>[" + ap.getArticleCNName() + "]</font>不存在" }));
				}
			}
		}
		
		Map<Byte, List<TimedTask>> taskTypeMap = xm.taskTypeMap;
		for(List list:taskTypeMap.values()){
			List<TimedTask> timedTaskList = list;
			for(TimedTask task:timedTaskList){
				boolean find = false;
				int monsterCategoryId =task.getMonsterCategoryId();
				for(XianLingLevelData data:levelDatas){
					String[] monsterCategoryIds = data.getMonsterCategoryIds();
					for(String ids:monsterCategoryIds){
						String[] mIds = ids.split(",");
						for(String id:mIds){
							if(monsterCategoryId == Integer.valueOf(id)){
								find = true;
							}
						}
					}
				}
				if(!find){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "限时任务", "目标id<font color=red>[" + monsterCategoryId + "]</font>在关卡中没有" }));
				}
			}
		}
		
		Map<Byte, List<ActivityProp>> timedTaskPrizeMap = xm.timedTaskPrizeMap;
		for(List list:timedTaskPrizeMap.values()){
			List<ActivityProp> apList = list;
			for(ActivityProp ap:apList){
				Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
				if(a == null){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "限时任务奖励", "物品<font color=red>[" + ap.getArticleCNName() + "]</font>不存在" }));
				}
			}
		}

		Map<Integer, XianLingSkill> skillMap = xm.skillMap;
		for(XianLingSkill skill:skillMap.values()){
			if(!skill.getArticleCNName().equals("null")){
				Article a = ArticleManager.getInstance().getArticleByCNname(skill.getArticleCNName());
				if(a == null){
					mailList.add(new SendHtmlToMail(titles, new String[] { "仙灵", "捕捉技能", skill.getName() + "对应的物品<font color=red>[" + skill.getArticleCNName() + "]</font>不存在" }));
				}
			}
		}
		
		
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
