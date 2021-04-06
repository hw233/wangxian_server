package com.fy.engineserver.menu;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_EnterDownCity extends Option {
	String downCityName;

	public String getDownCityName() {
		return downCityName;
	}

	public void setDownCityName(String downCityName) {
		this.downCityName = downCityName;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_进入副本;
	}

	public void doSelect(Game game, Player player) {

		
		DownCityManager dcm = DownCityManager.getInstance();
		DownCity dc = dcm.enterDownCity(player, downCityName);
		
		if(dc != null){
			DownCityInfo info = dc.getDi();
			
			try {
				TransportData td = new TransportData(0,0,0,0,info.getMapName(), info.getX() ,info.getY());
				game.transferGame(player, td);
//				CoreSubSystem.logger.info("[进入副本][" + downCityName + "][成功][" + player.getName() + "]");
				if(CoreSubSystem.logger.isInfoEnabled())
					CoreSubSystem.logger.info("[进入副本][{}][成功][{}]", new Object[]{downCityName,player.getName()});
			} catch (Exception e) {
				e.printStackTrace();
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4970);
				player.addMessageToRightBag(req);
				
				if(CoreSubSystem.logger.isInfoEnabled())
					CoreSubSystem.logger.info("[进入副本][" + downCityName + "][失败][" + player.getName() + "]" , e);
			}
		}
	}
	
	
}
