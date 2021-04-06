package com.fy.engineserver.sprite;

public class EntitySummoned extends AbstractSummoned {

	public long getId(){
		return id;
	}
	
	@Override
	public byte getClassType() {
		return 2;
	}
}
