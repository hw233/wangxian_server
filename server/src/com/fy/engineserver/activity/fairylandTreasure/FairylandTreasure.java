package com.fy.engineserver.activity.fairylandTreasure;

import com.fy.engineserver.activity.pickFlower.FlushPoint;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.fairylandTreasure.Option_FairylandTreasure;
import com.fy.engineserver.menu.fairylandTreasure.Option_FairylandTreasure_Yaoshi;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class FairylandTreasure {
	private int level;
	// 可不可以被采 ，一个人正在采就为false 不能采
	private boolean effect;

	// 对应的npc已经清除，只有清除了(true)，才能在心跳中再次出现npc
	private boolean disappear;
	// 进度条完成后 设置npc消失时间
	// private long npcDisappearTime;

	// 进度条完成后 设置新npc出现时间(随机5 - 10秒);
	// private long npcAppearTime;

	// private ArticleEntity ae;

	private long pickingPlayerId;

	// 创建的时候赋值这都是固定的
	public FlushPoint point;
	public FairylandTreasureEntity fairylandTreasureEntity;
	public Game game;

	public FairylandTreasureNpc npc;

	public void pick(Player player, FairylandTreasureNpc npc) {
		if (player.getLevel() < 221) {
			player.sendError(Translate.等级不足);
			return;
		}

		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		String key = ftm.getCurrentDateStr() + player.getId();
		if (ftm.disk.get(key) != null) {
			int picknum = (Integer) ftm.disk.get(key);
			if (picknum >= ftm.getMaxOpenNum()) {
				player.sendError(Translate.已达今日上限);
				return;
			}
		}

		if (this.getPickingPlayerId() == player.getId() || this.getPickingPlayerId() == 0l) {
			effect = true;
		}

		if (!effect) {
			// 提示玩家别人正在开箱子
			player.sendError(Translate.别人正在开箱子你不能同时进行);
			if (FairylandTreasureManager.logger.isInfoEnabled()) {
				FairylandTreasureManager.logger.info("[别人正在开宝箱] [" + player.getLogString() + "]");
			}
		} else {

			boolean bln = player.getKnapsack_common().isFull();

			if (!bln) {
				if (this.disappear) {
					return;
				}
				String countkey = ftm.getCurrentDateStr() + player.getId();
				int canOpenNum = 0;
				if (ftm.disk.get(countkey) == null) {
					canOpenNum = ftm.getMaxOpenNum();
				} else {
					int num = (Integer) ftm.disk.get(countkey);
					canOpenNum = ftm.getMaxOpenNum() - num;
				}
				// 开箱子过程
				String boxKeyName = ftm.getFairylandBoxList().get(level).getBoxKeyName();
				int count = player.getKnapsack_common().countArticle(boxKeyName);
				if (count >= 1) {
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setDescriptionInUUB(Translate.translateString(Translate.消耗钥匙开宝箱提示, new String[][] { { Translate.STRING_1, boxKeyName }, { Translate.STRING_2, "" + canOpenNum } }));
					Option_FairylandTreasure_Yaoshi option_sure = new Option_FairylandTreasure_Yaoshi(this, boxKeyName);
					option_sure.setText(Translate.确定);
					Option_Cancel option_cancle = new Option_Cancel();
					option_cancle.setText(Translate.取消);
					mw.setOptions(new Option[] { option_sure, option_cancle });
					QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(req);
					// if (npc != null) {
					// game.removeSprite(npc);
					// this.disappear=true;
					// } else {
					// FairylandTreasureManager.logger.warn("[仙界宝箱删除npc错误] [npc null]");
					// }
					// effect = false;
					// this.setPickingPlayerId(player.getId());
					// player.removeArticle(boxKeyName, "仙界宝箱");
					// ftm.send_START_DRAW_RES(player, ftm.getFairylandBoxList().get(level));
				} else {
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setDescriptionInUUB(Translate.translateString(Translate.花费银子开宝箱提示, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(ftm.getFairylandBoxList().get(level).getNeedSilver()) }, { Translate.STRING_2, "" + canOpenNum } }));
					Option_FairylandTreasure option_sure = new Option_FairylandTreasure(this);
					option_sure.setText(Translate.确定);
					Option_Cancel option_cancle = new Option_Cancel();
					option_cancle.setText(Translate.取消);
					mw.setOptions(new Option[] { option_sure, option_cancle });
					QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(req);
				}
				return;
			} else {
				player.sendError(Translate.背包空间不足);
			}
		}

	}

	// npc 出现设置
	public void appperUpdate() {

		effect = true;
		disappear = false;
	}
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public boolean isDisappear() {
		return disappear;
	}

	public void setDisappear(boolean disappear) {
		this.disappear = disappear;
	}

	public long getPickingPlayerId() {
		return pickingPlayerId;
	}

	public void setPickingPlayerId(long pickingPlayerId) {
		this.pickingPlayerId = pickingPlayerId;
	}

	public FlushPoint getPoint() {
		return point;
	}

	public void setPoint(FlushPoint point) {
		this.point = point;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public FairylandTreasureEntity getFairylandTreasureEntity() {
		return fairylandTreasureEntity;
	}

	public void setFairylandTreasureEntity(FairylandTreasureEntity fairylandTreasureEntity) {
		this.fairylandTreasureEntity = fairylandTreasureEntity;
	}

	public FairylandTreasureNpc getNpc() {
		return npc;
	}

	public void setNpc(FairylandTreasureNpc npc) {
		this.npc = npc;
	}

}
