package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_biYiLing extends Option{
	String giName;

	int gameCountryType;
	
	Player usePlayer;
	
	int x;
	int y;

	public Player getUsePlayer() {
		return this.usePlayer;
	}

	public void setUsePlayer(Player usePlayer) {
		this.usePlayer = usePlayer;
	}

	public Option_biYiLing(String name, int gameCountryType, int x, int y){
		super();
		this.gameCountryType = gameCountryType;
		giName = name;
		this.x = x; 
		this.y = y;
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
//			if(JJCManager.isJJCBattle(usePlayer.getCurrentGame().gi.name)){
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.不能前往混元至圣);
//				usePlayer.addMessageToRightBag(hreq);
//				return;
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		MarriageManager.getInstance().确定召唤(player, gameCountryType, giName, x, y);
	}
}
