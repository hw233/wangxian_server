package com.fy.engineserver.activity.fairyBuddha.challenge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;

public class FairyChallengeManager implements EventProc{
	public static Logger logger = FairyBuddhaManager.logger;
	
	private static FairyChallengeManager instance;
	/** 开启线程个数 */
	public static int threadNum = 5;
	/** 仙尊挑战线程list */
	public List<FairyChallengeThread> threads = new ArrayList<FairyChallengeThread>();
	/** 挑战仙尊的地图 */
	public static List<String> mapNames = new ArrayList<String>();					//init中把此地图的名字都加到list中
	/** 玩家进入出生点 */
	public static int[] bornPoint = new int[]{932, 621};
	/** boss出生点 */
	public static int[] bornPoint4Boss = new int[]{560, 372};
	/** bossid */
	public static int[] bossIds = new int[]{20113244, 20113244, 20113247, 20113245, 20113246, 20113355};
	/** 怪物属性与仙尊属性比率   外攻、内攻、血 */
	public static final double[] mul4Attr = new double[]{1.5, 1.5, 1.5};
	/** 哪天不能挑战 */
	public static int[] unOpenDays = new int[]{};
	
	private volatile int currentNum = 0;
	
	public Object lock = new Object();
	
	public static FairyChallengeManager getInst() {
		return instance;
	}
	
	public void init() {
		instance = this;
		this.doReg();
//		for(int i=0; i<threadNum; i++) {
//			FairyChallengeThread fc = new FairyChallengeThread();
//			fc.setName("仙尊挑战线程-" + i);
//			fc.start();
//			threads.add(fc);
//		}
		mapNames.add("mishi");
	}
	public boolean isFairyChallengeMap(String mapName) {
		return mapNames.contains(mapName);
	}
	
	public Game getChallengeGmae(Player player) {
		for(FairyChallengeThread fc : threads) {
			if(fc.isPlayerAtThread(player)) {
				return fc.getChallengeGmae(player);
			}
		}
		return null;
	}
	public byte getChallengeResult(Player player) {
		for(FairyChallengeThread fc : threads) {
			if(fc.isPlayerAtThread(player)) {
				return fc.getChallengeResult(player);
			}
		}
		return 0;
	}
	/**
	 * 得到正在挑战仙尊的人
	 * @param career
	 * @return
	 */
	public int getChallengerByCareer(byte career) {
		int num = 0;
		for(FairyChallengeThread fc : threads) {
			for(FariyChallenge fcc : fc.getGameList()) {
				if(fcc.getCareer() == career) {
					num++;
				}
			}
		}
		return num;
	}
	
	/**
	 * 开始调整仙尊
	 * @param player
	 */
	public synchronized void startChallenge(Player player, String gameName, int[] notOpenDays) {
		String result = this.verificationData(player, gameName,notOpenDays);
		int electorNum = FairyBuddhaManager.instance.getElectorNumByCareer(player.getCurrSoul().getCareer());
		int challengerNum = this.getChallengerByCareer(player.getCurrSoul().getCareer());
		if(result == null && (electorNum + challengerNum) >= FairyBuddhaManager.MAX_ELECTORS) {
			result = Translate.挑战者已达上限;
		}	
		if(result == null) {
			if(!mapNames.contains(gameName)) {
				mapNames.add(gameName);
			}
			add2Thread(player, gameName);
		} else {
			player.sendError(result);
			if(logger.isDebugEnabled()) {
				logger.debug("[仙尊挑战] [不允许挑战] [判定结果:" + result + "][" + player.getLogString() + "]");
			}
		}
	}
	/**
	 * 挑战验证
	 * @param player
	 * @return
	 */
	public String verificationData(Player player, String gameName, int[] notOpenDays) {
		String result = null;
		if(gameName == null || gameName.isEmpty()) {
			return Translate.地图名为空;
		}
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		for(int i=0; i<notOpenDays.length; i++) {
			if(notOpenDays[i] == day) {
				return Translate.挑战时间已过;
			}
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity == null || entity.getFeisheng() != 1) {
			return Translate.需要飞升才可以挑战;
		}
		if (player.getTeamMark() != Player.TEAM_MARK_NONE) {
			return Translate.组队状态不可挑战仙尊;
		}
		//判断是否已经挑战过
		FairyBuddhaManager fbm = FairyBuddhaManager.instance;
		Map<Byte, List<FairyBuddhaInfo>> electorMap = fbm.getCurrentElectorMap(fbm.getKey(0)+fbm.KEY_参选者);
		for(List<FairyBuddhaInfo> tl : electorMap.values()){
			for(FairyBuddhaInfo fb : tl) {
				if(fb.getId() == player.getId()) {
//					String str1 = CareerManager.careerNames[fb.getCareer()];
//					String dess = Translate.translateString(Translate.已挑战过2, new String[][] { { Translate.STRING_1, str1 } });
					String dess = Translate.已挑战过2;
					return dess;
				}
			}
		}
		int num = FairyBuddhaManager.instance.getElectorNumByCareer(player.getCurrSoul().getCareer());
		if(num >= FairyBuddhaManager.MAX_ELECTORS) {
			return Translate.竞选者已达上限;
		}
		return result ;
	}
	
	public void add2Thread(Player player, String gameName) {
		Game game = this.createNewGame(player, gameName);
		if (game != null) {
			synchronized (lock) {
				if(currentNum >= threads.size()) {
					currentNum = 0;
				}
				threads.get(currentNum++).notifyStartChallenge(game, player);
			}
		}
	}
	
	public Game createNewGame(Player player , String mapname){
		try{
			GameManager gameManager = GameManager.getInstance();
			GameInfo gi = gameManager.getGameInfo(mapname);
			if(gi == null){
				if(logger.isWarnEnabled())
					logger.warn("[仙尊挑战] [创建渡劫场景失败] [对应的地图信息不存在][玩家:{}][{}][{}]", new Object[]{player.getName(),player.getId(),mapname});
				return null;
			}
			Game newGame = new Game(gameManager,gi);
			try {
				newGame.init();
			} catch (Exception e) {
				logger.error("[仙尊挑战] [初始化场景异常][e:" + e + "]");
				e.printStackTrace();
			}
			return newGame; 
		}catch(Exception e){
			logger.error("[仙尊挑战] [创建渡劫场景异常][e:" + e + "]");
		}
		return null;
	}
	
	/**
	 * 怪物死亡通知
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if(monster != null/* && monster.getOwner() != null*/) {
			for(FairyChallengeThread ft : threads) {
				if (ft.isPlayerAtThread(monster.getOwnerId())) {
					ft.notifyMonsterKilled(monster);
					break;
				}
			}
		} else {
			if(logger.isWarnEnabled()) {
				logger.warn("[仙尊挑战] [有怪物被杀死通知,异常][monster:" + monster + "][info:" + monster + "]");
			}
		}
	}
	/**
	 * 玩家上线--如果下线时正在调整仙尊。自动算失败
	 * @param player
	 */
	public void notifyPlayerLogin(Player player) {
		if(player != null) {
			for(FairyChallengeThread ft : threads) {
				if (ft.isPlayerAtThread(player)) {
					ft.notifyPlayerDead(player, (byte) -2);
					break;
				}
			}
		} else {
			if(logger.isWarnEnabled()) {
				logger.warn("[仙尊挑战] [玩家上线通知,异常][" + player + "]");
			}
		}
	}
	
	public void notifyPlayerUseTransProp(Player player) {
		if(player != null) {
			for(FairyChallengeThread ft : threads) {
				ft.notifyPlayerUseTransProp(player);
			}
		}
	}
	/**
	 * 到时间清除所有挑战者
	 */
	public void cleanAllChallenger() {
		for(FairyChallengeThread thread : threads) {
			thread.cleanAllChallenger();
		}
	}
	/**
	 * 根据传入player设置怪物的avatar等(怪物属性、血量等如何设置需确认)
	 * @param game
	 * @param player
	 * @param x
	 * @param y
	 * @param career  当前挑战者职业
	 * @return
	 */
	public Monster refreshMonster(Game game , int x, int y, int monsterId, byte career) {
		if(game == null || monsterId <= 0) {
			if(logger.isWarnEnabled()) {
				logger.warn("[仙尊] [刷怪异常] [传入game:" + game + "] [monsterId :" + monsterId + "]");
			} 
			return null;
		}
		Monster sprite = null;
		Player player = null;
		String sName = "";
		int sLevel = 0;
		String avatarRace = "";
		String[] avatar = null;
		String avatarSex = "";
		byte[] avatarType = null;
		int phyAttack = 0;
		int magicAttack = 0;
		int hp = 0;
		try {
			//此处需要修改为获取仙尊(需要获取仙尊的名字-对应元神的等级-以及当时的avatar)
			FairyBuddhaInfo fbi = FairyBuddhaManager.instance.getFairyBuddhaMap().get(career);
			if(fbi != null) {
				player = GamePlayerManager.getInstance().getPlayer(fbi.getId());
				sName = player.getName();
				sLevel = fbi.getLevel();
				avatarRace = fbi.getStatue().getAvataRace();
				avatar = fbi.getStatue().getAvatas();
				avatarSex = fbi.getStatue().getAvataSex();
				avatarType = fbi.getStatue().getAvataType();
				phyAttack = fbi.getStatue().getPhyAttack();
				magicAttack = fbi.getStatue().getMagicAttack();
				if (fbi.getStatue().getMaxHP() > 0) {
					hp = fbi.getStatue().getMaxHP();
				} else {
					logger.warn("[仙尊挑战] [取出当前仙尊血量为负] [hp:" + fbi.getStatue().getMaxHP() + "] [" + player.getLogString() + "]");
				}
//				logger.error("");
			}
		} catch (Exception e) {
			logger.error("[仙尊] [查找当前仙尊信息出错] [career:" + career + "]", e);
		}
		try {
			sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
			sprite.setX(x);
			sprite.setY(y);
			sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
			if(player != null) {
				sprite.setName(sName);
				sprite.setLevel(sLevel);
				sprite.setAvataRace(avatarRace);
				sprite.setAvata(avatar);
				sprite.setAvataSex(avatarSex);
				sprite.setAvataType(avatarType);
				sprite.setPhyAttack((int)(phyAttack * mul4Attr[0]));
				sprite.setMagicAttack((int) (magicAttack * mul4Attr[1]));
				if (hp > 0) {
					sprite.setMaxHP((int) (hp * mul4Attr[2]));
					sprite.setHp(sprite.getMaxHP());
				}
				if(logger.isDebugEnabled()) {
					logger.debug("[仙尊挑战] [刷怪加倍数] [成功] [传入game:" + game + "] [monsterId :" + monsterId + "]");
				}
			}
			game.addSprite(sprite);
			if(logger.isDebugEnabled()) {
				logger.debug("[仙尊挑战] [刷怪] [成功] [传入game:" + game + "] [monsterId :" + monsterId + "]");
			} 
		} catch (Exception e) {
			logger.error("[仙尊] [刷出挑战怪异常] [怪物id:" + monsterId + "]", e);
		}
		return sprite;
	}
	/**
	 * 玩家是否正在参加仙尊挑战 
	 * @param player
	 * @return
	 */
	public boolean isPlayerChallenging(Player player) {
		boolean result = false;
		try {
			for(FairyChallengeThread ft : threads) {
				if(ft.isPlayerAtThread(player)) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("[仙尊挑战] [判断player是否正在挑战仙尊:" + player.getLogString() + "]", e);
		}
		return result ;
	}
	public boolean isPlayerChallenging(long playerId) {
		boolean result = false;
		try {
			for(FairyChallengeThread ft : threads) {
				if(ft.isPlayerAtThread(playerId)) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("[仙尊挑战] [判断player是否正在挑战仙尊:" + playerId + "]", e);
		}
		return result ;
	}
	
	
	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		Object[] obj = null;
		EventWithObjParam evtWithObj = null;
		switch (evt.id) {
		case Event.MONSTER_KILLED_Simple:
			evtWithObj = (EventWithObjParam) evt;
			obj = (Object[]) evtWithObj.param;
			SimpleMonster monster = (SimpleMonster) obj[0];
			if (monster.isBoss()) {
				this.notifyMonsterKilled(monster);
				SealManager.getInstance().handleBossDead(monster);
			}
			break;
		case Event.PLAYER_LOGIN: 
//			if(evt instanceof EventOfPlayer) {
//				EventOfPlayer e = (EventOfPlayer) evt;
//				this.notifyPlayerLogin(e.player);
//			}
			break;
		case Event.FAIRY_CHALLENGE_RESULT:
			evtWithObj = (EventWithObjParam) evt;
			obj = (Object[]) evtWithObj.param;
			Player player = (Player) obj[0];
			byte result = (Byte) obj[1];
			int soulType = (Integer) obj[2];
			logger.error("[挑战结束][" + player.getLogString() + "][result : " + result + "][soulType : " + soulType + "]");
			break;
		case Event.SERVER_DAY_CHANGE:
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			boolean flag = false;
			for(int i=0; i<unOpenDays.length; i++) {
				if(unOpenDays[i] == day) {
					flag = true;
					break;
				}
			}
			if(flag) {
				cleanAllChallenger();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.MONSTER_KILLED_Simple, this);
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
//		EventRouter.register(Event.PLAYER_LOGIN, this);
//		EventRouter.register(Event.FAIRY_CHALLENGE_RESULT, this);
	}
}
