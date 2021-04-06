/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.StatData;

/**
 * @author Administrator
 *
 */
public class Option_QueryCurrentDayOnlineTime extends Option {

	/**
	 * 
	 */
	public Option_QueryCurrentDayOnlineTime() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		StatData sd=player.getStatData(StatData.STAT_CURRENT_DAY_ONLINE_TIME);
		long minute=0;
		if(sd!=null){
			minute=sd.getValue()/1000/60;
		}
		MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
		mw.setTitle(Translate.text_5360);
		mw.setDescriptionInUUB(Translate.text_5361+minute+Translate.text_5362);
		Option_Cancel oc=new Option_Cancel();
		oc.setText(Translate.text_284);
		oc.setColor(0xffffff);
		mw.setOptions(new Option[]{oc});
		
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory
				.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_QUERY_CURRENT_DAY_ONLINE_TIME;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4944 ;
	}

}
