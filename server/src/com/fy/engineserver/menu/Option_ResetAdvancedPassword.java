/**
 * 
 */
package com.fy.engineserver.menu;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;

/**
 * @author Administrator
 *
 */
public class Option_ResetAdvancedPassword extends Option {

//	Logger log = Logger.getLogger("com.fy.engineserver.sprite");
public	static Logger log = LoggerFactory.getLogger("com.fy.engineserver.sprite");
	/**
	 * 
	 */
	public Option_ResetAdvancedPassword() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	// public void doSelect(Game game, Player player) {
	// // TODO Auto-generated method stub
	// Passport pp=player.getPassport();
	// if(pp!=null){
	// String oldPassport=pp.getProtectkey();
	// if(oldPassport!=null&&oldPassport.length()>0){
	// MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
	// mw.setTitle(Translate.text_5265);
	// mw.setDescriptionInUUB(Translate.text_5409);
	// Option_InputAdvancedPassword oiap=new Option_InputAdvancedPassword(true);
	// oiap.setColor(0xffffff);
	// oiap.setText(Translate.text_62);
	// Option_Cancel oc=new Option_Cancel();
	// oc.setColor(0xffffff);
	// oc.setText(Translate.text_364);
	// mw.setOptions(new Option[]{oiap,oc});
	//
	// INPUT_WINDOW_REQ iwr=new
	// INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)10,Translate.在这里输入,oiap.getText(),oc.getText(), new
	// byte[0]);
	// player.addMessageToRightBag(iwr);
	// }else{
	// MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
	// mw.setTitle(Translate.text_5265);
	// mw.setDescriptionInUUB(Translate.text_5266);
	// Option_InputNewAdvancedPassword oinap=new Option_InputNewAdvancedPassword(null,null);
	// oinap.setColor(0xffffff);
	// oinap.setText(Translate.text_117);
	// Option_Cancel oc=new Option_Cancel();
	// oc.setColor(0xffffff);
	// oc.setText(Translate.text_364);
	// mw.setOptions(new Option[]{oinap,oc});
	// INPUT_WINDOW_REQ iwr=new
	// INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)0,(byte)10,Translate.在这里输入,oinap.getText(),oc.getText(), new
	// byte[0]);
	// player.addMessageToRightBag(iwr);
	// }
	// }else{
	// HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5410);
	// player.addMessageToRightBag(req);
	// // this.log.warn("[设置高级密码] [失败] [passport不存在] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"]");
	// if(this.log.isWarnEnabled())
	// this.log.warn("[设置高级密码] [失败] [passport不存在] [玩家：{}] [玩家ID：{}]", new Object[]{player.getName(),player.getId()});
	// }
	// }
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_RESET_ADVANCED_PASSWORD;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4932 ;
	}

}
