package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.sprite.Sprite;

/**
 * 头上显示"血量",当"血量"小于某个值从地图上消失
 * 
 * 
 */
public class ShowTitleAsHpNPC extends NPC {

	private int bornValue;
	private int disappearValue;
	private int currentVlaue;

	public synchronized void reduceValue(int value) {
		currentVlaue -= value;
		if (currentVlaue < 0) {
			currentVlaue = 0;
		}
	}

	public synchronized void addValue(int value) {
		currentVlaue += value;
		if (currentVlaue < 0) {
			currentVlaue = 0;
		}
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		if (getName().equals("瑞祥年兽")) {
			if (currentVlaue <= disappearValue) {
				game.removeSprite(this);
				this.setTitle("(" + this.currentVlaue + "/" + bornValue + ")");
				if (game.country >= 1) {
					String notice = CountryManager.国家名称[game.country] + "的" + getName() + "已经被吓跑啦";
					ChatMessageService.getInstance().sendMessageToServer(notice);
				}
			}
			ActivitySubSystem.logger.error("[移除NPC]" + game.getGameInfo().displayName + "/" + game.country + "[npc:" + this.getName() + "(" + this.getX() + "," + this.getY() + ")" + "id:" + this.getId() + "]");
		}
	}

	public int getBornValue() {
		return bornValue;
	}

	public void setBornValue(int bornValue) {
		this.bornValue = bornValue;
	}

	public int getDisappearValue() {
		return disappearValue;
	}

	public void setDisappearValue(int disappearValue) {
		this.disappearValue = disappearValue;
	}

	public int getCurrentVlaue() {
		return currentVlaue;
	}

	public void setCurrentVlaue(int currentVlaue) {
		this.currentVlaue = currentVlaue;
	}

	public byte getSpriteType() {
		return Sprite.SPRITE_TYPE_NPC;
	}

	@Override
	public Object clone() {
		ShowTitleAsHpNPC npc = new ShowTitleAsHpNPC();
		npc.cloneAllInitNumericalProperty(this);
		npc.setBornValue(bornValue);
		npc.setDisappearValue(disappearValue);
		npc.setCurrentVlaue(bornValue);
		npc.setWindowId(this.getWindowId());
		npc.setnPCCategoryId(this.getnPCCategoryId());
		return npc;
	}
}
