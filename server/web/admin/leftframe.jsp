<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.fy.engineserver.core.*,com.fy.engineserver.downcity.*,java.util.*"%>

<%@page import="com.xuanzhi.tools.authorize.AuthorizedServletFilter"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="css/dtree.css" type="text/css" rel="stylesheet" />
<SCRIPT src="js/dtree.js" type=text/javascript></SCRIPT>

<META content="MSHTML 6.00.2900.3157" name=GENERATOR>
<style type="text/css">
<!--
body,td,th {
	font-size: 12px;
}

body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #DDEDFF;
}
-->
</style>
</HEAD>
<BODY>
<div align="center" class="dtree"><A
	href="javascript:%20d.openAll();" class="node">全部展开</A> | <A
	href="javascript:%20d.closeAll();" class="node">全部折叠</A></br>
</br>
</div>
<SCRIPT type=text/javascript>
		d = new dTree('d');

		d.add(0,-1,'所有游戏网关');

		
		<%
		ArrayList<Object[]> al = new ArrayList<Object[]>();
		
		al.add(new Object[]{1,0,"测试游戏网关",""});
		al.add(new Object[]{101,1,"在线玩家图形","admin/stat/online_num.jsp"});
		al.add(new Object[]{102,1,"内存使用图形","admin/stat/mem_stat.jsp"});
		al.add(new Object[]{103,1,"客户端链接情况","admin/conns.jsp?bean=game_server"});
		al.add(new Object[]{104,1,"玩家列表","admin/players.jsp?bean=sprite_sub_system"});
		al.add(new Object[]{105,1,"在线玩家列表！","admin/playersonline.jsp?bean=sprite_sub_system"});
		al.add(new Object[]{106,1,"在线玩家检查","admin/playersonlineCheck.jsp?bean=sprite_sub_system"});
		al.add(new Object[]{129,1,"疑似打金工作室","admin/dajing_gongzuoshi.jsp"});
		al.add(new Object[]{107,1,"数据库玩家检查","admin/allplayersCheck.jsp"});
		al.add(new Object[]{108,1,"在线玩家流量统计","admin/playersflowstat.jsp?bean=sprite_sub_system"});
		al.add(new Object[]{109,1,"跟踪玩家流量","admin/playersflowstat2.jsp?bean=sprite_sub_system"});
		al.add(new Object[]{109,1,"加速外挂检测","admin/check_players_jiasu.jsp"});
		al.add(new Object[]{120,1,"玩家网络状况统计","admin/stat/stat_network.jsp"});
		al.add(new Object[]{121,1,"数据包统计","admin/packetstat.jsp?bean=game_gateway"});
		al.add(new Object[]{122,1,"入服排队","admin/lineup/lineup.jsp"});
		
		al.add(new Object[]{130,1,"打金网络","admin/smith/mm.jsp"});
		al.add(new Object[]{131,1,"打物品网络","admin/smith/am.jsp"});
		al.add(new Object[]{132,1,"封禁查询","admin/smith/iforbid.jsp"});
		al.add(new Object[]{133,1,"按键外挂","admin/smith/waigua.jsp"});
		al.add(new Object[]{134,1,"查看检查","admin/faery/waigua/sendGetSomething4Android.jsp"});
		al.add(new Object[]{135,1,"筛选检查","admin/faery/waigua/AnalyseSomething4Android.jsp"});
		
		al.add(new Object[]{123,1,"线程情况","thread/threadDump_1_5.jsp?s=&n="});
		//al.add(new Object[]{160,1,"线程超时日志","admin/checkThreadLog.jsp"});
		al.add(new Object[]{124,1,"DiskCache","admin/showalldiskcache.jsp"});
		al.add(new Object[]{125,1,"与BOSS系统链接情况","admin/conns.jsp?bean=boss_system_client"});
		al.add(new Object[]{126,1,"对象跟踪","admin/objecttrack/object_tracker_service.jsp"});
		
		al.add(new Object[]{127,1,"与激活码系统连接情况 ","admin/conns.jsp?bean=boss_confirmation_client"});
		al.add(new Object[]{128,1,"与网关系统连接情况 ","admin/conns.jsp?bean=gateway_sendmessage_selector"});
		
		al.add(new Object[]{196,1,"清理垃圾内存","admin/allplayersCheckForJPA.jsp"});
		al.add(new Object[]{197,1,"SimpleJPA状态","admin/objecttrack/simplejpa_service.jsp"});
		al.add(new Object[]{198,1,"SimpleJPA对象读写跟踪","admin/objecttrack/operation_tracker_service.jsp"});
		al.add(new Object[]{199,1,"游戏心跳跟踪","admin/objecttrack/heartbeat_tracker_service.jsp"});
		
		//al.add(new Object[]{109,1,"与Stat系统链接情况","admin/conns.jsp?bean=stat_system_client"});
		//al.add(new Object[]{111,1,"与Cache系统链接情况","admin/conns.jsp?bean=cache_system_client"});
		//al.add(new Object[]{114,1,"跨服系统链接","admin/conns.jsp?bean=cross_gateway_selector"});
		//al.add(new Object[]{112,1,"数据库连接池情况","admin/dbpool/poolinfo.jsp"});
		//al.add(new Object[]{113,1,"统计消息队列","admin/queue/queue.jsp"});
		
		//al.add(new Object[]{121,1,"玩家的属性","admin/playersproperty.jsp"});
		//al.add(new Object[]{122,1,"技能公式比较","admin/listskillformula.jsp"});
		
		
		al.add(new Object[]{2,0,"游戏数据管理",""});
		al.add(new Object[]{200,2,"菜单系统","admin/menu_windows.jsp"});
		al.add(new Object[]{201,2,"设置检查路径","admin/checkpath.jsp"});
		al.add(new Object[]{202,2,"显示人物技能","admin/skills.jsp"});
		al.add(new Object[]{203,2,"显示怪物技能","admin/monsterskills.jsp"});
		al.add(new Object[]{223,2,"显示技能后效数据","admin/skillshouxiao.jsp"});
		al.add(new Object[]{204,2,"显示BUFF数据","admin/buffs.jsp"});
		al.add(new Object[]{205,2,"显示职业数据","admin/career.jsp"});
		al.add(new Object[]{221,2,"显示地图副本信息","admin/games_and_fubens_info.jsp"});
		al.add(new Object[]{218,2,"显示地图信息","admin/gamesinfo.jsp"});
		al.add(new Object[]{219,2,"显示地图功能NPC","admin/function_npcs_on_map.jsp"});
		al.add(new Object[]{215,2,"显示问题集","admin/questions.jsp"});
		al.add(new Object[]{222,2,"服务器人物技能加点显示","admin/player_on_server_skill.jsp"});
		al.add(new Object[]{206,2,"上传数据资源","admin/upLoadsvalid.jsp"});
		al.add(new Object[]{209,2,"经验值数据","admin/exp.jsp"});
		al.add(new Object[]{210,2,"体力任务验证题目","admin/check/validateAsks.jsp"});

		al.add(new Object[]{16,0,"GM频道",""});
		al.add(new Object[]{16218,16,"开启自动放号","admin/test.jsp"});
		al.add(new Object[]{16220,16,"GM邮件1","admin/gmmailstoplayer.jsp"});
		al.add(new Object[]{16221,16,"GM邮件2","admin/gmsendmailtoplayer.jsp"});
		al.add(new Object[]{16219,16,"GM设置系统自动发言","admin/gmSendMessage.jsp"});
		al.add(new Object[]{16222,16,"战场","admin/fightingplace.jsp"});
		al.add(new Object[]{16223,16,"DOTA战场","admin/dotabattlefield.jsp"});
		al.add(new Object[]{16224,16,"系统公告","admin/systemannouncement.jsp"});
		al.add(new Object[]{16226,16,"刷新BOSS","admin/flushBoss.jsp"});
		al.add(new Object[]{16227,16,"查询反馈","admin/feedback/showQueryFeedback.jsp"});
		al.add(new Object[]{16228,16,"gm群发邮件","admin/sendMailBySystem.jsp"});
		al.add(new Object[]{16229,16,"打印反馈","admin/feedback/printFeedback.jsp"});
		al.add(new Object[]{16230,16,"屏蔽词管理","admin/wordfilter.jsp"});
		
		al.add(new Object[]{3,0,"角色数据管理",""});
		al.add(new Object[]{301,3,"查询玩家角色","admin/player/selectuserbyuserid.jsp"});
		al.add(new Object[]{302,3,"查询角色状态","admin/player/query_player.jsp"});
		al.add(new Object[]{303,3,"设置玩家眩晕来查看是否使用setPosition外挂","admin/player/mod_player_checksetpositionwaigua.jsp"});
		al.add(new Object[]{304,3,"设置角色的属性！","admin/player/mod_player.jsp"});
		al.add(new Object[]{305,3,"查看角色的属性","admin/player/mod_playerLookOnly.jsp"});
		al.add(new Object[]{306,3,"角色物品装备修改！","admin/player/ArticlesOnPlayerModify.jsp"});
		al.add(new Object[]{307,3,"角色物品装备查看","admin/player/ArticlesOnPlayerLookOnly.jsp"});
		al.add(new Object[]{308,3,"角色封印","admin/player/seal_player.jsp"});
		al.add(new Object[]{309,3,"角色邮箱","admin/player/mails.jsp"});
		al.add(new Object[]{310,3,"角色发送邮件","admin/player/mail_send.jsp"});
		al.add(new Object[]{312,3,"用邮件给玩家发已有的物品","admin/sendMailByEntityId.jsp"});
		al.add(new Object[]{313,3,"用邮件给玩家发新生成的物品","admin/sendMailForGroup.jsp"});

		al.add(new Object[]{4,0,"物品管理",""});
		al.add(new Object[]{402,4,"查询物品资源","admin/Articles.jsp"});
		al.add(new Object[]{403,4,"查询物品数据库","admin/ArticlesQueryById.jsp"});
		al.add(new Object[]{404,4,"查询套装","admin/suitEquipment.jsp"});
		al.add(new Object[]{405,4,"内存中临时物品的信息","admin/ArticleMemorys.jsp"});

		al.add(new Object[]{5,0,"任务管理",""});
		
		al.add(new Object[]{520,5,"[新]任务列表","admin/task/TaskList.jsp"});
		al.add(new Object[]{521,5,"[新]任务测试","admin/task/test.jsp"});
		al.add(new Object[]{522,5,"[新]迅速杀怪","admin/task/killMonster.jsp"});
		al.add(new Object[]{523,5,"[新]重置押镖任务","admin/task/resetTaskTimes.jsp"});
		al.add(new Object[]{524,5,"[新]查看角色任务","admin/task/PlayerTaskList.jsp"});
		al.add(new Object[]{529,5,"[新]自动完成主线任务","admin/task/PlayerMainTask.jsp"});
		al.add(new Object[]{525,5,"<font color=red>[新]任务加载信息</font>","admin/task/taskLoadInfo.jsp"});
		al.add(new Object[]{526,5,"当前地图上的NPC任务状态","admin/task/functionNpcCheck.jsp"});
		al.add(new Object[]{527,5,"所有任务地图分布","admin/task/seeGameTasks.jsp"});
		al.add(new Object[]{528,5,"修复无主线任务","admin/task/repairPlayerTask.jsp"});
		
		al.add(new Object[]{6,0,"精灵管理",""});
		al.add(new Object[]{601,6,"NPC列表","admin/npcs.jsp"});
		al.add(new Object[]{602,6,"NPC模板","admin/npc_templates.jsp"});
		al.add(new Object[]{603,6,"怪物列表","admin/sprites.jsp"});
		al.add(new Object[]{604,6,"显示怪模板数据","admin/monster_templates.jsp"});
		al.add(new Object[]{608,6,"刷怪专用","admin/monster_flushs.jsp"});
		al.add(new Object[]{605,6,"显示NPC所在地图","admin/npc_temp.jsp"});
		al.add(new Object[]{606,6,"显示怪所在地图","admin/sprite_temp.jsp"});
		
		al.add(new Object[]{38,0,"渡劫-大师技能",""});
		al.add(new Object[]{3801,38,"大师技能","admin/dujie/skMaster.jsp"});
		al.add(new Object[]{3802,38,"渡劫","admin/dujie/robbery.jsp"});
		al.add(new Object[]{3803,38,"渡劫修复线程","admin/dujie/robbery2.jsp"});
		
		al.add(new Object[]{19,0,"宠物系统",""});
		al.add(new Object[]{1901,19,"宠物创建","admin/pet/createpet.jsp"});
		al.add(new Object[]{1902,19,"宠物查询","admin/pet/queryPet.jsp"});
		al.add(new Object[]{1903,19,"设置宠物技能","admin/pet/reBindSkill.jsp"});
		al.add(new Object[]{1904,19,"根据道具id查询","admin/pet/queryPetPropEntity.jsp"});
		al.add(new Object[]{1905,19,"根据宠物id查询","admin/pet/queryOnePet.jsp"});
		al.add(new Object[]{1906,19,"查询数据库宠物","admin/pet/queryAllPetInDb.jsp"});
		al.add(new Object[]{1907,19,"新版宠物后台","admin/pet/pet2.jsp?action=showBlank"});
		
		al.add(new Object[]{55,0,"公告",""});
		al.add(new Object[]{5501,55,"查看登陆公告","admin/notice/queryNotice.jsp"});
		al.add(new Object[]{5502,55,"查看临时公告","admin/notice/queryTempNotice.jsp"});
		al.add(new Object[]{5503,55,"设置登陆公告","admin/notice/setLoginNotice.jsp"});
		al.add(new Object[]{5504,55,"设置临时公告","admin/notice/setTempNotice.jsp"});
		al.add(new Object[]{5505,55,"查看活动公告","admin/notice/queryActivityNotice.jsp"});
		al.add(new Object[]{5506,55,"设置活动公告","admin/notice/setActivityNotice.jsp"});
		al.add(new Object[]{5507,55,"屏蔽渠道","admin/chargeCloseChannelList.jsp"});
		al.add(new Object[]{5508,55,"白名单","admin/chargeWhiteList.jsp"});

		al.add(new Object[]{7,0,"聊天管理",""});
		al.add(new Object[]{701,7,"发送系统消息","admin/system_chatmessage.jsp"});

		al.add(new Object[]{30,0,"国家管理",""});
		al.add(new Object[]{3001,30,"国家","admin/country/countryManager.jsp"});
		
		al.add(new Object[]{31,0,"城战",""});
		al.add(new Object[]{30001,31,"城战数据修改","admin/cityfight/cityfight.jsp"});

		al.add(new Object[]{8,0,"拍卖管理",""});
		al.add(new Object[]{801,8,"拍卖查询","admin/auctionList.jsp"});
		al.add(new Object[]{802,8,"求购管理","admin/requestBuyList.jsp"});
		al.add(new Object[]{803,8,"黑卡查询","admin/censusTradeMoney4Java.jsp"});
		al.add(new Object[]{804,8,"求购补货管理","admin/requestBuyBuSet.jsp"});

		al.add(new Object[]{9,0,"商店管理",""});
		al.add(new Object[]{901,9,"商店列表","admin/shop/shops.jsp"});

		
		al.add(new Object[]{15,0,"战场",""});
		al.add(new Object[]{1501,15,"战场信息","admin/battle/list_battlefields.jsp"});
		al.add(new Object[]{1502,15,"排队信息","admin/battle/list_battlefield_queue.jsp"});
		al.add(new Object[]{1503,15,"帮战信息","admin/battle/list_bangpai.jsp"});
		al.add(new Object[]{1510,15,"竞技信息","admin/battle/list_tournamentbattlefields.jsp"});
		al.add(new Object[]{1512,15,"村庄战信息","admin/battle/villageFight.jsp"});

		
		al.add(new Object[]{20,0,"家族管理",""});
		al.add(new Object[]{2001,20,"家族列表","admin/jiazu/allJiazuList.jsp"});
		al.add(new Object[]{2002,20,"家族资源修改（新）","admin/jiazu/jiazu2.jsp"});
						
		al.add(new Object[]{22,0,"个人庄园",""});
		al.add(new Object[]{2200,22,"庄园列表","admin/faery/faeryList.jsp"});
		
		
		al.add(new Object[]{40,0,"坐骑系统",""});
		al.add(new Object[]{401,40,"查询玩家坐骑","admin/horse/queryHorse.jsp"});
		al.add(new Object[]{402,40,"查询所有坐骑","admin/horse/queryAllHorse.jsp"});
		al.add(new Object[]{403,40,"坐骑(新)","admin/horse/horse2Opt.jsp"});

		al.add(new Object[]{41,0,"关系",""});
		al.add(new Object[]{411,41,"查询玩家关系","admin/society/queryRelation.jsp"});
		al.add(new Object[]{412,41,"查看个人聊天组","admin/society/queryChatGroups.jsp"});


		al.add(new Object[]{42,0,"情缘活动",""});
		al.add(new Object[]{421,42,"开始","admin/activity/fateActivity/beginActivity.jsp"});
		al.add(new Object[]{422,42,"删除","admin/activity/fateActivity/removeActivity.jsp"});
		al.add(new Object[]{425,42,"查看活动","admin/activity/fateActivity/queryActivity.jsp"});
		al.add(new Object[]{426,42,"查看活动详情","admin/activity/fateActivity/queryActivityInfo.jsp"});

		
		al.add(new Object[]{43,0,"寻宝活动",""});
		al.add(new Object[]{431,43,"开始","admin/activity/exploreActivity/beginExplore.jsp"});
		al.add(new Object[]{432,43,"查看寻宝活动详情","admin/activity/exploreActivity/showExploreEntity.jsp"});
		al.add(new Object[]{433,43,"放弃寻宝","admin/activity/exploreActivity/giveUpExplore.jsp"});
		al.add(new Object[]{434,43,"使用寻宝道具","admin/activity/exploreActivity/useProps.jsp"});
		al.add(new Object[]{435,43,"交换寻宝道具","admin/activity/exploreActivity/exchangeArticle.jsp"});
		al.add(new Object[]{436,43,"兑换寻宝道具","admin/activity/exploreActivity/exchangeExp.jsp"});
	
		al.add(new Object[]{44,0,"混沌万灵榜",""});
		al.add(new Object[]{441,44,"查看榜单","admin/activity/specialEquipment/showSepcialBillBoard.jsp"});
		al.add(new Object[]{443,44,"拾取","admin/activity/specialEquipment/pickUp.jsp"});
		
		al.add(new Object[]{45,0,"答题活动",""});
		al.add(new Object[]{452,45,"查询答题能不能自动开始","admin/activity/quizActivity/queryQuizAuto.jsp"});
		
		al.add(new Object[]{46,0,"数据校验",""});
		al.add(new Object[]{500,46,"检查刺探和偷砖","admin/check/peekAndBrick.jsp"});
		al.add(new Object[]{501,46,"检查押镖","admin/check/silvercar.jsp"});
		al.add(new Object[]{502,46,"调用时间检测","admin/check/checkTime.jsp"});
		al.add(new Object[]{503,46,"新手引导数据","admin/check/checkNewPlayerLead.jsp"});
		al.add(new Object[]{504,46,"家族驻地地图模板","admin/check/checkSeptstationMapTemplet.jsp"});
		al.add(new Object[]{505,46,"境界管理","admin/check/checkBourn.jsp"});
		al.add(new Object[]{506,46,"天晶虫活动","admin/check/feedSilkworm.jsp"});
		al.add(new Object[]{507,46,"充值列表","admin/check/checkChargeList.jsp"});


		al.add(new Object[]{47,0,"成就系统",""});
		al.add(new Object[]{600,47,"查看系统成就列表","admin/achievement/achievementList.jsp"});
		al.add(new Object[]{601,47,"查看角色达成成就列表","admin/achievement/playerAchievement.jsp"});
		al.add(new Object[]{602,47,"查看角色称号列表","admin/playerTitle/queryOneTitle.jsp"});
		
		al.add(new Object[]{100,0,"所有地图",""});
		al.add(new Object[]{600,100,"地图信息","admin/map/allmaps.jsp"});
		
		al.add(new Object[]{48,0,"四方神兽",""});
		al.add(new Object[]{4801,48,"四方神兽","admin/sifang.jsp"});
		
		al.add(new Object[]{49,0,"国战",""});
		al.add(new Object[]{4901,49,"国战管理","admin/guozhan/guozhan.jsp"});
		al.add(new Object[]{4902,49,"国战常量","admin/guozhan/constants.jsp"});
		
		al.add(new Object[]{51,0,"排行榜",""});
		al.add(new Object[]{5101,51,"查看排行榜","admin/billboard/queryBillboard.jsp"});
		al.add(new Object[]{5102,51,"更新排行榜宠物数据","admin/billboard/updatePet.jsp"});
		al.add(new Object[]{5103,51,"添加人物的排行榜数据","admin/billboard/adddate.jsp"});
		
		al.add(new Object[]{50,0,"配置信息",""});
		al.add(new Object[]{5001,50,"掉落物品","admin/confMsg/flopentity.jsp"});
		al.add(new Object[]{5002,50,"中立地图","admin/confMsg/gamemapincontry.jsp"});
		al.add(new Object[]{5003,50,"等级经验","admin/confMsg/levelExp.jsp"});
		al.add(new Object[]{5004,50,"王者之印","admin/confMsg/mapWangZheTransferPoints.jsp"});
		al.add(new Object[]{5005,50,"玩家title","admin/confMsg/playertitle.jsp"});
		al.add(new Object[]{5006,50,"祈福录","admin/confMsg/qifuluconf.jsp"});
		al.add(new Object[]{5007,50,"世界地图","admin/confMsg/worldmap.jsp"});
		
		al.add(new Object[]{53,0,"寻人启事",""});
		al.add(new Object[]{5301,53,"查看某人的寻人","admin/peopleSearch/playerSearch.jsp"});
		al.add(new Object[]{5302,53,"查看寻人地图","admin/peopleSearch/peopleSearcheScenes.jsp"});
		al.add(new Object[]{5303,53,"查看寻人数据","admin/check/checkPeopleSearch.jsp"});
		
		al.add(new Object[]{54,0,"千层塔",""});
		al.add(new Object[]{5401,54,"刷新千层塔","admin/QianCengTa_flush.jsp"});
		al.add(new Object[]{5402,54,"千层塔信息","admin/QianCengTa_msg.jsp"});
		al.add(new Object[]{5403,54,"千层塔玩家通过信息","admin/QianCengTa_Statistics1.jsp"});
		al.add(new Object[]{5404,54,"千层塔通过详细信息","admin/QianCengTa_Statistics2.jsp"});
		
		al.add(new Object[]{56,0,"世界BOSS",""});
        al.add(new Object[]{5601,56,"BOSS信息","admin/activity/worldBoss/index.jsp"});
    	al.add(new Object[]{57,0,"小游戏",""});
        al.add(new Object[]{5701,57,"重新加载小游戏静态文件","admin/minigame/reload.jsp"});
        al.add(new Object[]{5702,57,"重置小游戏","admin/minigame/resetGame.jsp"});
        
    	al.add(new Object[]{75,0,"法宝",""});
        al.add(new Object[]{7501,75,"法宝","admin/magicweapon/magicweapon.jsp"});
        
    	al.add(new Object[]{63,0,"恶魔广场",""});
        al.add(new Object[]{6301,63,"恶魔广场","admin/carbon/devilSquare.jsp"});
        al.add(new Object[]{6302,63,"合成","admin/carbon/compose.jsp"});
        al.add(new Object[]{6303,63,"翅膀副本","admin/carbon/wingCarbon.jsp"});
        al.add(new Object[]{6304,63,"宝箱大乱斗","admin/carbon/chestFight.jsp"});
		
        al.add(new Object[]{99,0,"目标系统",""});
        al.add(new Object[]{9901,99,"查看个人已达成目标","admin/playerAim/playerAimCheck.jsp"});
        al.add(new Object[]{9902,99,"个人目标操作","admin/playerAim/playerAim.jsp"});
        al.add(new Object[]{9902,99,"查看目标列表","admin/playerAim/playeraimList.jsp"});
        
    	al.add(new Object[]{58,0,"签到活跃度",""});
        al.add(new Object[]{5801,58,"查看玩家签到活跃度信息","admin/activeness/activenessShow.jsp"});
        al.add(new Object[]{5802,58,"后台签到-清除签到","admin/activeness/sign.jsp"});
        al.add(new Object[]{5803,58,"增加活跃度","admin/activeness/activessAdd.jsp"});
        al.add(new Object[]{5804,58,"杂项处理","admin/activeness/activenessFix.jsp"});
        
    	al.add(new Object[]{59,0,"仙尊",""});
        al.add(new Object[]{5901,59,"查看仙尊信息","admin/fairyBuddha/fairyBuddhaInfoShow.jsp"});
    	
		al.add(new Object[]{60,0,"充值",""});
		al.add(new Object[]{6001,60,"模拟充值","admin/gmadmin/repairChargeOrder.jsp"});
		
		
		
		al.add(new Object[]{61,0,"运营需求",""});
		
		al.add(new Object[]{6101,61,"发送活动奖励","admin/operation/sendPrizeMail.jsp"});
		al.add(new Object[]{6102,61,"服务器信息","admin/operation/serverMonitor.jsp"});
		al.add(new Object[]{6103,61,"韩服公告板","admin/operation/boardAndEvent.jsp"});
		al.add(new Object[]{6104,61,"查看玩家技能","admin/pet/playerSkill.jsp"});
		al.add(new Object[]{6105,61,"修改玩家技能等级","admin/pet/changeplayerSkill.jsp"});
		al.add(new Object[]{6106,61,"玩家数据统计","admin/yunying/gameSnapshot.jsp"});
		al.add(new Object[]{6107,61,"玩家信息批量查询","admin/yunying/resultbycond.jsp"});
		
		al.add(new Object[]{62,0,"技术相关",""});
		al.add(new Object[]{6201,62,"控制后台","admin/mconsole/mconsoleList.jsp"});
		
		al.add(new Object[]{63,0,"GM频道",""});
		al.add(new Object[]{6301,63,"VIP经验and积分","admin/gmManager/addVipExp.jsp"});
		al.add(new Object[]{6302,63,"修复无主线任务","admin/gmManager/repairPlayerTask.jsp"});
		al.add(new Object[]{6303,63,"修改仓库密码","admin/gmManager/resetCangku.jsp"});
		
		al.add(new Object[]{64,0,"测试频道",""});
		al.add(new Object[]{6401,64,"VIP经验and积分","admin/ceshiManager/addVipExp.jsp"});
		al.add(new Object[]{6402,64,"批量发送邮件","admin/ceshiManager/ceshisendMailForGroup.jsp"});
		al.add(new Object[]{6403,64,"等级-经验-银子-绑银","admin/ceshiManager/ceshiUnitedServerDataPrepare.jsp"});
		al.add(new Object[]{6404,64,"仙婴相关","admin/ceshiManager/flyTalentTest.jsp"});
		al.add(new Object[]{6405,64,"合服相关","admin/ceshiManager/forUnitServer2.jsp"});
		al.add(new Object[]{6406,64,"城战矿战","admin/ceshiManager/cityAndVilliageFightTime.jsp"});
		al.add(new Object[]{6407,64,"仙灵大战","admin/ceshiManager/xianling.jsp"});
		
		al.add(new Object[]{65,0,"策划频道",""});
		al.add(new Object[]{6501,65,"物品翻译","admin/cehua/translate_articleName.jsp"});
		al.add(new Object[]{6502,65,"跨服信息","admin/queryCrossInfo.jsp"});
		
		al.add(new Object[]{66,0,"激活码",""});
		al.add(new Object[]{6601,66,"发放激活码","admin/refreshTencentJiHuoMa.jsp"});
		
		if(TestServerConfigManager.isTestServer() || GameConstants.getInstance().getServerName().equals("国内本地测试")){
			al.add(new Object[]{67,0,"测试服表相关",""});
			al.add(new Object[]{6701,67,"上传下载表","admin/uploadTools/gameResourceUpdateTool_frame.jsp"});
		}
		
		String baseURL = request.getRequestURL().toString();
		int k = baseURL.lastIndexOf("/");
		if(k > 0){
			baseURL = baseURL.substring(0,k);
		}
		for(int i = 0 ; i < al.size() ; i++){
			
			Object os[] = al.get(i);
			
			if(os[3].equals("")){
				out.println("d.add("+os[0]+","+os[1]+",'"+os[2]+"','"+os[3]+"');");
			}else{
				String url = baseURL + "/" +os[3];
				com.xuanzhi.tools.authorize.User user = (com.xuanzhi.tools.authorize.User)session.getAttribute(AuthorizedServletFilter.USER);
				if(user != null){
					boolean canAccess = com.xuanzhi.tools.authorize.AuthorizeManager.getInstance().isPlatformAccessEnable(user,url,request.getRemoteAddr());
					if(canAccess){
						out.println("d.add("+os[0]+","+os[1]+",'"+os[2]+"','"+os[3]+"');");
					}
				}
			}
			
			
		}	
		%>
	
		document.write(d);
	//-->
	</SCRIPT>

<DIV></DIV>
</BODY>
</HTML>
