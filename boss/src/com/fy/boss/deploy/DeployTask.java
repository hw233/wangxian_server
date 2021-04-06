package com.fy.boss.deploy;

import java.util.Date;

import ch.qos.logback.classic.Logger;

import com.fy.boss.game.model.Server;
import com.fy.boss.deploy.DeployBoundry;
import com.fy.boss.deploy.Module;
import com.fy.boss.deploy.Project;
import com.fy.boss.deploy.ProjectManager;
import com.xuanzhi.tools.text.DateUtil;

public class DeployTask implements Runnable {	
	public Project project;
	
	public Module modules[];
	
	public Server server;
	
	public int percentage;
	
	public Thread localThread;
	
	public boolean running;
	
	public int type;
	
	public void start() {
		if(!running) {
			running = true;
			localThread = new Thread(this, "DeployTask-" + server.getName());
			localThread.start();
		}
	}
	
	public void stop() {
		running  = false;
		localThread.interrupt();
	}

	public Thread getLocalThread() {
		return localThread;
	}

	public void setLocalThread(Thread localThread) {
		this.localThread = localThread;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Module[] getModules() {
		return modules;
	}

	public void setModules(Module[] modules) {
		this.modules = modules;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void run() {
		boolean succ = false;
		// TODO Auto-generated method stub
		ProjectManager projectManager = ProjectManager.getInstance();
		if(type == ProjectManager.DEPLOY_NORMAIL) {
			succ = projectManager.deploy(project, modules, server, this);
		} else if(type == ProjectManager.DEPLOY_NOCOPY) {
			succ = projectManager.deployNoCopy(project, modules, server, this);
		} else if(type == ProjectManager.DEPLOY_COPY) {
			succ = projectManager.deployOnlyCopy(project, server, this);
		}
		running = false;
		projectManager.removeTask(server);
		
		if(!succ)
		{
			String errMess = "发布"+ server.getName() +  "服务器出现错误,时间为"+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
			synchronized (DeployBoundry.errinfoList) {
				DeployBoundry.errinfoList.add(errMess);
			}
			ProjectManager.logger.error("[发布服务器] [错误] [发布文件时出现问题] ["+server.getName()+"]");
				
		}
	}
}
