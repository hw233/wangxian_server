package com.fy.engineserver.activity.fairyBuddha.challenge;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;

public class FariyChallenge {
	/** 挑战者id */
	private long playerId;
	/** 挑战者所在game */
	private Game game;
	/** 挑战结果  0挑战中   1挑战成功   -1死亡 -2下线上线 */
	private byte result = 0;
	/** 目标怪物id */
	private long targetId = -1;
	/** 元神或者本尊 */
	private int soulType;
	/** 进入前地图名 */
	private String preMapName;
	/** 进入前x坐标 */
	private int x;
	/** 进入前y坐标 */
	private int y;
	/** 职业 */
	private byte career;
	
	public static final byte DEATH = -2;
	public static final byte SUCCEED = 1;
	public static final byte NOTONLINE = -2;
	public static final byte USETRANSPROP = -9;
	
	
	public FariyChallenge(long playerId, Game game, byte result, int soulType, String preMapName, int x, int y, byte career) {
		super();
		this.playerId = playerId;
		this.game = game;
		this.result = result;
		this.soulType = soulType;
		this.preMapName = preMapName;
		this.x = x;
		this.y = y;
		this.career = career;
	}
	@Override
	public String toString() {
		return "FariyChallenge [playerId=" + playerId + ", game=" + game + ", result=" + result + "]";
	}
	/**
	 * 杀死怪物
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if(targetId <= 0) {
			if (FairyChallengeManager.logger.isDebugEnabled()) {
				FairyChallengeManager.logger.debug("[仙尊挑战] [异常] [目标id错误:"+targetId+"][" + playerId + "]");
			}
			return;
		}
		if(monster.getSpriteId() == targetId) {
			setResult((byte) 1);
			if (FairyChallengeManager.logger.isDebugEnabled()) {
				FairyChallengeManager.logger.debug("[仙尊挑战] [成功] [杀死目标怪物] [" + monster + "]");
			}
		}
	}
	
	public void refreshMonster() {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Soul soul = player.getSoul(soulType);
			if(soul != null) {
				int index = soul.getCareer();
				if(index > FairyChallengeManager.bossIds.length) {
					if(FairyChallengeManager.logger.isWarnEnabled()) {
						FairyChallengeManager.logger.warn("[仙尊挑战] [获取bossid异常] [默认取第一个bossid] [" + player.getLogString() + "] [career :" + index + "] [FairyChallengeManager.bossIds.length:" + FairyChallengeManager.bossIds.length + "]");
					}
					index = 0;
				}
				Monster monster = FairyChallengeManager.getInst().refreshMonster(game, FairyChallengeManager.bornPoint4Boss[0], FairyChallengeManager.bornPoint4Boss[1], FairyChallengeManager.bossIds[index], soul.getCareer());
				targetId = monster.getId();
				if(FairyChallengeManager.logger.isInfoEnabled()) {
					FairyChallengeManager.logger.info("[仙尊挑战] [刷出boss] [目标bossId:" + targetId + "][" + player.getLogString() + "] [boss模板id:" + monster.getSpriteCategoryId() + "]");
				}
			}
		} catch (Exception e) {
			FairyChallengeManager.logger.error("[挑战仙尊] [异常] [playerid : " + playerId + "]", e);
		}
	}
	/**
	 * 角色死亡
	 * @param player
	 */
	public void notifyPlayerDead(Player player, byte type) {
		if(player.getId() == this.getPlayerId()) {
			setResult(type);
			if (FairyChallengeManager.logger.isDebugEnabled()) {
				FairyChallengeManager.logger.debug("[仙尊挑战] [失败] [角色死亡或者下线] [" + player.getLogString() + "]");
			}
		}
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.getPlayerId());
			if(result == SUCCEED) {				//只有胜利的时候通知
				EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.FAIRY_CHALLENGE_RESULT, new Object[] { player, result, soulType });
				EventRouter.getInst().addEvent(evt);
			}
			if(result == USETRANSPROP || result == SUCCEED || result == DEATH) {			
				回城(player);
			}
			player.chanllengeFlag = result;
		} catch (Exception e) {
			FairyChallengeManager.logger.error("[仙尊挑战] [结束异常] [playerId : " + this.getPlayerId() + "] ", e);
		}
	}
	
	private boolean 回城(Player p){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame == null) {
				FairyChallengeManager.logger.info("[玩家当前game为空][" + p.getLogString() + "]");
				return false;
			}
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				FairyChallengeManager.logger.info("[玩家已经不再这个区域了，不需要在回程了][" + p.getLogString() + "]");
				return true;
			}
			String mapName =  TransportData.getMainCityMap(p.getCountry());;
			int x = p.getResurrectionX();
			int y = p.getResurrectionY();
			if(preMapName != null && !preMapName.isEmpty()) {
				mapName = preMapName;
				x = this.x;
				y = this.y;
			}
			p.setHp(p.getMaxHP() / 2);
			p.setMp(p.getMaxMP() / 2);
			p.setState(Player.STATE_STAND);
			p.notifyRevived();
			currentGame.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", p.getMaxHP() / 2, p.getMaxMP() / 2);
			p.addMessageToRightBag(res);
			FairyChallengeManager.logger.warn("[仙尊挑战] [回城] [成功] ["+p.getLogString()+"]");
			return true;
		}catch(Exception e){
			if(p != null){
				FairyChallengeManager.logger.warn("[仙尊挑战] [回城] [异常][" + p.getLogString() + "]", e);
			} else {
				FairyChallengeManager.logger.error("[仙尊挑战] [传送出副本出错] ", e);
			}
			return false;
		}
	}
	public long getTargetId() {
		return targetId;
	}
	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}
	public int getSoulType() {
		return soulType;
	}
	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}

	public String getPreMapName() {
		return preMapName;
	}

	public void setPreMapName(String preMapName) {
		this.preMapName = preMapName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public byte getCareer() {
		return career;
	}
	public void setCareer(byte career) {
		this.career = career;
	}
	
}
