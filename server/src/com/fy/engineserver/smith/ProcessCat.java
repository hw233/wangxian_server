package com.fy.engineserver.smith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.Player_Process;
import com.fy.engineserver.message.USER_CLIENT_INFO_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

/**
 *
 * 
 * @version 创建时间：Aug 4, 2013 10:43:58 AM
 * 
 */
public class ProcessCat implements Runnable {
	private Thread thread;
	
	private boolean running = false;
	
	private String symbolFile;
	
	private HashSet<String> symbol = new HashSet<String>();
	
	private long kickNum;
	
	private HashMap<Long,HashSet<String>> kickMap = new HashMap<Long,HashSet<String>>();
	
	private HashMap<Long, String> blackMap = new HashMap<Long, String>();
	
	private long lastSyncSymbolTime;
	
	private static ProcessCat self;
	
	public static ProcessCat getInstance() {
		return self;
	}
	
	public void init() {
		
		self = this;
		if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
			return;
		}
		readSymbolFile();
//		start();
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 启动时从本地文件加载进程黑名单
	 */
	public void readSymbolFile() {
		String content = FileUtils.readFile(symbolFile);
		String str[] = content.split("\n");
		HashSet<String> sym = new HashSet<String>();
		for(String s : str) {
			sym.add(s.trim());
			EnterLimitManager.logger.warn("[黑名单进程包含序列] [初始化] [加载进程识别串:"+s.trim()+"]");
		}
		this.symbol = sym;
		lastSyncSymbolTime = System.currentTimeMillis();
	}
	
	/**
	 * 从网关同步进程黑名单
	 */
	public void syncSymbol() {
		String remoteFile = "http://"+Game.网关地址+":8882/game_gateway/processcat.txt";
		try {
			byte data[] = HttpUtils.webGet(new URL(remoteFile), new HashMap(), 5000, 5000);
			if(data != null && data.length > 0) {
				String content = new String(data, "UTF-8");
				String str[] = content.split("\n");
				HashSet<String> sym = new HashSet<String>();
				for(String s : str) {
					sym.add(s.trim());
					EnterLimitManager.logger.warn("[黑名单进程包含序列] [从远程加载] [加载进程识别串:"+s.trim()+"]");
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
			thread = new Thread(this, "ProcessCat");
			thread.start();
		}
	}
	
	public void stop() {
		this.running = false;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public String getSymbolFile() {
		return symbolFile;
	}

	public void setSymbolFile(String symbolFile) {
		this.symbolFile = symbolFile;
	}

	public HashSet<String> getSymbol() {
		return symbol;
	}
	
	public void setSymbol(HashSet<String> symbol) {
		this.symbol = symbol;
	}
	
	public void run() {
		while(running) {
			try {
				Thread.sleep(5000);
				long now = System.currentTimeMillis();
				String serverName = GameConstants.getInstance().getServerName();
				if (serverName.equals("新功能测试") || serverName.equals("pan3") || serverName.equals("pan4")
						 || serverName.equals("国内本地测试") || serverName.equals("ST")) {
					continue;
				}
				if(now - lastSyncSymbolTime > 30000) {
					lastSyncSymbolTime = now;
					syncSymbol();
				}
				PlayerManager pm = PlayerManager.getInstance();
				Player ps[] = pm.getOnlinePlayers();
//				//先检查是否已经被反复踢过
//				for(Player p : ps) {
//					HashSet<String> processStr = kickMap.get(p.getId());
//					if(processStr != null && processStr.size() >= 3) {
//						pm.kickPlayer(p.getId());
//						kickNum++;
//						EnterLimitManager.logger.warn("[黑名单进程包含序列] [踢下线] [此角色多次被定义黑名单进程,直接踢] ["+p.getLogString()+"] [kickPlayer:"+kickMap.size()+"] [kickNum:"+kickNum+"]");
//					}
//				}
				ps = pm.getOnlinePlayers();
				HashMap<String,Integer> ipmap = new HashMap<String,Integer>();
				for(Player p : ps) {
					USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
                    String mt = null;
					if(o != null){
						mt = o.getPhoneType();
                    }
					Player_Process pp = EnterLimitManager.player_process.get(p.getId());
					if(pp != null && pp.getAndroidProcesss() != null && pp.getAndroidProcesss().length > 0) {
						String proces[] = pp.getAndroidProcesss();
						String blackStr = isForbid(proces, mt);
						if(blackStr != null) {
							String ip = p.getConn()!=null?p.getConn().getIdentity():null;
							if(ip != null) {
								ip = ip.substring(0, ip.lastIndexOf("."));
								Integer n = ipmap.get(ip);
								if(n == null) {
									n = 1;
								} else {
									n += 1;
								}
								ipmap.put(ip, n);
								blackMap.put(p.getId(), blackStr);
							}
						} else {

						}
					} else {

					}
				}
				for(Player p : ps) {
					USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ)p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
                    String mt = null;
					if(o != null){
						mt = o.getPhoneType();
                    }
					String ip = p.getConn()!=null?p.getConn().getIdentity():null;
					if(ip != null) {
						ip = ip.substring(0, ip.lastIndexOf("."));
						Integer n = ipmap.get(ip);
						if(n != null && n >= 2) {
							//确定是黑名单进程序列
							pm.kickPlayer(p.getId());
							kickNum++;
							Player_Process pp = EnterLimitManager.player_process.get(p.getId());
//							if(pp != null) {
//								HashSet<String> processStr = kickMap.get(p.getId());
//								if(processStr == null) {
//									processStr = new HashSet<String>();
//								}
//								processStr.add(StringUtil.arrayToString(pp.getAndroidProcesss(),";"));
//								kickMap.put(p.getId(), processStr);
//							}
							EnterLimitManager.logger.warn("[黑名单进程包含序列] [踢下线] [机型:"+mt+"] ["+p.getLogString()+"] [IP:"+ip+"] [符合黑名单的定义:"+blackMap.get(p.getId())+"] [被踢的进程串:"+(pp!=null?StringUtil.arrayToString(pp.getAndroidProcesss(), ";"):"NULL")+"] [kickPlayer:"+kickMap.size()+"] [kickNum:"+kickNum+"]");
						}
					}
				}
				EnterLimitManager.logger.warn("[检查黑名单进程包含序列] ["+ps.length+"] [kickPlayer:"+kickMap.size()+"] [kickNum:"+kickNum+"]");
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
		running = false;
	}
	
	public String isForbid(String userProcess[], String mt) {
		HashSet<String> pset = new HashSet<String>();
		for(String ss : userProcess) {
			pset.add(ss.trim());
		}
		for(String pstr : symbol) {
			String str[] = pstr.split(";");
			boolean meet = true;
			if(str.length < 3) {
				if(mt != null && pstr.length() > 5 && pstr.trim().equals(mt)) {
					return mt;
				}
				meet = false;
			} else {
				for(String pp : str) {
					if(!pset.contains(pp.trim())) {
						meet = false;
						break;
					}
				}
			}
			if(meet) {
				return pstr;
			}
		}
		return null;
	}
}
