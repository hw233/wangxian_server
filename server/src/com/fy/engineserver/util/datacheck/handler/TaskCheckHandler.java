package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskGivenArticle;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.targets.TaskTargetOfCollection;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.TaskCollectionNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class TaskCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "任务检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "taskTemplet.xls" };
	}

	/**
	 * 任务|分类|描述
	 * 任务名|NPC|不存在
	 * 任务名|击杀怪物|不存在
	 * 任务名|给与物品|不存在
	 * 任务名|奖励物品|不存在
	 * 任务名|地图|不存在
	 * 任务名|区域|不存在
	 */
	CompoundReturn cr = CompoundReturn.create();
	String[] titles = new String[] { "任务统计名", "任务", "分类", "描述" };
	List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();
	TaskManager tManager = TaskManager.getInstance();

	@Override
	public CompoundReturn getCheckResult() {

		HashMap<Long, Task> taskIdMap = tManager.getTaskIdMap();
		Collection<Task> taskCollection = taskIdMap.values();
		Iterator<Task> taskIterator = taskCollection.iterator();
		while (taskIterator.hasNext()) {
			Task task = taskIterator.next();
			checkNPC(task);
			if (task.getId() != 100000) {
				checkKillMonster(task);
			}
			checkGivenArticle(task);
			checkPrize(task);
			checkExcess(task);
			// checkMap(task);
			checkArea(task);
			checkCollectionTarget(task);
		}
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}

	// 检查NPC是否存在
	public void checkNPC(Task task) {
		String startNpc = task.getStartNpc();
		if (null != startNpc && !"".equals(startNpc)) {
			NPCTempalte startNpcTemp = tManager.getNpcManager().getNPCTempalteByCategoryName(startNpc);
			if (startNpcTemp == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "NPC", "开始NPC<font color=red>[" + startNpc + "]</font>不存在" }));
			}
		}
		String endNpc = task.getEndNpc();
		if (null != endNpc && !"".equals(endNpc)) {
			NPCTempalte endNpcTemp = tManager.getNpcManager().getNPCTempalteByCategoryName(endNpc);
			if (endNpcTemp == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "NPC", "结束NPC<font color=red>[" + endNpc + "]</font>不存在" }));
			}
		}
	}

	// 检查击杀怪物是否存在
	public void checkKillMonster(Task task) {
		// TaskTarget[] targets = task.getTargets();
		MemoryMonsterManager mmm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
		for (TaskTarget target : task.getTargets()) {
			if (target.getTargetByteType() == 1) {
				for (String monsterName : target.getTargetName()) {
					Monster monster = mmm.getMonster(monsterName);
					if (monster == null) {
						mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "要击杀怪物", "怪物<font color=red>[" + monsterName + "]</font>不存在" }));
					}
				}
			}
		}
	}

	// 检查接任务给与的物品是否存在
	public void checkGivenArticle(Task task) {
		TaskGivenArticle tGivenArticle = task.getGivenArticle();
		if (tGivenArticle != null) {
			ArticleManager am = ArticleManager.getInstance();
			for (String articleName : tGivenArticle.getNames()) {
				Article givenArticle = am.getArticle(articleName);
				if (givenArticle == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "给予物品", "给予物品<font color=red>[" + articleName + "]</font>不存在" }));
				}
			}
		}
	}

	// 检查任务奖励的物品是否存在
	public void checkPrize(Task task) {
		TaskPrize[] taskPrizes = task.getPrizes();
		for (int i = 0; i < taskPrizes.length; i++) {
			if (taskPrizes[i] instanceof TaskPrizeOfArticle) {
				String[] prizeNames = taskPrizes[i].getPrizeName();
				for (int j = 0; j < prizeNames.length; j++) {
					ArticleManager am = ArticleManager.getInstance();
					Article taskPrize = am.getArticle(prizeNames[j]);
					if (taskPrize == null) {
						mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "奖励物品", "奖励物品<font color=red>[" + prizeNames[j] + "]</font>不存在" }));
					}
				}
			}
		}
	}

	// 如果有额外奖励，检查额外奖励物品是否存在
	public void checkExcess(Task task) {
		String excessName = task.getExcessTarget();
		if (excessName != null) {
			ArticleManager am = ArticleManager.getInstance();
			Article excessTaskName = am.getArticle(excessName);
			if (excessTaskName == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "额外奖励物品", "额外奖励物品<font color=red>[" + excessName + "]</font>不存在" }));
			}
		}
	}

	// 检查地图是否存在
	public void checkMap(Task task) {
		String startMap = task.getStartMap();
		if (null != startMap && !"".equals(startMap)) {
			Game game = GameManager.getInstance().getGameByName(startMap, CountryManager.国家A);
			if (game == null) {
				game = GameManager.getInstance().getGameByDisplayName(startMap, CountryManager.中立);
			}
			if (game == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "开始地图", "开始地图<font color=red>[" + startMap + "]</font>不存在" }));
			}
		}

		String endMap = task.getEndMap();
		if (null != endMap && !"".equals(endMap)) {
			Game game = GameManager.getInstance().getGameByName(endMap, CountryManager.国家A);
			if (game == null) {
				game = GameManager.getInstance().getGameByDisplayName(endMap, CountryManager.中立);
			}
			if (game == null) {
				mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "结束地图", "结束地图<font color=red>[" + startMap + "]</font>不存在" }));
			}
		}
	}

	// 检查区域是否存在
	public void checkArea(Task task) {
		for (TaskTarget target : task.getTargets()) {
			if (target.getTargetByteType() == 8) {
				String[] targetArea = target.getTargetName();
				String[] mapName = target.getMapName();
				for (int i = 0; i < mapName.length; i++) {
					Game game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.国家A);
					if (game == null) {
						game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.中立);
					}
					if (game != null) {
						MapArea ma = game.gi.getMapAreaByName(targetArea[i]);
						if (ma == null) {
							mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "区域", "区域<font color=red>[" + targetArea[i] + "]</font>不存在" }));
						}
					} else {
						mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName() + "(" + task.getGrade() + "级)", "地图", "地图<font color=red>[" + mapName[i] + "]</font>不存在" }));
					}
				}

			}
		}
	}

	// 检查有采集目标的任务
	public void checkCollectionTarget(Task task) {
		for (TaskTarget target : task.getTargets()) {
			if (target.getTargetByteType() == 14) {
				String[] targetName = ((TaskTargetOfCollection) target).getTargetName();
				LinkedHashMap<Integer, NPCTempalte> templates = ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).getTemplates();
				if (templates != null) {
					for (int id : templates.keySet()) {
						NPCTempalte nt = templates.get(id);
						if (targetName[0].equals(nt.npc.getName())) {
							if (nt.npc instanceof TaskCollectionNPC) if (!(((TaskCollectionNPC) nt.npc).getTaskNames().contains(task.getName()))) {
								mailList.add(new SendHtmlToMail(titles, new String[] { task.getName_stat(), task.getName(), "采集目标:" + targetName[0] + "(npc关联任务中没有该任务)", "与npc表关联有误" }));
							}
						}
					}
				}
			}
		}
	}

}
