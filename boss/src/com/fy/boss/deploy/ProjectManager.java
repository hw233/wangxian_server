package com.fy.boss.deploy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.cmd.CMDService;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;

public class ProjectManager implements Runnable {

	protected static ProjectManager m_self = null;
    
	public static final Log logger = LogFactory.getLog(ProjectManager.class);

	protected ServerManager serverManager;
	protected String projectFile;
	protected CMDService cmdService;
	protected String resinModuleName = "resin_app_server";
	
	public static int DEPLOY_NORMAIL = 0;
	public static int DEPLOY_NOCOPY = 1;
	public static int DEPLOY_COPY = 2;
	
	private HashMap<Long, ServerStatus> statusMap;
	
	protected List<Project> projects;
	
	protected HashMap<String,DeployTask> taskMap;
	
	protected String files[];
	
	public static ProjectManager self;
	
	public static ProjectManager getInstance() {
		return self;
	}
	
	public void setServerManager(ServerManager serverManager) {
		this.serverManager = serverManager;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public void setProjectFile(String projectFile) {
		this.projectFile = projectFile;
	}

	public void setCmdService(CMDService cmdService) {
		this.cmdService = cmdService;
	}

	public void initialize() {
		statusMap = new HashMap<Long, ServerStatus>();
		taskMap = new HashMap<String,DeployTask>();
        File file = new File(projectFile);
		configure(file);
        MyWatchDog dog = new MyWatchDog();
        dog.setDaemon(true);
        dog.setName("ProjectManager-Watchlog");
        dog.addFile(file);
        dog.start();		
		self = this;
		Thread t = new Thread(this, "ProjectManager-ServerStatus-refresher");
		t.start();
        System.out.println("["+this.getClass().getName()+"] [initialized]");
		logger.info("["+this.getClass().getName()+"] [initialized]");
	}
	
    public class MyWatchDog extends com.xuanzhi.tools.watchdog.FileWatchdog {
	    public void doOnChange(File file) {
	        try {
	        	configure(file);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
    }
	
	private void configure(File file) {
		try {
			List<Project> list = new ArrayList<Project>();
			Configuration conf = new DefaultConfigurationBuilder().buildFromFile(file);
			Configuration projects[] = conf.getChildren("project");
			for(int i=0; i<projects.length; i++) {
				String name = projects[i].getAttribute("name", null);
				String game = projects[i].getAttribute("game", null);
				String basedir = projects[i].getAttribute("basedir", null);
				if(name == null || name.trim().length() == 0 || basedir == null || basedir.length() == 0) {
					logger.error("[初始化配置时出错] [Project参数不能为空] [索引:"+i+"]");
					throw new RuntimeException("[初始化配置时出错] [Project参数不能为空] [索引:"+i+"]");
				}
				Project pj = new Project();
				pj.setName(name);
				pj.setBasedir(basedir);
				pj.setGame(game);
				Configuration moduleConfs[] = projects[i].getChildren("module");
				List<Module> mlist = new ArrayList<Module>();
				for(int j=0; j<moduleConfs.length; j++) {
					String mname = moduleConfs[j].getAttribute("name", null);
					String mtype = moduleConfs[j].getAttribute("type", null);
					String from = moduleConfs[j].getAttribute("from", null);
					String to = moduleConfs[j].getAttribute("to", null);
					String parser = moduleConfs[j].getAttribute("parser", null);
					boolean common = moduleConfs[j].getAttributeAsBoolean("common", false);
					if(mname == null || mname.trim().length() == 0 
							|| mtype == null || mtype.length() == 0
							|| from == null || from.length() == 0
							|| to == null || to.length() == 0) {
						logger.error("[初始化配置时出错] [module参数不能为空] [索引:"+i+"/" + j +"]");
						throw new RuntimeException("[初始化配置时出错] [Project参数不能为空] [索引:"+i+"/" + j + "]");
					}
					Module mod = new Module();
					mod.setName(mname);
					mod.setType(mtype);
					mod.setFrom(from);
					mod.setTo(to);
					mod.setParser(parser);
					mod.setCommon(common);
					Configuration excepts[] = moduleConfs[j].getChildren("except");
					List<String> ecs = new ArrayList<String>();
					if(excepts != null && excepts.length > 0) {
						for(Configuration ec : excepts) {
							ecs.add(ec.getValue(""));
						}
					}
					mod.setExcepts(ecs);
					mlist.add(mod);
				}
				pj.setModules(mlist);
				list.add(pj);
				logger.info("[载入工程] ["+name+"] ["+basedir+"] ["+mlist.size()+"]");
			}
			this.projects = list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[初始化ProjectManager失败]", e);
			throw new RuntimeException(e);
		} 
	}
	
	public void run() {
		//取各台服务器的状态
		while(true) {
			try {
				Thread.sleep(20 * 1000);
				List<Server> slist = serverManager.getServers();
				for(Server server : slist) {
					String ip = server.getGameipaddr();
					ServerStatus status = cmdService.getServerStatus(ip, server);
					statusMap.put(server.getId(), status);
				}
				//logger.info("[获取所有服务器状态] ["+statusMap.size()+"] ["+(System.currentTimeMillis()-l)+"]");
			} catch(Throwable e) {
				e.printStackTrace();
				logger.error("[获取服务器状态] [异常]", e);
			}
		}
	}
	
	public int getRunningTaskNum() {
		DeployTask tasks[] = getTasks();
		int n = 0;
		for(DeployTask task : tasks) {
			if(task != null && task.isRunning()) {
				n++;
			}
		}
		return n;
	}
	
	public boolean startTask(Project project, Module modules[], Server server, int type) {
		DeployTask task = taskMap.get(server.getName());
//		if(task != null) {
//			CMDService.logger.debug("[发布已存在] [启动失败] ["+server.getName()+"] ["+task.getPercentage()+"]");
//			return false;
//		}
		int num = getRunningTaskNum();
		if(num > 0) {
			CMDService.logger.debug("[已经有发布在进行中] [启动失败] ["+server.getName()+"] ["+task.getPercentage()+"]");
			return false;
		}
		task = new DeployTask();
		task.setModules(modules);
		task.setPercentage(0);
		task.setProject(project);
		task.setServer(server);
		task.setType(type);
		task.start();
		taskMap.put(server.getName(), task);
		return true;
	}
	
	public void removeTask(Server server) {
		DeployTask task = taskMap.get(server.getName());
		if(task != null) {
			task.stop();
			taskMap.remove(server.getName());
		}
	}
	
	public DeployTask getTask(Server server) {
		return taskMap.get(server.getName());
	}
	
	public DeployTask[] getTasks() {
		return taskMap.values().toArray(new DeployTask[0]);
	}
	
	public Project getProject(String name) {
		for(Project pj : projects) {
			if(pj.getName().equals(name)) {
				return pj;
			}
		}
		return null;
	}
	
	public List<Project> getProjects(String game) {
		List<Project> pjs = new ArrayList<Project>();
		for(Project pj : projects) {
			String gameS=pj.getGame();
			if(gameS.equals(game)) {
				pjs.add(pj);
			}
		}
		return pjs;
	}
	
	/**
	 * 发布一个工程
	 * @param project
	 * @return
	 */
	public boolean publish(Project project, Server server) {
		List<Module> mlist = project.getModules();
		String rootdir = project.getBasedir() + "/" + server.getName();
		FileUtils.remove(new File(rootdir));
		boolean succ = true;
		for(Module mod : mlist) {
			String name = mod.getName();
			String from = mod.getFrom();
			String to = mod.getTo();
			String parser = mod.getParser();
			String todir = rootdir + "/" + to;
			if(new File(todir).isDirectory()) {
				FileUtils.clear(new File(todir));
			}
			FileUtils.chkFolder(rootdir+  "/" + to);
			try {
				FileUtils.copy(new File(from), new File(rootdir+  "/" + to), true);
				logger.info("[发布] ["+project.getName()+"->"+name+"] ["+from+"] ["+rootdir+  "/" + to+"] ["+parser+"]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("[发布时发生异常] ["+project.getName()+"->"+name+"]", e);
				succ = false;
				break;
			}
			List<String> ecs = mod.getExcepts();
			if(ecs != null) {
				String base = rootdir + "/" + mod.getTo();
				for(String ec : ecs) {
					if(ec != null && ec.trim().length() > 0) {
						if(ec.endsWith("*")) {
							FileUtils.clear(new File(base +  "/" + ec));
						} else {
							FileUtils.remove(new File(base +  "/" + ec));
						}
					}
				}
			}
			if(parser != null) {
				try {
					ModuleParser mp = (ModuleParser)Class.forName(parser).newInstance();
					mp.parse(new File(rootdir+  "/" + to), server);
					logger.info("[parse] ["+project.getName()+"->"+name+"] ["+parser+"]");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("[发布时发生异常] ["+project.getName()+"->"+name+"]", e);
					succ = false;
					break;
				} 
			}
		}
		return succ;
	}
	
	/**
	 * 整体发布工程
	 * @param project
	 * @param server
	 * @return
	 */
	public boolean deploy(Project project, Server server) {
		Module mod = project.getModule(resinModuleName);
		if(mod == null) {
			logger.error("[找不到resin module] [请确认配置是否正确]");
			return false;
		}
		String rootdir = project.getBasedir() + "/" + server.getName() + "/" + mod.getTo();
		if(!new File(rootdir).isDirectory()) {
			logger.error("[没有根目录，请确认已经发布] ["+rootdir+"]");
			return false;
		}
		boolean succ = false;
		long l = System.currentTimeMillis();
		String filepath = rootdir + ".tgz";
		try {
			if(new File(filepath).isFile()) {
				FileUtils.remove(new File(filepath));
			}
			
			Process p = Runtime.getRuntime().exec("tar cvfz " + rootdir + ".tgz " + rootdir);
			String result = getExecResult(p);
			if(new File(filepath).isFile()) {			
				logger.info("[先给工程打包] [成功] ["+filepath+"] ["+"tar cvfz " + rootdir + ".tgz " + rootdir+"] ["+(System.currentTimeMillis()-l)+"ms] 结果:\n" + result);
				succ = true;
			} else {
				logger.info("[先给工程打包] [失败] [打包文件不存在] ["+filepath+"] ["+"tar cvfz " + rootdir + ".tgz " + rootdir+"] ["+(System.currentTimeMillis()-l)+"ms] 结果:\n" + result);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[先给工程打包] [异常] ["+"tar cvfz " + rootdir + ".tgz " + rootdir+"] ["+(System.currentTimeMillis()-l)+"ms]", e);
		}
		if(succ) {
			//拷贝到远程服务器
			String filename = filepath.substring(filepath.lastIndexOf("/")+1);
			String resinHome = server.getResinhome();
			String resinParent = resinHome.substring(0, resinHome.lastIndexOf("/"));
			String remoteSavePath = resinParent + "/" + filename;
			succ = cmdService.sendFile(server.getGameipaddr(), filepath, remoteSavePath, new ArrayList<String>());
			logger.info("[拷贝到远程服务器] ["+succ+"] [host:"+server.getGameipaddr()+"] [本地:"+filepath+"] [远程:"+remoteSavePath+"]");
		
			if(succ) {
				//先备份远程服务器的目录
			//  tar cvfz /data1/resin_server_150410_183900.tgz /data1/resin_server
				String cmd = "tar cvfz " + resinHome + "_"+DateUtil.formatDate(new Date(), "yyMMdd_HHmmss")+".tgz " + resinHome;
				
				//resinHome=/data1/resin_server  
				
				
//				succ = cmdService.execCMD(server.getGameipaddr(), cmd);
//				logger.info("[备份远程resinHome目录] ["+succ+"] [host:"+server.getGameipaddr()+"] [远程目录:"+resinHome+"]");
				if(succ) {
					//删除远程目录
					cmd = "rm -rf " + resinHome;
					succ = cmdService.execCMD(server.getGameipaddr(), cmd);
					logger.info("[删除远程resinHome目录] ["+succ+"] [host:"+server.getGameipaddr()+"] [远程:"+resinHome+"]");
					if(succ) {
						//在远程服务器解压
						
						//remoteSavePath = /data1/resin.tgz
						
						// tar xzvf /data1/resin.tgz
						cmd = "tar xzvf " + remoteSavePath;
						succ = cmdService.execCMD(server.getGameipaddr(), cmd);
						logger.info("[执行解压命令] ["+succ+"] [host:"+server.getGameipaddr()+"] [本地:"+filepath+"] [远程:"+remoteSavePath+"]");
					
						if(succ) {
							//远程目录移动
							
							//resinParent = /data1
							// rootdir = 
							String remoteFolder = resinParent + "/agent" + rootdir;
							
							//mv /data1/agent/data1/deploy/腾讯测试服/resin /data1/resin_server
							// remoteFolder =  /data1/agent/data1/deploy/腾讯测试服/resin
							// resinHome = /data1/resin_server
							cmd = "mv " + remoteFolder + " " + resinHome;
							succ = cmdService.execCMD(server.getGameipaddr(), cmd);
							logger.info("[执行移动目录命令] ["+succ+"] [host:"+server.getGameipaddr()+"] ["+remoteFolder+"->"+resinHome+"] ["+cmd+"]");
							String tarRoot = resinParent + "/agent/home";
							// rm -rf /data1/agent/home
							// tarRoot = /data1/agent/home
							cmd = "rm -rf " + tarRoot;
							succ = cmdService.execCMD(server.getGameipaddr(), cmd);
							logger.info("[执行删除tar目录命令] ["+succ+"] [host:"+server.getGameipaddr()+"] ["+tarRoot+"]");
							
							//完全部署后修改bin/httpd.sh的权限
							String httpd = resinHome + "/bin/httpd.sh";
							cmd = "chmod 755 " + httpd;
							succ = cmdService.execCMD(server.getGameipaddr(), cmd);
							logger.info("[修改httpd.sh执行权限] ["+succ+"] [host:"+server.getGameipaddr()+"] ["+cmd+"]");
						}
					}
				}
			}
		}
		return succ;
	}
	
	/**
	 * 部署模块
	 * @param project
	 * @param module
	 * @param server
	 * @return 成功
	 */
	public boolean deploy(Project project, Module modules[], Server server, DeployTask task) {
		logger.warn("部署模块======>"+project.getName()+"0");
		if(modules.length > 0) {
			task.setPercentage(0);
			ProjectManager projectManager = ProjectManager.getInstance();
			logger.warn("部署模块======>"+project.getName()+"1");
			boolean succ = projectManager.publish(project, server);
			logger.warn("部署模块======>"+project.getName()+"2");
			if(!succ) {
				task.setPercentage(-1);
				logger.error("[发布代码错误]");
				return false;
			}
			//先备份远程服务器的目录
			task.setPercentage(20);
			String resinHome = server.getResinhome();
			String cmd = "rm -rf " + resinHome + "_bak";
			
//			String cmd = "tar cvfz " + resinHome + "_"+DateUtil.formatDate(new Date(), "yyMMdd_HHmmss")+".tgz " + resinHome;
//			logger.warn("部署模块======>"+project.getName()+"3");
			cmdService.execCMD(server.getGameipaddr(), cmd);
//			cmd = "cp -r  "+resinHome+" "+resinHome+"_bak";
//			succ = cmdService.execCMD(server.getGameipaddr(), cmd);
//			logger.warn("部署模块======>"+project.getName()+"4");
			logger.info("[备份远程resinHome目录] ["+succ+"] [host:"+server.getGameipaddr()+"] [远程目录:"+resinHome+"]");
			if(succ) {
				task.setPercentage(30);
				float t = 70f/new Float(modules.length);
				
				if(files != null && files.length > 0) {
					t = 70f/new Float(files.length+1);
					String root = project.getBasedir() + "/" + server.getName() + "/resin/webapps/game_server";
					resinHome = server.getResinhome();
					String resinParent = resinHome.substring(0, resinHome.lastIndexOf("/"));
					int findex = 0;
					for(String f : files) {
						String ff = root + f;
						String remotePath = ff.replaceAll(root, resinHome + "/webapps/game_server");
						succ = cmdService.sendFile(server.getGameipaddr(), ff, remotePath, new ArrayList<String>());
						logger.info("[拷贝选中文件] ["+succ+"] ["+f+"] [本地:"+ff+"] [远程:"+remotePath+"]");
						if(!succ) {
							task.setPercentage(-1);
							return false;
						}
						task.setPercentage(30+new Float(((findex++)+1)*t).intValue());
					}
					
//					for(int i=0; i<modules.length; i++) {
//						if(modules[i].getTo().indexOf("spring_config") != -1 || modules[i].getName().equals("web.xml")) {
//							String resinName =  resinHome.substring(resinHome.lastIndexOf("/")+1);
//							String moduleRemotePath = resinParent + "/" + modules[i].getTo();
//							moduleRemotePath = moduleRemotePath.replaceAll("resin", resinName);
//							String moduleLocalPath = project.getBasedir() + "/" + server.getName() + "/" + modules[i].getTo();
//							
//							List<String> ecs = modules[i].getExcepts();
//							List<String> necs = new ArrayList<String>();
//							for(String ec : ecs) {
//								necs.add(moduleLocalPath + "/" + ec);
//							}
//							logger.warn("部署模块======>"+project.getName()+" "+modules[i].getName()+" 5");
//							succ = cmdService.sendFile(server.getGameipaddr(), moduleLocalPath, moduleRemotePath, necs);
//							logger.info("[部署模块] ["+succ+"] [本地:"+moduleLocalPath+"] [远程:"+moduleRemotePath+"]");
//							if(!succ) {
//								logger.warn("部署模块失败======>"+project.getName()+" "+modules[i].getName()+" 6");
//								task.setPercentage(-1);
//								return false;
//							}
//						}
//						
//						task.setPercentage(30+new Float(((findex)+1)*t).intValue());
//						
//					}
					
				} else {
					for(int i=0; i<modules.length; i++) {
						if(modules[i].getName().equals(resinModuleName)) {
							logger.error("[无效的部署模块：部署此模块需要完整部署，跳过] ["+resinModuleName+"]");
							continue;
						} else {
							resinHome = server.getResinhome();
							String resinName =  resinHome.substring(resinHome.lastIndexOf("/")+1);
							String resinParent = resinHome.substring(0, resinHome.lastIndexOf("/"));
							String moduleRemotePath = resinParent + "/" + modules[i].getTo();
							moduleRemotePath = moduleRemotePath.replaceAll("resin", resinName);
							String moduleLocalPath = project.getBasedir() + "/" + server.getName() + "/" + modules[i].getTo();
							
							List<String> ecs = modules[i].getExcepts();
							List<String> necs = new ArrayList<String>();
							for(String ec : ecs) {
								necs.add(moduleLocalPath + "/" + ec);
							}
							logger.warn("部署模块======>"+project.getName()+" "+modules[i].getName()+" 5");
							succ = cmdService.sendFile(server.getGameipaddr(), moduleLocalPath, moduleRemotePath, necs);
							logger.info("[部署模块] ["+succ+"] [本地:"+moduleLocalPath+"] [远程:"+moduleRemotePath+"]");
							if(!succ) {
								logger.warn("部署模块失败======>"+project.getName()+" "+modules[i].getName()+" 6");
								task.setPercentage(-1);
								return false;
							}
						}
						task.setPercentage(30+new Float((i+1)*t).intValue());
					}
				}
				task.setPercentage(100);
				logger.warn("[全部部署完成] ["+task.getServer().getName()+"] ["+task.getProject().getName()+"] ["+task.getPercentage()+"]");
				return succ;
			}
		}
		task.setPercentage(-1);
		return false;
	}
	
	/**
	 * 部署模块
	 * @param project
	 * @param module
	 * @param server
	 * @return 成功
	 */
	public boolean deployNoCopy(Project project, Module modules[], Server server, DeployTask task) {
		logger.warn("部署模块不拷贝======>"+project.getName()+"0");
		if(modules.length > 0) {
			task.setPercentage(0);
			ProjectManager projectManager = ProjectManager.getInstance();
			logger.warn("部署模块不拷贝======>"+project.getName()+"1");
			boolean succ = projectManager.publish(project, server);
			logger.warn("部署模块不拷贝======>"+project.getName()+"2");
			if(!succ) {
				task.setPercentage(-1);
				logger.error("[发布代码错误]");
				return false;
			}
			task.setPercentage(20);
			String resinHome = server.getResinhome();
			if(succ) {
				task.setPercentage(30);
				float t = 70f/new Float(modules.length);
			
				for(int i=0; i<modules.length; i++) {
					if(modules[i].getName().equals(resinModuleName)) {
						logger.error("[无效的部署模块不拷贝：部署此模块需要完整部署，跳过] ["+resinModuleName+"]");
						continue;
					} else {
						resinHome = server.getResinhome();
						String resinName =  resinHome.substring(resinHome.lastIndexOf("/")+1);
						String resinParent = resinHome.substring(0, resinHome.lastIndexOf("/"));
						String moduleRemotePath = resinParent + "/deploy/" + modules[i].getTo();
						moduleRemotePath = moduleRemotePath.replaceAll("resin", resinName);
						String moduleLocalPath = project.getBasedir() + "/" + server.getName() + "/" + modules[i].getTo();
						
						List<String> ecs = modules[i].getExcepts();
						List<String> necs = new ArrayList<String>();
						for(String ec : ecs) {
							necs.add(moduleLocalPath + "/" + ec);
						}
						logger.warn("部署模块不拷贝======>"+project.getName()+" "+modules[i].getName()+" 5");
						succ = cmdService.sendFile(server.getGameipaddr(), moduleLocalPath, moduleRemotePath, necs);
						logger.info("[部署模块不拷贝] ["+succ+"] [本地:"+moduleLocalPath+"] [远程:"+moduleRemotePath+"]");
						if(!succ) {
							logger.warn("部署模块不拷贝失败======>"+project.getName()+" "+modules[i].getName()+" 6");
							task.setPercentage(-1);
							return false;
						}
					}
					task.setPercentage(30+new Float((i+1)*t).intValue());
				}
				task.setPercentage(100);
				logger.warn("[全部部署完成] ["+task.getServer().getName()+"] ["+task.getProject().getName()+"] ["+task.getPercentage()+"]");
				return succ;
			}
		}
		task.setPercentage(-1);
		return false;
	}
	
	public boolean deployOnlyCopy(Project project, Server server, DeployTask task) {
		logger.warn("部署模块仅拷贝======>"+project.getName()+"0");
		task.setPercentage(0);
		String resinHome = server.getResinhome();
		String resinName =  resinHome.substring(resinHome.lastIndexOf("/")+1);
		String resinParent = resinHome.substring(0, resinHome.lastIndexOf("/"));
		String deployRemotePath = resinParent + "/deploy/" + resinName + "/webapps/game_server";
		String cmd = "cp -r " + deployRemotePath + " " + resinHome + "/webapps/";
		boolean succ = cmdService.execCMD(server.getGameipaddr(), cmd);
		if(succ) {
			task.setPercentage(100);
			logger.info("[发布仅拷贝] ["+succ+"] [host:"+server.getGameipaddr()+"] [cmd:"+cmd+"] [远程目录:"+resinHome+"]");
			return true;
		} else {
			task.setPercentage(-1);
			return false;
		}
	}
	
	private String getExecResult(Process p) {
		try {
	        BufferedReader br =
	                new BufferedReader(
	                        new InputStreamReader(p.getInputStream()));
	        String line = null;
	        StringBuffer sb = new StringBuffer();
	        while ((line = br.readLine()) != null) {
	        		sb.append( line +  "\n");
	        }
	        br.close();
	        return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public ServerStatus getServerStatus(Server server) {
		return statusMap.get(server.getId());
	}

	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}
	
	
	
}
