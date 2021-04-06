package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.PET_MERGE_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.PetManager;

public class Option_Pet_Store extends Option{


	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		PetSubSystem.getInstance().handlerPetHousePage(player);
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
		return Translate.服务器选项;
	}
}
