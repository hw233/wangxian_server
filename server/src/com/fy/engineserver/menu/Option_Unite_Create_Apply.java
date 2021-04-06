package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;
import com.fy.engineserver.unite.UniteSubSystem;

/**
 * 创建结义申请
 * @author Administrator
 *
 */
public class Option_Unite_Create_Apply extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
		try {
			UniteManager.getInstance().uniteApply(player);
		} catch (Exception e) {
			UniteSubSystem.logger.error("[队长发送结义申请失败] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
