package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 传送符
 * 
 */
public class TransferPaper extends Props{

	/**
	 * 使用传送符。
	 * 1，判断传送目标地图是不是玩家当前所在地图。如果是，发送SET_POSITION_REQ指令
	 * 2，如果不是，直接把玩家从当前地图中退出，设置跳转点，发送CHANGE_GAME_REQ指令
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		if(!super.use(game,player,ae)){
			return false;
		}
		game.transferGame(player, new TransportData(0,0,0,0,player.getHomeMapName(),player.getHomeX(),player.getHomeY()));
		return true;
	}
	
	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {
		
		String resultStr = super.canUse(p);
		if(resultStr == null){
			GameManager gm = GameManager.getInstance();
			Game currentGame = null;
			Game transferGame = null;
			if(p.getGame() != null){
				currentGame = p.getCurrentGame();
				if(currentGame == null){
					return Translate.text_3559;
				}
			}else{
				return Translate.text_3559;
			}
			if(p.getHomeMapName() != null){
				transferGame = gm.getGameByName(p.getHomeMapName(),p.getCountry());
				if(transferGame == null){
					transferGame = gm.getGameByName(p.getHomeMapName(),0);
				}
				if(transferGame == null){
					return Translate.text_3847;
				}
			}else{
				return Translate.text_3847;
			}
			if(!transferGame.equals(currentGame)){
				if (transferGame.getNumOfPlayer() >= transferGame.gi.getMaxPlayerNum()) {
					resultStr = p.getHomeMapName()+Translate.text_3564;
				}
			}
		}
		return resultStr;
	}
}
