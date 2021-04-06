package com.xuanzhi.tools.transaction.impl;

import java.sql.Connection;
import java.util.Date;

import com.xuanzhi.tools.transaction.TransactionFuture;

public class JDBCTransactionFuture implements TransactionFuture {
	
	private Connection connection = null;
	
	private boolean success;
	
	private Object resultObject;
	
	private String trace;
	
	private java.util.Date startTime;
	
	private boolean transactionFinisehd = false;
	
	public JDBCTransactionFuture(Connection con, boolean success, Object resultObject, String trace) {
		this.connection = con;
		this.success = success;
		this.resultObject = resultObject;
		this.trace = trace;
		this.startTime = new java.util.Date();
	}

	public void commit() throws Exception {
		// TODO Auto-generated method stub
		try {
			if(connection != null) {
				connection.commit();
				connection.setAutoCommit(true);
				connection.close();
			}
		} catch(Exception e) {
			throw e;
		}
		this.transactionFinisehd = true;
	}

	public Object getResultObject() {
		// TODO Auto-generated method stub
		return resultObject;
	}

	public boolean isAccomplished() {
		// TODO Auto-generated method stub
		return success;
	}

	public void rollback() throws Exception {
		// TODO Auto-generated method stub
		try {
			if(connection != null) {
				connection.rollback();
				connection.setAutoCommit(true);
				connection.close();
			}
		} catch(Exception e) {
			throw e;
		}
		this.transactionFinisehd = true;
	}

	public boolean isTransactionFinished() {
		// TODO Auto-generated method stub
		return transactionFinisehd;
	}
	
	public Date getStartTime() {
		// TODO Auto-generated method stub
		return startTime;
	}

	public String getTrace() {
		return trace;
	}

}
