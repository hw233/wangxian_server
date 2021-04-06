package com.fy.engineserver.menu.lookWindow;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.PlayerMessagePair;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_REQ;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;

public class ReturnWindow  extends Option{

	
	private int npcId;
	
	@Override
	public void doSelect(Game game, Player player) {
		
		NPCManager nm = MemoryNPCManager.getNPCManager();
		NPCTempalte nt = ((MemoryNPCManager)nm).getNPCTempalteByCategoryId(npcId);
		long id = nt.npc.getId();
		
		Game g = player.getCurrentGame();
		if(g == null){
			return ;
		}
		String name = nt.npc.getName();
		NPC n = null;
		for(LivingObject lo : g.getLivingObjects()){
			if(lo instanceof NPC){
				if(((NPC)lo).getName().equals(name)){
					n = ((NPC)lo);
				}
			}
		}
		if(n != null){
			id = n.getId();
		}
		game.messageQueue.push(new PlayerMessagePair(player, new QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), id), null));
		if(SocialManager.logger.isInfoEnabled()){
			SocialManager.logger.info("[返回新的窗口] ["+player.getLogString()+"]");
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

}
