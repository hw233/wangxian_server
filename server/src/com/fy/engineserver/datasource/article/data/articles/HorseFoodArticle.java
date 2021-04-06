package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.props.Gem;
import com.fy.engineserver.sprite.horse.Horse;


public class HorseFoodArticle extends Article implements Gem{

	public static int white = 60;
	public static int green = 120;
	public static int blue = 180;
	public static int purple = 240;
	public static int orange = 300;
	
	// i*60 白1 
	
	private byte type;
	private int energy;
	
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	
	public void addProperty(Horse horse){
		
		int max = horse.getMaxEnergy();
		int now = horse.getEnergy() + this.energy;
		if(now >= max){
			horse.setEnergy(max);
		}else{
			horse.setEnergy(now);
		}
	}
	
}
