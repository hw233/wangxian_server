package com.fy.engineserver.activity.fairylandTreasure;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

public class FairylandTreasureNpc extends NPC {
	private FairylandTreasure fairylandTreasure;
	
	public void pick(Player player){
		if(this.fairylandTreasure.getPickingPlayerId() == player.getId()){
			fairylandTreasure.pick(player,this);
		}else{
			synchronized (fairylandTreasure) {
				fairylandTreasure.pick(player,this);		
			}
		}
	}

	@Override
	public byte getNpcType() {
		return NPC_TYPE_COLLECTION;
	}

	@Override
	public Object clone() {
		FairylandTreasureNpc p = new FairylandTreasureNpc();
		p.cloneAllInitNumericalProperty(this);
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.npcType = NPC_TYPE_COLLECTION;
		return p;
	}

	public FairylandTreasure getFairylandTreasure() {
		return fairylandTreasure;
	}

	public void setFairylandTreasure(FairylandTreasure fairylandTreasure) {
		this.fairylandTreasure = fairylandTreasure;
	}
	
	
}
