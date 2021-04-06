package com.fy.engineserver.menu.activity;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedInitialiseOption;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 新服活动，领取礼包
 * @author Administrator
 *
 */
public class Option_Continue_Activity_Reward extends Option implements NeedCheckPurview,NeedInitialiseOption{
	

	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr="2013-07-03 00:00:00";
	private String endTimeStr="2013-07-10 23:59:59";
	
	/* 解析后的数据 */
	private long startTime;
	private long endTime;
	
	
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		String servername = GameConstants.getInstance().getServerName();
		Set<String> set = new HashSet<String>();
		set.add("天下无双");
		set.add("海纳百川");
		set.add("琼楼金阙");
		set.add("飘渺仙道");
		set.add("万里苍穹");
		set.add("盛世欢歌");
		set.add("修罗转生");
		String mesString = "各位仙友，首先感谢大家长时间以来对《飘渺寻仙曲》的关注以及厚爱，由于7月2日维护后出现的连续登录奖励变更的BUG，想必不少同学因为没有领取到相应的连登奖励而感到懊恼。不过在7月3日重新维护后，也就是今天，为感谢大家的支持，丰厚的奖励将会再次回馈所有新老玩家，同时线上开启新属性宝石兑换、VIP专属礼包等多重给力活动！";
		mw.setDescriptionInUUB(mesString);
		Option_Continut_Activity_Reward_Sure option = new Option_Continut_Activity_Reward_Sure();
		option.setText("领取");
		mw.setOptions(new Option[]{option});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
	}
	
	@Override
	public void init() {
		if (startTimeStr == null || "".equals(startTimeStr)) {
			System.out.println(getText() + "无时间配置");
			return;
		}
		startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
	}
	
	@Override
	public boolean canSee(Player player) {
		String servername = GameConstants.getInstance().getServerName();
		if(servername==null){
			return false;
		}
		
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(new Platform[] { Platform.官方 })) {
			return false;
		}
		
		return true;
	}

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}


