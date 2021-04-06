package com.xuanzhi.tools.dbpool;
public class ResettingPoolObserver implements PoolObserver
{
    /**
     * resets the properties of the connection to the original driver 
     * connection values (resets auto commit, catalog, read only, transcation 
     * isolation, and type map)
     */
    public void connectionReturned(PooledConnection conn) throws java.sql.SQLException{
			conn.setAutoCommit(conn.getDefaultAutoCommit());
            //conn.setCatalog(conn.getDefaultCatalog());
            //conn.setReadOnly(conn.getDefaultReadOnly());
            //conn.setTypeMap(conn.getDefaultTypeMap());
    }
}
