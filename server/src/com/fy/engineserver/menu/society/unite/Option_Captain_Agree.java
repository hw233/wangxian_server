package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.UNITE_CONFIRM_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 队长确定完成结义
 * @author Administrator
 *
 */
public class Option_Captain_Agree extends Option {
	
	private UNITE_CONFIRM_REQ req;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		UniteManager uniteManager = UniteManager.getInstance();
		String title = req.getTitle();
		String name = req.getName();
		
		try {
			String result = uniteManager.uniteBeforeValidate(player);
			if(!result.equals("")){
				player.sendError(result);
				return ;
			}
			result = uniteManager.uniteNameCheck(title, name);
			if(result.equals("")){
				result = uniteManager.unite_apply_confirm(player, req);
				if(result.equals("")){
					for(Player p1 : player.getTeamMembers()){
						p1.sendError(Translate.text_结义完成);
					}
				}else{
					player.sendError(result);
				}
			}else{
				player.sendError(result);
			}
		} catch (Exception e) {
			UniteManager.logger.error("[结义队长确定结义] ["+player.getLogString()+"]",e);
		}
		
	}

	public UNITE_CONFIRM_REQ getReq() {
		return req;
	}

	public void setReq(UNITE_CONFIRM_REQ req) {
		this.req = req;
	}
	
	
	

}
