package com.xuanzhi.tools.dbpool;

import java.sql.*;
import java.util.Calendar;
import java.util.ArrayList;
import java.math.BigDecimal;

import java.io.InputStream;
import java.io.Reader;

import org.apache.log4j.*;

public class PooledPreparedStatement extends PooledStatement implements PreparedStatement {
    protected PreparedStatement pstmt;
	
	protected String sql ;
	protected int m_iParams = 0;
	protected Object[] m_oParamList = null;
	
    public PooledPreparedStatement(PreparedStatement stmt, String sql, PooledConnection oPooledCon)
    {
    	super(stmt, oPooledCon);
    	
		this.sql = sql;
        this.pstmt = (PreparedStatement)stmt;
        
        this.m_iParams = sql.split("\\?").length - (sql.endsWith("?") ? 0 : 1);
    }

	 public String dumpInfo()
	 {
        String LS = System.getProperty("line.separator");
        String report = "\t\t\t\tPooledPreparedStatement: " + this.toString()+ LS;
        report += "\t\t\t\t\t SQL: "+ sql+ LS;
        return report;
    }
	 
    public void close() throws SQLException {
        pstmt.clearParameters();
    }

    /**************************************************************************
     *
     * PreparedStatment methods
     *
     **************************************************************************/

    public ResultSet executeQuery() throws SQLException
    {
        SQLException oException = null;
        long l = System.currentTimeMillis();
        
    	try
    	{
        	return pstmt.executeQuery();
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(sql);
        	oException = e;
        	throw e;
        }
        finally
		{
        	if (ConnectionPool.m_bDebug && m_oParamList != null)
        	{
        		Category oDebugCat = Category.getInstance(ConnectionPool.DEBUG_LOG_DESC);
        		if (oDebugCat != null)
        		{
        			String strParams = "";
        			
    				for (int i = 0; i < m_oParamList.length; i++)
    				{
    					strParams += m_oParamList[i] + (i == m_oParamList.length - 1 ? "" : ", ");
    					m_oParamList[i] = null;
    				}
    				
        			oDebugCat.debug("["+(System.currentTimeMillis() - l)+" MS] ["+sql+"] ["+strParams+"]", oException);
        		}
        	}
		}
    }

    public int executeUpdate() throws SQLException
    {
        SQLException oException = null;
        long l = System.currentTimeMillis();
        
    	try
    	{
	        return pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(sql);
        	oException = e;
        	throw e;
        }
        finally
		{
        	if (ConnectionPool.m_bDebug && m_oParamList != null)
        	{
        		Category oDebugCat = Category.getInstance(ConnectionPool.DEBUG_LOG_DESC);
        		if (oDebugCat != null)
        		{
        			String strParams = "";
        			
    				for (int i = 0; i < m_oParamList.length; i++)
    				{
    					strParams += m_oParamList[i] + (i == m_oParamList.length - 1 ? "" : ", ");
    					m_oParamList[i] = null;
    				}
    				
        			oDebugCat.debug("["+(System.currentTimeMillis() - l)+" MS] ["+sql+"] ["+strParams+"]", oException);
        		}
        	}
		}
    }

    private void registerParam(int iIndex, Object o)
    {
    	if (ConnectionPool.m_bDebug)
    	{
	    	if (m_oParamList == null)
	    	{
	    		m_oParamList = new Object[m_iParams];
	    	}
    		
        	try
			{
        		m_oParamList[iIndex - 1] = o;
			}
        	catch (Throwable e)
			{
        		e.printStackTrace();
			}
    	}
    }
    
    public void setNull(int index, int sqlType) throws SQLException {
        pstmt.setNull(index, sqlType);
    }

    public void setBoolean(int index, boolean x) throws SQLException
	{
        pstmt.setBoolean(index, x);
        
        registerParam(index, x + "");
	}

    public void setByte(int index, byte x) throws SQLException
	{
        pstmt.setByte(index, x);
        
        registerParam(index, x + "");
    }

    public void setShort(int index, short x) throws SQLException
	{
        pstmt.setShort(index, x);
        
        registerParam(index, x + "");
	}

    public void setInt(int index, int x) throws SQLException
	{
        pstmt.setInt(index, x);
        
        registerParam(index, x + "");
    }

    public void setLong(int index, long x) throws SQLException
	{
        pstmt.setLong(index, x);
        
        registerParam(index, x + "");
    }

    public void setFloat(int index, float x) throws SQLException
	{
        pstmt.setFloat(index, x);
        
        registerParam(index, x + "");
    }

    public void setDouble(int index, double x) throws SQLException
	{
        pstmt.setDouble(index, x);
        
        registerParam(index, x + "");
    }

    public void setBigDecimal(int index, BigDecimal x) throws SQLException
	{
        pstmt.setBigDecimal(index, x);
        
        registerParam(index, x);
    }

    public void setString(int index, String x) throws SQLException
	{
        pstmt.setString(index, x);
        
        registerParam(index, x);
   }

    public void setBytes(int index, byte x[]) throws SQLException
	{
        pstmt.setBytes(index, x);
    }

    public void setDate(int index, Date x) throws SQLException
	{
        pstmt.setDate(index, x);
        
        registerParam(index, x);
    }

    public void setTime(int index, Time x) throws SQLException
	{
        pstmt.setTime(index, x);
        
        registerParam(index, x);
    }

    public void setTimestamp(int index, Timestamp x) throws SQLException
	{
        pstmt.setTimestamp(index, x);
        
        registerParam(index, x);
    }

    public void setAsciiStream(int index, InputStream x, int length)
        throws SQLException {
        pstmt.setAsciiStream(index, x, length);
    }
	/**
	 * @deprecated 
	 */
    public void setUnicodeStream(int index, InputStream x, int length)
        throws SQLException {
        pstmt.setUnicodeStream(index, x, length);
    }

    public void setBinaryStream(int index, InputStream x, int length)
        throws SQLException {
        pstmt.setBinaryStream(index, x, length);
    }

    public void clearParameters() throws SQLException
	{
        pstmt.clearParameters();
        
        if (m_oParamList != null)
        {
        	for (int i = 0; i < m_oParamList.length; i++)
        	{
        		m_oParamList[i] = null;
        	}
        }
    }

    public void setObject(int index, Object x, int target, int scale)
        throws SQLException {
        pstmt.setObject(index, x, target, scale);
    }

    public void setObject(int index, Object x, int target)
        throws SQLException {
        pstmt.setObject(index, x, target);
    }

    public void setObject(int index, Object x) throws SQLException
	{
        pstmt.setObject(index, x);
        
        registerParam(index, x);
    }

    public boolean execute() throws SQLException
    {
    	try
    	{
	        return pstmt.execute();
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(sql);
        	throw e;
        }
    }

    public void setCharacterStream(int i, java.io.Reader r, int j) throws SQLException {
        pstmt.setCharacterStream(i,r,j);
    }
    public int getFetchDirection() throws SQLException {
        return pstmt.getFetchDirection();
    }
    public int executeBatch()[] throws SQLException 
    {
    	try
    	{
	        return pstmt.executeBatch();
        }
        catch (SQLException e)
        {
        	m_oPooledCon.removeCachedStatement(sql);
        	throw e;
        }
    }
    public void setFetchSize(int i) throws SQLException {
        pstmt.setFetchSize(i);
    }
    public void clearBatch() throws SQLException {
        pstmt.clearBatch();
    }
    public void addBatch() throws SQLException {
        pstmt.addBatch();
    }
    public void addBatch(java.lang.String s) throws SQLException {
        pstmt.addBatch(s);
    }
    public void setRef(int i, java.sql.Ref r)  throws SQLException {
        pstmt.setRef(i,r);
    }
    public int getResultSetConcurrency() throws SQLException   {
        return pstmt.getResultSetConcurrency();
    }
    public void setClob(int i, java.sql.Clob c) throws SQLException {
        pstmt.setClob(i, c);
    }
    public void setFetchDirection(int i) throws SQLException {
        pstmt.setFetchDirection(i);
    }
    public void setDate(int i, java.sql.Date d, java.util.Calendar c) throws SQLException {
        pstmt.setDate(i, d, c);
    }
    public void setArray(int i, java.sql.Array a) throws SQLException {
        pstmt.setArray(i,a);
    }
    public void setBlob(int i, java.sql.Blob b) throws SQLException {
        pstmt.setBlob(i,b);
    }
    public int getFetchSize() throws SQLException {
        return pstmt.getFetchSize();
    }
    public void setTime(int i, java.sql.Time t , java.util.Calendar c) throws SQLException  {
        pstmt.setTime(i,t,c);
    }
    public java.sql.Connection getConnection() throws SQLException {
        return pstmt.getConnection();
    }
    public java.sql.ResultSetMetaData getMetaData() throws SQLException {
        return pstmt.getMetaData();
    }
    public int getResultSetType() throws SQLException {
        return pstmt.getResultSetType();
    }

    public void setTimestamp(int i , java.sql.Timestamp t, java.util.Calendar c) throws SQLException {
        pstmt.setTimestamp(i,t,c);
    }

    public void setNull(int i, int j, java.lang.String s) throws SQLException {
        pstmt.setNull(i,j,s);
    }
	
	public ParameterMetaData getParameterMetaData() throws SQLException 
	{
		return pstmt.getParameterMetaData();
	}
	
	public void setURL(int i,java.net.URL u) throws SQLException
	{
		pstmt.setURL(i,u);
        
        registerParam(i, u);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setAsciiStream(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setBinaryStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setBinaryStream(parameterIndex, x, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setBlob(parameterIndex, inputStream);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setBlob(parameterIndex, inputStream,length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setCharacterStream(parameterIndex, reader);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setCharacterStream(parameterIndex, reader, length);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setClob(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setClob(parameterIndex, reader, length);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNCharacterStream(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNClob(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNClob(parameterIndex, reader);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNClob(parameterIndex, reader, length);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setNString(parameterIndex, value);
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setRowId(parameterIndex, x);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		pstmt.setSQLXML(parameterIndex, xmlObject);
	}

}

