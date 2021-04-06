package com.fy.engineserver.menu.cityfight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerActivity;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 虚拟报名参加勇夺昆仑圣殿
 * 
 */
public class Option_CityFight_Baoming extends Option implements NeedCheckPurview {
	// /** 开始和结束时间 */
	// private String startTimeStr;
	// private String endTimeStr;
	//	
	// /**参加活动的服务器，逗号间隔，为空的话表示所有服都参加*/
	// private String serverNames;
	private String activityName;

	public static List<Long> zongpaiId = new ArrayList<Long>();

	@Override
	public void doSelect(Game game, Player player) {
		String result = ZongPaiManager.getInstance().宗主身份判断(player);
		if ("".equals(result)) {
			ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			Iterator<Long> idIter = zongpaiId.iterator();
			while (idIter.hasNext()) {
				long id = idIter.next();
				if (id == zongpai.getId()) {
					player.sendError(Translate.您已经报过名了);
					ActivitySubSystem.logger.warn(player.getLogString() + zongpai.getLogString() + "[已经报过名了]");
					return;
				}
			}
			player.sendError(Translate.报名成功);
			ActivitySubSystem.logger.warn(player.getLogString() + zongpai.getLogString() + "[城战报名成功]");
			zongpaiId.add(zongpai.getId());
		} else {
			player.sendError(Translate.非宗主不能报名);
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// //是否在活动时间内
		// long startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		// long endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		// long now = System.currentTimeMillis();
		// if(startTime<=now && now<=endTime){
		// rightTime = true;
		// }
		// //服务器是否参加活动
		// String servername = GameConstants.getInstance().getServerName();
		// String showServernames[] = serverNames.trim().split(",");
		// if(showServernames!=null&&showServernames.length>0){
		// for (int i = 0; i < showServernames.length; i++) {
		// if (servername.trim().equals(showServernames[i].trim())) {
		// rightServer = true;
		// }
		// }
		// }else{
		// rightServer = true; //所有服都参加
		// }
		List<UnitServerActivity> usActivities = UnitedServerManager.getInstance().getUnitServerActivityByName(activityName);
		if (usActivities == null) {
			ActivitySubSystem.logger.error("[无法获取到勇夺昆仑圣殿活动：" + usActivities + "]");
			return false;
		}
		// 服务器是否参加活动
		for (UnitServerActivity usa : usActivities) {
			try {
				if (usa.isThisServerFit() == null) {
					return true;
				}
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[" + player.getLogString() + "] [勇夺昆仑圣殿活动出错：" + e + "]");
				e.printStackTrace();
			}
		}
		return false;

	}

	// public String getStartTimeStr() {
	// return startTimeStr;
	// }
	//
	// public void setStartTimeStr(String startTimeStr) {
	// this.startTimeStr = startTimeStr;
	// }
	//
	// public String getEndTimeStr() {
	// return endTimeStr;
	// }
	//
	// public void setEndTimeStr(String endTimeStr) {
	// this.endTimeStr = endTimeStr;
	// }
	//
	// public String getServerNames() {
	// return serverNames;
	// }
	//
	// public void setServerNames(String serverNames) {
	// this.serverNames = serverNames;
	// }

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public static List<Long> getZongpaiId() {
		return zongpaiId;
	}

	public static void setZongpaiId(List<Long> zongpaiId) {
		Option_CityFight_Baoming.zongpaiId = zongpaiId;
	}
}
