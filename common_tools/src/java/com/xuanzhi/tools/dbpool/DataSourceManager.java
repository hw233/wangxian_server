package com.xuanzhi.tools.dbpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataSourceManager {
	private static DataSourceManager self;
	
	private List<PooledDataSource> dataSources;
	
	private String defaultAlias;
	
	public static DataSourceManager getInstance() {
		return self;
	}
	
	public void initialize() {
		//
		self = this;
	}

	public void setDefaultAlias(String defaultAlias) {
		this.defaultAlias = defaultAlias;
	}

	public void setDataSources(List<PooledDataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	public void addDataSource(PooledDataSource ds) {
		if(this.dataSources  == null) {
			this.dataSources = new ArrayList<PooledDataSource>();
		}
		boolean updated = false;
		for(int i=0; i<dataSources.size(); i++) {
			if(dataSources.get(i).getAlias().equals(ds.getAlias())) {
				dataSources.set(i, ds);
				updated = true;
				break;
			}
		}
		if(!updated) {
			dataSources.add(ds);
		}
	}
	
	public PooledDataSource findDataSource(String alias) {
		for(PooledDataSource ds : dataSources) {
			if(ds.getAlias().equals(alias)) {
				return ds;
			}
		}
		return null;
	}
	
	public Connection getConnection() throws SQLException {
		PooledDataSource ds = findDataSource(defaultAlias);
		if(ds != null) {
			return ds.getConnection();
		} else {
			throw new SQLException("DataSource with alias "+defaultAlias+" not found.");
		}
	}
	
	public Connection getConnection(String alias) throws SQLException {
		PooledDataSource ds = findDataSource(alias);
		if(ds != null) {
			return ds.getConnection();
		} else {
			throw new SQLException("DataSource with alias "+alias+" not found.");
		}
	}
}
