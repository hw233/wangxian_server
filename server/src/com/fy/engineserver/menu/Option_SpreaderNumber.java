/**
 * 
 */
package com.fy.engineserver.menu;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_SpreaderNumber extends Option {
	
	Logger log=Game.logger;

	/**
	 * 
	 */
	public Option_SpreaderNumber() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
		mw.setTitle(Translate.text_5428);
		mw.setDescriptionInUUB(Translate.text_5429);
		Option_SpreaderNumber osn=new Option_SpreaderNumber();
		osn.setColor(0xffffff);
		osn.setText(Translate.text_62);
		Option_UseCancel ouc=new Option_UseCancel();
		ouc.setColor(0xffffff);
		ouc.setText(Translate.text_364);
		mw.setOptions(new Option[]{osn,ouc});
		INPUT_WINDOW_REQ iwr=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)20,Translate.在这里输入,osn.getText(),ouc.getText(), new byte[0]);
		player.addMessageToRightBag(iwr);
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_SPREADER_NUMBER;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4941 ;
	}

}
