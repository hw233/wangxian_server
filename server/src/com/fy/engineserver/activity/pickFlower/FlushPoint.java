package com.fy.engineserver.activity.pickFlower;

public class FlushPoint {

	
	 public String gameMap;
	 public byte country;
	 public int x;
	 public int y;
	
	
	public FlushPoint(String map,byte country,int x,int y){
		this.gameMap = map;
		this.country = country;
		this.x = x;
		this.y = y;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		FlushPoint fp = (FlushPoint) obj;
		if(fp.gameMap.equals(this.gameMap) && fp.country == this.country && fp.x == this.x && fp.y == this.y){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
	
	
}
