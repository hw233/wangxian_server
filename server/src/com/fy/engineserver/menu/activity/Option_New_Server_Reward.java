package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedInitialiseOption;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.model.Passport;

/**
 * 新服活动，领取礼包
 * @author Administrator
 *
 */
public class Option_New_Server_Reward extends Option implements NeedCheckPurview,NeedInitialiseOption{
	

	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr="2013-06-08 00:00:00";
	private String endTimeStr="2013-06-30 23:59:59";
	
	/* 解析后的数据 */
	private long startTime;
	private long endTime;
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setDescriptionInUUB("《천신》에 오신것을 환영합니다.KT 등록 유저들을 위한 패키지를 받아가세요.즐거운 게임 되세요.");
		Option_New_Server_Reward_Sure option = new Option_New_Server_Reward_Sure();
		option.setText("수령");
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
		String[] channels = {"KOREAKT_MIESHI"};
		boolean rightChannel = false;
		boolean rightTime = false;
		
		for(String channel:channels){
			Passport passport = player.getPassport();
			if(passport != null){
				String channelName = passport.getRegisterChannel();
				if(channelName.equals(channel)){
					rightChannel = true;
				}
			}else{
				ActivityManagers.logger.warn("[用于KT开服登录活动] [玩家渠道为空]");
			}
		}
		
		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			rightTime = true;
		}
		long now = SystemTime.currentTimeMillis();
		if(startTime <= now || now <= endTime){
			rightTime = true;
		}
		
		return rightChannel&&rightTime;
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


