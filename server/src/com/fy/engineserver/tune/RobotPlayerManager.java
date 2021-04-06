package com.fy.engineserver.tune;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;

public class RobotPlayerManager implements Runnable {
	
	private List<BeatHeartThread> threads = new LinkedList<BeatHeartThread>();
	
	private GatewayClientConnector gatewayClientConnector;
	
	private ServerClientConnector serverClientConnector;
	
	private String userfile;
	
	private List<String> userlist;
	
	private boolean userCreated;
	
	private int num;
	
	private boolean running;
	
	private String mapName;
	
	private static long seq;
	
	public static RobotPlayerManager instance;
	
	public static RobotPlayerManager getRobotPlayerManager() {
		return instance;
	}
	
	public synchronized long nextID() {
		return seq++;
	}
	
	public RobotPlayerManager(String file, int num, String mapName, String host, int port, String gateway, int gatewayPort) {
		try {
			if(SystemTime.getInstance() == null){
				SystemTime st = new SystemTime();
				st.init();
			}
			gatewayClientConnector = new GatewayClientConnector();
			
			DefaultConnectionSelector selector = new DefaultConnectionSelector();
			selector.setEnableHeapForTimeout(true);
			selector.setName("RobotPlayerManager-gw-selector");
			selector.setClientModel(true);
			selector.setMaxConnectionNum(1024);
			selector.setMinConnectionNum(256);
			selector.setThreadPoolCorePoolSize(128);
			selector.setThreadPoolKeepAlive(64);
			selector.setThreadPoolMaxPoolSize(256);
			selector.setHost(gateway);
			selector.setPort(gatewayPort);
			selector.init();
			
			
			gatewayClientConnector.setConnectionSelector(selector);
			gatewayClientConnector.init();
			
			this.serverClientConnector = new ServerClientConnector();
			
			selector = new DefaultConnectionSelector();
			selector.setEnableHeapForTimeout(true);
			selector.setName("RobotPlayerManager-server-selector");
			selector.setClientModel(true);
			selector.setThreadPoolCorePoolSize(128);
			selector.setThreadPoolKeepAlive(64);
			selector.setThreadPoolMaxPoolSize(256);
			selector.setMaxConnectionNum(1024);
			selector.setMinConnectionNum(256);
			selector.setHost(host);
			selector.setPort(port);
			selector.init();
			
			
			serverClientConnector.setConnectionSelector(selector);
			serverClientConnector.init();
			this.userfile = file;
			this.num = num;
			userlist = new ArrayList<String>();
			if(new File(userfile).isFile()) {
				loadFromFile(userfile);
			}
			this.mapName = mapName;
			System.out.println("[RobotPlayerManager实例化成功] ["+file+"] ["+num+"] ["+mapName+"] ["+new File(userfile).isFile()+"] [userlist:"+userlist.size()+"]");
		} catch(Exception e) {
			System.out.println("[RobotPlayerManager实例化失败] \n" + StringUtil.getStackTrace(e));
		}
	}
	
	private void loadFromFile(String file) throws Exception {
		String s = FileUtils.readFile(file);
		if(s.trim().length() > 0) {
			String str[] = s.trim().split("#####");
			for(String line : str) {
				userlist.add(line);
			}
		}
	}
	
	public int playerNums;
	
	public void start() {
		if(userlist != null) {
			int bhIndex = 0;
			BeatHeartThread bh = new BeatHeartThread("BeatHeartThread-" + (bhIndex++));
			threads.add(bh);
			for(int i=0; i<userlist.size() && i<num; i++) {
				String str = userlist.get(i);
				String auth[] = str.split(",");
				RobotPlayer r = createRobotPlayer(auth[0], auth[1], mapName);
				Player player = r.initPlayer();
				if(player != null){
					playerNums++;
					System.out.println("=====老账号==============【初始化成功,"+str+"】===="+playerNums+"=========");
					if(bh.getRobotPlayers().length < 20) {
						bh.addRobotPlayer(r);
					} else {
						bh = new BeatHeartThread("BeatHeartThread-" + (bhIndex++));
						threads.add(bh);
						bh.addRobotPlayer(r);
					}
				}
			}
			if(userlist.size() < num) {
				for(int i=0,len=num-userlist.size(); i<len; i++) {
					RobotPlayer r = createRobotPlayer(null, null, mapName);
					Player player = r.initPlayer();
					if(player != null){
						System.out.println("=====新账号==============【初始化成功,"+player.getName()+"】===="+playerNums+"=========");
						if(bh.getRobotPlayers().length < 20) {
							bh.addRobotPlayer(r);
						} else {
							bh = new BeatHeartThread("BeatHeartThread-" + (bhIndex++));
							threads.add(bh);
							bh.addRobotPlayer(r);
						}
					}
				}
			}
			for(BeatHeartThread bht : threads) {
				bht.start();
			}
		}
		if(!running) {
			running = true;
			Thread t = new Thread(this, "RobotPlayerManager");
			t.start();
		}
	}
	
	public void stop() {
	}
	
	public RobotPlayer createRobotPlayer(String username, String password, String mapName) {
		RobotPlayer rp = new RobotPlayer(nextID(), username, password,mapName, this);
		return rp;
	}
	
	public void addRegisterUser(String username, String password) {
		String str = username +","+password;
		userlist.add(str);
		userCreated = true;
	}
	
	public Connection getGatewayClientConnection(RobotPlayer player) throws Exception {
		Connection conn  = gatewayClientConnector.getConnection();
		return conn;
	}
	
	public Connection getServerClientConnection(RobotPlayer player) throws Exception {
		Connection conn  = serverClientConnector.getConnection();
		return conn;
	}
	
	public GatewayClientConnector getGatewayClientConnector() {
		return gatewayClientConnector;
	}

	public ServerClientConnector getServerClientConnector() {
		return serverClientConnector;
	}

	public void run() {
		while(running) {
			try {
				Thread.sleep(30000);
			} catch(Exception e) {
				System.out.println("[RobotPlayerManager心跳异常] \n" + StringUtil.getStackTrace(e));
			}
		}
		System.out.println("[RobotPlayerManager心跳结束]");
	}
	
	public static void main(String args[]) {
		String filename = args[0];
		int num = Integer.valueOf(args[1]);
		String appRoot = args[2];
		String map = null;
		String host = null;
		int port = 0;
		String gwhost = null;
		int gwport = 0;
		if(args.length >= 8) {
			map = args[3];
			host = args[4];
			port  = Integer.valueOf(args[5]);
			gwhost = args[6];
			gwport  = Integer.valueOf(args[7]);
		}
		GameMapManager gmm = new GameMapManager();
		gmm.setAppRoot(appRoot);
		gmm.init();
		RobotPlayerManager rpm = new RobotPlayerManager(filename, num, map, host, port, gwhost, gwport);
		rpm.start();
	}
}
