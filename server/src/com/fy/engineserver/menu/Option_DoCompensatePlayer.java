package com.fy.engineserver.menu;

import com.fy.engineserver.compensation.CompensationManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_DoCompensatePlayer extends Option {
	public void doSelect(Game game, Player player) {
		CompensationManager cm = CompensationManager.getInstance();
		
		try {
			int yuanBaoCount = cm.compensate(player);
			
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5208+yuanBaoCount+Translate.text_5209);
			player.addMessageToRightBag(hreq);
			
//			Game.logger.info("[发放补偿] [成功] ["+player.getId()+"] ["+yuanBaoCount+"] [元宝]");
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[发放补偿] [成功] [{}] [{}] [元宝]", new Object[]{player.getId(),yuanBaoCount});
		} catch (Exception e) {
//			Game.logger.info("[发放补偿] [失败] ["+player.getId()+"] ["+e.getMessage()+"]");
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[发放补偿] [失败] [{}] [{}]", new Object[]{player.getId(),e.getMessage()});
			
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5210);
			player.addMessageToRightBag(hreq);
			
			e.printStackTrace();
		}
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_发放补偿;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
