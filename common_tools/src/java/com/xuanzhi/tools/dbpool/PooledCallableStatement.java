package com.xuanzhi.tools.dbpool;


import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.math.BigDecimal;
import java.lang.reflect.*;
import java.io.InputStream;
import java.io.Reader;

public class PooledCallableStatement extends PooledPreparedStatement implements CallableStatement {
    
    protected CallableStatement cstmt;
    
    public PooledCallableStatement(CallableStatement stmt, PooledConnection oPooledCon)
    {
        super(stmt, null, oPooledCon);
        
        this.cstmt = (CallableStatement)cstmt;
    }
    
    public void close() throws SQLException {  }
    
    
    
    /**************************************************************************
     *
     * CallableStatment methods
     *
     **************************************************************************/
    
    public void registerOutParameter(int index, int type) throws SQLException {
        cstmt.registerOutParameter(index, type);
    }
    
    public void registerOutParameter(int index, int type, int scale) throws SQLException {
        cstmt.registerOutParameter(index, type, scale);
    }
    
    public boolean wasNull() throws SQLException {
        return cstmt.wasNull();
    }
    
	/**
	 *@deprecated  
	 */
    public BigDecimal getBigDecimal(int index, int scale) throws SQLException {
        return cstmt.getBigDecimal(index, scale);
    }
    
    public boolean getBoolean(int index) throws SQLException {
        return cstmt.getBoolean(index);
    }
    
    public byte getByte(int index) throws SQLException {
        return cstmt.getByte(index);
    }
    
    public byte[] getBytes(int index) throws SQLException {
        return cstmt.getBytes(index);
    }
    
    public Date getDate(int index) throws SQLException {
        return cstmt.getDate(index);
    }
    
    public double getDouble(int index) throws SQLException {
        return cstmt.getDouble(index);
    }
    
    public float getFloat(int index) throws SQLException {
        return cstmt.getFloat(index);
    }
    
    public int getInt(int index) throws SQLException {
        return cstmt.getInt(index);
    }
    
    public long getLong(int index) throws SQLException {
        return cstmt.getLong(index);
    }
    
    public Object getObject(int index) throws SQLException {
        return cstmt.getObject(index);
    }
    
    public short getShort(int index) throws SQLException {
        return cstmt.getShort(index);
    }
    
    public String getString(int index) throws SQLException {
        return cstmt.getString(index);
    }
    
    public Time getTime(int index) throws SQLException {
        return cstmt.getTime(index);
    }
    
    public Timestamp getTimestamp(int index) throws SQLException {
        return cstmt.getTimestamp(index);
    }
    
    
    public java.sql.Time getTime(int i , java.util.Calendar c)  throws SQLException {
        return cstmt.getTime(i,c);
    }
    public java.sql.Clob getClob(int i) throws SQLException  {
        return cstmt.getClob(i);
    }
    public java.math.BigDecimal getBigDecimal(int i)  throws SQLException {
        return cstmt.getBigDecimal(i);
    }
    public void registerOutParameter(int i, int j, java.lang.String s)  throws SQLException {
        cstmt.registerOutParameter(i,j,s);
    }
    public java.sql.Timestamp getTimestamp(int i, java.util.Calendar c)  throws SQLException {
        return cstmt.getTimestamp(i,c);
    }
    public java.sql.Blob getBlob(int i)  throws SQLException {
        return cstmt.getBlob(i);
    }
    public java.sql.Array getArray(int i)  throws SQLException {
        return cstmt.getArray(i);
    }
    public java.sql.Ref getRef(int i)  throws SQLException {
        return cstmt.getRef(i);
    }
    public java.sql.Date getDate(int i, java.util.Calendar c)  throws SQLException {
        return cstmt.getDate(i,c);
    }

	public java.sql.Array getArray(String s) throws SQLException 
	{
		return cstmt.getArray(s);
	}
	public BigDecimal getBigDecimal(String s) throws SQLException
	{
		return cstmt.getBigDecimal(s);
	}
	public Blob getBlob(String s) throws SQLException
	{
		return cstmt.getBlob(s);
	}
	public boolean getBoolean(String s) throws SQLException 
	{
		return cstmt.getBoolean(s);
	}
	public byte getByte(String s) throws SQLException{
		return cstmt.getByte(s);
	}
	public byte[] getBytes(String s) throws SQLException
	{
		return cstmt.getBytes(s);
	}
	public Clob getClob(String s) throws SQLException
	{
		return cstmt.getClob(s);
	}
	public java.sql.Date getDate(String s) throws SQLException
	{
		return cstmt.getDate(s);
	}
	public void setDate(String s,java.sql.Date d) throws SQLException
	{
		cstmt.setDate(s,d);
	}
	public java.sql.Date getDate(String s,Calendar c) throws SQLException
	{
		return cstmt.getDate(s,c);
	}
	public void setDate(String s,java.sql.Date d,Calendar c) throws SQLException
	{
		cstmt.setDate(s,d,c);
	}
	public double getDouble(String s) throws SQLException
	{
		return cstmt.getDouble(s);
	}
	public void setDouble(String s,double d) throws SQLException
	{
		cstmt.setDouble(s,d);
	}
    public float getFloat(String s) throws SQLException
	{
		return cstmt.getFloat(s);
	}
	public void setFloat(String s,float f) throws SQLException
	{
		cstmt.setFloat(s,f);
	}
	public int getInt(String s) throws SQLException
	{
		return cstmt.getInt(s);
	}
	public void setInt(String s,int l) throws SQLException
	{
		cstmt.setInt(s,l);
	}
    public long getLong(String s) throws SQLException
	{
		return cstmt.getLong(s);
	}
	public void setLong(String s,long l) throws SQLException
	{
		cstmt.setLong(s,l);
	}
	public Object getObject(String s) throws SQLException
	{
		return cstmt.getObject(s);
	}

	public void setNull(String s,int i) throws SQLException
	{
		cstmt.setNull(s,i);
	}
	public void setNull(String s,int i,String o) throws SQLException
	{
		cstmt.setNull(s,i,o);
	}
	public void setObject(String s,Object o) throws SQLException
	{
		cstmt.setObject(s,o);
	}
	public void setObject(String s,Object o,int i1) throws SQLException
	{
		cstmt.setObject(s,o,i1);
	}
	public void setObject(String s,Object o,int i1,int i2) throws SQLException
	{
		cstmt.setObject(s,o,i1,i2);
	}
	public Ref getRef(String s) throws SQLException
	{
		return cstmt.getRef(s);
	}
	public short getShort(String s) throws SQLException
	{
		return cstmt.getShort(s);
	}
	public void setShort(String s,short c) throws SQLException
	{
		cstmt.setShort(s,c);
	}
	public String getString(String s) throws SQLException
	{
		return cstmt.getString(s);
	}
	public void setString(String s,String c) throws SQLException
	{
		cstmt.setString(s,c);
	}
	public java.sql.Time getTime(String s) throws SQLException
	{
		return cstmt.getTime(s);
	}
	
	public void setTime(String s,java.sql.Time t) throws SQLException
	{
		cstmt.setTime(s,t);
	}
	public java.sql.Time getTime(String s,Calendar c) throws SQLException
	{
		return cstmt.getTime(s,c);
	}
	public void setTime(String s,java.sql.Time t,Calendar c) throws SQLException
	{
		cstmt.setTime(s,t,c);
	}
	public Timestamp getTimestamp(String s ) throws SQLException
	{
		return cstmt.getTimestamp(s);
	}
	public void setTimestamp(String s,Timestamp t) throws SQLException
	{
		cstmt.setTimestamp(s,t);
	}
	public Timestamp getTimestamp(String s,Calendar c) throws SQLException
	{
		return cstmt.getTimestamp(s,c);
	}
	public void setTimestamp(String s,Timestamp t,Calendar c) throws SQLException
	{
		cstmt.setTimestamp(s,t,c);
	}
	public java.net.URL getURL(int i) throws SQLException
	{
		return cstmt.getURL(i);
	}
	public java.net.URL getURL(String i) throws SQLException
	{
		return cstmt.getURL(i);
	}
	public void setURL(String s,java.net.URL  i) throws SQLException
	{
		cstmt.setURL(s,i);
	}
	public void registerOutParameter(String s,int i) throws SQLException
	{
		cstmt.registerOutParameter(s,i);
	}
	public void registerOutParameter(String s,int i1,int i2) throws SQLException
	{
		cstmt.registerOutParameter(s,i1,i2);
	}
	public void registerOutParameter(String s,int i1,String i2) throws SQLException
	{
		cstmt.registerOutParameter(s,i1,i2);
	}
	public void setAsciiStream(String s,java.io.InputStream input,int i) throws SQLException
	{
		cstmt.setAsciiStream(s,input,i);
	}
	public void setBigDecimal(String s,BigDecimal b) throws SQLException{
		cstmt.setBigDecimal(s,b);
	}
	public void setBinaryStream(String s,java.io.InputStream input,int i) throws SQLException
	{
		cstmt.setBinaryStream(s,input,i);
	}
	public void setBoolean(String s,boolean b) throws SQLException 
	{
		cstmt.setBoolean(s,b);
	}
	public void setByte(String s ,byte b) throws SQLException
	{
		cstmt.setByte(s,b);
	}
	public void setBytes(String s,byte b[]) throws SQLException 
	{
		cstmt.setBytes(s,b);
	}
	public void setCharacterStream(String s,java.io.Reader r,int i) throws SQLException
	{
		cstmt.setCharacterStream(s,r,i);
	}

	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		return cstmt.getCharacterStream(parameterIndex);
	}

	public Reader getCharacterStream(String parameterName) throws SQLException {
		return cstmt.getCharacterStream(parameterName);
	}

	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		return cstmt.getNCharacterStream(parameterIndex);
	}

	public Reader getNCharacterStream(String parameterName) throws SQLException {
		return cstmt.getNCharacterStream(parameterName);
	}

	public NClob getNClob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNClob(parameterIndex);
	}

	public NClob getNClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNClob(parameterName);
	}

	public String getNString(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNString(parameterIndex);
	}

	public String getNString(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNString(parameterName);
	}

	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getObject(parameterIndex, map);
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getObject(parameterName, map);
	}

	public RowId getRowId(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getRowId(parameterIndex);
	}

	public RowId getRowId(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getRowId(parameterName);
	}

	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getSQLXML(parameterIndex);
	}

	public SQLXML getSQLXML(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getSQLXML(parameterName);
	}

	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterName, x);
	}

	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterName, x,length);
	}

	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterName, x);
	}

	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterName, x, length);
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, x);
	}

	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, inputStream);
	}

	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, inputStream, length);
	}

	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterName, reader);
	}

	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterName, reader, length);
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, x);
	}

	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, reader);
	}

	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, reader, length);
	}

	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterName, value);
	}

	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterName, value, length);
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, value);
	}

	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, reader);
	}

	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, reader, length);
	}

	public void setNString(String parameterName, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNString(parameterName, value);
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setRowId(parameterName, x);
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setSQLXML(parameterName, xmlObject);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterIndex, x, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterIndex, inputStream);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterIndex, inputStream, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterIndex, reader);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterIndex, reader, length);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterIndex, reader, length);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, reader);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, reader, length);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNString(parameterIndex, value);
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setRowId(parameterIndex, x);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setSQLXML(parameterIndex, xmlObject);
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isClosed();
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isPoolable();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setPoolable(poolable);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.unwrap(iface);
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

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
