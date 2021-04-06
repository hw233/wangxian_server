package com.fy.engineserver.activity.explore;

public enum ExplorePosition {
	
	很远 ((byte)0),
	附近((byte)1),
	正上方((byte)2),
	右上方((byte)3),
	右边((byte)4),
	右下方((byte)5),
	正下方((byte)6),
	左下方((byte)7),
	左边((byte)8),
	左上方((byte)9);
	
	public byte position;
	
	ExplorePosition(byte position){
		this.position = position;
	}
	
	
}
