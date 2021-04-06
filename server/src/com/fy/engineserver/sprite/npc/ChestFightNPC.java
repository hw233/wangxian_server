package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 
 *
 */
public class ChestFightNPC extends NPC /*implements FightableNPC*/{
	
	public static final byte 采集过 = 1;
	
	private byte pickState;
	
	private int chestId;
	
	private long createTime;
	
	private long endTime;
	
	@Override
	public byte getNpcType() {
		return NPC_TYPE_CHESTFIGHT;
	}
	
	@Override
	public Object clone() {
		
		ChestFightNPC p = new ChestFightNPC();
		p.cloneAllInitNumericalProperty(this);
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.npcType = NPC_TYPE_CHESTFIGHT;
		return p;
	}
	
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}
	
	public void pickUp(Player player) {
		if (pickState == 采集过) {
			player.sendError(Translate.text_3050);
			return ;
		}
		boolean b = ChestFightManager.inst.fight.notifyPickUpChest(player, this.getChestId());
		if (b) {
			pickState = 采集过;
			this.setHp(0);
			setState(LivingObject.STATE_DEAD);
		}
	}
	public int getNameColor(){
		if (this.getnPCCategoryId() == 100111000) {
			return ArticleManager.COLOR_PURPLE;
		} else if (this.getnPCCategoryId() == 100111001) {
			return ArticleManager.COLOR_BLUE;
		} else {
			return ArticleManager.COLOR_GREEN;
		}
	}
	
	

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public byte getPickState() {
		return pickState;
	}

	public void setPickState(byte pickState) {
		this.pickState = pickState;
	}

	public int getChestId() {
		return chestId;
	}

	public void setChestId(int chestId) {
		this.chestId = chestId;
	}

}
