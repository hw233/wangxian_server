package com.fy.engineserver.menu.huntLife;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.Shouhun4Client;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_SHOUHUN_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开兽魂界面
 * 
 *
 */
public class Option_openHuntlife extends Option implements NeedCheckPurview {

	public void doSelect(Game game,Player player){
		int len = HuntLifeManager.命格属性类型.length;
		Shouhun4Client[] shouhunModels = new Shouhun4Client[len];
		for (int i=0; i<len; i++) {
			shouhunModels[i] = HuntLifeManager.instance.createModel4Client(player, HuntLifeManager.命格属性类型[i]);
		}
		OPEN_SHOUHUN_WINDOW_RES resp = new OPEN_SHOUHUN_WINDOW_RES(GameMessageFactory.nextSequnceNum(), shouhunModels, HuntLifeManager.对应兽魂道具名);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public String toString(){
		return Translate.服务器选项;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return player.getLevel() >= 110;
	}
}
