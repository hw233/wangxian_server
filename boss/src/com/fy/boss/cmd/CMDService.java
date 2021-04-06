package com.fy.boss.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.fy.boss.cmd.message.COMMON_CMD_REQ;
import com.fy.boss.cmd.message.COMMON_CMD_RES;
import com.fy.boss.cmd.message.FILE_PACKET_REQ;
import com.fy.boss.cmd.message.FILE_PACKET_RES;
import com.fy.boss.cmd.message.SERVER_LOG_REQ;
import com.fy.boss.cmd.message.SERVER_LOG_RES;
import com.fy.boss.cmd.message.SERVER_STATUS_REQ;
import com.fy.boss.cmd.message.SERVER_STATUS_RES;
import com.fy.boss.deploy.ProjectManager;
import com.fy.boss.deploy.ServerStatus;
import com.fy.boss.game.model.Server;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;

public class CMDService {

	protected static CMDService m_self = null;
    
	public static final Log logger = LogFactory.getLog(CMDService.class);
	
	private List<CMDConnector> connectors;
	
	public static final String GAME_SERVER_PROC_NAME = "GameServerApp";
	public static final int GAME_SERVER_AGENT_PORT = 4321;
	
    public static CMDService getInstance() {
		return m_self;
	}

	public void initialize() throws Exception{
		connectors = new ArrayList<CMDConnector>();
		m_self = this;
		System.out.println("["+this.getClass().getName()+"] [initialized]");
		logger.info("["+this.getClass().getName()+"] [initialized]");
	}
	
	/**
	 * 获取服务器的状态
	 * @param host
	 * @param port
	 * @param server
	 * @return
	 */
	public ServerStatus getServerStatus(String host,Server server) {
		long now = System.currentTimeMillis();
		CMDConnector con = getConnector(host);
		if(con == null) {
			con = buildConnector(host);
		}
		if(con == null) {
			logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			ProjectManager.logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			return null;
		}
		String serverbase = server.getResinhome();
		String processname = GAME_SERVER_PROC_NAME;
		SERVER_STATUS_REQ req = new SERVER_STATUS_REQ(CMDMessageFactory.nextSequnceNum() , serverbase, processname);
		try {
			SERVER_STATUS_RES res = (SERVER_STATUS_RES)con.requestMessage(req);
			logger.info("[获取服务器状态] ["+server.getName()+"] ["+res.getInstalled()+"] ["+res.getRunning()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			ServerStatus status = new ServerStatus();
			status.setAgenton(true);
			status.setInstalled(res.getInstalled());
			status.setRunning(res.getRunning());
			status.setServerid(server.getId());
			return status;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[获取服务器状态] [失败:请求失败] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
		}
		ServerStatus status = new ServerStatus();
		status.setAgenton(false);
		status.setInstalled(false);
		status.setRunning(false);
		status.setServerid(server.getId());
		return status;
	}
	
	public String getServerLog(String host, String remoteLog, int linenum) {
		long now = System.currentTimeMillis();
		CMDConnector con = getConnector(host);
		if(con == null) {
			con = buildConnector(host);
		}
		if(con == null) {
			logger.error("[获取服务器log] [失败:无法创建到服务器的连接] ["+remoteLog+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			ProjectManager.logger.error("[获取服务器log] [失败:无法创建到服务器的连接] ["+remoteLog+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			return "";
		}
		SERVER_LOG_REQ req = new SERVER_LOG_REQ(CMDMessageFactory.nextSequnceNum() , remoteLog, linenum);
		try {
			SERVER_LOG_RES res = (SERVER_LOG_RES)con.requestMessage(req);
			String result = res.getLogcontent();
			logger.info("[获取服务器log] ["+remoteLog+"] ["+linenum+"] ["+result.getBytes().length+"] ["+(System.currentTimeMillis()-now)+"ms]");
			ProjectManager.logger.info("[获取服务器log] ["+remoteLog+"] ["+linenum+"] ["+result.getBytes().length+"] ["+(System.currentTimeMillis()-now)+"ms] 结果:\n" + result);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[获取服务器log] [请求失败] ["+remoteLog+"] ["+linenum+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
			ProjectManager.logger.error("[获取服务器log] [请求失败] ["+remoteLog+"] ["+linenum+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
		}
		return "";
	}
	
	/**
	 * 在Server上执行指令
	 * @param server
	 * @param cmd
	 * @return 成功
	 */
	public boolean execCMD(String host, String cmd) {
		long now = System.currentTimeMillis();
		CMDConnector con = getConnector(host);
		if(con == null) {
			con = buildConnector(host);
		}
		if(con == null) {
			logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			ProjectManager.logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			return false;
		}
		COMMON_CMD_REQ req = new COMMON_CMD_REQ(CMDMessageFactory.nextSequnceNum() , cmd);
		try {
			COMMON_CMD_RES res = (COMMON_CMD_RES)con.requestMessage(req);
			String result = res.getResult()[0];
			logger.info("[执行命令] ["+cmd+"] ["+(System.currentTimeMillis()-now)+"ms] 结果:\n" + result);
			ProjectManager.logger.info("[执行命令] ["+cmd+"] ["+(System.currentTimeMillis()-now)+"ms] 结果:\n" + result);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[执行远程命令] [失败:请求失败] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
			ProjectManager.logger.error("[执行远程命令] [失败:请求失败] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
		}
		return false;
	}
	/**
	 * 在Server上执行指令
	 * @param server
	 * @param cmd
	 * @return 成功
	 */
	public boolean execCMDNoWait(String host, String cmd) {
		long now = System.currentTimeMillis();
		CMDConnector con = getConnector(host);
		if(con == null) {
			con = buildConnector(host);
		}
		if(con == null) {
			logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			ProjectManager.logger.error("[执行远程命令] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			return false;
		}
		COMMON_CMD_REQ req = new COMMON_CMD_REQ(CMDMessageFactory.nextSequnceNum() , cmd);
		try {
			con.requestMessageNoWait(req);
			logger.info("[执行命令] ["+cmd+"] ["+(System.currentTimeMillis()-now)+"ms]");
			ProjectManager.logger.info("[执行命令] ["+cmd+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[执行远程命令] [失败:请求失败] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
			ProjectManager.logger.error("[执行远程命令] [失败:请求失败] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"ms]", e);
		}
		return false;
	}
	
	/**
	 * 传输文件夹
	 * @param host
	 * @param port
	 * @param filepath
	 * @param remoteSavePath
	 * @return
	 */
	public boolean sendFolder(String host, String filepath, String remoteSavePath, List<String> excepts) {
		File file = new File(filepath);
		File files[] = file.listFiles();
		for(int i=0; i<files.length; i++) {
			String remotePath = remoteSavePath + files[i].getPath().substring(filepath.length());
			if(files[i].isDirectory()) {
				boolean succ = sendFolder(host, files[i].getPath(), remotePath, excepts);
				if(!succ) {
					return false;
				}
			} else {
				boolean succ = sendFile(host, files[i].getPath(), remotePath, excepts);
				if(!succ) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 传输文件到远程服务器
	 * @param host
	 * @param port
	 * @param filepath
	 * @return 成功
	 */
	public boolean sendFile(String host, String filepath, String remoteSavePath, List<String> excepts) {
		long now = System.currentTimeMillis();
		boolean needSend = true;
		for(String e : excepts) {
			if(filepath.startsWith(e)) {
				needSend = false;
				logger.info("[传输文件：此文件或目录不需要传输] ["+filepath+"] [match_except:"+e+"]");
				break;
			}
		}
		if(!needSend) {
			return true;
		}
		if(new File(filepath).isDirectory()) {
			return sendFolder(host, filepath, remoteSavePath, excepts);
		}
		CMDConnector con = getConnector(host);
		if(con == null) {
			con = buildConnector(host);
		}
		if(con == null) {
			logger.error("[传输文件] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			ProjectManager.logger.error("[传输文件] [失败:无法创建到服务器的连接] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"]");
			return false;
		}
		//传输
		byte data[] = FileUtils.readFileData(filepath);
		int packetSize = 102400;
		int total = data.length/packetSize;
		if(data.length % packetSize != 0) {
			total++;
		}
		//如果文件长度为0，那么也要传一个空数据的请求
		if(total == 0) {
			total = 1;
		}
		String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
		boolean succ = false;
		for(int i=0; i<total; i++) {
			long l = System.currentTimeMillis();
			int len = data.length - packetSize*i;
			if(len > packetSize) {
				len = packetSize;
			}
			byte sdata[] = new byte[len];
			System.arraycopy(data, i*packetSize, sdata, 0, len);
			FILE_PACKET_REQ req = new FILE_PACKET_REQ(CMDMessageFactory.nextSequnceNum(), sdata, total, i, filename, remoteSavePath);
			try {
				FILE_PACKET_RES res = (FILE_PACKET_RES)con.requestMessage(req);
				String result = res.getResult()[0];
				logger.info("[传送文件块] [文件:"+filename+"] [total:"+total+"] [index:"+i+"] [len:"+sdata.length+"] ["+(System.currentTimeMillis()-l)+"ms] 结果:" + result);
				ProjectManager.logger.info("[传送文件块] [文件:"+filename+"] [total:"+total+"] [index:"+i+"] ["+(System.currentTimeMillis()-l)+"ms] 结果:" + result);
				if(result.indexOf("error") != -1) {
					return false;
				}
				if( i == total-1 ) {
					//传的最后一个
					if(result.indexOf("succ") != -1) {
						succ = true;
					} else {
						return false;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("[传送文件块] [异常] [本地:"+filepath+"] [远程保存:"+remoteSavePath+"] [total:"+total+"] [index:"+i+"] [len:"+sdata.length+"] ["+host+"] ["+(System.currentTimeMillis()-l)+"ms]", e);
				ProjectManager.logger.info("[传送文件块] [异常] [本地:"+filepath+"] [远程保存:"+remoteSavePath+"] [total:"+total+"] [index:"+i+"] ["+host+"] ["+(System.currentTimeMillis()-l)+"ms]", e);
				return false;
			}
		}
		logger.info("[传输文件] ["+succ+"] [本地:"+filepath+"] [远程保存:"+remoteSavePath+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"]");
		ProjectManager.logger.info("[传输文件] ["+succ+"] [本地:"+filepath+"] [远程保存:"+remoteSavePath+"] ["+host+"] ["+GAME_SERVER_AGENT_PORT+"] ["+(System.currentTimeMillis()-now)+"]");
		return succ;
	}
	
	public boolean startServer(Server server) {
		String cmd = server.getResinhome() + "/bin/httpd.sh start";
		boolean succ = execCMD(server.getGameipaddr(), cmd);
		boolean started = false;
		int wnum = 0;
		while(!started && wnum < 30) {
			wnum++;
			try {
				Thread.sleep(1000);
				ServerStatus status = getServerStatus(server.getGameipaddr(), server);
				if(status.isRunning()) {
					started = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		succ = started;
		logger.info("[启动服务器] ["+succ+"] ["+server.getName()+"] ["+server.getGameipaddr()+"] ["+server.getResinhome()+"]");
		return succ;
	}
	
	public boolean stopServer(Server server) {
		String cmd = server.getResinhome() + "/bin/httpd.sh stop";
		boolean succ = execCMD(server.getGameipaddr(), cmd);
		boolean stoped = false;
		int wnum = 0;
		while(!stoped && wnum < 30) {
			wnum++;
			try {
				Thread.sleep(1000);
				ServerStatus status = getServerStatus(server.getGameipaddr(), server);
				if(!status.isRunning()) {
					stoped = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		succ = stoped;
		logger.info("[停止服务器] ["+succ+"] ["+server.getName()+"] ["+server.getGameipaddr()+"] ["+server.getResinhome()+"]");
		return succ;
	}
	
	
	class ServersMonitor implements Runnable
	{
		public Server[] servers;
		
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(servers != null)
			{
				for(Server server : servers)
				{
					try
					{
						boolean started = false;
						int wnum = 0;
						while(!started && wnum < 30) {
							wnum++;
							try {
								Thread.sleep(1000);
								ServerStatus status = getServerStatus(server.getGameipaddr(), server);
								if(status.isRunning()) {
									started = true;
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	
	public void startServers(Server[] servers) {
		
		for(Server server : servers)
		{
			String cmd = server.getResinhome() + "/bin/httpd.sh start";
			boolean succ = execCMDNoWait(server.getGameipaddr(), cmd);
		}
		
		ServersMonitor monitor = new ServersMonitor();
		monitor.servers = servers;
		Thread thread = new Thread(monitor);
		thread.start();
		logger.info("[批量启动服务器] [成功]");
		return ;
	}
	
	public void stopServers(Server[] servers) {
		for(Server server : servers)
		{
			String cmd = server.getResinhome() + "/bin/httpd.sh stop";
			boolean succ = execCMDNoWait(server.getGameipaddr(), cmd);
		}
		ServersMonitor monitor = new ServersMonitor();
		monitor.servers = servers;
		Thread thread = new Thread(monitor);
		thread.start();
		logger.info("[批量停止服务器] [成功]");
		return;
	}
	
		
	public CMDConnector buildConnector(String host) {
		DefaultConnectionSelector selector = new com.xuanzhi.tools.transport.DefaultConnectionSelector();
		selector.setEnableHeapForTimeout(true);
		selector.setName("CMDConnectorClient");
		selector.setClientModel(true);
		selector.setPort(GAME_SERVER_AGENT_PORT);
		selector.setHost(host);
		CMDConnector conn = new CMDConnector(selector);
		selector.setConnectionConnectedHandler(conn);
		try {
			selector.init();
			logger.error("[创建CMDConnector成功] [conn:"+conn.toString()+"]");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("[创建CMDConnector出错]", e1);
			ProjectManager.logger.error("[创建CMDConnector出错]", e1);
			return null;
		}
		connectors.add(conn);
		return conn;
	}
	
	public CMDConnector getConnector(String host) {
		for(int i=0; i<connectors.size(); i++ ) {
			CMDConnector con = connectors.get(i);
			DefaultConnectionSelector selector = (DefaultConnectionSelector)con.getConnectionSelector();
			if(selector.getHost().equals(host)) {
				return con;
			}
		}
		return null;
	}
	
	/**
	 * 同步测试服，并且发布页面
	 */
	public String syncTestAndPublish() {
		try {
			String cmd = "ant -buildfile /home/game/resin/game_engine_server/build.xml";
			Process proc = Runtime.getRuntime().exec(cmd);
			 BufferedReader br =
                 new BufferedReader(
                         new InputStreamReader(proc.getInputStream()));
	         String line = null;
	         while ((line = br.readLine()) != null) {
	        	 logger.debug(line + "\n");
	         }
	         br.close();
			cmd = "ant -buildfile /home/game/resin/game_engine_server/build.xml server_publish";
			proc = Runtime.getRuntime().exec(cmd);
			br =
                new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
	         while ((line = br.readLine()) != null) {
	        	 logger.debug(line + "\n");
	         }
	         br.close();
			cmd = "/home/game/resin/game_engine_server/conf/sync_50_init_conf.sh";
			proc = Runtime.getRuntime().exec(cmd);
			br =
                new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
	         while ((line = br.readLine()) != null) {
	        	 logger.debug(line + "\n");
	         }
	         br.close();
	         return "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[同步北京资源并且发布]", e);
		}
		return com.xuanzhi.language.translate.text_1477;
	}
	
}
