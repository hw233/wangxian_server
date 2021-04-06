package com.fy.engineserver.event.cate;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;

public class EventPlayerDropPet extends EventOfPlayer{
	
	public Pet pet;

	public EventPlayerDropPet(Player p) {
		super(p);
	}

	@Override
	public void initId() {
		id = Event.PLAYER_DROP_PET;
	}

}
