package com.fy.engineserver.sprite.pet2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.RequestMessage;

public class Option_Clear_Prop_CD extends Option  {
	public Connection conn;
	public RequestMessage message;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		PetSubSystem.getInstance().clearPetPropCD(conn, message, player, true);
	}
}
