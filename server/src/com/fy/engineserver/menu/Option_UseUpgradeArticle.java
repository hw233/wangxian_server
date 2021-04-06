package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 使用幸运符类道具
 * 
 * 
 *
 */
public class Option_UseUpgradeArticle extends Option{

	Player owner;
	
	Equipment e;
	EquipmentEntity ee;
	long emeraldId = -1;
	int sackIndex;

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Equipment getE() {
		return e;
	}

	public void setE(Equipment e) {
		this.e = e;
	}

	public EquipmentEntity getEe() {
		return ee;
	}

	public void setEe(EquipmentEntity ee) {
		this.ee = ee;
	}

	public long getEmeraldId() {
		return emeraldId;
	}

	public void setEmeraldId(long emeraldId) {
		this.emeraldId = emeraldId;
	}

	public int getSackIndex() {
		return sackIndex;
	}

	public void setSackIndex(int sackIndex) {
		this.sackIndex = sackIndex;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_CONFIRM_UPGRADE_EQUIPMENT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_483;
	}
}
