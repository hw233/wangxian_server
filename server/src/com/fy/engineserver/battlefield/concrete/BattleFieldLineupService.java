package com.fy.engineserver.battlefield.concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.core.PlayerMessagePair;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.event.TransferGameEvent;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_ForbidToGoToZhanChang;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.lineup.LineupManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Battle_WaittingConfirm_Enter;
import com.fy.engineserver.menu.Option_Battle_WaittingConfirm_Leave;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.BATTLEFIELD_ACTION_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_BATTLEFIELDLIST_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.queue.DefaultQueue;

/**
 * 战场排队系统
 * 
 */
public class BattleFieldLineupService implements Runnable{

//	static Logger logger = Logger.getLogger(BattleField.class);
	static Logger logger = LoggerFactory.getLogger(BattleField.class);
	
	static BattleFieldLineupService self = null;
	
	/**
	 * 跨服模式
	 */
	protected boolean crossMode;
	
	protected long checkTime = 5000;
	
	public LruMapCache forbidMap = null;
	
	public static BattleFieldLineupService getInstance(){
		return self;
	}
	
	//排队实体
	public static class LineItem{
		public ArrayList<Player> players = new ArrayList<Player>();
		public long lineTime;
		public String battleName;
	}
	
	//消息队列
	protected DefaultQueue queue = new DefaultQueue(40960);
	
	//线程
	Thread thread;
	
	//队列
	LinkedList<LineItem> itemList = new LinkedList<LineItem>();
	
	public LineItem[] getLineItems(){
		return itemList.toArray(new LineItem[0]);
	}
	
	public Player[] getPlayersByBattleFieldInfo(BattleFieldInfo bi,int side){
		ArrayList<Player> al = new ArrayList<Player>();
		for(LineItem li : itemList){
			if(li == null) {
				continue;
			}
			if(li.battleName.equals(bi.getName())){
				for(Player p : li.players){
					if(bi.getBattleFightingType() == BattleField.BATTLE_FIGHTING_TYPE_对战){
						if(p.getCountry() == 1 && side == BattleField.BATTLE_SIDE_A){
							al.add(p);
						}
						if(p.getCountry() == 2 && side == BattleField.BATTLE_SIDE_B){
							al.add(p);
						}
					}else{
						al.add(p);
					}
				}
			}
		}
		return al.toArray(new Player[0]);
	}
	
	public static class WaittingItem{
		public Player player;
		public long startTime;
		public BattleField bf;
		public int side;
	}
	
	LinkedList<WaittingItem> waitingItemList = new LinkedList<WaittingItem>();
	
	public WaittingItem[] getWaittingItems(){
		return waitingItemList.toArray(new WaittingItem[0]);
	}
	/**
	 * 得到某个战场正在进入的用户类别
	 * @param bf
	 * @param side
	 * @return
	 */
	public WaittingItem[] getWaittingItemByBattleField(BattleField bf,int side){
		ArrayList<WaittingItem> al = new ArrayList<WaittingItem>();
		for(WaittingItem wi : waitingItemList){
			if(wi == null) {
				continue;
			}
			if(wi.bf == bf && wi.side == side){
				al.add(wi);
			}
		}
		return al.toArray(new WaittingItem[0]);
	}
	
	public boolean playerInWaittingList(Player p){
		for(WaittingItem wi : waitingItemList){
			if(wi == null)
				continue;
			if(wi.player == p || wi.player.getId() == p.getId()){
				return true;
			}
		}
		return false;
	}
	
	public WaittingItem getPlayerWaitingItem(Player p, String battlefieldName) {
		for(WaittingItem wi : waitingItemList){
			if(wi == null)
				continue;
			if((wi.player == p || wi.player.getId() == p.getId()) && wi.bf.getName().equals(battlefieldName)){
				return wi;
			}
		}
		return null;
	}
	
	/**
	 * 玩家进入了跨服，替换已经存在的排队中的player
	 * @param player
	 */
	public void notifyPlayerEnterCrossServer(Player player) {
		for(WaittingItem wi : waitingItemList){
			if(wi.player.getId() == player.getId()){
				Player p1 = wi.player;
				player.setInBattleField(p1.isInBattleField());
				player.setBattleField(p1.getBattleField());
				player.setBattleFieldSide((byte)wi.side);
				Point point = wi.bf.getRandomBornPoint(p1.getBattleFieldSide());
				player.setEnterBattleFieldPoint(wi.bf.getGame().getGameInfo().getName(),point.x,point.y);
				wi.player = player;
//				logger.debug("[通知玩家进入跨服战场] ["+wi.bf.getName()+"] ["+player.getName()+"] ["+player.getId()+"]");
				if(logger.isDebugEnabled()){
					logger.debug("[通知玩家进入跨服战场] [{}] [{}] [{}]", new Object[]{wi.bf.getName(),player.getName(),player.getId()});
				}
				break;
			}
		}
	}
	
	public void init() throws Exception{
		
		
		
		forbidMap = new LruMapCache(32*1024*1024, 30*60*1000);
		
		thread = new Thread(this,Translate.text_1850);
		thread.start();
		
		self = this;
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 禁止一个玩家排队
	 * @param player
	 */
	public void forbidPlayerForLeave(Player player) {
		forbidMap.__put(player.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 15*60*1000);
//		logger.debug("[玩家离开战场15分钟惩罚] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
		if(logger.isDebugEnabled()){
			logger.debug("[玩家离开战场15分钟惩罚] [{}] [{}] [{}]", new Object[]{player.getId(),player.getUsername(),player.getName()});
		}
	}
	
	/**
	 * 得到玩家禁止排队的时间
	 * @param player
	 * @return
	 */
	public long getForbidTime(Player player) {
		Long ftime = (Long)forbidMap.__get(player.getId());
		if(ftime != null) {
			long min = ftime - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			min = min/(60*1000);
			if(min > 0) {
				return min+1;
			} else {
				forbidMap.remove(player.getId());
//				logger.debug("[玩家解禁] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
				if(logger.isDebugEnabled()){
					logger.debug("[玩家解禁] [{}] [{}] [{}]", new Object[]{player.getId(),player.getUsername(),player.getName()});
				}
			}
		}
		return 0;
	}
	
	public long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	public boolean isCrossMode() {
		return crossMode;
	}

	public void setCrossMode(boolean crossMode) {
		this.crossMode = crossMode;
	}

	public void pushMessageToQueue(PlayerMessagePair m){
		queue.push(m);
	}
	
	public void pushWaitingItemToQueue(WaittingItem wi){
		queue.push(wi);
	}
	
	/**
	 * 判断玩家是否在排队，只判断排队。不判断等待进入。
	 * @param p
	 * @return
	 */
	public boolean isPlayerInBattleLineup(Player p){
		LineItem items[] = this.getLineItems();
		for(int i = 0 ; i < items.length ; i++){
			for(Player pp : items[i].players){
				if(pp.getId() == p.getId()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 战场排队心跳函数
	 */
	private void heartbeat(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		handleMessage();
		if(logger.isInfoEnabled()){
//			logger.info("[handleMessage] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
			if(logger.isInfoEnabled())
				logger.info("[handleMessage] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
		}
		t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		doLineUp();
		if(logger.isInfoEnabled()){
//			logger.info("[doLineUp] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
			if(logger.isInfoEnabled())
				logger.info("[doLineUp] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
		}
	}
	
	private void handleMessage(){
		try{
		BattleFieldManager bfm = BattleFieldManager.getInstance();
		LinkedList<Object> messages = new LinkedList<Object>();
		while (!queue.isEmpty()) {
			messages.add(queue.pop());
		}
		Iterator<Object> it = messages.iterator();
		while (it.hasNext()) {

			Object obj = it.next();
			
			if(obj instanceof PlayerMessagePair){
				PlayerMessagePair pair = (PlayerMessagePair)obj;
				if(pair.message instanceof BATTLEFIELD_ACTION_REQ){
					BATTLEFIELD_ACTION_REQ req = (BATTLEFIELD_ACTION_REQ)pair.message;
					String name = req.getName();
					BattleFieldInfo bi = bfm.getBattleFieldInfoByName(name);
					if(bi == null){
						HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1851+name+Translate.text_1852);
						pair.player.addMessageToRightBag(req2);
						
						if(logger.isInfoEnabled()){
//							logger.info("[战场排队服务] [处理指令] [失败：指定的战场不存在] ["+pair.player.getName()+"] ["+name+"] [排队条目："+itemList.size()+"]");
							if(logger.isInfoEnabled())
								logger.info("[战场排队服务] [处理指令] [失败：指定的战场不存在] [{}] [{}] [排队条目：{}]", new Object[]{pair.player.getName(),name,itemList.size()});
						}
					}else{
						//0-个人排队，1-个人离开排队，2-小组排队，3-离开战场
						if(req.getActionType() == 0){
							if(this.isCrossMode()) {
								HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1853);
								pair.player.addMessageToRightBag(req2);
							} else {
								doPlayerLineup(pair.player,bi);
							}
						}else if(req.getActionType() == 1){
							doPlayerLeaveLine(pair.player,bi);
						}else if(req.getActionType() == 2){
							doTeamLineup(pair.player,bi);
						}else if(req.getActionType() == 3){
							doLeaveBattleField(pair.player,bi);
						}
					}
				}
			}else if(obj instanceof WaittingItem){
				//
				WaittingItem wi = (WaittingItem)obj;
				HINT_REQ req2 = null;
				if(this.waitingItemList.contains(wi) == false){
					req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1854);
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [进入战场] [失败：已过期] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [进入战场] [失败：已过期] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
					}
				}else if(wi.bf.isOpen() == false || wi.bf.getState() == BattleField.STATE_CLOSE){
					req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1855);
					
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [进入战场] [失败：战场已结束] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [进入战场] [失败：战场已结束] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
					}
				}else if(wi.player.getCurrentGame() == null && !crossMode){
					//跨服模式不判断用户是否在游戏中
					
					req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1856);
					
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [进入战场] [失败：正在跳地图] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [进入战场] [失败：正在跳地图] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
					}
				}else if(wi.player.getLevel() < wi.bf.getMinPlayerLevel() || wi.player.getLevel() > wi.bf.getMaxPlayerLevel()){
					req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1857);
					
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [进入战场] [失败：您的级别不符合战场的要求] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [进入战场] [失败：您的级别不符合战场的要求] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
					}
				} else if(wi.player.isDeath()){
					boolean alive = false;
					
					if(!alive) {
						req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1858);
						
						if(logger.isInfoEnabled()){
//							logger.info("[战场排队服务] [进入战场] [失败：您已死亡] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
							if(logger.isInfoEnabled())
								logger.info("[战场排队服务] [进入战场] [失败：您已死亡] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
						}
					}
				} else if(wi.player.isOnline() == false && !crossMode){
					
						req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1859);
						
						if(logger.isInfoEnabled()){
//							logger.info("[战场排队服务] [进入战场] [失败：您已掉线] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
							if(logger.isInfoEnabled())
								logger.info("[战场排队服务] [进入战场] [失败：您已掉线] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
						}
				} else if(wi.bf.getBattleFightingType() == BattleField.BATTLE_FIGHTING_TYPE_混战){
					
					if(wi.bf.getPlayers().length >= wi.bf.getMaxPlayerNumOnOneSide()){
						req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1860);
						
						if(logger.isInfoEnabled()){
//							logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
							if(logger.isInfoEnabled())
								logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
						}
					}else{
						playerEnterBattleField(wi);
						//进入战场
					}
					
				} else{
					if(wi.player.getCountry() == 1){
						
						if(wi.bf.getPlayersBySide(BattleField.BATTLE_SIDE_A).length >= wi.bf.getMaxPlayerNumOnOneSide()){
							req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1860);
							
							if(logger.isInfoEnabled()){
//								logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
								if(logger.isInfoEnabled())
									logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
							}
						}else{
							playerEnterBattleField(wi);
							//进入战场
						}
					}else if(wi.player.getCountry() == 2){
						if(wi.bf.getPlayersBySide(BattleField.BATTLE_SIDE_B).length >= wi.bf.getMaxPlayerNumOnOneSide()){
							req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1860);
							
							if(logger.isInfoEnabled()){
//								logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [时间："+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)+"ms前]");
								if(logger.isInfoEnabled())
									logger.info("[战场排队服务] [进入战场] [失败：战场人数已满] [{}] [{}] [{}] [时间：{}ms前]", new Object[]{wi.player.getName(),wi.bf.getName(),wi.bf.getId(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()- wi.startTime)});
							}
						}else{
							playerEnterBattleField(wi);
							//进入战场
						}
					}
				}
				if(req2 != null) {
					if(!crossMode) {
						wi.player.addMessageToRightBag(req2);
					} 
				}
			}else{//其他类型消息
				//
			}
		}
		}catch(Exception e){
			logger.error("处理消息出异常",e);
		}
	}
	
	/**
	 * 玩家正在进入战场
	 * @param wi
	 */
	private void playerEnterBattleField(WaittingItem wi){
		if(!crossMode) {
			Player p = wi.player;
			BattleField bf = wi.bf;
			p.setBattleField(bf);
			p.setBattleFieldSide((byte)wi.side);
			
			Point point = bf.getRandomBornPoint(p.getBattleFieldSide());
			if(p.getCurrentGame() != null) {
				p.setEnterBattleFieldPoint(p.getCurrentGame().getGameInfo().getName(), p.getX(), p.getY());
			} else {
				p.setEnterBattleFieldPoint(bf.getGame().getGameInfo().getName(),point.x,point.y);
			}
			
			p.getCurrentGame().transferGame(p, new TransportData(0,0,0,0,bf.getGame().getGameInfo().getName(),point.x,point.y));
			
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [玩家进入战场] ["+p.getUsername()+"] ["+p.getName()+"] ["+bf.getName()+"] ["+bf.getId()+"] [side:"+p.getBattleFieldSide()+"] ["+bf.getGame().getGameInfo().getName()+"] [出生点("+point.x+","+point.y+")]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [玩家进入战场] [{}] [{}] [{}] [{}] [side:{}] [{}] [出生点({},{})]", new Object[]{p.getUsername(),p.getName(),bf.getName(),bf.getId(),p.getBattleFieldSide(),bf.getGame().getGameInfo().getName(),point.x,point.y});
			}
			
			//这个地方不需要清空wi
		} 
	}
	
	/**
	 * 玩家离开队列
	 * @param player
	 */
	public void playerDeLineup(Player player) {
//		Iterator<WaittingItem> it2 = waitingItemList.iterator();
//		while(it2.hasNext()){
//			WaittingItem wi = it2.next();
//			Player p = wi.player;
//			if(p.getId() == player.getId()) {
//				it2.remove();
//			}
//		}
		for(int i=itemList.size()-1; i>=0; i--) {
			LineItem item = itemList.get(i);
			ArrayList<Player> plist = item.players;
			for(int k=plist.size()-1; k>=0; k--) {
				if(player.getId() == plist.get(k).getId()) {
					plist.remove(k);
				}
			}
			if(plist.size() == 0) {
				itemList.remove(i);
			}
		}
	}
	
	private void checkLineupItem(){
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		Iterator<WaittingItem> it2 = waitingItemList.iterator();
		while(it2.hasNext()){
			WaittingItem wi = it2.next();
			Player p = wi.player;
			if(p.isInBattleField()){ 
				it2.remove();
				if(logger.isDebugEnabled()){
//					logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，玩家已进入战场] ["+p.getUsername()+"] ["+p.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
					if(logger.isDebugEnabled())
						logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，玩家已进入战场] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),wi.bf.getName(),wi.bf.getId()});
				}
				continue;
			}
			if(p.isOnline() == false){
				boolean online = false;
				if(crossMode) {
					try {
						Player pp = PlayerManager.getInstance().getPlayer(p.getId());
						if(p != pp)
							p = pp;
						online = true;
					} catch (Exception e) {
						online = false;
					}	
				}
				if(!online) {
					it2.remove();
					if(logger.isDebugEnabled()){
//						logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，玩家不在线或者已不在跨服内存中] ["+p.getUsername()+"] ["+p.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
						if(logger.isDebugEnabled())
							logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，玩家不在线或者已不在跨服内存中] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),wi.bf.getName(),wi.bf.getId()});
					}
					continue;
				}
			}
			if(now - wi.startTime > 60000){
				it2.remove();
				if(logger.isDebugEnabled()){
//					logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，等待1分钟超时] ["+p.getUsername()+"] ["+p.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
					if(logger.isDebugEnabled())
						logger.debug("[战场排队服务] [心跳] [检查等待队列] [清除项目，等待1分钟超时] [{}] [{}] [{}] [{}]", new Object[]{p.getUsername(),p.getName(),wi.bf.getName(),wi.bf.getId()});
				}
				continue;
			}
		}
		
		for(LineItem li : itemList){
			if(li == null) {
				continue;
			}
			Iterator<Player> it = li.players.iterator();
			while(it.hasNext()){
				Player p = it.next();
				if(p.isInBattleField()){
					it.remove();
					if(logger.isDebugEnabled()){
//						logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家已进入战场] ["+p.getUsername()+"] ["+p.getName()+"] ["+li.battleName+"] [排队时间："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)+"ms]");
						if(logger.isDebugEnabled())
							logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家已进入战场] [{}] [{}] [{}] [排队时间：{}ms]", new Object[]{p.getUsername(),p.getName(),li.battleName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)});
					};
					continue;
				}
				if(p.isOnline() == false){
					boolean online = false;
					if(crossMode) {
						try {
							Player pp = PlayerManager.getInstance().getPlayer(p.getId());
							if(p != pp)
								p = pp;
							online = true;
						} catch (Exception e) {
							online = false;
						}	
					}
					if(!online) {
						it.remove();
						if(logger.isDebugEnabled()){
//							logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家不在线或者已不在跨服内存中] ["+p.getUsername()+"] ["+p.getName()+"] ["+li.battleName+"] [排队时间："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)+"ms]");
							if(logger.isDebugEnabled())
								logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家不在线或者已不在跨服内存中] [{}] [{}] [{}] [排队时间：{}ms]", new Object[]{p.getUsername(),p.getName(),li.battleName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)});
						}
						continue;
					}
				}
				if(playerInWaittingList(p)){
					it.remove();
					if(logger.isDebugEnabled()){
//						logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家在等待队列] ["+p.getUsername()+"] ["+p.getName()+"] ["+li.battleName+"] [排队时间："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)+"ms]");
						if(logger.isDebugEnabled())
							logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，玩家在等待队列] [{}] [{}] [{}] [排队时间：{}ms]", new Object[]{p.getUsername(),p.getName(),li.battleName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)});
					}
					continue;
				}
				BattleFieldManager bfm = BattleFieldManager.getInstance();
				BattleFieldInfo bi = bfm.getBattleFieldInfoByName(li.battleName);
				if(bi == null){
					it.remove();
					if(logger.isDebugEnabled()){
//						logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，战场信息已经不存在] ["+p.getUsername()+"] ["+p.getName()+"] ["+li.battleName+"] [排队时间："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)+"ms]");
						if(logger.isDebugEnabled())
							logger.debug("[战场排队服务] [心跳] [检查排队队列] [清除项目，战场信息已经不存在] [{}] [{}] [{}] [排队时间：{}ms]", new Object[]{p.getUsername(),p.getName(),li.battleName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - li.lineTime)});
					}
				}
			}
		}
		
		Iterator<LineItem> it = itemList.iterator();
		while(it.hasNext()){
			LineItem li = it.next();
			if(li == null || li.players.size() == 0){
				try {
					it.remove();
				} catch(Exception e) {
					logger.error("LinkedList在remove Object 时状态错误", e);
				}
			}
		}
		
		
	}
	//正在的排队
	private void doLineUp(){
		
		checkLineupItem();
		
		//将战场和排队匹配起来
		
		BattleFieldManager bfm = BattleFieldManager.getInstance();
		BattleFieldInfo bis[] = bfm.getBattleFieldInfos();
		for(int i = 0 ; i < bis.length ; i++){
			BattleFieldInfo bi = bis[i];
			ArrayList<LineItem> al = new ArrayList<LineItem>();
			
			for(LineItem li : itemList){
				if(li == null) 
					continue;
				if(li.battleName.equals(bi.getName())){
					al.add(li);
				}
			}
			doLineUp(bi,al);
			
			checkLineupItem();
		}
	}
	
	/**
	 * 针对某个战场，排队
	 * 
	 * 优先级：
	 * 1. 用户死亡，用户切磋，用户在切地图中不参与排队
	 * 2. 
	 * 
	 * @param bi
	 * @param al
	 */
	private void doLineUp(BattleFieldInfo bi,ArrayList<LineItem> al){}
	
	/**
	 * 通知用户进入战场
	 * @param wi
	 */
	private void notifyPlayerEnterBattleField(WaittingItem wi){
		if(!crossMode) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(60);
			mw.setTitle(Translate.text_215);
			mw.setDescriptionInUUB(wi.bf.getName()+Translate.text_1862);
			
			Option_Battle_WaittingConfirm_Enter op = new Option_Battle_WaittingConfirm_Enter(wi);
			op.setText(Translate.text_1863);
			Option_Battle_WaittingConfirm_Leave op2 = new Option_Battle_WaittingConfirm_Leave(wi);
			op2.setText(Translate.text_364);
			
			mw.setOptions(new Option[]{op,op2});
			
//			String optionTexts[] = new String[]{Translate.translate.text_1863,Translate.translate.text_364};
			
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),mw.getOptions());
			wi.player.addMessageToRightBag(req);
			
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [通知玩家准备进入战场] ["+wi.player.getUsername()+"] ["+wi.player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"] [side:"+wi.side+"] ["+wi.bf.getGame().getGameInfo().getName()+"]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [通知玩家准备进入战场] [{}] [{}] [{}] [{}] [side:{}] [{}]", new Object[]{wi.player.getUsername(),wi.player.getName(),wi.bf.getName(),wi.bf.getId(),wi.side,wi.bf.getGame().getGameInfo().getName()});
			}
		} else {
			
		}
	}
	
	
	/**
	 * 个人排队
	 * @param player
	 * @param bi
	 */
	public void doPlayerLineup(Player player,BattleFieldInfo bi){
		
		//如果已经在副本队列中，那么不允许加入战场队列
		LineupManager lum = LineupManager.getInstance();
		if(lum.isPlayerInQueue(player)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1864);
			player.addMessageToRightBag(req);
			return;
		}
		
		if(player.getBattleField() != null && player.isInBattleField()){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1865);
			player.addMessageToRightBag(req);
			return;
		}
		
		
		if(bi.getMinPlayerLevel() > player.getLevel() || bi.getMaxPlayerLevel() < player.getLevel()){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1866);
			player.addMessageToRightBag(req);
			return;
		}
		
		if(bi.isBangpaiFlag()){
			if(player.getGangName() != null && player.getGangName().equals(bi.getGangNameForA())){
				
			}else if(player.getGangName() != null && player.getGangName().equals(bi.getGangNameForB())){
				
			}else{
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1867);
				player.addMessageToRightBag(req);
				return;
			}
		}
		
		boolean buffForbid = false;
		int forbidTime = 0;
		Buff buffs[] = player.getActiveBuffs();
		for(Buff buff : buffs){
			if(buff instanceof Buff_ForbidToGoToZhanChang){
				Buff_ForbidToGoToZhanChang b = (Buff_ForbidToGoToZhanChang)buff;
				if(bi.getForbidBuffName().equals(b.getBattleFieldName())){
					buffForbid = true;
					forbidTime = (int)(b.getInvalidTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis())/60000;
					if(forbidTime == 0) forbidTime = 1;
				}
			}
		}
		if(buffForbid){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1868+forbidTime+Translate.text_1869);
			player.addMessageToRightBag(req);
			return;
		}
		
		//
		Iterator<WaittingItem> it2 = waitingItemList.iterator();
		while(it2.hasNext()){
			WaittingItem wi = it2.next();
			if(wi == null)
				continue;
			if(wi.player == player){
				it2.remove();
				
				if(logger.isInfoEnabled()){
//					logger.info("[战场排队服务] [处理消息] [检查等待队列] [清除项目，重新排队] ["+player.getUsername()+"] ["+player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
					if(logger.isInfoEnabled())
						logger.info("[战场排队服务] [处理消息] [检查等待队列] [清除项目，重新排队] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getName(),wi.bf.getName(),wi.bf.getId()});
				}
			}
		}
		
		
		for(LineItem li : itemList){
			if(li == null)
				continue;
			if(li.players.contains(player) && li.battleName.equals(bi.getName())){
				//玩家已经在排队中
				if(logger.isInfoEnabled()){
//					logger.info("[战场排队服务] [个人加入排队] [玩家已经在队列中] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
					if(logger.isInfoEnabled())
						logger.info("[战场排队服务] [个人加入排队] [玩家已经在队列中] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
				}
				return;
			}
		}
		LineItem li = new LineItem();
		li.battleName = bi.getName();
		li.players.add(player);
		li.lineTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		itemList.add(li);
		
		if(logger.isInfoEnabled()){
//			logger.info("[战场排队服务] [个人加入排队] [成功] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
			if(logger.isInfoEnabled())
				logger.info("[战场排队服务] [个人加入排队] [成功] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
		}
		
		QUERY_BATTLEFIELDLIST_RES res = new QUERY_BATTLEFIELDLIST_RES(GameMessageFactory.nextSequnceNum(),getAvailableBattleItems(player));
		player.addMessageToRightBag(res);
		Player.sendPlayerAction(player, PlayerActionFlow.行为类型_战场排队, bi.getName(), 0, new java.util.Date(), true);
	}
	
	public boolean doCrossPlayerLineup(Player player,BattleFieldInfo bi){
		return false;
	}
	
	/**
	 * 个人离开排队
	 * @param player
	 * @param bi
	 */
	public void doPlayerLeaveLine(Player player,BattleFieldInfo bi){
		for(LineItem li : itemList){
			if(li == null)
				continue;
			if(li.players.contains(player) && li.battleName.equals(bi.getName())){
				//玩家已经在排队中
				li.players.remove(player);
				
				if(logger.isInfoEnabled()){
//					logger.info("[战场排队服务] [个人离开排队] [成功] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
					if(logger.isInfoEnabled())
						logger.info("[战场排队服务] [个人离开排队] [成功] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
				}
				
			}
		}
		
		Iterator<WaittingItem> it2 = waitingItemList.iterator();
		while(it2.hasNext()){
			WaittingItem wi = it2.next();
			if(wi == null)
				continue;
			if(wi.player == player){
				it2.remove();
				
				if(logger.isDebugEnabled()){
//					logger.debug("[战场排队服务] [个人离开排队] [检查等待队列] [清除项目，玩家离开排队] ["+player.getUsername()+"] ["+player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
					if(logger.isDebugEnabled())
						logger.debug("[战场排队服务] [个人离开排队] [检查等待队列] [清除项目，玩家离开排队] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getName(),wi.bf.getName(),wi.bf.getId()});
				}
			}
		}
		
		Iterator<LineItem> it = itemList.iterator();
		while(it.hasNext()){
			LineItem li = it.next();
			if(li == null)
				continue;
			if(li.players.size() == 0){
				it.remove();
			}
		}
		
		QUERY_BATTLEFIELDLIST_RES res = new QUERY_BATTLEFIELDLIST_RES(GameMessageFactory.nextSequnceNum(),getAvailableBattleItems(player));
		player.addMessageToRightBag(res);
	}
	
	public boolean doCrossPlayerLeaveLine(Player player,BattleFieldInfo bi) {
		for(LineItem li : itemList){
			if(li == null)
				continue;
			if(li.players.contains(player) && li.battleName.equals(bi.getName())){
				//玩家已经在排队中
				li.players.remove(player);
				
				if(logger.isInfoEnabled()){
//					logger.info("[跨服战场排队服务] [个人离开排队] [成功] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
					if(logger.isInfoEnabled())
						logger.info("[跨服战场排队服务] [个人离开排队] [成功] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
				}
				
			}
		}
		
		Iterator<WaittingItem> it2 = waitingItemList.iterator();
		while(it2.hasNext()){
			WaittingItem wi = it2.next();
			if(wi == null)
				continue;
			if(wi.player == player){
				it2.remove();
				
				if(logger.isDebugEnabled()){
//					logger.debug("[跨服战场排队服务] [个人离开排队] [检查等待队列] [清除项目，玩家离开排队] ["+player.getUsername()+"] ["+player.getName()+"] ["+wi.bf.getName()+"] ["+wi.bf.getId()+"]");
					if(logger.isDebugEnabled())
						logger.debug("[跨服战场排队服务] [个人离开排队] [检查等待队列] [清除项目，玩家离开排队] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getName(),wi.bf.getName(),wi.bf.getId()});
				}
			}
		}
		
		Iterator<LineItem> it = itemList.iterator();
		while(it.hasNext()){
			LineItem li = it.next();
			if(li == null)
				continue;
			if(li.players.size() == 0){
				it.remove();
			}
		}
		
		return true;
	}
	
	public BattleFieldStatData[] getBattleFieldStatDataByPlayer(Player player){
		BattleField bf = player.getBattleField();
		if(bf == null){
			return new BattleFieldStatData[0];
		}else{
			Hashtable<Long,BattleFieldStatData> map = bf.getStatDataMap();
			BattleFieldStatData datas[] = map.values().toArray(new BattleFieldStatData[0]);
			Arrays.sort(datas,new Comparator<BattleFieldStatData>(){

				public int compare(BattleFieldStatData o1,
						BattleFieldStatData o2) {
					if(o1.getHonorPoints() > o2.getHonorPoints()){
						return -1;
					}
					if(o1.getHonorPoints() < o2.getHonorPoints()){
						return 1;
					}
					if(o1.getKillingNum() > o2.getKillingNum()){
						return -1;
					}
					if(o1.getKillingNum() < o2.getKillingNum()){
						return 1;
					}
					return 0;
				}
				
			});
			return datas;
		}
	}
	/**
	 * 得到某个人可以的战场列表
	 * @param player
	 * @return
	 */
	public BattleItem[] getAvailableBattleItems(Player player){
		BattleFieldManager bfm = BattleFieldManager.getInstance();
		BattleFieldInfo infos[] = bfm.getBattleFieldInfos();
		ArrayList<BattleFieldInfo> al = new ArrayList<BattleFieldInfo> ();
		for(int i = 0 ; i < infos.length ; i++){
			BattleFieldInfo bi = infos[i];
			if(bi.isBangpaiFlag() == false && bi.getMinPlayerLevel() <= player.getLevel() && bi.getMaxPlayerLevel() >= player.getLevel()){
				if(!bi.getMapName().equals(Translate.text_1754)&&!bi.getMapName().equals(Translate.text_1755)&&!bi.getMapName().equals(Translate.text_1756)&&!bi.getMapName().equals(Translate.text_1757)&&!bi.getMapName().equals(Translate.text_1758)&&!bi.getMapName().equals(Translate.text_1759)){
					al.add(bi);
				}
			}else if(bi.isBangpaiFlag() && bi.getMinPlayerLevel() <= player.getLevel() && bi.getMaxPlayerLevel() >= player.getLevel()){
				//帮战
				if(player.getGangName() != null && player.getGangName().trim().length() > 0){
					if(player.getGangName().equals(bi.getGangNameForA())
						|| player.getGangName().equals(bi.getGangNameForB())){
						al.add(bi);
					}
				}
			}
		}
		BattleItem items[] = new BattleItem[al.size()];
		for(int i = 0 ; i < items.length ; i++){
			items[i] = new BattleItem();
			BattleFieldInfo bi = al.get(i);
			items[i].setName(bi.getName());
			items[i].setDescription(bi.getDescription());
			items[i].setDisplayName(bi.getName()+"("+bi.getMinPlayerLevel()+"~"+bi.getMaxPlayerLevel()+")");
			items[i].setWaitTime("");
			int pt = 0;
			for(LineItem li : itemList){
				if(li == null)
					continue;
				for(Player p : li.players){
					if(p == player && li.battleName.equals(bi.getName())){
						pt = 1;
						items[i].setWaitTime(Translate.text_1870);
					}
				}
			}
			if(player.getBattleField() != null && player.getBattleField().getName().equals(bi.getName())){
				pt = 2;
				items[i].setWaitTime("");
			}
			items[i].setPlayerType(pt);
		}
		return items;
		
	}
	/**
	 * 团队加入排队
	 * @param player
	 * @param bi
	 */
	public void doTeamLineup(Player player,BattleFieldInfo bi){
		if(player.getTeam() == null){
			doPlayerLineup(player,bi);
		}
		if(bi.isBangpaiFlag()){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1871);
			player.addMessageToRightBag(req);
			
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [队伍加入排队] [失败] [帮战只能以个人的形式加入] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [队伍加入排队] [失败] [帮战只能以个人的形式加入] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
			}
			
			return;
		}
		
		if(player.getTeamMark() != Player.TEAM_MARK_CAPTAIN){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1872);
			player.addMessageToRightBag(req);
			
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [队伍加入排队] [失败] [不是队长] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [队伍加入排队] [失败] [不是队长] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
			}
			
			return;
		}
		if(player.getBattleField() != null || player.isInBattleField()){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1873);
			player.addMessageToRightBag(req);
			
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [队伍加入排队] [失败] [您已经能够在战场中] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [队伍加入排队] [失败] [您已经能够在战场中] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
			}
			
			return;
		}
		
		Player ps[] = player.getTeamMembers();
		for(int i = 0 ; i < ps.length ; i++){
			if(bi.getMinPlayerLevel() > ps[i].getLevel() || bi.getMaxPlayerLevel() < ps[i].getLevel()){
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1874+ps[i].getName()+Translate.text_1875);
				player.addMessageToRightBag(req);
				
				if(logger.isInfoEnabled()){
//					logger.info("[战场排队服务] [队伍加入排队] [失败] [队员的级别不符合战场的要求] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
					if(logger.isInfoEnabled())
						logger.info("[战场排队服务] [队伍加入排队] [失败] [队员的级别不符合战场的要求] [{}] [{}] [排队条目：{}]", new Object[]{player.getName(),bi.getName(),itemList.size()});
				}
				
				return;
			}
		}
		
		for(int i = 0 ; i < ps.length ; i++){
			boolean buffForbid = false;
			int forbidTime = 0;
			Buff buffs[] = ps[i].getActiveBuffs();
			for(Buff buff : buffs){
				if(buff instanceof Buff_ForbidToGoToZhanChang){
					Buff_ForbidToGoToZhanChang b = (Buff_ForbidToGoToZhanChang)buff;
					if(bi.getForbidBuffName().equals(b.getBattleFieldName())){
						buffForbid = true;
						forbidTime = (int)(b.getInvalidTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis())/60000;
						if(forbidTime == 0) forbidTime = 1;
					}
				}
			}
			if(buffForbid){
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1874+ps[i].getName()+Translate.text_1876+forbidTime+Translate.text_1869);
				player.addMessageToRightBag(req);
				return;
			}
		}
		
		for(int i = 0 ; i < ps.length ; i++){
			for(LineItem li : itemList){
				if(li == null)
					continue;
				if(li.players.contains(ps[i]) && li.battleName.equals(bi.getName())){
					li.players.remove(ps[i]);
					
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [队伍加入排队] [检查排队队列] [清除队员以前的排队] [队长："+player.getName()+"] [队员："+ps[i].getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [队伍加入排队] [检查排队队列] [清除队员以前的排队] [队长：{}] [队员：{}] [{}] [排队条目：{}]", new Object[]{player.getName(),ps[i].getName(),bi.getName(),itemList.size()});
					}
				}
			}
			
			Iterator<WaittingItem> it2 = waitingItemList.iterator();
			while(it2.hasNext()){
				WaittingItem wi = it2.next();
				if(wi == null)
					continue;
				if(wi.player == ps[i]){
					it2.remove();
					
					if(logger.isInfoEnabled()){
//						logger.info("[战场排队服务] [队伍加入排队] [检查等待队列] [清除队员等待进入队列] [队长："+player.getName()+"] [队员："+ps[i].getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"]");
						if(logger.isInfoEnabled())
							logger.info("[战场排队服务] [队伍加入排队] [检查等待队列] [清除队员等待进入队列] [队长：{}] [队员：{}] [{}] [排队条目：{}]", new Object[]{player.getName(),ps[i].getName(),bi.getName(),itemList.size()});
					}
				}
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		
		LineItem li = new LineItem();
		li.battleName = bi.getName();
		for(int i = 0 ; i < ps.length ; i++){
			if(ps[i].isInBattleField() == false){
				li.players.add(ps[i]);
				sb.append(ps[i].getName()+",");
			}
		}
		li.lineTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		this.itemList.add(li);
		
		if(logger.isInfoEnabled()){
//			logger.info("[战场排队服务] [队伍加入排队] [成功] [OK] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"] [队员："+sb.toString()+"]");
			if(logger.isInfoEnabled())
				logger.info("[战场排队服务] [队伍加入排队] [成功] [OK] [{}] [{}] [排队条目：{}] [队员：{}]", new Object[]{player.getName(),bi.getName(),itemList.size(),sb.toString()});
		}
		Player.sendPlayerAction(player, PlayerActionFlow.行为类型_战场排队, bi.getName()+Translate.text_1877+sb.toString()+")", 0, new java.util.Date(), true);
		Iterator<LineItem> it = itemList.iterator();
		while(it.hasNext()){
			li = it.next();
			if(li == null)
				continue;
			if(li.players.size() == 0){
				it.remove();
			}
		}
		for(int i = 0 ; i < ps.length ; i++){
			if(ps[i] == player){
				
				QUERY_BATTLEFIELDLIST_RES res = new QUERY_BATTLEFIELDLIST_RES(GameMessageFactory.nextSequnceNum(),getAvailableBattleItems(player));
				player.addMessageToRightBag(res);
			}else if(ps[i].isInBattleField() == false){
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_1878+bi.getName()+Translate.text_1879);
				ps[i].addMessageToRightBag(req);
			}
		}
		
	}
	
	public boolean doCrossTeamLineup(Player player,BattleFieldInfo bi) throws Exception {
		return false;
		
	}
	
	/**
	 * 离开战场
	 * @param player
	 * @param bi
	 */
	public void doLeaveBattleField(Player player,BattleFieldInfo bi){
		if(player.getBattleField() != null){
			BattleField bf = player.getBattleField();
			if(this.isCrossMode()) {
			
			} else {
				if(bf.isOpen()){
					bf.getGame().messageQueue.push(new TransferGameEvent(player,new TransportData(0,0,0,0,player.getEnterBattleFieldMapName(),player.getEnterBattleFieldX(),player.getEnterBattleFieldY())));
				}else{
					bf.getGame().transferGame(player, new TransportData(0,0,0,0,player.getEnterBattleFieldMapName(),player.getEnterBattleFieldX(),player.getEnterBattleFieldY()));
				}
				
				if(logger.isInfoEnabled()){
//					logger.info("[战场排队服务] [玩家离开战场] [成功] [OK] ["+player.getName()+"] ["+bi.getName()+"] ["+bf.getId()+"] [进入地点："+player.getEnterBattleFieldMapName()+"("+player.getEnterBattleFieldX()+","+player.getEnterBattleFieldY()+")]");
					if(logger.isInfoEnabled())
						logger.info("[战场排队服务] [玩家离开战场] [成功] [OK] [{}] [{}] [{}] [进入地点：{}({},{})]", new Object[]{player.getName(),bi.getName(),bf.getId(),player.getEnterBattleFieldMapName(),player.getEnterBattleFieldX(),player.getEnterBattleFieldY()});
				}
			}
		}else{
			if(logger.isInfoEnabled()){
//				logger.info("[战场排队服务] [玩家离开战场] [失败] [玩家不在战场中] ["+player.getName()+"] ["+bi.getName()+"] [排队条目："+itemList.size()+"] [进入地点："+player.getEnterBattleFieldMapName()+"("+player.getEnterBattleFieldX()+","+player.getEnterBattleFieldY()+")]");
				if(logger.isInfoEnabled())
					logger.info("[战场排队服务] [玩家离开战场] [失败] [玩家不在战场中] [{}] [{}] [排队条目：{}] [进入地点：{}({},{})]", new Object[]{player.getName(),bi.getName(),itemList.size(),player.getEnterBattleFieldMapName(),player.getEnterBattleFieldX(),player.getEnterBattleFieldY()});
			}
		}
	}
	
	public void run(){
		while(Thread.currentThread().isInterrupted() == false){
			try{
				Thread.sleep(checkTime);
				
				heartbeat();
				
			}catch(Throwable e){
				logger.error("心跳出现异常",e);
			}
		}
	}
}
