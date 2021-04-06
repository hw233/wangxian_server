/**
 * 
 */
package com.fy.engineserver.downcity.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity2.MapFlop;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * @author Administrator
 * 
 */
public class City4Basic implements CityAction {

	// 1、 全部小怪死完后，boss去除无敌

	private int id;
	private Game game;
	private long startTime;
	private Monster boss;
	private String endResult = "进行中";
	private long lastLogTime;
	private long noticeLeaveTime;

	public List<MapFlop> flops = new ArrayList<MapFlop>();
	
	CityStat stat = CityStat.进入;
	CityStat lastStat = CityStat.进入;

	// 奖励是怪物掉落，最后的额外结算需重新加
	public Map<Long, Player> pMap = new HashMap<Long, Player>();

	public enum CityStat {
		进入("进入副本"), 进行中("副本进行中"), 结束("通知结束"), 离开("离开副本"), 销毁("销毁副本");
		private String des;

		private CityStat(String desc) {
			this.des = desc;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}
	}

	public City4Basic(int id) {
		this.id = id;
		startTime = System.currentTimeMillis();
	}


	@Override
	public Game getGame() {
		return game;
	}

	public void setBos(Monster b) {
		this.boss = b;
	}

	public void setGame(Game b) {
		this.game = b;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return "基础副本";
	}

	public int monsterNum_pt;
	public int monsterNum_jy;
	public int monsterNum_boss;

	List<MapFlop> doFlop = new ArrayList<MapFlop>();
	List<MapFlop> giveFlop = new ArrayList<MapFlop>();  
	Random random = new Random();
	
	@Override
	public void killMonster(Monster m) {
		CityConstant.logger.warn("["+getName()+"] [杀死怪物] [id:"+m.getId()+"] [name:"+m.getName()+"] [monsterNum_pt:"+monsterNum_pt+"] [monsterNum_jy:"+monsterNum_jy+"] [monsterNum_boss:"+monsterNum_boss+"]");
		if (m.getName().contains("首领")) {
			monsterNum_jy--;
		} else if (m.getId() == boss.getId()) {
			monsterNum_boss--;
		} else {
			monsterNum_pt--;
		}
		Player flopPlayer = null;
		if (game.getPlayers() != null && game.getPlayers().size() > 0) {
			for (Player p : game.getPlayers()) {
				if (p != null && p.isOnline()) {
					flopPlayer = p;
					p.sendError(m.getName() + "被击杀");
				}
			}
		}
		
		//掉落

		if(m == null || flopPlayer == null){
			CityConstant.logger.warn("[{}] [副本id:{}] [flopPlayer:{}] [怪物死亡] [错误：怪物不存在]",new Object[]{getName(),getId(),flopPlayer});
			return;
		}
		doFlop.clear();
		giveFlop.clear();
		//logger.warn("[{}] [副本id:{}] [怪物死亡] [怪id:{}] [怪物:{}] [boss:{}] [flops:{}]",new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),boss.getName(),flops.size()});
		if(m.getName().equals(boss.getName())){
			int ranFlop = 0;
			int totleFlop = 0;
			int flopNum = 0;
			
			int articleTotleFlop = 0;
			for(MapFlop f : flops){
				if(f.getMonsterType() == 1){
					doFlop.add(f);
					articleTotleFlop += f.getArticleFlop();
					if(flopNum == 0){
						for(int i=0;i<f.getBossArticleFlop().length;i++){
							totleFlop += f.getBossArticleFlop()[i];
						}
						ranFlop = random.nextInt(totleFlop);
						int count = 0;
						for(int i=0;i<f.getBossArticleFlop().length;i++){
							count += f.getBossArticleFlop()[i];
							if(count >= ranFlop){
								flopNum = i + 1;
								break;
							}
						}
					}
				}
			}
			 
			while(giveFlop.size() < flopNum){
				int articleRanFlop = random.nextInt(articleTotleFlop);
				int calFlop = 0;
				for(MapFlop f : doFlop){
					calFlop += f.getArticleFlop();
					if(calFlop >= articleRanFlop){
						giveFlop.add(f);
						break;
					}
				}
			}
			
			if(giveFlop.size() > 0){
				DownCityManager2.instance.handleMonsterFlop(game,giveFlop,flopPlayer, m);
			}
			
//			CityConstant.logger.warn("[{}] [副本id:{}] [Boss死亡] [怪id:{}] [怪物:{}] [掉落个数:{}] [掉落概率:{}] [物品总概率:{}] [boss:{}] [最终给-boss数据-总数据{}]",
//					new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),flopNum,ranFlop+"/"+totleFlop,articleTotleFlop, boss.getName(),giveFlop.size()+"/"+ doFlop.size()+"/"+flops.size()});
			CityConstant.logger.warn("[{}] [副本id:{}] ",
					new Object[]{getName(),getId()});
		}else{

			int ranFlop = 0;
			int totleFlop = 0;
			int flopNum = 0;
			
			int articleTotleFlop = 0;
			for(MapFlop f : flops){
				if(f.getMonsterType() == 0){
					doFlop.add(f);
					articleTotleFlop += f.getArticleFlop();
					if(flopNum == 0){
						for(int i=0;i<f.getMonsterArticleFlop().length;i++){
							totleFlop += f.getMonsterArticleFlop()[i];
						}
						ranFlop = random.nextInt(totleFlop);
						int count = 0;
						for(int i=0;i<f.getMonsterArticleFlop().length;i++){
							count += f.getMonsterArticleFlop()[i];
							if(count >= ranFlop){
								flopNum = i + 1;
								break;
							}
						}
					}
				}
			}
			 
			while(giveFlop.size() < flopNum){
				int articleRanFlop = random.nextInt(articleTotleFlop);
				int calFlop = 0;
				for(MapFlop f : doFlop){
					calFlop += f.getArticleFlop();
					if(calFlop >= articleRanFlop){
						giveFlop.add(f);
						break;
					}
				}
			}
			
			if(giveFlop.size() > 0){
				DownCityManager2.instance.handleMonsterFlop(game,giveFlop,flopPlayer, m);
			}
			
			CityConstant.logger.warn("[{}] [副本id:{}] [小怪死亡] [怪id:{}] [怪物:{}] [掉落个数:{}] [掉落概率:{}] [物品总概率:{}] [boss:{}] [最终给-monster数据-总数据{}]",
					new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),flopNum,ranFlop+"/"+totleFlop,articleTotleFlop, boss.getName(),giveFlop.size()+"/"+ doFlop.size()+"/"+flops.size()});
		
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

	private void handleWuDi() {
		if(monsterNum_jy == 0 && monsterNum_pt == 0 && boss.isInvulnerable()){
			boss.setInvulnerable(false);
			boss.setParticleName("");
			CityConstant.logger.warn("[{}] [改变boss无敌状态] [boss无敌时间到] [副本id:{}] [id:{}] [name:{}] [overJY:{}] [overPT:{}]",
							new Object[] {getName(),id,boss.getSpriteCategoryId() + "/"+ boss.getId(), boss.getName(),monsterNum_jy, monsterNum_pt });
		
		}
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
			if (pMap.size() > 0) {
				stat = CityStat.进行中;
			}
			break;
		case 进行中:
			handleWuDi();
			break;
		case 结束:
			stat = CityStat.离开;
			noticeLeaveTime = System.currentTimeMillis() + 15000L;
			noticeCityEnt();
			break;
		case 离开:
			if (System.currentTimeMillis() >= noticeLeaveTime) {
				noticePlayerLeave();
				stat = CityStat.销毁;
			}
			break;
		}
		if (isEnd() && noticeLeaveTime == 0) {
			stat = CityStat.结束;
		}
		if (System.currentTimeMillis() - lastLogTime >= CityConstant.PRINT_LOGGER_TIMES) {
			lastLogTime = System.currentTimeMillis();
			CityConstant.logger
					.warn(
							"[{}] [副本状态:{}] [心跳-10秒] [副本id:{}] [小怪数:{}] [首领数:{}] [boss数:{}] [玩家数:{}] [bossXY:{}] [副本结束原因:{}]",
							new Object[] {
									getName(),
									stat.getDes(),
									id,
									monsterNum_pt,
									monsterNum_jy,
									monsterNum_boss,
									pMap.size(),
									boss.getX() + "/" + boss.getY() + "/"
											+ boss.isDeath(), endResult });
		}
		if (lastStat != stat) {
			lastStat = stat;
			CityConstant.logger.warn(
					"[{}] [副本状态:{}] [心跳-状态改变] [副本id:{}] [副本结束原因:{}]",
					new Object[] { getName(), stat.getDes(), id, endResult });
		}
	}

	@Override
	public boolean isDestroy() {
		return stat == CityStat.销毁;
	}

	public void noticeCityEnt() {
		List<Player> ps = game.getPlayers();
		if (ps != null && ps.size() > 0) {
			for (Player p : ps) {
				if (p != null && p.isOnline() && playerInGame(p)) {
					p.sendError(endResult + ",15秒后将传出副本");
				}
			}
		}
	}

	public void noticePlayerLeave() {
		//
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
				CityConstant.logger.warn("["+getName()+"] [副本id:"+id+"] [副本结束] [玩家离开副本] ["+p.getLogString()+"]");
			}
			it.remove();
		}
	}

	@Override
	public boolean isEnd() {
		if (System.currentTimeMillis() - startTime >= CityConstant.LAST_TIME_OF_WUDI) {
			endResult = "副本时间到";
			return true;
		}
		if (monsterNum_boss <= 0) {
			endResult = "boss死亡";
			return true;
		}
		if (game != null) {
			if (pMap == null || pMap.size() == 0) {
				endResult = "小队团灭";
				return true;
			}
		}
		return false;
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
		game.removeAllMonster();
		game = null;
		boss = null;
	}

}
