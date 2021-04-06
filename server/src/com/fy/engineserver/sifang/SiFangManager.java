package com.fy.engineserver.sifang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.data.props.FlopSchemeEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.sifang.Option_SiFang_Jingru;
import com.fy.engineserver.menu.sifang.Option_SiFang_Join_OK;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SIFANG_ENLIST_PLAYER_RES;
import com.fy.engineserver.message.SIFANG_OVER_MSG_RES;
import com.fy.engineserver.message.SIFANG_SHOW_INFO_BUTTON_RES;
import com.fy.engineserver.message.SIFANG_SHOW_START_TIME_RES;
import com.fy.engineserver.sifang.info.SiFangInfo;
import com.fy.engineserver.sifang.info.SiFangSimplePlayerInfo;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class SiFangManager implements Runnable, AbstractActivity{
	
	//限制飞行坐骑,传送模式 随机家族点
	//胜利条件：场地内只剩一个家族，如果有多个：击杀数，剩余人数，随机
	//结束后给在当前场景内的人弹信息
	
	public static Logger logger = LoggerFactory.getLogger(SiFangManager.class);
	
	private static SiFangManager instance;
	
	public static SimpleEntityManager<SiFangInfo> em;
	
	public Hashtable<Long, Integer> playerGoToMapType = new Hashtable<Long, Integer>();
	
	public String[] bossMmapName = new String[]{"qinglongshengtan","zhuqueshengtan","baihushengtan","xuanwushengtan","qilinshengtan"};
	private String[] bossName = new String[]{Translate.text_sifang_BossNames[0], Translate.text_sifang_BossNames[1], Translate.text_sifang_BossNames[2], Translate.text_sifang_BossNames[3], Translate.text_sifang_BossNames[4]};
	public Hashtable<Long, Integer> playerGoToBossMap = new Hashtable<Long, Integer>();
	
	public String[] pkMapName = new String[]{"qinglongmijing","zhuquemijing","baihumijing","xuanwumijing","qilinmijing"};
	public int[][] pkMapXY = new int[][]{{643,2125},{1103,3544},{2586,4257},{4392,4453},{6158,4031},{7395,2755},{6856,1277},{5337,524},{3434,384},{1738,947},};
	public int[][] pkMapStartXY = new int[][]{{1693,2238},{2951,3268},{4998,3277},{6309,2237},{5368,2335},{3695,2851},{2576,2400},{2977,1749},{4344,2259},{4622,1576},};
	public String[] infoName = new String[]{Translate.text_fifang_infoNames[0], Translate.text_fifang_infoNames[1], Translate.text_fifang_infoNames[2], Translate.text_fifang_infoNames[3], Translate.text_fifang_infoNames[4]};
	
	//结束的家族贡献奖励
	public int[] reward_JiaZu_GongXian = new int[]{100, 80, 60, 30, 25, 20, 15, 10, 5, 0};
	
	public int baoming_week = Calendar.WEDNESDAY;		//报名  周3
	public int baoming_house = 12;						//报名  12时
	public int baoming_min = 0;							//报名  0分
	
	public int notify_week = Calendar.SATURDAY;			//公告  周6
	public int notify_house = 14;						//公告  晚上18点
	public int notify_min = 45;							// 45分
	
//	public long[] 公告时间 = {0, 30*1000, 60*1000};
	public long[] 公告时间 = {0, 5*60*1000, 10*60*1000, 15*60*1000};
	
//	public long[] 进场时间 = {0, 30*1000, 60*1000};
	public long[] 进场时间 = {0, 1*60*1000, 3*60*1000, 5*60*1000};
	public int showStartTime = 60*1000;		//显示倒计时的时间
	
//	public long 战斗时间 = 5*60*1000;
	public long 战斗时间 = 30*60*1000;
	
	public int JOIN_JIAZU_MAX = 10;					//家族数目，每个领地一共5个领地
	
	public int JIAZU_PLAYER_MAX = 10;				//家族成员
	
	public int JOIN_MONEY = 100000;					//需要的钱
	
	public int JOIN_LEVEL_MAX = 40;
	
	public SiFangInfo[] infos;
	
	public void init(){
		
		if (logger.isWarnEnabled())
			logger.warn("四方神兽系统启动开始");
		setInstance(this);
		em = SimpleEntityManagerFactory.getSimpleEntityManager(SiFangInfo.class);
		
		List<SiFangInfo> list = null;
		try {
			list = em.query(SiFangInfo.class, null, new Object[]{},null, 1, 100);
		} catch (Exception e) {
			logger.error("载入四方神兽数据库出错:", e);
			return;
		}
		
		if (list == null || list.size() == 0) {
			list = new ArrayList<SiFangInfo>();
			for (int i = 0; i < 5; i++){
				SiFangInfo info = new SiFangInfo();
				try {
					info.setId(em.nextId());
				} catch (Exception e) {
					logger.error("生成四方神兽ID出错", e);
					return;
				}
				info.setInfoType(i);
				list.add(info);
				em.notifyNewObject(info);
			}
		}
		if (list.size() != 5) {
			logger.error("出错了，四方神兽有超过5条数据库纪录");
			return;
		}
		infos = new SiFangInfo[5];
		for (SiFangInfo info : list) {
			infos[info.getInfoType()] = info;
			infos[info.getInfoType()].setGonggaoNum(0);
			infos[info.getInfoType()].setGonggaoTime(0);
			infos[info.getInfoType()].setJingchangNum(0);
			infos[info.getInfoType()].setJingchangTime(0);
		}
		
		for (SiFangInfo info : infos) {
			if (isCalender(baoming_week, baoming_min, baoming_house) && !isCalender(notify_week, notify_house, notify_min)) {
				//如果是过了报名时间，就可以报名，即便是到了周6
				info.setState(SiFangInfo.SIFANG_STATE_START_ENTER);
			} else {
				//如果是没过报名时间，就不能报名
				info.setState(SiFangInfo.SIFANG_STATE_OVER);
			}
		}
		
		Thread t = new Thread(this, "SiFang");
		t.start();
		if (logger.isWarnEnabled())
			logger.warn("四方神兽系统启动结束");
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy(){
		em.destroy();
		if (logger.isWarnEnabled())
			logger.warn("四方神兽关闭");
	}

	@Override
	public void run() {
		
		while (true) {
			try{
				Thread.sleep(1000);
				
				try{
					long nowTime = SystemTime.currentTimeMillis();
					for (int i = 0; i < infos.length; i++) {
						infos[i].heartbeat();
					}
//					if (logger.isInfoEnabled())
//						logger.info("一次A心跳处理:" + (SystemTime.currentTimeMillis() - nowTime) + "毫秒");
				}catch(Exception e){
					logger.error("四方神兽game心跳出错:", e);
				}
				
				try{
					long nowTime = SystemTime.currentTimeMillis();
					for (int i = 0; i < infos.length; i++) {
						if (infos[i].getState() == SiFangInfo.SIFANG_STATE_OVER) {
							//如果到报名时候就开启报名
							if (isCalender(baoming_week, baoming_house, baoming_min) && !isCalender(notify_week, notify_house, notify_min)) {
								infos[i].setState(SiFangInfo.SIFANG_STATE_START_ENTER);
								if (logger.isWarnEnabled())
									logger.warn("[开始报名] [{}] [id={}] [JzID={}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID()});
							}
						}else if (infos[i].getState() == SiFangInfo.SIFANG_STATE_START_ENTER) {
							//报名 ,如果时间到了就开公告
							if (isCalender(notify_week, notify_house, notify_min)) {
								infos[i].startNew();
								if (logger.isWarnEnabled())
									logger.warn("[报名结束] [{}] [id={}] [JzID={}] [报名家族ID={}] [参赛ID={}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID(), infos[i].getLogJiaZuID(), infos[i].getLogCanSai()});
							}
						}else if (infos[i].getState() == SiFangInfo.SIFANG_STATE_START_NOTICE) {
							//开始广播
							int gonggaoNum = infos[i].getGonggaoNum();
							if (gonggaoNum < 公告时间.length) {
								if (infos[i].getGonggaoTime() + 公告时间[gonggaoNum] <= nowTime) {
									//公告还未公告完
									if (gonggaoNum == 0) {
										infos[i].setGonggaoTime(nowTime);
									}
									if (gonggaoNum == 公告时间.length - 1) {
										playerGoToMapType.clear();
										infos[i].setState(SiFangInfo.SIFANG_STATE_START_JOIN);
										Game game = new Game(GameManager.getInstance(), GameManager.getInstance().getGameInfo(pkMapName[i]));
										game.init();
										infos[i].setGame(game);
										infos[i].randomJizZuPost();
										infos[i].createOverTemp();
										if (logger.isWarnEnabled())
											logger.warn("[公告结束] [{}] [id={}] [JzID={}] [报名家族数={}] [参赛数{}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID(), infos[i].getEnListID().size(), infos[i].getLogCanSaiNum()});
									}else {
										if (i == 0) {
											for(Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
												String msg = Translate.text_sifang_001 + ((公告时间[公告时间.length - 1] - 公告时间[gonggaoNum])/1000/60)
												+Translate.text_sifang_002;
												pp.send_HINT_REQ(msg, (byte) 2);
											}
										}
									}
									infos[i].setGonggaoNum(gonggaoNum + 1);
								}
							}else {
								playerGoToMapType.clear();
								infos[i].setState(SiFangInfo.SIFANG_STATE_START_JOIN);
								Game game = new Game(GameManager.getInstance(), GameManager.getInstance().getGameInfo(pkMapName[i]));
								game.init();
								infos[i].setGame(game);
								infos[i].randomJizZuPost();
								infos[i].createOverTemp();
								if (logger.isWarnEnabled())
									logger.warn("[公告结束] [{}] [id={}] [JzID={}] [报名家族数={}] [参赛数{}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID(), infos[i].getEnListID().size(), infos[i].getLogCanSaiNum()});
							}
						}else if (infos[i].getState() == SiFangInfo.SIFANG_STATE_START_JOIN) {
							//开始进场
							int jinruNum = infos[i].getJingchangNum();
							if (jinruNum < 进场时间.length) {
								if (!infos[i].isStartCountDown() && (进场时间[进场时间.length - 1] + infos[i].getJingchangTime()) - nowTime <= showStartTime) {
									infos[i].setStartCountDown(true);
									int timeO = (int)((进场时间[进场时间.length - 1] + infos[i].getJingchangTime()) - nowTime);
									LivingObject[] living = infos[i].getGame().getLivingObjects();
									for (int infogamP = 0 ; infogamP < living.length; infogamP++) {
										if (living[infogamP] instanceof Player) {
											Player player = (Player)living[infogamP];
											SIFANG_SHOW_START_TIME_RES res = new SIFANG_SHOW_START_TIME_RES(GameMessageFactory.nextSequnceNum(), timeO);
											player.addMessageToRightBag(res);
										}
									}
								}
								if (infos[i].getJingchangTime() + 进场时间[jinruNum] <= nowTime) {
									//公告还未公告完
									if (jinruNum == 0) {
										infos[i].setJingchangTime(nowTime);
									}
									if (jinruNum == 进场时间.length - 1) {
										infos[i].setState(SiFangInfo.SIFANG_STATE_START_START);
										infos[i].setStartTime(nowTime);
										infos[i].setKillNum(new int[infos[i].getEnListID().size()]);
										if (i == 0) {
											for(Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
												pp.send_HINT_REQ(Translate.text_sifang_003, (byte) 2);
											}
										}
										tranGame(infos[i]);
										if (logger.isWarnEnabled())
											logger.warn("[进场结束] [{}] [id={}] [JzID={}] [报名家族数={}] [参赛数{}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID(), infos[i].getEnListID().size(), infos[i].getLogCanSaiNum()});
									}else {
										for (int ii = 0; ii < infos[i].getEnListPlayerID().size(); ii ++) {
											for (int jj = 0; jj < infos[i].getEnListPlayerID().get(ii).size(); jj++) {
												if (PlayerManager.getInstance().isOnline(infos[i].getEnListPlayerID().get(ii).get(jj))) {
													Player pp = null;
													try{
														pp = PlayerManager.getInstance().getPlayer(infos[i].getEnListPlayerID().get(ii).get(jj));
													}catch(Exception e){
														logger.error("发进场公告的时候：", e);
														continue;
													}
													String msg = Translate.text_sifang_004+infoName[i]+ Translate.text_sifang_005 + ((进场时间[进场时间.length - 1] - 进场时间[jinruNum])/1000/60)
													+Translate.text_sifang_006;
													MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(20);
													Option_Cancel cancel = new Option_Cancel();
													cancel.setText(Translate.取消);
													Option_SiFang_Jingru ok = new Option_SiFang_Jingru(i);
													ok.setText(Translate.确定);
													mw.setOptions(new Option[]{ok, cancel});
													CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
													pp.addMessageToRightBag(creq);
//													pp.send_HINT_REQ(msg, (byte) 5);
												}
											}
										}
									}
									infos[i].setJingchangNum(jinruNum + 1);
								}
							}else {
								infos[i].setState(SiFangInfo.SIFANG_STATE_START_START);
								infos[i].setStartTime(nowTime);
								infos[i].setKillNum(new int[infos[i].getEnListID().size()]);
								if (i == 0) {
									for(Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
										String msg = Translate.text_sifang_003;
										pp.send_HINT_REQ(msg, (byte) 2);
									}
								}
								tranGame(infos[i]);
								if (logger.isWarnEnabled())
									logger.warn("[进场结束] [{}] [id={}] [JzID={}] [报名家族数={}] [参赛数{}]", new Object[]{infoName[i], infos[i].getId(), infos[i].getJiaZuID(), infos[i].getEnListID().size(), infos[i].getLogCanSaiNum()});
							}
						}else if (infos[i].getState() == SiFangInfo.SIFANG_STATE_START_START) {
							//正式开始
						}
					}
//					if (logger.isInfoEnabled())
//						logger.info("一次B心跳处理:" + (SystemTime.currentTimeMillis() - nowTime) + "毫秒");
				}catch(Exception e){
					logger.error("四方神兽处理活动状态出错:", e);
				}
				
			}catch(Exception e){
				logger.error("四方神兽run出错:", e);
			}
		}
	}
	
	//添加一个参加者
	public void msg_add_enterPlayer(int type, Player player, long playerID){
		try{
			if (type < 0 || type >= infos.length) {
				logger.error("出错，不应该出现type越界" + type);
				return;
			}
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.text_sifang_007);
				return;
			}
			
			if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
				player.sendError(Translate.text_sifang_008);
				return;
			}
			
			if (jiazu.getJiazuMaster() != player.getId()) {
				player.sendError(Translate.text_sifang_009);
				return;
			}
			
//			Player other = null;
//			try {
//				other = PlayerManager.getInstance().getPlayer(playerID);
//			} catch (Exception e) {
//				logger.error("邀请参赛角色不存在:", e);
//				return;
//			}
			
			PlayerSimpleInfo other = PlayerSimpleInfoManager.getInstance().getInfoById(playerID);
			if (other == null) {
				logger.error("邀请参赛角色不存在:" + playerID);
				return ;
			}
			
			if (player.getJiazuId() != other.getJiazuId()) {
				logger.error("邀请角色家族和被邀请角色家族不一致");
				return ;
			}
			
			SiFangInfo info = infos[type];
			ArrayList<Long> ids = null;
			boolean isHave = false;
			for (int i = 0; i < info.getEnListID().size(); i++) {
				long id = info.getEnListID().get(i);
				if (id == player.getJiazuId()) {
					isHave = true;
					ids = info.getEnListPlayerID().get(i);
				}
			}
			if (!isHave) {
				player.sendError(Translate.text_sifang_010);
				return;
			}
			
			if (isInSiFang(playerID) >= 0) {
				player.sendError(Translate.text_sifang_011);
				return;
			}
			
			if (ids.size() >= JIAZU_PLAYER_MAX) {
				player.sendError(Translate.text_sifang_012);
				return;
			}
			
			ids.add(playerID);
			em.notifyFieldChange(info, "enListPlayerID");
			
			if (logger.isWarnEnabled())
				logger.warn("[四方神兽] [添加参赛者] [type{}] [p={}] [aID={}]", new Object[]{type, player.getId() + "--" + player.getName(), playerID});
		}catch(Exception e){
			logger.error("msg_add_enterPlayer:", e);
		}
	}
	
	//移除一个参加者
	public void msg_remove_enterPlayer(int type, Player player, long playerID){
		try{
			if (type < 0 || type >= infos.length) {
				logger.error("出错，不应该出现type越界" + type);
				return;
			}
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.text_sifang_007);
				return;
			}
			
			if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
				player.sendError(Translate.text_sifang_008);
				return;
			}
			
			if (jiazu.getJiazuMaster() != player.getId()) {
				player.sendError(Translate.text_sifang_009);
				return;
			}
			
			SiFangInfo info = infos[type];
			ArrayList<Long> ids = null;
			boolean isHave = false;
			for (int i = 0; i < info.getEnListID().size(); i++) {
				long id = info.getEnListID().get(i);
				if (id == player.getJiazuId()) {
					isHave = true;
					ids = info.getEnListPlayerID().get(i);
				}
			}
			if (!isHave) {
				player.sendError(Translate.text_sifang_010);
				return;
			}
			int index = -1;
			for (int i = 0; i < ids.size(); i++) {
				if (ids.get(i) == playerID) {
					index = i;
					break;
				}
			}
			if (index >= 0) {
				ids.remove(index);
				em.notifyFieldChange(info, "enListPlayerID");
			}
			
			if (logger.isWarnEnabled())
				logger.warn("[四方神兽] [删除参赛者] [type{}] [p={}] [aID={}]", new Object[]{type, player.getId() + "--" + player.getName(), playerID});
		}catch(Exception e){
			logger.error("msg_remove_enterPlayer", e);
		}
	}
	
	public SIFANG_OVER_MSG_RES msg_over_msg(Player player){
		int index = -1;
		for (int i = 0; i < infos.length; i++) {
			if (infos[i].getGame() == null) {
				continue;
			}
			if (player.getCurrentGame() == infos[i].getGame()) {
				index = i;
				break;
			}
		}
		
		if (index < 0) {
			player.sendError(Translate.text_sifang_013);
			return null;
		}
		
//		int index = -1;
//		for (int i = 0; i < infos.length; i++) {
//			for (int j = 0; j < infos[i].getEnListID().size(); j++) {
//				if (infos[i].getEnListID().get(j) == player.getJiazuId()) {
//					index = i;
//					break;
//				}
//			}
//		}
//		if (index < 0) {
//			player.sendError("您的家族不在报名列表中不能查看");
//			return null;
//		}
//		if (infos[index].getState() != SiFangInfo.SIFANG_STATE_START_START) {
//			player.sendError("五方圣兽活动还未开始或已经结束不能查看 ");
//			return null;
//		}
		return infos[index].buildMsg(player, false, true);
	}
	
	//点击NPC设置参赛
	public void opt_2SetPlayer(Player player, int type){
		try{
			if (type < 0 || type >= infos.length) {
				logger.error("出错，不应该出现type越界" + type);
				return;
			}
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.text_sifang_007);
				return;
			}
			
			if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
				player.sendError(Translate.text_sifang_008);
				return;
			}
			
			if (jiazu.getJiazuMaster() != player.getId()) {
				player.sendError(Translate.text_sifang_009);
				return;
			}
			
			SiFangInfo info = infos[type];
			ArrayList<Long> ids = null;
			boolean isHave = false;
			for (int i = 0; i < info.getEnListID().size(); i++) {
				long id = info.getEnListID().get(i);
				if (id == player.getJiazuId()) {
					isHave = true;
					ids = info.getEnListPlayerID().get(i);
					break;
				}
			}
			if (!isHave) {
				player.sendError(Translate.text_sifang_010);
				return;
			}
			
			ArrayList<Long> players = new ArrayList<Long>();
			Set<JiazuMember> memberIDs = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
			for (JiazuMember me : memberIDs) {
				int tt = isInSiFang(me.getPlayerID());
				logger.info("[家族成员] [{}] [{}] [{}]", new Object[]{jiazu.getJiazuID(), me.getPlayerID(), tt});
				if (tt != type && tt != -1) {
					continue;
				}
				PlayerSimpleInfo player2 = PlayerSimpleInfoManager.getInstance().getInfoById(me.getPlayerID());
				if (player2 == null) {
					logger.error("家族成员不存在 [{}] [{}]", new Object[]{jiazu.getJiazuID(), me.getPlayerID()});
					continue;
				}
				if (player2.getLevel()< JOIN_LEVEL_MAX){
					continue;
				}
				players.add(me.getPlayerID());
			}
			
			SiFangSimplePlayerInfo[] sifangPlayers = new SiFangSimplePlayerInfo[players.size()];
			for (int i = 0; i < players.size(); i++) {
				sifangPlayers[i] = new SiFangSimplePlayerInfo();
				PlayerSimpleInfo player2 = PlayerSimpleInfoManager.getInstance().getInfoById(players.get(i));
				if (player2 == null) {
					logger.error("家族成员? [{}] [{}]", new Object[]{jiazu.getJiazuID(), players.get(i)});
					sifangPlayers[i].setPlayerId(0);
					sifangPlayers[i].setPlayerLevel(0);
					sifangPlayers[i].setPlayerName(Translate.未知);
					sifangPlayers[i].setCareer(1);
					continue;
				}
				sifangPlayers[i].setPlayerId(player2.getId());
				sifangPlayers[i].setPlayerLevel(player2.getLevel());
				sifangPlayers[i].setPlayerName(player2.getName());
				sifangPlayers[i].setCareer(player2.getCareer());
				
			}
			
			long[] chooseIds = new long[ids.size()];
			for (int i = 0; i< ids.size(); i++){
				chooseIds[i] = ids.get(i);
			}
			
			SIFANG_ENLIST_PLAYER_RES res = new SIFANG_ENLIST_PLAYER_RES(GameMessageFactory.nextSequnceNum(), type, JIAZU_PLAYER_MAX, sifangPlayers, chooseIds);
			player.addMessageToRightBag(res);
			if (logger.isWarnEnabled())
				logger.warn("[设置参赛] [p={}] [t={}] [Csize={}] [ids={}]", new Object[]{player.getId() + "~" + player.getName(), type, sifangPlayers.length, ids.size()});
		}catch(Exception e){
			logger.error("opt_EnList", e);
		}
	}
	
	//点击NPC进去PK场景，type同info的type 5圣兽
	public void opt_Jingru(Player player, int type){
		if (type < 0 || type >= infos.length) {
			logger.error("opt_Jingru type错误" + type);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.text_sifang_007);
			return;
		}
		
		boolean isBaoMing = false;
		ArrayList<Long> ids = null;
		for (int i = 0; i< infos[type].getEnListID().size(); i++) {
			if (infos[type].getEnListID().get(i) == player.getJiazuId()) {
				isBaoMing = true;
				ids = infos[type].getEnListPlayerID().get(i);
			}
		}
		if (!isBaoMing) {
			player.sendError(Translate.text_sifang_014);
			return;
		}
		
		boolean isCanSai = false;
		for (int i = 0; i < ids.size(); i++) {
			if (ids.get(i) == player.getId()) {
				isCanSai = true;
			}
		}
		
		if (!isCanSai) {
			player.sendError(Translate.text_sifang_015);
			return;
		}
		
		if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_JOIN) {
			player.sendError(Translate.text_sifang_016);
			return;
		}
		int index = 0;
		for (int i = 0; i < infos[type].getJiaZuList().length; i++) {
			if (infos[type].getJiaZuList()[i] == jiazu.getJiazuID()) {
				index = i;
			}
		}
		
		if (player.isFlying()) {
			player.downFromHorse();
		}
		
		playerGoToMapType.put(player.getId(), type);
		int[] XY = pkMapXY[index];
		player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, pkMapName[type], XY[0], XY[1]));
		if (infos[type].isStartCountDown()) {
			int timeO = (int)((进场时间[进场时间.length - 1] + infos[type].getJingchangTime()) - SystemTime.currentTimeMillis());
			SIFANG_SHOW_START_TIME_RES res = new SIFANG_SHOW_START_TIME_RES(GameMessageFactory.nextSequnceNum(), timeO);
			player.addMessageToRightBag(res);
		}
		if (logger.isWarnEnabled())
			logger.warn("[参加比赛] [p={}] [t={}] [jiD={}] [{}]", new Object[]{player.getId() + "--" + player.getName(), type, player.getJiazuId(), index});
	}
	
	public void opt_JingRu_BOSS(Player player, int type){
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.text_sifang_007);
			return;
		}
		if (infos[type].getJiaZuID() != jiazu.getJiazuID()) {
			player.sendError(Translate.text_sifang_017);
			return;
		}
		if (infos[type].getState() != SiFangInfo.SIFANG_STATE_OVER && infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
			player.sendError(Translate.text_sifang_018);
			return;
		}
		if (infos[type].getBossGame() == null) {
			Game bossGame = new Game(GameManager.getInstance(), GameManager.getInstance().getGameInfo(bossMmapName[type]));
			try {
				bossGame.init();
			} catch (Exception e) {
				SiFangManager.logger.error("四方BOSS地图创建出问题:", e);
			}
			infos[type].setBossGame(bossGame);
		}
		MapArea mapArea = infos[type].getBossGame().gi.getMapAreaByName(Translate.出生点);
		Random random = new Random();
		int x = mapArea.getX()+random.nextInt(mapArea.getWidth());
		int y = mapArea.getY()+random.nextInt(mapArea.getHeight());
		playerGoToBossMap.put(player.getId(), type);
		player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, infos[type].getBossGame().gi.name, x, y));
	}
	
	//报名
	public void opt_BaoMing(Player player, int type) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.五方)) {
			player.sendError(Translate.合服功能关闭提示);
			return;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.text_sifang_007);
			return;
		}
		if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
			player.sendError(Translate.text_sifang_008);
			return;
		}
		if (infos[type].getEnListID().size() >= JOIN_JIAZU_MAX) {
			player.sendError(Translate.text_sifang_019);
			return;
		}
		if (jiazu.getLevel() <= 1) {
			player.sendError(Translate.text_sifang_020);
			return;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			player.sendError(Translate.text_sifang_009);
			return;
		}
		if (jiazu.getJiazuMoney() < JOIN_MONEY) {
			player.sendError(Translate.text_sifang_021);
			return;
		}
		if (isJoin(player, type)) {
			player.sendError(Translate.text_sifang_022);
			return;
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);
		mw.setTitle(Translate.text_sifang_023);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		Option_SiFang_Join_OK ok = new Option_SiFang_Join_OK(type);
		ok.setText(Translate.确定);
		mw.setOptions(new Option[]{ok, cancel});
		String message = Translate.text_sifang_024+TradeManager.putMoneyToMyText(JOIN_MONEY)+ Translate.text_sifang_025 +"(<f color='0xffff00'>"+infoName[type]+"</f>)" + Translate.text_sifang_026;
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		player.addMessageToRightBag(creq);
	}
	
	//报名
	public synchronized void opt_BaoMingOK(Player player, int type) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.text_sifang_007);
			return;
		}
		if (infos[type].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
			player.sendError(Translate.text_sifang_008);
			return;
		}
		if (infos[type].getEnListID().size() >= JOIN_JIAZU_MAX) {
			player.sendError(Translate.text_sifang_019);
			return;
		}
		if (jiazu.getLevel() <= 1) {
			player.sendError(Translate.text_sifang_020);
			return;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			player.sendError(Translate.text_sifang_009);
			return;
		}
		if (jiazu.getJiazuMoney() < JOIN_MONEY) {
			player.sendError(Translate.text_sifang_021);
			return;
		}
		if (isJoin(player, type)) {
			player.sendError(Translate.text_sifang_022);
			return;
		}
		if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
			player.sendError(Translate.家族资金不足封印);
			return ;
		}
		long oldM = jiazu.getJiazuMoney();
		jiazu.setJiazuMoney(jiazu.getJiazuMoney() - JOIN_MONEY);
		infos[type].addEnList(player.getJiazuId());
		player.send_HINT_REQ(Translate.text_sifang_027 + TradeManager.putMoneyToMyText(JOIN_MONEY), (byte)5);
		opt_2SetPlayer(player, type);
		if (logger.isWarnEnabled())
			logger.warn("[四方神兽] [报名成功] [pID={}] [jzID={}] [type={}] [jzM={}]", new Object[]{player.getId()+"--"+player.getName(), player.getJiazuId(), type, oldM + "--" + jiazu.getJiazuMoney()});
	}
	
	public static void setInstance(SiFangManager instance) {
		SiFangManager.instance = instance;
	}

	public static SiFangManager getInstance() {
		return instance;
	}
	
	//与当时时间比较，看是否过  true为超过， false为未超过  周天视为最小
	private boolean isCalender(int week, int house, int min){
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) > week) {
			//如果星期都大  肯定大
			return true;
		}else if (calendar.get(Calendar.DAY_OF_WEEK) == week) {
			//一样大 就比较小时
			if (calendar.get(Calendar.HOUR_OF_DAY) > house) {
				return true;
			}else if (calendar.get(Calendar.HOUR_OF_DAY) == house) {
				//一样大 就比较分钟
				if (calendar.get(Calendar.MINUTE) >= min) {
					return true;
				} else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	//是否报名
	private boolean isJoin (Player player, int type) {
		SiFangInfo info = infos[type];
		for (int i = 0; i < info.getEnListID().size(); i++) {
			long id = info.getEnListID().get(i);
			if (id == player.getJiazuId()) {
				return true;
			}
		}
		return false;
	}
	
	//无条件开启报名
	public synchronized void startBaoMing(){
		for (int i = 0; i < infos.length; i++) {
			if (infos[i].getState() == SiFangInfo.SIFANG_STATE_OVER) {
				infos[i].setState(SiFangInfo.SIFANG_STATE_START_ENTER);
				if (logger.isWarnEnabled()){
					logger.warn("后台点击了开始报名");
				}
			}else if (infos[i].getState() == SiFangInfo.SIFANG_STATE_START_ENTER) {
				infos[i].startNew();
				if (logger.isWarnEnabled()){
					logger.warn("后台点击了开始广播");
				}
			}
		}
	}
	
//	//无条件开启公告，开启公告后会自动进入下面流程，等会开启四方神兽活动
//	public synchronized void startGonggao(){
//		for (int i = 0; i < infos.length; i++) {
//			if (infos[i].getState() != SiFangInfo.SIFANG_STATE_START_ENTER) {
//				continue;
//			}
//			infos[i].setState(SiFangInfo.SIFANG_STATE_START_NOTICE);
//			infos[i].setGonggaoNum(0);
//			infos[i].setGonggaoTime(0);
//			infos[i].setJingchangNum(0);
//			infos[i].setJingchangTime(0);
//		}
//		if (logger.isWarnEnabled()){
//			logger.warn("后台点击了开始公告");
//		}
//	}
	
	//判断一个角色ID是不是已经在参赛名单中了 返回他在哪个名单
	public int isInSiFang(long playerID) {
		for (int i = 0; i< infos.length; i++) {
			for (int j = 0; j < infos[i].getEnListID().size(); j++) {
				for (int k = 0; k < infos[i].getEnListPlayerID().get(j).size(); k++) {
					if (infos[i].getEnListPlayerID().get(j).get(k) == playerID) {
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	//是否在参加四方神兽比赛
	public boolean isInSiFangGame(Player player){
		if (player.getCurrentGame() == null) {
			return false;
		}
		for (int i = 0; i < infos.length; i++) {
			if (infos[i].getGame() == null) {
				continue;
			}
			if (player.getCurrentGame() == infos[i].getGame()) {
				return true;
			}
		}
		return false;
	}
	
	public void notifyKilled(Player player, Fighter caster) {
		if (!(caster instanceof Player) && !(caster instanceof Pet)) {
			return;
		}
		int type = -1;
		for (int i = 0; i < infos.length; i++) {
			if (infos[i].getGame() == null) {
				continue;
			}
			if (player.getCurrentGame() == infos[i].getGame()) {
				type = i;
				break;
			}
		}
		Player kill = null;
		if (caster instanceof Player) {
			kill = (Player) caster;
		}else if (caster instanceof Pet) {
			Pet p = (Pet) caster;
			try {
				kill = PlayerManager.getInstance().getPlayer(p.getOwnerId());
			} catch (Exception e) {
				logger.error("根据杀人的宠物取Player出错", e);
				return;
			}
		}
		if (kill.getJiazuId() == player.getJiazuId()) {
			return;
		}
		int jiazuIndex = -1;
		for (int i = 0; i < infos[type].getEnListID().size(); i++) {
			if (kill.getJiazuId() == infos[type].getEnListID().get(i)) {
				jiazuIndex = i;
				break;
			}
		}
		if (jiazuIndex < 0) {
			if (logger.isWarnEnabled())
				logger.warn("[计杀人数] [出错，杀人者家族ID不在比赛列表里？] [kill={}] [player={}] [type={}] [info={}]", new Object[]{kill.getId()+"-"+kill.getName()+"-"+kill.getJiazuId(), player.getId()+"-"+player.getName()+"-"+player.getJiazuId(), type, infos[type].getLogJiaZuID()});
			return;
		}
		infos[type].getKillNum()[jiazuIndex] += 1;
	}
	
	public boolean isJiaZuWin(Player player, int type){
		return infos[type].getJiaZuID() == player.getJiazuId() && player.getJiazuId() > 0;
	}
	
	public boolean isJiaZuInSiFang(long jiaZuID){
		for (int i = 0; i < infos.length; i++) {
			if (infos[i].getJiaZuID() == jiaZuID) {
				return true;
			}
			for (int j = 0; j < infos[i].getEnListID().size(); j++) {
				if (infos[i].getEnListID().get(j) == jiaZuID) {
					return true;
				}
			}
		}
		return false;
	}
	
	//如果是四方圣兽的BOSS
	public boolean isSiFangBOSS(Player player, Game game, Monster mon, List os){
		try{
			String gameName = game.gi.getName();
			for (int i = 0; i < bossMmapName.length; i++) {
				if (bossMmapName[i].equals(gameName)) {
					if (bossName[i].equals(mon.getName())) {
						if (infos[i].getJiaZuID() > 0) {
							if (logger.isWarnEnabled())
								logger.warn("[四方BOSS掉落] [i={}] [jid={}] [g={}] [m={}]", new Object[]{i, infos[i].getJiaZuID(), game.gi.name, mon.getName()});
							Jiazu jiazu = JiazuManager.getInstance().getJiazu(infos[i].getJiaZuID());
							if (jiazu != null) {
								for (int j = 0, n = os.size(); j < n; j++) {
									Object object = os.get(j);
									if (object instanceof FlopSchemeEntity.ShareFlopEntity) {
										FlopSchemeEntity.ShareFlopEntity sfe = (FlopSchemeEntity.ShareFlopEntity)object;
										jiazu.addToWareHouse(sfe.getEntity(), 1);
										if (logger.isWarnEnabled()) 
											logger.warn("[四方BOSS掉落家族仓库] [t={}] [jid={}] [g={}] [EN={}] [n={}]", new Object[]{i, infos[i].getJiaZuID(), game.gi.name, sfe.getEntity().getArticleName(), 1});
									}else if (object instanceof FlopSchemeEntity.PrivateFlopEntity) {
										FlopSchemeEntity.PrivateFlopEntity pfe = (FlopSchemeEntity.PrivateFlopEntity)object;
										jiazu.addToWareHouse(pfe.getEntity(), 1);
										if (logger.isWarnEnabled()) 
											logger.warn("[四方BOSS掉落家族仓库] [t={}] [jid={}] [g={}] [EN={}] [n={}]", new Object[]{i, infos[i].getJiaZuID(), game.gi.name, pfe.getEntity().getArticleName(), 1});
									}
								}
							}
							LivingObject[] living = game.getLivingObjects();
							for (int aa = 0 ; aa < living.length; aa++) {
								if (living[aa] instanceof Player) {
									Player toMsg = (Player)living[aa];
									toMsg.sendError(Translate.text_sifang_028+bossName[i]+ Translate.text_sifang_029+"(<f color='0xff0000'>"+jiazu.getWareHouse().size() + "/" + JiazuManager.WAREHOUSE_MAX + "</f>)" + Translate.text_sifang_030);
								}
							}
						}
						return true;
					}
				}
			}
			return false;
		}catch(Exception e) {
			logger.error("boss掉落出错", e);
		}
		return false;
	}
	
	private void tranGame(SiFangInfo info){
		LivingObject[] living = info.getGame().getLivingObjects();
		for (int i = 0 ; i < living.length; i++) {
			if (living[i] instanceof Player) {
				Player player = (Player)living[i];
				if (player.isOnline()) {
					int index = 0;
					for (int j = 0; j < info.getJiaZuList().length; j++) {
						if (info.getJiaZuList()[j] == player.getJiazuId()) {
							index = j;
						}
					}
					int[] XY = pkMapStartXY[index];
					SIFANG_SHOW_INFO_BUTTON_RES res = new SIFANG_SHOW_INFO_BUTTON_RES(GameMessageFactory.nextSequnceNum());
					player.addMessageToRightBag(res);
					player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, info.getGame().gi.name, XY[0], XY[1]));
				}
			}
		}
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.五方圣兽;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		if (c.get(Calendar.DAY_OF_WEEK) != notify_week) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		result.add("15:00");
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		for (SiFangInfo sf : infos) {
			if (sf.getState() == SiFangInfo.SIFANG_STATE_START_JOIN || sf.getState() == SiFangInfo.SIFANG_STATE_START_START) {
				return true;
			}
		}
		return false;
	}
}
