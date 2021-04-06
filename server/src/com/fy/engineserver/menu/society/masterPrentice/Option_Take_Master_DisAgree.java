package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 徒弟发送拜师请求,师傅不同意
 * @author Administrator
 *
 */
public class Option_Take_Master_DisAgree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player prentice;

	public Player getPrentice() {
		return prentice;
	}
	public void setPrentice(Player prentice) {
		this.prentice = prentice;
	}
	@Override
	public void doSelect(Game game, Player player) {
//		prentice.sendError(player.getName()+"拒绝了你的拜师请求");

		prentice.sendError(Translate.translateString(Translate.xx拒绝了你的拜师请求, new String[][]{{Translate.STRING_1,player.getName()}}));
		
//		player.sendError("你拒绝了"+prentice.getName()+"的拜师请求");
		player.sendError(Translate.translateString(Translate.你拒绝了xx的拜师请求, new String[][]{{Translate.STRING_1,prentice.getName()}}));
	}
	
}
