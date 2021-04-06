package com.fy.engineserver.menu.vaildate;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.validate.ValidateAsk;

public class Option_InputRequestBuySaleAsk extends Option {

	private int index;
	private long entityId;
	private int saleNum;
	private long rbId;
	private Player player;
	private ValidateAsk validteAsk;
	
	public Option_InputRequestBuySaleAsk() {}
	
	public Option_InputRequestBuySaleAsk(int index, long entityId, int saleNum, long rbId, Player player, ValidateAsk validteAsk) {
		this.index = index;
		this.entityId = entityId;
		this.saleNum = saleNum;
		this.rbId = rbId;
		this.setPlayer(player);
		this.setValidteAsk(validteAsk);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doInput(Game game, Player player, String inputContent) {
		if (player != this.player) {
			if (RequestBuyManager.logger.isWarnEnabled()) {
				RequestBuyManager.logger.warn("[求购答题] [答题] " + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [非本人]");
			}
			return;
		}
		boolean right = TaskSubSystem.validateManager.notifyAnswerAsk(player, validteAsk, inputContent, 1);
		if (right) {
			long exp = player.getNextLevelExp() / 1000;
			if (exp < 500) {
				exp = 500;
			}
			RequestBuyManager.getInstance().releaseRequestSale(player, index, entityId, saleNum, rbId, false);
//			player.addExp(exp, ExperienceManager.ADDEXP_REASON_QUIZ);
//			player.sendError("恭喜你回答正确,获得了" + exp + "经验");
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[求购答题] [答题] " + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [正确]");
			}
		} else {
			player.sendError("很可惜你回答错误,开动脑筋再想想");
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[求购答题] [答题] " + player.getLogString() + "[玩家输入答案:" + inputContent + "] [题目:" + validteAsk.toString() + "] [失败]");
			}
		}
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setRbId(long rbId) {
		this.rbId = rbId;
	}

	public long getRbId() {
		return rbId;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setValidteAsk(ValidateAsk validteAsk) {
		this.validteAsk = validteAsk;
	}

	public ValidateAsk getValidteAsk() {
		return validteAsk;
	}
}
