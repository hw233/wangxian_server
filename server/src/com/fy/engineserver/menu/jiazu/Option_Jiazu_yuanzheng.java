package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.BossCity;
import com.fy.engineserver.jiazu2.manager.BossConfig;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.BATTLE_GUWU_INFO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
/**
 * 家族远征
 * 
 * @create 2017-8-28 下午11:40:37
 */
public class Option_Jiazu_yuanzheng extends Option {

	
	private int x = 472;
	private int y = 1052;
	private String mapName = "zhanmotianyu";

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if(game == null || player == null){
				Game.logger.warn("[家族远征] [失败:参数错误] [game:{}] [player:{}]",new Object[]{game,player});
				return;
			}
			
			if(x == 0 || y == 0 || mapName == null){
				Game.logger.warn("[家族远征] [失败:地图参数错误] [x:{}] [y:{}] [mapName:{}] [game:{}] [player:{}]",new Object[]{x,y,mapName, game,player});
				return;
			}
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if(jiazu == null){
				player.sendError(Translate.家族不存在);
				return;
			}
			
			if(jiazu.getLevel() < 1){
				player.sendError(Translate.家族等级大于5级才可以远征);
				return;
			}
			
			if(!JiazuManager2.instance.isOpen()){
				player.sendError(Translate.远征活动即将结束);
				jiazu.setCity(null);
				jiazu.setHasKillBoss(false);
				return;
			}
			
			if(jiazu.isHasKillBoss()){
				player.sendError(Translate.boss已经被击杀);
				return;
			}
			boolean isnew = false;
			BossCity city = (BossCity) jiazu.getCity();
			synchronized (jiazu) {
				if(city == null || city.getGame() == null){
					isnew = true;
					Game newGame = createGame();
					if(newGame == null){
						player.sendError(Translate.远征活动配置错误);
						return;
					}
					
					Sprite boss = refreshBoss(jiazu);
					if(boss == null){
						player.sendError(Translate.远征活动配置错误+"!");
						return;
					}
					newGame.addSprite(boss);
					city = new BossCity(newGame,(Monster)boss,jiazu);
					city.id = JiazuManager.getInstance().nextId();
					jiazu.setCity(city);
					DownCityManager2.instance.addCityTask(city);
				}
				city.pMap().put(player.getId(), player);
				DownCityManager2.instance.cityMap.put(player.getId(),city);
			}
			player.setTransferGameCountry(0);
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, mapName, x, y));
			
			Integer currGuLi = BossCityManager.getInstance().yuanzhenGuLi.get(new Long(player.getJiazuId()));
			if(currGuLi == null){
				BossCityManager.getInstance().yuanzhenGuLi.put(player.getJiazuId(),0);
				currGuLi = BossCityManager.getInstance().yuanzhenGuLi.get(new Long(player.getJiazuId()));
			}
			String mess = Translate.translateString(Translate.下一级鼓励比例, new String[][] { { Translate.STRING_1, "2%" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(BossCityManager.getInstance().jGuLi.get(currGuLi+2) * 1000) } });
			BATTLE_GUWU_INFO_RES res = new BATTLE_GUWU_INFO_RES(GameMessageFactory.nextSequnceNum(),mess,currGuLi,currGuLi);
			player.addMessageToRightBag(res);
			
			Game.logger.warn("[家族远征] [踏入远征] [副本id:{}] [isnew:{}] [currGuLi:{}] [jiazu:{}] [{}]",new Object[]{city.id,isnew,currGuLi,jiazu.getLogString() ,player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Sprite refreshBoss(Jiazu jiazu){
		int level = jiazu.getBossLevel();
		try{
			BossConfig config = JiazuManager2.bconfigs.get(level);
			if(config == null){
				Game.logger.warn("[家族远征] [刷新boss失败:配置文件错误] [level:{}] [{}]",new Object[]{level, jiazu.getLogString()});
				return null;
			}
			int bossId = config.getId();
			Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			sprite.setX(1532);
			sprite.setY(1102);
			sprite.setBornPoint(new com.fy.engineserver.core.g2d.Point(1532, 1102));
			sprite.setMaxHP((int)config.getHp());
			Game.logger.warn("[家族远征] [刷boss成功2] [level:{}] [bossid:{}] [bid:{}] [bossName:{}] [hp:{}] [maxHP:{}] [{}]",
					new Object[]{level,bossId ,sprite.getId(), sprite.getName(), sprite.getHp(),sprite.getMaxHP(), jiazu.getLogString()});
			return sprite;
		}catch (Exception e) {
			e.printStackTrace();
			Game.logger.warn("[家族远征] [副本结束:刷boss异常2] [level:{}] [{}] [{}]",new Object[]{level,jiazu.getLogString(),e});
		}
		return null;
	}
	
	public Game createGame(){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapName);
		if(gi == null){
			Game.logger.warn("[家族远征] [newGame] [失败:对应的地图信息不存在] [{}]", new Object[]{mapName});
			return null;
		}
		try {
			Game newGame = new Game(gameManager,gi);
			newGame.init();
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			Game.logger.warn("[家族远征] [newGame] [失败:game初始化失败] [{}] [{}]", new Object[]{mapName,e});
			return null;
		}
	}
	
	
}
