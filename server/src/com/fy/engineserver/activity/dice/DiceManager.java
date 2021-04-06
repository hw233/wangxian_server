package com.fy.engineserver.activity.dice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.activity.dice.city.DiceGame;
import com.fy.engineserver.activity.dice.config.DiceActivityConfig;
import com.fy.engineserver.activity.dice.config.DiceActivityWeekConfig;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.DICE_SIGN_UP_SURE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.DoorNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.ServiceStartRecord;
/**
 * 骰子迷城
 * 
 * @create 2016-3-9 上午10:51:54
 */
public class DiceManager implements Runnable, AbstractActivity{

	private static DiceManager self;

	public static Logger logger = ActivitySubSystem.logger;
	
	public static boolean START_RUN = true;
	
	public static long sleeptime = 200;
	
	public static boolean printTestDiceThreadLog = false;
	
	public long startNoticeTime = 0;
	
	//报名持续时间
	public static int SIGN_LAST_TIME = 3;
	
	public static int MAX_JOIN_NUM = 300;
	
	//活动状态
	public ActivityState activityStat = ActivityState.READY;
	
	public int currState;
	
	//准备战斗
	public static final int READY_FIGHT = 2;
	//活动开始
	public static final int START_FIGHT = 3;
	
	//活动开启条件
	public List<DiceActivityConfig> timeConfig = new ArrayList<DiceActivityConfig>();
	
	/**
		记录玩家当前打的层数，来判断是否允许玩家上线
		依据:如果不是当前层，不让上线，
		 如果是当前层，boss数量大于0不让上线
	*/
	public Map<Long, Integer> rewardLayer = new HashMap<Long, Integer>();
	
	//报名信息
	//private SignInfo signInfo;
	
	//报名玩家，不考虑临时维护影响
	public List<Long> signList = new ArrayList<Long>();
	
	//当前生效的活动配置
	public DiceActivityConfig currConfig = new DiceActivityWeekConfig(Calendar.WEDNESDAY,21,30);
	
	private int limitLevel = 111;
	
	public static long DICE_LAST_TIME = 22*60*1000;
	
	//地图信息
	public String mapName = "touxianmicheng";
	public int xypiont[] = new int[]{1704,1567};
	
	public static long winExps = 12000000L;
	public String winArticles [] = {"人物经验","古董","魂石宝箱"};
	public int winNums [] = {1,1,1};
	
	private int maxBossNum = 8;
	private static int[] boosIds= {20113394, 20113394, 20113394, 20113394, 20113394, 20113394, 20113394, 20113394};
	private static int bossXYPiont[] [] = new int[8][];
	static{
		bossXYPiont[0] = new int[]{665,613};
		bossXYPiont[1] = new int[]{1717,644};
		bossXYPiont[2] = new int[]{2849,713};
		bossXYPiont[3] = new int[]{548,1589};
		bossXYPiont[4] = new int[]{2852,1511};
		bossXYPiont[5] = new int[]{565,2526};
		bossXYPiont[6] = new int[]{1730,2578};
		bossXYPiont[7] = new int[]{2824,2410};
	}
	
	public static Map<Integer,Long> expDatas = new HashMap<Integer,Long>();
	static{
		expDatas.put(1, 4000000L);
		expDatas.put(2, 5000000L);
		expDatas.put(3, 6000000L);
		expDatas.put(4, 7000000L);
		expDatas.put(5, 8000000L);
		expDatas.put(6, 9000000L);
		expDatas.put(7, 10000000L);
		expDatas.put(8, 11000000L);
		expDatas.put(9, 12000000L);
	}
	
	//罩子npc
	private int doorNpcid = 610005;
	private int doorNpcX = 1704;
	private int doorNpcY = 1567;
	
	//当前正在进行的副本
	DiceGame diceGame;
	
	public List<Long> rewardIds = new ArrayList<Long>();
	
	public static int x = 1818;
	public static int y = 961;
	public static String tranMapName = "shenghunlingyu";
	
	public static DiceManager getInstance(){
		return self;
	}

	public void init(){
		
		self = this;
		initConfig();
		new Thread(this,"骰子副本").start();
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 配置活动开始时间
	 * eg:每周三21:30-22:00，22:00-22:30
	 *    每周五21:30-22:00，22:00-22:30
	 */
	public void initConfig(){
//		DiceActivityConfig config = new DiceActivityWeekConfig(Calendar.THURSDAY,19,00);
//		DiceActivityConfig config2 = new DiceActivityWeekConfig(Calendar.THURSDAY,19,30);
//		DiceActivityConfig config3 = new DiceActivityWeekConfig(Calendar.THURSDAY,20,00);
//		DiceActivityConfig config4 = new DiceActivityWeekConfig(Calendar.THURSDAY,20,30);
		DiceActivityConfig config = new DiceActivityWeekConfig(Calendar.WEDNESDAY,21,30);
		DiceActivityConfig config2 = new DiceActivityWeekConfig(Calendar.WEDNESDAY,22,00);
		DiceActivityConfig config3 = new DiceActivityWeekConfig(Calendar.FRIDAY,21,30);
		DiceActivityConfig config4 = new DiceActivityWeekConfig(Calendar.FRIDAY,22,00);
		timeConfig.add(config);
		timeConfig.add(config2);
		timeConfig.add(config3);
		timeConfig.add(config4);
	}
	
	/**
	 * 活动是否开启,指是否可以报名
	 */
	public boolean isStartActivity(){
		for(DiceActivityConfig c : timeConfig){
			if(c != null && c.isStart()){
				currConfig = c;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通知活动开启
	 */
	public void noticeActivityStart(){
		try {
			startNoticeTime = System.currentTimeMillis() + SIGN_LAST_TIME * 60 * 1000;
			ChatMessage msg = new ChatMessage();
			msg.setMessageText(Translate.骰子活动开启通告信息);
			ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
			if(logger.isWarnEnabled()){
				logger.warn("[骰子仙居] [通知活动开启] [成功] [当前状态:"+activityStat+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[骰子仙居] [通知活动开启] [异常]",e);
		}
	}
	
	public Game createDiceGame(String mapname){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapname);
		if(gi == null){
			logger.error("[骰子仙居] [创建迷城副本] [失败:对应的地图信息不存在] [{}]", new Object[]{mapname});
			return null;
		}
		Game newGame = new Game(gameManager,gi);
		try {
			newGame.init();
			diceGame = new DiceGame(newGame);
			if(logger.isWarnEnabled()){
				logger.warn("[骰子仙居] [创建迷城副本] [成功] [{}]", new Object[]{mapname});
			}
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[骰子仙居] [创建迷城副本] [失败:game初始化失败] [{}] [{}]", new Object[]{mapname,e});
			return null;
		}
	}
	
	public void createDoorNpc(Game game){
		MemoryNPCManager mmm = (MemoryNPCManager)com.fy.engineserver.sprite.npc.MemoryNPCManager.getNPCManager();
		CaveDoorNPC m = (CaveDoorNPC)mmm.createNPC(doorNpcid);
		if(m != null){
			m.setX(doorNpcX);
			m.setY(doorNpcY);
			m.setBornPoint(new Point(doorNpcX,doorNpcY));
			m.setAlive(true);
			game.addSprite(m);
			m.closeDoor(game);
			if(logger.isWarnEnabled()){
				logger.warn("[骰子仙居] [刷门NPC] [成功] [id:{}] [name:{}]", new Object[]{doorNpcid,m.getName()});
			}
		}else{
			if(logger.isWarnEnabled()){
				logger.warn("[骰子仙居] [刷门NPC] [失败] [对应的npc{}不存在]", new Object[]{doorNpcid});
			}
		}
	}
	
	/**
	 * 根据玩家数量刷新boss
	 * @param game
	 * @param playerNums
	 */
	public void refreshBoss(Game game,int bossNums){
		if(bossNums > maxBossNum){
			bossNums = maxBossNum;
		}
		for(int i=0;i<bossNums;i++){
			int bossId = boosIds[i];
			int xys [] = bossXYPiont[i];
			Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			if (sprite != null && game != null) {
				sprite.setX(xys[0]);
				sprite.setY(xys[1]);
				sprite.setBornPoint(new com.fy.engineserver.core.g2d.Point(xys[0], xys[1]));
				game.addSprite(sprite);
				if (logger.isWarnEnabled()) {
					logger.warn("[骰子仙居] [刷新boss] [成功] [boss:{}] [bossid:{}] [bossNums:{}] [x:{}] [y:{}]", new Object[] { sprite.getName(), bossId,bossNums,xys[0],xys[1] });
				}
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[骰子仙居] [刷新boss] [失败boss不存在:{}] [bossid:{}] [bossNums:{}]", new Object[] { (sprite == null ? "sprite==null" : "game==null"), bossId,bossNums });
				}
			}
		}
	}
	
	public void openDoor(Game game) {
		if(game == null){
			return;
		}
		LivingObject los[] = game.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof DoorNpc){
				DoorNpc dn = (DoorNpc)los[i];
				if(dn != null){
					dn.openDoor(game);
				}
				if(logger.isWarnEnabled()){
					logger.warn("[骰子仙居] [执行开门] [{}] [{}]", new Object[]{(game.gi != null ?game.gi.name:""),dn!=null?dn.getName():"null"});
				}
			}
		}
	}
	
	
	public Game getDiceGame(Player player,String mName){
		if(mName != null && mName.equals(mapName)){
			if(diceGame != null){
				Integer layer = rewardLayer.get(player.getId());
				if(activityStat != ActivityState.READY){
					if(diceGame.containPlayer(player)){
						if(layer != null){
							if(diceGame.getCurrLayer() == layer){
								if(DiceManager.getInstance().activityStat == ActivityState.START_SIGN){
									if(logger.isWarnEnabled()){
										logger.warn("[骰子仙居] [满足条件:同层状态是开始报名] [状态:"+activityStat+"] [mName:"+mName+"] [layer:"+layer+"] ["+player.getLogString()+"]");
									}
									return diceGame.getGame();
								}else{
									if(diceGame.getBossNums() != 0){
										if(logger.isWarnEnabled()){
											logger.warn("[骰子仙居] [满足条件:同层且boss还存在] [状态:"+activityStat+"] [mName:"+mName+"] [layer:"+layer+"] [bossNUms:"+diceGame.getBossNums()+"] ["+player.getLogString()+"]");
										}
										return diceGame.getGame();
									}
								}
							}
						}
					}
				}
				if(logger.isWarnEnabled()){
					logger.warn("[骰子仙居] [条件不满足] [是否在地图:"+diceGame.containPlayer(player)+"] [mName:"+mName+"] [玩家所在层:"+layer+"] [当前游戏进行到的层:"+diceGame.getCurrLayer()+"] [当前状态:"+activityStat+"] [bossNUms:"+diceGame.getBossNums()+"] ["+player.getLogString()+"]");
				}
			}
		}
		
		return null;
	}
	
	public boolean isDiceGame(Player player){
		Game game = player.getCurrentGame();
		if(game != null && game.gi != null){
			String mName = game.gi.name;
			if(mName != null && mName.equals(mapName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 报名参加活动
	 */
	public void signUpActivity(Player player){
		if(player.getLevel() < limitLevel){
			player.sendError(Translate.角色等级不足);
			return;
		}
		if(currConfig == null){
			player.sendError(Translate.骰子活动报名还没开始);
			return;
		}
		if(!currConfig.isStart()){
			player.sendError(Translate.报名时间已经结束);
			return;
		}
		if(signList.contains(player.getId())){
			player.sendError(Translate.已经报名);
			return;
		}
		if(signList.size() >= MAX_JOIN_NUM){
			player.sendError(Translate.报名人数已达上限);
			return;
		}
		if(System.currentTimeMillis() > startNoticeTime){
			player.sendError(Translate.活动报名时间已经结束);
			return;
		}
		if(activityStat != ActivityState.START_SIGN){
			player.sendError(Translate.活动报名时间已经结束+"!");
			return;
		}
		signList.add(player.getId());
		
		if(player.getCurrentGame() != null){
			if(diceGame != null){
				diceGame.addDiceGame(player);
			}
			player.setPkMode(Player.和平模式);
			rewardLayer.put(player.getId(), 1);
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, mapName, xypiont[0], xypiont[1]));
		}
		DICE_SIGN_UP_SURE_RES res = new DICE_SIGN_UP_SURE_RES(GameMessageFactory.nextSequnceNum(), true);
		player.addMessageToRightBag(res);
		if(logger.isWarnEnabled()){
			logger.warn("[骰子仙居] [报名] [成功] [当前报名人数:{}] [层:{}] [{}]",new Object[]{signList.size(),rewardLayer.get(player.getId()), player.getLogString()});
		}
	}
	
	public void cancelSignUp(Player p){
		if(p == null){
			return;
		}
		if(signList.contains(p.getId())){
			signList.remove(p.getId());
			p.sendError(Translate.取消报名成功);
			DICE_SIGN_UP_SURE_RES res = new DICE_SIGN_UP_SURE_RES(GameMessageFactory.nextSequnceNum(), true);
			p.addMessageToRightBag(res);
		}
	}
	
	/**
	 * 刷新的boss数量
	 * 怪物数=求整(幂(人数,2))
	 * @param playerNum 当前人数
	 * @return
	 */
	public int getBossNum(int playerNum){
		return (int)(Math.log(playerNum) / Math.log(2.0));
	}

	public DiceGame getDiceGame() {
		return diceGame;
	}

	public void setDiceGame(DiceGame diceGame) {
		this.diceGame = diceGame;
	}
	
	@Override
	public void run() {
		while(DiceManager.START_RUN){
			try {
				Thread.sleep(DiceManager.sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				if(diceGame == null){
					Game game = createDiceGame(mapName);
					if(game == null){
						logger.warn("[骰子仙居] [创建副本异常] [退出]");
						break;
					}
					if(diceGame == null){
						
					}
				}else{
					if(activityStat == ActivityState.READY){
						if(isStartActivity()){
							if(System.currentTimeMillis() > startNoticeTime){
								activityStat = ActivityState.START_SIGN;
								noticeActivityStart();
								diceGame.clearGameInfo("新的活动开启");
							}
						}
					} 
					diceGame.heartbeat();
				}
			}catch(Exception e){
				if(logger.isWarnEnabled()){
					e.printStackTrace();
					logger.warn("[骰子副本心跳线程] [异常]",e);
				}
			}
		}
	}
	
	public void playerEnter(Player p){
		if(diceGame != null){
			if(diceGame.containPlayer(p) == false){
				return;
			}
		}
		if(activityStat == ActivityState.START_SIGN){
			if(currConfig != null){
				int overTime = (int)((currConfig.getEndTime() - System.currentTimeMillis())/1000);
				if(overTime > 0){
					NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
					resp.setCountdownType(CountdownAgent.COUNT_TYPE_DICE);
					resp.setLeftTime(overTime);
					resp.setDescription(Translate.刷boss倒计时);
					p.addMessageToRightBag(resp);
					if(logger.isWarnEnabled()){
						logger.warn("[骰子仙居] [玩家上线通知boss倒计时] [overTime:"+overTime+"] ["+p.getLogString()+"]"); 
					}
				}
			}
		}else if(activityStat == ActivityState.START_FIGHT){
			if(diceGame != null){
				int overTime = (int)((diceGame.fightLastTime - System.currentTimeMillis())/1000);
				if(overTime > 0){
					NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
					resp.setCountdownType((byte)50);
					resp.setLeftTime(overTime);
					resp.setDescription(Translate.副本倒计时);
					p.addMessageToRightBag(resp);
					if(logger.isWarnEnabled()){
						logger.warn("[骰子仙居] [玩家上线通知副本倒计时] [overTime:"+overTime+"] ["+p.getLogString()+"]"); 
					}
				}
			}
		}
	}
	
	public void playerLeave(Player p){
		if(diceGame != null){
			if(diceGame.ids.contains(p.getId())){
				diceGame.ids.remove(p.getId());
			}
			if(signList.contains(p.getId())){
				signList.remove(p.getId());
			}
			if(diceGame.notices.contains(p.getId())){
				diceGame.notices.remove(p.getId());
			}
		}
	}
	
	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.骰子仙居;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		int w = calendar.get(Calendar.DAY_OF_WEEK);
		for(DiceActivityConfig c : timeConfig){
			if (c instanceof DiceActivityWeekConfig) {
				DiceActivityWeekConfig d = (DiceActivityWeekConfig) c;
				if (d.getDayOfWeek() == w) {
					result.add(d.getHourOfDay() + ":" + ActivityDataManager.getMintisStr(d.getMinute()));
				}
			}
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		return this.isStartActivity();
	}

}
