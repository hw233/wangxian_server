package com.fy.engineserver.septstation;

/**
 * 家族地表形式NPC配置
 * 
 * 
 */
public class GroundNpcStation {
	private int dependIndex;
	private long npcId;
	private int x;
	private int y;

	public GroundNpcStation(int dependIndex, long npcId, int x, int y) {
		this.dependIndex = dependIndex;
		this.npcId = npcId;
		this.x = x;
		this.y = y;
	}

	public int getDependIndex() {
		return dependIndex;
	}

	public void setDependIndex(int dependIndex) {
		this.dependIndex = dependIndex;
	}

	public long getNpcId() {
		return npcId;
	}

	public void setNpcId(long npcId) {
		this.npcId = npcId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "GroundNpcStation [dependIndex=" + dependIndex + ", npcId=" + npcId + ", x=" + x + ", y=" + y + "]";
	}
}
