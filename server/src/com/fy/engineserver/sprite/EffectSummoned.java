package com.fy.engineserver.sprite;

public class EffectSummoned extends AbstractSummoned {
	
	public static final byte STATE_MOVE = 0;
	public static final byte STATE_EXPLODE = 1;
	
	private static int summonedId = 10000;

	
	public static int nextEffectSummonedId(){
		return summonedId++;
	} 
	
	@Override
	public byte getClassType() {
		return 3;
	}
}
