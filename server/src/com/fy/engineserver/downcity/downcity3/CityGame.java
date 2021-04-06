package com.fy.engineserver.downcity.downcity3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;

public class CityGame {

	private int id;
	
	private Game game;
	
	private BossRoom room;
	
	/**
	 * 副本时间到，显示5秒结束界面，state设为1
	 * 用来副本结束后的清理标记
	 */
	public int state;
	
	private long endTime;
	
	
	public CityGame(int id,Game game){
		this.id = id;
		this.game = game;
	}
	
	public void heartbeat(){
		try {
			game.heartbeat();
		} catch (Exception e) {
			e.printStackTrace();
			BossCityManager.logger.warn("[全名boss] [game心跳异常] [id:"+id+"]",e);
		}
		noticeEndFight();
		noticeLeaveGame();
		noticePlayerDead();
		checkBossHP();
		heartbeatLog();
	}
	
	private long lastHeartTime;
	//心跳中获取感兴趣的数据
	boolean noticePrintLog = false;
	public void heartbeatLog(){
		if(SystemTime.currentTimeMillis() - lastHeartTime >= 10000){
			lastHeartTime = SystemTime.currentTimeMillis();
			noticePrintLog = true;
		}
		if(state == 1){
			noticePrintLog = true;
		}
		if(endTime > 0){
			noticePrintLog = true;
		}
		if(noticePrintLog){
			noticePrintLog = false;
			BossCityManager.logger.warn("[全民boss心跳] [id:"+id+"] [state:"+state+"] [endTime:"+endTime+"]");
		}
	}
	
	public void checkBossHP(){
		List<BossMonster> bossMonsters = game.getBosss();
		for(BossMonster bossMonster : bossMonsters){
			Monster b = bossMonster;
//			System.out.println("boss状态:"+b.getState()+"--血量:"+b.getHp());
			if(b.getState() == Player.STATE_DEAD){
				b.setState(Player.STATE_STAND);
			}
			if(b.getHp() <= (b.getMaxHP() / 2)){
				b.setHp(b.getMaxHP()-1000);
			}
		}
	}
	
	Map<Long,Long> deadRecord = new HashMap<Long,Long>();
	public void noticePlayerDead(){
		List<Player> ps = game.getPlayers();
		if(ps != null && ps.size() > 0){
			for(Player p : ps){
				if(p.isDeath() && !deadRecord.containsKey(new Long(p.getId()))){
					deadRecord.put(p.getId(), SystemTime.currentTimeMillis());
					BossCityManager.logger.warn("[全民boss] [id:{}] [玩家死亡] [boss:{}] [{}]",new Object[]{id,game.getMonsterNum(),p.getLogString()});
				}
				if(deadRecord.containsKey(new Long(p.getId())) && SystemTime.currentTimeMillis() - deadRecord.get(new Long(p.getId())).longValue() >= 5000){
					p.setHp(p.getMaxHP()/2);
					p.setMp(p.getMaxMP()/2);
					p.setState(Player.STATE_STAND);
					p.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
					p.addMessageToRightBag(res);
					deadRecord.remove(new Long(p.getId()));
					BossCityManager.logger.warn("[全民boss] [id:{}] [玩家复活] [boss:{}] [{}]",new Object[]{id,game.getMonsterNum(),p.getLogString()});
				}
				if(BossCityManager.getInstance().isClose() && p.isDeath()){
					p.setHp(p.getMaxHP()/2);
					p.setMp(p.getMaxMP()/2);
					p.setState(Player.STATE_STAND);
					p.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
					p.addMessageToRightBag(res);
					BossCityManager.logger.warn("[全民boss] [id:{}] [玩家死亡复活] [boss:{}] [{}]",new Object[]{id,game.getMonsterNum(),p.getLogString()});
				}
			}
		}
	}
	
	public void noticeEndFight(){
		if(BossCityManager.getInstance().isClose() && endTime == 0){
			endTime = SystemTime.currentTimeMillis();
			
			//game.removeAllMonster();
			
			List<Player> ps = game.getPlayers();
			if(ps == null || ps.size() <= 0){
				BossCityManager.logger.warn("[副本结束] [全民boss] [id:"+id+"] [副本剩余人数为0]");
				return;
			}
			
			try {
//				for (Player pp : ps) {
//					if(pp != null && pp.isOnline()){
//						if(pp.room != null && pp.room.getIds().contains(new Long(pp.getId()))){
							ChatMessage msg = new ChatMessage();
							msg.setMessageText("<f color='0x14ff00'>"+Translate.boss挑战活动结束了+"</f>");
							ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
							BossCityManager.logger.warn("[全民boss] [id:"+id+"] [5秒结束提示]");
//						}
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			BossCityManager.logger.warn("[通知玩家副本结束] [全民boss] [id:{}] [在线人数:{}]",new Object[]{id,ps.size()});
		}
	}
	
	public void noticeLeaveGame(){
		if(endTime > 0){
			if(SystemTime.currentTimeMillis() > endTime + 5000L){
				List<Player> ps = game.getPlayers();
				if(ps != null && ps.size() > 0){
					for(Player p : ps){
						if(p.room != null && p.room.getIds().contains(new Long(p.getId()))){
							p.room = null;
							String mapName = TransportData.getMainCityMap(p.getCountry());
							Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, p.getCountry());
							MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
							game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, area.getX(), area.getY()));
							BossCityManager.logger.warn("[通知玩家副本结束] [全民boss] [id:"+id+"] [副本结束5秒后传送] [mapName:{}] [xy:{}] [{}]",
									new Object[]{mapName,area.getX()+","+area.getY(),p.getLogString()});
							return;
						}
					}
				}
				if(ps == null || ps.size() == 0){
					state = 1;
					BossCityManager.logger.warn("[全民boss] [id:"+id+"] [副本结束标记]");
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public Game getGame() {
		return game;
	}
	
	public BossRoom getRoom() {
		return room;
	}

	public void setRoom(BossRoom room) {
		this.room = room;
	}

	public void clearPlayer(long pid){
		room.leaveRoom(pid);
	}
	
	public void destory(){
		
	}
	
}
