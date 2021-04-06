package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.PET_STRONG_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 宠物强化确认窗口
 * 
 * 
 *
 */
public class Option_PetStrongConfirm extends Option{

	PET_STRONG_REQ req;

	public PET_STRONG_REQ getReq() {
		return req;
	}

	public void setReq(PET_STRONG_REQ req) {
		this.req = req;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		PetManager am = PetManager.getInstance();
		if(am != null){
			if(req == null){
				return;
			}
			am.confirmPetStrongReq(player, req.getPetEntityId(), req.getStrongMaterialID(), req.getSequnceNum(), req.getStrongType());
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_绑定;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_472;
	}
}
