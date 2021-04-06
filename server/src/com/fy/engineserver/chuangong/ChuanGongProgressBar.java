package com.fy.engineserver.chuangong;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.FINISH_CHUANGONG_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

public class ChuanGongProgressBar implements Callbackable {

	Player player;
	long cgId;

	ChuanGongManager cgm = ChuanGongManager.getInstance();

	public ChuanGongProgressBar(Player player, long cgId) {

		this.player = player;
		this.cgId = cgId;
	}

	@Override
	public void callback() {
		this.finishChuanGong();
	}

	private void finishChuanGong() {

		Chuangong cg = cgm.map.get(cgId);
		if (cg != null) {
			try {
				this.setChuangongNum(player);

				// 主动
				Player active = cg.getActive();
				active.setChuangonging(false);
				active.setLastChuangongTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				active.setChuangongState(false);

				// 被动
				Player passive = cg.getPassive();
				passive.setChuangonging(false);
				passive.setLastAcceptChuangongTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				passive.setChuangongState(false);

				
				cg.reward(active);
				cg.reward(cg.getPassive());
				cgm.map.remove(cgId);

				AchievementManager.getInstance().record(active, RecordAction.传功次数);
				AchievementManager.getInstance().record(passive, RecordAction.被传功次数);
				
				// 活跃度统计
				ActivenessManager.getInstance().record(active, ActivenessType.传功授业);
				
				if(AchievementManager.logger.isWarnEnabled()){
					AchievementManager.logger.warn("[传功完成统计次数] [active:"+active.getLoginServerTime()+"] [passive:"+passive.getLogString()+"]");
				}
				if (ChuanGongManager.logger.isWarnEnabled()) {
					ChuanGongManager.logger.warn("[传功完成] [active:" + active.getLoginServerTime() + "] [active:" + active.getLogString() + "] [passive:" + passive.getLogString() + "]");
				}
				FINISH_CHUANGONG_RES res = new FINISH_CHUANGONG_RES(GameMessageFactory.nextSequnceNum(), false);
				active.addMessageToRightBag(res);
				passive.addMessageToRightBag(res);
			} catch (Exception e) {
				ChuanGongManager.logger.error("[传功异常] [activite:" + (cg.getActive() != null ? cg.getActive().getLogString() : "null") + "] [passive:" + (cg.getPassive() != null ? cg.getPassive().getLogString() : "null") + "]", e);
			}
		} else {
			ChuanGongManager.logger.error("[完成传功错误] [没有指定传功对象] [" + cgId + "]");
		}
	}

	private void setChuangongNum(Player active) {

		boolean isSameDay = TimeTool.instance.isSame(SystemTime.currentTimeMillis(), active.getLastChuangongTime(), TimeDistance.DAY);
		if (isSameDay) {
			active.setChuangongNum(active.getChuangongNum() + 1);
		} else {
			active.setChuangongNum(1);
		}
		ChuanGongManager.logger.warn("[传功完成设置次数] [" + active.getChuangongNum() + "] [" + active.getLogString() + "] [现在:" + SystemTime.currentTimeMillis() + "] [上一次:" + active.getLastChuangongTime() + "]");
	}

}
