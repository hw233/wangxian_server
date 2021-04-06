
package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.dig.DigPropsEntity;
import com.fy.engineserver.activity.dig.DigTemplate;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FIND_WAY_RES;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class Option_FindWay extends Option {

	private DigPropsEntity ae;

	public Option_FindWay(DigPropsEntity ae) {
		this.ae = ae;
	}
	@Override
	public void doSelect(Game game, Player player) {
		// TODO 寻路
		DigTemplate dt = player.getDigInfo().get(ae.getId());
		FIND_WAY_RES res = new FIND_WAY_RES();
		res.setIntx(dt.getPoints().x);
		res.setInty(dt.getPoints().y);
		res.setMapName(dt.getMapName());
		TaskSubSystem.logger.error(player.getLogString()+"[要寻路到"+dt.getMapName()+"]");
		player.addMessageToRightBag(res);
	}


	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}


}
