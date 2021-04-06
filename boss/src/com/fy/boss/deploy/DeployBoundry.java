package com.fy.boss.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fy.boss.cmd.CMDService;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;

public class DeployBoundry {
	public static List<String> errinfoList = new ArrayList<String>();
	
	
	public String[] getAllServerStatus() {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager sm = ServerManager.getInstance();
		List<Server> slist = sm.getServers();
		String status[] = new String[slist.size()];
		for(int i=0; i<status.length; i++) {
			DeployTask task = pm.getTask(slist.get(i));
			if(task != null) {
				long id = task.getServer().getId();
				int percentage = task.getPercentage();
				status[i] = id + "__" + percentage;
			} else {
				status[i] = slist.get(i).getId() + "__-2";
			}
		}
		return status;
	}
	
	public void deploy(long serverid) {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager smanager = ServerManager.getInstance();
		Server server = smanager.getServer(serverid);
		List<Project> list = pm.getProjects("飘渺寻仙曲");
		Project pj = null;
		for(Project p : list) {
			if(p.getName().indexOf("游戏服") != -1) {
				pj = p;
				break;
			}
		}
		Module mods[] = pj.getCommonModules().toArray(new Module[0]);
		ProjectManager.logger.warn("[发布TEST] [serverid:"+serverid+"] [server:"+(server!=null?server.getName():"null")+"] [list长度:"+(list!=null?list.size():"null")+"] [pj:"+(pj!=null?pj:"null")+"]");
		if(mods != null){
			for(Module m : mods){
				ProjectManager.logger.warn("[ModuleTEST] ["+m+"]");
			}
		}
		
		pm.startTask(pj, mods, server, ProjectManager.DEPLOY_NORMAIL);
	}
	
	public void deployNoCopy(long serverid) {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager smanager = ServerManager.getInstance();
		Server server = smanager.getServer(serverid);
		List<Project> list = pm.getProjects("飘渺寻仙曲");
		Project pj = null;
		for(Project p : list) {
			if(p.getName().indexOf("游戏服") != -1) {
				pj = p;
				break;
			}
		}
		Module mods[] = pj.getCommonModules().toArray(new Module[0]);
		pm.startTask(pj, mods, server, ProjectManager.DEPLOY_NOCOPY);
	}
	
	public void deployCopy(long serverid) {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager smanager = ServerManager.getInstance();
		Server server = smanager.getServer(serverid);
		List<Project> list = pm.getProjects("飘渺寻仙曲");
		Project pj = null;
		for(Project p : list) {
			if(p.getName().indexOf("游戏服") != -1) {
				pj = p;
				break;
			}
		}
		Module mods[] = pj.getCommonModules().toArray(new Module[0]);
		pm.startTask(pj, mods, server, ProjectManager.DEPLOY_COPY);
	}
	
	public void syncToPublish() {
		CMDService cmdService = CMDService.getInstance();
		cmdService.syncTestAndPublish();
	}
	
	public void startServer(long serverid) {
		ServerManager smanager = ServerManager.getInstance();
		CMDService cmdService = CMDService.getInstance();
		Server server = smanager.getServer(serverid);
		cmdService.startServer(server);
	}
	
	public void stopServer(long serverid) {
		ServerManager smanager = ServerManager.getInstance();
		CMDService cmdService = CMDService.getInstance();
		Server server = smanager.getServer(serverid);
		cmdService.stopServer(server);
	}
	
	public void confirmSucc(long serverid) {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager smanager = ServerManager.getInstance();
		Server server = smanager.getServer(serverid);
		if(server != null) {
			pm.removeTask(server);
		}
	}
	
	public void removeDeploy(long serverid) {
		ProjectManager pm = ProjectManager.getInstance();
		ServerManager smanager = ServerManager.getInstance();
		Server server = smanager.getServer(serverid);
		pm.removeTask(server);
	}
	
	public String getAllFiles() {
		ProjectManager pm = ProjectManager.getInstance();
		List<Project> list = pm.getProjects("飘渺寻仙曲");
		Project pj = null;
		for(Project p : list) {
			if(p.getName().indexOf("游戏服") != -1) {
				pj = p;
				break;
			}
		}
		Module mods[] = pj.getCommonModules().toArray(new Module[0]);
		for(Module mod : mods) {
			if(mod.getName().equals("gameapp")) {
				StringBuffer sb = new StringBuffer();
				File f = new File(mod.getFrom());
				listFilePathRecursive(1, mod.getFrom(), f, sb);
				return sb.toString();
			}
		}
		return "";
	}
	
	public void listFilePathRecursive(int depth, String root, File f, StringBuffer sb) {
		File ff[] = f.listFiles();
		for(File fs : ff) {
			if(fs.getPath().indexOf("WEB-INF") != -1) {
				if(!fs.getPath().endsWith("WEB-INF") && fs.getPath().indexOf("/game_init_config") == -1  && fs.getPath().indexOf("/spring_config") == -1 && fs.getPath().indexOf("/lib") == -1) {
					continue;
				}
			} else {
				if(fs.getPath().indexOf("/admin") == -1 && fs.getPath().indexOf("/gm") == -1 && fs.getPath().indexOf("/migu") == -1) {
					continue;
				}
			}
			if(fs.isDirectory()) {
				if(!fs.getPath().endsWith("WEB-INF")) {
					sb.append(fs.getPath().substring(root.length()) + "##0##" + depth + ";;");
				}
				if(fs.getPath().indexOf("WEB-INF") != -1 && depth < 3) {
					listFilePathRecursive(depth+1, root, fs, sb);
				} else if(fs.getPath().indexOf("/admin") != -1 && depth < 2) {
					listFilePathRecursive(depth+1, root, fs, sb);
				} else if(fs.getPath().indexOf("/gm") != -1 && depth < 2) {
					listFilePathRecursive(depth+1, root, fs, sb);
				}
			} else {
				sb.append(fs.getPath().substring(root.length()) + "##1##" + depth + ";;");
			}
		}
	}
	
	public String getSelectedFiles() {
		String files[] = ProjectManager.getInstance().getFiles();
		if(files != null && files.length > 0) {
			StringBuffer sb = new StringBuffer();
			for(String f : files) {
				sb.append(f + ";;");
			}
			String s = sb.toString();
			if(s.length() > 0) {
				s = s.substring(0, s.length()-2);
			}
			return s;
		}
		return "";
	}
	
	public int getRunningTaskNum() {
		ProjectManager pm = ProjectManager.getInstance();
		DeployTask tasks[] = pm.getTasks();
		int n = 0;
		for(DeployTask task : tasks) {
			if(task != null && task.isRunning()) {
				n++;
			}
		}
		return n;
	}
	
	public void batchStopServers(long[]serverIds)
	{
		for(long serverId : serverIds)
		{
			stopServer(serverId);
		}
	}
	
	public void batchStartServers(long[]serverIds)
	{
		for(long serverId : serverIds)
		{
			startServer(serverId);
		}
	}
	
	
	public void startServers(long[] serverids) {
		ServerManager smanager = ServerManager.getInstance();
		CMDService cmdService = CMDService.getInstance();
		Server[] servers = new Server[serverids.length];
		for(int i= 0; i<serverids.length; i++)
		{
			Server server = smanager.getServer(serverids[i]);
			servers[i] = server;
		}
		cmdService.startServers(servers);
	}
	
	public void stopServers(long[] serverids) {
		ServerManager smanager = ServerManager.getInstance();
		CMDService cmdService = CMDService.getInstance();
		Server[] servers = new Server[serverids.length];
		for(int i= 0; i<serverids.length; i++)
		{
			Server server = smanager.getServer(serverids[i]);
			servers[i] = server;
		}
		cmdService.stopServers(servers);
	}
	
	public String[] getErrMesses()
	{
		return errinfoList.toArray(new String[0]);
	}
	
}
