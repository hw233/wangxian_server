package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 确定花费金币炼化
 * @author Administrator
 *
 */
public class Option_Repair_Confirm extends Option {
	
	
	private Player player;
	private Pet pet;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		PetManager.getInstance().repairPet(player, pet, true, 0,true);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	
	
}


