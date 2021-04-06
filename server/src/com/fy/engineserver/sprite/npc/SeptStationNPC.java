package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.cache.Cacheable;

public class SeptStationNPC extends GroundNPC implements Cacheable {

	private static final long serialVersionUID = 1L;

	/** 所属家族ID */
	private long septId;

	public SeptStationNPC() {

	}

	public SeptStationNPC(String[] avatas) {
		this.setAvatas(avatas);
	}

	@Override
	public byte getNpcType() {
		return Sprite.NPC_TYPE_GROUND;
	}

	@Override
	public int getSize() {
		return 0;
	}

	public long getSeptId() {
		return septId;
	}

	public void setSeptId(long septId) {
		this.septId = septId;
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	@Override
	public Object clone() {
		SeptStationNPC npc = new SeptStationNPC();
		npc.cloneAllInitNumericalProperty(this);
		npc.setnPCCategoryId(this.getnPCCategoryId());
		npc.setAvatas(this.getAvatas());
		npc.setBuildingAvatas(this.getBuildingAvatas());
		return npc;
	}
}
