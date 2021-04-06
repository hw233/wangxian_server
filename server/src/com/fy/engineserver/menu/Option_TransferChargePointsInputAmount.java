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
public class Option_TransferChargePointsInputAmount extends Option {

	String playerName;
	/**
	 * 
	 */
	public Option_TransferChargePointsInputAmount(String playerName) {
		// TODO Auto-generated constructor stub
		this.playerName=playerName;
	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub
		if(inputContent!=null&&inputContent.length()>0){
			int amount=0;
			try{
				amount=Integer.parseInt(inputContent);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(amount>0&&amount<=player.getChargePoints()){
				MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
				mw.setTitle(Translate.text_5467);
				mw.setDescriptionInUUB(Translate.text_5477+amount+Translate.text_5478+this.playerName+Translate.text_2749);
				Option_TransferChargePointsConfirm otcpin=new Option_TransferChargePointsConfirm(this.playerName,amount);
				otcpin.setColor(0xffffff);
				otcpin.setText(Translate.text_117);
				Option_UseCancel oc=new Option_UseCancel();
				oc.setColor(0xffffff);
				oc.setText(Translate.text_364);
				mw.setOptions(new Option[]{otcpin,oc});
				CONFIRM_WINDOW_REQ cwr=new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),mw.getOptions());
				player.addMessageToRightBag(cwr);
			}else{
				HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5479);
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
		return OptionConstants.SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_INPUT_AMOUNT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5480 ;
	}

}
