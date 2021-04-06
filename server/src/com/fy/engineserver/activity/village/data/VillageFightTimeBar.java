package com.fy.engineserver.activity.village.data;

import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.OreNPC;

public class VillageFightTimeBar implements Callbackable{

	Player player;
	OreNPC on;
	
	public VillageFightTimeBar(Player player, OreNPC on){
		this.player = player;
		this.on = on;
	}
	
	@Override
	public void callback() {
		// TODO Auto-generated method stub
		if(on != null){
			on.申请占领灵矿(player);
		}
	}

}
