package com.fy.engineserver.menu.huntLife;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.TAKE_SHOUHUN_LUCK_REQ;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.ResponseMessage;
/**
 * 兽魂抽奖二次确认
 * 
 *
 */
public class Option_ConfirmChouJiang extends Option{
	/** 0单抽  1十连抽 */
	private int choujiangType;

	public void doSelect(Game game,Player player){
		TAKE_SHOUHUN_LUCK_REQ req = new TAKE_SHOUHUN_LUCK_REQ(GameMessageFactory.nextSequnceNum(), choujiangType);
		ResponseMessage resp = null;
		if (choujiangType == 0) {
			resp = HuntLifeManager.instance.onceHunt(player, req);
		} else if (choujiangType == 1) {	//十连抽
			resp = HuntLifeManager.instance.tenHunt(player, req);
		}
		if (resp != null) {
			player.addMessageToRightBag(resp);
		}
		
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public String toString(){
		return Translate.服务器选项;
	}

	public int getChoujiangType() {
		return choujiangType;
	}

	public void setChoujiangType(int choujiangType) {
		this.choujiangType = choujiangType;
	}
}
