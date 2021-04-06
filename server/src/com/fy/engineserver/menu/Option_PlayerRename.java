/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_PlayerRename extends Option {

	/**
	 * 
	 */
	public Option_PlayerRename() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		String name=player.getName();
		HINT_REQ req;
		if(name.endsWith(Translate.text_5231)||name.endsWith(Translate.text_5232)||name.endsWith(Translate.text_5233)||name.endsWith(Translate.text_5234)){
			MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
			mw.setTitle(Translate.text_5353);
			mw.setDescriptionInUUB(Translate.text_5236);
			Option_PlayerRenameInput opri=new Option_PlayerRenameInput();
			opri.setText(Translate.text_62);
			opri.setColor(0xffffff);
			Option_Cancel oc=new Option_Cancel();
			oc.setText(Translate.text_364);
			oc.setColor(0xffffff);
			mw.setOptions(new Option[]{opri,oc});
			INPUT_WINDOW_REQ iwr=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)10,Translate.在这里输入,opri.getText(),oc.getText(), new byte[0]);
			player.addMessageToRightBag(iwr);
		}else{
			req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5354);
			player.addMessageToRightBag(req);
		}
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_PLAYER_RENAME;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4923 ;
	}

}
