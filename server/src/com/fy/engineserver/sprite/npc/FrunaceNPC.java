package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_ALCHEMY_REQ;
import com.fy.engineserver.sprite.Player;


/**
 * 丹炉npc
 * 
 *
 */
public class FrunaceNPC extends NPC /*implements FightableNPC*/{
	
	public static final byte 采集过 = 1;
	
	private byte pickState;
	
	private long minTime;
	
	private long maxTime;
	
	public static String 采集过动作 = "死亡2__";

	@Override
	public byte getNpcType() {
		return NPC_TYPE_FRUNACE;
	}
	
	@Override
	public Object clone() {
		
		FrunaceNPC p = new FrunaceNPC();
		p.cloneAllInitNumericalProperty(this);
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.npcType = NPC_TYPE_FRUNACE;
		return p;
	}
	
	@Override
	public void setState(byte value) {
		super.setState(value);
	}
	@Override					//改变状态不能被地图清除
	public boolean isAlive() {
		return true;
	}
	
	public void pickUp(Player player) {
//		if (pickState == 采集过) {
//			player.sendError(FurnaceManager.inst.translate.get(1));
//			return ;
//		}
//		pickState = 采集过;
		if (FurnaceManager.inst.isPicked(player, this.getId())) {
			player.sendError(FurnaceManager.inst.translate.get(1));
			return ;
		}
		//发协议给客户端
		int ran = 0;
		if (maxTime > minTime) {
			int temp = (int) (maxTime - minTime);
			ran = player.random.nextInt(temp);
		}
		long duration = minTime + ran;				//读条时间
		NOTICE_CLIENT_ALCHEMY_REQ req = new NOTICE_CLIENT_ALCHEMY_REQ(GameMessageFactory.nextSequnceNum(), duration,this.getId());
		player.addMessageToRightBag(req);
//		this.setAvataAction(采集过动作);
		
	}

	public byte getPickState() {
		return pickState;
	}

	public void setPickState(byte pickState) {
		this.pickState = pickState;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
	
	
}
