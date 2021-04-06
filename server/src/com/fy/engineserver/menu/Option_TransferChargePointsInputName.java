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
import com.fy.engineserver.sprite.PlayerManager;

/**
 * @author Administrator
 *
 */
public class Option_TransferChargePointsInputName extends Option {

	/**
	 * 
	 */
	public Option_TransferChargePointsInputName() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub
		if(inputContent!=null&&inputContent.length()>0){
			Player p=null;
			try {
				p=PlayerManager.getInstance().getPlayer(inputContent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p!=null){
				MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
				mw.setTitle(Translate.text_5467);
				mw.setDescriptionInUUB(Translate.text_5481);
				Option_TransferChargePointsInputAmount otcpin=new Option_TransferChargePointsInputAmount(inputContent);
				otcpin.setColor(0xffffff);
				otcpin.setText(Translate.text_117);
				Option_Cancel oc=new Option_Cancel();
				oc.setColor(0xffffff);
				oc.setText(Translate.text_364);
				mw.setOptions(new Option[]{otcpin,oc});
				INPUT_WINDOW_REQ iwr=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)10,Translate.在这里输入,otcpin.getText(),oc.getText(), new byte[0]);
				player.addMessageToRightBag(iwr);
			}else{
				HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5476);
				player.addMessageToRightBag(req);
			}
		}else{
			HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5272);
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
		return OptionConstants.SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_INPUT_NAME;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5482 ;
	}

}
