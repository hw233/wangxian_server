package com.fy.engineserver.jiazu2.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.model.BaseModel;
import com.fy.engineserver.jiazu2.model.BiaoCheQianghuaModel;
import com.fy.engineserver.jiazu2.model.BiaocheRewardModel;
import com.fy.engineserver.jiazu2.model.JiazuRenwuModel;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.jiazu2.model.QifuModel;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_TakeOne_Task;
import com.fy.engineserver.menu.activity.jiazu2.Open_refreshTaskConfirm;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_Buff;
import com.fy.engineserver.menu.jiazu.Option_Jiazu_Lingkuang_Notice;
import com.fy.engineserver.menu.jiazu2.Option_Confirm_LevelDown;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.date.charm.YuanZhenBillboard;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.util.CompoundReturn;

public class JiazuManager2 {
	public static JiazuManager2 instance;
	
	public static Logger logger = JiazuSubSystem.logger;
	
	private String fileName;
	private String fileName2;
	/** vip等级对应的每天可祈福的次数 */
	public Map<Byte, Integer> qifuTimes = new HashMap<Byte, Integer>();
	/**  key=祈福等级*/
	public Map<Integer, QifuModel> qifuMaps = new HashMap<Integer, QifuModel>();
	/** key=修炼技能id */
	public Map<Integer, PracticeModel> praticeMaps = new LinkedHashMap<Integer, PracticeModel>();
	/** 镖车强化配置   key=强化类型  key2=强化等级 */
	public Map<Byte, HashMap<Integer, BiaoCheQianghuaModel>> biaocheMap = new HashMap<Byte, HashMap<Integer, BiaoCheQianghuaModel>>();
	/** 基础配置 */
	public BaseModel base ;
	/** 家族资金提示 */
	public Map<Integer, Integer> noticeNum = new HashMap<Integer, Integer>();
	/** 镖车相关（key=jiazu等级） */
	public Map<Integer, BiaocheRewardModel> biaocheRewards = new Hashtable<Integer, BiaocheRewardModel>();
	/** key=任务星级  value=任务id列表 */
	public Map<Integer, Long[]> taskStarMap = new HashMap<Integer, Long[]>();
	/** 星级随机概率 */
	public static int[] starProbabbly = new int[5];
	/** key=任务id */
	public Map<Long, JiazuRenwuModel> taskMap = new HashMap<Long, JiazuRenwuModel>();
	/** key=playerId, value=任务列表 */
	private Map<Long, List<Long>> playerTasks = new HashMap<Long, List<Long>>();
	
	public Map<Integer, String> translate = new HashMap<Integer, String>();
	/** 家族面板描述信息  key=面板按钮名 */
	public Map<String, String> releventDes = new HashMap<String, String>();
	
	public static Map<Integer,BossConfig> bconfigs = new HashMap<Integer, BossConfig>();
	public static Map<Long,BossDamage> damages = new HashMap<Long, BossDamage>();
//	/多人副本
	public List<HuanJingSelfConfig> hconfigs = new ArrayList<HuanJingSelfConfig>();
	public static final int 每天击杀镖车获得奖励次数 = 2;
	public static final int 击杀镖车奖励修炼值 = 5;
	
	public static int 任务数量 = 7;
	
	public static final byte 封印家族功能 = -1;
	public static final byte 家族功能正常 = 0;
	
	public static String[] 家族酒称号 = Translate.家族酒称号;
	
	public static final int 家族面板描述 = 1;
	
	public void init() throws Exception{
		instance = this;
		this.initFile();
		this.initFile2();
	}
	
	
	public void updateBattleDamage(Fighter f,int damage){
		if(!JiazuManager2.instance.isOpen()){
			return;
		}
		Player p = null;
		try {
			if(f instanceof Player){
				p = (Player)f;
			}else if(f instanceof Pet){
				p = ((Pet)f).getMaster();
			}
			
			if(p == null || p.getJiazuId() <= 0){
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiazuId());
			if(jiazu == null){
				logger.warn("[更新战斗伤害] [家族远征] [失败:家族不存在] [{}] [{}]",new Object[]{jiazu,p.getLogString()});
				return;
			}
			CityAction c = jiazu.getCity();
			
			if(jiazu.isHasKillBoss() || jiazu.getCity() == null){
				logger.warn("[更新战斗伤害] [家族远征] [失败:不在副本] [{}] [{}] [{}] [{}]",new Object[]{jiazu.isHasKillBoss(),jiazu.getCity(),jiazu.getLogString(), p.getLogString()});
				return; 
			}
			if(c == null || !(c instanceof BossCity)){
				logger.warn("[更新战斗伤害] [家族远征] [失败:副本不正确] [{}] [{}] [{}] [{}]",new Object[]{jiazu.isHasKillBoss(),jiazu.getCity(),jiazu.getLogString(), p.getLogString()});
				return; 
			}
			BossCity bc = (BossCity)c;
			if(!bc.playerInGame(p)){
				logger.warn("[更新战斗伤害] [家族远征] [失败:不在同一副本] [{}] [{}] [{}] [{}]",new Object[]{jiazu.isHasKillBoss(),jiazu.getCity(),jiazu.getLogString(), p.getLogString()});
				return; 
			}
			
			BossDamage d = damages.get(p.getId());
			if(d == null){
				d = new BossDamage();
			}
			d.setPlayerId(p.getId());
			d.setDamages(damage + d.getDamages());
			damages.put(p.getId(),d);
			
			DownCityManager2.logger.warn("[更新战斗伤害] [家族远征] [成功] [damages:{}] [技能初始伤害:{}] [总伤害:{}] [玩家:{}] [家族:{}]",
					new Object[]{damages.size(), damage,d.getDamages(), p.getName(),p.getJiazuName()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[更新战斗伤害] [家族远征] [异常] [damage:{}] [{}] [{}]",new Object[]{damage,p.getLogString(),e});
		}
	}
	
	public static void main(String[] args) throws Exception {
//		JiazuManager2 m = new JiazuManager2();
//		m.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//jiazu2//jiazu2data.xls");
//		m.setFileName2("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//jiazu2//jiazu_biaoche.xls");
//		MagicWeaponManager mw = new MagicWeaponManager();
//		mw.initAttrTypeMapping();
//		m.initFile2();
//		m.initFile();
		for (int i=0; i<=30; i++) {
			System.out.println("[家族等级:" + i + "] [炼丹颜色:" + getLuckTreePermisionLevel(i) + "]");
		}
	}
	
	public static int[] 家族炼丹颜色对应等级 = new int[]{2, 6, 9, 12, 15};
	/**
	 * 根据等级获取炼丹颜色等级
	 * @param level
	 * @return
	 */
	public static int getLuckTreePermisionLevel(int level) {
		int color = -1;
		for (int i=0; i<家族炼丹颜色对应等级.length; i++) {
			if (level >= 家族炼丹颜色对应等级[i]) {
				color = i;
			} else {			//基本不满足去判断是否有活动开启
				CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.platLuckTree, level, i);
				if (cr != null && cr.getBooleanValue()) {
					color = i;
				}
			}
		}
		return color;
	}
	
	
	
	/**
	 * 活跃度奖励
	 */
	boolean hasReward = false;
	public void livenessReward(){
		try {
			if(!JiazuManager.getInstance().testJiaZuReward){
				if (hasReward) return;
				Calendar cal = Calendar.getInstance();
				int d = cal.get(Calendar.DAY_OF_WEEK);
				int h = cal.get(Calendar.HOUR_OF_DAY);
				int m = cal.get(Calendar.MINUTE);
				if (h != 0) return;
				if (d != Calendar.SUNDAY) return;
				if (m != 0) return;
			}
			
			List<Jiazu> list = JiazuManager.jiazuEm.query(Jiazu.class, " liveness > 0", "liveness DESC ", 1, 5000);
			logger.warn("[活跃度奖励] [test] [list:{}]",new Object[]{list.size()});
			if(list != null && list.size() > 0){
				int rank = 0;
				for(Jiazu jiazu : list){
					rank++;
					long addSalary = 0;
					if(rank == 1){
						addSalary = 3000 * 1000L;
					}else if(rank == 2){
						addSalary = 2500 * 1000L;
					}else if(rank == 3){
						addSalary = 2000 * 1000L;
					}else if(rank >= 4 && rank <= 5){
						addSalary = 1500 * 1000L;
					}else if(rank >= 6 && rank <= 10){
						addSalary = 1000 * 1000L;
					}else if(rank >= 11 && rank <= 20){
						addSalary = 500 * 1000L;
					}else if(rank >= 21 && rank <= 50){
						addSalary = 200 * 1000L;
					}else if(rank >= 51){
						addSalary = 100 * 1000L;
					}
					jiazu.setSalaryLeft(jiazu.getSalaryLeft() + addSalary);
					jiazu.setSalarySum(jiazu.getSalarySum() + addSalary);
					hasReward = true;
					logger.warn("[活跃度奖励] [资金:{}] [剩余可发资金:{}] [活跃度:{}] [{}]",new Object[]{addSalary,jiazu.getSalaryLeft(),jiazu.getLiveness(), jiazu.getLogString()});
					jiazu.setLiveness(0);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[活跃度奖励] [异常]",e);
		}
	}
	
	public void addLiveness(Player p,JiaZuLivenessType type){
		try {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiazuId());
			if(jiazu == null){
				return;
			}
			float score = type.getScore();
			jiazu.setLiveness(jiazu.getLiveness() + score);
			logger.warn("[增加家族活跃度] [个人荣誉] [{}] [成功] [活跃度:{}] [家族:{}] [玩家:{}]",new Object[]{type,score,jiazu.getLogString(), p.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[增加家族活跃度] [个人荣誉] [{}] [异常] [玩家:{}] [{}]",new Object[]{type,p.getLogString(),e});
		}
	}
	
	public void addLiveness(Jiazu jiazu,JiaZuLivenessType type){
		try {
			if(jiazu == null){
				return;
			}
			float score = type.getScore();
			jiazu.setLiveness(jiazu.getLiveness() + score);
			logger.warn("[增加家族活跃度] [家族荣誉] [{}] [成功] [活跃度:{}] [家族:{}]",new Object[]{type,score,jiazu.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[增加家族活跃度] [家族荣誉] [{}] [异常] [{}]",new Object[]{type,e});
		}
	}
	
	
	public static String checkJiazu(Player player) {
		if(player.getJiazuId() <= 0) {
			return Translate.您还没有家族;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.您还没有家族;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jm == null) {
			return Translate.您还没有家族;
		}
		return null;
	}
	
	public int[] getAlreadyCompTaskNumAndMaxNum(Player player) {
		int[] result = new int[2];
		JiazuRenwuModel jmm = null;
		for (JiazuRenwuModel jrm : taskMap.values()) {
			jmm = jrm;
			break;
		}
		Task task = TaskManager.getInstance().getTask(jmm.getTaskId());
		if (task.getBigCollection() != null && !"".equals(task.getBigCollection())) {
			List<Task> bigCollection = TaskManager.getInstance().getTaskBigCollections().get(task.getBigCollection());
			if (bigCollection != null && bigCollection.size() > 0) {
				int maxNum = task.getDailyTaskMaxNum();// 周期内上限
				int groupTaskCycleDoneNum = 0;// 周期内实际完成次数
				for (Task _groupTask : bigCollection) {
					TaskEntity tte = player.getTaskEntity(_groupTask.getId());
					if (tte != null && _groupTask.getType() == Player.TASK_TYPE_DAILY) {
						CompoundReturn info = tte.getCycleDeilverInfo();
						int doneNum = maxNum - info.getIntValue();
						groupTaskCycleDoneNum += doneNum;
					}
				}
				result[0] = maxNum;
				result[1] = groupTaskCycleDoneNum;
			}
		}
		return result;
	}
	
	public boolean taskTask(Player player, long taskId) {
		return this.getPlayerJiazuTask(player).remove(taskId);
	}
	
	public boolean refreshTaskList(Player player) {
		return refreshTaskList(player, false);
	}
	public boolean refreshTaskList(Player player, boolean free) {
		return refreshTaskList(player, free, true);
	}
	/**
	 * 付费刷任务列表
	 * @param player
	 */
	public boolean refreshTaskList(Player player, boolean free, boolean confirm) {
		synchronized (player) {
			if (!free) {
				String result = JiazuEntityManager2.instance.expenseSiliver(player, JiazuEntityManager2.优先扣除工资, base.getCost4RefreshTask(), (byte) (confirm?0:1), "", "刷新任务");
				if (result != null) {
					if (result.indexOf(JiazuEntityManager2.needConfirm) >= 0) {
						refreshConfirm(player, Long.parseLong(result.split("_")[1]));
						return false;
					}
					player.sendError(result);
					return false;
				}
			}
			List<Long> list = new ArrayList<Long>();
			while (list.size() < 任务数量) {
				long taskId = getRandomTaskId(player);
				if (!list.contains(taskId)) {
					list.add(taskId);
				}
			}
			playerTasks.put(player.getId(), list);
			return true;
		}
	}
	public void refreshConfirm(Player player, long needMoney) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Open_refreshTaskConfirm option1 = new Open_refreshTaskConfirm();
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = translate.get(4);
		mw.setDescriptionInUUB(String.format(msg, needMoney/1000+""));
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	/**
	 * 接取任务弹窗
	 * @param player
	 * @param taskId
	 */
	public void takeOneTaksConfirm(Player player, long taskId) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_TakeOne_Task option1 = new Option_TakeOne_Task();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setTaskId(taskId);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = translate.get(3);
		Task task = TaskManager.getInstance().getTask(taskId);
		mw.setDescriptionInUUB(String.format(msg, task.getName()));
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	/**
	 * 获取玩家家族任务列表
	 * @param player
	 * @return
	 */
	public List<Long> getPlayerJiazuTask(Player player) {
		if (playerTasks.containsKey(player.getId())) {
			return playerTasks.get(player.getId());
		}
		List<Long> list = new ArrayList<Long>();
		while (list.size() < 任务数量) {
			long taskId = getRandomTaskId(player);
			if (!list.contains(taskId)) {
				list.add(taskId);
			}
		}
		playerTasks.put(player.getId(), list);
		return list;
	}
	
	public long getRandomTaskId(Player player) {
		int star = 1;
		int ran = player.random.nextInt(1000);
		int tempRan = 0;
		for (int i=0; i<starProbabbly.length; i++) {
			tempRan += starProbabbly[i];
			if (ran <= tempRan) {
				star = i+1;
				break;
			}
		}
		Long[] taskIds = taskStarMap.get(star);
		int ran2 = player.random.nextInt(taskIds.length);
		return taskIds[ran2];
	}
	
	public void noticeLingKuangZhanStart(long jiazuId, byte country, String mapName, int x, int y) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jiazu == null) {
			VillageFightManager.logger.warn("[新家族] [灵矿战开始弹窗提示] [异常] [家族没找到] [jiazuId : " + jiazuId + "]");
			return ;
		}
		List<JiazuMember> list = jiazu.getMembers();
		Player player = null;
		if (list != null && list.size() > 0) {
			for (JiazuMember jm : list) {
				if (GamePlayerManager.getInstance().isOnline(jm.getPlayerID())) {
					try {
						player = GamePlayerManager.getInstance().getPlayer(jm.getPlayerID());
						popConfirmWindow(player, country, mapName, x, y);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						VillageFightManager.logger.error("[新家族] [灵矿战开始通知异常] [" + jm.getPlayerID() + "]", e);
					}
				}
			}
		}
	}
	
	private void popConfirmWindow(Player p, byte country, String mapName, int x, int y) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_Jiazu_Lingkuang_Notice option1 = new Option_Jiazu_Lingkuang_Notice();
		option1.setCountry(country);
		option1.setMapName(mapName);
		option1.setX(x);
		option1.setY(y);
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.灵矿战开始;
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	public static void popConfirmTakeBuff(Player p, NPC npc, long money) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_Jiazu_Buff option1 = new Option_Jiazu_Buff();
		option1.setNPC(npc);
		option1.setConfirm(true);
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = String.format(Translate.发布家族buff确认, BillingCenter.得到带单位的银两(money));
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	public static void popConfirmLevelDown(Player p, SeptStation station, long NPCId, SeptBuildingTemplet template, String buildName) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_Confirm_LevelDown option1 = new Option_Confirm_LevelDown();
		option1.setNPCId(NPCId);
		option1.setStation(station);
		option1.setTemplate(template);
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = String.format(Translate.家族建筑降级确认, buildName);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	/**
	 * 通知官员家族资金不足
	 * @param jiazu
	 */
	public void noticeJizuMondeyNotEnough(Jiazu jiazu) {
		try {
			ArrayList<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master, 
					JiazuTitle.vice_master, JiazuTitle.law_elder, JiazuTitle.fight_elder, JiazuTitle.research_elder);
			if (list == null || list.size() <= 0) {
				return ;
			}
			for (JiazuMember jm : list) {
				try {
					MailManager.getInstance().sendMail(jm.getPlayerID(), new ArticleEntity[] {}, new int[] { }, 
							Translate.家族资金不足邮件title, Translate.家族资金不足邮件内容, 0L, 0L, 0L, "家族资金不足邮件提醒");
				} catch (Exception eex) {
					logger.error("[新家族] [给官员发送邮件通知] [异常] [" + jiazu.getLogString() + "] [playerId : " + jm.getPlayerID() + "]", eex);
				}
			}
		} catch (Exception e) {
			logger.error("[新家族] [通知家族官员家族资金不足] [异常] [" + jiazu.getLogString() + "]", e);
		}
	}
	public void noticeJizuLevelDown(Jiazu jiazu) {
		try {
			ArrayList<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master, 
					JiazuTitle.vice_master, JiazuTitle.law_elder, JiazuTitle.fight_elder, JiazuTitle.research_elder);
			if (list == null || list.size() <= 0) {
				return ;
			}
			for (JiazuMember jm : list) {
				try {
					MailManager.getInstance().sendMail(jm.getPlayerID(), new ArticleEntity[] {}, new int[] { }, 
							Translate.家族资金不足邮件title, Translate.家族降级邮件内容, 0L, 0L, 0L, "家族降级邮件通知");
				} catch (Exception eex) {
					logger.error("[新家族] [给官员发送邮件通知] [异常] [" + jiazu.getLogString() + "] [playerId : " + jm.getPlayerID() + "]", eex);
				}
			}
		} catch (Exception e) {
			logger.error("[新家族] [通知家族官员家族资金不足] [异常] [" + jiazu.getLogString() + "]", e);
		}
	}
	/**
	 * 家族维护通知家族成员   发消息到家族
	 * @param jiazu
	 */
	public void noticeJizuMaint(Jiazu jiazu, String title, String content) {
		try {
//			ArrayList<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master, 
//					JiazuTitle.vice_master, JiazuTitle.law_elder, JiazuTitle.fight_elder, JiazuTitle.research_elder);
//			if (list == null || list.size() <= 0) {
//				return ;
//			}
//			for (JiazuMember jm : list) {
//				try {
//					MailManager.getInstance().sendMail(jm.getPlayerID(), new ArticleEntity[] {}, new int[] { }, 
//							title, content, 0L, 0L, 0L, "家族维护邮件");
//				} catch (Exception eex) {
//					logger.error("[新家族] [给官员发送邮件通知1] [异常] [" + jiazu.getLogString() + "] [playerId : " + jm.getPlayerID() + "]", eex);
//				}
//			}
			ChatMessageService.getInstance().sendMessageToJiazu(jiazu, content, title);
		} catch (Exception e) {
			logger.error("[新家族] [通知家族官员家族资金不足] [异常] [" + jiazu.getLogString() + "]", e);
		}
	}
	
	public static int getLianDanNums(int jiazuLevel) {
		if (jiazuLevel < 2) {
			return 0;
		} else if (jiazuLevel <=5) {
			return 4;
		} else if (jiazuLevel <= 9) {
			return 6;
		} else if (jiazuLevel <= 14) {
			return 8;
		}
		return 10;
	}
	
	public static int getSiYangNums(int jiazuLevel) {
		if (jiazuLevel < 2) {
			return 0;
		} else if (jiazuLevel <=5) {
			return 4;
		} else if (jiazuLevel <= 9) {
			return 6;
		} else if (jiazuLevel <= 14) {
			return 8;
		}
		return 10;
	}
	
	
	private void initFile2() throws Exception {
		File f = new File(getFileName2());
		if(!f.exists()){
			throw new Exception(getFileName2() + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(0);	//血量强化 
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				BiaoCheQianghuaModel bcm = this.getBiaoCheQianghuaModel(row);
				bcm.setType(BiaocheEntityManager.强化血量);
				HashMap<Integer, BiaoCheQianghuaModel> map = biaocheMap.get(BiaocheEntityManager.强化血量);
				if (map == null) {
					map = new HashMap<Integer, BiaoCheQianghuaModel>();
				}
				map.put(bcm.getLevel(), bcm);
				biaocheMap.put(bcm.getType(), map);
//				System.out.println(bcm);
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);	//双防强化
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				BiaoCheQianghuaModel bcm = this.getBiaoCheQianghuaModel(row);
				bcm.setType(BiaocheEntityManager.强化双防);
				HashMap<Integer, BiaoCheQianghuaModel> map = biaocheMap.get(BiaocheEntityManager.强化双防);
				if (map == null) {
					map = new HashMap<Integer, BiaoCheQianghuaModel>();
				}
				map.put(bcm.getLevel(), bcm);
				biaocheMap.put(bcm.getType(), map);
//				System.out.println(bcm);
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(2);	//资金不足提醒
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				noticeNum.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getInt(row, 1, logger));
				BiaocheRewardModel brm = new BiaocheRewardModel();
				brm.setJiazuLevel(ReadFileTool.getInt(row, 0, logger));
				brm.setRewardMoney(ReadFileTool.getLong(row, 2, logger, 0));
//				System.out.println(brm);
				biaocheRewards.put(brm.getJiazuLevel(), brm);
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		sheet = workbook.getSheetAt(3);				//面板描述信息
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					releventDes.put(ReadFileTool.getString(row, 0, logger), ReadFileTool.getString(row, 1, logger));
//					System.out.println(ReadFileTool.getInt(row, 0, logger) + "   " + ReadFileTool.getString(row, 1, logger));
				}catch(Exception e){
					throw new Exception("[新家族翻译] [异常] [行："+i+"]",e);
				}
			}
		}
	}
	
	private BiaoCheQianghuaModel getBiaoCheQianghuaModel(HSSFRow row) {
		BiaoCheQianghuaModel bcm = new BiaoCheQianghuaModel();
		int index = 0;
		bcm.setLevel(ReadFileTool.getInt(row, index++, logger));
		bcm.setCostJiazuMoney(ReadFileTool.getInt(row, index++, logger));
		bcm.setCostLingmai(ReadFileTool.getLong(row, index++, logger, 0));
		bcm.setAddNum(ReadFileTool.getInt(row, index++, logger));
		bcm.setNeedJiazuLevel(ReadFileTool.getInt(row, index++, logger));
		return bcm;
	}
	
	
	private void initFile() throws Exception {
		File f = new File(getFileName());
		if(!f.exists()){
			throw new Exception(getFileName() + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);	//基础配置
		base = new BaseModel();
		HSSFRow row1 = sheet.getRow(1);
		base.setAddExp(ReadFileTool.getInt(row1, 1, logger));
		base.setCostXiulian(ReadFileTool.getInt(row1, 2, logger));
		base.setCostSiliverNums(ReadFileTool.getInt(row1, 3, logger)*1000);
		base.setCost4RefreshTask(ReadFileTool.getInt(row1, 4, logger)*1000);
		 
		sheet = workbook.getSheetAt(1);	
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				qifuTimes.put(ReadFileTool.getByte(row, 0, logger), ReadFileTool.getInt(row, 1, logger));
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(2);	
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				QifuModel qm = new QifuModel();
				qm.setQifuLevel(ReadFileTool.getInt(row, 0, logger));
				qm.setCostType(ReadFileTool.getByte(row, 1, logger));
				qm.setCostNum(ReadFileTool.getLong(row, 2, logger, 0) * 1000);
				qm.setAddNum(ReadFileTool.getInt(row, 3, logger));
				qm.setAddJiazuZiyuan(ReadFileTool.getInt(row, 4, logger));
				qifuMaps.put(qm.getQifuLevel(), qm);
//				System.out.println(qm);
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(3);	
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				PracticeModel pm = getPracticeModel(row);
				praticeMaps.put(pm.getSkillId(), pm);
//				System.out.println(pm);
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(4);	
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				int star = ReadFileTool.getInt(row, index++, logger);
				int prob = ReadFileTool.getInt(row, index++, logger);
				Long[] taskIds = ReadFileTool.getLongArrByString(row, index++, logger, ",");
				int[] xiulians = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				int[] ziyuans = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				for (int ii=0; ii<taskIds.length; ii++) {
					if (TaskManager.getInstance().getTask(taskIds[ii]) == null) {
						throw new Exception("[任务不存在:" + taskIds[ii] + "]");
					}
					JiazuRenwuModel jrm = new JiazuRenwuModel();
					jrm.setTaskId(taskIds[ii]);
					jrm.setAddXiulian(xiulians[ii]);
					jrm.setAddJiazuZiyuan(ziyuans[ii]);
					jrm.setStar(star);
					taskMap.put(jrm.getTaskId(), jrm);
//					System.out.println(i + "==" + jrm);
				}
				taskStarMap.put(star, taskIds);
				starProbabbly[star-1] = prob;
			} catch (Exception e) {
				throw new Exception("[" + getFileName() + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(5);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					translate.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getString(row, 1, logger));
//					System.out.println(ReadFileTool.getInt(row, 0, logger) + "   " + ReadFileTool.getString(row, 1, logger));
				}catch(Exception e){
					throw new Exception("[新家族翻译] [异常] [行："+i+"]",e);
				}
			}
		}
		
//		sheet = workbook.getSheetAt(6);	
//		rows = sheet.getPhysicalNumberOfRows();
//		for(int i=1;i<rows;i++){
//			HSSFRow row = sheet.getRow(i);
//			if(row!=null){
//				try{
//					int index = 0;
//					int level = ReadFileTool.getInt(row, index++, logger);
//					int id = ReadFileTool.getInt(row, index++, logger);
//					long hp = ReadFileTool.getLong(row, index++, logger, 0);
//					String[] run_rewards = ReadFileTool.getStringArr(row, index++, logger, ",");
//					String[] kill_rewards = ReadFileTool.getStringArr(row, index++, logger, ",");
//					String run_other_reward = ReadFileTool.getString(row, index++, logger);
//					String kill_other_reward = ReadFileTool.getString(row, index++, logger);
//					BossConfig c = new BossConfig();
//					c.setLevel(level);
//					c.setId(id);
//					c.setHp(hp);
//					c.setRun_reward1_10(run_rewards);
//					c.setKill_reward1_10(kill_rewards);
//					c.setRun_other_reward(run_other_reward);
//					c.setKill_other_reward(kill_other_reward);
//					bconfigs.put(c.getLevel(), c);
//					logger.warn("[家族远征配置] ["+c+"]");
//				}catch(Exception e){
//					throw new Exception("[boss奖励] [异常] [行："+i+"]",e);
//				}
//			}
//		}
		
//		sheet = workbook.getSheetAt(7);	
//		rows = sheet.getPhysicalNumberOfRows();
//		for(int i=1;i<rows;i++){
//			HSSFRow row = sheet.getRow(i);
//			if(row!=null){
//				try{
//					HuanJingSelfConfig c = new HuanJingSelfConfig();
//					int playerLevel = ReadFileTool.getInt(row,0, logger);
//					int id = ReadFileTool.getInt(row, 1, logger);
//					String bossName = ReadFileTool.getString(row, 2, logger);
//					String bossIcon = ReadFileTool.getString(row, 3, logger);
//					String mapName = ReadFileTool.getString(row, 4, logger);
//					int x = ReadFileTool.getInt(row, 5, logger);
//					int y = ReadFileTool.getInt(row, 6, logger);
//					String [] rewardName = ReadFileTool.getStringArr(row, 7, logger, ",");
//					int [] rewardNums = ReadFileTool.getIntArrByString(row, 8, logger, ",");
//					String cityName = ReadFileTool.getString(row, 9, logger);
//					c.setBossId(id);
//					c.setMapName(mapName);
//					c.setX(x);
//					c.setY(y);
//					c.setBossIcon(bossIcon);
//					c.setBossName(bossName);
//					c.setPlayerLevel(playerLevel);
//					c.setRewardNames(rewardName);
//					c.setRewardNums(rewardNums);
//					c.setCityName(cityName);
//					hconfigs.add(c);
//					logger.warn("[家族幻境配置] ["+c+"]");
//				}catch(Exception e){
//					throw new Exception("[boss奖励] [异常] [行："+i+"]",e);
//				}
//			}
//		}
		
		sheet = workbook.getSheetAt(6);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = (int) row.getCell(0).getNumericCellValue();
					double exp = row.getCell(1).getNumericCellValue();
					petBasicExpMap.put(level, exp);
				}catch(Exception e){
					throw new Exception("[petBasicExpMap] [异常] [行："+i+"]",e);
				}
			}
		}
		
		sheet = workbook.getSheetAt(7);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = ReadFileTool.getInt(row, 0, logger);
					double exp = 0;
					try {
						exp = row.getCell(1).getNumericCellValue();
					} catch (Exception e) {
						exp = Double.valueOf(row.getCell(1).getStringCellValue());
						
					}
					petOtherExpMap.put(level, exp);
				}catch(Exception e){
					throw new Exception("[petOtherExpMap] [异常] [行："+i+"]",e);
				}
			}
		}
		
		sheet = workbook.getSheetAt(8);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = ReadFileTool.getInt(row, 0, logger);
					int exp = ReadFileTool.getInt(row, 1, logger);
					petBlessExpMap.put(level, exp);
				}catch(Exception e){
					throw new Exception("[petBlessExpMap] [异常] [行："+i+"]",e);
				}
			}
		}
		
//		sheet = workbook.getSheetAt(10);	
//		rows = sheet.getPhysicalNumberOfRows();
//		for(int i=1;i<rows;i++){
//			HSSFRow row = sheet.getRow(i);
//			if(row!=null){
//				try{
//					HuanJingSelfConfig c = new HuanJingSelfConfig();
//					int id = ReadFileTool.getInt(row, 0, logger);
//					String bossName = ReadFileTool.getString(row, 1, logger);
//					String bossIcon = ReadFileTool.getString(row, 2, logger);
//					String mapName = ReadFileTool.getString(row, 3, logger);
//					int x = ReadFileTool.getInt(row, 4, logger);
//					int y = ReadFileTool.getInt(row, 5, logger);
//					int playerLevel = ReadFileTool.getInt(row,6, logger);
//					int jiazuLevel = ReadFileTool.getInt(row,7, logger);
//					String [] rewardName = ReadFileTool.getStringArr(row, 8, logger, ",");
//					int [] rewardNums = ReadFileTool.getIntArrByString(row, 9, logger, ",");
//					String cityName = ReadFileTool.getString(row, 10, logger);
//					c.setBossId(id);
//					c.setMapName(mapName);
//					c.setX(x);
//					c.setY(y);
//					c.setBossIcon(bossIcon);
//					c.setBossName(bossName);
//					c.setPlayerLevel(playerLevel);
//					c.setJiazuLevel(jiazuLevel);
//					c.setRewardNames(rewardName);
//					c.setRewardNums(rewardNums);
//					c.setCityName(cityName);
//					hjConfigs.add(c);
//					logger.warn("[幻境单人副本配置] ["+c+"]");
//				}catch(Exception e){
//					throw new Exception("[petBlessExpMap] [异常] [行："+i+"]",e);
//				}
//			}
//		}
		
		
	}
	
	public Map<Integer,Double> petBasicExpMap = new HashMap<Integer, Double>();
	public Map<Integer,Double> petOtherExpMap = new HashMap<Integer, Double>();
	public Map<Integer,Integer> petBlessExpMap = new HashMap<Integer, Integer>();
	public List<HuanJingSelfConfig> hjConfigs = new ArrayList<HuanJingSelfConfig>();
	
	private PracticeModel getPracticeModel(HSSFRow row) throws Exception {
		PracticeModel pm = new PracticeModel();
		int index = 0;
		pm.setSkillId(ReadFileTool.getInt(row, index++, logger));
		pm.setSkillName(ReadFileTool.getString(row, index++, logger));
		pm.setMaxLevel(ReadFileTool.getInt(row, index++, logger));
//		pm.setCostPracticeNum(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		pm.setExp4next(ReadFileTool.getLongArrByString(row, index++, logger, ",", 0));
		pm.setCostSiliverType(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		String temp = ReadFileTool.getString(row, index++, logger);
		pm.setAddAttrType(MagicWeaponConstant.mappingValueKey.get(temp));
		pm.setAddType(ReadFileTool.getInt(row, index++, logger));
		pm.setAddNum(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		pm.setDescription(ReadFileTool.getString(row, index++, logger));
		pm.setIcon(ReadFileTool.getString(row, index++, logger));
		pm.setWeaponShopLevel(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		pm.setArmorShopLevel(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		if (pm.getMaxLevel() != pm.getExp4next().length || pm.getMaxLevel() != pm.getCostSiliverType().length
				|| pm.getMaxLevel() != pm.getAddNum().length || pm.getMaxLevel() != pm.getWeaponShopLevel().length || pm.getMaxLevel() != pm.getArmorShopLevel().length) {
				throw new Exception("[配置错误] [pm.getMaxLevel(): " + pm.getMaxLevel() + "] [pm.getExp4next().length : " 
					+ pm.getExp4next().length + "] [pm.getCostSiliverType().length: " + pm.getCostSiliverType().length + "] ["
					+ "] [pm.getAddNum().length: " + pm.getAddNum().length + "] [pm.getWeaponShopLevel().length: " + pm.getWeaponShopLevel().length 
					+ "] [pm.getArmorShopLevel().length: " + pm.getArmorShopLevel().length + "]");
		}
		return pm;
	}
	
	/**
	 * 增加次数活动可以在此加
	 * @param vipLevel
	 * @return
	 */
	public int getQifuTimesByVip(byte vipLevel) {
		if (qifuTimes.get(vipLevel) == null) {
			logger.warn("[新家族] [根据vip等级获取每天可祈福的次数] [异常] [vipLevel:" + vipLevel + "]");
			return 0;
		}
		int times = qifuTimes.get(vipLevel);
		return times;
	}
	
	public int [] opweek1 = {3,5};
	public int openHour = 19;
	public int [] openTime = {20,30};
	public boolean isOpen(){
		Calendar cl = Calendar.getInstance();
		int week = cl.get(Calendar.DAY_OF_WEEK);
		if(week == opweek1[0] || week == opweek1[1]){
			int hour = cl.get(Calendar.HOUR_OF_DAY);
			if(hour == openHour){
				int minute = cl.get(Calendar.MINUTE);
				if(minute >= openTime[0] && minute < openTime[1]){
					return true;
				}
			}
		}
		return false;
	}
	
	boolean hasNotice = false;
	int lastNoticeMinute = 0;
	boolean updateB = false;
	public void noticePlayer(){
		try {
			if(isOpen()){
				if(!hasNotice){
					updateB = false;
					hasNotice = true;
					ChatMessage msg = new ChatMessage();
					msg.setMessageText(Translate.家族远征活动开启通知);
					JiazuManager2.damages.clear();
					JiazuManager.getInstance().清理远征排行();
					try {
						ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
						Game.logger.warn("[家族远征] [通知活动现在开启] ["+Translate.家族远征活动开启通知+"]");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				if(!updateB){
					updateB = true;
					YuanZhenBillboard bb = (YuanZhenBillboard)BillboardsManager.getInstance().getBillboard("家族远征", "远征排行");
					try {
						bb.update();
						logger.warn("[家族远征] [活动结束更新排行] [成功]");
					} catch (Exception e) {
						e.printStackTrace();
						logger.warn("[家族远征] [活动结束更新排行] [异常]",e);
					}
				}
				BossCityManager.getInstance().yuanzhenGuLi.clear();
				hasNotice = false;
			}
			Calendar cl = Calendar.getInstance();
			int week = cl.get(Calendar.DAY_OF_WEEK);
			if(week == opweek1[0] || week == opweek1[1]){
				int hour = cl.get(Calendar.HOUR_OF_DAY);
				if(hour == openHour){
					if(lastNoticeMinute == 0){
						int minute = cl.get(Calendar.MINUTE);
						if(minute + 1 == openTime[0]){
							lastNoticeMinute = minute;
							JiazuManager2.damages.clear();
							String mess = Translate.translateString(Translate.家族远征活动即将开启通知, new String[][] { { Translate.COUNT_1, String.valueOf(1)}});
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(mess);
							try {
								ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
								Game.logger.warn("[家族远征] [通知活动即将开启] ["+mess+"]");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}else{
					lastNoticeMinute = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFileName2() {
		return fileName2;
	}

	public void setFileName2(String fileName2) {
		this.fileName2 = fileName2;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<Long, List<Long>> getPlayerTasks() {
		return playerTasks;
	}

	public void setPlayerTasks(Map<Long, List<Long>> playerTasks) {
		this.playerTasks = playerTasks;
	}

}
