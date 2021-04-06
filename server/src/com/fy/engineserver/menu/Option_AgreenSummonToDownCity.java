package com.fy.engineserver.menu;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_AgreenSummonToDownCity extends Option {
	Game targetGame;
	int x , y;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_同意副本召唤;
	}

	public void doSelect(Game game, Player player) {
		if(targetGame == null){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4969);
			player.addMessageToRightBag(req);
//			CoreSubSystem.logger.info("[同意召唤进入副本][失败][" + player.getName() + "][副本不存在]");
			if(CoreSubSystem.logger.isInfoEnabled())
				CoreSubSystem.logger.info("[同意召唤进入副本][失败][{}][副本不存在]", new Object[]{player.getName()});
			return;
		}
		
		try {
			TransportData td = new TransportData(0,0,0,0,targetGame.getGameInfo().getName(), x , y);
			game.transferGame(player, td);
			
//			CoreSubSystem.logger.info("[同意召唤进入副本][成功][" + player.getName() + "][" + targetGame.getGameInfo().getName() + "]");
			if(CoreSubSystem.logger.isInfoEnabled())
				CoreSubSystem.logger.info("[同意召唤进入副本][成功][{}][{}]", new Object[]{player.getName(),targetGame.getGameInfo().getName()});
		} catch (Exception e) {
			e.printStackTrace();
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4970);
			player.addMessageToRightBag(req);
			if(CoreSubSystem.logger.isInfoEnabled())
				CoreSubSystem.logger.info("[同意召唤进入副本][失败][" + player.getName() + "][" + targetGame.getGameInfo().getName() + "]" , e);
		}
	}
	
	public Game getTargetGame() {
		return targetGame;
	}

	public void setTargetGame(Game targetGame) {
		this.targetGame = targetGame;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
