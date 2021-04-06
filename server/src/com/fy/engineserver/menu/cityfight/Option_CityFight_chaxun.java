package com.fy.engineserver.menu.cityfight;

import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 申请攻占城市
 * 
 * 
 *
 */
public class Option_CityFight_chaxun extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CityFightManager cfm = CityFightManager.getInstance();
		String result = cfm.得到占领城市及帮派信息(game.country);
		if(result != null){
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setTitle(Translate.城市信息一览);
			mw.setDescriptionInUUB(result);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.确定);
			cancel.setColor(0xFFFFFF);
			mw.setOptions(new Option[]{cancel});
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
		}
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doInput(Game game, Player player, String inputContent){}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_比武进比武场;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5035 ;
	}

}
