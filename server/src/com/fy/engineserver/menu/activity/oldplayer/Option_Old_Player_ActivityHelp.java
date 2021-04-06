package com.fy.engineserver.menu.activity.oldplayer;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.uniteserver.UnitServerActivity;
import com.fy.engineserver.uniteserver.UnitedServerManager;

public class Option_Old_Player_ActivityHelp extends Option implements NeedCheckPurview,NeedRecordNPC{

	private NPC npc;
	/**以下参数要和Option_Old_Player_ComeBack里面保持一致*/
	private String activityName;
	
	@Override
	public void doSelect(Game game, Player player) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.活动介绍_title);
		mw.setDescriptionInUUB(Translate.活动介绍_content);
		Option_Cancel option = new Option_Cancel();
		option.setText(Translate.确定);
		mw.setOptions(new Option[] {option});
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
		
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public boolean canSee(Player player) {
//		GameConstants gcConstants = GameConstants.getInstance();
//		if(gcConstants==null){
//			return false;
//		}
//		
//		long starttime = TimeTool.formatter.varChar19.parse("2013-06-08 00:00:00");
//		long endtime = TimeTool.formatter.varChar19.parse("2013-12-02 23:59:59");
//		long now = System.currentTimeMillis();
//		if(now < starttime || endtime < now){
//			return false;
//		}
//		
////		if(player.getLevel()<70){
////			return false;
////		}
//		
//		if(!PlatformManager.getInstance().isPlatformOf(Platform.官方)){
//			return false;
//		}
//		
//		String servername = gcConstants.getServerName();
//		if(servername!=null){
//			if(!servername.equals("国内本地测试") &&!servername.equals("域外之战") && !servername.equals("仙道独尊") && !servername.equals("天道再临")|| !servername.equals("神魂不灭")){
//				return false;
//			}
//		}
//		return true;
		List<UnitServerActivity> usActivities= UnitedServerManager.getInstance().getUnitServerActivityByName(activityName);
		if(usActivities==null){
			ActivitySubSystem.logger.error("[无法获取到老玩家召回活动："+usActivities+"]");
			return false;
		}
		// 服务器是否参加活动
		for (UnitServerActivity usa : usActivities) {
			try {
				if (usa.isThisServerFit() == null) {
					return true;
				}
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[" + player.getLogString() + "] [老玩家召回活动出错：" + e + "]");
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
}
