package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tournament.manager.TournamentManager;

/**
 * 比武报名
 * 
 * 
 *
 */
public class Option_Battle_BiWuSignUp extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

		if(player == null){
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[报名] [失败] [player == null]");
			return;
		}
		//检查
		TournamentManager tm = TournamentManager.getInstance();
		if(tm != null){
			tm.signUp(player);
		}else{
			String des = Translate.竞技管理器还未启动;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
			player.addMessageToRightBag(hreq);
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[报名] [失败] [仙武大会管理器还未启动，请联系客服] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName()});
		}
	}
	
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_SIGN_UP_比武;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5057 ;
	}

}
