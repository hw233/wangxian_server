package com.fy.engineserver.menu.articleProtect;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_NewJianDing_OK extends Option {

	private EquipmentEntity equip;
	private ArticleEntity prop;
	
	public Option_NewJianDing_OK(){
		
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			ArticleManager.getInstance().确定新鉴定(player, equip, prop);
		}catch(Exception e) {
			ArticleManager.logger.error("新鉴定出错", e);
		}
	}

	public void setEquip(EquipmentEntity equip) {
		this.equip = equip;
	}

	public EquipmentEntity getEquip() {
		return equip;
	}

	public void setProp(ArticleEntity prop) {
		this.prop = prop;
	}

	public ArticleEntity getProp() {
		return prop;
	}
}
