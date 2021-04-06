package com.fy.boss.game.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.game.dao.ServerDAO;
import com.fy.boss.game.model.Server;
import com.xuanzhi.tools.cache.LruMapCache;

public class ServerManager {

	protected static ServerManager m_self = null;
    
	public static final Logger log = Logger.getLogger(ServerManager.class);
	
	protected static LruMapCache mCache;
    
    protected ServerDAO serverDAO;
        
    public static ServerManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
    	mCache = new LruMapCache();
		m_self = this;
		System.out.println("["+ServerManager.class.getName()+"] [initialized]");
		log.info("["+ServerManager.class.getName()+"] [initialized]");
	}
	
	public Server createServer(Server server) {
		if(getServer(server.getName()) != null) {
			log.info("[创建服务器] [失败] [服务器名字已存在] ["+server.getName()+"]");
			return null;
		}
		serverDAO.saveNew(server);
		mCache.__put(server.getId(), server);
		log.info("[创建服务器] [成功] [-----------] ["+server.getName()+"]");
		return server;
	}

	public Server getServer(long id) {
		if(mCache.__get(id) != null) {
			return (Server)mCache.__get(id);
		}
		Server server = serverDAO.getById(id);
		if(server != null) {
			mCache.__put(server.getId(), server);
			mCache.__put(server.getName(), server);
			log.info("[获得服务器] [成功] [--------] ["+server.getName()+"]");
		} else {
			log.info("[获得服务器] [失败] [未找到服务器] ["+id+"]");
		}
		return server;
	}

	public Server getServer(String name) {
		if(mCache.__get(name) != null) {
			return (Server)mCache.__get(name);
		}
		Server server = serverDAO.getByName(name);
		log.info("[获得服务器] [失败] [未找到服务器] ["+name+"]");
		return server;
	}

	public long getCount() {
		return serverDAO.getCount();
	}
	
	public List<Server> getServers() {
		return serverDAO.getServerList();
	}
	
	public void updateServer(Server d) {
		serverDAO.update(d);
		mCache.remove(d.getId());
		if(log.isInfoEnabled()) {
			log.info("[更新服务器] ["+d.getId()+"] ["+d.getName()+"]");
		}
	}
	
	public void deleteServer(long id) {
		Server d = getServer(id);
		if(d != null) {
			serverDAO.delete(getServer(id));
			mCache.remove(d.getId());
		}
	}

	public ServerDAO getServerDAO() {
		return serverDAO;
	}

	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}
}
