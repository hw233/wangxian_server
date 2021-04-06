package com.fy.engineserver.menu.pet;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET_DAO_DATA_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.sprite.petdao.PetData;

/**
 * 进入宠物迷城
 * 
 *
 */
public class Option_Enter_Pet_Dao extends Option{

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		List<PetData> datas = PetDaoManager.clientneewdata;
		if(datas==null){
			datas = new ArrayList<PetData>();
		}
		
		for(PetData pd:datas){
			pd.setIsopen(PetDaoManager.getInstance().isOpen(pd.getLevel(), pd.getDaotype()));
		}
		
		PET_DAO_DATA_RES res = new PET_DAO_DATA_RES(GameMessageFactory.nextSequnceNum(), datas.toArray(new PetData[]{}));
		player.addMessageToRightBag(res);	
		PetDaoManager.log.warn("[宠物迷城] [请求迷城基础数据] [datas:"+datas.size()+"] [OK] [玩家："+player.getName()+"]");
	}
}
