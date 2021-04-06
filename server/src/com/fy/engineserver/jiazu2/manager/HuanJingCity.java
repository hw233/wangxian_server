package com.fy.engineserver.jiazu2.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.downcity.city.CityConfig;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity2.MapFlop;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;

public class HuanJingCity implements CityAction{
	
	Game game;
	Monster boss;
	Player player;
	Logger logger = DownCityManager2.logger;
	public int id;
	List<MapFlop> flops;
	CityConfig config;
	
	public HuanJingCity(Game g,Monster boss,Player player,List<MapFlop> flop,CityConfig config){
		this.game = g;
		this.boss = boss;
		this.player = player;
		this.flops = flop;
		this.config = config;
	}
	
	long lastPrintTime;
	long printLogLength = 10000;
	
	long endTime;
	private int state;
	
	public void heartbeat(){
		if(System.currentTimeMillis() - lastPrintTime >= printLogLength){
			lastPrintTime = System.currentTimeMillis();
			logger.warn("[家族幻境] [副本心跳] [副本id:{}] [人数:{}] [boss:{}] [boss血量:{}]",new Object[]{id,game.getPlayers().size(),boss.getName(),boss.getHp(),player.getLogString()});
		}
		
		try {
			game.heartbeat();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[家族幻境] [副本id:{}] [game心跳异常:{}]",new Object[]{id,e});
		}
		
//		if((boss.isDeath() || !boss.isAlive()) && (endTime == 0)){
//			logger.warn("[家族幻境] [boss死亡] [副本id:{}] [人数:{}] [boss:{}] [boss血量:] [{}]",new Object[]{id,game.getPlayers().size(),boss.getName(),boss.getHp(),player.getLogString()});
//			endTime = System.currentTimeMillis() + 15000;
//			player.sendError(Translate.pk副本时间到2);
//		}
		
		if(endTime > 0 && System.currentTimeMillis() >= endTime && state == 0){
			if(player.isDeath()){
				player.setHp(player.getMaxHP());
				player.setMp(player.getMaxMP());
				player.setState(Player.STATE_STAND);
				player.setPkMode(Player.和平模式);
				player.notifyRevived();
				PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(
						GameMessageFactory.nextSequnceNum(), (byte) 0,
						Translate.战场免费复活成功, player.getMaxHP(), player.getMaxMP());
				player.addMessageToRightBag(res);
			}
			DownCityManager2.instance.cityMap.remove(new Long(player.getId()));
			String mapName = TransportData.getMainCityMap(player.getCountry());
			Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, player.getCountry());
			MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
			game.transferGame(player, new TransportData(0, 0, 0, 0, mapName, area.getX(), area.getY()));
			state = 2;
//			game.transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()));
			logger.warn("[家族幻境] [副本结束] [副本id:{}] [game心跳异常:{}]",new Object[]{id,player.getLogString()});
		}
		
		if (game != null) {
			if (state != 1 && (pMap == null || pMap.size() == 0)) {
				logger.warn("[家族幻境] [玩家死亡] [副本id:{}] [人数:{}] [boss:{}] [boss血量:] [{}]",new Object[]{id,game.getPlayers().size(),boss.getName(),boss.getHp(),player.getLogString()});
				state = 1;
			}
		}
		
		if(state == 2){
			List<Player> ps = game.getPlayers();
			if(ps == null || ps.size() == 0){
				state = 1;
				logger.warn("[家族幻境] [id:"+id+"] [副本结束标记]");
			}
		}
	}
	
	public Game getGame(){
		return game;
	}
	
	@Override
	public boolean isEnd() {
		if(state == 0){
			return false;
		}
		return true;
	}

	@Override
	public void destory() {
		logger.warn("[家族幻境] [副本销毁] [副本id:{}] [boss:{}] [角色:{}]",new Object[]{getId(),boss.getName(), player.getLogString()});
		game.removeAllMonster();
		game = null;
		boss = null;
		pMap.clear();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return "家族幻境";
	}

	@Override
	public boolean isDestroy() {
		return state == 1;
	}

	List<MapFlop> doFlop = new ArrayList<MapFlop>();
	List<MapFlop> giveFlop = new ArrayList<MapFlop>();  
	Random random = new Random();
	@Override
	public void killMonster(Monster m) {
		if(m == null || boss == null){
			logger.warn("[{}] [副本id:{}] [怪物死亡] [boss:{}]  [错误：怪物不存在]",new Object[]{getName(),getId(),boss});
			return;
		}
		doFlop.clear();
		giveFlop.clear();
		//logger.warn("[{}] [副本id:{}] [怪物死亡] [怪id:{}] [怪物:{}] [boss:{}] [flops:{}]",new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),boss.getName(),flops.size()});
		if(m.getSpriteCategoryId() == boss.getSpriteCategoryId()){
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
				DownCityManager2.instance.handleMonsterFlop(game,giveFlop,player, m);
			}
			
			endTime = System.currentTimeMillis() + 15000;
			player.sendError(Translate.pk副本结束);
			
			logger.warn("[{}] [副本id:{}] [Boss死亡] ]",
					new Object[]{getName()});
		}else{

			boolean hasRightMonster = false;
			int [][] monsterIds = config.getMonsterIds();
			for(int i=0;i<monsterIds.length;i++){
				int [] ids = monsterIds[i];
				for(int j=0;j<ids.length;j++){
					if(ids[j] == m.getSpriteCategoryId()){
						hasRightMonster = true;
					}
				}
			}
			if(hasRightMonster == false){
				logger.warn("[{}] [副本id:{}] [小怪死亡:召唤生物不掉落] [怪id:{}] [怪物:{}][boss:{}] [最终给-monster数据-总数据{}]",
						new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),boss.getName(),giveFlop.size()+"/"+ doFlop.size()+"/"+flops.size()});
				return;
			}
			
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
				DownCityManager2.instance.handleMonsterFlop(game,giveFlop,player, m);
			}
			
			logger.warn("[{}] [副本id:{}] [小怪死亡] [怪id:{}] [怪物:{}] [掉落个数:{}] [掉落概率:{}] [物品总概率:{}] [boss:{}] [最终给-monster数据-总数据{}]",
					new Object[]{getName(),getId(),m.getSpriteCategoryId(),m.getName(),flopNum,ranFlop+"/"+totleFlop,articleTotleFlop, boss.getName(),giveFlop.size()+"/"+ doFlop.size()+"/"+flops.size()});
		
		}
	}

	@Override
	public void killPlayer(Player p) {
		endTime = System.currentTimeMillis() + 2000;
		player.sendError("您已死亡，副本即将结束");
		logger.warn("[{}] [副本id:{}] [玩家死亡] ]",
				new Object[]{getName(),id});
	}
	public Map<Long, Player> pMap = new HashMap<Long, Player>();
	@Override
	public Map<Long, Player> pMap() {
		return pMap;
	}

	@Override
	public boolean playerInGame(Player p) {
		return pMap.containsKey(new Long(p.getId()));
	}
	
}
