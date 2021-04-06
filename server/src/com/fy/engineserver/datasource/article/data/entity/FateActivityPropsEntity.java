package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;


@SimpleEntity(name="FAPEntity")
public class FateActivityPropsEntity extends PropsEntity {

	
	private static final long serialVersionUID = 641877525288047078L;
	
	//仙缘  论道
	private byte activityType;

	public FateActivityPropsEntity(){}
	public FateActivityPropsEntity(long id){
		super(id);
	}
	@Override
	public String getInfoShow(Player p) {
		return super.getInfoShow(p);
	}
	@Override
	public int getSize() {
		return super.getSize();
	}
	
	public byte getActivityType() {
		return activityType;
	}
	public void setActivityType(byte activityType) {
		this.activityType = activityType;
	}

}
