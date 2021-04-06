package com.fy.engineserver.menu.activity;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.xuanzhi.boss.game.GameConstants;

public class Option_MergeServer_Help extends Option implements NeedCheckPurview{

	public Option_MergeServer_Help() {
	}
	
	@ChangeAble("startTime")
	private String startTime = "2013-08-12 00:00:00";
	@ChangeAble("endTime")
	private String endTime = "2013-11-21 23:59:59";
	@ChangeAble("openplats")
	private Platform[] openplats = { Platform.官方 };
	@ChangeAble("limitservers")
	private Set<String> limitservers = new HashSet<String>();
	@ChangeAble("openServers")
	private String[] openServers = {"国内本地测试","金霞玉鼎","海天佛国","百花深谷","东极仙岛","鹊桥仙境","瑶台仙宫","紫阳青峰","普陀潮音","雪域冰城","白露横江","左岸花海","裂风峡谷","蓬莱仙阁","乾元金光","北冥佛光","七宝莲台"};
	
	public static String servernames [] = {"国内本地测试","碧海青天","侠骨柔肠","凌霜傲雪","仙岛秘境","崆峒灵宝","万佛朝宗","无量净土","金宫银坊","问鼎江湖","笑傲江湖","鱼跃龙门","龙翔凤舞","冲霄云楼","王者之域","独战群神","傲剑凌云"};
	public static int 同类集合[] = {0,0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3};
	public static String mess[] = {
		"《飘渺寻仙曲》预计于11月14日维护后至22日维护前,我们将临时关闭角色创建、邮件、武圣战、五方圣兽、结婚、求购、拍卖、城战、矿战、仙府、镖局、国战，及时提取邮件并收回拍卖行物品。\n"
				+"11月22日维护后将进行停区合服，26日10：00将在四区一位开启新区【九转炼仙】\n"
				+"1.服务器要点：<f color='#FFFF00' >金霞玉鼎、海天佛国、百花深谷、东极仙岛</f>原服务器名称将变更为四区一位九转炼仙服务器。\n"
				+"2.改名：同角色名称,合区后叫xxx@金霞玉鼎、xxx@海天佛国、xxx@百花深谷、xxx@东极仙岛（包括角色、家族、宗派）在第一次登录服务器的同时会重新起名,但该角色原数据仍保留。\n"
				+"3.选择角色：若同一账号下在原服务器均有角色,合区后登录界面可选择角色登录。\n"
				+"4.封印等级：合区后,解封时间将按照后解封的服务器时间为准。\n"
				+"5.充值处理：合区后,同一账号下同时拥有合区服务器的角色,玩家登录哪个服务器进行充值则充值数据进入该服务器角色中。\n"
				+"6.游戏注意事项：\n"
				+"①城市争夺战全部重置为无人占领状态\n"
				+"②灵矿争夺战全部重置为无人占领状态\n"
				+"③国战、全部国家管理员重置\n"
				+"④邮件,拍卖,求购将全部清空,请合区前及时提取附件\n"
				+"⑤武圣争夺战积分全部重置清零\n"
				+"⑥同角色名玩家改名后,仙府解封的同时,新仙府数据中原角色名变更为新角色名,但玩家数据不变\n"
				+"⑦各排行榜全部重新计算,以登录为准\n"
				+"合服前，11月14~21日，每日登陆领取不同礼包；酒贴使用次数翻倍；建立宗派、结义打折\n"
				+"合服中，论坛发帖送极品激活码和实物奖励\n"
				+"合服后，合服礼包领取，昆仑圣殿之战送价值400元的高级礼包\n"
				+"精彩内容，请时时关注游戏内与论坛公告",
				"《飘渺寻仙曲》预计于11月14日维护后至22日维护前,我们将临时关闭角色创建、邮件、武圣战、五方圣兽、结婚、求购、拍卖、城战、矿战、仙府、镖局、国战，及时提取邮件并收回拍卖行物品。\n"
						+"11月22日维护后将进行停区合服，26日10：00将在四区一位开启新区【混斩天地】\n"
						+"1.服务器要点：<f color='#FFFF00' >鹊桥仙境、瑶台仙宫、紫阳青峰、普陀潮音</f>原服务器名称将变更为四区一位混斩天地服务器。\n"
						+"2.改名：同角色名称,合区后叫xxx@鹊桥仙境、xxx@瑶台仙宫、xxx@紫阳青峰、xxx@普陀潮音（包括角色、家族、宗派）在第一次登录服务器的同时会重新起名,但该角色原数据仍保留。\n"
						+"3.选择角色：若同一账号下在原服务器均有角色,合区后登录界面可选择角色登录。\n"
						+"4.封印等级：合区后,解封时间将按照后解封的服务器时间为准。\n"
						+"5.充值处理：合区后,同一账号下同时拥有合区服务器的角色,玩家登录哪个服务器进行充值则充值数据进入该服务器角色中。\n"
						+"6.游戏注意事项：\n"
						+"①城市争夺战全部重置为无人占领状态\n"
						+"②灵矿争夺战全部重置为无人占领状态\n"
						+"③国战、全部国家管理员重置\n"
						+"④邮件,拍卖,求购将全部清空,请合区前及时提取附件\n"
						+"⑤武圣争夺战积分全部重置清零\n"
						+"⑥同角色名玩家改名后,仙府解封的同时,新仙府数据中原角色名变更为新角色名,但玩家数据不变\n"
						+"⑦各排行榜全部重新计算,以登录为准\n"
						+"合服前，11月14~21日，每日登陆领取不同礼包；酒贴使用次数翻倍；建立宗派、结义打折\n"
						+"合服中，论坛发帖送极品激活码和实物奖励\n"
						+"合服后，合服礼包领取，昆仑圣殿之战送价值400元的高级礼包\n"
						+"精彩内容，请时时关注游戏内与论坛公告",
						"《飘渺寻仙曲》预计于11月14日维护后至22日维护前,我们将临时关闭角色创建、邮件、武圣战、五方圣兽、结婚、求购、拍卖、城战、矿战、仙府、镖局、国战，及时提取邮件并收回拍卖行物品。\n"
								+"11月22日维护后将进行停区合服，26日10：00将在二区一位开启新区【武尊天下】\n"
								+"1.服务器要点：<f color='#FFFF00' >雪域冰城、白露横江、左岸花海、裂风峡谷</f>原服务器名称将变更为二区一位武尊天下服务器。\n"
								+"2.改名：同角色名称,合区后叫xxx@雪域冰城、xxx@白露横江、xxx@左岸花海、xxx@裂风峡谷（包括角色、家族、宗派）在第一次登录服务器的同时会重新起名,但该角色原数据仍保留。\n"
								+"3.选择角色：若同一账号下在原服务器均有角色,合区后登录界面可选择角色登录。\n"
								+"4.封印等级：合区后,解封时间将按照后解封的服务器时间为准。\n"
								+"5.充值处理：合区后,同一账号下同时拥有合区服务器的角色,玩家登录哪个服务器进行充值则充值数据进入该服务器角色中。\n"
								+"6.游戏注意事项：\n"
								+"①城市争夺战全部重置为无人占领状态\n"
								+"②灵矿争夺战全部重置为无人占领状态\n"
								+"③国战、全部国家管理员重置\n"
								+"④邮件,拍卖,求购将全部清空,请合区前及时提取附件\n"
								+"⑤武圣争夺战积分全部重置清零\n"
								+"⑥同角色名玩家改名后,仙府解封的同时,新仙府数据中原角色名变更为新角色名,但玩家数据不变\n"
								+"⑦各排行榜全部重新计算,以登录为准\n"
								+"合服前，11月14~21日，每日登陆领取不同礼包；酒贴使用次数翻倍；建立宗派、结义打折\n"
								+"合服中，论坛发帖送极品激活码和实物奖励\n"
								+"合服后，合服礼包领取，昆仑圣殿之战送价值400元的高级礼包\n"
								+"精彩内容，请时时关注游戏内与论坛公告",
								"《飘渺寻仙曲》预计于11月14日维护后至22日维护前,我们将临时关闭角色创建、邮件、武圣战、五方圣兽、结婚、求购、拍卖、城战、矿战、仙府、镖局、国战，及时提取邮件并收回拍卖行物品。\n"
										+"11月22日维护后将进行停区合服，26日10：00将在四区一位开启新区【天开传说】\n"
										+"1.服务器要点：<f color='#FFFF00' >蓬莱仙阁、乾元金光、北冥佛光、七宝莲台</f>原服务器名称将变更为四区一位天开传说服务器。\n"
										+"2.改名：同角色名称,合区后叫xxx@蓬莱仙阁、xxx@乾元金光、xxx@北冥佛光、xxx@七宝莲台（包括角色、家族、宗派）在第一次登录服务器的同时会重新起名,但该角色原数据仍保留。\n"
										+"3.选择角色：若同一账号下在原服务器均有角色,合区后登录界面可选择角色登录。\n"
										+"4.封印等级：合区后,解封时间将按照后解封的服务器时间为准。\n"
										+"5.充值处理：合区后,同一账号下同时拥有合区服务器的角色,玩家登录哪个服务器进行充值则充值数据进入该服务器角色中。\n"
										+"6.游戏注意事项：\n"
										+"①城市争夺战全部重置为无人占领状态\n"
										+"②灵矿争夺战全部重置为无人占领状态\n"
										+"③国战、全部国家管理员重置\n"
										+"④邮件,拍卖,求购将全部清空,请合区前及时提取附件\n"
										+"⑤武圣争夺战积分全部重置清零\n"
										+"⑥同角色名玩家改名后,仙府解封的同时,新仙府数据中原角色名变更为新角色名,但玩家数据不变\n"
										+"⑦各排行榜全部重新计算,以登录为准\n"
										+"合服前，11月14~21日，每日登陆领取不同礼包；酒贴使用次数翻倍；建立宗派、结义打折\n"
										+"合服中，论坛发帖送极品激活码和实物奖励\n"
										+"合服后，合服礼包领取，昆仑圣殿之战送价值400元的高级礼包\n"
										+"精彩内容，请时时关注游戏内与论坛公告"
		
	};
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
			String servername = GameConstants.getInstance().getServerName();
			int index = 0;
			for(int i=0;i<servernames.length;i++){
				if(servernames[i].equals(servername)){
					index = i;
					break;
				}
			}
			int in = 同类集合[index];
			String des = mess[in];
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(des);
			Option_Cancel oc = new Option_Cancel();
			oc.setText(Translate.确定);
			mw.setOptions(new Option[]{oc});
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		if(!PlatformManager.getInstance().isPlatformOf(openplats)){
			return false;
		}
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		
		if(limitservers.contains(gc.getServerName())){
			return false;
		}
		
		if(TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)){
			return false;
		}
		
		for(String s:openServers){
			if(s.equals(gc.getServerName())){
				return true;
			}
		}
		
		if(openServers.length==0){
			return true;
		}
		
		return false;
	}
}
