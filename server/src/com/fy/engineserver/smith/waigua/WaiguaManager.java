package com.fy.engineserver.smith.waigua;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;

/**
 *
 * 
 * @version 创建时间：Oct 17, 2013 11:08:51 AM
 * 
 */
public class WaiguaManager implements Runnable {
	public static Logger logger = LoggerFactory.getLogger(WaiguaManager.class);
	
	private boolean running;
	
	private Thread thread;
	
	private Map<Long, ClientMessageHistory> playerMessageHistoryMap = new Hashtable<Long, ClientMessageHistory>();
	
	private String storeFile;
	
	private List<String> symbol = new ArrayList<String>();
	
	private DefaultDiskCache forbidPlayers;
	
	private long lastSyncSymbolTime;
	
	private int colSize = 100;
	
	private int maxNum = 2000;
	
	private boolean openning = false;
	
	private static WaiguaManager self;
	
	public static WaiguaManager getInstance() {
		return self;
	}
	
	/**
	 * 从网关同步进程黑名单
	 */
	public void syncSymbol() {
		String remoteFile = "http://"+Game.网关地址+":8882/game_gateway/waiguamessage.txt";
		try {
			byte data[] = HttpUtils.webGet(new URL(remoteFile), new HashMap(), 5000, 5000);
			if(data != null && data.length > 0) {
				String content = new String(data, "UTF-8");
				String str[] = content.split("\n");
				List<String> sym = new ArrayList<String>();
				for(String s : str) {
					sym.add(s.trim());
					logger.warn("[外挂消息定义] [从远程加载] [加载进程识别串:"+s.trim()+"]");
				}
				this.symbol = sym;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		if(!running) {
			running = true;
			thread = new Thread(this, "WaiguaManager");
			thread.start();
		}
	}
	
	public void init() {
		
		forbidPlayers = new DefaultDiskCache(new File(storeFile),"MatchDataStoreCache", 300 * 24 * 60 * 60 * 1000L);
//		syncSymbol();
//		start();
		self = this;
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy() {
		forbidPlayers.destory();
	}
	
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000L*60);
			} catch(Exception e) {
				logger.error("外挂检测程序出现异常", e);
			}
			try {
				long now = System.currentTimeMillis();
				if(now - lastSyncSymbolTime > 30000) {
					lastSyncSymbolTime = now;
					syncSymbol();
				}
			} catch(Throwable e) {
				logger.error("外挂检测程序出现异常", e);
			}
		}
	}

	public String getStoreFile() {
		return storeFile;
	}

	public void setStoreFile(String storeFile) {
		this.storeFile = storeFile;
	}

	public DiskCache getForbidPlayers() {
		return forbidPlayers;
	}

	public void setForbidPlayers(DefaultDiskCache forbidPlayers) {
		this.forbidPlayers = forbidPlayers;
	}

	public Map<Long, ClientMessageHistory> getPlayerMessageHistoryMap() {
		return playerMessageHistoryMap;
	}

	public void setPlayerMessageHistoryMap(Map<Long, ClientMessageHistory> playerMessageHistoryMap) {
		this.playerMessageHistoryMap = playerMessageHistoryMap;
	}
	
	public void notifyPlayerMessage(Player player, String message, long seqNum) {
		ClientMessageHistory ch = playerMessageHistoryMap.get(player.getId());
		if(ch == null) {
			Boolean forbid = (Boolean)forbidPlayers.get(player.getId());
			if(forbid != null && !forbid) {
				if(logger.isDebugEnabled()) {
					logger.debug("[玩家的消息] [已加入白名单，直接扔掉] ["+player.getLogString()+"] ["+message+"]");
					return;
				}
			}
			if(playerMessageHistoryMap.size() > maxNum) {
				if(logger.isDebugEnabled()) {
					logger.debug("[玩家的消息] [>"+maxNum+"，直接扔掉] ["+player.getLogString()+"] ["+message+"]");
					return;
				}
			}
			ch = new ClientMessageHistory(player.getId());
			playerMessageHistoryMap.put(player.getId(), ch);
		}
		if(!message.equals("HEARTBEAT_CHECK_REQ")) {
			List<String> mlist = ch.getMessageList();
			if(mlist.size() < colSize) {
				mlist.add(message);
				logger.info("[玩家的消息] ["+seqNum+"] ["+player.getLogString()+"] ["+message+"] [listSize:"+mlist.size()+"] [mapSize:"+playerMessageHistoryMap.size()+"]");
			}
		}
	}
	
	public boolean isWaigua(long playerId) {
		ClientMessageHistory ch = playerMessageHistoryMap.get(playerId);
		if(ch != null) {
			List<String> mlist = ch.getMessageList();
			StringBuffer sb = new StringBuffer();
			for(String s : mlist) {
				sb.append(s + ";");
			}
			String ms = sb.toString();
			if(ms.length() > 0) {
				ms = ms.substring(0, ms.length()-1);
			}
			for(String s : symbol) {
				if(ms.indexOf(s) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Long[] getRecordPlayers() {
		return playerMessageHistoryMap.keySet().toArray(new Long[0]);
	}
	
	public void addForbidList(List<Long> players) {
		PlayerManager pm = PlayerManager.getInstance();
		for(Long pid : players) {
			forbidPlayers.put(pid, Boolean.TRUE);
			playerMessageHistoryMap.remove(pid);
			Player p = null;
			try {
				p = pm.getPlayer(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("[加入封禁] ["+(p!=null?p.getLogString():"NULL")+"]");
		}
	}
	
	public void addWhiteList(List<Long> players) {
		PlayerManager pm = PlayerManager.getInstance();
		for(Long pid : players) {
			forbidPlayers.put(pid, Boolean.FALSE);
			playerMessageHistoryMap.remove(pid);
			Player p = null;
			try {
				p = pm.getPlayer(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("[加入白名单] ["+(p!=null?p.getLogString():"NULL")+"]");
		}
	}
	
	public void releaseUsers(List<Long> players) {
		PlayerManager pm = PlayerManager.getInstance();
		for(Long pid : players) {
			forbidPlayers.remove(pid);
			Player p = null;
			try {
				p = pm.getPlayer(pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("[解除封禁] ["+(p!=null?p.getLogString():"NULL")+"]");
		}
	}
	
	public boolean isForbid(long pid) {
		Boolean forbid = (Boolean)forbidPlayers.get(pid);
		if(forbid != null && forbid) {
			return true;
		}
		return false;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public List<String> getSymbol() {
		return symbol;
	}

	public void setSymbol(List<String> symbol) {
		this.symbol = symbol;
	}

	public long getLastSyncSymbolTime() {
		return lastSyncSymbolTime;
	}

	public void setLastSyncSymbolTime(long lastSyncSymbolTime) {
		this.lastSyncSymbolTime = lastSyncSymbolTime;
	}

	public int getColSize() {
		return colSize;
	}

	public void setColSize(int colSize) {
		this.colSize = colSize;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public boolean isOpenning() {
		return openning;
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
	}
}
