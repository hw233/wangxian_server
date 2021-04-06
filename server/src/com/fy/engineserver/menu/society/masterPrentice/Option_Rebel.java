package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 确认判师
 * @author Administrator
 *
 */
public class Option_Rebel extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		
		SocialManager socialManager = SocialManager.getInstance();
		PlayerManager playerManager = PlayerManager.getInstance();
		
		Relation relation = socialManager.getRelationById(player.getId());
		MasterPrentice mp = relation.getMasterPrentice();
		if(mp != null){
			long masterId = mp.getMasterId();
			if(masterId <= 0){
				
				player.sendError(Translate.你还没有师傅);
			}else{
				
				try {
					Player master = playerManager.getPlayer(masterId);
					MasterPrenticeManager.getInstance().rebelMaster(player, master);
				} catch (Exception e) {
					MasterPrenticeManager.logger.error("[玩家判师异常] ["+player.getLogString()+"]",e);
					mp.setMasterId(-1);
				}
			}
		}else{
			player.sendError(Translate.text_没有师傅关系);
		}
		
	}
	
}
