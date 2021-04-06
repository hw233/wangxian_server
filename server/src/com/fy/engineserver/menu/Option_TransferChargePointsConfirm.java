/**
 * 
 */
package com.fy.engineserver.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_TransferChargePointsConfirm extends Option {

	String playerName;
	
	int amount;
	
//	Logger log=Logger.getLogger(BillingCenter.class.getName() + ".A");
public	static Logger log = LoggerFactory.getLogger(BillingCenter.class.getName() + ".A");
	/**
	 * 
	 */
	public Option_TransferChargePointsConfirm(String playerName,int amount) {
		// TODO Auto-generated constructor stub
		this.playerName=playerName;
		this.amount=amount;
	}
	
	@Override
	public void doSelect(Game game, Player player) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_CONFIRM;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4940 ;
	}

}
