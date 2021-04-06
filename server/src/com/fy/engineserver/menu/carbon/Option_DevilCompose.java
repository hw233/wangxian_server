package com.fy.engineserver.menu.carbon;

import java.util.Iterator;

import com.fy.engineserver.compose.ComposeManager;
import com.fy.engineserver.compose.model.ChangeColorCompose;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.DEVILSQUARE_COMPOSE_TIPS_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_DevilCompose extends Option implements NeedCheckPurview{

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		
		Iterator<String> ite = ComposeManager.instance.colorComposeMap.keySet().iterator();
		int[] succeedRate = new int[ComposeManager.instance.colorComposeMap.size()];
		int i=0;
		while(ite.hasNext()) {
			String key = ite.next();
			ChangeColorCompose cm = ComposeManager.instance.colorComposeMap.get(key);
			succeedRate[i] = cm.getSuccessRate();
			i++;
		}
		DEVILSQUARE_COMPOSE_TIPS_REQ resp = new DEVILSQUARE_COMPOSE_TIPS_REQ(GameMessageFactory.nextSequnceNum(), succeedRate, ComposeManager.instance.colorProps, ComposeManager.instance.ticketProps);
		player.addMessageToRightBag(resp);
	}
	
	
}
