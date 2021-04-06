package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 提升境界道具
 * 
 */
public class JingjieProps extends Props{

	/**
	 * 使用传送符。
	 * 1，判断传送目标地图是不是玩家当前所在地图。如果是，发送SET_POSITION_REQ指令
	 * 2，如果不是，直接把玩家从当前地图中退出，设置跳转点，发送CHANGE_GAME_REQ指令
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(player.getClassLevel() < Player.最大境界){
			player.setClassLevel((short)(player.getClassLevel() + 1));
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.恭喜您境界提升);
			player.addMessageToRightBag(hreq);
		}
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
			if(p.getClassLevel() >= Player.最大境界){
				resultStr = Translate.已经到达最大境界;
			}
		}
		return resultStr;
	}
}
