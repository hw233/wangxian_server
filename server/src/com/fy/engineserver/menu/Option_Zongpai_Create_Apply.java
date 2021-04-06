package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.CREATE_ZONGPAI_APPLY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 创建宗派申请
 * @author Administrator
 *
 */
public class Option_Zongpai_Create_Apply extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
		String result = ZongPaiManager.getInstance().创建宗派合法性判断(player);
		if(result == null){
			return ;
		}
		if (ZongPaiManager.logger.isInfoEnabled()){
			ZongPaiManager.logger.info("[创建宗派] ["+player.getLogString()+"] ["+result+"]");
		}
		if(result.equals("")){
			CREATE_ZONGPAI_APPLY_RES res = new CREATE_ZONGPAI_APPLY_RES(GameMessageFactory.nextSequnceNum(), result);
			player.addMessageToRightBag(res);
		}else{
			player.sendError(result);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
