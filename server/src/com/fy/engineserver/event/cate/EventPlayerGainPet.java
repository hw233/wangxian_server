package com.fy.engineserver.event.cate;

import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 玩家获得宠物的事件.
 * 
 * create on 2013年8月16日
 */
public class EventPlayerGainPet extends EventOfPlayer{
	
	public Pet pet;
	public PetEggProps eggProp;
	public PetProps petArticle;

	public EventPlayerGainPet(Player p) {
		super(p);
	}

	@Override
	public void initId() {
		id = PLAYER_GAIN_PET;
	}

}
