/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.operating.SystemAnnouncementManager;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_QuerySystemAnnouncement extends Option {

	/**
	 * 
	 */
	public Option_QuerySystemAnnouncement() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		SystemAnnouncementManager sam = SystemAnnouncementManager.getInstance();
		if(sam != null) {
			MenuWindow mw = new MenuWindow();
			mw.setTitle(Translate.text_4951);
			mw.setDescriptionInUUB(sam.getAnnouncement());
			mw.setWidth(1000);
			mw.setHeight(1000);
			
			Option_Cancel o = new Option_Cancel();
			o.setText(Translate.text_2901);
			
			mw.setOptions(new Option[]{o});
			
			QUERY_WINDOW_RES re = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),
					mw,mw.getOptions());
			player.addMessageToRightBag(re);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_QUERY_SYSTEM_ANNOUNCEMENT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5370;
	}

}
