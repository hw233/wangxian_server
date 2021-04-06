package com.fy.engineserver.activity.wolf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.WOLF_END_NOTICE_RES;
import com.fy.engineserver.message.WOLF_SKILL_ID_RES;
import com.fy.engineserver.message.WOLF_START_FIGHT_NOTICE_RES;
import com.fy.engineserver.message.WOLF_UPDATE_FIGHT_INFO_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.TimeTool;

public class WolfGame {

	public Game game;
	
	private Logger logger = WolfManager.logger;
	
	public List<Long> joinList = new ArrayList<Long>();
	public List<Player> duanWangList = new ArrayList<Player>();
	
	public ActivityState state = ActivityState.等待进入;
	
	List<Long> noticeList = new ArrayList<Long>();
	public List<Long> notice2List = new ArrayList<Long>();
	
	List<Long> noticeBuffList = new ArrayList<Long>();
	
	List<Long> noticeEndList = new ArrayList<Long>();
	
	public long endWaitTime;
	
	//通知进入副本时间
	public long noticeEnterTime;
	
	public long startFightTime;
	public long refreshGrassTime;
	public long refreshBoxTime;
	
	public long startNoticeResultTime;
	
	private long clientreqtime = 20 * 1000L;
	
	//准备时间
	public long startReadyTime;
	
	//出生信息
	public Map<Long, BornPoint> bornInfos = new ConcurrentHashMap<Long, BornPoint>(); 
	//战斗信息
	public Map<Long, WolfBattleInfo> battleInfos = new ConcurrentHashMap<Long, WolfBattleInfo>(); 
	
	public String wolfName;
	public String [] sheepNames = new String[]{"","","",""};
	public int [] careers = new int[5];
	
	public WolfGame(Game game,List<Long> joinList){
		this.game = game;
		this.joinList = joinList;
		endWaitTime = System.currentTimeMillis() + WolfManager.getInstance().waitTime;
		initBornPoint();
		initBattleInfo();
		addMatchNums();
	}
	
	public boolean inGame(Player player){
		if(joinList.contains(player.getId())){
			return true;
		}
		return false;
	}
	
	public void addMatchNums(){
		for(long id : joinList){
			JoinNumData data = WolfManager.getInstance().joinNums.get(id);
			if(data == null){
				data = new JoinNumData();
			}
			data.setJoinNums(data.getJoinNums() + 1);
			data.setJoinTime(System.currentTimeMillis());
			WolfManager.getInstance().joinNums.put(id, data);
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [增加参与次数] [id:"+id+"] [次数:"+data.getJoinNums()+"] [人员:"+joinList+"]");
			}
		}
	}
	
	public void initBornPoint(){
		Random random = new Random();
		int wolfIndex = random.nextInt(joinList.size());
		int sheepIndex = 0;
		int [][] points = WolfManager.getInstance().getRandomPoints(WolfManager.getInstance().sheepPoints,joinList.size() - 1);
		for(int i=0;i<joinList.size();i++){
			long id = joinList.get(i);
			if(id > 0){
				if(PlayerManager.getInstance().isOnline(id)){
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						if(p != null){
							careers[i] = p.getCareer();
							if(i==wolfIndex){
								wolfName = p.getName();
							}else{
								sheepNames[sheepIndex] = p.getName();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(i==wolfIndex){
					BornPoint point = new BornPoint(joinList.get(i), 1, WolfManager.getInstance().wolfPoints[0], WolfManager.getInstance().wolfPoints[1]);
					bornInfos.put(joinList.get(i), point);
				}else{
					BornPoint point = new BornPoint(joinList.get(i), 0, points[sheepIndex][0],points[sheepIndex][1]);
					bornInfos.put(joinList.get(i), point);
					if(WolfManager.getInstance().testLog){
						logger.warn("[小羊快跑] [初始化出生点信息] [test] [sheepIndex:"+sheepIndex+"] [points:"+points.length+"] [joinList:"+joinList.size()+"] [id:"+id+"] [point:"+point+"]");
					}
					sheepIndex++;
				}
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[小羊快跑] [初始化出生点信息] [成功] [人数:"+joinList.size()+"] [信息数:"+bornInfos+"] [wolfName:"+wolfName+"] [sheepNames:"+Arrays.toString(sheepNames)+"]");
		}
	}
	
	public void initBattleInfo(){
		for(int i=0;i<joinList.size();i++){
			long id = joinList.get(i);
			if(id > 0){
				if(PlayerManager.getInstance().isOnline(id)){
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						if(p != null){
							WolfBattleInfo info = new WolfBattleInfo();
							info.setId(p.getId());
							info.setName(p.getName());
							info.setCareer(p.getCareer());
							if(p.getName().equals(wolfName)){
								info.setBattleType(1);
							}
							battleInfos.put(p.getId(), info);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[小羊快跑] [初始化战斗信息] [成功] [人数:"+joinList.size()+"] [信息数:"+battleInfos.size()+"] [wolfName:"+wolfName+"] [sheepNames:"+Arrays.toString(sheepNames)+"]");
		}
	}
	
	public void heartbeat(){
		List<Player> ps = game.getPlayers();
		if(WolfManager.getInstance().testLog){
			Game.logger.warn("[小羊活动心跳] [当前状态:"+state+"] [狼:"+wolfName+"] [报名人数:"+joinList+"] [地图人数:"+(ps!=null?ps.size():"0")+"]");
		}
		
		try{
			game.heartbeat();
		}catch(Exception e){
			e.printStackTrace();
			WolfManager.logger.warn("[小羊快跑] [游戏心跳异常]",e);
		}
		
		if(state == ActivityState.通知结果 &&  ps.size() == 0){
			state = ActivityState.战斗结束;
			joinList.clear();
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [副本结束] [state:"+state+"] [人数:"+joinList.size()+"] [地图人数:"+ps.size()+"] ["+joinList+"]");
			}
			return;
		}
		
		//处理断网玩家
		for(Player p : duanWangList){
			if(p != null){
				if(joinList.contains(p.getId())){
					WolfManager.getInstance().playerLeave(p,"断网玩家");
					playerLeave(p);
					if(logger.isWarnEnabled()){
						logger.warn("[小羊快跑] [处理玩家断网] [duanWangList:"+duanWangList.size()+"] ["+p.getLogString()+"]");
					}
				}
			}
		}
		
		for(Player p : ps){
			if(p != null){
				//掉线断网
				if(!p.isOnline() || (System.currentTimeMillis() - p.getLastRequestTime() > clientreqtime)){
					if(joinList.contains(p.getId())){
						if(!duanWangList.contains(p)){
							duanWangList.add(p);
							if(logger.isWarnEnabled()){
								logger.warn("[小羊快跑] [玩家掉线] [name:"+p.getName()+"] [id:"+p.getId()+"] [state:"+state+"] [是否在线:"+p.isOnline()+"] [是否断网:"+(System.currentTimeMillis() - p.getLastRequestTime() > clientreqtime)+"] [人数:"+joinList.size()+"] [地图人数:"+ps.size()+"] ["+joinList+"] [是否在线:"+p.isOnline()+"] [上次客户端请求时间:"+TimeTool.formatter.varChar23.format(p.getLastRequestTime())+"]");
							}
						}
					}
				}
				if(p.getInterval() >= clientreqtime){
					if(joinList.contains(p.getId())){
						WolfManager.getInstance().playerLeave(p,"掉线断网");
						if(logger.isWarnEnabled()){
							logger.warn("[小羊快跑] [玩家断网] [心跳间隔:"+p.getInterval()+"] [name:"+p.getName()+"] [id:"+p.getId()+"] [state:"+state+"] [是否在线:"+p.isOnline()+"] [是否断网:"+(System.currentTimeMillis() - p.getLastRequestTime() > clientreqtime)+"] [人数:"+joinList.size()+"] [地图人数:"+ps.size()+"] ["+joinList+"] [是否在线:"+p.isOnline()+"] [上次客户端请求时间:"+TimeTool.formatter.varChar23.format(p.getLastRequestTime())+"]");
						}
					}
				}
				if (p.isIsUpOrDown()){
					p.downFromHorse(true);
				}
				if (p.getActivePetId() > 0 ){
					p.packupPet(true);
				}
			}
		}
		
		if(startFightTime > 0 && (System.currentTimeMillis() >= (startFightTime + WolfManager.fightLastTime))){
			if(startNoticeResultTime == 0){
				state = ActivityState.通知结果;
				startNoticeResultTime = System.currentTimeMillis();
				if(logger.isWarnEnabled()){
					logger.warn("[小羊快跑] [游戏心跳] [状态改变] [当前状态:"+state+"] [参加人数:"+joinList.size()+"]");
				}
			}
			
			Iterator<WolfBattleInfo> it2 = battleInfos.values().iterator();
			int sheepNums = 0;
			while(it2.hasNext()){
				WolfBattleInfo wInfo = it2.next();
				if(wInfo != null && wInfo.getBattleType() == 0){
					if(!wInfo.isDead()){
						sheepNums++;
					}
				}
			}
			int dNums = 4 - sheepNums;
			if(dNums < 0){
				dNums = 0;
			}
			String wolfInfo = Translate.translateString(Translate.识破小羊, new String[][] { { Translate.COUNT_1, String.valueOf(dNums) }});
			String sheepInfo = Translate.translateString(Translate.存活小羊, new String[][] { { Translate.COUNT_1, String.valueOf(sheepNums) }});
			Iterator<WolfBattleInfo> it = battleInfos.values().iterator();
			while(it.hasNext()){
				WolfBattleInfo wInfo = it.next();
				if(wInfo != null){
					if(PlayerManager.getInstance().isOnline(wInfo.getId())){
						try {
							Player p = PlayerManager.getInstance().getPlayer(wInfo.getId());
							if(p == null){
								continue;
							}
							if(!game.contains(p)){
								if(logger.isWarnEnabled()){
									logger.warn("[小羊快跑] [副本时间到通知结果] [出错:玩家不在该地图] [当前状态:"+state+"] ["+p.getLogString()+"]");
								}
								continue;
							}
							//0:失败，1:成功，2:封神
							if(!noticeEndList.contains(p.getId())){
								long rewardExps = 0;
								if(dNums == 4){
									if(wInfo.getBattleType() == 1){
										rewardExps = WolfManager.getInstance().rewardExpDatas.get(3).get(p.getCurrSoul().getGrade());
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2, wolfInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}else{
										rewardExps = WolfManager.getInstance().rewardExpDatas.get(1).get(p.getCurrSoul().getGrade());
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 0, sheepInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}
								}else if(dNums == 0){
									if(wInfo.getBattleType() == 1){
										rewardExps = WolfManager.getInstance().rewardExpDatas.get(1).get(p.getCurrSoul().getGrade());
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 0, wolfInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}else{
										rewardExps = WolfManager.getInstance().rewardExpDatas.get(3).get(p.getCurrSoul().getGrade());
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2, sheepInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}
								}else{
									rewardExps = WolfManager.getInstance().rewardExpDatas.get(2).get(p.getCurrSoul().getGrade());
									if(wInfo.getBattleType() == 1){
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 1, wolfInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}else{
										WOLF_END_NOTICE_RES res = new WOLF_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 1, sheepInfo, rewardExps+wInfo.getExps(), new long[]{});
										p.addMessageToRightBag(res);
									}
								}
								p.addExp(rewardExps, ExperienceManager.小羊结算经验);
								noticeEndList.add(p.getId());
								if(logger.isWarnEnabled()){
									logger.warn("[小羊快跑] [副本时间到通知结果] [成功] [rewardExps:"+rewardExps+"] [当前元神等级:"+p.getCurrSoul().getGrade()+"] [noticeEndList:"+noticeEndList+"] [wolfInfo:"+wolfInfo+"] [sheepInfo:"+sheepInfo+"] [sheepNums:"+sheepNums+"] [死亡数量:"+dNums+"] [type:"+wInfo.getBattleType()+"] ["+p.getLogString()+"]");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}
		
		switch (state) {
			case 准备战斗:
				if(System.currentTimeMillis() > endWaitTime){
					if(startReadyTime == 0){
						startReadyTime = System.currentTimeMillis() + 10 *1000;
					}
					for(long id : joinList){
						if(!noticeList.contains(id)){
							if(PlayerManager.getInstance().isOnline(id)){
								try {
									Player p = PlayerManager.getInstance().getPlayer(id);
									if(p == null){
										continue;
									}
									if(!game.contains(p)){
										if(logger.isWarnEnabled()){
											logger.warn("[小羊快跑] [进入副本通知玩家倒计时] [失败:玩家不在该地图] ["+p.getLogString()+"]");
										}
										continue;
									}
									if(p.isFlying()){
										p.downFromHorse();
									}
									if(!p.isHold()){
										p.setHold(true);
									}
									noticeList.add(id);
									if(p.getName().equals(wolfName)){
										WOLF_START_FIGHT_NOTICE_RES res2 = new WOLF_START_FIGHT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 10,p.getId(),Translate.狼信息);
										p.addMessageToRightBag(res2);
									}else{
										WOLF_START_FIGHT_NOTICE_RES res2 = new WOLF_START_FIGHT_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 10,p.getId(),Translate.羊信息);
										p.addMessageToRightBag(res2);
									}
									p.sendError(Translate.战斗开始);
									if(logger.isWarnEnabled()){
										logger.warn("[小羊快跑] [进入副本通知玩家倒计时] [hole:"+p.isHold()+"] ["+p.getLogString()+"]");
									}
								} catch (Exception e) {
									e.printStackTrace();
									continue;
								}
							}
						}
					}
					if(System.currentTimeMillis() >= startReadyTime){
						state = ActivityState.战斗开始;
					}
				}
				break;
			case 战斗开始:
				if(startFightTime == 0){
					refreshGrassTime = System.currentTimeMillis();
					refreshBoxTime = System.currentTimeMillis();
					startFightTime = System.currentTimeMillis();
					updateBattleInfo("战斗开始",battleInfos);
					WolfManager.getInstance().refreshMonster(game);
					for(long id : joinList){
						if(PlayerManager.getInstance().isOnline(id)){
							try {
								Player p = PlayerManager.getInstance().getPlayer(id);
								p.setHold(false);
								WolfManager.getInstance().noticeEndTime(p, WolfManager.fightLastTime);
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}
					}
					if(logger.isWarnEnabled()){
						logger.warn("[小羊快跑] [进入战斗状态]");
					}
				}
				
				if(System.currentTimeMillis() >= refreshGrassTime + WolfManager.grassRefrshTimeLength){
					refreshGrassTime = System.currentTimeMillis();
					refreshGrass();
				}
				if(System.currentTimeMillis() >= refreshBoxTime + WolfManager.boxRefrshTimeLength){
					refreshBoxTime = System.currentTimeMillis();
					refreshBox();
				}
				break;
			case 通知结果:
				if(System.currentTimeMillis() >= startNoticeResultTime + 10 * 1000){
					if(joinList != null && joinList.size() > 0){
						for(long id : joinList){
							if(id > 0 && PlayerManager.getInstance().isOnline(id)){
								try {
									Player p = PlayerManager.getInstance().getPlayer(id);
									if(game.contains(p)){
										playerLeave(p);
									}
									if(logger.isWarnEnabled()){
										logger.warn("[小羊快跑] [战斗结束] [avata:"+Arrays.toString(p.getAvata())+"] ["+p.getLogString()+"]");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						joinList.clear();
					}
				}
				break;
		}
	}
	
	public boolean isEffect(){
		if(game == null){
			return false;
		}
		if(joinList.size() == 0){
			return false;
		}
		return true;
	}
	
	public void updateBattleInfo(String reason,Map<Long, WolfBattleInfo> infos){
		String sheepDeadInfo = "";
		battleInfos = infos;
		
		int deads [] = new int[]{1,1,1,1};
		long ids [] = new long[4];
		String sNames [] = new String[]{"","","",""};
		int cs [] = new int[4];
		Iterator<WolfBattleInfo> it = battleInfos.values().iterator();
		int index = 0;
		int sheepNums = 0;
		while(it.hasNext()){
			WolfBattleInfo wInfo = it.next();
			if(wInfo != null){
				if(wInfo.getBattleType() == 1){
					continue;
				}
				if(!wInfo.isDead()){
					sheepNums++;
					deads[index] = 0;
				}
				ids[index] = wInfo.getId();
				sNames[index] = wInfo.getName();
				cs[index] = wInfo.getCareer();
				index++;
			}
		}
		
		int deadNum = 4 - sheepNums;
		if(deadNum < 0){
			deadNum = 0;
		}
		sheepDeadInfo = Translate.translateString(Translate.死亡信息, new String[][] { { Translate.COUNT_1, String.valueOf(sheepNums) }, {Translate.COUNT_2, String.valueOf(deadNum) }});
		for(long id : joinList){
			if(id > 0 && PlayerManager.getInstance().isOnline(id)){
				try {
					Player p = PlayerManager.getInstance().getPlayer(id);
					if(p == null){
						continue;
					}
					if(!game.contains(p)){
						if(logger.isWarnEnabled()){
							logger.warn("[小羊快跑] [更新战斗信息] [出错:玩家不在该地图] [当前状态:"+state+"] ["+p.getLogString()+"]");
						}
						continue;
					}
					WolfBattleInfo info = battleInfos.get(id);
					if(info == null){
						if(logger.isWarnEnabled()){
							logger.warn("[小羊快跑] [更新战斗信息] [出错:信息不存在] [state:"+state+"] ["+id+"]");
						}
						continue;
					}
					WOLF_UPDATE_FIGHT_INFO_RES res = new WOLF_UPDATE_FIGHT_INFO_RES(GameMessageFactory.nextSequnceNum(), wolfName, sheepDeadInfo, info.getExps(), ids, sNames, cs, deads);
					p.addMessageToRightBag(res);
					if(logger.isWarnEnabled()){
						logger.warn("[小羊快跑] [更新战斗信息] [reason:"+reason+"] [hold:"+p.isHold()+"] [deadNum:"+deadNum+"] [infos:"+infos.size()+"] ["+sheepDeadInfo+"] [state:"+state+"] [joinList:"+joinList.size()+"] [battleInfos:"+battleInfos.size()+"] [ids:"+Arrays.toString(ids)+"] [deads:"+Arrays.toString(deads)+"] [sNames:"+Arrays.toString(sNames)+"] ["+p.getLogString()+"]");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendSkillIds(Player p){
		if(p.getName().equals(wolfName)){
			//攻击距离120，320
			WOLF_SKILL_ID_RES res = new WOLF_SKILL_ID_RES(GameMessageFactory.nextSequnceNum(), p.getId(), new int[]{WolfManager.利齿, WolfManager.撕咬},new String[]{"wh_huenji","gs_duren"},new int[]{5,20},new int[]{220,420});
			p.addMessageToRightBag(res);
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [发送技能通知] [狼] [state:"+state+"] ["+p.getLogString()+"]");
			}
		}else{
			//攻击距离320,120
			WOLF_SKILL_ID_RES res = new WOLF_SKILL_ID_RES(GameMessageFactory.nextSequnceNum(), p.getId(), new int[]{WolfManager.召唤牧场大叔, WolfManager.极速},new String[]{"shouxuefeiteng","shoukui-gangdan"},new int[]{20,20},new int[]{320,-1});
			p.addMessageToRightBag(res);
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [发送技能通知] [羊] [state:"+state+"] ["+p.getLogString()+"]");
			}
		}
	}
	
	//比赛开启20秒后刷经验草，每20秒刷新一次
	public void refreshGrass(){
		int [][] points = WolfManager.getInstance().getRandomPoints(WolfManager.grassPoints, 9);
		List<Long> playersList = new ArrayList<Long>();
		Iterator<WolfBattleInfo> it = battleInfos.values().iterator();
		while(it.hasNext()){
			WolfBattleInfo wInfo = it.next();
			if(wInfo != null && wInfo.getBattleType() == 0){
				if(!wInfo.isDead() && wInfo.getBattleType() == 0){
					playersList.add(wInfo.getId());
				}
			}
		}
		
		FlopCaijiNpc npc = (FlopCaijiNpc)MemoryNPCManager.getNPCManager().createNPC(1000008);
		FlopCaijiNpc npc2 = (FlopCaijiNpc)MemoryNPCManager.getNPCManager().createNPC(1000007);
		FlopCaijiNpc npc3 = (FlopCaijiNpc)MemoryNPCManager.getNPCManager().createNPC(1000006);
		if(npc == null || npc2 == null || npc3 == null){
			logger.warn("[小羊快跑] [刷经验草失败：npc不存在] [npc:"+npc+"] [npc2:"+npc2+"] [npc3:"+npc3+"]");
			return;
		}
		for(int i=0;i<points.length;i++){
			int [] ps = points[i];
			if(ps != null && ps.length == 2){
				if(i<5){
					npc.setX(ps[0]);
					npc.setY(ps[1]);
					npc.setPlayersList(playersList);
					npc.setAllCanPickAfterTime(1000);
					npc.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					npc.setEndTime(SystemTime.currentTimeMillis() + 60*1000);
					npc.setGrassType(WolfManager.凡级青草);
					ResourceManager.getInstance().getAvata(npc);
					game.addSprite(npc);
					if(WolfManager.getInstance().testLog){
						logger.warn("[小羊快跑] [刷新经验草] [成功] [npcID:"+(npc==null?"null":npc.getId())+"] [grassType:"+npc.getGrassType()+"] [CategoryId:"+(npc==null?"null":npc.getNPCCategoryId())+"] [npcName:"+(npc==null?"null":npc.getName())+"] [数量:"+points.length+"] [坐标:"+(ps!=null?Arrays.toString(ps):"null")+"]");
					}
				}
				if(i >= 5 && i < 8){
					npc2.setX(ps[0]);
					npc2.setY(ps[1]);
					npc2.setPlayersList(playersList);
					npc2.setAllCanPickAfterTime(1000);
					npc2.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					npc2.setEndTime(SystemTime.currentTimeMillis() + 60*1000);
					npc2.setGrassType(WolfManager.灵级青草);
					ResourceManager.getInstance().getAvata(npc2);
					game.addSprite(npc2);
					if(WolfManager.getInstance().testLog){
						logger.warn("[小羊快跑] [刷新经验草] [成功] [npcID:"+(npc2==null?"null":npc2.getId())+"] [grassType:"+npc2.getGrassType()+"] [CategoryId:"+(npc2==null?"null":npc2.getNPCCategoryId())+"] [npcName:"+(npc2==null?"null":npc2.getName())+"] [数量:"+points.length+"] [坐标:"+(ps!=null?Arrays.toString(ps):"null")+"]");
					}
				}
				if(i == 8){
					npc3.setX(ps[0]);
					npc3.setY(ps[1]);
					npc3.setPlayersList(playersList);
					npc3.setAllCanPickAfterTime(1000);
					npc3.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					npc3.setEndTime(SystemTime.currentTimeMillis() + 60*1000);
					npc3.setGrassType(WolfManager.仙级青草);
					ResourceManager.getInstance().getAvata(npc3);
					game.addSprite(npc3);
					if(WolfManager.getInstance().testLog){
						logger.warn("[小羊快跑] [刷新经验草] [成功] [npcID:"+(npc3==null?"null":npc3.getId())+"] [grassType:"+npc3.getGrassType()+"] [CategoryId:"+(npc3==null?"null":npc3.getNPCCategoryId())+"] [npcName:"+(npc3==null?"null":npc3.getName())+"] [playersList:"+playersList+"] [数量:"+points.length+"] [坐标:"+(ps!=null?Arrays.toString(ps):"null")+"]");
					}
				}
			}
		}
	}
	
	//开启后10秒后刷神奇大宝箱，每45秒刷新一次
	public void refreshBox(){
		try {
			Article article = ArticleManager.getInstance().getArticle(WolfManager.getInstance().boxName);
			if(article == null){
				logger.warn("[小羊快跑] [刷神奇大宝箱失败：物品不存在]");
				return;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.小羊活动, null, article.getColorType(), 1, false);
			if(ae == null){
				logger.warn("[小羊快跑] [刷神奇大宝箱失败：创建物品失败]");
				return;
			}
			int [][] points = WolfManager.getInstance().getRandomPoints(WolfManager.boxPoints, 1);
			FlopCaijiNpc npc = (FlopCaijiNpc)MemoryNPCManager.getNPCManager().createNPC(1000005);
			if(npc == null){
				logger.warn("[小羊快跑] [刷神奇大宝箱失败：npc不存在] [id:"+1000005+"]");
				return;
			}
			List<Long> playersList = new ArrayList<Long>();
			for(long id : joinList){
				if(id > 0 && PlayerManager.getInstance().isOnline(id)){
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						if(p != null && p.getName().equals(wolfName)){
							playersList.add(p.getId());
							p.sendError(Translate.神奇大宝箱已刷新);
						}else{
							WolfBattleInfo sinfo = battleInfos.get(p.getId());
							if(sinfo.isDead()){
								playersList.add(p.getId());
								p.sendError(Translate.神奇大宝箱已刷新);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for(int j=0;j<points.length;j++){
				int [] ps = points[j];
				if(ps != null && ps.length == 2){
					npc.setX(ps[0]);
					npc.setY(ps[1]);
					npc.setArticle(article);
					npc.setAe(ae);
					npc.setPlayersList(playersList);
					npc.setAllCanPickAfterTime(1000);
					npc.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					npc.setEndTime(SystemTime.currentTimeMillis() + 3*60*1000);
					ResourceManager.getInstance().getAvata(npc);
					game.addSprite(npc);
				}
				if(logger.isWarnEnabled()){
					logger.warn("[小羊快跑] [刷神奇大宝箱] [成功] [npcID:"+(npc==null?"null":npc.getId())+"] [CategoryId:"+(npc==null?"null":npc.getNPCCategoryId())+"] [npcName:"+(npc==null?"null":npc.getName())+"] [数量:"+points.length+"] [坐标:"+(ps!=null?Arrays.toString(ps):"null")+"] [playersList:"+playersList+"]");
				}
			}
			
		}catch(Exception e){
			logger.warn("[小羊快跑] [刷神奇大宝箱异常]",e);
		}
	}
	
	//开宝箱
	public boolean openBox(Player p){
		WolfBattleInfo sinfo = battleInfos.get(p.getId());
		if(sinfo != null){
			if(sinfo.isDead() || sinfo.getBattleType() == 1){
				return true;
			}
		}
		return false;
	}
	
	//吃草
	public boolean eatGrass(Player player,long exps){
		WolfBattleInfo sinfo = battleInfos.get(player.getId());
		if(sinfo == null || sinfo.isDead() || sinfo.getBattleType() == 1){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [处理小羊吃草] [失败:战斗信息不存在] [被识破:"+(sinfo!=null?sinfo.isDead():"null")+"] ["+(sinfo!=null?sinfo.getBattleType():"null")+"] ["+player.getLogString()+"]");
			}
			return false;
		}
		sinfo.setExps(sinfo.getExps() + exps);
		battleInfos.put(player.getId(), sinfo);
		updateBattleInfo("小羊吃草",battleInfos);
		return true;
	}

	//小羊被抓 
	public void playerDead(Player wolf,Player sheep){
		WolfBattleInfo sinfo = battleInfos.get(sheep.getId());
		WolfBattleInfo winfo = battleInfos.get(wolf.getId());
		if(sinfo == null || winfo == null){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [处理小羊被抓] [失败:战斗信息不存在] [狼:"+wolf.getLogString()+"] [羊:"+ sheep.getLogString()+"]");
			}
			return;
		}
		sinfo.setDead(true);
		long eatSheepExp = WolfManager.getInstance().wolfExpDatas.get(wolf.getLevel());
		winfo.setExps(winfo.getExps() + eatSheepExp);
		battleInfos.put(sheep.getId(), sinfo);
		battleInfos.put(wolf.getId(), winfo);
		updateBattleInfo("小羊被抓",battleInfos);
	}
	
	//玩家离开
	public void playerLeave(Player p){
		if(p.isOnline()){
			p.setHold(false);
			p.setSpeedA(150);
			p.setPkMode(Player.和平模式);
			ResourceManager.getInstance().getAvata(p);
			game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
		}
	}
	
	public void destory(){
		game.removeAllNpc();
		joinList.clear();
		duanWangList.clear();
		bornInfos.clear();
		battleInfos.clear();
		game = null;
	}
	
}
