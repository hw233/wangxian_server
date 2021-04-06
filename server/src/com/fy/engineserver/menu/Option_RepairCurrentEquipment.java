package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 修理当前装备
 * 
 * 
 *
 */
public class Option_RepairCurrentEquipment extends Option{

	private int calPrice(Player player){
		
		return 0;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		EquipmentColumn ec = player.getEquipmentColumns();
		try {
			for (int i = 0; i < ec.getEquipmentIds().length; i++) {
				ec.repaire(i);
			}
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备修理成功);
			player.addMessageToRightBag(hreq);
		} catch (Exception ex) {

		}
	}
	

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_修理当前装备;
	}

	public void setOId(int oid) {
	}
	public String toString(){
		return "" ;
	}
	
	String oldGame = "";
	
	/**
	 * 为玩家copy一个选项出来，特殊的选项可以重载此方法
	 * 
	 * 如果传送，修改，治疗，可以根据玩家的具体信息来追加要花多少钱
	 * 
	 * @param p
	 * @return
	 */
	public Option copy(Game game,Player p){
		Option_RepairCurrentEquipment op = new Option_RepairCurrentEquipment();
		op.setColor(getColor());
		op.setIconId(getIconId());
		
		int priceForAll = calPrice(p);
		
		if(p.getBindSilver() < priceForAll){
			op.setColor(0xff0000);
		}
		op.setText(getText() + Translate.text_5199+priceForAll+Translate.text_2305);
		
		op.oldGame = game.getGameInfo().getName();
		
		return op;
	}
}
