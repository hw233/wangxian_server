/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_UniqueGift extends Option {

	/**
	 * 
	 */
	public Option_UniqueGift() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
		mw.setTitle(Translate.text_5490);
		mw.setDescriptionInUUB(Translate.text_5491);
		Option_UniqueGiftInput ougi=new Option_UniqueGiftInput();
		ougi.setColor(0xffffff);
		ougi.setText(Translate.text_62);
		Option_Cancel oc=new Option_Cancel();
		oc.setColor(0xffffff);
		oc.setText(Translate.text_364);
		mw.setOptions(new Option[]{ougi,oc});
		INPUT_WINDOW_REQ iwr=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)20,Translate.在这里输入,ougi.getText(),oc.getText(), new byte[0]);
		player.addMessageToRightBag(iwr);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_UNIQUE_GIFT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4938 ;
	}

}
