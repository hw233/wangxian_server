package com.fy.engineserver.carbon.devilSquare.instance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.fy.engineserver.carbon.devilSquare.DevilSquareConstant;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.carbon.devilSquare.model.DsBornPoint;
import com.fy.engineserver.carbon.devilSquare.model.DsPlayerDataModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.DEVILSQUARE_COUNTDOWNTIME_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;

public class DevilSquare{
	public static Logger logger = DevilSquareManager.logger;
	/** 副本game */
	public Game game;
	/** 当前副本状态*/
	private byte stauts = DevilSquareConstant.UNSTART;
	/** 上次副本状态改变时间 */
	public long lastStatusChangeT;
	/** 副本内玩家集合*/
	public List<Long> playerList = new ArrayList<Long>();
	/** 最大人数限制 */
	public int maxNumbers;
	/** 副本开启时间(从传送门开启计算) */
	public long openTime;
	/** 传送门持续时间 */
	public long lastTime;
	/** 副本持续时间 */
	public long carbonLastTime;			//到此时间后不管还有多少只怪物都直接结束副本，等待时间传送出副本
	/** 副本结束后多久传送出副本 */
	public long kickoutTime;
	/** 当前副本进度（怪物刷新到第几波） */
	public int refreashTimes = 0;			//每次刷怪后需要更新此值，判断是否已经到达boss--boss前需要清楚所有小怪
	/** 上次刷怪时间 */
	public long lastRefreashTime;
	/** 是否到了boss时间 */
	public boolean isBossTime = false;
	/** 出生点集合 */
	public List<Integer[]> bornPoints;
	/** 需要传送出场景的玩家列表 */
	public Set<Long> backTownPlayers = new HashSet<Long>();
	/** 副本开启后玩家准备时间 */
	public long prepareTime;
	/** 最终bossId,方便判断副本结束 */
	public int bossId;
	/** 副本中最多存在的怪物数量 */
	public int maxMonsterNum = 300;
	/** 第一次倒计时通知标志 */
	public boolean firstNoticeFlag = false;
	/** 副本中刷出来的需要无限复活的怪物列表 */
	public List<DsBornPoint> needReliveMonsters = new ArrayList<DsBornPoint>();
	/** 宝箱倒计时次数 */
	public int boxRefreashTimes = 0;
	/** 宝箱倒计时时间 */
	public List<Integer> intever4Box = new ArrayList<Integer>();
	/** 宝箱刷新标记 */
	public boolean isBoxAct = false;
	/** 倒计时map */
	public Map<Integer, Long> countMap = new ConcurrentHashMap<Integer, Long>();
	
	public Random random = new Random(System.currentTimeMillis());
	/** 副本中使用回城出副本的玩家列表 */
	public List<Long> outCarbonList = new ArrayList<Long>();
	/** 记录玩家行为，方便限制--目前只有原地复活的次数 */
	public Map<Long, DsPlayerDataModel> limitMap = new Hashtable<Long, DsPlayerDataModel>();
	/** 副本等级 */
	public int carbonLevel;
	
	public List<Integer> monsterIdList = new ArrayList<Integer>();
	
	
	private Object lock = new Object();
	
	public void heartBeat() {
		game.heartbeat();
		Player player = null;
		if(playerList == null) {
			return ;
		}
		long currentTime = System.currentTimeMillis();
		List<Long> rmList = new ArrayList<Long>();
		for(Long id : playerList) {
			try{
				player = GamePlayerManager.getInstance().getPlayer(id);
				if((!player.isOnline() && currentTime > (player.getLastRequestTime() + DevilSquareConstant.kick4NotOnlineTime))/* || player.isDeath()*/) {			//出副本
					rmList.add(id);
				}
			}catch (Exception e) {
				logger.error("[恶魔广场,检测玩家是否死亡或者下线] [playerId=" + id + "]");
			}
		}
		synchronized (playerList) {
			for(Long id : rmList) {			//维护副本中的玩家列表
				playerList.remove(id);
				backTownPlayers.add(id);
			}
			List<Long> rr = new ArrayList<Long>();
			for(Long id : outCarbonList) {
				playerList.remove(id);
				rr.add(id);
			}
			synchronized (outCarbonList) {
				for(Long id : rr) {
					outCarbonList.remove(id);
				}
			}
		}
		// 将玩家传送出副本
		backTownAlive();
		if(this.stauts == DevilSquareConstant.START) {
			long leftTime = currentTime - (openTime + carbonLastTime - kickoutTime);
			if(leftTime >= 0 && this.stauts != DevilSquareConstant.FINISH) {
				this.setStatus(DevilSquareConstant.FINISH);
				logger.warn("[恶魔城堡] [副本到时间自动结束] [currentTime:" + currentTime + "] [openTime:" + openTime + "] [carbonLastTime:" + carbonLastTime + "] [kickoutTime:" + kickoutTime + "] [carbonLevel:" + carbonLevel + "] [leftTime:" + leftTime +"]");
	//			notifyFinishCarbon();
			}
		}
		if(this.stauts == DevilSquareConstant.FINISH && playerList.size() > 0 && currentTime > (lastStatusChangeT + kickoutTime)) {
			notifyFinishCarbon();
		}
		if(this.stauts == DevilSquareConstant.UNSTART && this.playerList.size() > 0) {				//做容错，未开启的副本不应该有玩家存在
			notifyFinishCarbon();
		}
		setPVPLimit();
		if(this.stauts == DevilSquareConstant.START && intever4Box != null && intever4Box.size() > boxRefreashTimes) {
			int time = (int) ((openTime + intever4Box.get(boxRefreashTimes) * 1000 - currentTime) / 1000);
			if(time <= 10) {
				boxRefreashTimes++;
				isBoxAct = true;
				noticeAllPlayerCountTime(DevilSquareConstant.count_reftreashBox, time, Translate.天降宝箱倒计时);
			}
		}
		try{
			if(countMap.size() > 0) {
				if(countMap.get(DevilSquareConstant.count_refreashMonster) != null) {
					long time = countMap.get(DevilSquareConstant.count_refreashMonster);
					if(currentTime >= time) {
						noticeAllPlayerCountTime(DevilSquareConstant.count_refreashMonster, 30, Translate.下波怪物倒计时);
						countMap.remove(DevilSquareConstant.count_refreashMonster);
					}
				}
			}
		}catch(Exception e) {
			logger.error("[倒计时出错]", e);
		}
	}
	
	/**
	 * 使用回城通知
	 * @param p
	 */
	public void notifyUseTransProp(Player p) {
		if(playerList.contains(p.getId())) {
			outCarbonList.add(p.getId());
			if(logger.isInfoEnabled()) {
				logger.info("[恶魔广场] [玩家使用回城卷轴出副本] [" + p.getLogString() + "]");
			}
		}
	}
	/**
	 * 宝箱获得物品通知
	 * @param p
	 */
	public void notifyGetPropByBox(Player p, ArticleEntity ae, Props prop) {
		if(playerList.contains(p.getId())) {
			noticePlayerGetProp(p, ae, prop);
			if(logger.isInfoEnabled()) {
				logger.info("[恶魔广场] [通知玩家开宝箱得东西] [" + p.getLogString() + "][宝箱:" + prop.getName_stat() + "&" + prop.getColorType() + "][开出物品:" + ae.getArticleName() + "&" + ae.getColorType() + "]" );
			} 
		}
	}
	
	public void noticePlayerGetProp(Player player, ArticleEntity ae, Props prop) {
		try{
			Player p = null;
			List<Long> pl = playerList;
			String boxName = prop.getName();
			if(prop.getColorType() < DevilSquareConstant.colorString.length) {
				boxName = DevilSquareConstant.colorString[prop.getColorType()] + prop.getName() + "</f>";
			}
			String articleName = ae.getArticleName();
			if(ae.getColorType() < DevilSquareConstant.colorString.length) {
				articleName = DevilSquareConstant.colorString[ae.getColorType()] + ae.getArticleName() + "</f>";
			}
			String ct = Translate.translateString(Translate.恭喜玩家幸运开出物品, new String[][]{{Translate.PLAYER_NAME_1,player.getName()},{Translate.STRING_1,boxName},{Translate.STRING_2,articleName}});
			for(Long playerId : pl) {
				try{
					p = GamePlayerManager.getInstance().getPlayer(playerId);
					p.send_HINT_REQ(ct, (byte) 2);
					if(logger.isDebugEnabled()) {
						logger.debug("[恶魔广场] [通知玩家有人从宝箱获得物品] [" + p.getLogString() + "] [content:" + ct + "]");
					}
				} catch(Exception e) {
					logger.error("[恶魔广场] [通知玩家有人从宝箱获得物品] [" + p.getLogString() + "]", e);
				}
			}
		} catch(Exception e) {
			logger.error("[恶魔广场] [通知玩家有人从宝箱获得物品]", e);
		}
	}
	
	private void setPVPLimit() {
		long currentTime = System.currentTimeMillis();
		if(this.stauts == DevilSquareConstant.START && currentTime >= (this.lastStatusChangeT + prepareTime)) {	//副本正式启动刷怪，此时可以pk
			if(game.gi.isLimitPVP()) {
				game.gi.setLimitPVP(false);
			}
		} else if(this.stauts != DevilSquareConstant.FINISH) {												//副本没有正式开启前都不允许pk
			if(!game.gi.isLimitPVP()) {
				game.gi.setLimitPVP(true);
			}
		}
	}
	
	public void setStatus(byte st) {
		lastStatusChangeT = System.currentTimeMillis();		
		if(st == DevilSquareConstant.PREPARE || st == DevilSquareConstant.START) {		//重置刷怪次数--清怪
			this.refreashTimes = 0;
			this.notifyRemoveAllMonster();
			this.isBossTime = false;
			this.firstNoticeFlag = false;
			needReliveMonsters.clear();
			boxRefreashTimes = 0 ;
			isBoxAct = false;
			if(st == DevilSquareConstant.START) {							//准备就算开启时间？ 
				this.openTime = System.currentTimeMillis();
				this.noticeAllPlayerCountTime(DevilSquareConstant.count_allTime, (int)(carbonLastTime/1000), Translate.副本整体倒计时);
			}
		}
		if(st == DevilSquareConstant.FINISH) {
			noticeAllPlayerCountTime(DevilSquareConstant.count_exitCarbon, (int) (kickoutTime / 1000), Translate.副本结束倒计时);
			notifyRemoveAllMonster();
			rewardByScores();
			limitMap.clear();
		}
		this.stauts = st;
	}
	
	/**
	 * 副本结束发送奖励
	 */
	private void rewardByScores() {
		Iterator<Long> ite = limitMap.keySet().iterator();
		while(ite.hasNext()) {
			long playerId = ite.next();
			try {
				ArticleEntity ae = null;
				int index = 0;
				if(carbonLevel == 150) {
					index = 1;
				} else if(carbonLevel == 190) {
					index = 2;
				} else if(carbonLevel == 220) {
					index = 3;
				}
				DsPlayerDataModel dpm = limitMap.get(playerId);
				int num = (int) Math.round(dpm.getCarbonScores() * DevilSquareConstant.scoreRate);
				if(num > 0) {
					Player player = GamePlayerManager.getInstance().getPlayer(playerId);
					Article a = ArticleManager.getInstance().getArticle(DevilSquareConstant.scoreArticlename[index]);
					ae = DefaultArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.恶魔广场杀怪积分获取, player, a.getColorType(), 1, true);
					MailManager.getInstance().sendMail(playerId, new ArticleEntity[] {ae}, new int[] { num }, Translate.恶魔广场积分, Translate.恶魔广场积分, 0L, 0L, 0L, "恶魔城堡杀怪积分获得");
					if(logger.isWarnEnabled()) {
						logger.warn("[恶魔广场][结束邮件发送积分奖励]["+player.getLogString()+"][articlename:" + ae.getArticleName() + "][color:" + ae.getColorType() + "][num:" + num + "]");
					}
				}
			} catch(Exception e) {
				logger.error("[恶魔广场] [副本结束发送奖励异常][" + playerId + "]", e);
			}
		}
	}
	
	public void setStatus(byte st, int prepareTime) {
		this.setStatus(st);
		if(st == DevilSquareConstant.START) {
			this.noticeAllPlayerCountTime(DevilSquareConstant.count_ready, prepareTime, Translate.怪物开启倒计时);
		}
	}
	
	public byte getStatus() {
		return stauts;
	}
	/**
	 * 进入副本前的条件判断
	 * @param p
	 * @return 
	 */
	public String ready2Carbon(Player p) {
		synchronized (lock) {
			String result = null;
//			long curT = System.currentTimeMillis();
			if(stauts != DevilSquareConstant.PREPARE) {
				return Translate.副本已开始;					//需要将返回加入translate中
			}
//			if(curT > (openTime + lastTime)) {
//				return Translate.超出传送时间;
//			}
			if(playerList.size() >= maxNumbers) {
				result = Translate.副本人数已满;
			}
			return result;
		}
	}
	/**
	 * 通知所有玩家倒计时
	 * @param type
	 * @param countTime
	 */
	public void noticeAllPlayerCountTime(int type, int countTime, String content) {
		if(countTime <= 3) {
			return;
		}
		try{
			Player p = null;
			List<Long> pl = playerList;
			for(Long playerId : pl) {
				try{
					p = GamePlayerManager.getInstance().getPlayer(playerId);
					DEVILSQUARE_COUNTDOWNTIME_REQ resp = new DEVILSQUARE_COUNTDOWNTIME_REQ(GameMessageFactory.nextSequnceNum(), content, type, countTime);
					p.addMessageToRightBag(resp);
					if(logger.isDebugEnabled()) {
						logger.debug("[恶魔广场] [通知玩家倒计时] [" + p.getLogString() + "] [type:" + type + "***countTime:" + countTime + "]");
					}
				} catch(Exception e) {
					logger.error("[恶魔广场] [通知倒计时出异常] [" + p.getLogString() + "]", e);
				}
			}
		} catch(Exception e) {
			logger.error("[恶魔广场] [通知倒计时出异常] [type:" + type + "****countTime:" + countTime + "]", e);
		}
	}
	/**
	 * 通知副本内玩家某怪物或宝箱出现
	 * @param content
	 */
	public void noticeAllPlayerSthRefreash(String content) {
		try{
			Player p = null;
			List<Long> pl = playerList;
			String ct = DevilSquareConstant.startWith + content + DevilSquareConstant.endWith;
			for(Long playerId : pl) {
				try{
					p = GamePlayerManager.getInstance().getPlayer(playerId);
					p.send_HINT_REQ(ct, (byte) 6);
					if(logger.isDebugEnabled()) {
						logger.debug("[恶魔广场] [通知玩家有东西出现] [" + p.getLogString() + "] [content:" + content + "]");
					}
				} catch(Exception e) {
					logger.error("[恶魔广场] [通知倒计时出异常] [" + p.getLogString() + "]", e);
				}
			}
		} catch(Exception e) {
			logger.error("[恶魔广场] [通知倒计时出异常] [content:" + content + "]", e);
		}
	}
	public void noticePlayerCountTime(int type, int countTime, long playerId, String msg) {
		try{
			Player p = null;
			try{
				p = GamePlayerManager.getInstance().getPlayer(playerId);
				DEVILSQUARE_COUNTDOWNTIME_REQ resp = new DEVILSQUARE_COUNTDOWNTIME_REQ(GameMessageFactory.nextSequnceNum(), msg, type, countTime);
				p.addMessageToRightBag(resp);
				if(logger.isDebugEnabled()) {
					logger.debug("[恶魔广场] [通知玩家倒计时] [" + p.getLogString() + "] [type:" + type + "***countTime:" + countTime + "]");
				}
			} catch(Exception e) {
				logger.error("[恶魔广场] [通知倒计时出异常2] [" + p.getLogString() + "]", e);
			}
		} catch(Exception e) {
			logger.error("[恶魔广场] [通知倒计时出异常2] [type:" + type + "****countTime:" + countTime + "]", e);
		}
	}
	/**
	 * 通知单个玩家倒计时
	 * @param type
	 * @param countTime
	 */
	public void noticePlayerCountTime(int type, int countTime, long playerId) {
		noticePlayerCountTime(type, countTime, playerId, Translate.怪物开启倒计时);
	}
	/**
	 * 通知副本玩家原地复活
	 * @param p
	 * @return
	 */
	public boolean notifyPlayerRelive(Player p) {
		if(playerList.contains(p.getId())) {
			DsPlayerDataModel ddm = limitMap.get(p.getId());
			if(ddm == null) {
				logger.error("[恶魔广场] [没有玩家复活信息] [" + p.getLogString() + "]");
				return false;
			}
			if(ddm.getReliveCount() >= DevilSquareManager.instance.getPlayerCanReliveTimes(p)) {
				if(logger.isInfoEnabled()) {
					logger.info("[恶魔广场] [玩家复活失败，超过次数上限] [已经复活的次数:" + ddm.getReliveCount() + "] [" + p.getLogString() + "]");
				}
				return false;
			}
			ddm.setReliveCount((ddm.getReliveCount() + 1));
			limitMap.put(p.getId(), ddm);
			int temp = DevilSquareManager.instance.getPlayerCanReliveTimes(p) - ddm.getReliveCount();
			String tempStr = Translate.translateString(Translate.恶魔广场原地复活剩余次数, new String[][] {{Translate.STRING_1, temp+""}});
			p.sendError(tempStr);
		}
		return true;
	}
	/**
	 * 针对玩家掉线重新登录倒计时通知操作
	 * @param p
	 */
	public void notifyPlayerEnterServer(Player p) {
		if(!this.playerList.contains(p.getId())) {
			if(logger.isDebugEnabled()) {
				logger.debug("[恶魔广场] [收到玩家上线通知2][玩家不在副本中][" + p.getLogString() + "][" + carbonLevel + "]");
			}
			return;
		}
		if(this.stauts == DevilSquareConstant.START || this.stauts == DevilSquareConstant.PREPARE) {
			long currentTime = System.currentTimeMillis();
			//   //已经进行的时间
			int tt = (int) ((carbonLastTime - (currentTime-openTime)) / 1000);
			noticePlayerCountTime(DevilSquareConstant.count_allTime, tt, p.getId(), Translate.副本整体倒计时);
		}
	}
	
	/**
	 * 进入副本
	 * @param p
	 * @throws Exception 
	 */
	public void notifyEnterCarbon(Player p) throws Exception {
		synchronized (lock) {
			if(playerList.size() < maxNumbers && !playerList.contains(p.getId())) {
				playerList.add(p.getId());
				limitMap.put(p.getId(), new DsPlayerDataModel(0));
			} else {
				logger.error("[已经超过人数上限，玩家又要进入][" + p.getLogString() + "] [" + playerList.contains(p.getId()) + "]");
//				throw new Exception("副本人满又要进入，抛出异常!");
				return;
			}
			//传送进副本场景
			Game currentGame = p.getCurrentGame();
			if(currentGame == null){
				logger.error("[恶魔广场] [玩家当前game为空，进入恶魔广场副本失败] [" + p.getLogString() + "]");
				return ;
			}
			int index = p.random.nextInt(bornPoints.size());
			Integer[] points = bornPoints.get(index);
			currentGame.transferGame(p, new TransportData(0, 0, 0, 0, game.gi.name, points[0], points[1]));
			long currentTime = System.currentTimeMillis();
//			int time = (int) ((currentTime - lastTime) / 1000);
			int tt = (int) ((lastTime - (currentTime - lastStatusChangeT)) / 1000);
			noticePlayerCountTime(DevilSquareConstant.count_enterCarbon, tt, p.getId());
			try {
				int ii = 0;
				if(carbonLevel == 150) {
					ii = 1;
				} else if (carbonLevel == 190) {
					ii = 2;
				} else if (carbonLevel == 220) {
					ii = 3;
				}
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), PlayerAimManager.进入恶魔城堡action[ii], 1L});
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [统计玩家进入恶魔城堡异常] [" + p.getLogString() + "]", e);
			}
		}
	}
	/**
	 * 通知刷新需要复活的怪物
	 */
	public void notifyReLiveOldMonster() {
		try{
			if(needReliveMonsters.size() > 0) {
				long curT = System.currentTimeMillis();
				for(int i=0; i<needReliveMonsters.size(); i++) {
					if(needReliveMonsters.get(i).isAlive) {
						continue;
					} else if (curT >= (needReliveMonsters.get(i).lastDisappearTime + needReliveMonsters.get(i).flushFrequency)) {
						Sprite sp = MemoryMonsterManager.getMonsterManager().createMonster(needReliveMonsters.get(i).spriteCategoryId);
						if(sp != null) {
							needReliveMonsters.get(i).isAlive = true;
							needReliveMonsters.get(i).monsterRandId = sp.getId();
							sp.setX(needReliveMonsters.get(i).getX());
							sp.setY(needReliveMonsters.get(i).getY());
							sp.setBornPoint(new Point(sp.getX(), sp.getY()));
							game.addSprite(sp);
//							if(logger.isDebugEnabled()) {
//								logger.debug("[恶魔广场] [复活已死亡怪物] [" + needReliveMonsters.get(i).monsterRandId + "***复活间隔时间:" + needReliveMonsters.get(i).spriteCategoryId + "]");
//							}
						} else {
							logger.error("[恶魔广场] [怪物没复活] [创建出来怪物等于空] [" + needReliveMonsters.get(i).spriteCategoryId + "]");
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error("[恶魔广场] [复活已死亡怪物异常] ", e);
		}
	}
	/**
	 * 通知恶魔广场刷宝箱
	 * @param categoryId
	 * @param articleName
	 * @param boxCleanTime
	 * @param points
	 */
	public void notifyRefreashBox(int categoryId, String articleName, int boxCleanTime, List<Integer[]> points) {
		if(playerList.size() <= 0) {
			if(logger.isDebugEnabled()) {
				logger.debug("[恶魔广场] [场景中人数为0，没有刷箱子]");
			}
			return;
		}
		String boxName = articleName;
		isBoxAct = false;
		Article at = ArticleManager.getInstance().getArticle(articleName);
		if(at == null) {
			logger.warn("[没有找到相应物品] [ " + articleName + "]");
			return ;
		}
		int num = points.size();
		for(int i=0; i<num; i++) {
			FlopCaijiNpc npc = getFlopCaijiNpc(categoryId, at, boxCleanTime);
			npc.setX(points.get(i)[0]);
			npc.setY(points.get(i)[1]);
			game.addSprite(npc);
			if(logger.isDebugEnabled()) {
				logger.debug("[恶魔广场] [刷箱子] [物品名:" + articleName + "] [npcId:" + categoryId + "] [坐标:" + npc.getX() + " && " + npc.getY() + "]");
			}
		}
		if(!"".equals(boxName)) {
			String tempStr = Translate.translateString(Translate.XX宝箱刷新了, new String[][] {{Translate.STRING_1, boxName}});
			noticeAllPlayerSthRefreash(tempStr);
		}
	}
	
	private FlopCaijiNpc getFlopCaijiNpc(int categoryId, Article article, int boxCleanTime) {
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, null, article.getColorType(), 1, true);
		} catch (Exception e) {
			logger.error("[恶魔广场刷宝箱出错]", e);
			return null;
		}
		NPCManager nm = MemoryNPCManager.getNPCManager();
		FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
		fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		fcn.setAllCanPickAfterTime(1000);
		fcn.setEndTime(SystemTime.currentTimeMillis() + boxCleanTime*1000);
		ResourceManager.getInstance().getAvata(fcn);
		fcn.setPlayersList(playerList);
		fcn.setAe(ae);
		fcn.setNameColor(ArticleManager.getColorValue(article, article.getColorType()));
		fcn.setCount(1);
		fcn.setName(article.getName());
		Article at = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if(at.getFlopNPCAvata() != null) {
			fcn.setAvataSex(at.getFlopNPCAvata());
			if (article.getFlopNPCAvata().equals("yinyuanbao")) {
				fcn.setAvataSex("baoxiang");
			}
			ResourceManager.getInstance().getAvata(fcn);
		}
		return fcn;
	}
	
	/**
	 * 通知副本刷怪(刷boss也使用此方法)
	 * @param monsterId
	 * @param points
	 * @param num
	 */
	public void notifyRefreashMonster(int monsterId, List<Integer[]> points, int num, int reliveTime) {
		refreashTimes++;
		lastRefreashTime = System.currentTimeMillis();
		if(this.playerList.size() <= 0) {
//			logger.warn("[恶魔广场][副本中没有人，怪物没刷新]");
			return ;
		}
		if (DevilSquareConstant.是否开启恶魔城堡怪物积分限制 && !monsterIdList.contains(monsterId)) {
			monsterIdList.add(monsterId);
		}
		int refreashNum = points.size();
		boolean nomalRefreash = true;
		String monsterName = "";
		Map<Integer, Integer> specilRefreashRule = null;
		if(refreashNum > (this.maxMonsterNum - game.getMonsterNum())) {			//将要刷出的怪物数量超出副本最大怪物限制
			nomalRefreash = false;
			specilRefreashRule = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {					//根据坐标点周围范围内怪物数量从小到大排序
					// TODO Auto-generated method stub
					if(o1 > o2) {
						return -1;
					} else {
						return 1;
					}
				}
			});
			refreashNum = this.maxMonsterNum - game.getMonsterNum();
//			if(logger.isInfoEnabled()) {
//				logger.info("[恶魔广场] [场景怪物将要达到上限，此次刷怪数量为:" + refreashNum + "]");
//			}
			for(int i=0; i<points.size();i++) {
				Integer[] ii = points.get(i);
				int monsterNum = game.getMonsterNumByPoints(ii[0], ii[1]);
				specilRefreashRule.put(monsterNum, i);
			}
		}
		if(nomalRefreash) {
			for(int i=0; i<points.size(); i++) {
				monsterName = refreashMonster(monsterId, points.get(i), num, reliveTime);
			}
		} else {
			if(specilRefreashRule == null) {
				logger.error("[恶魔广场刷怪] [怪物没有刷出来，specilRefreashRule==null]");
				return ;
			}
			for(Integer ii : specilRefreashRule.values()) {
				if(ii < points.size()) {
					if(refreashNum <= 0) {
						break;
					}
					monsterName = refreashMonster(monsterId, points.get(ii), num, reliveTime);
					refreashNum--;
				}
			}
		}
		if(!"".equals(monsterName)) {
			String tempStr = Translate.translateString(Translate.XX怪物刷新了, new String[][] {{Translate.STRING_1, monsterName}});
			noticeAllPlayerSthRefreash(tempStr);
		}
	}
	
	private String refreashMonster(int monsterId, Integer[] point, int num, int reliveTime) {
		String monsterName = "";
		for(int i=0; i<num; i++) {
			try{
				Sprite sprite = null;
				sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
				if(sprite != null){
					sprite.setX(point[0]);
					sprite.setY(point[1]);
					sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
					game.addSprite(sprite);
					if(reliveTime > 0) {
						DsBornPoint temp = new DsBornPoint(point[0], point[1]);
						Monster mm = (Monster) sprite;
						temp.monsterRandId = sprite.getId();
						temp.spriteCategoryId = mm.getSpriteCategoryId();
						temp.isAlive = true;
						temp.flushFrequency = reliveTime * 1000;
						needReliveMonsters.add(temp);
					}
					monsterName = sprite.getName();
				} else {
					logger.error("[恶魔广场][怪物没有创建好][monsterId=" + monsterId + "]");
				}
			}catch(Exception e){
				logger.error("[恶魔广场][刷怪异常]", e);
			}
		}
		return monsterName;
	}
	/**
	 * 通知副本清除所有小怪，等待boss刷新
	 */
	public void notifyRemoveAllMonster() {
//		logger.warn("[恶魔广场] [通知清除副本内所有怪物]");
		game.removeAllMonster();
		isBossTime = true;
		needReliveMonsters.clear();
//		lastRefreashTime = System.currentTimeMillis();
	}
	/**
	 * 完成副本,将所有玩家传送回各自国家的昆仑圣殿
	 */
	public synchronized void notifyFinishCarbon() {
		Player p = null;
		Set<Long> succeedBack = new HashSet<Long>();
		for(Long id : playerList) {
			try {
				p = GamePlayerManager.getInstance().getPlayer(id);
				succeedBack.add(id);
				回城(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[恶魔广场] [传送玩家回城异常] [" + p.getLogString() + "]", e);
			}
		}
		for(Long id : succeedBack) {
			playerList.remove(id);
		}
	}
	/**
	 * 副本中死亡或下线回城
	 * @param player
	 */
	private void backTownAlive() {
		synchronized (backTownPlayers) {
			Set<Long> temp = new HashSet<Long>();
			for(Long id : backTownPlayers) {
				temp.add(id);
			}
			for(Long id : temp) {
//				Player player = null;								//玩家死亡不传送出副本，只是不允许点击原地复活
				try{
//					player = GamePlayerManager.getInstance().getPlayer(id);
//					if(player.isDeath()) {
//						player.setHp(player.getMaxHP() / 2);
//						player.setMp(player.getMaxMP() / 2);
//						player.setState(Player.STATE_STAND);
//						player.notifyRevived();
//						PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP() / 2, player.getMaxMP() / 2);
//						player.addMessageToRightBag(res);
//					}
//					if(回城(player)) {
						backTownPlayers.remove(id);
//					}
				}catch(Exception e) {
					logger.error("[恶魔广场] [传送死人出副本出错][" + id + "]", e);
//					if(player != null) {
//						logger.error("[恶魔广场] [传送死人出副本出错] [" + player.getLogString() + "]", e);
//					} else {
//						logger.error("[传送出副本出错] ", e);
//					}
				}
			}
		}
	}
	
	/**
	 * 怪物被杀死时调用，用来判断是否到达时间副本结束
	 * @param m
	 */
	public void notifyBossKilled(Monster m) {
		try{
			Player owner = m.getOwner();
			if(owner != null) {
				if(!playerList.contains(owner.getId())) {
					return;
				}
				DsPlayerDataModel dsm = limitMap.get(owner.getId());
				if(dsm == null) {
					logger.warn("[恶魔广场] [怪物拥有者不在副本中:" + owner.getLogString() + "][配表怪物id:" + m.getSpriteCategoryId() + "]");
					return;
				}
				if (DevilSquareConstant.是否开启恶魔城堡怪物积分限制 && !monsterIdList.contains(m.getSpriteCategoryId())) {
					if (logger.isInfoEnabled()) {
						logger.info("[恶魔广场] [杀死的怪物不属于此副本] [" + owner.getLogString() + "] [配表怪物id:" + m.getSpriteCategoryId() + "] [副本lv:" + carbonLevel + "]");
					}
					return ;
				}
				int score = DevilSquareManager.instance.getScore(m.getSpriteCategoryId());
				if(score <= 0) {
					return;
				}
				long old = dsm.getCarbonScores();
				dsm.setCarbonScores(old + score);
				limitMap.put(owner.getId(), dsm);
				if(logger.isInfoEnabled()) {
					logger.info("[恶魔广场][杀怪获得积分][副本等级:"+carbonLevel+"][怪物id:" + m.getSpriteCategoryId() + "][" + owner.getLogString() + "][old:" + old + "][new:" + dsm.getCarbonScores() + "]");
				}
			} else {
				logger.warn("[恶魔广场] [怪物拥有者为空, 怪物id:" + m.getId() + "][配表怪物id:" + m.getSpriteCategoryId() + "]");
			}
		} catch(Exception e) {
			logger.error("[恶魔广场] [杀怪积分出错][怪物id:" + m.getSpriteCategoryId() + "][击杀者:" + m.getOwner()==null?null:m.getOwner().getLogString() + "]");
		}
		if(this.bossId == m.getSpriteCategoryId()) {
			this.setStatus(DevilSquareConstant.FINISH);
			logger.warn("[恶魔广场] [击杀最终boss，副本提前结束] [bossid=" + bossId + "]");
		} else {
			for(int i=0; i<needReliveMonsters.size(); i++) {
				if(needReliveMonsters.get(i).monsterRandId == m.getId()) {
					needReliveMonsters.get(i).isAlive = false;
					needReliveMonsters.get(i).lastDisappearTime = System.currentTimeMillis();
					break;
				}
			}
		}
	}
	
	private boolean 回城(Player p){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				logger.info("[玩家已经不再这个区域了，不需要在回程了]");
				return true;
			}
			
			String mapName = TransportData.getMainCityMap(p.getCountry());
			Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, p.getCountry());
			MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
			
			int x = area.getX();
			int y = area.getY();
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			logger.warn("[恶魔广场副本] [回城] [成功] [mapName:"+mapName+"] [xy:"+x+"/"+y+"] ["+p.getLogString()+"]");
			return true;
		}catch(Exception e){
			if(p != null){
				logger.warn("[恶魔广场副本结束] [回城] [异常][" + p.getLogString() + "]", e);
			} else {
				logger.error("[传送出副本出错] ", e);
			}
			return false;
		}
	}
}
