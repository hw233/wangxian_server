package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_DeliveToSamePlaceWithLevelLimit extends Option {
	//目地地
	String destinationMapName;
	String destinationAreaName;
	int level;
	
	/**
	 * 指定每天可以传送的时间段，当前格式为
	 * hh:mm:ss-hh:mm:ss
	 */
	String timeInterval;
	public String getDestinationMapName() {
		return destinationMapName;
	}
	public void setDestinationMapName(String destinationMapName) {
		this.destinationMapName = destinationMapName;
	}
	public String getDestinationAreaName() {
		return destinationAreaName;
	}
	public void setDestinationAreaName(String destinationAreaName) {
		this.destinationAreaName = destinationAreaName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_传送到某地有等级限制;
	}
	
	public void doSelect(Game game,Player player){}
}
