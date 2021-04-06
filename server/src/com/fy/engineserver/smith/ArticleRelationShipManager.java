package com.fy.engineserver.smith;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 *
 * 
 * @version 创建时间：Mar 27, 2013 6:51:43 PM
 * 
 */
public class ArticleRelationShipManager implements Runnable {
	public static Logger logger = LoggerFactory.getLogger(ArticleRelationShipManager.class);
	
	private String datafile = null;
	
	private DiskCache cache = null;
	
	private int relationSequence;
	
	/**
	 * 汇聚者map
	 */
	private HashMap<Long, ArticleMaker> makerMap;
	
	/**
	 * 组织关系map
	 */
	private HashMap<Integer,ArticleRelationShip> relationShipMap;
	
	/**
	 * 玩家对应的组织关系
	 */
	private HashMap<Long, Integer> playerRelationMap;
	
	/**
	 * 被封禁的组织关系，角色全部不允许进入服务器
	 */
	private ArrayList<Integer> forbidShips;
	
	/**
	 * 白名单，如果在封禁的组织关系但是也在白名单中，那么允许进入服务器
	 */
	private HashSet<Long> forbidPlayers;
	
	public boolean openning = true;
	
	private List<ArticleMove> moveList;
	
	//一些数据采样标准
	private int 不采样充值RMB = 7000;
	private int 自动封禁充值额度 = 7000;
	private int 不采样充值等级 = 160;
	public int forbidDownCount = 20;
	public int forbidMaxLevel = 110;
	
	private static ArticleRelationShipManager self;
	
	public static ArticleRelationShipManager getInstance() {
		return self;
	}
	
	public synchronized int nextRelationId() {
		relationSequence++;
		cache.put("relationSequence", relationSequence);
		return relationSequence;
	}
	
	public void init() {
		
		long start = System.currentTimeMillis();
		moveList = new LinkedList<ArticleMove>();
		cache = new DefaultDiskCache(new File(datafile), "打物品工作室组织关系网", 1000L * 60 * 60 * 24 * 365 * 10);
		makerMap = (HashMap<Long,ArticleMaker>)cache.get("makerMap");
		if(makerMap == null) {
			makerMap = new HashMap<Long, ArticleMaker>();
			cache.put("makerMap", makerMap);
		}
		relationShipMap = (HashMap<Integer,ArticleRelationShip>)cache.get("relationShipMap");
		if(relationShipMap == null) {
			relationShipMap = new HashMap<Integer, ArticleRelationShip>();
			cache.put("relationShipMap", relationShipMap);
		}
		playerRelationMap = (HashMap<Long, Integer>)cache.get("playerRelationMap");
		if(playerRelationMap == null) {
			playerRelationMap = new HashMap<Long, Integer>();
			cache.put("playerRelationMap", playerRelationMap);
		}
		forbidShips = (ArrayList<Integer>)cache.get("forbidShips");
		if(forbidShips == null) {
			forbidShips = new ArrayList<Integer>();
			cache.put("forbidShips", forbidShips);
		}
		forbidPlayers = (HashSet<Long>)cache.get("forbidPlayers");
		if(forbidPlayers == null) {
			forbidPlayers = new HashSet<Long>();
			cache.put("forbidPlayers", forbidPlayers);
		}
		Integer seq = (Integer)cache.get("relationSequence");
		if(seq == null) {
			cache.put("relationSequence", 0);
		} else {
			relationSequence = seq.intValue();
		}
		Integer a = (Integer)cache.get("不采样充值RMB");
		if(a == null) {
			cache.put("不采样充值RMB", 不采样充值RMB);
		}
		a = (Integer)cache.get("不采样充值等级");
		if(a == null) {
			cache.put("不采样充值等级", 不采样充值等级);
		}
		self = this;
		if(openning) {
			start();
		}
		Game.logger.warn("[打物品工作室关系网管理初始化成功] ["+ArticleRelationShipManager.class.getName()+"] " +
				"[seq:"+relationSequence+"] [openning:"+openning+"] [makerNum:"+makerMap.size()+"] " +
						"[relationNum:"+relationShipMap.size()+"] [playerNum:"+playerRelationMap.size()+"] " +
								"[封禁的组织:"+forbidShips.size()+"] [封禁的角色:"+forbidPlayers.size()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		ServiceStartRecord.startLog(this);
	}
	
	public void start() {
		this.openning = true;
		Thread t = new Thread(this, "ArticleRelationShipManager");
		t.start();
	}
	
	public void stop() {
		this.openning = false;
	}
	
	public synchronized ArticleMove popOne() {
		if(moveList.size() > 0) {
			return moveList.remove(0);
		}
		return null;
	}
	
	/**
	 * 增加一个物品转移行为
	 * @param down	下线
	 * @param up	上线
	 * @param amount	数量
	 */
	public synchronized void addArticleMove(Player down, Player up, int amount) {
		if(openning) {
			ArticleMove mm = new ArticleMove();
			mm.down = down;
			mm.up = up;
			mm.amount = amount;
			this.moveList.add(mm);
		}
	}
	
	public void destroy() {
		long start = System.currentTimeMillis();
		cache.put("makerMap", makerMap);
		cache.put("relationShipMap", relationShipMap);
		cache.put("playerRelationMap", playerRelationMap);
		cache.put("relationSequence", relationSequence);
		cache.put("forbidShips", forbidShips);
		cache.put("forbidPlayers", forbidPlayers);
		cache.put("不采样充值RMB", 不采样充值RMB);
		cache.put("不采样充值等级", 不采样充值等级);
		System.out.println("[打物品工作室关系网管理销毁] ["+ArticleRelationShipManager.class.getName()+"] [openning:"+openning+"] " +
				"[relationNum:"+relationShipMap.size()+"] [makerNum:"+makerMap.size()+"] " +
						"[playerNum:"+playerRelationMap.size()+"] [封禁的组织:"+forbidShips.size()+"] [封禁的角色:"+forbidPlayers.size()+"] " +
								"["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	/**
	 * 重建数据
	 */
	public void recreate() {
		long start = System.currentTimeMillis();
		moveList = new LinkedList<ArticleMove>();
		makerMap = new HashMap<Long, ArticleMaker>();
		cache.put("makerMap", makerMap);
		relationShipMap = new HashMap<Integer, ArticleRelationShip>();
		cache.put("relationShipMap", relationShipMap);
		playerRelationMap = new HashMap<Long, Integer>();
		cache.put("playerRelationMap", playerRelationMap);
		forbidShips = new ArrayList<Integer>();
		cache.put("forbidShips", forbidShips);
		forbidPlayers = new HashSet<Long>();
		cache.put("forbidPlayers", forbidPlayers);
		cache.put("relationSequence", 0);
		if(logger.isInfoEnabled()) {
			logger.info("[完成recreate] [relationNum:"+relationShipMap.size()+"] [makerNum:"+makerMap.size()+"] " +
							"[playerNum:"+playerRelationMap.size()+"] [封禁的组织:"+forbidShips.size()+"] [封禁的角色:"+forbidPlayers.size()+"] " +
									"["+(System.currentTimeMillis()-start)+"ms]");
		}
	}

	public void setDatafile(String datafile) {
		this.datafile = datafile;
	}
	
	public int get不采样充值RMB() {
		return 不采样充值RMB;
	}

	public void set不采样充值RMB(int 不采样充值rmb) {
		不采样充值RMB = 不采样充值rmb;
	}

	public int get不采样充值等级() {
		return 不采样充值等级;
	}

	public void set不采样充值等级(int 不采样充值等级) {
		this.不采样充值等级 = 不采样充值等级;
	}

	public int get自动封禁充值额度() {
		return 自动封禁充值额度;
	}

	public void set自动封禁充值额度(int 自动封禁充值额度) {
		this.自动封禁充值额度 = 自动封禁充值额度;
	}

	public boolean isOpenning() {
		return openning;
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastCheck = 0;
		while(openning) {
			try {
				ArticleMove mm = popOne();
				if(mm != null) {
					if(mm.down.getRMB() >= this.不采样充值RMB || mm.down.getLevel() >= this.不采样充值等级) {
						if(logger.isDebugEnabled()) {
							logger.debug("[不符合采样条件] [down:({})({})] [up:({})({})]", new Object[]{mm.down.getRMB(), mm.down.getLevel(), mm.up.getRMB(), mm.up.getLevel()});
						}
						continue;
					}
					articleMoved(mm.down, mm.up, mm.amount);
				} else {
					Thread.sleep(1000);
				}
				if(System.currentTimeMillis()-lastCheck > 5000) {
					//检查有没有死循环的工作室
					ArticleRelationShip ships[] = relationShipMap.values().toArray(new ArticleRelationShip[0]);
					for(ArticleRelationShip ship : ships) {
						if(!isForbid(ship)) {
							boolean dead = ship.isDeadLooped();
							if(dead) {
								logger.error("[死循环的工作室：删除此工作室] [id:"+ship.getId()+"] [tops:"+ship.getTopLevelList().size()+"]");
								removeShip(ship);
							}
						}
					}
					logger.info("[运行定时(5s)打印，如果长时间不打印本信息说明有死循环!] [moveList:"+moveList.size()+"] [makerNum:"+makerMap.size()+"] " +
							"[relationNum:"+relationShipMap.size()+"] [playerNum:"+playerRelationMap.size()+"] " +
									"[封禁的组织:"+forbidShips.size()+"] [封禁的角色:"+forbidPlayers.size()+"]");
					lastCheck = System.currentTimeMillis();
				}
			} catch(Exception e) {
				logger.error("[处理数据时发生异常]", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获得所有工作室组织的map
	 * @return
	 */
	public Map<Integer,ArticleRelationShip> getAllArticleRelationShip() {
		return relationShipMap;
	}
	
	/**
	 * 获得单层下线数量的关系
	 * @param min
	 * @return
	 */
	public List<ArticleRelationShip> getMinDownCountShips(int min) {
		List<ArticleRelationShip> listA = new ArrayList<ArticleRelationShip>();
		List<ArticleRelationShip> listB = new ArrayList<ArticleRelationShip>();
		ArticleRelationShip ships[] = relationShipMap.values().toArray(new ArticleRelationShip[0]);
		for(ArticleRelationShip ship : ships) {
			if(ship.hasMinDownCount(min)) {
				if(isForbid(ship)) {
					listB.add(ship);
				} else {
					listA.add(ship);
				}
			}
		}
		Collections.sort(listA, new Comparator<ArticleRelationShip>() {

			@Override
			public int compare(ArticleRelationShip o1, ArticleRelationShip o2) {
				// TODO Auto-generated method stub
				if(o1.getMaxDownCount() < o2.getMaxDownCount()) {
					return -1;
				} else if(o1.getMaxDownCount() > o2.getMaxDownCount()) {
					return 1;
				}
				return 0;
			}
			
		} ) ;
		
		listA.addAll(listB);
		return listA;
	}	

	/**
	 * 获得最小层级的关系
	 * @param min
	 * @return
	 */
	public List<ArticleRelationShip> getMinLayerCountShips(int min) {
		List<ArticleRelationShip> list = new ArrayList<ArticleRelationShip>();
		ArticleRelationShip ships[] = relationShipMap.values().toArray(new ArticleRelationShip[0]);
		for(ArticleRelationShip ship : ships) {
			if(ship.hasMinLayerCount(min)) {
				list.add(ship);
			}
		}
		return list;
	}
	
	/**
	 * 获得玩家所在的工作室组织
	 * @return
	 */
	public Map<Long, Integer> getPlayerRelationShipMap() {
		return playerRelationMap;
	}
	
	public ArticleRelationShip createNewRelation() {
		ArticleRelationShip ship = new ArticleRelationShip();
		ship.setId(this.nextRelationId());
		relationShipMap.put(ship.getId(), ship);
		return ship;
	}
	
	public ArticleMaker createArticleMaker(Player player) {
		ArticleMaker mm = new ArticleMaker();
		mm.setName(player.getName());
		mm.setUsername(player.getUsername());
		mm.setId(player.getId());
		try {
			mm.setIp(player.getIPAddress().substring(1, player.getIPAddress().indexOf(":")));
		} catch(Exception e) {
			mm.setIp("unknown");
		}
		mm.setLevel(player.getLevel());
		mm.setMt(player.getUserAgent());
		mm.setRmb(player.getRMB());
		makerMap.put(player.getId(), mm);
		return mm;
	}
	
	public void refreshArticleMaker(ArticleMaker mm, Player player) {
		if(mm.getId() != player.getId()) {
			return ;
		}
		try {
			mm.setIp(player.getIPAddress().substring(1, player.getIPAddress().indexOf(":")));
		} catch(Exception e) {
			//不更新
			if(mm.getIp() == null) {
				mm.setIp("unknown");
			}
		}
		mm.setLevel(player.getLevel());
		mm.setMt(player.getUserAgent());
		mm.setCurrentSilver(player.getSilver());
		mm.setRmb(player.getRMB());
	}
	
	public ArticleMaker getArticleMaker(long id) {
		return makerMap.get(id);
	}
	
	/**
	 * 封禁一个组织关系
	 * @param ship
	 */
	public void forbidShip(ArticleRelationShip ship, List<Long> except, boolean auto) {
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveForbidArticleMaker(mm, except, auto);
				if(!except.contains(mmId)) {
					if(mm.getRmb() > 自动封禁充值额度 || mm.getLevel() <= forbidMaxLevel) {
						if(logger.isInfoEnabled()) {
							logger.info("[封禁工作室成员] [auto:"+auto+"] [发现此人充值>"+自动封禁充值额度+"，或等级<="+forbidMaxLevel+"] [不予封禁] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] ["+mm.getLevel()+"] ["+mm.getRmb()+"]");
						}
						continue;
					}
					forbidPlayers.add(mmId);
					//forbidUserForLogin(mm.getUsername());
//					PlayerManager pm = PlayerManager.getInstance();
//					pm.kickPlayer(mmId);
					if(logger.isInfoEnabled()) {
						logger.info("[封禁组织成员] [auto:"+auto+"] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
					}
				}
			}
		}
		if(!forbidShips.contains(ship.getId())) {
			forbidShips.add(ship.getId());
			if(logger.isInfoEnabled()) {
				logger.info("[封禁组织] [auto:"+auto+"] [id:"+ship.getId()+"]");
			}
		}
	}
	
	/**
	 * 递归封禁玩家
	 * @param maker
	 * @param except
	 */
	public void recursiveForbidArticleMaker(ArticleMaker maker, List<Long> except, boolean auto) {
		if(!openning) {
			return;
		}
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveForbidArticleMaker(mm, except, auto);
			}		
			if(!except.contains(mmId)) {
				if(mm.getRmb() > 自动封禁充值额度 || mm.getLevel() <= forbidMaxLevel) {
					if(logger.isInfoEnabled()) {
						logger.info("[封禁工作室成员] [auto:"+auto+"] [发现此人充值>"+自动封禁充值额度+"，或等级<="+forbidMaxLevel+"] [不予封禁] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] ["+mm.getLevel()+"] ["+mm.getRmb()+"]");
					}
					continue;
				}
				forbidPlayers.add(mmId);
				//forbidUserForLogin(mm.getUsername());
//				PlayerManager pm = PlayerManager.getInstance();
//				pm.kickPlayer(mmId);
				if(logger.isInfoEnabled()) {
					logger.info("[封禁组织成员] [auto:"+auto+"] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
				}
			}
		}
	}
	
	/**
	 * 仅仅标记封禁一个组织关系，并不对组织成员进行封禁，这种场合多在已经被封禁的一个组织被并入新的组织后发生。
	 * @param ship
	 */
	public void symbolForbidShip(ArticleRelationShip ship) {
		if(!forbidShips.contains(ship.getId())) {
			forbidShips.add(ship.getId());
			if(logger.isInfoEnabled()) {
				logger.info("[标记封禁组织] [id:"+ship.getId()+"]");
			}
		}
	}
	
//	public void forbidUserForLogin(String username) {
//		try {
//			DENY_USER_REQ req = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", username, "您因为打金工作室行为被封禁", "系统", false, true, true, 0, 0);
//			MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
//			logger.warn("[在网关上封禁了账号] [因为打金工作室行为] ["+username+"]");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 解禁一个组织
	 * @param ship
	 */
	public void releaseShip(ArticleRelationShip ship) {
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveReleaseArticleMaker(mm);
				forbidPlayers.remove(new Long(mmId));
				if(logger.isInfoEnabled()) {
					logger.info("[解禁组织成员] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
				}
			}
		}
		forbidShips.remove(new Integer(ship.getId()));
		if(logger.isInfoEnabled()) {
			logger.info("[解禁组织] [id:"+ship.getId()+"]");
		}
	}
	
	/**
	 * 递归解禁玩家
	 * @param maker
	 * @param except
	 */
	public void recursiveReleaseArticleMaker(ArticleMaker maker) {
		if(!openning) {
			return;
		}
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveReleaseArticleMaker(mm);
			}		
			forbidPlayers.remove(new Long(mmId));
			if(logger.isInfoEnabled()) {
				logger.info("[解禁组织成员] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
			}
		}
	}
	
	/**
	 * 玩家是否被封禁
	 * @param player
	 * @return
	 */
	public boolean isForbid(long playerId) {
		if(forbidPlayers.contains(playerId)) {
			return true;
		}
		return false;
	}
	
	public boolean isForbid(ArticleRelationShip ship) {
		if(forbidShips.contains(ship.getId())) {
			return true;
		}
		return false;
	}
	
	public void removeShip(ArticleRelationShip ship) {
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveRemoveArticleMaker(mm, new HashSet<Long>());
				playerRelationMap.remove(new Long(mm.getId()));
				makerMap.remove(new Long(mm.getId()));
				forbidPlayers.remove(new Long(mm.getId()));
				if(logger.isInfoEnabled()) {
					logger.info("[删除组织成员] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
				}
			}
		}
		relationShipMap.remove(new Integer(ship.getId()));
		forbidShips.remove(new Integer(ship.getId()));
		if(logger.isInfoEnabled()) {
			logger.info("[删除组织] [id:"+ship.getId()+"]");
		}
	}
	
	/**
	 * 递归解禁玩家
	 * @param maker
	 * @param except
	 */
	public void recursiveRemoveArticleMaker(ArticleMaker maker, HashSet<Long> ids) {
		if(ids.contains(maker.getId()) || !openning) {
			return;
		}
		ids.add(maker.getId());
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			ArticleMaker mm = this.getArticleMaker(mmId);
			if(mm != null) {
				recursiveRemoveArticleMaker(mm, ids);	
				playerRelationMap.remove(new Long(mm.getId()));
				makerMap.remove(new Long(mm.getId()));
				forbidPlayers.remove(new Long(mm.getId()));
				if(logger.isInfoEnabled()) {
					logger.info("[删除组织成员] ["+mm.getId()+"] ["+mm.getName()+"] ["+mm.getUsername()+"] [上一次交易时充值额:"+mm.getRmb()+"] [共从下线获取:"+mm.getTotalSilver()+"] [总共向外交易:"+mm.getTotalUp()+"]");
				}
			}	
		}
		ids.remove(maker.getId());
	}
	
	/**
	 * 
	 * @param downId 下线
	 * @param upId 上线
	 * @param amount 数量
	 */
	private synchronized void articleMoved(Player down, Player up, int amount) {
		long start = System.currentTimeMillis();
		if(down.getId() == up.getId()) {
			logger.error("[交易双方一样：严重的错误] [down:"+down.getLogString()+"] [up:"+up.getLogString()+"]");
		}
		ArticleRelationShip relationDown = null;
		ArticleRelationShip relationUp = null;
		ArticleMaker downMaker = null;
		ArticleMaker upMaker = null;
		Integer relationDownId = playerRelationMap.get(down.getId());
		Integer relationUpId = playerRelationMap.get(up.getId());
		if(relationDownId == null && relationUpId == null) {
			//两个都不在任何组织关系内
			downMaker = createArticleMaker(down);
			upMaker = createArticleMaker(up);
			if(!downMaker.getUpList().contains(upMaker.getId())) {
				downMaker.getUpList().add(upMaker.getId());
			}
			if(!upMaker.getDownList().contains(downMaker.getId())) {
				upMaker.getDownList().add(downMaker.getId());
			}
			ArticleRelationShip relation = createNewRelation();
			if(!relation.getTopLevelList().contains(upMaker.getId())) {
				relation.getTopLevelList().add(upMaker.getId());
			}
			playerRelationMap.put(down.getId(), relation.getId());
			playerRelationMap.put(up.getId(), relation.getId());
			downMaker.addUp(up.getId(), amount);
			upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
			if(logger.isDebugEnabled()) {
				logger.debug("[ArticleMoved] [双方都没有组织关系，创建新关系] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
						new Object[]{relation.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
			}
			return;
		} else if(relationDownId == null && relationUpId != null) {
			//下线不在任何组织关系内，上线已经在一个组织关系内
			relationUp = relationShipMap.get(relationUpId);
			if(relationUp == null) {
				playerRelationMap.remove(new Long(up.getId()));
				articleMoved(down, up, amount);
				return;
			}
			upMaker = makerMap.get(up.getId());
			if(upMaker == null) {
				playerRelationMap.remove(up.getId());
				relationShipMap.remove(relationUpId);
				articleMoved(down, up, amount);
				return;
			}
			refreshArticleMaker(upMaker, up);
			downMaker = createArticleMaker(down);
			if(!downMaker.getUpList().contains(upMaker.getId())) {
				downMaker.getUpList().add(upMaker.getId());
			}
			if(!upMaker.getDownList().contains(downMaker.getId())) {
				upMaker.getDownList().add(downMaker.getId());
			}
			playerRelationMap.put(down.getId(), relationUp.getId());
			downMaker.addUp(up.getId(), amount);
			upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
			checkForbidRelation(relationUp);
			if(logger.isDebugEnabled()) {
				logger.debug("[ArticleMoved] [下线不在任何组织关系内，上线已经在一个组织关系内] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
						new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
			}
			return;
		} else if(relationDownId != null && relationUpId == null) {
			//上线不在任何组织关系内，下线已经在一个组织关系内
			relationDown = relationShipMap.get(relationDownId);
			if(relationDown == null) {
				playerRelationMap.remove(new Long(down.getId()));
				articleMoved(down, up, amount);
				return;
			}
			downMaker = makerMap.get(down.getId());
			if(downMaker == null) {
				playerRelationMap.remove(down.getId());
				relationShipMap.remove(relationDownId);
				articleMoved(down, up, amount);
				return;
			}
			refreshArticleMaker(downMaker, down);
			upMaker = createArticleMaker(up);
			if(!downMaker.getUpList().contains(upMaker.getId())) {
				downMaker.getUpList().add(upMaker.getId());
			}
			if(!upMaker.getDownList().contains(downMaker.getId())) {
				upMaker.getDownList().add(downMaker.getId());
			}
			playerRelationMap.put(up.getId(), relationDown.getId());
			if(!relationDown.getTopLevelList().contains(upMaker.getId())) {
				relationDown.getTopLevelList().add(upMaker.getId());
			}
			if(relationDown.getTopLevelList().contains(downMaker.getId())) {
				relationDown.getTopLevelList().remove(new Long(downMaker.getId()));
			}
			downMaker.addUp(up.getId(), amount);
			upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
			if(logger.isDebugEnabled()) {
				logger.debug("[ArticleMoved] [上线不在任何组织关系内，下线已经在一个组织关系内] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
						new Object[]{relationDown.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
			}
			return;
		} else if(relationDownId != null && relationUpId != null) {
			//如果两个都已经在组织关系内，分两种情况
			if(relationDownId.intValue() == relationUpId.intValue()) {
				//如果两个在同一个组织关系内，那么寻找下线的图谱中如果已经有此上线角色，那么不允许反转，否则允许
				relationUp = relationShipMap.get(relationUpId);
				relationDown = relationShipMap.get(relationDownId);
				if(relationUp == null || relationDown == null) {
					if(relationDown == null) {
						playerRelationMap.remove(down.getId());
					}
					if(relationUp == null) {
						playerRelationMap.remove(up.getId());
					}
					if(logger.isDebugEnabled()) {
						logger.debug("[ArticleMoved] [两个在同一个组织关系内，但是有关系为NULL] [relation:{}] [down:{}] [up:{}] [{}ms]", 
								new Object[]{"null", down.getLogString(), up.getLogString(), (System.currentTimeMillis()-start)});
					}
					return;
				}
				downMaker  = makerMap.get(down.getId());
				if(downMaker == null) {
					playerRelationMap.remove(down.getId());
					relationShipMap.remove(relationDownId);
					articleMoved(down, up, amount);
					return;
				}
				refreshArticleMaker(downMaker, down);
				upMaker = makerMap.get(up.getId());
				if(upMaker == null) {
					playerRelationMap.remove(up.getId());
					relationShipMap.remove(relationUpId);
					articleMoved(down, up, amount);
					return;
				}
				refreshArticleMaker(upMaker, up);
				if(upMaker.getDownList().contains(down.getId())) {
					//此前就是直接的上下线关系
					if(logger.isDebugEnabled()) {
						logger.debug("[ArticleMoved] [两个在同一个组织关系内，相同的上下线关系] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
										new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
					}
					downMaker.addUp(up.getId(), amount);
					upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
					return;
				} else if(downMaker.getDownList().contains(up.getId())) {
					//此前是反转的关系
					//比较谁汇聚的多
					long upUpCount = upMaker.getUpAmount(down.getId());
					long downUpCount = downMaker.getUpAmount(up.getId());
					if(downUpCount+amount <= upUpCount) {
						//保持反转关系不变
						if(logger.isDebugEnabled()) {
							logger.debug("[ArticleMoved] [两个在同一个组织关系内，相同的上下线关系] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
											new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
						}
						downMaker.addUp(up.getId(), amount);
						upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
						return;
					} else {
						//需要反转，如果down的所有下线延展中包含up，那么不进行反转
						List<Long> downs = downMaker.getDownList();
						for(Long id : downs) {
							ArticleMaker am = makerMap.get(id);
							if(am != null && am.getId() != up.getId()) {
								ArticleMaker find = relationDown.searchArticleMaker(am, up.getName());
								if(find != null) {
									//反转将导致死循环，不翻转
									if(logger.isDebugEnabled()) {
										logger.debug("[ArticleMoved] [两个在同一个组织关系内，本来要进行反转但是可能导致死循环，保持不变] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
														new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
									}
									return;
								}
							}
						}
						//可以进行反转
						downMaker.getDownList().remove(new Long(up.getId()));
						downMaker.getUpList().add(new Long(up.getId()));
						upMaker.getUpList().remove(new Long(down.getId()));
						upMaker.getDownList().add(new Long(down.getId()));
						if(relationDown.getTopLevelList().contains(downMaker.getId())) {
							relationDown.getTopLevelList().remove(new Long(downMaker.getId()));
						}
						if(!relationDown.getTopLevelList().contains(new Long(upMaker.getId())) && upMaker.getUpList().size() == 0) {
							relationDown.getTopLevelList().add(new Long(upMaker.getId()));
						}
						downMaker.addUp(up.getId(), amount);
						upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
						if(logger.isDebugEnabled()) {
							logger.debug("[ArticleMoved] [两个在同一个组织关系内，符合反转条件] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
											new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
						}
						return;
					}
				} else {
					//可能是处于不同的分支中，那么查看下线的下线路径中能不能找到上线，如果不能找到，那么进行关系设置
					List<Long> downs = downMaker.getDownList();
					for(Long id : downs) {
						ArticleMaker am = makerMap.get(id);
						if(am != null && am.getId() != up.getId()) {
							ArticleMaker find = relationDown.searchArticleMaker(am, up.getName());
							if(find != null) {
								//反转将导致死循环，不翻转
								if(logger.isDebugEnabled()) {
									logger.debug("[ArticleMoved] [两个在同一个组织关系内，平级关系本来要划线，但是可能导致死循环，保持不变] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
													new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
								}
								return;
							}
						}
					}
					//可以进行反转
					if(!downMaker.getUpList().contains(up.getId())) {
						downMaker.getUpList().add(new Long(up.getId()));
					}
					if(!upMaker.getDownList().contains(down.getId())) {
						upMaker.getDownList().add(new Long(down.getId()));
					}
					downMaker.addUp(up.getId(), amount);
					upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
					if(logger.isDebugEnabled()) {
						logger.debug("[ArticleMoved] [两个在同一个组织关系内，平级关系可以进行划线连接] [relation:{}] [down:{}] [up:{}] [amount:{}] [{}ms]", 
										new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), amount, (System.currentTimeMillis()-start)});
					}
					return;
				}
			} else {
				//合并这两个组织关系
				relationUp = relationShipMap.get(relationUpId);
				relationDown = relationShipMap.get(relationDownId);
				if(relationUp == null || relationDown == null) {
					if(relationDown == null) {
						playerRelationMap.remove(down.getId());
					}
					if(relationUp == null) {
						playerRelationMap.remove(up.getId());
					}
					if(logger.isDebugEnabled()) {
						logger.debug("[ArticleMoved] [两个在同一个组织关系内，但是有关系为NULL] [relation:{}] [down:{}] [up:{}] [{}ms]", 
								new Object[]{"null", down.getLogString(), up.getLogString(), (System.currentTimeMillis()-start)});
					}
					return;
				}
				downMaker  = makerMap.get(down.getId());
				if(downMaker == null) {
					playerRelationMap.remove(down.getId());
					relationShipMap.remove(relationDownId);
					articleMoved(down, up, amount);
					return;
				}
				refreshArticleMaker(downMaker, down);
				upMaker = makerMap.get(up.getId());
				if(upMaker == null) {
					playerRelationMap.remove(up.getId());
					relationShipMap.remove(relationUpId);
					articleMoved(down, up, amount);
					return;
				}
				refreshArticleMaker(upMaker, up);
				if(!relationDown.getTopLevelList().contains(downMaker.getId())) {
					relationUp.getTopLevelList().addAll(relationDown.getTopLevelList());
					refactorRelation(relationDown, relationUp.getId());
				}
				if(!downMaker.getUpList().contains(upMaker.getId())) {
					downMaker.getUpList().add(upMaker.getId());
				}
				if(!upMaker.getDownList().contains(downMaker.getId())) {
					upMaker.getDownList().add(downMaker.getId());	
				}
				playerRelationMap.put(downMaker.getId(), relationUp.getId());
				relationShipMap.remove(new Integer(relationDown.getId()));
				downMaker.addUp(up.getId(), amount);
				upMaker.setTotalSilver(upMaker.getTotalSilver()+amount);
				if(isForbid(relationUp)) {
					//如果上线是一家封禁的，那么标记为为封禁，让这个合并后的组织再次显露出来
					forbidShips.remove(new Integer(relationUp.getId()));
				}
				if(logger.isDebugEnabled()) {
					logger.debug("[ArticleMoved] [两个在不同的组织关系内，合并这两个关系] [relation:{}] [down:{}] [up:{}] [{}ms]", 
							new Object[]{relationUp.getId(), down.getLogString(), up.getLogString(), (System.currentTimeMillis()-start)});
				}
				return;
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("[ArticleMoved] [两个在同一个组织关系内，没进行任何设置，有可能是导致循环的转移] [relation:{}] [down:{}] [up:{}] [{}ms]", 
					new Object[]{"null", down.getLogString(), up.getLogString(), (System.currentTimeMillis()-start)});
		}
	}
	
	public void refactorRelation(ArticleRelationShip ship, int newShipId) {
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			ArticleMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorRelation(mm, newShipId);
			}
		}
	}
	
	public void refactorRelation(ArticleMaker maker, int newShipId) {
		playerRelationMap.put(maker.getId(), newShipId);
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			ArticleMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorRelation(mm, newShipId);
			}
		}
	}
	
	public static void main(String args[]) {
	}

	public int getRelationSequence() {
		return relationSequence;
	}

	public void setRelationSequence(int relationSequence) {
		this.relationSequence = relationSequence;
	}

	public HashMap<Long, ArticleMaker> getMakerMap() {
		return makerMap;
	}

	public void setMakerMap(HashMap<Long, ArticleMaker> makerMap) {
		this.makerMap = makerMap;
	}

	public HashMap<Integer, ArticleRelationShip> getRelationShipMap() {
		return relationShipMap;
	}

	public void setRelationShipMap(HashMap<Integer, ArticleRelationShip> relationShipMap) {
		this.relationShipMap = relationShipMap;
	}

	public HashMap<Long, Integer> getPlayerRelationMap() {
		return playerRelationMap;
	}

	public void setPlayerRelationMap(HashMap<Long, Integer> playerRelationMap) {
		this.playerRelationMap = playerRelationMap;
	}

	public ArrayList<Integer> getForbidShips() {
		return forbidShips;
	}

	public void setForbidShips(ArrayList<Integer> forbidShips) {
		this.forbidShips = forbidShips;
	}

	public HashSet<Long> getForbidPlayers() {
		return forbidPlayers;
	}

	public void setForbidPlayers(HashSet<Long> forbidPlayers) {
		this.forbidPlayers = forbidPlayers;
	}

	public List<ArticleMove> getMoveList() {
		return moveList;
	}

	public void setMoveList(List<ArticleMove> moveList) {
		this.moveList = moveList;
	}

	public String getDatafile() {
		return datafile;
	}
	
	public void checkForbidRelation(ArticleRelationShip ship) {
		int count = ship.getMaxDownCount();
		//统计IP地址
		HashMap<String,Integer> ipmap = ship.getUniqueIPAddress();
		int ipcount = ipmap.size();
		if(count >= forbidDownCount) {
			if(ipcount < count / 3) {
				this.forbidShip(ship, new ArrayList<Long>(), true);
				if(logger.isInfoEnabled()) {
					logger.info("[自动封禁组织] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"]");
				}
			} else {
				if(logger.isInfoEnabled()) {
					logger.info("[自动封禁组织:不满足IP地址数量要求] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"]");
				}
			}
		} 
		
//		else if(count >= forbidDownCountLow) {
//			//少数量的自动封禁，要求更严格，IP地址在3个以内，顶层成员数量在2个以内
//			if(ipcount <= 2 && ship.getTopLevelList().size() <= 1) {
//				this.forbidShip(ship, new ArrayList<Long>(), true);
//				if(logger.isInfoEnabled()) {
//					logger.info("[自动封禁组织] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"] [topLevel:"+ship.getTopLevelList().size()+"]");
//				}
//			} else {
//				if(logger.isInfoEnabled()) {
//					logger.info("[自动封禁组织:不满足IP地址数量或者顶层数量要求] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"] [topLevel:"+ship.getTopLevelList().size()+"]");
//				}
//			}
//		} else if(count >= forbidDownCountMaxLow) {
//			//最少数量的自动封禁，要求更严格，IP地址在2个以内，顶层成员1个
//			if(ipcount == 1 && ship.getTopLevelList().size() == 1) {
//				this.forbidShip(ship, new ArrayList<Long>(), true);
//				if(logger.isInfoEnabled()) {
//					logger.info("[自动封禁组织] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"] [topLevel:"+ship.getTopLevelList().size()+"]");
//				}
//			} else {
//				if(logger.isInfoEnabled()) {
//					logger.info("[自动封禁组织:不满足IP地址数量或者顶层数量要求] ["+ship.getId()+"] [最大下线:"+count+"] [ipcount:"+ipcount+"] [topLevel:"+ship.getTopLevelList().size()+"]");
//				}
//			}
//		}
	}
	
	public int countIp(HashMap<String,Integer> map, int min) {
		int count = 0;
		String keys[] = map.keySet().toArray(new String[0]);
		for(String k : keys) {
			Integer v = map.get(k);
			if(v > min) {
				count++;
			}
		}
		return count;
	}
	
	public HashSet<String> getCountIp(HashMap<String,Integer> map, int count) {
		HashSet<String> ips = new HashSet<String>();
		String keys[] = map.keySet().toArray(new String[0]);
		for(String k : keys) {
			Integer v = map.get(k);
			if(v == count) {
				ips.add(k);
			}
		}
		return ips;
	}
}
