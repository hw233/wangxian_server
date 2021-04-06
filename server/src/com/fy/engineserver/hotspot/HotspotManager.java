package com.fy.engineserver.hotspot;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HOTSPOT_OPEN_RES;
import com.fy.engineserver.message.HOTSPOT_OVER_RES;
import com.fy.engineserver.message.NOTIFY_HOTSPOT_CHANGE_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.HotspotTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
//热点管理器
public class HotspotManager implements Runnable {

	private ArrayList<Hotspot> baseHotspot; // 基础热点

	public int sleepTime = 5 * 60 * 1000;

	private Hashtable<Long, HotspotInfo[]> playerHotspots; // 所有人的热点保存
	private Hashtable<Long, Long> playerHotspotsLoadTime; // 热点载入的时间

	public static Logger logger = LoggerFactory.getLogger(HotspotManager.class);

	private static HotspotManager instance;

	public SimpleEntityManager<HotspotInfo> em;

	private String file;

	public void init() {
		
		if (logger.isInfoEnabled()) logger.info("初始化HotspotManager");
		playerHotspots = new Hashtable<Long, HotspotInfo[]>();
		playerHotspotsLoadTime = new Hashtable<Long, Long>();
		em = SimpleEntityManagerFactory.getSimpleEntityManager(HotspotInfo.class);
		setInstance(this);
		instance.loadFile();
		if (logger.isInfoEnabled()) logger.info("初始化HotspotManager结束");
		Thread t = new Thread(instance, "Hotspot");
		t.start();
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		if (logger.isInfoEnabled()) logger.info("停服HotspotManager保存开始");
		em.destroy();
		if (logger.isInfoEnabled()) logger.info("停服HotspotManager保存结束");
	}

	public static void setInstance(HotspotManager instance) {
		HotspotManager.instance = instance;
	}

	public static HotspotManager getInstance() {
		return instance;
	}

	public void loadFile() {
		try {
			baseHotspot = new ArrayList<Hotspot>();
			File file = new File(getFile());
			if (!file.exists()) {
				if (logger.isWarnEnabled()) logger.warn("任务配置文件不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			List<String> deliveTaskName = new ArrayList<String>();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				if (cells.length == 0 || cells[0].getContents() == null || cells[0].getContents().equals("")) {
					continue;
				}
				Hotspot base = new Hotspot();
				base.setId(Integer.parseInt(cells[0].getContents()));
				base.setTitle((cells[1].getContents()));
				base.setIconName(cells[2].getContents());
				String[] opentypes = cells[3].getContents().split(",");
				int[] opentypesI = new int[opentypes.length];
				for (int k = 0; k < opentypes.length; k++) {
					opentypesI[k] = Integer.parseInt(opentypes[k]);
				}
				base.setOpenType(opentypesI);
				base.setOpenValue(cells[4].getContents().split(","));
				base.setOverType(Integer.parseInt(cells[5].getContents()));
				base.setOverValue((cells[6].getContents()));
				base.setMsg((cells[7].getContents()));
				if (base.getOverType() == Hotspot.OVERTYPE_TASK) {
					deliveTaskName.add(base.getOverValue());
					// logger.debug("基础热点信息 完成条件是任务的: [任务名称:{}]", new Object[]{base.getOverValue()});
				}
				base.setPos(i - 1);
				// logger.debug("载入基础热点信息 第[{}],id=[{}],title=[{}]", new Object[]{i-1, base.getId(), base.getTitle()});
				baseHotspot.add(base);
			}
			HotspotTaskEventTransact.deliver_TaskName = deliveTaskName.toArray(new String[0]);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("载入基础热点出错", e);
		}
	}
	
	public static boolean 是否启用新的读表 = true;

	public void loadPlayerHotspotInfo(Player player) {
		List<HotspotInfo> list = new ArrayList<HotspotInfo>();
		try {
			long count = 0;
			if (是否启用新的读表) {
				count = em.count(HotspotInfo.class, "playerID= ? ", new Object[]{player.getId()});
				list.addAll(em.query(HotspotInfo.class, "playerID= ? ", new Object[]{player.getId()}, "", 1, count + 1));
			} else {
				count = em.count(HotspotInfo.class, "playerID=" + player.getId());
				list.addAll(em.query(HotspotInfo.class, "playerID=" + player.getId(), "", 1, count + 1));
			}
		} catch (Exception e) {
			logger.error("载入拍卖数据库出错:严重", e);
			return;
		}
		if (logger.isInfoEnabled()) logger.info("载入玩家热点数据: [id={}] [size={}]", new Object[] { player.getId(), list.size() });
		HotspotInfo[] infos = new HotspotInfo[baseHotspot.size()];
		for (int i = 0; i < baseHotspot.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (baseHotspot.get(i).getId() == list.get(j).getHotspotID()) {
					if (infos[i] != null) {
						try {
							em.remove(list.get(j));
							list.remove(j);
							logger.warn("[热点数据不正确] [hid={}] [pid={}]", new Object[] { baseHotspot.get(i).getId(), player.getId() });
							break;
						} catch (Exception e) {
							logger.error("删除多于的热点出错 [" + baseHotspot.get(i).getId() + "] [" + i + "]", e);
						}
					} else {
						infos[i] = list.get(j);
					}
				}
			}
		}
		putPlaerHotspts(player.getId(), infos);
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	public void setPlayerHotspots(Hashtable<Long, HotspotInfo[]> playerHotspots) {
		this.playerHotspots = playerHotspots;
	}

	public Hashtable<Long, HotspotInfo[]> getPlayerHotspots() {
		return playerHotspots;
	}

	public void putPlaerHotspts(long pid, HotspotInfo[] infos) {
		playerHotspots.put(pid, infos);
		playerHotspotsLoadTime.put(pid, SystemTime.currentTimeMillis());
	}

	public synchronized HotspotInfo[] getPlayerHotspts(Player player) {
		HotspotInfo[] infos = playerHotspots.get(player.getId());
		if (infos == null) {
			loadPlayerHotspotInfo(player);
			infos = playerHotspots.get(player.getId());
		}
		playerHotspotsLoadTime.put(player.getId(), SystemTime.currentTimeMillis());
		return infos;
	}

	// 开启一个热点，这个方法是去检查热点不一定能开启
	public void openHotspot(Player player, int type, String parameter) {
		try {
			if (logger.isInfoEnabled()) logger.info("开启Hotspot: [玩家：{}], [type：{}], [参数：{}]", new Object[] { player.getName(), type, parameter });
			boolean isRelOpen = false; // 是否真的有个成功开启，如果开启给客户端发协议
			for (int i = 0; i < baseHotspot.size(); i++) {
				Hotspot hotspot = baseHotspot.get(i);
				// if (hotspot.getOpenType() != type) {
				// continue;
				// }
				for (int k = 0; k < hotspot.getOpenType().length; k++) {
					if (hotspot.getOpenType()[k] != type) {
						continue;
					}
					HotspotInfo[] infos = getPlayerHotspts(player);
					switch (type) {
					case Hotspot.OPENTYPE_HOTSPOT:
						// 其他热点
						break;
					case Hotspot.OPENTYPE_LEVEL:
						// 等级
						if (Integer.parseInt(hotspot.getOpenValue()[k]) <= player.getLevel()) {
							if (infos[i] == null) {
								long id = 0;
								try {
									id = em.nextId();
								} catch (Exception e) {
									logger.error("开启一个热点生成ID出错:", e);
									return;
								}
								infos[i] = new HotspotInfo(id, player.getId(), hotspot.getId());
								infos[i].setSee(new int[hotspot.getOpenType().length]);
								em.notifyNewObject(infos[i]);
								overHotspot(player, infos[i]);
								isRelOpen = true;
							}
							if (infos[i].isSee()[k] == 0) {
								infos[i].isSee()[k] = 1;
								em.notifyFieldChange(infos[i], "isSee");
								HOTSPOT_OPEN_RES res = new HOTSPOT_OPEN_RES(GameMessageFactory.nextSequnceNum(), infos[i].getHotspotID(), hotspot.getIconName());
								player.addMessageToRightBag(res);
							}
						}
						break;
					}
				}
			}
			if (isRelOpen) {
				sendHotspot2Client(player);
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("openHotspot出错:[玩家名字:" + player.getName() + "], [类型:" + type + "], [参数:" + parameter + "]", e);
		}
	}

	public void openHotspot(Player player, Hotspot hotspot) {
		HotspotInfo[] infos = getPlayerHotspts(player);
		for (int i = 0; i < baseHotspot.size(); i++) {
			for (int k = 0; k < baseHotspot.get(i).getOpenType().length; k++) {
				if (baseHotspot.get(i).getOpenType()[k] == Hotspot.OPENTYPE_HOTSPOT && baseHotspot.get(i).getOpenValue()[k].equals("" + hotspot.getId())) {
					if (infos[i] == null) {
						long id = 0;
						try {
							id = em.nextId();
						} catch (Exception e) {
							logger.error("开启一个热点生成ID出错:", e);
							return;
						}
						infos[i] = new HotspotInfo(id, player.getId(), baseHotspot.get(i).getId());
						infos[i].setSee(new int[baseHotspot.get(i).getOpenType().length]);
						em.notifyNewObject(infos[i]);
						overHotspot(player, infos[i]);
					}
					if (infos[i].isSee()[k] == 0) {
						infos[i].isSee()[k] = 1;
						em.notifyFieldChange(infos[i], "isSee");
						HOTSPOT_OPEN_RES res = new HOTSPOT_OPEN_RES(GameMessageFactory.nextSequnceNum(), infos[i].getHotspotID(), baseHotspot.get(i).getIconName());
						player.addMessageToRightBag(res);
					}
					break;
				}
			}
		}
	}

	// 尝试去完成一个热点
	public void overHotspot(Player player, HotspotInfo info) {
		Hotspot hotspot = null;
		for (int i = 0; i < baseHotspot.size(); i++) {
			if (baseHotspot.get(i).getId() == info.getHotspotID()) {
				hotspot = baseHotspot.get(i);
				break;
			}
		}
		if (hotspot == null) {
			if (logger.isWarnEnabled()) logger.warn("热点出错：找不到对应的基础热点  [角色ID{}],  [热点ID{}]", new Object[] { player.getId(), info.getHotspotID() });
			return;
		}
		switch (hotspot.getOverType()) {
		case Hotspot.OVERTYPE_LEVEL:
			// 级别
			if (Integer.parseInt(hotspot.getOverValue()) <= player.getLevel()) {
				info.setOver(true);
				HOTSPOT_OVER_RES res = new HOTSPOT_OVER_RES(GameMessageFactory.nextSequnceNum(), info.getHotspotID(), hotspot.getIconName());
				player.addMessageToRightBag(res);
				openHotspot(player, hotspot);
			}
			break;
		case Hotspot.OVERTYPE_TASK:
			// 任务
			String taskName = hotspot.getOverValue();
			// logger.debug("尝试去完成一个任务热点 [id:{}], [playerName:{}], [任务名字:{}]", new Object[]{hotspot.getId(), player.getName(), taskName});
			Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
			if (task != null) {
				if (player.inDeliver(task)) {
					info.setOver(true);
					HOTSPOT_OVER_RES res = new HOTSPOT_OVER_RES(GameMessageFactory.nextSequnceNum(), info.getHotspotID(), hotspot.getIconName());
					player.addMessageToRightBag(res);
					openHotspot(player, hotspot);
				}
			}
			break;
		}
	}

	public void overHotspot(Player player, int type, String parameter) {
		try {
			boolean isRelOver = false;
			if (logger.isInfoEnabled()) logger.info("overHotspot: [玩家：{}], [type：{}], [参数：{}]", new Object[] { player.getName(), type, parameter });
			HotspotInfo[] infos = getPlayerHotspts(player);
			for (int i = 0; i < infos.length; i++) {
				if (infos[i] == null || infos[i].isOver()) {
					continue;
				}
				Hotspot hotspot = baseHotspot.get(i);
				if (hotspot.getOverType() == Hotspot.OVERTYPE_宠物升级 && type == Hotspot.OVERTYPE_宠物升级) {
					if (Integer.parseInt(hotspot.getOverValue()) > Integer.parseInt(parameter)) {
						continue;
					}
				} else {
					if (hotspot.getOverType() != type || !hotspot.getOverValue().equals(parameter)) {
						continue;
					}
				}

				infos[i].setOver(true);
				HOTSPOT_OVER_RES res = new HOTSPOT_OVER_RES(GameMessageFactory.nextSequnceNum(), infos[i].getHotspotID(), hotspot.getIconName());
				player.addMessageToRightBag(res);
				openHotspot(player, hotspot);
				isRelOver = true;
			}
			if (isRelOver) {
				sendHotspot2Client(player);
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("overHotspot出错:[玩家名字:" + player.getName() + "], [类型:" + type + "], [参数:" + parameter + "]", e);
		}
	}

	public void sendHotspot2Client(Player player) {
		HotspotInfo[] infos = getPlayerHotspts(player);
		ArrayList<HotspotClientInfo> list = new ArrayList<HotspotClientInfo>();
		if (infos == null) {
			logger.error("[热点] [热点不能初始化？] [{}]", new Object[]{player.getLogString()});
			return;
		}
		for (int i = 0; i < infos.length; i++) {
			if (infos[i] != null) {
				HotspotClientInfo clientInfo = new HotspotClientInfo();
				clientInfo.setHotspotID(infos[i].getHotspotID());
				clientInfo.setName(baseHotspot.get(i).getTitle());
				clientInfo.setIcon(baseHotspot.get(i).getIconName());
				clientInfo.setIsSee(infos[i].isRealSee());
				clientInfo.setIsOver(infos[i].isOver());
				list.add(clientInfo);
				// if (logger.isInfoEnabled()) logger.info("[角色ID={}] [infoID={}] [Title={}] [see={}] [over={}]", new Object[]{player.getId(), clientInfo.getHotspotID(),
				// clientInfo.getName(), clientInfo.isIsSee(), clientInfo.isIsOver()});
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("sendHotspot2Client :[角色ID:{}], [角色名字:{}], [总个数:{}], [当前个数:{}]", new Object[] { player.getId(), player.getName(), baseHotspot.size(), list.size() });
		}
		NOTIFY_HOTSPOT_CHANGE_RES res = new NOTIFY_HOTSPOT_CHANGE_RES(GameMessageFactory.nextSequnceNum(), baseHotspot.size(), list.toArray(new HotspotClientInfo[0]));
		player.addMessageToRightBag(res);
	}

	@Override
	public void run() {
		ArrayList<Long> remove = new ArrayList<Long>();
		while (true) {
			try {
				Thread.sleep(sleepTime);

				long now = SystemTime.currentTimeMillis();
				remove.clear();
				int all = playerHotspots.size();
				for (Long pid : playerHotspotsLoadTime.keySet()) {
					long time = playerHotspotsLoadTime.get(pid);
					if (now - time > 10 * 60 * 1000) {
						remove.add(pid);
					}
				}

				for (Long pid : remove) {
					playerHotspotsLoadTime.remove(pid);
					HotspotInfo[] infos = playerHotspots.get(pid);
					if (infos != null) {
						for (int i = 0; i < infos.length; i++) {
							if (infos[i] != null) {
								em.save(infos[i]);
							}
						}
					}
					playerHotspots.remove(pid);
				}
				if (logger.isInfoEnabled()) logger.info("[热点] [心跳检查] [all={}] [remove={}] [hava={}]", new Object[] { all, remove.size(), playerHotspots.size() });
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("HotspotManager run 出错", e);
			}
		}
	}

	public CompoundReturn seeHotspot(Player player, int hotspotID) {
		CompoundReturn cre = new CompoundReturn();
		HotspotInfo[] infos = getPlayerHotspts(player);
		for (int i = 0; i < infos.length; i++) {
			if (infos[i] == null) {
				continue;
			}
			if (infos[i].getHotspotID() == hotspotID) {
				if (!infos[i].isRealSee()) {
					infos[i].setRealSee();
				}
				cre.setIntValue(hotspotID);
				cre.setStringValue(baseHotspot.get(i).getMsg());
				return cre;
			}
		}
		return cre;
	}

}
