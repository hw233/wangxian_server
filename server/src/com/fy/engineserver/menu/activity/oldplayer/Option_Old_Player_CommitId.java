package com.fy.engineserver.menu.activity.oldplayer;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerComeBackManager;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerInfo;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.uniteserver.UnitServerActivity;
import com.fy.engineserver.uniteserver.UnitedServerManager;

public class Option_Old_Player_CommitId extends Option implements NeedCheckPurview , NeedRecordNPC{
	private NPC npc;
	/**以下三个参数要和Option_Old_Player_ComeBack里面保持一致*/
	private String activityName;
	private String activityKey;
	private String levelLimit;
	
	@Override
	public void doSelect(Game game, Player player) {
		OldPlayerComeBackManager ocb = OldPlayerComeBackManager.getInstance();
		OldPlayerInfo oldInfo = (OldPlayerInfo)ocb.getDdc().get(player.getId()+activityKey);
		
		if(oldInfo==null){
			player.sendError(Translate.获取找回id错误请您重新登录);
			OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [获得召回id] [错误] [玩家召回id信息记录错误] ["+player.getLogString()+"]");
			return;
		}
		
		if(oldInfo!=null && oldInfo.getOLD_PLAYER_TYPE()!=2){
			player.sendError(Translate.没有登录了的玩家才能填写召回id);
			return;
		}
		
		if(oldInfo!=null){
			try {
				ActivitySubSystem.logger.warn("lastClickTime:"+ActivityManagers.lastClickTime+"--"+(System.currentTimeMillis()-ActivityManagers.lastClickTime));
//				if(System.currentTimeMillis()>ActivityManagers.lastClickTime){
//					ActivityManagers.lastClickTime = System.currentTimeMillis()+1000*50;
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					mw.setTitle(Translate.填写召回id);
					mw.setDescriptionInUUB(Translate.请认真填写召回您朋友提供给您的召回id);
					Option_BackId_Input option = new Option_BackId_Input(activityKey);
					option.setText(Translate.确定);
					mw.setOptions(new Option[] {option});
					INPUT_WINDOW_REQ req = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)1, (byte)30, "", Translate.确定, Translate.取消, new byte[]{});
					player.addMessageToRightBag(req);
//				}else{
//					player.sendError(Translate.操作过快);
//					return;
//				}
			} catch (Exception e) {
				e.printStackTrace();
				OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [获得召回id] [异常] ["+player.getLogString()+"] ["+e+"]");
			}
		}
		
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public boolean canSee(Player player) {
		if(player.getLevel()<Integer.parseInt(levelLimit)){
			return false;
		}
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
//		GameConstants gcConstants = GameConstants.getInstance();
//		if(gcConstants==null){
//			return false;
//		}
//		
//		if(!PlatformManager.getInstance().isPlatformOf(Platform.官方)){
//			return false;
//		}
//		
//		long starttime = TimeTool.formatter.varChar19.parse("2013-10-30 00:00:00");
//		long endtime = TimeTool.formatter.varChar19.parse("2013-12-02 23:59:59");
//		long now = System.currentTimeMillis();
//		if(now < starttime || endtime < now){
//			return false;
//		}
//		//填写召回id的玩家必须是大于70
//		if(player.getLevel()<70){
//			return false;
//		}
		
//		String servername = gcConstants.getServerName();
//		if(servername!=null){
//			if(!servername.equals("国内本地测试") &&!servername.equals("域外之战") && !servername.equals("仙道独尊") && !servername.equals("天道再临")|| !servername.equals("神魂不灭")){
//				return false;
//			}
//		}
//		
//		return true;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}

	public String getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(String levelLimit) {
		this.levelLimit = levelLimit;
	}

	@Override
	public NPC getNPC() {
		// TODO Auto-generated method stub
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
		
	}
	
}
