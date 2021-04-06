package com.fy.engineserver.seal;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;

public class LeaveDownCityBar implements Callbackable{

	private Player player;
	
	public LeaveDownCityBar(){}
	
	public LeaveDownCityBar(Player player){
		this.player = player;
	}
	
	@Override
	public void callback() {
		try{
			System.out.println("====回城了");
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()));
			Game.logger.warn("[封印副本线程] [回城] [成功] ["+player.getLogString()+"]");
		}catch(Exception e){
			Game.logger.warn("[封印副本线程] [回城] [异常]"+e);
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
