package com.fy.engineserver.menu.society.masterPrentice;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_MASTERPRENTICE_RES;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 确认取消收徒拜师登记
 * @author Administrator
 *
 */
public class Option_Rebel_Evict extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private boolean masterOrPrentice;
	
	public boolean isMasterOrPrentice() {
		return masterOrPrentice;
	}
	public void setMasterOrPrentice(boolean masterOrPrentice) {
		this.masterOrPrentice = masterOrPrentice;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		SocialManager socialManager = SocialManager.getInstance();
		PlayerManager playerManager = PlayerManager.getInstance();
		
		Relation relation = socialManager.getRelationById(player.getId());
		MasterPrentice mp = relation.getMasterPrentice();
		Player p = null;
		if(mp != null){
			long masterId = mp.getMasterId();
			if(masterId != -1){
				boolean[] relations = new boolean[1];
				long[] ids = new long[1];
				String[] names = new String[1];
				byte[] careers = new byte[1];
				int[] levels = new int[1];
				int i = 0;
				if(masterId != -1){
					try {
						p = playerManager.getPlayer(masterId);
						relations[0] = true;
						ids[0] = masterId;
						names[0] = p.getName();
						careers[0] = p.getMainCareer();
						levels[0] = p.getLevel();
						QUERY_MASTERPRENTICE_RES res = new QUERY_MASTERPRENTICE_RES(GameMessageFactory.nextSequnceNum(), relations, ids, names, careers, levels);
						player.addMessageToRightBag(res);
						return ;
					} catch (Exception e) {
						mp.setMasterId(-1);
						MasterPrenticeManager.logger.error("[查询自己的师徒关系] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+masterId+"]",e);
						return;
					}
				}
			}else{
				List<Long> prenticeIds = mp.getPrentices();
				
				if(prenticeIds.size() > 0){
					List<Long> copy = null;
					for(long id : prenticeIds){
						try {
							playerManager.getPlayer(id);
						} catch (Exception e) {
							if(copy == null)  copy = new ArrayList<Long>(prenticeIds);
							copy.remove(id);
						}
					}
					if(copy != null){
						mp.setPrentices(copy);
						prenticeIds = copy;
					}
				}
				
				int temp = prenticeIds.size();
				if(temp <= 0){
					player.sendError(Translate.text_没有师徒关系);
					return;
				}
				boolean[] relations = new boolean[temp];
				long[] ids = new long[temp];
				String[] names = new String[temp];
				byte[] careers = new byte[temp];
				int[] levels = new int[temp];
				int i = 0;
				List<Long> copyList = null;
				for(long id : prenticeIds){
					try {
						p = playerManager.getPlayer(id);
						relations[i] = false;
						ids[i] = id;
						names[i] = p.getName();
						careers[i] = p.getMainCareer();
						levels[i] = p.getLevel();
						i++;
					} catch (Exception e) {
						if(copyList == null)
							copyList = new ArrayList<Long>(prenticeIds);
						ids[i] = -1;
						relations[i] = false;
						names[i] = "";
						careers[i] = 0;
						levels[i] = 0;
						i++;
						copyList.remove(id);
						MasterPrenticeManager.logger.error("[查询自己的师徒关系] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+id+"]",e);
					}
				}
				if(copyList != null)
					mp.setPrentices(copyList);
				QUERY_MASTERPRENTICE_RES res = new QUERY_MASTERPRENTICE_RES(GameMessageFactory.nextSequnceNum(), relations, ids, names, careers, levels);
				player.addMessageToRightBag(res);
				return ;
			}
		}else{
			player.sendError(Translate.text_没有师徒关系);
		}
		
	}
	
}
