package com.fy.engineserver.activity.fairyRobbery.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.fairyRobbery.model.FairyRobberyModel;
import com.fy.engineserver.activity.fairyRobbery.model.RobberyMonsterModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.DEVILSQUARE_COUNTDOWNTIME_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_CLIENT_VICTORY_TIPS_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

public class FairyRobbery {
	/** 渡劫名称 */
	public String name;
	/** 最后一个更新进度时间（刷新boss或者击杀boss等） */
	public long lastChangeTime;
	/** 副本状态改变时间 */
	public long lastChangeStateTime;
	/** 角色id */
	public long playerId;
	/** 刷新的boss */
	public List<Long> refreshBossId = new ArrayList<Long>();
	
	public long lastRefreshBossTime ;
	
	public long lastChectKickTime ;
	
	public Game game;
	/** 下次需要刷新的boss模板 */
	public TempBoss tb = null;
	/** 正在挑战的boss模板 */
	public TempBoss2 tb2 = null;
	/**  渡劫状态   0渡劫中   -1结束  1胜利（胜利后一定时间后才传出副本） */
	private int state;
	public static final int 结束 = -2;			//此状态关闭副本心跳
	public static final int 准备 = 0;
	public static final int 开始渡劫 = 1;				
	public static final int 胜利 = 2;				//胜利后不传送玩家出副本，需要等玩家确定后修改为结束将玩家传送出去
	public static final int 失败 = -1;
	/** 改变状态，根据此值心跳中调整副本状态（防止多线程时间不一致） */
	public volatile int tempState = 默认临时状态;
	public static final int 默认临时状态 = 999;
	
	public static long 刷新boss间隔时间 = 20*1000L;
	
	public static long 胜利或失败多久后传送出副本 = 10000;
	public static long 开始后多久开始刷怪 = 0;
	public static long 刷新boss后多久释放秒杀技能 = 3*60*1000L;
	
	private Object lock = new Object();
	
	public void heartBeat(long now){
		
		game.heartbeat();
		
		try {
			if (now >= (lastChectKickTime+5000)) {			//5秒检测一次，boss刷新后三分钟不死就释放天雷秒杀玩家
				lastChectKickTime = now;
				long id = -1;
				if (refreshBossId != null && refreshBossId.size() > 0) {
					id = refreshBossId.get(refreshBossId.size()-1);
					if (lastRefreshBossTime > 0 && game.containsAndAlive(id,1) && now >= (lastRefreshBossTime + 刷新boss后多久释放秒杀技能) && this.getState() != 胜利  && this.getState() !=失败 && this.getState() !=结束) {
						//释放天雷秒杀
						Player player = GamePlayerManager.getInstance().getPlayer(playerId);
						int damage = player.getMaxHP() + 10000;
						player.causeDamageByRayRobbery(damage);
						if (FairyRobberyManager.logger.isInfoEnabled()) {
							FairyRobberyManager.logger.info("[仙界渡劫] [3分钟内玩家未击杀boss] [释放天雷秒杀] [成功] [dmg:" + damage + "] [" + player.getLogString() + "]");
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		
		try {			
			if (tb != null && this.getState() == 开始渡劫 /*&& (now >= (lastChangeStateTime + 开始后多久开始刷怪))*/) {					//判断是否需要刷新boss
				synchronized (lock) {
					if (tb.getRefreshTime() <= 0) {
						tb.setRefreshTime((now + 刷新boss间隔时间));
						//通知玩家即将刷新boss
						noticeClientTime(0,Translate.下个boss刷新倒计时,(int) (刷新boss间隔时间/1000));
					} else if (now >= tb.getRefreshTime()) {
						this.refreshBoss(now);
						this.tb = null;
					}
				}
			} else if (tb == null && tb2 == null && refreshBossId.size() == 0) {
				FairyRobberyModel model = FairyRobberyManager.inst.robberys.get(name);
				RobberyMonsterModel rmm = model.getBossList().get(0);
				TempBoss t = new TempBoss(-1, rmm.getBossId(), rmm.getBornPoint(), rmm.getLimitTime(), rmm.getSuccType());
				this.tb = t;
			}
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [异常] [" + this.playerId + "] ", e);
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.playerId);
			if (!player.isOnline()) {
				if (this.getState() != 胜利  && this.getState() !=失败 && this.getState() !=结束) {
					this.setState(失败, now);
				}
			}
		} catch (Exception e) {
			
		}
		if (FairyRobberyManager.logger.isDebugEnabled()) {
			FairyRobberyManager.logger.debug("[仙界渡劫] [心跳] [" + this.playerId + "] [" + this.tempState + "] [" + this.getState() + "]");
		}
		this.isTimeEnd(now);				//判断是否到达挑战时间
		if (this.tempState != 默认临时状态) {
			this.setState(tempState, now);
			this.tempState = 默认临时状态;
		}
		if (this.getState() == 胜利 || this.getState() == 失败) {
			if (now >= (lastChangeStateTime + 胜利或失败多久后传送出副本)) {
				this.setState(结束, now);
			}
		}
	}
	/**
	 * 通知客户端倒计时
	 * @param type  
	 * @param des
	 * @param time	秒
	 */
	public void noticeClientTime(int type, String des, int time) {
		if (FairyRobberyManager.logger.isDebugEnabled()) {
			FairyRobberyManager.logger.debug("[仙界渡劫] [通知客户端倒计时] [" + this.playerId + "] [" + type + "] [" + des + "] [" + time + "]");
		}
		if (time > 0) {
			try {
				Player player = GamePlayerManager.getInstance().getPlayer(this.playerId);
				DEVILSQUARE_COUNTDOWNTIME_REQ resp = new DEVILSQUARE_COUNTDOWNTIME_REQ(GameMessageFactory.nextSequnceNum(), des, 3, time);
				player.addMessageToRightBag(resp);
			} catch (Exception e) {
				FairyRobberyManager.logger.warn("[仙界渡劫] [通知倒计时] [异常] [" + this.playerId + "]", e);
			}
		} 
	}
	
	public boolean notifyPlayerDead(Player player) {
		if (player.getId() == this.playerId) {
			this.changeState(失败);
			return true;
		}
		return false;
	}
	/**
	 * 修改副本状态（临时存储，在副本心跳中修改）
	 * @param state
	 */
	public void changeState(int state) {
		if (this.tempState != 默认临时状态) {
			if (FairyRobberyManager.logger.isInfoEnabled()) {
				FairyRobberyManager.logger.info("[仙界渡劫] [修改副本临时状态] [old:" + this.tempState + "] [new:" + state + "] [playerId:" + this.playerId + "]");
			}
		}
		this.tempState = state;
	}
	/**
	 * 
	 * @param state
	 * @param now
	 */
	private void setState(int state, long now){
		if (this.state == state) {
			if (FairyRobberyManager.logger.isInfoEnabled()) {
				FairyRobberyManager.logger.info("[仙界渡劫] [修改副本状态] [失败] [状态相同] [" + this.playerId + "] [" + state + "]");
			}
			return ;
		}
		lastChangeStateTime = now;
		this.state = state;
		if (FairyRobberyManager.logger.isDebugEnabled()) {
			FairyRobberyManager.logger.debug("[仙界渡劫] [修改副本状态] [成功] [" + this.playerId + "] [" + state + "]");
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.playerId);
//			FairyRobberyModel model = FairyRobberyManager.inst.robberys.get(name);
			if (this.state == 结束 || this.state == 失败) {		
				game.removeAllMonster();
				if (this.state == 失败) {
					this.failure();
					NOTIFY_CLIENT_VICTORY_TIPS_REQ req = new NOTIFY_CLIENT_VICTORY_TIPS_REQ(GameMessageFactory.nextSequnceNum(), (byte)0);
					player.addMessageToRightBag(req);
//					FairyRobberyManager.popBackTownWindow(player, game, model.getFailureDes());
					noticeClientTime(0,Translate.离开倒计时,(int) (胜利或失败多久后传送出副本/1000));
				} else {			//传送玩家出副本（需要判断玩家是否已经出副本）
					try {
						FairyRobberyManager.回城(player, game);
					} catch (Exception e) {
						FairyRobberyManager.logger.warn("[仙界渡劫] [传送玩家出副本] [异常] [playerId:" + this.playerId + "]", e);
					}
				}
			} else if (this.state == 胜利) {
				game.removeAllMonster();
				this.succeed();
				NOTIFY_CLIENT_VICTORY_TIPS_REQ req = new NOTIFY_CLIENT_VICTORY_TIPS_REQ(GameMessageFactory.nextSequnceNum(), (byte)1);
				player.addMessageToRightBag(req);
//				FairyRobberyManager.popBackTownWindow(player, game, model.getEndDes());
				noticeClientTime(0,Translate.离开倒计时,(int) (胜利或失败多久后传送出副本/1000));
			}
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [修改状态] [异常] [playerId:" + this.playerId + "] [state:" + state + "]", e);
		}
	}
	
	public int getState() {
		return this.state;
	}
	/**
	 * 刷新boss
	 * @param now
	 * @param bossId
	 */
	public void refreshBoss(long now) {
		lastChangeTime = now;
		lastRefreshBossTime = now;
		Monster sprite = MemoryMonsterManager.getMonsterManager().createMonster(tb.getBossId());
		if (sprite != null) {
			sprite.setX(tb.getPoints()[0]);
			sprite.setY(tb.getPoints()[1]);
			sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
			game.addSprite(sprite);
		} else {
			FairyRobberyManager.logger.error("[仙界渡劫][怪物没有创建好][" + this.tb + "]");
			return ;
		}
		long id = sprite.getId();
		refreshBossId.add(id);
		long endTime = tb.getLastTime() < 0 ? -1 : (now + tb.getLastTime());
		tb2 = new TempBoss2(id, endTime, tb.getType());
		FairyRobberyManager.logger.warn("[仙界渡劫] [刷新boss] [成功] [pid:" + this.playerId + "] [" + tb + "] [" + refreshBossId + "]");
	}
	/**
	 * 生存模式
	 * @param now
	 */
	public void isTimeEnd(long now) {
		try {
			if (tb2 != null && tb2.getEndTime() > 0) {			//判断是否为限时击杀或生存
				if (now >= tb2.getEndTime()) {		//结束
					if (tb2.getType() == FairyRobberyManager.击杀) {		//规定时间内未击杀boss  失败
						this.changeState(失败);;
					} else if (tb2.getType() == FairyRobberyManager.生存) {		//完成，刷新下个boss
						FairyRobberyModel model = FairyRobberyManager.inst.robberys.get(name);
						int index= refreshBossId.size();
						synchronized (lock) {
							if (this.tb == null) {
								RobberyMonsterModel rmm = model.getBossList().get(index);
								TempBoss tb = new TempBoss(-1, rmm.getBossId(), rmm.getBornPoint(), rmm.getLimitTime(), rmm.getSuccType());
								this.tb = tb;
								if (FairyRobberyManager.logger.isDebugEnabled()) {
									FairyRobberyManager.logger.debug("[仙界渡劫] [时间到，刷新boss] [成功] [" + this.playerId + "] [" + this.tb + "]");
								}
							} else {
								FairyRobberyManager.logger.warn("[仙界渡劫] [刷新下次boss2] [存在未刷新的boss模板] [playerId:" + this.playerId + "] [rn:" + this.name + "] [" + tb + "]");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [判断时间] [异常] [" + this.playerId + "] ", e);
		}
	}
	
	public void succeed() {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.playerId);
			FairyRobberyEntity entity = FairyRobberyEntityManager.inst.getEntity(player);
			entity.setLastSucceTime(System.currentTimeMillis());
			entity.setPassLevel(entity.getPassLevel() + 1);
			try {
				AchievementManager.getInstance().record(player, RecordAction.完成仙界渡劫层数, entity.getPassLevel());
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计仙界渡劫次数] [异常] [" + player.getLogString() + "]", e);
			}
			FairyRobberyManager.logger.warn("[仙界渡劫] [玩家渡劫成功] [结束] [" + player.getLogString() + "] [" + entity + "]");
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [更新玩家渡劫数据] [异常] [" + this.playerId + "]", e);
		}
	}
	
	public void failure() {
		//加虚弱buff
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.playerId);
			if (player.isDeath()) {				//如果玩家死亡，复活玩家
				player.setHp(player.getMaxHP());
				player.setMp(player.getMaxMP());
				player.setState(Player.STATE_STAND);
				player.notifyRevived();
				PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP(), player.getMaxMP());
				player.addMessageToRightBag(res);
			}
			TransitRobberyManager.getInstance().robberyFail(player);		//加渡劫buff
			FairyRobberyManager.logger.warn("[仙界渡劫] [玩家渡劫失败] [结束] [" + player.getLogString() + "]");
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[仙界渡劫] [玩家渡劫失败] [异常] [playerId:" + this.playerId + "]", e);
		}
	}
	/**
	 * 玩家离开此地图
	 * @param player
	 * @return
	 */
	public boolean notifyLeaveGame(Player player) {
		if (this.playerId == player.getId()) {
			this.changeState(失败);;
			return true;
		}
		return false;
	}
	/**
	 * 收到客户端协议通知开始渡劫
	 * @param player
	 * @return
	 */
	public boolean notifyStart(Player player) {
		if (FairyRobberyManager.logger.isDebugEnabled()) {
			FairyRobberyManager.logger.debug("[仙界渡劫] [收到玩家开始通知] [成功] [" + player.getLogString() + "] [" + this.playerId + "]");
		}
		if (this.playerId == player.getId()) {
			this.changeState(开始渡劫);
			return true;
		}
		return false;
	}
	
	public boolean notifyKillBoss(long playerId,long bossId) {
		if (FairyRobberyManager.logger.isDebugEnabled()) {
			FairyRobberyManager.logger.debug("[仙界渡劫] [杀死怪物] [渡劫者id:" + this.playerId + "] [怪物拥有者id:" + playerId + "] [bossid:" + bossId + "]");
		}
		if (this.playerId == playerId && refreshBossId.contains(bossId)) {		// 玩家击杀boss	
			FairyRobberyModel model = FairyRobberyManager.inst.robberys.get(name);
			int index= refreshBossId.size();
			if (tb2.getBossId() != bossId) {
				FairyRobberyManager.logger.warn("[仙界渡劫] [玩家击杀boss和当前存储boss不符] [playerId:" + this.playerId + "] [bossId:" + bossId + "] [" + tb2 + "]");
			}
			synchronized (lock) {
				tb2 = null;
			}
			if (index >= model.getRefreshBossNum()) {		//胜利
				changeState(胜利);
			} else {										//通知准备刷新下次boss
				synchronized (lock) {
					if (this.tb == null) {
						RobberyMonsterModel rmm = model.getBossList().get(index);
						TempBoss tb = new TempBoss(-1, rmm.getBossId(), rmm.getBornPoint(), rmm.getLimitTime(), rmm.getSuccType());
						this.tb = tb;
						if (FairyRobberyManager.logger.isDebugEnabled()) {
							FairyRobberyManager.logger.debug("[仙界渡劫] [时间到，刷新boss] [成功] [" + this.playerId + "] [" + this.tb + "]");
						}
					} else {
						FairyRobberyManager.logger.warn("[仙界渡劫] [刷新下次boss] [存在未刷新的boss模板] [playerId:" + this.playerId + "] [rn:" + this.name + "] [" + tb + "]");
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
