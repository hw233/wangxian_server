package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.base.ActivityLingQuProp;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_getProp extends Option implements NeedCheckPurview {

	private String propName;
	
	public Option_getProp() {};
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		ActivityLingQuProp.instance.option_getProp(player, propName);
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}

	@Override
	public boolean canSee(Player player) {
		if (ActivityLingQuProp.instance.props.contains(propName)){
			return true;
		}
		return false;
	}
	
}
