package com.fy.engineserver.activity.chestFight.model;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.chestFight.instance.ChestFight;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 宝箱争夺模板
 *
 */
public class ChestFightModel {
	/** 宝箱id */
	private int chestId;
	/** 拿到宝箱的角色id */
	private long playerId;
	/** 获取宝箱时间 */
	private long obtainTime;
	/** 宝箱类型 */
	private int chestType;
	/** 宝箱物品是否已经被获取 */
	private boolean open = false;
	
	private volatile int pointIndex;
	
	@Override
	public String toString() {
		return "ChestFightModel [chestId=" + chestId + ", playerId=" + playerId + ", obtainTime=" + obtainTime + ", chestType=" + chestType + ", open=" + open + "]";
	}
	/**
	 * 宝箱丢失
	 * @param player
	 */
	public String dropChest(Player player) {
		if (open) {
			return "宝箱已经被开启";
		}
		if (playerId > 0) {
			playerId = -1;
			obtainTime = -1;
			return null;
		}
		return "宝箱未被获取";
	}
	/**
	 * 捡起宝箱
	 * @param player
	 */
	public boolean pickUpChest(Player player) {
		if (open) {
			return false;
		}
		if (playerId > 0) {
			return false;
		}
		playerId = player.getId();
		obtainTime = System.currentTimeMillis();
		return true;
	}
	
	public boolean openChest(long playerId,long now,ChestFight c) {
		if (open || this.playerId <= 0 || this.playerId != playerId) {
			return false;
		}
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			ChestModel model = ChestFightManager.inst.chests.get(chestType);
			if (model == null) {
				ChestFightManager.logger.warn("[宝箱争夺] [开启宝箱] [失败] [宝箱模板不存在] [" + playerId + "] [" + chestType + "]");
				return false;
			}
			if (now >= (this.getObtainTime() + model.getOpenTime())) {
				open = true;
			} else {
				return false;
			}
			StringBuffer sb = new StringBuffer();
			List<ChestArticleModel> aeList = new ArrayList<ChestArticleModel>();
			int ran = 0 ;
			for (int i=0; i<model.getArticleList().size(); i++) {
				ran = player.random.nextInt(10000);
				if (ran <= model.getArticleList().get(i).getProb()[model.getChestType()-1]) {
					//获得物品
					aeList.add(model.getArticleList().get(i));
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(model.getArticleList().get(i).getArticleCNNName());
					if (model.getArticleList().get(i).getNum() > 1) {
						sb.append("*" + model.getArticleList().get(i).getNum());
					}
				}
			}
			ChestFightManager.put2Bag(player, aeList);
			player.setChestType();
			player.sendError(String.format(Translate.获得XX物品,sb.toString()));
			ChestFightManager.logger.warn("[宝箱争夺] [开启宝箱] [成功] [" + player.getLogString() + "] [" + this.toString() + "] [" + sb.toString() + "]");
			return true;
		} catch (Exception e) {
			ChestFightManager.logger.warn("[宝箱争夺] [开启宝箱] [异常] [" + playerId + "] [" + chestType + "]", e);
		}
		return true;
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getObtainTime() {
		return obtainTime;
	}
	public void setObtainTime(long obtainTime) {
		this.obtainTime = obtainTime;
	}
	public int getChestType() {
		return chestType;
	}
	public void setChestType(int chestType) {
		this.chestType = chestType;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public int getPointIndex() {
		return pointIndex;
	}
	public void setPointIndex(int pointIndex) {
		this.pointIndex = pointIndex;
	}
	
	
}
