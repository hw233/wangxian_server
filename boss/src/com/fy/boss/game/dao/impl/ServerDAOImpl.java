package com.fy.boss.game.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.game.dao.ServerDAO;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class ServerDAOImpl implements ServerDAO {
	
	private static final Logger logger = ServerManager.log;
	

	// property constants
	public static final Class pojoClass = Server.class;
	
	private SimpleEntityManager<Server> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);
	
	public void saveNew(Server server)
	{
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			server.setId(id);
			em.flush(server);
			if(logger.isInfoEnabled())
				logger.info("[存储server实体] [成功] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储server实体] [失败] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	public void update(Server server)
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(server);
			if(logger.isInfoEnabled())
				logger.info("[更新server实体] [成功] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[更新server实体] [失败] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	public void delete(Server server)
	{
		long startTime = System.currentTimeMillis();
		try {
			em.remove(server);

			if(logger.isInfoEnabled())
				logger.info("[删除服务器] [成功] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[删除服务器] [失败] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
	}
	
	public Server getById(long id)
	{
		long startTime = System.currentTimeMillis();
		try {
			Server server = em.find(id);
			if(server != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id获取server实体] [成功] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id获取server实体] [成功] [没有对应此id的server] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return server;
		} catch (Exception e) {
			logger.error("[根据id获取server实体] [失败] [产生异常] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	public Server getByName(String name)
	{
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(Server.class, " name = '" +name+"'");
			Server server = em.find(ids[0]);
			if(server != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据Name获取server实体] [成功] [id:"+server.getId()+"] [serverName:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据Name获取server实体] [成功] [没有对应此name的server] [name:"+name+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return server;
		} catch (Exception e) {
			logger.error("[根据Name获取server实体] [失败] [产生异常] [name:"+name+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	public long getCount()
	{
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count();
			if(logger.isInfoEnabled())
				logger.info("[Servercount统计] [成功] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[Servercount统计] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}
	
	public List<Server> getServerList(){
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(Server.class,"");
			List<Server> lst = new ArrayList<Server>();
		
			for(long id : ids){
				Server server = em.find(id);
				lst.add(server);
				if(logger.isDebugEnabled())
					logger.debug("[获得所有游戏服务器信息] [成功] [加入到List中] [id:"+server.getId()+"] [name:"+server.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled()){
				logger.info("[获得所有游戏服务器信息] [成功] [Server数量:"+lst.size()+"] [id数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
			
		} catch (Exception e) {
			logger.error("[获得所有游戏服务器信息] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
}
