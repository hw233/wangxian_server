package com.fy.boss.game.dao;

import java.util.List;

import com.fy.boss.game.model.Server;

public interface ServerDAO {
	
	public void saveNew(Server server);
	
	public void update(Server server);
	
	public void delete(Server server);
	
	public Server getById(long id);
	
	public Server getByName(String name);
	
	public long getCount();
	
	public List<Server> getServerList();
}
