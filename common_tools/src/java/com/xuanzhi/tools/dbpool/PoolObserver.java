package com.xuanzhi.tools.dbpool;
public interface PoolObserver
{
    /**
     * called when a connection is returned to the pool
     */
    public void connectionReturned(PooledConnection conn) throws java.sql.SQLException;	
}
