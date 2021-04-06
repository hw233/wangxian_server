package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.activity.pickFlower.MagicWeaponNpc;
import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.activity.wolf.JoinNumData;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.client.FunctionNPC;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.core.event.ReconnectEvent;
import com.fy.engineserver.core.event.TransferGameEvent;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.MapPolyArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.core.tool.GameTimeStaticModule;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.AvataProps;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.FlopSchemeEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.Knapsack_TimeLimit;
import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.UsingPropsAgent.PropsCategoryCoolDown;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.deal.service.DealCenter;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.city.CityConfig;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerEnterScene;
import com.fy.engineserver.exception.KnapsackFullException;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanBornPoint;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.NeedRewriteText;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Knapsack_ConfirmAddCell;
import com.fy.engineserver.menu.Option_RepairAllEquipment;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_ForLuck;
import com.fy.engineserver.menu.cave.CaveOption;
import com.fy.engineserver.message.*;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfDiscovery;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.operating.SystemAnnouncementManager;
import com.fy.engineserver.operating.activitybuff.ActivityBuffItemManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.playerAims.model.PlayerAimModel;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.septstation.NpcStation;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.shortcut.ShortcutAgent;
import com.fy.engineserver.sprite.AbstractSummoned;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.EntitySummoned;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.effect.LinearEffectSummoned;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.AppointedAttackNPC;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.ChestFightNPC;
import com.fy.engineserver.sprite.npc.DisasterFireNPC;
import com.fy.engineserver.sprite.npc.DoorNpc;
import com.fy.engineserver.sprite.npc.FireWallNPC;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.sprite.npc.FrunaceNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.OreNPC;
import com.fy.engineserver.sprite.npc.SeedNpc;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.time.Timer;
import com.fy.engineserver.tournament.manager.TournamentManager;
import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 游戏主场景
 * 
 * 
 * 
 * 
 * 描述一个游戏的地图， 包括地图中所有生物的列表， 包括一个主线程, <br>
 * 包括一个需要处理的消息的队列，
 * 
 * 还包括心跳的频率设置等。
 * 
 * 主线程每一次心跳： <br>
 * 1）处理所有的在消息队列中的消息，所有的消息都是用来改变服务器端对象的属性，<br>
 * 不直接发送消息给客户端。<br>
 * 2) 对每一个地图中生物，执行心跳函数 <br>
 * 3）对每一个地图中的有视图的生物，更新其视图中其他生物的状态，包括新进入视图，<br>
 * 离开视图，属性的改变等。
 * 
 * 2009-06-13，： 1. 修改了刘勇的代码，去掉了在生物身上保留可见生物的列表， 因为服务器要维护这个信息，工作量实在太大，过于浪费。
 * 
 * 2. 将上一次心跳的位置，作为重要的信息，在心跳函数最后设置。 而不是在各自生物的心跳函数一开始设置。因为在生物心跳之前，
 * 我们的程序可能会修改生物的位置，导致上一次心跳的位置失去意义。 此修改，解决了由于网络延迟导致客户端看不见身边其他玩家的bug
 * 
 * 
 */
public class Game {
	public static final byte REASON_TRANSPORT = 0;
	public static final byte REASON_WRONG_PATH = 1;
	public static final byte REASON_CLIENT_STOP = 2;
	public static final byte REASON_PLAYER_BEKILLED = 3;
	public static final byte REASON_ENTER_GAME_GET_DOWN_HORSE_STOP = 4;
	// public static Logger logger = Logger.getLogger(Game.class);
	public static Logger logger = LoggerFactory.getLogger(Game.class);

	// 是否检查客户端发出的SET_POSITION_REQ消息是否合法
	public static boolean isCheck_SetPositionReq = true;

	// public static Logger handleMessagelogger = Logger.getLogger("com.fy.engineserver.core.GameHandleMessage");
	public static Logger handleMessagelogger = LoggerFactory.getLogger("com.fy.engineserver.core.GameHandleMessage");

	/**
	 * 保存此场景里的当前心跳所有生物，包括玩家、任务NPC、职能NPC和怪物等
	 */
	private Collection<LivingObject> livingSet = new HashSet<LivingObject>();

	// 此变量每个心跳中重新赋值
	private LivingObject livingObjectArray[] = new LivingObject[0];

	private List<LivingObject> newEnterObjectList = Collections.synchronizedList(new LinkedList<LivingObject>());
	private List<LivingObject> newRemoveObjectList = Collections.synchronizedList(new LinkedList<LivingObject>());
	public DefaultQueue messageQueue;

	protected ArrayList<PlayerInOutGameListener> piogListeners = new ArrayList<PlayerInOutGameListener>();
	// 心跳时间：
	// 本次心跳开始的时间
	private long heartBeatStartTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

	public static String 网关地址 = "119.254.85.172";
	
	public long getLastHeartBeatTime() {
		return heartBeatStartTime;
	}
	
	public String toString() {
		return this.gi.name;
	}

	// 两次心跳的时间间隔
	private long interval;

	public GameInfo gi;
	public GameManager gm;

	public byte country = 0;

	BucketMatrix lastBM;
	BucketMatrix currentBM;

	public long heartbeatCount = 0;
	public long totalCosts[] = new long[10];
	public long collectEnterCosts[] = new long[10];

	MonsterFlushAgent spriteFlushAgent;
	NPCFlushAgent npcFlushAgent;

	HashMap<String, Integer> templateMonsterAndNpcIndexInGame = new HashMap<String, Integer>();
	Sprite templateMonsterAndNpcsInGame[];

	public boolean 村庄战争提示;

	// 战场，只有战场才有此变量的值
	protected BattleField battleField = null;

	public void setBattleField(BattleField bf) {
		battleField = bf;
	}

	public BattleField getBattleField() {
		return battleField;
	}

	// 副本，只有副本才有此变量的值
	protected DownCity downCity;

	public DownCity getDownCity() {
		return downCity;
	}

	public void setDownCity(DownCity downCity) {
		this.downCity = downCity;
	}

	// 千层塔，只有为千层塔的game才会设置此属性
	protected QianCengTa_Ceng qianCengTa_Ceng;

	public void setQianCengTa(QianCengTa_Ceng taCeng) {
		qianCengTa_Ceng = taCeng;
	}

	public QianCengTa_Ceng getQianCengTa() {
		return qianCengTa_Ceng;
	}

	public Game(GameManager gm, GameInfo gi) {
		this.gm = gm;

		this.gi = gi;

		lastBM = new BucketMatrix(this);
		currentBM = new BucketMatrix(this);

	}

	/** 真实场景名字 */
	public String realSceneName = "";

	public MonsterFlushAgent getSpriteFlushAgent() {
		return spriteFlushAgent;
	}

	public void setSpriteFlushAgent(MonsterFlushAgent spriteFlushAgent) {
		this.spriteFlushAgent = spriteFlushAgent;
	}

	public NPCFlushAgent getNpcFlushAgent() {
		return npcFlushAgent;
	}

	public void setNpcFlushAgent(NPCFlushAgent npcFlushAgent) {
		this.npcFlushAgent = npcFlushAgent;
	}

	public void addPlayerInOutGameListener(PlayerInOutGameListener l) {
		if (this.piogListeners.contains(l) == false) {
			this.piogListeners.add(l);
		}
	}

	public void removePlayerInOutGameListener(PlayerInOutGameListener l) {
		this.piogListeners.remove(l);
	}

	protected void firePlayerEnterGame(Player player) {
		for (int i = 0; i < piogListeners.size(); i++) {
			PlayerInOutGameListener l = piogListeners.get(i);
			l.enterGame(this, player);
		}
		ActivityBuffItemManager abim = ActivityBuffItemManager.getInstance();
		if (abim != null) {
			abim.enterGame(this, player);
		}
	}

	protected void firePlayerLeaveGame(Player player) {
		for (int i = 0; i < piogListeners.size(); i++) {
			PlayerInOutGameListener l = piogListeners.get(i);
			l.leaveGame(this, player);
		}
		ActivityBuffItemManager abim = ActivityBuffItemManager.getInstance();
		if (abim != null) {
			abim.leaveGame(this, player);
		}
		if (logger.isDebugEnabled()) {
			if (logger.isDebugEnabled()) logger.debug("[{}] [{}] [{}] [玩家离开地图]", new Object[] { gi.getName(), player.getUsername(), player.getName() });
		}
	}

	public BucketMatrix getCurrentBucketMatrix() {
		return currentBM;
	}

	public GameInfo getGameInfo() {
		return gi;
	}

	public void init() throws Exception {
		messageQueue = new DefaultQueue(gi.maxQueueSize);

		// if (gi.monsterBornEle != null) {
		spriteFlushAgent = new MonsterFlushAgent(this, gi.monsterBornEle);
		spriteFlushAgent.load();
		// }

		// if (gi.npcBornEle != null) {
		npcFlushAgent = new NPCFlushAgent(this, gi.npcBornEle);
		npcFlushAgent.load();
		// }
		// 收集地图上怪物的种类
		HashMap<Integer, Monster> mapM = new HashMap<Integer, Monster>();
		MemoryMonsterManager sm = (MemoryMonsterManager) gm.getMonsterManager();
		Iterator<MonsterFlushAgent.BornPoint> it = spriteFlushAgent.templateMap.keySet().iterator();
		while (it.hasNext()) {

			MonsterFlushAgent.BornPoint b = it.next();
			Integer categoryId = spriteFlushAgent.templateMap.get(b);
			Monster m = sm.createMonster(categoryId);
			if (m == null) {
				throw new Exception(gi.displayName + ",上的怪物不存在:" + categoryId);
			}
			if (!(m instanceof BossMonster)) {
				mapM.put(categoryId, m);
			}
		}

		// 收集地图上怪物的种类
		HashMap<Integer, NPC> mapN = new HashMap<Integer, NPC>();
		MemoryNPCManager nm = (MemoryNPCManager) gm.getNpcManager();
		Iterator<NPCFlushAgent.BornPoint> itN = npcFlushAgent.templateMap.keySet().iterator();
		while (itN.hasNext()) {

			NPCFlushAgent.BornPoint b = itN.next();
			Integer categoryId = npcFlushAgent.templateMap.get(b);
			NPC m = nm.createNPC(categoryId);
			mapN.put(categoryId, m);
			if (m == null) {
				throw new Exception(gi.displayName + ",上的怪物不存在:" + categoryId);
			}
			try {
				logger.warn("[地图初始化] [放置NPC] [地图:" + this.gi.name + "] [npc:" + categoryId + "/" + m.getName() + "]");
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[地图初始化] [异常] [" + e + "]");
			}

		}

		this.templateMonsterAndNpcsInGame = new Sprite[mapM.size() + mapN.size()];
		int index = 0;
		Iterator<Integer> it2 = mapM.keySet().iterator();
		while (it2.hasNext()) {
			Integer cid = it2.next();
			Monster m = mapM.get(cid);
			m.objectColor = m.getColor();
			templateMonsterAndNpcsInGame[index] = m;
			this.templateMonsterAndNpcIndexInGame.put("m_" + cid, index);

			index++;
		}

		it2 = mapN.keySet().iterator();
		while (it2.hasNext()) {
			Integer cid = it2.next();
			NPC m = mapN.get(cid);

			templateMonsterAndNpcsInGame[index] = m;
			this.templateMonsterAndNpcIndexInGame.put("n_" + cid, index);

			index++;
		}

	}

	public List<LinearEffectSummoned> getLinearSummoned() {
		List<LinearEffectSummoned> players = new ArrayList<LinearEffectSummoned>();
		try {
			LivingObject los[] = getLivingObjects();
			for (int j = 0; j < los.length; j++) {
				if (los[j] instanceof LinearEffectSummoned) {
					players.add((LinearEffectSummoned) los[j]);
				}
			}
		} catch (Exception e) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("出现异常", e);
		}
		return players;
	}
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		try {
			LivingObject los[] = getLivingObjects();
			for (int j = 0; j < los.length; j++) {
				if (los[j] instanceof Player) {
					players.add((Player) los[j]);
				}
			}
		} catch (Exception e) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("出现异常", e);
		}
		return players;
	}
	public List<BossMonster> getBosss() {
		List<BossMonster> players = new ArrayList<BossMonster>();
		try {
			LivingObject los[] = getLivingObjects();
			for (int j = 0; j < los.length; j++) {
				if (los[j] instanceof BossMonster) {
					players.add((BossMonster) los[j]);
				}
			}
		} catch (Exception e) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("出现异常", e);
		}
		return players;
	}

	public int getNumOfPlayer() {
		int count = 0;
		try {
			LivingObject los[] = getLivingObjects();
			for (int j = 0; j < los.length; j++) {
				if (los[j] instanceof Player) {
					count++;
				}
			}
		} catch (Exception e) {
			if (Game.logger.isWarnEnabled()) Game.logger.warn("出现异常", e);
		}
		return count;
	}

	public LivingObject[] getLivingObjects() {
		return livingObjectArray;
	}

	/**
	 * 系统产生了新的Summoned
	 * 
	 * @param summoned
	 */
	public void addSummoned(AbstractSummoned summoned) {
		synchronized (newEnterObjectList) {
			newEnterObjectList.add(summoned);
		}
	}

	/**
	 * 一个由系统产生的Summoned消失了
	 * 
	 * @param summoned
	 */
	public void removeSummoned(AbstractSummoned summoned) {
		synchronized (newRemoveObjectList) {
			newRemoveObjectList.add(summoned);
		}
	}
	
	/**
	 * 系统产生了新的Summoned
	 * 
	 * @param summoned
	 */
	public void addSprite(Sprite s) {
		if (s != null) {
			s.setGameNames(gi);
			synchronized (newEnterObjectList) {
				newEnterObjectList.add(s);
			}
		} else {
			logger.error("[加入了一个空的Sprite]", new Exception());
		}
	}

	/**
	 * 一个由系统产生的Summoned消失了
	 * 
	 * @param summoned
	 */
	public void removeSprite(Sprite s) {
		synchronized (newRemoveObjectList) {
			newRemoveObjectList.add(s);
		}
	}
	
	public void reEnter(Player player) {
		if (livingSet.contains(player) == false) {
			livingSet.add(player);
		}
	}

	private void playerEnterGame(Player player,String reason) {
		if (player != null) {
			if (player.getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
				try {
					String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
					GamePlayerManager.logger.warn("[TESTENTER] [game...playerEnterGame] [name:"+player.getName()+"\n] "+stacktrace);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			synchronized (newRemoveObjectList) {
				newRemoveObjectList.remove(player);
			}
			if (livingSet.contains(player) == false) livingSet.add(player);

			// 为gm进入玩家游戏中查询外挂设置
			{
				if ("hangmu".equals(player.getName())) {
					player.setObjectOpacity(true);
					player.setObjectScale((short) 1);
					player.setInvulnerable(true);
					player.setDodgeRateOther(10000);
					player.setSpeedA(450);
					player.setFlying(true);
				}
			}
			player.enterGame(this);
			BoothsaleManager.getInstance().msg_cancelBoothSale(player);

			firePlayerEnterGame(player);

			if (logger.isInfoEnabled()) logger.info("[{}] [{}] [{}] [玩家进入地图] [{}] [{}] [{}]", 
					new Object[] { gi.getName(), player.getUsername(), player.getName(),reason, country,player.room});

			// 标记用户刚刚进入地图
			player.setNewlyEnterGameFlag(true);
			EventPlayerEnterScene evt = new EventPlayerEnterScene(player, this);
			EventRouter.getInst().addEvent(evt);
			// 进入禁止骑马地图
			// if (player.getHorse() == 1 && !player.isFlying() &&
			// gi.isLimitMOUNT()) {
			// player.getDownFromHorse();
			// //
			// System.out.println("[username]["+player.getUsername()+"][playerId:"+player.getId()+"]["+player.getName()+"]进入禁止骑马地图");
			// }
			// 进入禁止飞行地图
			if (player.isFlying() && gi.isLimitFLY()) {
				player.getDownFromHorse();
				// System.out.println("[username]["+player.getUsername()+"][playerId:"+player.getId()+"]["+player.getName()+"]进入禁止飞行地图");
			}
			// 进入地图的时候，将死亡标记清空，这样有利于服务器再次检测到用户死亡给客户端发送死亡协议
			// 此操作主要是解决玩家死亡后下线再上线
			if (player.getState() == Player.STATE_DEAD) {
				player.setState(Player.STATE_STAND);
			}

			// 将地图上收集到的怪物数据，发送给客户端
			// 此操作是为了节省流量

			SPRITES_IN_GAME req44 = new SPRITES_IN_GAME(GameMessageFactory.nextSequnceNum(), this.templateMonsterAndNpcsInGame);
			player.addMessageToRightBag(req44);

			// 将门的碰撞区域发送给客户端
			LivingObject los[] = this.getLivingObjects();
			for (int i = 0; i < los.length; i++) {
				if (los[i] instanceof DoorNpc) {
					DoorNpc dn = (DoorNpc) los[i];

					if (dn.isClosed()) {
						short[] xs = dn.getPolygonX();
						short[] ys = dn.getPolygonY();
						MAP_POLYGON_MODIFY_REQ req = new MAP_POLYGON_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, xs, ys);
						player.addMessageToRightBag(req);
					}
				}
			}
			for (int i = 0; i < los.length; i++) {
				if (los[i] instanceof CaveDoorNPC) {
					CaveDoorNPC dn = (CaveDoorNPC) los[i];
					if (dn.isClosed()) {
						short[] xs = dn.getPolygonX();
						short[] ys = dn.getPolygonY();
						MAP_POLYGON_MODIFY_REQ req = new MAP_POLYGON_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, xs, ys);
						player.addMessageToRightBag(req);
					}
				}
			}
			if (村庄战争提示) {
				VillageFightManager vfm = VillageFightManager.getInstance();
				try {
					vfm.给进入游戏的人提示(this, player);
					vfm.给进入地图的村庄战玩家设置复活点(this, player);
				} catch (Exception ex) {
					if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[村庄战] [进地图异常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, ex);
				}
				if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[村庄战] [进地图正常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			} else {
				if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[没有村庄战] [进地图正常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			}
			// 大于3及在做弹出窗口操作
			if (player.getLevel() > 3) {
				SystemAnnouncementManager sam = SystemAnnouncementManager.getInstance();
				if (sam != null) {
					sam.sendAnnouncementToPlayer(player);
				}
			}
			// 清楚回城复活跳转地图的无敌buff
			if (player.是否回城复活) {
				Buff buff = player.getBuffByName(Translate.无敌buff);
				if (buff != null) {
					buff.setInvalidTime(0);
				}
				player.是否回城复活 = false;
			}
			if (gi != null && this.gi.name.equals(RobberyConstant.DUJIEMAP) && !TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())) {
				try {
					player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
				} catch (Exception e) {
				}
			}
			if (gi != null && this.gi.name.equals(BossCityManager.mapname) && BossCityManager.getInstance().isClose()) {
				try {
					player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
				} catch (Exception e) {
				}
			}
			if(gi != null && DownCityManager2.instance.inCityGame(this.gi.name)){
				try {
					if(!DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))){
						if(!DownCityManager2.instance.inTeamCityGame(player)){
							player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
							logger.warn("[处理玩家上线] [单人副本] ["+this.gi.name+"] ["+player.getLogString()+"]");
						}else{
							boolean isTran = false;
							if (player.getTeam() == null || player.getTeam().getCity() == null) {
								isTran = true;
								player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
							} 
							logger.warn("[处理玩家上线] [多人副本] [isTran:"+isTran+"] ["+this.gi.name+"] ["+player.getLogString()+"]");
						}
					}
				} catch (Exception e) {
				}
			}
			if(gi != null && gi.getName().equals("zhanmotianyu")){
				try {
					if(player.getJiazuId() <= 0){
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
						logger.warn("[处理玩家上线] [远征] [家族id不存在] ["+this.gi.name+"] ["+player.getLogString()+"]");
						return;
					}
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
					if(jiazu == null || jiazu.getCity() == null){
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
						logger.warn("[处理玩家上线] [远征] [家族副本结束] [jiazu:"+(jiazu!=null?jiazu.getLogString():"null")+"] ["+this.gi.name+"] ["+player.getLogString()+"]");
						return;
					}
					if(!JiazuManager2.instance.isOpen()){
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
						logger.warn("[处理玩家上线] [远征] [副本结束] ["+this.gi.name+"] ["+player.getLogString()+"]");
						return;
					}
				} catch (Exception e) {
				}
			}
			if(gi != null && gi.getName().equals(BossCityManager.mapname)){
				try {
					if(player.room == null){
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()), true);
						logger.warn("[处理玩家上线] [远征] [家族id不存在] ["+this.gi.name+"] ["+player.getLogString()+"]");
						return;
					}
				} catch (Exception e) {
				}
			}
		}
		if (player.getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [game...playerEnterGame2] [name:"+player.getName()+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void playerLeaveServer(Player player) {
		if (player != null) {

			playerLeaveGame(player);

			player.leaveServer();
		}
	}

	// 玩家重连到服务器，将周围的变化重新发送给客户端
	private void playerReconnectGame(Player player) {
		player.setNewlyEnterGameFlag(true);
	}

	public void playerLeaveGame(Player player) {
		if (player != null) {
			if (livingSet.contains(player)) {

				// livingSet.remove(player);
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(player);
				}

//				if (logger.isInfoEnabled()) logger.info("[{}] [{}] [{}] [玩家离开地图1] [isbooth:{}] [connstate:{}]", new Object[] { gi.getName(), player.getUsername(), player.getName() ,player.isBoothState(),(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")});
				player.leaveGame();

//				if (logger.isInfoEnabled()) logger.info("[{}] [{}] [{}] [玩家离开地图2] [isbooth:{}] [connstate:{}]", new Object[] { gi.getName(), player.getUsername(), player.getName() ,player.isBoothState(),(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")});
				firePlayerLeaveGame(player);

				if (logger.isInfoEnabled()) {
					if (logger.isInfoEnabled()) logger.info("[{}] [{}] [{}] [玩家离开地图3] [isbooth:{}] [connstate:{}]", new Object[] { gi.getName(), player.getUsername(), player.getName() ,player.isBoothState(),(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")});
				}
			}
		}
	}

	private void clearDeathPlayerAndOtherLivingObject() {
		MemoryMonsterManager sm = (MemoryMonsterManager) gm.getMonsterManager();
		MemoryNPCManager nm = (MemoryNPCManager) gm.getNpcManager();

		LivingObject[] os = null;
		synchronized (newRemoveObjectList) {
			os = newRemoveObjectList.toArray(new LivingObject[0]);
			newRemoveObjectList.clear();
		}

		for (int i = 0; i < os.length; i++) {
			livingSet.remove(os[i]);
		}

		Iterator<LivingObject> it = livingSet.iterator();

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		while (it.hasNext()) {
			LivingObject lo = it.next();
			if (lo instanceof Player) {
				Player p = (Player) lo;
				// 处理超时并且网络连接已经关闭的玩家
				if (now - p.getLastRequestTime() > gi.maxCorpseLifeTime && (p.getConn() == null || p.getConn().getState() == Connection.CONN_STATE_CLOSE)) {
					it.remove();

					// 处理团队
					if (p.getTeam() != null) {
						Team tt = p.getTeam();
						tt.removeMember(p, 1);
					}

					p.leaveGame();
					p.leaveServer();

					p.setConn(null);

					this.firePlayerLeaveGame(p);
					Player.sendPlayerAction(p, PlayerActionFlow.行为类型_玩家下线, Translate.text_2988, 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());

				} else if (p.getGame() == null || !p.getGame().equals(gi.name)) {// 复活后离开游戏
					// 处理不在当前地图上的玩家
					it.remove();

					this.firePlayerLeaveGame(p);
				}
			} else if (lo instanceof Sprite) {
				Sprite sprite = (Sprite) lo;
				if (!sprite.isAlive()) {
					it.remove();

					if (sprite instanceof Monster) {
						sm.removeMonster((Monster) sprite);
					} else if (sprite instanceof NPC) {
						nm.removeNPC((NPC) sprite);
					} else if (sprite instanceof Pet) {
						// Pet pet = (Pet) sprite;
						// if(pet.getMaster() != null) {
						// pet.getMaster().notifyPetDisapear();
						// }
					}
				}
			} else if (lo instanceof EntitySummoned) {
				EntitySummoned es = (EntitySummoned) lo;
				if (!es.isAlive()) {
					it.remove();
				}
			} else if (lo instanceof EffectSummoned) {
				EffectSummoned es = (EffectSummoned) lo;
				if (!es.isAlive()) {
					it.remove();

				}
			}
		}

	}

	public void checkMapAreaForPlayerEnterAndExit(LivingObject[] los) {

		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];

				MapArea ma = gi.getMapAreaByPoint(p.x, p.y);
				MapPolyArea[] mpas = gi.getMapPolyAreaByPoint(p.x, p.y);
				List<String> nowArea = new ArrayList<String>();
				List<String> comeArea = new ArrayList<String>();
				List<String> leaveArea = new ArrayList<String>();
				// if(ma != null){
				// nowArea.add(ma.name);
				// }
				if (mpas != null && mpas.length > 0) {
					for (int j = 0; j < mpas.length; j++) {
						MapPolyArea mpa = mpas[j];
						if (mpa != null) {
							nowArea.add(mpa.name);
						}
					}
				}
				if (p.getCurrentMapAreaNames() != null) {
					for (int j = 0; j < p.getCurrentMapAreaNames().length; j++) {
						String area = p.getCurrentMapAreaNames()[j];
						if (!nowArea.contains(area)) {
							leaveArea.add(area);
						}
					}
					for (int j = 0; j < nowArea.size(); j++) {
						String area = nowArea.get(j);
						boolean has = false;
						for (int k = 0; k < p.getCurrentMapAreaNames().length; k++) {
							if (area != null && area.equals(p.getCurrentMapAreaNames()[k])) {
								has = true;
								break;
							}
						}
						if (!has) {
							comeArea.add(area);
						}
					}
				} else {
					comeArea = nowArea;
				}
				if (nowArea.size() > 0) {
					if (comeArea.size() > 0 || leaveArea.size() > 0) {
						p.setCurrentMapAreaNames(nowArea.toArray(new String[0]));
					}
				} else {
					p.setCurrentMapAreaNames(new String[0]);
				}
				if (ma != null && !ma.name.equals(p.getCurrentMapAreaName())) {
					p.setCurrentMapAreaName(ma.name);
					TaskAction actionOfDiscovery = TaskActionOfDiscovery.createTaskAction(p);
					p.dealWithTaskAction(actionOfDiscovery);
				}
				if (ma == null) {
					p.setCurrentMapAreaName(gi.name);
				}
				if (comeArea.size() > 0) {
					for (int j = 0; j < comeArea.size(); j++) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.进入某某区域, new String[][] { { Translate.STRING_1, comeArea.get(j) } }));
						p.addMessageToRightBag(hreq);
					}

					TaskAction actionOfDiscovery = TaskActionOfDiscovery.createTaskAction(p);
					p.dealWithTaskAction(actionOfDiscovery);
				}
				if (leaveArea.size() > 0) {
					for (int j = 0; j < leaveArea.size(); j++) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.离开某某区域, new String[][] { { Translate.STRING_1, leaveArea.get(j) } }));
						p.addMessageToRightBag(hreq);
					}
				}
			}
		}
	}

	private long last_spriteFlushAgent_time = 0;
	private long last_checkMapAreaForPlayerEnterAndExit_time = 0;
	private long last_notifyPlayersAndClearNotifyFlagThroughBucket_time = 0;
	private long last_notifyFlopSchemeEntityAndClearModifyFlag_time = 0;

	/**
	 * 保存此场景里的上一次心跳所有生物，包括玩家、任务NPC、职能NPC和怪物等
	 */
	private Collection<LivingObject> lastLivingSet = new HashSet<LivingObject>();

	public static boolean isCheckBuff = true;
	public long lastCheckBuffTime = 0L;
	/**
	 * 时间统计记录
	 */
	public GameTimeStaticModule timeModule = new GameTimeStaticModule(new long[5], new String[]{"开始", "生物心跳结束", "检查区域进入", "收集窗口变化发送指令", "通知掉落"});
	
	public static boolean openTimeStatic = true;

	public void heartbeat() {

		// 心跳时间
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (openTimeStatic) {
			timeModule.getTimeStatics()[0] = now;
		}
		interval = now - heartBeatStartTime;
		heartBeatStartTime = now;

		// 保存上一次心跳的生物集合，必须放置在这里，并且必须和notifyPlayersAndClearNotifyFlagThroughBucket执行步骤一致
		lastLivingSet.clear();
		lastLivingSet.addAll(this.livingSet);

		// 处理所有的网络消息
		Object obj = null;
		DefaultQueue tempMessageQueue = new DefaultQueue(gi.maxQueueSize);

		while ((obj = messageQueue.pop()) != null) {
			tempMessageQueue.push(obj);
		}
		while ((obj = tempMessageQueue.pop()) != null) {
			handleMessagesAndSetNotifyFlag(obj);
		}

		// 刷新怪物和NPC
		if (now - last_spriteFlushAgent_time > 1000) {
			if (spriteFlushAgent != null) {
				spriteFlushAgent.heartbeat();
			}

			if (npcFlushAgent != null) {
				npcFlushAgent.heartbeat();
			}

			last_spriteFlushAgent_time = now;
		}

		// 所有生物心跳！
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		livingObjectArray = los;

		for (int i = 0; i < los.length; i++) {
			try {
				if (los[i] == null) {
					continue;
				}
				los[i].heartbeat(heartBeatStartTime, interval, this);
			} catch (Throwable e) {
				if (logger.isWarnEnabled()) logger.warn("生物心跳出现异常: " + los[i].getClass(), e);
			}
		}
		if (openTimeStatic) {
			timeModule.getTimeStatics()[1] = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
		// 检查玩家进入某个区域
		if (now - last_checkMapAreaForPlayerEnterAndExit_time > 1000) {
			last_checkMapAreaForPlayerEnterAndExit_time = now;
			checkMapAreaForPlayerEnterAndExit(los);
		}
		if (openTimeStatic) {
			timeModule.getTimeStatics()[2] = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
		// 容错处理
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				if (p.getHp() < 0) p.setHp(0);
				if (p.getHp() > p.getMaxHP()) p.setHp(p.getMaxHP());
				if (p.getMp() < 0) p.setMp(0);
				if (p.getMp() > p.getMaxMP()) p.setMp(p.getMaxMP());
			} else if (los[i] instanceof Sprite) {
				Sprite p = (Sprite) los[i];
				if (p.getHp() < 0) p.setHp(0);
				if (p.getHp() > p.getMaxHP()) p.setHp(p.getMaxHP());
			}

		}
		// 收集每个玩家窗口中的变化，并发送指令通知玩家
		notifyPlayersAndClearNotifyFlagThroughBucket();
		if (openTimeStatic) {
			timeModule.getTimeStatics()[3] = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}

		// 通知怪物掉落
		notifyFlopSchemeEntityAndClearModifyFlag(los);
		if (openTimeStatic) {
			timeModule.getTimeStatics()[4] = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			timeModule.analysis(this, BeatHeartThread.logger);
		}

		// 设置所有生物上一次心跳的位置

		heartbeatCount++;

	}

	protected void notifyFlopSchemeEntityAndClearModifyFlag(LivingObject los[]) {

		for (int i = 0; i < los.length; i++) {
			if (!(los[i] instanceof Monster)) continue;

			Monster sprite = (Monster) los[i];
			FlopSchemeEntity fse = sprite.getFlopSchemeEntity();
			if (fse != null && fse.getMofifyFlag()) { // 通知各个客户端
				Player players[] = fse.getAllPlayers();
				for (int j = 0; j < players.length; j++) {
					// 发消息通知客户端，可以捡东西
					if (this.contains(players[j])) {
						NOTIFY_FLOPAVAILABLE_REQ req = new NOTIFY_FLOPAVAILABLE_REQ(GameMessageFactory.nextSequnceNum(), new long[] { sprite.getId() }, new boolean[] { fse.isPickupAvailable(players[j]) });
						players[j].addMessageToRightBag(req);
					}
				}

				fse.clearMofifyFlag();
			}

		}
	}

	public static boolean isHiddenOpen = true; // 是否开启服务器屏蔽玩家功能

	protected void handleMessagesAndSetNotifyFlag(Object obj) {

		if (obj != null) {
			long startTime = 0;
			if (handleMessagelogger.isDebugEnabled()) {
				startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			}
			if (obj instanceof PlayerMessagePair) {
				PlayerMessagePair pair = (PlayerMessagePair) obj;
				Message msg = pair.message;
				if (msg instanceof SET_CLIENT_DISPLAY_FLAG) {
					// if (isHiddenOpen) {
					this.notifyPlayerChangeHiddenConfig(pair.player, (SET_CLIENT_DISPLAY_FLAG) msg);
					// } else {
					// if (logger.isDebugEnabled()) {
					// logger.debug("[收到玩家请求屏蔽功能] [服务器屏蔽功能未开启]");
					// }
					// }
				} else if (msg instanceof HEARTBEAT_CHECK_REQ) {
					// pair.player.handle_HEARTBEAT_CHECK_REQ((HEARTBEAT_CHECK_REQ) msg, com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				} else if (msg instanceof ENTER_GAME_REQ) {
					try{
						Game.logger.warn("[处理玩家进入地图-] [gamecountry:"+country+"] [country:"+pair.player.getCurrentGameCountry()+"] ["+gi.getName()+"] ["+(pair.player!=null?pair.player.getLogString():"nul")+"]");
						this.playerEnterGame(pair.player,"gameHandleMessage");
					}catch(Exception e){
						e.printStackTrace();
						Game.logger.warn("[处理玩家进入地图-] [异常] ["+(pair.player!=null?pair.player.getLogString():"nul")+"]");
					}
				} else if (msg instanceof LEAVE_CURRENT_GAME_REQ) {
					playerLeaveGame(pair.player);
					// 同时通知网关用户离开游戏
					// GatewayAddressManager gatewayAddressManager =
					// GatewayAddressManager.getInstance();
					// gatewayAddressManager.notifyLeaveGame( pair.player );
				} else if (msg instanceof USER_LEAVE_SERVER_REQ) {
					USER_LEAVE_SERVER_REQ req = (USER_LEAVE_SERVER_REQ) msg;
					playerLeaveServer(pair.player);
				} else if (msg instanceof PLAYER_MOVE_REQ) {
					doPlayerMoveReq(pair.player, (PLAYER_MOVE_REQ) msg);
				} else if (msg instanceof PLAYER_MOVETRACE_REQ) {
					doPlayerMovetraceReq(pair.player, (PLAYER_MOVETRACE_REQ) msg);
				} else if (msg instanceof SET_POSITION_REQ) {
					if (MagicWeaponManager.logger.isDebugEnabled()) {
						MagicWeaponManager.logger.debug("[收到玩家请求] [" + pair.player.getLogString() + "] ");
					}
					doSetPositionReq(pair.player, (SET_POSITION_REQ) msg);
					// } else if (msg instanceof SPRITE_INTERACT_REQ) {
					// doSpriteInteractReq(pair.player, (SPRITE_INTERACT_REQ)
					// msg);
				} else if (msg instanceof TOUCH_TRANSPORT_REQ) {
					if (logger.isInfoEnabled()) logger.info("[Game] [handle_message] [TOUCH_TRANSPORT_REQ]");
					doTouchTransportReq(pair.player, (TOUCH_TRANSPORT_REQ) msg);
				} else if (msg instanceof TARGET_SKILL_REQ) {
					doTargetSkillReq(pair.player, (TARGET_SKILL_REQ) msg);
				} else if (msg instanceof NONTARGET_SKILL_REQ) {
					doNontargetSkillReq(pair.player, (NONTARGET_SKILL_REQ) msg);
				} else if (msg instanceof BOTTLE_PICK_ARTICLE_REQ) {
					BOTTLE_PICK_ARTICLE_REQ req = (BOTTLE_PICK_ARTICLE_REQ) msg;
					long articleId = req.getArticleId();
					ArticleEntity ae = pair.player.getArticleEntity(articleId);
					if (ae instanceof BottlePropsEntity) {

						BottlePropsEntity bpe = (BottlePropsEntity) ae;
						if (bpe.getOpenedArticles() != null) {
							ArticleManager am = ArticleManager.getInstance();
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleEntity removeAe = pair.player.removeArticleEntityFromKnapsackByArticleId(articleId, "开瓶子删除", true);
							if (removeAe == null) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.删除物品不成功);
								pair.player.addMessageToRightBag(hreq);
								return;
							}

							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(pair.player, RecordAction.开启古董, 1);
							}

							// 统计
							ArticleStatManager.addToArticleStat(pair.player, null, removeAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "开瓶子删除", null);
							DajingStudioManager djm = DajingStudioManager.getInstance();
							MailManager mm = MailManager.getInstance();
							ArrayList<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
							ArrayList<Integer> aeCountList = new ArrayList<Integer>();
							for (int i = 0; i < bpe.getOpenedArticles().length; i++) {
								ArticleProperty ap = bpe.getOpenedArticles()[i];
								if (ap != null) {
									Article a = am.getArticle(ap.getArticleName());
									if (a != null) {
										try {
											boolean binded = ap.isBinded();
											if (!binded) {
												if (djm != null) {
													if (a.getName().equals(Translate.银块)) {
														if (!djm.古董是否可以产生非绑定银块(pair.player)) {
															binded = true;
														} else {
															djm.notify_古董产出银块(pair.player, ap.getCount());
														}
													} else if (Translate.酒.equals(a.get物品二级分类())) {
														if (!djm.古董是否可以产生非绑定酒(pair.player)) {
															binded = true;
														} else {
															djm.notify_古董产出非绑定酒(pair.player, ap.getCount());
														}
													} else if (Translate.封魔录.equals(a.get物品二级分类())) {
														if (!djm.古董是否可以产生非绑定帖(pair.player)) {
															binded = true;
														} else {
															djm.notify_古董产出非绑定帖(pair.player, ap.getCount());
														}
													} else if (a.getName().indexOf(Translate.宝石) >= 0 || Translate.宝石类.equals(a.get物品一级分类())) {
														if (!djm.古董是否可以产生非绑定宝石(pair.player)) {
															binded = true;
														} else {
															djm.notify_古董产出非绑定宝石(pair.player, ap.getCount());
														}
													}
												}
											}
											ArticleEntity aee = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_BOTTLE, pair.player, ap.getColor(), 1, true);
											if (aee != null) {
												if (!pair.player.putToKnapsacks(aee, ap.getCount(), "开瓶子获得")) {
													aeList.add(aee);
													aeCountList.add(ap.getCount());
												}
											}

											// 统计
											ArticleStatManager.addToArticleStat(pair.player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, ap.getCount(), "开瓶子获得", null);

										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}
								}
							}
							// 邮件
							if (mm != null && aeList.size() > 0) {
								ArticleEntity[] entities = aeList.toArray(new ArticleEntity[0]);
								int[] counts = new int[aeCountList.size()];
								for (int i = 0; i < counts.length; i++) {
									counts[i] = aeCountList.get(i);
								}
								String title = Translate.系统邮件提示;
								String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
								try {
									mm.sendMail(pair.player.getId(), entities, counts, title, content, 0, 0, 0, "开瓶子");
								} catch (Exception e) {
									e.printStackTrace();
									if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[邮件] [异常] [{}] [{}] [{}]", new Object[] { pair.player.getUsername(), pair.player.getId(), pair.player.getName() }, e);
								}
							}
						}
					}
				} else if (msg instanceof EXPEND_KNAPSACK_REQ) {
					EXPEND_KNAPSACK_REQ req = (EXPEND_KNAPSACK_REQ) msg;
					int knapsackIndex = req.getKnapsackIndex();
					Player player = pair.player;

					if (player.getSoulLevel() < 10) {
						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						Option_Cancel cancel = new Option_Cancel();
						mw.setDescriptionInUUB(Translate._10级以下不能扩展背包);
						Option[] options = new Option[] { cancel };
						mw.setOptions(options);
						cancel.setText(Translate.确定);

						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						pair.player.addMessageToRightBag(creq);
						return;
					}

					Knapsack knapsack = player.getKnapsack_common();
					Cell[] cells = knapsack.getCells();
					int num = 0;
					if (cells != null) {
						num = cells.length;
					}
					if (knapsackIndex <= 0 || knapsackIndex > Knapsack.MAX_CELL_NUM) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.请不要使用外挂);
						player.addMessageToRightBag(hreq);
						return;
					}
					if (num >= knapsackIndex) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.目前不提供缩包服务);
						player.addMessageToRightBag(hreq);
						return;
					}
					int cost = Game.得到扩包所需银两(knapsackIndex, num);
					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					Option_Knapsack_ConfirmAddCell option = new Option_Knapsack_ConfirmAddCell();
					option.knapsackIndex = knapsackIndex;
					option.setText(Translate.确定);
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.取消);
					mw.setDescriptionInUUB(Translate.translateString(Translate.扩展到格子需要银子信息, new String[][] { { Translate.COUNT_1, knapsackIndex + "" }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(cost) } }));
					Option[] options = new Option[] { option, cancel };
					mw.setOptions(options);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
					pair.player.addMessageToRightBag(creq);

				} else if (msg instanceof CLIFFORD_START_REQ) {
					CliffordManager cm = CliffordManager.getInstance();
					if (cm != null) {
						cm.打开祈福界面((CLIFFORD_START_REQ) msg, pair.player, true);
					}
				} else if (msg instanceof CLIFFORD_REQ) {
					CliffordManager cm = CliffordManager.getInstance();
					if (cm != null) {
						cm.祈福(((CLIFFORD_REQ) msg).getCliffordIndex(), ((CLIFFORD_REQ) msg).getCliffordType(), pair.player, true);
					}
				} else if (msg instanceof CLIFFORD_LOOKOVER_REQ) {
					CliffordManager cm = CliffordManager.getInstance();
					if (cm != null) {
						cm.查看剩余祈福(pair.player);
					}
				} else if (msg instanceof PET_TARGET_SKILL_REQ) {
					doPetTargetSkillReq(pair.player, (PET_TARGET_SKILL_REQ) msg);
				} else if (msg instanceof PET_NONTARGET_SKILL_REQ) {
					doPetNontargetSkillReq(pair.player, (PET_NONTARGET_SKILL_REQ) msg);
				} else if (msg instanceof ARTICLE_OPRATION_REQ) {

					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2989);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2990);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行使用道具命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ARTICLE_OPRATION_REQ req = (ARTICLE_OPRATION_REQ) msg;
						int index = req.getBagIndex();
						Knapsack knapsack = null;
						if (index == 1) {
							knapsack = player.getPetKnapsack();
						} else {
							knapsack = player.getKnapsack_common();
						}

						if (knapsack == null) {
							// ArticleManager.logger.warn("[第" + req.getBagIndex() + "个背包为空] [" + player.getUsername() + "] [" + player.getId() + "] ["
							// + player.getName() + "]");
							if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[第{}个背包为空] [{}] [{}] [{}]", new Object[] { req.getBagIndex(), player.getUsername(), player.getId(), player.getName() });
							return;
						}
						ArticleEntity cell = knapsack.getArticleEntityByCell(req.getIndex());
						MagicWeaponManager.logger.debug("[使用背包物品] [" + cell + "] [" + req.getOperation() + "]");
						if (cell != null) {
							if (req.getOperation() == 0 || req.getOperation() == 1) { // 使用
								// 背包中的内容
								// player.useItemOfKnapsack(this,req.getIndex(),cell);
								// System.out.println(player.getName() + "使用道具");
								player.getUsingPropsAgent().use(heartBeatStartTime, this, req.getOperation(), req.getBagIndex(), (int) req.getIndex(), req.getSoulType());

							} else if (req.getOperation() == 3) { // 续费 背包中的内容
								// player.useItemOfKnapsack(this,req.getIndex(),cell);

								player.getUsingPropsAgent().giveMoneyForContinueUseArticle(heartBeatStartTime, this, req.getOperation(), (int) req.getIndex());

							} else { // 丢弃背包中的内容
								player.removeItemOfKnapsack(this, req.getBagIndex(), req.getIndex());
							}
						} else {
							HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_2992);
							player.addMessageToRightBag(error);

							if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [使用背包中的道具或者装备] [背包类型:{}] [操作类型:{}] [格子：{}] [指定背包中的格子中没有任何东西] ", 
									new Object[] { gi.name, pair.player.getUsername(), pair.player.getName(),index,req.getOperation(), req.getIndex() });
						}
					}
				} else if (msg instanceof HOOK_USE_PROP_REQ) {

					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2989);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2990);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行使用道具命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						HOOK_USE_PROP_REQ req = (HOOK_USE_PROP_REQ) msg;
						int index = req.getBagIndex();
						long id = req.getEntityID();

						Knapsack knapsack = null;
						if (index == 1) {
							knapsack = player.getPetKnapsack();
						} else {
							knapsack = player.getKnapsack_common();
						}

						if (knapsack == null) {
							if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[第{}个背包为空] [{}] [{}] [{}]", new Object[] { req.getBagIndex(), player.getUsername(), player.getId(), player.getName() });
							return;
						}
						ArticleEntity cell = knapsack.getArticleEntityByCell(req.getIndex());
						if (cell != null) {
							if (cell.getId() == id) {
								player.getUsingPropsAgent().use(heartBeatStartTime, this, (byte) 0, req.getBagIndex(), (int) req.getIndex(), req.getSoulType());
							} else {
								if (logger.isWarnEnabled()) {
									logger.warn("[{}] [{}] [{}] [使用背包中的道具] [格子：{}] [指定格子id与使用物品id不一样]  [格子上的id:{}] [指定id:{}]", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName(), req.getIndex(), cell.getId(), id });
								}
							}
						} else {
							HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_2992);
							player.addMessageToRightBag(error);
							if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [使用背包中的道具] [格子：{}] [指定背包中的格子中没有任何东西] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName(), req.getIndex() });
						}
					}
				} else if (msg instanceof ARTICLE_OPRATION_MOVE_REQ) {

					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2989);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2990);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (!ArticleManager.使用物品日志规范) {
							if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行使用道具命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("[Move] [Fail] [" + pair.player.getLogString4Knap() + "] [] [] [用户已死亡不能执行使用道具命令]");
							}
						}
					} else {
						Player player = pair.player;
						ARTICLE_OPRATION_MOVE_REQ req = (ARTICLE_OPRATION_MOVE_REQ) msg;
						// type类型说明0:从一般包移动到一般包 1:从防爆包移动到防爆包
						// 2:从一般包移动到防爆包，3：从防爆包到一般包
						int bagType = 0;
						int indexFrom = req.getIndexFrom();
						int indexTo = req.getIndexTo();
						int moveCount = req.getSplitOutCount();
						if (moveCount <= 0) {
							return;
						}

						int type = req.getMoveType();
						Knapsack knapsackFrom = null;
						Knapsack knapsackTo = null;
						Cell cellFrom = null;
						Cell cellTo = null;
						if (type == 0) {
							knapsackFrom = player.getKnapsack_common();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
								cellTo = knapsackFrom.getCell(indexTo);
							}
							knapsackTo = knapsackFrom;
						} else if (type == 1) {
							knapsackFrom = player.getKnapsack_fangbao();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
								cellTo = knapsackFrom.getCell(indexTo);
							}
							knapsackTo = knapsackFrom;
						} else if (type == 2) {
							knapsackFrom = player.getKnapsack_common();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_fangbao();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 3) {
							knapsackFrom = player.getKnapsack_fangbao();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_common();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						}
						if (knapsackFrom == null || knapsackTo == null) {
							return;
						}
						if (cellFrom == null) {
							return;
						}
						if (cellTo == null) {
							return;
						}
						if (cellFrom.entityId <= 0 || cellFrom.count < moveCount) {
							return;
						}

						if (GreenServerManager.isBindYinZiServer()) {
							ArticleEntityManager em = ArticleEntityManager.getInstance();
							ArticleEntity fromEntity = em.getEntity(cellFrom.entityId);
							ArticleEntity toEntity = null;
							if (cellTo.entityId > 0) {
								toEntity = em.getEntity(cellTo.entityId);
							}
							if (fromEntity != null) {
								if (fromEntity.getArticleName().equals(GreenServerManager.bindpropName)) {
									player.sendError(Translate.银票不能移动);
									return;
								}
							}
							if (toEntity != null) {
								if (toEntity.getArticleName().equals(GreenServerManager.bindpropName)) {
									player.sendError(Translate.银票不能移动);
									return;
								}
							}
						}
						// 进行物品移动背包的合法性检查 0:从一般包移动到一般包 1:从防爆包移动到防爆包
						// 2:从一般包移动到防爆包，3：从防爆包到一般包
						// if (type == 2 || type == 3) {
						// if (toEntity != null && toEntity instanceof EquipmentEntity) {
						// EquipmentEntity ee = ((EquipmentEntity) toEntity);
						// //2014-03-07wtx修改
						// if (ee instanceof Special_1EquipmentEntity || ee instanceof Special_2EquipmentEntity) {
						// boolean bln = ((EquipmentEntity) toEntity).isOprate(player, false, EquipmentEntity.移动到仓库);
						// if (!bln) {
						// return;
						// }
						// }
						// }
						// if (fromEntity != null && fromEntity instanceof EquipmentEntity) {
						// EquipmentEntity ee = ((EquipmentEntity) fromEntity);
						// if (ee instanceof Special_1EquipmentEntity || ee instanceof Special_2EquipmentEntity) {
						// boolean bln = ((EquipmentEntity) fromEntity).isOprate(player, false, EquipmentEntity.移动到仓库);
						// if (!bln) {
						// return;
						// }
						// }
						// }
						// }
						if (type == 0) {

						} else if (type == 1) {
							if (!((Knapsack_TimeLimit) knapsackFrom).isValid()) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期);
								player.addMessageToRightBag(hreq);
								return;
							}
						} else if (type == 2) {
							if (!((Knapsack_TimeLimit) knapsackTo).isValid()) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期);
								player.addMessageToRightBag(hreq);
								return;
							}
							if (cellFrom.entityId > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleEntity temp = aem.getEntity(cellFrom.entityId);
								if (temp instanceof EquipmentEntity) {
									boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.拾取到防爆包);
									if (!bln) {
										return;
									}
								}
							}
						} else if (type == 3) {
							if (!((Knapsack_TimeLimit) knapsackFrom).isValid()) {
								if (cellTo.entityId > 0) {
									ArticleEntityManager aem = ArticleEntityManager.getInstance();
									ArticleEntity temp = aem.getEntity(cellTo.entityId);
									if (temp instanceof EquipmentEntity) {
										boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.拾取到防爆包);
										if (!bln) {
											return;
										}
									}
									ArticleManager am = ArticleManager.getInstance();
									if (cellFrom.entityId != cellTo.entityId) {
										if (aem != null && am != null) {
											ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
											ArticleEntity ae2 = aem.getEntity(cellTo.entityId);
											if (ae1 != null) {
												Article a1 = am.getArticle(ae1.getArticleName());
												if (a1 != null && !a1.isOverlap()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
											if (ae1 != null && ae2 != null) {
												if (!ae1.getArticleName().equals(ae2.getArticleName()) || ae1.getColorType() != ae2.getColorType()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									} else {
										if (aem != null && am != null) {
											ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
											if (ae1 != null) {
												Article a1 = am.getArticle(ae1.getArticleName());
												if (a1 != null && !a1.isOverlap()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									}
								}
							}
						} else {
							return;
						}
						if (cellTo.entityId > 0 && cellTo.count > 0) {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleManager am = ArticleManager.getInstance();
							if (aem != null && am != null) {
								ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
								ArticleEntity ae2 = aem.getEntity(cellTo.entityId);
								if (ae1 != null && ae2 != null) {
									if (ae1.getArticleName().equals(ae2.getArticleName()) && ae1.getColorType() == ae2.getColorType()) {
										Article a = am.getArticle(ae1.getArticleName());
										if (a != null) {
											if (a.isOverlap()) {
												if (a.isHaveValidDays()) {
													// 有时间限制的可堆叠物品进行合并
													Timer timer1 = ae1.getTimer();
													Timer timer2 = ae2.getTimer();
													if (timer1 == null || timer2 == null) {
														return;
													}
													if (a.getOverLapNum() < moveCount + cellTo.count) {
														moveCount = a.getOverLapNum() - cellTo.count;
													}
													// 计算时间，时长为总时长除以总个数
													long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
													long zongshichang = (timer1.getEndTime() - now) * moveCount + (timer2.getEndTime() - now) * cellTo.count;
													long endTime = now + zongshichang / (moveCount + cellTo.count);
													{
														// 新创建一个物品，来满足时间改变。
														boolean binded = true;
														// timer2.setEndTime(endTime);
														// ae2.setTimer(timer2);
														// ae2.setEndTime(endTime);
														if (!ae2.isBinded() && !ae1.isBinded()) {
															binded = false;
														}
														try {
															ArticleEntity newAe = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_merge_Article, player, ae1.getColorType(), (cellTo.count + moveCount), true);
															if (newAe != null && newAe.getTimer() != null) {
																newAe.getTimer().setEndTime(endTime);
																newAe.setTimer(newAe.getTimer());
																newAe.setEndTime(endTime);
															}
															if (newAe != null) {
																cellTo.entityId = newAe.getId();
															}
														} catch (Exception ex) {

														}
													}

													if (cellFrom.count > moveCount) {
														cellFrom.count = cellFrom.count - moveCount;
													} else {
														cellFrom.entityId = -1;
														cellFrom.count = 0;
													}

													// 设置原来两个物品的引用计数，因为从两个物品变成了一个新物品，所以原来两个物品都需要减去相应的引用计数
													ae1.setReferenceCount(ae1.getReferenceCount() - moveCount);
													ae2.setReferenceCount(ae2.getReferenceCount() - cellTo.count);

													cellTo.count = cellTo.count + moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", moveCount, type });
															Knapsack.logger.info("[move:createNewAe] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [newAeId:{}] [moveType={}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, cellTo.entityId, ae2.getArticleName(), "to", moveCount, cellTo.entityId, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(cellTo.entityId, moveCount, ArticleManager.To) + "] [] [move:createNewAe] [" + type + "]");
														}
													}
												} else {

													if (a.getOverLapNum() < moveCount + cellTo.count) {
														moveCount = a.getOverLapNum() - cellTo.count;
													}
													if (cellFrom.count > moveCount) {
														cellFrom.count = cellFrom.count - moveCount;
													} else {
														cellFrom.entityId = -1;
														cellFrom.count = 0;
													}

													// 由于没有时间的可堆叠道具服务器上的id是一样的，所以不能改道具的绑定状态，只能换id，即如果两个物品一个绑定一个不绑定，那么cellTo的id为绑定的id
													if (ae1.isBinded() != ae2.isBinded()) {
														if (!ae2.isBinded()) {
															cellTo.entityId = ae1.getId();
														}
													}

													cellTo.count = cellTo.count + moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", moveCount, type });
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", moveCount, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), moveCount, ArticleManager.To) + "] [] [" + type + "]");
														}
													}
												}
											} else {
												if (cellFrom.count == moveCount) {
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", cellFrom.count, type });
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", cellTo.count, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), cellFrom.count, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), cellTo.count, ArticleManager.To) + "] [] [" + type + "]");
														}
													}
													long id = cellFrom.entityId;
													cellFrom.entityId = cellTo.entityId;
													cellFrom.count = cellTo.count;
													cellTo.entityId = id;
													cellTo.count = moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.ADD_NEW_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
												} else {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.只能全部调换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									} else {
										Article a1 = am.getArticle(ae1.getArticleName());
										Article a2 = am.getArticle(ae2.getArticleName());
										if (a1 == null || a2 == null) {
											return;
										}

										if (cellFrom.count == moveCount) {
											if (!ArticleManager.使用物品日志规范) {
												if (Knapsack.logger.isInfoEnabled()) {
													Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", cellFrom.count, type });
													Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", cellTo.count, type });
												}
											} else {
												if (Knapsack.logger.isInfoEnabled()) {
													Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), cellFrom.count, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), cellTo.count, ArticleManager.To) + "] [] [" + type + "]");
												}
											}
											long id = cellFrom.entityId;
											cellFrom.entityId = cellTo.entityId;
											cellFrom.count = cellTo.count;
											cellTo.entityId = id;
											cellTo.count = moveCount;
											knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.ADD_NEW_ARTICLE;
											knapsackFrom.setModified(true);
											knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
											knapsackTo.setModified(true);
										} else {
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.只能全部调换);
											player.addMessageToRightBag(hreq);
											return;
										}
									}
								}
							}
						} else {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							if (type == 3) {
								ArticleManager am = ArticleManager.getInstance();
								if (aem != null && am != null) {
									ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
									if (ae1 == null) {
										return;
									}
									Article a1 = am.getArticle(ae1.getArticleName());
									if (a1 == null) {
										return;
									}
								}
							}
							if (!ArticleManager.使用物品日志规范) {
								if (Knapsack.logger.isInfoEnabled()) {
									ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
									Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, cellFrom.entityId, (ae1 != null ? ae1.getArticleName() : "无此物品"), "from", moveCount, type });
									Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType={}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, cellTo.entityId, "", "to", 0, type });
								}
							} else {
								if (Knapsack.logger.isInfoEnabled()) {
									Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(cellFrom.entityId, moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(cellTo.entityId, 0, ArticleManager.To) + "] [] [" + type + "]");
								}
							}
							cellTo.entityId = cellFrom.entityId;
							if (cellFrom.count > moveCount) {
								cellFrom.count = cellFrom.count - moveCount;
							} else {
								cellFrom.entityId = -1;
								cellFrom.count = 0;
							}
							cellTo.count = moveCount;
							knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
							knapsackFrom.setModified(true);
							knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
							knapsackTo.setModified(true);
						}
					}
				} else if (msg instanceof QUERY_KNAPSACK_REQ) {
					QUERY_KNAPSACK_REQ req = (QUERY_KNAPSACK_REQ) msg;
					Player player = pair.player;
					Knapsack[] sack = player.getKnapsacks_common();
					ArticleManager am = ArticleManager.getInstance();
					if (sack == null) {
						// logger.info("[没有包裹] [QUERY_KNAPSACK_REQ] [" + player.getUsername() + "] [" + player.getName() + "] [" + player.getId() + "]");
						if (logger.isInfoEnabled()) logger.info("[没有包裹] [QUERY_KNAPSACK_REQ] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId() });
						return;
					}
					byte rt = req.getRequestType();
					if (rt == 1) {
						for (int i = 0; i < sack.length; i++) {
							if (i == 1) {
								continue;
							}
							Knapsack knapsack = sack[i];
							if (knapsack != null) {
								knapsack.autoArrange();
							}
						}
						Knapsack fangbaos = player.getKnapsacks_fangBao();
						if (fangbaos != null) {
							fangbaos.autoArrange();
							QUERY_KNAPSACK_FB_RES res = null;
							Cell[] kcs = fangbaos.getCells();
							long[] ids = new long[kcs.length];
							short[] counts = new short[kcs.length];
							for (int j = 0; j < kcs.length; j++) {
								if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
									ids[j] = kcs[j].getEntityId();
									counts[j] = (short) kcs[j].getCount();
								} else {
									ids[j] = -1;
									counts[j] = 0;
								}
							}
							res = new QUERY_KNAPSACK_FB_RES(GameMessageFactory.nextSequnceNum(), player.getKnapsack_fangBao_Id(), Player.防爆包最大格子数, ids, counts);
							player.addMessageToRightBag(res);
						}
						Knapsack cangku = player.getKnapsacks_cangku();
						if (cangku != null) {
							cangku.autoArrange();
							int count = cangku.getCells().length;
							long[] entityIds = new long[count];
							int[] counts = new int[count];
							for (int j = 0; j < count; j++) {
								Cell cell = cangku.getCells()[j];
								if (cell != null) {
									entityIds[j] = cell.entityId;
									counts[j] = cell.count;
								}
							}
							WAREHOUSE_GET_RES res = new WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), false, entityIds, counts);
							player.addMessageToRightBag(res);
						}
						Knapsack cangku2 = player.getKnapsacks_warehouse();
						if (cangku2 != null) {
							cangku2.autoArrange();
							int count = cangku2.getCells().length;
							long[] entityIds = new long[count];
							int[] counts = new int[count];
							for (int j = 0; j < count; j++) {
								Cell cell = cangku2.getCells()[j];
								if (cell != null) {
									entityIds[j] = cell.entityId;
									counts[j] = cell.count;
								}
							}
							NEW_WAREHOUSE_GET_RES res = new NEW_WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(),6, false, entityIds, counts);
							player.addMessageToRightBag(res);
						}

						Knapsack qilingCangku = player.getKnapsacks_QiLing();
						if (qilingCangku != null) {
							qilingCangku.autoArrange();
							int count = qilingCangku.getCells().length;
							long[] entityIds = new long[count];
							short[] counts = new short[count];
							for (int j = 0; j < count; j++) {
								Cell cell = qilingCangku.getCells()[j];
								if (cell != null) {
									entityIds[j] = cell.entityId;
									counts[j] = (short) cell.count;
								}
							}
							QUERY_KNAPSACK_QILING_RES res = new QUERY_KNAPSACK_QILING_RES(GameMessageFactory.nextSequnceNum(), ArticleManager.器灵仓库格子数, entityIds, counts);
							player.addMessageToRightBag(res);
						}
					}
					BagInfo4Client[] bagInfo4Client = new BagInfo4Client[sack.length];
					for (int i = 0; i < sack.length; i++) {
						Knapsack knapsack = sack[i];
						bagInfo4Client[i] = new BagInfo4Client();
						bagInfo4Client[i].setBagtype((byte) i);
						if (i >= 3) {
							bagInfo4Client[i].setFangbaomax((short) 0);
						}
						if (knapsack != null) {
							Cell kcs[] = knapsack.getCells();
							long ids[] = new long[kcs.length];
							short counts[] = new short[kcs.length];
							for (int j = 0; j < kcs.length; j++) {
								if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
									ids[j] = kcs[j].getEntityId();
									counts[j] = (short) kcs[j].getCount();
								} else {
									ids[j] = -1;
									counts[j] = 0;
								}
							}
							bagInfo4Client[i].setEntityId(ids);
							bagInfo4Client[i].setCounts(counts);

							// 对应类型的防爆包
							// Knapsack knap_fangbao = player.getKnapsack_fangbao();
							// if (knap_fangbao != null) {
							// kcs = knap_fangbao.getCells();
							// ids = new long[kcs.length];
							// counts = new short[kcs.length];
							// for (int j = 0; j < kcs.length; j++) {
							// if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
							// ids[j] = kcs[j].getEntityId();
							// counts[j] = (short) kcs[j].getCount();
							// } else {
							// ids[j] = -1;
							// counts[j] = 0;
							// }
							// }
							// bagInfo4Client[i].setFangbaoEntityId(ids);
							// bagInfo4Client[i].setFangbaoCounts(counts);
							// }
						}
					}

					HashMap<String, PropsCategoryCoolDown> map = player.getUsingPropsAgent().getCooldownTable();
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String categoryName = it.next();
						PropsCategoryCoolDown cd = map.get(categoryName);
						if (cd != null && cd.end >= com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 5000) {
							PROPS_CD_MODIFY_REQ req2 = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), categoryName, cd.start, (byte) 0);
							player.addMessageToRightBag(req2);
						}
					}
					Fangbao_KNAPSACK_RES res = new Fangbao_KNAPSACK_RES(GameMessageFactory.nextSequnceNum(), player.getKnapsack_fangBao_Id());
					player.addMessageToRightBag(res);
					if (logger.isDebugEnabled()) {
						logger.debug("[返回包裹] [QUERY_KNAPSACK_REQ] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId() });
					}
					player.addMessageToRightBag(new QUERY_KNAPSACK_RES(req.getSequnceNum(), bagInfo4Client, am.getAllPropsCategory()));
				} else if (msg instanceof Fangbao_KNAPSACK_REQ) {

					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2989);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2990);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行使用道具命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						Fangbao_KNAPSACK_REQ req = (Fangbao_KNAPSACK_REQ) msg;
						byte type = req.getRequestType();
						// 返回防爆包的id
						if (type == -1) {
							Fangbao_KNAPSACK_RES res = new Fangbao_KNAPSACK_RES(req.getSequnceNum(), player.getKnapsack_fangBao_Id());
							player.addMessageToRightBag(res);
							return;
						} else {
							// 根据位置把防爆包放回背包
							if (type == 0) {
								Knapsack knapsack = player.getKnapsack_fangbao();
								if (knapsack != null && knapsack.getEmptyNum() != knapsack.getCells().length) {
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹中还有物品);
									player.addMessageToRightBag(hreq);
									return;
								}
								if (knapsack != null) {
									ArticleEntityManager aem = ArticleEntityManager.getInstance();
									ArticleManager am = ArticleManager.getInstance();
									ArticleEntity ae = aem.getEntity(player.getKnapsack_fangBao_Id());
									if (ae != null) {
										Article a = am.getArticle(ae.getArticleName());
										if (a != null) {
											Knapsack k = player.getKnapsack_common();
											if (k.isFull()) {
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.背包空间不足);
												player.addMessageToRightBag(hreq);
												return;
											}
											if (k.put(ae, "把防爆包放回背包")) {
												player.setKnapsacks_fangBao(null);
												player.setKnapsack_fangBao_Id(-1);
												player.notifyAllKnapsack();
												player.notifyKnapsackFB(player);
												return;
											}
										}
									}
								}
							}
						}
					}
				} else if (msg instanceof WAREHOUSE_MOVE_ARTICLE_REQ) {

					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2989);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2990);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行使用道具命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						WAREHOUSE_MOVE_ARTICLE_REQ req = (WAREHOUSE_MOVE_ARTICLE_REQ) msg;
						if (player.getCangkuPassword() != null && !player.getCangkuPassword().trim().equals("") && !player.getCangkuPassword().equals(req.getPassword())) {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.密码输入错误);
							player.addMessageToRightBag(hreq);
							return;
						}
						// type类型说明物品移动方式，0从普通背包到仓库，1从防爆背包到仓库，2从仓库到普通背包，3从仓库到防爆背包，4从仓库到仓库
						int bagType = req.getKnapsackIndex();
						int indexFrom = req.getCellIndexFrom();
						int indexTo = req.getCellIndexTo();
						int moveCount = req.getMoveCount();
						if (moveCount <= 0) {
							return;
						}

						int type = req.getMoveType();
						Knapsack knapsackFrom = null;
						Knapsack knapsackTo = null;
						Cell cellFrom = null;
						Cell cellTo = null;
						if (type == 0) {		//从普通背包到仓库
							knapsackFrom = player.getKnapsack_common();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsacks_cangku();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
							if (cellFrom != null && cellFrom.getEntityId() > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleEntity temp = aem.getEntity(cellFrom.getEntityId());
								if (temp instanceof EquipmentEntity) {
									boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.移动到仓库);
									if (!bln) {
										return;
									}
								}
							}
						} else if (type == 5) {		//普通背包到2号仓库
							knapsackFrom = player.getKnapsack_common();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsacks_warehouse();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
							if (cellFrom != null && cellFrom.getEntityId() > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleEntity temp = aem.getEntity(cellFrom.getEntityId());
								if (temp instanceof EquipmentEntity) {
									boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.移动到仓库);
									if (!bln) {
										return;
									}
								}
							}
						} else if (type == 1) {		//从防爆背包到仓库
							knapsackFrom = player.getKnapsack_fangbao();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsacks_cangku();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 6) {		//防爆包到2号仓库
							knapsackFrom = player.getKnapsack_fangbao();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsacks_warehouse();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 2) {		//从仓库到普通背包
							knapsackFrom = player.getKnapsacks_cangku();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_common();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
							if (cellTo != null && cellTo.getEntityId() > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleEntity temp = aem.getEntity(cellTo.getEntityId());
								if (temp instanceof EquipmentEntity) {
									boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.移动到仓库);
									if (!bln) {
										return;
									}
								}
							}

						} else if (type == 7) {		//从2号仓库到普通背包
							knapsackFrom = player.getKnapsacks_warehouse();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_common();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
							if (cellTo != null && cellTo.getEntityId() > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleEntity temp = aem.getEntity(cellTo.getEntityId());
								if (temp instanceof EquipmentEntity) {
									boolean bln = ((EquipmentEntity) temp).isOprate(player, false, EquipmentEntity.移动到仓库);
									if (!bln) {
										return;
									}
								}
							}
						} else if (type == 3) {		//从仓库到防爆背包
							knapsackFrom = player.getKnapsacks_cangku();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_fangbao();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 8) {		//从2号仓库到防爆包
							knapsackFrom = player.getKnapsacks_warehouse();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = player.getKnapsack_fangbao();
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 4) {		//从仓库到仓库
							knapsackFrom = player.getKnapsacks_cangku();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = knapsackFrom;
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						} else if (type == 9) {			//从2号仓库到2号仓库
							knapsackFrom = player.getKnapsacks_warehouse();
							if (knapsackFrom != null) {
								cellFrom = knapsackFrom.getCell(indexFrom);
							}
							knapsackTo = knapsackFrom;
							if (knapsackTo != null) {
								cellTo = knapsackTo.getCell(indexTo);
							}
						}
						if (knapsackFrom == null || knapsackTo == null) {
							return;
						}
						if (cellFrom == null) {
							return;
						}
						if (cellTo == null) {
							return;
						}
						if (cellFrom.entityId <= 0 || cellFrom.count < moveCount) {
							return;
						}

						if (GreenServerManager.isBindYinZiServer()) {
							ArticleEntityManager em = ArticleEntityManager.getInstance();
							ArticleEntity fromEntity = em.getEntity(cellFrom.entityId);
							ArticleEntity toEntity = null;
							if (cellTo.entityId > 0) {
								toEntity = em.getEntity(cellTo.entityId);
							}
							if (fromEntity != null) {
								if (fromEntity.getArticleName().equals(GreenServerManager.bindpropName)) {
									player.sendError(Translate.银票不能移动);
									return;
								}
							}
							if (toEntity != null) {
								if (toEntity.getArticleName().equals(GreenServerManager.bindpropName)) {
									player.sendError(Translate.银票不能移动);
									return;
								}
							}
						}

						// 执行背包物品转移合法性判断
						// 0从普通背包到仓库，1从防爆背包到仓库，2从仓库到普通背包，3从仓库到防爆背包，4从仓库到仓库 
						//5普通背包到2号仓库    6防爆包到2号仓库  7从2号仓库到普通背包  8从2号仓库到防爆包  9从2号仓库到2号仓库
						if (type == 0 || type == 5) {
							// 检查移动物品是否符合背包规范
							if (cellTo.entityId > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleManager am = ArticleManager.getInstance();
								if (aem != null && am != null) {
								}
							}
						} else if (type == 1 || type == 6) {
							if (!((Knapsack_TimeLimit) knapsackFrom).isValid()) {
								if (cellTo.entityId > 0) {
									ArticleEntityManager aem = ArticleEntityManager.getInstance();
									ArticleManager am = ArticleManager.getInstance();
									if (cellFrom.entityId != cellTo.entityId) {
										if (aem != null && am != null) {
											ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
											ArticleEntity ae2 = aem.getEntity(cellTo.entityId);
											if (ae1 != null) {
												Article a1 = am.getArticle(ae1.getArticleName());
												if (a1 != null && !a1.isOverlap()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
											if (ae1 != null && ae2 != null) {
												if (!ae1.getArticleName().equals(ae2.getArticleName()) || ae1.getColorType() != ae2.getColorType()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									} else {
										if (aem != null && am != null) {
											ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
											if (ae1 != null) {
												Article a1 = am.getArticle(ae1.getArticleName());
												if (a1 != null && !a1.isOverlap()) {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期不能执行交换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									}
								}
							}
						} else if (type == 2 || type == 7) {
							if (cellFrom.entityId > 0) {
								ArticleEntityManager aem = ArticleEntityManager.getInstance();
								ArticleManager am = ArticleManager.getInstance();
								ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
								if (ae1 != null) {
									player.obtainOneArticle(ae1);
								}
							}
						} else if (type == 3 || type == 8) {
							if (!((Knapsack_TimeLimit) knapsackTo).isValid()) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.包裹已到期);
								player.addMessageToRightBag(hreq);
								return;
							}
						} else if (type == 4 || type == 9) {

						} else {
							return;
						}

						if (cellTo.entityId > 0 && cellTo.count > 0) {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleManager am = ArticleManager.getInstance();
							if (aem != null && am != null) {
								ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
								ArticleEntity ae2 = aem.getEntity(cellTo.entityId);
								if (ae1 != null && ae2 != null) {
									if (ae1.getArticleName().equals(ae2.getArticleName()) && ae1.getColorType() == ae2.getColorType()) {
										Article a = am.getArticle(ae1.getArticleName());
										if (a != null) {
											if (a.isOverlap()) {
												if (a.isHaveValidDays()) {
													// 有时间限制的可堆叠物品进行合并
													Timer timer1 = ae1.getTimer();
													Timer timer2 = ae2.getTimer();
													if (timer1 == null || timer2 == null) {
														return;
													}
													if (a.getOverLapNum() < moveCount + cellTo.count) {
														moveCount = a.getOverLapNum() - cellTo.count;
													}
													// 计算时间，时长为总时长除以总个数
													long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
													long zongshichang = (timer1.getEndTime() - now) * moveCount + (timer2.getEndTime() - now) * cellTo.count;
													long endTime = now + zongshichang / (moveCount + cellTo.count);
													{
														// 新创建一个物品，来满足时间改变。
														boolean binded = true;
														// timer2.setEndTime(endTime);
														// ae2.setTimer(timer2);
														// ae2.setEndTime(endTime);
														if (!ae2.isBinded() && !ae1.isBinded()) {
															binded = false;
														}
														try {
															ArticleEntity newAe = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_merge_Article, player, ae1.getColorType(), (cellTo.count + moveCount), true);
															if (newAe != null && newAe.getTimer() != null) {
																newAe.getTimer().setEndTime(endTime);
																newAe.setTimer(newAe.getTimer());
																newAe.setEndTime(endTime);
															}
															if (newAe != null) {
																cellTo.entityId = newAe.getId();
															}
														} catch (Exception ex) {

														}
													}
													if (cellFrom.count > moveCount) {
														cellFrom.count = cellFrom.count - moveCount;
													} else {
														cellFrom.entityId = -1;
														cellFrom.count = 0;
													}

													// 设置原来两个物品的引用计数，因为从两个物品变成了一个新物品，所以原来两个物品都需要减去相应的引用计数
													ae1.setReferenceCount(ae1.getReferenceCount() - moveCount);
													ae2.setReferenceCount(ae2.getReferenceCount() - cellTo.count);

													cellTo.count = cellTo.count + moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", moveCount, type });
															Knapsack.logger.info("[move:createNewAe] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [newAeId:{}] [moveType{}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", moveCount, cellTo.entityId, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), moveCount, ArticleManager.To) + "] [] [" + type + "]");
														}
													}
												} else {
													if (a.getOverLapNum() < moveCount + cellTo.count) {
														moveCount = a.getOverLapNum() - cellTo.count;
													}
													if (cellFrom.count > moveCount) {
														cellFrom.count = cellFrom.count - moveCount;
													} else {
														cellFrom.entityId = -1;
														cellFrom.count = 0;
													}

													// 由于没有时间的可堆叠道具服务器上的id是一样的，所以不能改道具的绑定状态，只能换id，即如果两个物品一个绑定一个不绑定，那么cellTo的id为绑定的id
													if (ae1.isBinded() != ae2.isBinded()) {
														if (!ae2.isBinded()) {
															cellTo.entityId = ae1.getId();
														}
													}

													cellTo.count = cellTo.count + moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", moveCount, type });
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", moveCount, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), moveCount, ArticleManager.To) + "] [] [" + type + "]");
														}
													}
												}
											} else {
												if (cellFrom.count == moveCount) {
													if (!ArticleManager.使用物品日志规范) {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", cellFrom.count, type });
															Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", cellTo.count, type });
														}
													} else {
														if (Knapsack.logger.isInfoEnabled()) {
															Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + ")] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), cellFrom.count, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), cellTo.count, ArticleManager.To) + "] [] [" + type + "]");
														}
													}
													long id = cellFrom.entityId;
													cellFrom.entityId = cellTo.entityId;
													cellFrom.count = cellTo.count;
													cellTo.entityId = id;
													cellTo.count = moveCount;
													knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.ADD_NEW_ARTICLE;
													knapsackFrom.setModified(true);
													knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
													knapsackTo.setModified(true);
												} else {
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.只能全部调换);
													player.addMessageToRightBag(hreq);
													return;
												}
											}
										}
									} else {
										if (cellFrom.count == moveCount) {
											if (!ArticleManager.使用物品日志规范) {
												if (Knapsack.logger.isInfoEnabled()) {
													Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, ae1.getId(), ae1.getArticleName(), "from", cellFrom.count, type });
													Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, ae2.getId(), ae2.getArticleName(), "to", cellTo.count, type });
												}
											} else {
												if (Knapsack.logger.isInfoEnabled()) {
													Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(ae1.getId(), cellFrom.count, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(ae2.getId(), cellTo.count, ArticleManager.To) + "] [] [" + type + "]");
												}
											}
											long id = cellFrom.entityId;
											cellFrom.entityId = cellTo.entityId;
											cellFrom.count = cellTo.count;
											cellTo.entityId = id;
											cellTo.count = moveCount;
											knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.ADD_NEW_ARTICLE;
											knapsackFrom.setModified(true);
											knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
											knapsackTo.setModified(true);
										} else {
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.只能全部调换);
											player.addMessageToRightBag(hreq);
											return;
										}
									}
								}
							}
						} else {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							if (!ArticleManager.使用物品日志规范) {
								if (Knapsack.logger.isInfoEnabled()) {
									ArticleEntity ae1 = aem.getEntity(cellFrom.entityId);
									Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackFrom.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexFrom, cellFrom.entityId, (ae1 != null ? ae1.getArticleName() : "无此物品"), "from", moveCount, type });
									Knapsack.logger.info("[move] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [-] [{}] [moveCount:{}] [moveType{}]", new Object[] { knapsackTo.getClass().getSimpleName(), player.getId(), player.getUsername(), player.getName(), indexTo, cellTo.entityId, "", "to", 0, type });
								}
							} else {
								if (Knapsack.logger.isInfoEnabled()) {
									Knapsack.logger.info("[Move] [Succ] [" + player.getLogString4Knap() + "] [" + knapsackFrom.getKnapInfo4Log(indexFrom, ArticleManager.From) + "] [" + knapsackTo.getKnapInfo4Log(indexTo, ArticleManager.To) + "] [" + ArticleManager.getArticleInfo4Log(cellFrom.entityId, moveCount, ArticleManager.From) + "] [" + ArticleManager.getArticleInfo4Log(cellTo.entityId, 0, ArticleManager.To) + "] [] [" + type + "]");
								}
							}
							cellTo.entityId = cellFrom.entityId;
							if (cellFrom.count > moveCount) {
								cellFrom.count = cellFrom.count - moveCount;
							} else {
								cellFrom.entityId = -1;
								cellFrom.count = 0;
							}
							cellTo.count = moveCount;
							knapsackFrom.cellChangeFlags[indexFrom] = Knapsack.REMOVE_ARTICLE;
							knapsackFrom.setModified(true);
							knapsackTo.cellChangeFlags[indexTo] = Knapsack.ADD_NEW_ARTICLE;
							knapsackTo.setModified(true);
						}
					}
				} else if (msg instanceof REMOVE_AVATAPROPS_REQ) {
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行卸载时装命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						if (player.getAvataPropsId() > 0) {
							AvataProps.takeoff(player);
							ResourceManager rm = ResourceManager.getInstance();
							if (rm != null) {
								rm.getAvata(player);
							}
						} else {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.没有时装不能进行此操作);
							player.addMessageToRightBag(hreq);
						}
					}
				} else if (msg instanceof COUNTRY_WANGZHEZHIYIN_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行王者之印传送命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						if (cm != null) {
							cm.使用王者之印(player, (COUNTRY_WANGZHEZHIYIN_REQ) msg);
						}
					}

				} else if (msg instanceof COUNTRY_VOTE_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行投票命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						if (cm != null) {
							cm.投票(player, ((COUNTRY_VOTE_REQ) msg).getVotes(), (byte) 0);
							// cm.得到投票结果(player);
						}
					}

				} else if(msg instanceof WOLF_SIGN_UP_SURE_REQ){
					WOLF_SIGN_UP_SURE_REQ req = (WOLF_SIGN_UP_SURE_REQ)msg;
					int signType = req.getSignType();
					if(signType == 0){
						String result = WolfManager.getInstance().signUp(pair.player,false);
						if(result != null && !result.equals(Translate.报名成功)){
							pair.player.sendError(result);
						}
					}else{
						WolfManager.getInstance().cancelSignUp(pair.player,true);
					}
				} else if(msg instanceof DICE_SIGN_UP_SURE_REQ){
					DICE_SIGN_UP_SURE_REQ req = (DICE_SIGN_UP_SURE_REQ)msg;
					int signType = req.getSignType();
					if(signType == 0){
						DiceManager.getInstance().signUpActivity(pair.player);
					}else{
						DiceManager.getInstance().cancelSignUp(pair.player);
					}
				} else if(msg instanceof WOLF_ENTER_REQ){
					boolean result = WolfManager.getInstance().playerEnter(pair.player);
					if(result){
						pair.player.addMessageToRightBag(new WOLF_ENTER_RES(msg.getSequnceNum(), 1));
					}
				} else if(msg instanceof WOLF_SIGN_UP_QUERY_REQ){
					boolean hasSignUp = WolfManager.signList.contains(pair.player.getId());
					int joinNums = 0;
					JoinNumData data = WolfManager.getInstance().joinNums.get(pair.player.getId());
					if(data != null){
						joinNums = data.getJoinNums();
					}
					joinNums = WolfManager.ONE_DAY_MAX_JOIN_NUMS - joinNums;
					if(joinNums < 0){
						joinNums = 0;
					}
					String timeMess = WolfManager.getInstance().currConfig != null ?WolfManager.getInstance().currConfig.getOpenTimeMess():Translate.暂未开放敬请期待;
					String joinTimes = Translate.translateString(Translate.次数, new String[][] { { Translate.COUNT_1, String.valueOf(3) }});
					WOLF_SIGN_UP_QUERY_RES res = new WOLF_SIGN_UP_QUERY_RES(GameMessageFactory.nextSequnceNum(),Translate.规则1,Translate.失败描述,Translate.成功描述,
							Translate.封神描述,Translate.单人,joinTimes,timeMess,hasSignUp,WolfManager.signList.size(),joinNums);
					pair.player.addMessageToRightBag(res);
				} else if(msg instanceof WOLF_USE_SKILL_REQ){
					WolfManager.getInstance().handle_WOLF_USE_SKILL_RES(((WOLF_USE_SKILL_REQ)msg), pair.player);
				} else if(msg instanceof WOLF_SIGNUP_NUM_REQ){
					boolean activityIsOpen = WolfManager.getInstance().isStart();
					boolean isSignUP = WolfManager.signList.contains(pair.player.getId());
					WOLF_SIGNUP_NUM_RES res = new WOLF_SIGNUP_NUM_RES(msg.getSequnceNum(), WolfManager.signList.size(),activityIsOpen,isSignUP);
					pair.player.addMessageToRightBag(res);
				} else if (msg instanceof COUNTRY_COMMISSION_OR_RECALL_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行任命命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						COUNTRY_COMMISSION_OR_RECALL_REQ req = (COUNTRY_COMMISSION_OR_RECALL_REQ) msg;
						byte selectType = req.getSelectType();
						String playerBName = req.getPlayerName();
						int 授予官职 = req.getOfficialPositionType();
						if (cm != null) {
							Player playerB = null;
							try {
								PlayerManager pm = PlayerManager.getInstance();
								playerB = pm.getPlayer(playerBName);
							} catch (Exception ex) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您输入的玩家名不存在);
								player.addMessageToRightBag(hreq);
								return;
							}
							if (playerB != null) {
								if (selectType == 0) {
									cm.任命确认(player, playerB, 授予官职);
								} else {
									cm.罢免确认(player, playerB, 授予官职);
								}
								cm.返回任命或罢免国家官员列表(player);
							}
						}
					}
				} else if (msg instanceof COUNTRY_HONOURSOR_OR_CANCEL_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行任命命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						COUNTRY_HONOURSOR_OR_CANCEL_REQ req = (COUNTRY_HONOURSOR_OR_CANCEL_REQ) msg;
						byte selectType = req.getSelectType();
						String playerBName = req.getPlayerName();
						if (cm != null) {
							Player playerB = null;
							try {
								PlayerManager pm = PlayerManager.getInstance();
								playerB = pm.getPlayer(playerBName);
							} catch (Exception ex) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您输入的玩家名不存在);
								player.addMessageToRightBag(hreq);
								return;
							}
							if (playerB != null) {
								if (selectType == 0) {
									cm.授勋(player, playerB);
								} else {
									cm.撤销授勋(player, playerB);
								}
								cm.返回授勋或撤销国家官员列表(player);
							}
						}
					}
				} else if (msg instanceof COUNTRY_COMMEND_OR_CANCEL_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行任命命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						COUNTRY_COMMEND_OR_CANCEL_REQ req = (COUNTRY_COMMEND_OR_CANCEL_REQ) msg;
						byte selectType = req.getSelectType();
						String playerBName = req.getPlayerName();
						if (cm != null) {
							Player playerB = null;
							try {
								PlayerManager pm = PlayerManager.getInstance();
								playerB = pm.getPlayer(playerBName);
							} catch (Exception ex) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您输入的玩家名不存在);
								player.addMessageToRightBag(hreq);
								return;
							}
							if (playerB != null) {
								if (selectType == 0) {
									cm.表彰(player, playerB);
								} else {
									cm.撤销表彰(player, playerB);
								}
								cm.返回表彰或撤销国家官员列表(player);
							}
						}
					}
				} else if (msg instanceof COUNTRY_LINGQU_FENGLU_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行任命命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						if (cm != null) {
							cm.提取俸禄(player);
						}
					}
				} else if (msg instanceof COUNTRY_FAFANG_FENGLU_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行任命命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						CountryManager cm = CountryManager.getInstance();
						if (cm != null) {
							cm.发放俸禄(player);
						}
					}
				} else if (msg instanceof LEVELUP_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行升级命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						player.playerLevelUpByClick();
					}
				} else if (msg instanceof REMOVE_EQUIPMENT_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}

					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行卸载装备命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						REMOVE_EQUIPMENT_REQ req = (REMOVE_EQUIPMENT_REQ) msg;
						player.removeEquipment(req.getSoulType(), req.getEquipmentType());
					}

				} else if (msg instanceof GET_PLAYER_BY_ID_REQ) {

				} else if (msg instanceof SWITCH_SUIT_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2993);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行切换装备栏命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						SWITCH_SUIT_REQ req = (SWITCH_SUIT_REQ) msg;
						player.switchEquipmentSuit(req.getSuit());
					}
				} else if (msg instanceof EQUIPMENT_DRILL_REQ) {

				} else if (msg instanceof EQUIPMENT_INLAY_UUB_REQ) {
					EQUIPMENT_INLAY_UUB_REQ req = (EQUIPMENT_INLAY_UUB_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae = aem.getEntity(req.getEquipmentId());
					if (ae != null && ae instanceof EquipmentEntity) {

						EquipmentEntity ee = (EquipmentEntity) ae;
						StringBuffer sb = new StringBuffer();
						ee.getInlayInfo(pair.player, sb);
						EQUIPMENT_INLAY_UUB_RES nires = new EQUIPMENT_INLAY_UUB_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), sb.toString());
						pair.player.addMessageToRightBag(nires);
						logger.warn("[EQUIPMENT_INLAY_UUB_REQ] [" + sb + "]");
					} else {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [EQUIPMENT_INLAY_UUB_REQ] [找不到装备] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					}

				} else if (msg instanceof EQUIPMENT_INLAY_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行镶嵌命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentInlayReq(player, (EQUIPMENT_INLAY_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_OUTLAY_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行挖取命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentOutlayReq(player, (EQUIPMENT_OUTLAY_REQ) msg);
					}
				} else if (msg instanceof DEAL_AGREE_REQ) {
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2712);
						pair.player.addMessageToRightBag(nreq);
					}
					if (pair.player.isInCrossServer()) {
						// DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:" + player.getName() + "] [id:" + player.getId() + "]");
						if (DealCenter.logger.isWarnEnabled()) DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:{}] [id:{}]", new Object[] { pair.player.getName(), pair.player.getId() });
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2713);
						pair.player.addMessageToRightBag(nreq);
					}
					Deal deal = CoreSubSystem.getInstance().dealCenter.getDeal(pair.player);
					if (deal == null) {
						// logger.warn("[deal_not_found] [" + player.getName() + "]");
						if (logger.isWarnEnabled()) logger.warn("[deal_not_found] [{}]", new Object[] { pair.player.getName() });
						return;
					}
					if (!deal.isLockA() || !deal.isLockB()) {
						pair.player.sendError("您或者对方还未锁定，不能同意交易！");
					}
					try {
						boolean succ = CoreSubSystem.getInstance().dealCenter.agreeDeal(deal, pair.player);
					} catch (KnapsackFullException e) {
						e.printStackTrace();
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage(), e);
					}
				} else if (msg instanceof EQUIPMENT_INSCRIPTION_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行铭刻命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentInscriptionReq(player, (EQUIPMENT_INSCRIPTION_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_STRONG_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行强化命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentStrongReq(player, (EQUIPMENT_STRONG_REQ) msg);
					}
				} else if (msg instanceof NEW_EQUIPMENT_STRONG_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行强化命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						try {
							am.newEquipmentStrongReq(player, (NEW_EQUIPMENT_STRONG_REQ) msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} 
				else if (msg instanceof DEAL_AGREE_REQ) {
					Deal deal = CoreSubSystem.getInstance().dealCenter.getDeal(pair.player);
					if (deal == null) {
						// logger.warn("[deal_not_found] [" + player.getName() + "]");
						if (logger.isWarnEnabled()) logger.warn("[deal_not_found] [{}]", new Object[] { pair.player.getName() });
					} else {
						if (!deal.isLockA() || !deal.isLockB()) {
							pair.player.sendError(Translate.text_deal_016);
						}
						try {
							boolean succ = CoreSubSystem.getInstance().dealCenter.agreeDeal(deal, pair.player);
						} catch (KnapsackFullException e) {
							e.printStackTrace();
							logger.error(e.getMessage(), e);
						} catch (Exception e) {
							e.printStackTrace();
							logger.error(e.getMessage(), e);
						}
					}
				} else if (msg instanceof EQUIPMENT_JIANDING_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行鉴定命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentJianDingReq(player, (EQUIPMENT_JIANDING_REQ) msg);
					}
				} else if (msg instanceof NEW_JIANDING_OK_REQ) {
					ArticleManager.getInstance().新鉴定装备(pair.player, (NEW_JIANDING_OK_REQ) msg);
				} else if (msg instanceof EQUIPMENT_PICKSTAR_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行摘星命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentPickStarReq(player, (EQUIPMENT_PICKSTAR_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_INSERTSTAR_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行装星命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentInsertStarReq(player, (EQUIPMENT_INSERTSTAR_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_DEVELOP_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行炼化命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentDevelopReq(player, (EQUIPMENT_DEVELOP_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_DEVELOPUP_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行炼化升级命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentDevelopUpReq(player, (EQUIPMENT_DEVELOPUP_REQ) msg);
					}
				} else if (msg instanceof EQUIPMENT_BIND_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行绑定命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.equipmentBindReq(player, (EQUIPMENT_BIND_REQ) msg);
					}
				} else if (msg instanceof ARTICLE_COMPOSE_REQ) {
					if (pair.player.isInBattleField() && pair.player.getDuelFieldState() == 2) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3008);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.isLocked()) {
						HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3009);
						pair.player.addMessageToRightBag(nreq);
						return;
					}
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行合成命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						Player player = pair.player;
						ArticleManager am = ArticleManager.getInstance();
						am.articleComposeReq(player, (ARTICLE_COMPOSE_REQ) msg);
					}
				} else if (msg instanceof GOD_EQUIPMENT_UPGRADE_REQ) {
					GOD_EQUIPMENT_UPGRADE_REQ req = (GOD_EQUIPMENT_UPGRADE_REQ) msg;
					ArticleManager.getInstance().handleEquipmentUpgrade(pair.player, req);
				} else if (msg instanceof GOD_EQUIPMENT_UPGRADE_SURE_REQ) {
					GOD_EQUIPMENT_UPGRADE_SURE_REQ req = (GOD_EQUIPMENT_UPGRADE_SURE_REQ) msg;
					ArticleManager.getInstance().EquipmentUpgradeConfirm(pair.player, req, false);
				} else if (msg instanceof CONFIRM_ARTICLE_EXCHANGE_REQ) {
					CONFIRM_ARTICLE_EXCHANGE_REQ req = (CONFIRM_ARTICLE_EXCHANGE_REQ) msg;
					ActivityManagers.getInstance().handle_CONFIRM_ARTICLE_EXCHANGE_REQ(pair.player, req);
				} else if (msg instanceof QUERY_ARTICLE_EXCHANGE_REQ) {
					QUERY_ARTICLE_EXCHANGE_REQ req = (QUERY_ARTICLE_EXCHANGE_REQ) msg;
					ActivityManagers.getInstance().handle_QUERY_ARTICLE_EXCHANGE_REQ(pair.player, req);
				} else if (msg instanceof CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ) {
					CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ req = (CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ) msg;
					SealManager.getInstance().handle_CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ(req, pair.player);
				} else if (msg instanceof SEAL_TASK_INFO_REQ) {
					SEAL_TASK_INFO_REQ req = (SEAL_TASK_INFO_REQ) msg;
					SealManager.getInstance().handle_SEAL_TASK_INFO_REQ(req, pair.player);
				} else if (msg instanceof ACTIVITY_FIRST_PAGE_REQ) {
					ACTIVITY_FIRST_PAGE_REQ req = (ACTIVITY_FIRST_PAGE_REQ) msg;
					try {
//						ActivityManagers.getInstance().handle_ACTIVITY_FIRST_PAGE_REQ(req, pair.player);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof ACTIVITY_FIRST_PAGE2_REQ) {
					ACTIVITY_FIRST_PAGE2_REQ req = (ACTIVITY_FIRST_PAGE2_REQ) msg;
					try {
						ActivityManagers.getInstance().handle_ACTIVITY_FIRST_PAGE2_REQ(req, pair.player);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof GET_ACTIVITY_INFO_REQ) {
					GET_ACTIVITY_INFO_REQ req = (GET_ACTIVITY_INFO_REQ) msg;
					ActivityManagers.getInstance().handle_GET_ACTIVITY_INFO_REQ(req, pair.player);
				} else if (msg instanceof NOTICE_ACTIVITY_STAT_REQ) {
					NOTICE_ACTIVITY_STAT_REQ req = (NOTICE_ACTIVITY_STAT_REQ) msg;
					ActivityManagers.getInstance().ckilckButton(req, pair.player);
				} else if (msg instanceof NOTICE_ACTIVITY_BUTTON_STAT_REQ) {
					NOTICE_ACTIVITY_BUTTON_STAT_REQ req = (NOTICE_ACTIVITY_BUTTON_STAT_REQ) msg;
					ActivityManagers.getInstance().handle_NOTICE_ACTIVITY_BUTTON_STAT_REQ(pair.player);
				} else if (msg instanceof EQUIPMENT_COMPOUND_REQ) {

				} else if (msg instanceof PREPARE_PLAY_DICE_REQ) {

				} else if (msg instanceof CPATAIN_ASSIGN_REQ) {

				} else if (msg instanceof PICKUP_MONEY_REQ) {
					if (pair.player.getState() == Player.STATE_DEAD) {
						if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行拾取命令] ", new Object[] { gi.name, pair.player.getUsername(), pair.player.getName() });
					} else {
						PICKUP_MONEY_REQ req = (PICKUP_MONEY_REQ) msg;
						Monster sprite = gm.getMonsterManager().getMonster(req.getSpriteId());
						Player player = pair.player;
						if (sprite != null && this.contains(sprite) && this.contains(player)) {
							FlopSchemeEntity fse = sprite.getFlopSchemeEntity();
							if (fse != null) {
								int playerCount = (fse.getAllPlayers() != null ? fse.getAllPlayers().length : 0);
								int money = 0;
								if (playerCount != 0) {
									money = fse.getMoney() / playerCount;
								} else {
									money = fse.getMoney();
								}
								fse.pickUpMoney();
								Player.sendPlayerAction(player, PlayerActionFlow.行为类型_拾取金币, money + "", 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
							}
						}
					}
				} else if (msg instanceof PICKUP_ALLFLOP_REQ) {

				} else if (msg instanceof PICKUP_FLOP_REQ) {

				} else if (msg instanceof TOURNAMENT_TAKE_REWARD_REQ) {
					TournamentManager tm = TournamentManager.getInstance();
					tm.领取奖励(pair.player);
				} else if (msg instanceof GET_VIP_REWARD_REQ) {
					VipManager vm = VipManager.getInstance();
					vm.获取vip奖励(pair.player);
				} else if (msg instanceof PICKUP_CAIJINPC_REQ) {
					PICKUP_CAIJINPC_REQ req = (PICKUP_CAIJINPC_REQ) msg;
					MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
					NPC npc = mnm.getNPC(req.getSpriteId());
					if (npc == null) {
						// HINT_REQ err = new
						// HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)
						// 2,
						// "您要拾取的NPC对象【"+req.getSpriteId()+"】不存在");
						// pair.player.addMessageToRightBag(err);
						pair.player.send_HINT_REQ(Translate.text_3048 + req.getSpriteId() + Translate.text_1852);

					} else if (this.contains(npc) == false) {
						pair.player.send_HINT_REQ(Translate.text_3048 + npc.getName() + Translate.text_3049);

					} else {
						// HINT_REQ err = new
						// HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)
						// 2,
						// "您要拾取的NPC对象【"+npc.getName()+"】不是可采集NPC!");
						// pair.player.addMessageToRightBag(err);
						pair.player.send_HINT_REQ(Translate.text_3050);
					}
				} else if (msg instanceof PLAYER_REVIVED_REQ) {
					/*{
						List<Buff> buffs = pair.player.getAllBuffs();
						if (buffs != null) {
							for (Buff buff : buffs) {
								if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
									if (buff.getInvalidTime() > heartBeatStartTime) {
										pair.player.sendError(Translate.您有囚禁BUFF不能复活);
										return;
									}
								}
							}
						}
					}*/
					PLAYER_REVIVED_REQ req = (PLAYER_REVIVED_REQ) msg;
					try {
						if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(pair.player.getId())) {
							if (RobberyConstant.DUJIEMAP.equals(gi.name)) {
								TransitRobberyManager.logger.info("[玩家正在渡劫中，不能使用协议复活][" + pair.player.getLogString() + "]");
								return;
							} else {
								TransitRobberyManager.getInstance().回城(pair.player, false);
							}
						}
						if(req.getRevivedType() == 0 &&  DownCityManager2.instance.cityMap.containsKey(new Long(pair.player.getId()))){
							if(DownCityManager2.instance.cityMap.get(new Long(pair.player.getId())).playerInGame(pair.player)){
								if(!DownCityManager2.instance.inTeamCityGame(pair.player)){
									DownCityManager2.instance.cityMap.get(new Long(pair.player.getId())).pMap().remove(new Long(pair.player.getId()));
									DownCityManager2.instance.cityMap.remove(new Long(pair.player.getId()));
									DownCityManager2.logger.warn("[单人副本回城复活] ["+pair.player.getLogString()+"]");
								}else{
									if(pair.player.getTeam() != null && pair.player.getTeam().getCity() != null){
//										pair.player.transferGameCountry = pair.player.getCountry();
//										pair.player.getTeam().setCity(null);
//										DownCityManager2.instance.cityMap.get(new Long(pair.player.getId())).pMap().remove(new Long(pair.player.getId()));
//										Game chuanCangGame = GameManager.getInstance().getGameByName(pair.player.getCurrentGame().gi.name, pair.player.getCountry());
//										MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
//										2219,1462
//										int x = 0;
//										int y = 0;
										if(pair.player.getCurrentGame().gi.name.equals("zhanmotianyu")){
											int x = 472;
											int y = 1052;
											pair.player.setHp(pair.player.getMaxHP() / 2);
											pair.player.setMp(pair.player.getMaxMP() / 2);
											pair.player.setState(Player.STATE_STAND);
											pair.player.notifyRevived();
											PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
											pair.player.addMessageToRightBag(res);
											this.transferGame(pair.player, new TransportData(0, 0, 0, 0, pair.player.getCurrentGame().gi.name, x, y));
											pair.player.setX(x);
											pair.player.setY(y);
											
											DownCityManager2.logger.warn("[家族远征] [地图:"+this.gi.name+"] [xy:"+x+"/"+y+"] ["+pair.player.getLogString()+"]");
										}else{
											CityConfig config = null;
											for(CityConfig cityConfig : DownCityManager2.instance.cityConfigs){
												if(cityConfig != null && cityConfig.getMapName().equals(pair.player.getCurrentGame().gi.name)){
													config = cityConfig;
												}
											}
											if(config != null){
												int x = config.getPlayerXY()[0];
												int y = config.getPlayerXY()[1];
												pair.player.setHp(pair.player.getMaxHP() / 2);
												pair.player.setMp(pair.player.getMaxMP() / 2);
												pair.player.setState(Player.STATE_STAND);
												pair.player.notifyRevived();
												PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
												pair.player.addMessageToRightBag(res);
												this.transferGame(pair.player, new TransportData(0, 0, 0, 0, pair.player.getCurrentGame().gi.name, x, y));
												pair.player.setX(x);
												pair.player.setY(y);
											}
											DownCityManager2.logger.warn("[多人副本回城复活] [config:"+config+"] ["+pair.player.getLogString()+"]");
										}
										return;
									}
								}
							}
						}
						
						
					} catch (Exception e) {
						TransitRobberyManager.logger.error("【玩家点击复活，渡劫出错】【" + pair.player.getLogString() + "】", e);
					}

					// 判断用户是否在战场中复活？
					if (pair.player.isInBattleField() == false || pair.player.getBattleField() == null) {
						if (req.getRevivedType() == 0) {
							{
								List<Buff> buffs = pair.player.getAllBuffs();
								if (buffs != null) {
									for (Buff buff : buffs) {
										if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
											if (buff.getInvalidTime() > heartBeatStartTime) {
												pair.player.sendError(Translate.您有囚禁BUFF不能复活);
												return;
											}
										}
									}
								}
							}
							{
								if (pair.player.getState() == Player.STATE_DEAD) {
//									this.country,pair.player.transferGameCountry,
									//3,2
									int oldCountry = pair.player.getCountry();
//									if(oldCountry != this.country){
//										oldCountry = this.country;
//									}
//									if(gi.getName().equals("bianjing")){
//										oldCountry = this.country;
//									}
									
									String mapName = pair.player.getResurrectionMapName();
									int tranx = pair.player.getResurrectionX();
									int trany = pair.player.getResurrectionY();
									
									if((this.country != 0 && oldCountry != this.country) || gi.getName().equals("bianjing")){
										mapName = TransportData.getMainCityMap(pair.player.getCountry());
										Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, pair.player.getCountry());
										if(chuanCangGame != null && chuanCangGame.gi != null){
											MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
											if(area != null){
												tranx = area.getX();
												trany = area.getY();
											}
										}
									}
									
									logger.warn("测试玩家复活:this.country:"+this.country+"---gi.getName():"+gi.getName()+"--pCountry:"+pair.player.getCountry()+"--name:"+pair.player.getName()+"--mapName:"+mapName);
									pair.player.setTransferGameCountry(oldCountry);
									int extraHp = 0;
									int extraMp = 0;
									if (pair.player.getAliveHpPercent() > 0) {
										float tempF = pair.player.getAliveHpPercent() / 100f;
										extraHp = (int) (pair.player.getMaxHP() * tempF);
										extraMp = (int) (pair.player.getMaxMP() * tempF);
									}
									pair.player.setHp(pair.player.getMaxHP() / 2);
									if (pair.player.getHp() + extraHp <= pair.player.getMaxHP()) {
										pair.player.setHp(pair.player.getHp() + extraHp);
									}
									pair.player.setMp(pair.player.getMaxMP() / 2);
									if (pair.player.getMp() + extraMp <= pair.player.getMaxMP()) {
										pair.player.setMp(pair.player.getMp() + extraMp);
									}
									pair.player.setState(Player.STATE_STAND);

									pair.player.notifyRevived();

									PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
									pair.player.addMessageToRightBag(res);
									// 对player的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
									List<Buff> buffs = pair.player.getAllBuffs();
									if (buffs != null) {
										for (Buff buff : buffs) {
											if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
												return;
											}
										}
									}

									// 国战复活特殊处理
									if (pair.player.isIsGuozhan()) {
										Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(pair.player);
										if (guozhan != null) {
											GuozhanBornPoint bp = guozhan.getBornPoint(pair.player);
											pair.player.setTransferGameCountry(bp.getMapCountry());
											int x = bp.getX();
											int y = bp.getY();
											int ranX = new Random().nextInt(30);
											int ranY = new Random().nextInt(30);
											ranX -= 15;
											ranY -= 15;
											x += ranX;
											y += ranY;
											this.transferGame(pair.player, new TransportData(0, 0, 0, 0, bp.getMapName(), x, y));
											pair.player.setLastGame(bp.getMapName());
											pair.player.setLastX(pair.player.getX());
											pair.player.setLastY(pair.player.getY());
											pair.player.setGame(bp.getMapName());
											pair.player.setX(x);
											pair.player.setY(y);
											if (GuozhanOrganizer.logger.isDebugEnabled()) GuozhanOrganizer.logger.debug("[玩家国战复活] [" + pair.player.getLogString() + "] [出生点:" + bp.getLogStr() + "]");
											return;
										}
									}

									// 迷城
									PetDaoManager pdm = PetDaoManager.getInstance();
									if (pdm.isPetDao(gi.name)) {
										int index = 0;
										for (int i = 0; i < pdm.mapnames.size(); i++) {
											if (pdm.mapnames.get(i).equals(gi.name)) {
												index = i;
												break;
											}
										}
										int xy[] = pdm.xypiont[index];
										this.transferGame(pair.player, new TransportData(0, 0, 0, 0, gi.name, xy[0], xy[1]));
										return;
									}
									
									if (pair.player.inSelfSeptStation()) {
										// 在自己家族的驻地
										SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(pair.player.getJiazuId());
										if (septStation != null) {
											if (septStation.isJiazuBossalive()) {
												// boss还活着,在驻地内复活
												String reliveName = SeptStationMapTemplet.getInstance().getReliveName();
												MapArea mapArea = septStation.getGame().gi.getMapAreaByName(reliveName);
												if (mapArea != null) {
													Fighter fs[] = this.getVisbleFighter(pair.player, false);
													int x = mapArea.getX();
													int y = mapArea.getY();
													pair.player.setX(x);
													pair.player.setY(y);
													pair.player.removeMoveTrace();
													SET_POSITION_REQ message = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), Game.REASON_TRANSPORT, pair.player.getClassType(), pair.player.getId(), (short) pair.player.getX(), (short) pair.player.getY());
													if (logger.isInfoEnabled()) {
														if (logger.isInfoEnabled()) logger.info("[处理跳转点] [成功] [{}][在livingSet:{}][{}] [{}] [{}] [家族地图同地图跳转] [({},{})]", new Object[] { gi.getName(), livingSet.contains(pair.player), pair.player.getGame(), pair.player.getUsername(), pair.player.getName(), pair.player.getX(), pair.player.getY() });

													}
													sendMessage(pair.player, message, "change game by transport");
													for (int i = 0; i < fs.length; i++) {
														if (fs[i] instanceof Player) {
															Player p = (Player) fs[i];
															if (p.getConn() != null) {
																sendMessage(p, message, "change game by transport");
															}
														}
													}
													pair.player.设置进入区域(this);
													if (JiazuSubSystem.logger.isInfoEnabled()) {
														JiazuSubSystem.logger.info(pair.player.getLogString() + "[家族BOSS活动] [复活成功] [复活区域:" + reliveName + "] [位置:(" + pair.player.getX() + "," + pair.player.getY() + ")]");
													}
													return;
												} else {
													JiazuSubSystem.logger.error(pair.player.getLogString() + "[家族BOSS活动] [配置异常] [复活区域不存在:" + reliveName + "]");
												}
											}
										}
									}
									
									logger.warn("[玩家回城复活] [currGameCountry:{}] [transferGameCountry:{}] [currGameName:{}] [mapName:{}] [复活点:{}] [xy:{}] [{}]",new Object[]{
										this.country,pair.player.transferGameCountry,(this.gi!=null?this.gi.name:"null"),mapName,pair.player.getResurrectionMapName(),pair.player.getResurrectionX()+"/"+pair.player.getResurrectionY(),pair.player.getLogString()
									});
									this.transferGame(pair.player, new TransportData(0, 0, 0, 0, mapName, tranx, trany));
									pair.player.setLastGame(mapName);
									// 给跳转地图复活的玩家一个无敌buff，避免在跳转过程中被杀死
									if (!pair.player.getResurrectionMapName().equals(pair.player.getGame())) {
										BuffTemplateManager btm = BuffTemplateManager.getInstance();
										BuffTemplate buffTemplate = btm.getBuffTemplateByName(Translate.无敌buff);
										Buff buff = buffTemplate.createBuff(1);
										if (buff != null) {
											buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 2000);
											pair.player.placeBuff(buff);
											pair.player.是否回城复活 = true;
										}
									}

									pair.player.setLastX(tranx);
									pair.player.setLastY(trany);
									pair.player.setGame(mapName);
								
									pair.player.setX(tranx);
									pair.player.setY(trany);
//									pair.player.setGame(pair.player.getResurrectionMapName());
//									pair.player.setX(pair.player.getResurrectionX());
//									pair.player.setY(pair.player.getResurrectionY());
									try {
										if (DevilSquareManager.instance.isPlayerIndevilSquareMap(pair.player)) {
											DevilSquareManager.instance.notifyUseTransProp(pair.player);
										}
									} catch (Exception e) {
										DevilSquareManager.logger.error("[恶魔广场] [通知玩家使用回城复活出异常] [" + pair.player.getLogString() + "]", e);
									}
									try {
										if (FairyChallengeManager.getInst().isPlayerChallenging(pair.player)) {
											FairyChallengeManager.getInst().notifyPlayerUseTransProp(pair.player);
										}
									} catch (Exception e) {
										DevilSquareManager.logger.error("[仙尊挑战] [通知玩家使用回城复活出异常] [" + pair.player.getLogString() + "]", e);
									}

								} else {
									PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
									pair.player.addMessageToRightBag(res);
								}
							}
						} else {
							try {
								if (DevilSquareManager.instance.isPlayerIndevilSquareMap(pair.player)) {
									if (!DevilSquareManager.instance.notifyPlayerRelive(pair.player)) {
										DevilSquareManager.logger.info("[恶魔广场][复活次数用尽][" + pair.player.getLogString() + "]");
										pair.player.sendError(Translate.恶魔广场副本中不允许原地复活);
										return;
									}
								}
							} catch (Exception e) {
								DevilSquareManager.logger.error("[恶魔广场中使用原地复活协议出错] [" + pair.player.getLogString() + "]", e);
								;
							}
							try {
								if (FairyChallengeManager.getInst().isPlayerChallenging(pair.player)) {
									pair.player.sendError(Translate.挑战仙尊不可原地复活);
									return;
								}
							} catch (Exception e) {
								DevilSquareManager.logger.error("[恶魔广场中使用原地复活协议出错] [" + pair.player.getLogString() + "]", e);
								;
							}
							try {
								if(BossCityManager.getInstance().isBossCityGame(pair.player)){
									pair.player.sendError(Translate.副本中不可原地复活);
									return;
								}
								if(DownCityManager2.instance.inCityGame(pair.player)){
									pair.player.sendError(Translate.副本中不可原地复活);
									return;
								}
								if(pair.player.getCurrentGame().gi.name.equals("zhanmotianyu")){
									pair.player.sendError(Translate.副本中不可原地复活);
									return;
								}
							} catch (Exception e) {
								DevilSquareManager.logger.error("[副本中不可原地复活协议出错] [" + pair.player.getLogString() + "]", e);
								;
							}
							try{
								if(DiceManager.getInstance().isDiceGame(pair.player)){
									pair.player.sendError(Translate.骰子副本不能原地复活);
									return;
								}
							}catch(Exception e){
								e.printStackTrace();
								DiceManager.logger.warn("[骰子迷城] [原地复活] [异常]",e);
							}
							
							if (pair.player.getState() == Player.STATE_DEAD) {

//								if (SiFangManager.getInstance().isInSiFangGame(pair.player)) {
//									// 五方圣兽复活
//									pair.player.sendError(Translate.您在五方圣兽活动中不能原地复活);
//									return;
//								}

								int r = pair.player.tryToRevived(ArticleManager.RELIVE_ARTICLE_NAME);
								String resultString[] = new String[] { Translate.成功, Translate.translateString(Translate.原地复活需要绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(pair.player.reviCostYinzi(pair.player.revivedCount)) } }), Translate.未到时间, Translate.未知错误 };
								if (r == 0) {
									pair.player.resetShouStat("原地复活");
									pair.player.notifyRevived();
								}
								PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) r, resultString[r], pair.player.getHp(), pair.player.getMp());
								pair.player.addMessageToRightBag(res);
							} else {
								PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
								pair.player.addMessageToRightBag(res);
							}
						}
					} else {
						// 让战场来处理此消息
						if (pair.player.getState() == Player.STATE_DEAD) {
							pair.player.getBattleField().playerRevived(pair.player, req);
						} else {
							PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) 0, Translate.text_1498, pair.player.getHp(), pair.player.getMp());
							pair.player.addMessageToRightBag(res);
						}
					}

				} else if (msg instanceof NPC_REPAIR_REPAIR_REQ) {
					int repairType = ((NPC_REPAIR_REPAIR_REQ) msg).getRepairType();
					int price = 0;
					// 批量加载背包物品
					pair.player.loadAllKnapsack();
					// 修理全部
					if (repairType == 0) {
						// 修理当前装备
						EquipmentColumn ec = pair.player.getEquipmentColumns();

						// EquipmentColumn ec2 = player.getEquipmentColumns();
						ArticleEntityManager aem = ArticleEntityManager.getInstance();
						// 批量加载装备栏
						long[] ids = ec.getEquipmentIds();
						aem.getEntityByIds(ids);
						ArticleManager am = ArticleManager.getInstance();
						try {
							for (int i = 0; i < ec.getEquipmentIds().length; i++) {
								if (ec.getEquipmentIds()[i] > 0) {
									ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
									if (aee instanceof EquipmentEntity) {
										// EquipmentEntity ee = (EquipmentEntity)aee ;
										price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) aee);
									}
								}
							}
							Knapsack knapsack = pair.player.getKnapsack_common();
							if (knapsack != null) {
								for (int i = 0; i < knapsack.getCells().length; i++) {
									if (knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0) {
										ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
										if (ae instanceof EquipmentEntity) {
											price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) ae);
										}
									}
								}
							}
							knapsack = pair.player.getKnapsack_fangbao();
							if (knapsack != null) {
								for (int i = 0; i < knapsack.getCells().length; i++) {
									if (knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0) {
										ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
										if (ae instanceof EquipmentEntity) {
											price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) ae);
										}
									}
								}
							}
						} catch (Exception ex) {
							ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]", new Object[] { pair.player.getUsername(), pair.player.getId(), pair.player.getName() }, ex);
						}
					} else {
						// 修理当前装备
						EquipmentColumn ec = pair.player.getEquipmentColumns();
						ArticleEntityManager aem = ArticleEntityManager.getInstance();
						// 批量加载装备栏
						long[] ids = ec.getEquipmentIds();
						aem.getEntityByIds(ids);
						ArticleManager am = ArticleManager.getInstance();
						try {
							for (int i = 0; i < ec.getEquipmentIds().length; i++) {
								if (ec.getEquipmentIds()[i] > 0) {
									ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
									// EquipmentEntity ee = (EquipmentEntity) aem.getEntity(ec.getEquipmentIds()[i]);
									if (aee instanceof EquipmentEntity) {
										price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) aee);
									}
								}
							}
						} catch (Exception ex) {
							ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]", new Object[] { pair.player.getUsername(), pair.player.getId(), pair.player.getName() }, ex);
						}
					}
					if (price == 0) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备无需修理);
						pair.player.addMessageToRightBag(hreq);
						return;
					} else {
						if(pair.player.getRepairDiscount() > 0) {
							float tempF = pair.player.getRepairDiscount() / 100F;
							int tempDiscount = (int) (price * tempF);
							if (tempDiscount > 0 && tempDiscount < price) {
								price -= tempDiscount;
							}
						}
						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						Option_RepairAllEquipment option = new Option_RepairAllEquipment();
						option.repairType = repairType;
						option.setText(Translate.确定);
						Option_Cancel cancel = new Option_Cancel();
						cancel.setText(Translate.取消);
						if (repairType == 0) {
							mw.setDescriptionInUUB(Translate.translateString(Translate.修理全部装备需要绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(price) } }));
						} else {
							mw.setDescriptionInUUB(Translate.translateString(Translate.修理当前所穿装备需要绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(price) } }));
						}
						Option[] options = new Option[] { option, cancel };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						pair.player.addMessageToRightBag(req);
					}
				} else if (msg instanceof REPAIR_CARRY_REQ) {
					VipManager vm = VipManager.getInstance();
					if (vm != null && vm.vip是否开启随身修理(pair.player)) {
						int repairType = ((REPAIR_CARRY_REQ) msg).getRepairType();
						int price = 0;
						// 批量加载背包物品
						pair.player.loadAllKnapsack();
						// 修理全部
						if (repairType == 0) {
							// 修理当前装备
							EquipmentColumn ec = pair.player.getEquipmentColumns();
							// EquipmentColumn ec2 = player.getEquipmentColumns();
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							// 批量加载装备栏
							long[] ids = ec.getEquipmentIds();
							aem.getEntityByIds(ids);
							ArticleManager am = ArticleManager.getInstance();
							try {
								for (int i = 0; i < ec.getEquipmentIds().length; i++) {
									if (ec.getEquipmentIds()[i] > 0) {
										ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
										// EquipmentEntity ee = (EquipmentEntity) aem.getEntity(ec.getEquipmentIds()[i]);
										if (aee instanceof EquipmentEntity) {
											price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) aee);
										}
									}
								}
								Knapsack knapsack = pair.player.getKnapsack_common();
								if (knapsack != null) {
									for (int i = 0; i < knapsack.getCells().length; i++) {
										if (knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0) {
											ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
											if (ae instanceof EquipmentEntity) {
												price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) ae);
											}
										}
									}
								}
								knapsack = pair.player.getKnapsack_fangbao();
								if (knapsack != null) {
									for (int i = 0; i < knapsack.getCells().length; i++) {
										if (knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0) {
											ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
											if (ae instanceof EquipmentEntity) {
												price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) ae);
											}
										}
									}
								}
							} catch (Exception ex) {
								ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]", new Object[] { pair.player.getUsername(), pair.player.getId(), pair.player.getName() }, ex);
							}
						} else if (repairType == 1 || repairType == 2) {
							// 修理当前装备
							EquipmentColumn ec = pair.player.getEquipmentColumns();
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							// 批量加载装备栏
							long[] ids = ec.getEquipmentIds();
							aem.getEntityByIds(ids);
							ArticleManager am = ArticleManager.getInstance();
							try {
								for (int i = 0; i < ec.getEquipmentIds().length; i++) {
									if (ec.getEquipmentIds()[i] > 0) {
										ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
										// EquipmentEntity ee = (EquipmentEntity) aem.getEntity(ec.getEquipmentIds()[i]);
										if (aee instanceof EquipmentEntity) {
											price += am.getEquipmentEntityRepairPrice(pair.player, (EquipmentEntity) aee);
										}
									}
								}
							} catch (Exception ex) {
								ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]", new Object[] { pair.player.getUsername(), pair.player.getId(), pair.player.getName() }, ex);
							}
						} else if (repairType == 3) { // 自动修理，无需确认框
							Option_RepairAllEquipment option = new Option_RepairAllEquipment();
							option.repairType = repairType;
							option.doSelect(pair.player.getCurrentGame(), pair.player);
							return;
						}
						if (price == 0) {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备无需修理);
							pair.player.addMessageToRightBag(hreq);
							return;
						} else {
							if(pair.player.getRepairDiscount() > 0) {
								float tempF = pair.player.getRepairDiscount() / 100F;
								int tempDiscount = (int) (price * tempF);
								if (tempDiscount > 0 && tempDiscount < price) {
									price -= tempDiscount;
								}
							}
							WindowManager wm = WindowManager.getInstance();
							MenuWindow mw = wm.createTempMenuWindow(600);
							Option_RepairAllEquipment option = new Option_RepairAllEquipment();
							option.repairType = repairType;
							option.setText(Translate.确定);
							Option_Cancel cancel = new Option_Cancel();
							cancel.setText(Translate.取消);
							if (repairType == 0) {
								mw.setDescriptionInUUB(Translate.translateString(Translate.修理全部装备需要绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(price) } }));
							} else {
								mw.setDescriptionInUUB(Translate.translateString(Translate.修理当前所穿装备需要绑银, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(price) } }));
							}
							Option[] options = new Option[] { option, cancel };
							mw.setOptions(options);
							CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
							pair.player.addMessageToRightBag(req);
						}
					} else {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的vip级别不足);
						pair.player.addMessageToRightBag(hreq);
					}
				} else if (msg instanceof TAKE_SEAL_TASK_REQ) {
					Player player = pair.player;
					TaskManager tm = TaskManager.getInstance();
					long taskId = SealManager.getInstance().getSealTaskId(player.getCountry());
					Task task = tm.getTask(taskId);
					logger.warn("[接受封印任务] [] [" + player.getLogString() + "]");
					if (task != null) {
						int max = task.getDailyTaskMaxNum();
						TaskEntity te = player.getTaskEntity(taskId);
						int leftCount = 0;
						if (te != null) {
							leftCount = te.getCycleDeilverInfo().getIntValue();
						} else {
							leftCount = task.getDailyTaskMaxNum();
						}
						if (leftCount <= 0) {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.不能接取任务);
							player.addMessageToRightBag(hreq);
							logger.warn("[接受封印任务] [失败1] [" + player.getLogString() + "]");
							return;
						}
						int cost = SealManager.得到发布封印任务的费用(max, leftCount);
						if (player.getSilver() + player.getShopSilver() < cost) {
							BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
							logger.warn("[接受封印任务] [失败2] [" + player.getLogString() + "]");
							return;
						}
						CompoundReturn returns = player.takeOneTask(task, false, null);
						if (returns != null && returns.getBooleanValue()) {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.SEAL_TASK, "封印任务");
								player.addTaskByServer(task);
								logger.warn("[接受封印任务] [成功] [" + player.getLogString() + "] [cost:" + cost + "]");
							} catch (Exception ex) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
								player.addMessageToRightBag(hreq);
								logger.warn("[接受封印任务] [失败3] [" + player.getLogString() + "]");
								return;
							}
						} else {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.不能接取任务);
							player.addMessageToRightBag(hreq);
							logger.warn("[接受封印任务] [失败4] [" + player.getLogString() + "]");
							return;
						}
					} else {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.服务器物品出现错误);
						pair.player.addMessageToRightBag(hreq);
						logger.warn("[接受封印任务] [失败5] [" + player.getLogString() + "]");
					}
				} else if (msg instanceof DOWNCITY_PLAYER_STATUS_CHANGE_REQ) {
					DOWNCITY_PLAYER_STATUS_CHANGE_REQ message = (DOWNCITY_PLAYER_STATUS_CHANGE_REQ) msg;
					DownCityManager dcm = DownCityManager.getInstance();
					if (dcm != null) {
						dcm.队员修改队列状态(pair.player, message.getStatus());
					}
				} else if (msg instanceof CHANAGE_PLAYER_AVATA_REQ) {
					TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(pair.player.getId());
					if (entity == null || entity.getFeisheng() != 1) {
						pair.player.sendError(Translate.未飞升不能切换);
					} else {
						Long ti = TransitRobberyManager.getInstance().getPlayerChanageAvataTimes().get(pair.player.getId());
						if (ti != null && ti.longValue() > System.currentTimeMillis()) {
							pair.player.addMessageToRightBag(new CHANAGE_PLAYER_AVATA_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.切换形象太过频繁, ti.longValue() - System.currentTimeMillis()));
						} else {
							if (pair.player.getAvataRace().equals(Constants.race_human)) {
								pair.player.setAvataRace(Constants.race_human_new);
							} else {
								pair.player.setAvataRace(Constants.race_human);
							}
							ResourceManager.getInstance().getAvata(pair.player);
							try {
								Soul soul = pair.player.getCurrSoul();
								for (long horseId : soul.getHorseArr()) {
									Horse horse = HorseManager.getInstance().getHorseById(horseId, pair.player);
									if (horse != null && !horse.isFly()) {
										ResourceManager.getInstance().getHorseAvataForPlayer(horse, pair.player);
										horse.selfMarks[31] = true;
										horse.notifySelfChange();
									}
								}
							} catch (Exception e) {
								HorseManager.logger.error("[新坐骑系统] [玩家切换形象坐骑形象紧跟变化] [异常] [" + pair.player.getLogString() + "]", e);
							}
							TransitRobberyManager.getInstance().getPlayerChanageAvataTimes().put(pair.player.getId(), System.currentTimeMillis() + TransitRobberyManager.CHANAGE_AVATA_CD);
							pair.player.addMessageToRightBag(new CHANAGE_PLAYER_AVATA_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, "", TransitRobberyManager.CHANAGE_AVATA_CD));
							logger.warn("[用户切换形象成功] [" + pair.player.getLogString() + "] [" + Arrays.toString(pair.player.getAvata()) + "]");
						}
					}
				} else if (msg instanceof DOWNCITY_CREATE_REQ) {
					DownCityManager dcm = DownCityManager.getInstance();
					if (dcm != null) {
						dcm.创建并进入副本(pair.player);
					}
				} else if (msg instanceof TUNSHI_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					TUNSHI_REQ req = (TUNSHI_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity mainAe = aem.getEntity(req.getMainArticleId());
					ArticleEntity materialAe = aem.getEntity(req.getMaterialArticleId());
					am.吞噬弹窗(pair.player, mainAe, materialAe, req.getMainBagType(), req.getMaterialBagType(), req.getTunshiType());
				} else if (msg instanceof XILIAN_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					XILIAN_REQ req = (XILIAN_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae = aem.getEntity(req.getArticleId());
					am.器灵洗练(pair.player, ae, req.getXilianType());
				} else if (msg instanceof QILING_INLAY_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					QILING_INLAY_REQ req = (QILING_INLAY_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ee = aem.getEntity(req.getEquipmentId());
					ArticleEntity ae = aem.getEntity(req.getMaterialId());
					if (ee instanceof EquipmentEntity) {
						am.器灵镶嵌(pair.player, (EquipmentEntity) ee, ae, req.getHoleIndex());
					}

				} else if (msg instanceof QILING_OUTLAY_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					QILING_OUTLAY_REQ req = (QILING_OUTLAY_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ee = aem.getEntity(req.getEquipmentId());
					if (ee instanceof EquipmentEntity) {
						am.器灵挖取(pair.player, (EquipmentEntity) ee, req.getHoleIndex());
					}
				} else if (msg instanceof LIANQI_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					LIANQI_REQ req = (LIANQI_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ee = aem.getEntity(req.getArticleId());
					if (ee instanceof EquipmentEntity) {
						am.炼器申请(pair.player, (EquipmentEntity) ee, req.getMaterialId(), req.getBindIndexs());
					}
				} else if (msg instanceof KNAPSACK_QILING_MOVE_REQ) {
					ArticleManager am = ArticleManager.getInstance();
					KNAPSACK_QILING_MOVE_REQ req = (KNAPSACK_QILING_MOVE_REQ) msg;
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae = aem.getEntity(req.getQilingId());
					// 0把器灵移动到背包，1把器灵移动到器灵包
					byte moveType = req.getMoveType();
					Knapsack k1 = pair.player.getKnapsack_common();
					Knapsack k2 = pair.player.getKnapsacks_QiLing();
					if (ae instanceof QiLingArticleEntity) {
						if (moveType == 0) {
							if (k1.isFull()) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.背包已满);
								pair.player.addMessageToRightBag(hreq);
								return;
							}
							if (!k2.contains(ae)) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的器灵仓库中没有这个物品);
								pair.player.addMessageToRightBag(hreq);
								return;
							}
							ArticleEntity removeAe = k2.removeByArticleId(ae.getId(), "从器灵包移动到背包", false);
							if (removeAe != null) {
								k1.put(removeAe, "从器灵包移动到背包");
							}
						} else {
							if (k2.isFull()) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.器灵包已满);
								pair.player.addMessageToRightBag(hreq);
								return;
							}
							if (!k1.contains(ae)) {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的背包中没有这个物品);
								pair.player.addMessageToRightBag(hreq);
								return;
							}
							ArticleEntity removeAe = k1.removeByArticleId(ae.getId(), "从背包移动到器灵包", false);
							if (removeAe != null) {
								k2.put(removeAe, "从背包移动到器灵包");
							}
						}
					}
				} else if (msg instanceof QIECUO_INVITE_REQ) {

				} else if (msg instanceof QIECUO_BE_INVITED_RESPONSE_REQ) {

				} else if (pair.message instanceof SET_QUEUE_READYNUM_REQ) {
					SET_QUEUE_READYNUM_REQ req = (SET_QUEUE_READYNUM_REQ) pair.message;
					if (req.getSelectedQueueReadyNum() >= 4) {
						pair.player.setAroundNotifyPlayerNum(0);
						pair.player.send_HINT_REQ(Translate.text_2902);
					} else {
						pair.player.setAroundNotifyPlayerNum(255);
					}
				} else if (msg instanceof QUERY_WINDOW_REQ) {
					doQueryWindowReq((QUERY_WINDOW_REQ) msg, pair.player);
				} else if (msg instanceof OPTION_SELECT_REQ) {
					WindowManager windowManager = WindowManager.getInstance();
					OPTION_SELECT_REQ req = (OPTION_SELECT_REQ) msg;
					windowManager.doSelect(this, pair.player, req);
				} else if (msg instanceof OPTION_INPUT_REQ) {
					WindowManager windowManager = WindowManager.getInstance();
					OPTION_INPUT_REQ req = (OPTION_INPUT_REQ) msg;
					windowManager.doSelect(this, pair.player, req);
				} else if (msg instanceof SAVE_SHORTCUT_REQ) {
					SAVE_SHORTCUT_REQ req = (SAVE_SHORTCUT_REQ) msg;
					byte d[] = req.getShortcutData();

					try {
						ShortcutAgent agent = new ShortcutAgent();
						agent.load(d);
						d = agent.toByteArray();
						pair.player.setShortcut(d);
						if (logger.isInfoEnabled())
						// logger.info("快捷键设置，成功" + pair.player.getName());
						if (logger.isInfoEnabled()) logger.info("快捷键设置，成功{}", new Object[] { pair.player.getName() });
					} catch (Exception e) {
						if (logger.isWarnEnabled()) logger.warn("快捷键设置，失败" + pair.player.getName(), e);
					}
				} else if (msg instanceof FUNCTION_NPC_REQ) {
					FUNCTION_NPC_REQ req = (FUNCTION_NPC_REQ) msg;
					TaskManager tm = TaskManager.getInstance();
					FunctionNPC fns[] = tm.getFunctionNPCsByGame(this, pair.player);
					FUNCTION_NPC_RES res = new FUNCTION_NPC_RES(req.getSequnceNum(), fns);
					pair.player.addMessageToRightBag(res);

				} else if (msg instanceof HORSE_RIDE_REQ) {
					long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					HORSE_RIDE_REQ req = (HORSE_RIDE_REQ) msg;
					long horseId = req.getHorseId();
					boolean ride = req.getRide();
					HORSE_RIDE_RES res = null;
					Player player = pair.player;
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[玩家坐骑请求] [" + player.getLogString() + "] [id:" + horseId + "] [上true:" + ride + "]");
					}
					if (player.getHorseIdList().contains(horseId)) {
						Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
						if (horse != null) {
							if (ride) {
								if (!player.isIsUpOrDown()) {
									player.upToHorse(horseId);

								} else {
									return;
								}
							} else {
								if (player.isIsUpOrDown()) {
									player.downFromHorse();
								} else {
									return;
								}
							}

						} else {
							// logger.error("[骑乘坐骑] [失败：请求坐骑不存在] [参数:"+horseId+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"]", start);
							HorseManager.logger.error("[骑乘坐骑] [失败：请求坐骑不存在] [参数:{}] [{}] [{}] [{}] [{}]", new Object[] { horseId, player.getId(), player.getName(), player.getUsername(), start });
						}
					} else {
						// logger.error("[骑乘坐骑] [失败：用户没有这个坐骑id] [参数:"+horseId+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"]", start);
						HorseManager.logger.error("[骑乘坐骑] [失败：用户没有这个坐骑id] [参数:{}] [{}] [{}] [{}] [{}]", new Object[] { horseId, player.getId(), player.getName(), player.getUsername(), start });
					}
				} else if (msg instanceof SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ) {
					try {
						Skill.logger.warn("[checksync] [" + msg.getTypeDescription() + "] [" + pair.player.getLogString() + "]");
						SYNC_MAGICWEAPON_FOR_KNAPSACK_RES res = MagicWeaponManager.instance.checkMagicWeaponMess(pair.player, (SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ) msg);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof QUERY_SHENSHI_REQ) {
					try {
						QUERY_SHENSHI_RES res = MagicWeaponManager.instance.queryShenShi(pair.player, (QUERY_SHENSHI_REQ) msg);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof CONFIRM_MAGICWEAPON_EAT_REQ) {
					try {
						CONFIRM_MAGICWEAPON_EAT_RES res = MagicWeaponManager.instance.confirmMagicWeaponEat(pair.player, (CONFIRM_MAGICWEAPON_EAT_REQ) msg, false);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof QUERY_MAGICWEAPON_EAT_REQ) {
					try {
						QUERY_MAGICWEAPON_EAT_RES res = MagicWeaponManager.instance.queryMagicWeaponEat(pair.player, (QUERY_MAGICWEAPON_EAT_REQ) msg);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof CONFIRM_SHENSHI_REQ) {
					try {
						MagicWeaponManager.instance.confirmShenShi(pair.player, (CONFIRM_SHENSHI_REQ) msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof QUERY_MAGICWEAPON_STRONG_REQ) {
					try {
						MagicWeaponManager.instance.logger.warn("[QUERY_MAGICWEAPON_STRONG_REQ] [length:" + msg.getLength() + "] [type:" + msg.getType() + "] [TEST] [player:" + pair.player.getLogString() + "]");
						QUERY_MAGICWEAPON_STRONG_RES res = MagicWeaponManager.instance.queryMagicWeaponStrong(pair.player, (QUERY_MAGICWEAPON_STRONG_REQ) msg);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof MAGICWEAPON_STRONG_REQ) {
					try {
						MagicWeaponManager.instance.magicWeaponStrong(pair.player, (MAGICWEAPON_STRONG_REQ) msg);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (msg instanceof QUERY_MAGICWEAPON_HIDDLE_PROP_REQ) {
					try {
						QUERY_MAGICWEAPON_HIDDLE_PROP_RES res = MagicWeaponManager.instance.queryHiddleProp(pair.player, (QUERY_MAGICWEAPON_HIDDLE_PROP_REQ) msg);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ) {
					try {
						JIHUO_MAGICWEAPON_HIDDLE_PROP_RES res = MagicWeaponManager.instance.jihuoMagicWeapon(pair.player, (JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ) msg, false);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg instanceof REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ) {
					try {
						REFRESH_MAGICWEAPON_HIDDLE_PROP_RES res = MagicWeaponManager.instance.refrshMagicWeaponProp(pair.player, (REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ) msg, false);
						pair.player.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// logger.warn("[unkown_message_pair] [" + msg.getTypeDescription() + "]");
					if (logger.isWarnEnabled()) logger.warn("[unkown_message_pair] [{}]", new Object[] { msg.getTypeDescription() });
				}// end of msg

				if (handleMessagelogger.isDebugEnabled()) {
					// handleMessagelogger.debug("[游戏心跳处理消息] [" + gi.getName() + "] [" + Thread.currentThread().getName() + "] [" + pair.player.getUsername()
					// + "] [" + pair.player.getName() + "] [" + pair.message.getTypeDescription() + "] [cost:" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() -
					// startTime)
					// + "]");
					if (handleMessagelogger.isDebugEnabled()) {
						handleMessagelogger.debug("[游戏心跳处理消息] [{}] [{}] [{}] [{}] [{}] [cost:{}]", new Object[] { gi.getName(), Thread.currentThread().getName(), pair.player.getUsername(), pair.player.getName(), pair.message.getTypeDescription(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
					}
				}

			} else if (obj instanceof LeaveGameEvent) {
				playerLeaveGame(((LeaveGameEvent) obj).getPlayer());
			} else if (obj instanceof ReconnectEvent) {
				playerReconnectGame(((ReconnectEvent) obj).getPlayer());
			} else if (obj instanceof TransferGameEvent) {
				TransferGameEvent event = (TransferGameEvent) obj;
				transferGame(event.getPlayer(), event.getTransportData());
			}

		}
	}
	
	public static int[] 官方专用npcWid = new int[]{300090, 300091, 51173};

	private void doQueryWindowReq(QUERY_WINDOW_REQ req, Player player) {

		long npcId = req.getNpcId();
		long id2 = 0;
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = null;
		if (npcId < 0) {

		} else {
			MemoryNPCManager nm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			id2 = req.getNpcId();
			NPC npc = nm.getNPC(id2);
			if (npc != null) {
				if (npc.getCountry() != 0 && npc.getCountry() != player.getCountry()) {

					if (player.getPeopleSearch() != null && player.getPeopleSearch().isMessageNpc(npc, this)) {
						mw = windowManager.createTempMenuWindow(60);
						mw.setNpcId(npcId);
						mw.setOptions(new Option[0]);
						mw.setDescriptionInUUB(Translate.打探消息);
						mw = PeopleSearchManager.getInstance().addOption(player, npc, mw, this);
						QUERY_WINDOW_RES r = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						player.addMessageToRightBag(r);
					} else {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.边去忙着呢);
						player.addMessageToRightBag(hreq);
					}
					return;
				}
				Collection<Option> questionEntrances = new ArrayList<Option>();
				if (npc instanceof SeedNpc) {
					if (((SeedNpc) npc).getCurrentState() == SeedNpc.UN_RIPE_STATE) {

					} else if (((SeedNpc) npc).getCurrentState() == SeedNpc.RIPE_STATE_AND_PLANTPLAYER_ONLY) {

					} else if (((SeedNpc) npc).getCurrentState() == SeedNpc.RIPE_STATE_AND_ALLPLAYER_CAN) {

					}
				} else if (npc instanceof FlopCaijiNpc) {
					FlopCaijiNpc fnpc = (FlopCaijiNpc) npc;
					fnpc.pickUp(player);
				} else if (npc instanceof OreNPC) {
					OreNPC onpc = (OreNPC) npc;
					onpc.点击弹出菜单(player);
					// if (VillageFightManager.logger.isInfoEnabled()) VillageFightManager.logger.info("[点击占领灵矿] [{}] [{}] [{}]", new Object[] { player.getUsername(),
					// player.getId(), player.getName() });
				} else if (npc instanceof FrunaceNPC) {
					FrunaceNPC fnpc = (FrunaceNPC) npc;
					fnpc.pickUp(player);
				} else if (npc instanceof ChestFightNPC) {
					ChestFightNPC cnpc = (ChestFightNPC) npc;
					cnpc.pickUp(player);
				} else if (npc instanceof DisasterFireNPC) {
					return ;
				} else if (npc instanceof FireWallNPC) {
					return;
				} else if (npc instanceof CaveNPC) {// 洞府NPC
					CaveNPC caveNPC = (CaveNPC) npc;
					int wid = npc.getWindowId();
					try {
						mw = windowManager.getWindowById(wid);

					} catch (Exception e) {
						FaeryManager.logger.error(player.getLogString() + "[洞府NPC异常]", e);
					}

					// 遍历玩家身上所有的任务实体，逐个处理STATUS_TAKE状态的任务实体
					// 对每个任务遍历任务目标，找到答题类型且触发答题的NPC正是当前交互NPC的任务目标
					// 核对当前目标的进度，构建答题窗口

					if (mw != null) {
						MenuWindow temp = null;
						try {
							temp = (MenuWindow) mw.clone();
						} catch (CloneNotSupportedException e1) {
							logger.error("[window clone error]", e1);
						}
						mw = WindowManager.getInstance().createTempMenuWindow(180);

						if (FaeryManager.logger.isDebugEnabled()) {
							FaeryManager.logger.debug(player.getLogString() + "[queryWindow] [npc.getWindowId(){}] [mw:{}]", new Object[] { wid, mw.getId() });
						}
						if (caveNPC.getCave() == null) {
							mw.setTitle(npc.getName());
							mw.setDescriptionInUUB(temp.getDescriptionInUUB());
						} else {
							CaveBuilding cb = caveNPC.getCave().getBuildings().get(caveNPC.getId());
							if (cb != null) {
								mw.setTitle(cb.getGrade() + Translate.级 + caveNPC.getName());
								try {
									String[] values = FaeryManager.getInstance().getLvUpInfo(cb.getType(), cb.getGrade()).getStringValues();
									mw.setDescriptionInUUB(values[0]);
								} catch (OutOfMaxLevelException e) {
									FaeryManager.logger.error("[查看当前等级描述异常]", e);
								}
							}
						}
						mw.setNpcId(npcId);
						mw.setShowTask(true);
						mw.setWidth(temp.getWidth());
						mw.setHeight(temp.getHeight());

						for (Option option : temp.getOptions()) {
							if (option instanceof CaveOption) {
								((CaveOption) option).setNpc(caveNPC);
								if (caveNPC.getCave().getOwnerId() != player.getId()) {
									// GameManager.logger.error(player.getLogString() + "[点击仙府建筑] [出现异常] [不是自己的仙府] [建筑:{}] [NPCID:{}] [id:{}] [id2:{}] [mwID:{}] [NNDwid:{}]", new
									// Object[] { caveNPC.getName(), caveNPC.getId(), npcId, id2, mw.getId(), wid });
								}
							}
						}
						addOption2MenuWindow(player, mw, temp.getOptions());
						addOption2MenuWindow(player, mw, questionEntrances.toArray(new Option[0]));
					}
				} else if (npc instanceof ForLuckFruitNPC) {// 祝福果树NPC
					ForLuckFruitNPC forLuckNPC = (ForLuckFruitNPC) npc;
					int wid = npc.getWindowId();
					try {
						mw = (MenuWindow) windowManager.getWindowById(wid).clone();
					} catch (Exception e) {
						if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info(player.getLogString() + "[NPC异常]", e);
					}

					// 遍历玩家身上所有的任务实体，逐个处理STATUS_TAKE状态的任务实体
					// 对每个任务遍历任务目标，找到答题类型且触发答题的NPC正是当前交互NPC的任务目标
					// 核对当前目标的进度，构建答题窗口

					if (mw != null) {
						MenuWindow temp = mw;
						mw = WindowManager.getInstance().createTempMenuWindow(180);
						mw.setTitle(npc.getName());
						mw.setNpcId(npcId);
						mw.setShowTask(true);
						mw.setWidth(temp.getWidth());
						mw.setHeight(temp.getHeight());
						for (Option option : temp.getOptions()) {
							if (option instanceof Option_ForLuck) {
								((Option_ForLuck) option).setNpc(forLuckNPC);
							}
						}

						addOption2MenuWindow(player, mw, temp.getOptions());
					}
				}else if(npc.getName().equals("仙兽房")){
						PetSubSystem.getInstance().handlePetHouse2(player);
						Game.logger.warn("[请求窗口SeptStationNPC] [npc:"+npc.getName()+"] ["+player.getLogString()+"]");
				} else if (this.contains(npc)) {
					player.talkWithOneNPC(npc.getName());
					try {
						RecordAction recordAction = null;
						for (int i = 0; i < PlayerAimManager.特殊处理npcId.length; i++) {
							if (npc.getnPCCategoryId() == PlayerAimManager.特殊处理npcId[i]) {
								PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(PlayerAimManager.特殊处理npc对应仙录id[i]);
								if (pam != null && PlayerAimeEntityManager.instance.isAimComplete(player.getId(), pam)) {
									break;
								}
								recordAction = PlayerAimManager.特殊处理NPC目标[i];
								if (recordAction != null) {
									AchievementManager.getInstance().record(player, recordAction);
								}
								MenuWindow mw1 = WindowManager.getInstance().createTempMenuWindow(600);
								mw1.setDescriptionInUUB(PlayerAimManager.特殊处理npc弹窗[i]);
								mw1.setNpcId(-1);
								QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw1, new Option[0]);
								player.addMessageToRightBag(res);
								break;
							}
						}
					} catch (Exception e) {
						PlayerAimManager.logger.warn("[目标系统] [统计玩家仙界npc探索] [异常] [" + player.getLogString() + "]", e);
					}
					int wid = npc.getWindowId();
					if (wid < 0 && nm.getNpcWidMap().containsKey(npc.getnPCCategoryId())) {
						wid = nm.getNpcWidMap().get(npc.getnPCCategoryId());
					}
					mw = windowManager.getWindowById(wid);
					if (JiazuSubSystem.logger.isDebugEnabled()) {
						JiazuSubSystem.logger.debug("[测试] [查看npcWid:" + wid + "] [" + npc.getName() + "] [" + npc.getnPCCategoryId() + "] [" + mw + "]");
					}
					try {
						if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
							for (int i=0; i<官方专用npcWid.length; i++) {
								if (wid == 官方专用npcWid[i]) {
									if (logger.isDebugEnabled()) {
										logger.debug("[玩家点击非官方NPC] [不显示内容] [" + npc.getId() + "," + npc.getName() + "," + npc.getNPCCategoryId() + "] [" + player.getLogString() + "]");
									}
									return ;
								}
							}
						}
					} catch (Exception e) {
						logger.warn("[判断非官方NPC] [异常] [" + player.getLogString() + "] [" + npc.getName() + "]", e);
					}

					// 遍历玩家身上所有的任务实体，逐个处理STATUS_TAKE状态的任务实体
					// 对每个任务遍历任务目标，找到答题类型且触发答题的NPC正是当前交互NPC的任务目标
					// 核对当前目标的进度，构建答题窗口

					if (mw != null) {
						// NPC身上本来就有事先编辑的功能窗口
						MenuWindow newMW = windowManager.createTempMenuWindow(600);

						newMW.setShowTask(true);
						newMW.setNpcId(npcId);
						newMW.setTitle(mw.getTitle());
						newMW.setDescriptionInUUB(mw.getDescriptionInUUB());

						ArrayList<Option> optionList = new ArrayList<Option>();
						for (int i = 0; i < mw.getOptions().length; i++) {
							Option option = mw.getOptions()[i].copy(this, player);
							if (option != null) {
								optionList.add(option);
								if (option instanceof NeedRecordNPC) {
									if (JiazuSubSystem.logger.isInfoEnabled()) {
										JiazuSubSystem.logger.info("[被菜单记录下NPC] [npc:" + npc.getName() + "] [菜单:" + option + "]");
									}
									((NeedRecordNPC) option).setNPC(npc);
								}
							}

						}
						addOption2MenuWindow(player, newMW, optionList.toArray(new Option[0]));
						// newMW.setOptions(options);
						newMW.setWidth(mw.getWidth());
						newMW.setHeight(mw.getHeight());

						mw = newMW;

					} else {
						// NPC身上没有功能窗口
						String desc;
						if (npc.getDialogContent() != null && npc.getDialogContent().length() > 0 && !npc.getDialogContent().equals("null")) {
							desc = npc.getDialogContent();
						} else {
							desc = Translate.你好 + ":" + player.getName() + "！";
						}
						if (questionEntrances.isEmpty()) {
							mw = new MenuWindow();
						} else {
							mw = WindowManager.getInstance().createTempMenuWindow(180);
							mw.setOptions(questionEntrances.toArray(new Option[questionEntrances.size()]));
						}
						if (npc instanceof SeptStationNPC) {// 是家族建筑Npc
							NpcStation ns = SeptStationMapTemplet.getInstance().getNpcStationByCategoryId(npc.getnPCCategoryId());
							if (ns != null) {
								desc = ns.getDes();
							} else {
								JiazuSubSystem.logger.error("[配置未拿到]" + npc.getnPCCategoryId());
							}
							if (JiazuSubSystem.logger.isDebugEnabled()) {
								JiazuSubSystem.logger.debug("[测试] [npcId:" + npc.getId() + "] [" + npc.getName() + "] [" + player.getLogString() + "]");
							}
						}
						mw.setTitle(npc.getName());
						mw.setDescriptionInUUB(desc);
						mw.setNpcId(npcId);
						mw.setShowTask(true);
					}
				} else {
					mw = new MenuWindow();
					mw.setId(0);
					mw.setTitle(Translate.错误);
					mw.setDescriptionInUUB(Translate.错误);
					mw.setNpcId(npcId);
					mw.setShowTask(false);
					mw.setOptions(new Option[0]);
				}
				{
					// 增加寻人活动的数据,针对所有NPC,国外NPC如何?
					mw = PeopleSearchManager.getInstance().addOption(player, npc, mw, this);
				}
				// if (mw != null) {
				// for (Option option : mw.getOptions()) {
				// if (option instanceof NeedRecordNPC) {
				// ((NeedRecordNPC) option).setNPC(npc);
				// }
				// }
				// }
			} else {
				logger.error("[查询窗口时候NPC是null][id:{}]", new Object[] { req.getNpcId() });
			}
		}
		if (mw != null) {
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(req.getSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		}
	}

	private void sendLeftArticleToClient(long spriteId, Player player, FlopSchemeEntity fse) {

	}

	private void doPlayerMovetraceReq(Player player, PLAYER_MOVETRACE_REQ msg) {
		// 矫正协议的错误
		MoveTrace4Client mt = msg.getMoveTrace4Client();
		if (mt.getType() == player.getClassType()) {

			player.notifyReceivePlayerMove((PLAYER_MOVETRACE_REQ) msg);

			if (player.getState() == Player.STATE_DEAD) {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能移动] ", new Object[] { gi.name, player.getUsername(), player.getName() });
				return;
			}
			// TODO 指令合法性未验证

			if (GameNetworkFramework.statFlowUserSet.contains(player.getUsername())) {
				logger.warn("[控制杆路经] [username:" + player.getUsername() + "] [Player:" + player.getName() + "] [玩家位置(" + player.getX() + "," + player.getY() + ")] [路径(" + mt.toString() + ")] [时间：" + (mt.getDestineTimestamp() - System.currentTimeMillis()) + "ms] [客户端信息：" + (mt.getDestineTimestamp() - mt.getStartTimestamp()) + "ms,speed:" + mt.getSpeed() + "]");
			} else if (logger.isDebugEnabled()) {
				if (logger.isDebugEnabled()) {
					logger.debug("[控制杆路经] [username:" + player.getUsername() + "] [Player:" + player.getName() + "] [玩家位置(" + player.getX() + "," + player.getY() + ")] [路径(" + mt.toString() + ")] [时间：" + (mt.getDestineTimestamp() - System.currentTimeMillis()) + "ms] [客户端信息：" + (mt.getDestineTimestamp() - mt.getStartTimestamp()) + "ms,speed:" + mt.getSpeed() + "]");
				}
			}

			MoveTrace path = new MoveTrace(msg.getMoveTrace4Client());

			player.notifyClientMoveTrace(path);
			if (path.getTotalLength() != 0 && player.getSpeed() > 0) {
				path.setStartTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				path.setDestineTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + path.getTotalLength() * 1000L / player.getSpeed());
			}
			player.getActiveSkillAgent().breakExecutingByPlayerMove(this);
			player.setMoveTrace(path);

			// 走路修改仙缘状态
			if (player.isSitdownState()) {
				player.setSitdownState(false);
			}

			if (!livingSet.contains(player)) {
				if (logger.isDebugEnabled()) {
					if (logger.isDebugEnabled()) {
						logger.debug("[{}] [{}] [{}] [玩家不在地图的LivingSet]", new Object[] { gi.getName(), player.getUsername(), player.getName() });
					}
				}
			}
		} else if (mt.getType() == Constants.SPRITE && mt.getId() == player.getActivePetId()) {
			/**
			 * 玩家的宠物移动
			 */
			PetManager pm = PetManager.getInstance();
			Pet pet = pm.getPet(mt.getId());
			if (pet == null) {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [宠物:{}][非法操作] [找不到宠物,不能移动] ", new Object[] { gi.name, player.getUsername(), player.getName(), mt.getId() });
				return;
			}
			if (pet.getState() == LivingObject.STATE_DEAD) {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [宠物:{},{}][非法操作] [已死亡不能移动] ", new Object[] { gi.name, player.getUsername(), player.getName(), mt.getId(), pet.getName() });
				return;
			}
			// TODO 指令合法性未验证

			MoveTrace path = new MoveTrace(msg.getMoveTrace4Client());
			// pet.notifyClientMoveTrace(path);

			pet.getSkillAgent().breakExecutingByPlayerMove(this);

			if (path.getTotalLength() != 0 && pet.getSpeed() > 0) {
				path.setStartTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				path.setDestineTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + path.getTotalLength() * 1000L / pet.getSpeed());
			}
			pet.setMoveTrace(path);

			if (logger.isDebugEnabled()) {
				if (logger.isDebugEnabled()) {
					logger.debug("[宠物{} 寻路路经] [主人:{}] [Player:{}] [宠物位置({},{})] [路径({})] [时间：{}ms] [客户端信息：{}ms,{}]", new Object[] { pet.getName(), player.getUsername(), player.getName(), pet.x, pet.y, pet.getMoveTrace(), (path.getDestineTimestamp() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis()), (msg.getMoveTrace4Client().getDestineTimestamp() - msg.getMoveTrace4Client().getStartTimestamp()), msg.getMoveTrace4Client().getSpeed() });
				}
			}

			if (!livingSet.contains(player)) {
				if (logger.isDebugEnabled()) {
					// logger.debug("[" + gi.getName() + "] [" + player.getUsername() + "] [" + player.getName() + "] [玩家不在地图的LivingSet]");
					if (logger.isDebugEnabled()) logger.debug("[{}] [{}] [{}] [玩家不在地图的LivingSet]", new Object[] { gi.getName(), player.getUsername(), player.getName() });
				}
			}
		} else if (mt.getType() == Constants.SPRITE && mt.getId() == player.getActiveMagicWeaponId()) {
			/** 法宝移动 */
			NPC npc = MemoryNPCManager.getNPCManager().getNPC(player.getActiveMagicWeaponId());
			if (npc instanceof MagicWeaponNpc) {
				MagicWeaponNpc mnpc = (MagicWeaponNpc) npc;
				MoveTrace path = new MoveTrace(msg.getMoveTrace4Client());
				if (path.getTotalLength() != 0 && mnpc.getSpeed() > 0) {
					path.setStartTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					path.setDestineTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + path.getTotalLength() * 1000L / mnpc.getSpeed());
				}
				mnpc.setMoveTrace(path);
				if (MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[收到客户端发送的法宝路径][ " + player.getLogString() + "] [" + mt.getId() + "]");
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [未知ID:{}] [classtype {}][激活宠物:{}] [非法操作]", new Object[] { gi.name, player.getUsername(), player.getName(), mt.getId(), mt.getType(), player.getActivePetId() });
		}
	}

	private void doPlayerMoveReq(Player player, PLAYER_MOVE_REQ req) {
		if (player.getState() == Player.STATE_DEAD) {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能移动] ", new Object[] { gi.name, player.getUsername(), player.getName() });
			return;
		}

		if (player != null && livingSet.contains(player)) {

			player.getActiveSkillAgent().breakExecutingByPlayerMove(this);

			if (logger.isDebugEnabled()) {
				if (logger.isDebugEnabled()) logger.debug("[{}] [{}] [{}] [trace_PLAYER_MOVE_REQ] [dt:{}] [ex:{}] [ey:{}] [speed:{}] [st:{}] [sx:{}] [sy:{}] [{}] [{}]", new Object[] { gi.name, player.getUsername(), player.getName(), req.getDestineTimestamp(), req.getEndX(), req.getEndY(), req.getSpeed(), req.getStartTimestamp(), req.getStartX(), req.getStartY(), req.getType(), StringUtil.arrayToString(req.getSignPosts(), ",") });
			}

			// 验证移动指令有效性
			int isIllegal = 0;
			short[] signPostIds = req.getSignPosts();

			if (signPostIds.length == 0 && req.getStartX() == req.getEndX() && req.getStartY() == req.getEndY()) {
				isIllegal = 1;
			}
			// 根据服务器上玩家的当前位置，验证客户端发来的起点是否合理
			if (isIllegal == 0) {
				float dx = req.getStartX() - player.getX();
				float dy = req.getStartY() - player.getY();
				if (dx > 300 || dx < -300 || dy > 300 || dy < -300) {
					isIllegal = 2;
				}
			}
			// 验证下标是否越界
			if (isIllegal == 0) {
				for (int i = 0; i < signPostIds.length && isIllegal == 0; i++) {
					if (signPostIds[i] < 0 || signPostIds[i] >= gi.navigator.getSignPostNum()) {
						isIllegal = 3;
						break;
					}
				}
			}

			if (isIllegal == 0) {

				// 路径合法性验证通过，生成路径
				Point startPoint = new Point(req.getStartX(), req.getStartY());
				Point endPoint = new Point(req.getEndX(), req.getEndY());

				// 保存路径上所有的点，包括导航点和起止点
				Point[] points = new Point[signPostIds.length + 2];
				points[0] = startPoint;// 起点
				points[points.length - 1] = endPoint;// 终点
				for (int i = 0; i < signPostIds.length; i++) {
					SignPost signPost = gi.navigator.getSignPost(signPostIds[i]);
					points[i + 1] = new Point(signPost.x, signPost.y);
				}

				// 保存路径上所有点之间的距离
				short[] roadLength = new short[signPostIds.length + 1];
				if (signPostIds.length == 0) {
					roadLength[0] = (short) Graphics2DUtil.distance(startPoint, endPoint);
				} else {
					SignPost firstPost = gi.navigator.getSignPost(signPostIds[0]);
					SignPost lastPost = gi.navigator.getSignPost(signPostIds[signPostIds.length - 1]);
					roadLength[0] = (short) Graphics2DUtil.distance(startPoint, new Point(firstPost.x, firstPost.y));
					roadLength[roadLength.length - 1] = (short) Graphics2DUtil.distance(endPoint, new Point(lastPost.x, lastPost.y));
					// 保存导航点之间的距离
					for (int i = 0; i < signPostIds.length - 1 && isIllegal == 0; i++) {
						Road road = gi.navigator.getRoadBySignPost(signPostIds[i], signPostIds[i + 1]);
						if (road == null) {
							isIllegal = 4;
						} else {
							roadLength[i + 1] = road.len;
						}
					}
				}
				if (isIllegal == 0) {

					MoveTrace path = new MoveTrace(roadLength, points, req.getDestineTimestamp(), req.getStartTimestamp(), req.getSpeed());

					player.notifyClientMoveTrace(path);

					if (GameNetworkFramework.statFlowUserSet.contains(player.getUsername())) {
						logger.warn("[寻路路经] [username:" + player.getUsername() + "] [Player:" + player.getName() + "] [玩家位置(" + player.getX() + "," + player.getY() + ")] [路径(" + path.toString() + ")] [时间：" + (path.getDestineTimestamp() - System.currentTimeMillis()) + "ms] [客户端信息：" + (path.getDestineTimestamp() - path.getStartTimestamp()) + "ms,speed:" + req.getSpeed() + "]");
					} else if (logger.isDebugEnabled()) {
						if (logger.isDebugEnabled()) {
							logger.debug("[寻路路经] [username:" + player.getUsername() + "] [Player:" + player.getName() + "] [玩家位置(" + player.getX() + "," + player.getY() + ")] [路径(" + path.toString() + ")] [时间：" + (path.getDestineTimestamp() - System.currentTimeMillis()) + "ms] [客户端信息：" + (path.getDestineTimestamp() - path.getStartTimestamp()) + "ms,speed:" + req.getSpeed() + "]");
						}
					}

					if (path.getTotalLength() != 0) {
						path.setStartTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						if (player.getSpeed() <= 0) {
							path.setDestineTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + path.getTotalLength() * 1000L / 1);
						} else {
							path.setDestineTimestamp(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + path.getTotalLength() * 1000L / player.getSpeed());
						}
					}
					player.setMoveTrace(path);// new MoveTrace(roadLength,

					// 走路修改仙缘状态
					if (player.isSitdownState()) {
						player.setSitdownState(false);
					}

					if (logger.isDebugEnabled()) {
						if (logger.isDebugEnabled()) logger.debug("[寻路路经] [errorcode:{}] [{}] [Player:{}] [玩家位置({},{})] [路径({})] [时间：{}ms] ", new Object[] { isIllegal, player.getUsername(), player.getName(), player.x, player.y, player.getMoveTrace(), (req.getDestineTimestamp() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) });
					}

				}
			}
			if (isIllegal > 0) {
				// TODO 如果路径不合法怎么办？
				SET_POSITION_REQ req2 = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte) REASON_WRONG_PATH, player.getClassType(), player.getId(), (short) player.x, (short) player.y);
				player.addMessageToRightBag(req2, Translate.路径不合法);

				if (logger.isWarnEnabled()) logger.warn("[非法路经] [errorcode:{}] [{}] [Player:{}] [玩家位置({},{})] [路径起止({},{})->({},{})] ", new Object[] { isIllegal, player.getUsername(), player.getName(), player.x, player.y, req.getStartX(), req.getStartY(), req.getEndX(), req.getEndY() });
			}
		}
	}

	/**
	 * TODO 任务相关，暂保留现状
	 */
	// private void doSpriteInteractReq(Player player, SPRITE_INTERACT_REQ m) {
	// TaskSprite taskSprite = gi.getTaskSprite(m.getSprite());
	// if (player != null && livingSet.contains(player) && taskSprite != null) {
	// TaskDefine[] taskDefines = taskSprite.interact(player);
	// // TODO 组装成回复指令
	// } else if (logger.isDebugEnabled()) {
	// logger.debug("SPRITE_INTERACT_REQ error: player[" + player.getName() +
	// "] sprite[" + m.getSprite() + "]");
	// }
	// }
	/**
	 * 处理接收到的攻击消息（选定目标），处理逻辑：<br>
	 * 1，验证指令是否合法<br>
	 * 2，向其他客户端转发指令<br>
	 * 3，根据指令中的类型、编号、位置等信息获取目标的对象<br>
	 * 4，施放技能
	 */
	private void doTargetSkillReq(Player player, TARGET_SKILL_REQ skillReq) {
		// index目前事实上是技能编号，不是玩家技能表中的索引
		if (player.getState() == Player.STATE_DEAD) {
			if (Skill.logger.isWarnEnabled()) Skill.logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行只能命令] ", new Object[] { gi.name, player.getUsername(), player.getName() });
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(player)) {
//			if (Skill.logger.isDebugEnabled()) {
//				Skill.logger.warn("[{}] [{}] [{}] [非法操作] [金猴天灾场景内不允许玩家使用技能] ", new Object[] { gi.name, player.getUsername(), player.getName() });
//			}
//			return ;
//		}

		int skillId = skillReq.getSkillId();
		long[] targetIds = skillReq.getTargetId();
		byte[] targetTypes = skillReq.getTargetType();
		int tX = skillReq.getTargetX();
		int tY = skillReq.getTargetY();
		byte direction = skillReq.getDirection();
		Skill skill = player.getSkillById(skillId);
		int level = player.getSkillLevel(skillId);
		byte targetType = -1;
		long targetId = -1;
		if (skillReq.getTargetType() != null && skillReq.getTargetType().length > 0 && skillReq.getTargetId() != null && skillReq.getTargetId().length > 0) {
			targetType = skillReq.getTargetType()[0];
			targetId = skillReq.getTargetId()[0];
		}
		if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[玩家使用技能] [{}] [skillId:{}] [{}] [{}] [{}] [{}] [Skill:{}]", new Object[] { player.getLogString(), skillId, targetId, targetType, tX, tY, (skill == null ? "NULL" : skill.getName()) });

		if (skill != null && (skill instanceof ActiveSkill)) {
			if (level > 0) {
				// 国王技能共用cooldown
				if (player.kingSkillCoolDown) {
					if (skill.getName().equals(CountryManager.国王专用技能1) || skill.getName().equals(CountryManager.国王专用技能2)) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您现在不能用这个技能);
						player.addMessageToRightBag(hreq);
						return;
					}
				}

				try {
					if (skill.getName().equals(CountryManager.国王专用技能1) || skill.getName().equals(CountryManager.国王专用技能2) || skill.getName().equals(Translate.战血) || skill.getName().equals(Translate.万鬼窟) || skill.getName().equals(Translate.冰川时代) || skill.getName().equals(Translate.九黎之怒)) {
//						if (JJCManager.isJJCBattle(player.getCurrentGame().gi.name)) {
//							player.sendError(Translate.您现在不能用这个技能);
//							return;
//						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				LivingObject target = null;

				switch (targetType) {
				case 0:
					try {
						target = gm.playerManager.getPlayer(targetId);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 1:
					target = gm.getMonsterManager().getMonster(targetId);
					if (target == null) {
						target = gm.getNpcManager().getNPC(targetId);
					}
					if (target == null) {
						target = PetManager.getInstance().getPetInMemory(targetId);
					}
					break;
				}
				if (target != null && target instanceof Fighter) {

					player.useTargetSkill((ActiveSkill) skill, (Fighter) target, skillReq.getTargetType(), skillReq.getTargetId(), direction);

					if (((ActiveSkill) skill).check(player, (Fighter) target, level) == Skill.OK) {
						TARGET_SKILL_BROADCAST_REQ broadcastReq = new TARGET_SKILL_BROADCAST_REQ(GameMessageFactory.nextSequnceNum(), player.getClassType(), player.getId(), targetType, targetId, skillId, (byte) level);
						LivingObject[] los = getVisbleLivings(player, false);
						for (int i = 0; i < los.length; i++) {
							if (los[i] instanceof Player) {
								Player p = (Player) los[i];
								if (p.hiddenAllPlayer) {
									// noop
								} else if (p.hiddenSameCountryPlayer) {
									if (p.getCountry() != player.getCountry()) {
										p.addMessageToRightBag(broadcastReq);
									}
								} else {
									p.addMessageToRightBag(broadcastReq);
								}
							}
						}

					}
				} else {

					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.目标不存在或不能攻击);
					player.addMessageToRightBag(hreq);

					if (target != null) {
						if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [类型：{}] [技能:{}] [目标非Fighter]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillReq.getTargetType(), skill.getName() });
					} else {
						if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [无目标攻击] [错误] [{}] [{}] [目标:{}] [类型：{}] [技能:{}] [目标不存在]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillReq.getTargetType(), skill.getName() });
					}
				}
			} else {
				if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [还没有学习此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
			}
		} else {

			if (skill != null) {
				if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [此技能非主动技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
			} else {
				CareerManager cm = CareerManager.getInstance();
				skill = cm.getSkillById(skillId);
				if (skill != null) {
					if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [玩家没有此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
				} else {
					if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [系统中没有此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillId });
				}

			}

		}
	}

	private void doPetTargetSkillReq(Player player, PET_TARGET_SKILL_REQ skillReq) {
		// index目前事实上是技能编号，不是玩家技能表中的索引
		Logger log = Skill.logger;
		if (player.getState() == Player.STATE_DEAD) {
			if (log.isWarnEnabled()) log.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行只能命令] ", new Object[] { gi.name, player.getUsername(), player.getName() });
			return;
		}

		long petId = player.getActivePetId();
		if (petId <= 0) {
			if (log.isDebugEnabled()) {
				log.debug("[宠物使用技能] 玩家名称[{}] [宠物id:{}] [宠物为空]", player.getName(), petId);
			}
			return;
		}
		PetManager petManager = PetManager.getInstance();
		if (petManager == null) {
			if (log.isDebugEnabled()) {
				log.debug("[宠物使用技能] 玩家名称[{}] [宠物id:{}] [petManager == null]", player.getName(), petId);
			}
			return;
		}
		Pet pet = petManager.getPet(petId);
		if (pet == null) {
			if (log.isDebugEnabled()) {
				log.debug("[宠物使用技能] 玩家名称[{}] [宠物id:{}] [pet == null]", new Object[] { player.getName(), petId });
			}
			return;
		}
		int skillId = skillReq.getSkillId();
		byte direction = skillReq.getDirection();
		Skill skill = pet.getSkillById(skillId);
		int level = pet.getSkillLevelById(skillId);

		if (log.isInfoEnabled()) {
			log.info("[宠物使用技能] petName[{}] [skillId:{}] [Skill:{}]", new Object[] { pet.getName(), skillId, (skill == null ? "NULL" : skill.getName()) });
		}

		if (skill != null && (skill instanceof ActiveSkill)) {
			if (level > 0) {
				byte targetType = -1;
				long targetId = -1;
				if (skillReq.getTargetType() != null && skillReq.getTargetType().length > 0 && skillReq.getTargetId() != null && skillReq.getTargetId().length > 0) {
					targetType = skillReq.getTargetType()[0];
					targetId = skillReq.getTargetId()[0];
				}

				if (log.isDebugEnabled()) {
					log.debug("[宠物使用技能] [{}] [targetType:{}] [targetId:{}] [{}] [skillId:{}] [Skill:{}]", new Object[] { level, targetType, targetId, player.getLogString(), skillId, (skill == null ? "NULL" : skill.getName()) });
				}
				LivingObject target = null;
				switch (targetType) {
				case 0:
					try {
						target = gm.playerManager.getPlayer(targetId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 1:
					target = gm.getMonsterManager().getMonster(targetId);
					if (target == null) {
						target = petManager.getPetInMemory(targetId);
					}
					if (target == null) {
						target = gm.getNpcManager().getNPC(targetId);
					}
					break;
				}
				if (target != null && target instanceof Fighter) {

					pet.useTargetSkill((ActiveSkill) skill, (Fighter) target, skillReq.getTargetType(), skillReq.getTargetId(), direction);
					if (log.isDebugEnabled()) {
						log.debug("[宠物使用技能判断成功进入使用技能逻辑] [{}] [targetType:{}] [targetId:{}] [skillId:{}] [Skill:{}]", new Object[] { level, targetType, targetId, skillId, (skill == null ? "NULL" : skill.getName()) });
					}
				} else {

					if (target != null) {
						if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [类型：{}] [技能:{}] [目标非Fighter]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillReq.getTargetType(), skill.getName() });
					} else {
						if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [类型：{}] [技能:{}] [目标不存在]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillReq.getTargetType(), skill.getName() });
					}
				}
			} else {
				if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [还没有学习此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
			}
		} else {

			if (skill != null) {
				if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [此技能非主动技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
			} else {
				CareerManager cm = CareerManager.getInstance();
				skill = cm.getSkillById(skillId);
				if (skill != null) {
					if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [玩家宠物没有此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skill.getName() });
				} else {
					if (log.isInfoEnabled()) log.info("[处理宠物攻击指令] [有目标攻击] [错误] [{}] [{}] [目标:{}] [技能:{}] [系统中没有此技能]", new Object[] { player.getUsername(), player.getName(), skillReq.getTargetId(), skillId });
				}

			}

		}
	}

	/**
	 * 通知玩家，怪使用技能攻击
	 */
	public void doTargetSkillReqForSprite(Sprite sprite, Fighter target, ActiveSkill skill) {
		// index目前事实上是技能编号，不是玩家技能表中的索引
		TARGET_SKILL_BROADCAST_REQ broadcastReq = new TARGET_SKILL_BROADCAST_REQ(GameMessageFactory.nextSequnceNum(), sprite.getClassType(), sprite.getId(), target.getClassType(), target.getId(), skill.getId(), (byte) 1);
		if (sprite instanceof Pet) {
			Player player = ((Pet) sprite).getMaster();
			if (player != null) {
				boolean includeTarget = false;
				Fighter[] los = this.getVisbleFighter(sprite, false);
				for (int i = 0; i < los.length; i++) {
					if (los[i] instanceof Player) {
						Player p = (Player) los[i];

						if (p == player) {
							// noop
						} else if (p.hiddenAllPlayer) {
							// noop
						} else if (p.hiddenSameCountryPlayer) {
							if (p.getCountry() != player.getCountry()) {
								p.addMessageToRightBag(broadcastReq);
							}
						} else {
							p.addMessageToRightBag(broadcastReq);
						}
						if (p == target) {
							includeTarget = true;
						}
					}
				}
				if (includeTarget == false && target != player) {
					if (target instanceof Player) {
						((Player) target).addMessageToRightBag(broadcastReq);
					}
				}

			} else {
				// noop
			}
		} else {
			boolean includeTarget = false;
			Fighter[] los = this.getVisbleFighter(sprite, false);
			for (int i = 0; i < los.length; i++) {
				if (los[i] instanceof Player) {
					((Player) los[i]).addMessageToRightBag(broadcastReq);
					if (los[i] == target) {
						includeTarget = true;
					}
				}
			}
			if (includeTarget == false) {
				if (target instanceof Player) {
					((Player) target).addMessageToRightBag(broadcastReq);
				}
			}
		}

	}

	/**
	 * 处理接收到的攻击消息（未选定目标），处理逻辑：<br>
	 * 1，验证指令是否合法<br>
	 * 2，向其他客户端转发指令<br>
	 * 3，施放技能
	 */
	private void doNontargetSkillReq(Player player, NONTARGET_SKILL_REQ skillReq) {
		if (player.getState() == Player.STATE_DEAD) {
			if (Skill.logger.isWarnEnabled()) Skill.logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行只能命令]", new Object[] { gi.name, player.getUsername(), player.getName() });
			return;
		}
//		if (DisasterManager.getInst().isPlayerInGame(player)) {
//			if (Skill.logger.isDebugEnabled()) {
//				Skill.logger.warn("[{}] [{}] [{}] [非法操作] [金猴天灾场景内不允许玩家使用技能]", new Object[] { gi.name, player.getUsername(), player.getName() });
//			}
//			return ;
//		}
		

		// index目前事实上是技能编号，不是玩家技能表中的索引
		int skillId = skillReq.getSkillId();
		byte direction = skillReq.getDirection();
		Skill skill = player.getSkillById(skillId);
		int level = player.getSkillLevel(skillId);
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("请求使用技能 {} {}", player.getName(), skill == null ? "null " + skillId : skill.getName());
		}
		if (level > 0 && skill != null && (skill instanceof ActiveSkill)) {
			// 国王技能共用cooldown
			if (player.kingSkillCoolDown) {
				if (skill.getName().equals(CountryManager.国王专用技能1) || skill.getName().equals(CountryManager.国王专用技能2)) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您现在不能用这个技能);
					player.addMessageToRightBag(hreq);
					return;
				}
			}

			try {
				if (skill.getName().equals(CountryManager.国王专用技能1) || skill.getName().equals(CountryManager.国王专用技能2) || skill.getName().equals(Translate.战血) || skill.getName().equals(Translate.万鬼窟) || skill.getName().equals(Translate.冰川时代) || skill.getName().equals(Translate.九黎之怒)) {
//					if (JJCManager.isJJCBattle(player.getCurrentGame().gi.name)) {
//						player.sendError(Translate.您现在不能用这个技能);
//						return;
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if (skill.getName().equals(CountryManager.国王专用技能1) || skill.getName().equals(CountryManager.国王专用技能2)) {
					if (DiceManager.getInstance().isDiceGame(player)) {
						player.sendError(Translate.您现在不能用这个技能);
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			player.useNonTargetSkill((ActiveSkill) skill, skillReq.getTargetX(), skillReq.getTargetY(), skillReq.getTargetType(), skillReq.getTargetId(), direction);

			if (((ActiveSkill) skill).check(player, null, level) == Skill.OK) {
				NONTARGET_SKILL_BROADCAST_REQ broadcastReq = new NONTARGET_SKILL_BROADCAST_REQ(GameMessageFactory.nextSequnceNum(), player.getClassType(), player.getId(), skillReq.getTargetX(), skillReq.getTargetY(), skillId, (byte) level);

				LivingObject[] los = getVisbleLivings(player, false);
				for (int i = 0; i < los.length; i++) {
					if (los[i] instanceof Player) {
						Player p = (Player) los[i];
						if (p.hiddenAllPlayer) {
							//
						} else if (p.hiddenSameCountryPlayer) {
							if (p.getCountry() != player.getCountry()) {
								p.addMessageToRightBag(broadcastReq);
							}
						} else {
							p.addMessageToRightBag(broadcastReq);
						}
					}
				}

			}
		} else {
			if (skill == null) {
				// logger.warn("[非法技能] [" + player.getUsername() + "] [" + player.getName() + "] [索引：" + skillId + "]");
				if (Skill.logger.isWarnEnabled()) Skill.logger.warn("[非法技能] [{}] [{}] [索引：{}]", new Object[] { player.getUsername(), player.getName(), skillId });
			} else {
				// logger.warn("[非法技能] [" + player.getUsername() + "] [" + player.getName() + "] [索引：" + skillId + "]");
				if (Skill.logger.isWarnEnabled()) Skill.logger.warn("[非法技能] [{}] [{}] [索引：{}]", new Object[] { player.getUsername(), player.getName(), skillId });
			}
		}
	}

	private void doPetNontargetSkillReq(Player player, PET_NONTARGET_SKILL_REQ skillReq) {
		if (player.getState() == Player.STATE_DEAD) {
			if (Skill.logger.isWarnEnabled()) Skill.logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行只能命令]", new Object[] { gi.name, player.getUsername(), player.getName() });
			return;
		}

		long petId = player.getActivePetId();
		if (petId <= 0) {
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("[宠物使用技能] [宠物id:{}] [宠物为空]", new Object[] { petId });
			}
			return;
		}
		PetManager petManager = PetManager.getInstance();
		if (petManager == null) {
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("[宠物使用技能] [宠物id:{}] [petManager == null]", new Object[] { petId });
			}
			return;
		}
		Pet pet = petManager.getPet(petId);
		if (pet == null) {
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("[宠物使用技能][宠物id:{}] [pet == null]", new Object[] { petId });
			}
			return;
		}
		int skillId = skillReq.getSkillId();
		byte direction = skillReq.getDirection();
		Skill skill = pet.getSkillById(skillId);
		int level = pet.getSkillLevelById(skillId);

		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[宠物使用技能] [skillId:{}] [Skill:{}]", new Object[] { skillId, (skill == null ? "NULL" : skill.getName()) });
		}

		if (skill != null && (skill instanceof ActiveSkill)) {
			if (level > 0) {
				NONTARGET_SKILL_BROADCAST_REQ broadcastReq = new NONTARGET_SKILL_BROADCAST_REQ(GameMessageFactory.nextSequnceNum(), pet.getClassType(), pet.getId(), skillReq.getTargetX(), skillReq.getTargetY(), skillId, (byte) level);

				LivingObject[] los = getVisbleLivings(player, false);
				for (int i = 0; i < los.length; i++) {
					if (los[i] instanceof Player) {
						Player p = (Player) los[i];
						if (p.hiddenAllPlayer) {
							//
						} else if (p.hiddenSameCountryPlayer) {
							if (p.getCountry() != player.getCountry()) {
								p.addMessageToRightBag(broadcastReq);
							}
						} else {
							p.addMessageToRightBag(broadcastReq);
						}
					}
				}

				pet.useNonTargetSkill((ActiveSkill) skill, skillReq.getTargetX(), skillReq.getTargetY(), skillReq.getTargetType(), skillReq.getTargetId(), direction);
			} else {
				if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理宠物攻击指令] [无目标攻击] [错误] [{}] [{}] [技能:{}] [还没有学习此技能]", new Object[] { player.getUsername(), player.getName(), skill.getName() });
			}
		} else {
			if (skill == null) {
				if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理宠物攻击指令] [无目标攻击] [{}] [{}] [索引：{}]", new Object[] { player.getUsername(), player.getName(), skillId });
			} else {
				if (Skill.logger.isInfoEnabled()) Skill.logger.info("[处理宠物攻击指令] [无目标攻击] [{}] [{}] [索引：{}]", new Object[] { player.getUsername(), player.getName(), skillId });
			}
		}
	}

	private void doTouchTransportReq(Player player, TOUCH_TRANSPORT_REQ msg) {
		if (player.getState() == Player.STATE_DEAD) {
			if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能执行跳转]", new Object[] { gi.name, player.getUsername(), player.getName() });
			RequestMessage message = new CHANGE_GAME_REQ(GameMessageFactory.nextSequnceNum(), "", 0, new short[0]);
			sendMessage(player, message, "change game by transport fail");
			return;
		}
		int id = msg.getTransportId();
		TransportData tds[] = gi.getTransports();
		if (id >= 0 && id < tds.length && tds[id] != null) {
			TransportData transportData = tds[id];
			if (transportData.isTouched(player.getX(), player.getY())) {
				if (player.getFollowableNPC() != null) {
					if (player.getFollowableNPC().ownerIsAround()) {
						player.getFollowableNPC().setTransferWithOwner(true);
						if (TaskSubSystem.logger.isDebugEnabled()) {
							TaskSubSystem.logger.debug(player.getLogString() + "[设置跟随NPC:{}传送状态:true]", new Object[] { player.getFollowableNPC().getName() });
						}
					}
				}
				transferGame(player, transportData);
			} else {
				if (logger.isInfoEnabled()) logger.info("[处理跳转点] [失败] [{}] [{}] [{}] [地图跳转] [{}] [玩家的位置距离跳转点太远] [({},{})--({},{})",
						new Object[] { gi.getName(), player.getUsername(), player.getName(), transportData.getTargetMapName(), player.getX(), player.getY(), transportData.getX(), transportData.getY() });

				RequestMessage message = new CHANGE_GAME_REQ(GameMessageFactory.nextSequnceNum(), "", 0, new short[0]);
				sendMessage(player, message, "change game by transport fail");
			}
		} else {

			if (logger.isWarnEnabled()) logger.warn("[处理跳转点] [失败] [{}] [{}] [{}] [跳转点的ID越界] [id={}]", new Object[] { gi.getName(), player.getUsername(), player.getName(), id });
			RequestMessage message = new CHANGE_GAME_REQ(GameMessageFactory.nextSequnceNum(), "", 0, new short[0]);
			sendMessage(player, message, "change game by transport fail");
		}
	}

	/**
	 * 移动中的玩家中止了移动（按键操作的情况）
	 */
	private void doSetPositionReq(Player player, SET_POSITION_REQ msg) {
		if (msg.getObjectType() == player.getClassType()) {
			if (player.getState() == Player.STATE_DEAD) {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户已死亡不能设置坐标位置] ", new Object[] { gi.name, player.getUsername(), player.getName() });
				return;
			}

			// TODO 没有验证指令合法性
			boolean isValid = true;
			if (isCheck_SetPositionReq) {
				// 检查玩家发送SET_POSITION_REQ消息的频率
				// 只要持续一段时间频率过快,就封

			}
			if (isValid) {
				// TODO 应该以客户端的位置为准还是以服务器的位置为准？
				player.setX(msg.getX());// 目前以客户端位置为准，可以防止客户端闪跳
				player.setY(msg.getY());
				player.stopAndNotifyOthers(REASON_CLIENT_STOP, "stop moving");

				player.notifyClientSetPosition(msg);
				if (logger.isDebugEnabled()) {
					logger.debug("[{}] [{}] [x:{}] [y:{}][setPosition] [用户设置坐标位置 远离最近发生的路径]", new Object[] { gi.name, player.getLogString(), msg.getX(), msg.getY() });
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [非法操作] [用户设置坐标位置 远离最近发生的路径] ", new Object[] { gi.name, player.getUsername(), player.getName() });
			}
		} else if (msg.getObjectType() == Constants.SPRITE && msg.getObjectId() == player.getActivePetId()) {
			/**
			 * 玩家的宠物移动
			 */
			PetManager pm = PetManager.getInstance();
			Pet pet = pm.getPet(msg.getObjectId());
			if (pet == null) {
				// logger.warn("[" + gi.name + "] [" + player.getUsername() + "] [" + player.getName() + "] [宠物:" + msg.getObjectId()
				if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [{}] [宠物:{}][非法操作] [找不到宠物,不能setPostionReq] ", new Object[] { gi.name, player.getUsername(), player.getName(), msg.getObjectId() });
				return;
			}
			pet.setX(msg.getX());// 目前以客户端位置为准，可以防止客户端闪跳
			pet.setY(msg.getY());
			pet.stopAndNotifyOthers();
		} else if (msg.getObjectType() == Constants.SPRITE && msg.getObjectId() == player.getActiveMagicWeaponId()) {
			NPC mnpc = MemoryNPCManager.getNPCManager().getNPC(msg.getObjectId());
			if (mnpc != null) {
				if (MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[收到玩家请求2] [" + player.getLogString() + "] [" + mnpc + "] [原:" + mnpc.getX() + "] [原:" + mnpc.getY() + "]");
				}
				mnpc.setX(msg.getX());
				mnpc.setY(msg.getY());
				mnpc.stopAndNotifyOthers();
				if (MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[收到玩家请求2] [" + player.getLogString() + "] [" + mnpc + "] [" + mnpc.getX() + "] [" + mnpc.getY() + "]");
				}
			}
		}
	}

	/**
	 * 根据坐标从桶阵列里获取桶
	 * 
	 * @param matrix
	 * @param tx
	 * @param ty
	 * @return
	 */
	private static Bucket getBucket(BucketMatrix matrix, float tx, float ty) {
		int x = (int) (tx / matrix.w);
		int y = (int) (ty / matrix.h);
		if (x < 0) x = 0;
		if (x >= matrix.x) x = matrix.x - 1;
		if (y < 0) y = 0;
		if (y >= matrix.y) y = matrix.y - 1;
		return matrix.buckets[x][y];
	}

	/**
	 * 通过桶阵列来收集每个玩家窗口中的变化，以提高效率
	 * 
	 * @param los
	 */
	public void notifyPlayersAndClearNotifyFlagThroughBucket() {

		synchronized (newEnterObjectList) {
			if (newEnterObjectList.size() > 0) {
				LivingObject[] os = newEnterObjectList.toArray(new LivingObject[0]);
				newEnterObjectList.clear();
				for (int i = 0; i < os.length; i++) {
					livingSet.add(os[i]);
					//
					if (os[i] instanceof Player) {
						this.playerEnterGame((Player) os[i],"heartBeatNotice");
					}
				}

			}
		}
		clearDeathPlayerAndOtherLivingObject();

		LivingObject los[] = livingSet.toArray(new LivingObject[0]);

		BucketMatrix tempBM = lastBM;
		lastBM = currentBM;
		currentBM = tempBM;
		tempBM.clear();

		// 收集周围生物属性发生的变化
		boolean collectingArroundFieldChange = true;

		// 收集玩家自身属性发生的变化
		boolean collectingSelfFieldChange = true;

		// 保留每个LivingObject的LeftPath，以提高效率
		HashMap<MoveTrace, MoveTrace4Client> pathMap = new HashMap<MoveTrace, MoveTrace4Client>();

		for (int i = 0, n = los.length; i < n; i++) {
			LivingObject living = los[i];

			// 为了提高效率
			living.lastLiveingSetContainsFlag = lastLivingSet.contains(living);

			// 计算生物的桶坐标，并把生物放到桶里
			Bucket bucket = getBucket(tempBM, living.x, living.y);
			bucket.add(living);

			// 计算并保存生物属性值的变化
			if (collectingArroundFieldChange) {
				living.filedChangeEventList.clear();
				boolean marks[] = living.getAroundMarks();
				for (int l = 0; l < marks.length; l++) {
					if (marks[l]) {
						FieldChangeEvent event = new FieldChangeEvent(living, (byte) l, living.getValue(l));
						marks[l] = false;
						living.filedChangeEventList.add(event);
					}
				}
			}

			if (los[i] instanceof Player) {
				Player p = (Player) los[i];

				if (collectingSelfFieldChange) {
					fieldList.clear();
					boolean marks[] = p.getSelfMarks();
					for (int l = 0; l < marks.length; l++) {
						if (marks[l]) {
							FieldChangeEvent event = new FieldChangeEvent(p, (byte) l, p.getSelfValue(l));
							marks[l] = false;
							fieldList.add(event);
						}
					}
					if (fieldList.size() > 0) {

						NOTIFY_SELFCHANGE_REQ req = new NOTIFY_SELFCHANGE_REQ(GameMessageFactory.nextSequnceNum(), fieldList.toArray(new FieldChangeEvent[0]));
						p.addMessageToRightBag(req);

					}

				}
			}

			MoveTrace path = living.getMoveTrace();
			if (path != null) {
				pathMap.put(path, path.getLeftPath());
			}
		}

		boolean hiddenFlag = false;
		// 对所有生物进行循环，检查此生物周围的其他生物
		for (int i = 0, n = los.length; i < n; i++) {

			if (los[i] instanceof Player) {
				enterPets.clear();
				enterPlayers.clear();
				enterSprites.clear();
				enterSummoneds.clear();

				exitPets.clear();
				exitPlayers.clear();
				exitSprites.clear();
				exitSummoneds.clear();

				moveTraces.clear();
				fieldList.clear();
				pickupAvailableSprites.clear();

				Player p = (Player) los[i];
				// 计算当前广播区范围
				float x1 = p.getX() - p.getViewWidth() / 2;
				float x2 = p.getX() + p.getViewWidth() / 2;
				float y1 = p.getY() - p.getViewHeight() / 2;
				float y2 = p.getY() + p.getViewHeight() / 2;

				// 计算前一次心跳广播区范围
				float lastx1 = p.lastHeartBeatX - p.getViewWidth() / 2;
				float lastx2 = p.lastHeartBeatX + p.getViewWidth() / 2;
				float lasty1 = p.lastHeartBeatY - p.getViewHeight() / 2;
				float lasty2 = p.lastHeartBeatY + p.getViewHeight() / 2;

				// if (gm.enableHeartBeatStat) startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

				int minx = (int) x1 / tempBM.w;
				if (minx < 0) minx = 0;
				int maxx = (int) x2 / tempBM.w;
				if (maxx >= tempBM.x) maxx = tempBM.x - 1;
				int miny = (int) y1 / tempBM.h;
				if (miny < 0) miny = 0;
				int maxy = (int) y2 / tempBM.h;
				if (maxy >= tempBM.y) maxy = tempBM.y - 1;

				// 收集新进入的人 和 一直在的人身上的变化
				for (int x = minx; x <= maxx; x++) {
					for (int y = miny; y <= maxy; y++) {
						int size = tempBM.buckets[x][y].size();
						if (size > 0) {
							LivingObject losInB[] = tempBM.buckets[x][y].getLivingObjects();
							for (int k = 0; k < size; k++) {

								LivingObject o = losInB[k];

								if (o instanceof EffectSummoned) continue;

								hiddenFlag = false;
								if (p.hiddenAllPlayer || p.hiddenSameCountryPlayer) {
									if (o instanceof Player) {
										if (p.hiddenAllPlayer && p != o) {
											hiddenFlag = true;
										} else if (p.hiddenSameCountryPlayer) {
											if (((Player) o).getCountry() == p.getCountry() && p != o) {
												hiddenFlag = true;
											}
										}
									} else if (o instanceof Pet) {
										Pet pet = (Pet) o;
										if (p.hiddenAllPlayer && p != pet.getMaster()) {
											hiddenFlag = true;
										} else if (p.hiddenSameCountryPlayer) {
											if (pet.getMaster() != null && pet.getMaster().getCountry() == p.getCountry() && p != pet.getMaster()) {
												hiddenFlag = true;
											}
										}
									}
								}

								if (o.x >= x1 && o.x <= x2 && o.y >= y1 && o.y <= y2) {// 当前心跳在当前窗口中

									if (!p.newlyEnterGameFlag && p.lastLiveingSetContainsFlag && o.lastLiveingSetContainsFlag && o.lastHeartBeatX >= lastx1 && o.lastHeartBeatX <= lastx2 && o.lastHeartBeatY >= lasty1 && o.lastHeartBeatY <= lasty2) {
										// 上一次心跳在上一次窗口中

										// 原来就在的人
										// 玩家屏蔽的所有的周围玩家

										if (hiddenFlag) {
											// 屏蔽所有的内容
										} else {// 玩家没有屏蔽的所有的周围玩家

											fieldList.addAll(o.filedChangeEventList);

											// 收集buff的变化
											if (o instanceof Player) {
												collectingBuffChange(p, (Player) o, false);
											} else if (o instanceof Sprite) {
												collectingBuffChange(p, (Sprite) o, false);
											}

											// 收集路径

											MoveTrace path = o.getMoveTrace();
											if (path != null && o != p) {
												if (o instanceof Pet) {
													Pet pet = (Pet) o;
													if (pet.getId() != p.getActivePetId()) {
														if (!path.hasNotify(p)) {
															path.setNotifyMark(p);
															if (pathMap.get(path) == null) {
																pathMap.put(path, path.getLeftPath());
															}
															moveTraces.add(pathMap.get(path));
														}
													}
												} else if (o instanceof MagicWeaponNpc) {
													if (o.getId() != p.getActiveMagicWeaponId()) {
														if (!path.hasNotify(p)) {
															path.setNotifyMark(p);
															if (pathMap.get(path) == null) {
																pathMap.put(path, path.getLeftPath());
															}
															moveTraces.add(pathMap.get(path));
														}
													}
												} else if (!path.hasNotify(p)) {
													path.setNotifyMark(p);
													if (pathMap.get(path) == null) {
														pathMap.put(path, path.getLeftPath());
													}
													moveTraces.add(pathMap.get(path));
												}
											}
										}

									} else { // 新进入的人
										// 玩家屏蔽的所有的周围玩家
										if (hiddenFlag) {
											// 新进入的人
										} else {
											// 新进入的人
											if (o instanceof Player) {
												if (o == p) {
													// 通知玩家自己，属性的变化，buff的变化
													fieldList.addAll(o.filedChangeEventList);
													collectingBuffChange(p, (Player) o, false);
												} else {
													enterPlayers.add((Player) o);
												}
											} else if (o instanceof Pet) {
												Pet s = (Pet) o;
												enterPets.add(s);
											} else if (o instanceof Sprite) {
												Sprite s = (Sprite) o;
												if(s.getAvata() == null || s.getAvata().length == 0){
													logger.warn("[AROUND_CHANGE_REQ] [id:"+s.getId()+"] [name:"+s.getName()+"] [gamename:"+s.getGameCNName()+"]");
												}
												if (s instanceof Monster) {
													FlopSchemeEntity fse = ((Monster) s).getFlopSchemeEntity();
													if (fse != null && fse.isPickupAvailable(p)) {
														pickupAvailableSprites.add(s);
													}
												}
												enterSprites.add(s);
											} else if (o instanceof EntitySummoned) {
												enterSummoneds.add((EntitySummoned) o);
											}

											MoveTrace path = o.getMoveTrace();
											if (path != null && o != p) {
												if (o instanceof Pet) {
													Pet pet = (Pet) o;
													if (pet.getId() != p.getActivePetId()) {
														if (!path.hasNotify(p)) {
															path.setNotifyMark(p);
															if (pathMap.get(path) == null) {
																pathMap.put(path, path.getLeftPath());
															}
															moveTraces.add(pathMap.get(path));
														}
													}
												} else {
													path.setNotifyMark(p);
													if (pathMap.get(path) == null) {
														pathMap.put(path, path.getLeftPath());
													}
													moveTraces.add(pathMap.get(path));
												}
											}
										}
									}
								}
							}// for o
						} // size > 0
					}// for
				}// for

				minx = (int) lastx1 / tempBM.w;
				if (minx < 0) minx = 0;
				maxx = (int) lastx2 / tempBM.w;
				if (maxx >= tempBM.x) maxx = tempBM.x - 1;
				miny = (int) lasty1 / tempBM.h;
				if (miny < 0) miny = 0;
				maxy = (int) lasty2 / tempBM.h;
				if (maxy >= tempBM.y) maxy = tempBM.y - 1;

				// 收集离开的人
				for (int x = minx; x <= maxx; x++) {
					for (int y = miny; y <= maxy; y++) {
						int size = lastBM.buckets[x][y].size();
						if (size > 0) {
							LivingObject losInB[] = lastBM.buckets[x][y].getLivingObjects();
							for (int k = 0; k < size; k++) {
								LivingObject o = losInB[k];

								hiddenFlag = false;
								if (p.hiddenAllPlayer || p.hiddenSameCountryPlayer) {
									if (o instanceof Player) {
										if (p.hiddenAllPlayer && p != o) {
											hiddenFlag = true;
										} else if (p.hiddenSameCountryPlayer) {
											if (((Player) o).getCountry() == p.getCountry() && p != o) {
												hiddenFlag = true;
											}
										}
									} else if (o instanceof Pet) {
										Pet pet = (Pet) o;
										if (p.hiddenAllPlayer && p != pet.getMaster()) {
											hiddenFlag = true;
										} else if (p.hiddenSameCountryPlayer) {
											if (pet.getMaster() != null && pet.getMaster().getCountry() == p.getCountry() && p != pet.getMaster()) {
												hiddenFlag = true;
											}
										}
									}
								}

								if (!p.newlyEnterGameFlag && p.lastLiveingSetContainsFlag && o.lastHeartBeatX >= lastx1 && o.lastHeartBeatX <= lastx2 && o.lastHeartBeatY >= lasty1 && o.lastHeartBeatY <= lasty2) { // 上一次心跳在上一次窗口中
									if (livingSet.contains(o) && o.x >= x1 && o.x <= x2 && o.y >= y1 && o.y <= y2) {// 当前心跳在当前窗口中
										// 原来就在的人
									} else if (hiddenFlag) {
										//
									} else {
										// 离开的人

										if (o instanceof Player) {

											// 玩家屏蔽的所有的周围玩家
											if (p.getAroundNotifyPlayerNum() == 0) {

												Player pp = (Player) o;
												if (p.isSameTeam(pp) || p.getFightingType(pp) == Fighter.FIGHTING_TYPE_ENEMY) {
													exitPlayers.add((Player) o);
												}

											} else {
												exitPlayers.add((Player) o);
											}

											// 通知战斗
											if (p.isFighting()) {
												p.notifyEndToFighting((Player) o);
												p.notifyEndToBeFighted((Player) o);
											}

											if (((Player) o).isFighting()) {
												((Player) o).notifyEndToBeFighted(p);
											}

										} else if (o instanceof Pet) {
											Pet pet = (Pet) o;
											// exitPets.add(pet);
											if (pet.getId() != p.getActivePetId()) {
												exitPets.add(pet);
											}

											// 通知战斗
											if (p.isFighting()) {
												p.notifyEndToFighting((Pet) o);
												p.notifyEndToBeFighted((Pet) o);
											}

											// ((Sprite)o).notifyEndToBeFighted(p);

										} else if (o instanceof Sprite) {
											exitSprites.add((Sprite) o);

											// 通知战斗
											if (p.isFighting()) {
												p.notifyEndToFighting((Sprite) o);
												p.notifyEndToBeFighted((Sprite) o);
											}

											// ((Sprite)o).notifyEndToBeFighted(p);

										} else if (o instanceof EntitySummoned) {
											exitSummoneds.add((EntitySummoned) o);
										}
									}
								} else {

								}
							}// if
						} // for(k = 0
					}// for
				}// for

				if (!(fieldList.isEmpty() && enterPlayers.isEmpty() && enterPets.isEmpty() && enterSprites.isEmpty() && enterSummoneds.isEmpty() && exitPlayers.isEmpty() && exitPets.isEmpty() && exitSprites.isEmpty() && exitSummoneds.isEmpty() && moveTraces.isEmpty())) {
					// TODO:去掉原来为了客户端bug所修改的代码
					// if (p.getTeam() != null) {
					// Iterator<Player> epIt = exitPlayers.iterator();
					// while (epIt.hasNext()) {
					// Player ep = epIt.next();
					// for (int ll=0 ; ll < p.getTeam().getMembers().size(); ll++) {
					// if (p.getTeam().getMembers().get(ll).getId() == ep.getId()) {
					// epIt.remove();
					// }
					// }
					// }
					// }
					ArrayList<Sprite> al = new ArrayList<Sprite>();

					Iterator<Sprite> it = enterSprites.iterator();
					while (it.hasNext()) {
						Sprite s = it.next();
						if (s.getSpriteType() == Sprite.SPRITE_TYPE_MONSTER) {
							Monster m = (Monster) s;
							if (this.templateMonsterAndNpcIndexInGame.containsKey("m_" + m.getSpriteCategoryId())) {
								it.remove();
								al.add(s);
							}
						} else if (s.getSpriteType() == Sprite.SPRITE_TYPE_NPC) {
							NPC m = (NPC) s;
							if (this.templateMonsterAndNpcIndexInGame.containsKey("n_" + m.getNPCCategoryId())) {
								it.remove();
								al.add(s);
							}
						}
					}

					int indexOfs[] = new int[al.size()];
					for (int j = 0; j < al.size(); j++) {
						Sprite s = al.get(j);
						if (s.getSpriteType() == Sprite.SPRITE_TYPE_MONSTER) {
							Monster m = (Monster) s;
							indexOfs[j] = templateMonsterAndNpcIndexInGame.get("m_" + m.getSpriteCategoryId());

						} else if (s.getSpriteType() == Sprite.SPRITE_TYPE_NPC) {
							NPC m = (NPC) s;
							indexOfs[j] = templateMonsterAndNpcIndexInGame.get("n_" + m.getNPCCategoryId());
						}
					}

					try {
						
						AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), enterPlayers.toArray(new Player[enterPlayers.size()]), enterPets.toArray(new Pet[enterPets.size()]), enterSprites.toArray(new Sprite[enterSprites.size()]), exitPlayers.toArray(new Player[exitPlayers.size()]), exitPets.toArray(new Pet[exitPets.size()]), exitSprites.toArray(new Sprite[exitSprites.size()]), moveTraces.toArray(new MoveTrace4Client[moveTraces.size()]), fieldList.toArray(new FieldChangeEvent[fieldList.size()]), indexOfs, al.toArray(new Sprite[al.size()]),new String[0],new long[0]);
						/*if (DisasterManager.logger.isDebugEnabled()) {
						StringBuffer sb = new StringBuffer();
						for (Player tempP : req.getLeavePlayers()) {
							sb.append(tempP.getLogString());
						}
						DisasterManager.logger.debug("[通知玩家aroundchange] [" + p.getLogString() + "] [离开的玩家:" + sb.toString() + "]" );
						}*/
						
						gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
					} catch (Exception e) {
						e.printStackTrace();
						logger.warn("[AROUND_CHANGE_REQ] [异常] ["+e+"]",e);
					}

				}

				// 将所有Notify_EVENT_REQ发送给客户端
				p.sendAllHoldNotifyEvents();

				// 通知客户端，那些怪身上可以捡东西
				if (!pickupAvailableSprites.isEmpty()) {
					long spriteIds[] = new long[pickupAvailableSprites.size()];
					boolean flag[] = new boolean[pickupAvailableSprites.size()];
					for (int k = 0; k < spriteIds.length; k++) {
						spriteIds[k] = pickupAvailableSprites.get(k).getId();
						flag[k] = true;
					}
					NOTIFY_FLOPAVAILABLE_REQ req = new NOTIFY_FLOPAVAILABLE_REQ(GameMessageFactory.nextSequnceNum(), spriteIds, flag);
					p.addMessageToRightBag(req);
				}

				if (!enterPlayers.isEmpty()) {
					for (Player player : enterPlayers) {
						// 收集buff的变化
						collectingBuffChange(p, player, true);
					}
				}

				if (!enterPets.isEmpty()) {
					for (Pet sripte : enterPets) {
						// 收集buff的变化
						collectingBuffChange(p, sripte, true);
					}
				}

				if (!enterSprites.isEmpty()) {
					for (Sprite sripte : enterSprites) {
						// 收集buff的变化
						collectingBuffChange(p, sripte, true);
					}
				}

				// 清除新进入游戏的标记
				p.setNewlyEnterGameFlag(false);

			} // end los is player
		}// end for each los

		// 处理观察者
		// 把living object的变量改变标记恢复为“未改变”

		if (collectingArroundFieldChange) {
			for (int i = 0, n = los.length; i < n; i++) {
				LivingObject o = los[i];

				// 清空buff修改的标记
				if (o instanceof Player) {
					((Player) o).getRemovedBuffs().clear();
					((Player) o).getNewlyBuffs().clear();
				} else if (o instanceof Sprite) {
					((Sprite) o).getRemovedBuffs().clear();
					((Sprite) o).getNewlyBuffs().clear();
				}
			}
		}

		for (int i = 0; i < los.length; i++) {
			los[i].lastHeartBeatX = los[i].x;
			los[i].lastHeartBeatY = los[i].y;
		}

	}

	/**
	 * 通知玩家，周围某个玩家或者怪身上的buff或者buff的变化 如果是刚进入广播区域的，那么就通知身上所有的buff
	 * 如果是已经存在的，那么就通知发生变化的buff
	 * 
	 * @param player
	 * @param f
	 * @param enter
	 */
	private void collectingBuffChange(Player player, Fighter f, boolean enter) {
		try {
			if (f instanceof Player) {
				Player p = (Player) f;
				if (enter) {
					if (player == f) return;
					Buff buffs[] = p.getActiveBuffs();
					for (int i = 0; i < buffs.length; i++) {
						if (buffs[i] != null) {
							NOTIFY_BUFF_REQ req = new NOTIFY_BUFF_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), buffs[i]);
							player.addMessageToRightBag(req);
						}
					}
				} else {
					ArrayList<Buff> al = p.getNewlyBuffs();
					for (Buff b : al) {
						NOTIFY_BUFF_REQ req = new NOTIFY_BUFF_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), b);
						player.addMessageToRightBag(req);
					}
					al = p.getRemovedBuffs();
					for (Buff b : al) {
						NOTIFY_BUFF_REMOVED_REQ req = new NOTIFY_BUFF_REMOVED_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), b.getSeqId());
						player.addMessageToRightBag(req);
					}
				}
			} else if (f instanceof Sprite) {
				Sprite p = (Sprite) f;
				if (enter) {
					ArrayList<Buff> al = p.getBuffs();
					for (Buff b : al) {

						NOTIFY_BUFF_REQ req = new NOTIFY_BUFF_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, p.getId(), b);
						player.addMessageToRightBag(req);

					}
				} else {
					ArrayList<Buff> al = p.getNewlyBuffs();
					for (Buff b : al) {
						NOTIFY_BUFF_REQ req = new NOTIFY_BUFF_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, p.getId(), b);
						player.addMessageToRightBag(req);
					}
					al = p.getRemovedBuffs();
					for (Buff b : al) {
						NOTIFY_BUFF_REMOVED_REQ req = new NOTIFY_BUFF_REMOVED_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, p.getId(), b.getSeqId());
						player.addMessageToRightBag(req);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("[collectingBuffChange] [异常]", ex);
		}
	}

	public boolean contains(LivingObject lo) {
		return livingSet.contains(lo);
	}
	/**
	 *回
	 * @param id
	 * @param type   1代表怪物
	 * @return
	 */
	public boolean containsAndAlive(long id, int type) {
		synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (type == 1 && l instanceof Monster) {
					if (((Monster)l).getId() == id && !((Monster)l).isDeath()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Fighter[] getVisbleFighter(LivingObject living, boolean includeItself) {
		Collection<Fighter> visbles = new ArrayList<Fighter>();
		float x1 = living.getX() - living.getViewWidth() / 2;
		float x2 = living.getX() + living.getViewWidth() / 2;
		float y1 = living.getY() - living.getViewHeight() / 2;
		float y2 = living.getY() + living.getViewHeight() / 2;

		int minx = (int) x1 / currentBM.w;
		int maxx = (int) x2 / currentBM.w;
		int miny = (int) y1 / currentBM.h;
		int maxy = (int) y2 / currentBM.h;

		if (minx < 0) minx = 0;
		if (maxx > currentBM.x - 1) maxx = currentBM.x - 1;
		if (miny < 0) miny = 0;
		if (maxy > currentBM.y - 1) maxy = currentBM.y - 1;

		for (int x = minx; x <= maxx; x++) {
			for (int y = miny; y <= maxy; y++) {
				int size = currentBM.buckets[x][y].size();
				if (size > 0) {
					LivingObject los[] = currentBM.buckets[x][y].getLivingObjects();
					for (int i = 0; i < size; i++) {
						if (((los[i] instanceof Fighter) || (los[i] instanceof Player)) && (includeItself || living != los[i]) && los[i].x >= x1 && los[i].x <= x2 && los[i].y >= y1 && los[i].y <= y2) {
							visbles.add((Fighter) los[i]);
						}
					}
				}
			}
		}
		return visbles.toArray(new Fighter[visbles.size()]);
	}

	public LivingObject[] getVisbleLivings(LivingObject living, boolean includeItself) {
		Collection<LivingObject> visbles = new ArrayList<LivingObject>();
		float x1 = living.getX() - living.getViewWidth() / 2;
		float x2 = living.getX() + living.getViewWidth() / 2;
		float y1 = living.getY() - living.getViewHeight() / 2;
		float y2 = living.getY() + living.getViewHeight() / 2;

		int minx = (int) x1 / currentBM.w;
		int maxx = (int) x2 / currentBM.w;
		int miny = (int) y1 / currentBM.h;
		int maxy = (int) y2 / currentBM.h;

		if (minx < 0) minx = 0;
		if (maxx > currentBM.x - 1) maxx = currentBM.x - 1;
		if (miny < 0) miny = 0;
		if (maxy > currentBM.y - 1) maxy = currentBM.y - 1;

		for (int x = minx; x <= maxx; x++) {
			for (int y = miny; y <= maxy; y++) {
				int size = currentBM.buckets[x][y].size();
				if (size > 0) {
					LivingObject los[] = currentBM.buckets[x][y].getLivingObjects();
					for (int i = 0; i < size; i++) {
						if ((includeItself || living != los[i]) && los[i].x >= x1 && los[i].x <= x2 && los[i].y >= y1 && los[i].y <= y2) {
							visbles.add(los[i]);
						}
					}
				}
			}
		}
		return visbles.toArray(new LivingObject[visbles.size()]);
	}

	ArrayList<Sprite> pickupAvailableSprites = new ArrayList<Sprite>();

	ArrayList<FieldChangeEvent> fieldList = new ArrayList<FieldChangeEvent>(500);

	ArrayList<Player> enterPlayers = new ArrayList<Player>(500);
	ArrayList<Pet> enterPets = new ArrayList<Pet>(500);
	ArrayList<Sprite> enterSprites = new ArrayList<Sprite>(500);
	ArrayList<EntitySummoned> enterSummoneds = new ArrayList<EntitySummoned>(500);
	ArrayList<Player> exitPlayers = new ArrayList<Player>(500);
	ArrayList<Pet> exitPets = new ArrayList<Pet>(500);
	ArrayList<Sprite> exitSprites = new ArrayList<Sprite>(500);
	ArrayList<EntitySummoned> exitSummoneds = new ArrayList<EntitySummoned>(500);
	Collection<MoveTrace4Client> moveTraces = new ArrayList<MoveTrace4Client>(500);

	public DefaultQueue getQueue() {
		return messageQueue;
	}

	public void sendMessage(Player player, RequestMessage req, String description) {
		gm.framework.sendMessage(player.getConn(), req, description);
	}

	/**
	 * 
	 * @param player
	 * @param transportData
	 * @param backTown
	 */
	public void transferGame(Player player, TransportData transportData, boolean backTown) {
		RequestMessage message;
		// 对player的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName()) && buff.getInvalidTime() > 0) {
					if (!transportData.getTargetMapName().equals("jianyu")) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您现在被囚禁不能传送);
						player.addMessageToRightBag(hreq);
						try {
							if (player.getCurrentGame() != null) {
								if (!player.getCurrentGame().gi.getName().equals("jianyu")) {
									DENY_USER_REQ denyreq = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), Translate.客户端修改地图文件名字, GameConstants.getInstance().getServerName() + Translate.客户端修改地图文件名字, false, true, false, 0, 24);
									MieshiGatewayClientService.getInstance().sendMessageToGateway(denyreq);
									logger.warn("[地图不匹配] [{}] [{}]", new Object[] { player.getLogString(), player.getCurrentGame().gi.getName() });
									player.getConn().close();
								}
							}
						} catch (Exception e) {
							logger.error("transferGame", e);
						}
						return;
					}
				}
			}
		}
		if (player.isBoothState()) {
			BoothsaleManager.getInstance().msg_cancelBoothSale(player);
			// player.sendError("您正在摆摊，不能切换地图，请收摊!");
			// return;
		}

		if (player.getTimerTaskAgent() != null) {
			player.getTimerTaskAgent().notifyPassiveTransfer();
		}
		// if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())){
		// player.sendError(Translate);
		// return;
		// }

		DownCityManager dcm = DownCityManager.getInstance();

		// 如果是副本地图就不论是不是当前地图，都跳转
		boolean isDownCity = false;

		if (dcm != null && dcm.isDownCityByName(transportData.getTargetMapName())) {
			isDownCity = true;
		}

		if (gi.getName().equals(transportData.getTargetMapName()) && this.country == player.transferGameCountry && !isDownCity) {
			// 如果是在同一张地图就直接跳转
			Fighter fs[] = this.getVisbleFighter(player, false);
			player.setX(transportData.getTargetMapX());
			player.setY(transportData.getTargetMapY());
			player.removeMoveTrace();
			message = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), Game.REASON_TRANSPORT, player.getClassType(), player.getId(), (short) player.getX(), (short) player.getY());
			if (logger.isInfoEnabled()) {
				if (logger.isInfoEnabled()) logger.info("[处理跳转点] [成功] [{}][在livingSet:{}][{}] [{}] [{}] [同地图跳转] [({},{})]", new Object[] { gi.getName(), livingSet.contains(player), player.getGame(), player.getUsername(), player.getName(), player.getX(), player.getY() });

			}
			sendMessage(player, message, "change game by transport");
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					Player p = (Player) fs[i];
					if (p.getConn() != null) {
						sendMessage(p, message, "change game by transport");
					}
				}
			}
			player.设置进入区域(this);
		} else {
			playerLeaveGame(player);
			player.setTransportData(transportData);

			int version2 = 0;

			String gameName = transportData.getTargetMapName();
			if (gameName != null) {
				GameInfo gi = gm.getGameInfo(gameName);
				if (gi != null) {
					version2 = gi.getVersion();
				}
			}
			short spriteTypes[] = gm.getAllSpriteTypeOnGame(gameName);

			message = new CHANGE_GAME_REQ(GameMessageFactory.nextSequnceNum(), transportData.getTargetMapName(), version2, spriteTypes);

			if (logger.isInfoEnabled()) {
				// logger.info("[处理跳转点] [成功] [" + gi.getName() + "] [" + player.getUsername() + "] [" + player.getName() + "] [不同地图跳转] ["
				// + transportData.getTargetMapName() + "(" + player.getX() + "," + player.getY() + ")]");

				if (logger.isInfoEnabled()) logger.info("[处理跳转点] [成功] [{}] [不同地图跳转] [从:{}({},{})] [到:{}({},{})]", new Object[] { player.getLogString(), gi.getName(), player.getX(), player.getY(), transportData.getTargetMapName(), transportData.getTargetMapX(), transportData.getTargetMapY() });

			}
			sendMessage(player, message, "change game by transport");

			// 统计想要进行地图跳转
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_准备跳地图, Translate.text_3096 + gi.getName() + "->" + transportData.getTargetMapName() + ")", 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
		}
	}

	/**
	 * 发送一个消息给指定的玩家
	 * 
	 * @param player
	 *            玩家
	 * @param req
	 *            要发送给玩家的消息
	 * @param description
	 *            日志中的描述
	 */
	public void transferGame(Player player, TransportData transportData) {
		RequestMessage message;
		// 对player的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName()) && buff.getInvalidTime() > 0) {
					if (!transportData.getTargetMapName().equals("jianyu")) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您现在被囚禁不能传送);
						player.addMessageToRightBag(hreq);
						try {
							if (player.getCurrentGame() != null) {
								if (!player.getCurrentGame().gi.getName().equals("jianyu")) {
									DENY_USER_REQ denyreq = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), Translate.客户端修改地图文件名字, GameConstants.getInstance().getServerName() + Translate.客户端修改地图文件名字, false, true, false, 0, 24);
									MieshiGatewayClientService.getInstance().sendMessageToGateway(denyreq);
									logger.warn("[地图不匹配] [{}] [{}]", new Object[] { player.getLogString(), player.getCurrentGame().gi.getName() });
									player.getConn().close();
								}
							}
						} catch (Exception e) {
							logger.error("transferGame", e);
						}
						return;
					}
				}
			}
		}

		if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())) {
			player.sendError(Translate.渡劫中不可以使用拉令);
			return;
		}

		if (player.isBoothState()) {
			BoothsaleManager.getInstance().msg_cancelBoothSale(player);
			// player.sendError("您正在摆摊，不能切换地图，请收摊!");
			// return;
		}

		if (player.getTimerTaskAgent() != null) {
			player.getTimerTaskAgent().notifyPassiveTransfer();
		}

		DownCityManager dcm = DownCityManager.getInstance();

		// 如果是副本地图就不论是不是当前地图，都跳转
		boolean isDownCity = false;

		if (dcm != null && dcm.isDownCityByName(transportData.getTargetMapName())) {
			isDownCity = true;
		}
		
		logger.warn("[玩家回城复活2] [currGameCountry:{}] [transferGameCountry:{}] [currGameName:{}] [复活点:{}] [transportMap:{}] [xy:{}] [isDownCity{{}] [{}]",new Object[]{
				this.country,player.transferGameCountry,(this.gi!=null?this.gi.getName():"null"),player.getResurrectionMapName(),transportData.getTargetMapName(),player.getResurrectionX()+"/"+player.getResurrectionY(),isDownCity,player.getLogString()
			});

		if (gi.getName().equals(transportData.getTargetMapName()) && this.country == player.transferGameCountry && !isDownCity) {
			// 如果是在同一张地图就直接跳转
			Fighter fs[] = this.getVisbleFighter(player, false);
			player.setX(transportData.getTargetMapX());
			player.setY(transportData.getTargetMapY());
			player.removeMoveTrace();
			message = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), Game.REASON_TRANSPORT, player.getClassType(), player.getId(), (short) player.getX(), (short) player.getY());
			if (logger.isInfoEnabled()) {
				if (logger.isInfoEnabled()) logger.info("[处理跳转点] [成功] [{}][在livingSet:{}][{}] [{}] [{}] [同地图跳转] [({},{})]", new Object[] { gi.getName(), livingSet.contains(player), player.getGame(), player.getUsername(), player.getName(), player.getX(), player.getY() });

			}
			sendMessage(player, message, "change game by transport");
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					Player p = (Player) fs[i];
					if (p.getConn() != null) {
						sendMessage(p, message, "change game by transport");
					}
				}
			}
			player.设置进入区域(this);
		} else {
			playerLeaveGame(player);
			player.setTransportData(transportData);

			int version2 = 0;

			String gameName = transportData.getTargetMapName();
			if (gameName != null) {
				GameInfo gi = gm.getGameInfo(gameName);
				if (gi != null) {
					version2 = gi.getVersion();
				}
			}
			short spriteTypes[] = gm.getAllSpriteTypeOnGame(gameName);
			int cu = player.getCurrentGameCountry();
			if(gameName == null || gameName.isEmpty()){
				if(player.getCurrentGameCountry() == 0){
					cu = player.getCountry();
				}
				if(cu == 1){
					gameName = transportData.getTargetMapName1();
				}else if(cu == 2){
					gameName = transportData.getTargetMapName2();
					
				}else if(cu == 3){
					gameName = transportData.getTargetMapName3();
					
				}
			}
			
			
			message = new CHANGE_GAME_REQ(GameMessageFactory.nextSequnceNum(), gameName, version2, spriteTypes);

			if (logger.isInfoEnabled()) {
				// logger.info("[处理跳转点] [成功] [" + gi.getName() + "] [" + player.getUsername() + "] [" + player.getName() + "] [不同地图跳转] ["
				// + transportData.getTargetMapName() + "(" + player.getX() + "," + player.getY() + ")]");

				if (logger.isInfoEnabled()) logger.info("[处理跳转点] [成功] [cu:{}] [{}] [不同地图跳转] [从:{}({},{})] [到:{}({},{})] [{}]", 
						new Object[] {cu, player.getLogString(), gi.getName(), player.getX(), player.getY(),gameName, transportData.getTargetMapX(), transportData.getTargetMapY(),this.country+"/"+player.transferGameCountry });

			}
			sendMessage(player, message, "change game by transport");

			// 统计想要进行地图跳转
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_准备跳地图, Translate.text_3096 + gi.getName() + "->" + transportData.getTargetMapName() + ")", 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
		}

	}

	/**
	 * 将OPtion放入MW.判断是否能看到
	 * @param player
	 * @param mw
	 * @param options
	 */
	public void addOption2MenuWindow(Player player, MenuWindow mw, Option... options) {
		List<Option> needAddd = new ArrayList<Option>();
		for (Option option : options) {
			if (options == null) {
				continue;
			}
			if (ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug(player.getLogString() + "[增加窗口] [wid:" + mw.getId() + "] [option:" + option.getClass() + "]");
			}
			if (option instanceof NeedCheckPurview) {
				try {
					if (((NeedCheckPurview) option).canSee(player)) {
						needAddd.add(option);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn("[增加窗口] [异常] [option:" + option.getText() + "] [" + e + "]");
					}
				}
			} else {
				needAddd.add(option);
			}
			if (option instanceof NeedRewriteText) {
				try {
					((NeedRewriteText) option).rewriteText(player);
				} catch (Exception e) {
					e.printStackTrace();
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn("[重写窗口内容] [异常] [option:" + option.getText() + "] [" + e + "]");
					}
				}
			}
		}

		// 加入到列表的后面
		int oldLength = mw.getOptions().length;
		Option[] newOptions = new Option[mw.getOptions().length + needAddd.size()];
		for (int i = 0; i < oldLength; i++) {
			newOptions[i] = mw.getOptions()[i];
		}
		for (int i = 0; i < needAddd.size(); i++) {
			newOptions[oldLength + i] = needAddd.get(i);
			if (ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug(player.getLogString() + "[增加窗口] [真正添加] [wid:" + mw.getId() + "] [option:" + needAddd.get(i).getClass() + "] [" + needAddd.get(i).getText() + "]");
			}
		}
		mw.setOptions(newOptions);

	}

	public static int 得到扩包所需银两(int knapsackIndex, int num) {
		int cost = 0;
		for (int i = num + 1; i <= knapsackIndex; i++) {
			cost += 得到扩展某个格子的银两(i);
		}
		return cost;
	}

	public static int 得到扩展某个格子的银两(int index) {
		if (index >= 41 && index <= 45) {
			return 5000;
		} else if (index >= 46 && index <= 50) {
			return 10000;
		} else if (index >= 51 && index <= 55) {
			return 20000;
		} else if (index >= 56 && index <= 60) {
			return 40000;
		} else if (index >= 61 && index <= 65) {
			return 80000;
		} else if (index >= 66 && index <= 70) {
			return 160000;
		} else if (index >= 71 && index <= 75) {
			return 320000;
		} else if (index >= 76 && index <= 80) {
			return 640000;
		}

		return 640000;
	}

	/**
	 * 获得当前地图配置的所有任务ID
	 * @return
	 */
	public synchronized Long[] getAllTasks() {
		List<Long> ids = new ArrayList<Long>();
		HashMap<String, List<Task>> NPCtasks = TaskManager.getInstance().gameTasks.get(this.gi.displayName);
		if (NPCtasks != null) {
			for (Iterator<String> out = NPCtasks.keySet().iterator(); out.hasNext();) {
				String NPCName = out.next();
				if (NPCName != null) {
					List<Task> npcTasks = NPCtasks.get(NPCName);
					if (npcTasks != null) {
						for (Task task : npcTasks) {
							if (task != null) {
								ids.add(task.getId());
							}
						}
					}
				}
			}
		}
		return ids.toArray(new Long[0]);
	}

	public void notifyPlayerChangeHiddenConfig(Player p, SET_CLIENT_DISPLAY_FLAG flag) {

		// 从不屏蔽所有玩家，改为 屏蔽所有玩家 需要通知客户端去除屏幕内的玩家和玩家的宠物
		ArrayList<Player> teams = new ArrayList<Player>();
		if (p.getTeam() != null) {
			for (Player tt : p.getTeam().getMembers()) {
				if (tt == null || tt.getId() == p.getId()) {
					continue;
				}
				teams.add(tt);
			}
		}

		if (p.hiddenAllPlayer == false && flag.getShowPlayer() == false) {
			p.hiddenAllPlayer = true;
			ArrayList<Player> exitPlayers = new ArrayList<Player>();
			ArrayList<Pet> exitPets = new ArrayList<Pet>();

			Fighter fs[] = this.getVisbleFighter(p, false);
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					exitPlayers.add((Player) fs[i]);
				} else if (fs[i] instanceof Pet) {
					Pet pet = (Pet) fs[i];
					if (pet.getMaster() != p) {
						exitPets.add(pet);
					}
				}
			}

			AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), teams.toArray(new Player[0]), new Pet[0], new Sprite[0], exitPlayers.toArray(new Player[exitPlayers.size()]), exitPets.toArray(new Pet[exitPets.size()]), new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

			gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
		}

		// 从不屏蔽本国玩家，改为 屏蔽本国玩家 需要通知客户端去除屏幕内的玩家和玩家的宠物
		if (p.hiddenAllPlayer == false && p.hiddenSameCountryPlayer == false && flag.getShowSameCountryPlayer() == false) {
			p.hiddenSameCountryPlayer = true;
			ArrayList<Player> exitPlayers = new ArrayList<Player>();
			ArrayList<Pet> exitPets = new ArrayList<Pet>();

			Fighter fs[] = this.getVisbleFighter(p, false);
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					Player o = (Player) fs[i];
					if (o.getCountry() == p.getCountry()) {
						exitPlayers.add((Player) fs[i]);
					}
				} else if (fs[i] instanceof Pet) {
					Pet pet = (Pet) fs[i];
					if (pet.getMaster() != p && pet.getMaster() != null && pet.getMaster().getCountry() == p.getCountry()) {
						exitPets.add(pet);
					}
				}
			}

			AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), teams.toArray(new Player[0]), new Pet[0], new Sprite[0], exitPlayers.toArray(new Player[exitPlayers.size()]), exitPets.toArray(new Pet[exitPets.size()]), new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

			gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
		}

		// 从屏蔽所有玩家 到 不屏蔽所有玩家
		if (p.hiddenAllPlayer && flag.getShowPlayer()) {
			p.hiddenAllPlayer = false;

			// 是否要屏蔽本国玩家
			if (flag.getShowSameCountryPlayer() == false) {

				ArrayList<Player> enterPlayers = new ArrayList<Player>();
				ArrayList<Pet> enterPets = new ArrayList<Pet>();

				Fighter fs[] = this.getVisbleFighter(p, false);
				for (int i = 0; i < fs.length; i++) {
					if (fs[i] instanceof Player) {
						Player o = (Player) fs[i];
						if (o.getCountry() != p.getCountry()) {
							enterPlayers.add((Player) fs[i]);
						}
					} else if (fs[i] instanceof Pet) {
						Pet pet = (Pet) fs[i];
						if (pet.getMaster() != p && pet.getMaster() != null && pet.getMaster().getCountry() != p.getCountry()) {
							enterPets.add(pet);
						}
					}
				}

				int start = 0;
				int page = 20;
				while (start < enterPlayers.size()) {
					ArrayList<Player> al = new ArrayList<Player>();
					for (int i = start; i < start + page && i < enterPlayers.size(); i++) {
						al.add(enterPlayers.get(i));
					}
					start += page;
					al.addAll(teams);
					AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), al.toArray(new Player[al.size()]), new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

					gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
				}
				start = 0;
				page = 20;
				while (start < enterPets.size()) {
					ArrayList<Pet> al = new ArrayList<Pet>();
					for (int i = start; i < start + page && i < enterPets.size(); i++) {
						al.add(enterPets.get(i));
					}
					start += page;
					AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), teams.toArray(new Player[0]), al.toArray(new Pet[al.size()]), new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

					gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
				}

			} else {
				ArrayList<Player> enterPlayers = new ArrayList<Player>();
				ArrayList<Pet> enterPets = new ArrayList<Pet>();

				Fighter fs[] = this.getVisbleFighter(p, false);
				for (int i = 0; i < fs.length; i++) {
					if (fs[i] instanceof Player) {
						Player o = (Player) fs[i];
						enterPlayers.add(o);

					} else if (fs[i] instanceof Pet) {
						Pet pet = (Pet) fs[i];
						if (pet.getMaster() != p) {
							enterPets.add(pet);
						}
					}
				}

				int start = 0;
				int page = 20;
				while (start < enterPlayers.size()) {
					ArrayList<Player> al = new ArrayList<Player>();
					for (int i = start; i < start + page && i < enterPlayers.size(); i++) {
						al.add(enterPlayers.get(i));
					}
					start += page;
					al.addAll(teams);
					AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), al.toArray(new Player[al.size()]), new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

					gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
				}
				start = 0;
				page = 20;
				while (start < enterPets.size()) {
					ArrayList<Pet> al = new ArrayList<Pet>();
					for (int i = start; i < start + page && i < enterPets.size(); i++) {
						al.add(enterPets.get(i));
					}
					start += page;
					AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), teams.toArray(new Player[0]), al.toArray(new Pet[al.size()]), new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

					gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
				}
			}
		}

		// 从 屏蔽本国玩家 到 打开本国玩家
		if (p.hiddenAllPlayer == false && p.hiddenSameCountryPlayer && flag.getShowSameCountryPlayer()) {
			p.hiddenSameCountryPlayer = false;
			ArrayList<Player> enterPlayers = new ArrayList<Player>();
			ArrayList<Pet> enterPets = new ArrayList<Pet>();

			Fighter fs[] = this.getVisbleFighter(p, false);
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					Player o = (Player) fs[i];
					if (o.getCountry() == p.getCountry()) {
						enterPlayers.add((Player) fs[i]);
					}
				} else if (fs[i] instanceof Pet) {
					Pet pet = (Pet) fs[i];
					if (pet.getMaster() != p && pet.getMaster() != null && pet.getMaster().getCountry() == p.getCountry()) {
						enterPets.add(pet);
					}
				}
			}

			int start = 0;
			int page = 20;
			while (start < enterPlayers.size()) {
				ArrayList<Player> al = new ArrayList<Player>();
				for (int i = start; i < start + page && i < enterPlayers.size(); i++) {
					al.add(enterPlayers.get(i));
				}
				start += page;
				al.addAll(teams);
				AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), al.toArray(new Player[al.size()]), new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

				gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
			}
			start = 0;
			page = 20;
			while (start < enterPets.size()) {
				ArrayList<Pet> al = new ArrayList<Pet>();
				for (int i = start; i < start + page && i < enterPets.size(); i++) {
					al.add(enterPets.get(i));
				}
				start += page;
				AROUND_CHANGE_REQ req = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), teams.toArray(new Player[0]), al.toArray(new Pet[al.size()]), new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0],new String[0],new long[0]);

				gm.framework.sendMessage(p.getConn(), req, "AROUND_CHANGE_REQ");
			}
		}

		p.hiddenAllPlayer = !flag.getShowPlayer();
		p.hiddenSameCountryPlayer = !flag.getShowSameCountryPlayer();
		p.hiddenChatMessage = !flag.getShowChat();

	}

	/**
	 * 删除所有此id的怪物
	 * @param mosterId
	 * @return
	 */
	public int removeAllMonsterById(int mosterId, int count) {
		// boolean exist = false;
		int killCount = 0;
		synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof Monster) {
					Monster m = (Monster) l;
					if (m.getSpriteCategoryId() == mosterId) {
						killCount += 1;
						// exist = true;
						newRemoveObjectList.add(l);
						if (killCount >= count) {
							break;
						}
					}
				}
			}
		}
		return killCount;
	}

	/**
	 * 清楚场景中所有的怪物
	 */
	public void removeAllMonster() {
		
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof Monster) {
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(l);
				}
			}
		}
		
		/*synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof Monster) {
					newRemoveObjectList.add(l);
				}
			}
		}*/
	}
	public void removeAllMonster(int cateId) {
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof Monster) {
				if (((Monster)l).getSpriteCategoryId() == cateId) {
					synchronized (newRemoveObjectList) {
						newRemoveObjectList.add(l);
					}
				}
			}
		}
	}

	public void removeAllChestNpc() {
		
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof ChestFightNPC) {
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(l);
				}
			}
		}
		
		/*synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof ChestFightNPC) {
					newRemoveObjectList.add(l);
				}
			}
		}*/
	}
	public void removeAllFireNpc() {
		
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof DisasterFireNPC) {
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(l);
				}
			}
		}
		
		/*synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof DisasterFireNPC) {
					newRemoveObjectList.add(l);
				}
			}
		}*/
	}
	
	public void removeAllNpc() {
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof NPC) {
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(l);
				}
			}
		}
		
		/*synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof NPC) {
					newRemoveObjectList.add(l);
				}
			}
		}*/
	}
	public void removeAllAppNpc() {
		
		LivingObject los[] = livingSet.toArray(new LivingObject[0]);
		for (LivingObject l : los) {
			if (l instanceof AppointedAttackNPC) {
				synchronized (newRemoveObjectList) {
					newRemoveObjectList.add(l);
				}
			}
		}
		
		/*synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof AppointedAttackNPC) {
					newRemoveObjectList.add(l);
				}
			}
		}*/
	}

	public void addParticle2Monster(int monsterId, String particleName) {
		for (LivingObject l : livingSet) {
			if (l instanceof Monster) {
				Monster m = (Monster) l;
				if (m.getSpriteCategoryId() == monsterId) {
					m.setParticleName(particleName);
				}
			}
		}
	}

	public void removeParticle2Monster(int monsterId) {
		for (LivingObject l : livingSet) {
			if (l instanceof Monster) {
				Monster m = (Monster) l;
				if (m.getSpriteCategoryId() == monsterId) {
					m.setParticleName("");
				}
			}
		}
	}

	public boolean hasMonster(int monster) {
		for (LivingObject l : livingSet) {
			if (l instanceof Monster) {
				Monster m = (Monster) l;
				if (m.getSpriteCategoryId() == monster) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 返回game中存在的怪物数量
	 * @return
	 */
	public int getMonsterNum() {
		int num = 0;
		for (LivingObject l : livingSet) {
			if (l instanceof Sprite) {
				num++;
			}
		}
		return num;
	}

	public int getMonsterNums() {
		int num = 0;
		for (LivingObject l : livingSet) {
			if (l instanceof Monster) {
				num++;
			}
		}
		return num;
	}

	/**
	 * 根据地图坐标点返回周围5范围内的怪物数量
	 * @param x
	 * @param y
	 * @return
	 */
	public int getMonsterNumByPoints(int x, int y) {
		int num = 0;
		for (LivingObject l : livingSet) {
			if ((l.getX() < (x + 5) && l.getX() > (x - 5)) && (l.getY() < (y + 5) && l.getY() > (y - 5))) {
				if (l instanceof Sprite) {
					num++;
				}
			}
		}
		return num;
	}

	public int getMonsterNumByPoints(int x, int y, int r) {
		int num = 0;
		for (LivingObject l : livingSet) {
			if ((l.getX() < (x + r) && l.getX() > (x - r)) && (l.getY() < (y + r) && l.getY() > (y - r))) {
				if (l instanceof Sprite) {
					num++;
				}
			}
		}
		return num;
	}

	/***
	 * 神魂劫剧情--秒杀所有怪
	 * @param mosterId
	 * @param isplayerDamage
	 *            玩家是否收伤害
	 * @return
	 */
	public boolean shenhun_juqing(Player player, boolean isplayerDamage) {
		boolean exist = false;
		synchronized (livingSet) {
			for (LivingObject l : livingSet) {
				if (l instanceof Monster) {
					Monster m = (Monster) l;
					player.rayDamageFeedback(m, m.getMaxHP());
				} else if (isplayerDamage && l instanceof Player) {
					if (player.getId() == l.getId()) {
						((Player) l).causeDamageByRayRobbery((int) (((Player) l).getHp() * 0.99));
					}
				}
			}
		}
		return exist;
	}

	/**
	 * 判断玩家是否在当前地图
	 * @param player
	 * @return
	 */
	public boolean isPlayerInGame(Player player) {
		return livingSet.contains(player);
	}
}
