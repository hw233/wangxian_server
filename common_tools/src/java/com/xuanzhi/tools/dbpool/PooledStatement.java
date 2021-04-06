package com.xuanzhi.tools.dbpool;
import java.sql.*;
import java.util.Calendar;
import java.math.BigDecimal;

import org.apache.log4j.*;

public class PooledStatement implements java.sql.Statement 
{
    protected Statement stmt;
    protected PooledConnection m_oPooledCon;

    protected String lastQuerySQL;
    protected String lastUpdateSQL;
	
    public PooledStatement(Statement stmt, PooledConnection oPooledCon)
    {
        this.stmt = stmt;
        m_oPooledCon = oPooledCon;
    }

    public void close() throws SQLException {
        stmt.setMaxRows(0);
    }

    public Statement getStatement() {
        return stmt;
    }

    public String dumpInfo() {
        String LS = System.getProperty("line.separator");
        String report = "\t\t\t\tPooledStatement: " + this.toString()+ LS;
        report += "\t\t\t\t\t Last Query SQL: "+ lastQuerySQL+ LS;
        report += "\t\t\t\t\t Last Update SQL: "+ lastUpdateSQL+ LS;
        return report;
    }
    //
    //  Methods in Statement
    //

    public ResultSet executeQuery(String sql) throws SQLException
    {
        lastQuerySQL = sql;
        
        SQLException oException = null;
        long l = System.currentTimeMillis();
        
        try
        {
        	return stmt.executeQuery(sql);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	oException = e;
        	throw e;
        }
        finally
		{
        	if (ConnectionPool.m_bDebug)
        	{
        		Category oDebugCat = Category.getInstance(ConnectionPool.DEBUG_LOG_DESC);
        		if (oDebugCat != null)
        		{
        			oDebugCat.debug("["+(System.currentTimeMillis() - l)+" MS] ["+sql+"] []", oException);
        		}
        	}
		}
    }

    public int executeUpdate(String sql) throws SQLException
    {
        lastUpdateSQL = sql;
        
        SQLException oException = null;
        long l = System.currentTimeMillis();

        try
        {
        	return stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	oException = e;
        	throw e;
        }
        finally
		{
        	if (ConnectionPool.m_bDebug)
        	{
        		Category oDebugCat = Category.getInstance(ConnectionPool.DEBUG_LOG_DESC);
        		if (oDebugCat != null)
        		{
        			oDebugCat.debug("["+(System.currentTimeMillis() - l)+" MS] ["+sql+"] []", oException);
        		}
        	}
		}
    }

    public int getMaxFieldSize() throws SQLException
    {
        return stmt.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        stmt.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        return stmt.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        stmt.setMaxRows(max);
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        stmt.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        return stmt.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        stmt.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        stmt.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        return stmt.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        stmt.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        stmt.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException
    {
    	try
    	{
        	return stmt.execute(sql);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
    }

    public ResultSet getResultSet() throws SQLException {
        return stmt.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        return stmt.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        return stmt.getMoreResults();
    }
    public void setCharacterStream(int i, java.io.Reader r, int j) throws SQLException {
        setCharacterStream(i,r,j);
    }
    public int getFetchDirection() throws SQLException {
        return stmt.getFetchDirection();
    }
    public int executeBatch()[] throws SQLException
    {
    	try
    	{
        	return stmt.executeBatch();
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
    }
    public void setFetchSize(int i) throws SQLException {
        stmt.setFetchSize(i);
    }
    public void clearBatch() throws SQLException {
        stmt.clearBatch();
    }
    public void addBatch(java.lang.String s) throws SQLException {
        stmt.addBatch(s);
    }
    public int getResultSetConcurrency() throws SQLException   {
        return stmt.getResultSetConcurrency();
    }
    public void setFetchDirection(int i) throws SQLException {
        stmt.setFetchDirection(i);
    }
    public int getFetchSize() throws SQLException {
        return stmt.getFetchSize();
    }
    public java.sql.Connection getConnection() throws SQLException {
        return stmt.getConnection();
    }
    public int getResultSetType() throws SQLException {
        return stmt.getResultSetType();
    }
	public boolean execute(String sql,int i) throws SQLException
	{
		try
		{
			return stmt.execute(sql,i);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public boolean execute(String sql,int i[]) throws SQLException
	{
		try
		{
			return stmt.execute(sql,i);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public boolean execute(String sql,String params[]) throws SQLException
	{
        try
        {
			return stmt.execute(sql,params);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public int executeUpdate(String sql,int i) throws SQLException
	{
        try
        {
			return stmt.executeUpdate(sql,i);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public int executeUpdate(String sql,int i[]) throws SQLException
	{
        try
        {
			return stmt.executeUpdate(sql,i);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public int executeUpdate(String sql,String param[]) throws SQLException
	{
        try
        {
			return stmt.executeUpdate(sql,param);
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(this);
        	throw e;
        }
	}
	public boolean getMoreResults(int i) throws SQLException 
	{
		return stmt.getMoreResults(i);
	}
	public int getResultSetHoldability()  throws SQLException 
	{
		return stmt.getResultSetHoldability();
	}
	public ResultSet getGeneratedKeys() throws SQLException 
	{
		return stmt.getGeneratedKeys();
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return stmt.isClosed();
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return stmt.isPoolable();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		stmt.setPoolable(poolable);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return stmt.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return stmt.unwrap(iface);
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	



} 
