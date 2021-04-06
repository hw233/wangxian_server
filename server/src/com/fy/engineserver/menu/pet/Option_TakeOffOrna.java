package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PUTON_PET_ORNA_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 确认脱宠物饰品
 *
 */
public class Option_TakeOffOrna extends Option{

	private long ornaId;
	
	private long petId;
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		String result = PetManager.getInstance().putOnOrnaments(player, petId, ornaId, true);
		if (result != null && !result.isEmpty()) {
			player.sendError(result);
		}
		Pet pet = PetManager.getInstance().getPet(petId);
		PUTON_PET_ORNA_RES resp = new PUTON_PET_ORNA_RES(GameMessageFactory.nextSequnceNum(), petId, pet.getOrnaments()[0]);
		player.addMessageToRightBag(resp);
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[宠物饰品穿脱]1 [" + player.getLogString() + "] [RESULT:" + result + "] [petId:" + petId + "] [ornaId:" + ornaId + "]");
		}
	}

	public long getOrnaId() {
		return ornaId;
	}

	public void setOrnaId(long ornaId) {
		this.ornaId = ornaId;
	}

	public long getPetId() {
		return petId;
	}

	public void setPetId(long petId) {
		this.petId = petId;
	}
	
	
}
