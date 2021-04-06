package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Marriage_req extends Option{

	private Player from;			//发请求的玩家
	private byte reqType;				//0愿意  1不愿意
	
	public Option_Marriage_req(Player from, byte type){
		super();
		this.from = from;
		this.reqType = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().option_marriageReq(from, player, reqType);
	}
}
