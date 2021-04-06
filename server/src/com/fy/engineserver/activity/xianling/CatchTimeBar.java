package com.fy.engineserver.activity.xianling;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;

public class CatchTimeBar extends TimerTask implements Callbackable {
	private XianLingChallenge xc;
	private boolean result;
	private Player player;
	private int monsterCategoryId;

	public CatchTimeBar(XianLingChallenge xc, boolean result, Player player, int monsterCategoryId) {
		super();
		this.xc = xc;
		this.result = result;
		this.player = player;
		this.monsterCategoryId = monsterCategoryId;
	}

	public CatchTimeBar() {
	}

	@Override
	public void callback() {
		try {
			if (result) { // 成功则通知
				xc.setResult(XianLingChallenge.SUCCEED);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵] [读条结束] [读条开始时间：" + sdf.format(new Date(getStartTime())) + "] [" + player.getLogString() + "] [monsterCategoryId:" + monsterCategoryId +"] [targetId:" + xc.getTargetId() + "]");
				
			}
		} catch (Exception e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [读条异常]", e);
		}
	}

	@Override
	public void breakNoticePlayer(Player owner) {
		super.breakNoticePlayer(owner);
		owner.sendError("捕捉失败");
		XianLingChallengeManager.logger.warn("[捕捉进度条打断] [" + owner.getLogString() + "]");
	}

	public XianLingChallenge getXc() {
		return xc;
	}

	public void setXc(XianLingChallenge xc) {
		this.xc = xc;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getMonsterCategoryId() {
		return monsterCategoryId;
	}

	public void setMonsterCategoryId(int monsterCategoryId) {
		this.monsterCategoryId = monsterCategoryId;
	}

}
