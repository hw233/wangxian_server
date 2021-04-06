package com.fy.engineserver.menu.huntLife;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_SHOUHUN_LUCK_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开兽魂界面
 * 
 *
 */
public class CopyOfOption_openShouhunChouJiang extends Option implements NeedCheckPurview {

	public void doSelect(Game game,Player player){
		HuntLifeEntity he = player.getHuntLifr();
		long now = System.currentTimeMillis();
		long result = HuntLifeManager.instance.baseModel.getInterverFreeTime() - (now - he.getLastTaskFreeTime());
		if (result < 0) {
			result = 0L;
		}
		OPEN_SHOUHUN_LUCK_WINDOW_RES resp = new OPEN_SHOUHUN_LUCK_WINDOW_RES(GameMessageFactory.nextSequnceNum(), result, Translate.单抽描述, Translate.十连抽描述);
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
