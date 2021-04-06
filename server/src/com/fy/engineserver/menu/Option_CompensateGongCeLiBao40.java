package com.fy.engineserver.menu;

import com.fy.engineserver.compensation.CompensationManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_CompensateGongCeLiBao40 extends Option {
	public void doSelect(Game game, Player player) {
		CompensationManager cm = CompensationManager.getInstance();
		long playerId = player.getId();
		
		if(cm.isCompensatedGongCeLiBao40(playerId)){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5158);
			player.addMessageToRightBag(hreq);
			
			return;
		}
		
		if(!cm.hasConsumedGongCeLiBao40(playerId)){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5159);
			player.addMessageToRightBag(hreq);
			
			return;
		}
		
		try {
			cm.compensateGongCeLiBao40(player);
			
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5160+CompensationManager.COMPENSATE_GONG_CE_LIBAO_ARTICLE_NAME+Translate.text_5161);
			player.addMessageToRightBag(hreq);
			
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[领取] [] [] [] [] []");
		} catch (Exception e) {
			e.printStackTrace();
			
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5162);
			player.addMessageToRightBag(hreq);
		}
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_补偿40级公测大礼包;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
