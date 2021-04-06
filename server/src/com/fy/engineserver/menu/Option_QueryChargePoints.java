/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_QueryChargePoints extends Option {

	/**
	 * 
	 */
	public Option_QueryChargePoints() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(3600);
		mw.setTitle(Translate.text_4115);
		mw.setDescriptionInUUB(Translate.text_5358+player.getChargePoints()+Translate.text_5359);
		Option_Cancel oc=new Option_Cancel();
		oc.setColor(0xffffff);
		oc.setText(Translate.text_284);
		mw.setOptions(new Option[]{oc});
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_QUERY_CHARGE_POINTS;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4920 ;
	}

}
