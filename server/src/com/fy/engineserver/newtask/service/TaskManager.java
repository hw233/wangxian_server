package com.fy.engineserver.newtask.service;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureNpc;
import com.fy.engineserver.activity.pickFlower.FlowerNpc;
import com.fy.engineserver.constants.GameConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.NPCFlushAgent;
import com.fy.engineserver.core.NPCFlushAgent.BornPoint;
import com.fy.engineserver.core.client.FunctionNPC;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.FUNCTION_NPC_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newtask.ActivityTaskExp;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.TaskGivenArticle;
import com.fy.engineserver.newtask.TaskGivenBuff;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfBindSilver;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfBuff;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfCareer;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfClassLevel;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfContribute;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfCountryRes;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfExp;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfGongxun;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfLilian;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfPneuma;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfPrestige;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfRandomArticle;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfSkillPoint;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfTitle;
import com.fy.engineserver.newtask.targets.RandomNum;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.targets.TaskTargetOfBuff;
import com.fy.engineserver.newtask.targets.TaskTargetOfCaveBuild;
import com.fy.engineserver.newtask.targets.TaskTargetOfCaveHarvest;
import com.fy.engineserver.newtask.targets.TaskTargetOfCavePlant;
import com.fy.engineserver.newtask.targets.TaskTargetOfCaveSteal;
import com.fy.engineserver.newtask.targets.TaskTargetOfCollection;
import com.fy.engineserver.newtask.targets.TaskTargetOfConvoyNPC;
import com.fy.engineserver.newtask.targets.TaskTargetOfDiscovery;
import com.fy.engineserver.newtask.targets.TaskTargetOfGetArticle;
import com.fy.engineserver.newtask.targets.TaskTargetOfGetArticleAndDelete;
import com.fy.engineserver.newtask.targets.TaskTargetOfKillPlayer;
import com.fy.engineserver.newtask.targets.TaskTargetOfMonster;
import com.fy.engineserver.newtask.targets.TaskTargetOfMonsterLv;
import com.fy.engineserver.newtask.targets.TaskTargetOfMonsterLvNearPlayer;
import com.fy.engineserver.newtask.targets.TaskTargetOfMonsterRandomNum;
import com.fy.engineserver.newtask.targets.TaskTargetOfRelation;
import com.fy.engineserver.newtask.targets.TaskTargetOfTalkToNPC;
import com.fy.engineserver.newtask.targets.TaskTargetOfTaskDeliver;
import com.fy.engineserver.newtask.targets.TaskTargetOfUseArticle;
import com.fy.engineserver.newtask.timelimit.DeliverTimeLimit;
import com.fy.engineserver.newtask.timelimit.TimeLimitFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.FollowableNPC;
import com.fy.engineserver.sprite.npc.FrunaceNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.SeedNpc;
import com.fy.engineserver.sprite.npc.TaskCollectionNPC;
import com.fy.engineserver.sprite.npc.ZhongQiuNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class TaskManager implements TaskConfig, MConsole {

	private static TaskManager instance;

	public static SimpleEntityManager<DeliverTask> deliverTaskEm;

	private String file = "";

	/** <任务组名字,后续任务> */
	public HashMap<String, List<Task>> nextTaskMap = new HashMap<String, List<Task>>();
	/** 任务组列表 <组名字,任务列表> */
	public HashMap<String, List<Task>> taskGrouopMap = new HashMap<String, List<Task>>();
	/** 任务集合列表<集合名[非空],任务列表> */
	public HashMap<String, List<Task>> taskCollections = new HashMap<String, List<Task>>();
	/** 任务大集合列表<集合名[非空],任务列表> */
	public HashMap<String, List<Task>> taskBigCollections = new HashMap<String, List<Task>>();
	/** 任务组列表 <任务名,任务> */
	//应该只用来判断任务重名
	public HashMap<String, Task> taskNameMap = new HashMap<String, Task>();
	public HashMap<String, HashMap<Integer,Task>> taskNameMapOfCountry = new HashMap<String, HashMap<Integer,Task>>();
	/** 任务ID列表<ID,任务> */
	public HashMap<Long, Task> taskIdMap = new HashMap<Long, Task>();
	/** <gameName,<NPC名字,任务列表>> */
	public HashMap<String, HashMap<String, List<Task>>> gameTasks = new HashMap<String, HashMap<String, List<Task>>>();
	/** <gameName,<NPC名字,任务列表>> 发布和取消发布任务时更改此列表 */
	public HashMap<Game, HashMap<String, List<Task>>> gameReleaseTasks = new HashMap<Game, HashMap<String, List<Task>>>();
	/** 所有线任务的第一个 [做可接列表使用] */
	public List<Task> fristTasksOfAlLine = new ArrayList<Task>();
	/** 活动类任务经验奖励<奖励的ID,奖励> */
	public HashMap<Integer, ActivityTaskExp> activityPrizeMap = new HashMap<Integer, ActivityTaskExp>();
	/** 开启体力值的任务 */
	public String openThewTaskName;

	@ChangeAble("第1个测试数据")
	public String 测试_a = "测试数据A";
	@ChangeAble("第2个测试数据")
	public String 测试_b = "测试数据B";
	@ChangeAble("第3个测试数据")
	public String 测试_c = "测试数据C";
	@ChangeAble("开服时间")
	public int 测试_d = 4;
	@ChangeAble("结束时间")
	public long 测试_e = 5;
	@ChangeAble("第6个测试数据")
	public double 测试_f = 6.6;
	@ChangeAble("第7个测试数据")
	public byte 测试_g = 6;
	@ChangeAble("整理背包间隔")
	public long autoKnapsackSpaceTime = 10*1000L;

	public static Logger logger = LoggerFactory.getLogger(TaskManager.class);

	static DecimalFormat df = new DecimalFormat("##.00%");

	public static long currentLoadId;

	private MemoryNPCManager npcManager;
	private GameManager gameManager;

	private ArticleEntityManager entityManager;
	private ArticleManager articleManager;

	public static StringBuffer notices = new StringBuffer("任务加载器:<BR/>");
	/** 主线需要断开的列表 */
	public static List<String> disconnectionMainTask = new ArrayList<String>();

	public static TaskManager getInstance() {
		return instance;
	}

	public static Random RANDOM = new Random();
	//
	// /** 所有角色完成的单次任务 需要定时保存 */
	// private ArrayList<DeliverTask> allPlayerCurrDeliverTasks = new ArrayList<DeliverTask>();
	/** 执行 */
	private boolean running = true;
	/** 每分钟执行多少次 */
	private long FPS = 1;
	/** 最后一次执行时间 */
	private long lastRunTime = 0;

	/**
	 * 将任务加载到任务集合
	 * @param task
	 */
	private void addToCollections(Task task) {
		checkTaskGivenArticleValid(task.getGivenArticle());
		{ // 检查NPC合法性
			if (task.getStartMap() != null && !task.getStartMap().isEmpty() && task.getStartNpc() != null && !task.getStartNpc().isEmpty()) {
				CompoundReturn cr = getNPCInfoByGameAndName(task.getStartMap(), task.getStartNpc());
				if (cr.getBooleanValue()) {
					task.setStartX(cr.getIntValues()[0]);
					task.setStartY(cr.getIntValues()[1]);
					task.setStartNPCAvataRace(cr.getStringValues()[0]);
					task.setStartNPCAvataSex(cr.getStringValues()[1]);
				} else {
					if (logger.isInfoEnabled()) logger.info("[任务开始NPC不存在][taskName:{}][开始地图:{}][开始NPC:{}]地图数量:{}", 
							new Object[] { task.getName(), task.getStartMap(), task.getStartNpc(), getGameManager().getGames().length });
				}
			}
			if (task.getEndMap() != null && !task.getEndMap().isEmpty() && task.getEndNpc() != null && !task.getEndNpc().isEmpty()) {
				CompoundReturn cr = getNPCInfoByGameAndName(task.getEndMap(), task.getEndNpc());
				if (cr.getBooleanValue()) {
					task.setEndX(cr.getIntValues()[0]);
					task.setEndY(cr.getIntValues()[1]);
					task.setEndNPCAvataRace(cr.getStringValues()[0]);
					task.setEndNPCAvataSex(cr.getStringValues()[1]);
				} else {
					logger.error("[任务结束NPC不存在][taskName:{}][结束地图:{}][结束NPC:{}]地图数量:{}", new Object[] { task.getName(), task.getEndMap(), task.getEndNpc(), getGameManager().getGames().length });
				}
			}
		}

		if (!taskGrouopMap.containsKey(task.getGroupName())) {
			taskGrouopMap.put(task.getGroupName(), new ArrayList<Task>());
		}
		taskGrouopMap.get(task.getGroupName()).add(task);

		if (taskNameMap.containsKey(task.getName())) {
//			logger.error("发现重名任务:{}", new Object[] { task.getName() });
//			notices.append("<font color=red>发现重名任务[").append(task.getName()).append("]</font><BR/>");
		} else {
			taskNameMap.put(task.getName(), task);
		}
		
		HashMap<Integer, Task> ts = taskNameMapOfCountry.get(task.getName());
		if(ts == null){
			ts = new HashMap<Integer, Task>();
		}
		if (ts.containsKey(new Integer(task.getCountryLimit()))) {
//			logger.error("发现重名任务:{}", new Object[] { task.getName() });
			notices.append("<font color=red>发现重名任务[").append(task.getName()).append("]</font><BR/>");
		} else {
			ts.put(task.getCountryLimit(), task);
		}
		taskNameMapOfCountry.put(task.getName(), ts);

		if (taskIdMap.containsKey(task.getId())) {
			logger.warn("发现重ID任务:{}][ID={}]", new Object[] { task.getName(), task.getId() });
			notices.append("<font color=red>发现重ID任务[").append(task.getId()).append("]</font><BR/>");
		} else {
			taskIdMap.put(task.getId(), task);
		}

		if (!gameTasks.containsKey(task.getStartMap())) {
			gameTasks.put(task.getStartMap(), new HashMap<String, List<Task>>());
		}
		if (!gameTasks.get(task.getStartMap()).containsKey(task.getStartNpc())) {
			gameTasks.get(task.getStartMap()).put(task.getStartNpc(), new ArrayList<Task>());
		}
		gameTasks.get(task.getStartMap()).get(task.getStartNpc()).add(task);
		// 加载到后续任务列表
		String groupName = task.getFrontGroupName();
		if (!nextTaskMap.containsKey(groupName)) {
			nextTaskMap.put(groupName, new ArrayList<Task>());
		}
		nextTaskMap.get(groupName).add(task);

		// 加载集合任务
		if (task.getCollections() != null && !task.getCollections().isEmpty()) {
			if (!taskCollections.containsKey(task.getCollections())) {
				taskCollections.put(task.getCollections(), new ArrayList<Task>());
			}
			taskCollections.get(task.getCollections()).add(task);
		}
		// 加载大集合任务
		if (task.getBigCollection() != null && !task.getBigCollection().isEmpty()) {
			if (!taskBigCollections.containsKey(task.getBigCollection())) {
				taskBigCollections.put(task.getBigCollection(), new ArrayList<Task>());
			}
			taskBigCollections.get(task.getBigCollection()).add(task);
		}
		// 加载所有线的第一个任务
		// 加载主线，支线 且没有前置任务的
		if ((task.getShowType() == TASK_SHOW_TYPE_MAIN || task.getShowType() == TASK_SHOW_TYPE_SUB) && (task.getFrontGroupName() == null || task.getFrontGroupName().isEmpty())) {
			fristTasksOfAlLine.add(task);
		}
	}

	/**
	 * 通过地图名和NPC名获得NPC的信息<BR/>
	 * 返回false则表示没取到<BR/>
	 * int[2]{x,y}<BR/>
	 * String[2]{AvataRace,AvataSex}<BR/>
	 * @param gameName
	 * @param NPCName
	 * @return
	 */
	public CompoundReturn getNPCInfoByGameAndName(String gameName, String NPCName) {
		Game game = getGameManager().getGameByDisplayName(gameName, CountryManager.国家A);
		if (game == null) {
			game = getGameManager().getGameByDisplayName(gameName, CountryManager.中立);
		}
		if (game != null) {
			NPCFlushAgent flushAgent = game.getNpcFlushAgent();
			if (flushAgent != null) {
				NPCTempalte tempalte = getNpcManager().getNPCTempalteByCategoryName(NPCName);
				if (tempalte != null) {
					BornPoint[] bornPoints = flushAgent.getBornPoints4NpcCategoryId(tempalte.NPCCategoryId);
					String[] avata = new String[2];
					if (tempalte != null && tempalte.npc != null) {
						avata[0] = tempalte.npc.getAvataRace();
						avata[1] = tempalte.npc.getAvataSex();
						if (bornPoints != null && bornPoints.length > 0) {
							return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValues(new int[] { bornPoints[0].getX(), bornPoints[0].getY() }).setStringValues(avata);
						}
					}
				}
			}
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc存在][地图:{}npc:{}][任务ID:{}]", new Object[] { gameName, NPCName, currentLoadId });
		} else {
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc不存在][地图:{}npc:{}][任务ID:{}]", new Object[] { gameName, NPCName, currentLoadId });
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}
	
	public CompoundReturn getNPCInfoByGameAndNameByCountry(String gameName, String NPCName,int country) {
		Game game = getGameManager().getGameByDisplayName(gameName, country);
		if (game == null) {
			game = getGameManager().getGameByDisplayName(gameName, CountryManager.中立);
		}
		if (game != null) {
			NPCFlushAgent flushAgent = game.getNpcFlushAgent();
			if (flushAgent != null) {
				NPCTempalte tempalte = getNpcManager().getNPCTempalteByCategoryName(NPCName);
				if (tempalte != null) {
					BornPoint[] bornPoints = flushAgent.getBornPoints4NpcCategoryId(tempalte.NPCCategoryId);
					String[] avata = new String[2];
					if (tempalte != null && tempalte.npc != null) {
						avata[0] = tempalte.npc.getAvataRace();
						avata[1] = tempalte.npc.getAvataSex();
						if (bornPoints != null && bornPoints.length > 0) {
							return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValues(new int[] { bornPoints[0].getX(), bornPoints[0].getY() }).setStringValues(avata);
						}
					}
				}
			}
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc存在2] [地图:{}npc:{}] [任务ID:{}] [country:{}]", new Object[] { gameName, NPCName, currentLoadId,country });
		} else {
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc不存在2] [地图:{}npc:{}] [ 任务ID:{}] [country:{}]", new Object[] { gameName, NPCName, currentLoadId,country });
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}
	
	
	public CompoundReturn getNPCInfoByGameAndName(String gameName, String NPCName,Player p) {
		Game game = getGameManager().getGameByDisplayName(gameName, CountryManager.国家A);
		if (game == null) {
			game = getGameManager().getGameByDisplayName(gameName, CountryManager.中立);
		}
		if(game==null){
			game = GameManager.getInstance().getGameByDisplayName(gameName, p.getCountry());
		}
		if (game != null) {
			NPCFlushAgent flushAgent = game.getNpcFlushAgent();
			int tempId = 0;
			int bornPointLength = 0;
			int bornPointLength2 = 0;
			if (flushAgent != null) {
				NPCTempalte tempalte = getNpcManager().getNPCTempalteByCategoryName(NPCName);
				if (tempalte != null) {
					tempId = tempalte.NPCCategoryId;
					BornPoint[] bornPoints = flushAgent.getBornPoints4NpcCategoryId(tempalte.NPCCategoryId);
					String[] avata = new String[2];
					if (tempalte != null && tempalte.npc != null) {
						avata[0] = tempalte.npc.getAvataRace();
						avata[1] = tempalte.npc.getAvataSex();
						if (bornPoints != null && bornPoints.length > 0) {
							bornPointLength = bornPoints.length;
							return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValues(new int[] { bornPoints[0].getX(), bornPoints[0].getY() }).setStringValues(avata).setObjValue(game);
						}
					}
				}
			}
			MemoryMonsterManager mmm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
			Monster monster = mmm.getMonster(NPCName);
			if(monster!=null){
				tempId = monster.getSpriteCategoryId();
				com.fy.engineserver.core.MonsterFlushAgent.BornPoint[] bornPoints2 = game.getSpriteFlushAgent().getBornPoints4SpriteCategoryId(monster.getSpriteCategoryId());
				if (bornPoints2 != null && bornPoints2.length > 0) {
					bornPointLength2 = bornPoints2.length;
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValues(new int[] { bornPoints2[0].getX(), bornPoints2[0].getY() }).setObjValue(game);
				}
			}
			
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc存在][地图:{}npc:{}][任务ID:{}] [tempId:{}] [bornPointLength:{}] [bornPointLength2:{}]", new Object[] { gameName, NPCName, currentLoadId ,tempId,bornPointLength,bornPointLength2});
		} else {
			if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[地图上的npc不存在][地图:{}npc:{}][任务ID:{}]", new Object[] { gameName, NPCName, currentLoadId });
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	/**
	 * 通过ID获得任务
	 * @param taskId
	 * @return
	 */
	public Task getTask(long taskId) {
		return taskIdMap.get(taskId);
	}

	/**
	 * 通过NAME获得任务
	 * @param name
	 * @return
	 */
	public Task getTask(String name) {
		return taskNameMap.get(name);
	}
	
	public Task getTask(String name,int country) {
		if(taskNameMapOfCountry.get(name) != null){
			if(taskNameMapOfCountry.get(name).get(new Integer(country)) != null){
				return taskNameMapOfCountry.get(name).get(new Integer(country));
			}
		}
		return taskNameMap.get(name);
	}

	/**
	 * 通过组名获得该组任务
	 * @param groupName
	 * @return
	 */
	public List<Task> getTaskGroupByGroupName(String groupName) {
		return taskGrouopMap.get(groupName);
	}

	/**
	 * 通过集合名获得任务集合
	 * @param groupName
	 * @return
	 */
	public List<Task> getTaskCollectionsByName(String collectionName) {
		return taskCollections.get(collectionName);
	}

	/**
	 * 国家匹配
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean countryFit(Player player, Task task) {
		return task.getCountryLimit() == COUNTRY_NON ? true : task.getCountryLimit() == player.getCountry();
	}

	/**
	 * 职业匹配
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean workFit(Player player, Task task) {
		if(task.getWorkLimits() != null && task.getWorkLimits().length > 0){
			for(int career : task.getWorkLimits()){
				if(career == player.getMainCareer()){
					return true;
				}
			}
		}	
		return task.getWorkLimit() == WORK_NON ? true : task.getWorkLimit() == player.getMainCareer();
	}

	/**
	 * 性别是否符合
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean sexFit(Player player, Task task) {
		return task.getSexLimit() == SEX_NON ? true : task.getSexLimit() == player.getSex();
	}

	/**
	 * 等级是否符合
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean gradeFit(Player player, Task task) {
		int fitMin = task.getMinGradeLimit() <= -1 ? player.getLevel() : task.getMinGradeLimit();
		int fitMax = task.getMaxGradeLimit() <= -1 ? player.getLevel() : task.getMaxGradeLimit();
		return player.getLevel() >= fitMin && player.getLevel() <= fitMax;
	}

	/**
	 * 国家职务符合
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean CountryOfficialFit(Player player, Task task) {
		// TODO
		// return task.getCountryOfficialLimit() == -1 ? true : player.get国家职务 == task.getCountryOfficialLimit();
		return true;
	}

	/**
	 * 家族职务符合[绝对匹配]
	 * @param player
	 * @param task
	 * @param npc
	 * @return
	 */
	public static boolean septOfficialFit(Player player, Task task, FunctionNPC npc) {
		if (task.getSeptOfficialLimit() == -1) {
			return true;
		}
		if (player.getJiazuId() <= 0) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.JIAZU_GOT, task);
			}
			return false;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.JIAZU_GOT, task);
			}
			return false;
		}
		JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jiazuMember == null) {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.JIAZU_GOT, task);
			}
			return false;
		}
		if (task.getSeptOfficialLimit() == jiazuMember.getTitle().ordinal()) {
			return true;
		} else {
			if (npc != null) {
				npc.addTask2Wait(ModifyType.JIAZU_TITLE_MODIFY, task);
			}
			return false;
		}
	}

	/**
	 * 境界是否满足接取条件
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean bournFit(Player player, Task task) {
		return player.getClassLevel() >= task.getBourn();
	}

	/**
	 * 封印状态是否满足接取条件
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean sealFit(Player player, Task task) {
		return player.getSealState() ? true : task.getSealLimit() == -1;
	}

	/**
	 * 元神等级是否符合
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean soulLevelFit(Player player, Task task) {
		if (task.getSoulLevelLimit() == -1) {
			return true;
		}
		Soul soul = player.getSoul(Soul.SOUL_TYPE_SOUL);
		if (soul == null) {
			return false;
		}
		return soul.getGrade() >= task.getSoulLevelLimit();
	}

	/**
	 * 家族是否符合
	 * @param player
	 * @param task
	 * @return
	 */
	public static boolean septFit(Player player, Task task) {
		if (task.getSeptLimit() == -1) {
			return true;
		}
		// TODO
		return true;
	}

	public static boolean socialRelationsFit(Player player, Task task) {
		if (task.getSocialRelationsLimit() == -1) {
			return true;
		}
		// TODO
		return false;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public HashMap<String, List<Task>> getTaskGrouopMap() {
		return taskGrouopMap;
	}

	public void setTaskGrouopMap(HashMap<String, List<Task>> taskGrouopMap) {
		this.taskGrouopMap = taskGrouopMap;
	}

	public HashMap<Long, Task> getTaskIdMap() {
		return taskIdMap;
	}

	public void setTaskIdMap(HashMap<Long, Task> taskIdMap) {
		this.taskIdMap = taskIdMap;
	}

	public HashMap<String, List<Task>> getNextTaskMap() {
		return nextTaskMap;
	}

	public void setNextTaskMap(HashMap<String, List<Task>> nextTaskMap) {
		this.nextTaskMap = nextTaskMap;
	}

	//
	// public ArrayList<DeliverTask> getAllPlayerCurrDeliverTasks() {
	// return allPlayerCurrDeliverTasks;
	// }
	//
	// public void setAllPlayerCurrDeliverTasks(ArrayList<DeliverTask> allPlayerCurrDeliverTasks) {
	// this.allPlayerCurrDeliverTasks = allPlayerCurrDeliverTasks;
	// }

	public HashMap<String, Task> getTaskNameMap() {
		return taskNameMap;
	}

	public void setTaskNameMap(HashMap<String, Task> taskNameMap) {
		this.taskNameMap = taskNameMap;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	/**
	 * 得到该任务的后续任务
	 * @param groupName
	 * @return
	 */
	public List<com.fy.engineserver.newtask.Task> getnextTask(String groupName) {
		return getNextTaskMap().get(groupName);
	}

	/**
	 * 获得某个玩家当前地图上各个NPC可接的任务
	 * @param game
	 * @param player
	 * @return
	 */
	public FunctionNPC[] getFunctionNPCsByGame(Game game, Player player) {
		player.getCurrMapFunctuinNPC().clear();// 清除和角色相关的NPC
		ArrayList<FunctionNPC> al = new ArrayList<FunctionNPC>();
		if (game == null) {
			logger.error("查询地图功能NPC 地图不存在");
			return al.toArray(new FunctionNPC[0]);
		}
		LivingObject los[] = game.getLivingObjects();
		try {
			for (int i = 0; i < los.length; i++) {
				if (los[i] instanceof NPC) {
					NPC npc = (NPC) los[i];
					if (npc.getCountry() == GameConstant.中立阵营 || npc.getCountry() == player.getCountry()) {
						FunctionNPC fn = new FunctionNPC(game, npc);

						if (fn.functionType < Sprite.NPC_SHORT_NAMES.length) {
							fn.setAvaiableTaskIds(getCanAcceptTaskIds(fn, player));

							byte taskTypes[] = new byte[fn.getAvaiableTaskIds().length];
							String[] taskNames = new String[fn.getAvaiableTaskIds().length];
							int[] grades = new int[fn.getAvaiableTaskIds().length];
							for (int j = 0; j < taskTypes.length; j++) {
								Task task = getTask(fn.getAvaiableTaskIds()[j]);
								if (task != null) {
									taskTypes[j] = task.getShowType();
									taskNames[j] = task.getName();
									grades[j] = task.getGrade();
								}
							}
							fn.setAvaiableTaskGrades(grades);
							fn.setAvaiableTaskNames(taskNames);
							fn.setAvaiableTaskTypes(taskTypes);
							// 将NPC加到人身上 -- 临时变量
							player.getCurrMapFunctuinNPC().add(fn);
							al.add(fn);
						}
					}
				}
			}
			// logger.info("角色[" + player.getName() + "]进入地图 [" + game.getGameInfo().name + "]功能NPC个数:" + player.getCurrMapFunctuinNPC().size());
			if (logger.isInfoEnabled()) logger.info("角色[{}]进入地图 [{}]功能NPC个数:{}", new Object[] { player.getName(), game.getGameInfo().name, player.getCurrMapFunctuinNPC().size() });
		} catch (Exception e) {
			logger.error(player.getLogString() + "进入地图查询NPCERROR :", e);
		}
		return al.toArray(new FunctionNPC[0]);

	}

	/**
	 * 获得某个玩家当前地图上各个NPC可接的任务
	 * @param game
	 * @param player
	 * @return
	 */
	public FunctionNPC[] 只用于世界地图的npc查询(Game game, Player player) {
		// player.getCurrMapFunctuinNPC().clear();// 清除和角色相关的NPC
		ArrayList<FunctionNPC> al = new ArrayList<FunctionNPC>();
		if (game == null) {
			logger.error("查询地图功能NPC 地图不存在");
			return al.toArray(new FunctionNPC[0]);
		}
		LivingObject los[] = game.getLivingObjects();
		boolean dongfu = false;
		if (FaeryConfig.GAME_NAME.equals(game.gi.getName())) {
			dongfu = true;
		}
		try {
			for (int i = 0; i < los.length; i++) {
				if (dongfu) {
					if (!(los[i] instanceof CaveDoorNPC) && (los[i] instanceof CaveNPC)) {
						continue;
					}
				}
				// 过滤掉镖车和护送NPC
				if (los[i] instanceof FollowableNPC) {
					continue;
				}
				// 过滤采花npc
				if (los[i] instanceof FlowerNpc) {
					continue;
				}
				// 过滤仙界宝箱npc
				if (los[i] instanceof FairylandTreasureNpc) {
					continue;
				}
				if (los[i] instanceof NPC) {
					NPC npc = (NPC) los[i];

					// 去除灯谜npc
					if (npc instanceof ZhongQiuNpc) {
						continue;
					}
					if (npc instanceof FlopCaijiNpc) {
						continue;
					}
					if (npc instanceof TaskCollectionNPC) {
						continue;
					}
					// 只能看见自己种植的npc
					if (npc instanceof SeedNpc) {
						SeedNpc sn = (SeedNpc) npc;
						if (sn.getPlantPlayerId() != player.getId()) {
							continue;
						}
					}
					if (npc.getCountry() == GameConstant.中立阵营 || npc.getCountry() == player.getCountry()) {
						FunctionNPC fn = new FunctionNPC(game, npc);

						if (fn.functionType < Sprite.NPC_SHORT_NAMES.length || npc instanceof FrunaceNPC) {
							fn.setAvaiableTaskIds(getCanAcceptTaskIds(fn, player));

							byte taskTypes[] = new byte[fn.getAvaiableTaskIds().length];
							String[] taskNames = new String[fn.getAvaiableTaskIds().length];
							int[] grades = new int[fn.getAvaiableTaskIds().length];
							for (int j = 0; j < taskTypes.length; j++) {
								Task task = getTask(fn.getAvaiableTaskIds()[j]);
								if (task != null) {
									taskTypes[j] = task.getShowType();
									taskNames[j] = task.getName();
									grades[j] = task.getGrade();
								}
							}
							fn.setAvaiableTaskGrades(grades);
							fn.setAvaiableTaskNames(taskNames);
							fn.setAvaiableTaskTypes(taskTypes);
							// 将NPC加到人身上 -- 临时变量
							// player.getCurrMapFunctuinNPC().add(fn);
							al.add(fn);
						}
					}
				}
			}
			// logger.info("角色[" + player.getName() + "]进入地图 [" + game.getGameInfo().name + "]功能NPC个数:" + player.getCurrMapFunctuinNPC().size());
			// if (logger.isInfoEnabled()) logger.info("角色[{}]进入地图 [{}]功能NPC个数:{}", new Object[] { player.getName(), game.getGameInfo().name, player.getCurrMapFunctuinNPC().size()
			// });
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		Collections.sort(al, orderByName);
		return al.toArray(new FunctionNPC[0]);

	}

	Comparator<FunctionNPC> orderByName = new Comparator<FunctionNPC>() {

		@Override
		public int compare(FunctionNPC o1, FunctionNPC o2) {
			int length1 = o1.getName() == null ? 0 : o1.getName().length();
			int length2 = o2.getName() == null ? 0 : o2.getName().length();
			return length1 - length2;
		}
	};

	public boolean testLogger = false;
	
	/**
	 * 得到玩家在NPC身上可接任务
	 * @param npc
	 * @param player
	 * @return
	 */
	public long[] getCanAcceptTaskIds(FunctionNPC npc, Player player) {
		List<Task> tasks = getTasksByNPC(npc, player);
		List<Long> returns = new ArrayList<Long>();
		if (tasks != null && !tasks.isEmpty()) {
			for (Task task : tasks) {
				if (task == null) {
					continue;
				}
				CompoundReturn cr = player.takeOneTask(task, false, npc);
				if (cr.getIntValue() == 0) {
					returns.add(task.getId());
				}else{
					if(testLogger){
						logger.warn("[npc:"+npc.getName()+"] [任务id:"+task.getId()+"] [任务:"+task.getName()+"] [结果:"+cr.getBooleanValue()+"] [原因:"+cr.getIntValue()+"] [角色:"+player.getName()+"]");
					}
				}
			}
		}
		long[] rs = new long[returns.size()];
		for (int i = 0; i < returns.size(); i++) {
			rs[i] = returns.get(i);
			Task task = getTask(rs[i]);
			if (task != null) {
				// buffer.append(task.getName()).append("[id=" + returns.get(i) + "]");
			} else {
				// buffer.append("[任务不存在>>>> id=" + returns.get(i) + "]");
			}
		}
		// logger.info(buffer.toString());
		return rs;
	}

	/**
	 * 得到NPC身上的任务
	 * @param npc
	 * @return
	 */
	private List<Task> getTasksByNPC(FunctionNPC npc, Player player) {
		List<Task> returns = new ArrayList<Task>();
		// 先查询普通任务
		if (gameTasks.get(npc.mapName) != null) {
			String name = npc.getName();
			if (name.indexOf(CaveBuilding.nameseparator) != -1 && name.indexOf(player.getName()) != -1) {
				name = name.substring(name.indexOf(CaveBuilding.nameseparator) + 1);
				logger.info("[修改后的NPC名字]" + name + "[npc类型:" + npc.getFunctionType() + "]");
			}
			if (gameTasks.get(npc.mapName).get(name) != null) {
				returns.addAll(gameTasks.get(npc.mapName).get(name));
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(npc.toString() + "[NPC没有任何任务]" + name);
				}
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(npc.toString() + "[地图没有任何任务]" + npc.mapName);
			}
		}
		// 再查询发布任务
		if (gameReleaseTasks.get(npc.getCurrentGame()) != null) {
			if (gameReleaseTasks.get(npc.getCurrentGame()).get(npc.name) != null) {
				returns.addAll(gameReleaseTasks.get(npc.getCurrentGame()).get(npc.name));
			}
		}
		return returns;
	}

	private int[] StringToInt(String[] a){
		if(a != null){
			int [] fs = new int[a.length];
			for(int k=0;k<a.length;k++){
				if(a[k] != null){
					fs[k] = Integer.parseInt(a[k]);
				}
			}
			return fs;
		}
		return null;
	}
	
	public void loadFilePoi() throws Exception {
		XSSFWorkbook workbook = null;
		try {
			if (logger.isInfoEnabled()) logger.info("=========================================开始加载任务=========================================");
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			long ID = 0;
			// file
			File file = new File(getFile());
			if (!file.exists()) {
				notices.append("任务配置文件不存在");
				return;
			}
			workbook = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = workbook.getSheetAt(1);
			int maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 3; i < maxRow; i++) {
				XSSFRow row = sheet.getRow(i);
				int cellNum = row.getLastCellNum() - row.getFirstCellNum();// row.getPhysicalNumberOfCells();
				List<Cell> cellList = new ArrayList<Cell>();
				for (int c = 0; c < cellNum; c++) {
					cellList.add(row.getCell(c, Row.RETURN_BLANK_AS_NULL));
				}
				Cell[] cells = cellList.toArray(new Cell[cellList.size()]);
				Task task = new Task();
				int index = 0;
				// 需要翻译的2,8,9,31,32,33,34,35,36,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,81,82,83,84,
				String strId = getCellValue(cells[++index]);
				if (strId == null || strId.isEmpty()) {
					if (logger.isInfoEnabled()) logger.info("ID 异常: 行数[{}]", new Object[] { i });
					notices.append("ID 异常: 行数:").append(i).append("<BR/>");
					continue;
				}
				task.setId(Long.valueOf(modifyContent(strId)));
				ID = task.getId();
				currentLoadId = ID;

				task.setName((getCellValue(cells[++index])));
				task.setName_stat((getCellValue(cells[++index])));
				task.setGroupName((getCellValue(cells[++index])));
				task.setGroupName_stat((getCellValue(cells[++index])));

				// 集合名
				String collectionValue = getCellValue(cells[++index]);
				task.setCollections((collectionValue == null ? "" : collectionValue.trim()));

				collectionValue = getCellValue(cells[++index]);
				task.setCollections_stat((collectionValue == null ? "" : collectionValue.trim()));

				collectionValue = getCellValue(cells[++index]);
				task.setBigCollection((collectionValue == null ? "" : collectionValue.trim()));

				collectionValue = getCellValue(cells[++index]);
				task.setBigCollection_stat((collectionValue == null ? "" : collectionValue.trim()));

				task.setShowType(Byte.valueOf(getCellValue(cells[++index])));
				task.setGrade(Integer.valueOf(getCellValue(cells[++index])));
				task.setDes((getCellValue(cells[++index])));
				task.setUnDeliverTalk((getCellValue(cells[++index])));//
				task.setMinGradeLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setMaxGradeLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setSexLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setCountryLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setThewLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setCountryOfficialLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setBourn(Integer.valueOf(getCellValue(cells[++index])));
				int careerLimitIndex = ++index;
				try{
					int careerLimit = Integer.valueOf(getCellValue(cells[careerLimitIndex]));
					task.setWorkLimit(careerLimit);
				}catch(Exception e){
					String careerLimitStr[] = getCellValue(cells[careerLimitIndex]).split(",");
					if(StringToInt(careerLimitStr) != null){
						task.setWorkLimits(StringToInt(careerLimitStr));
					}
				}
				task.setSeptLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setSeptOfficialLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setSocialRelationsLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setType(Integer.valueOf(getCellValue(cells[++index])));
				// ++index; 第一个，对玩家封印状态的限制
				task.setSealLimit(Integer.valueOf(getCellValue(cells[++index])));
				task.setShowTarget(Byte.valueOf(getCellValue(cells[++index])));
				// 是否是完成自动交付
				task.setAutoDeliver(1 == Integer.valueOf(getCellValue(cells[++index])));
				// 限制元神等级
				String soulLevelLimitStr = getCellValue(cells[++index]);
				if (soulLevelLimitStr == null || "".equals(soulLevelLimitStr)) {
					task.setSoulLevelLimit(-1);
				} else {
					task.setSoulLevelLimit(Integer.valueOf(soulLevelLimitStr));
				}
				String serverAutoAccept = getCellValue(cells[++index]);
				if (serverAutoAccept == null || "".equals(serverAutoAccept)) {
					task.setServerAutoAccept(false);
				} else {
					task.setServerAutoAccept(Integer.valueOf(serverAutoAccept) == 1);
				}
				++index;
				++index;
				++index;
				++index;// 30
				task.setStartNpc((getCellValue(cells[++index])));
				task.setStartMap((getCellValue(cells[++index])));
				task.setStartTalk((getCellValue(cells[++index])));
				task.setEndNpc((getCellValue(cells[++index])));
				task.setEndMap((getCellValue(cells[++index])));
				task.setEndTalk((getCellValue(cells[++index])));// 36
				task.setFrontGroupName((getCellValue(cells[++index])));
				task.setDependType(Integer.valueOf(getCellValue(cells[++index])));
				task.setThewCost(Integer.valueOf(getCellValue(cells[++index])));

				// 接取给予buff
				String givenBuffStr = getCellValue(cells[++index]);
				if (givenBuffStr != null && !givenBuffStr.isEmpty()) {
					TaskGivenBuff givenBuff = getGivenBuff(givenBuffStr, task.getId());
					if (givenBuff != null) {
						task.setGivenBuff(givenBuff);
					}
				}
				// 接取给予物品
				String givenArticleStr = getCellValue(cells[++index]);
				if (givenArticleStr != null && !givenArticleStr.isEmpty()) {
					TaskGivenArticle givenArticle = getGivenArticle(givenArticleStr, task.getId());
					if (givenArticle != null) {
						task.setGivenArticle(givenArticle);
					}
				}

				String timeLimitType = getCellValue(cells[++index]);
				String timeLimitValue = getCellValue(cells[++index]);
				if (timeLimitType != null && !timeLimitType.isEmpty() && timeLimitValue != null && !timeLimitValue.isEmpty()) {
					task.setTimeLimit(TimeLimitFactory.createTimeLimit(Integer.valueOf(timeLimitType), modifyContent(timeLimitValue)));
				}
				String deliverTimeLimitType = getCellValue(cells[++index]);
				String deliverTimeLimitValue = getCellValue(cells[++index]);
				if (deliverTimeLimitType != null && !deliverTimeLimitType.isEmpty() && deliverTimeLimitValue != null && !deliverTimeLimitValue.isEmpty()) {
					int timeType = Integer.valueOf(deliverTimeLimitType);
					long time = Long.valueOf(deliverTimeLimitValue);
					if (timeType > 0 && time > 0) {
						task.setDeliverTimeLimit(new DeliverTimeLimit(timeType, time));
					}
				}

				task.setDailyTaskCycle(Integer.valueOf(getCellValue(cells[++index])));
				int dailyTaskMaxNum = Integer.valueOf(getCellValue(cells[++index]));
				// 2012-10-17 15:11:45 限制任务最大次数为50
				dailyTaskMaxNum = dailyTaskMaxNum >= 50 ? 50 : dailyTaskMaxNum;
				task.setDailyTaskMaxNum(dailyTaskMaxNum);

				task.setCountScore(Integer.valueOf(getCellValue(cells[++index])));

				/** 开始加载任务目标 */
				String readTarget = null;
				String[] relTargets = null;

				List<TaskTarget> targetResult = new ArrayList<TaskTarget>();
				List<TaskTarget> list = new ArrayList<TaskTarget>();
				// 任务目标 - 物品
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getArticleTargets(relTargets,task.getCountryLimit());
					targetResult.addAll(list);
				}
				/******** 49 ********/
				// 任务目标 - 使用物品
				readTarget = getCellValue(cells[++index]);// 颜色,物品名,数量,地图名,x,y&颜色,物品名,数量,地图名,区域名|颜色,物品名,数量,地图名,x,y
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getUseArticleTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 确定怪
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {// 1,猴子,7|1,猩猩,7&1,狗熊,2
					list = getKillMonsterTargets(relTargets, task.getId(),task.getCountryLimit());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 等级段怪
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {// 颜色,等级下限,等级上限,数目&颜色,等级下限,等级上限,数目
					list = getKillMonsterLvTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}

				// 任务目标 - 角色等级附近怪
				readTarget = getCellValue(cells[++index]);
				// System.out.println("readTarget:" + readTarget);

				if (readTarget != null && !readTarget.isEmpty()) {
					String[] gradeLimit = readTarget.split(",");// 高级限制，低级限制,数量 10 ,-5,num
					try {
						if (gradeLimit.length != 3) {
							throw new Exception();
						} else {
							TaskTargetOfMonsterLvNearPlayer lvNearPlayer = new TaskTargetOfMonsterLvNearPlayer(Integer.valueOf(gradeLimit[1]), Integer.valueOf(gradeLimit[0]), Integer.valueOf(gradeLimit[2]));
							targetResult.add(lvNearPlayer);
						}
					} catch (Exception e1) {
						logger.error("[任务加载错误][任务目标-角色的你攻击附近怪][ID={}][错误项:{}]", new Object[] { task.getId(), readTarget });
						notices.append("[任务加载错误][任务目标-角色的你攻击附近怪][ID=").append(task.getId()).append("]<BR/>");
						throw e1;
					}
				}

				// 任务目标 - NPC对话
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {// 地图名,NPC名
					list = getTalkToNPCTargets(relTargets, task.getId(),task.getCountryLimit());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 护送NPC
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {// NPC名
					list = getConvoyNPCTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 杀人
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getKillPlayerTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 充值
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getReChargeTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 任务完成
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getTaskDeliverTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 - 探索
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getDiscoveryTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				// 任务目标 获得BUFF
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					TaskTargetOfBuff targetOfBuff = new TaskTargetOfBuff(readTarget);
					targetResult.add(targetOfBuff);
				}
				/********** 60 ***********/
				// 任务目标 建造庄园建筑
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
					TaskTargetOfCaveBuild targetOfCaveBuild = new TaskTargetOfCaveBuild(values[0], values[1]);
					targetResult.add(targetOfCaveBuild);
				}
				// 任务目标 庄园种植
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
					TaskTargetOfCavePlant targetOfCavePlant = new TaskTargetOfCavePlant(values[0], values[1]);
					targetResult.add(targetOfCavePlant);
				}
				// 任务目标 偷取果实
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
					TaskTargetOfCaveSteal targetOfCaveSteal = new TaskTargetOfCaveSteal(values[0], values[1]);
					targetResult.add(targetOfCaveSteal);
				}
				// 任务目标 收获果实
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
					TaskTargetOfCaveHarvest targetOfCaveHarvest = new TaskTargetOfCaveHarvest(values[0], values[1]);
					targetResult.add(targetOfCaveHarvest);
				}
				// 任务目标 物品,且交付时扣除 //颜色,物品,个数&颜色,物品,个数
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					String[] targets = turn2TargetString(readTarget);
					list = getArticleTargetAnddeletes(targets);
					targetResult.addAll(list);
				}

				// 任务目标 - 采集
				readTarget = getCellValue(cells[++index]);
				if (readTarget != null && !readTarget.isEmpty()) {
					TaskTarget target = getCollectionTarget(readTarget, task.getId());
					if (target != null) {
						targetResult.add(target);
					}
				}

				// 任务目标 - 社会关系
				readTarget = getCellValue(cells[++index]);
				relTargets = turn2TargetString(readTarget);
				if (readTarget != null && !readTarget.isEmpty()) {
					list = getRelationTargets(relTargets, task.getId());
					if (list != null && list.size() > 0) {
						targetResult.addAll(list);
					}
				}
				/****************************** 任务目标加载完成 *******************************/
				String excess = getCellValue(cells[++index]);
				if (excess != null && !excess.isEmpty()) {
					String[] values = StringTool.string2Array(excess, ",", String.class);
					task.setExcessTarget(values[0]);
					task.setHasExcess(!task.getExcessTarget().isEmpty());
					task.setExcessTargetRate(df.parse(values[1]).doubleValue());
					Article article = ArticleManager.getInstance().getArticle(values[0]);
					if (article == null) {
						notices.append("[额外任务目标物品不存在][ID:").append(ID).append("][物品名:").append(values[0]).append("]<BR/>");
					}

				}
				/****************************** 任务奖励加载开始 *******************************/
				List<TaskPrize> prizeResult = new ArrayList<TaskPrize>();
				List<TaskPrize> tempPrizeList = new ArrayList<TaskPrize>();

				// 任务奖励 - 经验
				String expPrizeType = getCellValue(cells[++index]);
				String expValue = getCellValue(cells[++index]);
				if ("".equals(expPrizeType)) {
					expPrizeType = "0";
				}
				if (expPrizeType != null && !expPrizeType.isEmpty() && expValue != null && !expValue.isEmpty()) {
					int exp = Integer.valueOf(expValue);
					TaskPrize prize = TaskPrizeOfExp.createTaskPrize(Integer.valueOf(expPrizeType), exp);
					prizeResult.add(prize);
				}
				/********** 70 **********/
				String money1 = getCellValue(cells[++index]);
				if (money1 != null && !money1.isEmpty()) {
					TaskPrize moneyPrize = TaskPrizeOfBindSilver.createTaskPrize(Integer.valueOf(money1));
					prizeResult.add(moneyPrize);
				}
				String money2 = getCellValue(cells[++index]);
				String money3 = getCellValue(cells[++index]);

				String readPirze = "";
				TaskPrize prizeTemp = null;
				// 任务奖励 -声望
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					String[] prestigeArr = readPirze.split(",");
					try {
						if (prestigeArr.length != 2) {
							throw new Exception();
						}
						prizeTemp = TaskPrizeOfPrestige.createTaskPrize(Integer.valueOf(prestigeArr[0]), Integer.valueOf(prestigeArr[1]));
						prizeResult.add(prizeTemp);
					} catch (Exception e) {
						logger.error("[任务加载错误][任务奖励-声望][ID={}][错误项:{}]", new Object[] { task.getId(), readPirze });
						notices.append("[任务加载错误][任务奖励-声望][ID=").append(task.getId()).append("]<BR/>");
						throw e;
					}
				}

				// 任务奖励 -修法值
				String pneuma = getCellValue(cells[++index]);
				if (pneuma != null && !pneuma.isEmpty()) {
					prizeTemp = TaskPrizeOfPneuma.createTaskPrize(Integer.valueOf(pneuma));
					prizeResult.add(prizeTemp);
				}

				// 任务奖励 - 境界
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					prizeTemp = TaskPrizeOfClassLevel.createTaskPrize(Integer.valueOf(readPirze));
					prizeResult.add(prizeTemp);
				}

				// 任务奖励 -贡献度
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					String[] contributeArr = readPirze.split(",");
					try {
						if (contributeArr.length != 2) {
							throw new Exception();
						}
						prizeTemp = TaskPrizeOfContribute.createTaskPrize(Integer.valueOf(contributeArr[0]), Integer.valueOf(contributeArr[1]));
						prizeResult.add(prizeTemp);
					} catch (Exception e) {
						logger.error("[任务加载错误][任务奖励-贡献度][ID=" + task.getId() + "][错误项:" + readPirze + "]", e);
						notices.append("[任务加载错误][任务奖励-贡献度][ID=").append(task.getId()).append("]<BR/>");
						throw e;
					}
				}

				// 功勋值奖励
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					int gongxunNum = Integer.valueOf(readPirze);
					prizeResult.add(TaskPrizeOfGongxun.createTaskPrize(gongxunNum));
				}

				// 任务奖励 - 技能点
				String skillPoint = getCellValue(cells[++index]);
				if (skillPoint != null && !skillPoint.isEmpty()) {
					int intValue = Integer.valueOf(skillPoint);
					if (intValue > 0) {
						prizeTemp = TaskPrizeOfSkillPoint.createTaskPrize(Integer.valueOf(skillPoint));
						prizeResult.add(prizeTemp);
					}
				}
				String[] relPrizes = null;

				// 任务奖励 -buff
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					relPrizes = turn2TargetString(readPirze);
					tempPrizeList = getBuffPrizes(relPrizes, task.getId());
					prizeResult.addAll(tempPrizeList);
				}
				/******* 80 ********/
				// 任务奖励 -称号
				String title = getCellValue(cells[++index]);
				if (title != null && !title.isEmpty()) {
					prizeTemp = TaskPrizeOfTitle.createTaskPrize(title);
					prizeResult.add(prizeTemp);
				}

				// 任务奖励 -- 道具
				readPirze = getCellValue(cells[++index]);
				// 1，杯具，6|1,餐具,7|1,具具,8@2&1,洗具,2
				relPrizes = turn2TargetString(readPirze);
				if (relPrizes != null && relPrizes.length > 0) {
					tempPrizeList = getArticlePrizes(relPrizes, task.getId());
					prizeResult.addAll(tempPrizeList);
				}

				// 任务奖励 - 按职业给予奖励
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !readPirze.isEmpty()) {
					prizeTemp = getCareerArtcleTarget(readPirze);
					if (prizeTemp != null) {
						prizeResult.add(prizeTemp);
					}
				}

				// 任务奖励 -随机
				readPirze = getCellValue(cells[++index]);
				relPrizes = turn2TargetString(readPirze);
				if (relPrizes != null && relPrizes.length > 0) {
					tempPrizeList = getRandomPrizes(relPrizes, task.getId());
					prizeResult.addAll(tempPrizeList);
					task.setHasRandomPrize(true);
				}

				// 任务温馨提示
				readPirze = getCellValue(cells[++index]);

				// 任务目标--随机怪物个数
				readTarget = getCellValue(cells[++index]);
				TaskTarget t = getMonsterRandomNum(readTarget, ID);
				if (t != null) {
					task.setHasRandomTarget(true);
					targetResult.add(t);
				}

				// 任务奖励-历练值
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !"".equals(readPirze)) {
					TaskPrize prize = TaskPrizeOfLilian.createTaskPrize(Integer.valueOf(readPirze));
					prizeResult.add(prize);
				}
				// 任务奖励-国家资源
				readPirze = getCellValue(cells[++index]);
				if (readPirze != null && !"".equals(readPirze)) {
					TaskPrize prize = TaskPrizeOfCountryRes.createTaskPrize(Integer.valueOf(readPirze));
					prizeResult.add(prize);
				}

				task.setTargets(targetResult.toArray(new TaskTarget[0]));
				// 奖励录入完毕
				task.setPrizes(prizeResult.toArray(new TaskPrize[0]));

				loadSearchInfo(task);
				addToCollections(task);
			}
			/****************************** 特殊任务配置 ******************************/
			sheet = workbook.getSheetAt(2);
			XSSFRow row = sheet.getRow(1);
			int cellNum = row.getLastCellNum() - row.getFirstCellNum();// row.getPhysicalNumberOfCells();
			List<Cell> cellList = new ArrayList<Cell>();
			for (int c = 0; c < cellNum; c++) {
				cellList.add(row.getCell(c, Row.RETURN_BLANK_AS_NULL));
			}
			Cell[] cells = cellList.toArray(new Cell[cellList.size()]);
			Cell cell = cells[0];
			// 断开
			String sp = getCellValue(cell);
			if (sp != null) {
				String[] specialTaskNames = StringTool.string2Array(getCellValue(cell), ",", String.class);
				if (specialTaskNames != null) {
					for (String taskName : specialTaskNames) {
						disconnectionMainTask.add((taskName));
					}
					if (logger.isInfoEnabled()) logger.info("[断开连续接任务][" + Arrays.toString(specialTaskNames));
				}
			}
			// 开启体力值
			cell = cells[1];
			openThewTaskName = getCellValue(cell);
			/****************************** 活动类任务ji ******************************/
			sheet = workbook.getSheetAt(3);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 2; i < rows; i++) {
				row = sheet.getRow(i);
				cellNum = row.getLastCellNum() - row.getFirstCellNum();// row.getPhysicalNumberOfCells();
				cellList = new ArrayList<Cell>();
				for (int c = 0; c < cellNum; c++) {
					cellList.add(row.getCell(c, Row.RETURN_BLANK_AS_NULL));
				}
				cells = cellList.toArray(new Cell[cellList.size()]);
				int index = 0;
				String name = getCellValue(cells[index++]);
				logger.warn("name:" + name);
				int thew = Integer.valueOf(getCellValue(cells[index++]));
				int prizeId = Integer.valueOf(getCellValue(cells[index++]));
				Long[] expPrize = new Long[ActivityTaskExp.maxLevel];
				for (int k = 0; k < expPrize.length; k++) {
					expPrize[k] = Long.valueOf(getCellValue(cells[index++]));
				}
				ActivityTaskExp activityTaskExp = new ActivityTaskExp(prizeId, name, thew, expPrize);

				activityPrizeMap.put(activityTaskExp.getPrizeId(), activityTaskExp);
				if (logger.isInfoEnabled()) logger.info(activityTaskExp.toString());
			}
			logger.warn("[任务加载完毕] [耗时:{}]", new Object[] { (SystemTime.currentTimeMillis() - start) });
		} catch (Exception e) {
			logger.error("[任务加载异常] [" + currentLoadId + "]", e);
			notices.append("[任务加载异常:").append(e.getMessage()).append("][ID:").append(currentLoadId).append("]<BR/>");
			throw e;
		} finally {
			if (workbook != null) {
				// workbook.close();
			}
		}
		currentLoadId = 0;
	}

	private String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		Object result = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellInternalDateFormatted(cell)) result = cell.getDateCellValue();
			else result = (int) cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			result = (int) cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			result = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue();
			break;
		default:
			result = "";
			break;
		}
		return result.toString().trim();
	}

	// public void loadFile() throws Exception {
	// Workbook workbook = null;
	// try {
	// if (logger.isInfoEnabled()) logger.info("=========================================开始加载任务=========================================");
	// long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	// long ID = 0;
	// // file
	// File file = new File(getFile());
	// if (!file.exists()) {
	// notices.append("任务配置文件不存在");
	// return;
	// }
	// workbook = Workbook.getWorkbook(file);
	// Sheet sheet = workbook.getSheet(1);
	// int maxRow = sheet.getRows();
	// for (int i = 3; i < maxRow; i++) {
	// Cell[] cells = sheet.getRow(i);
	// Task task = new Task();
	// int index = 0;
	// // 需要翻译的2,8,9,31,32,33,34,35,36,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,81,82,83,84,
	// String strId = modifyContent(cells[++index].getContents());
	// if (strId == null || strId.isEmpty()) {
	// if (logger.isInfoEnabled()) logger.info("ID 异常: 行数[{}]", new Object[] { i });
	// notices.append("ID 异常: 行数:").append(i).append("<BR/>");
	// continue;
	// }
	// task.setId(Long.valueOf(modifyContent(strId)));
	// ID = task.getId();
	// currentLoadId = ID;
	//
	// task.setName((modifyContent(cells[++index].getContents())));
	// task.setName_stat((modifyContent(cells[++index].getContents())));
	// task.setGroupName((modifyContent(cells[++index].getContents())));
	// task.setGroupName_stat((modifyContent(cells[++index].getContents())));
	//
	// // 集合名
	// String collectionValue = StringTool.modifyContent(cells[++index]);
	// task.setCollections((collectionValue == null ? "" : collectionValue.trim()));
	//
	// collectionValue = StringTool.modifyContent(cells[++index]);
	// task.setCollections_stat((collectionValue == null ? "" : collectionValue.trim()));
	//
	// collectionValue = StringTool.modifyContent(cells[++index]);
	// task.setBigCollection((collectionValue == null ? "" : collectionValue.trim()));
	//
	// collectionValue = StringTool.modifyContent(cells[++index]);
	// task.setBigCollection_stat((collectionValue == null ? "" : collectionValue.trim()));
	//
	// task.setShowType(Byte.valueOf(modifyContent(cells[++index].getContents())));
	// task.setGrade(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setDes((modifyContent(cells[++index].getContents())));
	// task.setUnDeliverTalk((modifyContent(cells[++index].getContents())));//
	// task.setMinGradeLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setMaxGradeLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setSexLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setCountryLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setThewLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setCountryOfficialLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setBourn(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setWorkLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setSeptLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setSeptOfficialLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setSocialRelationsLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setType(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// // ++index; 第一个，对玩家封印状态的限制
	// task.setSealLimit(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setShowTarget(Byte.valueOf(modifyContent(cells[++index].getContents())));
	// // 是否是完成自动交付
	// task.setAutoDeliver(1 == Integer.valueOf(modifyContent(cells[++index].getContents())));
	// // 限制元神等级
	// String soulLevelLimitStr = modifyContent(cells[++index].getContents());
	// if (soulLevelLimitStr == null || "".equals(soulLevelLimitStr)) {
	// task.setSoulLevelLimit(-1);
	// } else {
	// task.setSoulLevelLimit(Integer.valueOf(soulLevelLimitStr));
	// }
	// String serverAutoAccept = modifyContent(cells[++index].getContents());
	// if (serverAutoAccept == null || "".equals(serverAutoAccept)) {
	// task.setServerAutoAccept(false);
	// } else {
	// task.setServerAutoAccept(Integer.valueOf(serverAutoAccept) == 1);
	// }
	// ++index;
	// ++index;
	// ++index;
	// ++index;// 30
	// task.setStartNpc((modifyContent(cells[++index].getContents())));
	// task.setStartMap((modifyContent(cells[++index].getContents())));
	// task.setStartTalk((modifyContent(cells[++index].getContents())));
	// task.setEndNpc((modifyContent(cells[++index].getContents())));
	// task.setEndMap((modifyContent(cells[++index].getContents())));
	// task.setEndTalk((modifyContent(cells[++index].getContents())));// 36
	// task.setFrontGroupName((modifyContent(cells[++index].getContents())));
	// task.setDependType(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// task.setThewCost(Integer.valueOf(modifyContent(cells[++index].getContents())));
	//
	// // 接取给予buff
	// String givenBuffStr = modifyContent(cells[++index].getContents());
	// if (givenBuffStr != null && !givenBuffStr.isEmpty()) {
	// TaskGivenBuff givenBuff = getGivenBuff(givenBuffStr, task.getId());
	// if (givenBuff != null) {
	// task.setGivenBuff(givenBuff);
	// }
	// }
	// // 接取给予物品
	// String givenArticleStr = modifyContent(cells[++index].getContents());
	// if (givenArticleStr != null && !givenArticleStr.isEmpty()) {
	// TaskGivenArticle givenArticle = getGivenArticle(givenArticleStr, task.getId());
	// if (givenArticle != null) {
	// task.setGivenArticle(givenArticle);
	// }
	// }
	//
	// String timeLimitType = modifyContent(cells[++index].getContents());
	// String timeLimitValue = modifyContent(cells[++index].getContents());
	// if (timeLimitType != null && !timeLimitType.isEmpty() && timeLimitValue != null && !timeLimitValue.isEmpty()) {
	// task.setTimeLimit(TimeLimitFactory.createTimeLimit(Integer.valueOf(timeLimitType), modifyContent(timeLimitValue)));
	// }
	// String deliverTimeLimitType = modifyContent(cells[++index].getContents());
	// String deliverTimeLimitValue = modifyContent(cells[++index].getContents());
	// if (deliverTimeLimitType != null && !deliverTimeLimitType.isEmpty() && deliverTimeLimitValue != null && !deliverTimeLimitValue.isEmpty()) {
	// int timeType = Integer.valueOf(deliverTimeLimitType);
	// long time = Long.valueOf(deliverTimeLimitValue);
	// if (timeType > 0 && time > 0) {
	// task.setDeliverTimeLimit(new DeliverTimeLimit(timeType, time));
	// }
	// }
	//
	// task.setDailyTaskCycle(Integer.valueOf(modifyContent(cells[++index].getContents())));
	// int dailyTaskMaxNum = Integer.valueOf(modifyContent(cells[++index].getContents()));
	// // 2012-10-17 15:11:45 限制任务最大次数为50
	// dailyTaskMaxNum = dailyTaskMaxNum >= 50 ? 50 : dailyTaskMaxNum;
	// task.setDailyTaskMaxNum(dailyTaskMaxNum);
	//
	// task.setCountScore(Integer.valueOf(modifyContent(cells[++index].getContents())));
	//
	// /** 开始加载任务目标 */
	// String readTarget = null;
	// String[] relTargets = null;
	//
	// List<TaskTarget> targetResult = new ArrayList<TaskTarget>();
	// List<TaskTarget> list = new ArrayList<TaskTarget>();
	// // 任务目标 - 物品
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getArticleTargets(relTargets);
	// targetResult.addAll(list);
	// }
	// /******** 49 ********/
	// // 任务目标 - 使用物品
	// readTarget = modifyContent((cells[++index].getContents()));// 颜色,物品名,数量,地图名,区域名&颜色,物品名,数量,地图名,区域名|颜色,物品名,数量,地图名,区域名
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getUseArticleTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 确定怪
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {// 1,猴子,7|1,猩猩,7&1,狗熊,2
	// list = getKillMonsterTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 等级段怪
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {// 颜色,等级下限,等级上限,数目&颜色,等级下限,等级上限,数目
	// list = getKillMonsterLvTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	//
	// // 任务目标 - 角色等级附近怪
	// readTarget = modifyContent((cells[++index].getContents()));
	// String[] gradeLimit = readTarget.split(",");// 高级限制，低级限制,数量 10 ,-5,num
	// if (readTarget != null && !readTarget.isEmpty()) {
	// try {
	// if (gradeLimit.length != 3) {
	// throw new Exception();
	// } else {
	// TaskTargetOfMonsterLvNearPlayer lvNearPlayer = new TaskTargetOfMonsterLvNearPlayer(Integer.valueOf(gradeLimit[1]), Integer.valueOf(gradeLimit[0]),
	// Integer.valueOf(gradeLimit[2]));
	// targetResult.add(lvNearPlayer);
	// }
	// } catch (Exception e1) {
	// logger.error("[任务加载错误][任务目标-角色的你攻击附近怪][ID={}][错误项:{}]", new Object[] { task.getId(), readTarget });
	// notices.append("[任务加载错误][任务目标-角色的你攻击附近怪][ID=").append(task.getId()).append("]<BR/>");
	// throw e1;
	// }
	// }
	//
	// // 任务目标 - NPC对话
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {// 地图名,NPC名
	// list = getTalkToNPCTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 护送NPC
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {// NPC名
	// list = getConvoyNPCTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 杀人
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getKillPlayerTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 充值
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getReChargeTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 任务完成
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getTaskDeliverTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 - 探索
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getDiscoveryTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// // 任务目标 获得BUFF
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// TaskTargetOfBuff targetOfBuff = new TaskTargetOfBuff(readTarget);
	// targetResult.add(targetOfBuff);
	// }
	// /********** 60 ***********/
	// // 任务目标 建造庄园建筑
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
	// TaskTargetOfCaveBuild targetOfCaveBuild = new TaskTargetOfCaveBuild(values[0], values[1]);
	// targetResult.add(targetOfCaveBuild);
	// }
	// // 任务目标 庄园种植
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
	// TaskTargetOfCavePlant targetOfCavePlant = new TaskTargetOfCavePlant(values[0], values[1]);
	// targetResult.add(targetOfCavePlant);
	// }
	// // 任务目标 偷取果实
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
	// TaskTargetOfCaveSteal targetOfCaveSteal = new TaskTargetOfCaveSteal(values[0], values[1]);
	// targetResult.add(targetOfCaveSteal);
	// }
	// // 任务目标 收获果实
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// Integer[] values = StringTool.string2Array(readTarget, ",", Integer.class);
	// TaskTargetOfCaveHarvest targetOfCaveHarvest = new TaskTargetOfCaveHarvest(values[0], values[1]);
	// targetResult.add(targetOfCaveHarvest);
	// }
	// // 任务目标 物品,且交付时扣除 //颜色,物品,个数&颜色,物品,个数
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// String[] targets = turn2TargetString(readTarget);
	// list = getArticleTargetAnddeletes(targets);
	// targetResult.addAll(list);
	// }
	//
	// // 任务目标 - 采集
	// readTarget = modifyContent((cells[++index].getContents()));
	// if (readTarget != null && !readTarget.isEmpty()) {
	// TaskTarget target = getCollectionTarget(readTarget, task.getId());
	// if (target != null) {
	// targetResult.add(target);
	// }
	// }
	//
	// // 任务目标 - 社会关系
	// readTarget = modifyContent((cells[++index].getContents()));
	// relTargets = turn2TargetString(readTarget);
	// if (readTarget != null && !readTarget.isEmpty()) {
	// list = getRelationTargets(relTargets, task.getId());
	// if (list != null && list.size() > 0) {
	// targetResult.addAll(list);
	// }
	// }
	// /****************************** 任务目标加载完成 *******************************/
	// String excess = modifyContent((cells[++index].getContents()));
	// if (excess != null && !excess.isEmpty()) {
	// String[] values = StringTool.string2Array(excess, ",", String.class);
	// task.setExcessTarget(values[0]);
	// task.setHasExcess(!task.getExcessTarget().isEmpty());
	// task.setExcessTargetRate(df.parse(values[1]).doubleValue());
	// Article article = ArticleManager.getInstance().getArticle(values[0]);
	// if (article == null) {
	// notices.append("[额外任务目标物品不存在][ID:").append(ID).append("][物品名:").append(values[0]).append("]<BR/>");
	// }
	//
	// }
	// /****************************** 任务奖励加载开始 *******************************/
	// List<TaskPrize> prizeResult = new ArrayList<TaskPrize>();
	// List<TaskPrize> tempPrizeList = new ArrayList<TaskPrize>();
	//
	// // 任务奖励 - 经验
	// String expPrizeType = modifyContent(cells[++index].getContents());
	// String expValue = modifyContent(cells[++index].getContents());
	// if ("".equals(expPrizeType)) {
	// expPrizeType = "0";
	// }
	// if (expPrizeType != null && !expPrizeType.isEmpty() && expValue != null && !expValue.isEmpty()) {
	// int exp = Integer.valueOf(expValue);
	// TaskPrize prize = TaskPrizeOfExp.createTaskPrize(Integer.valueOf(expPrizeType), exp);
	// prizeResult.add(prize);
	// }
	// /********** 70 **********/
	// String money1 = modifyContent(cells[++index].getContents());
	// if (money1 != null && !money1.isEmpty()) {
	// TaskPrize moneyPrize = TaskPrizeOfBindSilver.createTaskPrize(Integer.valueOf(money1));
	// prizeResult.add(moneyPrize);
	// }
	// String money2 = modifyContent(cells[++index].getContents());
	// String money3 = modifyContent(cells[++index].getContents());
	//
	// String readPirze = "";
	// TaskPrize prizeTemp = null;
	// // 任务奖励 -声望
	// readPirze = modifyContent(cells[++index].getContents());
	// String[] prestigeArr = readPirze.split(",");
	// if (readPirze != null && !readPirze.isEmpty()) {
	// try {
	// if (prestigeArr.length != 2) {
	// throw new Exception();
	// }
	// prizeTemp = TaskPrizeOfPrestige.createTaskPrize(Integer.valueOf(prestigeArr[0]), Integer.valueOf(prestigeArr[1]));
	// prizeResult.add(prizeTemp);
	// } catch (Exception e) {
	// logger.error("[任务加载错误][任务奖励-声望][ID={}][错误项:{}]", new Object[] { task.getId(), readPirze });
	// notices.append("[任务加载错误][任务奖励-声望][ID=").append(task.getId()).append("]<BR/>");
	// throw e;
	// }
	// }
	//
	// // 任务奖励 -修法值
	// String pneuma = modifyContent(cells[++index].getContents());
	// if (pneuma != null && !pneuma.isEmpty()) {
	// prizeTemp = TaskPrizeOfPneuma.createTaskPrize(Integer.valueOf(pneuma));
	// prizeResult.add(prizeTemp);
	// }
	//
	// // 任务奖励 - 境界
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !readPirze.isEmpty()) {
	// prizeTemp = TaskPrizeOfClassLevel.createTaskPrize(Integer.valueOf(readPirze));
	// prizeResult.add(prizeTemp);
	// }
	//
	// // 任务奖励 -贡献度
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !readPirze.isEmpty()) {
	// String[] contributeArr = readPirze.split(",");
	// try {
	// if (contributeArr.length != 2) {
	// throw new Exception();
	// }
	// prizeTemp = TaskPrizeOfContribute.createTaskPrize(Integer.valueOf(contributeArr[0]), Integer.valueOf(contributeArr[1]));
	// prizeResult.add(prizeTemp);
	// } catch (Exception e) {
	// logger.error("[任务加载错误][任务奖励-贡献度][ID=" + task.getId() + "][错误项:" + readPirze + "]", e);
	// notices.append("[任务加载错误][任务奖励-贡献度][ID=").append(task.getId()).append("]<BR/>");
	// throw e;
	// }
	// }
	//
	// // 功勋值奖励
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !readPirze.isEmpty()) {
	// int gongxunNum = Integer.valueOf(readPirze);
	// prizeResult.add(TaskPrizeOfGongxun.createTaskPrize(gongxunNum));
	// }
	//
	// // 任务奖励 - 技能点
	// String skillPoint = modifyContent(cells[++index].getContents());
	// if (skillPoint != null && !skillPoint.isEmpty()) {
	// int intValue = Integer.valueOf(skillPoint);
	// if (intValue > 0) {
	// prizeTemp = TaskPrizeOfSkillPoint.createTaskPrize(Integer.valueOf(skillPoint));
	// prizeResult.add(prizeTemp);
	// }
	// }
	// String[] relPrizes = null;
	//
	// // 任务奖励 -buff
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !readPirze.isEmpty()) {
	// relPrizes = turn2TargetString(readPirze);
	// tempPrizeList = getBuffPrizes(relPrizes, task.getId());
	// prizeResult.addAll(tempPrizeList);
	// }
	// /******* 80 ********/
	// // 任务奖励 -称号
	// String title = modifyContent((cells[++index].getContents()));
	// if (title != null && !title.isEmpty()) {
	// prizeTemp = TaskPrizeOfTitle.createTaskPrize(title);
	// prizeResult.add(prizeTemp);
	// }
	//
	// // 任务奖励 -- 道具
	// readPirze = modifyContent((cells[++index].getContents()));
	// // 1，杯具，6|1,餐具,7|1,具具,8@2&1,洗具,2
	// relPrizes = turn2TargetString(readPirze);
	// if (relPrizes != null && relPrizes.length > 0) {
	// tempPrizeList = getArticlePrizes(relPrizes, task.getId());
	// prizeResult.addAll(tempPrizeList);
	// }
	//
	// // 任务奖励 - 按职业给予奖励
	// readPirze = modifyContent((cells[++index].getContents()));
	// if (readPirze != null && !readPirze.isEmpty()) {
	// prizeTemp = getCareerArtcleTarget(readPirze);
	// if (prizeTemp != null) {
	// prizeResult.add(prizeTemp);
	// }
	// }
	//
	// // 任务奖励 -随机
	// readPirze = modifyContent((cells[++index].getContents()));
	// relPrizes = turn2TargetString(readPirze);
	// if (relPrizes != null && relPrizes.length > 0) {
	// tempPrizeList = getRandomPrizes(relPrizes, task.getId());
	// prizeResult.addAll(tempPrizeList);
	// task.setHasRandomPrize(true);
	// }
	//
	// // 任务温馨提示
	// readPirze = modifyContent(cells[++index].getContents());
	//
	// // 任务目标--随机怪物个数
	// readTarget = modifyContent(cells[++index].getContents());
	// TaskTarget t = getMonsterRandomNum(readTarget, ID);
	// if (t != null) {
	// task.setHasRandomTarget(true);
	// targetResult.add(t);
	// }
	//
	// // 任务奖励-历练值
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !"".equals(readPirze)) {
	// TaskPrize prize = TaskPrizeOfLilian.createTaskPrize(Integer.valueOf(readPirze));
	// prizeResult.add(prize);
	// }
	// // 任务奖励-国家资源
	// readPirze = modifyContent(cells[++index].getContents());
	// if (readPirze != null && !"".equals(readPirze)) {
	// TaskPrize prize = TaskPrizeOfCountryRes.createTaskPrize(Integer.valueOf(readPirze));
	// prizeResult.add(prize);
	// }
	//
	// task.setTargets(targetResult.toArray(new TaskTarget[0]));
	// // 奖励录入完毕
	// task.setPrizes(prizeResult.toArray(new TaskPrize[0]));
	//
	// loadSearchInfo(task);
	// addToCollections(task);
	// }
	// /****************************** 特殊任务配置 ******************************/
	// sheet = workbook.getSheet(2);
	// Cell[] cells = sheet.getRow(1);
	// Cell cell = cells[0];
	// // 断开
	// String sp = (StringTool.modifyContent(cell));
	// if (sp != null) {
	// String[] specialTaskNames = StringTool.string2Array(StringTool.modifyContent(cell), ",", String.class);
	// if (specialTaskNames != null) {
	// for (String taskName : specialTaskNames) {
	// disconnectionMainTask.add((taskName));
	// }
	// if (logger.isInfoEnabled()) logger.info("[断开连续接任务][" + Arrays.toString(specialTaskNames));
	// }
	// }
	// // 开启体力值
	// cell = cells[1];
	// openThewTaskName = (StringTool.modifyContent(cell));
	// /****************************** 活动类任务ji ******************************/
	// sheet = workbook.getSheet(3);
	// int rows = sheet.getRows();
	// for (int i = 2; i < rows; i++) {
	// cells = sheet.getRow(i);
	// int index = 0;
	// String name = StringTool.modifyContent(cells[index++]);
	// int thew = Integer.valueOf(StringTool.modifyContent(cells[index++]));
	// int prizeId = Integer.valueOf(StringTool.modifyContent(cells[index++]));
	// Long[] expPrize = new Long[ActivityTaskExp.maxLevel];
	// for (int k = 0; k < expPrize.length; k++) {
	// expPrize[k] = Long.valueOf(StringTool.modifyContent(cells[index++]));
	// }
	// ActivityTaskExp activityTaskExp = new ActivityTaskExp(prizeId, name, thew, expPrize);
	//
	// activityPrizeMap.put(activityTaskExp.getPrizeId(), activityTaskExp);
	// if (logger.isInfoEnabled()) logger.info(activityTaskExp.toString());
	// }
	// logger.warn("[任务加载完毕] [耗时:{}]", new Object[] { (SystemTime.currentTimeMillis() - start) });
	// } catch (Exception e) {
	// logger.error("[任务加载异常]", e);
	// notices.append("[任务加载异常:").append(e.getMessage()).append("][ID:").append(currentLoadId).append("]<BR/>");
	// throw e;
	// } finally {
	// if (workbook != null) {
	// workbook.close();
	// }
	// }
	// currentLoadId = 0;
	// }

	private void checkTaskGivenArticleValid(TaskGivenArticle givenArticle) {
		if (givenArticle != null) {
			for (String aeticleName : givenArticle.getNames()) {
				Article article = articleManager.getArticle(aeticleName);
				if (article == null) {
					notices.append("		[任务给予物品不存在] [ID:").append(currentLoadId).append("] [物品:").append(aeticleName).append("]<BR/>");
				}
			}
		}
	}

	private void loadSearchInfo(Task task) {
		Game start = GameManager.getInstance().getGameByDisplayName(task.getStartMap(), CountryManager.国家A);
		if (start == null) {
			start = GameManager.getInstance().getGameByDisplayName(task.getStartMap(), CountryManager.中立);
		}
		if (start != null) {
			task.setStartMapResName(start.getGameInfo().name);
		}
		Game end = GameManager.getInstance().getGameByDisplayName(task.getEndMap(), CountryManager.国家A);
		if (end == null) {
			end = GameManager.getInstance().getGameByDisplayName(task.getEndMap(), CountryManager.中立);
		}
		if (end != null) {
			task.setEndMapResName(end.getGameInfo().name);
		}

		// for (TaskTarget target : task.getTargets()) {
		// target.initResName();
		// }
		for (TaskPrize prize : task.getPrizes()) {
			prize.initArticle(task);
		}
	}

	/**
	 * 加载任务接取时给予的物品
	 * @param givenArticleStr
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private TaskGivenArticle getGivenArticle(String givenArticleStr, long id) throws Exception {
		String[] subs = givenArticleStr.split("&");
		int[] nums = new int[subs.length];
		String[] names = new String[subs.length];
		int[] colors = new int[subs.length];

		for (int i = 0; i < subs.length; i++) {
			String[] values = subs[i].split(",");
			if (values.length != 3) {
				// logger.error("[任务加载错误][接任务时给予物品][ID=" + id + "][错误项:" + subs[i] + "]");
				logger.error("[任务加载错误][接任务时给予物品][ID={}][错误项:{}]", new Object[] { id, subs[i] });
				notices.append("[任务加载错误][接任务时给予物品][ID=").append(id).append("]<BR/>");
				throw new Exception();
			}
			colors[i] = Integer.valueOf(values[0]);
			names[i] = values[1];
			nums[i] = Integer.valueOf(values[2]);
		}
		return new TaskGivenArticle(nums, names, colors);
	}

	/**
	 * 加载任务给予的buff 多个随机给一个
	 * @param givenBuffStr
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private TaskGivenBuff getGivenBuff(String givenBuffStr, long id) throws Exception {
		String[] subs = givenBuffStr.split("\\|");
		double[] rates = new double[subs.length];
		int[] colors = new int[subs.length];
		String[] names = new String[subs.length];

		try {
			for (int i = 0; i < subs.length; i++) {
				String[] values = subs[i].split(",");
				if (values.length != 3) {
					logger.error("[任务加载错误][接任务时给予BUFF][ID={}][错误项:{}]", new Object[] { id, subs[i] });
					notices.append("[任务加载错误][接任务时给予BUFF][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
				colors[i] = Integer.valueOf(values[0]);
				names[i] = values[1];
				rates[i] = df.parse(values[2]).doubleValue();
			}
		} catch (Exception e) {
			logger.error("[任务加载错误][接任务时给予BUFF][ID=" + id + "][错误项:" + givenBuffStr + "]", e);
			notices.append("[任务加载错误][接任务时给予BUFF][ID=").append(id).append("]<BR/>");
			throw e;
		}
		return new TaskGivenBuff(names, colors, rates);
	}

	/**
	 * 对excel表中输入的内容做修正<BR/>
	 * 1.删除所有的半角空格[ ]<BR/>
	 * 2.删除所有的全角空格[　]<BR/>
	 * @param input
	 * @return
	 */
	private static String modifyContent(String input) {
		String result = input.trim();// replaceAll("[ ]*", "").replaceAll("[　]*", "");
		return result;
	}

	/**
	 * 获得随机奖励
	 * // 颜色,物品,数量,无额外条件获得概率,有额外条件获得概率@0~X个物品分别的概率|另一个|另2个|另3个
	 * &
	 * @param relPrizes
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskPrize> getRandomPrizes(String[] prizes, long id) throws Exception {
		List<TaskPrize> list = new ArrayList<TaskPrize>();
		for (int i = 0; i < prizes.length; i++) {
			String[] subs = prizes[i].split("\\|");
			// 颜色,物品,数量,无额外条件获得概率,有额外条件获得概率,@0~X个物品分别的概率
			int[] prizeColor = new int[subs.length];
			String[] prizeName = new String[subs.length];
			long[] prizeNum = new long[subs.length];
			double[] baseRate = new double[subs.length];
			double[] highRate = new double[subs.length];
			double[][] everyNumRate = new double[subs.length][];

			try {
				double totalRateCheck = 0;
				for (int j = 0; j < subs.length; j++) {
					String[] bases = subs[j].split("@");// 基础数据,随机各个数量的几率
					if (bases.length > 0 && bases.length <= 2) {
						String[] baseMsg = bases[0].split(",");// 颜色,物品,数量,无额外条件获得概率,有额外条件获得概率
						if (baseMsg.length != 5) {
							logger.error("[任务加载错误][任务奖励-随机][ID={}][错误项:{}]", new Object[] { id, bases[0] });
							notices.append("[任务加载错误][任务奖励-随机][ID=").append(id).append("]<BR/>");
							throw new Exception();
						}
						prizeColor[j] = Integer.valueOf(baseMsg[0]);
						prizeName[j] = baseMsg[1];
						prizeNum[j] = Integer.valueOf(baseMsg[2]);
						baseRate[j] = df.parse(baseMsg[3]).doubleValue();
						highRate[j] = df.parse(baseMsg[4]).doubleValue();
						totalRateCheck += highRate[j];
						// 处理数量概率
						if (bases.length == 2) {// 处理数量随机
							String[] everyRate = bases[1].split(",");
							if (everyRate.length == prizeNum[j]) {
								everyNumRate[j] = new double[everyRate.length];
								double evetyNumRateCheck = 0;// 检查所有几率只和是不是1
								for (int k = 0; k < everyRate.length; k++) {
									everyNumRate[j][k] = df.parse(everyRate[k]).doubleValue();
									evetyNumRateCheck += everyNumRate[j][k];
								}

								if (evetyNumRateCheck != 1) {
									logger.error("[任务加载错误][任务奖励-随机][单个物品几率配置异常,和不是1:{}][ID={}][错误项:{}]", new Object[] { evetyNumRateCheck, id, prizes[i] });
									notices.append("[任务加载错误][任务奖励-随机][单个物品几率配置异常][ID=").append(id).append("]<BR/>");
									throw new Exception();
								}
							}
						}
					} else {
						logger.error("[任务加载错误][任务奖励-随机][ID={}][错误项:{}]", new Object[] { id, prizes[i] });
						notices.append("[任务加载错误][任务奖励-随机][ID=").append(id).append("]<BR/>");
						throw new Exception();
					}
				}
				if (totalRateCheck != 1) {
					logger.error("[任务加载错误][任务奖励-随机][组内随机配置错误: {}][ID={}][错误项:{}]", new Object[] { totalRateCheck, id, prizes[i] });
					notices.append("[任务加载错误][任务奖励-随机][组内随机配置错误][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
			} catch (Exception e) {
				logger.error("[任务加载错误][任务奖励-随机][组内随机配置错误B:", e);
				notices.append("[任务加载错误][任务奖励-随机][组内随机配置错误B][ID=").append(id).append("]<BR/>");
				throw e;
			}

			TaskPrize taskPrize = TaskPrizeOfRandomArticle.createTaskPrize(prizeColor, prizeName, prizeNum, baseRate, highRate, everyNumRate);
			list.add(taskPrize);
		}
		return list;
	}

	/**
	 * 任务奖励 BUFF 可选
	 * 1,二倍经验| 2,三倍经验@1&3,双倍攻击
	 * @param prizes
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskPrize> getBuffPrizes(String[] prizes, long id) throws Exception {
		List<TaskPrize> list = new ArrayList<TaskPrize>();
		for (int i = 0; i < prizes.length; i++) {
			// 检测奖励目标数量
			int totalNum = -1;
			String[] prizeName = new String[0];
			int[] prizeColor = new int[0];
			String[] numberTest = prizes[i].split("@");
			if (numberTest.length <= 2) {// 是不可选择的奖励
				if (numberTest.length == 2) {
					totalNum = Integer.valueOf(numberTest[1]);
				}
				String[] subs = numberTest[0].split("\\|");// 1,二倍经验
				prizeName = new String[subs.length];
				prizeColor = new int[subs.length];
				for (int j = 0; j < subs.length; j++) {
					String[] values = subs[j].split(",");
					if (values.length == 2) {
						prizeColor[j] = Integer.valueOf(values[0]);
						prizeName[j] = values[1];
					} else {
						logger.error("[任务加载错误][任务奖励-BUFF][ID={}][错误项:{}]", new Object[] { id, prizes[i] });
						notices.append("[任务加载错误][任务奖励-BUFF][ID=").append(id).append("]<BR/>");
						throw new Exception();
					}
				}
			} else {
				logger.error("[任务加载错误][任务奖励-BUFF][ID={}][错误项:{}]", new Object[] { id, prizes[i] });
				notices.append("[任务加载错误][任务奖励-BUFF B][ID=").append(id).append("]<BR/>");
				throw new Exception();
			}
			TaskPrize prize = TaskPrizeOfBuff.createTaskPrize(prizeName, prizeColor, totalNum);
			list.add(prize);
		}
		return list;
	}

	/**
	 * 加载任务物品奖励
	 * @param relPrizes
	 *            1，杯具，6|1,餐具,7|1,具具,8@2&1,洗具,2
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskPrize> getArticlePrizes(String[] prizes, long id) throws Exception {
		List<TaskPrize> list = new ArrayList<TaskPrize>();
		for (int i = 0; i < prizes.length; i++) {
			// 检测奖励目标数量
			int totalNum = -1;
			String[] prizeName = new String[0];
			int[] prizeColor = new int[0];
			long[] prizeNum = new long[0];
			String[] numberTest = prizes[i].split("@");
			if (numberTest.length <= 2) {// 是不可选择的奖励
				if (numberTest.length == 2) {
					totalNum = Integer.valueOf(numberTest[1]);
				}
				String[] subs = numberTest[0].split("\\|");// 1，杯具，6
				prizeName = new String[subs.length];
				prizeNum = new long[subs.length];
				prizeColor = new int[subs.length];
				for (int j = 0; j < subs.length; j++) {
					String[] values = subs[j].split(",");
					if (values.length == 3) {
						prizeColor[j] = Integer.valueOf(values[0]);
						prizeName[j] = values[1];
						prizeNum[j] = Integer.valueOf(values[2]);
					} else {
						logger.error("[任务加载错误][任务奖励-道具A][ID={}][错误项:{}]", new Object[] { id, prizes[i] });
						notices.append("[任务加载错误][任务奖励-道具 A][ID=").append(id).append("]<BR/>");
						throw new Exception();
					}
				}
			} else {
				logger.error("[任务加载错误][任务奖励-道具B][ID={}][错误项:{}]", new Object[] { id, prizes[i] });
				notices.append("[任务加载错误][任务奖励-道具 B][ID=").append(id).append("]<BR/>");
				throw new Exception();
			}
			TaskPrize prize = TaskPrizeOfArticle.createTaskPrize(prizeColor, prizeName, prizeNum, totalNum);
			list.add(prize);
		}
		return list;
	}

	/**
	 * 
	 * @param targets
	 * @param id
	 * @return
	 */
	private static TaskTarget getMonsterRandomNum(String target, long id) {
		try {
			if (target == null || "".equals(target)) {
				return null;
			}
			String[] values = StringTool.string2Array(target, ",", String.class);
			TaskTargetOfMonsterRandomNum monsterRandomNum = new TaskTargetOfMonsterRandomNum(Integer.valueOf(values[0]), values[1], Integer.valueOf(values[2]), Integer.valueOf(values[3]));
			return monsterRandomNum;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static TaskTarget getCollectionTarget(String target, long id) throws Exception {
		TaskTargetOfCollection collection = null;
		try {
			String[] values = StringTool.string2Array(target, ",", String.class);
			String[] mapName = { values[0] };
			String[] targetName = { values[2] };
			int targetColor = Integer.valueOf(values[1]);
			int targetNum = Integer.valueOf(values[3]);
			collection = new TaskTargetOfCollection(mapName, targetName, targetColor, targetNum);
		} catch (Exception e) {
			logger.error("[任务加载异常][采集目标][任务Id:{}]", new Object[] { id });
			notices.append("[任务加载错误][采集目标][ID=").append(id).append("]<BR/>");
			throw e;
		}
		return collection;
	}

	/**
	 * 社会关系
	 * 
	 * @param targets
	 * @param id
	 * @return
	 */
	private static List<TaskTarget> getRelationTargets(String[] targets, long id) {
		try {
			List<TaskTarget> list = new ArrayList<TaskTarget>();
			int type = Integer.valueOf(targets[0]);
			TaskTargetOfRelation target = new TaskTargetOfRelation(type);
			list.add(target);
			return list;
		} catch (NumberFormatException e) {
			logger.error("[任务加载错误][社会关系][数量不符][ID=" + id + "][错误项:" + targets[0] + "]", e);
			notices.append("[任务加载错误][社会关系][数量不符][ID=").append(id).append("]<BR/>");
			throw e;
		}
	}

	private static TaskPrize getCareerArtcleTarget(String content) {
		String[] values = content.split("\\|");
		String[] names = new String[values.length];
		int[] colors = new int[values.length];
		long[] nums = new long[values.length];

		try {
			for (int i = 0; i < values.length; i++) {
				String value = values[i];

				String[] temp = StringTool.string2Array(value, ",", String.class);
				colors[i] = Integer.valueOf(temp[0]);
				names[i] = temp[1];
				nums[i] = Long.valueOf(temp[2]);
			}
			return TaskPrizeOfCareer.createTaskPrize(names, colors, nums);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 探索
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private List<TaskTarget> getDiscoveryTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		// 地图名,区域名|地图名,区域名
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			String[] subs = value.split("\\|");
			String[] mapName = new String[subs.length];
			String[] targetArea = new String[subs.length];
			// 颜色,物品名,数量,地图名,区域名
			for (int j = 0; j < subs.length; j++) {
				String sub = subs[j];
				String[] values = sub.split(",");
				if (values.length == 2) {
					mapName[j] = values[0];
					targetArea[j] = values[1];
				} else {
					logger.error("[任务加载错误][探索任务][数量不符,不是2][ID={}][错误项:{}]", new Object[] { id, value });
					notices.append("[任务加载错误][探索任务][数量不符,不是2][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
			}
			TaskTargetOfDiscovery taskTarget = new TaskTargetOfDiscovery(mapName, targetArea);
			list.add(taskTarget);
		}
		return list;

	}

	/**
	 * 其他任务完成
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getTaskDeliverTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		try {
			for (int i = 0; i < targets.length; i++) {
				String value = targets[i];
				String[] subs = value.split("\\|");
				TaskTargetOfTaskDeliver taskTarget = new TaskTargetOfTaskDeliver(subs);
				list.add(taskTarget);
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	private List<TaskTarget> getReChargeTargets(String[] relTarget, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 杀人做为任务目标
	 * 20，-5，0（-1不限制,0非本国)
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getKillPlayerTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			String[] values = value.split(",");
			int maxLv = -1;
			int minLv = -1;
			int limit = -1;
			int num = 1;
			if (values.length == 4) {
				maxLv = Integer.valueOf(values[0]);
				minLv = Integer.valueOf(values[1]);
				limit = Integer.valueOf(values[2]);
				num = Integer.valueOf(values[3]);
			} else {
				logger.error("[任务加载错误][杀人][数量不符,不是3][ID={}][错误项:{}]", new Object[] { id, value });
				notices.append("[任务加载错误][杀人][数量不符,不是3][ID=").append(id).append("]<BR/>");
				throw new Exception();
			}

			TaskTargetOfKillPlayer taskTarget = new TaskTargetOfKillPlayer(minLv, maxLv, limit, num);
			list.add(taskTarget);
		}
		return list;
	}

	/**
	 * 护送NPC
	 * NPCID,目标所在地图,目标名字
	 * NPCname
	 * @param relTarget
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getConvoyNPCTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			// String[] subs = value.split("\\|");
			// String[] targetName = new String[subs.length];
			String[] values = StringTool.string2Array(value, ",", String.class);
			int refreshNPCId = 0;
			String[] targetName = new String[1];
			String[] targetMapName = new String[1];
			if (values.length == 3) {
				refreshNPCId = Integer.valueOf(values[0]);
				targetMapName[0] = values[1];
				targetName[0] = values[2];
			} else {
				logger.error("[任务加载错误][护送NPC][数量不符,不是1][ID={}][错误项:{}]", new Object[] { id, value });
				notices.append("[任务加载错误][护送任务][数量不符,不是1][ID=").append(id).append("]<BR/>");
				throw new Exception();
			}

			TaskTargetOfConvoyNPC taskTarget = new TaskTargetOfConvoyNPC(refreshNPCId, targetName, targetMapName);
			list.add(taskTarget);
		}
		return list;
	}

	/**
	 * 和NPC对话作为任务目标
	 * 地图名，NPC名
	 * @param relTarget
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getTalkToNPCTargets(String[] targets, long id,int country) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		try {
			for (int i = 0; i < targets.length; i++) {
				String value = targets[i];
				String[] subs = value.split("\\|");
				String[] targetName = new String[subs.length];
				String[] targetMapName = new String[subs.length];
				for (int j = 0; j < subs.length; j++) {
					String[] values = subs[j].split(",");
					targetMapName[j] = values[0];
					targetName[j] = values[1];
				}
				TaskTargetOfTalkToNPC taskTarget = new TaskTargetOfTalkToNPC(targetMapName, targetName,country);
				list.add(taskTarget);
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info("[任务加载错误][和NPC 对话 错误] ID ：" + id, e);
			notices.append("[任务加载错误][和NPC 对话 错误][数量不符,不是1][ID=").append(id).append("]<BR/>");
			throw e;
		}
		return list;
	}

	/**
	 * 杀等级段的怪物
	 * 颜色,等级下限,等级上限,数目
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getKillMonsterLvTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			String[] subs = value.split("\\|");
			int[] minLv = new int[subs.length];
			int[] maxLv = new int[subs.length];
			int targetColor = -1;
			int targetNum = 0;
			for (int j = 0; j < subs.length; j++) {
				String[] values = subs[j].split(",");
				if (values.length == 4) {
					targetColor = Integer.valueOf(values[0]);
					minLv[j] = Integer.valueOf(values[1]);
					maxLv[j] = Integer.valueOf(values[2]);
					targetNum = Integer.valueOf(values[3]);
				} else {
					logger.error("[任务加载错误][杀等级段怪][数量不符,不是4][ID={}][错误项:{}]", new Object[] { id, value });
					notices.append("[任务加载错误][杀等级段怪][数量不符,不是4][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
			}
			TaskTargetOfMonsterLv taskTarget = new TaskTargetOfMonsterLv(targetColor, targetNum, minLv, maxLv);
			list.add(taskTarget);
		}
		return list;
	}

	/**
	 * 杀确定怪
	 * 1,猴子,7|1,猩猩,7
	 * 1,狗熊,2
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getKillMonsterTargets(String[] targets, long id,int country) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			String[] subs = value.split("\\|");
			String[] targetName = new String[subs.length];
			int targetColor = -1;
			int targetNum = 0;
			for (int j = 0; j < subs.length; j++) {
				String[] values = subs[j].split(",");
				if (values.length == 3) {
					targetColor = Integer.valueOf(values[0]);
					targetName[j] = values[1];
					targetNum = Integer.valueOf(values[2]);
				} else {
					logger.error("[任务加载错误][杀确定怪][数量不符,不是3][ID={}][错误项:{}]", new Object[] { id, value });
					notices.append("[任务加载错误][杀确定怪][数量不符,不是3][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
			}
			TaskTargetOfMonster taskTarget = new TaskTargetOfMonster(targetName, targetColor, targetNum,country);
			list.add(taskTarget);

		}
		return list;
	}

	/**
	 * 使用物品配置
	 * 颜色,物品名,数量,地图名,区域名|颜色,物品名,数量,地图名,区域名
	 * 颜色,物品名,数量,地图名,区域名
	 * @param targets
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static List<TaskTarget> getUseArticleTargets(String[] targets, long id) throws Exception {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		// 颜色,物品名,数量,地图名,区域名|颜色,物品名,数量,地图名,区域名
		for (int i = 0; i < targets.length; i++) {
			String value = targets[i];
			// TaskTargetOfUseArticle taskTarget = new TaskTargetOfUseArticle(targetName, mapName, targetX, targetY, targetColor, targetNum)
			String[] subs = value.split("\\|");
			String[] targetName = new String[subs.length];
			String[] mapName = new String[subs.length];
			String[] targetArea = new String[subs.length];
			int x = 0;
			int y = 0;
			int targetColor = -1;
			int targetNum = 0;

			// 颜色,物品名,数量,地图名,x,y
			for (int j = 0; j < subs.length; j++) {
				String sub = subs[j];
				String[] values = sub.split(",");
				if (values.length == 5) {
					targetColor = Integer.valueOf(values[0]);
					targetName[j] = values[1];
					targetNum = Integer.valueOf(values[2]);
					mapName[j] = values[3];
//					x = Integer.parseInt(values[4]);
//					y = Integer.parseInt(values[5]);
					targetArea[j] = values[4];
				} else {
					logger.error("[任务加载错误][使用物品][数量不符,不是6][ID={}][错误项:{}]", new Object[] { id, value });
					notices.append("[任务加载错误][使用物品][数量不符,不是6][ID=").append(id).append("]<BR/>");
					throw new Exception();
				}
			}
//			TaskTargetOfUseArticle taskTarget = new TaskTargetOfUseArticle(targetName, targetColor, targetNum, x, y , mapName);
			TaskTargetOfUseArticle taskTarget = new TaskTargetOfUseArticle(targetName, targetColor, targetNum, targetArea, mapName);
			list.add(taskTarget);
		}
		return list;
	}

	/**
	 * 物品作为目标 且完成的时候需要删除物品
	 * @param targets
	 * @return
	 */
	private static List<TaskTarget> getArticleTargetAnddeletes(String[] targets) {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		for (int i = 0; i < targets.length; i++) {
			String[] values = targets[i].split(",");
			TaskTargetOfGetArticleAndDelete andDelete = new TaskTargetOfGetArticleAndDelete(new String[] { values[1] }, Integer.valueOf(values[0]), Integer.valueOf(values[2]));
			list.add(andDelete);
		}
		return list;
	}

	/**
	 * 物品作为目标
	 * 1，杯具,7@1,猴子,20%|1,餐具,7@猩猩,20% & 1,洗具,2@狗熊,20%
	 * @param value
	 * @return
	 */
	private static List<TaskTarget> getArticleTargets(String[] targets,int country) {
		List<TaskTarget> list = new ArrayList<TaskTarget>();
		// 1,杯具,7@1,猴子,20%|1,餐具,7@猩猩,20%
		// 1,洗具,2@狗熊,20%
		try {
			for (int i = 0; i < targets.length; i++) {
				String value = targets[i];
				// 每循环一次生成一个target
				String[] subs = StringTool.string2StringArr(value, "\\|");
				String[] targetName = new String[subs.length];
				String[][] monsterNames = new String[subs.length][];
				int[][] monsterColors = new int[subs.length][];
				double[][] drops = new double[subs.length][];
				int targetColor = -1;
				int targetNum = 0;

				// 1，杯具,7@1,猴子,20%@1,猴子,20%
				// 1，杯具,7
				for (int j = 0; j < subs.length; j++) {
					String sub = subs[j];
					int _index = sub.indexOf("@");
					String baseConf;
					if (_index == -1) {
						baseConf = sub;
					} else {
						baseConf = sub.substring(0, _index);
					}

					String[] bases = baseConf.split(",");
					targetColor = Integer.valueOf(bases[0]);
					targetName[j] = bases[1];
					targetNum = Integer.valueOf(bases[2]);
					if (_index == -1) {
						continue;
					}
					sub = sub.substring(_index + 1, sub.length());

					String[] monsters = sub.split("@");
					monsterColors[j] = new int[monsters.length];
					monsterNames[j] = new String[monsters.length];
					drops[j] = new double[monsters.length];
					for (int k = 0; k < monsters.length; k++) {
						String[] monster = monsters[k].split(",");
						if (monster.length == 3) {
							monsterColors[j][k] = Integer.valueOf(monster[0]);
							monsterNames[j][k] = monster[1];
							drops[j][k] = df.parse(monster[2]).doubleValue();
						}
					}
				}
				TaskTargetOfGetArticle targetOfGetArticle = new TaskTargetOfGetArticle(targetName, targetColor, targetNum, monsterNames, monsterColors, drops,country);
				list.add(targetOfGetArticle);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过配置获得任务的目标
	 * 该方法返回规定配置的每一个target的配置
	 * 即 外部程序会生成数组长度个target
	 * @param targetType
	 * @param value
	 * @return
	 */
	public static String[] turn2TargetString(String value) {
		return turn2TargetString(value, "&");
	}

	public static String[] turn2TargetString(String value, String separator) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		return value.split(separator);
	}

	/**
	 * 等级是否在限制的等级段内
	 * @param minLimit
	 * @param maxLimit
	 * @param currGrade
	 * @return
	 */
	public static boolean gradeFit(int minLimit, int maxLimit, int currGrade) {
		int modifyMin = minLimit == -1 ? currGrade : minLimit;
		int modifyMax = maxLimit == -1 ? currGrade : maxLimit;
		return currGrade <= modifyMax && currGrade >= modifyMin;
	}

	private void checkExist() {
		for (Iterator<String> itor = nextTaskMap.keySet().iterator(); itor.hasNext();) {
			String taskGroupName = itor.next();
			List<Task> task = getTaskGrouopMap().get(taskGroupName);
			if (task == null) {
				notices.append("<font color=red>");
				notices.append("[致命错误][任务不存在:").append(taskGroupName).append("]");
				notices.append("</font><BR/>");
			}
		}
	}

	public static List<Long> needAnswerTasks = new ArrayList<Long>();

	public void init() throws Exception {
		
		deliverTaskEm = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
		instance = this;
		instance.loadFilePoi();
		needAnswerTasks.add(1075L);
		needAnswerTasks.add(1106L);
		needAnswerTasks.add(1144L);
		needAnswerTasks.add(1175L);
		needAnswerTasks.add(1196L);
		needAnswerTasks.add(1224L);
		needAnswerTasks.add(1238L);
		needAnswerTasks.add(1248L);
		needAnswerTasks.add(1269L);
		checkExist();
		MConsoleManager.register(getInstance());
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		deliverTaskEm.destroy();
	}

	public void setNpcManager(MemoryNPCManager npcManager) {
		this.npcManager = npcManager;
	}

	public MemoryNPCManager getNpcManager() {
		return npcManager;
	}

	public List<Task> getFristTasksOfAlLine() {
		return fristTasksOfAlLine;
	}

	public void setFristTasksOfAlLine(List<Task> fristTasksOfAlLine) {
		this.fristTasksOfAlLine = fristTasksOfAlLine;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	/**
	 * 通知客户端由于任务 获得了物品
	 * @param player
	 * @param article
	 * @param num
	 * @param colorType
	 */
	public void addArticleAndNoticeClient(Player player, Article article, long num, int colorType) {
		try {
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug(player.getLogString() + "[获得物品奖励] [物品:{}] [颜色:{}] [数量:{}]", new Object[] { article.getName(), colorType, num });
			}
			if (article == null || player == null) {
				return;
			}
			List<ArticleEntity> list = new ArrayList<ArticleEntity>();
			int clickType = 2;
			StringBuffer sbf = new StringBuffer(Translate.text_boothsale_010 + ":");
			if (article.isOverlap()) {// 可堆叠
				try {
					ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, player, colorType, (int) num, true);
					for (int k = 0; k < num; k++) {
						if (articleEntity instanceof PetEggPropsEntity) {
							((PetEggPropsEntity) articleEntity).setEggType((byte) 1);
						}
						list.add(articleEntity);
					}
					sbf.append(StringTool.toClientClickMsg(getColorValue(colorType, article), article.getName(), articleEntity.getId(), clickType)).append("X").append(num);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				long eid = 0;
				for (int k = 0; k < num; k++) {
					try {
						ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, player, colorType, 1, true);
						if (k == 0) {
							eid = articleEntity.getId();
						}
						if (articleEntity instanceof PetEggPropsEntity) {
							((PetEggPropsEntity) articleEntity).setEggType((byte) 1);
						}
						list.add(articleEntity);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				sbf.append(StringTool.toClientClickMsg(getColorValue(colorType, article), article.getName(), eid, clickType)).append("X").append(num);
			}

			if (list.size() > 0) {
				boolean success = player.putAll(list.toArray(new ArticleEntity[0]), "任务");

				if (TaskSubSystem.logger.isDebugEnabled()) {
					TaskSubSystem.logger.debug(player.getLogString() + "[获得物品奖励] [物品:{}] [颜色:{}] [数量:{}] [成功:{}]", new Object[] { article.getName(), colorType, num, success });
				}
				if (success) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, sbf.toString());
					player.addMessageToRightBag(req);
					for (int i = 0; i < list.size(); i++) {
						ArticleStatManager.addToArticleStat(player, null, list.get(i), ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "任务", "");
					}
				} else {
					TaskSubSystem.logger.error(player.getLogString() + "[通过任务获得物品:{}] [失败]", new Object[] { article.getName() });
				}
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("[奖励物品错误]", e);
		}
	}

	private int getColorValue(int colorType, Article a) {
		if (a instanceof Equipment) {
			return ArticleManager.color_equipment[colorType];
		} else {
			return ArticleManager.color_article[colorType];
		}
	}

	public static int getRandom(TaskTarget target) {
		if (target instanceof RandomNum) {
			RandomNum randomNum = (RandomNum) target;
			return RANDOM.nextInt(randomNum.getMaxNum() - randomNum.getMinNum()) + randomNum.getMinNum();
		}
		return 0;
	}

	public HashMap<String, List<Task>> getTaskBigCollections() {
		return taskBigCollections;
	}

	public void setTaskBigCollections(HashMap<String, List<Task>> taskBigCollections) {
		this.taskBigCollections = taskBigCollections;
	}

	public ArticleEntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(ArticleEntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ArticleManager getArticleManager() {
		return articleManager;
	}

	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager = articleManager;
	}

	/**
	 * 在NPC上发布任务(新增任务)
	 * @param gameName
	 * @param npcName
	 * @param taskNames
	 */
	public void releaseTask(Game game, String npcName, String... taskNames) {
		List<Task> releaseTasks = new ArrayList<Task>();
		NPC npc = null;
		for (String taskName : taskNames) {
			Task task = getTask(taskName);
			if (task != null) {
				inner: for (LivingObject livingObject : game.getLivingObjects()) {// 这要改===先循环NPC 后循环任务
					if (livingObject instanceof NPC) {
						NPC tempNPC = (NPC) livingObject;
						if (tempNPC.getName().equals(npcName)) {
							npc = tempNPC;
							break inner;
						}
					}
				}
				if (game != null && npc != null) {
					if (!gameReleaseTasks.containsKey(game)) {
						gameReleaseTasks.put(game, new HashMap<String, List<Task>>());
					}
					if (!gameReleaseTasks.get(game).containsKey(npc.getName())) {
						gameReleaseTasks.get(game).put(npc.getName(), new ArrayList<Task>());
					}
					gameReleaseTasks.get(game).get(npc.getName()).add(task);
					releaseTasks.add(task);
				} else {
					if (TaskSubSystem.logger.isWarnEnabled()) logger.warn("【发布任务】没找到Npc{}", new Object[] { npcName });
				}
			} else {
				if (TaskSubSystem.logger.isInfoEnabled()) logger.info("【发布任务】没找到任务{}", new Object[] { taskName });
			}
		}
		if (logger.isDebugEnabled()) {
			for (Task task : releaseTasks) {
				if (TaskSubSystem.logger.isDebugEnabled()) logger.debug("【确实发布】" + task.getName());
			}
		}
		noticePlayerTaskChange(game);
	}

	/**
	 * 取消发布任务
	 * @param game
	 * @param npcName
	 * @param taskNames
	 */
	public void cancelReleaseTask(Game game, String npcName, String... taskNames) {
		if (logger.isInfoEnabled()) logger.info("[取消发布任务:{}]", new Object[] { Arrays.toString(taskNames) });
		if (gameReleaseTasks.containsKey(game)) {
			List<Task> needCancel = new ArrayList<Task>();
			for (String taskName : taskNames) {
				inner: for (Task task : gameReleaseTasks.get(game).get(npcName)) {
					if (task.getName().equals(taskName)) {
						needCancel.add(task);
						break inner;
					}
				}
			}
			for (Task task : needCancel) {
				gameReleaseTasks.get(game).get(npcName).remove(task);
			}
			noticePlayerTaskChange(game);
		}
	}

	/**
	 * 通知用户任务变化
	 * @param game
	 */
	private void noticePlayerTaskChange(Game game) {
		for (LivingObject livingObject : game.getLivingObjects()) {
			if (livingObject instanceof Player) {
				Player player = (Player) livingObject;
				// TODO 此处可优化，根据变化的任务发更改，但考虑到任务量不会太大，所以暂时查询所有
				FunctionNPC fns[] = getFunctionNPCsByGame(game, player);
				FUNCTION_NPC_RES res = new FUNCTION_NPC_RES(GameMessageFactory.nextSequnceNum(), fns);
				player.addMessageToRightBag(res);
			}
		}
	}

	/**
	 * 查询某人某个任务的额外目标。
	 * @param player
	 * @param task
	 */
	public static void checkExcessTarget(TaskEntity taskEntity, TaskAction action) {
		if (action.getTargetType().isBringExcess() && taskEntity.getTask().isHasExcess()) {
			if (!taskEntity.isExcess()) {// 没达到额外条件
				Task task = taskEntity.getTask();
				String excessName = task.getExcessTarget();
				double excessRate = task.getExcessTargetRate();
				Article article = ArticleManager.getInstance().getArticle(excessName);
				if (article == null) {
					if (logger.isWarnEnabled()) logger.warn("[处理角色{}的额外物品目标][任务:{}][物品不存在{}]", new Object[] { taskEntity.getOwner().getName(), taskEntity.getTask().getName(), excessName });
					return;
				}
				double result = RANDOM.nextDouble();
				if (logger.isDebugEnabled()) {
					logger.debug("[处理角色{}的额外物品目标][随机结果:{}][几率:{}]", new Object[] { taskEntity.getOwner().getName(), result, excessRate });
				}
				if (result <= excessRate) {// 得到该物品
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, taskEntity.getOwner(), ArticleManager.COLOR_WHITE, 1, true);
						boolean sucess = taskEntity.getOwner().putToKnapsacks(ae, "任务");
						if (sucess) {
							taskEntity.getOwner().sendNotice(Translate.translateString(Translate.text_task_038, new String[][] { { Translate.STRING_1, excessName } }));
							taskEntity.setExcess(true);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String getMConsoleName() {
		return "任务管理器";
	}

	@Override
	public String getMConsoleDescription() {
		return "针对任务一些功能";
	}

	public static void main(String[] args) throws Exception {
		TaskManager manager = new TaskManager();
		TaskManager.instance = manager;
		manager.setFile("d:/taskTemplet.xlsx");
		manager.loadFilePoi();
	}
}
