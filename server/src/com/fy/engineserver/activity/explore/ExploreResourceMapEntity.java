package com.fy.engineserver.activity.explore;

import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;


@SimpleEntity
public class ExploreResourceMapEntity extends PropsEntity {

	
	private static final long serialVersionUID = 1L;



	public ExploreResourceMapEntity(){	
	}
	public ExploreResourceMapEntity(long id){
		setId(id);
	}
	
	
	

	@Override
	public String getInfoShow(Player player) {
		
		return super.getInfoShow(player);
	}

}
