package com.fy.engineserver.activity.dice.city;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.dice.ActivityState;
import com.fy.engineserver.activity.dice.DiceBillboardData;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.dice.DicePointData;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.DICE_BILLBOARD_RES;
import com.fy.engineserver.message.DICE_END_NOTICE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.SHOOK_DICE2_RES;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
/**
 * 骰子副本
 * 
 * @create 2016-3-12 下午01:30:11
 */
public class DiceGame{

	public List<Long> ids = new ArrayList<Long>();
	public List<Long> newids = new ArrayList<Long>();
	
	public List<Long> notices = new ArrayList<Long>();
	private List<Long> noticesTimes = new ArrayList<Long>();
	
	private static long sleeptime = 100;
	
	private Game game;
	
	private int currLayer = 1;
	
	private Random random = new Random();
	
	//开始摇骰子的时间
	private long startShookDiceTime;
	//开始显示排行榜的时间
	private long showBillboradTime;
	
	private long noticeEnoughTime;
	
	//副本结束时间
	public long fightLastTime;
	
	private long showResultTime;
	
	//摇骰子结果
	private Map<String,DicePointData> pdatas = new HashMap<String,DicePointData>();
	
	public DiceGame(Game game){
		this.game = game;
	}
	
	public List<Long> getIds(){
		newids.clear();
		for(long id : ids){
			if(PlayerManager.getInstance().isOnline(id)){
				try {
					Player	p = PlayerManager.getInstance().getPlayer(id);
					if(game.contains(p)){
						newids.add(id);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return newids;
	}

	public void heartbeat(){
		List<Player> ps = game.getPlayers();
		
		try{
			game.heartbeat();
		}catch(Exception e){
			e.printStackTrace();
			if(DiceManager.logger.isWarnEnabled()){
				DiceManager.logger.warn("[骰子仙居] [副本心跳] [游戏心跳处理异常2] [人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [当前状态:"+DiceManager.getInstance().activityStat+"]",e);
			}
		}
		
		//死亡的玩家,飞行坐骑玩家监测
		if(ps != null && ps.size() > 0){
			List<Long> rList = new ArrayList<Long>();
			for(Player p : ps){
				if(p == null){
					continue;
				}
				if(p.isFlying()){
					p.downFromHorse();
				}
				if(p != null &&(p.isDeath() && p.isOnline())){
					p.setHp(p.getMaxHP());
					p.setMp(p.getMaxMP());
					p.setState(Player.STATE_STAND);
					p.setPkMode(Player.和平模式);
					p.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
					p.addMessageToRightBag(res);
					rList.add(p.getId());
					p.sendError(Translate.副本挑战失败);
					game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y));
					DiceManager.getInstance().playerLeave(p);
					if(DiceManager.logger.isWarnEnabled()){
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束:玩家死亡] [人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"]  [当前状态:"+DiceManager.getInstance().activityStat+"] ["+p.getLogString()+"]");
					}
				}
			}
			if(rList.size() > 0){
				ids.removeAll(rList);
			}
		}
		
		//玩家不足
		if(DiceManager.getInstance().activityStat == ActivityState.READY){
			if(System.currentTimeMillis() >= noticeEnoughTime + 3 * 1000){
				noticeEnoughTime = System.currentTimeMillis();
				if(ps != null && ps.size() > 0){
					for(Player p : ps){
						if(p != null && p.isOnline()){
							game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y));
							DiceManager.getInstance().playerLeave(p);
							if(DiceManager.logger.isWarnEnabled()){
								DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束:玩家不足] [ids:"+ids.size()+"] [人数:"+(ps==null?"null":ps.size())+"] [当前状态:"+DiceManager.getInstance().activityStat+"] ["+p.getLogString()+"]");
							}
						}
					}
				}
			}
			return;
		}
		
		//副本时间到
		if(fightLastTime > 0 && System.currentTimeMillis() >= fightLastTime){
			fightLastTime = System.currentTimeMillis();
			if(ps != null && ps.size() > 0){
				for(Player p : ps){
					if(p != null && p.isOnline()){
						p.sendError(Translate.副本时间到);
						game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y));
						if(DiceManager.logger.isWarnEnabled()){
							DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束:时间到] [ids:"+ids.size()+"] [人数:"+(ps==null?"null":ps.size())+"] [当前状态:"+DiceManager.getInstance().activityStat+"] ["+p.getLogString()+"]");
						}
					}
				}
			}
			endGame();
			clearGameInfo("副本时间到");
			return;
		}
		
		int bossNums = 0;
		int npcNums = 0;
		int playerNums = 0;
		for (LivingObject l : game.getLivingObjects()) {
			if (l instanceof Monster) {
				bossNums++;
			}else if(l instanceof NPC){
				npcNums++;
			}else if(l instanceof Player){
				playerNums++;
			}
		}
		
		switch (DiceManager.getInstance().activityStat) {
		case START_SIGN:
			//ps确保在场景，ids针对ps不能及时更新
			for(Player p : ps){
				if(p != null && p.isOnline()){
					if(!notices.contains(p.getId())){
						notices.add(p.getId());
						int overTime = (int)((DiceManager.getInstance().currConfig.getEndTime() - System.currentTimeMillis())/1000);
						if(overTime > 0){
							NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
							resp.setCountdownType(CountdownAgent.COUNT_TYPE_DICE);
							resp.setLeftTime(overTime);
							resp.setDescription(Translate.刷boss倒计时);
							p.addMessageToRightBag(resp);
							if(DiceManager.logger.isWarnEnabled()){
								DiceManager.logger.warn("[骰子仙居] [boss倒计时] [overTime:"+overTime+"] ["+p.getLogString()+"]"); 
							}
						}
					}
				}
			}
			
			if(DiceManager.getInstance().currConfig != null){
				if(System.currentTimeMillis() >= DiceManager.getInstance().currConfig.getEndTime()){
					fightLastTime = System.currentTimeMillis() + DiceManager.DICE_LAST_TIME;
					if(currLayer == 1){
						if(ps == null || ps.size() == 1 || getIds().size() <= 1){
							String pMess = "";
							endGame();
							if(ps != null && ps.size() == 1){
								noticeEnoughTime = System.currentTimeMillis();
								Player p = ps.get(0);
								p.sendError(Translate.当前人数不够);
								pMess = p.getLogString();
							}else{
								if(getIds().size() == 1){
									noticeEnoughTime = System.currentTimeMillis();
									try {
										Player p = PlayerManager.getInstance().getPlayer(getIds().get(0));
										p.sendError(Translate.当前人数不够);
										pMess = p.getLogString();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							if(DiceManager.logger.isWarnEnabled()){
								DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束:没有玩家参与或者人数不够] [人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [当前状态:"+DiceManager.getInstance().activityStat+"] ["+pMess+"]");
							}
							return;
						}
					}
					try{
						//有一种情况，
						if(getIds().size() <= 1){
							if (DiceManager.logger.isWarnEnabled()) {
								DiceManager.logger.warn("[骰子仙居] [刷新boss] [失败:没有玩家参与] [ids:{}] [currLayer:{}] [当前状态:{}] [playerNums:{}] [bossNums:{}] [npcNums:{}]", new Object[] { ids.size(),currLayer, DiceManager.getInstance().activityStat,playerNums,bossNums,npcNums });
							}
							endGame();
							return;
						}
						int refNums = DiceManager.getInstance().getBossNum(getIds().size());
						if(refNums <= 0){
							if (DiceManager.logger.isWarnEnabled()) {
								DiceManager.logger.warn("[骰子仙居] [刷新boss] [失败] [refNums:{}] [ids:{}] [currLayer:{}] [当前状态:{}] [playerNums:{}] [bossNums:{}] [npcNums:{}]", new Object[] { refNums,ids.size(),currLayer, DiceManager.getInstance().activityStat,playerNums,bossNums,npcNums });
							}
							endGame();
							return;
						}
						DiceManager.getInstance().refreshBoss(game, refNums);
						for(long id : getIds()){
							if(PlayerManager.getInstance().isOnline(id)){
								Player p = PlayerManager.getInstance().getPlayer(id);
								if(p != null && p.isOnline()){
									p.sendError(Translate.boss已刷新);
								}
							}
						}
						DiceManager.getInstance().activityStat = ActivityState.START_FIGHT;
						if (DiceManager.logger.isWarnEnabled()) {
							DiceManager.logger.warn("[骰子仙居] [刷新boss] [成功] [refNums:{}] [ids:{}] [currLayer:{}] [当前状态:{}] [playerNums:{}] [bossNums:{}] [npcNums:{}]", new Object[] { refNums,ids.size(),currLayer, DiceManager.getInstance().activityStat,playerNums,bossNums,npcNums });
						}
						game.heartbeat();
					}catch(Exception e){
						e.printStackTrace();
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [游戏心跳处理异常1] [人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [currLayer:"+currLayer+"] [当前状态:"+DiceManager.getInstance().activityStat+"]",e);
					}
				}
			}
			break;
		case START_FIGHT:	
			if(ps != null){
				
				for(Player p : ps){
					if(p != null && p.isOnline()){
						if(!noticesTimes.contains(p.getId())){
							noticesTimes.add(p.getId());
							int overTime = (int)((fightLastTime - System.currentTimeMillis())/1000);
							if(overTime > 0){
								NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
								resp.setCountdownType((byte)50);
								resp.setLeftTime(overTime);
								resp.setDescription(Translate.副本倒计时);
								p.addMessageToRightBag(resp);
								if(DiceManager.logger.isWarnEnabled()){
									DiceManager.logger.warn("[骰子仙居] [副本倒计时] [overTime:"+overTime+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"] ["+p.getLogString()+"]"); 
								}
							}
						}
					}
				}
				
				if(bossNums == 0){
					if(ps == null || ps.size() == 0){
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [没有玩家参与] [当前状态:"+DiceManager.getInstance().activityStat+"]");
						endGame();
						return;
					}
					if(ps.size() == 1){
						Player p = ps.get(0);
						game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.getInstance().mapName, DiceManager.getInstance().xypiont[0], DiceManager.getInstance().xypiont[1]));
						if(DiceManager.logger.isWarnEnabled()){
							DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束] [胜利者:"+p.getLogString()+"] [ps:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
						}
						DiceManager.getInstance().activityStat = ActivityState.SHOW_RESULT;
					}else{
						if(getIds().size() == 1){
							if(PlayerManager.getInstance().isOnline(getIds().get(0))){
								try {
									Player p = PlayerManager.getInstance().getPlayer(getIds().get(0));
									game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.getInstance().mapName, DiceManager.getInstance().xypiont[0], DiceManager.getInstance().xypiont[1]));
									if(DiceManager.logger.isWarnEnabled()){
										DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束] [胜利者2:"+p.getLogString()+"] [ps:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
									}
									DiceManager.getInstance().activityStat = ActivityState.SHOW_RESULT;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}else{
							DiceManager.getInstance().activityStat = ActivityState.DICE_POINT;
							startShookDiceTime = System.currentTimeMillis();
							pdatas.clear();
							for(Player p : ps){
								if(p != null && p.isOnline()){
									DicePointData data = getDicePointData(p);
									if(data != null){
										SHOOK_DICE2_RES res = new SHOOK_DICE2_RES(GameMessageFactory.nextSequnceNum(), data.getPoint(), data.getPointList(), 5);
										p.addMessageToRightBag(res);
										if(DiceManager.logger.isWarnEnabled()){
											DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知玩家摇骰子] [当前层数:"+currLayer+"] [ps:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"] [data:"+data+"]");
										}
									}else{
										if(DiceManager.logger.isWarnEnabled()){
											DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知玩家摇骰子] [失败] [当前层数:"+currLayer+"] [ps:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"] [data:"+data+"]");
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case DICE_POINT:
			if(ps == null || ps.size() == 0){
				DiceManager.logger.warn("[骰子仙居] [副本心跳] [没有玩家参与] [当前状态:"+DiceManager.getInstance().activityStat+"]");
				endGame();
				return;
			}
			try {
				if(System.currentTimeMillis() >= startShookDiceTime + 20 * 1000){
					DiceManager.getInstance().activityStat = ActivityState.SHOW_BILLBORAD;
					showBillboradTime = System.currentTimeMillis();
					DicePointData[] ds = getSortDicePointData();
					int outPlayer = getIds().size()/2;
					String mess = "";
					if(outPlayer > 0){
						mess = Translate.translateString(Translate.前几名进去, new String[][] { { Translate.COUNT_1, outPlayer+"" }});
					}
					
					List<DiceBillboardData> list = new ArrayList<DiceBillboardData>();
					for(int i=0;i<ds.length;i++){
						if(getIds().contains(ds[i].getPid())){
							int rank = i + 1;
							DiceBillboardData d = new DiceBillboardData();
							d.setRank(rank);
							d.setPlayerName(ds[i].getPlayerName());
							d.setPoint(ds[i].getPoint());
							list.add(d);
						}
					}
					
					for(int i=0;i<list.size();i++){
						DiceBillboardData d = list.get(i);
						if(d != null){
							long layerExps = DiceManager.expDatas.get(currLayer);
							if(PlayerManager.getInstance().isOnline(d.getPlayerName())){
								int rank = i + 1;
								Player p = PlayerManager.getInstance().getPlayer(d.getPlayerName());
								DICE_BILLBOARD_RES res = new DICE_BILLBOARD_RES(GameMessageFactory.nextSequnceNum(), rank, 15, mess, layerExps, list.toArray(new DiceBillboardData[]{}));
								p.addMessageToRightBag(res);
								long oldExps = p.getExp();
								p.addExp(layerExps, ExperienceManager.骰子仙居胜利者);
								if(DiceManager.logger.isWarnEnabled()){
									DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知显示排行] [层:"+currLayer+"] [经验:"+layerExps+"] [rank:"+rank+"] [ids:"+ids.size()+"] [outPlayer:"+outPlayer+"] [经验变化:"+oldExps+"-->"+p.getExp()+"]  [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"] ["+p.getLogString()+"]");
								}
							}else{
								if(DiceManager.logger.isWarnEnabled()){
									DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知显示排行] [玩家不在线] [层:"+currLayer+"] [经验:"+layerExps+"] [ids:"+ids.size()+"] [outPlayer:"+outPlayer+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"] [pName:"+d.getPlayerName()+"]");
								}
							}
						}
					}
					if(DiceManager.logger.isWarnEnabled()){
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知显示排行结束] [ds:"+ds.length+"] [list:"+list.size()+"] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [outPlayer:"+outPlayer+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(DiceManager.logger.isWarnEnabled()){
					DiceManager.logger.warn("[骰子仙居] [副本心跳] [通知显示排行结束] [异常] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]",e);
				}
			}
			break;
		case SHOW_BILLBORAD:
			if(ps == null || ps.size() == 0){
				DiceManager.logger.warn("[骰子仙居] [副本心跳] [没有玩家参与] [当前状态:"+DiceManager.getInstance().activityStat+"]");
				endGame();
				return;
			}
			if(System.currentTimeMillis() >= showBillboradTime + 15 * 1000){
				//传送
				Player winP = null;
				DicePointData[] ds = getSortDicePointData();
				int outPlayer = getIds().size()/2;
				List<Long> rList = new ArrayList<Long>();
				if(outPlayer > 0){
					for(int i=0;i<ds.length;i++){
						DicePointData d = ds[i];
						if(d != null){
							if(PlayerManager.getInstance().isOnline(d.getPlayerName())){
								try {
									if(getIds().contains(d.getPid())){
										Player p = PlayerManager.getInstance().getPlayer(d.getPlayerName());
										winP = p;
										if(i < outPlayer){
											currLayer++;
											DiceManager.getInstance().rewardLayer.put(p.getId(), currLayer);
											game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.getInstance().mapName, DiceManager.getInstance().xypiont[0], DiceManager.getInstance().xypiont[1]));
										}else{
											p.sendError(Translate.副本挑战失败);
											game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y));
											DiceManager.getInstance().playerLeave(p);
											rList.add(p.getId());
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				if(rList.size() > 0){
					ids.removeAll(rList);
				}
				
				if(getIds().size() == 1){
					//Player p = ps.get(0);
					//game.transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.getInstance().mapName, DiceManager.getInstance().xypiont[0], DiceManager.getInstance().xypiont[1]));
					DiceManager.logger.warn("[骰子仙居] [副本心跳] [副本结束:胜利者2] [人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [当前状态:"+DiceManager.getInstance().activityStat+"] ["+(winP!=null?winP.getLogString():null)+"]");
					DiceManager.getInstance().activityStat = ActivityState.SHOW_RESULT;
				}else{
					DiceManager.getInstance().activityStat = ActivityState.START_SIGN;
				}
				if(DiceManager.logger.isWarnEnabled()){
					DiceManager.logger.warn("[骰子仙居] [副本心跳] [排行结束，进入下一层] [当前状态:"+DiceManager.getInstance().activityStat+"] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [ds:"+ds.length+"] [rList:"+rList.size()+"] [outPlayer:"+outPlayer+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
				}
			}
			break;
		case SHOW_RESULT:
			if(getIds().size() == 1){
				FlopCaijiNpc npc = (FlopCaijiNpc)MemoryNPCManager.getNPCManager().createNPC(1000004);
				if(npc == null){
					DiceManager.logger.warn("[骰子仙居] [副本心跳] [刷箱子失败：npc不存在] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
					return;
				}
				long aeIds [] = new long[DiceManager.getInstance().winArticles.length];
				int index = 0;
				for(String name : DiceManager.getInstance().winArticles){
					Article article = ArticleManager.getInstance().getArticleByCNname(name);
					if(article == null){
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [获取奖励数据出错] [物品不存在:"+name+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [ids:"+ids.size()+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
						continue;
					}
					ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, article.getColorType());
					if(ae == null){
						try {
							int colorType = article.getColorType();
							if(name.equals("古董")){
								colorType = 3;
							}else if(name.equals("人物经验")){
								colorType = 4;
							}
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.骰子活动物品, null, colorType);
							ae.setBinded(true);
						} catch (Exception e) {
							e.printStackTrace();
							DiceManager.logger.warn("[骰子仙居] [副本心跳] [创建临时物品出错] [物品不存在:"+name+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [ids:"+ids.size()+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
						}
					}
					aeIds[index] = ae.getId();
					index++;
				}
				Player p = null;
				try {
					p = PlayerManager.getInstance().getPlayer(getIds().get(0));
					List<Long> playersList = new ArrayList<Long>();
					playersList.add(p.getId());
					npc.setX(DiceManager.getInstance().xypiont[0]);
					npc.setY(DiceManager.getInstance().xypiont[1]);
					npc.setPlayersList(playersList);
					npc.setAllCanPickAfterTime(1000);
					npc.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					npc.setEndTime(SystemTime.currentTimeMillis() + 60*1000);
					ResourceManager.getInstance().getAvata(npc);
					DICE_END_NOTICE_RES res = new DICE_END_NOTICE_RES(GameMessageFactory.nextSequnceNum(),currLayer,DiceManager.winExps,aeIds,DiceManager.getInstance().winNums);
					npc.setMessage(res);
					game.addSprite(npc);
					if(DiceManager.logger.isWarnEnabled()){
						DiceManager.logger.warn("[骰子仙居] [副本心跳] [刷箱子] ["+(p==null?"nul":p.getLogString())+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				if(DiceManager.logger.isWarnEnabled()){
					DiceManager.logger.warn("[骰子仙居] [副本心跳] [刷箱子] [失败:人数不正确] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [ids:"+ids.size()+"] [newids:"+getIds().size()+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
				}
			}
			DiceManager.getInstance().activityStat = ActivityState.GAME_OVER;
			showResultTime = System.currentTimeMillis();
			break;
		case GAME_OVER:
			if(System.currentTimeMillis() >= showResultTime + 20 * 1000){
				if(DiceManager.getInstance().rewardIds == null || DiceManager.getInstance().rewardIds.size() == 0){
					if(getIds().size() == 1){
						if(PlayerManager.getInstance().isOnline(getIds().get(0))){
							try {
								Player player = PlayerManager.getInstance().getPlayer(getIds().get(0));
								long oldExps = player.getExp();
								player.addExp(DiceManager.winExps, ExperienceManager.骰子仙居胜利者);
								
								List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
								for(int i=1;i<DiceManager.getInstance().winArticles.length;i++){
									String name = DiceManager.getInstance().winArticles[i];
									Article article = ArticleManager.getInstance().getArticleByCNname(name);
									if(article == null){
										DiceManager.logger.warn("[骰子仙居] [胜利者点箱子发放奖励1] [获取奖励数据出错] [物品不存在:"+name+"] ["+player.getLogString()+"]");
										continue;
									}
									try {
										int colorType = article.getColorType();
										if(name.equals("古董")){
											colorType = 3;
										}
										ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.骰子活动物品, player, colorType, 1, true); 
										if(ae != null){
											aes.add(ae);
										}
									} catch (Exception e) {
										e.printStackTrace();
										DiceManager.logger.warn("[骰子仙居] [胜利者点箱子发放奖励1] [创建临时物品出错] [物品不存在:"+name+"] ["+player.getLogString()+"]");
									}
								}
								if(aes.size() > 0){
									if (player.putAll(aes.toArray(new ArticleEntity[]{}), "胜利者点箱子发放奖励")) {
									} else {
										try {
											MailManager.getInstance().sendMail(player.getId(), aes.toArray(new ArticleEntity[]{}),  Translate.骰子奖励通过邮件发送, Translate.骰子奖励通过邮件发送, 0, 0, 0, "胜利者点箱子发放奖励");
											player.sendError(Translate.骰子奖励通过邮件发送);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								clearGameInfo("游戏结束");
								if(DiceManager.logger.isWarnEnabled()){
									DiceManager.logger.warn("[骰子仙居] [获得经验] [aes:"+aes.size()+"] [经验变化:"+oldExps+"-->"+player.getExp()+"] ["+player.getLogString()+"]");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				endGame();
			}
			break;
		}
		if(DiceManager.printTestDiceThreadLog){
			DiceManager.logger.warn("[骰子仙居] [副本心跳] [当前状态:"+DiceManager.getInstance().activityStat+"] [地图人数:"+(ps==null?"null":ps.size())+"] [ids:"+ids.size()+"] [当前层数:"+currLayer+"] [playerNums:"+playerNums+"] [bossNums:"+bossNums+"] [npcNums:"+npcNums+"]");
		}
	}
	
	public DicePointData[] getSortDicePointData(){
		List<DicePointData> list = new ArrayList<DicePointData>();
		Iterator<DicePointData> it = pdatas.values().iterator();
		while(it.hasNext()){
			DicePointData d = it.next();
			if(d != null){
				if(!getIds().contains(d.getPid())){
					continue;
				}
				list.add(d);
			}
		}
		DicePointData datas [] = list.toArray(new DicePointData[]{});
		Arrays.sort(datas, new Comparator<DicePointData>() {
			@Override
			public int compare(DicePointData o1, DicePointData o2) {
				return o2.getPoint() - o1.getPoint();
			}
		});
		return datas;
	}
	
	public DicePointData getDicePointData(Player player){
		int points [] = new int[3];
		int pointTotle = 0;
		for(int i=0;i<points.length;i++){
			int p = random.nextInt(6);
			points[i] = p+1;
			pointTotle+=p+1;
		}
		DicePointData data = new DicePointData(player.getId(), player.getName(),pointTotle,points);
		pdatas.put(player.getName(), data);
		return data;
	}
	
	public void addDiceGame(Player player){
		if(!containPlayer(player)){
			ids.add(player.getId());
		}
	}
	
	public boolean containPlayer(Player player){
		for(long id : ids){
			if(id == player.getId()){
				return true;
			}
		}
		return false;
	}
	
	public void endGame(){
		if(DiceManager.getInstance().startNoticeTime < System.currentTimeMillis()){
			DiceManager.getInstance().startNoticeTime = System.currentTimeMillis();
		}
		DiceManager.getInstance().activityStat = ActivityState.READY;
	}

	public void clearGameInfo(String reason){
		DiceManager.getInstance().rewardLayer.clear();
		DiceManager.getInstance().signList.clear();
		currLayer = 1;
		notices.clear();
		ids.clear();
		fightLastTime = 0;
		noticesTimes.clear();
		pdatas.clear();
		if(game != null){
			game.removeAllMonster();
			game.removeAllNpc();
		}
		DiceManager.getInstance().rewardIds.clear();
		if(DiceManager.logger.isWarnEnabled()){
			DiceManager.logger.warn("[骰子仙居] [清理任务] [reason:"+reason+"] ["+DiceManager.getInstance().activityStat+"]");
		}
	}
	
	public int getBossNums(){
		if(game != null){
			return game.getBosss().size();
		}
		return 0;
	}
	
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public static long getSleeptime() {
		return sleeptime;
	}

	public static void setSleeptime(long sleeptime) {
		DiceGame.sleeptime = sleeptime;
	}

	public Game getGame() {
		return game;
	}

	public int getCurrLayer() {
		return currLayer;
	}

	public void setCurrLayer(int currLayer) {
		this.currLayer = currLayer;
	}

	public List<Long> getNotices() {
		return notices;
	}

	public void setNotices(List<Long> notices) {
		this.notices = notices;
	}
	
}
