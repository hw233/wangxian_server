package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 打开仓库界面
 * 
 * 
 *
 */
public class Option_Cangku_AddCell extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

		if(player.getArticleEntity(Player.扩展仓库物品) == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您的没有提升仓库空间的材料);
			player.addMessageToRightBag(hreq);
			return;
		}
		if(player.getKnapsacks_cangku().getCells().length >= Player.仓库最大格子数量){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.仓库已经扩展到最大);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.仓库);
		mw.setDescriptionInUUB(Translate.translateString(Translate.扩展仓库详细提示, new String[][]{{Translate.STRING_1,Player.扩展仓库物品}}));
		Option_Cangku_ConfirmAddCell option = new Option_Cangku_ConfirmAddCell();
		option.setText(Translate.确定);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		Option[] options = new Option[]{option,cancel};
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(req);
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
