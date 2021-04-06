package com.fy.engineserver.activity.dailyTurnActivity.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
/**
 * 每日转盘活动disk存储，保存玩家当日抽取到的物品信息
 * @author Administrator
 *
 */
public class DiskFileModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 玩家id */
	private long playerId;
	/** 更新时间，根据此时间做周期清除 */
	private long updateTime;
	/** 周期内抽奖信息   key=转盘类型*/
	private Map<Integer, PlayerRewardInfo> rewardInfo = new HashMap<Integer, PlayerRewardInfo>();
	
	public void reset(long now, Player player) {
		boolean result = TimeTool.instance.isSame(now, updateTime, TimeDistance.DAY);
		if (!result) {
			long oldTime = updateTime;
			updateTime = now;
			player.setOne_day_rmb(-1);
			for (PlayerRewardInfo pri : rewardInfo.values()) {
				pri.reset();
			}
			if (DailyTurnManager.logger.isInfoEnabled()) {
				DailyTurnManager.logger.info("[重置玩家抽奖信息] [成功] [" + player.getLogString() + "] [上次重置时间:" + oldTime + "]");
			}
		}
	}
	/**
	 * 给予奖励之前先改变使用状态
	 * @param player
	 * @param turnId
	 * @return
	 */
	public String changeUseStatus(Player player, int turnId) {
		int[] status = this.getConditionStatus(player, turnId);
		TurnModel tm = DailyTurnManager.instance.turnMaps.get(turnId);
		PlayerRewardInfo pri = rewardInfo.get(turnId);
		if (pri == null) {
			pri = new PlayerRewardInfo(tm.getTurnId(), tm.getTurnName());
			rewardInfo.put(turnId, pri);
		}
		String result = "需要达到一定条件才可继续参与抽奖！";
		boolean hasTips = false;
		for (int i=0; i<status.length; i++) {
			if (status[i] == DailyTurnManager.获取未用 && pri.changeUseStatus(i)) {
				return null;
			} else if (status[i] == DailyTurnManager.未获取 &&  tm.getConditions().length > (i+1) && !hasTips) {
				result = tm.getConditions()[i+1];
				hasTips = true;
			}
		}
		return result;
	}
	
	/**
	 * 对应条件玩家获取到抽奖资格状态  (由于每个转盘次数获取条件不一定统一，得单做)
	 * @return
	 */
	public int[] getConditionStatus(Player player, int turnId) {
		try {
			TurnModel tm = DailyTurnManager.instance.turnMaps.get(turnId);
			int[] result = new int[tm.getMaxPlayTimes()];
			PlayerRewardInfo pri = rewardInfo.get(turnId);
			if (pri != null && pri.getUseStatus() != null && pri.getUseStatus().length == result.length) {
				result = pri.getUseStatus();
			} else {
				if (DailyTurnManager.logger.isDebugEnabled()) {
					DailyTurnManager.logger.debug("[rewardInfo中没有对应信息] [pri:" + pri + "] [pri.getUseStatus():" +
							(pri==null?null:pri.getUseStatus()) + "]");
				}
			}
			long cycleChongzhi = DailyTurnManager.instance.getCycleChongzhi(player);					//当日充值金额
			byte vip = player.getVipLevel();
			switch (turnId) {			
			case 1:							//目前只有第一类转盘为特殊，需要单做
				if (result[0] != DailyTurnManager.获取已用) {
					result[0] = DailyTurnManager.获取未用;
				}
				if (vip >= tm.getVipLimit() && result[1] != DailyTurnManager.获取已用) {
					result[1] = DailyTurnManager.获取未用;
				}
				if (cycleChongzhi >= tm.getChongzhiLimit() && result[2] != DailyTurnManager.获取已用) {
					result[2] = DailyTurnManager.获取未用;
				}
				if (result[2] == DailyTurnManager.未获取) {
					result[2] = DailyTurnManager.需要充值;
				}
				break;
			default:						//除第一类外都统一条件   1.vip免费获得   2.周期内充值金额完成自动获得    3.购买获得
				if (vip >= tm.getVipLimit() && result[0] != DailyTurnManager.获取已用) {		
					result[0] = DailyTurnManager.获取未用;
				}
				if (cycleChongzhi >= tm.getChongzhiLimit() && result[1] != DailyTurnManager.获取已用) {
					result[1] = DailyTurnManager.获取未用;
				}
				if (result[1] == DailyTurnManager.未获取) {
					result[1] = DailyTurnManager.需要充值;
				}
				if (pri != null && pri.getPurchaseTimes() > 0 && result[2] != DailyTurnManager.获取已用) {
					result[2] = DailyTurnManager.获取未用;
				}
				if (result[2] == DailyTurnManager.未获取) {
					result[2] = DailyTurnManager.可购买;
				}
				break;
			}
			this.changeAllStatus(result);
			return result;
		} catch (Exception e) {
			DailyTurnManager.logger.warn("[getConditionStatus] [异常]", e);
		}
		return null;
	}
	
	private void changeAllStatus(int[] aa) {
		for (int i=0; i<aa.length; i++) {
			if (aa[i] == DailyTurnManager.未获取) {
				aa[i] = DailyTurnManager.需要充值;
			}
		}
	}
	
	/**
	 * 获取按钮描述
	 * @param turnId
	 * @return
	 */
	public String getBtnStr(int turnId) {
		return "我要抽大奖";
	}
	
	@Override
	public String toString() {
		return "DiskFileModel [playerId=" + playerId + ", updateTime=" + updateTime + ", rewardInfo=" + rewardInfo + "]";
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public Map<Integer, PlayerRewardInfo> getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(Map<Integer, PlayerRewardInfo> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	
	
}
