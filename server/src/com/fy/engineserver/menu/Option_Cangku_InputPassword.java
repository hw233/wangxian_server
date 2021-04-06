package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 输入password
 * 
 * 
 *
 */
public class Option_Cangku_InputPassword extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

	}
	
	@Override
	public void doInput(Game game, Player player, String inputContent) {
		// TODO Auto-generated method stub
		if(player.pressWrongPWCount >= Player.MAX_WRONG_NUM){
			if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.pressWrongPWTimePoint < Player.仓库密码输入错误保护持续时间){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您输入的次数过多请稍后再试);
				player.addMessageToRightBag(hreq);
				return;
			}
		}
		if(inputContent != null && inputContent.trim().equals(player.getCangkuPassword().trim())){
			player.pressWrongPWCount = 0;
			player.OpenCangku();
		}else{
			player.pressWrongPWCount++;
			player.pressWrongPWTimePoint = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if(player.pressWrongPWCount >= Player.MAX_WRONG_NUM){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您输入的次数过多请稍后再试);
				player.addMessageToRightBag(hreq);
				return;
			}
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.仓库);
			mw.setDescriptionInUUB(Translate.translateString(Translate.密码错误请重新输入密码详细,new String[][]{{Translate.COUNT_1,player.pressWrongPWCount+""}}));
			Option_Cangku_InputPassword option = new Option_Cangku_InputPassword();
			option.setText(Translate.密码);
			Option_UseCancel cancel = new Option_UseCancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[]{option,cancel});
			INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)2, (byte)10,Translate.在这里输入, Translate.确定, Translate.取消, new byte[0]);
			player.addMessageToRightBag(res);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_OPEN_CANGKU;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
