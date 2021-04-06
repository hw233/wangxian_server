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
public class Option_InputNewAdvancedPassword extends Option {

	String newPassword;
	
	String oldPassword;
	
//	Logger log = Logger.getLogger("com.fy.engineserver.sprite");
public	static Logger log = LoggerFactory.getLogger("com.fy.engineserver.sprite");
	/**
	 * 
	 */
	public Option_InputNewAdvancedPassword(String newPassword,String oldPassword) {
		// TODO Auto-generated constructor stub
		this.newPassword=newPassword;
		this.oldPassword=oldPassword;
	}
	
	@Override
	// public void doInput(Game game, Player player, String inputContent) {
	// // TODO Auto-generated method stub
	// HINT_REQ req=null;
	// Passport pp=player.getPassport();
	// if(inputContent!=null&&inputContent.length()>0){
	// if(pp!=null){
	// boolean isPass=true;
	// if(this.oldPassword!=null){
	// if(!this.oldPassword.equals(pp.getProtectkey())){
	// isPass=false;
	// }
	// }
	// if(isPass){
	// boolean isSame=true;
	// if(this.newPassword!=null){
	// if(!this.newPassword.equals(inputContent)){
	// isSame=false;
	// }
	// if(isSame){
	// pp.setProtectkey(inputContent);
	// pp.setDirty(true);
	// BossClientService.getInstance().setPassportProtectStatus(player.getUsername(), pp.getProtectkey(), pp.getProtecting().intValue());
	// req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5273);
	// if(this.log.isInfoEnabled()){
	// // this.log.info("[设置高级密码] [成功] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"] [新密码："+pp.getProtectkey()+"] [旧密码："+this.oldPassword+"]");
	// if(this.log.isInfoEnabled())
	// this.log.info("[设置高级密码] [成功] [玩家：{}] [玩家ID：{}] [新密码：{}] [旧密码：{}]", new Object[]{player.getName(),player.getId(),pp.getProtectkey(),this.oldPassword});
	// }
	// }else{
	// req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5274);
	// if(this.log.isInfoEnabled()){
	// // this.log.info("[设置高级密码] [失败] [两次密码输入不匹配] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"] [第一次："+this.newPassword+"] [第二次："+inputContent+"]");
	// if(this.log.isInfoEnabled())
	// this.log.info("[设置高级密码] [失败] [两次密码输入不匹配] [玩家：{}] [玩家ID：{}] [第一次：{}] [第二次：{}]", new Object[]{player.getName(),player.getId(),this.newPassword,inputContent});
	// }
	// }
	// }else{
	// MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
	// mw.setTitle(Translate.text_5265);
	// mw.setDescriptionInUUB(Translate.text_5275);
	// Option_InputNewAdvancedPassword oinap=new Option_InputNewAdvancedPassword(inputContent,this.oldPassword);
	// oinap.setColor(0xffffff);
	// oinap.setText(Translate.text_117);
	// Option_Cancel oc=new Option_Cancel();
	// oc.setColor(0xffffff);
	// oc.setText(Translate.text_364);
	// mw.setOptions(new Option[]{oinap,oc});
	// INPUT_WINDOW_REQ iwr=new
	// INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getTitle(),mw.getDescriptionInUUB(),(byte)2,(byte)10,Translate.在这里输入,oinap.getText(),oc.getText(), new
	// byte[0]);
	// player.addMessageToRightBag(iwr);
	// }
	// }else{
	// req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5270);
	// if(this.log.isInfoEnabled()){
	// // this.log.info("[设置高级密码] [失败] [密码错误] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"] [密码："+pp.getProtectkey()+"] [输入："+this.oldPassword+"]");
	// if(this.log.isInfoEnabled())
	// this.log.info("[设置高级密码] [失败] [密码错误] [玩家：{}] [玩家ID：{}] [密码：{}] [输入：{}]", new Object[]{player.getName(),player.getId(),pp.getProtectkey(),this.oldPassword});
	// }
	// }
	// }else{
	// req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5271);
	// // this.log.warn("[设置高级密码] [失败] [passport不存在] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"]");
	// if(this.log.isWarnEnabled())
	// this.log.warn("[设置高级密码] [失败] [passport不存在] [玩家：{}] [玩家ID：{}]", new Object[]{player.getName(),player.getId()});
	// }
	// }else{
	// req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5272);
	// }
	// if(req!=null){
	// player.addMessageToRightBag(req);
	// }
	// }
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_INPUT_NEW_ADVANCED_PASSWORD;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4934 ;
	}

}
