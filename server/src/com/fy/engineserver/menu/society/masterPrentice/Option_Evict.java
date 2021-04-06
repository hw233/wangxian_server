package com.fy.engineserver.menu.society.masterPrentice;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_MASTERPRENTICE_RES;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 确认逐徒
 * @author Administrator
 *
 */
public class Option_Evict extends Option {
	
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
			
			if(masterId > 0){
				//错误
				MasterPrenticeManager.logger.error("[玩家逐徒错误] [有师傅] ["+player.getLogString()+"] ["+masterId+"]");
			}else{
				
				List<Long> prenticeIds = mp.getPrentices();
				if(prenticeIds == null || prenticeIds.size() == 0){
					player.sendError(Translate.你还没有徒弟);
				}else{
					int prenticeNum = prenticeIds.size();
					Player prentice = null;
					List<Player> list = new ArrayList<Player>();
					for(int i= 0;i<prenticeNum;i++){
						try {
							prentice = playerManager.getPlayer(prenticeIds.get(i));
							list.add(prentice);
						} catch (Exception e) {
							e.printStackTrace();
							prenticeIds.remove(i);
						}
					}
					
					mp.setPrentices(prenticeIds);
					if(list.size() == 0){
						player.sendError(Translate.你的徒弟已经不存在了);
					}else{
						int temp = list.size();
						boolean[] relations = new boolean[temp];
						long[] ids = new long[temp];
						String[] names = new String[temp];
						byte[] careers = new byte[temp];
						int[] levels = new int[temp];
						int i= 0;
						for(Player p1 : list){
							relations[i] = false;
							ids[i] = p1.getId();
							names[i] = p1.getName();
							careers[i] = p1.getMainCareer();
							levels[i] = p1.getLevel();
							i++;
						}
						QUERY_MASTERPRENTICE_RES res = new QUERY_MASTERPRENTICE_RES(GameMessageFactory.nextSequnceNum(), relations, ids, names, careers, levels);
						player.addMessageToRightBag(res);
					}
					
				}
			}
		}else{
			player.sendError(Translate.text_没有徒弟关系);
		}
		
	}
	
}
