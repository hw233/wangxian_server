package com.fy.engineserver.menu.activity.oldplayer;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.translateString;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerComeBackManager;
import com.fy.engineserver.activity.oldPlayerComeBack.OldPlayerInfo;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedInitialiseOption;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerActivity;
import com.fy.engineserver.uniteserver.UnitedServerManager;

public class Option_Old_Player_ComeBack extends Option implements NeedCheckPurview ,NeedInitialiseOption {

	private String activityName="老玩家召回";
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
		
		if(oldInfo.getId()>0){
			player.sendError(translateString(Translate.您已经领取过召回id了您的召回, new String[][]{{STRING_1,oldInfo.getId()+""}}));
			return;
		}
		
		if(oldInfo.getOLD_PLAYER_TYPE()==2){
			player.sendError(Translate.请您先填写您朋友提供给您的召回id为您和朋友赢得奖励后再领取召回id);
			return;
		}
		
		try {
			long backid = ocb.getPlayerBackId(player);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{}, Translate.您的召回id, translateString(Translate.您已经领取过召回id了您的召回, new String[][]{{STRING_1,backid+""}}), 0, 0, 0, "老玩家回归活动");
			oldInfo.setId(backid);
			player.sendError(Translate.恭喜您成功获得了召回id请查看邮件);
			//这写的不好，，记得改动
			ocb.getDdc().put(player.getId()+activityKey, oldInfo);
			OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [获得召回id] [成功] [召回id:"+backid+"] ["+player.getLogString()+"] ");
		} catch (Exception e) {
			e.printStackTrace();
			OldPlayerComeBackManager.logger.warn("[老玩家回归活动] [获得召回id] [异常] ["+player.getLogString()+"] ["+e+"]");
		}
		
		
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
		
		if(player.getLevel()<Integer.parseInt(levelLimit)){
			return false;
		}
		
//		String servername = gcConstants.getServerName();
//		if(servername!=null){
//			if(!servername.equals("国内本地测试") &&!servername.equals("域外之战") && !servername.equals("仙道独尊") && !servername.equals("天道再临")|| !servername.equals("神魂不灭")){
//				return false;
//			}
//		}
//		
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
	public void init() throws Exception {
		OldPlayerComeBackManager.getInstance().activitykey = activityKey;
		
	}
	
}
