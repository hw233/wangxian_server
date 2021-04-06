package com.fy.engineserver.activity.pickFlower;

import com.fy.engineserver.sprite.npc.NPC;

public class MagicWeaponNpc extends NPC{
	@Override
	public byte getNpcType() {
		return NPC_TYPE_MAGIC_WEAPON;
	}
	
	@Override
	public Object clone() {
		
		MagicWeaponNpc p = new MagicWeaponNpc();
		p.cloneAllInitNumericalProperty(this);
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.npcType = NPC_TYPE_MAGIC_WEAPON;
		return p;
		
	}
}
