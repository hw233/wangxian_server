package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
/**
 * 使用官员囚禁功能
 * 
 * 
 *
 */
public class Option_Country_qiujin_byName extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		if(cm != null){
			
		}
	}
@Override
public void doInput(Game game, Player player, String inputContent) {
	// TODO Auto-generated method stub
	CountryManager cm = CountryManager.getInstance();
	PlayerManager pm = PlayerManager.getInstance();
	if(cm != null && pm != null){
		Player playerB = null;
		try{
//			CountryManager.logger.info("[囚禁申请] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+inputContent+"]");
			if(CountryManager.logger.isInfoEnabled())
				CountryManager.logger.info("[囚禁申请] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),inputContent});
			playerB = pm.getPlayer(inputContent.trim());
		}catch(Exception ex){
			
		}
		if(playerB == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.请正确输入要囚禁人的名字);
			player.addMessageToRightBag(hreq);
			return;
		}
		cm.囚禁(player, playerB);
	}
}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
