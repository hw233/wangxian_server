package com.fy.engineserver.menu.trade;

import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CLIFFORD_START_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_Clifford_Choose extends Option {

	public Option_Clifford_Choose(){
		
	}
	
	private CLIFFORD_START_REQ req;
	private int type ;
	// type 0为 银子  1为 银票
	public Option_Clifford_Choose(CLIFFORD_START_REQ req, int type) {
		super();
		this.type = type;
		this.req = req;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		CliffordManager.getInstance().greenCliffordChoose.put(player.getId(), type);
		CliffordManager.getInstance().打开祈福界面(req, player, false);
	}
	
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}

	public void setReq(CLIFFORD_START_REQ req) {
		this.req = req;
	}

	public CLIFFORD_START_REQ getReq() {
		return req;
	}
}
