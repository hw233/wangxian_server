/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_PlayerRenameInput extends Option {

	/**
	 * 
	 */
	public Option_PlayerRenameInput() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub
		HINT_REQ req;
		if(player.getName().endsWith(Translate.text_5231)||player.getName().endsWith(Translate.text_5232)||player.getName().endsWith(Translate.text_5233)||player.getName().endsWith(Translate.text_5234)){
			if(inputContent!=null&&inputContent.length()>0){
				Option_PlayerRenameConfirm oprc=new Option_PlayerRenameConfirm(inputContent);
				oprc.setColor(0xffffff);
				oprc.setText(Translate.text_117);
				Option_UseCancel oc=new Option_UseCancel();
				oc.setColor(0xffffff);
				oc.setText(Translate.text_364);
				MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
				mw.setOptions(new Option[]{oprc,oc});
				CONFIRM_WINDOW_REQ cwr=new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),Translate.text_5243+inputContent+Translate.text_5357,mw.getOptions());
				player.addMessageToRightBag(cwr);
			}else{
				req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5245);
				player.addMessageToRightBag(req);
			}
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
		return OptionConstants.SERVER_FUNCTION_PLAYER_RENAME_INPUT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4926 ;
	}

}
