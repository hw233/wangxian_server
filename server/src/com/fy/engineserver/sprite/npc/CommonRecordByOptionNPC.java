package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;

public class CommonRecordByOptionNPC extends NPC implements NeedRecordByOption {

	public Object clone() {
		CommonRecordByOptionNPC p = new CommonRecordByOptionNPC();

		p.cloneAllInitNumericalProperty(this);

		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());

		if (items != null) {
			p.items = new NpcExecuteItem[this.items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					try {
						p.items[i] = (NpcExecuteItem) items[i].clone();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		p.windowId = windowId;
		p.id = nextId();
		return p;
	}
}
