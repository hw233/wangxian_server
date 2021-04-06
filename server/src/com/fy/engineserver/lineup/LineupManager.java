package com.fy.engineserver.lineup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.battlefield.concrete.BattleFieldLineupService;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_lineup_entercity;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.TeamSubSystem;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

/**
 * 排队控制类
 * 
 *
 */
public class LineupManager implements Runnable,ConfigFileChangedListener {
	
//	public static Logger log = Logger.getLogger(LineupManager.class.getName());
public	static Logger log = LoggerFactory.getLogger(LineupManager.class.getName());
	
	protected static LineupManager self;
	
	protected DownCityManager downCityManager;
	protected String cityXml = "";
	
	protected Hashtable<Integer, List<DownCityInfo>> levelCityMapZiwei;
	protected Hashtable<Integer, List<DownCityInfo>> levelCityMapRiyue;
	
	protected Hashtable<Integer, List<DownCityInfo>> randomLevelCityMapZiwei;
	protected Hashtable<Integer, List<DownCityInfo>> randomLevelCityMapRiyue;
	
	protected Hashtable<Integer, List<DownCityInfo>> levelCityHeroMapZiwei;
	protected Hashtable<Integer, List<DownCityInfo>> levelCityHeroMapRiyue;
	
	protected Hashtable<Integer, List<DownCityInfo>> randomLevelCityHeroMapZiwei;
	protected Hashtable<Integer, List<DownCityInfo>> randomLevelCityHeroMapRiyue;
	
	protected int startlevel;
	
	public Hashtable<String,Long> lineupTimePoint = new Hashtable<String,Long>();
	
	/**
	 * 各个副本排队的列表
	 */
	public Hashtable<String, List<LineupPlayer>> lineupMap;
	
	public static final Object create_lock = new Object(){};
	
	/**
	 * 配队线程
	 */
	public Thread localThread;
	
	public boolean testting;
			
	public static LineupManager getInstance() {
		return self;
	}
		
	public void initialize() throws Exception{
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		lineupMap = new Hashtable<String, List<LineupPlayer>>();
		reconfigure(new File(cityXml));
//		log.debug("[系统初始化] [排队管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)+"毫秒]");
		if (log.isDebugEnabled())
		log.debug("[系统初始化] [排队管理器] [初始化完成] [{}] [耗时：{}毫秒]", new Object[]{getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)});
		System.out.println("[系统初始化] [排队管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)+"毫秒]");
		ConfigFileChangedAdapter.getInstance().addListener(new File(cityXml),this);
		self = this;
		localThread = new Thread(this, Translate.text_4675);
		localThread.start();
	}
	
	public void fileChanged(File cityXml) {
		reconfigure(cityXml);
	}
	
	public void reconfigure(File cityXml) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Hashtable<Integer, List<DownCityInfo>> _levelCityMapZiwei = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _levelCityMapRiyue = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _randomLevelCityMapZiwei = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _randomLevelCityMapRiyue = new Hashtable<Integer, List<DownCityInfo>>();
		//勇士副本数据
		Hashtable<Integer, List<DownCityInfo>> _levelCityHeroMapZiwei = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _levelCityHeroMapRiyue = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _randomLevelCityHeroMapZiwei = new Hashtable<Integer, List<DownCityInfo>>();
		Hashtable<Integer, List<DownCityInfo>> _randomLevelCityHeroMapRiyue = new Hashtable<Integer, List<DownCityInfo>>();
		try {
			Configuration conf = new DefaultConfigurationBuilder().buildFromFile(cityXml);
			this.startlevel = conf.getChild("startlevel").getValueAsInteger(12);
			Configuration cities[] = conf.getChild("city-list").getChildren("city");
			for(Configuration city : cities) {
				byte polcamp = (byte)city.getAttributeAsInteger("polcamp", -1);
				int minlevel = city.getAttributeAsInteger("minlevel", 1);
				int maxlevel = city.getAttributeAsInteger("maxlevel", 100);
				byte downCityType = (byte)city.getAttributeAsInteger("downCityType", 0);
				String dcName = TransferLanguage.transferString(city.getAttribute("name", ""));
				DownCityInfo dci = downCityManager.getDownCityInfo(dcName);
				if(dci != null) {
//					log.debug("[初始化配置] [副本信息] ["+dcName+"] [polcamp:"+polcamp+"] [level:"+dci.getMinPlayerLevel()+"]");
					if (log.isDebugEnabled())
					log.debug("[初始化配置] [副本信息] [{}] [polcamp:{}] [level:{}]", new Object[]{dcName,polcamp,dci.getMinPlayerLevel()});
					int needlevel = dci.getMinPlayerLevel();
					if(downCityType == 0){
						if(polcamp == 1 || polcamp == -1) {
							for(int i=needlevel; i<=100; i++) {
								List<DownCityInfo> levelList = _levelCityMapZiwei.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_levelCityMapZiwei.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
							for(int i=minlevel; i<=maxlevel; i++) {
								List<DownCityInfo> levelList = _randomLevelCityMapZiwei.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_randomLevelCityMapZiwei.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
						} 
						if(polcamp == 2 || polcamp == -1) {
							for(int i=needlevel; i<=100; i++) {
								List<DownCityInfo> levelList = _levelCityMapRiyue.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_levelCityMapRiyue.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
							for(int i=minlevel; i<=maxlevel; i++) {
								List<DownCityInfo> levelList = _randomLevelCityMapRiyue.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_randomLevelCityMapRiyue.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
						}
					}else{
						if(polcamp == 1 || polcamp == -1) {
							for(int i=needlevel; i<=100; i++) {
								List<DownCityInfo> levelList = _levelCityHeroMapZiwei.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_levelCityHeroMapZiwei.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
							for(int i=minlevel; i<=maxlevel; i++) {
								List<DownCityInfo> levelList = _randomLevelCityHeroMapZiwei.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_randomLevelCityHeroMapZiwei.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
						} 
						if(polcamp == 2 || polcamp == -1) {
							for(int i=needlevel; i<=100; i++) {
								List<DownCityInfo> levelList = _levelCityHeroMapRiyue.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_levelCityHeroMapRiyue.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
							for(int i=minlevel; i<=maxlevel; i++) {
								List<DownCityInfo> levelList = _randomLevelCityHeroMapRiyue.get(i);
								if(levelList == null) {
									levelList = new ArrayList<DownCityInfo>();
									_randomLevelCityHeroMapRiyue.put(new Integer(i), levelList);
								}
								if(!levelList.contains(dci)) {
									levelList.add(dci);
								}
							}
						}
					}
					
				} else {
//					log.warn("[警告] [配置的副本不存在] [name:"+dcName+"] [polcamp:"+polcamp+"]");
					if(log.isWarnEnabled())
						log.warn("[警告] [配置的副本不存在] [name:{}] [polcamp:{}]", new Object[]{dcName,polcamp});
				}
			}
			this.levelCityMapZiwei = _levelCityMapZiwei;
			this.levelCityMapRiyue = _levelCityMapRiyue;
			this.randomLevelCityMapZiwei = _randomLevelCityMapZiwei;
			this.randomLevelCityMapRiyue = _randomLevelCityMapRiyue;
			this.levelCityHeroMapZiwei = _levelCityHeroMapZiwei;
			this.levelCityHeroMapRiyue = _levelCityHeroMapRiyue;
			this.randomLevelCityHeroMapZiwei = _randomLevelCityHeroMapZiwei;
			this.randomLevelCityHeroMapRiyue = _randomLevelCityHeroMapRiyue;
//			log.debug("[配置加载完成] [startlevel:"+startlevel+"] [副本总数:"+cities.length+"] [levelCityMapZiwei:"+levelCityMapZiwei.size()+"] [levelCityMapRiyue:"+levelCityMapRiyue.size()+"] [randomLevelCityMapZiwei:"+randomLevelCityMapZiwei.size()+"] [randomLevelCityMapRiyue:"+randomLevelCityMapRiyue.size()+"[levelCityHeroMapZiwei:"+levelCityHeroMapZiwei.size()+"] [levelCityHeroMapRiyue:"+levelCityHeroMapRiyue.size()+"] [randomLevelCityHeroMapZiwei:"+randomLevelCityHeroMapZiwei.size()+"] [randomLevelCityHeroMapRiyue:"+randomLevelCityHeroMapRiyue.size()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[配置加载完成] [startlevel:{}] [副本总数:{}] [levelCityMapZiwei:{}] [levelCityMapRiyue:{}] [randomLevelCityMapZiwei:{}] [randomLevelCityMapRiyue:{}[levelCityHeroMapZiwei:{}] [levelCityHeroMapRiyue:{}] [randomLevelCityHeroMapZiwei:{}] [randomLevelCityHeroMapRiyue:{}] [{}ms]", new Object[]{startlevel,cities.length,levelCityMapZiwei.size(),levelCityMapRiyue.size(),randomLevelCityMapZiwei.size(),randomLevelCityMapRiyue.size(),levelCityHeroMapZiwei.size(),levelCityHeroMapRiyue.size(),randomLevelCityHeroMapZiwei.size(),randomLevelCityHeroMapRiyue.size(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		} catch (Exception e) {
			log.error("[重新加载排队副本信息时出错]", e);
		} 
	}
	
	public void setDownCityManager(DownCityManager downCityManager) {
		this.downCityManager = downCityManager;
	}

	public void setCityXml(String cityXml) {
		this.cityXml = cityXml;
	}

	public int getStartlevel() {
		return startlevel;
	}

	public void setStartlevel(int startlevel) {
		this.startlevel = startlevel;
	}

	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				synchronized(localThread) {
					try {
						localThread.wait(5000);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				String keys[] = lineupMap.keySet().toArray(new String[0]);
				int queueNum = 0;
				for(String dcName : keys) {
					List<LineupPlayer> lps = lineupMap.get(dcName);
					if(lps != null && lps.size() > 0) {
						notifyTeamBuilding(dcName, lps);
						queueNum += lps.size();
					}
					if(lps != null)  {
						StringBuffer sb = new StringBuffer();
						for(LineupPlayer lp : lps) {
							sb.append(TeamRole.getRoleDesp(lp.teamrole) + ",");
						}
						String roles = sb.toString();
						if(roles.length() > 0) {
							roles = roles.substring(0, roles.length()-1);
						}
						if(log.isDebugEnabled()) {
//							log.debug("[组队线程] [完成副本组队] [目前排队:"+roles+"]");
							if(log.isDebugEnabled())
								log.debug("[组队线程] [完成副本组队] [目前排队:{}]", new Object[]{roles});
						}
					}
				}
				if(log.isDebugEnabled()) {
//					log.debug("[组队线程] [运行] [队列中玩家:"+queueNum+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
					if(log.isDebugEnabled())
						log.debug("[组队线程] [运行] [队列中玩家:{}] [{}ms]", new Object[]{queueNum,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
				}
			} catch(Throwable e) {
				log.error("[自由组队时发生异常]", e);
			}
		}
	}
	
	/**
	 * 得到玩家可以进入的普通副本
	 * @param player
	 * @return
	 */
	public List<DownCityInfo> getPlayerCanEnterCommonCities(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<DownCityInfo> list = null;
		if(player.getCountry() == (byte)1) {
			list = levelCityMapZiwei.get(new Integer(player.getLevel()));
		} else if(player.getCountry() == (byte)2) {
			list = levelCityMapRiyue.get(new Integer(player.getLevel()));
		}
		if(list == null) {
			list = new ArrayList<DownCityInfo>();
		}
//		log.debug("[获取玩家可进入的副本] ["+player.getId()+"] ["+player.getName()+"] ["+player.getLevel()+"] [数量:"+list.size()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[获取玩家可进入的副本] [{}] [{}] [{}] [数量:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getLevel(),list.size(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return list;
	}
	/**
	 * 得到玩家可以进入的勇士副本
	 * @param player
	 * @return
	 */
	public List<DownCityInfo> getPlayerCanEnterHeroCities(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<DownCityInfo> list = null;
		if(player.getCountry() == (byte)1) {
			list = levelCityHeroMapZiwei.get(new Integer(player.getLevel()));
		} else if(player.getCountry() == (byte)2) {
			list = levelCityHeroMapRiyue.get(new Integer(player.getLevel()));
		}
		if(list == null) {
			list = new ArrayList<DownCityInfo>();
		}
//		log.debug("[获取玩家可进入的副本] ["+player.getId()+"] ["+player.getName()+"] ["+player.getLevel()+"] [数量:"+list.size()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[获取玩家可进入的副本] [{}] [{}] [{}] [数量:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getLevel(),list.size(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return list;
	}
	/**
	 * 得到玩家可以进入的随机普通副本
	 * @param player
	 * @return
	 */
	public List<DownCityInfo> getPlayerCanEnterRandomCommonCities(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<DownCityInfo> list = null;
		if(player.getCountry() == (byte)1) {
			list = randomLevelCityMapZiwei.get(new Integer(player.getLevel()));
		} else if(player.getCountry() == (byte)2) {
			list = randomLevelCityMapRiyue.get(new Integer(player.getLevel()));
		}
		if(list == null) {
			list = new ArrayList<DownCityInfo>();
		}
//		log.debug("[获取玩家可进入的副本] ["+player.getId()+"] ["+player.getName()+"] ["+player.getLevel()+"] [数量:"+list.size()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[获取玩家可进入的副本] [{}] [{}] [{}] [数量:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getLevel(),list.size(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return list;
	}
	/**
	 * 得到玩家可以进入的随机勇士副本
	 * @param player
	 * @return
	 */
	public List<DownCityInfo> getPlayerCanEnterRandomHeroCities(Player player) {
		
		return null;
	}
	/**
	 * 玩家随机选取一个能进入的副本
	 * @param player
	 * @return
	 */
	public DownCityInfo randomPlayerCity(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<DownCityInfo> list = null;
		if(player.getCountry() == (byte)1) {
			list = randomLevelCityMapZiwei.get(new Integer(player.getLevel()));
		} else if(player.getCountry() == (byte)2) {
			list = randomLevelCityMapRiyue.get(new Integer(player.getLevel()));
		}
		if(list != null && list.size() > 0) {
			DownCityInfo dci  = list.get(new java.util.Random().nextInt(list.size()));
//			log.debug("[随机获取一个可排队的副本] ["+player.getId()+"] ["+player.getName()+"] ["+player.getLevel()+"] [副本:"+dci.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[随机获取一个可排队的副本] [{}] [{}] [{}] [副本:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getLevel(),dci.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return dci;
		} 
//		log.debug("[随机获取一个可排队的副本] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+player.getLevel()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[随机获取一个可排队的副本] [失败] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getLevel(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return null;
	}
	
	/**
	 * 玩家是否在此副本的队列中
	 * @param player
	 * @param downCityName
	 * @return
	 */
	public boolean isPlayerInQueue(Player player, String downCityName) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<LineupPlayer> list = lineupMap.get(downCityName);
		LineupPlayer lp = new LineupPlayer();
		lp.player = player;
		boolean in = false;
		if(list != null && list.contains(lp)) {
			in = true;
		}
//		log.debug("[玩家是否在队列中] [在队列中:"+in+"] ["+player.getId()+"] ["+player.getName()+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[玩家是否在队列中] [在队列中:{}] [{}] [{}] [{}] [{}ms]", new Object[]{in,player.getId(),player.getName(),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return in;
	}
	
	/**
	 * 玩家是否在副本排队中
	 * @param player
	 * @return
	 */
	public boolean isPlayerInQueue(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String keys[] = lineupMap.keySet().toArray(new String[0]);
		boolean in = false;
		LineupPlayer lp = new LineupPlayer();
		lp.player = player;
		for(String dcName : keys) {
			List<LineupPlayer> list = lineupMap.get(dcName);
			if(list != null && list.contains(lp)) {
				in = true;
				break;
			}
		}
//		log.debug("[玩家是否在任一队列中] [在队列中:"+in+"] ["+player.getId()+"] ["+player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		if (log.isDebugEnabled())
		log.debug("[玩家是否在任一队列中] [在队列中:{}] [{}] [{}] [{}ms]", new Object[]{in,player.getId(),player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		return in;
	}

	/**
	 * 副本玩家排队，如果可以排队，那么返回true
	 * @param player
	 * @param teamroleId 玩家在队伍中的角色
	 * @param downCityName
	 * @return
	 */
	public boolean takein(Player player, byte teamroleId, String downCityName) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		//如果玩家已经在战场队列中，那么不能进行排队
		BattleFieldLineupService bs = BattleFieldLineupService.getInstance();
		if(bs.isPlayerInBattleLineup(player)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4676);
			player.addMessageToRightBag(req);
//			log.debug("[玩家排队] [失败:玩家已经在战场队列] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[玩家排队] [失败:玩家已经在战场队列] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return false;
		}
		
		DownCityManager dcmanager = DownCityManager.getInstance();
		DownCityInfo dci = dcmanager.getDownCityInfo(downCityName);
		
		if(!playerCanEnterCity(player, downCityName)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4677);
			player.addMessageToRightBag(req);
//			log.debug("[玩家排队] [失败:玩家不能进入此副本] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[玩家排队] [失败:玩家不能进入此副本] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return false;
		}
		
		//玩家已经在此副本队列中
		if(this.isPlayerInQueue(player, downCityName)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4678);
			player.addMessageToRightBag(req);
//			log.debug("[玩家排队] [玩家已经在队列中] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[玩家排队] [玩家已经在队列中] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return false;
		}
		
		if(dci == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4679);
			player.addMessageToRightBag(req);
//			log.debug("[玩家排队] [失败:玩家选择的副本不存在] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled())
			log.debug("[玩家排队] [失败:玩家选择的副本不存在] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return false;
		}
		List<LineupPlayer> lplist = lineupMap.get(downCityName);
		if(lplist == null) {
			synchronized(create_lock) {
				lplist = lineupMap.get(downCityName);
				if(lplist == null) {
					lplist = Collections.synchronizedList(new LinkedList<LineupPlayer>());
					lineupMap.put(downCityName, lplist);
				}
			}
		}
		LineupPlayer lp = new LineupPlayer();
		lp.player = player;
		lp.teamrole = teamroleId;
		boolean added = false;
		if(!lplist.contains(lp)) {
			synchronized(lplist) {
				if(!lplist.contains(lp)) {
					lplist.add(lp);
					added = true;
				}
			}
		}
		if(added) {
			synchronized(localThread) {
				try {
					localThread.notify();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4680);
			player.addMessageToRightBag(req);
//			log.debug("[玩家排队] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
				if (log.isDebugEnabled()) log.debug("[玩家排队] [成功] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			lineupTimePoint.put(downCityName+player.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_副本排队, downCityName, 0, new java.util.Date(), true);
			return true;
		} else {
//			log.debug("[玩家排队] [玩家已经在队列中] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if(log.isDebugEnabled())
				log.debug("[玩家排队] [玩家已经在队列中] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
			return false;
		}
	}
	/**
	 * 副本玩家排队，如果可以排队，那么返回true
	 * @param player
	 * @param teamroleId 玩家在队伍中的角色
	 * @param downCityNames 副本集合名字
	 * @return
	 */
	public boolean takein(Player player, byte teamroleId, String[] downCityNames) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		if(downCityNames == null){
			return false;
		}
		boolean takeinok = false;
		for(String downCityName : downCityNames){
			//如果玩家已经在战场队列中，那么不能进行排队
			BattleFieldLineupService bs = BattleFieldLineupService.getInstance();
			if(bs.isPlayerInBattleLineup(player)) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4676);
				player.addMessageToRightBag(req);
//				log.debug("[玩家排队] [失败:玩家已经在战场队列] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
					if (log.isDebugEnabled()) log.debug("[玩家排队] [失败:玩家已经在战场队列] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
				return false;
			}
			
			DownCityManager dcmanager = DownCityManager.getInstance();
			DownCityInfo dci = dcmanager.getDownCityInfo(downCityName);
			
			if(!playerCanEnterCity(player, downCityName)) {
				continue;
			}
			
			//玩家已经在此副本队列中
			if(this.isPlayerInQueue(player, downCityName)) {
				continue;
			}
			
			if(dci == null) {
				continue;
			}
			List<LineupPlayer> lplist = lineupMap.get(downCityName);
			if(lplist == null) {
				synchronized(create_lock) {
					lplist = lineupMap.get(downCityName);
					if(lplist == null) {
						lplist = Collections.synchronizedList(new LinkedList<LineupPlayer>());
						lineupMap.put(downCityName, lplist);
					}
				}
			}
			LineupPlayer lp = new LineupPlayer();
			lp.player = player;
			lp.teamrole = teamroleId;
			boolean added = false;
			if(!lplist.contains(lp)) {
				synchronized(lplist) {
					if(!lplist.contains(lp)) {
						lplist.add(lp);
						added = true;
						takeinok = true;
					}
				}
			}
			if(added) {
//				log.debug("[玩家排队] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+TeamRole.getRoleDesp(teamroleId)+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
					if (log.isDebugEnabled()) log.debug("[玩家排队] [成功] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getId(),player.getName(),TeamRole.getRoleDesp(teamroleId),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
				lineupTimePoint.put(downCityName+player.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				Player.sendPlayerAction(player, PlayerActionFlow.行为类型_副本排队, downCityName, 0, new java.util.Date(), true);
			}
		}
		if(takeinok) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4680);
			player.addMessageToRightBag(req);
			synchronized(localThread) {
				try {
					localThread.notify();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_4681);
			player.addMessageToRightBag(req);
		}
		return takeinok;
	}
	/**
	 * 玩家是否能进入副本
	 * @param player
	 * @param downCityName
	 * @return
	 */
	public boolean playerCanEnterCity(Player player, String downCityName) {
		DownCityManager dcmanager = DownCityManager.getInstance();
		DownCityInfo dci = dcmanager.getDownCityInfo(downCityName);
		if(dci == null) {
			return false;
		}
		List<DownCityInfo> list = this.getPlayerCanEnterCommonCities(player);
		if(list != null) {
			for(DownCityInfo info : list) {
				if(info.getName().equals(dci.getName())) {
					return true;
				}
			}
		}
		List<DownCityInfo> list2 = this.getPlayerCanEnterHeroCities(player);
		if(list2 != null) {
			for(DownCityInfo info : list2) {
				if(info.getName().equals(dci.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 玩家取消所有排队
	 * @param player
	 * @return 如果玩家在队列中，并且离开了，返回true
	 */
	public boolean deLineup(Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		LineupPlayer lp = new LineupPlayer();
		lp.player = player;
		boolean leaved = false;
		try {
			String keys[] = lineupMap.keySet().toArray(new String[0]);
			for(String dc : keys) {
				List<LineupPlayer> lps = lineupMap.get(dc);
				if(lps.contains(lp)) {
					synchronized(lps) {
						if(lps.contains(lp)) {
							lps.remove(lp);
//							log.debug("[取消排队,玩家主动取消] ["+dc+"] ["+player.getId()+"] ["+player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms] [排队时长:"+(lineupTimePoint.get(dc+player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+player.getId())):"排队时间出错")+"]");
								if (log.isDebugEnabled()) log.debug("[取消排队,玩家主动取消] [{}] [{}] [{}] [{}ms] [排队时长:{}]", new Object[]{dc,player.getId(),player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start),(lineupTimePoint.get(dc+player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+player.getId())):"排队时间出错")});
							leaved = true;
						}
					}
				}
			}
		} catch(Throwable e) {
			log.error("[玩家取消排队时发生异常] ["+lp.player.getId()+"] ["+lp.player.getName()+"]", e);
		}
//		log.debug("[玩家取消排队] [leaved:"+leaved+"] ["+player.getId()+"] ["+player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");			
			if (log.isDebugEnabled()) log.debug("[玩家取消排队] [leaved:{}] [{}] [{}] [{}ms]", new Object[]{leaved,player.getId(),player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});			
		return leaved;
	}
	
	/**
	 * 通知进行自由组队
	 * @param downCityName
	 * @param lplist
	 */
	public void notifyTeamBuilding(String downCityName, List<LineupPlayer> lplist) {
		DownCityManager dcmanager = DownCityManager.getInstance();
		DownCityInfo dci = dcmanager.getDownCityInfo(downCityName);
		if(dci == null) {
			return;
		}
		try {
			TeamTemplate ttZiwei = null;
			TeamTemplate ttRiyue = null;
			if(dci.getPlayerNumLimit() == 5) {
				ttZiwei = new Team5Template();
				ttRiyue = new Team5Template();
			}
			if(dci.getPlayerNumLimit() == 10) {
				ttZiwei = new Team10Template();
				ttRiyue = new Team10Template();
			}
			boolean ziweiOK = false;
			boolean riyueOK = false;
			List<LineupPlayer> queuePlayer = new ArrayList<LineupPlayer>();
			queuePlayer.addAll(lplist);
			for(int i=0; i<queuePlayer.size(); i++) {
				LineupPlayer lp = queuePlayer.get(i);
				if(!lp.player.isOnline()) {
					clearLineupPlayerFromQueue(lp);
					continue;
				} else if ( lp.player.isDeath() || lp.player.getCurrentGame() == null ) {
					//死亡或者在跳地图，那么不进行组队
					continue;
				} else if(downCityManager.getDownCityInfoByMapName(lp.player.getCurrentGame().getGameInfo().getName()) != null
						&& lp.player.getCurrentGame().getGameInfo().getName().equals(downCityName)) {
					//玩家在此副本中，那么直接从队列中去掉
					clearLineupPlayerFromQueue(lp);
					continue;
				}
				if(lp.player.getCountry() == 1) {
					boolean succ = ttZiwei.takein(lp);
					if(succ && ttZiwei.isFull()) {
						ziweiOK = true;
						break;
					}
				} else if(lp.player.getCountry() == 2) {
					boolean succ = ttRiyue.takein(lp);
					if(succ && ttRiyue.isFull()) {
						riyueOK = true;
						break;
					}
				}
			}
			if(ziweiOK || riyueOK) {
				if(ziweiOK) {
					//紫薇组合已经OK
					StringBuffer sb = new StringBuffer();
					List<LineupPlayer> lps = ttZiwei.getTeamplayers();
					for(int i=0; i<lps.size(); i++) {
						clearLineupPlayerFromQueue(lps.get(i));
//						log.debug("[取消排队,玩家成功组好队伍] ["+downCityName+"] ["+lps.get(i).player.getId()+"] ["+lps.get(i).player.getName()+"] [--] [排队时长:"+(lineupTimePoint.get(downCityName+lps.get(i).player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(downCityName+lps.get(i).player.getId())):"排队时间出错")+"]");
							if (log.isDebugEnabled()) log.debug("[取消排队,玩家成功组好队伍] [{}] [{}] [{}] [--] [排队时长:{}]", new Object[]{downCityName,lps.get(i).player.getId(),lps.get(i).player.getName(),(lineupTimePoint.get(downCityName+lps.get(i).player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(downCityName+lps.get(i).player.getId())):"排队时间出错")});
						Player.sendPlayerAction(lps.get(i).player, PlayerActionFlow.行为类型_副本排队组队成功, downCityName, 0, new Date(), true);
						sb.append(lps.get(i).player.getId()+"/"+lps.get(i).player.getName()+"/"+TeamRole.getRoleDesp(lps.get(i).teamrole)+ ",");
					}
					String lpstr = sb.toString();
					if(lpstr.length() > 0) {
						lpstr = lpstr.substring(0, lpstr.length()-1);
					}
					notifyBuildTeamAndEnterDownCity(ttZiwei, downCityName);
//					log.debug("[组合成功] [紫薇] ["+downCityName+"] ["+lpstr+"]");
						if (log.isDebugEnabled()) log.debug("[组合成功] [紫薇] [{}] [{}]", new Object[]{downCityName,lpstr});
				}
				if(riyueOK) {
					//日月组合已经OK
					StringBuffer sb = new StringBuffer();
					List<LineupPlayer> lps = ttRiyue.getTeamplayers();
					for(int i=0; i<lps.size(); i++) {
						clearLineupPlayerFromQueue(lps.get(i));
//						log.debug("[取消排队,玩家成功组好队伍] ["+downCityName+"] ["+lps.get(i).player.getId()+"] ["+lps.get(i).player.getName()+"] [--] [排队时长:"+(lineupTimePoint.get(downCityName+lps.get(i).player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(downCityName+lps.get(i).player.getId())):"排队时间出错")+"]");
							if (log.isDebugEnabled()) log.debug("[取消排队,玩家成功组好队伍] [{}] [{}] [{}] [--] [排队时长:{}]", new Object[]{downCityName,lps.get(i).player.getId(),lps.get(i).player.getName(),(lineupTimePoint.get(downCityName+lps.get(i).player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(downCityName+lps.get(i).player.getId())):"排队时间出错")});
						Player.sendPlayerAction(lps.get(i).player, PlayerActionFlow.行为类型_副本排队组队成功, downCityName, 0, new Date(), true);
						sb.append(lps.get(i).player.getId()+"/"+lps.get(i).player.getName()+"/"+TeamRole.getRoleDesp(lps.get(i).teamrole)+ ",");
					}
					String lpstr = sb.toString();
					if(lpstr.length() > 0) {
						lpstr = lpstr.substring(0, lpstr.length()-1);
					}
					notifyBuildTeamAndEnterDownCity(ttRiyue, downCityName);
//					log.debug("[组合成功] [日月] ["+downCityName+"] ["+lpstr+"]");
						if (log.isDebugEnabled()) log.debug("[组合成功] [日月] [{}] [{}]", new Object[]{downCityName,lpstr});
				}
				notifyTeamBuilding(downCityName, lplist);
			}
		} catch(Exception e) {
			log.error("[自由组队时发生异常]", e);
		}
	}
	
	/**
	 * 把排队的玩家从队列里清除
	 * @param lp
	 */
	public void clearLineupPlayerFromQueue(LineupPlayer lp) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			String keys[] = lineupMap.keySet().toArray(new String[0]);
			for(String dc : keys) {
				List<LineupPlayer> lps = lineupMap.get(dc);
				if(lps.contains(lp)) {
					lps.remove(lp);
					if(!lp.player.isOnline()) {
//						log.debug("[取消排队,玩家不在线] ["+dc+"] ["+lp.player.getId()+"] ["+lp.player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms] [排队时长:"+(lineupTimePoint.get(dc+lp.player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+lp.player.getId())):"排队时间出错")+"]");
							if (log.isDebugEnabled()) log.debug("[取消排队,玩家不在线] [{}] [{}] [{}] [{}ms] [排队时长:{}]", new Object[]{dc,lp.player.getId(),lp.player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start),(lineupTimePoint.get(dc+lp.player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+lp.player.getId())):"排队时间出错")});
					} else if ( lp.player.isDeath() || lp.player.getCurrentGame() == null ) {
						//死亡或者在跳地图，那么不进行组队

					} else if(downCityManager.getDownCityInfoByMapName(lp.player.getCurrentGame().getGameInfo().getName()) != null
							&& lp.player.getCurrentGame().getGameInfo().getName().equals(dc)) {
						//玩家在此副本中，那么直接从队列中去掉
//						log.debug("[取消排队,玩家已经在副本中] ["+dc+"] ["+lp.player.getId()+"] ["+lp.player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms] [排队时长:"+(lineupTimePoint.get(dc+lp.player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+lp.player.getId())):"排队时间出错")+"]");
							if (log.isDebugEnabled()) log.debug("[取消排队,玩家已经在副本中] [{}] [{}] [{}] [{}ms] [排队时长:{}]", new Object[]{dc,lp.player.getId(),lp.player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start),(lineupTimePoint.get(dc+lp.player.getId()) != null ? (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lineupTimePoint.get(dc+lp.player.getId())):"排队时间出错")});
					}
				}
			}
//			log.debug("[取消玩家排队] ["+lp.player.getId()+"] ["+lp.player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
				if (log.isDebugEnabled()) log.debug("[取消玩家排队] [{}] [{}] [{}ms]", new Object[]{lp.player.getId(),lp.player.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		} catch(Throwable e) {
			log.error("[清理排队信息发生异常] ["+lp.player.getId()+"] ["+lp.player.getName()+"]", e);
		}
	}
	
	/**
	 * 进行组队并且通知玩家进入副本
	 * @param tt
	 * @param downCityName
	 */
	public void notifyBuildTeamAndEnterDownCity(TeamTemplate tt, String downCityName) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		DownCityManager dcmanager = DownCityManager.getInstance();
		DownCityInfo dci = dcmanager.getDownCityInfo(downCityName);
		if(dci == null) {
			return;
		}
		List<LineupPlayer> lps = tt.getTeamplayers();
		//检查组队信息，如果在队伍中，全部离开队伍
		for(LineupPlayer lp : lps) {
			if(lp.player.getTeamMark() == Player.TEAM_MARK_CAPTAIN) {
				Team team = lp.player.getTeam();
				if (team != null) {
					Player ps[] = team.getMembers().toArray(new Player[0]);
					team.dissolve();
					//让客户端清空队友
					for(int i = 0 ; i < ps.length ; i++){
						if(ps[i].getAroundNotifyPlayerNum() == 0){
							ps[i].setNewlySetAroundNotifyPlayerNum(true);
						}
					}
				}
				if (log.isInfoEnabled()) {
//					log.info("[自由组合成功] [玩家在老队伍中：解散队伍] [成功] [" + lp.player.getName() + "] [队长解散队伍]");
					if(log.isInfoEnabled())
						log.info("[自由组合成功] [玩家在老队伍中：解散队伍] [成功] [{}] [队长解散队伍]", new Object[]{lp.player.getName()});
				}
			} else if(lp.player.getTeamMark() != Player.TEAM_MARK_NONE) {
				Team team = lp.player.getTeam();
				if (team != null) {
					Player ps[] = team.getMembers().toArray(new Player[0]);
					team.removeMember(lp.player, 0);
					//让客户端清空费队友
					for(int i = 0 ; i < ps.length ; i++){
						if(ps[i].getAroundNotifyPlayerNum() == 0){
							ps[i].setNewlySetAroundNotifyPlayerNum(true);
						}
					}
					if(log.isInfoEnabled()){
//						log.info("[自由组合成功] [玩家在老队伍中：离开队伍] [成功] [" + lp.player.getName() + "] [队员离开]");
						if(log.isInfoEnabled())
							log.info("[自由组合成功] [玩家在老队伍中：离开队伍] [成功] [{}] [队员离开]", new Object[]{lp.player.getName()});
					}
				} 
			}
		}
		//创建队伍
		Team team = new Team(TeamSubSystem.nextId(), lps.get(0).player, lps.size());
		//系统默认分配方案
		team.setAssignRule((byte)1, (byte)2);
		for(int i=0; i<lps.size(); i++) {
			if(i>0) {
				team.addMember(lps.get(i).player);
			}
			//目地是为了看见队友
			if(lps.get(i).player.getAroundNotifyPlayerNum() == 0){
				lps.get(i).player.setNewlyEnterGameFlag(true);
			}
			//以下弹窗通知客户端，是否进入副本
			
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setTitle(Translate.text_419);
			
			Option ops[] = new Option[2];
			Option_lineup_entercity oc = new Option_lineup_entercity(downCityName);
			ops[0] = oc;
			oc.setText(Translate.text_4682 + downCityName);
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			
			ops[1] = new Option_Cancel();
			ops[1].setOptionId(StringUtil.randomString(10));
			ops[1].setText(Translate.text_4683);
			ops[1].setColor(0xffffff);
			
			mw.setOptions(ops);
			
			mw.setDescriptionInUUB(Translate.text_4684);
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
			lps.get(i).player.addMessageToRightBag(res);
			
//			log.debug("[排队进入副本] [弹出进入副本通知] ["+lps.get(i).player.getId()+"] ["+lps.get(i).player.getName()+"] ["+downCityName+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"]");
				if (log.isDebugEnabled()) log.debug("[排队进入副本] [弹出进入副本通知] [{}] [{}] [{}] [{}]", new Object[]{lps.get(i).player.getId(),lps.get(i).player.getName(),downCityName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		}
		
		for(int i=0; i<lps.size(); i++) {
			ChatMessage mes = new ChatMessage();
			mes.setSort(ChatChannelType.TEAM);
			mes.setMessageText(Translate.text_4685 + lps.get(i).player.getName() + Translate.text_4686 + TeamRole.getRoleDesp(lps.get(i).teamrole));
			ChatMessageService.getInstance().sendMessageToTeam(team, mes);
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<lps.size(); i++) {
			sb.append(lps.get(i).player.getId()+"/"+lps.get(i).player.getName()+"/"+TeamRole.getRoleDesp(lps.get(i).teamrole)+ ",");
		}
		String lpstr = sb.toString();
		if(lpstr.length() > 0) {
			lpstr = lpstr.substring(0, lpstr.length()-1);
		}
//		log.debug("[完成组队和弹窗通知] ["+downCityName+"] ["+lps.size()+"] ["+lpstr+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
			if (log.isDebugEnabled()) log.debug("[完成组队和弹窗通知] [{}] [{}] [{}] [{}ms]", new Object[]{downCityName,lps.size(),lpstr,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)});
		
	}

}
