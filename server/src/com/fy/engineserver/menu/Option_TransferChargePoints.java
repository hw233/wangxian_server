/**
 * 
 */
package com.fy.engineserver.menu;

import java.util.Calendar;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_TransferChargePoints extends Option {

	/**
	 * 
	 */
	public Option_TransferChargePoints() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Calendar ca=Calendar.getInstance();
		ca.set(2010, Calendar.OCTOBER, 1, 0, 0, 0);
		long startTime=ca.getTimeInMillis();
		ca.set(2010, Calendar.OCTOBER, 7, 23, 59, 59);
		long endTime=ca.getTimeInMillis();
		long ct = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		if((ct>=startTime&&ct<=endTime)||GameConstants.getInstance().getServerName().equals("美丽的神话")){
			MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
			mw.setTitle(Translate.text_5467);
			mw.setDescriptionInUUB(Translate.text_5468);
			Option_TransferChargePointsInputName otcpin=new Option_TransferChargePointsInputName();
			otcpin.setColor(0xffffff);
			otcpin.setText(Translate.text_117);
			Option_Cancel oc=new Option_Cancel();
			oc.setColor(0xffffff);
			oc.setText(Translate.text_364);
			mw.setOptions(new Option[]{otcpin,oc});
			INPUT_WINDOW_REQ iwr=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)15,Translate.在这里输入,otcpin.getText(),oc.getText(), new byte[0]);
			player.addMessageToRightBag(iwr);
//		}else{
//			HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"积分转账只在10月1日至7日开放，请到时再来！");
//			player.addMessageToRightBag(req);
//		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TRANSFER_CHARGE_POINTS;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4135 ;
	}

}
