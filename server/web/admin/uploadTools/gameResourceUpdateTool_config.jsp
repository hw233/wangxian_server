<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackManager"%>
<%@page import="com.fy.boss.gm.gmuser.server.GmSystemNoticeManager"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<%!

	public static Map<String,String> addressConfig = new HashMap<String,String>();
	static{
		addressConfig.put("测试服", "http://119.254.154.199:12114/admin/uploadTools/gameResourceUpdateTool_right.jsp");
	}
	
	public static List<String> powers = new ArrayList<String>();
	static{
		powers.add("wtx001");
	}
	
	public static Map<String,String> paths = new HashMap<String,String>();
	static{
		paths.put("shops.xls", "/");
		paths.put("article.xls","/");
		paths.put("chargeList.xls","/");
		paths.put("npc.xls","/");
		paths.put("playerTitle.xls","/");
		paths.put("buffs.xml","/");
		paths.put("skills.xls","/");
     	paths.put("articlegreen.xls","/");
		paths.put("monster.xls","/");
		paths.put("newPlayer.xls","/");
		paths.put("shops.xls","/");
		paths.put("vip.xls","/");
		paths.put("horse2Data.xls","/horse2");
		paths.put("commonFire.xls","/");
		paths.put("fateActivity.xls","/");
		paths.put("korea.xls","/translate/");
		paths.put("taskTemplet.xlsx","/task/");
		paths.put("jiazu_biaoche.xls","/jiazu2/");
		paths.put("jiazu2data.xls","/jiazu2/");
		paths.put("skEnhance.xls","/transitRobbery/");
		paths.put("fairyrobbery.xls","/transitRobbery/");
		paths.put("activity_newChongZhi.xls","/activity/");
		paths.put("activityIntroduce.xls","/activity/");
		paths.put("addActivityTimes.xls","/activity/");
		paths.put("xianling.xls","/activity/");
		paths.put("frunace.xls","/frunace/");
		paths.put("mulActivities.xls","/activity/");
		paths.put("shopActivities.xls","/activity/");
		paths.put("exchangeActivity.xls","/activity/");
		paths.put("activity_showMess.xls","/activity/");
		paths.put("dailyturnActivity.xls","/activity/");
		paths.put("treasureActivity.xls","/activity/");
		paths.put("levelupReward.xls","/activity/");
		paths.put("wafenActivity.xls","/activity/");
		paths.put("qiancengtaActivity.xls","/activity/");
		paths.put("modify_tencent_refreashSpriteActivities.xls","/activity/");
		paths.put("refreashSpriteActivities.xls","/activity/");		
		paths.put("delivertaskAct.xls","/activity/");
		paths.put("activeness.xls","/activity/");
		paths.put("transitRobbery.xls","/transitRobbery/");
		paths.put("unitServer.xls","/");
		paths.put("xuanzhi-mieshiserver-1.0.jar","/");
		paths.put("wing.xls","/");
		paths.put("windows.xls","/");
		paths.put("npcWid.xls","/");
		paths.put("bourn.xls","/bourn/");
		paths.put("petSkill.xls","/pet2/");
		paths.put("pet2data.xls","/pet2/");
		paths.put("enchantData.xls","/enchant/");
		paths.put("magicWeapon.xls","/magicweapon/");
		paths.put("magicweapondata.xls","/magicweapon/");
		paths.put("septConf.xls","/septstation/");
		paths.put("enchantData.xls","/enchant/");
		paths.put("forUnitServer.xls","/");
		
		paths.put("modify_tencent_shops.xls", "/");
		paths.put("modify_tencent_article.xls","/");
		paths.put("modify_tencent_npc.xls","/");
		paths.put("modify_tencent_playerTitle.xls","/");
		paths.put("modify_tencent_buffs.xml","/");
		paths.put("modify_tencent_skills.xls","/");
     	paths.put("modify_tencent_articlegreen.xls","/");
		paths.put("modify_tencent_monster.xls","/");
		paths.put("modify_tencent_newPlayer.xls","/");
		paths.put("modify_tencent_shops.xls","/");
		paths.put("modify_tencent_vip.xls","/");
		paths.put("modify_tencent_taskTemplet.xlsx","/task/");
		paths.put("modify_tencent_skEnhance.xls","/transitRobbery/");
		paths.put("modify_tencent_activity_newChongZhi.xls","/activity/");
		paths.put("modify_tencent_activityIntroduce.xls","/activity/");
		paths.put("modify_tencent_addActivityTimes.xls","/activity/");
		paths.put("modify_tencent_mulActivities.xls","/activity/");
		paths.put("modify_tencent_shopActivities.xls","/activity/");
		paths.put("modify_tencent_exchangeActivity.xls","/activity/");
		paths.put("modify_tencent_activity_showMess.xls","/activity/");
		paths.put("modify_tencent_refreashSpriteActivities.xls","/activity/");		
		paths.put("modify_tencent_delivertaskAct.xls","/activity/");
		paths.put("modify_tencent_activeness.xls","/activity/");
		paths.put("modify_tencent_transitRobbery.xls","/transitRobbery/");
		paths.put("modify_tencent_wafenActivity.xls","/activity/");
		paths.put("modify_tencent_unitServer.xls","/");
		paths.put("modify_tencent_bourn.xls","/bourn/");
		paths.put("modify_tencent_petSkill.xls","/pet2/");
		paths.put("modify_tencent_pet2data.xls","/pet2/");
		paths.put("modify_tencent_magicWeapon.xls","/magicweapon/");
		paths.put("modify_tencent_dailyturnActivity.xls","/activity/");
		paths.put("modify_tencent_treasureActivity.xls","/activity/");
		paths.put("modify_tencent_refreashSpriteActivities.xls","/activity/");	
		paths.put("modify_tencent_magicweapondata.xls","/magicweapon/");
		paths.put("modify_tencent_qiancengtaActivity.xls","/activity/");
	}
	
	// wtx 2014-02-24 下载了 article.xls
	public static String recordLog(String recorder,long time,String type,String filename){
		StringBuffer str = new StringBuffer();
		str.append("["+GameConstants.getInstance().getServerName()+"] ").append("[具体操作] ").append("[").append(recorder).append("] ").append("[").append(TimeTool.formatter.varChar19.format(time)).append("] ").append("[").append(type).append("] ").append("[").append(filename).append("] ");
		NewFeedbackManager.logger.warn(str.toString());
		return str.toString();
	}
%>

