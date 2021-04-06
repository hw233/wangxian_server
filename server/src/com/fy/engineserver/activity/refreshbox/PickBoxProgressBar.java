package com.fy.engineserver.activity.refreshbox;

import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;

public class PickBoxProgressBar implements Callbackable{

	private FlopCaijiNpc npc;
	private Player player;
	
	public PickBoxProgressBar(){}
	
	public PickBoxProgressBar(Player player,FlopCaijiNpc npc){
		this.player = player;
		this.npc = npc;
	}
	
	@Override
	public void callback() {
		npc.callback(player);
	}

	public FlopCaijiNpc getNpc() {
		return npc;
	}

	public void setNpc(FlopCaijiNpc npc) {
		this.npc = npc;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
