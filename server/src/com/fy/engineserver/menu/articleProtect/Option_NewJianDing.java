package com.fy.engineserver.menu.articleProtect;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.concrete.ReadEquipmentExcelManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_JIANDING_MSG_RES;
import com.fy.engineserver.sprite.Player;

public class Option_NewJianDing extends Option {

	public Option_NewJianDing(){
		
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			NEW_JIANDING_MSG_RES res = new NEW_JIANDING_MSG_RES(GameMessageFactory.nextSequnceNum(), Translate.新鉴定说明1, Translate.新鉴定说明2, ReadEquipmentExcelManager.endowmentsNewNames, ReadEquipmentExcelManager.endowmentsNewValues_int, ReadEquipmentExcelManager.endowmentsNewPropNames);
			player.addMessageToRightBag(res);
		}catch(Exception e) {
			ArticleManager.logger.error("新鉴定出错", e);
		}
	}
}
