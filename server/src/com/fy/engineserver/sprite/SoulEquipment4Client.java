package com.fy.engineserver.sprite;

/**
 * 元神的装备信息-发给客户端用
 * 
 *
 */
public class SoulEquipment4Client {
	private int soulTyp;
	private long[] equipment;

	public SoulEquipment4Client() {

	}

	public SoulEquipment4Client(int soulTyp, long[] equipment) {
		this.soulTyp = soulTyp;
		this.equipment = equipment;
	}

	public int getSoulTyp() {
		return soulTyp;
	}

	public void setSoulTyp(int soulTyp) {
		this.soulTyp = soulTyp;
	}

	public long[] getEquipment() {
		return equipment;
	}

	public void setEquipment(long[] equipment) {
		this.equipment = equipment;
	}

}
