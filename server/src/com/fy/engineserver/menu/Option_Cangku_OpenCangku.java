package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.WAREHOUSE_INPUT_PASSWORD_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开仓库界面
 * 
 * 
 *
 */
public class Option_Cangku_OpenCangku extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		//如果一次输入错误过了10分钟，那么就清空错误次数
		if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.pressWrongPWTimePoint > Player.仓库密码输入错误保护持续时间){
			player.pressWrongPWCount = 0;
		}
		//输入3次错误后，就要等待10分钟后才能继续输入
		if(player.pressWrongPWCount >= Player.MAX_WRONG_NUM){
			if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.pressWrongPWTimePoint < Player.仓库密码输入错误保护持续时间){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您输入的次数过多请稍后再试);
				player.addMessageToRightBag(hreq);
				return;
			}
		}
		if(player.getCangkuPassword() == null || player.getCangkuPassword().trim().equals("")){
			player.OpenCangku();
		}else{
			String description = "";
			if(player.pressWrongPWCount > 0){
				description = Translate.translateString(Translate.密码错误请重新输入密码详细,new String[][]{{Translate.COUNT_1,player.pressWrongPWCount+""}});
			}
			WAREHOUSE_INPUT_PASSWORD_RES res = new WAREHOUSE_INPUT_PASSWORD_RES(GameMessageFactory.nextSequnceNum(),description);
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
