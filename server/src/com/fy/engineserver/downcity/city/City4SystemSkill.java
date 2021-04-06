/**
 * 
 */
package com.fy.engineserver.downcity.city;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * @author Administrator
 *
 */
public class City4SystemSkill implements CityAction{

//	1、	副本总时长20分钟，进入倒计时，时间结束传出
//	2、	每5分钟释放一次地火秒杀，在玩家周围随机出现安全区域，未在安全区域直接秒杀
//	3、	每2.5分钟释放一次持续伤害，在玩家周围随机出现安全区域，未在安全区域每秒掉血10%，持续10秒
//	4、	需要玩家击杀所有小怪后，BOSS出现

	private int id;
	private Game game;
	private long startTime;
	private String endResult = "进行中";
	public int monsterNum;
	private long lastLogTime;
	CityStat stat = CityStat.进入;
	CityStat lastStat = CityStat.进入;
	private long noticeLeaveTime;
	public Map<Long, Player> pMap = new HashMap<Long, Player>();
	public CityConfig config;
	private long lastSkillTime;
	
	
	public enum CityStat{
		进入("进入副本"),
		进行中("副本进行中"),
		结束("通知结束"),
		离开("离开副本"),
		销毁("销毁副本");
		private String des;
		private CityStat(String desc){
			this.des = desc;
		}
		public String getDes() {
			return des;
		}
		public void setDes(String des) {
			this.des = des;
		}
	}
	
	public City4SystemSkill(int id,Game game){
		this.id = id;
		this.game = game;
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return "机关副本";
	}

	@Override
	public void heartbeat() {
		try {
			game.heartbeat();
		} catch (Exception e) {
			e.printStackTrace();
			BossCityManager.logger.warn("["+getName()+"] [game心跳异常] [id:"+id+"]",e);
		}
		
		switch (stat) {
		case 进入:
			if(pMap.size() > 0){
				stat = CityStat.进行中;
			}
			break;
		case 进行中:
			handleGameSkill();
			break;
		case 结束:
			stat = CityStat.离开;
			noticeLeaveTime = System.currentTimeMillis() + 5000L;
			noticeCityEnt();
			break;
		case 离开:
			if(System.currentTimeMillis() >= noticeLeaveTime){
				noticePlayerLeave();
				stat = CityStat.销毁;
			}
			break;
		}
		
		if(isEnd()){
			stat = CityStat.结束;
		}
		if(System.currentTimeMillis() - lastLogTime >= CityConstant.PRINT_LOGGER_TIMES){
			lastLogTime = System.currentTimeMillis();
			CityConstant.logger.warn("[{}] [副本状态:{}] [心跳-10秒] [副本id:{}] [怪物数:{}] [玩家数:{}] [副本结束原因:{}]",
					new Object[]{getName(),stat.getDes(), id,monsterNum,pMap.size(), endResult});
		}
		if(lastStat != stat){
			lastStat = stat;
			CityConstant.logger.warn("[{}] [副本状态:{}] [心跳-状态改变] [副本id:{}] [怪物数:{}] [副本结束原因:{}]",new Object[]{getName(),stat.getDes(), id,monsterNum,endResult});
		}
	}
	
	int lastSkillIndex = 0;
	public void handleGameSkill(){
		if(System.currentTimeMillis() - lastSkillTime >= 20 * 1000L){
			if(lastSkillIndex == 0){
				lastSkillIndex = 1;
				
				
				
				CityConstant.logger.warn("[{}] [副本状态:{}] [释放秒杀技能] [副本id:{}] [怪物数:{}]",
						new Object[]{getName(),stat.getDes(),id,monsterNum});
			}else if(lastSkillIndex == 1){
				lastSkillIndex = 0;
				CityConstant.logger.warn("[{}] [副本状态:{}] [释放buff技能] [副本id:{}] [怪物数:{}]",
						new Object[]{getName(),stat.getDes(),id,monsterNum});
			}
		}
	}
	
	public void noticeCityEnt(){
		List<Player> ps = game.getPlayers();
		if(ps != null && ps.size() > 0){
			for(Player p : ps){
				if(p != null && p.isOnline()){
					p.sendError(endResult+",5秒后将传出副本");
				}
			}
		}
	}
	
	public void noticePlayerLeave() {
		
		List<Player> ps = game.getPlayers();
		if (ps != null && ps.size() > 0) {
			for (Player p : ps) {
				if (p.isDeath()) {
					p.setHp(p.getMaxHP());
					p.setMp(p.getMaxMP());
					p.setState(Player.STATE_STAND);
					p.setPkMode(Player.和平模式);
					p.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(
							GameMessageFactory.nextSequnceNum(), (byte) 0,
							Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
					p.addMessageToRightBag(res);
					
					if(p.getTeam() != null){
						p.getTeam().setCity(null);
					}
					
					DownCityManager2.instance.cityMap.remove(new Long(p.getId()));
					String mapName = TransportData.getMainCityMap(p.getCountry());
					Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, p.getCountry());
					MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
					game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, area.getX(), area.getY()));
					CityConstant.logger.warn("["+getName()+"] [副本id:"+id+"] [副本结束] [玩家死亡离开副本] ["+p.getLogString()+"]");
				}
				CityConstant.logger.warn("["+getName()+"] [副本结束] [副本id:"+id+"] [ps:"+(ps!=null?ps.size():"null")+"] [isdead:"+p.isDeath()+"] ["+p.getLogString()+"]");
			}
		}
		CityConstant.logger.warn("["+getName()+"] [副本结束] [副本id:"+id+"] [ps:"+(ps!=null?ps.size():"null")+"] [pMap:"+pMap.size()+"]");
		
		
		for(Iterator it = pMap.values().iterator();it.hasNext();){
			Player p = (Player)it.next();
			if(p != null){
				if (p.isDeath()) {
					p.setHp(p.getMaxHP());
					p.setMp(p.getMaxMP());
					p.setState(Player.STATE_STAND);
					p.setPkMode(Player.和平模式);
					p.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(
							GameMessageFactory.nextSequnceNum(), (byte) 0,
							Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
					p.addMessageToRightBag(res);
				}
				if(p.getTeam() != null){
					p.getTeam().setCity(null);
				}
				DownCityManager2.instance.cityMap.remove(new Long(p.getId()));
				String mapName = TransportData.getMainCityMap(p.getCountry());
				Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, p.getCountry());
				MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
				game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, area.getX(), area.getY()));
				CityConstant.logger.warn("["+getName()+"] [副本id:"+id+"] [玩家离开副本] ["+p.getLogString()+"]");
			}
			it.remove();
		}
	}

	@Override
	public boolean isDestroy() {
		return stat == CityStat.销毁;
	}

	@Override
	public boolean isEnd() {
		if(System.currentTimeMillis() - startTime >= CityConstant.LAST_TIME_OF_WUDI){
			endResult = "副本时间到";
			return true;
		}
		if(game != null){
			if(pMap == null || pMap.size() == 0){
				endResult = "小队团灭";
				return true;
			}
		}
		return false;
	}

	@Override
	public void killMonster(Monster m) {
		monsterNum--;
		if(monsterNum == 0){
			Monster boss = MemoryMonsterManager.getMonsterManager().createMonster(config.getBossId());
			boss.setX(config.getBossXY()[0]);
			boss.setY(config.getBossXY()[1]);
			boss.setMonsterLocat((byte) 2);
			game.addSprite(boss);
			for(Player p : game.getPlayers()){
				p.sendError("Boss已出没，快前去击杀");
			}
		}
	}

	@Override
	public void killPlayer(Player p) {
		if(isEnd()){
			for (Player p2Player : pMap.values()) {
				if (p2Player != null && p2Player.isOnline()){
					p2Player.sendError("小队团灭，副本即将结束");
				}
			}
		}
		pMap.remove(new Long(p.getId()));
	}

	@Override
	public boolean playerInGame(Player p) {
		return pMap.containsKey(new Long(p.getId()));
	}

	@Override
	public Map<Long, Player> pMap() {
		return pMap;
	}

	@Override
	public void destory() {
		DownCityManager2.logger.warn("[{}] [副本销毁] [副本id:{}]",new Object[]{getName(), getId()});
		game.removeAllMonster();
		game = null;
	}

}
