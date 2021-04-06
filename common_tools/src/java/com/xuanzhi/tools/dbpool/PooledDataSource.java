package com.xuanzhi.tools.dbpool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * DataSource implementation, for spring hibernate 3 injection
 * 	<bean id="dataSource"
		class="com.linktone.tools.dbpool.PooledDataSource" init-method="initialize">
		<property name="alias" value="Connection-Pool-hibernate"></property>
		<property name="driverClassName"
			value="com.mysql.jdbc.Driver">
		</property>
		<property name="url"
			value="jdbc:mysql://locahost:3306/dbname?useUnicode=true&amp;characterEncoding=gbk">
		</property>
		<property name="username" value="root"></property>
		<property name="password" value="123456"></property>
		<property name="maxActive" value="50"></property>
		<property name="idleTimeout" value="120"></property>
		<property name="checkoutTimeout" value="120"></property>
	</bean>
 *
 */
public class PooledDataSource implements DataSource {
	
	//datasource名称，spring注入
	protected String alias;
	
	//驱动名
	protected String driverClassName;
	
	//url
	protected String url;
	
	//用户名
	protected String username;
	
	//密码
	protected String password;
	
	//最大连接数
	protected int maxActive = 32;
	
	//空闲超时
	protected int idleTimeout = 300;
	
	//获取超时
	protected int checkoutTimeout = 120;
	
	protected ConnectionPoolManager poolManager;
    
	public void initialize() throws Exception {
		poolManager = new ConnectionPoolManager("ConnectionPoolManager", 30);
		poolManager.addAlias(alias,driverClassName,url,username,password,maxActive,idleTimeout,checkoutTimeout);
		System.out.println("["+PooledDataSource.class.getName()+"] [initialized]");
	}

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return poolManager.getPool(alias).getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported by PoolableDataSource");
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported by PoolableDataSource");
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported by PoolableDataSource");
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported by PoolableDataSource");
	}

	public void setLoginTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not supported by PoolableDataSource");
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getCheckoutTimeout() {
		return checkoutTimeout;
	}

	public void setCheckoutTimeout(int checkoutTimeout) {
		this.checkoutTimeout = checkoutTimeout;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
