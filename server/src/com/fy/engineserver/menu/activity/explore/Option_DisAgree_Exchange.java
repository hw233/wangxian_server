package com.fy.engineserver.menu.activity.explore;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 不同意交换
 * @author Administrator
 *
 */
public class Option_DisAgree_Exchange extends Option {
	
	private Player playerA;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
//		playerA.send_HINT_REQ(player.getName()+"拒绝了你的交换请求");
		playerA.send_HINT_REQ(Translate.translateString(Translate.xx拒绝了你的交换请求, new String[][]{{Translate.STRING_1,player.getName()}}));
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}
	
	
}


