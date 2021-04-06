package com.fy.engineserver.activity.dig;

import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
@SimpleEntity
public class DigPropsEntity extends PropsEntity {
	private static final long serialVersionUID = 1L;

	public DigPropsEntity(long id){
		setId(id);
	}
	public DigPropsEntity(){}

	@Override
	public String getInfoShow(Player p) {
		// TODO Auto-generated method stub
		return super.getInfoShow(p);
	}
	
}
