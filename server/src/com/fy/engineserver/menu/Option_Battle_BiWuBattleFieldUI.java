package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 比武进比武场
 * 
 * 
 *
 */
public class Option_Battle_BiWuBattleFieldUI extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == null){
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		if(windowManager == null){
			return;
		}

		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.text_5036);
		mw.setDescriptionInUUB("");
		Option_Battle_BiWuBattleField option1 = new Option_Battle_BiWuBattleField();
		option1.setSelectIndex(0);
		option1.setText(Translate.text_1754);
		option1.setColor(0xFFFFFF);
		Option_Battle_BiWuBattleField option2 = new Option_Battle_BiWuBattleField();
		option2.setSelectIndex(1);
		option2.setText(Translate.text_1755);
		option2.setColor(0xFFFFFF);
		Option_Battle_BiWuBattleField option3 = new Option_Battle_BiWuBattleField();
		option3.setSelectIndex(2);
		option3.setText(Translate.text_1756);
		option3.setColor(0xFFFFFF);
		Option_Battle_BiWuBattleField option4 = new Option_Battle_BiWuBattleField();
		option4.setSelectIndex(3);
		option4.setText(Translate.text_1757);
		option4.setColor(0xFFFFFF);
		Option_Battle_BiWuBattleField option5 = new Option_Battle_BiWuBattleField();
		option5.setSelectIndex(4);
		option5.setText(Translate.text_1758);
		option5.setColor(0xFFFFFF);
		Option_Battle_BiWuBattleField option6 = new Option_Battle_BiWuBattleField();
		option6.setSelectIndex(5);
		option6.setText(Translate.text_1759);
		option6.setColor(0xFFFFFF);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.text_364);
		cancel.setColor(0xFFFFFF);
		mw.setOptions(new Option[]{option1,option2,option3,option4,option5,option6,cancel});
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
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
