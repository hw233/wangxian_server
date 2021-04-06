package com.fy.engineserver.core;

import org.w3c.dom.Element;

import com.fy.engineserver.core.res.GameMap;

/**
 * 表示一个地图的一些静态信息：
 * 
 * 包括地图的名字，地图的描述，地图的属性 地图的数据等等
 * 
 */
public class GameInfo extends GameMap {
	protected int maxPlayerNum = 1024;

	protected int maxQueueSize = maxPlayerNum * 64;

	protected long maxCorpseLifeTime = 2 * 60 * 1000L;

	protected int maxFindingPathStep = 512;

	protected Element monsterBornEle;
	protected Element npcBornEle;

	protected int ejectA = 8;
	protected int ejectB = 8;

	public int getMaxQueueSize() {
		return maxQueueSize;
	}

	public void setMaxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	public int getMaxPlayerNum() {
		return maxPlayerNum;
	}

	public void setMaxPlayerNum(int maxPlayerNum) {
		this.maxPlayerNum = maxPlayerNum;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public int getCellW() {
		return 16;
	}

	public int getCellH() {
		return 16;
	}

}
