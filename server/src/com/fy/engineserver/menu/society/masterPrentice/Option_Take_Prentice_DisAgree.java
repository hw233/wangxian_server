package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 师傅发送收徒请求,徒弟不同意
 * @author Administrator
 *
 */
public class Option_Take_Prentice_DisAgree extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player master;
	public Player getMaster() {
		return master;
	}
	public void setMaster(Player master) {
		this.master = master;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
//		master.sendError(player.getName()+"拒绝了你的收徒请求");
		master.sendError(Translate.translateString(Translate.xx拒绝了你的收徒请求,new String[][]{{Translate.STRING_1,player.getName()}}));
		
//		player.sendError("你拒绝了"+master.getName()+"的收徒请求");
		player.sendError(Translate.translateString(Translate.你拒绝了xx的收徒请求,new String[][]{{Translate.STRING_1,master.getName()}}));
		
	}
	
}
