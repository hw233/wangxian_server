package com.fy.engineserver.activity.fireActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.cache.Cacheable;


public class CommonFireActivityNpc extends NPC implements Cacheable {
	
	private static final long serialVersionUID = 5863409755242303104L;
	
	
	private CommonFireEntity commonFireEntity;
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		if(commonFireEntity != null){
			commonFireEntity.heartbeat(game,this);
		}else{
			if(FireManager.getInstance() != null)
			FireManager.getInstance().createCommonFireNpc(this);
		}
		
	}

	@Override
	public Object clone() {
		
		CommonFireActivityNpc npc = new CommonFireActivityNpc();
		npc.cloneAllInitNumericalProperty(this);
		npc.setWindowId(this.getWindowId());
		npc.setNPCCategoryId(this.getNPCCategoryId());
		return npc;
	}

	
	public CommonFireEntity getCommonFireEntity() {
		return commonFireEntity;
	}

	public void setCommonFireEntity(CommonFireEntity commonFireEntity) {
		this.commonFireEntity = commonFireEntity;
	}

}
