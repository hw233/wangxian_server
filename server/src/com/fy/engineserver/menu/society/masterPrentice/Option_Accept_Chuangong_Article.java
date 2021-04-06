package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.chuangong.ChuanGongManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 领取传功石
 * @author Administrator
 *
 */
public class Option_Accept_Chuangong_Article extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	
	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			String result = ChuanGongManager.getInstance().acceptChuangongArticle(player);
			if(!result.equals("")){
				player.send_HINT_REQ(result);
			}
		} catch (Exception e) {
			ChuanGongManager.logger.error("[领取传功石错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
	}
	
}
