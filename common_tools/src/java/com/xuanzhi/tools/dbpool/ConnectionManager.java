package com.xuanzhi.tools.dbpool;

import java.sql.*;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;


/**
 * @author Frank
 *
 */
public class ConnectionManager {

	protected ConnectionPoolManager poolManager;
	protected String defaultPoolName;
	protected String name = "DefaultConnectionManager";
	
	protected String poolConfigXml;
	
	private static ConnectionManager self = null;
	
	public static ConnectionManager getInstance(){
		return self;
	}
	
	public void initialize() throws Exception{
		poolManager = new ConnectionPoolManager(this.name, 30);
		Configuration config = new DefaultConfigurationBuilder().buildFromFile(poolConfigXml);
		Configuration cc[] = config.getChildren("pool");
		for(int i=0; i<cc.length; i++) {
			String poolName = cc[i].getAttribute("name", "");
			boolean isDefault = cc[i].getAttributeAsBoolean("default", false);
			String driver = cc[i].getChild("driver").getValue("");
			String url = cc[i].getChild("url").getValue("");
			String username = cc[i].getChild("user").getValue("");
			String password = cc[i].getChild("password").getValue("");
			int maxCon = cc[i].getChild("max-connection").getValueAsInteger(100);
			int  idleTimeout = cc[i].getChild("idle-timeout").getValueAsInteger(300);
			int checkoutTimeout = cc[i].getChild("checkout-timeout").getValueAsInteger(600);
			long dumpLimit = cc[i].getChild("dump-limit").getValueAsLong(2000L);
			poolManager.addAlias(poolName,driver,url,username,password,maxCon,idleTimeout,checkoutTimeout,0,dumpLimit,false);
			if(isDefault) {
				defaultPoolName = poolName;
			}
			System.out.println("["+poolName+"] ["+isDefault+"] ["+url+"]");
		}
		self = this;
		System.out.println("["+ConnectionManager.class.getName()+"] [initialized]");
	}
	
	
	public Connection getConnection() throws SQLException{
		return getConnection(defaultPoolName);
	}

	public Connection getConnection(String alias) throws SQLException {
		return poolManager.getPool(alias).getConnection();
	}
	
	public String getPoolConfigXml() {
		return poolConfigXml;
	}
	
	public void setPoolConfigXml(String poolConfigXml) {
		this.poolConfigXml = poolConfigXml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

