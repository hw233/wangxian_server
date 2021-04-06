package com.fy.engineserver.activity.refreshbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;

/**
 * 天降密藏
 * 
 * 
 */
public class GodDownReward implements MConsole {

	private static List<MapInfo> mapInfos = new ArrayList<MapInfo>();
	@ChangeAble("开始小时")
	public int 开始小时 = 20;
	
	private Random random = new Random();

	static {
		MapInfo info = new MapInfo();
		info.setMapname("kunhuagucheng");
		info.setName(Translate.新年贺礼);
		// if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
		// 开始小时 = 23;
		// info.setNum(100);
		// }else{
		info.setNum(200);
		// }

		MapInfo info2 = new MapInfo();
		info2.setMapname("kunlunshengdian");
		info2.setName(Translate.新年贺礼);
		// if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
		// info2.setNum(100);
		// }else{
		info2.setNum(200);
		// }

		MapInfo info3 = new MapInfo();
		info3.setMapname("miemoshenyu");
		info3.setName(Translate.新年贺礼);
		// if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
		// info3.setNum(200);
		// }else{
		info3.setNum(300);
		mapInfos.add(info);
		mapInfos.add(info2);
		mapInfos.add(info3);
	}
	private String 新手城市 [] = {"kunhuagucheng","shangzhouxianjing","wanfayiji"};
	private String 国家城市 [] = {"kunlunshengdian","jiuzhoutiancheng","wanfazhiyuan"};
	private String 中立成市 [] = {"miemoshenyu"};
	private int [][][] 新手城市XY = {{{7183,748},{6469,1510},{6034,1727},{6424,2392},{6297,2755},{6342,3157},{5179,3175},{4706,3161},{4499,2844},{4147,2319},{4071,1772},{3436,1507},{2936,1284},{2486,1505},{804,748},{2419,2287},{2748,2524},{2332,2776},{1584,2999},{1189,3368},{971,3497},{2189,2360},{3832,2494},{4377,2541}},
								{{2692,636},{1970,791},{1391,761},{1674,1158},{723,1636},{715,2817},{1776,3493},{1331,4125},{7108,359},{7453,544},{6379,1116},{7400,1651},{7413,2790},{6912,3084},{7235,4062},{6561,3147},{5687,3590},{4993,3353},{4612,3036},{4403,2616},{4423,1998},{3698,2362},{3098,1800},{2386,2507}},
								{{1180,1265},{1935,1843},{2986,2188},{4221,1585},{4704,3077},{1131,3245},{841,4421},{1928,4718},{1296,5968},{1660,6546},{1389,7105},{3347,6669},{4639,6851},{4412,6004},{7314,6921},{8068,6433},{8794,6117},{9588,2117},{8916,1506},{8648,2860},{9572,3551},{9161,3994},{8542,4507},{4773,4534},{5355,3972},{6241,3562},{5095,3682},{4588,3318},{3173,3922},{8479,3050},{9768,3659},{7959,4473},{5100,4402},{5083,3699}}};
	private int [][][] 国家城市XY = {{{10264,6485},{9843,6331},{9356,6131},{8206,5431},{7336,5496},{7838,5186},{6380,4103},{6000,3784},{6542,3252},{5467,3507},{4485,3962},{3682,3957},{3320,3498},{3917,3143},{4766,3173},{5895,2699},{3516,1692},{3192,1464},{2670,2071},{2180,1761},{1798,2323},{1360,2488},{879,3261},{1270,4497},{2334,5806},{1274,5969},{1639,6509},{4355,5475},{4887,5898},{5287,7053},{6193,7013},{7198,7088},{10115,3707},{10168,4324}}
									,{{9942,6059},{9454,5813},{8806,5541},{8285,5184},{8039,4855},{7100,5088},{6804,4720},{6405,3569},{5600,3909},{5522,3449},{5865,2797},{4911,3199},{3906,3852},{3096,3595},{3358,2761},{4214,2327},{3651,1103},{2893,1466},{2172,1851},{2051,1197},{1471,935},{6030,1123},{10081,4073},{10255,4577},{1359,4646},{3710,5304},{4238,5692},{5906,6007},{5823,6826},{6908,6808},{7907,6983},{2406,6903},{1231,6811},{2188,6071}}
									,{{8377,5189},{6547,4660},{5942,5197},{5044,5612},{4335,4932},{3643,5159},{1918,6374},{1336,6648},{3716,7258},{5984,4439},{7661,4713},{8636,5188},{10180,4471},{9182,3500},{9357,2670},{8627,3187},{7116,4151},{5820,2675},{5858,1878},{6684,1345},{4061,2949},{3934,3920},{2958,3564},{2115,4086},{1164,3242},{1489,2022},{2974,2081},{2720,1408},{4069,776},{5049,591},{8573,828},{9271,459},{9993,1488},{1751,847}}};
	private int [][][] 中立成市XY = {{{1979,4887},{2268,4713},{2541,4618},{2846,4458},{3148,4323},{4158,3519},{4515,3278},{5113,3030},{5385,3565},{4371,2988},{4494,2618},{5102,2613},{5867,3020},{6095,3442},{6919,4361},{6412,4728},{7041,4725},{7491,4863},{8243,4466},{8286,3165},{8116,1171},{7686,986},{8249,689},{6956,1851},{6244,1841},{7098,2098},{5121,1001},{4369,1061},{2442,1362},{694,3204},{1356,3376},{979,3745},{1164,459},{4621,1026}}};

	String names [] = null;
	int xys[] [][] = new int[0][][];
	//新手城市1,国家主城2，中立3
	public void doPrizes(int type,String liZiName,String rewardname,boolean isdownflower,int ranCountry) {
		String mapname = "";
		int [][] xy = new int[0][];
		try {
			if(type == 1){
				names = 新手城市;
				xys = 新手城市XY;
			}else if(type == 2){
				names = 国家城市;
				xys = 国家城市XY;
			}else {
				names = 中立成市;
				xys = 中立成市XY;
			}
			
			if(names == null || names.length != xys.length){
				RefreshBoxManager.log.warn("[天降密藏] [失败] [降落信息出错] [type:"+type+"] [names:"+names+"] [xys:"+xys+"]");
				return;
			}
			
			Article article = ArticleManager.getInstance().getArticle(rewardname);
			if (article == null) {
				RefreshBoxManager.log.warn("[天降密藏] [失败] [物品不存在] [type:"+type+"] [rewardname:"+rewardname+"]");
				return;
			}
//			for(int kk=0;kk<names.length;kk++){
				mapname = names[ranCountry-1];
				xy = xys[ranCountry-1];
				Game game = GameManager.getInstance().getGameByName(mapname, TransportData.getCountry(mapname));
				if(game == null){
					RefreshBoxManager.log.warn("[天降密藏] [失败] [地图信息不存在] [type:"+type+"] [rewardname:"+rewardname+"] [game:"+mapname+"] [country:"+TransportData.getCountry(mapname)+"]");
					return;
				}
				int count = 0;
				int x = 0;
				int y = 0;
				for (int j = 0; j < 100; j++) {
					FlopCaijiNpc npc = getFlopCaiJiNpc(article, game);
					if(count >= xy.length){
						count = 0;
					}
					x = xy[count][0];
					y = xy[count][1];
					npc.setX(x + random.nextInt(300));
					npc.setY(y + random.nextInt(300));
					game.addSprite(npc);
					count++;
				}
				if(isdownflower){
					piaoLiZi(mapname, liZiName);
				}
				RefreshBoxManager.log.warn("[天降密藏] [成功] ["+mapname+"] [xy:"+x+"/"+y+"] [country:" + TransportData.getCountry(mapname) + "] [ranCountry:"+ranCountry+"] [isdownflower:"+isdownflower+"] [数量：" + count + "]");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			RefreshBoxManager.log.warn("[天降密藏] [mapname:" + mapname + "] [" + e + "]");
		}
	}
	
	public void piaoLiZi(String gamename,String liZiName){
		try{
			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			if(ps != null){
				for(Player p : ps){
					if(p.getMapName().equals(gamename)){
						ParticleData[] particleDatas = new ParticleData[1];
						particleDatas[0] = new ParticleData(p.getId(), liZiName, 2000, 2, 1, 1);
						NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
						p.addMessageToRightBag(resp);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[天降密藏] [飘箱子] [异常:{}]",e);
		}
	}

	private FlopCaijiNpc getFlopCaiJiNpc(Article article, Game g) {
		try {
			ArticleEntity ae = null;
			try {
				ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, null, article.getColorType(), 1, true);
			} catch (Exception e) {
				return null;
			}
			NPCManager nm = MemoryNPCManager.getNPCManager();
			FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
			fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			fcn.setAllCanPickAfterTime(1000);
			fcn.setEndTime(SystemTime.currentTimeMillis() + 2 * 60 * 1000 + 30 * 1000);
			ResourceManager.getInstance().getAvata(fcn);
			Player[] ps = GamePlayerManager.getInstance().getOnlinePlayers();
			List<Long> list = new ArrayList<Long>();
			for (LivingObject lo : ps) {
				if (lo != null) {
					list.add(lo.getId());
				}
			}
			fcn.setPlayersList(list);
			fcn.setAe(ae);
			fcn.setCount(1);
			fcn.setName(article.getName());
			return fcn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private int ranCountry = 1;
	private long lastRanDate;
	
	public void startActivity(String liziname,String rewardname,boolean isdownflower) {
		Calendar cl = Calendar.getInstance();
		int currMiunte = cl.get(Calendar.MINUTE);
		int currhour = cl.get(Calendar.HOUR_OF_DAY);
		
		if(!ActivityManagers.isSameDay(SystemTime.currentTimeMillis(), lastRanDate)){
			ranCountry = random.nextInt(4);
			lastRanDate = SystemTime.currentTimeMillis();
			if(ranCountry == 0){
				ranCountry = 1;
			}
		}
		
		if (currhour == 开始小时) {
			if (currMiunte == 0) {
				sendSystemNotice(rewardname,15, "新手城市", 0,ranCountry);
			} else if (currMiunte == 10) {
				sendSystemNotice(rewardname,5, "新手城市", 0,ranCountry);
			} else if (currMiunte == 15 || currMiunte == 18 || currMiunte == 21 || currMiunte == 24) {// 刷新
				sendSystemNotice(rewardname,0, "新手城市", 1,ranCountry);
				doPrizes(1,liziname,rewardname,isdownflower,ranCountry);
			} else if (currMiunte == 30) {
				sendSystemNotice(rewardname,15, "国家主城", 0,ranCountry);
			} else if (currMiunte == 40) {
				sendSystemNotice(rewardname,5, "国家主城", 0,ranCountry);
			} else if (currMiunte == 45 || currMiunte == 48 || currMiunte == 51 || currMiunte == 54) {// 刷新
				sendSystemNotice(rewardname,0, "国家主城", 1,ranCountry);
				doPrizes(2,liziname,rewardname,isdownflower,ranCountry);
			} 
		}

		if (currhour == 开始小时 + 1) {
			if (currMiunte == 0) {// 15分钟后开
				sendSystemNotice(rewardname,15, "灭魔神域", 0,0);
			} else if (currMiunte == 10) {// 5分钟后开
				sendSystemNotice(rewardname,5, "灭魔神域", 0,0);
			} else if (currMiunte == 15 || currMiunte == 18 || currMiunte == 21 || currMiunte == 24) {// 刷新
				sendSystemNotice(rewardname,0, "灭魔神域", 1,0);
				doPrizes(3,liziname,rewardname,isdownflower,1);
			} 
		}
	}

	private void sendSystemNotice(String rewardname,int minute, String countryname, int type,int country) {
		RefreshBoxManager.log.warn("[天降密藏] [in sendSystemNotice] [rewardname:"+rewardname+"] [minute：" + minute + "] [countryname：" + countryname + "] [country:"+country+"] [type:" + type + "]");
		ChatMessage msg = new ChatMessage();
		String mes = "";
		if (type == 0) {
			mes = Translate.translateString(Translate.诸天秘宝描述, new String[][] { { Translate.STRING_1, rewardname }, { Translate.STRING_2, minute + "" }, { Translate.STRING_3, TransportData.getCountryName(country) } , { Translate.STRING_4, countryname }});
		} else if (type == 1) {
			mes = Translate.translateString(Translate.诸天秘宝降临, new String[][] { { Translate.STRING_1, rewardname },{ Translate.STRING_2, TransportData.getCountryName(country) } , { Translate.STRING_3, countryname }});
		}
		msg.setMessageText(mes);
		try {
			//ChatMessageService.getInstance().sendHintMessageToSystem(msg);
//			for (int i = 0; i < 2; i++) {
				ChatMessageService.getInstance().sendRoolMessageToSystem(msg);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getMConsoleName() {
		return "刷宝箱的开始公告时间";
	}

	@Override
	public String getMConsoleDescription() {
		return "3大主城刷宝箱";
	}

}
