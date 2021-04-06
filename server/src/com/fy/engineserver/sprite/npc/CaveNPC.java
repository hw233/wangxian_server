package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.sprite.Sprite;

public class CaveNPC extends GroundNPC implements com.xuanzhi.tools.cache.Cacheable {

	private static final long serialVersionUID = 1L;

	/** 所属的家园 */
	private Cave cave;

	public CaveNPC() {

	}

	public CaveNPC(String[] avatas) {
		this.setAvatas(avatas);
	}

	@Override
	public byte getNpcType() {
		return Sprite.NPC_TYPE_GROUND;
	}

	public Cave getCave() {
		return cave;
	}

	public void setCave(Cave cave) {
		this.cave = cave;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	@Override
	public void setTitle(String value) {
		// TODO Auto-generated method stub
		super.setTitle(value);
	}

	@Override
	public Object clone() {
		CaveNPC npc = new CaveNPC();
		npc.cloneAllInitNumericalProperty(this);
		npc.setAvatas(this.getAvatas());
		npc.setWindowId(this.getWindowId());
		npc.setBuildingAvatas(this.getBuildingAvatas());
		return npc;
	}
}
